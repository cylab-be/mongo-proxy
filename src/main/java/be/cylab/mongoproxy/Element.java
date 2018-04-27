/*
 * The MIT License
 *
 * Copyright 2018 tibo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package be.cylab.mongoproxy;

import static be.cylab.mongoproxy.ConnectionHandler.readInt;
import static be.cylab.mongoproxy.Document.readByte;
import static be.cylab.mongoproxy.ConnectionHandler.readCString;
import static be.cylab.mongoproxy.ConnectionHandler.readString;
import java.util.Arrays;

/**
 *
 * @author tibo
 * @param <T> type of value contained in the element
 */
public abstract class Element<T> {

    public static boolean readBoolean(byte[] msg, int start) {
        byte b = readByte(msg, start);
        return b == 1;
    }

    private static byte[] readObjectId(byte[] msg, int start) {
        return Arrays.copyOfRange(msg, start, start + 12);
    }



    private final int type;
    private final String name;

    Element(final int type, final String name) {
        this.type = type;
        this.name = name;
    }

    public int size() {
        return 2 + name.length();
    }

    /**
     *
     * @return
     */
    public String toString() {
        return type + ":" + name;
    }

    /**
     *
     * @param msg
     * @param start
     * @return
     */
    public static Element parse(final byte[] msg, final int start) {
        int type = readByte(msg, start);
        String name = readCString(msg, start + 1);

        if (type == 16) {
            int value = readInt(msg, start + name.length() + 2);
            return new ElementInt(type, name, value);
        }

        if (type == 2) {
            String value = readString(msg, start + name.length() + 2);
            return new ElementString(type, name, value);
        }

        if (type == 3) {
            Document value = new Document(msg, start + name.length() + 2);
            return new ElementDocument(type, name, value);
        }

        if (type == 4) {
            Document value = new Document(msg, start + name.length() + 2);
            return new ElementDocument(type, name, value);
        }

        if (type == 7) {
            byte[] value = readObjectId(msg, start + name.length() + 2);
            return new ElementObjectId(type, name, value);
        }

        if (type == 8) {
            boolean value = readBoolean(msg, start + name.length() + 2);
            return new ElementBoolean(type, name, value);
        }

        return new DummyElement(type, name);
    }

    protected T value;

    public T getValue() {
        return value;
    }

}
