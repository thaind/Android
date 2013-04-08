public class ObjectFactory {
	
	// Return a new LargeObject with the defined 'x' and 'y' member variables.
	public static LargeObject createLargeObject(final int pX, final int pY){
		return new LargeObject(pX, pY);
	}
	
	// Return a new SmallObject with the defined 'x' and 'y' member variables.
	public static SmallObject createSmallObject(final int pX, final int pY){
		return new SmallObject(pX, pY);
	}
	
	// BaseObject class
	public static class BaseObject {
		
		/* The mX and mY variables have no real purpose in this recipe, however in
		 * a real factory class, member variables might be used to define position,
		 * color, scale, and more, of a sprite or other entity.   */
		private int mX;
		private int mY;
		
		// BaseObject constructor, all subtypes should define an mX and mY value on creation
		BaseObject(final int pX, final int pY){
			this.mX = pX;
			this.mY = pY;
		}
		
		// LargeObject class
		public static class LargeObject extends BaseObject{

			// LargeObject constructor
			public LargeObject(int pX, int pY) {
				// Call the BaseObject constructor, storing the x and y values as
				// member variables
				super(pX, pY);
			}
			
		}
		
		// SmallObject class
		public static class SmallObject extends BaseObject{

			// SmallObject constructor
			public SmallObject(int pX, int pY) {
				// Call the BaseObject constructor, storing the x and y values as
				// member variables
				super(pX, pY);
			}
		}
	}
}
