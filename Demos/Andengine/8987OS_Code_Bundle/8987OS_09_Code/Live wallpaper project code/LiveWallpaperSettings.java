public class LiveWallpaperSettings extends Activity{

	// Handle to the 'DefinedParticleSpeedTextView' view which displays
	// the current mParticleSpeed value on-screen
	private TextView mParticleSpeedTextView;
	
	// Handle to the 'ParticleSeekBar' view which is used to control
	// the value of mParticleSpeed pending user touch events
	private SeekBar mParticleSeekBar;
	
	// Variable containing the movement speed value of the particles
	private int mParticleSpeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set this activity to use the 'settings_main.xml' layout
		this.setContentView(R.layout.settings_main);
		
		// Obtain the handle to the layout's TextView
		mParticleSpeedTextView = (TextView) findViewById(R.id.DefinedParticleSpeedTextView);
		
		// Create a RunnablePoolItem to be called when the TextView needs updates
		// from the UI thread
		final RunnablePoolItem mUpdateTextRunnable = new RunnablePoolItem(){
			@Override
			public void run() {
				// Update TextView
				mParticleSpeedTextView.setText("-" + String.valueOf(mParticleSpeed));
			}
		};

		// Obtain the saved particle speed from the preference file, converting it 
		// to a negative value in order to move the particles downward
		mParticleSpeed = -LiveWallpaperPreferences.getInstance().getParticleSpeed();
		
		// Update the TextView initially when the activity starts up -
		// Changes to Android's views must be called inside the UI thread
		this.runOnUiThread(mUpdateTextRunnable);
		
		// Obtain the handle to the layout's SeekBar
		mParticleSeekBar = (SeekBar) findViewById(R.id.ParticleSeekBar);

		// Changes to Android's views must be called inside the UI thread
		this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				// Set the initial SeekBar progress to match the particle speed value
				mParticleSeekBar.setProgress(mParticleSpeed);
			}
		});
		
		// Initialize the SeekBar's listener
		mParticleSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			// OnProgressChanged represents a movement on the slider
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Set the mParticleSpeed depending on the SeekBar's position(progress)
				mParticleSpeed = progress;
				
				// Update the mParticleSpeedTextView with the current SeekBar progress value
				LiveWallpaperSettings.this.runOnUiThread(mUpdateTextRunnable);
				
				// Recycle the runnable pool item so that it can be reused instead of
				// creating multiple
				if(!mUpdateTextRunnable.isRecycled()){
					mUpdateTextRunnable.recycle();
				}
			}

			// OnStartTrackingTouch represents a user's initial touch on the slider
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Do nothing...
			}

			// OnStopTrackingTouch represents a user letting go of the slider
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Do nothing...
			}
		});
	}

	@Override
	protected void onPause() {
		// onPause(), we save the current value of mParticleSpeed to the preference file.
		// Anytime the wallpaper's lifecycle is executed, the mParticleSpeed value is loaded
		LiveWallpaperPreferences.getInstance().setParticleSpeed(mParticleSpeed);
		super.onPause();
	}

}
