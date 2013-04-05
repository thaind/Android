package com.mimminito.livewallpapertemplate;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.SharedPreferences;

public class LiveWallpaperTemplate extends BaseLiveWallpaperService implements SharedPreferences.OnSharedPreferenceChangeListener
{
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final String SHARED_PREFS_NAME = "livewallpapertemplatesettings";

	//Camera Constants
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;

	// ===========================================================
	// Fields
	// ===========================================================
	
	//Shared Preferences
	private SharedPreferences mSharedPreferences;

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
	public org.anddev.andengine.engine.Engine onLoadEngine()
	{
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new org.anddev.andengine.engine.Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), mCamera));
	}

	@Override
	public void onLoadResources()
	{
		//Set the Base Texture Path
		TextureRegionFactory.setAssetBasePath("gfx/");
	}

	@Override
	public Scene onLoadScene()
	{
		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.0f, 0.0f, 0.0f));

		return scene;
	}

	@Override
	public void onLoadComplete()
	{

	}
	
	@Override
	protected void onTap(final int pX, final int pY)
	{
		
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences pSharedPrefs, String pKey)
	{
		
	}

	// ===========================================================
	// Methods
	// ===========================================================	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}