package ifl.games.runtime;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.util.time.TimeConstants;

/** This Engine object acts exactly like a FixedStepEngine when the
 *  EnableFixedStep() method has been called.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class SwitchableFixedStepEngine extends Engine {
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private final long mStepLength;
	private long mSecondsElapsedAccumulator;
	private boolean mFixedStepEnabled = false;
	public final int mStepsPerSecond;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public SwitchableFixedStepEngine(final EngineOptions pEngineOptions, final int pStepsPerSecond, final boolean pFixedStepEnabled) {
		super(pEngineOptions);
		this.mStepLength = TimeConstants.NANOSECONDS_PER_SECOND / pStepsPerSecond;
		mFixedStepEnabled = pFixedStepEnabled;
		mStepsPerSecond = pStepsPerSecond;
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void EnableFixedStep() {
		mFixedStepEnabled = true;
	}
	
	public void DisableFixedStep() {
		mFixedStepEnabled = false;
	}
	
	@Override
	public void onUpdate(final long pNanosecondsElapsed) throws InterruptedException {
		if(mFixedStepEnabled)
		{
			this.mSecondsElapsedAccumulator += pNanosecondsElapsed;

			final long stepLength = this.mStepLength;
			while(this.mSecondsElapsedAccumulator >= stepLength) {
				super.onUpdate(stepLength);
				this.mSecondsElapsedAccumulator -= stepLength;
			}
		} else {
			super.onUpdate(pNanosecondsElapsed);
		}
	}
	
}