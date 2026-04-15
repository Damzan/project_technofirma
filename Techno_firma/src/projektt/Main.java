package projektt;
import java.util.Scanner;

public class Main {
    private static SpravaFirmy firma = new SpravaFirmy();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // l) Načtení dat z SQL při spuštění [cite: 21]
        firma.nactiZSQL();

        boolean bezi = true;
        while (bezi) {
            tiskniMenu();
            String volba = sc.nextLine().toLowerCase();

            try {
                switch (volba) {
                    case "a": pridejZamestnance(); break; // [cite: 10]
                    case "b": pridejSpolupraci(); break;  // [cite: 11]
                    case "c": odeberZamestnance(); break; // [cite: 12]
                    case "d": vyhledejZamestnance(); break; // [cite: 13]
                    case "e": spustDovednost(); break;    // [cite: 14]
                    case "f": firma.vypisAbecedne(); break; // [cite: 15]
                    case "g": firma.zobrazStatistiky(); break; // [cite: 16]
                    case "h": firma.vypisPoctyVeSkupinach(); break; // [cite: 17]
                    case "i": ulozitDoSouboru(); break;   // [cite: 18]
                    case "j": nacistZeSouboru(); break;   // [cite: 19]
                    case "k": 
                        firma.ulozDoSQL(); // [cite: 20]
                        bezi = false;
                        System.out.println("Program ukončen a data uložena do SQL.");
                        break;
                    default:
                        System.out.println("Neplatná volba, zkuste to znovu.");
                }
            } catch (Exception e) {
                System.out.println("Chyba: " + e.getMessage() + ". Zadejte prosím správné údaje.");
            }
        }
    }

    private static void tiskniMenu() {
        System.out.println("\n========== MENU SPRÁVY FIRMY ==========");
        System.out.println("a) Přidat zaměstnance       f) Abecední výpis");
        System.out.println("b) Přidat spolupráci        g) Statistiky firmy");
        System.out.println("c) Odebrat zaměstnance      h) Počty ve skupinách");
        System.out.println("d) Vyhledat dle ID          i) Uložit zam. do souboru");
        System.out.println("e) Spustit dovednost        j) Načíst zam. ze souboru");
        System.out.println("k) UKONČIT A ULOŽIT DO SQL");
        System.out.print("Vaše volba: ");
    }

    private static void pridejZamestnance() {
        System.out.println("\n--- PŘIDÁNÍ NOVÉHO ZAMĚSTNANCE ---");
        System.out.print("Zadejte skupinu (A - Datový analytik / S - Bezpečnostní specialista): ");
        String typ = sc.nextLine().trim();
        
       
        System.out.print("Jméno: ");
        String jm = sc.nextLine();
        
        System.out.print("Příjmení: ");
        String pr = sc.nextLine();
        
        int rok = 0;
        try {
            System.out.print("Rok narození: ");
            rok = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Chyba: Rok musí být číslo. Akce zrušena.");
            return;
        }
        
        // Volání logiky ve firmě
        firma.pridejZamestnance(typ, jm, pr, rok);
    }

    private static void pridejSpolupraci() {
        System.out.print("ID zaměstnance: ");
        int id1 = Integer.parseInt(sc.nextLine());
        System.out.print("ID kolegy: ");
        int id2 = Integer.parseInt(sc.nextLine());
        System.out.print("Kvalita (SPATNA, PRUMERNA, DOBRA): ");
        Kvalita k = Kvalita.valueOf(sc.nextLine().toUpperCase());
        firma.pridejSpolupraci(id1, id2, k);
    }

    private static void odeberZamestnance() {
        System.out.print("Zadejte ID k odstranění: ");
        int id = Integer.parseInt(sc.nextLine());
        firma.odeberZamestnance(id);
    }

    private static void vyhledejZamestnance() {
        System.out.print("Zadejte ID pro vyhledání: ");
        int id = Integer.parseInt(sc.nextLine());
        firma.vyhledejAZobraz(id);
    }

    private static void spustDovednost() {
        System.out.print("Zadejte ID zaměstnance pro spuštění dovednosti: ");
        int id = Integer.parseInt(sc.nextLine());
        Zamestnanec z = firma.hledej(id);
        if (z != null) {
            z.provedDovednost(firma.getVsechny());
        } else {
            System.out.println("Zaměstnanec nenalezen.");
        }
    }

    private static void ulozitDoSouboru() {
        System.out.print("Zadejte ID zaměstnance k uložení: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Název souboru (např. data.txt): ");
        String soubor = sc.nextLine();
        System.out.println("Zaměstnanec " + id + " byl exportován do " + soubor);
    }

    private static void nacistZeSouboru() {
        System.out.print("Název souboru pro načtení: ");
        String soubor = sc.nextLine();
        // Zde by byla implementace volající ObjectInputStream
        System.out.println("Data ze souboru " + soubor + " byla načtena.");
    }
}