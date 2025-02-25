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
package org.openmrs.module.reporting.cohort.definition.persister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;

/**
 * This class returns CohortDefinitions that are persisted in memory.
 */
public class MockCohortDefinitionPersister implements CohortDefinitionPersister {
	
	// Keep track of the primary keys handed out
	Integer primaryKeySequence = new Integer(1);	
	
	List<CohortDefinition> cohortDefinitions = new ArrayList<CohortDefinition>();	
	Map<Integer,CohortDefinition> indexById = new HashMap<Integer,CohortDefinition>();	
	Map<String,CohortDefinition> indexByUuid = new HashMap<String,CohortDefinition>();	
	
	/**
     * @see CohortDefinitionPersister#getDefinition(Integer)
     */
    public CohortDefinition getDefinition(Integer id) {
    	return indexById.get(id);
    }
    
	/**
     * @see CohortDefinitionPersister#getDefinitionByUuid(String)
     */
    public CohortDefinition getDefinitionByUuid(String uuid) {
    	return indexByUuid.get(uuid);
    }

	/**
     * @see CohortDefinitionPersister#getAllDefinitions(boolean)
     */
    public List<CohortDefinition> getAllDefinitions(boolean includeRetired) {
    	return cohortDefinitions;
    }
    
	/**
	 * @see CohortDefinitionPersister#getNumberOfDefinitions(boolean)
	 */
	public int getNumberOfDefinitions(boolean includeRetired) {
		return cohortDefinitions.size();
	}

	/**
     * @see CohortDefinitionPersister#getDefinitionByName(String, boolean)
     */
    public List<CohortDefinition> getDefinitions(String name, boolean exactMatchOnly) {
    	return cohortDefinitions;
    }
    
	/**
     * @see CohortDefinitionPersister#saveDefinition(Definition)
     */
    public CohortDefinition saveDefinition(CohortDefinition cohortDefinition) {
    	
    	// Remove the existing cohort definition
    	if (getDefinitionByUuid(cohortDefinition.getUuid())!=null) { 
    		purgeDefinition(cohortDefinition);
    	} 
    	// Otherwise, we set the UUID and identifier of the new cohort definition
    	else { 
    		// Set values
    		cohortDefinition.setId(primaryKeySequence++);
    		cohortDefinition.setUuid(UUID.randomUUID().toString());
    	}
    	
    	// Add the dataset definition to the list 
    	cohortDefinitions.add(cohortDefinition);
    	
    	// Index the dataset definition
    	indexById.put(cohortDefinition.getId(), cohortDefinition);
    	indexByUuid.put(cohortDefinition.getUuid(), cohortDefinition);
    	return cohortDefinition;
    }

	/**
     * @see CohortDefinitionPersister#purgeDefinition(CohortDefinition)
     */
    public void purgeDefinition(CohortDefinition cohortDefinition) {    	
    	indexById.remove(cohortDefinition.getId());
    	indexByUuid.remove(cohortDefinition.getUuid());
    	cohortDefinitions.remove(cohortDefinition);    	
    }   
}