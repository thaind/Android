/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXColorModifier {
    public float mFromRed;
    public float mToRed;
    public float mFromGreen;
    public float mToGreen;
    public float mFromBlue;
    public float mToBlue;
    public float mFromTime;
    public float mToTime;
    
    public PXColorModifier (final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final float pFromTime, final float pToTime){
        mFromRed = pFromRed;
        mToRed = pToRed;
        mFromGreen = pFromGreen;
        mToGreen = pToGreen;
        mFromBlue = pFromBlue;
        mToBlue = pToBlue;
        mFromTime = pFromTime;
        mToTime = pToTime;
    }
    
}
