package ifl.games.runtime.Managers;

import ifl.games.runtime.ManagedScene;
import ifl.games.runtime.Layers.ManagedLayer;
import ifl.games.runtime.Layers.OptionsLayer;
import ifl.games.runtime.Menus.MainMenu;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

/**
*** @author Brian Broyles - IFL Game Studio
**/
public class SceneManager extends Object
{
	// Holds the global SceneManager instance.
	private static final SceneManager INSTANCE = new SceneManager();

	// ====================================================
	// VARIABLES
	// ====================================================
	// These variables reference the current scene and next scene when switching scenes.
	public ManagedScene mCurrentScene;
	private ManagedScene mNextScene;
	// Keep a reference to the engine.
	private Engine mEngine = ResourceManager.getInstance().engine;
	// Used by the mLoadingScreenHandler, this variable ensures that the loading screen is shown for one frame prior to loading resources.
	private int mNumFamesPassed = -1;
	// Keeps the mLoadingScreenHandler from being registered with the engine if it has already been registered.
	private boolean mLoadingScreenHandlerRegistered = false;
	// An update handler that shows the loading screen of mNextScene before calling it to load.
	private IUpdateHandler mLoadingScreenHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// Increment the mNumFamesPassed
			mNumFamesPassed++;
			// And increment the amount of time that the loading screen has been visible.
			mNextScene.elapsedLoadingScreenTime += pSecondsElapsed;
			// On the first frame AFTER the loading screen has been shown.
			if(mNumFamesPassed==1) {
				// Hide and unload the previous scene if it exists.
				if(mCurrentScene!=null) {
					mCurrentScene.onHideManagedScene();
					mCurrentScene.onUnloadManagedScene();
				}
				// Load the new scene.
				mNextScene.onLoadManagedScene();
			}
			// On the first frame AFTER the scene has been completely loaded and the loading screen has been shown for its minimum limit.
			if(mNumFamesPassed>1 && mNextScene.elapsedLoadingScreenTime>=mNextScene.minLoadingScreenTime && mNextScene.isLoaded) {
				// Remove the loading screen that was set as a child in the showScene() method.
				mNextScene.clearChildScene();
				// Tell the new scene to unload its loading screen.
				mNextScene.onLoadingScreenUnloadAndHidden();
				// Tell the new scene that it is shown.
				mNextScene.onShowManagedScene();
				// Set the new scene to the current scene.
				mCurrentScene = mNextScene;
				// Reset the handler & loading screen variables to be ready for another use.
				mNextScene.elapsedLoadingScreenTime = 0f;
				mNumFamesPassed = -1;
				mEngine.unregisterUpdateHandler(this);
				mLoadingScreenHandlerRegistered = false;
			}
		}
		@Override public void reset() {}
	};
	// Set to TRUE in the showLayer() method if the camera had a HUD before the layer was shown.
	private boolean mCameraHadHud = false;
	// Boolean to reflect whether there is a layer currently shown on the screen.
	public boolean mIsLayerShown = false;
	// An empty place-holder scene that we use to apply the modal properties of the layer to the currently shown scene.
	private Scene mPlaceholderModalScene;
	// Hold a reference to the current managed layer (if one exists).
	public ManagedLayer mCurrentLayer;

	// ====================================================
	// CONSTRUCTOR AND INSTANCE GETTER
	// ====================================================
	public SceneManager() {
	}
	// Singleton reference to the global SceneManager.
	public static SceneManager getInstance(){
		return INSTANCE;
	}

	// ====================================================
	// PUBLIC METHODS
	// ====================================================
	// Convenience method to quickly show the Main Menu.
	public void showMainMenu() {
		showScene(MainMenu.getInstance());
	}

	// Initiates the process of switching the current managed scene for a new managed scene.
	public void showScene(final ManagedScene pManagedScene) {
		// Reset the camera. This is automatically overridden by any calls to alter the camera from within a managed scene's onShowScene() method.
		mEngine.getCamera().set(-ResourceManager.getInstance().cameraWidth/2f, -ResourceManager.getInstance().cameraHeight/2f, ResourceManager.getInstance().cameraWidth/2f, ResourceManager.getInstance().cameraHeight/2f);
		// If the new managed scene has a loading screen.
		if(pManagedScene.hasLoadingScreen) {
			// Set the loading screen as a modal child to the new managed scene.
			pManagedScene.setChildScene(pManagedScene.onLoadingScreenLoadAndShown(),true,true,true);
			// This if/else block assures that the LoadingScreen Update Handler is only registered if necessary.
			if(mLoadingScreenHandlerRegistered)
			{
				mNumFamesPassed = -1;
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
			} else {

				mEngine.registerUpdateHandler(mLoadingScreenHandler);
				mLoadingScreenHandlerRegistered = true;
			}
			// Set pManagedScene to mNextScene which is used by the loading screen update handler.
			mNextScene = pManagedScene;
			// Set the new scene as the engine's scene.
			mEngine.setScene(pManagedScene);
			// Exit the method and let the LoadingScreen Update Handler finish the switching.
			return;
		}
		// If the new managed scene does not have a loading screen.
		// Set pManagedScene to mNextScene and apply the new scene to the engine.
		mNextScene = pManagedScene;
		mEngine.setScene(mNextScene);
		// If a previous managed scene exists, hide and unload it.
		if(mCurrentScene!=null)
		{
			mCurrentScene.onHideManagedScene();
			mCurrentScene.onUnloadManagedScene();
		}
		// Load and show the new managed scene, and set it as the current scene.
		mNextScene.onLoadManagedScene();
		mNextScene.onShowManagedScene();
		mCurrentScene = mNextScene;
	}

	// Convenience method to quickly show the Options Layer.
	public void showOptionsLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(OptionsLayer.getInstance(),false,pSuspendCurrentSceneUpdates,true);
	}

	// Shows a layer by placing it as a child to the Camera's HUD.
	public void showLayer(final ManagedLayer pLayer, final boolean pSuspendSceneDrawing, final boolean pSuspendSceneUpdates, final boolean pSuspendSceneTouchEvents) {
		// If the camera already has a HUD, we will use it.
		if(mEngine.getCamera().hasHUD()){
			mCameraHadHud = true;
		} else {
			// Otherwise, we will create one to use.
			mCameraHadHud = false;
			HUD placeholderHud = new HUD();
			mEngine.getCamera().setHUD(placeholderHud);
		}
		// If the managed layer needs modal properties, set them.
		if(pSuspendSceneDrawing || pSuspendSceneUpdates || pSuspendSceneTouchEvents) {
			// Apply the managed layer directly to the Camera's HUD
			mEngine.getCamera().getHUD().setChildScene(pLayer, pSuspendSceneDrawing, pSuspendSceneUpdates, pSuspendSceneTouchEvents);
			mEngine.getCamera().getHUD().setOnSceneTouchListenerBindingOnActionDownEnabled(true);
			// Create the place-holder scene if it needs to be created.
			if(mPlaceholderModalScene==null) {
				mPlaceholderModalScene = new Scene();
				mPlaceholderModalScene.setBackgroundEnabled(false);
			}
			// Apply the place-holder to the current scene.
			mCurrentScene.setChildScene(mPlaceholderModalScene, pSuspendSceneDrawing, pSuspendSceneUpdates, pSuspendSceneTouchEvents);
		} else {
			// If the managed layer does not need to be modal, simply set it to the HUD.
			mEngine.getCamera().getHUD().setChildScene(pLayer);
		}
		// Set the camera for the managed layer so that it binds to the camera if the camera is moved/scaled/rotated.
		pLayer.setCamera(mEngine.getCamera());
		// Scale the layer according to screen size.
//		pLayer.setScale(ResourceManager.getInstance().cameraScaleFactorX, ResourceManager.getInstance().cameraScaleFactorY);
		// Let the layer know that it is being shown.
		pLayer.onShowManagedLayer();
		// Reflect that a layer is shown.
		mIsLayerShown = true;
		// Set the current layer to pLayer.
		mCurrentLayer = pLayer;
	}

	// Hides the open layer if one is open.
	public void hideLayer() {
		if(mIsLayerShown) {
			// Clear the HUD's child scene to remove modal properties.
			mEngine.getCamera().getHUD().clearChildScene();
			// If we had to use a place-holder scene, clear it.
			if(mCurrentScene.hasChildScene())
				if(mCurrentScene.getChildScene()==mPlaceholderModalScene)
					mCurrentScene.clearChildScene();
			// If the camera did not have a HUD before we showed the layer, remove the place-holder HUD.
			if(!mCameraHadHud)
				mEngine.getCamera().setHUD(null);
			// Reflect that a layer is no longer shown.
			mIsLayerShown = false;
			// Remove the reference to the layer.
			mCurrentLayer = null;
		}
	}
}