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

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.osgi.service.dal.DeviceException;

/**
 * Validates {@link DeviceException} class.
 */
public final class DeviceExceptionTest {

	/**
	 * Tests the exception default constructor.
	 */
	@Test
	public void testEmptyException() {
		DeviceException de = new DeviceException();
		assertThat(de.getMessage()).as("No exception message is expected.").isNull();
		assertThat(de.getCode()).as("Unknown exception code is expected.").isEqualTo(DeviceException.UNKNOWN);
		assertThat(de.getCause()).as("No exception cause is expected.").isNull();
	}

	/**
	 * Tests the exception message.
	 */
	@Test
	public void testExceptionMessage() {
		checkExceptionMessage("test exception message");
		checkExceptionMessage(null);
	}

	/**
	 * Tests the exception cause.
	 */
	@Test
	public void testExceptionCause() {
		checkExceptionCause(new IOException());
		checkExceptionCause(null);
	}

	/**
	 * Tests the exception codes.
	 */
	@Test
	public void testExceptionCodes() {
		int[] exceptionCodes = new int[] {
				DeviceException.COMMUNICATION_ERROR,
				DeviceException.NO_DATA,
				DeviceException.NOT_INITIALIZED,
				DeviceException.TIMEOUT,
				DeviceException.UNKNOWN,
				Integer.MIN_VALUE,
		};
		for (int i = 0; i < exceptionCodes.length; i++) {
			DeviceException de = new DeviceException(null, null, exceptionCodes[i]);
			assertThat(de.getCode()).as("The exception code is not correct.").isEqualTo(exceptionCodes[i]);
		}
	}

	private void checkExceptionMessage(String message) {
		DeviceException de = new DeviceException(message);
		assertThat(de.getMessage()).as("The exception message is not correct.").isEqualTo(message);

		de = new DeviceException(message, null);
		assertThat(de.getMessage()).as("The exception message is not correct.").isEqualTo(message);

		de = new DeviceException(message, null, DeviceException.UNKNOWN);
		assertThat(de.getMessage()).as("The exception message is not correct.").isEqualTo(message);
	}

	private void checkExceptionCause(Throwable cause) {
		DeviceException de = new DeviceException(null, cause);
		assertThat(de.getCause()).as("The exception cause is not correct.").isSameAs(cause);

		de = new DeviceException(null, cause, DeviceException.UNKNOWN);
		assertThat(de.getCause()).as("The exception cause is not correct.").isEqualTo(cause);
	}
}
