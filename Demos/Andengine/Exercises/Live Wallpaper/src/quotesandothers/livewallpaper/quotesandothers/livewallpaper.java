package quotesandothers.livewallpaper.quotesandothers;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.LoopModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier;
import org.anddev.andengine.entity.shape.modifier.PathModifier.IPathModifierListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Path;
import android.content.SharedPreferences;


import android.content.SharedPreferences;

public class livewallpaper extends BaseLiveWallpaperService implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String SHARED_PREFS_NAME = "elegantworldtemplatesettings";

	// Camera Constants
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 854;

	// ===========================================================
	// Fields
	// ===========================================================

	// Shared Preferences
	private SharedPreferences mSharedPreferences;

	private Texture mTexture;

	private TiledTextureRegion mPlayerTextureRegion;

	private TiledTextureRegion mEnemyTextureRegion;

	private Texture mAutoParallaxBackgroundTexture;

	private TextureRegion mParallaxLayerMid1;

	private TextureRegion mParallaxLayerTop;
	
	private TextureRegion mParallaxLayerTop2;

	private TextureRegion mParallaxLayerMid2;

	private TextureRegion mParallaxLayerLow1;

	private TextureRegion mParallaxLayerLow2;

	private TiledTextureRegion mFaceTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public org.anddev.andengine.engine.Engine onLoadEngine() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new org.anddev.andengine.engine.Engine(
				new EngineOptions(true, ScreenOrientation.PORTRAIT,
						new FillResolutionPolicy(), mCamera));
	}

	@Override
	public void onLoadResources() {
		

		this.mTexture = new Texture(2048, 2048, TextureOptions.DEFAULT);
		

		this.mAutoParallaxBackgroundTexture = new Texture(2048, 2048,
				TextureOptions.DEFAULT);
		this.mParallaxLayerMid1 = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/middle1.png",0, 320);
		this.mParallaxLayerTop = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/top.png", 0, 0);
		this.mParallaxLayerTop2 = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/top2.png", 0, 173);
		this.mParallaxLayerMid2 = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/middle2.png",0, 450);
		this.mParallaxLayerLow1 = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/lower1.png", 200,574);
		this.mParallaxLayerLow2 = TextureRegionFactory.createFromAsset(
				this.mAutoParallaxBackgroundTexture, this, "gfx/lower2.png", 0,740);
		this.mEngine.getTextureManager().loadTextures(this.mTexture,
				this.mAutoParallaxBackgroundTexture);

	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(2.7f,
				new Sprite(0, 0, this.mParallaxLayerTop)));
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(2.9f,
				new Sprite(0, 173, this.mParallaxLayerTop2)));
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(2.4f,
				new Sprite(0, 320, this.mParallaxLayerMid1)));
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-1.4f,
				new Sprite(0, 450, this.mParallaxLayerMid2)));
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(2.3f,
				new Sprite(200, 574, this.mParallaxLayerLow1)));
		autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-4.1f,
				new Sprite(0, 740, this.mParallaxLayerLow2)));



		scene.setBackground(autoParallaxBackground);

		/*
		 * Calculate the coordinates for the face, so its centered on the
		 * camera.
		 */
	

		return scene;
	}

	private float setHeight(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	protected void onTap(final int pX, final int pY) {

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pSharedPrefs,
			String pKey) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}