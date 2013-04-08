public class HidingAndRetrievingLayers extends BaseGameActivity {

	public static int WIDTH = 800;
	public static int HEIGHT = 480;

	public static float CENTER_X = WIDTH * 0.5f;
	public static float CENTER_Y = HEIGHT * 0.5f;
	
	/* Number of Entity layers/screens */
	public static int SCREEN_COUNT = 3;

	/* Rectangle dimensions - width/height */
	public static int DIMENSION = 90;

	private Scene mScene;
	private Camera mCamera;

	private Font mFont;

	/* These three Entity objects will represent different screens */
	private final Entity mScreenOne = new Entity();
	private final Entity mScreenTwo = new Entity();
	private final Entity mScreenThree = new Entity();

	/* This entity modifier is defined as the 'transition-in' modifier
	 * which will move an Entity/screen into the camera-view */
	private final ParallelEntityModifier mMoveInModifier = new ParallelEntityModifier(
			new MoveXModifier(3, WIDTH, 0), new RotationModifier(3, 0, 360),
			new ScaleModifier(3, 0, 1));
	
	/* This entity modifier is defined as the 'transition-out' modifier
	 * which will move an Entity/screen out of the camera-view */
	private final ParallelEntityModifier mMoveOutModifier = new ParallelEntityModifier(
			new MoveXModifier(3, 0, -WIDTH), new RotationModifier(3, 360, 0),
			new ScaleModifier(3, 1, 0));

	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		/* Create the mFont object */
		mFont = FontFactory.create(mEngine.getFontManager(),
				mEngine.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 24f, true,
				android.graphics.Color.WHITE);
		
		/* Load the mFont object */
		mFont.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {

		mScene = new Scene() {
			/* Variable which will accumulate time passed to
			 * determine when to switch screens */
			float timeCounter = 0;

			/* Define the first screen indices to be transitioned in and out */
			int layerInIndex = 0;
			int layerOutIndex = SCREEN_COUNT - 1;

			/* Execute the code below on every update to the mScene object */
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {

				/* If accumulated time is equal to or greater than 4 seconds */
				if (timeCounter >= 4) {

					/* Set screens to be transitioned in and out */
					setLayer(mScene.getChildByIndex(layerInIndex),
							mScene.getChildByIndex(layerOutIndex));

					/* Reset the time counter */
					timeCounter = 0;

					/* Setup the next screens to be swapped in and out */
					if (layerInIndex >= SCREEN_COUNT - 1) {
						layerInIndex = 0;
						layerOutIndex = SCREEN_COUNT - 1;
					} else {
						layerInIndex++;
						layerOutIndex = layerInIndex - 1;
					}

				}
				/* Accumulate seconds passed since last update */
				timeCounter += pSecondsElapsed;
				super.onManagedUpdate(pSecondsElapsed);
			}
		};

		/* Attach the layers to the scene.
		 * Their layer index (according to mScene) is relevant to the
		 * order in which they are attached */
		mScene.attachChild(mScreenOne); // layer index == 0
		mScene.attachChild(mScreenTwo); // layer index == 1
		mScene.attachChild(mScreenThree); // layer index == 2

		/* Loop through the screen count, applying rectangles/text
		 * which represent separate screens of a game */
		for (int i = 0; i < SCREEN_COUNT; i++) {

			/* Obtain the Entity whose index value is equal to i */
			Entity layer = (Entity) mScene.getChildByIndex(i);

			/* Set the initial properties of the scene */
			//layer.setRotationCenter(CENTER_X, CENTER_Y);
			layer.setVisible(false);

			/* Attach a set of four rectangles to the screen */
			Rectangle rectangleOne = new Rectangle(CENTER_X - DIMENSION * 0.5f,
					CENTER_Y - DIMENSION * 0.5f, DIMENSION, DIMENSION,
					mEngine.getVertexBufferObjectManager());
			rectangleOne.setColor(org.andengine.util.adt.color.Color.RED);

			Rectangle rectangleTwo = new Rectangle(CENTER_X - DIMENSION * 0.5f, CENTER_Y
					+ DIMENSION * 0.5f, DIMENSION, DIMENSION,
					mEngine.getVertexBufferObjectManager());
			rectangleTwo.setColor(org.andengine.util.adt.color.Color.BLUE);

			Rectangle rectangleThree = new Rectangle(CENTER_X + DIMENSION * 0.5f, CENTER_Y + DIMENSION * 0.5f,
					DIMENSION, DIMENSION,
					mEngine.getVertexBufferObjectManager());
			rectangleThree.setColor(org.andengine.util.adt.color.Color.GREEN);

			Rectangle rectangleFour = new Rectangle(CENTER_X + DIMENSION * 0.5f,
					CENTER_Y - DIMENSION * 0.5f, DIMENSION, DIMENSION,
					mEngine.getVertexBufferObjectManager());
			rectangleFour.setColor(org.andengine.util.adt.color.Color.YELLOW);

			/* Attach the rectangles to the current Entity/screen being created */
			layer.attachChild(rectangleOne);
			layer.attachChild(rectangleTwo);
			layer.attachChild(rectangleThree);
			layer.attachChild(rectangleFour);

			/* Define text coordinates */
			final String layerText = "Screen: " + (i + 1);
			final float textX = CENTER_X;

			/* Apply a Text object to the Entity object which will help
			 * differentiate between the different screens */
			Text text = new Text(textX, HEIGHT - mFont.getLineHeight(), mFont,
					layerText, layerText.length(),
					mEngine.getVertexBufferObjectManager());
			
			layer.attachChild(text);
		}

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/* This method is used to swap screens in and out of the camera-view */
	private void setLayer(IEntity pLayerIn, IEntity pLayerOut) {
		
		/* If the layer being transitioned into the camera-view is invisible,
		 * set it to visibile */
		if (!pLayerIn.isVisible()) {
			pLayerIn.setVisible(true);
		}

		/* Global modifiers must be reset after each use */
		mMoveInModifier.reset();
		mMoveOutModifier.reset();

		/* Register the transitional effects to the screens */
		pLayerIn.registerEntityModifier(mMoveInModifier);
		pLayerOut.registerEntityModifier(mMoveOutModifier);
	}
}