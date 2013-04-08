public class ClientMessages {

	// Client message flags
	public static final short CLIENT_MESSAGE_ADD_POINT = 0;
	
	// We'll need to obtain the number of flags that the client has
	// as our server flags should increment off this value. We should not
	// mix client message flags with server message flags as this will lead to
	// ClassCastExceptions from the message pool
	public static final int	CLIENT_FLAG_COUNT = CLIENT_MESSAGE_ADD_POINT + 1;
	
	// Create a bew client message to be sent to the server when a client adds
	// a point to the screen via touch events
	public static class AddPointClientMessage extends ClientMessage{

		// Color flags
		public static final int COLOR_RED = 0;
		public static final int COLOR_GREEN = 1;
		public static final int COLOR_BLUE = 2;
		
		// Member variables to be read in from the server and sent to clients
		private int mID;
		private float mX;
		private float mY;
		private int mColorId;
		
		// Empty constructor needed for message pool allocation
		public AddPointClientMessage(){
			// Do nothing...
		}
		
		// Constructor
		public AddPointClientMessage(final int pID, final float pX, final float pY, final int pColorId){
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
			this.mColorId = pColorId;
		}
		
		// A Setter is needed to change values when we obtain a message from the message pool
		public void set(final int pID, final float pX, final float pY, final int pColorId){
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
			this.mColorId = pColorId;
		}
		
		// Getters
		public int getID(){
			return this.mID;
		}
		public float getX(){
			return this.mX;
		}
		public float getY(){
			return this.mY;
		}
		public int getColorId(){
			return this.mColorId;
		}
		
		// Get the message flag
		@Override
		public short getFlag() {
			return CLIENT_MESSAGE_ADD_POINT;
		}

		// Apply the read data to the message's member variables
		@Override
		protected void onReadTransmissionData(DataInputStream pDataInputStream)
				throws IOException {
			this.mID = pDataInputStream.readInt();
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream. readFloat();
			this.mColorId = pDataInputStream.readInt();
		}

		// Write the message's member variables to the output stream
		@Override
		protected void onWriteTransmissionData(
				DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeInt(this.mID);
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
			pDataOutputStream.writeInt(mColorId);
		}
	}
}