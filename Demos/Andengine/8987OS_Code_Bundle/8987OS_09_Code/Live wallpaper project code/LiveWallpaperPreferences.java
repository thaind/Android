public class LiveWallpaperPreferences {

	// Handle for the LiveWallpaperPreferences singleton instance
	private static LiveWallpaperPreferences INSTANCE;
	
	// String containing the live wallpaper's preferences name
	private static final String PREFERENCE_NAME = "LWP_PREFS";
	
	// String containing the key to the particle speed preference value
	private static final String PARTICLE_SPEED_KEY = "PARTICLE_SPEED";
	
	// Default value for the particle speed on first initialization of the
	// wallpaper
	private static final int DEFAULT_PARTICLE_SPEED = 10;
	
	// Shared preference objects
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferencesEditor;
	
	// Shared preference values
	private int mParticleSpeed;
	
	LiveWallpaperPreferences(){
		// Do nothing...
	}
	
	// Obtain the LiveWallpaperPreferences instance
	public static LiveWallpaperPreferences getInstance(){
		if(INSTANCE == null){
			INSTANCE = new LiveWallpaperPreferences();
		}
		return INSTANCE;
	}
	
	// Initialize the wallpaper's preference file
	public void initPreferences(Context pContext){
		if(mSharedPreferences == null){
			mSharedPreferences = pContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			mSharedPreferencesEditor = mSharedPreferences.edit();
			
			mParticleSpeed = mSharedPreferences.getInt(PARTICLE_SPEED_KEY, DEFAULT_PARTICLE_SPEED);
		}
	}
	
	// Return the saved value for the mParticleSpeed variable
	public int getParticleSpeed(){
		return -mParticleSpeed;
	}
	
	// Save the mParticleSpeed value to the wallpaper's preference file
	public void setParticleSpeed(int pParticleSpeed){
		this.mParticleSpeed = pParticleSpeed;
		this.mSharedPreferencesEditor.putInt(PARTICLE_SPEED_KEY, mParticleSpeed);
		this.mSharedPreferencesEditor.commit();
	}
}
