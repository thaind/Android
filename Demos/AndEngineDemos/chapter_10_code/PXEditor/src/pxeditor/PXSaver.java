/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author rick
 */
public class PXSaver {
    PrintWriter oStream;
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PXSaver() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================


	public void save(final PXSystem pPXSystem, final File pSaveFile) throws PXSaveException {
		try{
                    oStream =  new PrintWriter(new FileWriter(pSaveFile));
		} catch (final IOException e) {
			throw new PXSaveException(e);
		}
                
                tagOut(0,PXConstants.TAG_PCONF+">");
                 tagOut(1,PXConstants.TAG_EMITTER);
                  attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_SHAPE,pPXSystem.mPXEmitter.mPEShape);
                  attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_CENTER_X,Float.toString(pPXSystem.mPXEmitter.mPECenterX));
                  attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_CENTER_Y,Float.toString(pPXSystem.mPXEmitter.mPECenterY));
                  if ((pPXSystem.mPXEmitter.mPEShape.equals(PXConstants.TAG_EMITTER_ATTRIBUTE_SHAPE_VALUE_CIRCLE)) ||
                          (pPXSystem.mPXEmitter.mPEShape.equals(PXConstants.TAG_EMITTER_ATTRIBUTE_SHAPE_VALUE_CIRCLE_OUTLINE))) {
                    attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_RADIUS_X,Float.toString(pPXSystem.mPXEmitter.mPERadiusX));
                    attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_RADIUS_Y,Float.toString(pPXSystem.mPXEmitter.mPERadiusY));
                  }
                  if ((pPXSystem.mPXEmitter.mPEShape.equals(PXConstants.TAG_EMITTER_ATTRIBUTE_SHAPE_VALUE_RECTANGLE)) ||
                          (pPXSystem.mPXEmitter.mPEShape.equals(PXConstants.TAG_EMITTER_ATTRIBUTE_SHAPE_VALUE_RECTANGLE_OUTLINE))) {
                    attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_WIDTH,Float.toString(pPXSystem.mPXEmitter.mPEWidth));
                    attOut(2,PXConstants.TAG_EMITTER_ATTRIBUTE_HEIGHT,Float.toString(pPXSystem.mPXEmitter.mPEHeight));
                  }
                  oStream.println("\t\t>");
                 tagEnd(1,PXConstants.TAG_EMITTER);
                 tagOut(1,PXConstants.TAG_SYSTEM);
                  attOut(2,PXConstants.TAG_SYSTEM_TEXTURE,pPXSystem.mTextureName);
                  attOut(2,PXConstants.TAG_SYSTEM_MIN_RATE,Integer.toString(pPXSystem.minRate));
                  attOut(2,PXConstants.TAG_SYSTEM_MAX_RATE,Integer.toString(pPXSystem.maxRate));
                  lastAttOut(2,PXConstants.TAG_SYSTEM_MAX_PARTICLES,Integer.toString(pPXSystem.maxParticles));
                  tagOut(2,PXConstants.TAG_INITIAL_ACCELERATION);
                    attOut(3,PXConstants.TAG_INITIAL_ACCELERATION_ATTRIBUTE_MIN_X,Float.toString(pPXSystem.mPSIAccelMinX));
                    attOut(3,PXConstants.TAG_INITIAL_ACCELERATION_ATTRIBUTE_MAX_X,Float.toString(pPXSystem.mPSIAccelMaxX));
                    attOut(3,PXConstants.TAG_INITIAL_ACCELERATION_ATTRIBUTE_MIN_Y,Float.toString(pPXSystem.mPSIAccelMinY));
                    lastAttOut(3,PXConstants.TAG_INITIAL_ACCELERATION_ATTRIBUTE_MAX_Y,Float.toString(pPXSystem.mPSIAccelMaxY));
                  tagEnd(2,PXConstants.TAG_INITIAL_ACCELERATION);
                  tagOut(2,PXConstants.TAG_INITIAL_ALPHA);
                    attOut(3,PXConstants.TAG_INITIAL_ALPHA_ATTRIBUTE_MIN_ALPHA,Float.toString(pPXSystem.mPSIAlphaMin));
                    lastAttOut(3,PXConstants.TAG_INITIAL_ALPHA_ATTRIBUTE_MAX_ALPHA,Float.toString(pPXSystem.mPSIAlphaMax));
                  tagEnd(2,PXConstants.TAG_INITIAL_ALPHA);
                  tagOut(2,PXConstants.TAG_INITIAL_COLOR);
                    attOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MIN_RED,Float.toString(pPXSystem.mPSIRedMin));
                    attOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MAX_RED,Float.toString(pPXSystem.mPSIRedMax));
                    attOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MIN_GREEN,Float.toString(pPXSystem.mPSIGreenMin));
                    attOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MAX_GREEN,Float.toString(pPXSystem.mPSIGreenMax));
                    attOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MIN_BLUE,Float.toString(pPXSystem.mPSIBlueMin));
                    lastAttOut(3,PXConstants.TAG_INITIAL_COLOR_ATTRIBUTE_MAX_BLUE,Float.toString(pPXSystem.mPSIBlueMax));
                  tagEnd(2,PXConstants.TAG_INITIAL_COLOR);
                  tagOut(2,PXConstants.TAG_INITIAL_GRAVITY);
                    lastAttOut(3,PXConstants.TAG_INITIAL_GRAVITY_ATTRIBUTE_VALUE,Boolean.toString(pPXSystem.mGravity));
                  tagEnd(2,PXConstants.TAG_INITIAL_GRAVITY);
                  tagOut(2,PXConstants.TAG_INITIAL_ROTATION);
                    attOut(3,PXConstants.TAG_INITIAL_ROTATION_ATTRIBUTE_MIN_ROTATION,Float.toString(pPXSystem.mPSIRotationMin));
                    lastAttOut(3,PXConstants.TAG_INITIAL_ROTATION_ATTRIBUTE_MAX_ROTATION,Float.toString(pPXSystem.mPSIRotationMax));
                  tagEnd(2,PXConstants.TAG_INITIAL_ROTATION);
                  tagOut(2,PXConstants.TAG_INITIAL_VELOCITY);
                    attOut(3,PXConstants.TAG_INITIAL_VELOCITY_ATTRIBUTE_MIN_X,Float.toString(pPXSystem.mPSIVelMinX));
                    attOut(3,PXConstants.TAG_INITIAL_VELOCITY_ATTRIBUTE_MAX_X,Float.toString(pPXSystem.mPSIVelMaxX));
                    attOut(3,PXConstants.TAG_INITIAL_VELOCITY_ATTRIBUTE_MIN_Y,Float.toString(pPXSystem.mPSIVelMinY));
                    lastAttOut(3,PXConstants.TAG_INITIAL_VELOCITY_ATTRIBUTE_MAX_Y,Float.toString(pPXSystem.mPSIVelMaxY));
                  tagEnd(2,PXConstants.TAG_INITIAL_VELOCITY);
                  for (int i=0; i<pPXSystem.mAlphaMods.size(); i++){  
                    tagOut(2,PXConstants.TAG_MODIFY_ALPHA);
                        attOut(3,PXConstants.TAG_MODIFY_ALPHA_ATTRIBUTE_FROM_ALPHA,Float.toString(pPXSystem.mAlphaMods.get(i).mFromAlpha));
                        attOut(3,PXConstants.TAG_MODIFY_ALPHA_ATTRIBUTE_TO_ALPHA,Float.toString(pPXSystem.mAlphaMods.get(i).mToAlpha));
                        attOut(3,PXConstants.TAG_MODIFY_ALPHA_ATTRIBUTE_FROM_TIME,Float.toString(pPXSystem.mAlphaMods.get(i).mFromTime));
                        lastAttOut(3,PXConstants.TAG_MODIFY_ALPHA_ATTRIBUTE_TO_TIME,Float.toString(pPXSystem.mAlphaMods.get(i).mToTime));
                    tagEnd(2,PXConstants.TAG_MODIFY_ALPHA);
                  }
                  for (int i=0; i<pPXSystem.mColorMods.size(); i++){  
                    tagOut(2,PXConstants.TAG_MODIFY_COLOR);
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_FROM_RED,Float.toString(pPXSystem.mColorMods.get(i).mFromRed));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_TO_RED,Float.toString(pPXSystem.mColorMods.get(i).mToRed));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_FROM_GREEN,Float.toString(pPXSystem.mColorMods.get(i).mFromGreen));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_TO_GREEN,Float.toString(pPXSystem.mColorMods.get(i).mToGreen));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_FROM_BLUE,Float.toString(pPXSystem.mColorMods.get(i).mFromBlue));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_TO_BLUE,Float.toString(pPXSystem.mColorMods.get(i).mToBlue));
                        attOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_FROM_TIME,Float.toString(pPXSystem.mColorMods.get(i).mFromTime));
                        lastAttOut(3,PXConstants.TAG_MODIFY_COLOR_ATTRIBUTE_TO_TIME,Float.toString(pPXSystem.mColorMods.get(i).mToTime));
                    tagEnd(2,PXConstants.TAG_MODIFY_COLOR);
                  }
                  tagOut(2,PXConstants.TAG_MODIFY_EXPIRE);
                      attOut(3,PXConstants.TAG_MODIFY_EXPIRE_ATTRIBUTE_MIN_LIFETIME,Float.toString(pPXSystem.mExpireMod.mMinLifeTime));
                      lastAttOut(3,PXConstants.TAG_MODIFY_EXPIRE_ATTRIBUTE_MAX_LIFETIME,Float.toString(pPXSystem.mExpireMod.mMaxLifeTime));
                  tagEnd(2,PXConstants.TAG_MODIFY_EXPIRE);
                  for (int i=0; i<pPXSystem.mRotationMods.size(); i++){  
                    tagOut(2,PXConstants.TAG_MODIFY_ROTATION);
                        attOut(3,PXConstants.TAG_MODIFY_ROTATION_ATTRIBUTE_FROM_ROTATION,Float.toString(pPXSystem.mRotationMods.get(i).mFromRotation));
                        attOut(3,PXConstants.TAG_MODIFY_ROTATION_ATTRIBUTE_TO_ROTATION,Float.toString(pPXSystem.mRotationMods.get(i).mToRotation));
                        attOut(3,PXConstants.TAG_MODIFY_ROTATION_ATTRIBUTE_FROM_TIME,Float.toString(pPXSystem.mRotationMods.get(i).mFromTime));
                        lastAttOut(3,PXConstants.TAG_MODIFY_ROTATION_ATTRIBUTE_TO_TIME,Float.toString(pPXSystem.mRotationMods.get(i).mToTime));
                    tagEnd(2,PXConstants.TAG_MODIFY_ROTATION);
                  }
                  for (int i=0; i<pPXSystem.mScaleMods.size(); i++){  
                    tagOut(2,PXConstants.TAG_MODIFY_SCALE);
                        attOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_FROM_SCALE_X,Float.toString(pPXSystem.mScaleMods.get(i).mFromScaleX));
                        attOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_TO_SCALE_X,Float.toString(pPXSystem.mScaleMods.get(i).mToScaleX));
                        attOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_FROM_SCALE_Y,Float.toString(pPXSystem.mScaleMods.get(i).mFromScaleY));
                        attOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_TO_SCALE_Y,Float.toString(pPXSystem.mScaleMods.get(i).mToScaleY));
                        attOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_FROM_TIME,Float.toString(pPXSystem.mScaleMods.get(i).mFromTime));
                        lastAttOut(3,PXConstants.TAG_MODIFY_SCALE_ATTRIBUTE_TO_TIME,Float.toString(pPXSystem.mScaleMods.get(i).mToTime));
                    tagEnd(2,PXConstants.TAG_MODIFY_SCALE);
                  }
                          
                 tagEnd(1,PXConstants.TAG_SYSTEM);
                tagEnd(0,PXConstants.TAG_PCONF);
                
                oStream.close();
	}
        
        private void tagOut(int level, String tag){
            String tabs = "";
            for (int i=0; i<level; i++) tabs += '\t';
            oStream.println(tabs+"<"+tag);
        }
        
        private void tagEnd(int level, String tag){
            String tabs = "";
            for (int i=0; i<level; i++) tabs += '\t';
            oStream.println(tabs+"</"+tag+">");
        }
        
        private void attOut(int level, String att, String value){
            String tabs = "";
            for (int i=0; i<level; i++) tabs += '\t';
            oStream.println(tabs+att+"=\""+value+"\"");
        }
        
        private void lastAttOut(int level, String att, String value){
            String tabs = "";
            for (int i=0; i<level; i++) tabs += '\t';
            oStream.println(tabs+att+"=\""+value+"\">");
        }

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

}
