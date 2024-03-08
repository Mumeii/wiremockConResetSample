package com.dummy.reactiveness.connectors;


import com.dummy.reactiveness.configuration.ConnectorExceptionManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class RegularConnector {

	public static final String STORE_INFORMATION_URL = "/store/{storeId}/information";

	private final ConnectorExceptionManagement connectorExceptionManagement;

	public String nonReactiveImplem( final int storeId ) {
		return reactiveImplem( storeId )
			.block();
	}


	public Mono< String > reactiveImplem( final int storeId ) {
		return WebClient.builder()
			.baseUrl("http://localhost:8081")
			.build()
			.post()
			.uri( uriBuilder -> uriBuilder
				.path( STORE_INFORMATION_URL )
				.build( of( "storeId", storeId ) )
			)
			.contentType( MediaType.APPLICATION_JSON )
			.bodyValue( """
				{
				    "whatever": "dummy"
				}
				""" )
			.retrieve()
			.onStatus(
				httpStatus -> httpStatus.equals( NOT_FOUND ),
				_ -> Mono.empty()
			)
			.onStatus(
				HttpStatusCode::isError,
				connectorExceptionManagement.raiseWebClientException( "An HTTP error occurred" )
			)
			.bodyToMono( String.class )
			.retryWhen( connectorExceptionManagement.retryRules() )
			.doOnError( throwable -> log.error( "An HTTP error occurred", throwable ) );
	}

}
