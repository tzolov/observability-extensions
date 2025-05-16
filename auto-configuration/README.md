# Spring AI Chat Observation Auto Configuration Extensions

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.logaritex.spring.ai/spring-ai-autoconfigure-model-chat-observation.svg)](https://search.maven.org/search?q=g:com.logaritex.spring.ai%20AND%20a:spring-ai-autoconfigure-model-chat-observation)

## Overview

This module provides Spring Boot auto-configuration for the Spring AI Observability Extensions. It automatically registers the necessary observation filters and handlers based on your application configuration, making it easy to integrate enhanced observability features into your Spring AI applications.

## Features

- Automatic registration of observation filters and handlers
- Conditional configuration based on application properties
- Support for both OpenTelemetry-based and fallback configurations
- Seamless integration with Spring Boot's auto-configuration mechanism

## Installation

### Maven

```xml
<dependency>
    <groupId>com.logaritex.spring.ai</groupId>
    <artifactId>spring-ai-autoconfigure-model-chat-observation</artifactId>
    <version>${observability-extensions.version}</version>
</dependency>
```

## How It Works

The auto-configuration module uses Spring Boot's auto-configuration mechanism to automatically register the appropriate observation components based on your application's environment and configuration.

### Configuration Classes

- **ObservationAutoConfigurationExtensions**: The main auto-configuration class that registers observation filters and handlers
- **PrimaryChatContentObservationConfiguration**: Configures OpenTelemetry-based observation handlers when OpenTelemetry is available
- **FallbackChatContentObservationConfiguration**: Configures fallback observation filters when OpenTelemetry is not available

### Configuration Properties

The auto-configuration is controlled by the following properties:

#### Chat Model Observations

```properties
# Enable prompt content logging for chat models
spring.ai.chat.observations.include-prompt=true

# Enable completion content logging for chat models
spring.ai.chat.observations.include-completion=true
```

#### Chat Client Observations

```properties
# Enable prompt content logging for chat clients
spring.ai.chat.client.observations.include-prompt=true

# Legacy property (deprecated)
spring.ai.chat.client.observations.include-input=true
```

## Primary vs. Fallback Configuration

The auto-configuration module provides two approaches for capturing observation data:

1. **Primary Configuration (with OpenTelemetry)**: When OpenTelemetry is available, the auto-configuration registers `ChatModelPromptContentObservationHandler2` and `ChatModelCompletionObservationHandler2` to capture content as span events.

2. **Fallback Configuration**: When OpenTelemetry is not available, the auto-configuration registers `ChatModelPromptContentObservationFilter` and `ChatModelCompletionObservationFilter` to include content as high-cardinality attributes.

## Usage

The auto-configuration is automatically activated when the module is included in your Spring Boot application. No additional configuration is required beyond setting the appropriate properties.

### Example

```java
@SpringBootApplication
public class MyAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyAiApplication.class, args);
    }
}
```

With the following in your `application.properties`:

```properties
spring.ai.chat.observations.include-prompt=true
spring.ai.chat.observations.include-completion=true
spring.ai.chat.client.observations.include-prompt=true
```

## Security Considerations

When enabling prompt and completion content logging, be aware that sensitive information may be exposed in your observability data. The auto-configuration includes warning logs to remind you of this risk when these features are enabled.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](../LICENSE) file for details.

## Links

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring Boot Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)
- [Project GitHub Repository](https://github.com/spring-ai-community/observability-extensions)
