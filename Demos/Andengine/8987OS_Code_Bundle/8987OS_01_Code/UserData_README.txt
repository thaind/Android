This class demonstrates just how easy it can be to store and retrieve game data.

1. We have 3 main types of methods for this class. The first is the init() method, which creates our settings file for our app if none currently exists. If our app does have a shared preferences file associated with it, we load the options.

2. Each setting we have available for saving/loading data will have a respective 'get' method which will be used in our various gameplay-related classes in order to aqcuire the setting's value. Assuming a user has turned off the sound, before playing our theme music we may check for UserData.getInstance().getIsSoundMuted(). Depending on the returning value, we would either continue to play the sound or skip playing the sound.

3. Additionally, each setting we have available will have a corresponding 'set' method, such as the unlockNextLevel() method. This method simply obtains or updates a current value, applies it to the shared preferences of our application via the preference editor, then calls its commit() method in order to save  any changes made. commit() MUST be called after making any changes to your settings, otherwise they will not be applied.

Installation:
This class can be copied into your project, importing required classes.