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
import static org.assertj.core.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.osgi.service.dal.FunctionData;

/**
 * Test class validates the function data.
 */
public class FunctionDataTest extends AbstractDeviceTest {

	/**
	 * Validates {@link FunctionData#equals(Object)} method.
	 */
	@Test
	public void testEqualsAndHashCode() {
		// test without metadata
		checkEqualsAndHashCode(null);

		// test with empty metadata
		checkEqualsAndHashCode(new HashMap<>());

		// test with comparable values
		Map<String,Object> metadata = new HashMap<>();
		metadata.put("test-string", "test");
		metadata.put("test-boolean", Boolean.TRUE);
		metadata.put("test-int", Integer.valueOf(Integer.MAX_VALUE));
		checkEqualsAndHashCode(metadata);

		// test with array
		int[] testArray = new int[] {0, 1, 2, 3, 4, 5};
		metadata.put("test-array", testArray);
		checkEqualsAndHashCode(metadata);

		// test with Map
		Map<String,Object> testMap = new HashMap<>();
		testMap.put("test-map-array", testArray);
		checkEqualsAndHashCode(metadata);

		metadata.clear();
		metadata.put("test-value", Boolean.TRUE);
		Map<String,Object> otherMetadata = new HashMap<>();
		otherMetadata.put("test-value", "true");
		assertThat((new TestFunctionData(Long.MIN_VALUE, metadata)).equals(
						new TestFunctionData(Long.MIN_VALUE, otherMetadata))).as("The function data instances are equal for different metadata.").isFalse();

		metadata.clear();
		otherMetadata.clear();
		metadata.put("test-value", new int[] {1, 2, 3});
		otherMetadata.put("test-value", new int[] {1, 2, 4});
		assertThat((new TestFunctionData(Long.MIN_VALUE, metadata)).equals(
						new TestFunctionData(Long.MIN_VALUE, otherMetadata))).as("The function data instances are equal for different metadata.").isFalse();

		metadata.clear();
		otherMetadata.clear();
		metadata.put("test-value", metadata);
		otherMetadata.put("test-value", otherMetadata);
		assertThat((new TestFunctionData(Long.MIN_VALUE, metadata)).equals(
						new TestFunctionData(Long.MIN_VALUE, otherMetadata))).as("The function data instances are equal for self-contained metadata.").isFalse();
	}

	/**
	 * Validates {@link FunctionData#compareTo(Object)} method.
	 */
	@Test
	public void testComparison() {
		Map<String,Object> metadata = new HashMap<>();
		metadata.put("test-value", Integer.valueOf(1));

		Map<String,Object> otherMetadata = new HashMap<>();
		otherMetadata.put("test-value", Integer.valueOf(2));
		checkComparison(metadata, otherMetadata);

		metadata.clear();
		otherMetadata.clear();
		metadata.put("test-value", new int[] {1, 2, 3});
		otherMetadata.put("test-value", new int[] {1, 2, 4});
		checkComparison(metadata, otherMetadata);

		metadata.clear();
		otherMetadata.clear();
		Map<String,Object> metadataValue = new HashMap<>();
		metadataValue.put("test-value", Integer.valueOf(1));
		metadata.put("test-value", metadataValue);
		Map<String,Object> otherMetadataValue = new HashMap<>();
		otherMetadataValue.put("test-value", Integer.valueOf(2));
		otherMetadata.put("test-value", otherMetadataValue);
	}

	/**
	 * Validates {@link FunctionData#FunctionData(long, java.util.Map)}.
	 */
	@Test
	public void testFields() {
		long currentTime = System.currentTimeMillis();
		// check with null metadata
		FunctionData data = new TestFunctionData(currentTime, null);
		assertThat(data.getTimestamp()).as("The function data timestamp is not correct.").isEqualTo(currentTime);
		assertThat(data.getMetadata()).as("There is function data metadata.").isNull();

		// check with metadata
		Map<String,Object> metadata = new HashMap<>();
		data = new TestFunctionData(currentTime, metadata);
		assertThat(data.getMetadata()).as("The function data metadata is not correct.").isSameAs(metadata);

		// check with map constructor
		Map<String,Object> fields = new HashMap<>();
		fields.put(FunctionData.FIELD_TIMESTAMP, Long.valueOf(currentTime));
		fields.put(FunctionData.FIELD_METADATA, metadata);
		data = new TestFunctionData(fields);
		assertThat(data.getTimestamp()).as("The function data timestamp is not correct.").isEqualTo(currentTime);
		assertThat(data.getMetadata()).as("The function data metadata is not correct.").isSameAs(metadata);
	}

	/**
	 * Validates {@link FunctionData#FunctionData(long, java.util.Map)} with
	 * invalid fields.
	 */
	@Test
	public void testInvalidFields() {
		Map<String,Object> fields = new HashMap<>();
		fields.put(FunctionData.FIELD_TIMESTAMP, "1");
		checkInvalidFieldType(fields);

		fields.clear();
		fields.put(FunctionData.FIELD_TIMESTAMP, Long.valueOf(System.currentTimeMillis()));
		fields.put(FunctionData.FIELD_METADATA, "test");
		checkInvalidFieldType(fields);
	}

	private void checkInvalidFieldType(Map<String,Object> fields) {
		try {
			new TestFunctionData(fields);
			fail("The function data is built with invalid fields: " + fields);
		} catch (ClassCastException cce) {
			// go ahead, it's expected
		}
	}

	private void checkComparison(Map<String,Object> lessMetadata,
			Map<String,Object> greaterMetadata) {
		long currentTime = System.currentTimeMillis();
		long otherTime = currentTime - 1;

		FunctionData data = new TestFunctionData(currentTime, lessMetadata);
		assertThat(data.compareTo(new TestFunctionData(otherTime, greaterMetadata)) > 0).as("Function data timestamp is not compared with higher priority.").isTrue();

		assertThat(data.compareTo(new TestFunctionData(currentTime, greaterMetadata)) < 0).as("Function data metadata comparison is not correct.").isTrue();

		assertThat(data.compareTo(new TestFunctionData(currentTime, lessMetadata))).as("Function data comparison is not correct.").isZero();
	}

	private void checkEqualsAndHashCode(Map<String,Object> metadata) {
		TestFunctionData testData = new TestFunctionData(Long.MIN_VALUE, metadata);
		assertThat(testData.equals(null)).as("The function data is equal to null.").isFalse(); // NOPMD
		assertThat(testData.equals(new Object())).as("The function data is equal to java.lang.Object instance.").isFalse();
		assertThat(testData).as("The function data is not equal to itself.").isEqualTo(testData);
		TestFunctionData otherTestData = new TestFunctionData(Long.MIN_VALUE, metadata);
		assertThat(otherTestData).as("The function data is not equal to a copy.").isEqualTo(testData);
		assertThat(otherTestData.hashCode()).as("Equal function data instances don't have equal hash code.").isEqualTo(testData.hashCode());
	}
}
