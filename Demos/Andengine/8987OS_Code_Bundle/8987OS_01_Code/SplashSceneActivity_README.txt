In the splash screen class, we create a splash screen with the text "HELLO SPLASH SCREEN!" The splash screen will be shown for 4 seconds before moving to the menu scene, represented by the text "HELLO MENU SCREEN!"

The SplashSceneActivity class is similar to the PacktActivity class discussed in the first topic, except in this case we're using our resource manager to load a generic font which will be used for the scene identifier text objects on the onCreateResources method.

The next step we take is creating and setting up our initial scenes (both mMenuScene and mSplashScene). We're creating a text object for each scene, aligning it to the center in both x and y axis' and attaching them to their respected scenes. Once the scenes have been created and text applied, we let AndEngine know we're ready to move to the next process via the onCreateSceneFinished callback.

In the onPopulateScene portion of our activity, all we're doing here is registering a timer handler to the engine. When the timer handler reaches its duration, the onTimePassed method is called. In this case we're calling 'mEngine.setScene(mMenuScene)' which changes over to our menu from our splash screen.

Installation:
1. Add the SplashSceneActivity class to your project, including the resource manager with a font of your choice. 
2. Replace your AndroidManifest.xml's startup activity with ".SplashSceneActivity".