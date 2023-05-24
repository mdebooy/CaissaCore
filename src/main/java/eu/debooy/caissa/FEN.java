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

import static eu.debooy.caissa.FEN.resourceBundle;
import eu.debooy.caissa.exceptions.FenException;
import eu.debooy.doosutils.DoosUtils;
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

  public static final String  ERR_AANZET    = "fen.aanzet.incorrect";
  public static final String  ERR_ENPASSANT = "fen.ep.incorrect";
  public static final String  ERR_ROKADE    = "fen.rokade.incorrect";
  public static final String  ERR_ZET       = "fen.zet.incorrect";

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private       char    aanZet            = CaissaConstants.WIT;
  private final int[]   bord              = new int[120];
  private       boolean schaak960         = false;
  private       String  enPassant         = "-";
  private final String  eol               = System.lineSeparator();
  private       Integer halvezetten       = 0;
  private       char    koninglijn;
  private       char    kortetoren        = '@';
  private       char    langetoren        = '@';
  private       String  positie           = BEGINSTELLING;
  private       Boolean witKorteRokade;
  private       Boolean witLangeRokade;
  private       Integer zetnummer         = 1;
  private       Boolean zwartKorteRokade;
  private       Boolean zwartLangeRokade;

  public FEN() {
    positieToBord();
    kortetoren        = 'h';
    langetoren        = 'a';
    witKorteRokade    = true;
    witLangeRokade    = true;
    zwartKorteRokade  = true;
    zwartLangeRokade  = true;
  }

  public FEN(String fen) throws FenException {
    var veld    = fen.split(" ");

    positie     = veld[0];
    positieToBord();
    aanZet      = veld[1].charAt(0);
    setRokade(veld[2]);
    enPassant   = veld[3];
    halvezetten = Integer.valueOf(veld[4]);
    zetnummer   = Integer.valueOf(veld[5]);
  }

  private void aanZetWit(int veldVan, int veldNaar, int stukVan) {
    aanZet  = CaissaConstants.ZWART;
    if (veldVan == 21
        && stukVan == CaissaConstants.TOREN) {
      witLangeRokade  = false;
    }
    if (veldVan == 25
        && stukVan == CaissaConstants.KONING) {
      witKorteRokade  = false;
      witLangeRokade  = false;
    }
    if (veldVan == 28
        && stukVan == CaissaConstants.TOREN) {
      witKorteRokade  = false;
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
      zwartLangeRokade  = false;
    }
    if (veldVan == 95
        && stukVan == CaissaConstants.ZKONING) {
      zwartKorteRokade  = false;
      zwartLangeRokade  = false;
    }
    if (veldVan == 98
        && stukVan == CaissaConstants.ZTOREN) {
      zwartKorteRokade  = false;
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

  private boolean checkEnPassant(String enPassant) {
    if ("abcdefgh".indexOf(enPassant.charAt(0)) < 0
        || enPassant.charAt(1) != (aanZet == CaissaConstants.WIT ? '6' : '3')) {
      return false;
    }
    var intern  = CaissaUtils.externToIntern(enPassant);
    var pion    = aanZet == 'w' ? CaissaConstants.ZPION : CaissaConstants.PION;
    if (bord[intern] != 0) {
      return false;
    }
    return bord[intern + (aanZet == CaissaConstants.WIT ? -10 : +10)] == pion;
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
      if (aanZet == CaissaConstants.ZWART) {
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
    if (aanZet == CaissaConstants.WIT
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
    fen.append(getRokade()).append(" ");
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

  protected String getRokade() {
    var rochade = new StringBuilder();

    if (Boolean.TRUE.equals(witKorteRokade)) {
      if (schaak960) {
        rochade.append(Character.toUpperCase(kortetoren));
      } else {
        rochade.append("K");
      }
    }
    if (Boolean.TRUE.equals(witLangeRokade)) {
      if (schaak960) {
        rochade.append(Character.toUpperCase(langetoren));
      } else {
        rochade.append("Q");
      }
    }
    if (Boolean.TRUE.equals(zwartKorteRokade)) {
      if (schaak960) {
        rochade.append(kortetoren);
      } else {
        rochade.append("k");
      }
    }
    if (Boolean.TRUE.equals(zwartLangeRokade)) {
      if (schaak960) {
        rochade.append(langetoren);
      } else {
        rochade.append("q");
      }
    }
    if (rochade.length() == 0) {
      rochade.append('-');
    }

    return rochade.toString();
  }

  public Boolean getWitKorteRokade() {
    return witKorteRokade;
  }

  public String getWitKorteToren() {
    return kortetoren + "1";
  }

  public Boolean getWitLangeRokade() {
    return witLangeRokade;
  }

  public String getWitLangeToren() {
    return langetoren + "1";
  }

  public Integer getZetnummer() {
    return zetnummer;
  }

  public Boolean getZwartKorteRokade() {
    return zwartKorteRokade;
  }

  public String getZwartKorteToren() {
    return kortetoren + "8";
  }

  public Boolean getZwartLangeRokade() {
    return zwartLangeRokade;
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
          if (ch == 'k') {
            koninglijn  = (char) (kolom+96);
          }
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

    if (!checkEnPassant(enPassant)) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ENPASSANT), enPassant));
    }

    this.enPassant = enPassant;
  }

  public void setFen(String fen) throws FenException {
    var veld  = fen.split(" ");

    setPositie(veld[0]);
    setAanZet(veld[1]);
    setRokade(veld[2]);
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

  private void setRokade(String rokade) throws FenException {
    if (DoosUtils.isBlankOrNull(rokade)) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ROKADE), rokade));
    }

    witKorteRokade    = false;
    witLangeRokade    = false;
    zwartKorteRokade  = false;
    zwartLangeRokade  = false;

    if (rokade.equals("-")) {
      return;
    }

    var uniek         = DoosUtils.uniekeCharacters(rokade.toLowerCase());
    if (uniek.length() > 2) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ROKADE), rokade));
    }
    if (uniek.length() == 1
        && !(uniek.equals("k") || uniek.equals("q"))) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ROKADE), rokade));
    }

    var rokades       = rokade.toCharArray();
    for (var i = 0; i < rokades.length; i++) {
      if (rokades[i] == Character.toUpperCase(rokades[i])) {
        setRokadewit(Character.toLowerCase(rokades[i]), koninglijn, rokade);
      } else {
        setRokadezwart(rokades[i], koninglijn, rokade);
      }
    }
  }

  private void setRokadewit(char lijn, char koninglijn, String rokade)
      throws FenException {
    if (lijn == koninglijn) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ROKADE), rokade));
    }

    if (lijn == 'k') {
      kortetoren      = 'h';
      witKorteRokade  = true;
      return;
    }
    if (lijn == 'q') {
      langetoren      = 'a';
      witLangeRokade  = true;
      return;
    }

    if (lijn > koninglijn
        && CaissaConstants.FEN_KORTEROKADE.indexOf(lijn) != -1) {
      kortetoren      = lijn;
      schaak960       = true;
      witKorteRokade  = true;
      return;
    }
    if (lijn < koninglijn
            && CaissaConstants.FEN_LANGEROKADE.indexOf(lijn) != -1) {
      langetoren      = lijn;
      schaak960       = true;
      witLangeRokade  = true;
      return;
    }

    throw new FenException(MessageFormat.format(
        resourceBundle.getString(ERR_ROKADE), rokade));
  }

  private void setRokadezwart(char lijn, char koninglijn, String rokade)
      throws FenException {
    if (lijn == koninglijn) {
      throw new FenException(MessageFormat.format(
          resourceBundle.getString(ERR_ROKADE), rokade));
    }

    if (lijn == 'k') {
      kortetoren        = 'h';
      zwartKorteRokade  = true;
      return;
    }
    if (lijn == 'q') {
      langetoren        = 'a';
      zwartLangeRokade  = true;
      return;
    }

    if (lijn < koninglijn
            && CaissaConstants.FEN_LANGEROKADE.indexOf(lijn) != -1) {
      langetoren        = lijn;
      schaak960         = true;
      zwartLangeRokade  = true;
      return;
    }
    if (lijn > koninglijn
        && CaissaConstants.FEN_KORTEROKADE.indexOf(lijn) != -1) {
      kortetoren        = lijn;
      schaak960         = true;
      zwartKorteRokade  = true;
      return;
    }

    throw new FenException(MessageFormat.format(
        resourceBundle.getString(ERR_ROKADE), rokade));
  }

  public void setWitKorteRokade(Boolean witKorteRokade) {
    this.witKorteRokade  = witKorteRokade;
  }

  public void setWitLangeRokade(Boolean witLangeRokade) {
    this.witLangeRokade  = witLangeRokade;
  }

  public void setZetnummer(Integer zetnummer) {
    this.zetnummer = zetnummer;
  }

  public void setZwartKorteRokade(Boolean zwartKorteRokade) {
    this.zwartKorteRokade  = zwartKorteRokade;
  }

  public void setZwartLangeRokade(Boolean zwartLangeRokade) {
    this.zwartLangeRokade  = zwartLangeRokade;
  }

  @Override
  public String toString() {
    return getFen();
  }
}
