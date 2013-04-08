public class SpritePool extends GenericPool<Sprite>{

	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;

	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	public SpritePool(ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
		this.mTextureRegion = pTextureRegion;
		this.mVertexBufferObjectManager = pVertexBufferObjectManager;
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	@Override
	protected Sprite onAllocatePoolItem() {
		return new Sprite(0, 0, this.mTextureRegion, this.mVertexBufferObjectManager);
	}

	/* obtainPoolItem handles the re-initialization of every object being
	 obtained from the pool. In the case of a sprite pool, some general
	 method calls would be to reposition the sprite, as well as set visible
	 and allow updates within the update thread */
	public synchronized Sprite obtainPoolItem(final float pX, final float pY) {
		Sprite sprite = super.obtainPoolItem();
		
		sprite.setPosition(pX, pY);
		sprite.setVisible(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);

		return sprite;
	}
	
	/* onHandleRecycleItem is called when we use the recyclePoolItem() method.
	 This method should handle the uninitializing of the object being recycled.
	 In the case of a sprite pool, some methods that should be called include
	 setting visibility to false, ignoring updates, and clearing modifiers and handlers */
	@Override
	protected void onHandleRecycleItem(Sprite pItem) {
		super.onHandleRecycleItem(pItem);
		
		pItem.setVisible(false);
		pItem.setIgnoreUpdate(true);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
	}
}
