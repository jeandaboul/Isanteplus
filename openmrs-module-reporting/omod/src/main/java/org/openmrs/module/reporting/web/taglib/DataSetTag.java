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
package org.openmrs.module.reporting.web.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;

public class DataSetTag extends BodyTagSupport {
	
	public static final long serialVersionUID = 1L;
	
	private final Log log = LogFactory.getLog(getClass());

	// Styling information
	private String id = null;
	private String cssStyle = null;
	private String cssClass = null;

	// 
	private DataSet dataSet = null;
	private String dataSetName = null;
	private String cohortDefinition = null;
	private String datasetDefinition = null;
	
	public int doStartTag() throws JspException {
		try { 		
			// By default we try to pull the dataSet out of the request
			if (dataSet == null) { 
				dataSetName = (dataSetName!=null)?dataSetName:"dataSet";			
				dataSet = (DataSet) pageContext.getRequest().getAttribute(dataSetName);
			} 
			else { 
				// TODO Evaluate the dataset using the UUID from the cohort and dataset definition.
				
			}
			
			
			if (dataSet != null) { 
				List<DataSetColumn> columns = dataSet.getMetaData().getColumns();
						
				pageContext.getOut().write("<table id=\"" + id + "\" class=\"" + cssClass + "\">");
				pageContext.getOut().write("<thead>");
				pageContext.getOut().write("<tr>");
				
				int headerCount = 0;
				for (DataSetColumn column : columns) { 
					if (headerCount++<7) { 
						pageContext.getOut().write("<th>");
						pageContext.getOut().write(column.getLabel());
						pageContext.getOut().write("</th>");
					}
					
				}
				if (dataSet.getMetaData().getColumns().size() > 7 ) { 
					pageContext.getOut().write("<th>Showing " + headerCount + " out of " + columns.size() + " columns</th>");
				}
				
				pageContext.getOut().write("</tr>");
				pageContext.getOut().write("</thead>");
				pageContext.getOut().write("<tbody>");
				
				// Iterate over all rows
				for(DataSetRow dataSetRow : dataSet) { 							
									
					pageContext.getOut().write("<tr>");
					int columnCount = 0;
					for(DataSetColumn column : columns) { 
						if(columnCount++<7) { 
							pageContext.getOut().write("<td>");				
							pageContext.getOut().write("" + dataSetRow.getColumnValue(column));				
							pageContext.getOut().write("</td>");
						}
					}
					if (dataSet.getMetaData().getColumns().size() > 7 ) { 
						pageContext.getOut().write("<td>...</td>");
					}	
					pageContext.getOut().write("</tr>");
				}				
				pageContext.getOut().write("</tbody>");
				pageContext.getOut().write("</table>");
			
			} 
			else { 
				pageContext.getOut().write("<span>Unable to display dataset table.</span>");
			
			}
			
		} catch(IOException e) { 
			log.error("Unable to write timespan to output", e);
		}		
		return SKIP_BODY;
	}


	public DataSet getDataSetObject() {
		return dataSet;
	}


	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public String getDataSetName() {
		return dataSetName;
	}


	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}


	
	public String getCohortDefinition() {
		return cohortDefinition;
	}


	public void setCohortDefinition(String cohortDefinition) {
		this.cohortDefinition = cohortDefinition;
	}


	public String getDatasetDefinition() {
		return datasetDefinition;
	}


	public void setDatasetDefinition(String datasetDefinition) {
		this.datasetDefinition = datasetDefinition;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCssStyle() {
		return cssStyle;
	}


	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}


	public String getCssClass() {
		return cssClass;
	}


	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	
}
