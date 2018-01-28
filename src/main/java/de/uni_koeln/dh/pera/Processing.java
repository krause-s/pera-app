package de.uni_koeln.dh.pera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO
public class Processing {

		private Logger logger = LoggerFactory.getLogger(getClass());
	
	public Processing(int keyCode, String input) {
		logger.info("INPUT (" + keyCode + "): " + input);
	}
	
}
