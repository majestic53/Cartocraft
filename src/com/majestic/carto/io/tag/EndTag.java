/**
 * 	Cartocraft -- EndTag.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly 
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io.tag;

import com.majestic.carto.io.tag.Tag.TagType.Type;

public class EndTag extends Tag {

	public EndTag() {
		super(null);
	}
	
	public boolean equal(Tag tag) {
		if(tag.getType() == Type.TAG_END)
			return true;
		return false;
	}
	
	public Type getType() {
		return Type.TAG_END;
	}
	
	public byte getTypeByte() {
		return (byte) Type.TAG_END.ordinal();
	}
	
	public String toString() {
		return new String("(" + Type.TAG_END + ")");
	}
	
	public Object getValue() {
		return null;
	}
}
