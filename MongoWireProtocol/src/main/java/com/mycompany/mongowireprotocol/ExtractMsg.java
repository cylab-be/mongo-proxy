/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

import java.util.Arrays;

/**
 *
 * @author sonoflight
 */
public class ExtractMsg implements Runnable {

    private final byte[] msg;

    private static final int BYTE_1 = 0x000000ff;
    private static final int BYTE_2 = 0x0000ff00;
    private static final int BYTE_3 = 0x00ff0000;
    private static final int BYTE_4 = 0xff000000;

    private static final int BYTEPOSITION_8 = 8;
    private static final int BYTEPOSITION_16 = 16;
    private static final int BYTEPOSITION_24 = 24;

    ExtractMsg(byte[] m) {
        this.msg = m;
    }

    @Override
    public void run() {
        //Determination of the length of the collection 
        //name in the OP_QUERY msg
        int i = 20;//start position of cstring
        char t = 0x00;
        while (msg[i] != t) {
            System.out.println((char) msg[i]);

            i++;
        }

        int name_collection_length = i - 20;

        int l = 0;

        char[] name_collection = new char[name_collection_length];

        System.out.println("Create msg");

        while (l < name_collection_length) {
            name_collection[l] = (char) msg[20 + l];
            l++;
        }

        System.out.println("collection name : " + Arrays.toString(name_collection));
        System.out.println("End of name collection extract.");

        //Get documcument in msg
        System.out.println("extract document...");
        int j = 20 + name_collection_length + 8;//start position of Document

        final int document_lenght = (msg[j + 3] << BYTEPOSITION_24) & BYTE_4
                | (msg[j + 2] << BYTEPOSITION_16) & BYTE_3
                | (msg[j + 1] << BYTEPOSITION_8) & BYTE_2
                | (msg[j] << 0) & BYTE_1;
        System.out.println("lenght document : " + document_lenght);
        byte[] document = new byte[document_lenght];
        int k;
        for (k = 0; k < document_lenght; k++) {
            document[0] = msg[j + k];
        }
        String doc = Arrays.toString(document);
        System.out.append("document : " + doc);
    }

}
