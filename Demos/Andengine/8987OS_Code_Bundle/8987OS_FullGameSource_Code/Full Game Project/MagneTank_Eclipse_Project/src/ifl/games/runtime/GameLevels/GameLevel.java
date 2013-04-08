package ifl.games.runtime.GameLevels;

import ifl.games.runtime.MagneTankSmoothCamera;
import ifl.games.runtime.SwitchableFixedStepEngine;
import ifl.games.runtime.GameLevels.Levels.BeamsInLevelDef;
import ifl.games.runtime.GameLevels.Levels.EnemiesInLevelDef;
import ifl.games.runtime.GameLevels.Levels.LevelDef;
import ifl.games.runtime.GameLevels.Elements.BouncingPowerBar;
import ifl.games.runtime.GameLevels.Elements.MagneTank;
import ifl.games.runtime.GameLevels.Elements.MagneticCrate.MagneticCrateDef;
import ifl.games.runtime.GameLevels.Elements.MagneticOrb;
import ifl.games.runtime.GameLevels.Elements.MagneticPhysObject;
import ifl.games.runtime.GameLevels.Elements.MechRat;
import ifl.games.runtime.GameLevels.Elements.MetalBeamDynamic;
import ifl.games.runtime.GameLevels.Elements.MetalBeamStatic;
import ifl.games.runtime.GameLevels.Elements.ParallaxLayer;
import ifl.games.runtime.GameLevels.Elements.ParallaxLayer.ParallaxEntity;
import ifl.games.runtime.GameLevels.Elements.PhysObject;
import ifl.games.runtime.GameLevels.Elements.RemainingCratesBar;
import ifl.games.runtime.GameLevels.Elements.TexturedBezierLandscape;
import ifl.games.runtime.GameLevels.Elements.WoodenBeamDynamic;
import ifl.games.runtime.Input.GrowButton;
import ifl.games.runtime.Input.GrowToggleButton;
import ifl.games.runtime.Layers.LevelPauseLayer;
import ifl.games.runtime.Layers.LevelWonLayer;
import ifl.games.runtime.Managers.GameManager;
import ifl.games.runtime.Managers.GameManager.GameLevelGoal;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;
import ifl.games.runtime.Managers.SceneManager;

