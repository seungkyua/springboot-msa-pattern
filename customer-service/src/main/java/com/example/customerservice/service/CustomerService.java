package com.example.customerservice.service;

import com.example.common.Money;
import com.example.customerservice.domain.Customer;
import com.example.customerservice.exception.CustomerCreditLimitExceededException;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(String name, Money creditLimit) {
        Customer customer = new Customer(name, creditLimit);
        return customerRepository.save(customer);
    }

    public void reservedCredit(
            long customerId,
            long orderId,
            Money orderTotal) throws CustomerCreditLimitExceededException {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        customer.reserveCredit(orderId, orderTotal);
    }
}
