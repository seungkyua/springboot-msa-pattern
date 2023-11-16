package com.example.customerservice.web;

import com.example.common.web.GetCustomerResponse;
import com.example.customerservice.domain.Customer;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerController(
            CustomerService customerService,
            CustomerRepository customerRepository) {

        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @PostMapping(value = "/customers")
    public CreateCustomerResponse createCustomer(
            @RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(
                createCustomerRequest.getName(),
                createCustomerRequest.getCreditLimit());
        return new CreateCustomerResponse(customer.getId());
    }

    @GetMapping(value = "/customers/{customerId}")
    public ResponseEntity<GetCustomerResponse> getCustomer(
            @PathVariable Long customerId) {
        return customerRepository
                .findById(customerId)
                .map(c -> new ResponseEntity<>(
                            new GetCustomerResponse(c.getId(), c.getName(), c.getCreditLimit()),
                            HttpStatus.OK
                        )
                )
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
