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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.dal.Device;
import org.osgi.service.dal.DeviceException;
import org.osgi.service.dal.Function;
import org.osgi.service.dal.FunctionData;
import org.osgi.service.dal.PropertyMetadata;
import org.osgi.test.cases.dal.step.DeviceTestSteps;

/**
 * Test class validates the function.
 */
public class FunctionTest extends AbstractDeviceTest {

	/**
	 * Validates that the {@code Function} class is a part of the function
	 * registration.
	 */
	@Test
	public void testRegistrationClasses() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_FUNCTION,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_FUNCTION);
		Function[] functions = null;
		try {
			functions = super.getFunctions(Function.SERVICE_UID, null);
		} catch (InvalidSyntaxException e) {
			// not possible
			fail(null, e);
		}
		for (int i = 0; i < functions.length; i++) {
			String[] regClasses = (String[]) functions[i].getServiceProperty(Constants.OBJECTCLASS);
			assertThat(regClasses.length >= 1).as("At least one registration class is expected!").isTrue();
			String functionClassName = Function.class.getName();
			boolean functionClassInUse = false;
			for (int ii = 0; ii < regClasses.length; ii++) {
				if (functionClassName.equals(regClasses[ii])) {
					functionClassInUse = true;
					break;
				}
			}
			assertThat(functionClassInUse).as("Function class is not used for the function registration.").isTrue();
		}
	}

	/**
	 * Initially, the function services are registered first. The test method
	 * checks that rule.
	 * 
	 * @throws InvalidSyntaxException If the registered device UID breaks the
	 *         LDAP filter.
	 */
	@Test
	public void testRegistrationOrder() throws InvalidSyntaxException {
		super.deviceServiceListener.clear();
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_REGISTER_DEVICE_FUNCTION,
				DeviceTestSteps.STEP_MESSAGE_REGISTER_DEVICE_FUNCTION);
		ServiceEvent[] serviceEvents = super.deviceServiceListener.getEvents();
		assertThat(serviceEvents.length > 0).as("At least one device should be registered, but there are no events.").isTrue();
		for (int i = 0; i < serviceEvents.length; i++) {
			assertThat(serviceEvents[i].getType()).as("The event type must be registered.").isEqualTo(ServiceEvent.REGISTERED);
			String deviceUID = (String) serviceEvents[i].getServiceReference().getProperty(Device.SERVICE_UID);
			assertThat(deviceUID).as("The device unique identifier is missing.").isNotNull();
			Function[] functions = getFunctions(Function.SERVICE_DEVICE_UID, deviceUID);
			long deviceServiceID = ((Long) super.getDevice(deviceUID).getServiceProperty(Constants.SERVICE_ID)).longValue();
			for (int ii = 0; ii < functions.length; ii++) {
				long functionServiceID = ((Long) functions[ii].getServiceProperty(Constants.SERVICE_ID)).longValue();
				assertThat(functionServiceID < deviceServiceID).as("The function must be registered before the device!").isTrue();
			}
		}
	}

	/**
	 * The device service is unregistered first. The test method checks that
	 * rule.
	 * 
	 * @throws InvalidSyntaxException If the registered device UID can break the
	 *         LDAP filter.
	 * @throws DeviceException An error while removing the device.
	 */
	@Test
	public void testUnregistrationOrder() throws InvalidSyntaxException, DeviceException {
		super.deviceServiceListener.clear();
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_REGISTER_DEVICE_FUNCTION,
				DeviceTestSteps.STEP_MESSAGE_REGISTER_DEVICE_FUNCTION);
		ServiceEvent[] serviceEvents = super.deviceServiceListener.getEvents();
		assertThat(serviceEvents.length > 0).as("At least one device should be registered.").isTrue();
		for (int i = 0; i < serviceEvents.length; i++) {
			String deviceUID = (String) serviceEvents[i].getServiceReference().getProperty(Device.SERVICE_UID);
			assertThat(deviceUID).as("The device unique identifier is missing.").isNotNull();
			Device device = super.getDevice(deviceUID);
			Function[] functions = getFunctions(Function.SERVICE_DEVICE_UID, deviceUID);
			super.deviceServiceListener.clear();
			TestServiceListener testServiceListener = new TestServiceListener(
					context, TestServiceListener.DEVICE_FUNCTION_FILTER);
			try {
				device.remove();
				ServiceEvent[] deviceServiceEvents = testServiceListener.getEvents();
				assertThat(deviceServiceEvents.length > 1).as("At least two service events are expected on device remove.").isTrue();
				for (int ii = 0; ii < functions.length; ii++) {
					boolean isDeviceUnregistered = false;
					boolean isFunctionUnregistered = false;
					assertThat(deviceServiceEvents[0].getType()).as("The event type must be service modified.").isEqualTo(ServiceEvent.MODIFIED);
					for (int iii = 1; iii < deviceServiceEvents.length; iii++) {
						assertThat(deviceServiceEvents[iii].getType()).as("The event type must be unregistering.").isEqualTo(ServiceEvent.UNREGISTERING);
						if (deviceUID.equals(deviceServiceEvents[iii].getServiceReference().getProperty(Device.SERVICE_UID))) {
							assertThat(isDeviceUnregistered).as("The device is already unregistered!").isFalse();
							isDeviceUnregistered = true;
						} else
							if (functions[ii].getServiceProperty(Function.SERVICE_UID).equals(
									deviceServiceEvents[iii].getServiceReference().getProperty(Function.SERVICE_UID))) {
								assertThat(isDeviceUnregistered).as("The device must be unregistered first!").isTrue();
								assertThat(isFunctionUnregistered).as("The function is already unregistered!").isFalse();
								isFunctionUnregistered = true;
							}
					}
				}
			} finally {
				testServiceListener.unregister();
			}
		}
	}

	/**
	 * Checks that all functions support all required properties.
	 */
	@Test
	public void testRequiredFunctionProperties() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_FUNCTION,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_FUNCTION);
		ServiceReference< ? >[] functionSRefs = getFunctionSRefs();
		for (int i = 0; i < functionSRefs.length; i++) {
			super.checkRequiredProperties(
					functionSRefs[i],
					new String[] {Function.SERVICE_UID});
		}
	}

	/**
	 * Checks that {@link Function#getServiceProperty(String)} returns the same
	 * value as {@link ServiceReference#getProperty(String)}.
	 */
	@Test
	public void testFunctionProperties() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_FUNCTION,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_FUNCTION);
		ServiceReference< ? >[] functionSRefs = getFunctionSRefs();
		boolean compared = false;
		for (int i = 0; i < functionSRefs.length; i++) {
			Function function = (Function) context.getService(functionSRefs[i]);
			if (null == function) {
				continue;
			}
			String[] refKeys = functionSRefs[i].getPropertyKeys();
			for (int ii = 0; ii < refKeys.length; ii++) {
				assertThat(TestUtil.areEqual(functionSRefs[i].getProperty(refKeys[ii]), function.getServiceProperty(refKeys[ii]))).as("The function property and service property values are different.").isTrue();
			}
			compared = true;
		}
		assertThat(compared).as("No function properties have been compared.").isTrue();
	}

	/**
	 * Checks that function property value type is correct.
	 */
	@Test
	public void testFunctionPropertyTypes() {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_FUNCTIONS_ALL_PROPS,
				DeviceTestSteps.STEP_MESSAGE_FUNCTIONS_ALL_PROPS);
		checkFunctionPropertyType(Function.SERVICE_DESCRIPTION, new Class[] {String.class});
		checkFunctionPropertyType(Function.SERVICE_DEVICE_UID, new Class[] {String.class});
		checkFunctionPropertyType(Function.SERVICE_OPERATION_NAMES, new Class[] {String[].class});
		checkFunctionPropertyType(Function.SERVICE_PROPERTY_NAMES, new Class[] {String[].class});
		checkFunctionPropertyType(Function.SERVICE_REFERENCE_UIDS, new Class[] {String[].class});
		checkFunctionPropertyType(Function.SERVICE_TYPE, new Class[] {String.class});
		checkFunctionPropertyType(Function.SERVICE_UID, new Class[] {String.class});
		checkFunctionPropertyType(Function.SERVICE_VERSION, new Class[] {String.class});
	}

	/**
	 * Checks that property getter is available and accessible.
	 * 
	 * @throws NoSuchMethodException If the getter is missing.
	 * @throws ClassNotFoundException If the function class cannot be find.
	 */
	@Test
	public void testPropertyGetter() throws NoSuchMethodException, ClassNotFoundException {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_PROPERTY_READABLE,
				DeviceTestSteps.STEP_MESSAGE_PROPERTY_READABLE);
		Function[] functions = getFunctions(
				null, PropertyMetadata.ACCESS_READABLE);
		for (int i = 0; i < functions.length; i++) {
			String[] propertyNames = (String[]) functions[i].getServiceProperty(Function.SERVICE_PROPERTY_NAMES);
			for (int ii = 0; ii < propertyNames.length; ii++) {
				if (isPropertyAccessValid(functions[i], propertyNames[ii], PropertyMetadata.ACCESS_READABLE)) {
					checkPropertyGetter(functions[i], propertyNames[ii]);
				}
			}
		}
	}

	/**
	 * Checks that property setter is available and accessible.
	 * 
	 * @throws NoSuchMethodException If the getter is missing.
	 * @throws ClassNotFoundException If the function class cannot be find.
	 */
	@Test
	public void testPropertySetters() throws NoSuchMethodException, ClassNotFoundException {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_PROPERTY_WRITABLE,
				DeviceTestSteps.STEP_MESSAGE_PROPERTY_WRITABLE);
		Function[] functions = getFunctions(
				null, PropertyMetadata.ACCESS_WRITABLE);
		for (int i = 0; i < functions.length; i++) {
			String[] propertyNames = (String[]) functions[i].getServiceProperty(Function.SERVICE_PROPERTY_NAMES);
			for (int ii = 0; ii < propertyNames.length; ii++) {
				if (isPropertyAccessValid(functions[i], propertyNames[ii], PropertyMetadata.ACCESS_READABLE)) {
					checkPropertySetter(functions[i], propertyNames[ii]);
				}
			}
		}
	}

	/**
	 * Tests that there is no operation overloading.
	 * 
	 * @throws ClassNotFoundException If the function class cannot be loaded.
	 */
	@Test
	public void testOperations() throws ClassNotFoundException {
		super.testStepProxy.execute(
				DeviceTestSteps.STEP_ID_AVAILABLE_OPERATION,
				DeviceTestSteps.STEP_MESSAGE_AVAILABLE_OPERATION);
		Function[] functions = null;
		try {
			functions = super.getFunctions(Function.SERVICE_OPERATION_NAMES, null);
		} catch (InvalidSyntaxException e) {
			fail(null, e);
		}
		for (int i = 0; i < functions.length; i++) {
			String[] operationNames = (String[]) functions[i].getServiceProperty(Function.SERVICE_OPERATION_NAMES);
			for (int ii = 0; ii < operationNames.length; ii++) {
				Method[] methods = TestUtil.getFunctionMethods(functions[i], operationNames[ii], context);
				assertThat(methods).as("There is no method for operation: " + operationNames[ii]).isNotNull();
				assertThat(methods.length).as("There is operation overloading for: " + operationNames[ii]).isOne();
			}
		}
	}

	private void checkPropertySetter(Function function, String propertyName) throws ClassNotFoundException {
		String setterName = TestUtil.getBeanAccessor(propertyName, "set");
		Method[] setters = TestUtil.getFunctionMethods(function, setterName, context);
		assertThat(setters).as("There are no setters for property: " + propertyName).isNotNull();
		assertThat((1 == setters.length) || (2 == setters.length)).as("There must be one or two setters.").isTrue();
		boolean setterWithOneParam = false;
		boolean setterWithTwoParam = false;
		for (int i = 0; i < setters.length; i++) {
			assertThat(setters[i].getReturnType()).as("No return type is expected for the property setter: " + propertyName).isEqualTo(Void.TYPE);
			Class< ? >[] paramTypes = setters[i].getParameterTypes();
			switch (paramTypes.length) {
				case 1 :
					assertThat(setterWithOneParam).as("There must be only one setter with one parameters: " + setters[i]).isFalse();
					setterWithOneParam = true;
					break;
				case 2 :
					assertThat(setterWithTwoParam).as("There must be only one setter with two parameters: " + setters[i]).isFalse();
					setterWithTwoParam = true;
					break;
				default :
					fail("The setter must have one or two parameters.");
			}
		}
	}

	private void checkPropertyGetter(Function function, String propertyName) throws NoSuchMethodException, ClassNotFoundException {
		String getterName = TestUtil.getBeanAccessor(propertyName, "get");
		Method[] getters = TestUtil.getFunctionMethods(function, getterName, context);
		assertThat(getters.length).as("Only one getter is expected: " + getterName).isOne();
		Method getter = getters[0];
		assertThat(getter.getParameterTypes().length).as("The getter must not accept parameters.").isZero();
		Class< ? > returnType = getter.getReturnType();
		assertThat(returnType).as("The function getter must have return type: " + getterName).isNotNull();
		assertThat(FunctionData.class.isAssignableFrom(returnType)).as("The function getter must return a subclass of " + FunctionData.class.getName()).isTrue();
	}

	private ServiceReference< ? >[] getFunctionSRefs() {
		try {
			ServiceReference< ? >[] functionSRefs = context
					.getServiceReferences(
					(String)null, '(' + Function.SERVICE_UID + "=*)");
			assertThat(functionSRefs).as("There are no functions.").isNotNull();
			return functionSRefs;
		} catch (InvalidSyntaxException e) {
			// null is valid filter
		}
		return null;
	}

	private Function[] getFunctions(String functionClass, int propertyAccess) {
		try {
			ServiceReference< ? >[] functionSRefs = context
					.getServiceReferences(
					functionClass, '(' + Function.SERVICE_PROPERTY_NAMES + "=*)");
			assertThat(functionSRefs).as("There are no functions.").isNotNull();
			List<Function> result = new ArrayList<>(functionSRefs.length);
			for (int i = 0; i < functionSRefs.length; i++) {
				Function function = (Function) context.getService(functionSRefs[i]);
				if (null == function) {
					continue;
				}
				String[] propertyNames = (String[]) function.getServiceProperty(
						Function.SERVICE_PROPERTY_NAMES);
				for (int ii = 0; ii < propertyNames.length; ii++) {
					if (isPropertyAccessValid(function, propertyNames[ii], propertyAccess)) {
						result.add(function);
						break;
					}
				}
			}
			assertThat(result).as("There is no function, which contains a proeprty with an access: " + propertyAccess).isNotEmpty();
			return result.toArray(new Function[0]);
		} catch (InvalidSyntaxException e) {
			// the filter is valid
		}
		return null;
	}

	private static boolean isPropertyAccessValid(
			Function function, String propertyName, int propertyAccess) {
		PropertyMetadata propertyMetadata = function.getPropertyMetadata(propertyName);
		if (null == propertyMetadata) {
			return false;
		}
		Map<String, ? > metadata = propertyMetadata.getMetadata(null);
		if (null == metadata) {
			return false;
		}
		Integer accessType = (Integer) metadata.get(PropertyMetadata.ACCESS);
		return (null != accessType) && (propertyAccess == (accessType.intValue() & propertyAccess));
	}

	private void checkFunctionPropertyType(String propertyName,
			Class< ? >[] expectedTypes) {
		Function[] functions = null;
		try {
			functions = super.getFunctions(propertyName, null);
		} catch (InvalidSyntaxException e) {
			fail(null, e);
		}
		for (int i = 0; i < functions.length; i++) {
			Class< ? > propertyType = functions[i]
					.getServiceProperty(propertyName)
					.getClass();
			assertThat(TestUtil.contains(expectedTypes, propertyType)).as("The function proeprty type is not correct: " + propertyName + ", type: " + propertyType).isTrue();
		}
	}
}
