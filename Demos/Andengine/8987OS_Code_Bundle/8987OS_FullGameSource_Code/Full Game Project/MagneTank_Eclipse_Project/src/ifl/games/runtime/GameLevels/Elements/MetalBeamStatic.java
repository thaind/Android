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

/** Much like the MetalBeamDynamic class, this class also represents a
 *  girder, but the BodyType of this object is set to Static to create an
 *  immobile barrier.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class MetalBeamStatic {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mMETAL_BEAM_DENSITY = 100f;
	private static final float mMETAL_BEAM_ELASTICITY = 0.0f;
	private static final float mMETAL_BEAM_FRICTION = 0.95f;
	private static final FixtureDef mMETAL_BEAM_STATIC_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mMETAL_BEAM_DENSITY, mMETAL_BEAM_ELASTICITY, mMETAL_BEAM_FRICTION);
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public Sprite metalBeamStaticSprite;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MetalBeamStatic(float pX, float pY, float pWidth, float pAngle, GameLevel pGameLevel) {
		
		pGameLevel.mBasePositions.add(new float[] {pX, pY});
		
		ITextureRegion metalBeamStaticTRCopy = ResourceManager.gameMetalBeamStaticTR.deepCopy();
		metalBeamStaticTRCopy.setTextureWidth(pWidth);
		
		metalBeamStaticSprite = new Sprite(pX,pY,metalBeamStaticTRCopy,ResourceManager.getActivity().getVertexBufferObjectManager());
		pGameLevel.attachChild(metalBeamStaticSprite);
		
		Body MetalBeamStaticBody = PhysicsFactory.createBoxBody(pGameLevel.mPhysicsWorld, metalBeamStaticSprite, BodyType.StaticBody, mMETAL_BEAM_STATIC_FIXTURE_DEF);
		pGameLevel.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(metalBeamStaticSprite, MetalBeamStaticBody));
		
		MetalBeamStaticBody.setTransform(MetalBeamStaticBody.getWorldCenter(), MathConstants.DEG_TO_RAD * pAngle);
	}
}