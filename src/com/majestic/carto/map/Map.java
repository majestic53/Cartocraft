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
 *  Cartocraft -- Map.java
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.majestic.carto.io.ChunkInputStream;
import com.majestic.carto.io.RegionInputStream;
import com.majestic.carto.io.tag.Tag;

public class Map {

	public static class Region {

		private int[] region;
		private RegionInputStream regionStream;

		/**
		 * Region constructor
		 * @param file File
		 * @param regionX int
		 * @param regionZ int
		 * @throws IOException
		 */
		public Region(File file, int regionX, int regionZ) throws IOException {
			regionStream = new RegionInputStream(file);
			this.region = new int[]{regionX, regionZ};
		}

		/**
		 * Closes a region input stream
		 * @throws IOException
		 */
		public void close() throws IOException {
			regionStream.close();
		}

		/**
		 * Returns a ChunkInputStream at (x, z)
		 * @param x int
		 * @param z int
		 * @return ChunkInputStream
		 * @throws IOException
		 */
		public ChunkInputStream getChunk(int x, int z) throws IOException {
			return new ChunkInputStream(regionStream.getRegion(x, z));
		}

		/**
		 * Returns a regions x coordinate
		 * @return int
		 */
		public int getRegionX() {
			return region[0];
		}

		/**
		 * Returns a regions z coordinate
		 * @return int
		 */
		public int getRegionZ() {
			return region[1];
		}

		/**
		 * Returns a RegionInputStream
		 * @return RegionInputStream
		 */
		public RegionInputStream getRegionFile() {
			return regionStream;
		}
	}

	public static class RegionWorker implements Runnable {

		private int threadID;
		private boolean lighting;
		private boolean occlusion;

		/**
		 * RegionWorker constructor
		 * @param threadID int
		 * @param occlusion boolean
		 * @param lighting boolean
		 */
		public RegionWorker(int threadID, boolean occlusion, boolean lighting) {
			this.threadID = threadID;
			this.lighting = lighting;
			this.occlusion = occlusion;
		}

		/**
		 * Apply illumination to a pixel
		 * @param x int
		 * @param z int
		 * @param amount float
		 */
		private void applyIllumination(int x, int z, float amount) {
			image[x][z][0] += (int) ((image[x][z][0] * amount) % (255 - image[x][z][0]));
			image[x][z][1] += (int) ((image[x][z][1] * amount) % (255 - image[x][z][1]));
			image[x][z][2] += (int) ((image[x][z][2] * amount) % (255 - image[x][z][2]));
		}

		/**
		 * Apply occlusion to a pixel
		 * @param x int
		 * @param z int
		 * @param amount float
		 */
		private void applyOcclusion(int x, int z, float amount) {
			if(amount == 0)
				return;
			image[x][z][0] *= amount;
			image[x][z][1] *= amount;
			image[x][z][2] *= amount;
		}

		/**
		 * Returns the surface blocks
		 * @return byte[][]
		 */
		public byte[][] getSurfaceBlocks() {
			return block;
		}

		/**
		 * Returns true if a block is an emitter
		 * @param blockID byte
		 * @return boolean
		 */
		private boolean isEmitter(byte blockID) {
			if(EMITTER.containsKey(blockID))
				return true;
			return false;
		}

		/**
		 * Returns true if a block is to be excluded
		 * @param blockID byte
		 * @return boolean
		 */
		private boolean isExcluded(byte blockID) {
			if(EXCLUDE.containsKey(blockID))
				return true;
			return false;
		}

		/**
		 * Returns true if a block is not an air or water block
		 * @param blockID byte
		 * @return boolean
		 */
		private boolean isSurface(byte blockID) {
			if(blockID == 0 || blockID == 8 || blockID == 9 || blockID == 10 || blockID == 11)
				return false;
			return true;
		}

		/**
		 * Renders a series of region files
		 * @param regions ArrayList<File>
		 * @throws IOException
		 */
		private void render(ArrayList<File> regions) throws IOException {
			Region region;
			Pattern pat = Pattern.compile(REGION_REGEX);
			for(File f : regions) {
				Matcher mat = pat.matcher(f.getName());
				if(mat.find()) {
					region = new Region(f, Integer.valueOf(mat.group(2)), Integer.valueOf(mat.group(1)));
					renderRegion(region);
					region.close();
				}
			}
		}

