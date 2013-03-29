/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXScaleModifier {
    public float mFromScaleX;
    public float mToScaleX;
    public float mFromScaleY;
    public float mToScaleY;
    public float mFromTime;
    public float mToTime;
    
    public PXScaleModifier (final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pFromTime, final float pToTime){
        mFromScaleX = pFromScaleX;
        mToScaleX = pToScaleX;
        mFromScaleY = pFromScaleY;
        mToScaleY = pToScaleY;
        mFromTime = pFromTime;
        mToTime = pToTime;
    }
}
