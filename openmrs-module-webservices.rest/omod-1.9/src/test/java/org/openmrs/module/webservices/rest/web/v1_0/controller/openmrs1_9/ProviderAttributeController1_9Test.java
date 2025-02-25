/**
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.web.v1_0.controller.openmrs1_9;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.ProviderAttribute;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestTestConstants1_9;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest;

/**
 * Tests functionality of {@link ProviderAttributeController}.
 */
public class ProviderAttributeController1_9Test extends MainResourceControllerTest {
	
	private ProviderService service;
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest#getURI()
	 */
	@Override
	public String getURI() {
		return "provider/" + RestTestConstants1_9.PROVIDER_UUID + "/attribute";
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest#getUuid()
	 */
	@Override
	public String getUuid() {
		return RestTestConstants1_9.PROVIDER_ATTRIBUTE_UUID;
	}
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest#getAllCount()
	 */
	@Override
	public long getAllCount() {
		return service.getProviderByUuid(RestTestConstants1_9.PROVIDER_UUID).getActiveAttributes().size();
	}
	
	@Before
	public void before() throws Exception {
		executeDataSet(RestTestConstants1_9.TEST_DATASET);
		this.service = Context.getProviderService();
	}
	
	@Test
	public void shouldAddAttributeToProvider() throws Exception {
		int before = service.getProviderByUuid(RestTestConstants1_9.PROVIDER_UUID).getAttributes().size();
		String json = "{\"attributeType\":\"" + RestTestConstants1_9.PROVIDER_ATTRIBUTE_TYPE_UUID
		        + "\", \"value\":\"2012-05-05\"}";
		
		handle(newPostRequest(getURI(), json));
		int after = service.getProviderByUuid(RestTestConstants1_9.PROVIDER_UUID).getAttributes().size();
		Assert.assertEquals(before + 1, after);
	}
	
	@Test
	public void shouldEditProviderAttribute() throws Exception {
		String json = "{ \"attributeType\":\"9516cc50-n8ik-bc4f-8dw4-001e378eb67e\" }";
		
		ProviderAttribute providerAttribute = service.getProviderAttributeByUuid(getUuid());
		Assert.assertEquals("Joining Date", providerAttribute.getAttributeType().getName());
		
		handle(newPostRequest(getURI() + "/" + getUuid(), json));
		
		providerAttribute = service.getProviderAttributeByUuid(getUuid());
		Assert.assertEquals("Leave Date", providerAttribute.getAttributeType().getName());
	}
	
	@Test
	public void shouldVoidAttribute() throws Exception {
		ProviderAttribute providerAttribute = service.getProviderAttributeByUuid(getUuid());
		Assert.assertFalse(providerAttribute.isVoided());
		
		handle(newDeleteRequest(getURI() + "/" + getUuid(), new Parameter("reason", "unit test")));
		
		providerAttribute = service.getProviderAttributeByUuid(getUuid());
		Assert.assertTrue(providerAttribute.isVoided());
		Assert.assertEquals("unit test", providerAttribute.getVoidReason());
	}
	
}
