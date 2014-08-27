#adt_transport_utils_plugin
A ABAP in Eclipse plug in providing additional tools for the SAP transport system. 
The first version of the plug-in provide functionality to create and release transports of copies based on existing 
transport requests.

##Installation
The installation of the plug-in consists of two steps:
1. Installing the eclipse plug-in
1. Installing the ABAP part of the plug-in
###Installing the eclipse plug-in
The easiest way to install the eclipse plug-in is via the update site. The update site is available at the 
following URL: https://raw.githubusercontent.com/ceedee666/adt_transport_utils_plugin/master/eclipse-plugin/adt_transport_utils_update_site/
###Installing the ABAP part of the plug-in
In oder to install the ABAP part of the plug-in you need to have SAPlink (https://www.assembla.com/spaces/saplink/wiki) installed on the application server.
If SAPlink is installed simply download the Z_ADT_TRANSPORT_UTILS.nugg from the abap-plugin folder and install it using SAPlink.
After the installation you should make sure that all objects contained in the nugg file are active. Especially the contained enhancement
needs to be active in order for the tool to work.
