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
package eu.debooy.caissa.sorteer;

import eu.debooy.caissa.CaissaConstants;
import eu.debooy.caissa.Round;
import eu.debooy.caissa.interfaces.PGNSortable;
import eu.debooy.doosutils.SortStrategy;

import java.io.Serializable;


/**
 * @author Marco de Booij
 * @deprecated Gebruik de Comparator in de PGN class.
 */
@Deprecated
public class PGNSortDefault implements SortStrategy, Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Vergelijk 2 PGN objecten
   * 
   * @param obj1 een PGN object
   * @param obj2 een PGN object
   * @retrun -1, 0 of 1
   */
  @Override
  public int sortingAlgorithm(Object obj1, Object obj2) {
    PGNSortable<?>  pgn1  = (PGNSortable<?>) obj1;
    PGNSortable<?>  pgn2  = (PGNSortable<?>) obj2;

    // 1e sleutel
    int         diff  = pgn1.getTag(CaissaConstants.PGNTAG_DATE)
                            .replace('?', '0')
                            .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_DATE)
                                           .replace('?', '0'));
    if (diff != 0) {
      return diff;
    }
    // 2e sleutel
    diff  = pgn1.getTag(CaissaConstants.PGNTAG_EVENT)
                .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_EVENT));
    if (diff != 0) {
      return diff;
    }
    // 3e sleutel
    diff  = pgn1.getTag(CaissaConstants.PGNTAG_SITE)
                .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_SITE));
    if (diff != 0) {
      return diff;
    }
    // 4e sleutel
    Round round1  = new Round(pgn1.getTag(CaissaConstants.PGNTAG_ROUND));
    Round round2  = new Round(pgn2.getTag(CaissaConstants.PGNTAG_ROUND));
    diff  = round1.compareTo(round2);
    if (diff != 0) {
      return diff;
    }
    // 5e sleutel
    diff  = pgn1.getTag(CaissaConstants.PGNTAG_WHITE)
                .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_WHITE));
    if (diff != 0) {
      return diff;
    }
    // 6e sleutel
    diff  = pgn1.getTag(CaissaConstants.PGNTAG_BLACK)
                .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_BLACK));
    if (diff != 0) {
      return diff;
    }
    // 7e sleutel
    diff  = pgn1.getTag(CaissaConstants.PGNTAG_RESULT)
                .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_RESULT));
    if (diff != 0) {
      return diff;
    }
    // 8e sleutel
    return pgn1.getZetten().compareTo(pgn2.getZetten());
  }

  /**
   * Geeft de sortering.
   * 
   * @return beschrijving van de sortering.
   */
  @Override
  public String toString() {
    return "Standaard Sortering. Date, Event, Site, Round, White, Black, "
           + "Result en Zetten";
  }
}
