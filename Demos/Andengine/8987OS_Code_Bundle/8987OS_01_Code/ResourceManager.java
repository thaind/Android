public class ResourceManager {

	// ResourceManager Singleton instance
	private static ResourceManager INSTANCE;
	
	/* The variables listed should be kept public, allowing us easy access
	   to them when creating new Sprites, Text objects and to play sound files */
	public ITextureRegion mGameBackgroundTextureRegion;
	public ITextureRegion mMenuBackgroundTextureRegion;
	
	public Sound mSound;

	public Font	mFont;

	ResourceManager(){
		// The constructor is of no use to us
	}

	public synchronized static ResourceManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ResourceManager();
		}
		return INSTANCE;
	}

	/* Each scene within a game should have a loadTextures method as well
	 * as an accompanying unloadTextures method. This way, we can display
	 * a loading image during scene swapping, unload the first scene's textures
	 * then load the next scenes textures.
	 */
	public synchronized void loadGameTextures(Engine pEngine, Context pContext){
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 800, 480);

		mGameBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "game_background.png");
		
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	
	/* All textures should have a method call for unloading once
	 * they're no longer needed; ie. a level transition. */
	public synchronized void unloadGameTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mGameBackgroundTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}
	
	/* Similar to the loadGameTextures(...) method, except this method will be
	 * used to load a different scene's textures
	 */
	public synchronized void loadMenuTextures(Engine pEngine, Context pContext){
		// Set our menu assets folder in "assets/gfx/menu/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager() ,800 , 480);
		
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "menu_background.png");
		
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	
	// Once again, this method is similar to the 'Game' scene's for unloading
	public synchronized void unloadMenuTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mMenuBackgroundTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}
	
	/* As with textures, we can create methods to load sound/music objects
	 * for different scene's within our games.
	 */
	public synchronized void loadSounds(Engine pEngine, Context pContext){
		// Set the SoundFactory's base path
		SoundFactory.setAssetBasePath("sounds/");
		 try {
			 // Create mSound object via SoundFactory class
			 mSound	= SoundFactory.createSoundFromAsset(pEngine.getSoundManager(), pContext, "sound.mp3");			 
		 } catch (final IOException e) {
             Log.v("Sounds Load","Exception:" + e.getMessage());
		 }
	}	
	
	/* In some cases, we may only load one set of sounds throughout
	 * our entire game's life-cycle. If that's the case, we may not
	 * need to include an unloadSounds() method. Of course, this all
	 * depends on how much variance we have in terms of sound
	 */
	public synchronized void unloadSounds(){
		// we call the release() method on sounds to remove them from memory
		if(!mSound.isReleased())mSound.release();
	}
	
	/* Lastly, we've got the loadFonts method which, once again,
	 * tends to only need to be loaded once as Font's are generally 
	 * used across an entire game, from menu to shop to game-play.
	 */
	public synchronized void loadFonts(Engine pEngine){
		FontFactory.setAssetBasePath("fonts/");
		
		// Create mFont object via FontFactory class
		mFont = FontFactory.create(pEngine.getFontManager(), pEngine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),  32f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);

		mFont.load();
	}
	
	/* If an unloadFonts() method is necessary, we can provide one
	 */
	public synchronized void unloadFonts(){
		// Similar to textures, we can call unload() to destroy font resources
		mFont.unload();
	}
}