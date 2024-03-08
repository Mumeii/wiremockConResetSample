package com.dummy.reactiveness.services;


import com.dummy.reactiveness.connectors.RegularConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static java.util.stream.Collectors.joining;


@Service
@RequiredArgsConstructor
public class DefaultService {

	private final RegularConnector connector;


	public String multiSequentialCalls( final int storeId ) {

		return STR."""
			{
				"a": \{ connector.nonReactiveImplem( storeId ) }
				"b": \{ connector.nonReactiveImplem( storeId ) }
				"c": \{ connector.nonReactiveImplem( storeId ) }
				"d": \{ connector.nonReactiveImplem( storeId ) }
				"e": \{ connector.nonReactiveImplem( storeId ) }
				"f": \{ connector.nonReactiveImplem( storeId ) }
				"g": \{ connector.nonReactiveImplem( storeId ) }
				"h": \{ connector.nonReactiveImplem( storeId ) }
				"i": \{ connector.nonReactiveImplem( storeId ) }
				"j": \{ connector.nonReactiveImplem( storeId ) }
			}
			""";
	}


	public String multiAsyncCalls( final int storeId ) {
		return Flux
			.range( 1, 10 )
			.flatMap( _ -> connector.reactiveImplem( storeId ) )
			.index()
			.map( idxZipResult -> STR."\"res_\{idxZipResult.getT1()}\": \{idxZipResult.getT2()}" )
			.collect( joining(
				",\n\t",
				"{\n\t",
				"}"
			) )
			.block();
	}
}
