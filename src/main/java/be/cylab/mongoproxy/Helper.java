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
public final class Helper {

    /**
     * Read constructor.
     */
    private Helper() {
    }

    /**
     *
     */
    public static final char STRING_TERMINATION = 0;

    /**
     * Read a Cstring from the byte array.
     *
     * @param msg byte array which contain the message.
     * @param start byte position from which the reading will begin.
     * @return a Sting.
     */
    public static String readCString(final byte[] msg, final int start) {
        StringBuilder string = new StringBuilder();
        int i = start;
        while (msg[i] != STRING_TERMINATION) {
            string.append((char) msg[i]);
            i++;
        }
        return string.toString();
    }

    /**
     *
     * @param bytes array were the Int64 will be read.
     * @param start byte position from which the reading will begin.
     * @return a double.
     */
    protected static long readInt64(final byte[] bytes, final int start) {
        long longbits = 0;
        int i = start;
        for (int shiftby = 0; shiftby < 64; shiftby += 8) {
            longbits |= ((long) (bytes[start + i] & 0xff)) << shiftby;
            i++;
        }

        return longbits;
    }

    /**
     * Read a string from the byte array.
     *
     * @param msg byte array which contain the message.
     * @param start byte position from which the reading will begin.
     * @return a String.
     */
    public static String readString(final byte[] msg, final int start) {
        return readCString(msg, start + 4);
    }

    /**
     * Consertion of 4bytes to a single int.
     *
     * @param bytes array were the integer will be read.
     * @param start byte position from which the reading will begin.
     * @return an integer.
     */
    protected static int readInt(final byte[] bytes, final int start) {
        return (bytes[start + 3] << 24) & -16777216 | (
                bytes[start + 2] << 16) & 16711680 | (
                bytes[start + 1] << 8) & 65280 | (
                bytes[start]) & 255;
    }

    /**
     *
     * @param bytes array were the double will be read.
     * @param start byte position from which the reading will begin.
     * @return a double.
     */
    protected static double readDouble(final byte[] bytes, final int start) {
        long longbits = 0;
        int i = start;
        for (int shiftby = 0; shiftby < 64; shiftby += 8) {
            longbits |= ((long) (bytes[start + i] & 0xff)) << shiftby;
            i++;
        }

        return Double.longBitsToDouble(longbits);
    }

    /**
     * Extract a Byte from a Bytes array.
     *
     * @param msg bytes array from which the BSON will be read.
     * @param start byte position from which the reading will begin.
     * @return a byte.
     */
    public static Byte readByte(final byte[] msg, final int start) {
        return msg[start];
    }

    /**
     * reconstruct a parts of a BSON document from a byte array.
     *
     * @param msg bytes array from which the BSON will be read.
     * @param start byte position from which the reading will begin.
     * @return an object of type Element.
     */
    public static Element readElement(final byte[] msg, final int start) {
        return Element.parse(msg, start);
    }

}
