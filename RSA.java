/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 **  🔐 Chiavi RSA
 * Chiave pubblica: (e,n)
 * Chiave privata: (d,n)
 * 
 *     Dove:
 * 
 * n = p ⋅ q
 * ϕ(n) = (p − 1)(q − 1) numero di Eulero
 * e è l’esponente pubblico (tipicamente 65537, e_fermat)
 * d è l’inverso di e modulo ϕ(n), cioè:
 * d ⋅ e ≡ 1 mod ϕ(n)
 * @author User
 */
public class RSA {
    private final SecureRandom random;
    private final int nBit; 
    private final BigInteger e_fermat; 
    private BigInteger eulero;
    private BigInteger p; 
    private BigInteger q; 
    private BigInteger n; 
    private BigInteger d; 
    private BigInteger diff; 
    private final String chiavePubblica; 
    private final String chiavePrivata; 
    
    public RSA(int nBit){
        this.random = new SecureRandom();
        this.nBit = nBit;
        this.e_fermat = BigInteger.valueOf(65537);
        this.inversoMoltiplicativo();
        this.chiavePubblica = this.e_fermat + ", " + this.n;
        this.chiavePrivata = this.d + ", " + this.n;
    }
    
    public BigInteger getN(){
        return this.n;
    }
    public BigInteger getE(){
        return this.e_fermat;
    }
    public BigInteger getD(){
        return this.d;
    }
    
    private BigInteger generaRandom(){
        BigInteger isPrimo = null;
        
        if (this.nBit < 1) {
            throw new IllegalArgumentException("I Bit inseriti devono essere maggiori di 1");
        }
        else{
            do{
            // Genera un numero casuale con nBits
                isPrimo = new BigInteger(this.nBit, this.random);
                // Assicura che il numero abbia esattamente nBits (cioè il bit più alto è 1)
                isPrimo = isPrimo.setBit(this.nBit - 1);
                //Controlla se il numero e' pari e aggiunge 1 facendolo diventare dispari. Un numero Pari non e' primo!!
                if(isPrimo.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                    isPrimo = isPrimo.add(BigInteger.ONE);
                }
            }while(!isPrimo.isProbablePrime(100));
        }
        //la probabilita' che sia composto e' estremamente bassa: < 1/10^30 Praticamente zero
				
        return isPrimo;
    }
    
    //Calcola D
    private void inversoMoltiplicativo(){
        BigInteger app;      
        boolean flag = false;
        
        do{
            this.calcolaN();
            //formula e = (p - 1)*(q - 1)
            BigInteger p_1 = this.p.subtract(BigInteger.ONE);
            BigInteger q_1 = this.q.subtract(BigInteger.ONE);
            this.eulero = p_1.multiply(q_1);                   //Numero di eulero cioe phi(n)
            app = this.eulero.gcd(this.e_fermat);              //Numero di eulero e il numero di Fermat non devono avere divisori comuni(coprimi)
            flag = !app.equals(BigInteger.ONE);
        }while(flag);
        //                               Calcolo d tale che (e * d) % φ(n) = 1
        this.d = this.e_fermat.modInverse(this.eulero); // Formula inversoMoltiplicativo: d * e_fermat % phi(n) = 1. (phi(n) = eulero)
    }
    
    private void calcolaN(){
        int app = 0;
        //soglia minima di P e Q
        BigInteger soglia = BigInteger.TWO.pow(this.nBit -2); 
        
        do{
            this.p = this.generaRandom();
            this.q = this.generaRandom();

            //Calcola il valore asssoluto di P e Q 
            this.diff = this.p.subtract(this.q).abs();
            
            if(!this.p.equals(this.q) && (this.diff.compareTo(soglia) > 0)){
                this.n = this.p.multiply(this.q);    //calcolo n se P e Q sono diversi e la loro differenza e' maggiore della soglia
                app = this.n.bitLength(); //controllo se i due numeri moltiplicati rispettano le cifre decimali 
            }
        }while(app != (this.nBit * 2));
    }

    @Override
    public String toString(){
        String info = "\nP: " +  this.p + "\nQ: " + this.q + "\nN: " + this.n;
        info += "\nLunghezza N: " + this.n.toString().length();
        info += "\nNumero Bit N: " + this.n.bitLength();
        info += "\nDistanza tra P e Q: " + this.diff;
        info += "\nChiave Pubblica: " + this.chiavePubblica; 
        info += "\nChiave Privata: " + this.chiavePrivata;
        info += "\n--------------------------------------\n";
        
        return info;
    }
}