/**
 * 	Cartocraft -- Carto.java
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