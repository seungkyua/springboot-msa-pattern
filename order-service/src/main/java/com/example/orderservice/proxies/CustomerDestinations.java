package com.example.orderservice.proxies;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "customer.destinations")
public class CustomerDestinations {

    @NotNull
    private String customerServiceUrl;

    public String getCustomerServiceUrl() {
        return customerServiceUrl;
    }

    public void setCustomerServiceUrl(String customerServiceUrl) {
        this.customerServiceUrl = customerServiceUrl;
    }
}
