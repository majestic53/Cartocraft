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
 *  Cartocraft -- MapWriter.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapWriter {

	/**
	 * Returns an image
	 * @param image char[][][]
	 * @return BufferedImage
	 */
	public static BufferedImage colorMap(char[][][] image) {
		int color = 0;
		int dimX = image.length;
		int dimZ = image[0].length;
		BufferedImage buffImage = new BufferedImage(dimX, dimZ, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < dimX; x++)
			for(int z = 0; z < dimZ; z++) {
				color = (image[x][z][0] << 16) | (image[x][z][1] << 8) | image[x][z][2];

				// flip x to correct orientation
				buffImage.setRGB((dimX - 1) - x, z, color);
			}
		return buffImage;
	}

	/**
	 * Returns a rainbow scale image
	 * @param height int[][]
	 * @param heightMin int
	 * @param heightMax int
	 * @return BufferedImage
	 */
	public static BufferedImage elevationMap(byte[][] height, byte heightMin, byte heightMax) {
		float hue;
		int dimX = height.length;
		int dimZ = height[0].length;
		BufferedImage buffImage = new BufferedImage(dimX, dimZ, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < dimX; x++)
			for(int z = 0; z < dimZ; z++) {
				if(height[x][z] == 0)
					continue;

				// check for sea-level
				if(height[x][z] < 64)
					hue = 240 - 180 * ((float) (height[x][z] - heightMin) / (heightMax - heightMin));
				else
					hue = 120 - 120 * ((float) (height[x][z] - heightMin) / (heightMax - heightMin));

				// flip x to correct orientation
				buffImage.setRGB((dimX - 1) - x, z, hueToRGB(hue));
			}
		return buffImage;
	}

	/**
	 * Returns a height map
	 * @param height int[][]
	 * @param heightMax int
	 * @return BufferedImage
	 */
	public static BufferedImage heightMap(byte[][] height, byte heightMax) {
		int grey = 0;
		int dimX = height.length;
		int dimZ = height[0].length;
		BufferedImage buffImage = new BufferedImage(dimX, dimZ, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < dimX; x++)
			for(int z = 0; z < dimZ; z++) {
				grey = (int) (255 * ((float) height[x][z] / heightMax));

				// flip x to correct orientation
				buffImage.setRGB((dimX - 1) - x, z, ((grey << 16) | (grey << 8) | grey));
			}
		return buffImage;
	}

	/**
	 * Hue to RGB conversion
	 * @param hue float
	 * @return int
	 */
	private static int hueToRGB(float hue) {
		int red = 0, green = 0, blue = 0;
		hue /= 60;
		float temp = 1 - Math.abs(hue % 2 - 1);
		if(hue >= 0 && hue < 1) {
			red = 255;
			green = (int) (255 * temp);
			blue = 0;
		} else if(hue >= 1 && hue < 2) {
			red = (int) (255 * temp);
			green = 255;
			blue = 0;
		} else if(hue >= 2 && hue < 3) {
			red = 0;
			green = 255;
			blue = (int) (255 * temp);
		} else if(hue >= 3 && hue < 4) {
			red = 0;
			green = (int) (255 * temp);
			blue = 255;
		} else if(hue >= 4 && hue < 5) {
			red = (int) (255 * temp);
			green = 0;
			blue = 255;
		} else if(hue >= 5 && hue < 6) {
			red = 255;
			green = 0;
			blue = (int) (255 * temp);
		}
		return new Integer((red << 16) | (green << 8) | blue);
	}

	/**
	 * Writes rendered BufferedImage to file
	 * @param path File
	 * @param image BufferedImage
	 * @throws IOException
	 */
	public static void write(File path, BufferedImage image) throws IOException {
		System.out.println("Writing to file... (" + path.getName() + ")");
		ImageIO.write(image, "png", path);
	}
}
