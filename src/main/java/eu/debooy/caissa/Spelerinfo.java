/**
 * Copyright 2008 Marco de Booij
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
import java.util.Date;


/**
 * @author Marco de Booij
 */
public class Spelerinfo implements Comparable<Spelerinfo>, Serializable {
  private static final long serialVersionUID = 1L;

  private Date    officieel;
  private Date    maxDatum;
  private Date    minDatum;
  private Double  punten            = 0.0;
  private Double  weerstandspunten  = 0.0;
  private Integer partijen          = 0;
  private Integer spelerId;
  private Integer elo;
  private Integer maxElo;
  private Integer minElo;
  private String  landKode;
  private String  naam              = "";

  /**
   * Add punt to punten.
   * @param punt
   */
  public void addPunt(Double punt) {
    punten += punt;
  }

  /**
   * Add 1 to partijen.
   */
  public void addPartij() {
    partijen++;
  }

  /**
   * Add weerstandspunten to weerstandspunten.
   * @param weerstandspunten
   */
  public void addWeerstandspunten(Double weerstandspunten) {
    this.weerstandspunten += weerstandspunten;
  }

  /**
   * De SpelerInfo moet als volgt gesorteerd worden:
   *   1 De meeste punten.
   *   2 De minste partijen.
   *   3 De meeste weerstandspunten
   *   4 De 'kleinste' naam
   * De grootste moet als eerste in de lijst komen. 
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
    verschil  = other.weerstandspunten.compareTo(this.weerstandspunten);
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
    if (!spelerId.equals(other.spelerId)) {
      return false;
    }

    return true;
  }

  /**
   * @return the elo
   */
  public Integer getElo() {
    return elo;
  }

  /**
   * @return the landKode
   */
  public String getLandKode() {
    return landKode;
  }

  /**
   * @return the maxDatum
   */
  public Date getMaxDatum() {
    if (null == maxDatum) {
      return null;
    }
    return (Date) maxDatum.clone();
  }

  /**
   * @return the maxElo
   */
  public Integer getMaxElo() {
    return maxElo;
  }

  /**
   * @return the minDatum
   */
  public Date getMinDatum() {
    if (null == minDatum) {
      return null;
    }
    return (Date) minDatum.clone();
  }

  /**
   * @return the minElo
   */
  public Integer getMinElo() {
    return minElo;
  }

  /**
   * @return the naam
   */
  public String getNaam() {
    return naam;
  }

  /**
   * @return the officiel
   */
  public Date getOfficieel() {
    if (null == officieel) {
      return null;
    }
    return (Date) officieel.clone();
  }

  /**
   * @return the partijen
   */
  public Integer getPartijen() {
    return partijen;
  }

  /**
   * @return the punten
   */
  public Double getPunten() {
    return punten;
  }

  /**
   * @return the spelerId
   */
  public Integer getSpelerId() {
    return spelerId;
  }

  /**
   * @return the weerstandspunten
   */
  public Double getWeerstandspunten() {
    return weerstandspunten;
  }

  @Override
  public int hashCode() {
    return spelerId.hashCode();
  }

  /**
   * @param elo the elo to set
   */
  public void setElo(Integer elo) {
    this.elo = elo;
  }

  /**
   * @param landKode the landKode to set
   */
  public void setLandKode(String landKode) {
    this.landKode = landKode;
  }

  /**
   * @param maxDatum the maxDatum to set
   */
  public void setMaxDatum(Date maxDatum) {
    if (null == maxDatum) {
      this.maxDatum = null;
    } else {
      this.maxDatum = (Date) maxDatum.clone();
    }
  }

  /**
   * @param maxElo the maxElo to set
   */
  public void setMaxElo(Integer maxElo) {
    this.maxElo = maxElo;
  }

  /**
   * @param minDatum the minDatum to set
   */
  public void setMinDatum(Date minDatum) {
    if (null == minDatum) {
      this.minDatum = null;
    } else {
      this.minDatum = (Date) minDatum.clone();
    }
  }

  /**
   * @param minElo the minElo to set
   */
  public void setMinElo(Integer minElo) {
    this.minElo = minElo;
  }

  /**
   * @param naam the naam to set
   */
  public void setNaam(String naam) {
    this.naam = naam;
  }

  /**
   * @param officiel the officiel to set
   */
  public void setOfficieel(Date officieel) {
    if (null == officieel) {
      this.officieel  = null;
    } else {
      this.officieel  = (Date) officieel.clone();
    }
  }

  /**
   * @param partijen the partijen to set
   */
  public void setPartijen(Integer partijen) {
    this.partijen = partijen;
  }

  /**
   * @param punten the punten to set
   */
  public void setPunten(Double punten) {
    this.punten = punten;
  }

  /**
   * @param spelerId the spelerId to set
   */
  public void setSpelerId(Integer spelerId) {
    this.spelerId = spelerId;
  }

  /**
   * @param weerstandspunten the weerstandspunten to set
   */
  public void setWeerstandspunten(Double weerstandspunten) {
    this.weerstandspunten = weerstandspunten;
  }

  @Override
  public String toString() {
    StringBuilder result  = new StringBuilder();

    result.append(spelerId).append(" - ")
          .append(naam).append(" - ")
          .append(landKode).append(" - ")
          .append(elo).append(" - ")
          .append(punten).append(" - ")
          .append(partijen).append(" - ")
          .append(weerstandspunten).append(" - ");

    return result.toString();
  }
}
