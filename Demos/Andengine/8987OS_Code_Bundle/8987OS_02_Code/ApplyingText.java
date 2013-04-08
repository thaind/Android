public class ApplyingText extends BaseGameActivity {

	//====================================================
	// CONSTANTS
	//====================================================
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	
	private static final String TEST_STRING = "The quick brown fox jumps over the lazy dog.";
	
	//====================================================
	// VARIABLES
	//====================================================
	private Scene mScene;
	private Camera mCamera;
	
	private Font mFont;
	private Text mText;
	
	
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

		// Load our font
		mFont = FontFactory.create(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),  32f, true, Color.WHITE_ARGB_PACKED_INT);
		mFont.load();
		
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

		// Measure the width our string will appear on the screen in pixels
		final float textLength = FontUtils.measureText(mFont, TEST_STRING);
		
		// Set our text to appear in the center of the screen based on its width and height
		final float x = (WIDTH / 2) - (textLength / 2);
		final float y = (HEIGHT / 2) - (mFont.getLineHeight() / 2);
		
		// Create TextOptions for our text
		final TextOptions textOptions = new TextOptions();
		
		// We can choose between a few different options for our text including
		// leading characters, autowrap, and text alignment
		
		//textOptions.setAutoWrap(AutoWrap.WORDS);
		//textOptions.setAutoWrapWidth(100);
		textOptions.setHorizontalAlign(HorizontalAlign.CENTER);
		
		// Create our text object
		mText = new Text(x, y, mFont, TEST_STRING, TEST_STRING.length(), textOptions, mEngine.getVertexBufferObjectManager());
		// Set the text color value to pure blue with full alpha (R,G,B,A)
		mText.setColor(0f, 0f, 1f, 1f);
		
		// Apply our text to the scene
		mScene.attachChild(mText);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}