/*
 *     Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *     WSO2 Inc. licenses this file to you under the Apache License,
 *     Version 2.0 (the "License"); you may not use this file except
 *     in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 */
package org.apache.synapse.config.xml.rest;

import junit.framework.TestCase;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.rest.API;

import java.util.Collection;

public class APIDispatchOrderTest extends TestCase {

    /**
     * This test will test whether the api addition to config get ordered properly
     */
	public void testAPITableOrderWithAddAPI() {
		SynapseConfiguration config = new SynapseConfiguration();
		API api1 = new API("API1", "/context/test");
		API api2 = new API("API2", "/context");
		API api3 = new API("API3", "/context/test/ctx");
		config.addAPI("API1", api1);
		config.addAPI("API2", api2);
		config.addAPI("API3", api3);
		//this should get the api table in descending order with longest at the first
		Collection<API> apis = config.getAPIs();
		API[] apisArray = apis.toArray(new API[apis.size()]);
		//api3 with context /context/test should be first in the list
		assertEquals("Order is not correct", api3, apisArray[0]);
    }

    /**
     * Test ability to add APIs without reordering
     */
	public void testAPITableOrderWithAddAPIWithoutReOrder() {
		SynapseConfiguration config = new SynapseConfiguration();
		API api1 = new API("API1", "/context/test");
		API api2 = new API("API2", "/context");
		API api3 = new API("API3", "/context/test/ctx");
		
		
		config.addAPI("API1", api1, false);
		config.addAPI("API2", api2, false);
		config.addAPI("API3", api3, false);		
		
		//this should get the api table as it is.
		Collection<API> apis = config.getAPIs();
		API[] apisArray = apis.toArray(new API[apis.size()]);
		//api1 with context /context/test should be first in the list
		assertEquals("Order is not correct before sorting", api1, apisArray[0]);	
		
		//calling explicitly to order the apitable
		config.reconstructAPITable();
		apis = config.getAPIs();
		API[] apisArray2 = apis.toArray(new API[apis.size()]);
		//api3 with context /context/test/ctx should be first in the list
		assertEquals("Order is not correct", api3, apisArray2[0]);
    }
}
