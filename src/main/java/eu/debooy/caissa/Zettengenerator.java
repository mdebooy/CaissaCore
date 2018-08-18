/**

 * Copyright 2008 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/7330l5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.caissa;

import eu.debooy.caissa.exceptions.CaissaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author Marco de Booij
 */
// TODO Rochade aanpassen aan schaak960.
public class Zettengenerator {
  private List<Zet>   zetten        = new ArrayList<Zet>();
  private boolean     korteRochade  = false;
  private boolean     langeRochade  = false;
  private FEN         fen           = null;
  private int[]       bord          = new int[120];
  private int         enPassant     = 0;
  private int         koning;
  private int         schaakDoel;
  private int[]       loop          = {9, 11, -9, -11, 10, 1, -1, -10,
                                       19, 21, 12, 8, -21, -19, -12, -8};

  public Zettengenerator(FEN fen) {
    this.fen    = fen;
    bord        = fen.getBord();
    String  ep  = fen.getEnPassant();
    if (!"-".equals(ep)) {
      enPassant = CaissaUtils.externToIntern(ep);
    }
    if (fen.getAanZet() == 'w') {
      korteRochade  = fen.getWitKorteRochade();
      langeRochade  = fen.getWitLangeRochade();
    } else {
      korteRochade  = fen.getZwartKorteRochade();
      langeRochade  = fen.getZwartLangeRochade();
      bordwissel();
      if (enPassant != 0) {
        enPassant = 110 - (enPassant/10) * 10 + enPassant%10;
      }
    }

    // Zoek de koning.
    int i       = 21;
    koning      = 0;
    schaakDoel  = 0;
    while ((koning == 0
        || schaakDoel == 0)
        && i < 99) {
      if (bord[i] == CaissaConstants.KONING) {
        koning      = i;
      }
      if (bord[i] == -6) {
        schaakDoel  = i;
      }
      i++;
    }

    genereerZetten();
  }

  /**
   * Kijkt of het stuk 'in' (aangevallen) staat.
   * @param stuk
   * @return
   */
  private boolean aangevallen (int stuk) {
    // Aangevallen door pion?
    if (bord[stuk +  9] == -1) {
      return true;
    }
    if (bord[stuk + 11] == -1) {
      return true;
    }
    // Aangevallen door koning?
    for (int i = 0; i < 8; i++) {
      if (bord[stuk + loop[i]] == -6) {
        return true;
      }
    }
    // Aangevallen door paard?
    for (int i = 8; i < 16; i++) {
      if (bord[stuk + loop[i]] == -2) {
        return true;
      }
    }
    int naarVeld;
    // Aangevallen door loper of dame?
    for (int i = 0; i < 4; i++) {
      naarVeld  = stuk + loop[i];
      while (bord[naarVeld] == 0) {
        naarVeld  = naarVeld + loop[i];
      }
      if (bord[naarVeld] == -3
          || bord[naarVeld] == -5) {
        return true;
      }
    }
    // Aangevallen door toren of dame?
    for (int i = 4; i < 8; i++) {
      naarVeld  = stuk + loop[i];
      while (bord[naarVeld] == 0) {
        naarVeld  = naarVeld + loop[i];
      }
      if (bord[naarVeld] == -4
          || bord[naarVeld] == -5) {
        return true;
      }
    }
    return false;
  }

  /**
   * Voeg de zet toe aan de ArrayList van zetten.
   * @param stuk
   * @param vanVeld
   * @param naarVeld
   * @param stukNaar
   */
  private void addZet(char stuk, int vanVeld, int naarVeld, int stukNaar) {
    addZet(stuk, vanVeld, naarVeld, stukNaar, ' ');
  }

