1. In the WorkingWithParticles activity we're setting up a particle system which continuously emits "marbles" from the bottom center 
area of the screen, upward. We start by creating a texture of the image we'd like to use in onCreateResource().
2. Next, we create a particle emitter (for the particle system) and a particle system in the onPopulateScene() method. 
3. For added effect, the particle system is introduced 2 particle initializers and 1 particle modifier which will change the 
behaviour of each particle spawned by this particular system.
4. The particles spawn point is set through a call to the particleEmitter.setCenter(WIDTH/2, HEIGHT) 
5. The particle system can now be attached to the scene and emit particles from the

Installation:
1. Add the WorkingWithParticles class to your project, including a "marble.png" asset in the "gfx/" folder of the project. If this folder does not currently exist in the project's eclipse-generated assets folder, create it and add the two images. (the marble is 31x30). 
2. Replace your AndroidManifest.xml's startup activity with ".WorkingWithParticles".