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

  /**
   * @param naar het naar veld
   * @param van het van veld
   */
  public Zet(int van, int naar) {
    this.stuk = ' ';
    this.naar = naar;
    this.van  = van;
  }

  /**
   * @param stuk het stuk
   * @param naar het naar veld
   * @param van het van veld
   */
  public Zet(char stuk, int van, int naar) {
    this.stuk = stuk;
    this.naar = naar;
    this.van  = van;
  }

  /**
   * @param stuk het stuk
   * @param naar het naar veld
   * @param van het van veld
   * @param promotieStuk het stuk waar de pion naar promoveert
   */
  public Zet(char stuk, int van, int naar, char promotieStuk) {
    this.stuk         = stuk;
    this.naar         = naar;
    this.van          = van;
    this.promotieStuk = promotieStuk;
  }

  /**
   * @return de zet in ChessTheatre formaat
   */
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

  /**
   * Bij correspondentieschaak geldt a1=11, a8=18, h1=81 en h8=88.
   * 
   * @return de zet in Correspondentieschaak formaat
   */
  public String getCorrespondentieZet() {
    StringBuilder zet = new StringBuilder();

    zet.append((char) (van%10 + 48));
    zet.append((char) (van/10 + 47));
    zet.append((char) (naar%10 + 48));
    zet.append((char) (naar/10 + 47));

    return zet.toString();
  }

  /**
   * @return de korte notatie
   */
  public String getKorteNotatie() {
    StringBuilder zet = new StringBuilder();
    
    if (stuk == 'K'
      && (van - naar) == -2) {
        zet.append("O-O");
    } else {
      if (stuk == 'K'
        && (van - naar) == 2) {
          zet.append("O-O-O");
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

  /**
   * @return het 'level' van de korte notatie
   */
  public int getKorteNotatieLevel() {
    return korteNotatieLevel;
  }

  /**
   * @return de lange notatie
   */
  public String getLangeNotatie() {
    StringBuilder zet = new StringBuilder();

    if (stuk == 'K'
      && (van - naar) == -2) {
        zet.append("O-O");
    } else {
      if (stuk == 'K'
        && (van - naar) == 2) {
        zet.append("O-O-O");
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
      zet.append(" e.p.");
    }

    return zet.toString().trim();
  }

  /**
   * @return het naar veld
   */
  public int getNaar() {
    return naar;
  }

  /**
   * @return de PGN notatie
   */
  public String getPgnNotatie() {
    String  zet       = getKorteNotatie();

    if (zet.endsWith(" e.p.")) {
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

  /**
   * @return het stuk waar de pion naar promoveert
   */
  public char getPromotieStuk() {
    return promotieStuk;
  }

  /**
   * @return het stuk
   */
  public char getStuk() {
    return stuk;
  }

  /**
   * @return het van veld
   */
  public int getVan() {
    return van;
  }

  /**
   * @return korte notatie
   */
  public String getZet() {
    return getKorteNotatie();
  }

  /**
   * Geef de zet van KortenotatieLevel 'notatie'
   * 
   * @param notatie het KortenotatieLevel
   * @return de zet in Kortenotatie van 'notatie' level
   */
  protected String getZet(int notatie) {
    StringBuilder zet = new StringBuilder();

    if (stuk == 'K') {
      if ((van - naar) == 2) {
        if (schaak) {
          return "O-O+";
        }
        if (mat) {
          return "O-O#";
        }
        return "O-O";
      }
      if ((van - naar) == -2) {
        if (schaak) {
          return "O-O-O+";
        }
        if (mat) {
          return "O-O-O#";
        }
        return "O-O-O";
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

  /**
   * @return is het een En-Passant zet?
   */
  public boolean isEp() {
    return ep;
  }

  /**
   * @return geeft de zet mat?
   */
  public boolean isMat() {
    return mat;
  }

  /**
   * @return geeft de zet schaak?
   */
  public boolean isSchaak() {
    return schaak;
  }

  /**
   * @return is het een slagzet?
   */
  public boolean isSlagzet() {
    return (slagzet || ep);
  }

  /**
   * @param ep is het een En-Passant zet?
   */
  public void setEp(boolean ep) {
    this.ep = ep;
  }

  /**
   * @param korteNotatieLevel
   */
  public void setKorteNotatieLevel(int korteNotatieLevel) {
    this.korteNotatieLevel = korteNotatieLevel;
  }

  /**
   * @param mat geeft de zet mat?
   */
  public void setMat(boolean mat) {
    this.mat = mat;
  }

  /**
   * @param naar het naar veld
   */
  public void setNaar(int naar) {
    this.naar = naar;
  }

  /**
   * @param promotieStuk het stuk waar de pion naar promoveert
   * @throws ZetException 
   */
  public void setPromotieStuk(char promotieStuk) throws ZetException {
    if ("NBRQ".indexOf(promotieStuk) < 0) {
      throw new ZetException("PromotieStuk foutief [" + promotieStuk + "]");
    }

    this.promotieStuk = promotieStuk;
  }

  /**
   * @param schaak geeft de schaak?
   */
  public void setSchaak(boolean schaak) {
    this.schaak = schaak;
  }

  /**
   * @param slagzet is het een slagzet?
   */
  public void setSlagzet(boolean slagzet) {
    this.slagzet = slagzet;
  }

  /**
   * @param stuk het stuk dat gezet wordt
   * @throws ZetException 
   */
  public void setStuk(char stuk) throws ZetException {
    if (CaissaConstants.NOTATIE_STUKKEN.indexOf(stuk) < 0) {
      throw new ZetException("Stuk foutief [" + stuk + "]");
    }

    this.stuk = stuk;
  }

  /**
   * @param van het van veld
   */
  public void setVan(int van) {
    this.van = van;
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
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

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
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
    if (van != other.van
        || naar != other.naar
        || promotieStuk != other.promotieStuk) {
      return false;
    }

    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime   = 31;
    int       result  = super.hashCode();

    result = prime * result + (ep ? 0 : 1);
    result = prime * result + (mat ? 0 : 1);
    result = prime * result + naar;
    result = prime * result + (schaak ? 0 : 1);
    result = prime * result + (slagzet ? 0 : 1);
    result = prime * result + stuk;
    result = prime * result + van;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getLangeNotatie() + "|" + getKorteNotatie();
  }
}
