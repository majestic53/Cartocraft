/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 *  Cartocraft -- ByteArrayTag.java
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
