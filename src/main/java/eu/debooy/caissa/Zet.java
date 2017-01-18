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

  public String getChessTheatreZet() {
    StringBuilder zet   = new StringBuilder();

    if (stuk == 'K'
      && (van - naar) == -2) {
      if (van == 25) {
        return "60.RK.";
      } else {
        return "4.rk.";
      }
    }
    if (stuk == 'K'
      && (van - naar) == 2) {
      if (van == 25) {
        return "56.1KR.";
      } else {
        return ".1kr.";
      }
    }

    int na        = (9 - van/10) * 8 + van%10;
    int voor      = (9 - naar/10) * 8 + naar%10;

    if (na < voor) {
      int hulp  = na;
      na        = voor;
      voor      = hulp;
    }

    if (ep) {
      if (van < naar) {
        // Wit
        if (naar%10 > van%10) {
          zet.append("" + (voor - 1) + " 8..");
        } else {
          zet.append("" + (voor - 1) + " 7..");
        }
      } else {
        // Zwart
        if (naar%10 > van%10) {
          zet.append("" + (voor - 1) + "..7p");
        } else {
          zet.append("" + (voor - 2) + "..6p");
        }
      }
    } else {
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
    StringBuilder zet = new StringBuilder();
    
    if (stuk == 'K'
      && (van - naar) == -2) {
        zet.append(CaissaConstants.KORTE_ROCHADE);
    } else {
      if (stuk == 'K'
        && (van - naar) == 2) {
          zet.append(CaissaConstants.LANGE_ROCHADE);
      } else {
        zet.append(stuk);
        if (korteNotatieLevel == 1
            || korteNotatieLevel == 3
            || (stuk == ' '
              && isSlagzet())) {
          zet.append(CaissaUtils.internToExtern(van).charAt(0));
        }
        if (korteNotatieLevel == 2
            || korteNotatieLevel == 3) {
          zet.append(CaissaUtils.internToExtern(van).charAt(1));
        }
        if (isSlagzet()) {
          zet.append('x');
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
      zet.append(" e.p.");
    }

    return zet.toString().trim();
  }

  public int getKorteNotatieLevel() {
    return korteNotatieLevel;
  }

  public String getLangeNotatie() {
    StringBuilder zet = new StringBuilder();

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
    String  zet       = getKorteNotatie();

    if (zet.endsWith(CaissaConstants.EN_PASSANT)) {
      zet = zet.substring(0, zet.length() - 5);
    }
    if (promotieStuk == ' ') {
      return zet;
    }

    int     promotie  = zet.indexOf(promotieStuk);

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

  public int getVan() {
    return van;
  }

  public String getZet() {
    return getKorteNotatie();
  }

  protected String getZet(int notatie) {
    StringBuilder zet = new StringBuilder();

    if (stuk == 'K') {
      if ((van - naar) == 2) {
        if (schaak) {
          return CaissaConstants.KORTE_ROCHADE + "+";
        }
        if (mat) {
          return CaissaConstants.KORTE_ROCHADE + "#";
        }
        return CaissaConstants.KORTE_ROCHADE;
      }
      if ((van - naar) == -2) {
        if (schaak) {
          return CaissaConstants.LANGE_ROCHADE + "+";
        }
        if (mat) {
          return CaissaConstants.LANGE_ROCHADE + "#";
        }
        return CaissaConstants.LANGE_ROCHADE;
      }
    }

    zet.append(stuk);
    if (notatie == 1
        || notatie == 3) {
      zet.append(CaissaUtils.internToExtern(van).charAt(0));
    }
    if (notatie > 1) {
      zet.append(CaissaUtils.internToExtern(van).charAt(1));
    }
    if (isSlagzet()) {
      zet.append('x');
    }
    zet.append(CaissaUtils.internToExtern(naar));
    if (schaak) {
      zet.append('+');
    }
    if (mat) {
      zet.append('#');
    }

    return zet.toString().trim();
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
    if ("NBRQ".indexOf(promotieStuk) < 0) {
      throw new ZetException("PromotieStuk foutief [" + promotieStuk + "]");
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
      throw new ZetException("Stuk foutief [" + stuk + "]");
    }

    this.stuk = stuk;
  }

  public void setVan(int van) {
    this.van = van;
  }

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
    if (van != other.van
        || naar != other.naar
        || promotieStuk != other.promotieStuk) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return van * 100000 + naar * 1000 + promotieStuk;
  }

  public String toString() {
    return getLangeNotatie() + "|" + getKorteNotatie();
  }
}
