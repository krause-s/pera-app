package de.uni_koeln.dh.pera;

import de.uni_koeln.dh.pera.gui.View;

// TODO Javadoc
public class Application {
	
	public static void main(String[] args) {		
		View view = new View("pera-app");	// TODO title
		view.init();
		
		if (view.isInitialized()) {
			view.loadComponents();			
			view.show();
		}
		
		view.dispose();
	}

}
