This class is an AndEngine activity with contains four rectangles. In the onPopulateScene() method we construct each
rectangle in order from one to four, applying different types of entity modifiers to the rectangles. The modifiers included
in this class are LoopEntityModifiers, MoveModifiers, SequenceEntityModifiers, ParallelEntityModifiers, RotationModifiers,
and QuadraticBezierCurveMoveModifiers. After each of the rectangles have had their modifiers initialized, they call the
registerEntityModifier() method in order to start the animations.

Installation:
1. Import the ApplyingEntityModifiers class to your project
2. Replace your AndroidManifest.xml's startup activity with ".ApplyingEntityModifiers".