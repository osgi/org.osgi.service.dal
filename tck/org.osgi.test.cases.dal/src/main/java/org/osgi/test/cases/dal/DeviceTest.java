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

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.dal.Device;
import org.osgi.service.dal.DeviceException;
import org.osgi.test.cases.dal.step.DeviceTestSteps;
import org.osgi.tck.step.TestStep;

/**
 * Test class validates the device operations and properties.
 */
public class DeviceTest extends AbstractDeviceTest {

	/**
	 * Tests device remove operation. The test method requires at least one
	 * device with remove support.
	 * 
	 * @throws DeviceException An error while removing the device.
	 */
	@Test
	public void testRemoveDevice() throws DeviceException {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_DEVICE,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_DEVICE);
		Collection<ServiceReference<Device>> deviceSRefs = getDeviceSRefs(null);
		boolean isRemoved = false;
		for (ServiceReference<Device> deviceSRef : deviceSRefs) {
			Device device = context.getService(deviceSRef);
			if (null == device) {
				continue;
			}
			try {
				device.remove();
				assertThat(context.getService(deviceSRef)).as("The device service is not unregistered.").isNull();
				isRemoved = true;
				break;
			} catch (UnsupportedOperationException uoe) {
				// expected
			}
		}
		assertThat(isRemoved).as("No device with remove support.").isTrue();
	}

	/**
	 * Tests the device registration through {@link TestStep} service.
	 * 
	 * @throws InvalidSyntaxException The registered device UID can break LDAP
	 *         filter.
	 * @throws DeviceException An error while removing the device.
	 */
	@Test
	public void testAddDevice() throws InvalidSyntaxException, DeviceException {
		super.deviceServiceListener.clear();
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_REGISTER_DEVICE,
				DeviceTestSteps.STEP_MESSAGE_REGISTER_DEVICE);
		ServiceEvent[] serviceEvents = super.deviceServiceListener.getEvents();
		assertThat(serviceEvents.length > 0).as("At least one device should be registered, but there are no events.").isTrue();
		for (int i = 0; i < serviceEvents.length; i++) {
			assertThat(serviceEvents[i].getType()).as("The event type must be registered.").isEqualTo(ServiceEvent.REGISTERED);
			String deviceUID = (String) serviceEvents[i].getServiceReference().getProperty(Device.SERVICE_UID);
			assertThat(deviceUID).as("The device unique identifier is missing.").isNotNull();
		}
	}

	/**
	 * Tests the device properties with device service properties.
	 */
	@Test
	public void testDeviceProperties() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_DEVICE,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_DEVICE);
		Collection<ServiceReference<Device>> deviceSRefs = getDeviceSRefs(null);
		boolean compared = false;
		for (ServiceReference<Device> deviceSRef : deviceSRefs) {
			Device device = context.getService(deviceSRef);
			if (null == device) {
				continue;
			}
			String[] refKeys = deviceSRef.getPropertyKeys();
			for (int ii = 0; ii < refKeys.length; ii++) {
				assertThat(TestUtil.areEqual(deviceSRef.getProperty(refKeys[ii]),
								device.getServiceProperty(refKeys[ii]))).as("The device property and service property values are different.").isTrue();
			}
			compared = true;
		}
		assertThat(compared).as("No device properties have been compared.").isTrue();
	}

	/**
	 * Tests the types of the device properties.
	 */
	@Test
	public void testDevicePropertyTypes() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_DEVICES_ALL_PROPS,
				DeviceTestSteps.STEP_MESSAGE_DEVICES_ALL_PROPS);
		checkDevicePropertyType(Device.SERVICE_DESCRIPTION, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_DRIVER, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_FIRMWARE_VENDOR, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_FIRMWARE_VERSION, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_HARDWARE_VENDOR, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_HARDWARE_VERSION, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_MODEL, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_NAME, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_REFERENCE_UIDS, new Class[] {String[].class});
		checkDevicePropertyType(Device.SERVICE_SERIAL_NUMBER, new Class[] {String.class});
		checkDevicePropertyType(Device.SERVICE_STATUS, new Class[] {Integer.class});
		checkDevicePropertyType(Device.SERVICE_STATUS_DETAIL, new Class[] {Integer.class});
		checkDevicePropertyType(Device.SERVICE_TYPES, new Class[] {String[].class});
		checkDevicePropertyType(Device.SERVICE_UID, new Class[] {String.class});
	}

	/**
	 * Tests the availability of the required device properties.
	 */
	@Test
	public void testRequiredDeviceProperties() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_DEVICE,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_DEVICE);
		Collection<ServiceReference<Device>> deviceSRefs = getDeviceSRefs(null);
		for (ServiceReference<Device> deviceSRef : deviceSRefs) {
			super.checkRequiredProperties(
					deviceSRef,
					new String[] {
							Device.SERVICE_UID,
							Device.SERVICE_DRIVER,
							Device.SERVICE_STATUS});
		}
	}

	private void checkDevicePropertyType(String propertyName,
			Class< ? >[] expectedTypes) {
		Device[] devices = null;
		try {
			devices = getDevices(propertyName);
		} catch (InvalidSyntaxException e) {
			fail(null, e); // not possible
		}
		for (int i = 0; i < devices.length; i++) {
			Class< ? > propertyType = devices[i]
					.getServiceProperty(propertyName)
					.getClass();
			assertThat(TestUtil.contains(expectedTypes, propertyType)).as("The device proeprty type is not correct: " + propertyName + ", type: " + propertyType).isTrue();
		}
	}

	private Collection<ServiceReference<Device>> getDeviceSRefs(String filter) {
		try {
			Collection<ServiceReference<Device>> deviceSRefs = context
					.getServiceReferences(Device.class, filter);
			assertThat(deviceSRefs).as("No device services; filter=%s.", filter)
					.isNotEmpty();
			return deviceSRefs;
		} catch (InvalidSyntaxException e) {
			// null is a valid filter
			return null;
		}
	}

	private Device[] getDevices(String devicePropName) throws InvalidSyntaxException {
		BundleContext bc = context;
		Collection<ServiceReference<Device>> deviceSRefs = getDeviceSRefs(
				"(" + devicePropName + "=*)");

		Device[] devices = deviceSRefs.stream().map(ref -> {
			Device device = bc.getService(ref);
			assertThat(device).as("The device service is missing with UID: "
							+ ref.getProperty(Device.SERVICE_UID)).isNotNull();
			return device;
		}).toArray(Device[]::new);
		return devices;
	}
}