  /**
   * Voeg de zet toe aan de ArrayList van zetten.
   * @param stuk
   * @param vanVeld
   * @param naarVeld
   * @param stukNaar
   * @param promotieStuk
   */
  private void addZet(char stuk, int vanVeld, int naarVeld, int stukNaar,
                      char promotieStuk) {
    int van   = vanVeld;
    int naar  = naarVeld;
    if (fen.getAanZet() == 'b') {
      van   = 110 - (vanVeld/10)  * 10 + vanVeld%10;
      naar  = 110 - (naarVeld/10) * 10 + naarVeld%10;
    }
    Zet zet = new Zet(stuk, van, naar, promotieStuk);
    if (stukNaar < 0) {
      zet.setSlagzet(true);
    }
    if (naarVeld == enPassant
        && stuk == ' ') {
      zet.setEp(true);
    }
    if (promotieStuk != ' ') {
      bord[naarVeld] = CaissaConstants.NOTATIE_STUKKEN.indexOf(promotieStuk)+1;
    }
    // Verzet bij rochade ook de toren.
    if (stuk == 'K') {
      if (vanVeld - naarVeld == -2) {
        bord[vanVeld+1] = bord[vanVeld+3];
        bord[vanVeld+3] = 0;
      }
      if (vanVeld - naarVeld == 2) {
        bord[vanVeld-1] = bord[vanVeld-4];
        bord[vanVeld-4] = 0;
      }
    }
    if (zetSchaak()) {
      zet.setSchaak(true);
    }
    // Zet bij rochade de toren terug.
    if (stuk == 'K') {
      if (vanVeld - naarVeld == -2) {
        bord[vanVeld+3] = bord[vanVeld+1];
        bord[vanVeld+1] = 0;
      }
      if (vanVeld - naarVeld == 2) {
        bord[vanVeld-4] = bord[vanVeld-1];
        bord[vanVeld-1] = 0;
      }
    }
    if (promotieStuk != ' ') {
      bord[naarVeld] = CaissaConstants.PION;
    }
    zetten.add(zet);
  }

  /**
   * Zet het bord onderste boven en verander de stukken van kleur.
   */
  private void bordwissel() {
    for (int i = 2; i < 6; i++) {
      for (int j = 1; j < 9; j++) {
        int hulp    = i * 10;
        int van     = hulp + j;
        int naar    = 110 - hulp + j;
        hulp        = bord[van];
        bord[van]   = -bord[naar];
        bord[naar]  = -hulp;
      }
    }
  }

  /**
   * Doe de zetten voor een dame.
   * @param veld
   */
  private void dameZet(int veld) {
    for (int i = 0; i < 8; i++) {
      ltdZet('Q', veld, i);
    }
  }

  /**
   * Genereer alle mogelijke zetten.
   */
  private void genereerZetten() {
    for (int i = 21; i < 99; i++) {
      switch (bord[i]) {
      case CaissaConstants.PION:
        pionZet(i);
        break;
      case CaissaConstants.PAARD:
        paardZet(i);
        break;
      case CaissaConstants.LOPER:
        loperZet(i);
        break;
      case CaissaConstants.TOREN:
        torenZet(i);
        break;
      case CaissaConstants.DAME:
        dameZet(i);
        break;
      case CaissaConstants.KONING:
        koningZet(i);
        break;
      default:
        break;
      }
    }

    if (korteRochade || langeRochade) {
      rochade();
    }

    Collections.sort(zetten);
    for (int i = 0; i < zetten.size()-1; i++) {
      Zet zet1  = zetten.get(i);
      Zet zet2  = zetten.get(i+1);
      // Kijk niet naar pion zetten. Enkel bij andere stukken kunnen er 2 naar
      // hetzelfde veld gaan. Uitgezonderd bij een slagzet van een pion maar
      // die zijn door de normale notatie al 'uniek'.
      if (zet1.getStuk() != ' '
          && zet1.getNaar() == zet2.getNaar()
          && zet1.getStuk() == zet2.getStuk()) {
        if (CaissaUtils.internToExtern(zet1.getVan()).charAt(0) !=
            CaissaUtils.internToExtern(zet2.getVan()).charAt(0)) {
          zet1.setKorteNotatieLevel(1);
          zet2.setKorteNotatieLevel(1);
        } else {
          zet1.setKorteNotatieLevel(2);
          zet2.setKorteNotatieLevel(2);
        }
        zetten.remove(i);
        zetten.add(i, zet1);
        zetten.remove(i+1);
        zetten.add(i+1, zet2);
      }
    }
  }

  /**
   * Geeft het aantal mogelijke zetten in de stelling.
   * @return Het aantal mogelijke zetten in de stelling.
   */
  public int getAantalZetten() {
    return zetten.size();
  }

