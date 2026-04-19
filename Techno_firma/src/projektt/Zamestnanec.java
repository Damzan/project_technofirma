package projektt;

import java.util.*;
import java.io.Serializable;


public abstract class Zamestnanec {
    
    private final int id; 
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    
    
    protected Map<Integer, Kvalita> spolupracovnici = new HashMap<>();

    public Zamestnanec(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
    }

    
    public abstract void provedDovednost(Map<Integer, Zamestnanec> vsichni);


    public void pridejVazbu(int idKolegy, Kvalita k) { 
        spolupracovnici.put(idKolegy, k); 
    }

    public void odeberVazbu(int idKolegy) { 
        spolupracovnici.remove(idKolegy); 
    }
    
    

    public int getId() { 
        return id; 
    }

    public String getJmeno() { 
        return jmeno; 
    }

    public String getPrijmeni() { 
        return prijmeni; 
    }

    public int getRokNarozeni() { 
        return rokNarozeni; 
    }

    public Map<Integer, Kvalita> getSpolupracovnici() { 
        return spolupracovnici; 
    }

   

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%d) | Počet vazeb: %d", 
            id, jmeno, prijmeni, rokNarozeni, spolupracovnici.size());
    }
}