		/**
		 * Renders a chunk file
		 * @param chunk ChunkInputStream
		 * @param offsetX int
		 * @param offsetZ int
		 * @param globOffsetX int
		 * @param globOffsetZ int
		 */
		private void renderChunk(ChunkInputStream chunk, int offsetX, int offsetZ) {
			float scale;
			byte blockID;
			int surfaceHeight;

			// read in chunk block data
			Tag blocksTag = chunk.getTag("Blocks");
			if(blocksTag == null)
				return;
			byte[] blocks = (byte[]) blocksTag.getValue();

			// find all surface block (skip over air/water)
			for(int x = 0; x < CHUNK_WIDTH; x++)
				for(int z = 0; z < CHUNK_WIDTH; z++) {
					for(int y = 0; y < rendHeight; y++) {
						blockID = blocks[y + (x * CHUNK_HEIGHT + z * CHUNK_AREA)];
						if(!isSurface(blockID) || isExcluded(blockID))
							continue;
						height[x + offsetX][z + offsetZ] = (byte) y;
					}
				}

			// render all surface block
			for(int x = 0; x < CHUNK_WIDTH; x++)
				for(int z = 0; z < CHUNK_WIDTH; z++) {
					surfaceHeight = height[x + offsetX][z + offsetZ];
					blockID = blocks[surfaceHeight + (x * CHUNK_HEIGHT + z * CHUNK_AREA)];

					// keep min/max surface heights
					if(surfaceHeight < heightMin)
						heightMin = (byte) surfaceHeight;
					else if(surfaceHeight > heightMax)
						heightMax = (byte) surfaceHeight;

					// check for water block above surface block if not excluded
					if((surfaceHeight + 1) < rendHeight
							&& (blocks[(surfaceHeight + 1) + (x * CHUNK_HEIGHT + z * CHUNK_AREA)] == 8
									|| blocks[(surfaceHeight + 1) + (x * CHUNK_HEIGHT + z * CHUNK_AREA)] == 9)
							&& (!isExcluded((byte) 8) || !isExcluded((byte) 9)))
						blockID = 8;

					// check for lava block above surface block if not excluded
					if((surfaceHeight + 1) < rendHeight
							&& (blocks[(surfaceHeight + 1) + (x * CHUNK_HEIGHT + z * CHUNK_AREA)] == 10
									|| blocks[(surfaceHeight + 1) + (x * CHUNK_HEIGHT + z * CHUNK_AREA)] == 11)
							&& (!isExcluded((byte) 10) || !isExcluded((byte) 11)))
						blockID = 10;

					// keep surface block type
					block[x + offsetX][z + offsetZ] = blockID;

					// run exposure function
					scale = (float) (1 - Math.exp(-((float) height[x + offsetX][z + offsetZ] / rendHeight) * EXPOSURE));
					image[x + offsetX][z + offsetZ][0] = (char) (color.getBlockColorComponent(blockID)[0] * scale);
					image[x + offsetX][z + offsetZ][1] = (char) (color.getBlockColorComponent(blockID)[1] * scale);
					image[x + offsetX][z + offsetZ][2] = (char) (color.getBlockColorComponent(blockID)[2] * scale);
				}
		}

		/**
		 * Renders a region file
		 * @param region Region
		 * @throws IOException
		 */
		private void renderRegion(Region region) throws IOException {
			if(region == null)
				return;

			ChunkInputStream chunk;
			System.out.println("[THREAD " + (threadID + 1) + "] Rendering REGION(" + region.getRegionZ() + "," + region.getRegionX() + ")");

			// find appropriate offset (x, z) for region
			int offsetX = centerX + REGION_DELTA * region.getRegionX();
			int offsetZ = centerZ + REGION_DELTA * region.getRegionZ();

			// render all chunks in region
			int globOffsetX = offsetX;
			int globOffsetZ = offsetZ;
			for(int x = 0; x < REGION_SIZE; x++) {
				for(int z = 0; z < REGION_SIZE; z++) {
					chunk = region.getChunk(x, z);
					if(chunk == null)
						continue;
					renderChunk(chunk, globOffsetX, globOffsetZ);
					globOffsetX += CHUNK_WIDTH;
				}
				globOffsetX = offsetX;
				globOffsetZ += CHUNK_WIDTH;
			}

			// render occlusion
			if(occlusion)
				renderRegionOcclusion(offsetX, offsetZ);

			// render illumination
			if(lighting)
				renderRegionIllumination(offsetX, offsetZ);
		}

