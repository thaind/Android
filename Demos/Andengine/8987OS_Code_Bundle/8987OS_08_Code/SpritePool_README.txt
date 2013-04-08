The SpritePool class is an extension of the GenericPool. This class allows for the reusability of sprites based on a recycling system whose purpose is to limit the amount of memory allocations needed in a game level or other situations.
The class makes use of two main methods which will be called within the game code structure. These methods include:

1. obtainPoolItem() - This method is in place to initialize objects that are being pulled from the sprite pool to be placed on the scene. Ideally, this method should be used to set the sprite to visible, allow updates, and reset any other properties of the sprite back to a default state.
2. recyclePoolItem(pItem) - This method allows us to recycle (return) sprites back into the pool, which we can obtain again in the future; hense the recycling. This method should take care of anything relating to the uninitialization of a sprite or object, setting visibility to false, clearing any handlers attached to the object, as well as ignoring all updates on the main thread. 

Installation:
1. Import the SpritePool class into your project. Creating a sprite pool requires a properly loaded TextureRegion to be available for the pool's constructor.