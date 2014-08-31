/**
 * Copyright 2014 Christian Drumm
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.drumm.adt_transport_utils;

public interface IAdtTransportUtilsConstants {
	static final String DISCOVERY_URI = "/ztransportutils/discovery";
	static final String TOC_RESOURCE_SCHEME = "http://www.drumm.de/ztransportutils/toc";
	static final String TOC_TERM = "create_toc";

	static final String QUERY_PARAMETER_TRANSPORT_REQUEST = "Z-ToC-RequestID";
	static final String QUERY_PARAMETER_RELEASE_IMMEDIATELY = "Z-ToC-ReleaseImmediately";
}
