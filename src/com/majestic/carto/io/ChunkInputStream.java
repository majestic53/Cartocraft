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
 *  Cartocraft -- ChunkInputStream.java
 *
 *  Created on: Jul 30, 2011
 *      Author: David Jolly
 *      		[jollyd@onid.oregonstate.edu]
 *
 */

package com.majestic.carto.io;

import java.io.DataInputStream;
import java.io.IOException;
import com.majestic.carto.io.tag.ByteArrayTag;
import com.majestic.carto.io.tag.ByteTag;
import com.majestic.carto.io.tag.CompoundTag;
import com.majestic.carto.io.tag.DoubleTag;
import com.majestic.carto.io.tag.EndTag;
import com.majestic.carto.io.tag.FloatTag;
import com.majestic.carto.io.tag.IntTag;
import com.majestic.carto.io.tag.ListTag;
import com.majestic.carto.io.tag.LongTag;
import com.majestic.carto.io.tag.ShortTag;
import com.majestic.carto.io.tag.StringTag;
import com.majestic.carto.io.tag.Tag;
import com.majestic.carto.io.tag.Tag.TagType.Type;

public class ChunkInputStream {

	private DataInputStream is;
	private Tag root;

	/**
	 * ChunkInputStream constructor
	 * @param is DataInputStream
	 * @throws IOException
	 */
	public ChunkInputStream(DataInputStream is) throws IOException {
		if(is == null)
			return;
		this.is = is;
		Type type = Tag.TagType.byteToType(this.is.readByte());
		if(type != Type.TAG_END)
			root = readTag(type, this.is.readUTF());
		else
			root = new EndTag();
		this.is.close();
	}

	/**
	 * Returns a chunks root tag
	 * @return Tag
	 */
	public Tag getRootTag() {
		return root;
	}

	/**
	 * Returns a tag by name
	 * @param name String
	 * @return Tag
	 */
	public Tag getTag(String name) {
		return getTagByNameHelper(root, name);
	}

	/**
	 * Helper method
	 * @param tag Tag
	 * @param name String
	 * @return Tag
	 */
	private Tag getTagByNameHelper(Tag tag, String name) {
		if(tag != null) {
			if(tag.getName().equals(name))
				return tag;
			else if(tag.getType() == Type.TAG_COMPOUND)
				return getTagByNameHelper(getTagByNameInCompound((Tag[]) tag.getValue(), name), name);
		}
		return null;
	}

	/**
	 * Returns a tag by name within a compound tag
	 * @param tags Tag[]
	 * @param name String
	 * @return Tag
	 */
	private Tag getTagByNameInCompound(Tag[] tags, String name) {
		for(int i = 0; i < tags.length; i++) {
			if(tags[i].getName() == null)
				continue;
			if(tags[i].getName().equals(name))
				return tags[i];
		}
		for(int i = 0; i < tags.length; i++)
			if(tags[i].getType() == Type.TAG_COMPOUND)
				return getTagByNameInCompound((Tag[]) tags[i].getValue(), name);
		return null;
	}

	/**
	 * Returns a tag read in from a chunk file
	 * @param type Type
	 * @param name String
	 * @return Tag
	 * @throws IOException
	 */
	private Tag readTag(Type type, String name) throws IOException {
		Tag tag = null;
		switch(type) {
			case TAG_END:
				return tag = new EndTag();
			case TAG_BYTE:
				return tag = new ByteTag(name, (Byte) readValue(type));
			case TAG_SHORT:
				return tag = new ShortTag(name, (Short) readValue(type));
			case TAG_INT:
				return tag = new IntTag(name, (Integer) readValue(type));
			case TAG_LONG:
				return tag = new LongTag(name, (Long) readValue(type));
			case TAG_FLOAT:
				return tag = new FloatTag(name, (Float) readValue(type));
			case TAG_DOUBLE:
				return tag = new DoubleTag(name, (Double) readValue(type));
			case TAG_BYTE_ARRAY:
				return tag = new ByteArrayTag(name, (byte[]) readValue(type));
			case TAG_STRING:
				return tag = new StringTag(name, (String) readValue(type));
			case TAG_LIST:
				return tag = new ListTag(name, (Tag[]) readValue(type));
			case TAG_COMPOUND:
				return tag = new CompoundTag(name, (Tag[]) readValue(type));
			default:
				return tag;
		}
	}

	/**
	 * Returns a value read in from a chunk file
	 * @param type Type
	 * @return Object
	 * @throws IOException
	 */
	private Object readValue(Type type) throws IOException {
		switch(type) {
			case TAG_BYTE:
				return is.readByte();
			case TAG_SHORT:
				return is.readShort();
			case TAG_INT:
				return is.readInt();
			case TAG_LONG:
				return is.readLong();
			case TAG_FLOAT:
				return is.readFloat();
			case TAG_DOUBLE:
				return is.readDouble();
			case TAG_BYTE_ARRAY:
				byte[] byteArray = new byte[is.readInt()];
				is.readFully(byteArray);
				return byteArray;
			case TAG_STRING:
				return is.readUTF();
			case TAG_LIST:
				Type eleType = Tag.TagType.byteToType(is.readByte());
				int eleLen = is.readInt();
				Tag[] elements = new Tag[eleLen];
				for(int i = 0; i < eleLen; i++)
					elements[i] = readTag(eleType, new String());
				return elements;
			case TAG_COMPOUND:
				Type tagType;
				Tag[] tags = new Tag[0];
				do {
					tagType = Tag.TagType.byteToType(is.readByte());
					String name = null;
					if(tagType != Type.TAG_END)
						name = is.readUTF();
					Tag[] temp = new Tag[tags.length + 1];
					for(int i = 0; i < tags.length; i++)
						temp[i] = tags[i];
					temp[tags.length] = readTag(tagType, name);
					tags = temp;
				} while(tagType != Type.TAG_END);
				return tags;
			default:
				return null;
		}
	}
}
