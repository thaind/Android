package ifl.games.runtime.GameLevels.Elements;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.shape.IShape;
import org.andengine.opengl.util.GLState;

/**
 * @author Jay Schroeder
 */
public class ParallaxLayer extends Entity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final ArrayList<ParallaxEntity> mParallaxEntities = new ArrayList<ParallaxEntity>();
    private int mParallaxEntityCount;

    protected float mParallaxValue;
    protected float mParallaxScrollValue;
   
    protected float mParallaxChangePerSecond;
   
    protected float mParallaxScrollFactor = 0.2f;
   
    private Camera mCamera;
   
    private float mCameraPreviousX;
    private float mCameraOffsetX;
   
    private float mLevelWidth = 0;
   
    private boolean mIsScrollable = false;

   
    // ===========================================================
    // Constructors
    // ===========================================================
    public ParallaxLayer() {
    }

    public ParallaxLayer(final Camera camera, final boolean mIsScrollable){
            this.mCamera = camera;
            this.mIsScrollable = mIsScrollable;
           
            mCameraPreviousX = camera.getCenterX();
    }
   
    public ParallaxLayer(final Camera camera, final boolean mIsScrollable, final float mLevelWidth){
            this.mCamera = camera;
            this.mIsScrollable = mIsScrollable;
            this.mLevelWidth = mLevelWidth;
           
            mCameraPreviousX = camera.getCenterX();
    }
   
    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setParallaxValue(final float pParallaxValue) {
            this.mParallaxValue = pParallaxValue;
    }
   
    public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
            this.mParallaxChangePerSecond = pParallaxChangePerSecond;
    }

    public void setParallaxScrollFactor(final float pParallaxScrollFactor){
            this.mParallaxScrollFactor = pParallaxScrollFactor;
    }
   
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onManagedDraw(GLState pGLState, Camera pCamera) {
            super.preDraw(pGLState, pCamera);

           
            final float parallaxValue = this.mParallaxValue;
            final float parallaxScrollValue = this.mParallaxScrollValue;
            final ArrayList<ParallaxEntity> parallaxEntities = this.mParallaxEntities;

            for(int i = 0; i < this.mParallaxEntityCount; i++) {
                    if(parallaxEntities.get(i).mIsScrollable){
                            parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxScrollValue, mLevelWidth);
                    } else {
                            parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxValue, mLevelWidth);
                    }

            }
    }
   
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
           
            if(mIsScrollable && mCameraPreviousX != this.mCamera.getCenterX()){
                            mCameraOffsetX = mCameraPreviousX - this.mCamera.getCenterX();
                            mCameraPreviousX = this.mCamera.getCenterX();
                           
                            this.mParallaxScrollValue += mCameraOffsetX * this.mParallaxScrollFactor;
                            mCameraOffsetX = 0;
            }
           
            this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
            super.onManagedUpdate(pSecondsElapsed);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void attachParallaxEntity(final ParallaxEntity parallaxEntity) {
            this.mParallaxEntities.add(parallaxEntity);
            this.mParallaxEntityCount++;
    }

    public boolean detachParallaxEntity(final ParallaxEntity pParallaxEntity) {
            this.mParallaxEntityCount--;
            final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
            if(!success) {
                    this.mParallaxEntityCount++;
            }
            return success;
    }
   
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class ParallaxEntity {
            // ===========================================================
            // Constants
            // ===========================================================

            // ===========================================================
            // Fields
            // ===========================================================

            final float mParallaxFactor;
            final IShape mAreaShape;
            final boolean mIsScrollable;

            final float shapeWidthScaled;

            // ===========================================================
            // Constructors
            // ===========================================================

            public ParallaxEntity(final float pParallaxFactor, final IShape pAreaShape) {
                    this.mParallaxFactor = pParallaxFactor;
                    this.mAreaShape = pAreaShape;
                    this.mIsScrollable = false;
                    shapeWidthScaled = this.mAreaShape.getWidth()*this.mAreaShape.getScaleX();
            }
           
            public ParallaxEntity(final float pParallaxFactor, final IShape pAreaShape, final boolean mIsScrollable) {
                    this.mParallaxFactor = pParallaxFactor;
                    this.mAreaShape = pAreaShape;
                    this.mIsScrollable = mIsScrollable;
                    shapeWidthScaled = this.mAreaShape.getWidth()*this.mAreaShape.getScaleX();
            }
           
            public ParallaxEntity(final float pParallaxFactor, final IShape pAreaShape, final boolean mIsScrollable, final float mReduceFrequency) {
                    this.mParallaxFactor = pParallaxFactor;
                    this.mAreaShape = pAreaShape;
                    this.mIsScrollable = mIsScrollable;
                    shapeWidthScaled = this.mAreaShape.getWidth()*this.mAreaShape.getScaleX() * mReduceFrequency;
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

            public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue, final float mLevelWidth) {
                    pGLState.pushModelViewGLMatrix();
                    {
                            float widthRange;
                           
                            if(mLevelWidth != 0){
                                    widthRange = mLevelWidth;
                            } else {
                                    widthRange = pCamera.getWidth();
                            }

                            float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeWidthScaled;

                            while(baseOffset > 0) {
                                    baseOffset -= shapeWidthScaled;
                            }
                            pGLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

                            float currentMaxX = baseOffset;
                           
                            do {
                                    this.mAreaShape.onDraw(pGLState, pCamera);
                                    pGLState.translateModelViewGLMatrixf(shapeWidthScaled - 1, 0, 0);
                                    currentMaxX += shapeWidthScaled;
                            } while(currentMaxX < widthRange);
                    }
                    pGLState.popModelViewGLMatrix();
            }

            // ===========================================================
            // Inner and Anonymous Classes
            // ===========================================================
    }


}