public class MultiplayerServer implements
		ISocketServerListener<SocketConnectionClientConnector>,
		ISocketConnectionClientConnectorListener {
	private static final String TAG = "SERVER";

	private Engine mEngine;
	
	// Port variable needed for clients to connect to the server
	private int mServerPort;

	// Server's socket meant for handling many simultaneous connections
	private SocketServer<SocketConnectionClientConnector> mSocketServer;

	// Constructor
	public MultiplayerServer(final int pServerPort, final Engine pEngine) {
		this.mServerPort = pServerPort;
		this.mEngine = pEngine;
	}

	// Server initialization method, to be called on server initialization
	public void initServer() {
		
		this.mEngine.runOnUpdateThread(new Runnable(){

			@Override
			public void run() {
				
				// Create the SocketServer, specifying a port, client listener and 
				// a server state listener (listeners are implemented in this class)
				MultiplayerServer.this.mSocketServer = new SocketServer<SocketConnectionClientConnector>(
						MultiplayerServer.this.mServerPort,
						MultiplayerServer.this, MultiplayerServer.this) {

					// Called when a new client connects to the server...
					@Override
					protected SocketConnectionClientConnector newClientConnector(
							SocketConnection pSocketConnection)
							throws IOException {

						// Create a new client connector from the socket connection
						final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);
						
						// Register the point-drawing client message to the new client
						clientConnector.registerClientMessage(ClientMessages.CLIENT_MESSAGE_ADD_POINT, AddPointClientMessage.class, new IClientMessageHandler<SocketConnection>(){

							// Handle message received by the server...
							@Override
							public void onHandleMessage(
									ClientConnector<SocketConnection> pClientConnector,
									IClientMessage pClientMessage)
									throws IOException {
								// Obtain the class-casted client message
								AddPointClientMessage incomingMessage = (AddPointClientMessage) pClientMessage;
								
								// Create a new server message containing the contents of the message received
								// from a client
								AddPointServerMessage outgoingMessage = new AddPointServerMessage(incomingMessage.getID(), incomingMessage.getX(), incomingMessage.getY(), incomingMessage.getColorId());
								
								// Reroute message received from client to all other clients
								sendMessage(outgoingMessage);
							}
							
						});
						
						// Return the new client connector
						return clientConnector;
					}
				};
				// Start the server once it's initialized
				MultiplayerServer.this.mSocketServer.start();
			}
			
		});

	}

	// Send broadcast server message to all clients
	public void sendMessage(ServerMessage pServerMessage){
		try {
			this.mSocketServer.sendBroadcastServerMessage(pServerMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Terminate the server socket and stop the server thread
	public void terminate(){
		if(this.mSocketServer != null)
		this.mSocketServer.terminate();
	}
	
	// Listener - In the event of a client connecting
	@Override
	public void onStarted(ClientConnector<SocketConnection> pClientConnector) {
		Log.i(TAG, "Client Connected: "
				+ pClientConnector.getConnection().getSocket().getInetAddress()
						.getHostAddress());
	}

	// Listener - In the event of a client terminating the connection
	@Override
	public void onTerminated(ClientConnector<SocketConnection> pClientConnector) {
		Log.i(TAG, "Client Disconnected: "
				+ pClientConnector.getConnection().getSocket().getInetAddress()
						.getHostAddress());
	}

	// Listener - In the event of the server starting up
	@Override
	public void onStarted(
			SocketServer<SocketConnectionClientConnector> pSocketServer) {
		Log.i(TAG, "Started");
	}

	// Listener - In the event of the server shutting down
	@Override
	public void onTerminated(
			SocketServer<SocketConnectionClientConnector> pSocketServer) {
		Log.i(TAG, "Terminated");
	}

	@Override
	public void onException(
			SocketServer<SocketConnectionClientConnector> pSocketServer,
			Throwable pThrowable) {
		Log.i(TAG, "Exception: ", pThrowable);

	}

}
