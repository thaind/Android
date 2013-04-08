	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		// Set the raw points for our rectangle mesh
		float baseBuffer[] = {
				// Triangle one
				-200, -100, 0, // point one
				200, -100, 0, // point two
				200, 100, 0, // point three
				
				// Triangle two
				200, 100, 0, // point one
				-200, 100, 0, // point two
				-200, -100, 0 // point three
		};

		// Create the base mesh at the bottom of the screen
		Mesh meshBase = new Mesh(400, 480 - 100, baseBuffer, 6, DrawMode.TRIANGLES, mEngine.getVertexBufferObjectManager());

		// Set the meshes color to a 'brown' color
		meshBase.setColor(0.45f, 0.164f, 0.3f);
		// Attach base mesh to the scene
		mScene.attachChild(meshBase);
		
		// Create the raw points for our triangle mesh
		float roofBuffer[] = {
				// Triangle
				-300, 0, 0, // point one
				0, -200, 0, // point two
				300, 0, 0, // point three
		};
		
		// Create the roof mesh above the base mesh
		Mesh meshRoof = new Mesh(400, 480 - 200, roofBuffer, 3, DrawMode.TRIANGLES, mEngine.getVertexBufferObjectManager());

		meshRoof.setColor(Color.RED);
		// Attach the roof to the scene
		mScene.attachChild(meshRoof);
		
		// Create the raw points for our line mesh
		float doorBuffer[] = {
				-25, -100, 0, // point one
				25, -100, 0, // point two
				25, 0, 0, // point three
				-25, 0, 0, // point four
				-25, -100, 0 // point five
		};
		
		// Create the door mesh
		Mesh meshDoor = new Mesh(400, 480, doorBuffer, 5, DrawMode.LINE_STRIP, mEngine.getVertexBufferObjectManager());

		meshDoor.setColor(Color.BLUE);
		// Attach the door to the scene
		mScene.attachChild(meshDoor);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}