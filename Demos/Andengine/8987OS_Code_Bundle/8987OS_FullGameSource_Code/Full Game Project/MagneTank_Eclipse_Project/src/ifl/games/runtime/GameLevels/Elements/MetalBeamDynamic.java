package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathConstants;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/** This class represents the non-static physics-enabled girders seen in the
 *  game. The length of each beam can be set thanks to its repeating texture.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MetalBeamDynamic {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mMETAL_BEAM_ANGULAR_DAMPING = 0.4f;
	private static final float mMETAL_BEAM_DENSITY = 110f;
	private static final float mMETAL_BEAM_ELASTICITY = 0.2f;
	private static final float mMETAL_BEAM_FRICTION = 0.5f;
	private static final FixtureDef mMETAL_BEAM_DYNAMIC_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mMETAL_BEAM_DENSITY, mMETAL_BEAM_ELASTICITY, mMETAL_BEAM_FRICTION);
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public final Sprite mMetalBeamDynamicSprite;
	public final Body mMetalBeamDynamicBody;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MetalBeamDynamic(float pX, float pY, float pWidth, float pAngle, final GameLevel pGameLevel) {
		
		pGameLevel.mBasePositions.add(new float[] {pX, pY});
		
		ITextureRegion metalBeamDynamicTRCopy = ResourceManager.gameMetalBeamDynamicTR.deepCopy();
		metalBeamDynamicTRCopy.setTextureWidth(pWidth);
		mMetalBeamDynamicSprite = new Sprite(pX,pY,metalBeamDynamicTRCopy,ResourceManager.getActivity().getVertexBufferObjectManager()) {
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if(mMetalBeamDynamicBody!=null)
					pGameLevel.reportBaseBodySpeed(mMetalBeamDynamicBody.getLinearVelocity().len2());
			}
		};
		pGameLevel.attachChild(mMetalBeamDynamicSprite);
		
		mMetalBeamDynamicBody = PhysicsFactory.createBoxBody(pGameLevel.mPhysicsWorld, mMetalBeamDynamicSprite, BodyType.DynamicBody, mMETAL_BEAM_DYNAMIC_FIXTURE_DEF);
		pGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mMetalBeamDynamicSprite, mMetalBeamDynamicBody));
		
		mMetalBeamDynamicBody.setAngularDamping(mMETAL_BEAM_ANGULAR_DAMPING);
		mMetalBeamDynamicBody.setTransform(mMetalBeamDynamicBody.getWorldCenter(), MathConstants.DEG_TO_RAD * pAngle);
	}
}