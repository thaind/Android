The MultiplayerServer class is the server component for the Multiplayer example project. This class makes up the server object, defining methods such as sendMessage(), terminate(), startup/shutdown, and client connect/disconnect listeners. This class also includes the code which handles received messages by the server by registering the AddPointClientMessage as a viable message for the server to receive. Once the server receives the message, it relay it to the rest of the clients on the network.

Installation:
1. Include this class in the Multiplayer example project.