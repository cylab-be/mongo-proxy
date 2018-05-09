/*
 * The MIT License
 *
 * Copyright 2018 sonoflight.
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

/**
 *
 * @author sonoflight
 */
public class Read {

    public static final char STRING_TERMINATION = 0;

    /**
     * Read a Cstring from the byte array.
     *
     * @param msg byte array which contain the message.
     * @param start byte position from which the reading will begin.
     * @return a Sting.
     */
    public static String CString(final byte[] msg, final int start) {
        StringBuilder string = new StringBuilder();
        int i = start;
        while (msg[i] != STRING_TERMINATION) {
            string.append((char) msg[i]);
            i++;
        }
        return string.toString();
    }

    /**
     * Read a string from the byte array.
     *
     * @param msg byte array which contain the message.
     * @param start byte position from which the reading will begin.
     * @return a String.
     */
    public static String String(final byte[] msg, final int start) {
        return CString(msg, start + 4);
    }

    /**
     * Consertion of 4bytes to a single int.
     *
     * @param bytes array were the integer will be read.
     * @param start byte position from which the reading will begin.
     * @return an integer.
     */
    protected static int Int(final byte[] bytes, final int start) {
        return (bytes[start + 3] << 24) & -16777216 | (
                bytes[start + 2] << 16) & 16711680 | (
                bytes[start + 1] << 8) & 65280 | (
                bytes[start]) & 255;
    }

    /**
     * Extract a Byte from a Bytes array.
     *
     * @param msg bytes array from which the BSON will be read.
     * @param start byte position from which the reading will begin.
     * @return a byte.
     */
    public static final Byte Byte(final byte[] msg, final int start) {
        return msg[start];
    }

    /**
     * reconstruct the parts of the document.
     *
     * @param msg bytes array from which the BSON will be read.
     * @param start byte position from which the reading will begin.
     * @return an object of type Element.
     */
    public static final Element Element(final byte[] msg, final int start) {
        return Element.parse(msg, start);
    }

}
