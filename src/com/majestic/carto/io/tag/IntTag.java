/**
 * 	Cartocraft -- IntTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class IntTag extends Tag {

	private int value;
	
	public IntTag(String name, int value) {
		super(name);
		setValue(value);
	}
	
	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_INT && tag.getValue().equals(getValue()))
			return true;
		return false;
	}

	public Type getType() {
		return Type.TAG_INT;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_INT.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_INT + ") " + getName() + ": " + value);
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
