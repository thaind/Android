package ifl.games.runtime.Input;

import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

/** Based on the GrowButton class, this class adds the functionality of
 *  showing one of two TiledTextureRegion indices depending on the state
 *  of a condition.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class GrowToggleButton extends TiledSprite {
	
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
	
	private boolean isStateTrue;
	
	// ====================================================
	// ABSTRACT METHODS
	// ====================================================
	public abstract void onClick();
	public abstract boolean checkState();
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public GrowToggleButton(final float pX, final float pY, final ITiledTextureRegion pTextureRegion, final boolean pCurrentState) {
		super(pX, pY, pTextureRegion, ResourceManager.getActivity().getVertexBufferObjectManager());
		isStateTrue = pCurrentState;
		if(isStateTrue)
			this.setCurrentTileIndex(0);
		else
			this.setCurrentTileIndex(1);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
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
		if(isStateTrue != checkState()) {
			isStateTrue = checkState();
			if(isStateTrue)
				this.setCurrentTileIndex(0);
			else
				this.setCurrentTileIndex(1);
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
			if(mTouchStartedOnThis)
				if(pTouchAreaLocalX>this.getWidth() || pTouchAreaLocalX < 0f || pTouchAreaLocalY>this.getHeight() || pTouchAreaLocalY < 0f) {
					if(mIsTouched) {
						mIsTouched = false;
					}
				} else {
					if(!mIsTouched && mTouchStartedOnThis)
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