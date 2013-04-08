The WorkingWithSVG activity makes use of the AndEngineSVGTextureRegionExtension to allow for loading SVG-type texture regions. In the activity, we're creating three different SVG texture regions, each with their own sprite in order to display the differences between low, through to high resolution SVG scaling factors. The activity also includes an easy-to-implement method of automatically adjusting the SVG scaling factor depending on the dpi of the device running the application. This implementation can be found in the onCreateEngineOptions() method of the activity, while the creation of the SVG texture region's happens within the onCreateResources() method.

Installation:
1. Import the WorkingWithSVG class to your project.
2. Replace your AndroidManifest.xml's startup activity with ".WorkingWithSVG"
3. Include an SVG file called "svg_image.svg" in the "assets/gfx/" project folder.