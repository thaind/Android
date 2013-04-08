The HidingAndRetrievingLayers activity includes a setLayer() method which allows for easily swapping in/out the 'current' layer with the 'next' layer. The layers are brought in and out of the scene through the use of entity modifiers, triggered every 4 seconds, causing the scenes to continuously rotate in/out of the scene. The activity shows how we can use a single method in order to handle the swapping of layers with a minimal amount of code.

Installation:
1. Import the HidingAndRetrievingLayers class to your project project.
2. Replace your AndroidManifest.xml's startup activity with ".HidingAndRetrievingLayers".