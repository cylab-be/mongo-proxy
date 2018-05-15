/*
 * The MIT License
 *
 * Copyright 2018 Thibault Debatty.
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

import java.util.LinkedList;

/**
 * In BSON, a document is actually a list of elements.
 * @author Thibault Debatty
 */
public class Document {

    private final int start;
    private final int byte_size;
    private final LinkedList<Element> elements;

    /**
     * Read BSON document from a Bytes array.
     *
     * @param msg bytes array from which the BSON will be read.
     * @param start byte position from which the reading will begin.
     */
    public Document(final byte[] msg, final int start) {
        this.start = start;
        byte_size = Helper.readInt(msg, start);

        elements = new LinkedList<>();
        int pointer = start + 4;

        while (pointer < (start + byte_size - 1)) {
            Element el = Helper.readElement(msg, pointer);
            elements.add(el);
            pointer += el.size();

        }
    }

    /**
     * Size of the document, in Bytes.
     *
     * @return an integer
     */
    public final int byteSize() {
        return byte_size;
    }

    /**
     *
     * @return a String.
     */
    @Override
    public final String toString() {
        return elements.toString();
    }

    /**
     * Get the element of this document at given position.
     *
     * @param index position of the Element
     * @return Element
     */
    public final Element get(final int index) {

        return elements.get(index);
    }

    /**
     * Get the number of elements in this document.
     * @return an Integer.
     */
    public final int size() {
        return elements.size();
    }

    /**
     * Get the element that has the given name.
     * @param name name of the element.
     * @return an Element.
     */
    public final Element get(final String name) {
        for (Element el : elements) {
            if (el.getName().equals(name)) {
                return el;
            }
        }

        throw new IllegalArgumentException(
                "No element found with name " + name);
    }

}
