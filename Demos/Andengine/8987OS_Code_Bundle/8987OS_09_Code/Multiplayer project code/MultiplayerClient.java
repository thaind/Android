public class MultiplayerClient implements ISocketConnectionServerConnectorListener{
	private static final String TAG = "CLIENT";
	
	private Engine mEngine;
	private Scene mScene;
	
	// Server variables
	private String mServerIP;
	private int mServerPort;
	
	// ServerConnector object which deals with the communication between client --> server
	private ServerConnector<SocketConnection> mServerConnector;
	
	// Color ID for the points to be drawn. This value can be stored
	// client-side
	private int mColorId = AddPointClientMessage.COLOR_RED;
	
	// Constructor
	public MultiplayerClient(final String pServerIP, final int pServerPort, final Engine pEngine, final Scene pScene){
		this.mServerIP = pServerIP;
		this.mServerPort = pServerPort;
		this.mEngine = pEngine;
		this.mScene = pScene;
	}

	// Client initialization method, to be called on client start-up
	public void initClient(){
		// A separate thread is needed for network communication
		// in order to avoid blocking on the main thread
		this.mEngine.runOnUpdateThread(new Runnable(){

			@Override
			public void run() {
				try {
					
					// Create the socket with the specified Server IP and port
					Socket socket = new Socket(MultiplayerClient.this.mServerIP, MultiplayerClient.this.mServerPort);
					// Create the socket connection, establishing the input/output stream
					SocketConnection socketConnection = new SocketConnection(socket);
					// Create the server connector with the specified socket connection
					// and client connection listener
					MultiplayerClient.this.mServerConnector = new SocketConnectionServerConnector(socketConnection, MultiplayerClient.this);
					
					// Register a server message to the with a message handler
					MultiplayerClient.this.mServerConnector.registerServerMessage(ServerMessages.SERVER_MESSAGE_ADD_POINT, AddPointServerMessage.class, new IServerMessageHandler<SocketConnection>(){
						
						// If a client receives the SERVER_MESSAGE_ADD_POINT server message,
						// Fire the following code...
						@Override
						public void onHandleMessage(
								ServerConnector<SocketConnection> pServerConnector,
								IServerMessage pServerMessage)
								throws IOException {
							
							// obtain the class casted server message
							AddPointServerMessage message = (AddPointServerMessage) pServerMessage;
							
							// Create a new Rectangle (point), based on values obtained via the server
							// message received
							Rectangle point = new Rectangle(message.getX(), message.getY(), 3, 3, mEngine.getVertexBufferObjectManager());
							
							// each point should stay on-screen for 3 seconds
							float pointExpireTime = 3;
							// Pass the expire timer to the point
							point.setUserData(pointExpireTime);
							
							// Obtain the color id from the message
							final int colorId = message.getColorId();

							// Set the point's color based on the message's color id
							switch(colorId){
							case AddPointClientMessage.COLOR_RED:
								point.setColor(1,0,0);
								break;
							case AddPointClientMessage.COLOR_GREEN:
								point.setColor(0,1,0);
								break;
							case AddPointClientMessage.COLOR_BLUE:
								point.setColor(0,0,1);
								break;
							}
							
							// Attach the point to the client's scene
							mScene.attachChild(point);
						}
						
					});
					
					// Once we've created our server connector and registered our server messages,
					// we can call start() on the server connector's connection
					MultiplayerClient.this.mServerConnector.getConnection().start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Set the client's point color
	public void setDrawColor(final int pColorId){
		this.mColorId = pColorId;
	}
	
	// Obtain color Id
	public int getDrawColor(){
		return this.mColorId;
	}
	
	// Send a client message through the server connector,
	// passing the message to the server
	public void sendMessage(ClientMessage pClientMessage){
		try {
			this.mServerConnector.sendClientMessage(pClientMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Terminate method, used to terminate both the client's connection
	// to the server as well as the client thread
	public void terminate(){
		if(this.mServerConnector != null)
		this.mServerConnector.terminate();
	}
	
	// Listener - states when we (as a client) have connected to a server
	@Override
	public void onStarted(ServerConnector<SocketConnection> pServerConnector) {
		Log.i(TAG, "Connected :" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress().toString());
	}

	// Listener - states when we (as a client) have disconnected from a server
	@Override
	public void onTerminated(ServerConnector<SocketConnection> pServerConnector) {
		Log.i(TAG, "Disonnected :" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress().toString());
	}
}