		/**
		 * Render SSIL effect
		 * @param offsetX int
		 * @param offsetZ int
		 */
		private void renderRegionIllumination(int offsetX, int offsetZ) {
			float value;
			for(int x = 0; x < REGION_DELTA; x++)
				for(int z = 0; z < REGION_DELTA; z++) {

					// skip over all non-emitting blocks
					if(!isEmitter(block[x + offsetX][z + offsetZ]))
						continue;

					// iterate through different illumination radii for each pixel
					for(int r = 0; r < ILLUM_RADIUS.length; r++) {

						// retrieve height of surrounding pixels and apply approprate illumination
						for(int i = -ILLUM_RADIUS[r]; i <= ILLUM_RADIUS[r]; i++)
							for(int j = -ILLUM_RADIUS[r]; j <= ILLUM_RADIUS[r]; j++) {
								if((i == 0 && j == 0)
										|| (x + offsetX + i) < 0 || (x + offsetX + i) >= dimX
										|| (z + offsetZ + j) < 0 || (z + offsetZ + j) >= dimZ
										|| (height[x + offsetX + i][z + offsetZ + j] - height[x + offsetX][z + offsetZ] < 0))
									continue;
								value = (float) (EMISSION / EXPOSURE) / (i * i + j * j);
								applyIllumination(x + i + offsetX, z + j + offsetZ, value);
							}
					}
				}
		}

		/**
		 * Render SSAO effect
		 * @param offsetX int
		 * @param offsetZ int
		 */
		private void renderRegionOcclusion(int offsetX, int offsetZ) {
			int samples;
			float average, value;
			for(int x = 0; x < REGION_DELTA; x++)
				for(int z = 0; z < REGION_DELTA; z++) {

					// iterate through different sample radii for each pixel
					for(int r = 0; r < SAMPLE_RADIUS.length; r++) {
						average = 0;
						samples = 0;

						// retrieve height values of surrounding pixels
						for(int i = -SAMPLE_RADIUS[r]; i <= SAMPLE_RADIUS[r]; i++)
							for(int j = -SAMPLE_RADIUS[r]; j <= SAMPLE_RADIUS[r]; j++) {
								if((i == 0 && j == 0)
										|| (x + offsetX + i) < 0 || (x + offsetX + i) >= dimX
										|| (z + offsetZ + j) < 0 || (z + offsetZ + j) >= dimZ
										|| (height[x + offsetX + i][z + offsetZ + j] - height[x + offsetX][z + offsetZ] < 0))
									continue;
								average += height[x + offsetX + i][z + offsetZ + j];
								samples++;
							}

						// average samples
						if(samples == 0)
							samples = 1;
						average /= samples;
						value = (height[x + offsetX][z + offsetZ] - average);
						if(value >= 0)
							continue;

						// apply occlusion value to pixel
						applyOcclusion(x + offsetX, z + offsetZ, Math.abs(((CHUNK_HEIGHT - 1) + value) / (CHUNK_HEIGHT - 1)));
					}
				}
		}

