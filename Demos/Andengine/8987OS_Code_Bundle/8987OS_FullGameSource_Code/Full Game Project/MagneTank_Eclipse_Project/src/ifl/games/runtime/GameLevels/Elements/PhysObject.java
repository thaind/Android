package ifl.games.runtime.GameLevels.Elements;

import ifl.games.runtime.GameLevels.GameLevel;
import ifl.games.runtime.Managers.ResourceManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/** The PhysObject class is used in MagneTank to delegate contacts received
 *  from the PhysicsWorld’s ContactListener. It also facilitates a destroy()
 *  method to make destroying physics objects easier.
 *  
*** @author Brian Broyles - IFL Game Studio
**/
public abstract class PhysObject<T extends IShape> implements ContactListener, IUpdateHandler {
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public Body mBody;
	public T mEntity;
	public GameLevel mGameLevel;
	public PhysicsWorld mPhysicsWorld;
	public PhysicsConnector mPhysicsConnector;
	public boolean mIsDestroyed = false;
	
	private boolean mBeginContactAlreadyCalled = false;
	private boolean mEndContactAlreadyCalled = false;
	private boolean mPostSolveReadyToBeCalled = false;
	
	float mMaxImpulse;

	// ====================================================
	// ABSTRACT METHODS
	// ====================================================
	public abstract void onBeginContact(Contact pContact);
	public abstract void onEndContact(Contact pContact);
	public abstract void onPreSolve(Contact pContact, Manifold pOldManifold);
	public abstract void onPostSolve(final float pMaxImpulse);

	// ====================================================
	// METHODS
	// ====================================================
	public void set(Body pBody, T pEntity, PhysicsConnector pPhysicsConnector, GameLevel pGameLevel) {
		this.mBody = pBody;
		this.mEntity = pEntity;
		mPhysicsConnector = pPhysicsConnector;
		mGameLevel = pGameLevel;
		mPhysicsWorld = mGameLevel.mPhysicsWorld;
		this.mBody.setUserData(PhysObject.this);
		this.mEntity.registerUpdateHandler(this);
	}
	
	public void destroy() {
		if(!mIsDestroyed)
			ResourceManager.getActivity().runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					PhysObject.this.mPhysicsWorld.unregisterPhysicsConnector(PhysObject.this.mPhysicsConnector);
					mPhysicsWorld.destroyBody(PhysObject.this.mBody);
					PhysObject.this.mEntity.detachSelf();
					PhysObject.this.mEntity.dispose();
					PhysObject.this.mEntity = null;
					PhysObject.this.mBody = null;
				}});
		mIsDestroyed = true;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		this.mBeginContactAlreadyCalled = false;
		this.mEndContactAlreadyCalled = false;
		
		if(this.mPostSolveReadyToBeCalled) {
			this.onPostSolve(mMaxImpulse);
			mMaxImpulse = 0f;
			this.mPostSolveReadyToBeCalled = false;
		}
	}
	@Override public void reset() {}

	@Override
	public void beginContact(Contact contact) {
		if(!this.mBeginContactAlreadyCalled) {
			this.mBeginContactAlreadyCalled = true;
			this.onBeginContact(contact);
		}
	}

	@Override
	public void endContact(Contact contact) {
		if(!this.mEndContactAlreadyCalled) {
			this.mEndContactAlreadyCalled = true;
			this.onEndContact(contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		this.onPreSolve(contact, oldManifold);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		this.mPostSolveReadyToBeCalled = true;
		
		float CurrentMaxImpulse = impulse.getNormalImpulses()[0];
		for(int i = 1; i < impulse.getNormalImpulses().length; i++)
			CurrentMaxImpulse = Math.max(impulse.getNormalImpulses()[i], CurrentMaxImpulse);
		CurrentMaxImpulse /= this.mBody.getMass();
		
		mMaxImpulse += CurrentMaxImpulse;
	}
	
	public static ContactListener PHYS_OBJECT_CONTACT_LISTENER = new ContactListener() {
		@Override
		public void beginContact(Contact contact) {
			PhysObject<?> physicsObjectA = (PhysObject<?>) contact.getFixtureA().getBody().getUserData();
			PhysObject<?> physicsObjectB = (PhysObject<?>) contact.getFixtureB().getBody().getUserData();
			
			if(physicsObjectA != null){
				physicsObjectA.beginContact(contact);
			}
			if(physicsObjectB != null){
				physicsObjectB.beginContact(contact);
			}
		}
		@Override
		public void endContact(Contact contact) {
			PhysObject<?> physicsObjectA = (PhysObject<?>) contact.getFixtureA().getBody().getUserData();
			PhysObject<?> physicsObjectB = (PhysObject<?>) contact.getFixtureB().getBody().getUserData();
			
			if(physicsObjectA != null){
				physicsObjectA.endContact(contact);
			}
			if(physicsObjectB != null){
				physicsObjectB.endContact(contact);
			}
		}
		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			PhysObject<?> physicsObjectA = (PhysObject<?>) contact.getFixtureA().getBody().getUserData();
			PhysObject<?> physicsObjectB = (PhysObject<?>) contact.getFixtureB().getBody().getUserData();
			
			if(physicsObjectA != null){
				physicsObjectA.preSolve(contact, oldManifold);
			}
			if(physicsObjectB != null){
				physicsObjectB.preSolve(contact, oldManifold);
			}
		}
		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			PhysObject<?> physicsObjectA = (PhysObject<?>) contact.getFixtureA().getBody().getUserData();
			PhysObject<?> physicsObjectB = (PhysObject<?>) contact.getFixtureB().getBody().getUserData();
			
			if(physicsObjectA != null){
				physicsObjectA.postSolve(contact, impulse);
			}
			if(physicsObjectB != null){
				physicsObjectB.postSolve(contact, impulse);
			}
		}
	};
}