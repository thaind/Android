The MultiplayerClient class is the client component for the Multiplayer example project. This class makes up the client object, defining methods such as sendMessage(), terminate(), and connect/disconnect listeners. This class also includes the code which handles received messages by the client by registering the AddPointServerMessage as a viable message for the client to receive. Once the client receives the message, it will draw a point on the screen depending on the data received.

Installation:
1. Include this class in the Multiplayer example project.