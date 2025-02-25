/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.web.v1_0.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.response.GenericRestException;
import org.openmrs.module.webservices.rest.web.response.IllegalPropertyException;
import org.openmrs.module.webservices.validation.ValidationException;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class BaseRestControllerTest extends BaseModuleWebContextSensitiveTest {
	
	BaseRestController controller;
	
	MockHttpServletRequest request;
	
	MockHttpServletResponse response;
	
	Log spyOnLog;
	
	@Before
	public void before() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		controller = new BaseRestController();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		spyOnLog = spy(LogFactory.getLog(BaseRestController.class));
		// Need to get the logger using reflection
		Field log;
		log = controller.getClass().getDeclaredField("log");
		log.setAccessible(true);
		
		log.set(controller, spyOnLog);
		
	}
	
	/**
	 * @verifies return unauthorized if not logged in
	 * @see BaseRestController#apiAuthenticationExceptionHandler(Exception,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Test
	public void apiAuthenticationExceptionHandler_shouldReturnUnauthorizedIfNotLoggedIn() throws Exception {
		Context.logout();
		
		controller.apiAuthenticationExceptionHandler(new APIAuthenticationException(), request, response);
		
		assertThat(response.getStatus(), is(HttpServletResponse.SC_UNAUTHORIZED));
	}
	
	/**
	 * @verifies return forbidden if logged in
	 * @see BaseRestController#apiAuthenticationExceptionHandler(Exception,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Test
	public void apiAuthenticationExceptionHandler_shouldReturnForbiddenIfLoggedIn() throws Exception {
		controller.apiAuthenticationExceptionHandler(new APIAuthenticationException(), request, response);
		
		assertThat(response.getStatus(), is(HttpServletResponse.SC_FORBIDDEN));
	}
	
	@Test
	public void validationException_shouldReturnBadRequestResponse() throws Exception {
		Errors ex = new BindException(new Person(), "");
		ex.reject("error.message");
		
		SimpleObject responseSimpleObject = controller.validationExceptionHandler(new ValidationException(ex), request,
		    response);
		assertThat(response.getStatus(), is(HttpServletResponse.SC_BAD_REQUEST));
		
		SimpleObject errors = (SimpleObject) responseSimpleObject.get("error");
		Assert.assertEquals("webservices.rest.error.invalid.submission", errors.get("code"));
	}
	
	@Test
	public void handleException_shouldLogUnannotatedAsErrors() throws Exception {
		
		String message = "ErrorMessage";
		Exception ex = new Exception(message);
		controller.handleException(ex, request, response);
		
		verify(spyOnLog).error(message, ex);
		
	}
	
	@Test
	public void handleException_shouldLog500AndAboveAsErrors() throws Exception {
		
		String message = "ErrorMessage";
		Exception ex = new GenericRestException(message);
		
		controller.handleException(ex, request, response);
		
		verify(spyOnLog).error(message, ex);
		
	}
	
	@Test
	public void handleException_shouldLogBelow500AsInfo() throws Exception {
		
		String message = "ErrorMessage";
		Exception ex = new IllegalPropertyException(message);
		
		controller.handleException(ex, request, response);
		
		verify(spyOnLog).info(message, ex);
	}
}
