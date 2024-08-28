# Skribble-Connector

The skribble connector helps you to get a easy access to the skribble features. 
Skibble is a e-signature solution for the Swiss- and EU-market and provides different layers on signing legal weight.

For more information about the skribbleplatform jump to: **[skribble.com](https://www.skribble.com/)**

In the connector-demo there are a simple request for creating a fast signature and as well a little overview over all signaturerequest you made with their status.


## Demo

A new signature requests can be created and monitored via the demo. If a signature request is created, the signature verification will be handled over the platform. 
Each participant has full access to their own documents and can sign or decline them.

On the demo there are two processes: The first process is an easy dialog to create a new signature request and the second shows all the signature request with their own status.

Creating a new Signer Request, upload your document which has to be sign and add your signer.
![image](doc/img/request1.png)

Loads all your documents from the Skribbleplatform.
If you created successfully a Request you will see it here. 
![image](doc/img/overview1.png)

Document view on the Skribbleplatform as a signer
![image](doc/img/skribble_doc_view1.png)

Refresh the overviewpage and you will see the status on the signer and the overallstatus are changed.
![image](doc/img/overview2.png)

To get a Sign from a signer there are two easy options:
	-set the notify parameter off the signer on true and he will recieve directly a mail from the skribble platform shorty after the request.
	-or if you want notify himself you can disable the notiy and send him the url over your own custom styled email.
	
There are three options for signing verification: SES, AES and QES	


## Setup

Before any signing interactions between the **Axon Ivy Engine** and the **Skribble-Platform** services can be run, they have to be introduced to each other. This can be done as follows:

1. Create an **Account** account: **[here](https://my.skribble.com/business/signup/?lang=en) **

2. Create an **demo API key** for more information jump to the admin documentation: **[here](https://docs.skribble.com/business-admin/api/apicreate#create-api-keys) **

3. Open the `Configuration/variables.yaml` in your Designer and set the username and the apikey from which you get on the skribbleplattform

   ```
	# == Variables ==
	# 
	# You can define here your project Variables.
	#
	Variables:
	  #set all paramaters for Skribble-connector
	  skribbleConnector:
	    #username
	    username: 'api_demo_xxxxx' 			#<-- paste here your username
	    #apikey
	    #[password]
	    authKey: ${decrypt:\u00AF\u00A8...} #<-- paste here your apikey and encrypt it

   ```

4. Save the changed settings and start a demo process
