package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.GameLevels.Elements.MagneticCrate.CrateType;
import ifl.games.runtime.Managers.ResourceManager;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseQuadInOut;

/** The RemainingCratesBar class gives a visual representation to the player
 *  of which crates are left to be shot by the MagneTank. The size, type, and
 *  number of crates left in each level are retrieved from the GameLevel
 *  class and vary from level to level. When one crate is shot, the
 *  RemainingCratesBar animates to reflect the change in the game state.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class RemainingCratesBar extends Entity {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mACTIVE_CRATE_SHRINKING_DURATION_SECONDS = 0.5f;
	private static final float mREMAINING_CRATES_SLIDING_DURATION_SECONDS = 0.5f;
	private static final float mREMAINING_CRATES_BAR_SCALE = 0.25f;
	private static final float mMARGIN_AND_SPACING = 15f;
	private static final float mINACTIVE_CRATE_ALPHA = 0.6f;
	private static final float mMARGIN_AND_SPACING_SCALED = mMARGIN_AND_SPACING / mREMAINING_CRATES_BAR_SCALE;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public final ArrayList<TiledSprite> mCrates = new ArrayList<TiledSprite>();
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public RemainingCratesBar(GameLevel pGameLevel) {
		for(int i = 0; i < pGameLevel.mCratesLeft.size(); i++) {
			TiledTextureRegion curCrateTTR;
			switch(pGameLevel.mCratesLeft.get(i).crateSize) {
			case MediumHorizontal:
				curCrateTTR = ResourceManager.gameCrateMediumHorizontalTTR;
				break;
			case MediumVertical:
				curCrateTTR = ResourceManager.gameCrateMediumVerticalTTR;
				break;
			case Large:
				curCrateTTR = ResourceManager.gameCrateLargeTTR;
				break;
			default:
				curCrateTTR = ResourceManager.gameCrateSmallTTR;
			}
			TiledSprite curCrate = new TiledSprite(0f,0f,curCrateTTR,ResourceManager.getActivity().getVertexBufferObjectManager());
			curCrate.setAlpha(i<1? 1f : mINACTIVE_CRATE_ALPHA);
			curCrate.setAnchorCenter(0f, 0f);
			curCrate.setPosition((i<1? mMARGIN_AND_SPACING_SCALED : mCrates.get(mCrates.size()-1).getX() + mCrates.get(mCrates.size()-1).getWidth() + mMARGIN_AND_SPACING_SCALED), mMARGIN_AND_SPACING_SCALED);
			switch(pGameLevel.mCratesLeft.get(i).crateType) {
			case Normal:
				curCrate.setCurrentTileIndex(CrateType.Normal.imageIndex);
				break;
			case Explosive:
				curCrate.setCurrentTileIndex(CrateType.Explosive.imageIndex);
				break;
			case Fragile:
				curCrate.setCurrentTileIndex(CrateType.Fragile.imageIndex);
				break;
			case Electric:
				curCrate.setCurrentTileIndex(CrateType.Electric.imageIndex);
			}
			this.attachChild(curCrate);
			mCrates.add(curCrate);
		}
		this.setScale(mREMAINING_CRATES_BAR_SCALE);
		ResourceManager.getEngine().getCamera().getHUD().attachChild(this);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void pullCrate() {
		ScaleModifier FirstCrateScaleModifier = new ScaleModifier(mACTIVE_CRATE_SHRINKING_DURATION_SECONDS, 1f, 0f);
		FirstCrateScaleModifier.addModifierListener(new IModifierListener<IEntity>() {
			@Override public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				for(int i = 1; i < mCrates.size(); i++) {
					mCrates.get(i).registerEntityModifier(new MoveModifier(mREMAINING_CRATES_SLIDING_DURATION_SECONDS, mCrates.get(i).getX(), mCrates.get(i).getY(), mCrates.get(i).getX() - mCrates.get(0).getWidth() - mMARGIN_AND_SPACING_SCALED, mCrates.get(i).getY(), EaseQuadInOut.getInstance()));
				}
				ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						mCrates.get(0).detachSelf();
						mCrates.get(0).dispose();
						mCrates.remove(0);
						if(mCrates.size()>0)
							mCrates.get(0).setAlpha(1f);
					}});
			}
		});
		mCrates.get(0).registerEntityModifier(FirstCrateScaleModifier);
	}
}