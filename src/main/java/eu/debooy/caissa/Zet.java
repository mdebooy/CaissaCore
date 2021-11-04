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

import eu.debooy.caissa.exceptions.ZetException;
import eu.debooy.doosutils.DoosConstants;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * Deze class bevat de informatie voor een zet. Als deze is aangemaakt door de
 * eu.debooy.caissa.Zettengenerator zijn de gegevens nog niet noodzakelijk
 * compleet. De attributen mat, schaak en korteNotatie hebben slechts hun
 * default waarde. Verdere verwerking is noodzakelijk.
 * mat               - Zet de tegenstander mat?
 * schaak            - Zet de tegenstander schaak?
 * korteNotatieLevel - Indien er uit een stelling meerdere zelfde stukken naar
 *                     hetzelfde veld kunnen moet er extra informatie met de
 *                     korteNotatie worden meegegeven. De waarde van deze
 *                     attribuut bepaald welke. 1=kolom, 2=rij en 3=kolom en
 *                     rij.
 *
 * @author Marco de Booij
 */
public class Zet implements Comparable<Object>, Serializable {
  private static final long serialVersionUID  = 1L;

  private static final String  ERR_PROMOTIESTUK = "zet.promotiestuk.incorrect";
  private static final String  ERR_STUK         = "zet.incorrect";

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private boolean ep                = false;
  private boolean mat               = false;
  private boolean schaak            = false;
  private boolean slagzet           = false;
  private char    promotieStuk      = ' ';
  private char    stuk              = ' ';
  private int     korteNotatieLevel = 0;
  private int     naar;
  private int     van;

  public Zet() {
  }

  public Zet(int van, int naar) {
    this.stuk = ' ';
    this.naar = naar;
    this.van  = van;
  }

  public Zet(char stuk, int van, int naar) {
    this.stuk = stuk;
    this.naar = naar;
    this.van  = van;
  }

  public Zet(char stuk, int van, int naar, char promotieStuk) {
    this.stuk         = stuk;
    this.naar         = naar;
    this.van          = van;
    this.promotieStuk = promotieStuk;
  }

  public Zet(Zet zet) {
    ep                = zet.ep;
    korteNotatieLevel = zet.korteNotatieLevel;
    mat               = zet.mat;
    naar              = zet.naar;
    promotieStuk      = zet.promotieStuk;
    schaak            = zet.schaak;
    slagzet           = zet.slagzet;
    stuk              = zet.stuk;
    van               = zet.van;
  }

  private void addExtraZetinfo(StringBuilder zet) {
    if (schaak) {
      zet.append('+');
    }
    if (mat) {
      zet.append('#');
    }
    if (ep) {
      zet.append(CaissaConstants.EN_PASSANT);
    }
  }

  @Override
  public int compareTo(Object obj) {
    Zet other = (Zet) obj;

    if (naar < other.getNaar()) {
      return DoosConstants.BEFORE;
    }
    if (naar > other.getNaar()) {
      return DoosConstants.AFTER;
    }
    if (stuk < other.getStuk()) {
      return DoosConstants.BEFORE;
    }
    if (stuk > other.getStuk()) {
      return DoosConstants.AFTER;
    }
    if (van < other.getVan()) {
      return DoosConstants.BEFORE;
    }
    if (van > other.getVan()) {
      return DoosConstants.AFTER;
    }
    if (promotieStuk < other.getPromotieStuk()) {
      return DoosConstants.BEFORE;
    }
    if (promotieStuk > other.getPromotieStuk()) {
      return DoosConstants.AFTER;
    }

    return DoosConstants.EQUAL;
  }

  private String doeChessTheatreRokade() {
    if ((van - naar) == -2) {
      if (van == 25) {
        return "60.RK.";
      } else {
        return "4.rk.";
      }
    }

    if (van == 25) {
      return "56.1KR.";
    } else {
      return ".1kr.";
    }
  }

  private void doeEp(StringBuilder zet, int voor) {
    if (van < naar) {
      // Wit
      if (naar%10 > van%10) {
        zet.append((voor - 1)).append(" 6..");
      } else {
        zet.append((voor - 1)).append(" 7..");
      }
    } else {
      // Zwart
      if (naar%10 > van%10) {
        zet.append((voor - 1)).append("..7p");
      } else {
        zet.append((voor - 2)).append("..6p");
      }
    }
  }

  private void doeGewoneZet(StringBuilder zet, int voor, int na) {
    if (voor > 1) {
      zet.append((voor - 1));
    }
    // Eerst veld schoonmaken of eerst veld zetten.
    if (((9 - naar/10) * 8 + naar%10) < ((9 - van/10) * 8 + van%10)) {
      zet.append(promotieStuk == ' ' ? stuk : promotieStuk);
      zet.append((na - voor) == 1 ? "" : (na - voor - 1));
      zet.append('.');
    } else {
      zet.append('.');
      zet.append((na - voor) == 1 ? "" : (na - voor - 1));
      zet.append(promotieStuk == ' ' ? stuk : promotieStuk);
    }
  }

  private String doeRokade() {
    if ((van - naar) == -2) {
      return CaissaConstants.KORTE_ROCHADE;
    }

    return CaissaConstants.LANGE_ROCHADE;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Zet other = (Zet) obj;
    return !(van != other.van || naar != other.naar
             || promotieStuk != other.promotieStuk);
  }

