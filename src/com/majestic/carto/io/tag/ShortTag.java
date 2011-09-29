/**
 * 	Cartocraft -- ShortTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class ShortTag extends Tag {

	private short value;
	
	public ShortTag(String name, short value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_SHORT && tag.getValue().equals(getValue()))
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_SHORT;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_SHORT.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_SHORT + ") " + getName() + ": " + value);
	}
	
	public Short getValue() {
		return value;
	}
	
	public void setValue(short value) {
		this.value = value;
	}
}
