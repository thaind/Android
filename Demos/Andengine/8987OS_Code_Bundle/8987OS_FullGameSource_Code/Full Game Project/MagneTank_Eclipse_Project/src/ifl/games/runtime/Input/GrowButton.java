package ifl.games.runtime.Input;

import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

/** The GrowButton class simply shows an image that grows to a specific scale
 *  while the player is touching it and returns to its original scale when
 *  the touch is lifted or lost.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class GrowButton extends Sprite {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mGROW_DURATION_SECONDS = 0.05f;
	private static final float mNORMAL_SCALE_DEFAULT = 1f;
	private static final float mGROWN_SCALE_DEFAULT = 1.4f;
	private static final float mENABLED_ALPHA = 1f;
	private static final float mDISABLED_ALPHA = 0.5f;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public boolean mIsEnabled = true;
	private float mNormalScale = mNORMAL_SCALE_DEFAULT;
	private float mGrownScale = mGROWN_SCALE_DEFAULT;
	private boolean mIsTouched = false;
	private boolean mIsLarge = false;
	private boolean mIsClicked = false;
	private boolean mTouchStartedOnThis = false;
	
	// ====================================================
	// ABSTRACT METHOD
	// ====================================================
	public abstract void onClick();
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public GrowButton(final float pX, final float pY, final ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, ResourceManager.getActivity().getVertexBufferObjectManager());
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void setScales(final float pNormalScale, final float pGrownScale) {
		mNormalScale = pNormalScale;
		mGrownScale = pGrownScale;
		this.setScale(pNormalScale);
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(!mIsLarge && mIsTouched) {
			this.registerEntityModifier(new ScaleModifier(mGROW_DURATION_SECONDS, mNormalScale, mGrownScale) {
				@Override
				protected void onModifierFinished(final IEntity pItem) {
					super.onModifierFinished(pItem);
					mIsLarge = true;
				}
			});
		} else if(mIsLarge && !mIsTouched) {
			this.registerEntityModifier(new ScaleModifier(mGROW_DURATION_SECONDS, mGrownScale, mNormalScale) {
				@Override
				protected void onModifierFinished(final IEntity pItem) {
					super.onModifierFinished(pItem);
					mIsLarge = false;
					if(mIsClicked) {
						onClick();
						mIsClicked = false;
					}
				}
			});
			mIsLarge = false;
		}
		if(mIsEnabled) {
			if(this.getAlpha()!=mENABLED_ALPHA)
				this.setAlpha(mENABLED_ALPHA);
		} else {
			if(this.getAlpha()!=mDISABLED_ALPHA)
				this.setAlpha(mDISABLED_ALPHA);
		}
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if(pTouchAreaLocalX>this.getWidth() || pTouchAreaLocalX < 0f || pTouchAreaLocalY>this.getHeight() || pTouchAreaLocalY < 0f) {
				mTouchStartedOnThis = false;
			} else {
				mTouchStartedOnThis = true;
			}
			if(mIsEnabled)
				mIsTouched = true;
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
				if(pTouchAreaLocalX>this.getWidth() || pTouchAreaLocalX < 0f || pTouchAreaLocalY>this.getHeight() || pTouchAreaLocalY < 0f) {
					if(mIsTouched) {
						mIsTouched = false;
					}
				} else {
					if(mTouchStartedOnThis && !mIsTouched)
						if(mIsEnabled)
							mIsTouched = true;
				}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP && mIsTouched && mTouchStartedOnThis) {
			mIsTouched = false;
			mIsClicked = true;
			mTouchStartedOnThis = false;
			SFXManager.playClick(1f, 0.5f);
		}
		return true;
	}
	
}