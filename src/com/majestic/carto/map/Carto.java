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
 *  Cartocraft -- Carto.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.map;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.majestic.carto.io.MapWriter;

public class Carto {

	/**
	 * Command-line parameters
	 */
	private static final int ARGS = 3;
	private static final boolean OCCLUSION = true;
	private static final boolean LIGHTING = true;
	private static final String USAGE = "Usage: ./Cartocraft [Directory] [Height] [Output]";

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != ARGS) {
			System.err.println(USAGE);
			System.exit(1);
		} else {
			try {
				Map map = new Map(new File(args[0]), Integer.valueOf(args[1]), OCCLUSION, LIGHTING, Runtime.getRuntime().availableProcessors());
				long start = System.nanoTime();
				map.render();
				MapWriter.write(new File(args[2]), MapWriter.colorMap(map.getImage()));
				MapWriter.write(new File("height.png"), MapWriter.heightMap(map.getHeight(), map.getHeightMax()));
				MapWriter.write(new File("elevation.png"), MapWriter.elevationMap(map.getHeight(), map.getHeightMin(), map.getHeightMax()));
				double elapsed = (System.nanoTime() - start) / 1000000000.0;
				System.out.println("DONE. (" + new DecimalFormat("#.##").format(elapsed) + " sec)");
			} catch (NumberFormatException e) {
				System.err.println("Invalid input value: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("IO Exception: " + e.getMessage());
			} catch (InterruptedException e) {
				System.err.println("Thread Exception: " + e.getMessage());
			}
		}
	}
}
