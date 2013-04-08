package ifl.games.runtime.Menus;

import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SceneManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.content.Intent;
import android.net.Uri;

/** The SplashScreen class uses entity modifiers and resolution-independent
 *  positioning to show the splash screens of the game. Each logo is
 *  clickable and starts an Intent related to the logo.
 *  
 *  This class is unique because it is seen only at the beginning of the game.
 *  After it is hidden, it's never used again. It also has to load as quickly
 *  as possible, and unload without a noticeable lag from garbage collection.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class SplashScreens extends ManagedSplashScreen {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mEachAnimationDuration = 0.25f;
	private static final float mEachAnimationPauseDuration = 2.2f;
	private static final float mEachScaleToSize = 0.7f * ResourceManager.getInstance().cameraScaleFactorY;
	private static final float mInfoBarSpriteYShown = -(ResourceManager.getInstance().cameraHeight) / 2f;
	private static final float mInfoBarSpriteYHidden = -((ResourceManager.getInstance().cameraHeight) / 2f) - (94f + 54f);
	
	private static final BitmapTextureAtlas mInfoBarTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 1, 36, TextureOptions.NEAREST);
	private static final ITextureRegion mInfoBarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mInfoBarTexture, ResourceManager.getContext(), "gfx/Splash/AboutBar.png", 0, 0);
	private static final Sprite mInfoBarSprite = new Sprite(0f, mInfoBarSpriteYHidden, mInfoBarTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager());
	static {
		mInfoBarSprite.setAnchorCenter(0f, 0f);
		mInfoBarSprite.setWidth(ResourceManager.getInstance().cameraWidth);
		mInfoBarSprite.setHeight(72f);
		mInfoBarSprite.setAlpha(0.85f);
	}
	
	private static final Text mDisplayInfoText1 = new Text(8f, mInfoBarSprite.getHeight() / 2f, ResourceManager.fontDefault72Bold, "follow @ifl", 100, ResourceManager.getEngine().getVertexBufferObjectManager());
	private static final Text mDisplayInfoText2 = new Text(mInfoBarSprite.getWidth() - 8f, mInfoBarSprite.getHeight() / 2f, ResourceManager.fontDefault72Bold, "Games & Graphics Development", 100, ResourceManager.getEngine().getVertexBufferObjectManager());
	private static final Text mDisplayInfoText3 = new Text(mInfoBarSprite.getWidth() / 2f, mInfoBarSprite.getHeight() + 36f, ResourceManager.fontDefault72Bold, "Copyright © 2010-2012 IFL Game Studio", 100, ResourceManager.getEngine().getVertexBufferObjectManager());
	static {
		mDisplayInfoText1.setAnchorCenter(0f, 0.5f);
		mDisplayInfoText1.setHorizontalAlign(HorizontalAlign.LEFT);
		mInfoBarSprite.attachChild(SplashScreens.mDisplayInfoText1);
		mDisplayInfoText1.setColor(1f, 1f, 1f, 0.75f);
		mDisplayInfoText1.setScale(0.444f);
		
		mDisplayInfoText2.setAnchorCenter(1f, 0.5f);
		mDisplayInfoText2.setHorizontalAlign(HorizontalAlign.RIGHT);
		mInfoBarSprite.attachChild(SplashScreens.mDisplayInfoText2);
		mDisplayInfoText2.setColor(1f, 1f, 1f, 0.75f);
		mDisplayInfoText2.setScale(0.444f);
		
		mInfoBarSprite.attachChild(SplashScreens.mDisplayInfoText3);
		mDisplayInfoText3.setColor(1f, 1f, 1f, 0.75f);
		mDisplayInfoText3.setScale(0.444f);
	}
	
	private static final BitmapTextureAtlas mIFLLogoTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 512, 472, TextureOptions.BILINEAR);
	private static final ITextureRegion mIFLLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mIFLLogoTexture, ResourceManager.getContext(), "gfx/Splash/IFL_logo.png", 0, 0);
	private static final Sprite mIFLLogoSprite = new Sprite((ResourceManager.getInstance().cameraWidth) / 2f, 0f, mIFLLogoTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager()) {
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()) {
				ResourceManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/ifl"));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ResourceManager.getContext().startActivity(browserIntent);
					}
				});
			}
			return false;
		}
	};
	
	private static final BitmapTextureAtlas mAndEngineLogoTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 512, 512, TextureOptions.BILINEAR);
	private static final ITextureRegion mAndEngineLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAndEngineLogoTexture, ResourceManager.getContext(), "gfx/Splash/AE_logo.png", 0, 0);
	private static final Sprite mAndEngineLogoSprite = new Sprite(SplashScreens.mIFLLogoSprite.getX(), 0f, mAndEngineLogoTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager()) {
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()) {
				ResourceManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.andengine.org/"));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ResourceManager.getContext().startActivity(browserIntent);
					}
				});
			}
			return false;
		}
	};
	
	private static final BitmapTextureAtlas mAndEngineBookLogoTexture = new BitmapTextureAtlas(ResourceManager.getEngine().getTextureManager(), 512, 189, TextureOptions.BILINEAR);
	private static final ITextureRegion mAndEngineBookLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAndEngineBookLogoTexture, ResourceManager.getContext(), "gfx/Splash/AE_Book.png", 0, 0);
	private static final Sprite mAndEngineBookLogoSprite = new Sprite(SplashScreens.mIFLLogoSprite.getX(), 0f, mAndEngineBookLogoTextureRegion, ResourceManager.getEngine().getVertexBufferObjectManager()) {
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			if(pSceneTouchEvent.isActionDown()) {
				ResourceManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.packtpub.com/andengine-for-android-game-development-cookbook/book"));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ResourceManager.getContext().startActivity(browserIntent);
					}
				});
			}
			return false;
		}
	};
	
	private static final SequenceEntityModifier mInfoBar_SequenceEntityModifier = new SequenceEntityModifier(new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYHidden, 0f, mInfoBarSpriteYShown), new DelayModifier(mEachAnimationPauseDuration), new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYShown, 0f, mInfoBarSpriteYHidden), new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYHidden, 0f, mInfoBarSpriteYShown), new DelayModifier(mEachAnimationPauseDuration), new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYShown, 0f, mInfoBarSpriteYHidden), new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYHidden, 0f, mInfoBarSpriteYShown), new DelayModifier(mEachAnimationPauseDuration * 2f), new MoveModifier(mEachAnimationDuration, 0f, mInfoBarSpriteYShown, 0f, mInfoBarSpriteYHidden));
	private static final SequenceEntityModifier mIFLLogo_SequenceEntityModifier = new SequenceEntityModifier(new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, 25f, mEachScaleToSize, 0.5f, 0.5f), new FadeInModifier(mEachAnimationDuration)), new DelayModifier(mEachAnimationPauseDuration), new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, mEachScaleToSize, 0f, 0.5f, 0.5f), new FadeOutModifier(mEachAnimationDuration)));
	private static final SequenceEntityModifier mAndEngineLogo_SequenceEntityModifier = new SequenceEntityModifier(new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, 25f, mEachScaleToSize, 0.5f, 0.5f), new FadeInModifier(mEachAnimationDuration)), new DelayModifier(mEachAnimationPauseDuration), new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, mEachScaleToSize, 0f, 0.5f, 0.5f), new FadeOutModifier(mEachAnimationDuration)));
	private static final SequenceEntityModifier mAndEngineBookLogo_SequenceEntityModifier = new SequenceEntityModifier(new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, 25f, 1f, 0.5f, 0.5f), new FadeInModifier(mEachAnimationDuration)), new DelayModifier(mEachAnimationPauseDuration * 2f), new ParallelEntityModifier(new ScaleAtModifier(mEachAnimationDuration, 1f, 0f, 0.5f, 0.5f), new FadeOutModifier(mEachAnimationDuration)));
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void onLoadScene() {
		mInfoBarTexture.load();
		mIFLLogoTexture.load();
		mAndEngineLogoTexture.load();
		mAndEngineBookLogoTexture.load();
		ResourceManager.getCamera().setCenterDirect(ResourceManager.getInstance().cameraWidth / 2f, ResourceManager.getInstance().cameraHeight / 2f);
		
		this.setBackgroundEnabled(true);
		this.setBackground(new Background(0.1f, 0.1f, 0.1f));
		
		this.attachChild(SplashScreens.mInfoBarSprite);
		
		mIFLLogoSprite.setAlpha(0.001f);
		this.attachChild(SplashScreens.mIFLLogoSprite);
		this.registerTouchArea(SplashScreens.mIFLLogoSprite);
		
		SplashScreens.mAndEngineLogoSprite.setScale(0.01f);
		SplashScreens.mAndEngineLogoSprite.setAlpha(0.001f);
		this.attachChild(SplashScreens.mAndEngineLogoSprite);
		this.registerTouchArea(SplashScreens.mAndEngineLogoSprite);
		
		SplashScreens.mAndEngineBookLogoSprite.setScale(0.01f);
		SplashScreens.mAndEngineBookLogoSprite.setAlpha(0.001f);
		this.attachChild(SplashScreens.mAndEngineBookLogoSprite);
		this.registerTouchArea(SplashScreens.mAndEngineBookLogoSprite);
		
		SplashScreens.mIFLLogo_SequenceEntityModifier.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				SplashScreens.mAndEngineLogoSprite.registerEntityModifier(SplashScreens.mAndEngineLogo_SequenceEntityModifier);
				SplashScreens.mDisplayInfoText1.setText("http://www.andengine.org/");
				SplashScreens.mDisplayInfoText2.setText("ANDROID 2D GAME ENGINE");
				SplashScreens.mDisplayInfoText3.setText("Copyright © 2010 Nicolas Gramlich, 2011 Zynga Inc.");
			}
			
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
		});
		SplashScreens.mAndEngineLogo_SequenceEntityModifier.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				SplashScreens.mAndEngineBookLogoSprite.registerEntityModifier(SplashScreens.mAndEngineBookLogo_SequenceEntityModifier);
				SplashScreens.mDisplayInfoText1.setText("| Amazon | Barnes&Noble | Safari |");
				SplashScreens.mDisplayInfoText2.setText("PACKT PUBLISHING");
				SplashScreens.mDisplayInfoText3.setText("INCLUDES SOURCE CODE FOR THIS GAME");
			}
			
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
		});
		SplashScreens.mAndEngineBookLogo_SequenceEntityModifier.addModifierListener(new IModifierListener<IEntity>() {
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
				SplashScreens.mInfoBarSprite.clearEntityModifiers();
				SceneManager.getInstance().showMainMenu();
				ResourceManager.getActivity().showAds();
			}
			
			@Override
			public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
		});
		
		this.registerUpdateHandler(new IUpdateHandler() {
			int counter = 0;
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				this.counter++;
				if(this.counter > 2) {
					SplashScreens.mIFLLogoSprite.registerEntityModifier(SplashScreens.mIFLLogo_SequenceEntityModifier);
					SplashScreens.mInfoBarSprite.registerEntityModifier(SplashScreens.mInfoBar_SequenceEntityModifier);
					SplashScreens.this.thisManagedSplashScene.unregisterUpdateHandler(this);
				}
			}
			
			@Override
			public void reset() {}
		});
	}
	
	@Override
	public void unloadSplashTextures() {
		mIFLLogoTexture.unload();
		mAndEngineLogoTexture.unload();
		mAndEngineBookLogoTexture.unload();
	}
	
}