  /**
   * Maak een lijst stellingen in 'FEN' notatie.
   * @return Een lijst stellingen in 'FEN' notatie.
   * @throws CaissaException 
   */
  public List<String> getNieuweStellingen() throws CaissaException {
    List<String>  stellingen  = new ArrayList<String>();

    for (Zet  zet: zetten) {
      FEN nieuweFen = new FEN(fen.getFen());
      nieuweFen.doeZet(zet);
      stellingen.add(nieuweFen.getFen());
    }

    return stellingen;
  }

  public List<Zet> getZetten() {
    Map<String, Zet>  zetMap  = new HashMap<String, Zet>();
    for (Zet zet: zetten) {
      zetMap.put(zet.getZet(), zet);
    }

    return new LinkedList<Zet>(zetMap.values());
  }

  /**
   * Doe de zetten voor een koning.
   * @param veld
   */
  private void koningZet(int veld) {
    for (int i = 0; i < 8; i++) {
      if (bord[veld + loop[i]] < 1) {
        int stukNaar  = zetHeen(veld, (veld + loop[i]));
        if (!aangevallen(veld + loop[i])) {
          addZet('K', veld, veld + loop[i], stukNaar);
        }
        zetTerug(veld, veld + loop[i], stukNaar);
      }
    }
  }

  /**
   * Doe de zetten voor een loper.
   * @param veld
   */
  private void loperZet(int veld) {
   for (int i = 0; i < 4; i++) {
      ltdZet('B', veld, i);
    }
  }

  /**
   * Doe alle zetten in dezelfde 'richting'. Dit kan enkel voor loper, toren of
   * dame.
   * @param stuk
   * @param van
   * @param index
   */
  private void ltdZet(char stuk, int van, int index) {
    int naar = van + loop[index];
    while (bord[naar] == 0) {
      zetHeen(van, naar);
      if (!aangevallen(koning)) {
        addZet(stuk, van, naar, 0);
      }
      zetTerug(van, naar, 0);
      naar = naar + loop[index];
    }
    // Stuk te slaan?
    if (bord[naar] < 0) {
      int stukNaar  = zetHeen(van, naar);
      if (!aangevallen(koning)) {
        addZet(stuk, van, naar, stukNaar);
      }
      zetTerug(van, naar, stukNaar);
    }
  }

  /**
   * Doe de zetten voor een paard.
   * @param veld
   */
  private void paardZet(int veld) {
    for (int i = 8; i < 16; i++) {
      if (bord[veld + loop[i]] < 1) {
        int stukNaar  = zetHeen(veld, (veld + loop[i]));
        if (!aangevallen(koning)) {
          addZet('N', veld, veld + loop[i], stukNaar);
        }
        zetTerug(veld, (veld + loop[i]), stukNaar);
      }
    }
  }

  /**
   * Doe de zetten voor een pion.
   * @param veld
   */
  private void pionZet(int veld) {
    if (bord[veld + 10] == 0) {
      zetHeen(veld, (veld + 10));
      if (!aangevallen(koning)) {
        if ((veld + 10) > 90) {
          // Promotie
          addZet(' ', veld, veld + 10, 0, 'Q');
          addZet(' ', veld, veld + 10, 0, 'R');
          addZet(' ', veld, veld + 10, 0, 'N');
          addZet(' ', veld, veld + 10, 0, 'B');
        } else {
          addZet(' ', veld, veld + 10, 0);
        }
      }
      if (veld < 40
          && bord[veld + 20] == 0) {
        zetHeen((veld + 10), (veld + 20));
        if (!aangevallen(koning)) {
          addZet(' ', veld, veld + 20, 0);
        }
        zetTerug((veld + 10), (veld + 20), 0);
      }
      zetTerug(veld, (veld + 10), 0);
    }
    // Stuk te slaan?
    if (bord[veld + 9] < 0) {
      int stukNaar  = zetHeen(veld, (veld + 9));
      if (!aangevallen(koning)) {
        if ((veld + 9) > 90) {
          // Promotie
          addZet(' ', veld, veld + 9, stukNaar, 'Q');
          addZet(' ', veld, veld + 9, stukNaar, 'R');
          addZet(' ', veld, veld + 9, stukNaar, 'N');
          addZet(' ', veld, veld + 9, stukNaar, 'B');
        } else {
          addZet(' ', veld, veld + 9, stukNaar);
        }
      }
      zetTerug(veld, (veld + 9), stukNaar);
    }
    // Stuk te slaan?
    if (bord[veld + 11] < 0) {
      int stukNaar  = zetHeen(veld, (veld + 11));
      if (!aangevallen(koning)) {
        if ((veld + 11) > 90) {
          // Promotie
          addZet(' ', veld, veld + 11, stukNaar, 'Q');
          addZet(' ', veld, veld + 11, stukNaar, 'R');
          addZet(' ', veld, veld + 11, stukNaar, 'N');
          addZet(' ', veld, veld + 11, stukNaar, 'B');
        } else {
          addZet(' ', veld, veld + 11, stukNaar);
        }
      }
      zetTerug(veld, (veld + 11), stukNaar);
    }
    // Stuk En-Passant te slaan?
    if ((veld + 9) == enPassant
        || (veld + 11) == enPassant) {
      int stukNaar  = zetHeen(veld, enPassant);
      // Haal geslagen pion weg.
      bord[enPassant - 10]  = 0;
      if (!aangevallen(koning)) {
        addZet(' ', veld, enPassant, bord[enPassant]);
      }
      zetTerug(veld, enPassant, stukNaar);
      // Zet geslagen pion terug.
      bord[enPassant - 10]  = -1;
    }
  }

