/**
 * 	Cartocraft -- BlockColor.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.map;

import java.awt.Color;
import java.io.IOException;
import java.util.Properties;

public class BlockColor {
	
	private Properties props;
	
	/**
	 * BlockColor constructor
	 * @param path String
	 * @throws IOException
	 */
	public BlockColor(String path) throws IOException {
		props = new Properties();
		props.load(Carto.class.getResourceAsStream(path));
	}
	
	/**
	 * Returns a blocks color
	 * @param blockID byte
	 * @return Color
	 */
	public Color getBlockColor(byte blockID) {
		if(blockID < 0 || blockID >= props.size()) {
			System.err.println("WARNING: Encountered unknown block id " + blockID + ".");
			return new Color(Integer.valueOf(props.getProperty(Byte.toString((byte) 0)), 16).intValue());
		}
		return new Color(Integer.valueOf(props.getProperty(Byte.toString(blockID)), 16).intValue());
	}
	
	/**
	 * Returns a blocks RGB components
	 * @param blockID byte
	 * @return int[]
	 */
	public int[] getBlockColorComponent(byte blockID) {
		Color color = getBlockColor(blockID);
		int[] components = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
		return components;
	}
}
