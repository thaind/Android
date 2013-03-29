/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

import java.util.ArrayList;

/**
 *
 * @author rick
 */
public class PXSystem {
        // ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
    
    PXEmitter mPXEmitter;
    int minRate;
    int maxRate;
    int maxParticles;
    String mTextureName;
    
    float mPSIAccelMinX = 0.0f;
    float mPSIAccelMaxX = 0.0f;
    float mPSIAccelMinY = 0.0f;
    float mPSIAccelMaxY = 0.0f;
    float mPSIVelMinX = 0.0f;
    float mPSIVelMaxX = 0.0f;
    float mPSIVelMinY = 0.0f;
    float mPSIVelMaxY = 0.0f;
    float mPSIAlphaMin = 0.0f;
    float mPSIAlphaMax = 0.0f;
    float mPSIRotationMin = 0.0f;
    float mPSIRotationMax = 0.0f;
    float mPSIRedMin = 0.0f;
    float mPSIRedMax = 0.0f;
    float mPSIGreenMin = 0.0f;
    float mPSIGreenMax = 0.0f;
    float mPSIBlueMin = 0.0f;
    float mPSIBlueMax = 0.0f;
    float mPSIExpireMin = 0.0f;
    float mPSIExpireMax = 0.0f;
    boolean mGravity = false;
    ArrayList<PXAlphaModifier> mAlphaMods;
    ArrayList<PXColorModifier> mColorMods;
    ArrayList<PXRotationModifier> mRotationMods;
    ArrayList<PXScaleModifier> mScaleMods;
    PXExpireModifier mExpireMod;
    

    	// ===========================================================
	// Constructors
	// ===========================================================
    public PXSystem( PXEmitter pPXEmitter, int pMinRate, int pMaxRate, int pMaxParticles, String pTextureName){
        mPXEmitter = pPXEmitter;
        minRate = pMinRate;
        maxRate = pMaxRate;
        maxParticles = pMaxParticles;
        mTextureName = pTextureName;
        
        mAlphaMods = new ArrayList<PXAlphaModifier>();
        mColorMods = new ArrayList<PXColorModifier>();
        mRotationMods = new ArrayList<PXRotationModifier>();
        mScaleMods = new ArrayList<PXScaleModifier>();
    }
    
        // ===========================================================
	// Getters and Setters
	// ===========================================================

    public void setInitialAcceleration ( float pAccelMinX, float pAccelMaxX, float pAccelMinY, float pAccelMaxY){
        mPSIAccelMinX = pAccelMinX;
        mPSIAccelMaxX = pAccelMaxX;
        mPSIAccelMinY = pAccelMinY;
        mPSIAccelMaxY = pAccelMaxY;
    }
    
    public void setInitialAlpha ( float pAlphaMin, float pAlphaMax) {
        mPSIAlphaMin = pAlphaMin;
        mPSIAlphaMax = pAlphaMax;
    }
    
    public void setInitialColor ( float pRedMin, float pRedMax, float pGreenMin, float pGreenMax, float pBlueMin, float pBlueMax){
        mPSIRedMin = pRedMin;
        mPSIRedMax = pRedMax;
        mPSIGreenMin = pGreenMin;
        mPSIGreenMax = pGreenMax;
        mPSIBlueMin = pBlueMin;
        mPSIBlueMax = pBlueMax;
    }
    
    public void setGravity (boolean pGravity) {
        mGravity = pGravity;
    }
    
    public void setInitialRotation (float pRotationMin, float pRotationMax){
        mPSIRotationMin = pRotationMin;
        mPSIRotationMax = pRotationMax;
    }
    
    public void setInitialVelocity ( float pVelMinX, float pVelMaxX, float pVelMinY, float pVelMaxY){
        mPSIVelMinX = pVelMinX;
        mPSIVelMaxX = pVelMaxX;
        mPSIVelMinY = pVelMinY;
        mPSIVelMaxY = pVelMaxY;
    }
    
    public void addAlphaModifier ( PXAlphaModifier pPXAlphaModifier ) {
        mAlphaMods.add(pPXAlphaModifier);
    }
    public void addColorModifier ( PXColorModifier pPXColorModifier ) {
        mColorMods.add(pPXColorModifier);
    }
    public void addExpireModifier ( PXExpireModifier pPXExpireModifier ) {
        mExpireMod = pPXExpireModifier;
    }
    public void addRotationModifier ( PXRotationModifier pPXRotationModifier ) {
        mRotationMods.add(pPXRotationModifier);
    }
    public void addScaleModifier ( PXScaleModifier pPXScaleModifier ) {
        mScaleMods.add(pPXScaleModifier);
    }
}
