/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DF;

/**
 *
 * @author nur_uddin
 */
public class Values {
    int value1;
    int value2;

    /**this method is the constructor of this class that assign initial values
     * into variable of this class
     * 
     * @param v1 this variable assign value into variable value1
     * @param v2 this variable assing value into variable value2
     */
    Values(int v1, int v2) {
        this.value1 = v1;
        this.value2 = v2;
    }

    /**this method updates the value of two class variable of this class
     * 
     * @param v1 this variable assign value into variable value1
     * @param v2 this variable assing value into variable value2
     */
    public void updateValue(int v1, int v2) {
        this.value1 = v1;
        this.value2 = v2;
    }
}
