public class ResourceManager extends Object {
	
	//====================================================
	// CONSTANTS
	//====================================================
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	//====================================================
	// VARIABLES
	//====================================================
	// We include these objects in the resource manager for
	// easy accessibility across our project.
	public Engine engine;
	public Context context;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;
	
	// The resource variables listed should be kept public, allowing us easy access
	// to them when creating new Sprite and Text objects and to play sound files.
	// ======================== Game Resources ================= //
	public static ITextureRegion gameBackgroundTextureRegion;
	
	// ======================== Menu Resources ================= //
	public static ITextureRegion menuBackgroundTextureRegion;
	
	// =================== Shared Game and Menu Resources ====== //
	public static ITiledTextureRegion buttonTiledTextureRegion;
	public static ITextureRegion cloudTextureRegion;
	public static Sound clickSound;
	public static Font fontDefault32Bold;
	public static Font fontDefault72Bold;
	
	// This variable will be used to revert the TextureFactory's default path when we change it.
	private String mPreviousAssetBasePath = "";

	//====================================================
	// CONSTRUCTOR
	//====================================================
	private ResourceManager(){
	}

	//====================================================
	// GETTERS & SETTERS
	//====================================================
	// Retrieves a global instance of the ResourceManager
	public static ResourceManager getInstance(){
		return INSTANCE;
	}
	
	//====================================================
	// PUBLIC METHODS
	//====================================================
	// Setup the ResourceManager
	public void setup(final Engine pEngine, final Context pContext, final float pCameraWidth, final float pCameraHeight, final float pCameraScaleX, final float pCameraScaleY){
		engine = pEngine;
		context = pContext;
		cameraWidth = pCameraWidth;
		cameraHeight = pCameraHeight;
		cameraScaleFactorX = pCameraScaleX;
		cameraScaleFactorY = pCameraScaleY;
	}
	
	// Loads all game resources.
	public static void loadGameResources() {
		getInstance().loadGameTextures();
		getInstance().loadSharedResources();
	}
	
	// Loads all menu resources
	public static void loadMenuResources() {
		getInstance().loadMenuTextures();
		getInstance().loadSharedResources();
	}
	
	// Unloads all game resources.
	public static void unloadGameResources() {
		getInstance().unloadGameTextures();
	}

	// Unloads all menu resources
	public static void unloadMenuResources() {
		getInstance().unloadMenuTextures();
	}
	
	// Unloads all shared resources
	public static void unloadSharedResources() {
		getInstance().unloadSharedTextures();
		getInstance().unloadSounds();
		getInstance().unloadFonts();
	}
	
	//====================================================
	// PRIVATE METHODS
	//====================================================
	// Loads resources used by both the game scenes and menu scenes
	private void loadSharedResources(){
		loadSharedTextures();
		loadSounds();
		loadFonts();
	}
	
	// ============================ LOAD TEXTURES (GAME) ================= //
	private void loadGameTextures(){
		// Store the current asset base path to apply it after we've loaded our textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		// Set our game assets folder to "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		// background texture - only load it if we need to:
		if(gameBackgroundTextureRegion==null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(engine.getTextureManager(), 11, 490);
			gameBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, "background.png");
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 4));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	// ============================ UNLOAD TEXTURES (GAME) =============== //
	private void unloadGameTextures(){
		// background texture - only unload it if it is loaded:
		if(gameBackgroundTextureRegion!=null) {
			if(gameBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
				gameBackgroundTextureRegion.getTexture().unload();
				gameBackgroundTextureRegion = null;
			}
		}
	}

	// ============================ LOAD TEXTURES (MENU) ================= //
	private void loadMenuTextures(){
		// Store the current asset base path to apply it after we've loaded our textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		// Set our menu assets folder to "assets/gfx/menu/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		// background texture:
		if(menuBackgroundTextureRegion==null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(engine.getTextureManager(), 11, 490);
			menuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, "background.png");
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 4));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}
		
		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	// ============================ UNLOAD TEXTURES (MENU) =============== //
	private void unloadMenuTextures(){
		// background texture:
		if(menuBackgroundTextureRegion!=null) {
			if(menuBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
				menuBackgroundTextureRegion.getTexture().unload();
				menuBackgroundTextureRegion = null;
			}
		}
	}
	
	// ============================ LOAD TEXTURES (SHARED) ================= //
	private void loadSharedTextures(){
		// Store the current asset base path to apply it after we've loaded our textures
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		// Set our shared assets folder to "assets/gfx/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// button texture:
		if(buttonTiledTextureRegion==null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(engine.getTextureManager(), 522, 74);
			buttonTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, "button01.png", 2, 1);
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 4));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// cloud texture:
		if(cloudTextureRegion==null) {
			BuildableBitmapTextureAtlas texture = new BuildableBitmapTextureAtlas(engine.getTextureManager(), 266, 138);
			cloudTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, "cloud.png");
			try {
				texture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 4));
				texture.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}
	// ============================ UNLOAD TEXTURES (SHARED) ============= //
	private void unloadSharedTextures(){
		// button texture:
		if(buttonTiledTextureRegion!=null) {
			if(buttonTiledTextureRegion.getTexture().isLoadedToHardware()) {
				buttonTiledTextureRegion.getTexture().unload();
				buttonTiledTextureRegion = null;
			}
		}
		// cloud texture:
		if(cloudTextureRegion!=null) {
			if(cloudTextureRegion.getTexture().isLoadedToHardware()) {
				cloudTextureRegion.getTexture().unload();
				cloudTextureRegion = null;
			}
		}
	}
	
	// =========================== LOAD SOUNDS ======================== //
	private void loadSounds(){
		SoundFactory.setAssetBasePath("sounds/");
		if(clickSound==null) {
			try {
				// Create the clickSound object via the SoundFactory class
				clickSound	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "click.mp3");
			} catch (final IOException e) {
				Log.v("Sounds Load","Exception:" + e.getMessage());
			}
		}
	}
	// =========================== UNLOAD SOUNDS ====================== //
	private void unloadSounds(){
		if(clickSound!=null)
			if(clickSound.isLoaded()) {
				// Unload the clickSound object. Make sure to stop it first.
				clickSound.stop();
				engine.getSoundManager().remove(clickSound);
				clickSound = null;
			}
	}

	// ============================ LOAD FONTS ========================== //
	private void loadFonts(){
		// Create the Font objects via FontFactory class
		if(fontDefault32Bold==null) {
			fontDefault32Bold = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD),  32f, true, Color.WHITE_ARGB_PACKED_INT);
			fontDefault32Bold.load();
		}
		if(fontDefault72Bold==null) {
			fontDefault72Bold = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 512, 512, Typeface.create(Typeface.DEFAULT, Typeface.BOLD),  72f, true, Color.WHITE_ARGB_PACKED_INT);
			fontDefault72Bold.load();
		}
	}
	// ============================ UNLOAD FONTS ======================== //
	private void unloadFonts(){
		// Unload the fonts
		if(fontDefault32Bold!=null) {
			fontDefault32Bold.unload();
			fontDefault32Bold = null;
		}
		if(fontDefault72Bold!=null) {
			fontDefault72Bold.unload();
			fontDefault72Bold = null;
		}
	}
}