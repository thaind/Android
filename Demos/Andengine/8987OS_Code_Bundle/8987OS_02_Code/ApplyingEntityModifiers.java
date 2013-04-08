public class ApplyingEntityModifiers extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	private final int RECTANGLE_WIDTH = 80;
	private final int RECTANGLE_HEIGHT = RECTANGLE_WIDTH;
	
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;
	
	private Rectangle rectangleOne;
	private Rectangle rectangleTwo;
	private Rectangle rectangleThree;
	private Rectangle rectangleFour;
	
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

		// Setup the first rectangle
		rectangleOne = new Rectangle(0, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager());
		rectangleOne.setColor(Color.RED);
		mScene.attachChild(rectangleOne);
		
		// Create a loop modifier
		LoopEntityModifier loopModifierA = new LoopEntityModifier(new MoveXModifier(3, 0, WIDTH, EaseElasticIn.getInstance()));
		
		// Register the entity modifier to rectangleOne
		rectangleOne.registerEntityModifier(loopModifierA);
		
		
		// Setup the second rectangle
		rectangleTwo = new Rectangle(WIDTH - RECTANGLE_WIDTH, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager());
		rectangleTwo.setColor(Color.BLUE);
		mScene.attachChild(rectangleTwo);
		
		// Create a sequence modifier
		SequenceEntityModifier sequenceModifier = new SequenceEntityModifier(
				new MoveYModifier(1.5f, 0, HEIGHT - RECTANGLE_HEIGHT), 
				new MoveYModifier(1.5f, HEIGHT - RECTANGLE_HEIGHT, 0));
		
		// Create a loop modifier for our sequence modifier
		LoopEntityModifier loopModifierB = new LoopEntityModifier(sequenceModifier);
		
		// Register the entity modifier to rectangleTwo
		rectangleTwo.registerEntityModifier(loopModifierB);
		
		// Setup the third rectangle
		rectangleThree = new Rectangle(WIDTH - RECTANGLE_WIDTH, HEIGHT - RECTANGLE_HEIGHT, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager());
		rectangleThree.setColor(Color.GREEN);
		mScene.attachChild(rectangleThree);
		
		// Create a parallel modifier
		ParallelEntityModifier parallelModifier = new ParallelEntityModifier(
				new MoveXModifier(3f, WIDTH - RECTANGLE_WIDTH, 0),
				new RotationModifier(3, 0, 720));
		
		// Create a loop modifier for our parallel modifier
		LoopEntityModifier loopModifierC = new LoopEntityModifier(parallelModifier);
		
		// Register the entity modifier to rectangleThree
		rectangleThree.registerEntityModifier(loopModifierC);
		
		// Setup the fourth rectangle
		rectangleFour = new Rectangle(0, HEIGHT - RECTANGLE_HEIGHT, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, mEngine.getVertexBufferObjectManager());
		rectangleFour.setColor(Color.YELLOW);
		mScene.attachChild(rectangleFour);
		
		// Create a 'curved movement' modifier
		QuadraticBezierCurveMoveModifier moveModifier = new QuadraticBezierCurveMoveModifier(3, 0, HEIGHT - RECTANGLE_HEIGHT, WIDTH / 2 - RECTANGLE_WIDTH / 2, HEIGHT / 2 - RECTANGLE_HEIGHT / 2, 0, 0);
		
		// Create a loop modifier for our quadratic curve modifier
		LoopEntityModifier loopModifierD = new LoopEntityModifier(moveModifier);
		
		// Register the entity modifier to rectangleFour
		rectangleFour.registerEntityModifier(loopModifierD);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}