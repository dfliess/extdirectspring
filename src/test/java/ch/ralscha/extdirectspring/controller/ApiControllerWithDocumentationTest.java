/**
 * Copyright 2010-2012 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.ralscha.extdirectspring.controller;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import ch.ralscha.extdirectspring.util.ApiCache;

/**
 * Tests for {@link ApiController}.
 * 
 * @author Ralph Schaer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/testApplicationContext.xml")
public class ApiControllerWithDocumentationTest {

	private final static Logger logger = LoggerFactory.getLogger(ApiControllerWithDocumentationTest.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApiController apiController;

	@Autowired
	private RouterController routerController;

	@Before
	public void setupApiController() throws Exception {
		ApiCache.INSTANCE.clear();

		routerController.getConfiguration().setTimeout(15000);
		routerController.getConfiguration().setEnableBuffer(false);
		routerController.getConfiguration().setMaxRetries(5);
		routerController.getConfiguration().setStreamResponse(true);
		// routerController.getConfiguration().setApiDocumentation(true);
	}

	/**
	 * to test the following need to activate Feature 'ALLOW_COMMENTS' for
	 * jackson parser
	 * <p>
	 * typical error is com.fasterxml.jackson.core.JsonParseException:
	 * Unexpected character ('/' (code 47)): maybe a (non-standard) comment?
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGroupDoc() throws IOException {

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/action/api-debug-doc.js");
		MockHttpServletResponse response = new MockHttpServletResponse();
		apiController.api("Ext.ns", "actionns", "REMOTING_API", "POLLING_URLS", "SSE", "groupdoc", false, null,
				request, response);
		String content = response.getContentAsString();
		logger.info("\n\n" + content + "\n\n");
		ApiControllerTest.compare(content, response.getContentType(), ApiControllerTest.group1ApisWithDoc("actionns"),
				"Ext.ns", "REMOTING_API", "POLLING_URLS", "SSE", routerController.getConfiguration(), "remoting");
		Assert.isTrue(content.contains("/**"));
		Assert.isTrue(content.contains("@deprecated"));
		Assert.isTrue(content.contains("methodDoc"));
		Assert.isTrue(content.contains("@author"));
		Assert.isTrue(content.contains("@param: [d]"));
		Assert.isTrue(content.contains("@param: [e]"));
		Assert.isTrue(content.contains("@param: [b]"));
		Assert.isTrue(content.contains("@param: [a]"));
		Assert.isTrue(content.contains("@return"));
		Assert.isTrue(content.contains("*/"));

		request = new MockHttpServletRequest("GET", "/action/api-debug.js");
		response = new MockHttpServletResponse();
		apiController.api("Ext.ns", "actionns", "REMOTING_API", "POLLING_URLS", "SSE", "groupdoc", false, null,
				request, response);
		content = response.getContentAsString();
		logger.info("\n\n" + content + "\n\n");
		ApiControllerTest.compare(content, response.getContentType(), ApiControllerTest.group1ApisWithDoc("actionns"),
				"Ext.ns", "REMOTING_API", "POLLING_URLS", "SSE", routerController.getConfiguration(), "remoting");
		Assert.doesNotContain("/**", content, "generation of api-debug.js should not contain method documentation");

		request = new MockHttpServletRequest("GET", "/action/api.js");
		response = new MockHttpServletResponse();
		apiController.api("Ext.ns", "actionns", "REMOTING_API", "POLLING_URLS", "SSE", "groupdoc", false, null,
				request, response);
		content = response.getContentAsString();
		logger.info("\n\n" + content + "\n\n");
		ApiControllerTest.compare(content, response.getContentType(), ApiControllerTest.group1ApisWithDoc("actionns"),
				"Ext.ns", "REMOTING_API", "POLLING_URLS", "SSE", routerController.getConfiguration(), "remoting");
		Assert.doesNotContain("/**", content, "generation of api.js should not contain method documentation");
	}

}