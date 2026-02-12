/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rsa;

import java.math.BigInteger;

/**
 *
 * @author User
 */
public class NumeroNBits {
    private long p; 
    private long q;
    
    // Calcola il massimo valore rappresentabile con un dato numero di bit.
    public BigInteger getMaxValue(int bits) {
        return BigInteger.valueOf(2).pow(bits).subtract(BigInteger.ONE);
    }
    
    //Calcola il valore minimo rappresentabile con un dato numero di bit.
    public BigInteger getMinValue(int bits){
        return BigInteger.valueOf(2).pow(bits - 1);
    }

    //Calcola quante cifre decimali ha il numero massimo rappresentabile con 'bits' bit.
    public int getDecimalDigits(int bits) {
        return getMaxValue(bits).toString().length();
    }
    
    //Calcola quante cifre decimali ha il numero massimo rappresentabile con 'bits' bit.
    public int getMinDecimalDigits(int bits) {
        return getMinValue(bits).toString().length();
    }

    //Stampa un riepilogo informativo per un dato numero di bit. Serve per vedere se P e Q sia davvero a quelle cifre decimali 
    public void printInfo(int bits) {
        BigInteger max = getMaxValue(bits);
        BigInteger min = getMinValue(bits);
        int digits = getDecimalDigits(bits);
        int minDigits = getMinDecimalDigits(bits);
        System.out.println("Bits: " + bits);
        System.out.println("Massimo valore: " + max);
        System.out.println("Valore Minimo: " + min);
        System.out.println("Massimo Cifre Decimali: " + digits);
        System.out.println("Minime Cifre Decimali: " + minDigits);
    }
    
    //Trovare P e Q a partire da N
    public void attaccoDiFermat(long n){
        long bQuadro, a, b;
        
        int cont = 0; 
        //Se i numeri primi sono vicini 
        a = (long)(Math.floor(Math.sqrt(n))); //Prende l intero piu vicino minore del numero radice
        do{
            a = a + 1;
            bQuadro = (a * a) - n;                    //Questo non e' b, e' b^2
            cont++;
            b = (long)Math.floor(Math.sqrt(bQuadro));          //Questo e' b
        }while(b * b != bQuadro);                     //per trovare b
        
        this.p = a - b;
        this.q = a + b;
    }
    
    public long getP(){
        return this.p;
    }
    
    public long getQ(){
        return this.q;
    }
}