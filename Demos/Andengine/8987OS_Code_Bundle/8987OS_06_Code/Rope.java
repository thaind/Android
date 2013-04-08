public class Rope extends Object {
	
public final int numRopeSegments;
public final float ropeSegmentsLength;
public final float ropeSegmentsWidth;
public final float ropeSegmentsOverlap;
public final Rectangle[] RopeSegments;
public final Body[] RopeSegmentsBodies;
public FixtureDef RopeSegmentFixtureDef;
	
	public Rope(Body pAttachTo, final int pNumRopeSegments, final float pRopeSegmentsLength, final float pRopeSegmentsWidth, final float pRopeSegmentsOverlap,
			final float pMinDensity, final float pMaxDensity, IEntity pScene, PhysicsWorld pPhysicsWorld, VertexBufferObjectManager pVertexBufferObjectManager) {
		numRopeSegments = pNumRopeSegments;
		ropeSegmentsLength = pRopeSegmentsLength;
		ropeSegmentsWidth = pRopeSegmentsWidth;
		ropeSegmentsOverlap = pRopeSegmentsOverlap;
		RopeSegments = new Rectangle[numRopeSegments];
		RopeSegmentsBodies = new Body[numRopeSegments];
		RopeSegmentFixtureDef = PhysicsFactory.createFixtureDef(pMaxDensity, 0.01f, 0.0f);
		for(int i = 0; i < numRopeSegments; i++)
		{
			final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			if(i<1)
				RopeSegments[i] = new Rectangle(pAttachTo.getWorldCenter().x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (pAttachTo.getWorldCenter().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT) - (ropeSegmentsLength / 2) + ropeSegmentsOverlap, ropeSegmentsWidth, ropeSegmentsLength, pVertexBufferObjectManager);
			else
				RopeSegments[i] = new Rectangle(RopeSegments[i-1].getX(), RopeSegments[i-1].getY() - RopeSegments[i-1].getHeight() + ropeSegmentsOverlap, ropeSegmentsWidth, ropeSegmentsLength, pVertexBufferObjectManager);
			RopeSegments[i].setColor(0.97f, 0.75f, 0.54f);
			if(i>0) RopeSegmentFixtureDef.density-=(pMaxDensity-pMinDensity)/numRopeSegments;
			RopeSegmentsBodies[i] = PhysicsFactory.createCircleBody(pPhysicsWorld, RopeSegments[i], BodyType.DynamicBody, RopeSegmentFixtureDef);
			RopeSegmentsBodies[i].setAngularDamping(4f);
			RopeSegmentsBodies[i].setLinearDamping(0.5f);
			RopeSegmentsBodies[i].setBullet(true);
			if(i<1)
				revoluteJointDef.initialize(pAttachTo, RopeSegmentsBodies[i], pAttachTo.getWorldCenter());
			else
				revoluteJointDef.initialize(RopeSegmentsBodies[i-1], RopeSegmentsBodies[i], 
						new Vector2(RopeSegmentsBodies[i-1].getWorldCenter().x,
								RopeSegmentsBodies[i-1].getWorldCenter().y - (ropeSegmentsLength/2)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
			PhysicsConnector RopeSegmentPhysConnector = new PhysicsConnector(RopeSegments[i], RopeSegmentsBodies[i]);
			pPhysicsWorld.registerPhysicsConnector(RopeSegmentPhysConnector);
			revoluteJointDef.collideConnected=false;
			pPhysicsWorld.createJoint(revoluteJointDef);
			pScene.attachChild(RopeSegments[i]);
		}
	}
	
}