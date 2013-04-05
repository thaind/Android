package org.andengine.testscrollscene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.scrollscene.ScrollScene;
import org.andengine.entity.scene.scrollscene.ScrollScene.IOnScrollScenePageListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseBackOut;

import android.graphics.Typeface;
import android.widget.Toast;


public class TestScrollScene extends SimpleBaseGameActivity implements IOnScrollScenePageListener{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 480;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFace1TextureRegion;
	private ITextureRegion mFace2TextureRegion;
	private ITextureRegion mFace3TextureRegion;
	private ITiledTextureRegion mBulletTextureRegion;
	private Font mFont;
	private Camera mCamera;
	private Entity pageIndicator;
	private ScrollScene mScene;

	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		this.mFace1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png");
		this.mFace2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png");
		this.mFace3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png");
		this.mBulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "bullets.png", 1, 2);
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.YELLOW_ABGR_PACKED_INT);
		this.mFont.load();
		
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		//the width and height of the page should be usually the same size
		//as the camera. T
		this.mScene = new ScrollScene(CAMERA_WIDTH, CAMERA_HEIGHT);
		//the offset represents how much the layers overlap
		this.mScene.setOffset(0);
		
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* Calculate the coordinates for the face, so its centered on the camera. */
		final float centerX = (this.mScene.getPageWidth() - this.mFace1TextureRegion.getWidth()) / 2;
		final float centerY = (this.mScene.getPageHeight() - this.mFace1TextureRegion.getHeight()) / 2;

		/**
		 * This looks stupid, but the main reason why I'm doing this is because Entity doesn't have width/height in gles2 branch
		 * here I use a rectangle since it has width and height and it's also easy to work with
		 * but you can use any variation of RectangularShape i.e. Sprite
		 **/
		Rectangle page1 = new Rectangle(0, 0, 0, 0, this.getVertexBufferObjectManager());
		
		//add width and height so we can see the rectangle
		Rectangle page2 = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		page2.setColor(Color.RED);
		
		Rectangle page3 = new Rectangle(0, 0, 0, 0, this.getVertexBufferObjectManager());
		
		/* Create the button and add it to the scene. */
		final Sprite face = new ButtonSprite(centerX, centerY, this.mFace1TextureRegion, this.mFace2TextureRegion, this.mFace3TextureRegion, this.getVertexBufferObjectManager(), new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(TestScrollScene.this, "Clicked", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		//You can add whatever you want (sprites, buttonSprites, text, menus etc)
		final Sprite face2 = new Sprite(centerX, centerY, this.mFace3TextureRegion, this.getVertexBufferObjectManager());
		
		final Text text = new Text(centerX, centerY, this.mFont, "Hello !", this.getVertexBufferObjectManager());
		
		this.mScene.registerTouchArea(face);
		page1.attachChild(face);
		page2.attachChild(face2);
		page3.attachChild(text);
		
		this.mScene.registerTouchArea(face);
		
		this.mScene.addPage(page1);
		this.mScene.addPage(page2);
		this.mScene.addPage(page3);
		
		//TODO find better solution
		this.pageIndicator = new Entity();
		final float margin = 10;
		final float bulletWidth = mBulletTextureRegion.getWidth();
		final int pageNo = this.mScene.getPagesCount();
		float pageIndicatorWidth = 0;
		for(int i = 0; i < pageNo; i++){
			final TiledSprite bullet = new TiledSprite(i * (bulletWidth + margin), 0, mBulletTextureRegion, this.getVertexBufferObjectManager());
			pageIndicator.attachChild(bullet);
			pageIndicatorWidth += bulletWidth + margin;
		}
		final float pageIndicatorCenterX = (CAMERA_WIDTH - pageIndicatorWidth) * 0.5f;
		pageIndicator.setPosition(pageIndicatorCenterX, CAMERA_HEIGHT - 100);
		HUD hud = new HUD();
		hud.attachChild(pageIndicator);
		this.mCamera.setHUD(hud);
		
		TiledSprite first = (TiledSprite) pageIndicator.getChildByIndex(0);
		first.setCurrentTileIndex(1);
		
		//change ease function test
		this.mScene.setEaseFunction(EaseBackOut.getInstance());
		
		this.mScene.registerScrollScenePageListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		return this.mScene;
	}
	
	@Override
	public void onMoveToPageStarted(int pPageNumber) {
		//reset all tiles
		final int count = pageIndicator.getChildCount();
		for(int i = 0; i < count; i++){
			TiledSprite first = (TiledSprite) pageIndicator.getChildByIndex(i);
			first.setCurrentTileIndex(0);
		}
		//change the one we need
		TiledSprite first = (TiledSprite) pageIndicator.getChildByIndex(pPageNumber);
		first.setCurrentTileIndex(1);
	}

	@Override
	public void onMoveToPageFinished(int pPageNumber) {

	}
	// ===========================================================
	// Methods
	// ===========================================================


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
