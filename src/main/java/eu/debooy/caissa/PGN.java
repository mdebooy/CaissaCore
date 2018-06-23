/**
 * Copyright 2008 Marco de Booij
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

import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.doosutils.Datum;
import eu.debooy.doosutils.DoosUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Deze class bevat een partij in PGN notatie met bijbehorende TAGs.
 * 
 * Volgens Wikipedia:
 *
 * Portable Game Notation(PGN) is een vorm schaaknotatie waarbij de gegevens van
 * schaakpartijen worden opgeslagen in ASCII-bestanden. Het is in de jaren
 * tachtig bedacht door een groep Internetgebruikers die beoogden de
 * uitwisselbaarheid van gegevens tussen schaaksoftware te vergroten. PGN heeft
 * zich ontwikkeld tot een standaard. Praktische elk schaakprogramma en elke
 * schaakdatabase kan PGN importeren of exporteren en vele websites stellen
 * partijenverzamelingen in PGN beschikbaar.
 * 
 * Een PGN bestand bestaat uitsluitend uit ASCII-karakters. De standaard
 * extensie van een bestand is .pgn. Een zinvol PGN bestand bestaat uit één of
 * meer partijen. Elke partij bestaat uit twee delen, de Tags en de Movetext.
 * 
 * Een Tag bestaat uit een linker blokhaak, een symbool, een waarde (tussen
 * dubbele aanhalingstekens) en een rechterblokhaak. Er zijn zeven Tags
 * verplicht, in deze volgorde, (dit is de STR, ofwel Seven Tag Roster):
 * 1. Event. De naam van het toernooi of de match, "?" als dat niet bekend is.
 * 2. Site. De plaats waar de partij werd gespeeld en de IOC Landcode, "?" als
 *    dat niet bekend is.
 * 3. Date. De datum waarop de partij begonnen is, in het formaat "YYYY.MM.DD".
 *    Onbekende gegevens worden vervangen door vraagtekens, bijvoorbeeld
 *    [Date "1904.??.??"].
 * 4. Round. De ronde van het toernooi of de match.
 * 5. White. De naam van de witspeler in de vorm "Achternaam, Voornaam" of
 *    "Computerprogramma versienr".
 * 6. Black. Hetzelfde voor de zwartspeler.
 * 7. Result. De uitslag. Dit kan zijn "1-0", "1/2-1/2", "0-1" of "*"(onbeslist,
 *    betekent meestal dat de partij nog bezig is).
 * 
 * Na deze Tags kunnen er nog meer volgen, veel gebruikt zijn:
 * - WhiteElo De Elo-rating van de witspeler.
 * - BlackElo De Elo-rating van de zwartspeler.
 * - ECO De opening volgens de ECO-code.
 * 
 * In het Movetext-gedeelte worden de zetten van de partij weergegeven in de
 * korte algebraïsche notatie, met Engelse aanduiding van de stukken. Dus K voor
 * koning, Q voor dame, R voor toren, B voor loper en N voor paard. Rokade wordt
 * aangegeven met "O-O" (korte rokade) of "O-O-O" (lange rokade). Let op dat dit
 * hoofdletter O's zijn, en niet nullen (dus niet "0-0" of "0-0-0"). Promotie
 * wordt aangegeven door "=" gevolgd door het stuk waarnaar gepromoveerd wordt.
 * Als wit bijvoorbeeld zijn pion van c7 naar c8 zet en promoveert tot paard,
 * wordt dit dus "c8=N". Schaak wordt aangeduid met "+" en mat met "#". Andere
 * tekens, zoals ?, ! en dergelijke worden niet gebruikt.
 * 
 * Commentaar kan worden toegevoegd tussen accolades. Commentaar begint met een
 * { en loopt door tot de eerste }.
 * 
 * De partij moet worden afgesloten door het resultaat. Dat moet hetzelfde zijn
 * als aangegeven bij de Tag Result.
 * 
 * @author Marco de Booij
 */
