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

import eu.debooy.caissa.exceptions.FenException;
import eu.debooy.doosutils.DoosConstants;

import java.io.Serializable;


/**
 * Deze class bevat een stelling in FEN notatie.
 * 
 * Volgens Wikipedia:
 * 
 * Een FEN 'record' definieert een partijstelling in één regel met uitsluitend
 * ASCII karakters.
 *
 * Een FEN record bestaat uit 6 velden. De velden worden gescheiden door een
 * spatie. De velden zijn:
 * 1. Positie van de stukken( gezien vanuit wit ). Elke rij wordt beschreven,
 *    beginnend met de achtste rij en eindigend met de eerste rij. De rijen
 *    worden gescheiden door een forward slash("/"). Binnen elke rij wordt de
 *    inhoud van elk veld beschreven van de a-lijn tot de h-lijn. Witte stukken
 *    worden aangegeven met hun Engelse aanduiding in hoofdleters("KQRBNP"),
 *    zwarte stukken met diezelfde aanduiding in kleine letters("kqrbnp"). Het
 *    aantal opeenvolgende lege velden op een rij wordt aangegeven met een
 *    nummer dat van "1" tot en met "8" kan lopen.
 * 2. Actieve kleur. "w" betekent wit aan zet, "b" betekent zwart aan zet.
 * 3. Rokademogelijkheid. "-" geeft aan dat geen rokade meer mogelijk is. Anders
 *    wordt één of meer van de volgende karakters gebruikt, in deze volgorde:
 *    "K"( wit kan op de koningsvleugel rokeren), "Q"( wit kan op de damevleugel
 *    rokeren ), "k"( zwart kan op de koningsvleugel rokeren ), "q"( zwart kan
 *    op de damevleugel rokeren).
 * 4. En passant veld. "-" geeft aan dat er niet en passant kan worden geslagen.
 *    Als een pion twee velden opschuift wordt het veld na de pion aangegeven.
 *    Bijvoorbeeld na de zet e2-e4 wordt "e3" aangegeven. Merk op dat dit
 *    gebeurt ook als er geen pion van de andere partij is die en passant kan
 *    slaan.
 * 5. Halve zetten teller. Geeft het aantal zetten aan sinds de laatste zet
 *    waarbij een pion is gezet of een stuk geslagen. Dit wordt gebruikt om te
 *    bepalen of een remiseclaim mogelijk is in verband met de 'vijftig zetten'
 *    regel.
 * 6. Volledige zetten teller. Het aantal volledige zetten. Dit begint met "1"
 *    en wordt opgehoogd na elke zet van zwart.
 *
 * @author Marco de Booij
 */
//TODO Aanpassen aan schaak960 ivm rochade.
public class FEN implements Serializable {
  private static final long serialVersionUID = 1L;

  private Boolean witKorteRochade   = true;
  private Boolean witLangeRochade   = true;
  private Boolean zwartKorteRochade = true;
  private Boolean zwartLangeRochade = true;
  private char    aanZet            = 'w';
  private int[]   bord              = new int[120];
  private Integer halvezetten       = 0;
  private Integer zetnummer         = 1;
  private String  enPassant         = "-";
  private String  positie           =
    "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
  private char[]  stuk              = {'k', 'q', 'r', 'b', 'n', 'p', '.',
                                       'P', 'N', 'B', 'R', 'Q', 'K'};

  public FEN() {
    positieToBord();
  }

  public FEN(String fen) throws FenException {
    String[]  veld  = fen.split(" ");

    this.positie      = veld[0];
    positieToBord();
    this.aanZet       = veld[1].charAt(0);
    setRochade(veld[2]);
    this.enPassant    = veld[3];
    this.halvezetten  = Integer.valueOf(veld[4]);
    this.zetnummer    = Integer.valueOf(veld[5]);
  }

  private void bordToPositie() {
    StringBuilder nieuwePositie = new StringBuilder();
    for (int i = 9; i > 1; i--) {
      int leeg  = 0;
      for (int j = 1; j < 9; j++) {
        if (bord[i*10+j] == 0) {
          leeg++;
        } else {
          if (leeg > 0) {
            nieuwePositie.append("").append(leeg);
            leeg  = 0;
          }
          nieuwePositie.append(stuk[bord[i*10+j] + 6]);
        }
      }
      if (leeg > 0) {
        nieuwePositie.append("").append(leeg);
      }
      if (i > 2) {
        nieuwePositie.append("/");
      }
    }
    positie = nieuwePositie.toString();
  }

