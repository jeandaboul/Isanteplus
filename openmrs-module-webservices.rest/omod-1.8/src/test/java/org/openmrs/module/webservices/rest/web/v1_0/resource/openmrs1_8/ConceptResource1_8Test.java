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
package org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RestTestConstants1_8;
import org.openmrs.module.webservices.rest.web.representation.NamedRepresentation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

public class ConceptResource1_8Test extends BaseDelegatingResourceTest<ConceptResource1_8, Concept> {

	@Override
	public Concept newObject() {
		return Context.getConceptService().getConceptByUuid(getUuidProperty());
	}
	
	@Override
	public void validateDefaultRepresentation() throws Exception {
		super.validateDefaultRepresentation();
		assertPropPresent("name");
		assertPropPresent("datatype");
		assertPropPresent("conceptClass");
		assertPropPresent("set");
		assertPropEquals("version", getObject().getVersion());
		assertPropEquals("retired", getObject().isRetired());
		assertPropPresent("names");
		assertPropPresent("descriptions");
		assertPropEquals("display", getDisplayProperty());
		assertPropPresent("answers");
		assertPropPresent("setMembers");
	}
	
	@Override
	public void validateFullRepresentation() throws Exception {
		super.validateFullRepresentation();
		assertPropPresent("name");
		assertPropPresent("datatype");
		assertPropPresent("conceptClass");
		assertPropPresent("set");
		assertPropEquals("version", getObject().getVersion());
		assertPropEquals("retired", getObject().isRetired());
		assertPropPresent("names");
		assertPropPresent("descriptions");
		assertPropPresent("auditInfo");
		assertPropEquals("display", getDisplayProperty());
		assertPropPresent("answers");
		assertPropPresent("setMembers");
	}
	
	@Override
	public String getDisplayProperty() {
		return "YES";
	}
	
	@Override
	public String getUuidProperty() {
		return RestTestConstants1_8.CONCEPT_UUID;
	}

    @Test
    public void testGetNamedRepresentation() throws Exception {
        Concept object = getObject();
        object.addSetMember(object);
        try {
            SimpleObject rep = getResource().asRepresentation(object, new NamedRepresentation("fullchildreninternal"));
        }catch (ConversionException e){
            Assert.assertFalse(e.getCause().getCause().getMessage().contains("Cycles in children are not supported."));
        }
    }
}
