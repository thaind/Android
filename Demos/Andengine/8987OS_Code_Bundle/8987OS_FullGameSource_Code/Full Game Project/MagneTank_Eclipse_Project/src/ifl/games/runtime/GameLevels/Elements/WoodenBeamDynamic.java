package ifl.games.runtime.GameLevels.Elements;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;
import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

/** This class is similar to the MetalBeam classes, but adds a health aspect
 *  that causes the WoodenBeamDynamic to be replaced with a particle effect
 *  once its health reaches zero.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class WoodenBeamDynamic extends PhysObject<Sprite> {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mWOODEN_BEAM_ANGULAR_DAMPING = 0.4f;
	private static final int mPOINTS_FOR_DESTROYING = 100;
	private static final int mNUMBER_WOODEN_PARTICLES = 20;
	private static final float mINITIAL_HEALTH = 100f;
	private static final float mMINIMUM_IMPULSE_TO_PLAY_SOUND = 12f;
	private static final float mMAXIMUM_DAMAGE_TOLERANCE = 20f;
	private static final float mMINIMUM_DAMAGE_TOLERANCE = 6.75f;
	
	private static final float mWOODEN_BEAM_DENSITY = 60f;
	private static final float mWOODEN_BEAM_ELASTICITY = 0.0f;
	private static final float mWOODEN_BEAM_FRICTION = 0.95f;
	private static final FixtureDef mWOODEN_BEAM_DUNAMIC_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mWOODEN_BEAM_DENSITY, mWOODEN_BEAM_ELASTICITY, mWOODEN_BEAM_FRICTION);
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private final GameLevel mGameLevel;
	private final Sprite mWoodenBeamDynamicSprite;
	private final Body mWoodenBeamDynamicBody;
	private final RectangleParticleEmitter mWoodenParticleEmitter;
	private final SpriteParticleSystem mWoodenParticleSystem;
	
	private float mHealth = mINITIAL_HEALTH;
	private float mParticleVelocityX;
	private float mParticleVelocityY;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public WoodenBeamDynamic(final float pX, final float pY, final float pWidth, final float pAngle, final GameLevel pGameLevel) {
		this.mGameLevel = pGameLevel;
		this.mGameLevel.mBasePositions.add(new float[] {pX, pY});
		final ITextureRegion woodenDynamicTRCopy = ResourceManager.gameWoodenDynamicTR.deepCopy();
		woodenDynamicTRCopy.setTextureWidth(pWidth);
		this.mWoodenBeamDynamicSprite = new Sprite(pX, pY, woodenDynamicTRCopy, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(WoodenBeamDynamic.this.mWoodenBeamDynamicBody != null) {
					WoodenBeamDynamic.this.mGameLevel.reportBaseBodySpeed(WoodenBeamDynamic.this.mWoodenBeamDynamicBody.getLinearVelocity().len2());
				}
			}
		};
		this.mGameLevel.attachChild(this.mWoodenBeamDynamicSprite);
		
		
		this.mWoodenBeamDynamicBody = PhysicsFactory.createBoxBody(pGameLevel.mPhysicsWorld, this.mWoodenBeamDynamicSprite, BodyType.DynamicBody, mWOODEN_BEAM_DUNAMIC_FIXTURE_DEF);
		final PhysicsConnector physConnector = new PhysicsConnector(this.mWoodenBeamDynamicSprite, this.mWoodenBeamDynamicBody);
		pGameLevel.mPhysicsWorld.registerPhysicsConnector(physConnector);
		
		this.mWoodenBeamDynamicBody.setAngularDamping(mWOODEN_BEAM_ANGULAR_DAMPING);
		
		this.mWoodenBeamDynamicBody.setTransform(this.mWoodenBeamDynamicBody.getWorldCenter(), MathConstants.DEG_TO_RAD * pAngle);
		
		this.set(this.mWoodenBeamDynamicBody, this.mWoodenBeamDynamicSprite, physConnector, this.mGameLevel);
		this.mGameLevel.TotalScorePossible += mPOINTS_FOR_DESTROYING;
		
		this.mWoodenParticleEmitter = new RectangleParticleEmitter(this.mEntity.getX(), this.mEntity.getY(), this.mWoodenBeamDynamicSprite.getWidth() - ResourceManager.gameWoodenParticleTR.getWidth(), this.mWoodenBeamDynamicSprite.getHeight() - ResourceManager.gameWoodenParticleTR.getHeight()) {
			@Override
			public void getPositionOffset(final float[] pOffset) {
				pOffset[VERTEX_INDEX_X] = (this.mCenterX - this.mWidthHalf) + (MathUtils.RANDOM.nextFloat() * this.mWidth);
				pOffset[VERTEX_INDEX_Y] = (this.mCenterY - this.mHeightHalf) + (MathUtils.RANDOM.nextFloat() * this.mHeight);
				MathUtils.rotateAroundCenter(pOffset, -(WoodenBeamDynamic.this.mEntity.getRotation()), this.mCenterX, this.mCenterY);
			}
		};
		this.mWoodenParticleSystem = new SpriteParticleSystem(this.mWoodenParticleEmitter, mNUMBER_WOODEN_PARTICLES, mNUMBER_WOODEN_PARTICLES, mNUMBER_WOODEN_PARTICLES, ResourceManager.gameWoodenParticleTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			SpriteParticleSystem ThisSpriteParticleSystem = this;
			boolean hasLoaded = false;
			
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(this.hasLoaded) {
					if(this.mParticlesAlive == 0) {
						ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								if(ThisSpriteParticleSystem != null) {
									ThisSpriteParticleSystem.detachSelf();
									if(!ThisSpriteParticleSystem.isDisposed()) {
										ThisSpriteParticleSystem.dispose();
									}
								}
							}
						});
					}
				} else {
					this.hasLoaded = true;
					this.addParticleInitializer(new AlphaParticleInitializer<Sprite>(1f));
					this.addParticleInitializer(new VelocityParticleInitializer<Sprite>(WoodenBeamDynamic.this.mParticleVelocityX - 20f, WoodenBeamDynamic.this.mParticleVelocityX + 20f, WoodenBeamDynamic.this.mParticleVelocityY - 20f, WoodenBeamDynamic.this.mParticleVelocityY + 20f));
					this.addParticleInitializer(new RotationParticleInitializer<Sprite>(WoodenBeamDynamic.this.mEntity.getRotation() - 20f, WoodenBeamDynamic.this.mEntity.getRotation() + 20f));
					this.addParticleInitializer(new ScaleParticleInitializer<Sprite>(1f, 2f));
					this.addParticleInitializer(new ExpireParticleInitializer<Sprite>(2f));
					this.addParticleInitializer(new ColorParticleInitializer<Sprite>(0.5f, 0.5f, 0.5f));
					this.addParticleModifier(new AlphaParticleModifier<Sprite>(1.2f, 2f, 1f, 0f));
					this.addParticleModifier(new OffCameraExpireParticleModifier<Sprite>(ResourceManager.getEngine().getCamera()));
				}
			}
		};
		
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void onBeginContact(final Contact pContact) {
		
	}
	
	@Override
	public void onEndContact(final Contact pContact) {
		
	}
	
	@Override
	public void onPostSolve(final float pMaxImpulse) {
		if(this.mGameLevel.mIsLevelSettled) {
			if(pMaxImpulse > mMINIMUM_DAMAGE_TOLERANCE) {
				this.mHealth -= pMaxImpulse;
				if((pMaxImpulse > mMINIMUM_IMPULSE_TO_PLAY_SOUND) && (this.mHealth >= 0f)) {
					SFXManager.playWood(1f - Math.min(pMaxImpulse / 120f, 0.3f), Math.min(pMaxImpulse / 10f, 1f));
				}
			}
			if((pMaxImpulse > mMAXIMUM_DAMAGE_TOLERANCE) || (this.mHealth < 0f)) {
				this.mGameLevel.addPointsToScore(this.mEntity, mPOINTS_FOR_DESTROYING);
				this.destroy();
				
				this.mWoodenParticleEmitter.setCenter(this.mEntity.getX(), this.mEntity.getY());
				this.mGameLevel.attachChild(this.mWoodenParticleSystem);
				this.mWoodenParticleSystem.onUpdate(0f); // update the particle system with zero seconds to add all initializers and modifiers
				this.mWoodenParticleSystem.onUpdate(1f);
				this.mWoodenParticleSystem.setParticlesSpawnEnabled(false);
			} else {
				this.mEntity.setColor((this.mHealth / 200f) + 0.5f, (this.mHealth / 200f) + 0.5f, (this.mHealth / 200f) + 0.5f);
			}
		}
	}
	
	@Override
	public void onPreSolve(final Contact pContact, final Manifold pOldManifold) {
		this.mParticleVelocityX = -this.mBody.getLinearVelocity().x;
		this.mParticleVelocityY = -this.mBody.getLinearVelocity().y;
	}
}