1. The ObjectFactory class need not be instantiated as its sole purpose is to provide creation methods for objects.

2. Each of the methods are in place in order to return a different subtype of the base object you are attempting to instantiate.

3. In this code, we can assume LargeObject and SmallObject both extend the BaseObject class.

Usage:
BaseObject baseObject;
baseObject = ObjectFactory.createLargeObject(5, 5);

With these two lines of code, we will successfully instantiate baseObject to subtype LargeObject.

Installation:
Copy the code into your project and import the required classes. 