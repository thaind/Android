package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import android.opengl.GLES20;

/** The MagneticOrb class creates a visual effect around the MagneTank’s
 *  current projectile. It rotates two swirl images in opposite directions
 *  to give the illusion of a spherical force. The MagneticOrb forms and
 *  fades as projectiles are loaded and shot.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MagneticOrb extends Entity {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mMIN_ALPHA = 0f;
	private static final float mMAX_ALPHA = 0.4f;
	private static final float mMAX_SCALE = 2f;
	private static final float mSECONDS_TO_TRANSITION = 0.5f;
	private static final float mROTATION_SPEED_DEGREES_PER_SECOND = 20f;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private final GameLevel mGameLevel;
	private final Sprite mMagOrbCW;
	private final Sprite mMagOrbCCW;
	
	private MagneticPhysObject<?> chaseEnt;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MagneticOrb(GameLevel pGameLevel) {
		mGameLevel = pGameLevel;
		
		mMagOrbCW = new Sprite(0f,0f,ResourceManager.gameMagOrbCWTR,ResourceManager.getActivity().getVertexBufferObjectManager());
		mMagOrbCW.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		mMagOrbCW.setBlendingEnabled(true);
		mMagOrbCW.setScale(mMAX_SCALE);
		mMagOrbCW.setAlpha(mMIN_ALPHA);
		this.attachChild(mMagOrbCW);
		
		mMagOrbCCW = new Sprite(0f,0f,ResourceManager.gameMagOrbCCWTR,ResourceManager.getActivity().getVertexBufferObjectManager());
		mMagOrbCCW.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		mMagOrbCCW.setBlendingEnabled(true);
		mMagOrbCCW.setScale(mMAX_SCALE);
		mMagOrbCCW.setAlpha(mMIN_ALPHA);
		this.attachChild(mMagOrbCCW);
		mGameLevel.attachChild(this);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	private void fadeIn(final float pSecondsElapsed) {
		if(mMagOrbCW.getAlpha()<mMAX_ALPHA) {
			mMagOrbCW.setAlpha(Math.min(mMagOrbCW.getAlpha() + (pSecondsElapsed/mSECONDS_TO_TRANSITION) * mMAX_ALPHA,mMAX_ALPHA));
			mMagOrbCCW.setAlpha(mMagOrbCW.getAlpha());
			mMagOrbCW.setScale(Math.min(mMagOrbCW.getScaleX() + (pSecondsElapsed/mSECONDS_TO_TRANSITION) * mMAX_SCALE,mMAX_SCALE));
			mMagOrbCCW.setScale(mMagOrbCW.getScaleX());
		}
	}

	private void fadeOut(final float pSecondsElapsed) {
		if(mMagOrbCW.getAlpha()>mMIN_ALPHA) {
			mMagOrbCW.setAlpha(Math.max(mMagOrbCW.getAlpha() - (pSecondsElapsed/mSECONDS_TO_TRANSITION) * mMAX_ALPHA,mMIN_ALPHA));
			mMagOrbCCW.setAlpha(mMagOrbCW.getAlpha());
			mMagOrbCW.setScale(Math.max(mMagOrbCW.getScaleX() - (pSecondsElapsed/mSECONDS_TO_TRANSITION) * mMAX_SCALE,mMIN_ALPHA));
			mMagOrbCCW.setScale(mMagOrbCW.getScaleX());
		}
	}
	
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(mGameLevel.mMagneTank.mGrabbedMagneticObject!=null)
			chaseEnt = mGameLevel.mMagneTank.mGrabbedMagneticObject;
		if(chaseEnt!=null)
			if(chaseEnt.mEntity!=null) {
				if(chaseEnt.mIsGrabbed) {
					this.fadeIn(pSecondsElapsed);
				} else {
					this.fadeOut(pSecondsElapsed);
				}
				mMagOrbCW.setRotation(mMagOrbCW.getRotation()+pSecondsElapsed*mROTATION_SPEED_DEGREES_PER_SECOND);
				mMagOrbCCW.setRotation(mMagOrbCCW.getRotation()-pSecondsElapsed*mROTATION_SPEED_DEGREES_PER_SECOND);
				this.setPosition(chaseEnt.mEntity);
			} else {
				this.fadeOut(pSecondsElapsed);
			}
		super.onManagedUpdate(pSecondsElapsed);
	}
}