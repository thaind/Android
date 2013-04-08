public class OverridingUpdates extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	private static final int RECTANGLE_WIDTH = 80;
	private static final int RECTANGLE_HEIGHT = RECTANGLE_WIDTH;
	
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;
	
	private Rectangle rectangleOne;
	private Rectangle rectangleTwo;
	
	//====================================================
	// CREATE ENGINE OPTIONS
	//====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);

		return engineOptions;
	}

	//====================================================
	// CREATE RESOURCES
	//====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	//====================================================
	// CREATE SCENE
	//====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	//====================================================
	// POPULATE SCENE
	//====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		// Setup our rectangle coordinates (center of the screen)
		final float x = (WIDTH / 2) - (RECTANGLE_WIDTH / 2);
		final float y = (HEIGHT / 2) - (RECTANGLE_HEIGHT / 2);
		
		// Create/attach the first rectangle
		rectangleOne = new Rectangle(x, y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager()){
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				Log.d("TIME", String.valueOf(pSecondsElapsed));
				// Calculate rotation offset based on time passed
				final float rotationOffset = pSecondsElapsed * 25;
				
				// Adjust this rectangle's rotation
				this.setRotation(this.getRotation() + rotationOffset);
				
				// Pass the seconds elapsed to our update thread
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		rectangleOne.setColor(Color.BLUE);
		mScene.attachChild(rectangleOne);
		
		// Create/attach second rectangle which overrides its update method
		rectangleTwo = new Rectangle(0, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager()){
			
			// Override the onManagedUpdateMethod in order to provide our own events
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				
				// Adjust our rectangles position
				if(this.getX() < (WIDTH)){
					// Increase the position by 5 pixels per update
					this.setPosition(this.getX() + 5f, this.getY());
				} else {
					// Reset the rectangles X position and slightly increase the Y position
					// If the rectangle exits camera view (width)
					this.setPosition(-RECTANGLE_WIDTH, this.getY() + (RECTANGLE_HEIGHT / 2));
				}
				
				// Reset to initial position if the rectangle exits camera view (height)
				if(this.getY() > HEIGHT){
					this.setPosition(0, 0);
				}
				
				// If our rectangle is colliding, set the color to green if it's
				// not already green
				if(this.collidesWith(rectangleOne) && this.getColor() != Color.GREEN){
					this.setColor(Color.GREEN);
					
				// If the shape is not colliding and not already red,
				// reset the rectangle to red
				} else if(this.getColor() != Color.RED){
					this.setColor(Color.RED);
				}
				
				// Pass the seconds elapsed to our update thread
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		rectangleTwo.setColor(Color.RED);
		mScene.attachChild(rectangleTwo);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}