  public String getChessTheatreZet() {
    StringBuilder zet   = new StringBuilder();

    if (stuk == 'K'
        && Math.abs(van - naar) == 2) {
      return doeChessTheatreRokade();
    }

    int na        = (9 - van/10) * 8 + van%10;
    int voor      = (9 - naar/10) * 8 + naar%10;

    if (na < voor) {
      int hulp  = na;
      na        = voor;
      voor      = hulp;
    }

    if (ep) {
      doeEp(zet, voor);
    } else {
      doeGewoneZet(zet, voor, na);
    }

    return zet.toString().replace(' ', 'P');
  }

  public String getCorrespondentieZet() {
    StringBuilder zet = new StringBuilder();

    zet.append((char) (van%10 + 48));
    zet.append((char) (van/10 + 47));
    zet.append((char) (naar%10 + 48));
    zet.append((char) (naar/10 + 47));

    return zet.toString();
  }

  public String getKorteNotatie() {
    return getKorteNotatie(korteNotatieLevel);
  }

  private String getKorteNotatie(int level) {
    var zet = new StringBuilder();

    if (stuk == 'K'
        && Math.abs(van - naar) == 2) {
      zet.append(doeRokade());
    } else {
      zet.append(stuk);
      if (level == 1
          || level == 3
          || (stuk == ' '
            && isSlagzet())) {
        zet.append(CaissaUtils.internToExtern(van).charAt(0));
      }
      if (level > 1) {
        zet.append(CaissaUtils.internToExtern(van).charAt(1));
      }
      if (isSlagzet()) {
        zet.append('x');
      }
      zet.append(CaissaUtils.internToExtern(naar));
      if (promotieStuk != ' ') {
        zet.append(promotieStuk);
      }
    }
    addExtraZetinfo(zet);

    return zet.toString().trim();
  }

  public int getKorteNotatieLevel() {
    return korteNotatieLevel;
  }

  public String getLangeNotatie() {
    var zet = new StringBuilder();

    if (stuk == 'K'
      && (van - naar) == -2) {
        zet.append(CaissaConstants.KORTE_ROCHADE);
    } else {
      if (stuk == 'K'
        && (van - naar) == 2) {
        zet.append(CaissaConstants.LANGE_ROCHADE);
      } else {
        zet.append(stuk);
        zet.append(CaissaUtils.internToExtern(van));
        if (isSlagzet()) {
          zet.append('x');
        } else {
          zet.append('-');
        }
        zet.append(CaissaUtils.internToExtern(naar));
      }
    }

    if (promotieStuk != ' ') {
      zet.append(promotieStuk);
    }
    if (schaak) {
      zet.append('+');
    }
    if (mat) {
      zet.append('#');
    }
    if (ep) {
      zet.append(CaissaConstants.EN_PASSANT);
    }

    return zet.toString().trim();
  }

  public int getNaar() {
    return naar;
  }

  public String getPgnNotatie() {
    var zet       = getKorteNotatie();

    if (zet.endsWith(CaissaConstants.EN_PASSANT)) {
      zet = zet.substring(0, zet.length() - 5);
    }
    if (promotieStuk == ' ') {
      return zet;
    }

    var promotie  = zet.indexOf(promotieStuk);

    if (promotie >= 0) {
      zet = zet.substring(0, promotie) + '=' + zet.substring(promotie);
    }

    return zet;
  }

  public char getPromotieStuk() {
    return promotieStuk;
  }

  public char getStuk() {
    return stuk;
  }

  public String getUciNotatie() {
    var zet = new StringBuilder();

    zet.append((char) (van%10 + 64));
    zet.append((char) (van/10 + 47));
    zet.append((char) (naar%10 + 64));
    zet.append((char) (naar/10 + 47));

    if (promotieStuk != ' ') {
      zet.append(promotieStuk);
    }

    return zet.toString().toLowerCase();
  }

  public int getVan() {
    return van;
  }

  public String getZet() {
    return getKorteNotatie(korteNotatieLevel);
  }

  protected String getZet(int level) {
    return getKorteNotatie(level);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(van). append(naar).append(promotieStuk)
                                .toHashCode();
  }

  public boolean isEp() {
    return ep;
  }

  public boolean isMat() {
    return mat;
  }

  public boolean isSchaak() {
    return schaak;
  }

  public boolean isSlagzet() {
    return (slagzet || ep);
  }

  public void setEp(boolean ep) {
    this.ep = ep;
  }

  public void setKorteNotatieLevel(int korteNotatieLevel) {
    this.korteNotatieLevel = korteNotatieLevel;
  }

  public void setMat(boolean mat) {
    this.mat = mat;
  }

  public void setNaar(int naar) {
    this.naar = naar;
  }

  public void setPromotieStuk(char promotieStuk) throws ZetException {
    if (CaissaConstants.PROMOTIE_STUKKEN.indexOf(promotieStuk) < 0) {
      throw new ZetException(MessageFormat.format(
          resourceBundle.getString(ERR_PROMOTIESTUK), promotieStuk));
    }

    this.promotieStuk = promotieStuk;
  }

  public void setSchaak(boolean schaak) {
    this.schaak = schaak;
  }

  public void setSlagzet(boolean slagzet) {
    this.slagzet = slagzet;
  }

  public void setStuk(char stuk) throws ZetException {
    if (CaissaConstants.NOTATIE_STUKKEN.indexOf(stuk) < 0) {
      throw new ZetException(MessageFormat.format(
          resourceBundle.getString(ERR_STUK), stuk));
    }

    this.stuk = stuk;
  }

  public void setVan(int van) {
    this.van = van;
  }

  @Override
  public String toString() {
    return getLangeNotatie() + "|" + getKorteNotatie();
  }
}