import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseElasticOut;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/** The GameLevel class brings all of the other in-game classes together to
 *  form the playable part of MagneTank. It handles the construction and
 *  execution of each actual game level. It extends a customized
 *  ManagedGameScene that incorporates a list of LoadingRunnables, which
 *  create the level in steps that allow each progression of the level
 *  construction to be shown on the screen. The GameLevel class also
 *  determines the completion or failure of each game level using the
 *  GameManager class to test for win or lose conditions.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class GameLevel extends ManagedGameScene implements IOnSceneTouchListener, GameLevelGoal {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	
	public static final float mLEVEL_WIDTH = 7200f;
	public static final float mTRAILING_DOTS_SPACING = 64f;
	public static final int mCRATE_POINT_VALUE = 100;
	
	private static final float mPHYSICS_WORLD_GRAVITY = -SensorManager.GRAVITY_EARTH * 4f;
	private static final int mPHYSICS_WORLD_POSITION_ITERATIONS = 20;
	private static final int mPHYSICS_WORLD_VELOCITY_ITERATIONS = 20;
	private static final float mSKY_BACKGROUND_DETAIL_Y = 800f;
	private static final float mSKY_BACKGROUND_DETAIL_X = 1280f;
	private static final float mCAMERA_ZOOM = 0.333f;
	private static final float mSECONDS_FOR_LEVEL_TO_SETTLE = 1f;
	private static final float mBASE_MOVEMENT_SPEED_THRESHOLD = 1f;
	private static final float mBASE_MOVEMENT_TIME_THRESHOLD = 0.75f;
	private static final int mNUMBER_TRAILING_DOTS = 25;
	
	private static final String mLOADING_STEP_STRING_1 = "Painting the sky...";
	private static final String mLOADING_STEP_STRING_2 = "Moving dirt...";
	private static final String mLOADING_STEP_STRING_3 = "Cross-checking instruments...";
	private static final String mLOADING_STEP_STRING_4 = "Aquiring all your base...";
	private static final String mLOADING_STEP_STRING_5 = "Prepping ammo...";
	private static final String mLOADING_STEP_STRING_6 = "\"Clank clank\" it's a tank...";
	private static final String mLOADING_STEP_STRING_7 = "Enemy in sight...";
	private static final String mLEVEL_NUMBER_PRETEXT = "LEVEL ";
	private static final String mON_SCREEN_SCORE_PRETEXT = "SCORE: ";
	
	

	// ====================================================
	// VARIABLES
	// ====================================================
	public FixedStepPhysicsWorld mPhysicsWorld;
	
	public final LevelDef mLevelDef;
	public float mBasePositionX;
	public float mBasePositionY;
	public ArrayList<float[]> mBasePositions = new ArrayList<float[]>();
	public MagneTank mMagneTank;
	public ArrayList<MagneticPhysObject<?>> mMagneticObjects = new ArrayList<MagneticPhysObject<?>>();
	public int mNumberEnemiesLeft;
	public ArrayList<MagneticCrateDef> mCratesLeft = new ArrayList<MagneticCrateDef>();
	public Entity mCrateLayer = new Entity();
	public RemainingCratesBar mRemainingCratesBar;
	public boolean mIsLevelSettled = false;
	public float mBaseTotalMovementTime;
	public boolean mIsThereBaseMovement = false;
	public int TotalScorePossible;
	public int CurrentScore;
	
	private final MagneTankSmoothCamera mCamera = ResourceManager.getCamera();
	private static float SCALED_CAMERA_ZOOM = mCAMERA_ZOOM * ResourceManager.getInstance().cameraScaleFactorX;
	private boolean mHasCompletionTimerRun = false;
	private int mTrailingDotCounter;
	private Sprite[] mTrailingDotSprites = null;
	private Text ScoreText;
	
	// ====================================================
	// UPDATE HANDLERS
	// ====================================================
	public IUpdateHandler SettleHandlerUpdateHandler = new IUpdateHandler() {
		float mTotalElapsedTime = 0f;
		
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			this.mTotalElapsedTime += pSecondsElapsed;
			if(this.mTotalElapsedTime >= mSECONDS_FOR_LEVEL_TO_SETTLE) {
				GameLevel.this.mIsLevelSettled = true;
				GameLevel.this.mMagneTank.equipNextCrate(true);
				GameLevel.this.unregisterUpdateHandler(this);
			}
		}
		
		@Override
		public void reset() {}
	};
	
	public IUpdateHandler mMovementReportingTimer = new IUpdateHandler() {
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			if(GameLevel.this.mIsThereBaseMovement) {
				GameLevel.this.mBaseTotalMovementTime += pSecondsElapsed;
				if(GameLevel.this.mBaseTotalMovementTime >= mBASE_MOVEMENT_TIME_THRESHOLD)
					if(GameLevel.this.mIsThereBaseMovement)
						GameLevel.this.mIsThereBaseMovement = false;
			}
		}
		@Override public void reset() {}
	};
	
	public IUpdateHandler onCompletionTimer = new IUpdateHandler() {
		final float COMPLETION_SECONDS_DELAY = 3f;
		public float mTotalElapsedTime = 0f;
		
		@Override
		public void onUpdate(final float pSecondsElapsed) {
			this.mTotalElapsedTime += pSecondsElapsed;
			if(this.mTotalElapsedTime >= this.COMPLETION_SECONDS_DELAY) {
				GameLevel.this.mHasCompletionTimerRun = true;
				if(!GameLevel.this.mIsThereBaseMovement) {
					if(GameLevel.this.isLevelCompleted()) {
						GameLevel.this.onLevelCompleted();
					} else {
						GameLevel.this.onLevelFailed();
					}
					GameLevel.this.unregisterUpdateHandler(this);
				}
			}
		}
		@Override public void reset() {}
	};
	
	// ====================================================
	// OBJECT POOLS
	// ====================================================
	GenericPool<Text> ScoreTextPool = new GenericPool<Text>() {
		@Override
		protected Text onAllocatePoolItem() {
			return new Text(0f, 0f, ResourceManager.fontDefaultMagneTank48, "", 15, ResourceManager.getActivity().getVertexBufferObjectManager()) {
				Text ThisText = this;
				
				@Override
				public void onAttached() {
					this.setVisible(true);
					this.setAlpha(1f);
					this.setScale(4f);
					this.setRotation(MathUtils.random(-35f, 35f));
				}
				
				@Override
				protected void onManagedUpdate(final float pSecondsElapsed) {
					super.onManagedUpdate(pSecondsElapsed);
					this.setAlpha(this.getAlpha() - (pSecondsElapsed / 2f));
					this.setScale(this.getScaleX() - pSecondsElapsed);
					this.setRotation(this.getRotation() - (this.getRotation() * pSecondsElapsed * 2f));
					if(this.getAlpha() <= 0.1f) {
						this.setVisible(false);
						ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								ThisText.detachSelf();
								GameLevel.this.ScoreTextPool.recyclePoolItem(ThisText);
							}
						});
					}
				}
			};
		}
	};
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public GameLevel(final LevelDef pLevelDef) {
		this.mLevelDef = pLevelDef;
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void addPointsToScore(final IEntity pEntity, final int pPoints) {
		this.CurrentScore += pPoints;
		this.ScoreText.setText(mON_SCREEN_SCORE_PRETEXT.toString() + this.CurrentScore);
		final Text scorePopup = this.ScoreTextPool.obtainPoolItem();
		scorePopup.setText(String.valueOf(pPoints));
		scorePopup.setPosition(pEntity.getX(), pEntity.getY());
		if(pEntity.hasParent()) {
			pEntity.getParent().attachChild(scorePopup);
		} else {
			this.attachChild(scorePopup);
		}
	}
	
	public void createExplosion(final Vector2 pBombPos, final float pExplosionConstant) {
		final Iterator<Body> bodies = this.mPhysicsWorld.getBodies();
		while(bodies.hasNext()) {
			final Body b = bodies.next();
			if(b.getType() == BodyType.DynamicBody) {
				final Vector2 BodyPos = Vector2Pool.obtain(b.getWorldCenter());
				final Vector2 NormalizedDirectionFromBombToBody = Vector2Pool.obtain(BodyPos).sub(pBombPos).nor();
				final float dist = BodyPos.dst(pBombPos);
				final Vector2 ForceBasedOnDist = Vector2Pool.obtain(NormalizedDirectionFromBombToBody).mul(pExplosionConstant * (1f / dist));
				b.applyForce(ForceBasedOnDist, b.getWorldCenter());
				Vector2Pool.recycle(ForceBasedOnDist);
				Vector2Pool.recycle(NormalizedDirectionFromBombToBody);
				Vector2Pool.recycle(BodyPos);
			}
		}
	}
	
	public void disposeLevel() {
		this.mCamera.setChaseEntity(null);
		final HUD oldHUD = this.mCamera.getHUD();
		if(oldHUD != null) {
			oldHUD.detachSelf();
			oldHUD.dispose();
			this.mCamera.setHUD(null);
		}
		MagneTankSmoothCamera.setupForMenus();
	}
	
	public Sprite getLastTrailingDot() {
		if(this.mTrailingDotSprites == null) {
			this.resetTrailingDots();
		}
		if(this.mTrailingDotCounter == 0) {
			return this.mTrailingDotSprites[0];
		}
		return this.mTrailingDotSprites[this.mTrailingDotCounter - 1];
	}
	
	@Override
	public boolean isLevelCompleted() {
		return this.mNumberEnemiesLeft <= 0;
	}
	
	@Override
	public boolean isLevelFailed() {
		return ((this.mNumberEnemiesLeft > 0) && (this.mCratesLeft.size() == 0));
	}
	
	@Override
	public void onLevelCompleted() {
		if(this.mHasCompletionTimerRun) {
			// player won - show winning screen
			SceneManager.getInstance().showLayer(LevelWonLayer.getInstance(this), false, false, true);
		} else {
			GameLevel.this.registerUpdateHandler(this.onCompletionTimer);
		}
	}
	
	@Override
	public void onLevelFailed() {
		if(this.mHasCompletionTimerRun) {
			// player lost - restart level
			this.restartLevel();
		} else {
			GameLevel.this.registerUpdateHandler(this.onCompletionTimer);
		}
	}
	
	@Override
	public void onLoadLevel() {
		GameManager.setGameLevel(this);
		
		this.ScoreTextPool.batchAllocatePoolItems(8);
		
		this.mPhysicsWorld = new FixedStepPhysicsWorld(ResourceManager.getEngine().mStepsPerSecond, new Vector2(0f, mPHYSICS_WORLD_GRAVITY), true, mPHYSICS_WORLD_VELOCITY_ITERATIONS, mPHYSICS_WORLD_POSITION_ITERATIONS);
		this.registerUpdateHandler(this.mPhysicsWorld);
		
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_1, this) {
			@Override
			public void onLoad() {
				final CameraScene testscn1 = new CameraScene(ResourceManager.getEngine().getCamera());
				
				final Sprite Sky = new Sprite(0f, 0f, ResourceManager.gameSkyBackgroundTR, ResourceManager.getActivity().getVertexBufferObjectManager());
				Sky.setAnchorCenter(0f, 0f);
				Sky.setSize(mSKY_BACKGROUND_DETAIL_X, mSKY_BACKGROUND_DETAIL_Y);
				testscn1.attachChild(Sky);
				GameLevel.this.attachChild(testscn1);
				
				final ParallaxLayer BGParallaxLayer = new ParallaxLayer(GameLevel.this.mCamera, true);
				
				final Sprite BG4 = new Sprite(0f, 256f, ResourceManager.gameParallaxBackdrop4TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				BGParallaxLayer.attachParallaxEntity(new ParallaxEntity(0.25f, BG4, true));
				
				final Sprite BG3 = new Sprite(0f, 240f, ResourceManager.gameParallaxBackdrop3TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				BGParallaxLayer.attachParallaxEntity(new ParallaxEntity(0.5f, BG3, true));
				
				final Sprite BG2 = new Sprite(0f, 128f, ResourceManager.gameParallaxBackdrop2TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				BGParallaxLayer.attachParallaxEntity(new ParallaxEntity(0.75f, BG2, true));
				
				final Sprite BG1 = new Sprite(0f, 64f, ResourceManager.gameParallaxBackdrop1TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				BGParallaxLayer.attachParallaxEntity(new ParallaxEntity(1f, BG1, true));
				
				final ParallaxLayer CloudParallaxLayer = new ParallaxLayer(GameLevel.this.mCamera, true);
				CloudParallaxLayer.setParallaxChangePerSecond(-200f);
				
				final Sprite Cloud1 = new Sprite(0f, 400f, ResourceManager.gameCloud1TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				Cloud1.setScale(MathUtils.random(1.25f, 1.5f));
				Cloud1.setY((Cloud1.getScaleX() * 150f) + 250f);
				Cloud1.setAlpha(0.4f);
				CloudParallaxLayer.attachParallaxEntity(new ParallaxEntity(Cloud1.getScaleX() / 4f, Cloud1, false, MathUtils.random(4f, 6f)));
				
				final Sprite Cloud2 = new Sprite(MathUtils.random(128f, 256f), 300f, ResourceManager.gameCloud2TR, ResourceManager.getActivity().getVertexBufferObjectManager());
				Cloud2.setScale(MathUtils.random(0.75f, 1.25f));
				Cloud2.setY((Cloud2.getScaleX() * 150f) + 150f);
				Cloud2.setAlpha(0.5f);
				CloudParallaxLayer.attachParallaxEntity(new ParallaxEntity(Cloud2.getScaleX() / 4f, Cloud2, false, MathUtils.random(4f, 6f)));
				
				testscn1.attachChild(BGParallaxLayer);
				testscn1.attachChild(CloudParallaxLayer);
				testscn1.setScale(Math.max(ResourceManager.getInstance().cameraWidth / 1280f, ResourceManager.getInstance().cameraHeight / 800f));
			}
		});
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_2, this) {
			@Override
			public void onLoad() {
				new TexturedBezierLandscape(512f, GameLevel.mLEVEL_WIDTH - 512f, GameLevel.this.mLevelDef.mTerrainSlopes, GameLevel.this);
			}
		});
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_3, this) {
			@Override
			public void onLoad() {
				GameLevel.this.mCamera.setHUD(new HUD());
				GameLevel.this.mCamera.getHUD().setVisible(false);
				GameLevel.this.ScoreText = new Text(GameLevel.this.mCamera.getWidth() / 2f, 0f, ResourceManager.fontDefaultMagneTank48, mON_SCREEN_SCORE_PRETEXT + "0      ", ResourceManager.getActivity().getVertexBufferObjectManager());
				GameLevel.this.ScoreText.setPosition(GameLevel.this.mCamera.getWidth() / 2f, GameLevel.this.mCamera.getHeight() - (GameLevel.this.ScoreText.getHeight() / 2f));
				GameLevel.this.ScoreText.setScale(0.75f);
				GameLevel.this.ScoreText.setAlpha(0.85f);
				final GrowButton PauseButton = new GrowButton(ResourceManager.gamePauseButtonTR.getWidth() / 2f, GameLevel.this.mCamera.getHeight() - (ResourceManager.gamePauseButtonTR.getHeight() / 2f), ResourceManager.gamePauseButtonTR) {
					@Override
					public void onClick() {
						SceneManager.getInstance().showLayer(LevelPauseLayer.getInstance(GameLevel.this), false, true, true);
					}
				};
				final GrowToggleButton MusicToggleButton = new GrowToggleButton(PauseButton.getX() + 75f, PauseButton.getY(), ResourceManager.MusicToggleTTR, !SFXManager.isMusicMuted()) {
					@Override
					public boolean checkState() {
						return !SFXManager.isMusicMuted();
					}
					
					@Override
					public void onClick() {
						SFXManager.toggleMusicMuted();
					}
				};
				final GrowToggleButton SoundToggleButton = new GrowToggleButton(MusicToggleButton.getX() + 75f, MusicToggleButton.getY(), ResourceManager.SoundToggleTTR, !SFXManager.isSoundMuted()) {
					@Override
					public boolean checkState() {
						return !SFXManager.isSoundMuted();
					}
					
					@Override
					public void onClick() {
						SFXManager.toggleSoundMuted();
					}
				};
				GameLevel.this.mCamera.getHUD().attachChild(GameLevel.this.ScoreText);
				GameLevel.this.mCamera.getHUD().attachChild(PauseButton);
				GameLevel.this.mCamera.getHUD().attachChild(MusicToggleButton);
				GameLevel.this.mCamera.getHUD().attachChild(SoundToggleButton);
				GameLevel.this.mCamera.getHUD().registerTouchArea(PauseButton);
				GameLevel.this.mCamera.getHUD().registerTouchArea(MusicToggleButton);
				GameLevel.this.mCamera.getHUD().registerTouchArea(SoundToggleButton);
				final Text LevelIndexText = new Text(GameLevel.this.mCamera.getWidth() / 2f, GameLevel.this.mCamera.getHeight() / 2f, ResourceManager.fontDefaultMagneTank48, mLEVEL_NUMBER_PRETEXT + GameLevel.this.mLevelDef.mLevelIndex, ResourceManager.getActivity().getVertexBufferObjectManager());
				LevelIndexText.setAlpha(0.85f);
				LevelIndexText.registerEntityModifier(new SequenceEntityModifier(new DelayModifier(1.5f), new MoveModifier(2f, GameLevel.this.mCamera.getWidth() / 2f, GameLevel.this.mCamera.getHeight() / 2f, GameLevel.this.mCamera.getWidth() - (LevelIndexText.getWidth() * 0.6f), GameLevel.this.mCamera.getHeight() - (LevelIndexText.getHeight() * 0.6f), EaseElasticOut.getInstance())));
				GameLevel.this.mCamera.getHUD().attachChild(LevelIndexText);
				GameLevel.this.mCamera.getHUD().setTouchAreaBindingOnActionDownEnabled(true);
				GameLevel.this.mCamera.getHUD().setTouchAreaBindingOnActionMoveEnabled(true);
			}
		});
		
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_4, this) {
			@Override
			public void onLoad() {
				
				for(final BeamsInLevelDef curBeam : GameLevel.this.mLevelDef.mBeams) {
					switch (curBeam.mBeamType) {
						case MetalDynamic:
							new MetalBeamDynamic(curBeam.mX, curBeam.mY, curBeam.mLength, curBeam.mRotation, GameLevel.this);
							break;
						case MetalStatic:
							new MetalBeamStatic(curBeam.mX, curBeam.mY, curBeam.mLength, curBeam.mRotation, GameLevel.this);
							break;
						case WoodenDynamic:
							new WoodenBeamDynamic(curBeam.mX, curBeam.mY, curBeam.mLength, curBeam.mRotation, GameLevel.this);
							break;
					}
				}
				for(final EnemiesInLevelDef curEnemy : GameLevel.this.mLevelDef.mEnemies) {
					switch (curEnemy.mEnemyType) {
						case Normal:
							new MechRat(curEnemy.mX, curEnemy.mY, GameLevel.this);
							break;
					}
				}
				
				float baseMinX = Float.MAX_VALUE;
				float baseMinY = Float.MAX_VALUE;
				float baseMaxX = Float.MIN_VALUE;
				float baseMaxY = Float.MIN_VALUE;
				for(int i = 1; i < GameLevel.this.mBasePositions.size(); i++) {
					baseMinX = Math.min(baseMinX, GameLevel.this.mBasePositions.get(i)[0]);
					baseMinY = Math.min(baseMinY, GameLevel.this.mBasePositions.get(i)[1]);
					baseMaxX = Math.max(baseMinX, GameLevel.this.mBasePositions.get(i)[0]);
					baseMaxY = Math.max(baseMinY, GameLevel.this.mBasePositions.get(i)[1]);
				}
				GameLevel.this.mBasePositionX = (baseMinX + baseMaxX) / 2f;
				GameLevel.this.mBasePositionY = (baseMinY + baseMaxY) / 2f;
				GameLevel.this.mCamera.setBasePosition(GameLevel.this.mBasePositionX, GameLevel.this.mBasePositionY);
				
			}
		});
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_5, this) {
			@Override
			public void onLoad() {
				GameLevel.this.resetTrailingDots();
				final MagneticCrateDef[] SetCrates = GameLevel.this.mLevelDef.mCrates;
				for(final MagneticCrateDef curDef : SetCrates) {
					GameLevel.this.mCratesLeft.add(curDef);
					GameLevel.this.TotalScorePossible += mCRATE_POINT_VALUE;
				}
				GameLevel.this.TotalScorePossible -= GameLevel.this.mLevelDef.mExpectedNumberCratesToCompleteLevel * mCRATE_POINT_VALUE;
				
				GameLevel.this.attachChild(GameLevel.this.mCrateLayer);
				
				GameLevel.this.mRemainingCratesBar = new RemainingCratesBar(GameLevel.this);
			}
		});
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_6, this) {
			@Override
			public void onLoad() {
				GameLevel.this.mMagneTank = new MagneTank(400f, 240f, GameLevel.this);
				GameLevel.this.mCamera.setMagneTankEntity(GameLevel.this.mMagneTank);
				BouncingPowerBar.attachInstanceToHud(GameLevel.this.mCamera.getHUD());
				new MagneticOrb(GameLevel.this);
				
			}
		});
		this.addLoadingStep(new LoadingRunnable(mLOADING_STEP_STRING_7, this) {
			@Override
			public void onLoad() {
				
				GameLevel.this.mCamera.getHUD().setVisible(true);
				GameLevel.this.mCamera.setZoomFactorDirect(SCALED_CAMERA_ZOOM);
				GameLevel.this.mCamera.setCenterDirect(GameLevel.this.mBasePositionX, GameLevel.this.mBasePositionY);
				GameLevel.this.mCamera.goToBaseForSeconds(0.8f);
				
				GameLevel.this.mCamera.setBounds(-256f, -256f, 4000f, 2000f);
				GameLevel.this.mCamera.setBoundsEnabled(true);
				
				GameManager.setGameLevelGoal(GameLevel.this);
				
				((SwitchableFixedStepEngine) ResourceManager.getEngine()).EnableFixedStep();
				
				GameLevel.this.registerUpdateHandler(GameLevel.this.SettleHandlerUpdateHandler);
			}
		});
		
		// delegate contact listeners to their respective
		this.mPhysicsWorld.setContactListener(PhysObject.PHYS_OBJECT_CONTACT_LISTENER);
		
		this.setBackgroundEnabled(true);
		this.setBackground(new Background(0.1f, 0.1f, 0.1f));
		this.setOnSceneTouchListener(this);
		
		this.registerUpdateHandler(this.mMovementReportingTimer);
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mMagneTank.onMagneTankTouchEvent(pScene, pSceneTouchEvent);
		return true;
	}
	
	public void reportBaseBodySpeed(final float pSpeed) {
		if(pSpeed >= mBASE_MOVEMENT_SPEED_THRESHOLD) {
			this.mBaseTotalMovementTime = 0f;
			this.mIsThereBaseMovement = true;
		}
	}
	
	public void resetTrailingDots() {
		if(this.mTrailingDotSprites == null) {
			this.mTrailingDotSprites = new Sprite[mNUMBER_TRAILING_DOTS];
			for(int i = 0; i < this.mTrailingDotSprites.length; i++) {
				this.mTrailingDotSprites[i] = new Sprite(0f, 0f, ResourceManager.gameTrailingDotTR, ResourceManager.getActivity().getVertexBufferObjectManager());
				this.attachChild(this.mTrailingDotSprites[i]);
			}
		}
		for(int i = 0; i < this.mTrailingDotSprites.length; i++) {
			this.mTrailingDotSprites[i].setPosition(-10000, -10000);
			this.mTrailingDotSprites[i].setAlpha(1f);
			this.mTrailingDotSprites[i].setScale(1f);
		}
		this.mTrailingDotCounter = 0;
	}
	
	public void restartLevel() {
		this.disposeLevel();
		SceneManager.getInstance().showScene(new GameLevel(Levels.getLevelDef(this.mLevelDef.mLevelIndex, this.mLevelDef.mWorldIndex)));
	}
	
	public void setNextTrailingDot(final float pX, final float pY) {
		if(this.mTrailingDotCounter == mNUMBER_TRAILING_DOTS) {
			return;
		}
		this.mTrailingDotSprites[this.mTrailingDotCounter].setPosition(pX, pY);
		this.mTrailingDotSprites[this.mTrailingDotCounter].setAlpha(1f - ((1f / mNUMBER_TRAILING_DOTS) * this.mTrailingDotCounter));
		this.mTrailingDotSprites[this.mTrailingDotCounter].setScale(0.5f - ((0.5f / mNUMBER_TRAILING_DOTS) * this.mTrailingDotCounter));
		this.mTrailingDotCounter++;
	}
	
	public void startNextLevel() {
		this.disposeLevel();
		
		SceneManager.getInstance().showScene(new GameLevel(Levels.getLevelDef(this.mLevelDef.mLevelIndex + 1, this.mLevelDef.mWorldIndex)));
	}
}