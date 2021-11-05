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
import eu.debooy.doosutils.exception.BestandException;
import eu.debooy.doosutils.test.BatchTest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import static junit.framework.TestCase.fail;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class PGNTest extends BatchTest {
  protected static final  ClassLoader CLASSLOADER =
      PGNTest.class.getClassLoader();

  private PGN pgn;

  @AfterClass
  public static void afterClass() {
    verwijderBestanden(TEMP + File.separator,
                       new String[] {TestConstants.BST_COMMENTAAR_PGN,
                                     TestConstants.BST_TEST_PGN});
  }

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale(TestConstants.TST_TAAL));
    resourceBundle   = ResourceBundle.getBundle("CaissaCore",
                                                Locale.getDefault());
    try {
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_COMMENTAAR_PGN,
                     TEMP + File.separator + TestConstants.BST_COMMENTAAR_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_TEST_PGN,
                     TEMP + File.separator + TestConstants.BST_TEST_PGN);
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
      throw new BestandException(e);
    }
  }

  @Before
  public void setUp() throws PgnException {
    pgn = new PGN();

    pgn.addTag(CaissaConstants.PGNTAG_EVENT, "schaak evenement");
    pgn.addTag(CaissaConstants.PGNTAG_SITE,  "tournooi ruimte");
    pgn.addTag(CaissaConstants.PGNTAG_DATE,  "2009.05.17");
    pgn.addTag(CaissaConstants.PGNTAG_ROUND, "-");
    pgn.addTag(CaissaConstants.PGNTAG_WHITE, "witspeler");
    pgn.addTag(CaissaConstants.PGNTAG_BLACK, "zwartspeler");
    pgn.addTag(CaissaConstants.PGNTAG_RESULT, "*");
  }

  @Test
  public void testMissingBlack() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_BLACK);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingDate() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_DATE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingEvent() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_EVENT);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingResult() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_RESULT);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingRound() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_ROUND);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingSite() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_SITE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingWhite() throws PgnException {
    pgn.deleteTag(CaissaConstants.PGNTAG_WHITE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testResultTag() {
    try {
      pgn.addTag(CaissaConstants.PGNTAG_RESULT, "fout");
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      assertEquals(resourceBundle.getString(PGN.ERR_PGN_UITSLAG),
                   e.getMessage());
    }
  }

  @Test
  public void testResultTags() {
    Map<String, String> tags  = new HashMap<>();

    tags.put(CaissaConstants.PGNTAG_EVENT,  "schaak evenement");
    tags.put(CaissaConstants.PGNTAG_SITE,   "tournooi ruimte");
    tags.put(CaissaConstants.PGNTAG_DATE,   "2009.05.17");
    tags.put(CaissaConstants.PGNTAG_ROUND,  "-");
    tags.put(CaissaConstants.PGNTAG_WHITE,  "witspeler");
    tags.put(CaissaConstants.PGNTAG_BLACK,  "zwartspeler");
    tags.put(CaissaConstants.PGNTAG_RESULT, "fout");

    try {
      pgn.setTags(tags);
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      assertEquals(resourceBundle.getString(PGN.ERR_PGN_UITSLAG),
                   e.getMessage());
    }
  }

  @Test
  public void testValidity() throws PgnException {
    assertTrue(pgn.isValid());
  }

  @Test
  public void testZuivereZetten() throws PgnException {
    List<PGN> partijen      = new ArrayList<>();
    partijen
        .addAll(
            CaissaUtils.laadPgnBestand(TEMP + File.separator
                                       + TestConstants.BST_COMMENTAAR_PGN));
    var       i             = 0;
    PGN       partij;
    String    resultaat;
    String    zuivereZetten;

    while (i < partijen.size()) {
      partij        = partijen.get(i);
      if (partij.hasTag("Vertaal")) {
        resultaat   = partij.getZuivereZetten(partij.getTag("Vertaal"));
      } else {
        resultaat   = partij.getZuivereZetten();
      }
      partij        = partijen.get(i+1);
      zuivereZetten = partij.getZetten();
      assertEquals("Verkeerd bij partij " + (i+1),
                   zuivereZetten, resultaat);
      i += 2;
    }
  }

  @Test
  public void testRankedRated() throws PgnException {
    assertTrue(pgn.isValid());
    assertTrue(pgn.isRanked());
    assertTrue(pgn.isRated());

    pgn.setZetten("{" + CaissaConstants.PARTIJ_UNRANKED + ","
                    + CaissaConstants.PARTIJ_UNRATED + "}");

    assertTrue(pgn.isValid());
    assertFalse(pgn.isRanked());
    assertFalse(pgn.isRated());
  }

  @Test
  public void testDefaultSort() throws PgnException {
    List<PGN> partijen    = new ArrayList<>();
    partijen.addAll(CaissaUtils.laadPgnBestand(TEMP + File.separator
                                                + TestConstants.BST_TEST_PGN));
    Collections.sort(partijen);
    var       partijTabel = partijen.toArray(new PGN[partijen.size()]);
    for (var i = 0; i < partijen.size()-1; i++) {
      assertTrue(partijTabel[i].compareTo(partijTabel[i+1]) < 0);
    }
  }

  @Test
  public void testSortByEvent() throws PgnException {
    Collection<PGN> partijen    = new TreeSet<>(new PGN.ByEventComparator());
    partijen.addAll(CaissaUtils.laadPgnBestand(TEMP + File.separator
                                                + TestConstants.BST_TEST_PGN));
    var             partijTabel = partijen.toArray(new PGN[partijen.size()]);
    for (var i = 0; i < partijen.size()-1; i++) {
      assertTrue(partijTabel[i].compareTo(partijTabel[i+1]) < 0);
    }
  }
}
