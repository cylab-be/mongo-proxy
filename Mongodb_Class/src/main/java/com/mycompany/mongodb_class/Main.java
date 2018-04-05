/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongodb_class;

/**
 *
 * @author sonoflight
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
     SearchFileClass mongodb = new SearchFileClass("myDb");
     
     mongodb.SearchDoc("Title", "Cylab");
     
     mongodb.SearchDoc("Title", "Projet");
     
    }
    
}
