The CreditBox class makes use of various components of AndEngine which we've discussed in previous topics/chapters. The end-result is a 'pull-out', scrollable crediting system which allows a developer to add/remove names easily through this classes constructor. 

The class initializates the majority of its components through the use of two (initBackground() and initModifiers()) methods. Additional methods include 'show' and 'hide' methods used to pull-out and pull-in the credit box. 

For the auto-scrolling aspect of the credit names, the CreditBox overrides the onManagedUpdate() method of its entity in order to continuously check/set new positions for the text.

Installation:
1. Import the CreditBox class to your project.
2. Create a new CreditBox (CreditBox creditBox = new CreditBox(...)) and attach it to the scene (mScene.attachChild(creditBox)). Call creditBox.showCredits() to 'pull' the credit box into camera view.