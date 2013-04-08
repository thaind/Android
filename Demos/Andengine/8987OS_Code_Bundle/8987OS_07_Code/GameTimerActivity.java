public class GameTimerActivity extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================
	public static int cameraWidth = 800;
	public static int cameraHeight = 480;

	// ====================================================
	// VARIABLES
	// ====================================================
	public Scene mScene;
	public Font fontDefault32Bold;
	public Text countingText;
	public float EndingTimer = 10f;

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), new Camera(0, 0, cameraWidth, cameraHeight)).setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		fontDefault32Bold = FontFactory.create(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD),  32f, true, Color.BLACK_ARGB_PACKED_INT);
		fontDefault32Bold.load();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();
		mScene.setBackground(new Background(0.9f,0.9f,0.9f));
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		countingText = new Text(400f, 240f, fontDefault32Bold, "10", 10, this.getVertexBufferObjectManager());
		mScene.attachChild(countingText);
		mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				EndingTimer-=pSecondsElapsed;
				if(EndingTimer<=0) {
					// The timer has ended
					countingText.setText("0");
					mScene.unregisterUpdateHandler(this);
				} else {
					countingText.setText(String.valueOf(Math.round(EndingTimer)));
				}
			}
			@Override public void reset() {}
		});
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}