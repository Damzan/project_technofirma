package projektt;

import java.sql.*;
import java.util.*;

public class SpravaFirmy {
    // Dynamická datová struktura pro uložení všech zaměstnanců [cite: 26]
    private Map<Integer, Zamestnanec> databaze = new HashMap<>();
    private int idCounter = 1;

    // Konfigurace MySQL - upravte heslo dle svého nastavení
    private final String URL = "jdbc:mysql://localhost:3306/firma_db";
    private final String USER = "root";
    private final String PASS = "tvoje_heslo";

    // a) Přidání zaměstnance [cite: 8, 10]
    public void pridejZamestnance(String typ, String jmeno, String prijmeni, int rok) {
        Zamestnanec novy;

        if (typ.equalsIgnoreCase("A")) {
            novy = new DatovyAnalytik(idCounter++, jmeno, prijmeni, rok);
        } else {
            novy = new BezpecnostniSpecialista(idCounter++, jmeno, prijmeni, rok);
        }

        databaze.put(novy.getId(), novy);
        System.out.println("Úspěch: Zaměstnanec " + prijmeni + " přidán s automatickým ID: " + novy.getId());
    }

    // b) Přidání spolupráce [cite: 11]
    public void pridejSpolupraci(int id1, int id2, Kvalita kvalita) {
        Zamestnanec z1 = databaze.get(id1);
        Zamestnanec z2 = databaze.get(id2);

        if (z1 != null && z2 != null) {
            z1.pridejVazbu(id2, kvalita);
            z2.pridejVazbu(id1, kvalita); // Spolupráce je oboustranná
            System.out.println("Spolupráce mezi " + id1 + " a " + id2 + " byla nastavena.");
        } else {
            System.out.println("Chyba: Jedno nebo obě ID neexistují.");
        }
    }

    // c) Odebrání zaměstnance včetně všech vazeb [cite: 12]
    public void odeberZamestnance(int id) {
        if (databaze.containsKey(id)) {
            databaze.remove(id);
            // Odstranění odkazu na toto ID u všech ostatních kolegů
            for (Zamestnanec z : databaze.values()) {
                z.odeberVazbu(id);
            }
            System.out.println("Zaměstnanec " + id + " byl kompletně odstraněn ze systému.");
        } else {
            System.out.println("Zaměstnanec s tímto ID nebyl nalezen.");
        }
    }

    // d) Vyhledání zaměstnance dle ID [cite: 13]
    public void vyhledejAZobraz(int id) {
        Zamestnanec z = databaze.get(id);
        if (z != null) {
            System.out.println("--- Detail zaměstnance ---");
            System.out.println(z.toString());
            System.out.println("Počet spolupracovníků: " + z.getSpolupracovnici().size());
        } else {
            System.out.println("ID " + id + " neexistuje.");
        }
    }

    // Pomocná metoda pro Main [cite: 13]
    public Zamestnanec hledej(int id) {
        return databaze.get(id);
    }

    // f) Abecední výpis zaměstnanců [cite: 15]
    public void vypisAbecedne() {
        System.out.println("\n--- Seznam zaměstnanců (dle příjmení) ---");
        databaze.values().stream()
                .sorted(Comparator.comparing(Zamestnanec::getPrijmeni))
                .forEach(System.out::println);
    }

    // g) Statistiky [cite: 16]
    public void zobrazStatistiky() {
        if (databaze.isEmpty()) {
            System.out.println("Databáze je prázdná.");
            return;
        }

        // Zaměstnanec s nejvíce vazbami
        Zamestnanec top = databaze.values().stream()
                .max(Comparator.comparingInt(z -> z.getSpolupracovnici().size()))
                .orElse(null);

        // Převažující kvalita spolupráce
        Map<Kvalita, Integer> frekvenceKvality = new HashMap<>();
        for (Zamestnanec z : databaze.values()) {
            for (Kvalita k : z.getSpolupracovnici().values()) {
                frekvenceKvality.put(k, frekvenceKvality.getOrDefault(k, 0) + 1);
            }
        }

        Kvalita nejcastejsi = frekvenceKvality.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        System.out.println("Nejvíce kontaktů: " + (top != null ? top.getPrijmeni() + " (" + top.getSpolupracovnici().size() + ")" : "N/A"));
        System.out.println("Převažující úroveň spolupráce: " + (nejcastejsi != null ? nejcastejsi : "N/A"));
    }

