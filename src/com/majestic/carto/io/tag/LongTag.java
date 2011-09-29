/**
 * 	Cartocraft -- LongTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class LongTag extends Tag {

	private long value;
	
	public LongTag(String name, long value) {
		super(name);
		setValue(value);
	}
	
	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_LONG && tag.getValue().equals(getValue()))
			return true;
		return false;
	}

	public Type getType() {
		return Type.TAG_LONG;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_LONG.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_LONG + ") " + getName() + ": " + value);
	}
	
	public Long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		this.value = value;
	}
}