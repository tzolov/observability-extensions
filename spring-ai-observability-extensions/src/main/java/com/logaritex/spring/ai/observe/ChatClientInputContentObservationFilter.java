/*
 * Copyright 2023-2025 the original author or authors.
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

import java.util.List;
import java.util.Map;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

import org.springframework.ai.chat.client.observation.ChatClientObservationContext;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.util.CollectionUtils;

/**
 * An {@link ObservationFilter} to include the chat prompt content in the observation.
 *
 * @author Christian Tzolov
 */
public class ChatClientInputContentObservationFilter implements ObservationFilter {

	@Override
	public Observation.Context map(Observation.Context context) {
		if (!(context instanceof ChatClientObservationContext chatClientObservationContext)) {
			return context;
		}
		chatClientSystemText(chatClientObservationContext);
		chatClientSystemParams(chatClientObservationContext);
		chatClientUserText(chatClientObservationContext);
		chatClientUserParams(chatClientObservationContext);

		return chatClientObservationContext;
	}

	protected void chatClientSystemText(ChatClientObservationContext context) {
		List<Message> messages = context.getRequest().prompt().getInstructions();
		if (CollectionUtils.isEmpty(messages)) {
			return;
		}

		var systemMessage = messages.stream()
			.filter(message -> message instanceof SystemMessage)
			.reduce((first, second) -> second);
		if (systemMessage.isEmpty()) {
			return;
		}
		context.addHighCardinalityKeyValue(
				KeyValue.of("spring.ai.chat.client.system.text", systemMessage.get().getText()));
	}

	@SuppressWarnings("unchecked")
	protected void chatClientSystemParams(ChatClientObservationContext context) {
		if (!(context.getRequest()
			.context()
			.get("spring.ai.chat.client.system.params") instanceof Map<?, ?> systemParams)) {
			return;
		}
		if (CollectionUtils.isEmpty(systemParams)) {
			return;
		}

		context.addHighCardinalityKeyValue(KeyValue.of("spring.ai.chat.client.system.params",
				TracingHelper.concatenateMaps((Map<String, Object>) systemParams)));
	}

	protected void chatClientUserText(ChatClientObservationContext context) {
		List<Message> messages = context.getRequest().prompt().getInstructions();
		if (CollectionUtils.isEmpty(messages)) {
			return;
		}

		if (!(messages.get(messages.size() - 1) instanceof UserMessage userMessage)) {
			return;
		}
		context.addHighCardinalityKeyValue(KeyValue.of("spring.ai.chat.client.user.text", userMessage.getText()));
	}

	@SuppressWarnings("unchecked")
	protected void chatClientUserParams(ChatClientObservationContext context) {
		if (!(context.getRequest()
			.context()
			.get("spring.ai.chat.client.user.params") instanceof Map<?, ?> userParams)) {
			return;
		}
		if (CollectionUtils.isEmpty(userParams)) {
			return;
		}
		context.addHighCardinalityKeyValue(KeyValue.of("spring.ai.chat.client.user.params",
				TracingHelper.concatenateMaps((Map<String, Object>) userParams)));
	}

}
