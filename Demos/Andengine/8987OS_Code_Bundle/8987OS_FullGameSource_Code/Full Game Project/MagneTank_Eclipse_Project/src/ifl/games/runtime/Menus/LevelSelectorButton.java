package ifl.games.runtime.Menus;

import ifl.games.runtime.MagneTankActivity;
import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.GameLevels.Levels;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;
import ifl.games.runtime.Managers.SceneManager;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** The LevelSelectorButton class visually shows the player the state of a
 *  level, locked or unlocked, and the number of stars achieved if the
 *  level is unlocked.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class LevelSelectorButton extends Sprite {
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public final int mLevelIndex;
	public final int mWorldIndex;
	public boolean mIsLocked = true;
	private final Text mButtonText;
	private final Sprite mLockedSprite;
	private final TiledSprite mStarsEnt;
	
	private boolean mIsTouched = false;
	private boolean mIsLarge = false;
	private boolean mIsClicked = false;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public LevelSelectorButton(final int pLevelIndex, final int pWorldIndex, final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.mLevelIndex = pLevelIndex;
		this.mWorldIndex = pWorldIndex;
		
		this.mButtonText = new Text(0f, 0f, ResourceManager.fontDefault32Bold, String.valueOf(this.mLevelIndex), ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mButtonText.setPosition((this.getWidth() / 3f), (this.getHeight() / 2f));
		this.mButtonText.setColor(0.7f, 0.7f, 0.7f);
		
		this.mLockedSprite = new Sprite(this.getWidth() / 2f, this.getHeight() / 2f, ResourceManager.menuLevelLockedTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		
		this.mStarsEnt = new TiledSprite((this.getWidth() / 3f) * 2f, (this.getHeight() / 2f), ResourceManager.menuLevelStarTTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		
		this.mIsLocked = (this.mLevelIndex > (MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_MAX_REACHED) + 1));
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.mIsTouched = true;
		} else if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
			if((pTouchAreaLocalX > this.getWidth()) || (pTouchAreaLocalX < 0f) || (pTouchAreaLocalY > this.getHeight()) || (pTouchAreaLocalY < 0f)) {
				if(this.mIsTouched) {
					this.mIsTouched = false;
				}
			} else {
				if(!this.mIsTouched) {
					this.mIsTouched = true;
				}
			}
		} else if((pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) && this.mIsTouched) {
			this.mIsTouched = false;
			
			// Touch event response
			if(this.mIsLocked) {
				// action if the level is locked
			} else {
				// action is the level is unlocked
				this.mIsClicked = true;
				SFXManager.playClick(1f, 0.5f);
			}
		}
		return true;
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(!this.mIsLocked) {
			if(!this.mButtonText.hasParent()) {
				this.attachChild(this.mButtonText);
				this.attachChild(this.mStarsEnt);
				this.mStarsEnt.setCurrentTileIndex(MagneTankActivity.getLevelStars(this.mLevelIndex));
			}
			if(this.mLockedSprite.hasParent()) {
				this.mLockedSprite.detachSelf();
			}
			if(!this.mIsLarge && this.mIsTouched) {
				this.registerEntityModifier(new ScaleModifier(0.05f, 1f, 1.4f) {
					@Override
					protected void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						LevelSelectorButton.this.mIsLarge = true;
					}
				});
			} else if(this.mIsLarge && !this.mIsTouched) {
				this.registerEntityModifier(new ScaleModifier(0.05f, 1.4f, 1f) {
					@Override
					protected void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						LevelSelectorButton.this.mIsLarge = false;
						if(LevelSelectorButton.this.mIsClicked) {
							SceneManager.getInstance().showScene(new GameLevel(Levels.getLevelDef(LevelSelectorButton.this.mLevelIndex, LevelSelectorButton.this.mWorldIndex)));
							LevelSelectorButton.this.mIsClicked = false;
						}
					}
				});
				this.mIsLarge = false;
			}
		} else {
			if(!this.mLockedSprite.hasParent()) {
				this.attachChild(this.mLockedSprite);
			}
			if(this.mButtonText.hasParent()) {
				this.mButtonText.detachSelf();
				this.mStarsEnt.detachSelf();
			}
			
		}
	}
	
	public void refreshStars() {
		this.mIsLocked = (this.mLevelIndex > (MagneTankActivity.getIntFromSharedPreferences(MagneTankActivity.SHARED_PREFS_LEVEL_MAX_REACHED) + 1));
		this.mStarsEnt.setCurrentTileIndex(MagneTankActivity.getLevelStars(this.mLevelIndex));
	}
}