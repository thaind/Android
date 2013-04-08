package ifl.games.runtime.GameLevels;

import ifl.games.runtime.GameLevels.Elements.MagneticCrate.MagneticCrateDef;

/** This class holds an array of all of the levels that can be played in the
 *  game as well as helper methods to retrieve specific levels.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class Levels {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final LevelDef[] AvailableLevels = new LevelDef[] {
		new LevelDef(1,1,new float[] {
				10f,100f,0f,0.9f,10f,
				3100f,3190f,0f,1f,10f},
				new MagneticCrateDef[] {MagneticCrateDef.SmallExplosive,MagneticCrateDef.LargeExplosive,MagneticCrateDef.SmallNormal},
				1,
				new BeamsInLevelDef[] {
				new BeamsInLevelDef(1400f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(3400f, 450f, 900f, 90f, BeamsInLevelDef.BeamType.WoodenDynamic),
				new BeamsInLevelDef(3400f, 932f, 600f, 0f, BeamsInLevelDef.BeamType.WoodenDynamic),
				new BeamsInLevelDef(4200f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(4200f, 432f, 200f, 0f, BeamsInLevelDef.BeamType.MetalDynamic)},
				new EnemiesInLevelDef[] {
				new EnemiesInLevelDef(3200f, 964f, EnemiesInLevelDef.EnemyType.Normal),
				new EnemiesInLevelDef(3600f, 964f, EnemiesInLevelDef.EnemyType.Normal),
				new EnemiesInLevelDef(4200f, 464f, EnemiesInLevelDef.EnemyType.Normal)}),
		new LevelDef(2,1,new float[] {
				10f,100f,0f,0.9f,10f,
				1800f,2100f,300f,0.33f,20f},
				new MagneticCrateDef[] {MagneticCrateDef.SmallNormal,MagneticCrateDef.SmallNormal,MagneticCrateDef.SmallNormal},
				3,
				new BeamsInLevelDef[] {
				new BeamsInLevelDef(1400f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2812.66f, 423.52f, 247.04f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2958.11f, 586.165f, 572.33f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2716.66f, 579.04f, 256f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2862.11f, 904.33f, 256f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2388.15f, 699.75f, 500f, -30f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(2579.96f, 1071.73f, 500f, -45f, BeamsInLevelDef.BeamType.MetalStatic)},
				new EnemiesInLevelDef[] {
				new EnemiesInLevelDef(2716.7f, 611.04f, EnemiesInLevelDef.EnemyType.Normal),
				new EnemiesInLevelDef(2862.1f, 936.33f, EnemiesInLevelDef.EnemyType.Normal)}),
		new LevelDef(3,1,new float[] {
				10f,100f,0f,0.9f,10f,
				1800f,3550f,150f,0.6f,10f},
				new MagneticCrateDef[] {MagneticCrateDef.LargeExplosive,MagneticCrateDef.MediumHorizontalExplosive,MagneticCrateDef.MediumHorizontalExplosive,MagneticCrateDef.LargeExplosive},
				1,
				new BeamsInLevelDef[] {
				new BeamsInLevelDef(1400f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
				new BeamsInLevelDef(3608f, 350f, 400f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(4008f, 350f, 400f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(4408f, 350f, 400f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(3808f, 582f, 400f, 0f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(4208f, 582f, 400f, 0f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(3808f, 814f, 400f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(4208f, 814f, 400f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				new BeamsInLevelDef(4008f, 1046f, 400f, 0f, BeamsInLevelDef.BeamType.MetalDynamic)},
				new EnemiesInLevelDef[] {
				new EnemiesInLevelDef(4008f, 614f, EnemiesInLevelDef.EnemyType.Normal),
				new EnemiesInLevelDef(3808f, 150f, EnemiesInLevelDef.EnemyType.Normal),
				new EnemiesInLevelDef(4208f, 150f, EnemiesInLevelDef.EnemyType.Normal)}),
				new LevelDef(4,1,new float[] {
				10f,100f,0f,0.9f,10f,
				3100f,3190f,0f,1f,10f},
				new MagneticCrateDef[] {MagneticCrateDef.LargeNormal,MagneticCrateDef.LargeNormal,MagneticCrateDef.LargeNormal},
				1,
				new BeamsInLevelDef[] {
						new BeamsInLevelDef(1400f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3165.85f, 197.5f, 395f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3140.19f, 416.94f, 290.5f, 142f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3000f, 559.93f, 128f, 121.75f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2948.79f, 672.15f, 128f, 106.2f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2932.5f, 791.6f, 128f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3176.84f, 824.18f, 128f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2932.5f, 1108.07f, 350f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3073.67f, 1108.07f, 350f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3450.4f, 1310.51f, 1100f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2982.66f, 889.11f, 400f, 0f, BeamsInLevelDef.BeamType.WoodenDynamic),
						new BeamsInLevelDef(3001.28f, 1096.22f, 300f, 90f, BeamsInLevelDef.BeamType.MetalDynamic),
				},
				new EnemiesInLevelDef[] {
						new EnemiesInLevelDef(2696f, 0f, EnemiesInLevelDef.EnemyType.Normal),
						new EnemiesInLevelDef(3456f, 0f, EnemiesInLevelDef.EnemyType.Normal),
						new EnemiesInLevelDef(3765f, 0f, EnemiesInLevelDef.EnemyType.Normal)}),
				new LevelDef(5,1,new float[] {
				10f,100f,0f,0.9f,10f,
				3100f,3190f,0f,1f,10f},
				new MagneticCrateDef[] {MagneticCrateDef.SmallNormal,MagneticCrateDef.SmallNormal,MagneticCrateDef.SmallNormal},
				1,
				new BeamsInLevelDef[] {
						new BeamsInLevelDef(1400f, 200f, 400f, 90f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3432f, 32f, 128f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3232f, 142f, 128f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3032f, 252f, 128f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2832f, 352f, 128f, 0f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(3217f, 1101.7f, 1000f, 30f, BeamsInLevelDef.BeamType.MetalStatic),
						new BeamsInLevelDef(2832f, 584f, 400f, 90f, BeamsInLevelDef.BeamType.WoodenDynamic),
						new BeamsInLevelDef(3032f, 534f, 500f, 90f, BeamsInLevelDef.BeamType.WoodenDynamic),
						new BeamsInLevelDef(3232f, 479f, 610f, 90f, BeamsInLevelDef.BeamType.WoodenDynamic),
						new BeamsInLevelDef(3432f, 424f, 720f, 90f, BeamsInLevelDef.BeamType.WoodenDynamic)},
				new EnemiesInLevelDef[] {
						new EnemiesInLevelDef(3700f, 0f, EnemiesInLevelDef.EnemyType.Normal),
						new EnemiesInLevelDef(4000f, 0f, EnemiesInLevelDef.EnemyType.Normal)})
		

	};
	
	// ====================================================
	// METHODS
	// ====================================================
	public static LevelDef getLevelDef(final int pLevelIndex, final int pWorldIndex) {
		for(LevelDef curLevelDef : AvailableLevels)
			if(curLevelDef.doIndicesMatch(pLevelIndex, pWorldIndex))
				return curLevelDef;
		return null;
	}
	
	public static boolean isNextLevelInCurrentWorld(final LevelDef pCurrentLevel) {
		if(getLevelDef(pCurrentLevel.mLevelIndex+1,pCurrentLevel.mWorldIndex) != null)
			return true;
		else
			return false;
	}
	
	public static boolean isThereAnotherWorldAfterCurrentWorld(final LevelDef pCurrentLevel) {
		if(getLevelDef(0,pCurrentLevel.mWorldIndex+1) != null)
			return true;
		else
			return false;
	}
	
	public static int getNumLevelsInWorld(final int pWorldIndex) {
		int LevelCounter = 0;
		for(LevelDef curLevelDef : AvailableLevels)
			if(curLevelDef.mWorldIndex == pWorldIndex)
				LevelCounter++;
		return LevelCounter;
	}
	
	// ====================================================
	// CLASSES
	// ====================================================
	public static class EnemiesInLevelDef {
		public enum EnemyType {
			Normal
		}
		public final float mX;
		public final float mY;
		public final EnemyType mEnemyType;
		
		public EnemiesInLevelDef(final float pX, final float pY, final EnemyType pEnemyType) {
			mX = pX;
			mY = pY;
			mEnemyType = pEnemyType;
		}
	}
	
	public static class BeamsInLevelDef {
		public enum BeamType {
			MetalStatic, MetalDynamic, WoodenDynamic
		}
		public final float mX;
		public final float mY;
		public final float mLength;
		public final float mRotation;
		public final BeamType mBeamType;
		
		public BeamsInLevelDef(final float pX, final float pY, final float pLength, final float pRotation, final BeamType pBeamType) {
			mX = pX;
			mY = pY;
			mLength = pLength;
			mRotation = pRotation;
			mBeamType = pBeamType;
		}
	}
	
	public static class LevelDef {
		public final int mLevelIndex;
		public final int mWorldIndex;
		public final float[] mTerrainSlopes;
		public final MagneticCrateDef[] mCrates;
		public final BeamsInLevelDef[] mBeams;
		public final EnemiesInLevelDef[] mEnemies;
		public final int mExpectedNumberCratesToCompleteLevel;
		
		public LevelDef(final int pLevelIndex, final int pWorldIndex, final float[] pTerrainSlopes, final MagneticCrateDef[] pCrates, final int pExpectedNumberCratesToCompleteLevel, final BeamsInLevelDef[] pBeamsInLevelDef, final EnemiesInLevelDef[] pEnemies) {
			mLevelIndex = pLevelIndex;
			mWorldIndex = pWorldIndex;
			mTerrainSlopes = pTerrainSlopes;
			mCrates = pCrates;
			mExpectedNumberCratesToCompleteLevel = pExpectedNumberCratesToCompleteLevel;
			mBeams = pBeamsInLevelDef;
			mEnemies = pEnemies;
		}
		
		public boolean doIndicesMatch(final int pLevelIndex, final int pWorldIndex) {
			if(mLevelIndex == pLevelIndex)
				if(mWorldIndex == pWorldIndex)
					return true;
			return false;
		}
	}
}