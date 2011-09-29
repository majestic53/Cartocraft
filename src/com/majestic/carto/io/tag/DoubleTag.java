/**
 * 	Cartocraft -- DoubleTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class DoubleTag extends Tag {

	private double value;
	
	public DoubleTag(String name, double value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_DOUBLE && tag.getValue().equals(getValue()))
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_DOUBLE;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_DOUBLE.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_DOUBLE + ") " + getName() + ": " + value);
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
}