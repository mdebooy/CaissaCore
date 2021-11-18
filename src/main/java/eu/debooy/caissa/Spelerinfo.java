/**
 * Copyright (c) 2008 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.caissa;

import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.DoosUtils;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;


/**
 * @author Marco de Booij
 */
public class Spelerinfo implements Comparable<Spelerinfo>, Serializable {
  private static final long serialVersionUID = 1L;

  private String  alias;
  private Date    eerstePartij;
  private Integer elo;
  private Integer elogroei;
  private String  email;
  private Boolean heenronde     = true;
  private Date    laatstePartij;
  private String  landKode;
  private Date    maxDatum;
  private Integer maxElo;
  private Date    minDatum;
  private Integer minElo;
  private String  naam          = "";
  private Date    officieel;
  private Integer partijen      = 0;
  private Double  punten        = 0.0;
  private Integer spelerId;
  private Boolean terugronde    = true;
  private Double  tieBreakScore = 0.0;

  public Spelerinfo() {}

  public Spelerinfo(Spelerinfo spelerinfo) {
    alias         = spelerinfo.getAlias();
    eerstePartij  = spelerinfo.getEerstePartij();
    elo           = spelerinfo.getElo();
    elogroei      = spelerinfo.getElogroei();
    email         = spelerinfo.getEmail();
    laatstePartij = spelerinfo.getLaatstePartij();
    landKode      = spelerinfo.getLandKode();
    maxDatum      = spelerinfo.getMaxDatum();
    maxElo        = spelerinfo.getMaxElo();
    minDatum      = spelerinfo.getMinDatum();
    minElo        = spelerinfo.getMinElo();
    naam          = spelerinfo.getNaam();
    officieel     = spelerinfo.getOfficieel();
    partijen      = spelerinfo.getPartijen();
    punten        = spelerinfo.getPunten();
    spelerId      = spelerinfo.getSpelerId();
    tieBreakScore = spelerinfo.getTieBreakScore();
  }

