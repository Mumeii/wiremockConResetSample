package com.dummy.reactiveness.controllers;


import com.dummy.reactiveness.services.DefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class DefaultController {

	private final DefaultService service;

	@GetMapping(
		value = "nonReactive/{storeId}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public String nonReactiveImplementation(
		@PathVariable( "storeId" )
		final int storeId
	) {
		return service.multiSequentialCalls( storeId );
	}


	@GetMapping(
		value = "reactiveWebClient/{storeId}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public String reactiveWebClientImplementation(
		@PathVariable( "storeId" )
		final int storeId
	) {
		return service.multiAsyncCalls( storeId );
	}
}
