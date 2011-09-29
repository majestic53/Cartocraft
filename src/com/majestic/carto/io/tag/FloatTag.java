/**
 * 	Cartocraft -- FloatTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class FloatTag extends Tag {

	private float value;
	
	public FloatTag(String name, float value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_FLOAT && tag.getValue().equals(getValue()))
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_FLOAT;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_FLOAT.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_FLOAT + ") " + getName() + ": " + value);
	}
	
	public Float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
}