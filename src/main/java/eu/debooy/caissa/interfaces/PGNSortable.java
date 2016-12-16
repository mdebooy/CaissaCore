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
package eu.debooy.caissa.interfaces;

import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.doosutils.SortStrategy;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * @author Marco de Booij
 * @deprecated Gebruik de Comparator in de PGN class.
 */
@Deprecated
public interface PGNSortable<T> extends Comparable<T>, Serializable {
  /**
   * @param tag de toe te voegen tag
   * @param value de waarde van de tag
   * @throws PgnException 
   */
  void addTag(String tag, String value) throws PgnException;

  /**
   * @param tag de te verwijderen tag
   */
  void deleteTag(String tag);

  /**
   * Geeft de "Date" tag als java.util.Date
   * @return Date
   */
  Date getDate();

  /**
   * Geeft de "EventDate" tag als java.util.Date
   * @return Date
   */
  Date getEventDate();

  /**
   * @param tag de tag waarvoor de value gevraagd wordt
   * @return de value van de tag
   */
  String getTag(String tag);

  /**
   * @return de tags
   */
  Map<String, String> getTags();

  /**
   * @return de zetten
   */
  String getZetten();

  /**
   * @param naarStukken the values of the tukken
   * @return the zetten in with the pieces in naarStukken
   */
  String getZetten(String naarStukken) throws PgnException;

  /**
   * Controleert of de PGN volledig is en de TAG Date correct.
   */
  boolean isValid();

  /**
   * @param sortStrategy
   */
  void setSortStrategy(SortStrategy sortStrategy);

  /**
   * @param tag de toe te voegen tag
   * @param value de waarde van de tag
   * @throws PgnException 
   */
  void setTag(String tag, String value) throws PgnException;

  /**
   * @param tag de toe te voegen tags
   * @throws PgnException 
   */
  void setTags(Map<String, String> tags) throws PgnException;

  /**
   * @param zetten de zetten van de partij
   */
  void setZetten(String zetten);

  /**
   * Deze method vertaald de stukken van een taal in de standaard taal.
   * 
   * @param zetten de zetten van de partij
   * @param vanStukken the waardes van de stukken in zetten
   */
  void setZetten(String zetten, String vanStukken) throws PgnException;
  /**
   * Compare this object to another object
   * 
   * @param other een ander PGN object
   * @return -1, 0 of 1
   */
  int compareTo(T other);
}
