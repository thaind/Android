package ifl.games.runtime.GameLevels;

import ifl.games.runtime.MagneTankSmoothCamera;
import ifl.games.runtime.ManagedScene;
import ifl.games.runtime.SwitchableFixedStepEngine;
import ifl.games.runtime.Managers.ResourceManager;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.adt.align.HorizontalAlign;

/** The idea behind using loading steps is the same as showing a loading
 *  screen for one frame before loading the game, much like how the
 *  SceneManager class functions when showing a new scene, but the loading
 *  screen is updated for each loading step instead of just once at the first
 *  showing of the loading screen.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class ManagedGameScene extends ManagedScene {
	
	private static final String mFIRST_LOADING_STEP_TEXT = "Loading level...";
	private static final float mLOADING_STEP_TEXT_COLOR_RED = 1f;
	private static final float mLOADING_STEP_TEXT_COLOR_GREEN = 1f;
	private static final float mLOADING_STEP_TEXT_COLOR_BLUE = 1f;
	private static final Font mLOADING_STEP_TEXT_FONT = ResourceManager.fontDefault32Bold;
	private static final int mLOADING_STEP_TEXT_MAXIMUM_LENGTH = 300;
	
	private static final float mLOADING_PROGRESS_BAR_COLOR_BLUE = 1f;
	private static final float mLOADING_PROGRESS_BAR_COLOR_GREEN = 1f;
	private static final float mLOADING_PROGRESS_BAR_COLOR_RED = 1f;
	private static final float mLOADING_PROGRESS_BAR_WIDTH = 10f;
	
	private static final float mLOADING_SCREEN_BACKGROUND_COLOR_RED = 0.1f;
	private static final float mLOADING_SCREEN_BACKGROUND_COLOR_GREEN = 0.1f;
	private static final float mLOADING_SCREEN_BACKGROUND_COLOR_BLUE = 0.1f;
	
	public Text mLoadingText;
	private Rectangle mLoadingRect;
	private Scene mLoadingScene;
	
	ArrayList<LoadingRunnable> mLoadingSteps = new ArrayList<LoadingRunnable>();
	public float mLoadingStepsTotal = 0;
	
	public IUpdateHandler mLoadingStepsHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(!ManagedGameScene.this.mLoadingSteps.isEmpty()) {
				ManagedGameScene.this.mLoadingSteps.get(0).run();
				ManagedGameScene.this.mLoadingSteps.remove(0);
				ManagedGameScene.this.mLoadingRect.setWidth(ResourceManager.getInstance().cameraWidth * (1f - (ManagedGameScene.this.mLoadingSteps.size() / ManagedGameScene.this.mLoadingStepsTotal)));
				if(ManagedGameScene.this.mLoadingSteps.isEmpty()) {
					ManagedGameScene.this.isLoaded = true;
					ResourceManager.getInstance().engine.unregisterUpdateHandler(this);
					return;
				}
				ManagedGameScene.this.isLoaded = false;
			}
		}
		@Override
		public void reset() {}
	};
	
	public void addLoadingStep(final LoadingRunnable pLoadingStep) {
		this.mLoadingSteps.add(pLoadingStep);
		this.mLoadingStepsTotal++;
	}
	
	public ManagedGameScene() {
		this(0.01f);
	}
	
	public ManagedGameScene(final float pLoadingScreenMinimumSecondsShown) {
		super(pLoadingScreenMinimumSecondsShown);
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
	}
	
	@Override
	public void onHideScene() {}
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		((SwitchableFixedStepEngine) ResourceManager.getEngine()).DisableFixedStep();
		MagneTankSmoothCamera.setupForMenus();
		this.mLoadingScene = new Scene();
		this.mLoadingScene.setBackgroundEnabled(true);
		this.mLoadingScene.setBackground(new Background(mLOADING_SCREEN_BACKGROUND_COLOR_RED, mLOADING_SCREEN_BACKGROUND_COLOR_GREEN, mLOADING_SCREEN_BACKGROUND_COLOR_BLUE));
		this.mLoadingText = new Text(0, 0, mLOADING_STEP_TEXT_FONT, mFIRST_LOADING_STEP_TEXT, mLOADING_STEP_TEXT_MAXIMUM_LENGTH, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		this.mLoadingText.setAnchorCenter(0f, 1f);
		this.mLoadingText.setPosition(0f, ResourceManager.getInstance().cameraHeight);
		this.mLoadingText.setScale(0.75f);
		this.mLoadingText.setHorizontalAlign(HorizontalAlign.LEFT);
		this.mLoadingText.setColor(mLOADING_STEP_TEXT_COLOR_RED, mLOADING_STEP_TEXT_COLOR_GREEN, mLOADING_STEP_TEXT_COLOR_BLUE);
		this.mLoadingScene.attachChild(this.mLoadingText);
		this.mLoadingRect = new Rectangle(0f, 0f, 0f, mLOADING_PROGRESS_BAR_WIDTH, ResourceManager.getEngine().getVertexBufferObjectManager());
		this.mLoadingRect.setAnchorCenter(0f, 0f);
		this.mLoadingRect.setColor(mLOADING_PROGRESS_BAR_COLOR_RED, mLOADING_PROGRESS_BAR_COLOR_GREEN, mLOADING_PROGRESS_BAR_COLOR_BLUE);
		this.mLoadingScene.attachChild(this.mLoadingRect);
		return this.mLoadingScene;
	}
	
	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// detach & dispose of the the loading screen resources.
		ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				ManagedGameScene.this.mLoadingText.detachSelf();
				ManagedGameScene.this.mLoadingRect.detachSelf();
				ManagedGameScene.this.mLoadingText.dispose();
				ManagedGameScene.this.mLoadingRect.dispose();
				ManagedGameScene.this.mLoadingScene.dispose();
				ManagedGameScene.this.mLoadingText = null;
				ManagedGameScene.this.mLoadingRect = null;
				ManagedGameScene.this.mLoadingScene = null;
			}
		});
	}
	
	public abstract void onLoadLevel();
	
	@Override
	public void onLoadScene() {
		ResourceManager.loadGameResources();
		
		this.willHandle_isLoaded = false;
		ResourceManager.getInstance().engine.registerUpdateHandler(this.mLoadingStepsHandler);
		this.onLoadLevel();
	}
	
	@Override
	public void onShowScene() {}
	
	@Override
	public void onUnloadScene() {
		// detach and unload the scene.
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				ManagedGameScene.this.detachChildren();
				ManagedGameScene.this.clearEntityModifiers();
				ManagedGameScene.this.clearTouchAreas();
				ManagedGameScene.this.clearUpdateHandlers();
			}
		});
	}
}