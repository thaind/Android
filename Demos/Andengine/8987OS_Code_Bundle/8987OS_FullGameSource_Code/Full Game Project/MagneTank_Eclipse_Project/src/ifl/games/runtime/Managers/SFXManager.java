package ifl.games.runtime.Managers;

import ifl.games.runtime.MagneTankActivity;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.debug.Debug;

/** This class handles the playback of music and sounds as well as
 *  their muted state.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public class SFXManager extends Object
{
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final SFXManager INSTANCE = new SFXManager();
	
	private static Music mMusic;
	
	private static Sound mClick;
	private static Sound mCrate;
	private static Sound mExplosion;
	private static Sound mShoot;
	private static Sound mWood;
	
	// ====================================================
	// INSTANCE GETTER
	// ====================================================
	public static SFXManager getInstance(){
		return INSTANCE;
	}
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public boolean mSoundsMuted;
	public boolean mMusicMuted;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public SFXManager() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(ResourceManager.getActivity().getMusicManager(), ResourceManager.getActivity(), "music.ogg");
			mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}

		SoundFactory.setAssetBasePath("sounds/");
		try {
			mClick = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "click.mp3");
		} catch (final IOException e) { Debug.e(e); }
		try {
			mCrate = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "crate.mp3");
		} catch (final IOException e) { Debug.e(e); }
		try {
			mExplosion = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "explosion.mp3");
		} catch (final IOException e) { Debug.e(e); }
		try {
			mShoot = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "shoot.mp3");
		} catch (final IOException e) { Debug.e(e); }
		try {
			mWood = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "wood.mp3");
		} catch (final IOException e) { Debug.e(e); }
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	private static void setVolumeForAllSounds(final float pVolume) {
		mClick.setVolume(pVolume);
		mCrate.setVolume(pVolume);
		mExplosion.setVolume(pVolume);
		mShoot.setVolume(pVolume);
		mWood.setVolume(pVolume);
	}
	
	public static boolean isSoundMuted() {
		return getInstance().mSoundsMuted;
	}
	
	public static void setSoundMuted(final boolean pMuted) {
		getInstance().mSoundsMuted = pMuted;
		setVolumeForAllSounds((getInstance().mSoundsMuted? 0f:1f));
		MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_SOUNDS_MUTED, (getInstance().mSoundsMuted? 1:0));
	}
	
	public static boolean toggleSoundMuted() {
		getInstance().mSoundsMuted = !getInstance().mSoundsMuted;
		setVolumeForAllSounds((getInstance().mSoundsMuted? 0f:1f));
		MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_SOUNDS_MUTED, (getInstance().mSoundsMuted? 1:0));
		return getInstance().mSoundsMuted;
	}
	
	public static boolean isMusicMuted() {
		return getInstance().mMusicMuted;
	}
	
	public static void setMusicMuted(final boolean pMuted) {
		getInstance().mMusicMuted = pMuted;
		if(getInstance().mMusicMuted) mMusic.pause(); else mMusic.play();
		MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_MUSIC_MUTED, (getInstance().mMusicMuted? 1:0));
	}
	
	public static boolean toggleMusicMuted() {
		getInstance().mMusicMuted = !getInstance().mMusicMuted;
		if(getInstance().mMusicMuted) mMusic.pause(); else mMusic.play();
		MagneTankActivity.writeIntToSharedPreferences(MagneTankActivity.SHARED_PREFS_MUSIC_MUTED, (getInstance().mMusicMuted? 1:0));
		return getInstance().mMusicMuted;
	}
	
	
	public static void playMusic() {
		if(!isMusicMuted())
			mMusic.play();
	}
	
	public static void pauseMusic() {
		mMusic.pause();
	}
	
	public static void resumeMusic() {
		if(!isMusicMuted())
			mMusic.resume();
	}
	
	public static float getMusicVolume() {
		return mMusic.getVolume();
	}
	
	public static void setMusicVolume(final float pVolume) {
		mMusic.setVolume(pVolume);
	}
	
	public static void playClick(final float pRate, final float pVolume) {
		playSound(mClick,pRate,pVolume);
	}
	
	public static void playCrate(final float pRate, final float pVolume) {
		playSound(mCrate,pRate,pVolume);
	}
	
	public static void playExplosion(final float pRate, final float pVolume) {
		playSound(mExplosion,pRate,pVolume);
	}
	
	public static void playShoot(final float pRate, final float pVolume) {
		playSound(mShoot,pRate,pVolume);
	}
	
	public static void playWood(final float pRate, final float pVolume) {
		playSound(mWood,pRate,pVolume);
	}
	
	private static void playSound(final Sound pSound, final float pRate, final float pVolume) {
		if(SFXManager.isSoundMuted()) return;
		pSound.setRate(pRate);
		pSound.setVolume(pVolume);
		pSound.play();
	}
}