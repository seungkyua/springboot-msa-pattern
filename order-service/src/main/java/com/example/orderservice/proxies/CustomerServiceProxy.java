package com.example.orderservice.proxies;

import com.example.common.web.GetCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class CustomerServiceProxy {

    private final WebClient client;
    private final String customerServiceUrl;

    private final Logger _logger = LoggerFactory.getLogger(getClass());

    public CustomerServiceProxy(WebClient client, String customerServiceUrl) {
        this.client = client;
        this.customerServiceUrl = customerServiceUrl;
    }

    public Mono<Optional<GetCustomerResponse>> findCustomerById(Long customerId) {
        return client.get()
                .uri(customerServiceUrl + "/customers/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(GetCustomerResponse.class).map(Optional::of);
                    } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.just(Optional.empty());
                    } else {
                        // Turn to error
                        return Mono.error(new UnknownProxyException("Unknown: " + response.statusCode()));
                    }
                });


//        Mono<ClientResponse> response = client
//                .get()
//                .uri(customerServiceUrl + "/customers/{customerId}", customerId)
//                .exchange();
//
//        return response.flatMap(resp -> {
//            switch (HttpStatus.valueOf(resp.statusCode().value())) {
//                case OK:
//                    return resp.bodyToMono(GetCustomerResponse.class).map(Optional::of);
//                case NOT_FOUND:
//                    Mono<Optional<GetCustomerResponse>> notFound = Mono.just(Optional.empty());
//                    return notFound;
//                default:
//                    return Mono.error(new UnknownProxyException("Unknown: " + resp.statusCode()));
//            }
//        });
    }
}
