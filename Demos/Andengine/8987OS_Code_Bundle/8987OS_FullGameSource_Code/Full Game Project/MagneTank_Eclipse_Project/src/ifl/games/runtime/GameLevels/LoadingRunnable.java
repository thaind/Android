package ifl.games.runtime.GameLevels;

/** The LoadingRunnable class acts as a Runnable object while also updating the
 *  loading screen in the ManagedGameScene class. An ArrayList of
 *  LoadingRunnables is present in each ManagedGameScene to give the
 *  developer as much or as little control over how much loading progression 
 *  is shown to the player. It is important to note that while the updating of
 *  the loading screen is not processor-intensive in MagneTank, a more
 *  complicated, graphically complex loading screen may take a large toll
 *  on the loading times of each level.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class LoadingRunnable implements Runnable {
	
	private final String mLoadingText;
	private final ManagedGameScene mGameScene;
	
	public abstract void onLoad();
	
	public LoadingRunnable(String pLoadingText, ManagedGameScene pGameScene)
	{
		mLoadingText = pLoadingText;
		mGameScene = pGameScene;
	}
	
	@Override
	public void run() {
		// Avoid using String.isEmpty() because at least a few phones do not support it.
		// An alternative is to use String.length()==0
		if(mLoadingText.trim().length()!=0)
			if(mGameScene!=null)
				if(mGameScene.mLoadingText!=null)
					if(mLoadingText!=null)
						mGameScene.mLoadingText.setText(mGameScene.mLoadingText.getText() + "\n" + mLoadingText);
		onLoad();
	}
}