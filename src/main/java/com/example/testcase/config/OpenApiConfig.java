package com.example.testcase.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Test case",
                description = "pre-employment test", version = "1.0.0",
                contact = @Contact(
                        name = "LeMauser",
                        url = "https://github.com/LemonadeMauser/testCasee"
                )
        )
)
public class OpenApiConfig {
}
