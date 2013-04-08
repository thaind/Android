public class MultiplayerExtensionExample extends SimpleBaseGameActivity {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;
	
	// Server & client id's
	private static final int SERVER_ID = 0;
	private static final int CLIENT_ID = 1;
	
	// Loopback IP (Localhost)
	private static final String LOCALHOST_IP = "127.0.0.1";
	
	// Server connection port
	private static final int SERVER_PORT = 4444;

	// Dialog constants
	private static final int DIALOG_CHOOSE_ENVIRONMENT_ID = 0;
	private static final int DIALOG_ENTER_SERVER_IP = DIALOG_CHOOSE_ENVIRONMENT_ID + 1;

	private Camera mCamera;
	private Scene mScene;
	
	// Client's current Color ID string
	private String mColorIdString;
	
	// RunnablePoolItem used for displaying an update text when a new color is selected
	private RunnablePoolItem mColorIdToastRunnablePoolItem = new RunnablePoolItem(){

		@Override
		public void run() {
			Toast.makeText(getApplicationContext(), "Draw color set to: " + mColorIdString, Toast.LENGTH_SHORT).show();
		}
	};
	
	// Client object
	MultiplayerClient mClient;
	
	// Server object
	MultiplayerServer mServer;

	// The server IP is localhost by default, unless a device chooses client-mode
	private String mServerIP = LOCALHOST_IP;

	// We should obtain a message pool in order to recycle old messages
	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		// Register/allocate the messages needed for the application's message pool
		this.mMessagePool.registerMessage(ServerMessages.SERVER_MESSAGE_ADD_POINT, AddPointServerMessage.class);
		this.mMessagePool.registerMessage(ClientMessages.CLIENT_MESSAGE_ADD_POINT, AddPointClientMessage.class);
		
		// Display the dialog which prompts the device to select either server,
		// client, or both
		this.showDialog(DIALOG_CHOOSE_ENVIRONMENT_ID);

		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	@Override
	protected void onCreateResources() throws IOException {
		// Do nothing...
	}

	@Override
	protected Scene onCreateScene() {
		mScene = new Scene(){
			
			
			// We are overriding the scene's onManagedUpdate method in order to
			// loop through the scene's children and hide them
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				
				// Obtain the scene's child count
				final int childCount = this.getChildCount();
					
				// Loop through the scene's children
				for(int i = 0; i < childCount; i++){
					
					// Obtain the current index entity
					final IEntity entity = this.getChildByIndex(i);
					
					// Obtain the individual entity's time left on the scene
					final float pointExpireTime = (Float) entity.getUserData();
					
					// Calculate the time remaining for this entity
					final float pointRemainingTime = pointExpireTime - pSecondsElapsed;
					
					// If the entity's remaining time is equal to or less than 0...
					if(pointRemainingTime <= 0){
						
						// Hide the entity and ignore updates to it
						entity.setVisible(false);
						entity.setIgnoreUpdate(true);
						
						// Otherwise...
					} else {
						// Set the user data of this entity to the newly calculated
						// remaining time
						entity.setUserData(pointRemainingTime);
					}
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
			
		};

		mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
						
						// If device is running as a server...
						if (mServer != null) {
							
							/* Since this recipe allows for clients to draw on client devices,
							 * we don't actually have any events for the server's touch method.
							 * We're simply including this conditional to show how we can create
							 * events based on whether or not the device is a server or client
							 */
							
							// The nested client-check inside the server-check means a device
							// is running as both the client & server
							if(mClient != null){
								// Obtain a ServerMessage object from the mMessagePool
								AddPointServerMessage message = (AddPointServerMessage) MultiplayerExtensionExample.this.mMessagePool.obtainMessage(ServerMessages.SERVER_MESSAGE_ADD_POINT);
								// Set up the message with the device's ID, touch coordinates and draw color
								message.set(SERVER_ID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), mClient.getDrawColor());
								// Send the client/server's draw message to all clients
								mServer.sendMessage(message);
								// Recycle the message back into the message pool
								MultiplayerExtensionExample.this.mMessagePool.recycleMessage(message);
							return true;
							}
							
						// If device is running as a client...
					} else if(mClient != null){
						/* Similar to the message sending code above, except
						 * in this case, the client is *not* running as a server.
						 * This means we have to first send the message to the server
						 * via a ClientMessage rather than ServerMessage
						 */
						AddPointClientMessage message = (AddPointClientMessage) MultiplayerExtensionExample.this.mMessagePool.obtainMessage(ClientMessages.CLIENT_MESSAGE_ADD_POINT);
						message.set(CLIENT_ID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), mClient.getDrawColor());
						mClient.sendMessage(message);
						MultiplayerExtensionExample.this.mMessagePool.recycleMessage(message);
			
						return true;
					}	
				}
				return false;
			}
		});
		return mScene;
	}

	/* The dialog code below acts as the entry-point to
	 * the server and client. In a real-world application,
	 * we might decide to forget dialogs and create a custom
	 * UI for a more appealing online lobby
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// Create dialog that allows user to choose either server, client,
		// or both
		case DIALOG_CHOOSE_ENVIRONMENT_ID:
			return new AlertDialog.Builder(this)
					.setTitle("Choose server or client environment...")
					.setCancelable(false)
					.setPositiveButton("Client", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// If user selects to be a client, open a text box
							// to enter the ip of a server to connect to
							MultiplayerExtensionExample.this
									.showDialog(DIALOG_ENTER_SERVER_IP);
						}

					})
					.setNeutralButton("Server", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// If the user selects to be a server, create the
							// server object and call initServer() on it
							mServer = new MultiplayerServer(SERVER_PORT, mEngine);
							mServer.initServer();
						}

					})
					.setNegativeButton("Server and Client",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// If the user selects to be both server and client,
									// call the initServerAndClient method
									MultiplayerExtensionExample.this
											.initServerAndClient();
								}

							}).create();

		// Create the 'Enter IP' dialog, allowing users to enter a server
		// IP to connect to if they select to be solely a client
		case DIALOG_ENTER_SERVER_IP:
			final EditText editText = new EditText(this);
			return new AlertDialog.Builder(this).setTitle("Enter Server IP...")
					.setCancelable(false).setView(editText)
					.setPositiveButton("Connect", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							// Set the mServerIP String to that of the edit text box
							MultiplayerExtensionExample.this.mServerIP = editText
									.getText().toString();
							
							// Create a new client object, passing the server, port,
							// engine and scene to our multiplayer client
							mClient = new MultiplayerClient(mServerIP,
									SERVER_PORT, mEngine, mScene);
							
							// Initialize the client
							mClient.initClient();
						}
					}).create();
		}
		return super.onCreateDialog(id);
	}

	// This method is called if a user selects to run as 
	// server & client via the dialog menu
	private void initServerAndClient() {
		// Create & initialize the server
		mServer = new MultiplayerServer(SERVER_PORT, mEngine);
		mServer.initServer();

		// We're allowing the thread to sleep for
		// a little bit to give the server time to start up
		// before initializing the client
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Create & initialize the server
		mClient = new MultiplayerClient(mServerIP, SERVER_PORT, mEngine, mScene);
		mClient.initClient();
	}

	@Override
	protected void onDestroy() {
		// Terminate the client and server socket connections
		// when the application is destroyed
		if (this.mClient != null)
			this.mClient.terminate();

		if (this.mServer != null)
			this.mServer.terminate();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// If the device's 'menu' key is pressed...
		if(keyCode == KeyEvent.KEYCODE_MENU){
			
			// If the device is running as a client...
			if(mClient != null){
				
				// Obtain the current color id
				int colorId = mClient.getDrawColor();
				
				// Depending on the current color value, we change
				// to a new color (either red, green or blue)
				switch(colorId){
				case AddPointClientMessage.COLOR_RED:
					colorId = AddPointClientMessage.COLOR_GREEN;
					this.mColorIdString = "Green";
					
					break;
				case AddPointClientMessage.COLOR_GREEN:
					colorId = AddPointClientMessage.COLOR_BLUE;
					this.mColorIdString = "Blue";
					break;
				case AddPointClientMessage.COLOR_BLUE:
					colorId = AddPointClientMessage.COLOR_RED;
					this.mColorIdString = "Red";
					break;
				}
				
				// Set the new drawing color via the color id
				mClient.setDrawColor(colorId);
				
				// Show a Toast, displaying the new color
				this.runOnUiThread(mColorIdToastRunnablePoolItem);
				if(!mColorIdToastRunnablePoolItem.isRecycled()){
					mColorIdToastRunnablePoolItem.recycle();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
