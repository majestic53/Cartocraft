/**
 * 	Cartocraft -- StringTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class StringTag extends Tag {

	private String value;
	
	public StringTag(String name, String value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_STRING && tag.getValue().equals(getValue()))
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_STRING;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_STRING.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_STRING + ") " + getName() + ": " + value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}