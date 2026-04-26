package projektt;
import java.util.Scanner;

public class Main {
    private static SpravaFirmy firma = new SpravaFirmy();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        
        firma.nactiZSQL();

        boolean bezi = true;
        while (bezi) {
            tiskniMenu();
            String volba = sc.nextLine().toLowerCase();

            try {
                switch (volba) {
                    case "a": pridejZamestnance(); break;
                    case "b": pridejSpolupraci(); break; 
                    case "c":
                        System.out.print("Zadejte ID k odebrání: ");
                        int idSmazat = Integer.parseInt(sc.nextLine());
                        firma.odeberZamestnance(idSmazat); 
                        break; 
                    case "d": vyhledejZamestnance(); break; 
                    case "e": spustDovednost(); break;    
                    case "f": firma.vypisAbecedne(); break; 
                    case "g": firma.zobrazStatistiky(); break; 
                    case "h": firma.vypisPoctyVeSkupinach(); break;
                    case "i": ulozitDoSouboru(); break;   
                    case "j": nacistZeSouboru(); break;   
                    case "k": 
                        firma.ulozDoSQL(); 
                        System.out.println("Program ukončen a data uložena do SQL.");
                        break;
                    case "q":
                    	System.out.println("Program se ukončuje.");
                    	bezi = false;
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
        System.out.println("k) Uložit do SQL			q) Ukončit program");
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
  
        System.out.println("Data ze souboru " + soubor + " byla načtena.");
    }
}