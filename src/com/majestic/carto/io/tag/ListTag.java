/**
 * 	Cartocraft -- ListTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class ListTag extends Tag {

	private Tag[] value;
	
	public ListTag(String name, Tag[] value) {
		super(name);
		setValue(value);
	}

	public boolean equal(Tag tag) {
		if(tag.getName().equals(getName()) && tag.getType() == Type.TAG_LIST) {
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
		return Type.TAG_LIST;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_LIST.ordinal();
	}
	
	public String toString() {
		String formattedByteArray = "(" + Type.TAG_LIST + ") " + getName() + " {";
		for(Tag b : value)
			formattedByteArray += "\n" + b.toString();
		return formattedByteArray + "\n}";
	}
	
	public Tag[] getValue() {
		return value;
	}
	
	public void setValue(Tag[] value) {
		this.value = value;
	}
}
