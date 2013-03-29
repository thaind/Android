/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXEmitter {
    	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
    String mPEShape = "Point";
    float mPECenterX = 0.0f;
    float mPECenterY = 0.0f;
    float mPERadiusX = 0.0f;
    float mPERadiusY = 0.0f;
    float mPEWidth = 0.0f;
    float mPEHeight = 0.0f;
    
 	// ===========================================================
	// Constructors
	// ===========================================================
   public PXEmitter( String pPEShape, float pPECenterX, float pPECenterY, float pPERadiusX, float pPERadiusY, float pPEWidth, float pPEHeight){
       mPEShape = pPEShape;
       mPECenterX = pPECenterX;
       mPECenterY = pPECenterY;
       mPERadiusX = pPERadiusX;
       mPERadiusY = pPERadiusY;
       mPEWidth = pPEWidth;
       mPEHeight = pPEHeight;
   }
}
