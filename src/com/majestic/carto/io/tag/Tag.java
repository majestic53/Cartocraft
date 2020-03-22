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
 *  Cartocraft -- Tag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public abstract class Tag {

	public static class TagType {

		/**
		 * Possible tag types
		 */
		public enum Type {
			TAG_END,
			TAG_BYTE,
			TAG_SHORT,
			TAG_INT,
			TAG_LONG,
			TAG_FLOAT,
			TAG_DOUBLE,
			TAG_BYTE_ARRAY,
			TAG_STRING,
			TAG_LIST,
			TAG_COMPOUND,
		}

		/**
		 * Returns a bytes tag type
		 * @param b byte
		 * @return Type
		 */
		public static Type byteToType(byte b) {
			return Type.values()[b];
		}

		/**
		 * Returns a tag type's byte
		 * @param type Type
		 * @return byte
		 */
		public static byte typeToByte(Type type) {
			return (byte) type.ordinal();
		}
	}

	private String name;

	/**
	 * Tag constructor
	 * @param name name
	 */
	public Tag(String name) {
		this.name = name;
	}

	/**
	 * Abstract equal method
	 * @param tag Tag
	 * @return boolean
	 */
	public abstract boolean equal(Tag tag);

	/**
	 * Returns a tag name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Abstract getType method
	 * @return Type
	 */
	public abstract Type getType();

	/**
	 * Abstract getTypeByte method
	 * @return byte
	 */
	public abstract byte getTypeByte();

	/**
	 * Abstract getValue method
	 * @return Object
	 */
	public abstract Object getValue();

	/**
	 * Abstract toString method
	 */
	public abstract String toString();
}
