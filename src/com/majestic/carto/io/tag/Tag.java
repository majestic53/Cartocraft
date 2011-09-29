/**
 * 	Cartocraft -- Tag.java
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
