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
 *  Cartocraft -- RegionInputStream.java
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
