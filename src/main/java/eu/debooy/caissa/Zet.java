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
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
public class Zet implements Comparable<Zet>, Serializable {
  private static final long serialVersionUID  = 1L;

  public static final String  ERR_NAAR         = "zet.naar.incorrect";
  public static final String  ERR_PROMOTIESTUK = "zet.promotiestuk.incorrect";
  public static final String  ERR_STUK         = "zet.stuk.incorrect";
  public static final String  ERR_VAN          = "zet.van.incorrect";

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private boolean ep                = false;
  private boolean mat               = false;
  private boolean rokade            = false;
  private boolean schaak            = false;
  private boolean slagzet           = false;
  private char    promotieStuk      = ' ';
  private char    stuk              = ' ';
  private int     korteNotatieLevel = 0;
  private int     naar;
  private int     van;

  public Zet() {
  }

  public Zet(int van, int naar) throws ZetException {
    this(' ', van, naar, ' ');
  }

  public Zet(char stuk, int van, int naar) throws ZetException {
    this(stuk, van, naar, ' ');
  }

  public Zet(int van, int naar, char promotieStuk) throws ZetException {
    this(' ', van, naar, promotieStuk);
  }

  protected Zet(char stuk, int van, int naar, char promotieStuk)
      throws ZetException {
    if (' ' == promotieStuk) {
      setStuk(stuk);
    } else {
      setStuk(' ');

    }
    setNaar(naar);
    setPromotieStuk(promotieStuk);
    setVan(van);
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
    if (promotieStuk != ' ') {
      zet.append(promotieStuk);
    }
    if (mat) {
      zet.append('#');
    } else {
      if (schaak) {
        zet.append('+');
      }
    }
    if (ep) {
      zet.append(CaissaConstants.EN_PASSANT);
    }
  }

  @Override
  public int compareTo(Zet other) {
    return new CompareToBuilder().append(naar, other.naar)
                                 .append(stuk, other.stuk)
                                 .append(van, other.van)
                                 .append(promotieStuk, other.promotieStuk)
                                 .toComparison();
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
    return ((van - naar) < 0) ? CaissaConstants.KORTE_ROCHADE
                              : CaissaConstants.LANGE_ROCHADE;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Zet)) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    final Zet other = (Zet) obj;
    return new EqualsBuilder().append(van, other.van)
                              .append(naar, other.naar)
                              .append(promotieStuk, other.promotieStuk)
                              .isEquals();
  }

  public String getChessTheatreZet() {
    var zet   = new StringBuilder();

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
    var zet = new StringBuilder();

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

    if (rokade) {
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
    }
    addExtraZetinfo(zet);

    return zet.toString().trim();
  }

  public int getKorteNotatieLevel() {
    return korteNotatieLevel;
  }

  public String getLangeNotatie() {
    var zet = new StringBuilder();

    if (rokade) {
      if ((van - naar) < 0) {
        zet.append(CaissaConstants.KORTE_ROCHADE);
      } else {
        zet.append(CaissaConstants.LANGE_ROCHADE);
      }
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
    addExtraZetinfo(zet);

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
    if (korteNotatieLevel < 0) {
      this.korteNotatieLevel  = 0;
      return;
    }
    if (korteNotatieLevel >3) {
      this.korteNotatieLevel  = 3;
      return;
    }

    this.korteNotatieLevel = korteNotatieLevel;
  }

  public void setMat(boolean mat) {
    this.mat = mat;
  }

  public final void setNaar(int naar) throws ZetException {
    if (naar < 21 || naar > 98 || naar%10 == 0 || naar%10 == 9) {
      throw new ZetException(resourceBundle.getString(ERR_NAAR));
    }

    this.naar = naar;
  }

  public final void setPromotieStuk(char promotieStuk) throws ZetException {
    if (' ' != promotieStuk
        && CaissaConstants.PROMOTIE_STUKKEN.indexOf(promotieStuk) < 0) {
      throw new ZetException(MessageFormat.format(
          resourceBundle.getString(ERR_PROMOTIESTUK), promotieStuk));
    }

    this.promotieStuk = promotieStuk;
  }

  public void setRokade(boolean rokade) {
    this.rokade   = rokade;
  }

  public void setSchaak(boolean schaak) {
    this.schaak   = schaak;
  }

  public void setSlagzet(boolean slagzet) {
    this.slagzet  = slagzet;
  }

  public final void setStuk(char stuk) throws ZetException {
    if (CaissaConstants.NOTATIE_STUKKEN.indexOf(stuk) < 0) {
      throw new ZetException(MessageFormat.format(
          resourceBundle.getString(ERR_STUK), stuk));
    }

    this.stuk = stuk;
  }

  public final void setVan(int van) throws ZetException {
    if (van < 21 || van > 98 || van%10 == 0 || van%10 == 9) {
      throw new ZetException(resourceBundle.getString(ERR_VAN));
    }

    this.van = van;
  }

  @Override
  public String toString() {
    return getLangeNotatie() + "|" + getKorteNotatie();
  }
}
