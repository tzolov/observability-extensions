# Spring AI Observability Extensions

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.logaritex.spring.ai/observability-extensions.svg)](https://search.maven.org/search?q=g:com.logaritex.spring.ai%20AND%20a:observability-extensions)

## Overview

Spring AI Observability Extensions enhances the observability capabilities of Spring AI applications by providing additional filters and handlers for monitoring and tracing AI operations. This project extends the core Spring AI observability features with more detailed insights into chat client and chat model interactions.

The extensions are built on top of Spring AI's observability framework and follow the OpenTelemetry Semantic Conventions for AI Systems.

## Features

- Enhanced prompt content observation for chat clients
- Enhanced completion content observation for chat models
- Support for both high-cardinality attributes and span events
- OpenTelemetry integration for distributed tracing
- Auto-configuration for seamless integration with Spring Boot applications

## Components

### Observation Filters

- **ChatClientPromptContentObservationFilter**: Captures and includes chat client prompt content in observations
- **ChatModelPromptContentObservationFilter**: Captures and includes chat model prompt content in observations
- **ChatModelCompletionObservationFilter**: Captures and includes chat model completion content in observations
- **ChatClientInputContentObservationFilter**: (Deprecated) Legacy filter for chat client input content

### Observation Handlers

- **ChatModelPromptContentObservationHandler2**: Adds chat model prompt content as span events in traces
- **ChatModelCompletionObservationHandler2**: Adds chat model completion content as span events in traces

### Utilities

- **ChatModelObservationContentProcessor**: Processes prompt and completion content for observations
- **TracingHelper**: Provides utilities for working with traces and formatting content for observations
- **AiObservationEventNames**: Defines standard event names for AI observations based on OpenTelemetry conventions

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
    <artifactId>spring-ai-autoconfigure-model-chat-observation</artifactId>
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

### Integration with Spring AI Observability

These extensions complement the built-in observability features of Spring AI:

- **Low Cardinality Keys**: Metrics and traces include standard keys like `gen_ai.operation.name`, `gen_ai.system`, etc.
- **High Cardinality Keys**: Traces can include detailed content like prompts and completions when enabled.

## Examples

### Observing Chat Model Interactions

When properly configured, you can observe the full lifecycle of chat model interactions:

1. Prompt content is captured when the model is called
2. Completion content is captured when the model responds
3. Both are included in traces for detailed analysis

### Tracing with OpenTelemetry

With OpenTelemetry configured, you can see detailed span events for AI operations:

- `gen_ai.content.prompt` events show the exact prompts sent to models
- `gen_ai.content.completion` events show the responses received from models

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](../LICENSE) file for details.

## Links

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenTelemetry Semantic Conventions for AI](https://github.com/open-telemetry/semantic-conventions/tree/main/docs/gen-ai)
- [Project GitHub Repository](https://github.com/spring-ai-community/observability-extensions)
