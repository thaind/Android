package uk.co.ditchkitty.TileCollision;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObject;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObjectGroup;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TileActivity extends BaseGameActivity {

	private TMXTiledMap mTMXTiledMap;
	private BoundCamera mBoundChaseCamera;

	private static final int CAMERA_WIDTH = 480;
    private static final int CAMERA_HEIGHT = 320;
    private Scene mScene;
    
	private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
	private static final int PLAYER_VELOCITY = 2;
    
	private BitmapTextureAtlas mTexturePlayer;
	private Body mPlayerBody;
	private TiledTextureRegion mPlayerTextureRegion;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private PhysicsWorld mPhysicsWorld;

	private enum PlayerDirection{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	private PlayerDirection playerDirection = PlayerDirection.NONE;


	@Override
	public Engine onLoadEngine() {
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// Control texture
		this.mOnScreenControlTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

		// Player sprite texture
		this.mTexturePlayer = new BitmapTextureAtlas(128, 128, TextureOptions.DEFAULT);
		this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTexturePlayer, this, "hero.png", 0, 0, 3, 4);

		// Load the textures
		this.mEngine.getTextureManager().loadTextures(this.mTexturePlayer, this.mOnScreenControlTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		// Create physics world
		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		
		// Create the scene and register the physics world
		mScene = new Scene();
		mScene.registerUpdateHandler(this.mPhysicsWorld);

		// Load the TMX map
		try {
			final TMXLoader tmxLoader = new TMXLoader(this, this.mEngine.getTextureManager(), TextureOptions.NEAREST, null);
			this.mTMXTiledMap = tmxLoader.loadFromAsset(this, "test.tmx");
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}

		// Add the non-object layers to the scene
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++){
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			if (!layer.getTMXLayerProperties().containsTMXProperty("wall", "true"))
			mScene.attachChild(layer);
		}

		// Read in the unwalkable blocks from the object layer and create boxes for each
		this.createUnwalkableObjects(mTMXTiledMap);
		
		// Make the camera not exceed the bounds of the TMXEntity.
		final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		this.mBoundChaseCamera.setBounds(0, tmxLayer.getWidth(), 0, tmxLayer.getHeight());
		this.mBoundChaseCamera.setBoundsEnabled(true);
		// Add outer walls
		this.addBounds(tmxLayer.getWidth(), tmxLayer.getHeight());

		// Calculate the coordinates for the player, so it's centred on the camera.
		final int centerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getTileWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getTileHeight()) / 2;

		// Create the player sprite and add it to the scene.
		final AnimatedSprite player = new AnimatedSprite(centerX, centerY, this.mPlayerTextureRegion);
		this.mBoundChaseCamera.setChaseEntity(player);
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
		mPlayerBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, player, BodyType.DynamicBody, playerFixtureDef);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(player, mPlayerBody, true, false){
			@Override
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				mBoundChaseCamera.updateChaseEntity();
			}
		});
		mScene.attachChild(player);
		
		// Add the control
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				// Set the correct walking animation
				if (pValueY == 1){
					// Up
					if (playerDirection != PlayerDirection.UP){
						player.animate(ANIMATE_DURATION, 0, 2, true);
						playerDirection = PlayerDirection.UP;
					}
				}else if (pValueY == -1){
					// Down
					if (playerDirection != PlayerDirection.DOWN){
						player.animate(ANIMATE_DURATION, 9, 11, true);
						playerDirection = PlayerDirection.DOWN;
					}
				}else if (pValueX == -1){
					// Left
					if (playerDirection != PlayerDirection.LEFT){
						player.animate(ANIMATE_DURATION, 3, 5, true);
						playerDirection = PlayerDirection.LEFT;
					}
				}else if (pValueX == 1){
					// Right
					if (playerDirection != PlayerDirection.RIGHT){
						player.animate(ANIMATE_DURATION, 6, 8, true);
						playerDirection = PlayerDirection.RIGHT;
					}
				}else{
					if (player.isAnimationRunning()){
						player.stopAnimation();
						playerDirection = PlayerDirection.NONE;
					}
				}
				// Set the player's velocity
				mPlayerBody.setLinearVelocity(pValueX * PLAYER_VELOCITY, pValueY * PLAYER_VELOCITY);
			}
		});
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.25f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.25f);
		this.mDigitalOnScreenControl.getControlKnob().setAlpha(0.5f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();

		mScene.setChildScene(this.mDigitalOnScreenControl);
		
		return mScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}
	
	private void createUnwalkableObjects(TMXTiledMap map){
		// Loop through the object groups
		 for(final TMXObjectGroup group: this.mTMXTiledMap.getTMXObjectGroups()) {
			 if(group.getTMXObjectGroupProperties().containsTMXProperty("wall", "true")){
				 // This is our "wall" layer. Create the boxes from it
				 for(final TMXObject object : group.getTMXObjects()) {
					final Rectangle rect = new Rectangle(object.getX(), object.getY(),object.getWidth(), object.getHeight());
					final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
					PhysicsFactory.createBoxBody(this.mPhysicsWorld, rect, BodyType.StaticBody, boxFixtureDef);
					rect.setVisible(false);
					mScene.attachChild(rect);
				 }
			 }
		 }
	}
	
	private void addBounds(float width, float height){
		final Shape bottom = new Rectangle(0, height - 2, width, 2);
		bottom.setVisible(false);
		final Shape top = new Rectangle(0, 0, width, 2);
		top.setVisible(false);
		final Shape left = new Rectangle(0, 0, 2, height);
		left.setVisible(false);
		final Shape right = new Rectangle(width - 2, 0, 2, height);
		right.setVisible(false);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottom, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, top, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(bottom);
		this.mScene.attachChild(top);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);
	}


}
