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
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;


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
  private static final long serialVersionUID  = 1L;

  private static final String  BEGINSTELLING  =
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

  public  static final String  ERR_AANZET     = "fen.aanzet.incorrect";
  public  static final String  ERR_ENPASSANT  = "fen.ep.incorrect";
  public  static final String  ERR_ZET        = "fen.zet.incorrect";

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private       char    aanZet            = 'w';
  private final int[]   bord              = new int[120];
  private       String  enPassant         = "-";
  private final String  eol               = System.lineSeparator();
  private       Integer halvezetten       = 0;
  private final char    kortetoren;
  private final char    langetoren;
  private       String  positie           = BEGINSTELLING;
  private       Boolean witKorteRochade   = true;
  private       Boolean witLangeRochade   = true;
  private       Integer zetnummer         = 1;
  private       Boolean zwartKorteRochade = true;
  private       Boolean zwartLangeRochade = true;

  public FEN() {
    kortetoren  = 'h';
    langetoren  = 'a';
    positieToBord();
  }

  public FEN(String fen) {
    var veld    = fen.split(" ");

    positie     = veld[0];
    positieToBord();
    aanZet      = veld[1].charAt(0);
    setRochade(veld[2]);
    enPassant   = veld[3];
    halvezetten = Integer.valueOf(veld[4]);
    zetnummer   = Integer.valueOf(veld[5]);

    kortetoren  = 'h';
    langetoren  = 'a';
  }

  private void aanZetWit(int veldVan, int veldNaar, int stukVan) {
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
  }

  private void aanZetZwart(int veldVan, int veldNaar, int stukVan) {
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

  private void bordToPositie() {
    var nieuwePositie = new StringBuilder();
    for (var i = 9; i > 1; i--) {
      var leeg  = 0;
      for (var j = 1; j < 9; j++) {
        if (bord[i*10+j] == 0) {
          leeg++;
        } else {
          if (leeg > 0) {
            nieuwePositie.append(leeg);
            leeg  = 0;
          }
          nieuwePositie.append(CaissaUtils.getStuk(bord[i*10+j]));
        }
      }
      if (leeg > 0) {
        nieuwePositie.append(leeg);
      }
      if (i > 2) {
        nieuwePositie.append("/");
      }
    }
    positie = nieuwePositie.toString();
  }

  public void doeEnPassant(int veldVan, int veldNaar, int stukVan) {
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
  }

  public void doeZet(Zet zet) {
    var veldVan   = zet.getVan();
    var veldNaar  = zet.getNaar();
    var stukVan   = bord[veldVan];
    var stukNaar  = bord[veldNaar];
    if (stukNaar != 0
        || Math.abs(stukVan) == CaissaConstants.PION) {
      halvezetten = 0;
    } else {
      halvezetten++;
    }

    if (zet.getPromotieStuk() != ' ') {
      stukVan = CaissaUtils.zoekStuk(zet.getPromotieStuk());
      if (aanZet == 'b') {
        stukVan *= -1;
      }
    }
    bord[veldVan]  = 0;
    bord[veldNaar] = stukVan;
    doeEnPassant(veldVan, veldNaar, stukVan);
    bordToPositie();

    if (aanZet == 'w') {
      aanZetWit(veldVan, veldNaar, stukVan);
    } else {
      aanZetZwart(veldVan, veldNaar, stukVan);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FEN)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    final FEN other = (FEN) obj;
    return getFen().equals(other.getFen());
  }

  public Zet geefZet(FEN fen) throws FenException {
    if (aanZet == fen.getAanZet()) {
      throw new FenException(resourceBundle.getString(ERR_ZET));
    }

    String    beginstelling;
    String    eindstelling;
    List<Zet> zetten;
    if (aanZet == 'w'
        && zetnummer.equals(fen.zetnummer)) {
      zetten  = new Zettengenerator(new FEN(getFen())).getZetten();
      beginstelling = getFen();
      eindstelling  = fen.getFen();
    } else {
      zetten  = new Zettengenerator(fen).getZetten();
      beginstelling = fen.getFen();
      eindstelling  = getFen();
    }

    Zet gevondenzet = null;
    for (var zet: zetten) {
      var stelling  = new FEN(beginstelling);
      stelling.doeZet(zet);
      if (eindstelling.equals(stelling.getFen())) {
        gevondenzet = zet;
      }
    }

    if (null == gevondenzet) {
      throw new FenException(resourceBundle.getString(ERR_ZET));
    }

    return gevondenzet;
  }

  public char getAanZet() {
    return aanZet;
  }

  public int[] getBord() {
    return bord.clone();
  }

  public String getEnPassant() {
    return enPassant;
  }

  public String getFen() {
    var fen = new StringBuilder();

    fen.append(getPositie()).append(" ");
    fen.append(getAanZet()).append(" ");
    fen.append(getRochade()).append(" ");
    fen.append(getEnPassant()).append(" ");
    fen.append(getHalvezetten().toString()).append(" ");
    fen.append(getZetnummer().toString());

    return fen.toString();
  }

  public Integer getHalvezetten() {
    return halvezetten;
  }

  public String getKortePositie() {
    var kortePositie = new StringBuilder();
    var leeg  = 0;
    for (var i = 9; i > 1; i--) {
      for (var j = 1; j < 9; j++) {
        if (bord[i*10+j] == 0) {
          leeg++;
        } else {
          if (leeg > 0) {
            kortePositie.append(leeg);
            leeg  = 0;
          }
          kortePositie.append(CaissaUtils.getStuk(bord[i*10+j]));
        }
      }
    }
    if (leeg > 0) {
      kortePositie.append(leeg);
    }

    return kortePositie.toString();
  }

  public String getPositie() {
    return positie;
  }

  protected String getRochade() {
    var rochade = new StringBuilder();

    if (Boolean.TRUE.equals(witKorteRochade)) {
      rochade.append("K");
    }
    if (Boolean.TRUE.equals(witLangeRochade)) {
      rochade.append("Q");
    }
    if (Boolean.TRUE.equals(zwartKorteRochade)) {
      rochade.append("k");
    }
    if (Boolean.TRUE.equals(zwartLangeRochade)) {
      rochade.append("q");
    }
    if (rochade.length() == 0) {
      rochade.append('-');
    }

    return rochade.toString();
  }

  public Boolean getWitKorteRochade() {
    return witKorteRochade;
  }

  public String getWitKorteToren() {
    return kortetoren + "1";
  }

  public Boolean getWitLangeRochade() {
    return witLangeRochade;
  }

  public String getWitLangeToren() {
    return langetoren + "1";
  }

  public Integer getZetnummer() {
    return zetnummer;
  }

  public Boolean getZwartKorteRochade() {
    return zwartKorteRochade;
  }

  public String getZwartKorteToren() {
    return kortetoren + "8";
  }

  public Boolean getZwartLangeRochade() {
    return zwartLangeRochade;
  }

  public String getZwartLangeToren() {
    return langetoren + "8";
  }

  @Override
  public int hashCode() {
    return getFen().hashCode();
  }

  private void positieToBord() {
    for (var i = 0; i<120; i++) {
      bord[i]  = 7;
    }

    for (var i = 2; i < 10; i++) {
      for (var j = 1; j < 9; j++) {
        bord[i*10+j]  = 0;
      }
    }

    String[]  rij = positie.split("/");
    for (var i = 0; i < 8; i++) {
      var kolom = 1;
      for (var j = 0; j < rij[i].length(); j++) {
        var ch  = rij[i].charAt(j);
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

  public String printBord() {
    var internBord  = new StringBuilder();

    for (var i = 9; i > 1; i--) {
      internBord.append((i - 1)).append(" ");
      for (var j = 1; j < 9; j++) {
        internBord.append(CaissaUtils.getStuk(bord[(i * 10 + j)]));
      }
      internBord.append(eol);
    }
    internBord.append("  ABCDEFGH").append(eol);

    return internBord.toString();
  }

  public void setAanZet(char aanZet) throws FenException {
    if ("bw".indexOf(aanZet) < 0) {
      throw new FenException(resourceBundle.getString(ERR_AANZET));
    }

    this.aanZet = aanZet;
  }

  public void setAanZet(String aanZet) throws FenException {
    if (aanZet.length() != 1) {
      throw new FenException(resourceBundle.getString(ERR_AANZET));
    }

    setAanZet(aanZet.charAt(0));
  }

  public void setEnPassant(String enPassant) throws FenException {
    if ("-".equals(enPassant)) {
      this.enPassant  = enPassant;
      return;
    }

    if ("abcdefgh".indexOf(enPassant.charAt(0)) < 0) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ENPASSANT), enPassant));
    }
    if (aanZet == 'b'
        && enPassant.charAt(1) != '3') {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ENPASSANT), enPassant));
    }
    if (aanZet == 'w'
        && enPassant.charAt(1) != '6') {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ENPASSANT), enPassant));
    }
    // TODO test of het wel een e.p. pion is.

    this.enPassant = enPassant;
  }

  public void setFen(String fen) throws FenException {
    var veld  = fen.split(" ");

    setPositie(veld[0]);
    setAanZet(veld[1]);
    setRochade(veld[2]);
    setEnPassant(veld[3]);
    setHalvezetten(Integer.valueOf(veld[4]));
    setZetnummer(Integer.valueOf(veld[5]));
  }

  public void setHalvezetten(Integer halvezetten) {
    this.halvezetten = halvezetten;
  }

  public void setPositie(String positie) {
    this.positie  = positie;
    positieToBord();
  }

  private void setRochade(String rochade) {
    // TODO Test de juiste volgorde.
    witKorteRochade   = rochade.indexOf('K') != -1;
    witLangeRochade   = rochade.indexOf('Q') != -1;
    zwartKorteRochade = rochade.indexOf('k') != -1;
    zwartLangeRochade = rochade.indexOf('q') != -1;
  }

  public void setWitKorteRochade(Boolean witKorteRochade) {
    this.witKorteRochade = witKorteRochade;
  }

  public void setWitLangeRochade(Boolean witLangeRochade) {
    this.witLangeRochade = witLangeRochade;
  }

  public void setZetnummer(Integer zetnummer) {
    this.zetnummer = zetnummer;
  }

  public void setZwartKorteRochade(Boolean zwartKorteRochade) {
    this.zwartKorteRochade = zwartKorteRochade;
  }

  public void setZwartLangeRochade(Boolean zwartLangeRochade) {
    this.zwartLangeRochade = zwartLangeRochade;
  }

  @Override
  public String toString() {
    return getFen();
  }
}
