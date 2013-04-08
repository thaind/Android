package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.Managers.GameManager;
import ifl.games.runtime.Managers.ResourceManager;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

/** The BouncingPowerBar class displays a bouncing indicator to the player
 *  that indicates how powerful each shot from the vehicle will be. It
 *  transforms the visible location of the indicator to a fractional value,
 *  which then has a cubic curve applied to add even more of a challenge when
 *  trying to achieve the most powerful shot.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class BouncingPowerBar extends Entity {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final BouncingPowerBar INSTANCE = new BouncingPowerBar();
	private static final float mLINE_SPEED = 1.5f;
	private static final float mLINE_MIN_Y = 17f;
	private static final float mLINE_MAX_Y = 239f;
	private static final float mLINE_RANGE = mLINE_MAX_Y - mLINE_MIN_Y;
	
	private static final float mBACKGROUND_POSITION_X = 32f;
	private static float mBACKGROUND_POSITION_Y;
	private static final float mLINE_POSITION_X = 32f;
	private static final float mLINE_POSITION_Y = 17f;
	private static final float mLENS_POSITION_X = 32f;
	private static final float mLENS_POSITION_Y = 128f;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private MagneTank mMagneTank;
	private boolean mIsLineMovingUp = true;
	private static Sprite mBACKGROUND;
	private static Sprite mLINE;
	private static Sprite mLENS;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	private BouncingPowerBar() {
		
		mBACKGROUND_POSITION_Y = ResourceManager.getInstance().cameraHeight/2f;
		mBACKGROUND = new Sprite(mBACKGROUND_POSITION_X,mBACKGROUND_POSITION_Y,ResourceManager.gamePowerBarBackgroundTR,ResourceManager.getActivity().getVertexBufferObjectManager());
		mLINE = new Sprite(mLINE_POSITION_X,mLINE_POSITION_Y,ResourceManager.gamePowerBarLineTR,ResourceManager.getActivity().getVertexBufferObjectManager());
		mLENS = new Sprite(mLENS_POSITION_X,mLENS_POSITION_Y,ResourceManager.gamePowerBarLensTR,ResourceManager.getActivity().getVertexBufferObjectManager());
		
		attachChild(BouncingPowerBar.mBACKGROUND);
		mBACKGROUND.attachChild(mLINE);
		mBACKGROUND.attachChild(mLENS);
		mMagneTank = GameManager.getGameLevel().mMagneTank;
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public static void attachInstanceToHud(final HUD pHud) {
		INSTANCE.mMagneTank = GameManager.getGameLevel().mMagneTank;
		if(INSTANCE.hasParent())
			INSTANCE.detachSelf();
		pHud.attachChild(INSTANCE);
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(mMagneTank.mTurretMagnetOn) {
			if(mIsLineMovingUp) {
				mLINE.setY(mLINE.getY()+(pSecondsElapsed * mLINE_SPEED * mLINE_RANGE));
				if(mLINE.getY()>mLINE_MAX_Y) {
					mIsLineMovingUp = false;
					mLINE.setY(mLINE_MAX_Y - (mLINE.getY() - mLINE_MAX_Y));
				}
			} else {
				mLINE.setY(mLINE.getY()-(pSecondsElapsed * mLINE_SPEED * mLINE_RANGE));
				if(mLINE.getY()<mLINE_MIN_Y) {
					mIsLineMovingUp = true;
					mLINE.setY(mLINE_MIN_Y + (mLINE_MIN_Y - mLINE.getY()));
				}
			}
			
			// turn the power level from a linear slope into a cubic slope.
			final float PercentageOfRange = ((mLINE.getY()-mLINE_MIN_Y)/mLINE_RANGE);
			final float PercentageOfPower = (float) Math.pow(PercentageOfRange, 3);
			mMagneTank.mShootingPower = ((MagneTank.mSHOOTING_POWER_MAXIMUM - MagneTank.mSHOOTING_POWER_MINIMUM) * PercentageOfPower) + MagneTank.mSHOOTING_POWER_MINIMUM;
		} else {
			mIsLineMovingUp = true;
			if(mLINE.getY()!=mLINE_MIN_Y)
				mLINE.setY(mLINE_MIN_Y);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
}