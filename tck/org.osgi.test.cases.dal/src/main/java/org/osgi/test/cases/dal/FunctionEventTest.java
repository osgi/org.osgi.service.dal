/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/

package org.osgi.test.cases.dal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.jupiter.api.Test;
import org.osgi.service.dal.FunctionEvent;

/**
 * Validates the {@link FunctionEvent}.
 */
public final class FunctionEventTest {

	private static final String				PROP_NAME		= "test-prop";
	private static final String				FUNCTION_UID	= "test-function-uid";
	private static final TestFunctionData	PROP_VALUE		= new TestFunctionData(System.currentTimeMillis(), null);

	/**
	 * Test the function event properties.
	 */
	@Test
	public void testEventProperties() {
		Dictionary<String,Object> eventProps = new Hashtable<>();
		eventProps.put(FunctionEvent.PROPERTY_NAME, PROP_NAME);
		eventProps.put(FunctionEvent.PROPERTY_VALUE, PROP_VALUE);
		eventProps.put(FunctionEvent.FUNCTION_UID, FUNCTION_UID);
		checkEventProps(new FunctionEvent(
				FunctionEvent.TOPIC_PROPERTY_CHANGED, eventProps));

	}

	private void checkEventProps(FunctionEvent event) {
		assertThat(event.getFunctionUID()).as("The event function UID is not correct!").isEqualTo(FUNCTION_UID);
		assertThat(event.getFunctionPropertyName()).as("The event function property name is not correct!").isEqualTo(PROP_NAME);
		assertThat(event.getFunctionPropertyValue()).as("The event function property value is not correct!").isEqualTo(PROP_VALUE);
	}
}
