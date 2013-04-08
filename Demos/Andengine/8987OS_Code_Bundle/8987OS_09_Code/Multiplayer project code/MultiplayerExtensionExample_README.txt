The MultiplayerExtensionExample class is the entry point to the application. This activity's purpose is to prompt the user to select either a server, client, or server and client environment on their device. This handles both the startup and termination of the client and server components. onDestroy(), this activity will cleanup all threads.

Additionally, the MultiplayerExtensionExample contains the IOnSceneTouchListener code. If the activity realizes that a touch event has occured on the device and the activity contains a non-null client component, a message will be sent to the server to notify other clients to draw a point at the same location.

Installation:
1. Import the MultiplayerExtensionExample and all associating class files into a fresh project.
2. Edit the AndroidManifest.xml file and set the ".MultiplayerExtensionExample" as the main activity.