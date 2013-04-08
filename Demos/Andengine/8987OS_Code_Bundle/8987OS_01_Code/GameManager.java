public class GameManager {
	
	/* Since this class is a singleton, we must declare an instance
	 * of this class within itself. The singleton will be instantiated
	 * a single time during the course of an application's full life-cycle
	 */
	private static GameManager INSTANCE;
	
	private static final int INITIAL_SCORE = 0;
	private static final int INITIAL_BIRD_COUNT = 3;
	private static final int INITIAL_ENEMY_COUNT = 5;
	
	/* The game manager should keep track of certain data involved in
	 * our game. This particular game manager holds data for score, bird
	 * counts and enemy counts.
	 */
	private int mCurrentScore;
	private int mBirdCount;
	private int mEnemyCount;
	
	// The constructor does not do anything for this singleton
	GameManager(){
	}
	
	/* For a singleton class, we must have some method which provides
	 * access to the class instance. getInstance is a static method,
	 * which means we can access it globally (within other classes).
	 * If the GameManager has not yet been instantiated, we create a 
	 * new one.
	 */
	public static GameManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new GameManager();
		}
		return INSTANCE;
	}
	
	// get the current score
	public int getCurrentScore(){
		return this.mCurrentScore;
	}
	
	// get the bird count
	public int getBirdCount(){
		return this.mBirdCount;
	}

	// get the enemy count
	public int getEnemyCount(){
		return this.mEnemyCount;
	}
	
	// increase the current score, most likely when an enemy is destroyed
	public void incrementScore(int pIncrementBy){
		mCurrentScore += pIncrementBy;
	}
	
	// Any time a bird is launched, we decrement our bird count
	public void decrementBirdCount(){
		mBirdCount -= 1;
	}
	
	// Any time an enemy is hit/destroyed, we decrement the enemy count
	public void decrementEnemyCount(){
		mEnemyCount -= 1;
	}
	
	// Resetting the game simply means we must revert back to initial values.
	public void resetGame(){
		this.mCurrentScore = GameManager.INITIAL_SCORE;
		this.mBirdCount = GameManager.INITIAL_BIRD_COUNT;
		this.mEnemyCount = GameManager.INITIAL_ENEMY_COUNT;
	}
}