  public void doeZet(Zet zet) throws FenException {
    int veldVan   = zet.getVan();
    int veldNaar  = zet.getNaar();
    int stukVan   = bord[veldVan];
    int stukNaar  = bord[veldNaar];
    if (stukNaar != 0
        || Math.abs(stukVan) == CaissaConstants.PION) {
      halvezetten = 0;
    } else {
      halvezetten++;
    }

    if (zet.getPromotieStuk() != ' ') {
      stukVan = zoekStuk(zet.getPromotieStuk());
      if (stukVan < 0) {
        throw new FenException("Promotiestuk " + zet.getPromotieStuk()
                               + " onbekend.");
      }
      stukVan -= 6;
      if (aanZet == 'b') {
        stukVan *= -1;
      }
    }
    bord[veldVan]  = 0;
    bord[veldNaar] = stukVan;
    if (!"-".equals(enPassant)
        && veldNaar == CaissaUtils.externToIntern(enPassant)
        && Math.abs(stukVan) == CaissaConstants.PION) {
      if ((veldVan-veldNaar) == -9
          || (veldVan-veldNaar) == 11) {
        bord[veldVan-1] = 0;
      } else {
        bord[veldVan+1] = 0;
      }
    }
    bordToPositie();

    if (aanZet == 'w') {
      aanZet  = 'b';
      if (veldVan == 21
          && stukVan == CaissaConstants.TOREN) {
        witLangeRochade = false;
      }
      if (veldVan == 25
          && stukVan == CaissaConstants.KONING) {
        witKorteRochade = false;
        witLangeRochade = false;
      }
      if (veldVan == 28
          && stukVan == CaissaConstants.TOREN) {
        witKorteRochade = false;
      }
      // Korte rochade
      if (stukVan == CaissaConstants.KONING
          && (veldVan - veldNaar) == -2) {
        bord[26]  = CaissaConstants.TOREN;
        bord[28]  = 0;
      }
      // Lange rochade
      if (stukVan == CaissaConstants.KONING
          && (veldVan - veldNaar) == 2) {
        bord[24]  = CaissaConstants.TOREN;
        bord[21]  = 0;
      }
      if (stukVan == CaissaConstants.PION
          && (veldNaar - veldVan) == 20) {
        enPassant = CaissaUtils.internToExtern(veldNaar-10);
      } else {
        enPassant = "-";
      }
    } else {
      aanZet  = 'w';
      if (veldVan == 91
          && stukVan == CaissaConstants.ZTOREN) {
        zwartLangeRochade = false;
      }
      if (veldVan == 95
          && stukVan == CaissaConstants.ZKONING) {
        zwartKorteRochade = false;
        zwartLangeRochade = false;
      }
      if (veldVan == 98
          && stukVan == CaissaConstants.ZTOREN) {
        zwartKorteRochade = false;
      }
      // Korte rochade
      if (stukVan == CaissaConstants.ZKONING
          && (veldVan - veldNaar) == -2) {
        bord[96]  = CaissaConstants.ZTOREN;
        bord[98]  = 0;
      }
      // Lange rochade
      if (stukVan == CaissaConstants.ZKONING
          && (veldVan - veldNaar) == 2) {
        bord[94]  = CaissaConstants.ZTOREN;
        bord[91]  = 0;
      }
      if (stukVan == CaissaConstants.ZPION
          && (veldVan - veldNaar) == 20) {
        enPassant = CaissaUtils.internToExtern(veldNaar+10);
      } else {
        enPassant = "-";
      }
      zetnummer++;
    }
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof FEN)) {
      return false;
    }

    final FEN other = (FEN) obj;
    return getFen().equals(other.getFen());
  }

  //TODO Werk uit.
  public Zet geefZet(FEN fen) {
    return null;
  }

  /**
   * @return the aanZet
   */
  public char getAanZet() {
    return aanZet;
  }

  /**
   * @return the bord
   */
  public int[] getBord() {
    return bord.clone();
  }

  /**
   * @return the enPassant
   */
  public String getEnPassant() {
    return enPassant;
  }

  public String getFen() {
    StringBuilder fen = new StringBuilder();

    fen.append(getPositie()).append(" ");
    fen.append(getAanZet()).append(" ");
    fen.append(getRochade()).append(" ");
    fen.append(getEnPassant()).append(" ");
    fen.append(getHalvezetten().toString()).append(" ");
    fen.append(getZetnummer().toString());

    return fen.toString();
  }

  /**
   * @return the halvezetten
   */
  public Integer getHalvezetten() {
    return halvezetten;
  }

  public String getKortePositie() {
    StringBuilder kortePositie = new StringBuilder();
    int leeg  = 0;
    for (int i = 9; i > 1; i--) {
      for (int j = 1; j < 9; j++) {
        if (bord[i*10+j] == 0) {
          leeg++;
        } else {
          if (leeg > 0) {
            kortePositie.append("").append(leeg);
            leeg  = 0;
          }
          kortePositie.append(stuk[bord[i*10+j] + 6]);
        }
      }
    }
    if (leeg > 0) {
      kortePositie.append("").append(leeg);
    }

    return kortePositie.toString();
  }

  /**
   * @return the positie
   */
  public String getPositie() {
    return positie;
  }

  /**
   * @return the rochade
   */
  protected String getRochade() {
    StringBuilder rochade = new StringBuilder();

    if (witKorteRochade) {
      rochade.append("K");
    }
    if (witLangeRochade) {
      rochade.append("Q");
    }
    if (zwartKorteRochade) {
      rochade.append("k");
    }
    if (zwartLangeRochade) {
      rochade.append("q");
    }
    if (rochade.length() == 0) {
      rochade.append('-');
    }

    return rochade.toString();
  }

  /**
   * @return the witKorteRochade
   */
  public Boolean getWitKorteRochade() {
    return witKorteRochade;
  }

  /**
   * @return the witLangeRochade
   */
  public Boolean getWitLangeRochade() {
    return witLangeRochade;
  }

  /**
   * @return the zetnummer
   */
  public Integer getZetnummer() {
    return zetnummer;
  }

  /**
   * @return the zwartKorteRochade
   */
  public Boolean getZwartKorteRochade() {
    return zwartKorteRochade;
  }

  /**
   * @return the zwartLangeRochade
   */
  public Boolean getZwartLangeRochade() {
    return zwartLangeRochade;
  }

  public int hashCode() {
    return getFen().hashCode();
  }

  private void positieToBord() {
    for (int i = 0; i<120; i++) {
      bord[i]  = 7;
    }

    for (int i = 2; i < 10; i++) {
      for (int j = 1; j < 9; j++) {
        bord[i*10+j]  = 0;
      }
    }
    
    String[]  rij = positie.split("/");
    for (int i = 0; i < 8; i++) {
      int kolom = 1;
      for (int j = 0; j < rij[i].length(); j++) {
        char  ch  = rij[i].charAt(j);
        if (CaissaConstants.STUKKEN.toLowerCase().indexOf(ch) > -1) {
          bord[(9-i)*10+kolom]  = (CaissaConstants.STUKKEN
                                                  .toLowerCase()
                                                  .indexOf(ch) + 1) * -1;
        } else if (CaissaConstants.STUKKEN.indexOf(ch) > -1) {
            bord[(9-i)*10+kolom]  = CaissaConstants.STUKKEN.indexOf(ch) + 1;
        } else if ("12345678".indexOf(ch) > -1) {
          kolom  += "12345678".indexOf(ch);
        }
        kolom++;
      }
    }
  }

  /**
   * Methode om het interne bord in 'leesbare' vorm te laten zien.
   * 
   * @return intern bord in 'leesbare' vorm.
   */
  public String printBord() {
    StringBuilder internBord  = new StringBuilder();

    for (int i = 9; i > 1; i--) {
      internBord.append((i - 1)).append(" ");
      for (int j = 1; j < 9; j++) {
        internBord.append(stuk[bord[(i * 10 + j)] + 6]);
      }
      internBord.append(DoosConstants.EOL);
    }
    internBord.append("  ABCDEFGH").append(DoosConstants.EOL);

    return internBord.toString();
  }

  /**
   * @param aanZet the aanZet to set
   * @throws FenException 
   */
  public void setAanZet(char aanZet) throws FenException {
    if ("bw".indexOf(aanZet) < 0) {
      throw new FenException("AanZet niet 'b' of 'w'");
    }

    this.aanZet = aanZet;
  }
  public void setAanZet(String aanZet) throws FenException {
    if (aanZet.length() != 1) {
      throw new FenException("AanZet geen char");
    }

    setAanZet(aanZet.charAt(0));
  }

  /**
   * @param enPassant the enPassant to set
   * @throws FenException 
   */
  public void setEnPassant(String enPassant) throws FenException {
    if ("-".equals(enPassant)) {
      this.enPassant = enPassant;
      return;
    }

    if ("abcdefgh".indexOf(enPassant.charAt(0)) < 0) {
      throw new FenException("EnPassant foutief [" + enPassant + "]");
    }
    if (aanZet == 'b'
        && enPassant.charAt(1) != '3') {
      throw new FenException("EnPassant foutief [" + enPassant + "]");
    }
    if (aanZet == 'w'
        && enPassant.charAt(1) != '6') {
      throw new FenException("EnPassant foutief [" + enPassant + "]");
    }
    // TODO test of het wel een e.p. pion is.

    this.enPassant = enPassant;
  }

  public void setFen(String fen) throws FenException {
    String[]  veld  = fen.split(" ");

    setPositie(veld[0]);
    setAanZet(veld[1]);
    setRochade(veld[2]);
    setEnPassant(veld[3]);
    setHalvezetten(Integer.valueOf(veld[4]));
    setZetnummer(Integer.valueOf(veld[5]));
  }

  /**
   * @param halvezetten the halvezetten to set
   */
  public void setHalvezetten(Integer halvezetten) {
    this.halvezetten = halvezetten;
  }

  /**
   * @param positie the positie to set
   */
  public void setPositie(String positie) {
    this.positie  = positie;
    positieToBord();
  }

  private void setRochade(String rochade) {
    // TODO Test de juiste volgorde.
    if (rochade.indexOf('K') != -1) {
      witKorteRochade   = true;
    } else {
      witKorteRochade   = false;
    }
    if (rochade.indexOf('Q') != -1) {
      witLangeRochade   = true;
    } else {
      witLangeRochade   = false;
    }
    if (rochade.indexOf('k') != -1) {
      zwartKorteRochade = true;
    } else {
      zwartKorteRochade = false;
    }
    if (rochade.indexOf('q') != -1) {
      zwartLangeRochade = true;
    } else {
      zwartLangeRochade = false;
    }
  }

  /**
   * @param witKorteRochade the witKorteRochade to set
   */
  public void setWitKorteRochade(Boolean witKorteRochade) {
    this.witKorteRochade = witKorteRochade;
  }

  /**
   * @param witLangeRochade the witLangeRochade to set
   */
  public void setWitLangeRochade(Boolean witLangeRochade) {
    this.witLangeRochade = witLangeRochade;
  }

  /**
   * @param zetnummer the zetnummer to set
   */
  public void setZetnummer(Integer zetnummer) {
    this.zetnummer = zetnummer;
  }

  /**
   * @param zwartKorteRochade the zwartKorteRochade to set
   */
  public void setZwartKorteRochade(Boolean zwartKorteRochade) {
    this.zwartKorteRochade = zwartKorteRochade;
  }

  /**
   * @param zwartLangeRochade the zwartLangeRochade to set
   */
  public void setZwartLangeRochade(Boolean zwartLangeRochade) {
    this.zwartLangeRochade = zwartLangeRochade;
  }

  /**
   * @param nodig te zoeken stuk
   * @return index van het stuk of -1 als het stuk niet bestaat
   */
  protected int zoekStuk(char nodig) {
    for (int i = 0; i < stuk.length; i++) {
      if (stuk[i] == nodig) {
        return i;
      }
    }

    return -1;
  }

  public String toString() {
    return getFen();
  }
}
