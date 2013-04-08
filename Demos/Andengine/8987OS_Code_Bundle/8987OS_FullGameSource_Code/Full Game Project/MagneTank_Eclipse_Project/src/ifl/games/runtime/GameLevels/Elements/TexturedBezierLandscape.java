package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.GameLevels.Elements.TexturedMesh.DrawMode;
import ifl.games.runtime.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.andengine.entity.Entity;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/** The TexturedBezierLandscape class creates two textured meshes and a
 *  physics Body that represent the ground of the level. As the name implies,
 *  the landscape is comprised of Bezier curves to show rising or falling
 *  slopes. The textured meshes are made from repeating textures to avoid
 *  any visible seams between landscaped areas. The image below shows the
 *  two textures used to create the landscape as well as a design-example
 *  of how the combined meshes appear after a Bezier slope has been applied.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class TexturedBezierLandscape {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final float mDISTANCE_TO_ADD_TO_LEFT_SIDE_OF_TERRAIN = -500f;
	private static final float mDISTANCE_TO_EXTEND_DIRT_BELOW_GROUND_LEVEL = -800f;
	
	private static final float mGROUND_DENSITY = 0f;
	private static final float mGROUND_ELASTICITY = 0.01f;
	private static final float mGROUND_FRICTION = 20f;
	private static final FixtureDef mGROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(mGROUND_DENSITY, mGROUND_ELASTICITY, mGROUND_FRICTION);
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	// a slope is 5 values: startX, endX, endY, slope ratio(0f to 1f), detail (number of segments in slope)
	public TexturedBezierLandscape(float pStartY, float pWidth, float[] pSlopes, GameLevel pGameLevel) {
		
		ArrayList<float[]> terrainShape = new ArrayList<float[]>();
		
		// add some terrain to the negative side of the Y axis
		terrainShape.add(new float[] {mDISTANCE_TO_ADD_TO_LEFT_SIDE_OF_TERRAIN,pStartY});
		
		final int numSlopes = pSlopes.length;
		terrainShape.add(new float[] {0f,pStartY});
		for(int i = 0; i < (numSlopes)/5; i++) {
			if(i==0) { // first slope
				addBezierFloat2sToArrayList(pSlopes[i*5],pStartY,pSlopes[i*5+1],pSlopes[i*5+2],pSlopes[i*5+3],pSlopes[i*5+4],terrainShape);
			} else { // subsequent slopes
				addBezierFloat2sToArrayList(pSlopes[i*5],pSlopes[(i-1)*5+2],pSlopes[i*5+1],pSlopes[i*5+2],pSlopes[i*5+3],pSlopes[i*5+4],terrainShape);
				
			}
			terrainShape.add(new float[] {pSlopes[i*5+1],pSlopes[i*5+2]});
		}
		terrainShape.add(new float[] {pWidth,terrainShape.get(terrainShape.size()-1)[1]});
		
		// add some terrain past the width of the terrain so that it doesn't just end abruptly
		terrainShape.add(new float[] {GameLevel.mLEVEL_WIDTH,terrainShape.get(terrainShape.size()-1)[1]});
		
		// sort the terrain points on the X axis
		Collections.sort(terrainShape, new Comparator<float[]>() { @Override public int compare(float[] arg0, float[] arg1) { return (int) (arg0[0] - arg1[0]); } });
		
		// create a mesh from the terrain shape data
		ArrayList<float[]> meshTriangleData = new ArrayList<float[]>();
		for(int i = 0; i < terrainShape.size()-1; i++) {
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],-ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
				
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],-ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],-ResourceManager.gameGroundTopLayerTR.getHeight()/2f});
		}
		
		float[] meshTriangleDataPreBufferData = new float[meshTriangleData.size()*2];
		for(int i = 0; i < meshTriangleData.size(); i++) {
			meshTriangleDataPreBufferData[i*2] = meshTriangleData.get(i)[0];
			meshTriangleDataPreBufferData[i*2+1] = meshTriangleData.get(i)[1];
		}
		
		float[] bufferData = new float[TexturedMesh.VERTEX_SIZE * (meshTriangleDataPreBufferData.length/2)];
		for( int i = 0; i < meshTriangleDataPreBufferData.length/2; i++) {
			bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X] = meshTriangleDataPreBufferData[i*2];
			bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y] = meshTriangleDataPreBufferData[i*2+1];
		}
		
		TexturedMesh groundTopLayer = new TexturedMesh(0f, 0f, bufferData, (meshTriangleDataPreBufferData.length/2), DrawMode.TRIANGLES, ResourceManager.gameGroundTopLayerTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		
		bufferData = groundTopLayer.getBufferData();
		for(int i = 0; i < bufferData.length/TexturedMesh.VERTEX_SIZE; i++) {
			for(float[] curFloat2 : terrainShape)
				if(bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X]==curFloat2[0])
					bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y]+=curFloat2[1];
		}
		
		// make the ground body...
		Entity groundEnt = new Entity();
		ArrayList<Vector2> groundVerts = new ArrayList<Vector2>();
		for(int i = 0; i < terrainShape.size()-1; i++) {
			groundVerts.add(new Vector2(terrainShape.get(i)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,terrainShape.get(i)[1]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
			groundVerts.add(new Vector2(terrainShape.get(i+1)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,terrainShape.get(i+1)[1]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
			groundVerts.add(new Vector2(terrainShape.get(i)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,(terrainShape.get(i)[1]-ResourceManager.gameGroundTopLayerTR.getHeight())/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT ));
			
			groundVerts.add(new Vector2(terrainShape.get(i+1)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,terrainShape.get(i+1)[1]/32f));
			groundVerts.add(new Vector2(terrainShape.get(i+1)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,(terrainShape.get(i+1)[1]-ResourceManager.gameGroundTopLayerTR.getHeight())/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT ));
			groundVerts.add(new Vector2(terrainShape.get(i)[0]/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,(terrainShape.get(i)[1]-ResourceManager.gameGroundTopLayerTR.getHeight())/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT ));
		}
		PhysicsFactory.createTrianglulatedBody(pGameLevel.mPhysicsWorld, groundEnt, groundVerts, BodyType.StaticBody, mGROUND_FIXTURE_DEF);
		
		// make the dirt mesh triangle data
		meshTriangleData = new ArrayList<float[]>();
		for(int i = 0; i < terrainShape.size()-1; i++) {
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],terrainShape.get(i)[1]});
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],terrainShape.get(i+1)[1]});
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],mDISTANCE_TO_EXTEND_DIRT_BELOW_GROUND_LEVEL});
				
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],terrainShape.get(i+1)[1]});
				meshTriangleData.add(new float[] {terrainShape.get(i+1)[0],mDISTANCE_TO_EXTEND_DIRT_BELOW_GROUND_LEVEL});
				meshTriangleData.add(new float[] {terrainShape.get(i)[0],mDISTANCE_TO_EXTEND_DIRT_BELOW_GROUND_LEVEL});
		}
		
		meshTriangleDataPreBufferData = new float[meshTriangleData.size()*2];
		for(int i = 0; i < meshTriangleData.size(); i++) {
			meshTriangleDataPreBufferData[i*2] = meshTriangleData.get(i)[0];
			meshTriangleDataPreBufferData[i*2+1] = meshTriangleData.get(i)[1];
		}
		
		bufferData = new float[TexturedMesh.VERTEX_SIZE * (meshTriangleDataPreBufferData.length/2)];
		for( int i = 0; i < meshTriangleDataPreBufferData.length/2; i++) {
			bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X] = meshTriangleDataPreBufferData[i*2];
			bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y] = meshTriangleDataPreBufferData[i*2+1];
		}
		TexturedMesh groundBottomLayer = new TexturedMesh(0f, 0f, bufferData, (meshTriangleDataPreBufferData.length/2), DrawMode.TRIANGLES, ResourceManager.gameGroundBottomLayerTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		
		pGameLevel.attachChild(groundBottomLayer);
		pGameLevel.attachChild(groundTopLayer);
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	private static float[] CalculateBezierPoint(float t, float pFirstX, float pFirstY, float pSecondX, float pSecondY, float pThirdX, float pThirdY, float pFourthX, float pFourthY) {
		float one_minus_t = 1f - t;
		float[] retValue = new float[2];
		retValue[0] = ((one_minus_t * one_minus_t * one_minus_t) * pFirstX) + ((3f * one_minus_t * one_minus_t * t) * pSecondX) + ((3f * one_minus_t * t * t) * pThirdX) + ((t * t * t) * pFourthX);
		retValue[1] = ((one_minus_t * one_minus_t * one_minus_t) * pFirstY) + ((3f * one_minus_t * one_minus_t * t) * pSecondY) + ((3f * one_minus_t * t * t) * pThirdY) + ((t * t * t) * pFourthY);
		return retValue;
	}
	
	private static void addBezierFloat2sToArrayList(final float pStartX, final float pStartY, final float pEndX, final float pEndY, final float pGrade, float pSections, ArrayList<float[]> pArrayList) {
		for(float i = 0; i <= 1f; i+=1f/pSections) {
			pArrayList.add(CalculateBezierPoint(i,
					pStartX,pStartY,
					pStartX + ((pEndX - pStartX) * pGrade),pStartY,
					pEndX - ((pEndX - pStartX) * pGrade),pEndY,
					pEndX,pEndY));
		}
	}
	
}