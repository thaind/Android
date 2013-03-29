/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pxeditor;

/**
 *
 * @author rick
 */
public class PXExpireModifier {
    public float mMinLifeTime;
    public float mMaxLifeTime;
     
    public PXExpireModifier (final float pMinLifeTime, final float pMaxLifeTime){
        mMinLifeTime = pMinLifeTime;
        mMaxLifeTime = pMaxLifeTime;
    }
    
}
