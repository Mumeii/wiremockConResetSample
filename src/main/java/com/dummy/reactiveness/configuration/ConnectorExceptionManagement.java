package com.dummy.reactiveness.configuration;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.function.Function;


@Configuration
@RequiredArgsConstructor
public class ConnectorExceptionManagement {

	@Value( "${api.parse-client-body-on-error}" )
	private boolean parseErrorBoby;

	@Value( "${api.retry.maxAttempts}" )
	private int maxAttempts;

	@Value( "${api.retry.backoffInMilliSecond}" )
	private int backoffMilliSecond;


	@SuppressWarnings( "java:S1452" )
	public Function< ClientResponse, Mono< ? extends Throwable > > raiseWebClientException( String message ) {
		if( parseErrorBoby ) {
			return clientResponse -> clientResponse
				.bodyToMono( ErrorDto.class )
				.onErrorReturn( ErrorDto
					.builder().message( "No message given" )
					.build() )
				.flatMap( clientMessage -> Mono.error(
					new ResponseStatusException( clientResponse.statusCode(),
						clientMessage.getMessage() != null ?
							message + " ###### Message from client ###### " + clientMessage.getMessage() :
							message )
				) );
		} else {
			return clientResponse -> Mono.error( new ResponseStatusException( clientResponse.statusCode(), message ) );
		}
	}


	public RetryBackoffSpec retryRules() {
		return Retry
			.backoff( maxAttempts, Duration.ofMillis( backoffMilliSecond ) )
			.filter( t -> t instanceof ResponseStatusException responseStatusException && responseStatusException
				.getStatusCode()
				.is5xxServerError() );
	}


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ErrorDto {

		private String message;
	}
}
