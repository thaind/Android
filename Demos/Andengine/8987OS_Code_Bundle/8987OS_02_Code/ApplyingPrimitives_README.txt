The ApplyingPrimitives class file contains only the code for onPopulateScene(). In this method, we're creating 3 float arrays which
will act as the buffer data. Within these arrays, we have different coordinates (x,y,z) which will plot points which will make up
different shaped meshes. Once we've plotted our points in the array, we create our Mesh object similar to how we would any other
AndEngine entity, while including the buffer, a draw mode and a vertex count as additional parameters.

Installation:
1. The onPopulateScene() method found in this class can be copied/pasted into any AndEngine activity.