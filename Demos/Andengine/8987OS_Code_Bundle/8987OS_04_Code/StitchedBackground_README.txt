In this activity, we're applying a large-scaled, zoom-able, pan-able background by placing two sprites side-by-side on a relative layer then attaching that layer to the scene. 
The camera is bound to an area from coordinates (0,0) to (1600,480). This is exactly the dimensions of the two background images placed side-by-side.
The pinch-to-zoom functionality allows for zooming in/out of the camera up to defined minimum/maximum zoom factors. With all of these camera bounds and zoom restrictions in place, we successfully create a camera zone which can not be exceeded, but can be moved around within.

Installation:
1. Import the StitchedBackground class to your project.
2. Replace your AndroidManifest.xml's startup activity with ".StitchedBackground"
3. This activity requires two PNG files ("background_left.png" and "background_right.png") to be loaded into two separate texture regions. Both of these images should be 800x480 pixels in dimension.