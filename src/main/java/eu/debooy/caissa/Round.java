/**
 * Copyright 2011 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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

/**
 * Deze class wordt gebruikt voor de PGN Tag Round. Met behulp van deze class
 * kan er op de Round gesorteerd worden.
 * 
 * De Round Tag geeft de ronde van het toernooi of de match. Als de ronde
 * onbekend is dan wordt een vraagteken gebruikt. Als er geen ronde is dan wordt
 * een streepje gebruikt.
 * Soms bestaat de Round uit meerdere delen. Dit zijn cijfers gescheiden door
 * een punt. Het linkse cijfer is het belangrijkste cijfer.
 * 
 * Voor het sorteren komt het vraagteken voor het streepje dat dan weer voor de
 * 'echte' ronde.
 * 
 * @author Marco de Booij
 */
public class Round implements Comparable<Round>, Cloneable {
  private String  round = "";

  /**
   * Constructor zonder parameter.
   */
  public Round() {}

  /**
   * Constructor met round als parameter.
   * 
   * @param round
   */
  public Round(String round) {
    super();
    this.round = round;
  }

  /**
   * @return de round
   */
  public String getRound() {
    return round;
  }

  /**
   * @param round de waarde van de round
   */
  public void setRound(String round) {
    this.round = round;
  }

  public int compareTo(Round other) {
    if (this.equals(other)) {
      return 0;
    }

    String[]  thisRound   = getRound().split("\\.");
    String[]  otherRound  = other.getRound().split("\\.");

    int min = 0;
    if (thisRound.length < otherRound.length) {
      min = thisRound.length;
    } else {
      min = otherRound.length;
    }

    for (int i = 0; i < min; i++) {
      int diff  = Integer.valueOf(thisRound[i].replace("-", "-1")
                                              .replace("?", "-2"))
                         .compareTo(
                  Integer.valueOf(otherRound[i].replace("-", "-1")
                                               .replace("?", "-2")));
      if (diff != 0) {
        return diff;
      }
    }

    return Integer.valueOf(thisRound.length)
                  .compareTo(Integer.valueOf(otherRound.length));
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Round)) {
      return false;
    }
    Round other = (Round) obj;
    return other.getRound().equals(getRound());
  }

  @Override
  public int hashCode() {
    return round.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder result  = new StringBuilder();
    result.append("Round data (");
    result.append("round: ");
    result.append("[").append(getRound()).append("])");

    return result.toString();
  } 
}
