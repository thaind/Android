package ifl.games.runtime.Managers;

import ifl.games.runtime.GameLevels.GameLevel;

import org.andengine.engine.handler.IUpdateHandler;

/** The GameManager class simply facilitates the checking of two conditions to
 *  determine if a level is completed or failed. Using that information, the
 *  GameManager then calls the appropriate methods set in the GameLevel class.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class GameManager implements IUpdateHandler {
	
	// ====================================================
	// INTERFACES
	// ====================================================
	public interface GameLevelGoal {
		public boolean isLevelCompleted();
		public void onLevelCompleted();
		public boolean isLevelFailed();
		public void onLevelFailed();
	}
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final GameManager INSTANCE = new GameManager();
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public GameLevelGoal mGameLevelGoal;
	public GameLevel mGameLevel;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public GameManager() {
		ResourceManager.getEngine().registerUpdateHandler(this);
	}

	// ====================================================
	// METHODS
	// ====================================================
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if(mGameLevelGoal!=null)
			if(mGameLevelGoal.isLevelCompleted()) {
				mGameLevelGoal.onLevelCompleted();
				mGameLevelGoal = null;
			} else if (mGameLevelGoal.isLevelFailed()) {
				mGameLevelGoal.onLevelFailed();
				mGameLevelGoal = null;
			}
	}
	@Override public void reset() {}
	
	public static void setGameLevel(GameLevel pGameLevel) {
		INSTANCE.mGameLevel = pGameLevel;
	}
	
	public static GameLevel getGameLevel() {
		return INSTANCE.mGameLevel;
	}
	
	public static void setGameLevelGoal(GameLevelGoal pGameLevelGoal) {
		INSTANCE.mGameLevelGoal = pGameLevelGoal;
	}
	
	public static void clearGameLevelGoal() {
		INSTANCE.mGameLevelGoal = null;
	}
	
}