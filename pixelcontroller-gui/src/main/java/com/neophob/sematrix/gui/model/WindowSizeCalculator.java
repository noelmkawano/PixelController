package com.neophob.sematrix.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * helper class to calculate the window size
 * @author michu
 *
 */
public class WindowSizeCalculator {

	private static final Logger LOG = Logger.getLogger(WindowSizeCalculator.class.getName());

	//defined by the gui
	public static final int MINIMAL_WINDOW_WIDTH = 820;
	public static final int MINIMAL_WINDOW_HEIGHT = 400;
	public static final int MINIMAL_VISUAL_HEIGHT = 40;


	private int singleVisualWidth;
	private int singleVisualHeight;

	private int windowHeight;
	private int windowWidth;

	//input values
	private int internalBufferWidth;
	private int internalBufferHeight;
	private int maxWindowHeight;
	private int nrOfScreens;

	/**
	 * 
	 * @param internalBufferWidth
	 * @param internalBufferHeight
	 * @param maximalWindowWidth
	 * @param maximalWindowHeigh
	 */
	public WindowSizeCalculator(int internalBufferWidth, int internalBufferHeight, int maximalWindowWidth, 
			int maximalWindowHeight, int nrOfScreens) {

		this.internalBufferWidth = internalBufferWidth;
		this.internalBufferHeight = internalBufferHeight; 
		this.nrOfScreens = nrOfScreens;

		if (maximalWindowWidth<MINIMAL_WINDOW_WIDTH) {
			windowWidth = MINIMAL_WINDOW_WIDTH;
			LOG.log(Level.WARNING, "Adjusted window width to minimal value {0}, configured value was {1}", new Object[] {MINIMAL_WINDOW_WIDTH, maximalWindowWidth});
		} else {
			windowWidth = maximalWindowWidth;			
		}

		if (maximalWindowHeight<MINIMAL_WINDOW_HEIGHT) {
			maxWindowHeight = MINIMAL_WINDOW_HEIGHT;
			LOG.log(Level.WARNING, "Adjusted window height to minimal value {0}, configured value was {1}", new Object[] {MINIMAL_WINDOW_HEIGHT, maximalWindowHeight});
		} else {
			maxWindowHeight = maximalWindowHeight;
		}		

		calculateWidth();
		calculateHeight();
	}

	/**
	 * calculate 1) window size and 2) single visual size
	 */
	private void calculateWidth() {
		//calculate optimal visual with
		singleVisualWidth = windowWidth/nrOfScreens;

		//apply factor to height
		float aspect = (float)singleVisualWidth/(float)internalBufferWidth;
		singleVisualHeight = (int)((float)internalBufferHeight*aspect+0.5f);
		
		while (singleVisualHeight>maxWindowHeight) {
			singleVisualHeight/=2;
			singleVisualWidth/=2;
		}
		
	}

	/**
	 * 
	 */
	private void calculateHeight() {
		//calculate optimal height
		//int oldSingleVisualHeight = singleVisualHeight;
		int newSingleVisualHeight = maxWindowHeight-MINIMAL_WINDOW_HEIGHT+MINIMAL_VISUAL_HEIGHT;
		
		//apply factor to width
		float aspect = (float)newSingleVisualHeight/(float)singleVisualHeight;
		int newSingleVisualWidth = (int)(singleVisualWidth*aspect+0.5f);
		
		//shrinking of the single visual is allowed
		if (singleVisualWidth > newSingleVisualWidth) {
			singleVisualWidth = newSingleVisualWidth;
			singleVisualHeight = newSingleVisualHeight;
			windowHeight = maxWindowHeight;
		} else {
			//else shrink window height
			LOG.log(Level.INFO, "Shrink window by "+(newSingleVisualHeight-singleVisualHeight));
			windowHeight = maxWindowHeight-(newSingleVisualHeight-singleVisualHeight);
		}
				
	}


	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public int getSingleVisualWidth() {
		return singleVisualWidth;
	}

	public int getSingleVisualHeight() {
		return singleVisualHeight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("WindowSizeCalculator [singleVisualWidth=%s, singleVisualHeight=%s, windowHeight=%s, windowWidth=%s]",
						singleVisualWidth, singleVisualHeight, windowHeight,
						windowWidth);
	}

}