public abstract class ManagedLayer extends HUD {
	// Is set TRUE if the layer is loaded.
	public boolean hasLoaded = false;
	// Set by the constructor, if true, the layer will be unloaded after being hidden.
	public boolean unloadOnHidden;
	// Convenience constructor. Creates a layer that does not unload when hidden.
	public ManagedLayer() {
		this(false);
	}
	// Constructor. Sets whether the layer will unload when hidden and ensures that there is no background on the layer.
	public ManagedLayer(boolean pUnloadOnHidden) {
		unloadOnHidden = pUnloadOnHidden;
		this.setBackgroundEnabled(false);
	}
	// If the layer is not loaded, load it. Ensure that the layer is not paused.
	public void onShowManagedLayer() {
		if(!hasLoaded) {
			hasLoaded = true;
			onLoadLayer();
		}
		this.setIgnoreUpdate(false);
		onShowLayer();
	}
	// Pause the layer, hide it, and unload it if it needs to be unloaded.
	public void onHideManagedLayer() {
		this.setIgnoreUpdate(true);
		onHideLayer();
		if(unloadOnHidden) {
			onUnloadLayer();
		}
	}
	// Methods to override in subclasses.
	public abstract void onLoadLayer();
	public abstract void onShowLayer();
	public abstract void onHideLayer();
	public abstract void onUnloadLayer();
}