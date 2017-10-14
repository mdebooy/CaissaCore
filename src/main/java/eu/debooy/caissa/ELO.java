/**
 * Copyright 2010 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
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


/**
 * Deze class berekend de ELO rating van een speler.
 * 
 * Volgens Wikipedia:
 * 
 * Een ELO-rating is een getalsmatige aanduiding van de sterkte van een speler.
 * Het wordt het meest gebruikt in schaken en go, maar kan in principe gebruikt
 * worden bij elke sport waarbij spelers 1 tegen 1 spelen.
 * 
 * Het wiskundige systeem hiervoor is bedacht door de Amerikaanse (van Hongaarse
 * afkomst) natuurkundige en schaker Árpád Élő.
 * 
 * Elo-ratings kunnen lopen van ongeveer 1000 tot ongeveer 2800.
 * 
 * @author Marco de Booij
 */
public final class ELO {
  public static final int ELO_SWITCH    = 2400;
  public static final int KFACTOR       = 30;
  public static final int KFACTOR1      = 15;
  public static final int KFACTOR2      = 10;
  public static final int MAX_VERSCHIL  = 400;
  public static final int MIN_PARTIJEN  = 30;

  private ELO() {}

  /**
   * Deze ELO berekening volgt de recentste FIDE regels.
   * 
   * @param elo             - De ELO van de speler.
   * @param uitslag         - De uitslag (2=winst, 1=remise, 0=verlies).
   * @param eloTegenstander - De ELO van de tegenstander.
   * @param partijen        - Aantal gespeelde partijen.
   * @return
   */
  public static Integer berekenELO(Integer elo, Integer uitslag,
                                   Integer eloTegenstander, Integer partijen) {
    int kFactor = KFACTOR;
    if (partijen >= MIN_PARTIJEN) {
      if (elo < ELO_SWITCH) {
        kFactor = KFACTOR1;
      } else {
        kFactor = KFACTOR2;
      }
    }

    return berekenELO(elo, uitslag, eloTegenstander, kFactor, MAX_VERSCHIL);
  }

  /**
   * Deze ELO berekening is gebaseerd op de Engelse Wikipedia pagina:
   * https://en.wikipedia.org/wiki/Elo_rating_system#Mathematical_details
   * 
   * @param elo             - De ELO van de speler.
   * @param uitslag         - De uitslag (2=winst, 1=remise, 0=verlies).
   * @param eloTegenstander - De ELO van de tegenstander.
   * @param kFactor         - Coëfficiënt.
   * @param maxVerschil     - Maximaal verschil tussen de ratings.
   * @return
   */
  public static Integer berekenELO(Integer elo, Integer uitslag,
                                   Integer eloTegenstander, Integer kFactor,
                                   Integer maxVerschil) {
    double  eigen     = Math.pow(10, (double) elo / maxVerschil);
    double  tegen     = Math.pow(10, (double) eloTegenstander / maxVerschil);
    Integer verschil  = (int) Math.round(kFactor
                                         * (((double) uitslag / 2)
                                             - (eigen / (eigen + tegen))));

    return elo + verschil;
  }
  
  /**
   * De TPR (Tournament Playing Ratio) geeft de ELO die bepaald wordt door het
   * gescoorde aantal punten in een toernooi.
   * 
   * @param toernooiElo - Gemiddelde ELO van de spelers van het toernooi.
   * @param gewonnen    - Aantal gewonnen partijen.
   * @param remise      - Aantal remise partijen.
   * @param verloren    - Aantal verloren partijen.
   * @return de ELO rating
   */
  public static Integer berekenTPR(Integer toernooiElo, Integer gewonnen,
                                   Integer remise, Integer verloren) {
    if (gewonnen + remise + verloren == 0) {
      return null;
    }

    Float   score           = gewonnen.floatValue() + (remise.floatValue() / 2);
    Integer eloVerandering  = Integer.valueOf(0);
    Integer partijen        = Integer.valueOf(gewonnen + remise + verloren);
    Integer tpr             = 0;

    Float   percentage      = score / partijen;
    if (Math.round(percentage * 100) == 0) {
      eloVerandering  = -800;
    } else {
      if (Math.round(percentage * 100) == 100) {
        eloVerandering  = 800;
      } else {
        eloVerandering  =
          Integer.valueOf((int) Math.round(-400
                          * Math.log10((1 - percentage) / percentage)));
      }
    }
    Float correctieFactor = new Float(-2 * percentage * percentage + 2
                                      * percentage + 0.5);
    tpr                   = Integer.valueOf(Math.round(toernooiElo
                                                       + eloVerandering
                                                       * correctieFactor));

    return tpr;
  }
}
