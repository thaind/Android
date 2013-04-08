package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.MagneTankSmoothCamera;
import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;
import android.util.FloatMath;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

/** The MagneticCrate class extends the MagneticPhysObject class. It creates
 *  and handles the various types of crates available to launch from the
 *  MagneTank vehicle. Each crate is displayed in the form of a TiledSprite,
 *  with the TiledSprite’s image index set to the crate’s type. The
 *  MagneticCrate class makes use of Box2D’s postSolve() method from the
 *  PhysicsWorld’s ContactListener.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MagneticCrate extends MagneticPhysObject<TiledSprite> {

	// ====================================================
	// ENUMS
	// ====================================================
	public enum MagneticCrateDef {
		SmallNormal(CrateSize.Small, CrateType.Normal),
		MediumHorizontalNormal(CrateSize.MediumHorizontal, CrateType.Normal),
		MediumVerticalNormal(CrateSize.MediumVertical, CrateType.Normal),
		LargeNormal(CrateSize.Large, CrateType.Normal),
		
		SmallExplosive(CrateSize.Small, CrateType.Explosive),
		MediumHorizontalExplosive(CrateSize.MediumHorizontal, CrateType.Explosive),
		MediumVerticalExplosive(CrateSize.MediumVertical, CrateType.Explosive),
		LargeExplosive(CrateSize.Large, CrateType.Explosive),
		
		SmallFragile(CrateSize.Small, CrateType.Fragile),
		MediumHorizontalFragile(CrateSize.MediumHorizontal, CrateType.Fragile),
		MediumVerticalFragile(CrateSize.MediumVertical, CrateType.Fragile),
		LargeFragile(CrateSize.Large, CrateType.Fragile),
		
		SmallElectric(CrateSize.Small, CrateType.Electric),
		MediumHorizontalElectric(CrateSize.MediumHorizontal, CrateType.Electric),
		MediumVerticalElectric(CrateSize.MediumVertical, CrateType.Electric),
		LargeElectric(CrateSize.Large, CrateType.Electric);
		
		public CrateSize crateSize;
		public CrateType crateType;
		MagneticCrateDef(CrateSize pCrateSize, CrateType pCrateType) {
			crateSize = pCrateSize;
			crateType = pCrateType;
		}
	}
	public enum CrateSize {
		Small(0), MediumHorizontal(1), MediumVertical(1), Large(2);
		public int sizeIndex;
		CrateSize(int pIndex) {
			sizeIndex = pIndex;
		}
	}
	public enum CrateType {
		Normal(0), Explosive(1), Fragile(2), Electric(3);
		public int imageIndex;
		CrateType(int pIndex) {
			imageIndex = pIndex;
		}
	}

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mCRATE_ANGULAR_DAMPING = 0.4f;
	private static final float mCRATE_DENSITY = 10f;
	private static final float mCRATE_ELASTICITY = 0f;
	private static final float mCRATE_FRICTION = 0.95f;
	private static final FixtureDef mCRATE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mCRATE_DENSITY, mCRATE_ELASTICITY, mCRATE_FRICTION);
	private static final int mMAX_SOUNDS_PER_SECOND = 5;
	private static final float mMINIMUM_SECONDS_BETWEEN_SOUNDS = 1f / mMAX_SOUNDS_PER_SECOND;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public CrateSize mCrateSize;
	public CrateType mCrateType;
	
	private GameLevel mGameLevel;
	
	private final Sprite mTrailingLinesSprite;
	private boolean mHasImpacted = false;
	private float mBodySpeed = 0f;
	
	private float secondsSinceLastSound = 0.5f;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MagneticCrate(float pX, float pY, CrateSize pCrateSize, CrateType pCrateType, GameLevel pGameLevel) {
		this.mCrateSize = pCrateSize;
		this.mCrateType = pCrateType;
		this.mGameLevel = pGameLevel;
		
		TiledTextureRegion CrateTiledSpriteTTR;
		
		switch (this.mCrateSize) {
		case MediumHorizontal:
			CrateTiledSpriteTTR = ResourceManager.gameCrateMediumHorizontalTTR;
			break;
		case MediumVertical:
			CrateTiledSpriteTTR = ResourceManager.gameCrateMediumVerticalTTR;
			break;
		case Large:
			CrateTiledSpriteTTR = ResourceManager.gameCrateLargeTTR;
			break;
		default:
			CrateTiledSpriteTTR = ResourceManager.gameCrateSmallTTR;
		}
		TiledSprite CrateTiledSprite = new TiledSprite(pX,pY,CrateTiledSpriteTTR,ResourceManager.getActivity().getVertexBufferObjectManager()) {
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(MagneticCrate.this.mBody!=null)
					mGameLevel.reportBaseBodySpeed(MagneticCrate.this.mBody.getLinearVelocity().len2());
			}
		};
		
		
		CrateTiledSprite.setCurrentTileIndex(this.mCrateType.imageIndex);
		Body CrateBody = PhysicsFactory.createBoxBody(this.mGameLevel.mPhysicsWorld, CrateTiledSprite, BodyType.DynamicBody, MagneticCrate.mCRATE_FIXTURE_DEF);
		final PhysicsConnector physConnector = new PhysicsConnector(CrateTiledSprite, CrateBody);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(physConnector);
		CrateBody.setAngularDamping(mCRATE_ANGULAR_DAMPING);
		this.set(CrateBody, CrateTiledSprite, physConnector, this.mGameLevel);
		this.mGameLevel.mMagneticObjects.add(this);
		this.mIsGrabbed = true;
		
		mTrailingLinesSprite = new Sprite(-10000f,-10000f,ResourceManager.gameTrailingLinesTR,ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(MagneticCrate.this.mEntity==null || MagneticCrate.this.mBody==null) {
					// destroy self
					final Sprite ThisSprite = this;
					ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							ThisSprite.detachSelf();
							if(!ThisSprite.isDisposed())
								ThisSprite.dispose();
						}});
				} else {
					if(MagneticCrate.this.mIsShot) {
						this.setPosition(MagneticCrate.this.mEntity);
						this.setRotation(MathUtils.radToDeg((float) Math.atan2(-MagneticCrate.this.mBody.getLinearVelocity().y, MagneticCrate.this.mBody.getLinearVelocity().x)) + 90f);
						this.setAlpha(Math.min(MagneticCrate.this.mBody.getLinearVelocity().len()/50f,0.5f));
						
						if(!mHasImpacted) {
							final Sprite lastTrailingDot = mGameLevel.getLastTrailingDot();
							if(MathUtils.distance(lastTrailingDot.getX(), lastTrailingDot.getY(), MagneticCrate.this.mEntity.getX(), MagneticCrate.this.mEntity.getY()) > GameLevel.mTRAILING_DOTS_SPACING) {
								mGameLevel.setNextTrailingDot(MagneticCrate.this.mEntity.getX(), MagneticCrate.this.mEntity.getY());
							}
						}
					}
				}
			}
		};
		mTrailingLinesSprite.setAnchorCenterY(1.5f);
		this.mGameLevel.mCrateLayer.attachChild(mTrailingLinesSprite);
		this.mGameLevel.mCrateLayer.attachChild(CrateTiledSprite);
		
		this.mEntity.setScale(0.05f);
		this.mBody.setActive(false);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		if(this.mEntity.getScaleX()<1f) {
			this.mEntity.setScale(Math.min(this.mEntity.getScaleX() + pSecondsElapsed, 1f));
		} else {
			if(!this.mBody.isActive()) {
				this.mBody.setActive(true);
				mGameLevel.mMagneTank.mTurretMagnetOn = true;
			} else if(this.mEntity.getY()<-1024 && !mHasImpacted) {
				mHasImpacted = true;
				((MagneTankSmoothCamera)ResourceManager.getEngine().getCamera()).goToMagneTank();
				ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						mGameLevel.mMagneTank.equipNextCrate(false);
					}});
				this.destroy();
			}
			mBodySpeed = 0f;
			if(secondsSinceLastSound < mMINIMUM_SECONDS_BETWEEN_SOUNDS)
				secondsSinceLastSound += pSecondsElapsed;
		}
	}
	
	private void playSound() {
		SFXManager.playCrate(1f - Math.min((mBodySpeed/3000f) * 0.3f, 0.3f), Math.min(mBodySpeed/1500f, 1f));
		secondsSinceLastSound = 0f;
	}
	
	@Override
	public void onPreSolve(Contact pContact, Manifold pOldManifold) {
		mBodySpeed = Math.max(this.mBody.getLinearVelocity().len2(),mBodySpeed);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void onPostSolve(float pMaxImpulse) {
		if(!this.mIsGrabbed) {
			if(this.mEntity != null && this!=null) {
				if(pMaxImpulse>0.5f && mBodySpeed>120f && secondsSinceLastSound>=mMINIMUM_SECONDS_BETWEEN_SOUNDS) {
					playSound();
				}

				switch(this.mCrateType) {
				case Explosive:
					if(pMaxImpulse > 20f) {
						this.mIsGrabbed = false;
						this.mGameLevel.mMagneticObjects.remove(this);
						if(this.mGameLevel.mMagneTank.mGrabbedMagneticObject == this)
							this.mGameLevel.mMagneTank.mGrabbedMagneticObject = null;
						this.destroy();
						this.mGameLevel.createExplosion(this.mBody.getWorldCenter(), (this.mCrateSize.sizeIndex+1f)*60000f*FloatMath.sqrt(this.mBody.getMass()));
						AnimatedSprite crateExplosion = new AnimatedSprite(this.mEntity.getX(),this.mEntity.getY(),ResourceManager.gameExplosionTTR,ResourceManager.getActivity().getVertexBufferObjectManager());
						this.mGameLevel.attachChild(crateExplosion);
						crateExplosion.setBlendFunction(GLES20.GL_ONE, GLES20.GL_ONE);
						crateExplosion.setBlendingEnabled(true);
						switch(this.mCrateSize) {
						case Small:
							crateExplosion.setScale(8f);
							SFXManager.playExplosion(1f, 1f);
							break;
						case Large:
							crateExplosion.setScale(16f);
							SFXManager.playExplosion(0.6f, 1f);
							break;
						default:
							crateExplosion.setScale(12f);
							SFXManager.playExplosion(0.85f, 1f);
						}
						crateExplosion.animate(40, new IAnimationListener() {
							@Override
							public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,int pRemainingLoopCount, int pInitialLoopCount) {
								final AnimatedSprite animSprite = pAnimatedSprite;
								animSprite.stopAnimation();
								ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										animSprite.detachSelf();
										if(!animSprite.isDisposed())
											animSprite.dispose();
									}});
							}

							@Override public void onAnimationStarted(AnimatedSprite pAnimatedSprite,int pInitialLoopCount) {}
							@Override public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex,int pNewFrameIndex) {}
							@Override public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {}
						});
					}
					break;
				}
			}
		}
	}
	
	@Override
	public void onBeginContact(Contact pContact) {
		if(!this.mIsGrabbed)
			if(this.mEntity != null && this!=null)
				if(!mHasImpacted){
					mHasImpacted = true;
					((MagneTankSmoothCamera)ResourceManager.getEngine().getCamera()).goToBaseForSeconds(1f);
					ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							mGameLevel.mMagneTank.equipNextCrate(false);
						}});
				}
	}
	
	@Override public void onEndContact(Contact pContact) {}
	
}