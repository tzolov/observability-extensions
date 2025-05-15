/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.logaritex.spring.ai.observe.autoconfig;

import com.logaritex.spring.ai.observe.ChatModelCompletionObservationFilter;
import com.logaritex.spring.ai.observe.ChatModelCompletionObservationHandler2;
import com.logaritex.spring.ai.observe.ChatModelPromptContentObservationFilter;
import com.logaritex.spring.ai.observe.ChatModelPromptContentObservationHandler2;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Spring AI chat model observations.
 *
 * @author Thomas Vitale
 * @since 1.0.0
 */
@AutoConfiguration(
		afterName = { "org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration" })
@ConditionalOnClass(ChatModel.class)
public class ChatObservationAutoConfigurationExt {

	private static final Logger logger = LoggerFactory.getLogger(ChatObservationAutoConfigurationExt.class);

	public static final String CONFIG_PREFIX = "spring.ai.chat.observations";

	private static void logPromptContentWarning() {
		logger.warn(
				"You have enabled the inclusion of the prompt content in the observations, with the risk of exposing sensitive or private information. Please, be careful!");
	}

	private static void logCompletionWarning() {
		logger.warn(
				"You have enabled the inclusion of the completion content in the observations, with the risk of exposing sensitive or private information. Please, be careful!");
	}

	/**
	 * The chat content is typically too big to be included in an observation as span
	 * attributes. That's why the preferred way to store it is as span events, which are
	 * supported by OpenTelemetry but not yet surfaced through the Micrometer APIs. This
	 * primary/fallback configuration is a temporary solution until
	 * https://github.com/micrometer-metrics/micrometer/issues/5238 is delivered.
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(OtelTracer.class)
	@ConditionalOnBean(OtelTracer.class)
	static class PrimaryChatContentObservationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = CONFIG_PREFIX, name = "include-prompt", havingValue = "true")
		ChatModelPromptContentObservationHandler2 chatModelPromptContentObservationHandler() {
			logPromptContentWarning();
			return new ChatModelPromptContentObservationHandler2();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = CONFIG_PREFIX, name = "include-completion", havingValue = "true")
		ChatModelCompletionObservationHandler2 chatModelCompletionObservationHandler() {
			logCompletionWarning();
			return new ChatModelCompletionObservationHandler2();
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingClass("io.micrometer.tracing.otel.bridge.OtelTracer")
	static class FallbackChatContentObservationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = CONFIG_PREFIX, name = "include-prompt", havingValue = "true")
		ChatModelPromptContentObservationFilter chatModelPromptObservationFilter() {
			logPromptContentWarning();
			return new ChatModelPromptContentObservationFilter();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = CONFIG_PREFIX, name = "include-completion", havingValue = "true")
		ChatModelCompletionObservationFilter chatModelCompletionObservationFilter() {
			logCompletionWarning();
			return new ChatModelCompletionObservationFilter();
		}

	}

}