		/**
		 * Run thread
		 */
		public void run() {
			try {
				render(allocedRegions.get(threadID));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Region/chunk size parameters
	 */
	private static int CHUNK_WIDTH;
	private static int CHUNK_HEIGHT;
	private static int CHUNK_AREA;
	private static int REGION_SIZE;
	private static int REGION_DELTA;

	/**
	 * SSAO/SSIL parameters
	 */
	public static int EXPOSURE;
	public static float EMISSION;
	public static int[] SAMPLE_RADIUS;
	public static int[] ILLUM_RADIUS;
	public static Hashtable<Byte, Byte> EXCLUDE;
	public static Hashtable<Byte, Byte> EMITTER;

	/**
	 * IO parameters
	 */
	private static String REGION_REGEX;
	private static String BLOCK_PATH = "Blocks.prop";
	private static String SETTINGS_PATH = "Settings.prop";

	public static String VERSION = "0.1.3";

	private int threadCount;
	private boolean lighting;
	private boolean occlusion;
	private Properties props;
	private static int dimX;
	private static int dimZ;
	private static int centerX;
	private static int centerZ;
	private static int rendHeight;
	private static byte heightMin;
	private static byte heightMax;
	private static byte[][] height;
	private static byte[][] block;
	private static char[][][] image;
	private static BlockColor color;
	private static ArrayList<ArrayList<File>> allocedRegions;

	/**
	 * Map constructor
	 * @param directory File
	 * @param rendHeight int
	 * @param occlusion boolean
	 * @param lighting boolean
	 * @param threads int
	 * @throws IOException
	 */
	public Map(File directory, int rendHeight, boolean occlusion, boolean lighting, int threads) throws IOException {
		if(!directory.isDirectory()) {
			System.err.println("Input directory does not exist.");
			System.exit(1);
		}

		// load settings from file
		EXCLUDE = new Hashtable<Byte, Byte>();
		EMITTER = new Hashtable<Byte, Byte>();
		loadSettings();

		// initialize
		int thread = 0;
		int minRegionX = 0;
		int maxRegionX = 0;
		int minRegionZ = 0;
		int maxRegionZ = 0;
		int regionX, regionZ;
		this.occlusion = occlusion;
		this.lighting = lighting;
		heightMin = (byte) (CHUNK_HEIGHT / 2);
		heightMax = (byte) (CHUNK_HEIGHT / 2);
		threadCount = threads;
		color = new BlockColor(BLOCK_PATH);
		ArrayList<File> regions = new ArrayList<File>();

		// print useful information
		System.out.println("CARTOCRAFT -- VER." + VERSION);
		System.out.println("----------");
		System.out.println("EXPOSURE: " + EXPOSURE);
		System.out.println("EMISSION: " + EMISSION);
		if(!EXCLUDE.isEmpty()) {
			System.out.print("EXCLUDING: ");
			for(Object k: EXCLUDE.keySet())
				System.out.print(k + " ");
			System.out.println();
		}

		// sanity check render height
		Map.rendHeight = rendHeight;
		if(rendHeight < 1 || rendHeight >= CHUNK_HEIGHT) {
			//System.err.println("Height out-of-range (set to default): " + (CHUNK_HEIGHT - 1));
			rendHeight = CHUNK_HEIGHT - 1;
		}

		System.out.println("THREADS initializing... (" + threadCount + ")");

		// retrieve all region file paths
		File[] files = directory.listFiles();
		Pattern pat = Pattern.compile(REGION_REGEX);
		for(int i = 0; i < files.length; i++) {
			Matcher mat = pat.matcher(files[i].getName());
			if(mat.find()) {
				regionX = Integer.valueOf(mat.group(2));
				regionZ = Integer.valueOf(mat.group(1));
				if(i == 0) {
					minRegionX = maxRegionX = regionX;
					minRegionZ = maxRegionZ = regionZ;
				} else {
					if(regionX < minRegionX)
						minRegionX = regionX;
					else if(regionX > maxRegionX)
						maxRegionX = regionX;
					if(regionZ < minRegionZ)
						minRegionZ = regionZ;
					else if(regionZ > maxRegionZ)
						maxRegionZ = regionZ;
				}
				regions.add(files[i]);
			}
		}

		// calculate image dimensions
		dimX = (maxRegionX - minRegionX) * REGION_DELTA + REGION_DELTA;
		dimZ = (maxRegionZ - minRegionZ) * REGION_DELTA + REGION_DELTA;

		// calculate the image center (relative to regions)
		centerX = dimX / 2 - (dimX / 2 - Math.abs(minRegionX) * REGION_DELTA);
		centerZ = dimZ / 2 - (dimZ / 2 - Math.abs(minRegionZ) * REGION_DELTA);

		// calculate memory requirements
		double memReq = (dimX / 1000000.0 * dimZ / 1000000.0 * Character.SIZE + 2 * dimX / 1000000.0 * dimZ / 1000000.0 * Byte.SIZE) * 1000.0;

		System.out.println(regions.size() + " REGIONS found... (" + dimX + "," + dimZ + ")");
		System.out.println(new DecimalFormat("#.##").format(memReq) + " GB of memory required");
		System.out.println("----------");

		// fail if not enough memory
		if(memReq > (Runtime.getRuntime().maxMemory() / 1000000000.0)) {
			System.err.println("Not enough heap space to complete this operation.");
			System.exit(1);
		}

		// initialize arrays
		image = new char[dimX][dimZ][3];
		height = new byte[dimX][dimZ];
		block = new byte[dimX][dimZ];

		// allocate regions to threads using round robit
		allocedRegions = new ArrayList<ArrayList<File>>();
		for(int i = 0; i < threads; i++)
			allocedRegions.add(new ArrayList<File>());
		for(File r : regions) {
			allocedRegions.get(thread).add(r);
			thread++;
			if(thread == allocedRegions.size())
				thread = 0;
		}
	}

	/**
	 * Returns a height map
	 * @return int[][]
	 */
	public byte[][] getHeight() {
		return height;
	}

	/**
	 * Returns a maps maximum height
	 * @return int
	 */
	public byte getHeightMax() {
		return heightMax;
	}

	/**
	 * Returns a maps minimum height
	 * @return int
	 */
	public byte getHeightMin() {
		return heightMin;
	}

	/**
	 * Returns an image
	 * @return char[][][]
	 */
	public char[][][] getImage() {
		return image;
	}

	/**
	 * Loads settings from a settings file
	 * @throws IOException
	 */
	private void loadSettings() throws IOException {
		try {
			props = new Properties();
			props.load(Carto.class.getResourceAsStream(SETTINGS_PATH));
			CHUNK_WIDTH = Integer.valueOf(props.getProperty("chunk_width"));
			CHUNK_HEIGHT = Integer.valueOf(props.getProperty("chunk_height"));
			CHUNK_AREA = Integer.valueOf(props.getProperty("chunk_area"));
			REGION_SIZE = Integer.valueOf(props.getProperty("region_size"));
			REGION_DELTA = Integer.valueOf(props.getProperty("region_delta"));
			EXPOSURE = Integer.valueOf(props.getProperty("exposure"));
			String[] ex_str = props.getProperty("exclude").split(",");;
			for(int i = 0; i < ex_str.length; i++)
				EXCLUDE.put(Byte.valueOf(ex_str[i]), (byte) 0);
			EMISSION = Float.valueOf(props.getProperty("emission"));
			String[] e_str = props.getProperty("emitter").split(",");
			for(int i = 0; i < e_str.length; i++)
				EMITTER.put(Byte.valueOf(e_str[i]), (byte) 0);
			String[] s_str = props.getProperty("sample_radii").split(",");
			int[] s_radii = new int[s_str.length];
			for(int i = 0; i < s_str.length; i++)
				s_radii[i] = Integer.valueOf(s_str[i]);
			SAMPLE_RADIUS = s_radii;
			String[] i_str = props.getProperty("illum_radii").split(",");
			int[] i_radii = new int[i_str.length];
			for(int i = 0; i < i_str.length; i++)
				i_radii[i] = Integer.valueOf(i_str[i]);
			ILLUM_RADIUS = i_radii;
			REGION_REGEX = props.getProperty("region_regex");
		} catch(NumberFormatException e) {
			System.err.println("Invalid setting: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Renders a series of region files
	 * @throws InterruptedException
	 */
	public void render() throws InterruptedException {
		Thread[] threads = new Thread[threadCount];
		for(int i = 0; i < this.threadCount; i++) {
			threads[i] = new Thread(new RegionWorker(i, occlusion, lighting));
			threads[i].start();
		}
		for(int i = 0; i < this.threadCount; i++)
			threads[i].join();
	}
}
