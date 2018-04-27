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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author tibo
 */
public class Document {
    private int start;
    private int size;
    private List<Element> elements;

    /**
     *
     * @param msg
     * @param start
     */
    public Document(final byte[] msg, final int start) {
        this.start = start;
        size = readInt(msg, start);
        // System.out.println("Document length: " + size);
        // System.out.println("Document end: " + (start + size));


        elements = new LinkedList<>();
        int pointer = start + 4;
        while (pointer < (start + size - 1)) {
            //System.out.println("Pointer position: " + pointer);
            Element el = readElement(msg, pointer);
            elements.add(el);
            pointer += el.size();
        }

    }

    /*public Element get(int index) {
        return elements.get(index);
    }*/

    public int size() {
        return size;
    }

    /**
     *
     * @param msg
     * @param start
     * @return
     */
    public final Element readElement(final byte[] msg, final int start) {

        return Element.parse(msg, start);
    }

    /**
     *
     * @param msg
     * @param start
     * @return
     */
    public static final Byte readByte(final byte[] msg, final int start) {
        return msg[start];
    }

    /**
     *
     * @return
     */
    public final String toString() {
        return elements.toString();
    }
}
