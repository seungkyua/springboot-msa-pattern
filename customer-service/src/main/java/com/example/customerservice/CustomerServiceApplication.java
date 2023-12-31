package com.example.customerservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example")
public class CustomerServiceApplication {

	private final Logger _logger = LoggerFactory.getLogger(getClass());

	@Bean
	public OncePerRequestFilter logFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(
					@NonNull HttpServletRequest request,
					@NonNull HttpServletResponse response,
					@NonNull FilterChain filterChain) throws ServletException, IOException {

				_logger.info("Path: {}", request.getRequestURI());
				filterChain.doFilter(request, response);
				_logger.info("Path: {} {}", request.getRequestURI(), response.getStatus());
			}
		};
	}

	@Bean
	public OncePerRequestFilter forwardHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(
					@NonNull HttpServletRequest request,
					@NonNull HttpServletResponse response,
					@NonNull FilterChain filterChain) throws ServletException, IOException {

				Map<String, String> xHeaders = new HashMap<>();
				Enumeration<String> headerNames = request.getHeaderNames();

				while (headerNames.hasMoreElements()) {
					String headerName = headerNames.nextElement();
					if (headerName.toLowerCase().startsWith("x-")) {
						String headerValue = request.getHeader(headerName);
						xHeaders.put(headerName, headerValue);
					}
				}

				xHeaders.forEach((key, value) -> {
					_logger.debug("Header {}: {}", key, value);
					response.setHeader(key, value);
				});

				filterChain.doFilter(request, response);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
