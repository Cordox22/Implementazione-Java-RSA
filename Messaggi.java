package com.mycompany.rsa;


import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
public class Messaggi {
    private BigInteger n; 
    private BigInteger e_fermat; 
    private BigInteger d;
    private BigInteger codifica;
    private String decodifica;
    private List<BigInteger> blocchiCifrati;
    
    public Messaggi(BigInteger e, BigInteger n){
        this.n = n; 
        this.d = BigInteger.ZERO; 
        this.e_fermat = e;
        this.blocchiCifrati = new ArrayList<>();
    }
    
    //Chiavi Mie
    public Messaggi(){
        this.n = new BigInteger("173900931606119316357169548397725628201");
        this.e_fermat = BigInteger.valueOf(65537l);
        this.d = new BigInteger("74042626234602642825888833592573382657");
        this.blocchiCifrati = new ArrayList<>();
    }
    
    public BigInteger getN() {
        return this.n;
    }
    public BigInteger getE_fermat() {
        return this.e_fermat;
    }
    
    //Messaggio codificato di prova
    public void codifica(String msg){
        //converti la stringa in intero 
        byte[] messaggeBytes = msg.getBytes(StandardCharsets.UTF_8);
        //1 forza il valore ad essere positivo byte va da -128 a +127
        BigInteger m = new BigInteger(1,messaggeBytes);   
        //verifica che m < n
        if(m.compareTo(this.n) >= 0){
            throw new IllegalArgumentException("Messaggio troppo lungo per questo modulo a " + this.n.bitLength() + " bit");
        }
        //3) Codifica RSA: c = m^e mod n 
        this.codifica = m.modPow(this.e_fermat, this.n);
    }
    
    //Messaggio codificato con un altra chiave non la propria 
    public void codifica(BigInteger eUtente, BigInteger nUtente, String msg){
        this.blocchiCifrati.clear();
        //this.blocchiCifrati = new ArrayList<>(); //Inizializza l arrayList
        //converte la stringa in array di byte, in UTF-8, ogni carattere e' 1 byte
        byte[] messaggeBytes = msg.getBytes(StandardCharsets.UTF_8);
        //1 forza il BigInteger ad essere positivo. Converte messaggeBytes in un BigInteger
        BigInteger m = new BigInteger(1, messaggeBytes);  
        
        //Se m > n puo' essere cifrato in blocco
        if(m.compareTo(nUtente) >= 0){ 
            //Numeri di bit necessari per rappresentare n : Quanti byte al massimo puo' contenere un blocco 
            //Ogni byte = 1 carattere del messaggio, quindi se maxBlockSize = 7 : puo' contenere un massimo di 7 byte 
            int maxBlockSize = (this.n.bitLength() - 1) / 8;
            int len = msg.length();
            for(int i = 0; i < len; i = i + maxBlockSize){
                //Controllo se non vado oltre il messaggio(messaggeBytes)
                //Restituisce il valore di maxBlockSize (riga 136) ad ogni iterazione
                int fineMessaggio = Math.min(i + maxBlockSize, messaggeBytes.length); 
                //Copia fino all' indice di fineMessaggio. es: fineMessaggio = 3 infila 3 indici dentro l array 
                byte[] pacchetti = Arrays.copyOfRange(messaggeBytes, i, fineMessaggio);
                //Costruisci il messaggio con i valori positivi, converte pacchetti in BigInteger (potrebbe cambiare il messaggio altrimenti)
                m = new BigInteger(1, pacchetti);
                //Modulo inverso.I blocchi sono cifrati in base ai bit e cifre decimali di n
                this.blocchiCifrati.add(m.modPow(eUtente, nUtente));
            }
        }
        else{
            this.codifica = m.modPow(eUtente, nUtente);
        }
    }
    
    //Codifica il messaggio con l oggetto utente DEVO USARE QUESTO 
    public void codifica(Messaggi utente, String msg){
        this.blocchiCifrati.clear();
        //Converto la Stringa in un intero 
        byte[] messaggeBytes = msg.getBytes(StandardCharsets.UTF_8);
        BigInteger m = new BigInteger(1, messaggeBytes);
        
        //Verifico che m < n 
        if(m.compareTo(utente.n) >= 0){
            int maxBlockSize = (n.bitLength() - 1 ) / 8;
            int len = messaggeBytes.length;
            for(int i = 0; i < len; i += maxBlockSize){
                int fineMessaggio = Math.min(i + maxBlockSize, messaggeBytes.length);
                //Copia i pacchetti nell array 
                byte[] pacchetti = Arrays.copyOfRange(messaggeBytes, i, fineMessaggio);
                //Costruisci il messaggio con valori positivi 
                m = new BigInteger(1, pacchetti);
                this.blocchiCifrati.add(m.modPow(utente.e_fermat, utente.n));
            }
        }
        else{
            //Codifica RSA :  C = m^e mod n
            this.codifica = m.modPow(utente.e_fermat, utente.n);
        }
    }    
    
