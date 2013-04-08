public class TextureFolderLoadingActivity extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================

	// ====================================================
	// VARIABLES
	// ====================================================
	public final ArrayList<ManagedStandardTexture> loadedTextures = new ArrayList<ManagedStandardTexture>();

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
		this.loadAllTextureRegionsInFolders(TextureOptions.BILINEAR, "gfx/FolderToLoad/");
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
		pScene.attachChild(new Sprite(144f, 240f, getLoadedTextureRegion("Coin1"), this.getVertexBufferObjectManager()));
		pScene.attachChild(new Sprite(272f, 240f, getLoadedTextureRegion("Coin5"), this.getVertexBufferObjectManager()));
		pScene.attachChild(new Sprite(400f, 240f, getLoadedTextureRegion("Coin10"), this.getVertexBufferObjectManager()));
		pScene.attachChild(new Sprite(528f, 240f, getLoadedTextureRegion("Coin50"), this.getVertexBufferObjectManager()));
		pScene.attachChild(new Sprite(656f, 240f, getLoadedTextureRegion("Coin100"), this.getVertexBufferObjectManager()));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	/** Unloads all managed textures and removes them from memory. */
	public void unloadAllTextures() {
		// Unload and remove every currently managed texture.
		for(ManagedStandardTexture curTex : loadedTextures) {
			curTex.removeFromMemory();
			curTex=null;
			loadedTextures.remove(curTex);
		}
		System.gc();
	}

	/** Retrieves a texture region by name if it is being managed.
	 * @param pName The name of the texture. If loaded by the Resource Manager, the texture name will be the short-filename without an extension.
	 * @return The first managed <b>TextureRegion</b> with the given Name.<br><b>null</b> if no managed texture has the given name.
	 */
	public ITextureRegion getLoadedTextureRegion(String pName) {
		// Check the currently managed texture regions and return the first one
		// that matches the name.
		for(ManagedStandardTexture curTex : loadedTextures)
			if(curTex.name.equalsIgnoreCase(pName))
				return curTex.textureRegion;
		return null;
	}

	/** Retrieves a texture region by Filename and starts managing it.
	 * @param pTextureOptions The TextureOptions that you want to apply to the TextureRegion.
	 * @param pFilename The path to the texture (inside of the 'assets' folder and with an extension) to be loaded and returned.
	 * @param pSceneName The name of the scene to associate the TextureRegion with.
	 * @return The <b>TextureRegion</b> that was loaded and managed by the ResourceManager.
	 */
	public ITextureRegion getTextureRegion(TextureOptions pTextureOptions, String pFilename) {
		loadAndManageTextureRegion(pTextureOptions,pFilename);
		return loadedTextures.get(loadedTextures.size()-1).textureRegion;
	}
	
	/** Loads a texture region by Filename and starts managing it.
	 * @param pTextureOptions The TextureOptions that you want to apply to the TextureRegion.
	 * @param pFilename The path to the texture (inside of the 'assets' folder and with an extension) to be loaded and returned.
	 * @param pSceneName The name of the scene to associate the TextureRegion with.
	 * @return The <b>TextureRegion</b> that was loaded and managed by the ResourceManager.
	 */
	public void loadAndManageTextureRegion(TextureOptions pTextureOptions, String pFilename) {
		AssetBitmapTextureAtlasSource cSource = AssetBitmapTextureAtlasSource.create(this.getAssets(), pFilename);  
		BitmapTextureAtlas TextureToLoad = new BitmapTextureAtlas(mEngine.getTextureManager(), cSource.getTextureWidth(), cSource.getTextureHeight(), pTextureOptions);
		TextureRegion TextureRegionToLoad = BitmapTextureAtlasTextureRegionFactory.createFromAsset(TextureToLoad, this, pFilename, 0, 0);     
		TextureToLoad.load();
		loadedTextures.add(new ManagedStandardTexture(pFilename.substring(pFilename.lastIndexOf("/")+1, pFilename.lastIndexOf(".")),TextureRegionToLoad));
	}

	/** Loads all textures inside multiple folders into memory and allows them to be accessed via {@link #getLoadedTextureRegion(String pName, String pSceneName) getLoadedTextureRegion}.
	 * @param pSceneName The name of the scene in which you use the textures loaded from the folders.
	 * @param pTextureOptions The TextureOptions that you want to apply to all textures in the folders.
	 * @param pFolderPaths The paths to the folders (inside the 'assets' folder) that contain the textures to load.
	 * @throws IOException if a folder doesn't exist or if its contents are unreadable.
	 */
	public void loadAllTextureRegionsInFolders(TextureOptions pTextureOptions, String... pFolderPaths)
	{
		String[] listFileNames;
		String curFilePath;
		String curFileExtension;
		// For every folder listed in pFolderPaths...
		for (int i = 0; i < pFolderPaths.length; i++)
			try {
				// create a list of all sub-paths and files.
				listFileNames = this.getAssets().list(pFolderPaths[i].substring(0, pFolderPaths[i].lastIndexOf("/")));
				// For every sub-path and file in the folder...
				for (String fileName : listFileNames) {
					// add the filename to get the full path of the file...
					curFilePath = pFolderPaths[i].concat(fileName);
					// and the extension of the file.
					curFileExtension = curFilePath.substring(curFilePath.length()-4);
					// If the file extension matches the image files supported by AndEngine, load it.
					if(curFileExtension.equalsIgnoreCase(".png") || curFileExtension.equalsIgnoreCase(".bmp") || curFileExtension.equalsIgnoreCase(".jpg"))
						loadAndManageTextureRegion(pTextureOptions, curFilePath);
				}
				// if the folder doesn't exist or if there is a problem retrieving its contents...
			} catch (IOException e) {
				System.out.print("Failed to load textures From Folder!");
				e.printStackTrace();
				return;
			}
	}
	
	// A simple class that holds the information for every managed Texture and can remove itself from memory.
	public class ManagedStandardTexture extends Object {
		public ITextureRegion textureRegion;
		public String name;

		public ManagedStandardTexture(String pName, final ITextureRegion pTextureRegion) {
			name = pName;
			textureRegion = pTextureRegion;
		}

		public void removeFromMemory() {
			loadedTextures.remove(this);
			textureRegion.getTexture().unload();
			textureRegion = null;
			name = null;
		}
	}
}