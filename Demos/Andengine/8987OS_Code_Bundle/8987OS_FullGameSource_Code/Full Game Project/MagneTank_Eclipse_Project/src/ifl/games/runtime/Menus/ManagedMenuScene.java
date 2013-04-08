package ifl.games.runtime.Menus;

import ifl.games.runtime.ManagedScene;

/**
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class ManagedMenuScene extends ManagedScene {
	public ManagedMenuScene(float pLoadingScreenMinimumSecondsShown) {
		super(pLoadingScreenMinimumSecondsShown);
	}
	@Override
	public void onUnloadManagedScene() {
		if(isLoaded) {
			// For menus, we are disabling the reloading of resources.
			// isLoaded = false;
			onUnloadScene();
		}
	}
}