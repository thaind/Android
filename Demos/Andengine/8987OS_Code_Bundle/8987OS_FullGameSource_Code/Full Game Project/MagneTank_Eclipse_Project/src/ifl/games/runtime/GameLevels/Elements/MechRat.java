package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import java.util.Arrays;
import java.util.List;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;
import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/** The MechRat class extends the PhysObject class to take advantage of the
 *  postSolve() method that gets called when it collides with another
 *  physics-enabled object. If the force is great enough, the MechRat is
 *  destroyed, and previously loaded particle effects are immediately shown.
 *  The MechRat also has wheels connected by revolute joints, which add to
 *  the challenge of destroying the MechRat.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MechRat extends PhysObject<Sprite> {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final int mPOINTS_FOR_DESTROYING = 250;
	private static final float mMECH_RAT_OFFSET_Y = 102f;
	private static final float mWHEELS_ANGULAR_DAMPING = 2f;
	private static final float mWHEELS_MAX_MOTOR_TORQUE = 1000f;
	private static final float mWHEELS_RADIUS = 30f;
	private static final float mWHEELS_OFFSET_Y = 75.7f;
	private static final float mRIGHT_WHEEL_OFFSET_X = 64.1f;
	private static final float mLEFT_WHEEL_OFFSET_X = 35.9f;
	
	private static final float mDESTRUCTION_IMPULSE_LIMIT = 20f;
	private static final int mMINIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR = 1;
	private static final int mMAXIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR = 3;
	private static final float mEXPLOSION_EFFECT_SCALE = 12f;
	
	private static final float mMECH_RAT_DENSITY = 10f;
	private static final float mMECH_RAT_ELASTICITY = 0.1f;
	private static final float mMECH_RAT_FRICTION = 0.5f;
	private static final float mWHEELS_DENSITY = 90f;
	private static final float mWHEELS_ELASTICITY = 0.4f;
	private static final float mWHEELS_FRICTION = 2f;
	private static final FixtureDef mMECH_RAT_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mMECH_RAT_DENSITY, mMECH_RAT_ELASTICITY, mMECH_RAT_FRICTION);
	private static final FixtureDef mWHEELS_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mWHEELS_DENSITY, mWHEELS_ELASTICITY, mWHEELS_FRICTION);
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private final GameLevel mGameLevel;
	
	private final Body mMechRatBody;
	private final Body mLeftWheelBody;
	private final Body mRightWheelBody;
	
	// EFFECTS
	private float mParticleVelocityX;
	private float mParticleVelocityY;
	private final ExplosionParticleSystem Gears1 = new ExplosionParticleSystem(MathUtils.random(MechRat.mMINIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR, MechRat.mMAXIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR), ResourceManager.gameGear1TR);
	private final ExplosionParticleSystem Gears2 = new ExplosionParticleSystem(MathUtils.random(MechRat.mMINIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR, MechRat.mMAXIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR), ResourceManager.gameGear2TR);
	private final ExplosionParticleSystem Gears3 = new ExplosionParticleSystem(MathUtils.random(MechRat.mMINIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR, MechRat.mMAXIMUM_NUMBER_OF_EACH_TYPE_OF_GEAR), ResourceManager.gameGear3TR);
	private final PointParticleEmitter SmokeEmitter = new PointParticleEmitter(0f, 0f);
	private final SpriteParticleSystem SmokearticleSystem;
	private final AnimatedSprite ratExplosion;
	
	// ====================================================
	// TRIANGULATED MESH
	// ====================================================
	private static final List<Vector2> mMECH_RAT_TRIANGULATED_MESH_VECTOR2_LIST = 
			Arrays.asList(
					new Vector2(0.9480932f, -2.9943874f), new Vector2(0.22321151f, -2.8505373f), new Vector2(-0.44874856f, -2.388081f),
					new Vector2(-0.44874856f, -2.388081f), new Vector2(-1.1314082f, -2.8663905f), new Vector2(-1.4825866f, -2.9501936f),
					new Vector2(-0.44874856f, -2.388081f), new Vector2(-1.4825866f, -2.9501936f), new Vector2(-1.8361397f, -2.8507562f),
					new Vector2(-0.44874856f, -2.388081f), new Vector2(-1.8361397f, -2.8507562f), new Vector2(-1.6151688f, -1.8011436f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-0.44874856f, -2.388081f), new Vector2(-1.6151688f, -1.8011436f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-1.6151688f, -1.8011436f), new Vector2(-2.2117903f, -1.0387936f),
					new Vector2(-2.2117903f, -1.0387936f), new Vector2(-2.9520428f, -1.0387936f), new Vector2(-3.5597124f, -0.607903f),
					new Vector2(-2.2117903f, -1.0387936f), new Vector2(-3.5597124f, -0.607903f), new Vector2(-3.5376153f, -0.27644673f),
					new Vector2(-2.2117903f, -1.0387936f), new Vector2(-3.5376153f, -0.27644673f), new Vector2(-1.9245281f, 0.23178765f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-2.2117903f, -1.0387936f), new Vector2(-1.9245281f, 0.23178765f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-1.9245281f, 0.23178765f), new Vector2(-1.4273437f, 0.41961265f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-1.4273437f, 0.41961265f), new Vector2(-1.3831494f, 0.8063126f),
					new Vector2(-1.3831494f, 0.8063126f), new Vector2(-1.106936f, 0.9609908f), new Vector2(-0.37773192f, 0.7621189f),
					new Vector2(-1.3831494f, 0.8063126f), new Vector2(-0.37773192f, 0.7621189f), new Vector2(-0.3003922f, 0.2759814f),
					new Vector2(-1.3831494f, 0.8063126f), new Vector2(-0.3003922f, 0.2759814f), new Vector2(-0.6539457f, -0.0f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-1.3831494f, 0.8063126f), new Vector2(-0.6539457f, -0.0f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(-0.6539457f, -0.0f), new Vector2(0.36252028f, -0.5637061f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(0.36252028f, -0.5637061f), new Vector2(1.8319768f, -0.08861861f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(1.8319768f, -0.08861861f), new Vector2(2.859491f, -0.12176548f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(2.859491f, -0.12176548f), new Vector2(3.2019963f, 0.30912828f),
					new Vector2(3.2240932f, 2.5077877f), new Vector2(2.9810252f, 2.861341f), new Vector2(3.3787727f, 2.982875f),
					new Vector2(3.2019963f, 0.30912828f), new Vector2(3.2240932f, 2.5077877f), new Vector2(3.3787727f, 2.982875f),
					new Vector2(3.2019963f, 0.30912828f), new Vector2(3.3787727f, 2.982875f), new Vector2(3.3787727f, 2.5740788f),
					new Vector2(3.2019963f, 0.30912828f), new Vector2(3.3787727f, 2.5740788f), new Vector2(3.5555494f, 1.8448751f),
					new Vector2(3.2019963f, 0.30912828f), new Vector2(3.5555494f, 1.8448751f), new Vector2(3.5776465f, 0.342272f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(3.2019963f, 0.30912828f), new Vector2(3.5776465f, 0.342272f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(3.5776465f, 0.342272f), new Vector2(3.1467535f, -0.44217485f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(3.1467535f, -0.44217485f), new Vector2(3.5003068f, -0.7625811f),
					new Vector2(3.5003068f, -0.7625811f), new Vector2(3.6107922f, -1.5249311f), new Vector2(3.2130446f, -2.0331624f),
					new Vector2(3.5003068f, -0.7625811f), new Vector2(3.2130446f, -2.0331624f), new Vector2(2.8152971f, -1.8563874f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(3.5003068f, -0.7625811f), new Vector2(2.8152971f, -1.8563874f),
					new Vector2(0.9480932f, -2.9943874f), new Vector2(2.8152971f, -1.8563874f), new Vector2(1.8761709f, -2.7844625f));
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MechRat(final float pX, float pY, final GameLevel pGameLevel) {
		this.mGameLevel = pGameLevel;
		this.mGameLevel.mNumberEnemiesLeft++;
		this.mGameLevel.mBasePositions.add(new float[] {pX, pY});
		
		final Sprite mechRatSprite = new Sprite(pX, pY + mMECH_RAT_OFFSET_Y, ResourceManager.gameMechRatTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(MechRat.this.mMechRatBody != null) {
					MechRat.this.mGameLevel.reportBaseBodySpeed(MechRat.this.mMechRatBody.getLinearVelocity().len2());
				}
				if(this.getX() > (MechRat.this.mGameLevel.mBasePositionX + (ResourceManager.getEngine().getCamera().getWidth() / 2f))) {
					MechRat.this.destroyMechRat();
				}
			}
		};
		
		this.mMechRatBody = PhysicsFactory.createTrianglulatedBody(this.mGameLevel.mPhysicsWorld, mechRatSprite, mMECH_RAT_TRIANGULATED_MESH_VECTOR2_LIST, BodyType.DynamicBody, mMECH_RAT_FIXTURE_DEF);
		final PhysicsConnector physConnector = new PhysicsConnector(mechRatSprite, this.mMechRatBody);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(physConnector);
		
		final Sprite mechRatLeftWheelSprite = new Sprite(mechRatSprite.getX() - mLEFT_WHEEL_OFFSET_X, mechRatSprite.getY() - mWHEELS_OFFSET_Y, ResourceManager.gameMechRatWheelTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		pGameLevel.attachChild(mechRatLeftWheelSprite);
		this.mLeftWheelBody = PhysicsFactory.createCircleBody(this.mGameLevel.mPhysicsWorld, mechRatLeftWheelSprite.getX(), mechRatLeftWheelSprite.getY(), mWHEELS_RADIUS, BodyType.DynamicBody, mWHEELS_FIXTURE_DEF);
		this.mLeftWheelBody.setSleepingAllowed(false);
		this.mLeftWheelBody.setAngularDamping(mWHEELS_ANGULAR_DAMPING);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mechRatLeftWheelSprite, this.mLeftWheelBody));
		
		final RevoluteJointDef revJointDef = new RevoluteJointDef();
		revJointDef.initialize(this.mMechRatBody, this.mLeftWheelBody, this.mLeftWheelBody.getWorldCenter());
		revJointDef.enableMotor = true;
		revJointDef.maxMotorTorque = mWHEELS_MAX_MOTOR_TORQUE;
		this.mGameLevel.mPhysicsWorld.createJoint(revJointDef);
		
		final Sprite mechRatRightWheelSprite = new Sprite(mechRatSprite.getX() + mRIGHT_WHEEL_OFFSET_X, mechRatSprite.getY() - mWHEELS_OFFSET_Y, ResourceManager.gameMechRatWheelTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		pGameLevel.attachChild(mechRatRightWheelSprite);
		this.mRightWheelBody = PhysicsFactory.createCircleBody(this.mGameLevel.mPhysicsWorld, mechRatRightWheelSprite.getX(), mechRatRightWheelSprite.getY(), mWHEELS_RADIUS, BodyType.DynamicBody, mWHEELS_FIXTURE_DEF);
		this.mRightWheelBody.setSleepingAllowed(false);
		this.mRightWheelBody.setAngularDamping(mWHEELS_ANGULAR_DAMPING);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mechRatRightWheelSprite, this.mRightWheelBody));
		
		revJointDef.initialize(this.mMechRatBody, this.mRightWheelBody, this.mRightWheelBody.getWorldCenter());
		this.mGameLevel.mPhysicsWorld.createJoint(revJointDef);
		
		pGameLevel.attachChild(mechRatSprite);
		
		this.set(this.mMechRatBody, mechRatSprite, physConnector, this.mGameLevel);
		this.mGameLevel.TotalScorePossible += mPOINTS_FOR_DESTROYING;
		
		this.SmokearticleSystem = new SpriteParticleSystem(this.SmokeEmitter, 3, 3, 3, ResourceManager.gameWhiteSmokeTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			SpriteParticleSystem ThisSpriteParticleSystem = this;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
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
			}
		};
		this.SmokearticleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0.5f));
		this.SmokearticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(this.mParticleVelocityX - 20f, this.mParticleVelocityX + 20f, this.mParticleVelocityY - 20f, this.mParticleVelocityY + 20f));
		this.SmokearticleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0f, 360f));
		this.SmokearticleSystem.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0f));
		this.SmokearticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(2f));
		this.SmokearticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(1f, 1.1f, 0.0f, 2.0f));
		this.SmokearticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(1.1f, 2f, 2.0f, 4.0f));
		this.SmokearticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(1f, 2f, 0.5f, 0f));
		this.SmokearticleSystem.addParticleModifier(new OffCameraExpireParticleModifier<Sprite>(ResourceManager.getEngine().getCamera()));
		
		
		this.ratExplosion = new AnimatedSprite(this.mEntity.getX(), this.mEntity.getY(), ResourceManager.gameExplosionTTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.ratExplosion.setBlendFunction(GLES20.GL_SRC_COLOR, GLES20.GL_ONE);
		this.ratExplosion.setBlendingEnabled(true);
		this.ratExplosion.setScale(mEXPLOSION_EFFECT_SCALE);
		this.ratExplosion.animate(30, new IAnimationListener() {
			@Override
			public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {}
			
			@Override
			public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex) {}
			
			@Override
			public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount) {
				final AnimatedSprite animSprite = pAnimatedSprite;
				animSprite.stopAnimation();
				ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						animSprite.detachSelf();
						if(!animSprite.isDisposed()) {
							animSprite.dispose();
						}
					}
				});
			}
			
			@Override
			public void onAnimationStarted(final AnimatedSprite pAnimatedSprite, final int pInitialLoopCount) {}
		});
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	private void destroyMechRat() {
		if(!this.mIsDestroyed) {
			this.mGameLevel.addPointsToScore(this.mEntity, mPOINTS_FOR_DESTROYING);
			this.destroy();
			this.mGameLevel.mNumberEnemiesLeft--;
			
			this.SmokeEmitter.setCenter(this.mEntity.getX(), this.mEntity.getY());
			this.mGameLevel.attachChild(this.SmokearticleSystem);
			this.SmokearticleSystem.onUpdate(1f);
			this.SmokearticleSystem.setParticlesSpawnEnabled(false);
			
			SFXManager.playExplosion(1f, 1f);
			
			this.Gears1.run();
			this.Gears2.run();
			this.Gears3.run();
			
			this.ratExplosion.setPosition(this.mEntity.getX(), this.mEntity.getY());
			this.mGameLevel.attachChild(this.ratExplosion);
		}
	}
	
	@Override
	public void onBeginContact(final Contact pContact) {}
	
	@Override
	public void onEndContact(final Contact pContact) {}
	
	@Override
	public void onPostSolve(final float pMaxImpulse) {
		if(this.mGameLevel.mIsLevelSettled) {
			if(pMaxImpulse > mDESTRUCTION_IMPULSE_LIMIT) {
				this.destroyMechRat();
			}
		}
	}
	
	@Override
	public void onPreSolve(final Contact pContact, final Manifold pOldManifold) {
		this.mParticleVelocityX = -this.mBody.getLinearVelocity().x;
		this.mParticleVelocityY = -this.mBody.getLinearVelocity().y;
	}
	
	public class ExplosionParticleSystem {
		
		final PointParticleEmitter emitter = new PointParticleEmitter(0f, 0f);
		public final SpriteParticleSystem ThisSpriteParticleSystem;
		
		public ExplosionParticleSystem(final int pNumGears, final ITextureRegion pTextureRegion) {
			this.ThisSpriteParticleSystem = new SpriteParticleSystem(this.emitter, pNumGears, pNumGears, pNumGears, pTextureRegion, ResourceManager.getActivity().getVertexBufferObjectManager()) {
				boolean hasLoaded = false;
				
				@Override
				protected void onManagedUpdate(final float pSecondsElapsed) {
					super.onManagedUpdate(pSecondsElapsed);
					if(this.hasLoaded) {
						if(this.mParticlesAlive == 0) {
							ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									if(ExplosionParticleSystem.this.ThisSpriteParticleSystem != null) {
										ExplosionParticleSystem.this.ThisSpriteParticleSystem.detachSelf();
										if(!ExplosionParticleSystem.this.ThisSpriteParticleSystem.isDisposed()) {
											ExplosionParticleSystem.this.ThisSpriteParticleSystem.dispose();
										}
									}
								}
							});
						}
					} else {
						this.hasLoaded = true;
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0f, 360f));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0f));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(3f));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(1f, 3f, 1f, 1.3f));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3f, 1f, 0f));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.addParticleModifier(new OffCameraExpireParticleModifier<Sprite>(ResourceManager.getEngine().getCamera()));
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.onUpdate(1f);
						for(final Particle<Sprite> curParticle : this.mParticles) {
							curParticle.getPhysicsHandler().setVelocity(MathUtils.random(-200f, 200f), MathUtils.random(200f, 1000f));
							curParticle.getPhysicsHandler().setAccelerationY(-SensorManager.GRAVITY_EARTH * 120);
						}
						ExplosionParticleSystem.this.ThisSpriteParticleSystem.setParticlesSpawnEnabled(false);
					}
				}
			};
			
		}
		
		public void run() {
			this.emitter.setCenter(MechRat.this.mEntity.getX(), MechRat.this.mEntity.getY());
			MechRat.this.mGameLevel.attachChild(this.ThisSpriteParticleSystem);
		}
		
	}
}