public class UsingParallaxBackgrounds extends BaseGameActivity{

	public static int WIDTH = 800;
	public static int HEIGHT = 480;

	/* Min/Max distances the camera can automatically pan to on the X axis */
	private static final float CAMERA_MIN_CENTER_X = WIDTH * 0.5f - 25;
	private static final float CAMERA_MAX_CENTER_X = WIDTH * 0.5f + 25;
	
	/* Camera scroll speed factor */
	private static final float SCROLL_FACTOR = 5;

	private Scene mScene;
	private Camera mCamera;

	private ITextureRegion mHillTextureRegion;

	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new Camera(0, 0, WIDTH, HEIGHT){
			
			/* Boolean value which will determine whether
			 * to increase or decrease x coordinate */
			boolean incrementX = true;
			
			/* On camera update... */
			@Override
			public void onUpdate(float pSecondsElapsed) {

				/* Obtain the current camera X coordinate */
				final float currentCenterX = this.getCenterX();
				
				/* Value which will be used to offset the camera */
				float offsetCenterX = 0;
				
				/* If incrementX is true... */
				if(incrementX){
					
					/* offset the camera's x coordinate according to time passed */
					offsetCenterX = currentCenterX + pSecondsElapsed * SCROLL_FACTOR;
					
					/* If the new offset coordinate is greater than the max X limit */
					if(offsetCenterX >= CAMERA_MAX_CENTER_X){
						
						/* Set to decrement the camera's X coordinate next */
						incrementX = false;
					}
				} else {
					/* If increment is equal to false, decrement X coordinate */
					offsetCenterX = currentCenterX - pSecondsElapsed * SCROLL_FACTOR;
					
					/* If the new offset coordinate is less than the min X limit */
					if(offsetCenterX <= CAMERA_MIN_CENTER_X){
						
						/* Set to increment the camera's X coordinate next */
						incrementX = true;
					}
				}
				
				/* Apply the offset position to the camera */
				this.setCenter(offsetCenterX, this.getCenterY());
				
				super.onUpdate(pSecondsElapsed);
			}
		};

		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Create a texture atlas suitable for the hill.png.
		 * The hill image used for this recipe is equal to 800x75
		 * pixels in dimension*/
		BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 800, 150);

		/* Create the mHillTextureRegion, passing in the hill.png image */
		mHillTextureRegion = BitmapTextureAtlasTextureRegionFactory
		.createFromAsset(bitmapTextureAtlas, getAssets(), "hill.png");

		/* Build the texture atlas. Since we're loading only a 
		 * single image into the texture atlas, we need not
		 * worry about padding or spacing values */
		try {
			bitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		/* Load the texture atlas */
		bitmapTextureAtlas.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		mScene = new Scene();

		/* Obtain the mHillTextureRegion image height for the purpose of
		 * placing the Sprites at different heights on the Scene */
		final float textureHeight = mHillTextureRegion.getHeight();
		
		/* Create the hill which will appear to be the furthest
		 * into the distance. This Sprite will be placed higher than the 
		 * rest in order to retain visibility of it */
		Sprite hillFurthest = new Sprite(WIDTH * 0.5f, textureHeight * 0.5f + 50, mHillTextureRegion,
				mEngine.getVertexBufferObjectManager());

		/* Create the hill which will appear between the furthest and closest
		 * hills. This Sprite will be placed higher than the closest hill, but
		 * lower than the furthest hill in order to retain visibility */
		Sprite hillMid = new Sprite(WIDTH * 0.5f, textureHeight * 0.5f + 25, mHillTextureRegion,
				mEngine.getVertexBufferObjectManager());

		/* Create the closest hill which will not be obstructed by any other hill 
		 * Sprites. This Sprite will be placed at the bottom of the Scene since
		 * nothing will be covering its view */
		Sprite hillClosest = new Sprite(WIDTH * 0.5f, textureHeight * 0.5f, mHillTextureRegion,
				mEngine.getVertexBufferObjectManager());

		/* Create the ParallaxBackground, setting the color values to represent 
		 * a blue sky */
		ParallaxBackground background = new ParallaxBackground(0.3f, 0.3f, 0.9f) {

			/* We'll use these values to calculate the 
			 * parallax value of the background */
			float cameraPreviousX = 0;
			float parallaxValueOffset = 0;
			
			/* onUpdates to the background, we need to calculate new 
			 * parallax values in order to apply movement to the background
			 * objects (the hills in this case) */
			@Override
			public void onUpdate(float pSecondsElapsed) {

				/* Obtain the camera's current center X value */
				final float cameraCurrentX = mCamera.getCenterX();

				/* If the camera's position has changed since last 
				 * update... */
				if (cameraPreviousX != cameraCurrentX) {
					
					/* Calculate the new parallax value offset by 
					 * subtracting the previous update's camera x coordinate
					 * from the current update's camera x coordinate */
					parallaxValueOffset +=  cameraCurrentX - cameraPreviousX;
					
					/* Apply the parallax value offset to the background, which 
					 * will in-turn offset the positions of entities attached
					 * to the background */
					this.setParallaxValue(parallaxValueOffset);
					
					/* Update the previous camera X since we're finished with this 
					 * update */
					cameraPreviousX = cameraCurrentX;
				}

				super.onUpdate(pSecondsElapsed);
			}
		};

		/* Rather than attaching Sprite's to the ParallaxBackground, we must
		 * attach ParallaxEntity objects. We create a new ParallaxEntity for
		 * each Sprite we'd like to attach to the background, specifying a
		 * parallax factor (speed in which to move). Further objects should move
		 * slower than closer objects */
		background.attachParallaxEntity(new ParallaxEntity(5, hillFurthest));
		background.attachParallaxEntity(new ParallaxEntity(10, hillMid));
		background.attachParallaxEntity(new ParallaxEntity(15, hillClosest));

		/* Set & Enabled the background */
		mScene.setBackground(background);
		mScene.setBackgroundEnabled(true);
		
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}