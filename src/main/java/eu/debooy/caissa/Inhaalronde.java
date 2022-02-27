/*
 * Copyright (c) 2022 Marco de Booij
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

import static eu.debooy.caissa.CaissaUtils.resourceBundle;
import eu.debooy.caissa.exceptions.CaissaException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * @author Marco de Booij
 */
public class Inhaalronde implements Comparable<Inhaalronde>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COD_INH_SPELER  = "INH-0001";
  public static final String  COD_INH_SPELERS = "INH-0002";

  public static final String  ERR_INH_SPELER  = "inhaalronde.speler.dubbel";
  public static final String  ERR_INH_SPELERS = "inhaalronde.spelers.dubbel";

  private Integer ronde;
  private String  speeldatum  = CaissaConstants.DEF_EINDDATUM;

  private final List<String>  spelers   = new ArrayList<>();
  private final List<Partij>  partijen  = new ArrayList<>();

  public static class ByRonde
      implements Comparator<Inhaalronde>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Inhaalronde inhaalronde1, Inhaalronde inhaalronde2) {
      return new CompareToBuilder()
                    .append(inhaalronde1.getRonde(),
                            inhaalronde2.getRonde())
                    .append(inhaalronde1.getSpeeldatum().replace("?", "9"),
                            inhaalronde2.getSpeeldatum().replace("?", "9"))
                    .toComparison();
    }
  }

  public static class BySpeeldatum
      implements Comparator<Inhaalronde>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Inhaalronde inhaalronde1, Inhaalronde inhaalronde2) {
      return new CompareToBuilder()
                    .append(inhaalronde1.getSpeeldatum().replace("?", "9"),
                            inhaalronde2.getSpeeldatum().replace("?", "9"))
                    .append(inhaalronde1.getRonde(),
                            inhaalronde2.getRonde())
                    .toComparison();
    }
  }

  public void addPartij(Partij partij) throws CaissaException {
    var witspeler   = partij.getWitspeler().getNaam();
    var zwartspeler = partij.getZwartspeler().getNaam();
    var afgewezen   = new ArrayList<String>();
    if (spelers.contains(witspeler)) {
      afgewezen.add(witspeler);
    }
    if (spelers.contains(zwartspeler)) {
      afgewezen.add(zwartspeler);
    }
    if (afgewezen.isEmpty()) {
      partijen.add(partij);
      spelers.add(witspeler);
      spelers.add(zwartspeler);
      return;
    }

    if (afgewezen.size() == 1) {
      throw new CaissaException(MessageFormat.format(
                resourceBundle.getString(ERR_INH_SPELER), afgewezen.get(0)));
    } else {
      throw new CaissaException(MessageFormat.format(
                    resourceBundle.getString(ERR_INH_SPELERS),
                    afgewezen.get(0), afgewezen.get(1)));
    }
  }

  @Override
  public int compareTo(Inhaalronde andere) {
    return new CompareToBuilder().append(ronde, andere.ronde)
                                 .build();
  }

  @Override
  public boolean equals(Object andere) {
    if (!(andere instanceof Inhaalronde)) {
      return false;
    }

    if (this == andere) {
      return true;
    }

    return ronde.equals(((Inhaalronde) andere).ronde);
  }

  public Integer getAantalPartijen() {
    return partijen.size();
  }

  public Integer getRonde() {
    return ronde;
  }

  public String getSpeeldatum() {
    return speeldatum;
  }

  public List<Partij> getPartijen() {
    return List.copyOf(partijen);
  }

  @Override
  public int hashCode() {
    return ronde.hashCode();
  }

  public void setRonde(Integer ronde) {
    this.ronde      = ronde;
  }

  public void setSpeeldatum(String speeldatum) {
    this.speeldatum = speeldatum;
  }
}
