In the ApplyingSpriteGroups activity, we're loading a texture in the onCreateResources() method which will be used in the SpriteGroup.
When creating the sprite group, we pass it a texture atlas which should contain all of the necessary texture regions for the sprites
we plan to use (in this case, just 'marble' sprites). We then attach the sprite group to the scene. The following code includes setting up a loop
which will continuously create a new sprite and attach it to the texture region; position is based on tempX and tempY variables, which are adjusted
after each new sprite is added.

Installation:
1. Add the ApplyingSpriteGroups class to your project. Include a .png image (less than 30x30 pixels wide/high!) in the project's "assets/gfx/" folder.
If you've not already created the "gfx/" folder in the eclipse-generated assets folder of the project, do so before including the image.
2. Replace your AndroidManifest.xml's startup activity with ".ApplyingSpriteGroups".