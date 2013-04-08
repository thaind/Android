package ifl.games.runtime.Layers;

import ifl.games.MagneTank.R;
import ifl.games.runtime.MagneTankActivity;
import ifl.games.runtime.Input.GrowButton;
import ifl.games.runtime.Input.GrowToggleTextButton;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;
import ifl.games.runtime.Managers.SceneManager;
import ifl.games.runtime.Menus.MainMenu;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.app.AlertDialog;
import android.content.DialogInterface;

/** This layer is accessible from the MainMenu scene and allows the player
 *  to enable or disable music and sounds as well as choose a graphics
 *  quality or reset the level completion data that they have achieved.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class OptionsLayer extends ManagedLayer {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final OptionsLayer INSTANCE = new OptionsLayer();
	
	// ====================================================
	// INSTANCE GETTER
	// ====================================================
	public static OptionsLayer getInstance() {
		return INSTANCE;
	}
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private Sprite LayerBG;
	private Text TitleText;
	private GrowToggleTextButton SoundEnabledText;
	private GrowToggleTextButton MusicEnabledText;
	private GrowToggleTextButton GraphicsQualityText;
	
	private GrowToggleTextButton ResetText;
	
	// ====================================================
	// UPDATE HANDLERS
	// ====================================================
	// Animates the layer to slide in from the top.
	IUpdateHandler mSlideInUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(OptionsLayer.this.LayerBG.getY() > 0f) {
				OptionsLayer.this.LayerBG.setY(Math.max(OptionsLayer.this.LayerBG.getY() - (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), 0f));
			} else {
				ResourceManager.getInstance().engine.unregisterUpdateHandler(this);
			}
		}
		@Override
		public void reset() {}
	};
	
	// Animates the layer to slide out through the top and tell the SceneManager
	// to hide it when it is off-screen;
	IUpdateHandler mSlideOutUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(OptionsLayer.this.LayerBG.getY() < ((ResourceManager.getInstance().cameraHeight / 2f) + (OptionsLayer.this.LayerBG.getHeight() / 2f))) {
				OptionsLayer.this.LayerBG.setY(Math.min(OptionsLayer.this.LayerBG.getY() + (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), (ResourceManager.getInstance().cameraHeight / 2f) + (OptionsLayer.this.LayerBG.getHeight() / 2f)));
			} else {
				ResourceManager.getInstance().engine.unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}
		
		@Override
		public void reset() {}
	};
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void onHideLayer() {
		// Register the slide-out animation with the engine.
		ResourceManager.getInstance().engine.registerUpdateHandler(this.mSlideOutUpdateHandler);
	}
	
	@Override
	public void onLoadLayer() {
		if(this.mHasLoaded) {
			return;
		}
		this.mHasLoaded = true;
		
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		
		final Rectangle fadableBGRect = new Rectangle(0f, 0f, ResourceManager.getInstance().cameraWidth, ResourceManager.getInstance().cameraHeight, ResourceManager.getActivity().getVertexBufferObjectManager());
		fadableBGRect.setColor(0f, 0f, 0f, 0.8f);
		this.attachChild(fadableBGRect);
		
		this.attachChild(this.LayerBG = new Sprite(0f, (ResourceManager.getInstance().cameraHeight / 2f) + (ResourceManager.gameLevelLayerBGTR.getHeight() / 2f), ResourceManager.gameLevelLayerBGTR, ResourceManager.getActivity().getVertexBufferObjectManager()));
		this.LayerBG.setScale(1.5f / ResourceManager.getInstance().cameraScaleFactorY);
		
		this.TitleText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "OPTIONS", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.TitleText.setScale(Math.min(390f / this.TitleText.getWidth(), 1f));
		this.TitleText.setPosition(256f, 225f);
		this.TitleText.setColor(0.31f, 0.35f, 0.31f);
		this.LayerBG.attachChild(this.TitleText);
		
		this.SoundEnabledText = new GrowToggleTextButton(256f, 197.2f, "SOUND: ENABLED", "SOUND: DISABLED", ResourceManager.fontDefaultMagneTank48, !SFXManager.isSoundMuted()) {
			@Override
			public boolean checkState() {
				return !SFXManager.isSoundMuted();
			}
			
			@Override
			public void onClick() {
				SFXManager.toggleSoundMuted();
			}
		};
		this.SoundEnabledText.setScales(Math.min(390f / this.SoundEnabledText.getWidth(), 0.85f), Math.min(390f / this.SoundEnabledText.getWidth(), 0.85f) * 1.4f);
		this.SoundEnabledText.setColor(0.212f, 0.275f, 0.212f);
		this.LayerBG.attachChild(this.SoundEnabledText);
		this.registerTouchArea(this.SoundEnabledText);
		
		this.MusicEnabledText = new GrowToggleTextButton(256f, 169.4f, "MUSIC: ENABLED", "MUSIC: DISABLED", ResourceManager.fontDefaultMagneTank48, !SFXManager.isMusicMuted()) {
			@Override
			public boolean checkState() {
				return !SFXManager.isMusicMuted();
			}
			
			@Override
			public void onClick() {
				SFXManager.toggleMusicMuted();
			}
		};
		this.MusicEnabledText.setScales(Math.min(390f / this.MusicEnabledText.getWidth(), 0.85f), Math.min(390f / this.MusicEnabledText.getWidth(), 0.85f) * 1.2f);
		this.MusicEnabledText.setColor(0.212f, 0.275f, 0.212f);
		this.LayerBG.attachChild(this.MusicEnabledText);
		this.registerTouchArea(this.MusicEnabledText);
		
		this.GraphicsQualityText = new GrowToggleTextButton(256f, 141.6f, "GRAPHICS: HIGH", "GRAPHICS: NORMAL", ResourceManager.fontDefaultMagneTank48, ResourceManager.isUsingHighQualityGraphics()) {
			@Override
			public boolean checkState() {
				return ResourceManager.isUsingHighQualityGraphics();
			}
			
			@Override
			public void onClick() {
				ResourceManager.getInstance().switchQuality();
			}
		};
		this.GraphicsQualityText.setScales(Math.min(390f / this.GraphicsQualityText.getWidth(), 0.85f), Math.min(390f / this.GraphicsQualityText.getWidth(), 0.85f) * 1.2f);
		this.GraphicsQualityText.setColor(0.212f, 0.275f, 0.212f);
		this.LayerBG.attachChild(this.GraphicsQualityText);
		this.registerTouchArea(this.GraphicsQualityText);
		
		this.ResetText = new GrowToggleTextButton(256f, 113.8f, "RESET DATA", "", ResourceManager.fontDefaultMagneTank48, true) {
			@Override
			public boolean checkState() {
				return true;
			}
			
			@Override
			public void onClick() {
				ResourceManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.getActivity());
						builder.setTitle("Reset Game Data?");
						builder.setIcon(R.drawable.icon);
						builder.setMessage("This will reset all scores and stars that you've achieved. Continue?").setPositiveButton("Reset Data", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, final int id) {
								final int ActivityStartCount = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_ACTIVITY_START_COUNT);
								final int MusicMuted = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_MUSIC_MUTED);
								final boolean Quality = MagneTankActivity.getBooleanFromSharedPreferences(MagneTankActivity.SHARED_PREFS_HIGH_QUALITY_GRAPHICS);
								final int MarketRated = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_RATING_SUCCESS);
								final int SoundMuted = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_SOUNDS_MUTED);
								ResourceManager.getActivity().getSharedPreferences(MagneTankActivity.SHARED_PREFS_MAIN, 0).edit().clear().putInt(MagneTankActivity.SHARED_PREFS_ACTIVITY_START_COUNT, ActivityStartCount).putInt(MagneTankActivity.SHARED_PREFS_MUSIC_MUTED, MusicMuted).putBoolean(MagneTankActivity.SHARED_PREFS_HIGH_QUALITY_GRAPHICS, Quality).putInt(MagneTankActivity.SHARED_PREFS_RATING_SUCCESS, MarketRated).putInt(MagneTankActivity.SHARED_PREFS_SOUNDS_MUTED, SoundMuted).apply();
								MainMenu.getInstance().RefreshLevelStars();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, final int id) {
								
							}
						}).setCancelable(false);
						final AlertDialog alert = builder.create();
						alert.show();
					}
				});
			}
		};
		this.ResetText.setScales(Math.min(390f / this.ResetText.getWidth(), 0.85f), Math.min(390f / this.ResetText.getWidth(), 0.85f) * 1.2f);
		this.ResetText.setColor(0.212f, 0.275f, 0.212f);
		this.LayerBG.attachChild(this.ResetText);
		this.registerTouchArea(this.ResetText);
		
		final GrowButton BackToTitleButton = new GrowButton(0f, 0f, ResourceManager.menuArrow2TR) {
			@Override
			public void onClick() {
				OptionsLayer.this.onHideLayer();
			}
		};
		BackToTitleButton.setFlippedHorizontal(true);
		this.LayerBG.attachChild(BackToTitleButton);
		BackToTitleButton.setPosition(256f, 66f);
		this.registerTouchArea(BackToTitleButton);
		
		this.setPosition(ResourceManager.getInstance().cameraWidth / 2f, ResourceManager.getInstance().cameraHeight / 2f);
	}
	
	@Override
	public void onShowLayer() {
		// Register the slide-in animation with the engine.
		ResourceManager.getInstance().engine.registerUpdateHandler(this.mSlideInUpdateHandler);
	}
	
	@Override
	public void onUnloadLayer() {
		
	}
}