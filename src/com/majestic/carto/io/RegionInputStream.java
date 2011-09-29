/**
 * 	Cartocraft -- RegionInputStream.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class RegionInputStream {

	/**
	 * Region file meta offsets
	 */
	private static final int REGION_SIZE = 32;
	private static final int SECTOR_SIZE = 4096;
	private static final int OFFSET_COUNT = SECTOR_SIZE / 4;
	
	/**
	 * Possible chunk compression types
	 */
	private static final int GZIP_TYPE = 1;
	private static final int DEF_TYPE = 2;
	
	private int[] offsets;
	private RandomAccessFile file;
	
	/**
	 * RegionInputStream constructor
	 * @param path File
	 * @throws IOException
	 */
	public RegionInputStream(File path) throws IOException {
		offsets = new int[OFFSET_COUNT];
		file = new RandomAccessFile(path, "r");
		for(int i = 0; i < OFFSET_COUNT; i++) {
			offsets[i] = file.readInt();
		}
	}
	
	/**
	 * Close input stream
	 * @throws IOException
	 */
	public void close() throws IOException {
		file.close();
	}
	
	/**
	 * Returns a stream to a chunk file at a given (x, z)
	 * @param x int
	 * @param z int
	 * @return DataInputStream
	 * @throws IOException
	 */
	public DataInputStream getRegion(int x, int z) throws IOException {
		file.seek(0);
		int offset = offsets[x + z * REGION_SIZE];
		if(offset == 0)
			return null;
		file.seek((offset >> 8) * SECTOR_SIZE);
		int length = file.readInt();
		byte type = file.readByte();
		byte[] nbtData = new byte[length - 1];
		file.read(nbtData);
		if(type == GZIP_TYPE)
			return new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(nbtData)));
		else if(type == DEF_TYPE)
			return new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(nbtData)));
		return null;
	}
}
