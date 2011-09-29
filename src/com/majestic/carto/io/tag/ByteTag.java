/**
 * 	Cartocraft -- ByteTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class ByteTag extends Tag {

	private byte value;
	
	public ByteTag(String name, byte value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_BYTE && tag.getValue().equals(getValue()))
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_BYTE;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_BYTE.ordinal();
	}
	
	public String toString() {
		String formattedByte = Integer.toString(value & 0xFF, 16).toUpperCase();
		if(formattedByte.length() == 1)
			formattedByte = "0" + formattedByte;
		return new String("(" + Type.TAG_BYTE + ") " + getName() + ": " + formattedByte);
	}
	
	public Byte getValue() {
		return value;
	}
	
	public void setValue(byte value) {
		this.value = value;
	}
}
