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

import java.net.HttpURLConnection;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.communication.message.HeadersFactory;
import com.sap.adt.communication.message.IHeaders;
import com.sap.adt.communication.message.IResponse;
import com.sap.adt.communication.message.IResponse.IErrorInfo;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IQueryParameter;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.communication.resources.QueryParameter;
import com.sap.adt.compatibility.discovery.AdtDiscoveryFactory;
import com.sap.adt.compatibility.discovery.IAdtDiscovery;
import com.sap.adt.compatibility.discovery.IAdtDiscoveryCollectionMember;
import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.project.IAdtCoreProject;
import com.sap.adt.project.ui.util.ProjectUtil;
import com.sap.adt.tm.IRequest;
import com.sap.adt.tools.core.project.IAbapProject;

public class TransportOfCopiesRequest {
	private IWorkbenchWindow window;
	private ITreeSelection selection;
	private boolean releaseREquest;

	public TransportOfCopiesRequest(IWorkbenchWindow window,
			ITreeSelection selection, boolean releaseRequest) {
		this.window = window;
		this.selection = selection;
		this.releaseREquest = releaseRequest;
	}

	public void executePost() {
		String destination = this.getAbapProjectDestination();
		URI tocResourceUri = this.getResourceUri(destination);
		if (tocResourceUri != null) {
			this.executePost(destination, tocResourceUri);
		} else {
			MessageDialog.openError(this.window.getShell(),
					"ADT Transport Utils Error",
					"Necessary backend resource could not be found. ");
		}
	}

	private void executePost(String destination, URI tocResourceUri) {
		IRestResource tocRessource = AdtRestResourceFactory
				.createRestResourceFactory()
				.createResourceWithStatelessSession(tocResourceUri, destination);

		IRequest transportRequest = (IRequest) this.selection.getFirstElement();

		IHeaders requestHeader = HeadersFactory.newHeaders();

		IQueryParameter transportRequestParam = new QueryParameter(
				IAdtTransportUtilsConstants.QUERY_PARAMETER_TRANSPORT_REQUEST,
				transportRequest.getNumber());
		IQueryParameter releaseRequestParam = null;
		if (this.releaseREquest) {
			releaseRequestParam = new QueryParameter(
					IAdtTransportUtilsConstants.QUERY_PARAMETER_RELEASE_IMMEDIATELY,
					"X");
		}

		try {
			IResponse response = tocRessource
					.post(null, requestHeader, IResponse.class,
							transportRequestParam, releaseRequestParam);
			int status = response.getStatus();
			if (status != HttpURLConnection.HTTP_OK) {
				IErrorInfo errorInfo = response.getErrorInfo();
				MessageDialog
						.openError(
								this.window.getShell(),
								"ADT Transport Utils Error",
								"An error occured creating or releasing the transport of copies! The error was: "
										+ errorInfo.getMessage());
			}
		} catch (RuntimeException e) {
			MessageDialog
					.openError(
							this.window.getShell(),
							"ADT Transport Utils Error",
							"An exception occured creating or releasing the transport of copies! Exception: "
									+ e.getMessage());
		}

	}

	private String getAbapProjectDestination() {
		IProject project = ProjectUtil.getActiveAdtCoreProject(this.selection,
				null, null, IAdtCoreProject.ABAP_PROJECT_NATURE);
		IAbapProject abapProject = (IAbapProject) project
				.getAdapter(IAbapProject.class);
		AdtLogonServiceUIFactory.createLogonServiceUI().ensureLoggedOn(
				abapProject.getDestinationData(),
				PlatformUI.getWorkbench().getProgressService());
		String destination = abapProject.getDestinationId();
		return destination;
	}

	private URI getResourceUri(String destination) {
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(
				destination,
				URI.create(IAdtTransportUtilsConstants.DISCOVERY_URI));

		IAdtDiscoveryCollectionMember collectionMember = discovery
				.getCollectionMember(
						IAdtTransportUtilsConstants.TOC_RESOURCE_SCHEME,
						IAdtTransportUtilsConstants.TOC_TERM,
						new NullProgressMonitor());

		if (collectionMember == null) {
			return null;
		} else {
			return collectionMember.getUri();
		}
	}
}
