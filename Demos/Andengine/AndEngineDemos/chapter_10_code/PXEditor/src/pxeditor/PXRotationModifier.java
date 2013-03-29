/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXRotationModifier {
    public float mFromRotation;
    public float mToRotation;
    public float mFromTime;
    public float mToTime;
    
    public PXRotationModifier (final float pFromRotation, final float pToRotation, final float pFromTime, final float pToTime){
        mFromRotation = pFromRotation;
        mToRotation = pToRotation;
        mFromTime = pFromTime;
        mToTime = pToTime;
    }
}
