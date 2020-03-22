/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 *  Cartocraft -- BlockColor.java
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
