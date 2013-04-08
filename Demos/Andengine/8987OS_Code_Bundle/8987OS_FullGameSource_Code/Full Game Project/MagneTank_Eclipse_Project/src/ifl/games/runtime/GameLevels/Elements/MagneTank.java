package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Input.BoundTouchInput;
import ifl.games.runtime.Managers.ResourceManager;
import ifl.games.runtime.Managers.SFXManager;

import java.util.Arrays;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.LineJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/** The MagneTank class creates and controls the vehicle that the game is
 *  based on. It pieces together Box2D bodies using joints to create the
 *  physics-enabled vehicle, and uses the player’s input, via
 *  BoundTouchInputs, to control how each part of the vehicle moves
 *  and functions. 
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MagneTank implements IUpdateHandler {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	public static final float mSHOOTING_POWER_MINIMUM = 80f;
	public static final float mSHOOTING_POWER_MAXIMUM = 400f;
	
	private static final float mWHEEL_VERTICAL_DISTANCE_FROM_VEHICLE_CENTER = 109.64f;
	private static final float mWHEEL_HORIZONTAL_DISTANCE_FROM_VEHICLE_CENTER = 146.74f;
	private static final float mWHEEL_ANGULAR_VELOCITY_MAX = 8f;
	private static final float mVEHICLE_MOVE_INPUT_SENSITIVITY = 200f;
	private static final float mVEHICLE_MOVE_MAXIMUM_TOUCH_DISTANCE_FROM_VEHICLE = 100f;
	
	private static final float mWHEEL_BODY_RADIUS = 80f;
	private static final float mWHEELS_ANGULAR_DAMPING = 2f;
	private static final float mWHEEL_SHOCK_LENGTH = 21f;
	private static final float mWHEEL_SHOCK_FREQUENCY = 3f;
	private static final float mWHEEL_SHOCK_DAMPING_RATIO = 0.7f;
	
	private static final float mCAMERA_LOWER_BOUND_FROM_VEHICLE_X = -650f;
	private static final float mCAMERA_LOWER_BOUND_FROM_VEHICLE_Y = -512f;
	
	private static final float mTURRETCONNECTION_OFFSET_CENTER_X = -0.6031171875f;
	private static final float mTURRETCONNECTION_OFFSET_CENTER_Y = 0.0926484375f;
	
	private static final float mTURRET_SHOOT_GRABBED_OBJECT_IF_WITHIN_RANGE = 30f;
	private static final float mTURRET_ROTATION_DRAG = 4f;
	private static final float mTURRET_SPRITE_OFFSET_FROM_VEHICLE_CENTER_X = 239.01f;
	private static final float mTURRET_SPRITE_OFFSET_FROM_VEHICLE_CENTER_Y = 82.681f;
	private static final float mTURRET_JOINT_LOWER_ANGLE = -0.5585f;
	private static final float mTURRET_JOINT_UPPER_ANGLE = 2.1817f;
	private static final float mTURRET_BODY_CENTER_OF_GRAVITY_X = -1.56234375f;
	private static final float mTURRET_BODY_CENTER_OF_GRAVITY_Y = 0f;
	private static final float mTURRET_JOINT_MAX_MOTOR_TORQUE = 5000000000f;
	private static final float mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_X = 10f;
	private static final float mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_Y = 0f;
	
	private static final float mVEHICLE_DENSITY = 300f;
	private static final float mVEHICLE_ELASTICITY = 0.1f;
	private static final float mVEHICLE_FRICTION = 0.5f;
	private static final float mTURRET_DENSITY = 10f;
	private static final float mTURRET_ELASTICITY = 0.1f;
	private static final float mTURRET_FRICTION = 0.5f;
	private static final float mWHEELS_DENSITY = 1220f;
	private static final float mWHEELS_ELASTICITY = 0.5f;
	private static final float mWHEELS_FRICTION = 5f;
	private static final FixtureDef mVEHICLE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mVEHICLE_DENSITY, mVEHICLE_ELASTICITY, mVEHICLE_FRICTION);
	private static final FixtureDef mTURRET_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mTURRET_DENSITY, mTURRET_ELASTICITY, mTURRET_FRICTION);
	private static final FixtureDef mWHEELS_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mWHEELS_DENSITY, mWHEELS_ELASTICITY, mWHEELS_FRICTION);
	
	private static final Vector2 mVECTOR2_ZERO = new Vector2(0f, 0f);
	private static final Vector2 mVECTOR2_VERTICAL_AXIS = new Vector2(0f, 1f);
	
	private static final float mSHADOW_OFFSET_X = 0f;
	private static final float mSHADOW_OFFSET_Y = -215f;
	private static final float mSHADOW_MAX_ALPHA = 0.5f;
	private static final float mSHADOW_MIN_ALPHA = 0.1f;
	private static final float mSHADOW_MAX_ALPHA_HEIGHT = 200f;
	private static final float mSHADOW_MIN_ALPHA_HEIGHT = 0f;
	private static final float mSHADOW_ALPHA_HEIGHT_DIFFERENCE = mSHADOW_MAX_ALPHA_HEIGHT - mSHADOW_MIN_ALPHA_HEIGHT;
	private static final float mSHADOW_ALPHA_DIFFERENCE = mSHADOW_MAX_ALPHA - mSHADOW_MIN_ALPHA;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public final GameLevel mGameLevel;
	public float mShootingPower;
	public final Sprite mVehicleSprite;
	public boolean mTurretMagnetOn = true;
	public MagneticPhysObject<?> mGrabbedMagneticObject;
	
	private final Sprite mTurretSprite;
	private final Sprite mLeftWheelSprite;
	private final Sprite mRightWheelSprite;
	private final Sprite mLeftWheelShadowSprite;
	private final Sprite mRightWheelShadowSprite;
	private final Sprite mTurretConnectionSprite;
	private final Body mLeftWheelBody;
	private final Body mRightWheelBody;
	private final Body mVehicleBody;
	private final Body mTurretBody;
	private final Rectangle mTurretSensorRectangle;
	private final RevoluteJoint mTurretRevoluteJoint;
	private float mWheelAngularVelocity;
	private float mTurretDesiredAngle;
	
	private final float mCHARACTER_START_X;
	private final float mCHARACTER_START_Y;
	private final float mSHADOW_START_X;
	private final float mSHADOW_START_Y;
	private final float mCHARACTER_SHADOW_Y_DIFFERENCE;
	private final Sprite mShadowSprite;
	
	// ====================================================
	// TRIANGULATED MESHES
	// ====================================================
	private static final List<Vector2> mVEHICLE_TRIANGULATED_MESH_VECTOR2_LIST = Arrays.asList(
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-7.212419f, -1.6279812f), new Vector2(-7.1880784f, -0.3379345f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-7.1880784f, -0.3379345f), new Vector2(-5.094796f, 0.29491854f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-5.094796f, 0.29491854f), new Vector2(-3.6830463f, 0.44095945f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-3.6830463f, 0.44095945f), new Vector2(-2.733767f, 0.19755602f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-2.733767f, 0.19755602f), new Vector2(-1.200316f, 0.17321539f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(-1.200316f, 0.17321539f), new Vector2(0.13841152f, 0.61134386f),
			new Vector2(1.0390091f, 1.5119438f), new Vector2(1.842246f, 2.777647f), new Vector2(2.5237799f, 3.313139f),
			new Vector2(1.0390091f, 1.5119438f), new Vector2(2.5237799f, 3.313139f), new Vector2(3.5460806f, 3.4591823f),
			new Vector2(0.13841152f, 0.61134386f), new Vector2(1.0390091f, 1.5119438f), new Vector2( 3.5460806f, 3.4591823f),
			new Vector2(0.13841152f, 0.61134386f), new Vector2(3.5460806f, 3.4591823f), new Vector2(5.055191f, 3.4105015f),
			new Vector2(0.13841152f, 0.61134386f), new Vector2(5.055191f, 3.4105015f), new Vector2(6.2965555f, 1.6823249f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(0.13841152f, 0.61134386f), new Vector2(6.2965555f, 1.6823249f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(6.2965555f, 1.6823249f), new Vector2(5.444639f, 0.24623728f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(5.444639f, 0.24623728f), new Vector2(7.00243f, -0.24057198f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2(7.00243f, -0.24057198f), new Vector2(7.197154f, -1.7253437f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2( 7.197154f, -1.7253437f), new Vector2(6.9050684f, -3.1370907f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2( 6.9050684f, -3.1370907f), new Vector2(1.4771385f, -3.4291782f),
			new Vector2(-6.8716536f, -3.1370907f), new Vector2( 1.4771385f, -3.4291782f), new Vector2(-1.6627855f, -3.4291782f));
	
	private static final List<Vector2> mTURRET_TRIANGULATED_MESH_VECTOR2_LIST = Arrays.asList(
			new Vector2(3.5114002f, 1.8913035f), new Vector2(3.2360096f, 1.0927284f), new Vector2(3.0983143f, 1.0590129f),
			new Vector2(3.1334705f, 0.7199316f), new Vector2(3.2535877f, 0.70259213f), new Vector2(3.5211658f, 0.02539277f),
			new Vector2(3.5211658f, 0.02539277f), new Vector2(3.531908f, 0.02539277f), new Vector2(3.5270252f, 0.012870073f),
			new Vector2(3.1334705f, 0.7199316f), new Vector2(3.5211658f, 0.02539277f), new Vector2(3.5270252f, 0.012870073f),
			new Vector2(3.1334705f, 0.7199316f), new Vector2(3.5270252f, 0.012870073f), new Vector2(3.5289783f, 0.00709033f),
			new Vector2(3.1334705f, 0.7199316f), new Vector2(3.5289783f, 0.00709033f), new Vector2(3.525072f, 0.00709033f),
			new Vector2(3.1334705f, 0.7199316f), new Vector2(3.525072f, 0.00709033f), new Vector2(3.2565174f, -0.67010903f),
			new Vector2(3.0983143f, 1.0590129f), new Vector2(3.1334705f, 0.7199316f), new Vector2(3.2565174f, -0.67010903f),
			new Vector2(3.0983143f, 1.0590129f), new Vector2(3.2565174f, -0.67010903f), new Vector2(3.1354237f, -0.6874484f),
			new Vector2(3.0983143f, 1.0590129f), new Vector2(3.1354237f, -0.6874484f), new Vector2(3.101244f, -1.0265297f),
			new Vector2(3.101244f, -1.0265297f), new Vector2(3.2389393f, -1.0612087f), new Vector2(3.51433f, -1.8588203f),
			new Vector2(3.101244f, -1.0265297f), new Vector2(3.51433f, -1.8588203f), new Vector2(1.3795643f, -1.7403347f),
			new Vector2(3.0983143f, 1.0590129f), new Vector2(3.101244f, -1.0265297f), new Vector2(1.3795643f, -1.7403347f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(3.0983143f, 1.0590129f), new Vector2(1.3795643f, -1.7403347f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(1.3795643f, -1.7403347f), new Vector2(1.328783f, -1.6045094f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(1.328783f, -1.6045094f), new Vector2(-0.15168595f, -1.58717f),
			new Vector2(-0.15168595f, -1.58717f), new Vector2(-0.20344377f, -1.7229953f), new Vector2(-1.5628188f, -1.7056559f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-0.15168595f, -1.58717f), new Vector2(-1.5628188f, -1.7056559f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-1.5628188f, -1.7056559f), new Vector2(-2.544264f, -1.5534544f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-2.544264f, -1.5534544f), new Vector2(-3.3011f, -0.856989f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-3.3011f, -0.856989f), new Vector2(-3.5100844f, -0.17978966f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-3.5100844f, -0.17978966f), new Vector2(-3.5081313f, 0.21227288f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-3.5081313f, 0.21227288f), new Vector2(-3.3040297f, 0.88947225f),
			new Vector2(-3.3040297f, 0.88947225f), new Vector2(-2.5462172f, 1.5859375f), new Vector2(-1.5657485f, 1.7381387f),
			new Vector2(-3.3040297f, 0.88947225f), new Vector2(-1.5657485f, 1.7381387f), new Vector2(-0.20637345f, 1.7554781f),
			new Vector2(-3.3040297f, 0.88947225f), new Vector2(-0.20637345f, 1.7554781f), new Vector2(-0.15461564f, 1.6196532f),
			new Vector2(-3.3040297f, 0.88947225f), new Vector2(-0.15461564f, 1.6196532f), new Vector2(1.3258533f, 1.636029f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(-3.3040297f, 0.88947225f), new Vector2(1.3258533f, 1.636029f),
			new Vector2(3.5114002f, 1.8913035f), new Vector2(1.3258533f, 1.636029f), new Vector2(1.3776112f, 1.7718544f));
	
	// ====================================================
	// BOUND TOUCH INPUTS
	// ====================================================
	private final BoundTouchInput mVehicleMovementBoundTouchInput = new BoundTouchInput() {
		@Override
		protected boolean onActionDown(final float pX, final float pY) {
			if(MathUtils.distance(pX, pY, MagneTank.this.mVehicleSprite.getX(), MagneTank.this.mVehicleSprite.getY()) > Math.max(MagneTank.mVEHICLE_MOVE_MAXIMUM_TOUCH_DISTANCE_FROM_VEHICLE, MagneTank.mVEHICLE_MOVE_MAXIMUM_TOUCH_DISTANCE_FROM_VEHICLE / ResourceManager.getCamera().getZoomFactor()))
				return false;
			return true;
		}
		
		@Override
		protected void onActionMove(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {
			if(pX < pStartX)
				MagneTank.this.mWheelAngularVelocity = Math.min((pStartX - pX) / mVEHICLE_MOVE_INPUT_SENSITIVITY, mWHEEL_ANGULAR_VELOCITY_MAX);
			else
				MagneTank.this.mWheelAngularVelocity = Math.max((pStartX - pX) / mVEHICLE_MOVE_INPUT_SENSITIVITY, -mWHEEL_ANGULAR_VELOCITY_MAX);
		}
		
		@Override
		protected void onActionUp(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {
			MagneTank.this.mWheelAngularVelocity = 0f;
			MagneTank.this.mLeftWheelBody.setAngularVelocity(0f);
			MagneTank.this.mRightWheelBody.setAngularVelocity(0f);
		}
	};
	
	private final BoundTouchInput mTurretMovementBoundTouchInput = new BoundTouchInput() {
		final float MaxTapDistance = 12f; // scaled to camera
		final float MaxTapTurretAngleChange = 4f * MathConstants.DEG_TO_RAD;
		float PreviousTurretAngle = 0f;
		
		@Override
		protected boolean onActionDown(final float pX, final float pY) {
			this.PreviousTurretAngle = MagneTank.this.mTurretRevoluteJoint.getJointAngle();
			final float touchAngleFromTurret = ((float) Math.atan2((pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT) - MagneTank.this.mTurretRevoluteJoint.getAnchorA().y, (pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT) - MagneTank.this.mTurretRevoluteJoint.getAnchorA().x));
			MagneTank.this.setMangeTankTurretDesiredAngle(touchAngleFromTurret);
			return true;
		}
		
		@Override
		protected void onActionMove(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {
			final float touchAngleFromTurret = ((float) Math.atan2((pY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT) - MagneTank.this.mTurretRevoluteJoint.getAnchorA().y, (pX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT) - MagneTank.this.mTurretRevoluteJoint.getAnchorA().x));
			if(touchAngleFromTurret > (mTURRET_JOINT_LOWER_ANGLE + MagneTank.this.mVehicleBody.getAngle()) && touchAngleFromTurret < (mTURRET_JOINT_UPPER_ANGLE + MagneTank.this.mVehicleBody.getAngle()))
				MagneTank.this.setMangeTankTurretDesiredAngle(touchAngleFromTurret);
		}
		
		@Override
		protected void onActionUp(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {
			if(MagneTank.this.mTurretRevoluteJoint.getJointAngle() < (this.PreviousTurretAngle + this.MaxTapTurretAngleChange))
				if(MagneTank.this.mTurretRevoluteJoint.getJointAngle() > (this.PreviousTurretAngle - this.MaxTapTurretAngleChange)) {
					final float MaxTapDistanceScaled = this.MaxTapDistance / ResourceManager.getCamera().getZoomFactor();
					if(pX > (pStartX - (MaxTapDistanceScaled)))
						if(pX < (pStartX + (MaxTapDistanceScaled)))
							if(pY > (pStartY - (MaxTapDistanceScaled)))
								if(pY < (pStartY + (MaxTapDistanceScaled)))
									if(MagneTank.this.mTurretMagnetOn)
										MagneTank.this.requestShoot();
				}
		}
	};
	
	private final BoundTouchInput mTurretShootingBoundTouchInput = new BoundTouchInput() {
		@Override
		protected boolean onActionDown(final float pX, final float pY) {
			if(MagneTank.this.mTurretMagnetOn)
				MagneTank.this.requestShoot();
			return true;
		}
		
		@Override
		protected void onActionMove(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {}
		
		@Override
		protected void onActionUp(final float pX, final float pY, final float pPreviousX, final float pPreviousY, final float pStartX, final float pStartY) {}
	};
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MagneTank(final float pX, final float pY, final GameLevel pGameLevel) {
		this.mCHARACTER_START_X = pX;
		this.mCHARACTER_START_Y = pY;
		this.mGameLevel = pGameLevel;
		this.mSHADOW_START_X = this.mCHARACTER_START_X + mSHADOW_OFFSET_X;
		this.mSHADOW_START_Y = this.mCHARACTER_START_Y + mSHADOW_OFFSET_Y;
		this.mCHARACTER_SHADOW_Y_DIFFERENCE = this.mCHARACTER_START_Y - this.mSHADOW_START_Y;
		
		this.mShadowSprite = new Sprite(this.mSHADOW_START_X, this.mSHADOW_START_Y, ResourceManager.gameMagneTankShadowTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mGameLevel.attachChild(this.mShadowSprite);
		
		this.mVehicleSprite = new Sprite(this.mCHARACTER_START_X, this.mCHARACTER_START_Y, ResourceManager.gameMagneTankBodyTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			public void setPosition(final float pX, final float pY) {
				super.setPosition(pX, pY);
				MagneTank.this.mShadowSprite.setPosition(pX + mSHADOW_OFFSET_X, MagneTank.this.mShadowSprite.getY());
				MagneTank.this.updateShadowAlpha();
			}
		};
		this.mTurretSprite = new Sprite(this.mVehicleSprite.getX() + mTURRET_SPRITE_OFFSET_FROM_VEHICLE_CENTER_X, this.mVehicleSprite.getY() + mTURRET_SPRITE_OFFSET_FROM_VEHICLE_CENTER_Y, ResourceManager.gameMagneTankTurretTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mTurretSensorRectangle = new Rectangle(5238f, 64f, 10000f, 100f, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mTurretSensorRectangle.setAlpha(0f);
		this.mTurretSprite.attachChild(this.mTurretSensorRectangle);
		
		this.mLeftWheelSprite = new Sprite(this.mVehicleSprite.getX() - mWHEEL_HORIZONTAL_DISTANCE_FROM_VEHICLE_CENTER, this.mVehicleSprite.getY() - mWHEEL_VERTICAL_DISTANCE_FROM_VEHICLE_CENTER, ResourceManager.gameMagneTankWheelTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mRightWheelSprite = new Sprite(this.mVehicleSprite.getX() + mWHEEL_HORIZONTAL_DISTANCE_FROM_VEHICLE_CENTER, this.mVehicleSprite.getY() - mWHEEL_VERTICAL_DISTANCE_FROM_VEHICLE_CENTER, ResourceManager.gameMagneTankWheelTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mLeftWheelShadowSprite = new Sprite(this.mLeftWheelSprite.getWidth() / 2f, this.mLeftWheelSprite.getHeight() / 2f, ResourceManager.gameMagneTankWheelShadowTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(this.getRotation() != -MagneTank.this.mLeftWheelSprite.getRotation()) {
					this.setRotation(-MagneTank.this.mLeftWheelSprite.getRotation());
				}
			}
		};
		this.mRightWheelShadowSprite = new Sprite(this.mRightWheelSprite.getWidth() / 2f, this.mRightWheelSprite.getHeight() / 2f, ResourceManager.gameMagneTankWheelShadowTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(this.getRotation() != -MagneTank.this.mRightWheelSprite.getRotation()) {
					this.setRotation(-MagneTank.this.mRightWheelSprite.getRotation());
				}
			}
		};
		
		this.mLeftWheelSprite.attachChild(this.mLeftWheelShadowSprite);
		this.mRightWheelSprite.attachChild(this.mRightWheelShadowSprite);
		
		mTurretConnectionSprite = new Sprite(0f,0f, ResourceManager.gameMagneTankTurretConnectionTR, ResourceManager.getActivity().getVertexBufferObjectManager()) {
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(this.getRotation() != MagneTank.this.mVehicleSprite.getRotation()) {
					this.setRotation(MagneTank.this.mVehicleSprite.getRotation());
				}
				if((this.getX() != MagneTank.this.mVehicleSprite.getX()) || (this.getY() != MagneTank.this.mVehicleSprite.getY())) {
					this.setPosition(MagneTank.this.mVehicleSprite);
				}
			}
		};
		mTurretConnectionSprite.setAnchorCenter(mTURRETCONNECTION_OFFSET_CENTER_X, mTURRETCONNECTION_OFFSET_CENTER_Y);
		
		this.mGameLevel.attachChild(this.mTurretConnectionSprite);
		this.mGameLevel.attachChild(this.mTurretSprite);
		this.mGameLevel.attachChild(this.mVehicleSprite);
		this.mGameLevel.attachChild(this.mLeftWheelSprite);
		this.mGameLevel.attachChild(this.mRightWheelSprite);
		
		this.mVehicleBody = PhysicsFactory.createTrianglulatedBody(this.mGameLevel.mPhysicsWorld, this.mVehicleSprite, mVEHICLE_TRIANGULATED_MESH_VECTOR2_LIST, BodyType.DynamicBody, mVEHICLE_FIXTURE_DEF);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mVehicleSprite, this.mVehicleBody));
		
		this.mTurretBody = PhysicsFactory.createTrianglulatedBody(this.mGameLevel.mPhysicsWorld, this.mTurretSprite, mTURRET_TRIANGULATED_MESH_VECTOR2_LIST, BodyType.DynamicBody, mTURRET_FIXTURE_DEF);
		this.mTurretBody.setSleepingAllowed(false);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mTurretSprite, this.mTurretBody));
		final MassData magneTankTurretMassData = this.mTurretBody.getMassData();
		magneTankTurretMassData.center.set(mTURRET_BODY_CENTER_OF_GRAVITY_X, mTURRET_BODY_CENTER_OF_GRAVITY_Y);
		this.mTurretBody.setMassData(magneTankTurretMassData);
		
		final RevoluteJointDef magneTankTurretRevoluteJointDef = new RevoluteJointDef();
		magneTankTurretRevoluteJointDef.initialize(this.mVehicleBody, this.mTurretBody, this.mTurretBody.getWorldCenter());
		magneTankTurretRevoluteJointDef.enableMotor = true;
		magneTankTurretRevoluteJointDef.maxMotorTorque = mTURRET_JOINT_MAX_MOTOR_TORQUE;
		magneTankTurretRevoluteJointDef.enableLimit = true;
		magneTankTurretRevoluteJointDef.lowerAngle = mTURRET_JOINT_LOWER_ANGLE;
		magneTankTurretRevoluteJointDef.upperAngle = mTURRET_JOINT_UPPER_ANGLE;
		this.mTurretRevoluteJoint = (RevoluteJoint) this.mGameLevel.mPhysicsWorld.createJoint(magneTankTurretRevoluteJointDef);
		
		this.mLeftWheelBody = PhysicsFactory.createCircleBody(this.mGameLevel.mPhysicsWorld, this.mLeftWheelSprite.getX(), this.mLeftWheelSprite.getY(), mWHEEL_BODY_RADIUS, BodyType.DynamicBody, mWHEELS_FIXTURE_DEF);
		this.mLeftWheelBody.setSleepingAllowed(false);
		this.mLeftWheelBody.setAngularDamping(mWHEELS_ANGULAR_DAMPING);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mLeftWheelSprite, this.mLeftWheelBody));
		
		final LineJointDef lineJointDef = new LineJointDef();
		lineJointDef.initialize(this.mVehicleBody, this.mLeftWheelBody, this.mLeftWheelBody.getWorldCenter(), mVECTOR2_VERTICAL_AXIS);
		lineJointDef.enableLimit = true;
		lineJointDef.lowerTranslation = -mWHEEL_SHOCK_LENGTH / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		lineJointDef.upperTranslation = mWHEEL_SHOCK_LENGTH / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		this.mGameLevel.mPhysicsWorld.createJoint(lineJointDef);
		
		final DistanceJointDef distanceJointDef = new DistanceJointDef();
		distanceJointDef.initialize(this.mVehicleBody, this.mLeftWheelBody, this.mLeftWheelBody.getWorldCenter(), this.mLeftWheelBody.getWorldCenter());
		distanceJointDef.frequencyHz = mWHEEL_SHOCK_FREQUENCY;
		distanceJointDef.dampingRatio = mWHEEL_SHOCK_DAMPING_RATIO;
		this.mGameLevel.mPhysicsWorld.createJoint(distanceJointDef);
		
		this.mRightWheelBody = PhysicsFactory.createCircleBody(this.mGameLevel.mPhysicsWorld, this.mRightWheelSprite.getX(), this.mRightWheelSprite.getY(), mWHEEL_BODY_RADIUS, BodyType.DynamicBody, mWHEELS_FIXTURE_DEF);
		this.mRightWheelBody.setSleepingAllowed(false);
		this.mRightWheelBody.setAngularDamping(mWHEELS_ANGULAR_DAMPING);
		this.mGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.mRightWheelSprite, this.mRightWheelBody));
		
		lineJointDef.initialize(this.mVehicleBody, this.mRightWheelBody, this.mRightWheelBody.getWorldCenter(), mVECTOR2_VERTICAL_AXIS);
		this.mGameLevel.mPhysicsWorld.createJoint(lineJointDef);
		distanceJointDef.initialize(this.mVehicleBody, this.mRightWheelBody, this.mRightWheelBody.getWorldCenter(), this.mRightWheelBody.getWorldCenter());
		this.mGameLevel.mPhysicsWorld.createJoint(distanceJointDef);
		
		this.mGameLevel.registerUpdateHandler(this);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void equipNextCrate(final boolean isFirstCrate) {
		if(this.mGameLevel.mCratesLeft.size() > 0) {
			final Vector2 NewCrateSpawnLocation = this.mTurretBody.getWorldPoint(new Vector2(mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_X, mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_Y));
			this.mGrabbedMagneticObject = new MagneticCrate(NewCrateSpawnLocation.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, NewCrateSpawnLocation.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, this.mGameLevel.mCratesLeft.get(0).crateSize, this.mGameLevel.mCratesLeft.get(0).crateType, this.mGameLevel);
			this.mGrabbedMagneticObject.mDesiredXY = NewCrateSpawnLocation;
			if(!isFirstCrate) {
				this.mGameLevel.mRemainingCratesBar.pullCrate();
			}
		} else {
			this.mTurretMagnetOn = false;
			this.mGameLevel.mRemainingCratesBar.pullCrate();
		}
	}
	
	public boolean onMagneTankTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(!this.mVehicleMovementBoundTouchInput.onTouch(pSceneTouchEvent)) {
			if(!this.mTurretMovementBoundTouchInput.onTouch(pSceneTouchEvent)) {
				this.mTurretShootingBoundTouchInput.onTouch(pSceneTouchEvent);
			}
		}
		return true;
	}
	
	@Override
	public void onUpdate(final float pSecondsElapsed) {
		ResourceManager.getCamera().setBounds(this.mVehicleSprite.getX() + mCAMERA_LOWER_BOUND_FROM_VEHICLE_X, this.mVehicleSprite.getY() + mCAMERA_LOWER_BOUND_FROM_VEHICLE_Y, GameLevel.mLEVEL_WIDTH, Float.MAX_VALUE);
		
		this.mLeftWheelBody.setAngularVelocity(this.mWheelAngularVelocity);
		this.mRightWheelBody.setAngularVelocity(this.mWheelAngularVelocity);
		
		if(this.mTurretRevoluteJoint.getJointAngle() < this.mTurretDesiredAngle) {
			if(this.mTurretDesiredAngle >= this.mTurretRevoluteJoint.getUpperLimit()) {
				this.mTurretBody.setTransform(this.mTurretBody.getWorldPoint(mVECTOR2_ZERO), this.mTurretRevoluteJoint.getUpperLimit());
			} else {
				this.mTurretBody.setTransform(this.mTurretBody.getWorldPoint(mVECTOR2_ZERO), this.mTurretBody.getAngle() + ((this.mTurretDesiredAngle - this.mTurretRevoluteJoint.getJointAngle()) / mTURRET_ROTATION_DRAG));
			}
		} else if(this.mTurretRevoluteJoint.getJointAngle() > this.mTurretDesiredAngle) {
			if(this.mTurretDesiredAngle <= this.mTurretRevoluteJoint.getLowerLimit()) {
				this.mTurretBody.setTransform(this.mTurretBody.getWorldPoint(mVECTOR2_ZERO), this.mTurretRevoluteJoint.getLowerLimit());
			} else {
				this.mTurretBody.setTransform(this.mTurretBody.getWorldPoint(mVECTOR2_ZERO), this.mTurretBody.getAngle() + ((this.mTurretDesiredAngle - this.mTurretRevoluteJoint.getJointAngle()) / mTURRET_ROTATION_DRAG));
			}
		}
		
		if(this.mGrabbedMagneticObject != null) {
			final Vector2 newDesiredXY = Vector2Pool.obtain(mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_X, mMAGNET_OFFSET_FROM_TURRET_BODY_CENTER_Y);
			this.mGrabbedMagneticObject.mDesiredXY = this.mTurretBody.getWorldPoint(newDesiredXY);
			Vector2Pool.recycle(newDesiredXY);
		}
	}
	
	public void requestShoot() {
		if(this.mTurretMagnetOn) {
			this.mTurretMagnetOn = false;
			if(this.mGrabbedMagneticObject != null) {
				this.mGrabbedMagneticObject.release();
				this.mGrabbedMagneticObject.mIsShot = true;
				ResourceManager.getCamera().setChaseEntity(this.mGrabbedMagneticObject.mEntity);
				this.mGameLevel.addPointsToScore(this.mGrabbedMagneticObject.mEntity, -GameLevel.mCRATE_POINT_VALUE);
				
				this.mGrabbedMagneticObject.mBody.setLinearVelocity(0f, 0f);
				
				final float vAng = (float) Math.atan2(this.mGrabbedMagneticObject.mBody.getWorldCenter().y - this.mTurretBody.getWorldCenter().y, this.mGrabbedMagneticObject.mBody.getWorldCenter().x - this.mTurretBody.getWorldCenter().x);
				final float vPower = ((MathUtils.distance(this.mTurretBody.getWorldCenter().x, this.mTurretBody.getWorldCenter().y, this.mGrabbedMagneticObject.mBody.getWorldCenter().x, this.mGrabbedMagneticObject.mBody.getWorldCenter().y) < mTURRET_SHOOT_GRABBED_OBJECT_IF_WITHIN_RANGE) ? this.mShootingPower : 0f);
				this.mGrabbedMagneticObject.mBody.applyLinearImpulse(FloatMath.cos(vAng) * vPower * this.mGrabbedMagneticObject.mBody.getMass(), FloatMath.sin(vAng) * vPower * this.mGrabbedMagneticObject.mBody.getMass(), this.mGrabbedMagneticObject.mBody.getWorldCenter().x, this.mGrabbedMagneticObject.mBody.getWorldCenter().y);
				this.mGrabbedMagneticObject = null;
				this.mGameLevel.mCratesLeft.remove(0);
				this.mGameLevel.resetTrailingDots();
				
				final float shootingVolume = 0.75f * ((this.mShootingPower - MagneTank.mSHOOTING_POWER_MINIMUM) / (MagneTank.mSHOOTING_POWER_MAXIMUM - MagneTank.mSHOOTING_POWER_MINIMUM));
				SFXManager.playShoot(0.5f, shootingVolume);
				SFXManager.playExplosion(2f, 0.25f + shootingVolume);
			}
		}
	}
	
	@Override
	public void reset() {}
	
	public void setMangeTankTurretDesiredAngle(final float pAngle) {
		this.mTurretDesiredAngle = MathUtils.bringToBounds(mTURRET_JOINT_LOWER_ANGLE, mTURRET_JOINT_UPPER_ANGLE, pAngle - this.mVehicleBody.getAngle());
	}
	
	public void updateShadowAlpha() {
		this.mShadowSprite.setAlpha(MathUtils.bringToBounds(mSHADOW_MIN_ALPHA, mSHADOW_MAX_ALPHA, mSHADOW_MAX_ALPHA - ((((this.mVehicleSprite.getY() - this.mCHARACTER_SHADOW_Y_DIFFERENCE) - this.mSHADOW_START_Y) / mSHADOW_ALPHA_HEIGHT_DIFFERENCE) * mSHADOW_ALPHA_DIFFERENCE)));
	}
}