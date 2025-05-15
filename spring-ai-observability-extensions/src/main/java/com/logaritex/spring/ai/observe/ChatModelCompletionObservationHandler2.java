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

package com.logaritex.spring.ai.observe;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.tracing.handler.TracingObservationHandler;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;

import org.springframework.ai.chat.observation.ChatModelObservationContext;

/**
 * Handler for including the chat completion content in the observation as a span event.
 *
 * @author Thomas Vitale
 * @since 1.0.0
 */
public class ChatModelCompletionObservationHandler2 implements ObservationHandler<ChatModelObservationContext> {

	@Override
	public void onStop(ChatModelObservationContext context) {
		TracingObservationHandler.TracingContext tracingContext = context
			.get(TracingObservationHandler.TracingContext.class);
		Span otelSpan = TracingHelper.extractOtelSpan(tracingContext);

		if (otelSpan != null) {
			otelSpan.addEvent(AiObservationEventNames.CONTENT_COMPLETION.value(),
					Attributes.of(AttributeKey.stringArrayKey("gen_ai.completion"),
							ChatModelObservationContentProcessor.completion(context)));
		}
	}

	@Override
	public boolean supportsContext(Observation.Context context) {
		return context instanceof ChatModelObservationContext;
	}

}