public class PGN implements Comparable<PGN>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private boolean       ranked          = true;
  private boolean       rated           = true;
  private Map<String, String>
                        tags            =
      new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
  private String[]      annotaties      = {"+-", "-+", "+--", "--+", "+/-",
                                           "-/+", "+=", "=+", "~", "?", "!",
                                           " ="};
  private String[]      uitslagen       = {CaissaConstants.PARTIJ_WIT_WINT,
                                           CaissaConstants.PARTIJ_ZWART_WINT,
                                           CaissaConstants.PARTIJ_REMISE,
                                           CaissaConstants.PARTIJ_BEZIG};
  private String[]      sevenTagRoster  = {CaissaConstants.PGNTAG_EVENT,
                                           CaissaConstants.PGNTAG_SITE,
                                           CaissaConstants.PGNTAG_DATE,
                                           CaissaConstants.PGNTAG_ROUND,
                                           CaissaConstants.PGNTAG_WHITE,
                                           CaissaConstants.PGNTAG_BLACK,
                                           CaissaConstants.PGNTAG_RESULT};
  private String        stukken         = CaissaConstants.STUKKEN;
  private String        zetten          = "";
  private String        zuivereZetten   = "";

  public PGN() {
    Arrays.sort(sevenTagRoster);
    Arrays.sort(uitslagen);
  }

  public PGN(PGN pgn) throws PgnException {
    Arrays.sort(sevenTagRoster);
    Arrays.sort(uitslagen);

    ranked        = pgn.ranked;
    rated         = pgn.rated;
    for (Entry<String, String> tag : pgn.getTags().entrySet()) {
      addTag(tag.getKey(), tag.getValue());
    }
    zetten        = pgn.getZetten();
  }

  /**
   * Sorteren op Event, Site, Round, Date, White, Black, Result, zetten.
   */
  public static class byEventComparator
      implements Comparator<PGN>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    public int compare(PGN pgn1, PGN pgn2) {
      // 1e sleutel
      int diff  = pgn1.getTag(CaissaConstants.PGNTAG_EVENT)
                      .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_EVENT));
      if (diff != 0) {
        return diff;
      }
      // 2e sleutel
      diff  = pgn1.getTag(CaissaConstants.PGNTAG_SITE)
                  .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_SITE));
      if (diff != 0) {
        return diff;
      }
      // 3e sleutel
      Round round1  = new Round(pgn1.getTag(CaissaConstants.PGNTAG_ROUND));
      Round round2  = new Round(pgn2.getTag(CaissaConstants.PGNTAG_ROUND));
      diff  = round1.compareTo(round2);
      if (diff != 0) {
        return diff;
      }
      // 4e sleutel
      diff  = pgn1.getTag(CaissaConstants.PGNTAG_DATE).replace('?', '0')
                  .compareTo(pgn2.getTag(CaissaConstants.PGNTAG_DATE)
                                 .replace('?', '0'));
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
  }

  /**
   * Sorteren op Date, Event, Site, Round, White, Black, Result, zetten.
   */
  public static class defaultComparator
      implements Comparator<PGN>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    public int compare(PGN pgn1, PGN pgn2) {
      // 1e sleutel
      int   diff    = pgn1.getTag(CaissaConstants.PGNTAG_DATE)
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
  }

  /**
   * @param tag de toe te voegen tag
   * @param value de waarde van de tag
   * @throws PgnException 
   */
  public void addTag(String tag, String value) throws PgnException {
    tags.put(tag, value);

    if (CaissaConstants.PGNTAG_RESULT.equals(tag)
        && Arrays.binarySearch(uitslagen, value) < 0) {
      throw new PgnException("Verkeerde uitslag.");
    }
  }

  /**
   * Vergelijk deze PGN met een andere
   * 
   * @param other een ander PGN object
   * @return -1, 0 of 1
   */
  public int compareTo(PGN other) {
    return getSevenTagsAsString()
        .compareTo(((PGN) other).getSevenTagsAsString());
  }

  /**
   * @param tag de te verwijderen tag
   */
  public void deleteTag(String tag) {
    if (tags.containsKey(tag)) {
      tags.remove(tag);
    }
  }

  /**
   * Is de PGN gelijk aan de andere?
   */
  public boolean equals(Object other) {
    if (!(other instanceof PGN)) {
      return false;
    }

    return getSevenTagsAsString()
        .equalsIgnoreCase(((PGN) other).getSevenTagsAsString());
  }

  /**
   * Geef de BLACK tag.
   * 
   * @return
   */
  public String getBlack() {
    return getTag(CaissaConstants.PGNTAG_BLACK);
  }

  /**
   * Geeft de "Date" tag als java.util.Date
   * Het hoeft niet altijd een volledige datum te zijn.
   * 
   * @return Date
   */
  public Date getDate() {
    Date  datum = null;

    try {
      datum = toDate(getTag(CaissaConstants.PGNTAG_DATE));
    } catch (ParseException e) {
      return null;
    }

    return datum;
  }

  /**
   * Geeft de "EventDate" tag als java.util.Date
   * Het hoeft niet altijd een volledige datum te zijn.
   * 
   * @return Date
   */
  public Date getEventDate() {
    Date  datum = null;

    try {
      datum = toDate(getTag(CaissaConstants.PGNTAG_EVENTDATE));
    } catch (ParseException e) {
      return null;
    }

    return datum;
  }

  /**
   * @return de '7 Tags' tags
   */
  public String getSevenTagsAsString() {
    String        eol     = "\"]\n";
    StringBuilder result  = new StringBuilder();

    result.append("[Event \"")
          .append(tags.get(CaissaConstants.PGNTAG_EVENT)).append(eol)
          .append("[Site \"")
          .append(tags.get(CaissaConstants.PGNTAG_SITE)).append(eol)
          .append("[Date \"")
          .append(tags.get(CaissaConstants.PGNTAG_DATE)).append(eol)
          .append("[Round \"")
          .append(tags.get(CaissaConstants.PGNTAG_ROUND)).append(eol)
          .append("[White \"").append(getWhite()).append(eol)
          .append("[Black \"").append(getBlack()).append(eol)
          .append("[Result \"")
          .append(tags.get(CaissaConstants.PGNTAG_RESULT)).append(eol);

    return result.toString();
  }

  /**
   * @return de stukken
   */
  public String getStukken() {
    return stukken;
  }

  /**
   * @param tag de tag waarvoor de value gevraagd wordt
   * @return de value van de tag
   */
  public String getTag(String tag) {
    if (tags.containsKey(tag)) {
      return tags.get(tag);
    }
    
    return null;
  }

  /**
   * @return de tags
   */
  public Map<String, String> getTags() {
    return tags;
  }

  /**
   * @return de tags
   */
  public String getTagsAsString() {
    String        eol     = "\"]\n";
    StringBuilder result  = new StringBuilder();

    result.append(getSevenTagsAsString());

    for (String tag: tags.keySet()) {
      if (Arrays.binarySearch(sevenTagRoster, tag) < 0) {
        result.append("[").append(tag).append(" \"").append(tags.get(tag))
              .append(eol);
      }
    }

    return result.toString();
  }

  /**
   * Geef de WHITE tag.
   * 
   * @return
   */
  public String getWhite() {
    return getTag(CaissaConstants.PGNTAG_WHITE);
  }

  /**
   * @return de zetten
   */
  public String getZetten() {
    return zetten;
  }

  /**
   * @param naarTaal de taal van de stukken in de return
   * @return de zetten in de taal van naarTaal
   */
  public String getZetten(String naarTaal) throws PgnException {
    return translateStukken(zetten,
                            CaissaConstants.Stukcodes
                                           .valueOf(naarTaal.toUpperCase())
                                           .getStukcodes());
  }

  /**
   * Verwijdert ongewenste tekens en commentaar uit de zetten.
   * @return 
   */
  public String getZuivereZetten() {
    if (zuivereZetten.isEmpty()
        && !zetten.isEmpty()) {
      zuivereZetten = " " + zetten + " ";
      // TAB wordt spatie, Verwijder spaties rond de punten (2x),
      // Verwijder 'diagram', Verwijder 'nieuwtje', Maak van meerdere spaties 1
      // spatie
      String[]  teVervangen = {"\t", "  ", ". ", " .", " D ", " N "};
      String[]  naar        = {" ",  " ",  ".",  ".",  " ",   " "};

      for (int i = 0; i<teVervangen.length; i++) {
        while (zuivereZetten.contains(teVervangen[i])) {
          zuivereZetten = zuivereZetten.replace(teVervangen[i], naar[i]);
        }
      }

      // Verwijder commentaar
      while (zuivereZetten.contains("{")) {
        int accoladesOpen = 1;
        int start         = zuivereZetten.indexOf('{');
        int eind          = start + 1;
        while (accoladesOpen > 0) {
          switch (zuivereZetten.charAt(eind)) {
            case '{': accoladesOpen++;
                      break;
            case '}': accoladesOpen--;
                      break;
          default:
            break;
          }
          eind++;
        }
        if (start == 0) {
          zuivereZetten = zuivereZetten.substring(eind);
        } else {
          if (eind == zuivereZetten.length()) {
            zuivereZetten = zuivereZetten.substring(0, start - 1);            
          } else {
            zuivereZetten = zuivereZetten.substring(0, start - 1)
                            + zuivereZetten.substring(eind);
          }
        }
      }
      while (zuivereZetten.contains("(")) {
        int haakjesOpen = 1;
        int start       = zuivereZetten.indexOf('(');
        int eind        = start + 1;
        while (haakjesOpen > 0) {
          switch (zuivereZetten.charAt(eind)) {
            case '(': haakjesOpen++;
                      break;
            case ')': haakjesOpen--;
                      break;
            default:
              break;
          }
          eind++;
        }
        if (start == 0) {
          zuivereZetten = zuivereZetten.substring(eind);
        } else {
          if (eind == zuivereZetten.length()) {
            zuivereZetten = zuivereZetten.substring(0, start - 1);            
          } else {
            zuivereZetten = zuivereZetten.substring(0, start - 1)
                            + zuivereZetten.substring(eind);
          }
        }
      }

      for (int i = 0; i < annotaties.length; i++) {
        if (zuivereZetten.contains(annotaties[i])) {
          zuivereZetten = zuivereZetten.replace(annotaties[i], "");
        }
      }

      // Verwijder $nn annotaties.
      while (zuivereZetten.contains("$")) {
        int start = zuivereZetten.indexOf('$');
        int eind  = start + 1;
        while ("0123456789".contains("" + zuivereZetten.charAt(eind))) {
          eind++;
        }
        if (start == 0) {
          zuivereZetten = zuivereZetten.substring(eind);
        } else {
          if (eind == zuivereZetten.length()) {
            zuivereZetten = zuivereZetten.substring(0, start - 1);            
          } else {
            zuivereZetten = zuivereZetten.substring(0, start - 1)
                            + zuivereZetten.substring(eind);
          }
        }
      }

      // Verwijder 'gesplitste' zetten.
      while (zuivereZetten.contains("...")) {
        int start = zuivereZetten.indexOf("...") - 1;
        int eind  = start + 4;
        while (zuivereZetten.charAt(start) != ' ') {
          start--;
        }
        while (zuivereZetten.charAt(eind) == '.') {
          eind++;
        }
        if (start > 0) {
          if (eind == zuivereZetten.length()) {
            zuivereZetten = zuivereZetten.substring(0, start + 1);            
          } else {
            zuivereZetten = zuivereZetten.substring(0, start + 1)
                            + zuivereZetten.substring(eind);
          }
        }
      }
      zuivereZetten = zuivereZetten.trim();
    }

    return zuivereZetten;
  }

  /**
   * Geef de hashCode van de PGN
   */
  public int hashCode() {
    return getSevenTagsAsString().hashCode();
  }

  /**
   * Bestaat de TAG?
   * 
   * @param tag
   * @return
   */
  public boolean hasTag(String tag) {
    return tags.containsKey(tag);
  }

  /**
   * Is dit een partij met 1 speler?
   * 
   * @return
   */
  public boolean isBye() {
    String  wit   = getWhite();
    String  zwart = getBlack();
    return ("bye".equalsIgnoreCase(wit)
        || "bye".equalsIgnoreCase(zwart)
        || DoosUtils.isBlankOrNull(wit)
        || DoosUtils.isBlankOrNull(zwart));
  }

  /**
   * Mag de partij gebruikt worden voor de stand?
   * 
   * @return
   */
  public boolean isRanked() {
    return ranked;
  }

  /**
   * Mag de partij gebruikt worden voor ELO rating?
   * 
   * @return
   */
  public boolean isRated() {
    return rated;
  }

  /**
   * Speelt de speler in deze partij?
   * 
   * @param speler
   * @return
   */
  public boolean isSpeler(String speler) {
    if (getBlack().equals(speler)
        || getWhite().equals(speler)) {
      return true;
    }

    return false;
  }

  /**
   * Controleert of de PGN volledig is en de TAG Date correct.
   */
  public boolean isValid() {
    for (int i = 0; i < sevenTagRoster.length; i++)  {
      if (!tags.containsKey(sevenTagRoster[i])) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param rated
   */
  public void setRated(boolean rated) {
    this.rated  = rated;
  }

  /**
   * @param stukken de waarde van stukken
   */
  public void setStukken(String stukken) {
    this.stukken = stukken;
  }

  /**
   * @param tag de toe te voegen tag
   * @param value de waarde van de tag
   * @throws PgnException 
   */
  public void setTag(String tag, String value) throws PgnException {
    tags.put(tag, value);

    if (CaissaConstants.PGNTAG_RESULT.equals(tag)
        && Arrays.binarySearch(uitslagen, value) < 0) {
      throw new PgnException("Verkeerde uitslag.");
    }
  }

  /**
   * @param tag de toe te voegen tags
   * @throws PgnException 
   */
  public void setTags(Map<String, String> tags) throws PgnException {
    this.tags = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    for (Entry<String, String> tag: tags.entrySet()) {
      setTag(tag.getKey(), tag.getValue());
    }
  }

  /**
   * @param zetten de zetten van de partij
   */
  public void setZetten(String zetten) {
    this.zetten = zetten;

    // Zorg ervoor dat de rochade volgens de standaard is.
    // Eerst de lange en dan de korte rochade.
    if (this.zetten.contains(".0")
        || this.zetten.contains(" 0")) {
      this.zetten = this.zetten.replaceAll("\\.0.0.0", ".O-O-O");
      this.zetten = this.zetten.replaceAll(" 0.0.0",   " O-O-O");
      this.zetten = this.zetten.replaceAll("\\.0.0",   ".O-O");
      this.zetten = this.zetten.replaceAll(" 0.0",     " O-O");
    }

    if (this.zetten.contains("unranked")) {
      ranked  = false;
    } else {
      ranked  = true;
    }
    if (this.zetten.contains("unrated")) {
      rated   = false;
    } else {
      rated   = true;
    }
  }

  /**
   * Deze method vertaald de stukken van een taal in de standaard taal.
   * 
   * @param zetten de zetten van de partij
   * @param vanTaal de taal van de stukken in zetten
   */
  public void setZetten(String zetten, String vanTaal) throws PgnException {
    setZetten(translateStukken(zetten,
                               CaissaConstants.Stukcodes
                                              .valueOf(vanTaal.toUpperCase())
                                              .getStukcodes()));
  }

  /**
   * Met welke kleur speelt de speler?
   * 
   * @param speler
   * @return
   */
  public String speeltKleur(String speler) {
    if (getWhite().equals(speler)) {
      return CaissaConstants.PGNTAG_WHITE;
    }
    if (getBlack().equals(speler)) {
      return CaissaConstants.PGNTAG_BLACK;
    }

    return null;
  }

  /**
   * Converteert een PGN Datum in een java.util.Date
   * @return de PGN Date
   * @throws ParseException 
   */
  private Date  toDate(String datum) throws ParseException {
    if (DoosUtils.isBlankOrNull(datum)
        || datum.contains("?")) {
      return null;
    }

    return Datum.toDate(datum, CaissaConstants.PGN_DATUM_FORMAAT);
  }

  /**
   * @result de PGN partij
   */
  public String toString() {
    StringBuilder result  = new StringBuilder();

    result.append(getTagsAsString());
    result.append("\n");
    result.append(zetten + " " + tags.get("Result") + "\n");
    
    return  result.toString();
  }

  /**
   * Vertaalt de zetten van de standaard taal naar een andere taal.
   * 
   * @param zetten de zetten van de partij
   * @param nieuweStukken de nieuwe waardes van de stukken in zetten
   * @return vertaalde zetten
   * @throws PgnException
   */
  private String translateStukken(String zetten, String naarStukken)
      throws PgnException {
    return CaissaUtils.vertaalStukken(zetten, stukken, naarStukken);
  }
}
