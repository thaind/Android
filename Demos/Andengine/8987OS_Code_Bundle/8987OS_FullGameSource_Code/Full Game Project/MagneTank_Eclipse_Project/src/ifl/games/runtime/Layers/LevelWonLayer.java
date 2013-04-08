package ifl.games.runtime.Layers;

import ifl.games.runtime.MagneTankActivity;
import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.GameLevels.Levels;
import ifl.games.runtime.Input.GrowButton;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SceneManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;

/** The LevelWonLayer class represents the layer that is shown to the player
 *  when a level is completed successfully. It displays the current level
 *  number, score, and high-score as well as the star rating that the player
 *  received. It also includes buttons to go back to the level-select screen,
 *  replay the level, or go on to the next level.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class LevelWonLayer extends ManagedLayer {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final LevelWonLayer INSTANCE = new LevelWonLayer();
	
	// ====================================================
	// INSTANCE GETTERS
	// ====================================================
	public static LevelWonLayer getInstance() {
		return INSTANCE;
	}
	
	public static LevelWonLayer getInstance(final GameLevel pCurrentLevel) {
		INSTANCE.setCurrentLevel(pCurrentLevel);
		return INSTANCE;
	}
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private GameLevel mCurrentLevel;
	private Sprite mLayerBG;
	private boolean mIsGoingToNextLevel = false;
	private boolean mIsGoingToRestartLevel = false;
	private TiledSprite mStarsTiledSprite;
	private Text mHighScoreText;
	
	private Text mMainText;
	private Text mScoreText;
	
	// ====================================================
	// UPDATE HANDLERS
	// ====================================================
	// Animates the layer to slide in from the top.
	IUpdateHandler mSlideInUpdateHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(LevelWonLayer.this.mLayerBG.getY() > 0f) {
				LevelWonLayer.this.mLayerBG.setY(Math.max(LevelWonLayer.this.mLayerBG.getY() - (pSecondsElapsed * ManagedLayer.mSLIDE_PIXELS_PER_SECONDS), 0f));
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
			if(LevelWonLayer.this.mLayerBG.getY() < ((ResourceManager.getInstance().cameraHeight / 2f) + (LevelWonLayer.this.mLayerBG.getHeight() / 2f))) {
				LevelWonLayer.this.mLayerBG.setY(Math.min(LevelWonLayer.this.mLayerBG.getY() + (pSecondsElapsed * ManagedLayer.mSLIDE_PIXELS_PER_SECONDS), (ResourceManager.getInstance().cameraHeight / 2f) + (LevelWonLayer.this.mLayerBG.getHeight() / 2f)));
			} else {
				ResourceManager.getInstance().engine.unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
				if(LevelWonLayer.this.mIsGoingToRestartLevel) {
					LevelWonLayer.this.mCurrentLevel.restartLevel();
					return;
				} else if(LevelWonLayer.this.mIsGoingToNextLevel) {
					if(Levels.isNextLevelInCurrentWorld(LevelWonLayer.this.mCurrentLevel.mLevelDef)) {
						LevelWonLayer.this.mCurrentLevel.startNextLevel();
						return;
					}
				}
				LevelWonLayer.this.mCurrentLevel.disposeLevel();
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
		fadableBGRect.setColor(0f, 0f, 0f, 0.5f);
		this.attachChild(fadableBGRect);
		
		this.attachChild(this.mLayerBG = new Sprite(0f, (ResourceManager.getInstance().cameraHeight / 2f) + (ResourceManager.gameLevelLayerBGTR.getHeight() / 2f), ResourceManager.gameLevelLayerBGTR, ResourceManager.getActivity().getVertexBufferObjectManager()));
		this.mLayerBG.setScale(1.5f / ResourceManager.getInstance().cameraScaleFactorY);
		
		final GrowButton BackToLevelSelectButton = new GrowButton(128.2f, 75f, ResourceManager.gameLevelLayerButtonLevelSelectTR) {
			@Override
			public void onClick() {
				LevelWonLayer.this.mIsGoingToRestartLevel = false;
				LevelWonLayer.this.mIsGoingToNextLevel = false;
				LevelWonLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(BackToLevelSelectButton);
		this.registerTouchArea(BackToLevelSelectButton);
		
		final GrowButton RestartLevelButton = new GrowButton(256f, 75f, ResourceManager.gameLevelLayerButtonRestartLevelTR) {
			@Override
			public void onClick() {
				LevelWonLayer.this.mIsGoingToRestartLevel = true;
				LevelWonLayer.this.mIsGoingToNextLevel = false;
				LevelWonLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(RestartLevelButton);
		this.registerTouchArea(RestartLevelButton);
		
		final GrowButton NextLevelButton = new GrowButton(383f, 75f, ResourceManager.gameLevelLayerButtonNextLevelTR) {
			@Override
			public void onClick() {
				LevelWonLayer.this.mIsGoingToRestartLevel = false;
				LevelWonLayer.this.mIsGoingToNextLevel = true;
				LevelWonLayer.this.onHideLayer();
			}
		};
		this.mLayerBG.attachChild(NextLevelButton);
		this.registerTouchArea(NextLevelButton);
		
		this.mLayerBG.attachChild(new Sprite(365f, 148f, ResourceManager.gameLevelLayerStarsBGTR, ResourceManager.getActivity().getVertexBufferObjectManager()));
		this.mLayerBG.attachChild(this.mStarsTiledSprite = new TiledSprite(365f, 148f, ResourceManager.gameLevelLayerStarsTTR, ResourceManager.getActivity().getVertexBufferObjectManager()));
		
		this.mMainText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "LEVEL *** SUCCESS!", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mMainText.setScale(Math.min(390f / this.mMainText.getWidth(), 1f));
		this.mMainText.setPosition(256f, 205f);
		this.mMainText.setColor(0.31f, 0.35f, 0.31f);
		this.mLayerBG.attachChild(this.mMainText);
		
		this.mScoreText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "SCORE: *****", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mScoreText.setAnchorCenterX(0f);
		this.mScoreText.setScale(Math.min(203f / this.mScoreText.getWidth(), 1f));
		this.mScoreText.setPosition(80f, 155f);
		this.mScoreText.setColor(0.31f, 0.35f, 0.31f);
		this.mLayerBG.attachChild(this.mScoreText);
		
		this.mHighScoreText = new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "HIGHSCORE: *****", ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mHighScoreText.setAnchorCenterX(0f);
		this.mHighScoreText.setScale(Math.min(203f / this.mHighScoreText.getWidth(), 1f));
		this.mHighScoreText.setPosition(80f, 128f);
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
		
		this.mCurrentLevel.mMagneTank.mTurretMagnetOn = false;
		
		// Set the title of the layer
		this.mMainText.setText("LEVEL " + this.mCurrentLevel.mLevelDef.mLevelIndex + " SUCCESS!");
		this.mMainText.setScale(Math.min(390f / this.mMainText.getWidth(), 1f));
		
		// Show the current level's score
		for(final TiledSprite curCrate : this.mCurrentLevel.mRemainingCratesBar.mCrates) {
			curCrate.setAnchorCenter(0.5f, 0.5f);
			curCrate.setPosition(curCrate.getX() + (curCrate.getWidth() / 2f), curCrate.getY() + (curCrate.getHeight() / 2f));
			this.mCurrentLevel.addPointsToScore(curCrate, GameLevel.mCRATE_POINT_VALUE);
		}
		this.mScoreText.setText("SCORE: " + this.mCurrentLevel.CurrentScore);
		this.mScoreText.setScale(Math.min(203f / this.mScoreText.getWidth(), 1f));
		
		// Show the highscore (or the current score if it is the highscore)
		final int PreviousHighScore = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_HIGHSCORE + this.mCurrentLevel.mLevelDef.mLevelIndex);
		if(PreviousHighScore >= this.mCurrentLevel.CurrentScore) {
			this.mHighScoreText.setText("HIGHSCORE: " + PreviousHighScore);
		} else {
			MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_HIGHSCORE + this.mCurrentLevel.mLevelDef.mLevelIndex, this.mCurrentLevel.CurrentScore);
			this.mHighScoreText.setText("HIGHSCORE: " + this.mCurrentLevel.CurrentScore);
		}
		this.mHighScoreText.setScale(Math.min(203f / this.mHighScoreText.getWidth(), 1f));
		
		// Show the appropriate number of stars
		final int NumStars = (int) Math.min((this.mCurrentLevel.CurrentScore / (this.mCurrentLevel.TotalScorePossible * 0.9f)) * (this.mStarsTiledSprite.getTileCount()), (this.mStarsTiledSprite.getTileCount() - 1));
		this.mStarsTiledSprite.setCurrentTileIndex(NumStars);
		if(NumStars > MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_STARS + this.mCurrentLevel.mLevelDef.mLevelIndex)) {
			MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_STARS + this.mCurrentLevel.mLevelDef.mLevelIndex, NumStars);
		}
		this.mStarsTiledSprite.registerEntityModifier(new ScaleModifier(0.5f, 25f, 1f));
		this.mStarsTiledSprite.registerEntityModifier(new AlphaModifier(0.5f, 0f, 1f));
		
		// Make the next level reachable
		final int currentMaxLevel = MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_MAX_REACHED);
		if(currentMaxLevel < this.mCurrentLevel.mLevelDef.mLevelIndex) {
			MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_MAX_REACHED, this.mCurrentLevel.mLevelDef.mLevelIndex);
		}
	}
	
	@Override
	public void onUnloadLayer() {}
	
	public void setCurrentLevel(final GameLevel pCurrentLevel) {
		this.mCurrentLevel = pCurrentLevel;
	}
}