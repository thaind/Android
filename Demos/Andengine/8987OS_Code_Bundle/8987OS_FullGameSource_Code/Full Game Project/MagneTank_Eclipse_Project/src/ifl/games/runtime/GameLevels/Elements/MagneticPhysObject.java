package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;

import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/** The MagneticPhysObject class extends the PhysObject class to allow an
 *  object to be grabbed, or released, by the MagneTank. When grabbed, the
 *  object has anti-gravity forces applied as well as forces that pull the
 *  object toward the MagneTank turret.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class MagneticPhysObject<T extends IShape> extends PhysObject<T> {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mAT_MAGNET_TOLERANCE_IN_METERS = 1f;
	private static final float mGRABBED_LINEAR_DAMPING = 15f;
	private static final float mNORMAL_LINEAR_DAMPING = 0.5f;
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public boolean mIsGrabbed = false;
	public boolean mIsShot = false;
	public Vector2 mDesiredXY = new Vector2();
	
	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void set(Body pBody, T pEntity, PhysicsConnector pPhysicsConnector, GameLevel pGameLevel) {
		super.set(pBody, pEntity, pPhysicsConnector, pGameLevel);
	}
	
	public void grab() { this.mIsGrabbed = true; }
	
	public void release() { this.mIsGrabbed = false; }
	
	public void setLinearDamping(final float pLinearDamping) {
		if(this.mBody.getLinearDamping()!=pLinearDamping)
			this.mBody.setLinearDamping(pLinearDamping);
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		if(this.mIsGrabbed) {
			if(this.mBody.isActive()) {
				if(MathUtils.distance(this.mDesiredXY.x, this.mDesiredXY.y, this.mBody.getWorldCenter().x, this.mBody.getWorldCenter().y)<mAT_MAGNET_TOLERANCE_IN_METERS)
					this.mBody.setLinearVelocity(0f, 0f);
				setLinearDamping(mGRABBED_LINEAR_DAMPING);
				this.mBody.applyForce(-this.mBody.getWorld().getGravity().x * this.mBody.getMass(), -this.mBody.getWorld().getGravity().y * this.mBody.getMass(), this.mBody.getWorldCenter().x, this.mBody.getWorldCenter().y);
				final float vAng = (float) Math.atan2(this.mDesiredXY.y - this.mBody.getWorldCenter().y, this.mDesiredXY.x - this.mBody.getWorldCenter().x);
				final float vDist = MathUtils.distance(this.mDesiredXY.x, this.mDesiredXY.y, this.mBody.getWorldCenter().x, this.mBody.getWorldCenter().y);
				final float vForce;
				if(vDist>2f)
					vForce = Math.max((5000f-(60f * vDist))/2f, 40f);
				else
					vForce = 500f * vDist;
				this.mBody.applyForce(
						FloatMath.cos(vAng) * vForce * this.mBody.getMass(),
						FloatMath.sin(vAng) * vForce * this.mBody.getMass(),
						this.mBody.getWorldCenter().x,
						this.mBody.getWorldCenter().y);
			} else {
				this.mBody.setTransform(this.mDesiredXY.x, this.mDesiredXY.y, this.mBody.getAngle());
			}
		} else {
			this.setLinearDamping(mNORMAL_LINEAR_DAMPING);
		}
	}
}