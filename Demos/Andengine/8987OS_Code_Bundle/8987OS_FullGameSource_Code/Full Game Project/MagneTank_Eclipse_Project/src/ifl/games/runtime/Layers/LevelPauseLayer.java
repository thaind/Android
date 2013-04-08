package ifl.games.runtime.Layers;

import ifl.games.runtime.MagneTankActivity;
import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.GameLevels.Levels;
import ifl.games.runtime.Input.GrowButton;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SceneManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

/** The LevelPauseLayer class represents the layer that is shown to the
 *  player when a level is paused. It displays the current level number,
 *  score, and high-score as well as buttons to go back to the game, back
 *  to the level-select screen, restart the level, or skip to the next level.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class LevelPauseLayer extends ManagedLayer {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final LevelPauseLayer INSTANCE = new LevelPauseLayer();
	
	// ====================================================
	// INSTANCE GETTERS
	// ====================================================
	public static LevelPauseLayer getInstance() {
		return INSTANCE;
	}
	
	public static LevelPauseLayer getInstance(final GameLevel pCurrentLevel) {
		INSTANCE.setCurrentLevel(pCurrentLevel);
		return INSTANCE;
	}
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private GameLevel mCurrentLevel;
	private Sprite mLayerBG;
	private boolean mIsGoingToRestartLevel = false;
	private boolean mIsGoingToNextLevel = false;
	private boolean mIsGoingBackToLevel = true;
	private GrowButton mNextLevelButton;
	private Text mMainText;
	private Text mScoreText;
	private Text mHighScoreText;
	
	// ====================================================
	// UPDATE HANDLERS
	// ====================================================
	// Animates the layer to slide in from the top.
	IUpdateHandler mSlideInUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(LevelPauseLayer.this.mLayerBG.getY() > 0f) {
				LevelPauseLayer.this.mLayerBG.setY(Math.max(LevelPauseLayer.this.mLayerBG.getY() - (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), 0f));
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
			if(LevelPauseLayer.this.mLayerBG.getY() < ((ResourceManager.getInstance().cameraHeight / 2f) + (LevelPauseLayer.this.mLayerBG.getHeight() / 2f))) {
				LevelPauseLayer.this.mLayerBG.setY(Math.min(LevelPauseLayer.this.mLayerBG.getY() + (pSecondsElapsed * mSLIDE_PIXELS_PER_SECONDS), (ResourceManager.getInstance().cameraHeight / 2f) + (LevelPauseLayer.this.mLayerBG.getHeight() / 2f)));
			} else {
				ResourceManager.getInstance().engine.unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
				if(LevelPauseLayer.this.mIsGoingToRestartLevel) {
					LevelPauseLayer.this.mCurrentLevel.restartLevel();
					return;
				} else if(LevelPauseLayer.this.mIsGoingToNextLevel) {
					if(Levels.isNextLevelInCurrentWorld(LevelPauseLayer.this.mCurrentLevel.mLevelDef)) {
						LevelPauseLayer.this.mCurrentLevel.startNextLevel();
						return;
					}
				} else if(LevelPauseLayer.this.mIsGoingBackToLevel) {
					return;
				}
				LevelPauseLayer.this.mCurrentLevel.disposeLevel();
				SceneManager.getInstance().showMainMenu();
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
		
		this.mLayerBG = new Sprite(0f, (ResourceManager.getInstance().cameraHeight / 2f) + (ResourceManager.gameLevelLayerBGTR.getHeight() / 2f), ResourceManager.gameLevelLayerBGTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.attachChild(this.mLayerBG);
		this.mLayerBG.setScale(1.5f / ResourceManager.getInstance().cameraScaleFactorY);
		
		final GrowButton ReturnToLevelButton = new GrowButton((-ResourceManager.getEngine().getCamera().getWidth() / 2f) + (ResourceManager.gamePauseButtonTR.getWidth() / 2f), (ResourceManager.getEngine().getCamera().getHeight() / 2f) - (ResourceManager.gamePauseButtonTR.getHeight() / 2f), ResourceManager.gamePauseButtonTR) {
			@Override
			public void onClick() {
				LevelPauseLayer.this.mIsGoingToRestartLevel = false;
				LevelPauseLayer.this.mIsGoingToNextLevel = false;
				LevelPauseLayer.this.mIsGoingBackToLevel = true;
				LevelPauseLayer.this.onHideLayer();
			}
		};
		this.attachChild(ReturnToLevelButton);
		this.registerTouchArea(ReturnToLevelButton);
		
		final GrowButton BackToLevelSelectButton = new GrowButton(128.2f, 75f, ResourceManager.gameLevelLayerButtonLevelSelectTR) {
			@Override
			public void onClick() {
				LevelPauseLayer.this.mIsGoingToRestartLevel = false;
				LevelPauseLayer.this.mIsGoingToNextLevel = false;
				LevelPauseLayer.this.mIsGoingBackToLevel = false;
				LevelPauseLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(BackToLevelSelectButton);
		this.registerTouchArea(BackToLevelSelectButton);
		
		final GrowButton RestartLevelButton = new GrowButton(256f, 75f, ResourceManager.gameLevelLayerButtonRestartLevelTR) {
			@Override
			public void onClick() {
				LevelPauseLayer.this.mIsGoingToRestartLevel = true;
				LevelPauseLayer.this.mIsGoingToNextLevel = false;
				LevelPauseLayer.this.mIsGoingBackToLevel = false;
				LevelPauseLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(RestartLevelButton);
		this.registerTouchArea(RestartLevelButton);
		
		this.mNextLevelButton = new GrowButton(383f, 75f, ResourceManager.gameLevelLayerButtonNextLevelTR) {
			@Override
			public void onClick() {
				LevelPauseLayer.this.mIsGoingToRestartLevel = false;
				LevelPauseLayer.this.mIsGoingToNextLevel = true;
				LevelPauseLayer.this.mIsGoingBackToLevel = false;
				LevelPauseLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(this.mNextLevelButton);
		this.registerTouchArea(this.mNextLevelButton);
		
		this.mMainText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "LEVEL *** PAUSED", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mMainText.setScale(Math.min(390f / this.mMainText.getWidth(), 1f));
		this.mMainText.setPosition(256f, 205f);
		this.mMainText.setColor(0.31f, 0.35f, 0.31f);
		this.mLayerBG.attachChild(this.mMainText);
		
		this.mScoreText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "CURRENT SCORE: ******", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mScoreText.setScale(Math.min(352f / this.mScoreText.getWidth(), 1f));
		this.mScoreText.setPosition(256f, 155f);
		this.mScoreText.setColor(0.31f, 0.35f, 0.31f);
		this.mLayerBG.attachChild(this.mScoreText);
		
		this.mHighScoreText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "HIGHSCORE: ******", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mHighScoreText.setScale(Math.min(352f / this.mHighScoreText.getWidth(), 1f));
		this.mHighScoreText.setPosition(256f, 128f);
		this.mHighScoreText.setColor(0.31f, 0.35f, 0.31f);
		this.mLayerBG.attachChild(this.mHighScoreText);
		
		this.setPosition(ResourceManager.getInstance().cameraWidth / 2f, ResourceManager.getInstance().cameraHeight / 2f);
	}
	
	@Override
	public void onShowLayer() {
		// Register the slide-in animation with the engine.
		ResourceManager.getInstance().engine.registerUpdateHandler(this.mSlideInUpdateHandler);
		
		this.mIsGoingToRestartLevel = false;
		this.mIsGoingToNextLevel = false;
		this.mIsGoingBackToLevel = true;
		
		// Set the title of the layer
		this.mMainText.setText("LEVEL " + this.mCurrentLevel.mLevelDef.mLevelIndex + " PAUSED");
		this.mMainText.setScale(Math.min(390f / this.mMainText.getWidth(), 1f));
		
		// Show the current level's score
		this.mScoreText.setText("CURRENT SCORE: " + (this.mCurrentLevel.CurrentScore));
		this.mScoreText.setScale(Math.min(352f / this.mScoreText.getWidth(), 1f));
		
		// Show the highscore (or the current score if it is the highscore)
		final int PreviousHighScore = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_HIGHSCORE + this.mCurrentLevel.mLevelDef.mLevelIndex);
		this.mHighScoreText.setText("HIGHSCORE: " + PreviousHighScore);
		this.mHighScoreText.setScale(Math.min(352f / this.mHighScoreText.getWidth(), 1f));
		
		// Make the next level reachable
		final int currentMaxLevel = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_MAX_REACHED);
		if(currentMaxLevel >= this.mCurrentLevel.mLevelDef.mLevelIndex) {
			this.mNextLevelButton.mIsEnabled = true;
		} else {
			this.mNextLevelButton.mIsEnabled = false;
		}
	}
	
	@Override
	public void onUnloadLayer() {}
	
	public void setCurrentLevel(final GameLevel pCurrentLevel) {
		this.mCurrentLevel = pCurrentLevel;
	}
	
}