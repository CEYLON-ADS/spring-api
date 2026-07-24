package com.ceylon_adds.system_api.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResourceNameGenerator {

    public String generateResourceName(String resourceName) {
        return new StringBuilder()
                .append(UUID.randomUUID())
                .append("-")
                .append("ceylon-ad")
                .append("-")
                .append(UUID.randomUUID())
                .append("-")
                .append(resourceName)
                .toString();
    }
}
