public class OverridingDeviceKeys extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================
	public static int WIDTH = 800;
	public static int HEIGHT = 480;

	// ====================================================
	// VARIABLES
	// ====================================================
	private Scene mScene;
	private Camera mCamera;

	// Each rectangle imitates a different scene in our game
	Rectangle screenOne;
	Rectangle screenTwo;
	Rectangle screenThree;
	Rectangle screenFour;

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		// Create each rectangle which takes up the screen width and height.
		// Each rectangle is a different color in order to represent a different
		// scene
		
		screenOne = new Rectangle(0, 0, WIDTH, HEIGHT,
				mEngine.getVertexBufferObjectManager());
		mScene.attachChild(screenOne);
		screenOne.setColor(Color.GREEN);

		screenTwo = new Rectangle(0, 0, WIDTH, HEIGHT,
				mEngine.getVertexBufferObjectManager());
		mScene.attachChild(screenTwo);
		screenTwo.setColor(Color.BLUE);

		screenThree = new Rectangle(0, 0, WIDTH, HEIGHT,
				mEngine.getVertexBufferObjectManager());
		mScene.attachChild(screenThree);
		screenThree.setColor(Color.PINK);

		screenFour = new Rectangle(0, 0, WIDTH, HEIGHT,
				mEngine.getVertexBufferObjectManager());
		mScene.attachChild(screenFour);
		screenFour.setColor(Color.CYAN);

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// Override Android's responses for the physical device 'buttons'
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		// On the event that the 'back' key is pressed
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// Loop through the rectangles/"scenes" starting from the last attached
			for (int i = mScene.getChildCount() - 1; i >= 0; i--) {
				// Get the current entity
				Entity entity = (Entity) mScene.getChildByIndex(i);
				
				// If the rectangle is still visible
				if (entity.isVisible()) {
					// Set the current rectangle to invisible
					entity.setVisible(false);
					
					// Return false so that a single rectangle is removed for each
					// touch of the 'back' key
					return false;
				}
			}
			
			// The code below is only executed after all rectangles are no
			// longer visible
			
			
			// Dialogs must be executed on the UI Thread!
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// Create a dialog box which asks if we'd like to quit
					AlertDialog dialog = new AlertDialog.Builder(
							OverridingDeviceKeys.this).create();
					// Set the dialog's title text
					dialog.setTitle("Quit Game?");
					
					// Set the dialogs message text
					dialog.setMessage("Are you sure you want to quit?");
					
					// Create a button for the dialog box, titled "YES"
					dialog.setButton("YES",
							new DialogInterface.OnClickListener() {
						// Override the click event of this specific button
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							// If the "YES" button is clicked, close the activity
							OverridingDeviceKeys.this.finish();
						}
					});
					
					// Create a second button which allows the app to keep running
					dialog.setButton2("NO",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// We can leave this listener blank, simply closing the dialog
							// and resuming our game
						}
					});
					// We must call show() on the dialog in order to display it
					dialog.show();
				}
			});
			// Return false, disallowing the 'back' key to close our activity
			// since we're handling that ourselves
			return false;
		}
		// This call will execute Android's native response. We can return false
		// if we'd like to handle every type of event ourselves or if we'd like to
		// disable all device keys
		return super.onKeyDown(keyCode, event);
	}
}