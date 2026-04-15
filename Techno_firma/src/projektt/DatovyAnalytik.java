package projektt;

import java.util.*;

public class DatovyAnalytik extends Zamestnanec {
    public DatovyAnalytik(int id, String jm, String pr, int rok) {
        super(id, jm, pr, rok);
    }

    @Override
    public void provedDovednost(Map<Integer, Zamestnanec> vsichni) {
        int maxSpolecnych = -1;
        Zamestnanec nejlepsiKolega = null;

        for (Zamestnanec jiny : vsichni.values()) {
            if (jiny.getId() == this.getId()) continue;
            
            Set<Integer> moji = new HashSet<>(this.spolupracovnici.keySet());
            moji.retainAll(jiny.getSpolupracovnici().keySet()); 

            if (moji.size() > maxSpolecnych) {
                maxSpolecnych = moji.size();
                nejlepsiKolega = jiny;
            }
        }
        System.out.println("Analytik " + getPrijmeni() + " zjistil: Nejvíce společných kolegů má s ID " + 
            (nejlepsiKolega != null ? nejlepsiKolega.getId() : "nikým"));
    }
}