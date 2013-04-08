public class LevelSelector extends Entity {

	/* Level selector layer properties */
	private final int COLUMNS = 6;
	private final int ROWS = 6;

	/* Level selector tile properties */
	private final int TILE_DIMENSION = 50;
	private final int TILE_PADDING = 15;

	private final Scene mScene;
	private final Engine mEngine;

	/*
	 * The mChapter variable can allow each LevelSelector object to contain
	 * level tiles which begin levels in different chapters.
	 */
	private final int mChapter;

	/* Variable containing the current max level unlocked */
	private final int mMaxLevel;

	/* Camera width and height are needed for the layout */
	private final int mCameraWidth;
	private final int mCameraHeight;

	/* Initial x/y coordinates used for tile positioning */
	private final float mInitialX;
	private final float mInitialY;

	/*
	 * Variable which defines whether the LevelSelector is hidden or visible
	 */
	private boolean mHidden = true;

	/**
	 * The LevelSelector object can be used to display a grid of level tiles for
	 * user selection.
	 * 
	 * @param pMaxLevel
	 *            Current max unlocked level.
	 * @param pChapter
	 *            Chapter/world number of this particular LevelSelector.
	 * @param pCameraWidth
	 *            Camera object's width value.
	 * @param pCameraHeight
	 *            Camera object's height value.
	 * @param pScene
	 *            The Scene in which the LevelSelector will be displayed on.
	 * @param pEngine
	 *            AndEngine's mEngine object.
	 */
	public LevelSelector(final int pMaxLevel, final int pChapter,
			final int pCameraWidth, final int pCameraHeight,
			final Scene pScene, final Engine pEngine) {
		/* Initialize member variables */
		this.mScene = pScene;
		this.mEngine = pEngine;
		this.mChapter = pChapter;
		this.mMaxLevel = pMaxLevel;
		this.mCameraWidth = pCameraWidth;
		this.mCameraHeight = pCameraHeight;

		/*
		 * Obtain the initial tile's X coordinate by subtracting half of the
		 * entire level selector width including all tiles and padding from the
		 * center of the Scene
		 */
		final float halfLevelSelectorWidth = ((TILE_DIMENSION * COLUMNS) + TILE_PADDING
				* (COLUMNS - 1)) * 0.5f;
		this.mInitialX = (this.mCameraWidth * 0.5f) - halfLevelSelectorWidth;

		/* Same math as above applies to the Y coordinate */
		final float halfLevelSelectorHeight = ((TILE_DIMENSION * ROWS) + TILE_PADDING
				* (ROWS - 1)) * 0.5f;
		this.mInitialY = (this.mCameraHeight * 0.5f) + halfLevelSelectorHeight;
	}

	/**
	 * Create the level tiles with a customized ITextureRegion representation as
	 * well as a customized Font.
	 * 
	 * @param pTextureRegion
	 *            The ITextureRegion to supply each of the level tiles.
	 * @param pFont
	 *            The Font to be displayed by Text written on the tiles,
	 *            specifying tile level number for example.
	 */
	public void createTiles(final ITextureRegion pTextureRegion,
			final Font pFont) {

		/* Temp coordinates for placing level tiles */
		float tempX = this.mInitialX + TILE_DIMENSION * 0.5f;
		float tempY = this.mInitialY - TILE_DIMENSION * 0.5f;

		/* Current level of the tile to be placed */
		int currentTileLevel = 1;

		/*
		 * Loop through the Rows, adjusting tempY coordinate after each
		 * iteration
		 */
		for (int i = 0; i < ROWS; i++) {

			/*
			 * Loop through the column positions, placing a LevelTile in each
			 * column
			 */
			for (int o = 0; o < COLUMNS; o++) {

				final boolean locked;

				/* Determine whether the current tile is locked or not */
				if (currentTileLevel <= mMaxLevel) {
					locked = false;
				} else {
					locked = true;
				}

				/* Create a level tile */
				LevelTile levelTile = new LevelTile(tempX, tempY, locked,
						currentTileLevel, pTextureRegion, pFont);

				/*
				 * Attach the level tile's text based on the locked and
				 * currentTileLevel variables pass to its constructor
				 */
				levelTile.attachText();

				/* Register & Attach the levelTile object to the LevelSelector */
				mScene.registerTouchArea(levelTile);
				this.attachChild(levelTile);

				/* Increment the tempX coordinate to the next column */
				tempX = tempX + TILE_DIMENSION + TILE_PADDING;

				/* Increment the level tile count */
				currentTileLevel++;
			}

			/* Reposition the tempX coordinate back to the first row (far left) */
			tempX = mInitialX + TILE_DIMENSION * 0.5f;

			/* Reposition the tempY coordinate for the next row to apply tiles */
			tempY = tempY - TILE_DIMENSION - TILE_PADDING;
		}
	}

	/**
	 * Display the LevelSelector on the Scene.
	 */
	public void show() {

		/* Register as non-hidden, allowing touch events */
		mHidden = false;

		/* Attach the LevelSelector the the Scene if it currently has no parent */
		if (!this.hasParent()) {
			mScene.attachChild(this);
		}

		/* Set the LevelSelector to visible */
		this.setVisible(true);
	}

	/**
	 * Hide the LevelSelector on the Scene.
	 */
	public void hide() {

		/* Register as hidden, disallowing touch events */
		mHidden = true;

		/* Remove the LevelSelector from view */
		this.setVisible(false);
	}

	public class LevelTile extends Sprite {

		/*
		 * The LevelTile should keep track of level number and lock status. Feel
		 * free to add additional data within level tiles
		 */
		private final boolean mIsLocked;
		private final int mLevelNumber;
		private final Font mFont;
		private Text mTileText;

		/*
		 * Each level tile will be sized according to the constant
		 * TILE_DIMENSION within the LevelSelector class
		 */
		public LevelTile(float pX, float pY, boolean pIsLocked,
				int pLevelNumber, ITextureRegion pTextureRegion, Font pFont) {
			super(pX, pY, LevelSelector.this.TILE_DIMENSION,
					LevelSelector.this.TILE_DIMENSION, pTextureRegion,
					LevelSelector.this.mEngine.getVertexBufferObjectManager());

			/* Initialize the necessary variables for the LevelTile */
			this.mFont = pFont;
			this.mIsLocked = pIsLocked;
			this.mLevelNumber = pLevelNumber;
		}

		/* Method used to obtain whether or not this level tile represents a
		 * level which is currently locked */
		public boolean isLocked() {
			return this.mIsLocked;
		}

		/* Method used to obtain this specific level tiles level number */
		public int getLevelNumber() {
			return this.mLevelNumber;
		}

		/*
		 * Attach the LevelTile's text to itself based on whether it's locked or
		 * not. If not, then the level number will be displayed on the level
		 * tile.
		 */
		public void attachText() {

			String tileTextString = null;

			/* If the tile's text is currently null... */
			if (this.mTileText == null) {

				/*
				 * Determine the tile's string based on whether it's locked or
				 * not
				 */
				if (this.mIsLocked) {
					tileTextString = "Locked";
				} else {
					tileTextString = String.valueOf(this.mLevelNumber);
				}

				/* Setup the text position to be placed in the center of the tile */
				final float textPositionX = LevelSelector.this.TILE_DIMENSION * 0.5f;
				final float textPositionY = textPositionX;
				
				/* Create the tile's text in the center of the tile */
				this.mTileText = new Text( textPositionX,
						textPositionY, this.mFont,
						tileTextString, tileTextString.length(),
						LevelSelector.this.mEngine
								.getVertexBufferObjectManager());

				/* Attach the Text to the LevelTile */
				this.attachChild(mTileText);
			}
		}

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {

			/* If the LevelSelector is not hidden, proceed to execute the touch
			 * event */
			if (!LevelSelector.this.mHidden) {

				/* If a level tile is initially pressed down on */
				if (pSceneTouchEvent.isActionDown()) {
					/* If this level tile is locked... */
					if (this.mIsLocked) {
						/* Tile Locked event... */
						LevelSelector.this.mScene.getBackground().setColor(
								org.andengine.util.adt.color.Color.RED);
					} else {
						/* Tile unlocked event... This event would likely prompt
						 * level loading but without getting too complicated we
						 * will simply set the Scene's background color to green */
						LevelSelector.this.mScene.getBackground().setColor(
								org.andengine.util.adt.color.Color.GREEN);
						
						/**
						 * Example level loading:
						 * 		LevelSelector.this.hide();
						 * 		SceneManager.loadLevel(this.mLevelNumber);
						 */
					}
					return true;
				}
			}
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}
	}
}