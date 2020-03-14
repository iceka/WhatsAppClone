# WhatsAppClone
WhatsApp Clone With FireBase.

Give some appreciation by clicking star the repo to support the project

### WhatsAppClone Camera
![](WhatsAppClone-Camera.gif)

### Screenshots
<img src="SS Chat.jpg" width="220"/> <img src="SS Chat Room.jpg" width="220"/> <img src="SS Contact.jpg" width="220"/> <img src="SS Status.jpg" width="220"/> <img src="SS Create Status.jpg" width="220"/> <img src="SS Status View.jpg" width="220"/>

# How To Use 
First you need to create FireBase Project. Download the google-service.json file and put it under /app folder like this: <img src = "https://www.gstatic.com/mobilesdk/160426_mobilesdk/images/android_studio_project_panel@2x.png" width="120"/>

After you complete creating your FireBase then you need to create FireBase Cloud Functions. [Follow this link to see the documentation.](https://firebase.google.com/docs/functions/get-started?authuser=1)

Then add below code to index.js file
```bash
'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
exports.countstatus = functions.database.ref('/status/{statususerid}/statusItem/{statusid}').onWrite(async (change) => {
	const collectionRef = change.after.ref.parent;
	const countRef = collectionRef.parent.child('statuscount');

	let increment;
	if (change.after.exists() && !change.before.exists()) {
		increment = 1; 
	} else if (!change.after.exists() && change.before.exists()) {
		increment = -1;
	} else {
		return null;
	}

	await countRef.transaction((current) => {
		return (current || 0) + increment;
	});
	console.log('Counter Updated.');
	return null
});

exports.recountStatus = functions.database.ref('/status/{statususerid}/statuscount').onWrite(async (snap) => {
  const counterRef = snap.ref;
  const collectionRef = counterRef.parent.child('likes');


  // Return the promise from counterRef.set() so our function
  // waits for this async event to complete before it exits.
  const messagesData = await collectionRef.once('value');
  return await counterRef.set(messagesData.numChildren());
});
```

# Done

# License


     Copyright [2020] [Imam Candra Kusuma]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
