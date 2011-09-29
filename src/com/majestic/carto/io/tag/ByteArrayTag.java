/**
 * 	Cartocraft -- ByteArrayTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class ByteArrayTag extends Tag {

	private byte[] value;
	
	public ByteArrayTag(String name, byte[] value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_BYTE_ARRAY) {
			byte[] tagValue = (byte[]) tag.getValue();
			if(tagValue.length != value.length)
				return false;
			for(int i = 0; i < tagValue.length; i++)
				if(tagValue[i] != value[i])
					return false;
		}
		return true;
	}
	
	public Type getType() {
		return Type.TAG_BYTE_ARRAY;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_BYTE_ARRAY.ordinal();
	}
	
	public String toString() {
		String formattedByteArray = "(" + Type.TAG_BYTE_ARRAY + ") " + getName() + " {";
		for(Byte b : value) {
			String formattedEle = Integer.toString(b & 0xFF, 16).toUpperCase();
			if(formattedEle.length() == 1)
				formattedEle = "0" + formattedEle;
			formattedByteArray += " " + formattedEle;
		}
		return formattedByteArray + " }";
	}
	
	public byte[] getValue() {
		return value;
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
}