  public static class ByNaamComparator
      implements Comparator<Spelerinfo>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Spelerinfo spelerinfo1, Spelerinfo spelerinfo2) {
      return spelerinfo1.getNaam().compareToIgnoreCase(spelerinfo2.getNaam());
    }
  }

  public void addPunt(Double punt) {
    punten += punt;
  }

  public void addPartij() {
    partijen++;
  }

  public void addTieBreakScore(Double tieBreakScore) {
    this.tieBreakScore  += tieBreakScore;
  }

  /**
   * De SpelerInfo moet als volgt gesorteerd worden:
   *   1 De meeste punten.
   *   2 De minste partijen.
   *   3 De meeste tieBreakScore
   *   4 De 'kleinste' naam
   * De grootste moet als eerste in de lijst komen.
   * @param other
   */
  @Override
  public int compareTo(Spelerinfo other) {
    if (this == other) {
      return DoosConstants.EQUAL;
    }

    var verschil  = other.punten.compareTo(this.punten);
    if (verschil != 0) {
      return verschil;
    }
    verschil  = this.partijen.compareTo(other.partijen);
    if (verschil != 0) {
      return verschil;
    }
    verschil  = other.tieBreakScore.compareTo(this.tieBreakScore);
    if (verschil != 0) {
      return verschil;
    }

    return this.naam.compareToIgnoreCase(other.naam);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Spelerinfo)) {
      return false;
    }

    if (this == other) {
      return true;
    }

    return spelerId.equals(((Spelerinfo) other).spelerId);
  }

  public Date getEerstePartij() {
    if (null == eerstePartij) {
      return null;
    }
    return new Date(eerstePartij.getTime());
  }

  public String getAlias() {
    return alias;
  }

  public Integer getElo() {
    return elo;
  }

  public Integer getElogroei() {
    return elogroei;
  }

  public String getEmail() {
    return email;
  }

  public Date getLaatstePartij() {
    if (null == laatstePartij) {
      return null;
    }
    return new Date(laatstePartij.getTime());
  }

  public String getLandKode() {
    return landKode;
  }

  public Date getMaxDatum() {
    if (null == maxDatum) {
      return null;
    }
    return new Date(maxDatum.getTime());
  }

  public Integer getMaxElo() {
    return maxElo;
  }

  public Date getMinDatum() {
    if (null == minDatum) {
      return null;
    }
    return new Date(minDatum.getTime());
  }

  public Integer getMinElo() {
    return minElo;
  }

  public String getNaam() {
    return naam;
  }

  public Date getOfficieel() {
    if (null == officieel) {
      return null;
    }

    return new Date(officieel.getTime());
  }

  public Integer getPartijen() {
    return partijen;
  }

  public Double getPunten() {
    return punten;
  }

  public Integer getSpelerId() {
    return spelerId;
  }

  public Double getTieBreakScore() {
    return tieBreakScore;
  }

  public String getVolledigenaam() {
    var delen = naam.split(",");
    if (delen.length == 1) {
      return naam;
    }

    return delen[1].trim() + " " + delen[0].trim();
  }

  public String getVoornaam() {
    String[] delen = naam.split(",");

    return delen[delen.length-1].trim();
  }

  @Override
  public int hashCode() {
    return spelerId.hashCode();
  }

  public Boolean inHeenronde() {
    return heenronde;
  }

  /**
   * De heenronde en terugronde worden genegeerd voor een match.
   *
   * @param ronde
   * @param rondes
   * @param toernooiType
   * @return
   */
  public boolean inRonde(int ronde, int rondes, int toernooiType) {
    if (ronde > rondes) {
      return false;
    }

    switch (toernooiType) {
      case CaissaConstants.TOERNOOI_MATCH:
        return true;
      case CaissaConstants.TOERNOOI_ENKEL:
        return inHeenronde();
      case CaissaConstants.TOERNOOI_DUBBEL:
        if (ronde*2 > rondes) {
          return inTerugronde();
        }
        return inHeenronde();
      default:
        return false;
    }
  }

  public Boolean inTerugronde() {
    return terugronde;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setEerstePartij(Date eerstePartij) {
    if (null == eerstePartij) {
      this.eerstePartij = null;
    } else {
      this.eerstePartij = new Date(eerstePartij.getTime());
    }
  }

  public void setElo(Integer elo) {
    this.elo = elo;
  }

  public void setElogroei(Integer elogroei) {
    this.elogroei  = elogroei;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setHeenronde(Boolean heenronde) {
    this.heenronde  = heenronde;
  }

  public void setLaatstePartij(Date laatstePartij) {
    if (null == laatstePartij) {
      this.laatstePartij  = null;
    } else {
      this.laatstePartij  = new Date(laatstePartij.getTime());
    }
  }

  public void setLandKode(String landKode) {
    this.landKode = landKode;
  }

  public void setMaxDatum(Date maxDatum) {
    if (null == maxDatum) {
      this.maxDatum = null;
    } else {
      this.maxDatum = new Date(maxDatum.getTime());
    }
  }

  public void setMaxElo(Integer maxElo) {
    this.maxElo = maxElo;
  }

  public void setMinDatum(Date minDatum) {
    if (null == minDatum) {
      this.minDatum = null;
    } else {
      this.minDatum = new Date(minDatum.getTime());
    }
  }

  public void setMinElo(Integer minElo) {
    this.minElo = minElo;
  }

  public void setNaam(String naam) {
    this.naam = DoosUtils.nullToEmpty(naam);
  }

  public void setOfficieel(Date officieel) {
    if (null == officieel) {
      this.officieel  = null;
    } else {
      this.officieel  = new Date(officieel.getTime());
    }
  }

  public void setPartijen(Integer partijen) {
    if (null == partijen) {
      this.partijen = 0;
    } else {
      this.partijen = partijen;
    }
  }

  public void setPunten(Double punten) {
    if (null == punten) {
      this.punten = 0.0d;
    } else {
      this.punten = punten;
    }
  }

  public void setSpelerId(Integer spelerId) {
    this.spelerId = spelerId;
  }

  public void setTerugronde(Boolean terugronde) {
    this.terugronde  = terugronde;
  }

  public void setTieBreakScore(Double tieBreakScore) {
    if (null == tieBreakScore) {
      this.tieBreakScore  = 0.0d;
    } else {
      this.tieBreakScore  = tieBreakScore;
    }
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Spelerinfo data (")
        .append("SpelerID: ").append(spelerId).append(", ")
        .append("naam: [").append(naam).append("], ")
        .append("landkode: ").append(landKode).append(", ")
        .append("ELO: ").append(elo).append(", ")
        .append("punten: ").append(punten).append(", ")
        .append("partijen: ").append(partijen).append(", ")
        .append("tieBreakScore: ").append(tieBreakScore).append(", ")
        .append("alias: ").append(alias).append(", ")
        .append("email: ").append(email).append(")").toString();
  }
}
