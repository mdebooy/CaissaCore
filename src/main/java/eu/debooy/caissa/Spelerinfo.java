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

import eu.debooy.doosutils.DoosUtils;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.json.simple.JSONObject;


/**
 * @author Marco de Booij
 */
public class Spelerinfo implements Comparable<Spelerinfo>, Serializable {
  private static final long serialVersionUID = 1L;

  private Integer aantalBye     = 0;
  private String  achternaam    = "";
  private String  adres;
  private String  alias;
  private Double  byeScore      = 0.0;
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
  private Date    officieel;
  private Integer partijen      = 0;
  private String  plaats;
  private Double  punten        = 0.0;
  private Integer spelerSeq;
  private Integer spelerId;
  private String  telefoon;
  private Boolean terugronde    = true;
  private Double  tieBreakScore = 0.0;
  private String  voornaam      = "";

  public Spelerinfo() {}

  public Spelerinfo(JSONObject spelerinfo) {
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_ACHTERNAAM)) {
      achternaam  =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_ACHTERNAAM).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_ADRES)) {
      adres       =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_ADRES).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_ALIAS)) {
      alias       =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_ALIAS).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_ELO)) {
      elo         =
          Integer.valueOf(spelerinfo.get(Competitie.JSON_TAG_SPELER_ELO)
                                    .toString());
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_EMAIL)) {
      email       =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_EMAIL).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_HEENRONDE)) {
      heenronde   =
          (boolean) spelerinfo.get(Competitie.JSON_TAG_SPELER_HEENRONDE);
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_LANDKODE)) {
      landKode    =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_LANDKODE).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_NAAM)) {
      setNaam(spelerinfo.get(Competitie.JSON_TAG_SPELER_NAAM).toString());
    }
     if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_PLAATS)) {
      plaats      =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_PLAATS).toString();
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_TERUGRONDE)) {
      terugronde  =
          (boolean) spelerinfo.get(Competitie.JSON_TAG_SPELER_TERUGRONDE);
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_SPELERID)) {
      spelerId    =
          Integer.valueOf(
              spelerinfo.get(Competitie.JSON_TAG_SPELER_SPELERID)
                        .toString());
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_SPELERSEQ)) {
      spelerSeq   =
          Integer.valueOf(
              spelerinfo.get(Competitie.JSON_TAG_SPELER_SPELERSEQ)
                        .toString());
    }
    if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_TELEFOON)) {
      telefoon    =
            spelerinfo.get(Competitie.JSON_TAG_SPELER_TELEFOON).toString();
    }
     if (spelerinfo.containsKey(Competitie.JSON_TAG_SPELER_VOORNAAM)) {
      voornaam    =
          spelerinfo.get(Competitie.JSON_TAG_SPELER_VOORNAAM).toString();
    }
 }

  public Spelerinfo(Spelerinfo spelerinfo) {
    achternaam    = spelerinfo.getAchternaam();
    adres         = spelerinfo.getAdres();
    alias         = spelerinfo.getAlias();
    byeScore      = spelerinfo.getByeScore();
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
    officieel     = spelerinfo.getOfficieel();
    partijen      = spelerinfo.getPartijen();
    plaats        = spelerinfo.getPlaats();
    punten        = spelerinfo.getPunten();
    spelerId      = spelerinfo.getSpelerId();
    spelerSeq     = spelerinfo.getSpelerSeq();
    telefoon      = spelerinfo.getTelefoon();
    tieBreakScore = spelerinfo.getTieBreakScore();
    voornaam      = spelerinfo.getVoornaam();
  }

  public static class ByNaamComparator
      implements Comparator<Spelerinfo>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Spelerinfo spelerinfo1, Spelerinfo spelerinfo2) {
      return spelerinfo1.getNaam().compareToIgnoreCase(spelerinfo2.getNaam());
    }
  }

  public static class BySpelerSeqComparator
      implements Comparator<Spelerinfo>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Spelerinfo spelerinfo1, Spelerinfo spelerinfo2) {
      return spelerinfo1.getSpelerSeq().compareTo(spelerinfo2.getSpelerSeq());
    }
  }

  public void addPunt(Double punt) {
    punten += punt;
  }

  public void addPartij() {
    partijen++;
  }

  public void addPartij(Integer partijen) {
    this.partijen       += partijen;
  }

  public void addByeScore(Double byeScore) {
    aantalBye++;
    this.byeScore       += byeScore;
  }

  public void addTieBreakScore(Double tieBreakScore) {
    this.tieBreakScore  += tieBreakScore;
  }

  /**
   * De SpelerInfo moet als volgt gesorteerd worden:
   *   1 De meeste punten.
   *   2 De minste partijen.
   *   3 De hoogste tieBreakScore
   *   4 Laagste byeScore
   *   5 De 'kleinste' naam
   * De grootste moet als eerste in de lijst komen.
   * @param other
   * @return
   */
  @Override
  public int compareTo(Spelerinfo other) {
    return new CompareToBuilder().append(other.punten, punten)
                                 .append(getSorteerPartijen(),
                                         other.getSorteerPartijen())
                                 .append(other.tieBreakScore, tieBreakScore)
                                 .append(byeScore, other.byeScore)
                                 .append(getNaam().toUpperCase(),
                                         other.getNaam().toUpperCase())
                                 .toComparison();
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

  public String getAchternaam() {
    return achternaam;
  }

  public String getAdres() {
    return adres;
  }

  public String getAlias() {
    return alias;
  }

  public Double getByeScore() {
    return byeScore;
  }

  public Date getEerstePartij() {
    if (null == eerstePartij) {
      return null;
    }
    return new Date(eerstePartij.getTime());
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
    if (achternaam.isEmpty() || voornaam.isEmpty()) {
      return (achternaam+voornaam).strip();
    }

    return String.format("%s, %s", achternaam, voornaam);
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

  public String getPlaats() {
    return plaats;
  }

  public Double getPunten() {
    return punten;
  }

  private Integer getSorteerPartijen() {
    if (partijen == 0) {
      return Integer.MAX_VALUE;
    }

    return partijen;
  }

  public Integer getSpelerId() {
    return spelerId;
  }

  public Integer getSpelerSeq() {
    return spelerSeq;
  }

  public String getTelefoon() {
    return telefoon;
  }

  public Double getTieBreakScore() {
    return tieBreakScore;
  }

  public String getVolledigenaam() {
    return (voornaam + " " + achternaam).strip();
  }

  public String getVoornaam() {
    return voornaam;
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

    if (toernooiType == Competitie.TOERNOOI_MATCH) {
      return true;
    }

    if (toernooiType == Competitie.TOERNOOI_ENKEL) {
      return inHeenronde();
    }

    if (toernooiType == Competitie.TOERNOOI_DUBBEL) {
      if (ronde*2 > rondes) {
        return inTerugronde();
      }
      return inHeenronde();
    }

    return false;
  }

  public Boolean inTerugronde() {
    return terugronde;
  }

  public Boolean isBye() {
    return getNaam().equalsIgnoreCase(CaissaConstants.BYE);
  }

  public void setAchternaam(String achternaam) {
    this.achternaam       = DoosUtils.nullToEmpty(achternaam).strip();
  }

  public void setAdres(String adres) {
    this.adres            = adres.strip();
  }

  public void setAlias(String alias) {
    this.alias            = alias;
  }

  public void setEerstePartij(Date eerstePartij) {
    if (null == eerstePartij) {
      this.eerstePartij   = null;
    } else {
      this.eerstePartij   = new Date(eerstePartij.getTime());
    }
  }

  public void setElo(Integer elo) {
    this.elo              = elo;
  }

  public void setElogroei(Integer elogroei) {
    this.elogroei         = elogroei;
  }

  public void setEmail(String email) {
    this.email            = email;
  }

  public void setHeenronde(Boolean heenronde) {
    this.heenronde        = heenronde;
  }

  public void setLaatstePartij(Date laatstePartij) {
    if (null == laatstePartij) {
      this.laatstePartij  = null;
    } else {
      this.laatstePartij  = new Date(laatstePartij.getTime());
    }
  }

  public void setLandKode(String landKode) {
    this.landKode         = landKode;
  }

  public void setMaxDatum(Date maxDatum) {
    if (null == maxDatum) {
      this.maxDatum       = null;
    } else {
      this.maxDatum       = new Date(maxDatum.getTime());
    }
  }

  public void setMaxElo(Integer maxElo) {
    this.maxElo           = maxElo;
  }

  public void setMinDatum(Date minDatum) {
    if (null == minDatum) {
      this.minDatum       = null;
    } else {
      this.minDatum       = new Date(minDatum.getTime());
    }
  }

  public void setMinElo(Integer minElo) {
    this.minElo           = minElo;
  }

  public final void setNaam(String naam) {
    var deel              = DoosUtils.nullToEmpty(naam).split(",");

      achternaam  = deel[0].strip();
      if (deel.length > 1) {
        voornaam  = deel[1].strip();
      } else {
        voornaam  = "";
      }
  }

  public void setOfficieel(Date officieel) {
    if (null == officieel) {
      this.officieel      = null;
    } else {
      this.officieel      = new Date(officieel.getTime());
    }
  }

  public void setPartijen(Integer partijen) {
    if (null == partijen) {
      this.partijen       = 0;
    } else {
      this.partijen       = partijen;
    }
  }

  public void setPlaats(String plaats) {
    this.plaats           = DoosUtils.nullToEmpty(plaats).strip();
  }

  public void setPunten(Double punten) {
    if (null == punten) {
      this.punten         = 0.0d;
    } else {
      this.punten         = punten;
    }
  }

  public void setSpelerId(Integer spelerId) {
    this.spelerId         = spelerId;
  }

  public void setSpelerSeq(Integer spelerSeq) {
    this.spelerSeq        = spelerSeq;
  }

  public void setTerugronde(Boolean terugronde) {
    this.terugronde       = terugronde;
  }

  public void setTelefoon(String telefoon) {
    this.telefoon         = DoosUtils.nullToEmpty(telefoon).strip();
  }

  public void setTieBreakScore(Double tieBreakScore) {
    if (null == tieBreakScore) {
      this.tieBreakScore  = 0.0d;
    } else {
      this.tieBreakScore  = tieBreakScore;
    }
  }

  public void setVoornaam(String voornaam) {
    this.voornaam         = DoosUtils.nullToEmpty(voornaam).strip();
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Spelerinfo data (")
        .append("SpelerID: ").append(spelerId).append(", ")
        .append("SpelerSeq: ").append(spelerSeq).append(", ")
        .append("naam: [").append(getNaam()).append("], ")
        .append("landkode: ").append(landKode).append(", ")
        .append("ELO: ").append(elo).append(", ")
        .append("punten: ").append(punten).append(", ")
        .append("partijen: ").append(partijen).append(", ")
        .append("byeScore: ").append(byeScore).append(", ")
        .append("tieBreakScore: ").append(tieBreakScore).append(", ")
        .append("alias: ").append(alias).append(", ")
        .append("email: ").append(email).append(")").toString();
  }
}
