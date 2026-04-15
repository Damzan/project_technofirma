package projektt;

import java.util.Map;

public class BezpecnostniSpecialista extends Zamestnanec {
    public BezpecnostniSpecialista(int id, String jm, String pr, int rok) {
        super(id, jm, pr, rok);
    }

    @Override
    public void provedDovednost(Map<Integer, Zamestnanec> vsichni) {
        if (spolupracovnici.isEmpty()) {
            System.out.println("Riziko: 0 (žádné vazby)");
            return;
        }
        
        double skore = spolupracovnici.values().stream()
            .mapToDouble(k -> k == Kvalita.SPATNA ? 10 : (k == Kvalita.PRUMERNA ? 5 : 0))
            .sum() / spolupracovnici.size();
            
        System.out.println("Vypočtené rizikové skóre specialisty " + getPrijmeni() + ": " + skore);
    }
}
