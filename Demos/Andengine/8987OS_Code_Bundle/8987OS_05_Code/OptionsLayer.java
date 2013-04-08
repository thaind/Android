public class OptionsLayer extends ManagedLayer
{
	private static final OptionsLayer INSTANCE = new OptionsLayer();
	public static OptionsLayer getInstance(){
		return INSTANCE;
	}
	
	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if(OptionsLayer.getInstance().getY()>ResourceManager.getInstance().cameraHeight/2f) {
				OptionsLayer.getInstance().setPosition(OptionsLayer.getInstance().getX(), Math.max(OptionsLayer.getInstance().getY()-(3600*(pSecondsElapsed)),ResourceManager.getInstance().cameraHeight/2f));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
			}
		}
		@Override public void reset() {}
	};
	
	// Animates the layer to slide out through the top and tell the SceneManager to hide it when it is off-screen;
	IUpdateHandler SlideOut = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if(OptionsLayer.getInstance().getY()<ResourceManager.getInstance().cameraHeight/2f+480f) {
				OptionsLayer.getInstance().setPosition(OptionsLayer.getInstance().getX(), Math.min(OptionsLayer.getInstance().getY()+(3600*(pSecondsElapsed)),ResourceManager.getInstance().cameraHeight/2f+480f));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}
		@Override public void reset() {}
	};
	
	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		final float BackgroundX = 0f, BackgroundY = 0f;
		final float BackgroundWidth = 760f, BackgroundHeight = 440f;
		Rectangle smth = new Rectangle(BackgroundX,BackgroundY,BackgroundWidth,BackgroundHeight,ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp() && pTouchAreaLocalX < this.getWidth() && pTouchAreaLocalX > 0 && pTouchAreaLocalY < this.getHeight() && pTouchAreaLocalY > 0) {
					ResourceManager.clickSound.play();
					onHideLayer();
				}
				return true;
			}
		};
		smth.setColor(0f, 0f, 0f, 0.85f);
		this.attachChild(smth);
		this.registerTouchArea(smth);
		
		// Create the OptionsLayerTitle text for the Layer.
		Text OptionsLayerTitle = new Text(0,0,ResourceManager.fontDefault32Bold,"OPTIONS",ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		OptionsLayerTitle.setPosition(0f,BackgroundHeight/2f-OptionsLayerTitle.getHeight());
		this.attachChild(OptionsLayerTitle);
		
		// Let the player know how to get out of the blank Options Layer
		Text OptionsLayerSubTitle = new Text(0,0,ResourceManager.fontDefault32Bold,"Tap to return",ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		OptionsLayerSubTitle.setScale(0.75f);
		OptionsLayerSubTitle.setPosition(0f,-BackgroundHeight/2f+OptionsLayerSubTitle.getHeight());
		this.attachChild(OptionsLayerSubTitle);
		
		this.setPosition(ResourceManager.getInstance().cameraWidth/2f, ResourceManager.getInstance().cameraHeight/2f+480f);
	}

	@Override
	public void onShowLayer() {
		this.registerUpdateHandler(SlideIn);
	}

	@Override
	public void onHideLayer() {
		this.registerUpdateHandler(SlideOut);
	}
	@Override
	public void onUnloadLayer() {
	}
}