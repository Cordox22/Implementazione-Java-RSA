# \# RSA Implementation in Java

# 

# Implementazione didattica del crittosistema \*\*RSA\*\* sviluppata interamente in Java.  

# Il progetto mostra in modo chiaro e progressivo come funziona RSA “dietro le quinte”: generazione dei numeri primi, calcolo delle chiavi, cifratura e decifratura dei messaggi, utilizzando esclusivamente `BigInteger`.

# 

# ---

# 

# \## Funzionalità principali

# 

# \- Generazione di numeri primi tramite `BigInteger.probablePrime`

# \- Calcolo di:

# &nbsp; - Modulo \\( n = p \\cdot q \\)

# &nbsp; - Funzione di Eulero \\( \\phi(n) \\)

# \- Creazione della \*\*chiave pubblica\*\* e della \*\*chiave privata\*\*

# \- Cifratura di messaggi numerici

# \- Decifratura del messaggio cifrato

# \- Conversione tra testo e rappresentazione numerica

# \- Gestione della lunghezza delle chiavi tramite classe dedicata

# \- Codice semplice, leggibile e pensato per lo studio della crittografia asimmetrica

# 

# ---

# 

# \## Struttura del progetto

# 

# \### \*\*`RSA.java` — Core dell’algoritmo\*\*

# Classe centrale che implementa l’intero flusso matematico dell’algoritmo RSA:

# 

# \- generazione dei numeri primi \\( p \\) e \\( q \\)  

# \- calcolo del modulo \\( n = p \\cdot q \\)  

# \- calcolo della funzione di Eulero \\( \\phi(n) \\)  

# \- generazione dell’esponente pubblico (e)  

# \- generazione dell’esponente privato (d)  

# \- metodi per \*\*cifrare\*\* e \*\*decifrare\*\* tramite operazioni modulari

# 

# Questa classe incapsula tutta la logica crittografica, mantenendo separata la parte matematica dalla gestione dei messaggi.

# 

# ---

# 

# \### \*\*`CodificaRSA.java` — Conversione tra testo e numeri\*\*

# Poiché RSA opera su numeri, questa classe fornisce i metodi necessari per:

# 

# \- convertire una stringa in un valore numerico (`codifica`)  

# \- riconvertire un numero decifrato nella stringina originale (`decodifica`)  

# 

# Garantisce la compatibilità tra messaggi testuali e algoritmo matematico.

# 

# ---

# 

# \### \*\*`Messaggi.java` — Gestione dei messaggi\*\*

# Classe di supporto che centralizza la gestione dei messaggi da codificare e decodificare:

# 

# \- rappresentazione del messaggio in forma numerica  

# \- gestione ordinata di input e output  

# \- integrazione con `CodificaRSA` e `RSA`

# 

# Serve a mantenere il codice pulito e separare i dati dalla logica crittografica.

# 

# ---

# 

# \### \*\*`NumeroNBits.java` — Configurazione della lunghezza delle chiavi\*\*

# Permette di definire la dimensione delle chiavi RSA (es. 512, 1024, 2048 bit):

# 

# \- validazione del numero di bit  

# \- generazione di numeri casuali della lunghezza desiderata  

# \- configurazione del livello di sicurezza del sistema  

# 

# Consente all’utente di scegliere la robustezza dell’algoritmo.

# 

# ---

# 

# \### \*\*`Main.java` — Dimostrazione pratica\*\*

# Contiene una \*\*dimostrazione completa e commentata\*\* del funzionamento del programma.  

# Mostra passo dopo passo:

# 

# \- creazione di un’istanza di RSA  

# \- generazione delle chiavi  

# \- codifica del messaggio  

# \- cifratura  

# \- decifratura  

# \- riconversione in testo leggibile  

# 

# È una guida pratica per comprendere rapidamente come utilizzare il progetto.

# 

# ---

# 

# \## Esempio di utilizzo

# 

# ```java

# RSA rsa = new RSA(new NumeroNBits(1024));

# 

# BigInteger messaggio = CodificaRSA.codifica("Hello");

# BigInteger cifrato = rsa.cifra(messaggio);

# BigInteger decifrato = rsa.decifra(cifrato);

# 

# String risultato = CodificaRSA.decodifica(decifrato);

# System.out.println("Messaggio decifrato: " + risultato);



