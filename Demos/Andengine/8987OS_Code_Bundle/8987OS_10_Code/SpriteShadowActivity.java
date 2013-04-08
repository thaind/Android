public class SpriteShadowActivity extends BaseGameActivity implements IOnSceneTouchListener {

	// ====================================================
	// CONSTANTS
	// ====================================================
	static float CHARACTER_START_X = 400f;
	static float CHARACTER_START_Y = 128f;
	static float SHADOW_OFFSET_X = 0f;
	static float SHADOW_OFFSET_Y = -64f;
	static float SHADOW_MAX_ALPHA = 0.75f;
	static float SHADOW_MIN_ALPHA = 0.1f;
	static float SHADOW_MAX_ALPHA_HEIGHT = 200f;
	static float SHADOW_MIN_ALPHA_HEIGHT = 0f;
	static float SHADOW_START_X = CHARACTER_START_X + SHADOW_OFFSET_X;
	static float SHADOW_START_Y = CHARACTER_START_Y + SHADOW_OFFSET_Y;
	static float CHARACTER_SHADOW_Y_DIFFERENCE = CHARACTER_START_Y - SHADOW_START_Y;
	static float SHADOW_ALPHA_HEIGHT_DIFFERENCE = SHADOW_MAX_ALPHA_HEIGHT - SHADOW_MIN_ALPHA_HEIGHT;
	static float SHADOW_ALPHA_DIFFERENCE = SHADOW_MAX_ALPHA - SHADOW_MIN_ALPHA;

	// ====================================================
	// VARIABLES
	// ====================================================
	Sprite shadowSprite;
	Sprite characterSprite;

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), new Camera(0, 0, 800, 480)).setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		Scene mScene = new Scene();
		mScene.setBackground(new Background(0.8f,0.8f,0.8f));
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		BitmapTextureAtlas characterTexture = new BitmapTextureAtlas(this.getTextureManager(), 128, 256, TextureOptions.BILINEAR);
		TextureRegion characterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(characterTexture, this, "gfx/character.png", 0, 0);
		characterTexture.load();
		BitmapTextureAtlas shadowTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		TextureRegion shadowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shadowTexture, this, "gfx/shadow.png", 0, 0);
		shadowTexture.load();
		shadowSprite = new Sprite(SHADOW_START_X,SHADOW_START_Y,shadowTextureRegion,this.getVertexBufferObjectManager());
		
		characterSprite = new Sprite(CHARACTER_START_X,CHARACTER_START_Y,characterTextureRegion,this.getVertexBufferObjectManager()) {
			@Override
			public void setPosition(final float pX, final float pY) {
				super.setPosition(pX, pY);
				shadowSprite.setPosition(pX + SHADOW_OFFSET_X, shadowSprite.getY());
				updateShadowAlpha();
			}
		};
		pScene.attachChild(shadowSprite);
		pScene.attachChild(characterSprite);
		updateShadowAlpha();
		
		pScene.setOnSceneTouchListener(this);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	public void updateShadowAlpha() {
		shadowSprite.setAlpha(
				MathUtils.bringToBounds(SHADOW_MIN_ALPHA, SHADOW_MAX_ALPHA,
				SHADOW_MAX_ALPHA - 
					((((characterSprite.getY()-CHARACTER_SHADOW_Y_DIFFERENCE)-SHADOW_START_Y) / SHADOW_ALPHA_HEIGHT_DIFFERENCE)
							*SHADOW_ALPHA_DIFFERENCE)
				));
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()) {
			characterSprite.setPosition(pSceneTouchEvent.getX(), Math.max(pSceneTouchEvent.getY(), CHARACTER_START_Y));
		}
		return false;
	}
}