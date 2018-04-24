/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongowireprotocol;

/**
 *
 * @author sonoflight
 */
public class ExtractMsg implements Runnable {

    private final byte[] msg;

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
            System.out.println("Reading...");
            System.out.println((char) msg[i]);

            i++;
        }

        int name_collection_length = i - 20;

        int l = 0;

        char[] name_collection = new char[name_collection_length];
        
        System.out.println("Create msg");

        while (l <= name_collection_length) {
            name_collection[l] = (char) msg[20 + l];
        }
        for (char r : name_collection) {
            System.out.println("char" + r);
        }
    }

}
