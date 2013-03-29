/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXAlphaModifier {
    
    public float mFromAlpha;
    public float mToAlpha;
    public float mFromTime;
    public float mToTime;
    
    public PXAlphaModifier (final float pFromAlpha, final float pToAlpha, final float pFromTime, final float pToTime){
        mFromAlpha = pFromAlpha;
        mToAlpha = pToAlpha;
        mFromTime = pFromTime;
        mToTime = pToTime;
    }
}
