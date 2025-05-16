# Spring AI Observability Extensions

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.logaritex.spring.ai/observability-extensions-parent.svg)](https://search.maven.org/search?q=g:com.logaritex.spring.ai%20AND%20a:observability-extensions-parent)

## Overview

Spring AI Observability Extensions is a project that enhances the observability capabilities of Spring AI applications. It provides additional filters and handlers for monitoring and tracing AI operations, with a focus on chat client and chat model interactions.

The project is built on top of Spring AI's observability framework and follows **some** of the OpenTelemetry Semantic Conventions for AI Systems.

## Modules

The project consists of the following modules:

### [Observability Extensions](observability-extensions/README.md)

The core module that provides the observation filters, handlers, and utilities for enhancing Spring AI observability.

### [Auto Configuration](auto-configuration/README.md)

A Spring Boot auto-configuration module that automatically registers the necessary observation components based on your application configuration.

## Features

- Enhanced prompt content observation for chat clients and models
- Enhanced completion content observation for chat models
- Support for both high-cardinality attributes and span events
- OpenTelemetry integration for distributed tracing
- Auto-configuration for seamless integration with Spring Boot applications

## Installation

### Maven

Add the following dependencies to your project:

```xml
<!-- Core observability extensions -->
<dependency>
    <groupId>com.logaritex.spring.ai</groupId>
    <artifactId>observability-extensions</artifactId>
    <version>${observability-extensions.version}</version>
</dependency>

<!-- Auto-configuration for Spring Boot -->
<dependency>
    <groupId>com.logaritex.spring.ai</groupId>
    <artifactId>autoconfigure-observability-extensions</artifactId>
    <version>${observability-extensions.version}</version>
</dependency>
```

## Usage

The extensions are automatically configured when used with Spring Boot, thanks to the auto-configuration module. The observation filters and handlers are conditionally registered based on your configuration properties.

### Prerequisites

- Spring Boot 3.x
- Spring AI 1.0.0-RC1 or higher
- Micrometer and Micrometer Tracing

## Configuration

### Chat Model Observations

Enable prompt and completion content logging in your `application.properties` or `application.yml`:

```properties
# Enable prompt content logging for chat models
spring.ai.chat.observations.include-prompt=true

# Enable completion content logging for chat models
spring.ai.chat.observations.include-completion=true
```

### Chat Client Observations

Enable prompt content logging for chat clients:

```properties
# Enable prompt content logging for chat clients
spring.ai.chat.client.observations.include-prompt=true
```

> **Warning**: Enabling prompt and completion content logging may expose sensitive information. Use with caution in production environments.

## How It Works

### Primary vs. Fallback Configuration

The extensions provide two approaches for capturing observation data:

1. **Primary Configuration (with OpenTelemetry)**: When OpenTelemetry is available, the extensions use span events to capture prompt and completion content, which is the preferred approach for large content.

2. **Fallback Configuration**: When OpenTelemetry is not available, the extensions use observation filters to include content as high-cardinality attributes.

## Integration with Spring AI Observability

These extensions complement the built-in observability features of Spring AI:

- **Low Cardinality Keys**: Metrics and traces include standard keys like `gen_ai.operation.name`, `gen_ai.system`, etc.
- **High Cardinality Keys**: Traces can include detailed content like prompts and completions when enabled.

## Building from Source

The project uses Maven for building. To build the project, run:

```bash
./mvnw clean install
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Links

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenTelemetry Semantic Conventions for AI](https://github.com/open-telemetry/semantic-conventions/tree/main/docs/gen-ai)
- [Project GitHub Repository](https://github.com/spring-ai-community/observability-extensions)
