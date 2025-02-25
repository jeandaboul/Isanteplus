package org.openmrs.module.reporting.web.controller.portlet;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.web.WebConstants;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.servlet.ModelAndView;

/**
 * This Controller provides common functionality required by all reporting portlets
 */
public class ReportingPortletController extends PortletController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String uniqueRequestId = (String) request.getAttribute(WebConstants.INIT_REQ_UNIQUE_ID);
		String lastRequestId = (String) session.getAttribute(WebConstants.OPENMRS_PORTLET_LAST_REQ_ID);
		if (uniqueRequestId.equals(lastRequestId)) {
			session.removeAttribute(WebConstants.OPENMRS_PORTLET_LAST_REQ_ID);
			session.removeAttribute(WebConstants.OPENMRS_PORTLET_CACHED_MODEL);
		}
		return super.handleRequest(request, response);
	}

	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		// TODO: Figure out why this is necessary.
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
    	model.put("portletUUID", UUID.randomUUID().toString().replace("-", ""));
	}
}
