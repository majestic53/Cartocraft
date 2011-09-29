/**
 * 	Cartocraft -- CompoundTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class CompoundTag extends Tag {

	private Tag[] value;
	
	public CompoundTag(String name, Tag[] value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_COMPOUND) {
			Tag[] tagValue = (Tag[]) tag.getValue();
			if(tagValue.length != value.length)
				return false;
			for(int i = 0; i < tagValue.length; i++)
				if(!tagValue[i].equal(value[i]))
					return false;
		}
		return true;
	}
	
	public Type getType() {
		return Type.TAG_COMPOUND;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_COMPOUND.ordinal();
	}
	
	public String toString() {
		String formattedCompound = "(" + Type.TAG_COMPOUND + ") " + getName() + " {";
		for(int i = 0; i < value.length; i++)
			formattedCompound += "\n" + value[i].toString();
		return formattedCompound;
	}
	
	public Tag[] getValue() {
		return value;
	}
	
	public void setValue(Tag[] value) {
		this.value = value;
	}
}