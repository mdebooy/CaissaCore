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
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;


/**
 * @author Marco de Booij
 */
public class Spelerinfo implements Comparable<Spelerinfo>, Serializable {
  private static final long serialVersionUID = 1L;

  private Date    eerstePartij;
  private Date    laatstePartij;
  private Date    maxDatum;
  private Date    minDatum;
  private Date    officieel;
  private Double  punten        = 0.0;
  private Double  tieBreakScore = 0.0;
  private Integer partijen      = 0;
  private Integer spelerId;
  private Integer elo;
  private Integer elogroei;
  private Integer maxElo;
  private Integer minElo;
  private String  alias;
  private String  email;
  private String  landKode;
  private String  naam          = "";

  public static class byNaamComparator
      implements Comparator<Spelerinfo>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Spelerinfo spelerinfo1, Spelerinfo spelerinfo2) {
      return spelerinfo1.getNaam().compareTo(spelerinfo2.getNaam());
    }
  }

  /**
   * Add punt to punten.
   * @param punt
   */
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

    int verschil  = other.punten.compareTo(this.punten);
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
  public boolean equals(Object obj) {
    if (!(obj instanceof Spelerinfo)) {
      return false;
    }

    final Spelerinfo other = (Spelerinfo) obj;

    return spelerId.equals(other.spelerId);
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

  @Override
  public int hashCode() {
    return spelerId.hashCode();
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
    this.naam = naam;
  }

  public void setOfficieel(Date officieel) {
    if (null == officieel) {
      this.officieel  = null;
    } else {
      this.officieel  = new Date(officieel.getTime());
    }
  }

  public void setPartijen(Integer partijen) {
    this.partijen = partijen;
  }

  public void setPunten(Double punten) {
    this.punten = punten;
  }

  public void setSpelerId(Integer spelerId) {
    this.spelerId = spelerId;
  }

  public void setTieBreakScore(Double tieBreakScore) {
    this.tieBreakScore  = tieBreakScore;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(spelerId).append(" - ")
                              .append(naam).append(" - ")
                              .append(landKode).append(" - ")
                              .append(elo).append(" - ")
                              .append(punten).append(" - ")
                              .append(partijen).append(" - ")
                              .append(tieBreakScore).append(" - ")
                              .append(alias).append(" - ")
                              .append(email).toString();
  }
}
