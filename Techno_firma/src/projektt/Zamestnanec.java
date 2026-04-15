package projektt;

import java.util.*;
import java.io.Serializable;

/**
 * Abstraktní třída reprezentující zaměstnance technologické firmy[cite: 2, 25].
 * Implementuje Serializable pro možnost ukládání do souboru[cite: 18].
 */
public abstract class Zamestnanec implements Serializable {
    // Základní atributy zaměstnance [cite: 2]
    private final int id; 
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    
    /** * Dynamická datová struktura pro seznam spolupracovníků a úroveň spolupráce.
     * Klíč: ID spolupracovníka, Hodnota: Kvalita (Enum)
     */
    protected Map<Integer, Kvalita> spolupracovnici = new HashMap<>();

    public Zamestnanec(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
    }

    /** * Abstraktní metoda pro specifické dovednosti skupin (Analytici/Specialisté)[cite: 14, 25].
     */
    public abstract void provedDovednost(Map<Integer, Zamestnanec> vsichni);

    // --- METODY PRO PRÁCI S VAZBAMI ---

    public void pridejVazbu(int idKolegy, Kvalita k) { 
        spolupracovnici.put(idKolegy, k); 
    }

    public void odeberVazbu(int idKolegy) { 
        spolupracovnici.remove(idKolegy); 
    }
    
    // --- VEŘEJNÉ GETTERY (Důležité pro SQL export a výpisy) ---

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

    // --- FORMÁTOVANÝ VÝPIS ---

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%d) | Počet vazeb: %d", 
            id, jmeno, prijmeni, rokNarozeni, spolupracovnici.size());
    }
}