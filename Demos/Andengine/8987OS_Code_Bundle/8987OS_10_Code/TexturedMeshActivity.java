public class TexturedMeshActivity extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================

	// ====================================================
	// VARIABLES
	// ====================================================

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), new Camera(0, 0, 800, 480)).setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		Scene mScene = new Scene();
		mScene.setBackground(new Background(0.9f,0.9f,0.9f));
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		BitmapTextureAtlas texturedMeshT = new BitmapTextureAtlas(this.getTextureManager(), 512, 128, TextureOptions.REPEATING_BILINEAR);
		ITextureRegion texturedMeshTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(texturedMeshT, this, "gfx/dirt.png", 0, 0);
		texturedMeshT.load();

		float[] meshTriangleVertices = {
				24.633111f,37.7835047f,
				-0.00898f,113.0324447f,
				-24.610162f,37.7835047f,

				0.00387f,-37.7900953f,
				-103.56176f,37.7901047f,
				103.56176f,37.7795047f,

				0.00387f,-37.7900953f,
				-39.814736f,-8.7311953f,
				-64.007044f,-83.9561953f,

				64.00771f,-83.9621953f,
				39.862562f,-8.7038953f,
				0.00387f,-37.7900953f};

		float[] meshBufferData = new float[TexturedMesh.VERTEX_SIZE * (meshTriangleVertices.length/2)];
		for( int i = 0; i < meshTriangleVertices.length/2; i++) {
			meshBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X] = meshTriangleVertices[i*2];
			meshBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y] = meshTriangleVertices[i*2+1];
		}

		TexturedMesh starTexturedMesh = new TexturedMesh(400f, 225f, meshBufferData, 12, DrawMode.TRIANGLES, texturedMeshTR, this.getVertexBufferObjectManager());
		
		pScene.attachChild(starTexturedMesh);
		
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}