  /**
   * Doe de mogelijke rochades.
   */
  private void rochade() {
    if (bord[25] == CaissaConstants.KONING
        && bord[26] == 0
        && bord[27] == 0
        && bord[28] == CaissaConstants.TOREN
        && korteRochade) {
      if (!aangevallen(25)
          && !aangevallen(26)
          && !aangevallen(27)) {
        addZet('K', 25, 27, 0);
      }
    }
    if (bord[25] == CaissaConstants.KONING
        && bord[24] == 0
        && bord[23] == 0
        && bord[22] == 0
        && bord[21] == CaissaConstants.TOREN
        && langeRochade) {
      if (!aangevallen(25)
          && !aangevallen(24)
          && !aangevallen(23)) {
        addZet('K', 25, 23, 0);
      }
    }
  }

  /**
   * Doe de zetten voor een toren.
   * @param veld
   */
  private void torenZet(int veld) {
    for (int i = 4; i < 8; i++) {
      ltdZet('R', veld, i);
    }
  }

  /**
   * Doe een zet. Geeft de waarde van het 'naar' veld terug om de zet te kunnen
   * 'terugdraaien'.
   * @param van
   * @param naar
   * @return De waarde van het stuk op het 'van' veld.
   */
  private int zetHeen(int van, int naar) {
    int stukNaar  = bord[naar];
    bord[naar]    = bord[van];
    bord[van]     = 0;

    return stukNaar;
  }

  /**
   * @return Staat de 'vijandelijke' koning schaak?
   */
  private boolean zetSchaak() {
    // Schaak door pion?
    if (bord[schaakDoel -  9] == CaissaConstants.PION) {
      return true;
    }
    if (bord[schaakDoel - 11] == CaissaConstants.PION) {
      return true;
    }
    // Schaak door paard?
    for (int i = 8; i < 16; i++) {
      if (bord[schaakDoel + loop[i]] == CaissaConstants.PAARD) {
        return true;
      }
    }
    int naarVeld;
    // Schaak door loper of dame?
    for (int i = 0; i < 4; i++) {
      naarVeld  = schaakDoel + loop[i];
      while (bord[naarVeld] == 0) {
        naarVeld  = naarVeld + loop[i];
      }
      if (bord[naarVeld] == CaissaConstants.LOPER
          || bord[naarVeld] == CaissaConstants.DAME) {
        return true;
      }
    }
    // Schaak door toren of dame?
    for (int i = 4; i < 8; i++) {
      naarVeld  = schaakDoel + loop[i];
      while (bord[naarVeld] == 0) {
        naarVeld  = naarVeld + loop[i];
      }
      if (bord[naarVeld] == CaissaConstants.TOREN
          || bord[naarVeld] == CaissaConstants.DAME) {
        return true;
      }
    }
    return false;
 }

  /**
   * Neem een zet terug.
   * @param van
   * @param naar
   * @param stukNaar
   */
  private void zetTerug(int van, int naar, int stukNaar) {
    bord[van]   = bord[naar];
    bord[naar]  = stukNaar;
  }
}
