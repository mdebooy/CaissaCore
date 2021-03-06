/**
 * Copyright 2009 Marco de Booij
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

import eu.debooy.caissa.exceptions.PgnException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


/**
 * @author Marco de Booij
 */
public class PGNTest extends TestCase {
  private PGN pgn; 
  
  @Before
  public void setUp() throws PgnException {
    pgn = new PGN();

    pgn.addTag("Event", "schaak evenement");
    pgn.addTag("Site",  "tournooi ruimte");
    pgn.addTag("Date",  "2009.05.17");
    pgn.addTag("Round", "-");
    pgn.addTag("White", "witspeler");
    pgn.addTag("Black", "zwartspeler");
    pgn.addTag("Result", "*");
  }

  @Test
  public void testMissingBlack() throws PgnException {
    pgn.deleteTag("Black");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingDate() throws PgnException {
    pgn.deleteTag("Date");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingEvent() throws PgnException {
    pgn.deleteTag("Event");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingResult() throws PgnException {
    pgn.deleteTag("Result");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingRound() throws PgnException {
    pgn.deleteTag("Round");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingSite() throws PgnException {
    pgn.deleteTag("Site");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingWhite() throws PgnException {
    pgn.deleteTag("White");
    assertFalse(pgn.isValid());
  }

  @Test
  public void testResultTag() {
    try {
      pgn.addTag("Result", "fout");
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      ; // Verwachte PgnException
    }
  }

  @Test
  public void testResultTags() {
    Map<String, String> tags  = new HashMap<String, String>();

    tags.put("Event", "schaak evenement");
    tags.put("Site",  "tournooi ruimte");
    tags.put("Date",  "2009.05.17");
    tags.put("Round", "-");
    tags.put("White", "witspeler");
    tags.put("Black", "zwartspeler");
    tags.put("Result", "fout");

    try {
      pgn.setTags(tags);
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      ; // Verwachte PgnException
    }
  }

  @Test
  public void testValidity() throws PgnException {
    assertTrue(pgn.isValid());
  }

  @Test
  public void testZuivereZetten() throws PgnException {
    List<PGN> partijen        = new ArrayList<PGN>();
    partijen
        .addAll(
            CaissaUtils.laadPgnBestand("target/test-classes/commentaar.pgn"));
    int       i               = 0;
    PGN       partij          = null;
    String    resultaat       = "";
    String    zuivereZetten   = "";

    while (i < partijen.size()) {
      partij        = partijen.get(i);
      if (partij.hasTag("Vertaal")) {
        resultaat   = partij.getZetten(partij.getTag("Vertaal"));
      } else {
        resultaat   = partij.getZuivereZetten();
      }
      partij        = partijen.get(i+1);
      zuivereZetten = partij.getZetten();
      assertTrue("Verkeerd bij partij " + (i+1),
                 resultaat.equals(zuivereZetten));
      i += 2;
    }
  }

  @Test
  public void testRankedRated() throws PgnException {
    assertTrue(pgn.isValid());
    assertTrue(pgn.isRanked());
    assertTrue(pgn.isRated());

    pgn.setZetten("{unranked, unrated}");

    assertTrue(pgn.isValid());
    assertFalse(pgn.isRanked());
    assertFalse(pgn.isRated());
  }

  @Test
  public void testDefaultSort() throws PgnException {
    List<PGN> partijen  = new ArrayList<PGN>();
    partijen.addAll(CaissaUtils.laadPgnBestand("target/test-classes/test"));
    Collections.sort(partijen);
    PGN[]     partijTabel = partijen.toArray(new PGN[partijen.size()]);
    for (int i = 0; i < partijen.size()-1; i++) {
      assertTrue(partijTabel[i].compareTo(partijTabel[i+1]) < 0);
    }
  }

  @Test
  public void testSortByEvent() throws PgnException {
    Collection<PGN> partijen  = new TreeSet<PGN>(new PGN.byEventComparator());
    partijen.addAll(CaissaUtils.laadPgnBestand("target/test-classes/test.pgn"));
    PGN[]     partijTabel = partijen.toArray(new PGN[partijen.size()]);
    for (int i = 0; i < partijen.size()-1; i++) {
      assertTrue(partijTabel[i].compareTo(partijTabel[i+1]) < 0);
    }
  }
}
