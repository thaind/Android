public final class Singleton {

	/* The singleton class should contain a static variable of its
	 * containing class, usually named instance or INSTANCE.   */
	private static Singleton INSTANCE;
	
	// Empty Constructor
	Singleton(){
	}
	
	/* A singleton should have a getInstance() method which will be its only
	 * public static method. We can use this method to obtain the Singleton
	 * instance.  */
	public static Singleton getInstance(){
		// We have not yet made a call to the singleton, create a new one
		if(INSTANCE == null){
			INSTANCE = new Singleton();
		}
		
		return INSTANCE;
	}
}