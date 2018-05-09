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

/**
 *
 * @author Thibault Debatty
 */
public enum OpCode {

    /**
     *
     */
    OP_REPLY(1),
    /**
     *
     */
    OP_UPDATE(2001),
    /**
     *
     */
    OP_INSERT(2002),
    /**
     *
     */
    RESERVED(2003),
    /**
     *
     */
    OP_QUERY(2004),
    /**
     *
     */
    OP_GET_MORE(2005),
    /**
     *
     */
    OP_DELETE(2006),
    /**
     *
     */
    OP_KILL_CURSORS(2007),
    /**
     *
     */
    OP_COMMAND(2010),
    /**
     *
     */
    OP_COMMANDREPLY(2011),
    /**
     *
     */
    OP_MSG(2013),
    /**
     *
     */
    OP_UNKNOWN(0);

    private final int code;

    /**
     *
     * @param code code of the request.
     */
    OpCode(final int code) {
        this.code = code;
    }

    /**
     *
     * @return an integer.
     */
    public int getValue() {
        return code;
    }

    /**
     * Find opcode from it's numerical value.
     *
     * @param value code of the request.
     * @return an Opcode.
     */
    public static OpCode findByValue(final int value) {
        for (OpCode opcode : values()) {
            if (opcode.getValue() == value) {
                return opcode;
            }
        }

        return OpCode.OP_UNKNOWN;
    }

}