    // h) Výpis počtu zaměstnanců ve skupinách [cite: 17]
    public void vypisPoctyVeSkupinach() {
        long analytici = databaze.values().stream().filter(z -> z instanceof DatovyAnalytik).count();
        long specialiste = databaze.values().stream().filter(z -> z instanceof BezpecnostniSpecialista).count();

        System.out.println("Počet datových analytiků: " + analytici);
        System.out.println("Počet bezpečnostních specialistů: " + specialiste);
    }

    // l) Načtení dat z SQL při spuštění [cite: 21, 22]
    public void nactiZSQL() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Načtení zaměstnanců
            ResultSet rsZam = conn.createStatement().executeQuery("SELECT * FROM zamestnanci");
            int maxId = 0;
            while (rsZam.next()) {
                int id = rsZam.getInt("id");
                String typ = rsZam.getString("typ");
                String jm = rsZam.getString("jmeno");
                String pr = rsZam.getString("prijmeni");
                int rok = rsZam.getInt("rok_narozeni");

                Zamestnanec z = typ.equals("A") 
                    ? new DatovyAnalytik(id, jm, pr, rok) 
                    : new BezpecnostniSpecialista(id, jm, pr, rok);
                
                databaze.put(id, z);
                if (id >= maxId) maxId = id + 1;
            }
            idCounter = maxId;

            // 2. Načtení vazeb
            ResultSet rsVaz = conn.createStatement().executeQuery("SELECT * FROM vazby");
            while (rsVaz.next()) {
                int id1 = rsVaz.getInt("id_zam1");
                int id2 = rsVaz.getInt("id_zam2");
                Kvalita k = Kvalita.valueOf(rsVaz.getString("kvalita"));
                if (databaze.containsKey(id1) && databaze.containsKey(id2)) {
                    databaze.get(id1).pridejVazbu(id2, k);
                    databaze.get(id2).pridejVazbu(id1, k);
                }
            }
            System.out.println("Data úspěšně načtena z SQL.");
        } catch (SQLException e) {
            System.out.println("SQL Načítání selhalo (pracuji bez DB): " + e.getMessage());
        }
    }

    // k) Uložení všech dat do SQL při ukončení [cite: 20]
    public void ulozDoSQL() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.createStatement().executeUpdate("DELETE FROM vazby");
            conn.createStatement().executeUpdate("DELETE FROM zamestnanci");

            PreparedStatement psZam = conn.prepareStatement("INSERT INTO zamestnanci VALUES (?, ?, ?, ?, ?)");
            PreparedStatement psVaz = conn.prepareStatement("INSERT INTO vazby VALUES (?, ?, ?)");

            for (Zamestnanec z : databaze.values()) {
                psZam.setInt(1, z.getId());
                psZam.setString(2, z.getJmeno());
                psZam.setString(3, z.getPrijmeni());
                psZam.setInt(4, z.getRokNarozeni());
                psZam.setString(5, z instanceof DatovyAnalytik ? "A" : "S");
                psZam.addBatch();

                for (Map.Entry<Integer, Kvalita> vazba : z.getSpolupracovnici().entrySet()) {
                    if (z.getId() < vazba.getKey()) {
                        psVaz.setInt(1, z.getId());
                        psVaz.setInt(2, vazba.getKey());
                        psVaz.setString(3, vazba.getValue().name());
                        psVaz.addBatch();
                    }
                }
            }
            psZam.executeBatch();
            psVaz.executeBatch();
            System.out.println("Data byla úspěšně zálohována do SQL.");
        } catch (SQLException e) {
            System.out.println("Chyba při ukládání do SQL: " + e.getMessage());
        }
    }

    // Pomocná metoda pro přístup k celé databázi
    public Map<Integer, Zamestnanec> getVsechny() {
        return databaze;
    }
}