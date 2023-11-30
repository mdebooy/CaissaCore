/*
 * Copyright (c) 2021 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.caissa;

import eu.debooy.doosutils.DoosUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * @author Marco de Booij
 */
public class Partij implements Comparable<Partij> {
  private boolean     forfait     = false;
  private boolean     ranked      = true;
  private Round       ronde;
  private String      uitslag;
  private Spelerinfo  witspeler;
  private Spelerinfo  zwartspeler;

  @Override
  public int compareTo(Partij partij) {
    return new CompareToBuilder().append(ronde, partij.ronde)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Partij)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var partij  = (Partij) object;
    return new EqualsBuilder().append(ronde, partij.ronde)
                              .isEquals();
  }

  public Round getRonde() {
    return ronde;
  }

  public String getUitslag() {
    if (null == uitslag) {
      return CaissaConstants.PARTIJ_BEZIG;
    }

    return uitslag;
  }

  public Spelerinfo getWitspeler() {
    return witspeler;
  }

  public Spelerinfo getZwartspeler() {
    return zwartspeler;
  }

  @Override
  public int hashCode() {
    return ronde.hashCode();
  }

  public boolean isBye() {
    return (CaissaConstants.BYE.equalsIgnoreCase(witspeler.getNaam())
        || CaissaConstants.BYE.equalsIgnoreCase(zwartspeler.getNaam())
        || DoosUtils.isBlankOrNull(witspeler.getNaam())
        || DoosUtils.isBlankOrNull(zwartspeler.getNaam()));
  }

  public boolean isForfait() {
    return forfait;
  }

  public boolean isGespeeld() {
    return DoosUtils.isNotBlankOrNull(uitslag)
        && !uitslag.equals(CaissaConstants.PARTIJ_BEZIG);
  }

  public boolean isRanked() {
    return ranked;
  }

  public void setForfait(boolean forfait) {
    this.forfait = forfait;
  }

  public void setRanked(boolean ranked) {
    this.ranked = ranked;
  }

  public void setRonde(Round ronde) {
    this.ronde = ronde;
  }

  public void setRonde(int ronde, int partijnummer) {
    this.ronde = new Round("" + ronde + "." + partijnummer);
  }

  public void setUitslag(String uitslag) {
    this.uitslag = uitslag;
  }

  public void setWitspeler(Spelerinfo witspeler) {
    this.witspeler = witspeler;
  }

  public void setZwartspeler(Spelerinfo zwartspeler) {
    this.zwartspeler = zwartspeler;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Partij data (")
        .append("ronde: [")
        .append(ronde.getRound()).append("], ")
        .append("witspeler: ")
        .append(witspeler.getVolledigenaam()).append(", ")
        .append("zwartspeler: ")
        .append(zwartspeler.getVolledigenaam()).append(", ")
        .append("uitslag: ").append(getUitslag()).append(", ")
        .append("bye: ").append(isBye()).append(", ")
        .append("forfait: ").append(isForfait()).append(", ")
        .append("ranked: ").append(isRanked()).append(")").toString();
  }
}
