This class takes advantage of the brand new BatchedSpriteParticleSystem class, improving performance by up to %50. In this activity
we are applying a few particle modifiers which work in sequence to add depth effects, we override our particle's initialization method to add customized movement modifiers to each individual particle, and override the particle's update method to apply continuous color changes on a per particle basis. 

Installation:
1. Import the AdvancedParticleSystems class to your project and include a 64x64 (Colored, white) "particle.png" image to the "assets/gfx/" folder of your project.
2. Replace your AndroidManifest.xml's startup activity with ".AdvancedParticleSystems".