    //Messaggio decodificato di prova
    public void decodifica(){
        //1. Decifra: m = c^d mod n 
        BigInteger m = this.codifica.modPow(this.d, this.n);
        //2. Converte in stringa 
        byte[] app = m.toByteArray();
        //3. Rimuove eventuale byte iniziale di padding | il primo byte PUO' essere 0 o -1 
        if((app.length > 1)&&(app[0] == 0)){
           app = Arrays.copyOfRange(app, 1, app.length);
        }
        //4. Converte in Stringa
        this.decodifica = new String(app, StandardCharsets.UTF_8);
    }
    
    //Messaggio decodificato con un altra chiave non la propria 
    public void decodifica(BigInteger dUtente, BigInteger nUtente){
        BigInteger m = this.codifica.modPow(dUtente, nUtente);
        
        if(m.compareTo(this.n) > 0){
            throw new IllegalArgumentException("Messaggio troppo lungo per questo modulo a " + n.bitLength() + " bit");
        }
        else{
            byte[] app = m.toByteArray();
            if((app.length > 1)&&(app[0] == 0)){
                app = Arrays.copyOfRange(app, 1, app.length);
            }
            this.decodifica = new String(app, StandardCharsets.UTF_8);
            System.out.println("Questo e' un blocco singolo");
        }
    }
    
    //Messaggio decodificato con l oggetto utente 
    public void decodifica(Messaggi utente){
        if(utente.codifica != null){
            BigInteger m = utente.codifica.modPow(this.d, this.n);
            byte[] app = m.toByteArray();
            if((app.length > 1)&&(app[0] == 0)){
                app = Arrays.copyOfRange(app, 1, app.length);
            }
            this.decodifica = new String(app, StandardCharsets.UTF_8);
        }
        else{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for(BigInteger c : utente.blocchiCifrati){
                BigInteger m = c.modPow(this.d, this.n);
                byte[] app = m.toByteArray();
                if((app.length > 1) && (app[0] == 0)){
                    app = Arrays.copyOfRange(app, 1, app.length);
                }
                baos.write(app, 0, app.length);
            }
            this.decodifica = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }
    }
    
    //Decodifica il messaggio con la sola Stringa: DEVO USARE QUESTO 
    public void decodifica(String input){
        List<BigInteger> blocchiCifrati = new ArrayList<>();
        //tolgo le parentesi dal messaggio cifrato
        input = input.replace("[", "").replace("]", "");
        //tolgo le virgole dal messaggio cifrato
        String[] valori = input.split(",");
        
        for(String n : valori){
            //tolgo tutti gli spazi 
            blocchiCifrati.add(new BigInteger(n.trim()));
        }
        
        //buffer per ricostruire il messaggio cifrato 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(BigInteger c : blocchiCifrati){
            //decifra il blocco 
            BigInteger m = c.modPow(this.d, this.n);
            //Rappresentazione in byte del BigInteger decifrato 
            byte[] chunk = m.toByteArray();
            //controllo se c'e' un byte a 0 all'inizio 
            if((chunk.length > 1) && (chunk[0] == 0)){
                //se c'e' lo tolgo 
                chunk = Arrays.copyOfRange(chunk, 1, chunk.length);
            }
            //Compone il numero baos(BigInteger)
            baos.write(chunk, 0, chunk.length);
        }
        this.decodifica = new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }
    
    @Override
    public String toString(){
        String info = "Chiave Pubblica: " + this.e_fermat + ", " + this.n;
        info += "\nChiave Privata " + this.d + ", " + this.n;
        
        if(this.codifica == null){
            info += "\nBlocchi Cifrati: " + this.blocchiCifrati;
        }
        else{
            info += "\nMessaggio Codificato: " + this.codifica;
        }
        
        info += "\nMessaggio Decodificato: " + this.decodifica + "\n----------------------------\n";
        
        return info;
    }
}