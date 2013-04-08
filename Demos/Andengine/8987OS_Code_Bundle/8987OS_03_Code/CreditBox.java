public class CreditBox extends Entity {

	// Pixel space between the text and edge of the background box
	private static final int WIDTH_PADDING = 15;
	private static final int HEIGHT_PADDING = 40;

	// Speed at which the credit names will scroll
	private static final int SCROLL_SPEED = 4;

	// Title at the top of the credit box
	private static final String TITLE = "~CREDITS~";

	// Height of the credit box
	private final float mHeight;

	private final Engine mEngine;

	private final Font mFont;

	// Credit box background
	private Rectangle mBackground;
	// private Sprite mBackground;

	// This string will keep keep a list of all the names to credit
	private String mCreditNames = "";

	// Text objects for title and credit names
	private Text mTitleText;
	private Text mCreditsText;

	// Boolean used to allow or disallow scrolling of the text
	private boolean mIsScrolling = false;

	// Maximum height (as an entity) of the credited names text object
	private float mTextHeight;

	// Modifiers used to show/hide the credit box
	private MoveXModifier moveInModifier;
	private MoveXModifier moveOutModifier;

	// Credit box constructor
	public CreditBox(float height, Font font, Engine engine) {
		this.mEngine = engine;
		this.mFont = font;
		this.mHeight = height;

		// Initialize the background (including the title text)
		this.initBackground();
		// Create/initialize the credit box's movement modifiers
		this.initModifiers();
	}

	// Init credit box's background
	private void initBackground() {
		// Create the background and attach it to 'this' entity
		mBackground = new Rectangle(0, 0, 0, mHeight,
				mEngine.getVertexBufferObjectManager());
		mBackground.setColor(Color.BLACK);
		this.attachChild(mBackground);

		// Create the text and attach it to the background created above
		mTitleText = new Text(0, 0, mFont, TITLE, TITLE.length(),
				mEngine.getVertexBufferObjectManager());
		mBackground.attachChild(mTitleText);

		// The credit box should initially be hidden
		this.setVisible(false);
	}

	// Populate the credits with names to be included in the credits
	public void populateCredits(final String... creditNames) {
		// Loop through the creditNames array, appending them to the
		// mCreditNames string
		for (int i = 0; i < creditNames.length; i++) {
			// Add a line-break after every name
			if (mCreditNames != null) {
				mCreditNames += '\n';
			}

			// Append the credit name to the final string
			mCreditNames += creditNames[i];
		}

		// Create the text object which will display credit names
		mCreditsText = new Text(WIDTH_PADDING, HEIGHT_PADDING, mFont,
				mCreditNames, mCreditNames.length(),
				mEngine.getVertexBufferObjectManager()) {
			// Scissor area values
			final int scissorWidth = 800;
			final int scissorHeight = (int) (mHeight + (HEIGHT_PADDING / 2));

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				// Enable scissoring
				pGLState.enableScissorTest();

				// Define scissor area for the credit text (the outside area
				// will not be drawn)
				GLES20.glScissor(0, HEIGHT_PADDING, scissorWidth, scissorHeight);
				super.preDraw(pGLState, pCamera);
			}

			@Override
			protected void postDraw(GLState pGLState, Camera pCamera) {
				// Disable states when we're finished with them!
				pGLState.disableScissorTest();
				super.postDraw(pGLState, pCamera);
			}
		};
		mBackground.attachChild(mCreditsText);

		// Restrict the background to that of our text (+ padding)
		mBackground.setWidth(mCreditsText.getWidth() + WIDTH_PADDING * 2);

		// We'll need the mTextHeight for use in the scrolling text
		// functionality
		mTextHeight = mCreditsText.getHeight();

		// Obtain and set the title text position/width values
		final float stringWidth = FontUtils.measureText(mFont, TITLE);
		final float titleX = (mBackground.getWidth() / 2) - (stringWidth / 2);
		final float titleY = 10;

		mTitleText.setPosition(titleX, titleY);
	}

	private void initModifiers() {
		// Create the 'move into view' modifier
		moveInModifier = new MoveXModifier(1, -mBackground.getWidth(), 0,
				new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// Set the credit names to their initial position (first
				// name at under the title)
				mCreditsText.setPosition(mCreditsText.getX(),
						HEIGHT_PADDING);
				// Allow the credit box to be drawn
				CreditBox.this.setVisible(true);
			}

			@Override
			public void onModifierFinished(
					IModifier<IEntity> pModifier, IEntity pItem) {
				// Once the modifier has finished, allow scrolling to
				// begin
				CreditBox.this.mIsScrolling = true;

			}
		}, EaseQuintIn.getInstance());

		// Create the 'move out of view' modifier
		moveOutModifier = new MoveXModifier(1, 0, -mBackground.getWidth(),
				new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// Disable text scrolling
				CreditBox.this.mIsScrolling = false;
			}

			@Override
			public void onModifierFinished(
					IModifier<IEntity> pModifier, IEntity pItem) {
				// Disallow the credit from being drawn
				CreditBox.this.setVisible(false);
			}
		}, EaseQuintIn.getInstance());

	}

	public void showCredits() {
		// Reset and reapply the moveInModifier
		moveInModifier.reset();
		this.registerEntityModifier(moveInModifier);

	}

	public void hideCredits() {
		// Reset and reapply the moveOutModifier
		moveOutModifier.reset();
		this.registerEntityModifier(moveOutModifier);

	}

	// Every time the credit box receives an update from the game engine
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// If scrolling is allowed
		if (mIsScrolling) {
			// set temporary x/y coordinates
			final float textX = mCreditsText.getX();
			final float textY = mCreditsText.getY();

			// If the credit name text object has scrolled outside of the credit
			// box
			if (textY > mHeight) {
				// Set the credit text to reappear at the top and continue to
				// scroll from top-down
				mCreditsText.setPosition(textX, -mTextHeight);
			} else {
				// Scroll the credit text
				mCreditsText.setPosition(textX, textY + SCROLL_SPEED);
			}
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
}