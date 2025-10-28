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

import eu.debooy.caissa.CaissaConstants.Stukcodes;
import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.doosutils.exception.BestandException;
import eu.debooy.doosutils.test.BatchTest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
    verwijderBestanden(getTemp() + File.separator,
                       new String[] {TestConstants.BST_COMMENTAAR_PGN,
                                     TestConstants.BST_DEFAULT_PGN,
                                     TestConstants.BST_EVENT_PGN,
                                     TestConstants.BST_PARTIJ_PGN,
                                     TestConstants.BST_PARTIJ_NL_PGN,
                                     TestConstants.BST_TEST_PGN});
  }

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale.Builder()
                                .setLanguage(TestConstants.TST_TAAL)
                                .build());
    resourceBundle   = ResourceBundle.getBundle(TestConstants.RESOURCEBUNDLE,
                                                Locale.getDefault());
    try {
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_COMMENTAAR_PGN,
                     getTemp() + File.separator
                      + TestConstants.BST_COMMENTAAR_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_DEFAULT_PGN,
                     getTemp() + File.separator
                      + TestConstants.BST_DEFAULT_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_EVENT_PGN,
                     getTemp() + File.separator + TestConstants.BST_EVENT_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_PARTIJ_PGN,
                     getTemp() + File.separator + TestConstants.BST_PARTIJ_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_PARTIJ_NL_PGN,
                     getTemp() + File.separator
                        + TestConstants.BST_PARTIJ_NL_PGN);
      kopieerBestand(CLASSLOADER,
                     TestConstants.BST_TEST_PGN,
                     getTemp() + File.separator + TestConstants.BST_TEST_PGN);
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
      throw new BestandException(e);
    }
  }

  @Before
  public void setUp() throws PgnException {
    pgn = new PGN();

    pgn.addTag(PGN.PGNTAG_EVENT, "schaak evenement");
    pgn.addTag(PGN.PGNTAG_SITE,  "tournooi ruimte");
    pgn.addTag(PGN.PGNTAG_DATE,  "2009.05.17");
    pgn.addTag(PGN.PGNTAG_ROUND, "-");
    pgn.addTag(PGN.PGNTAG_WHITE, "witspeler");
    pgn.addTag(PGN.PGNTAG_BLACK, "zwartspeler");
    pgn.addTag(PGN.PGNTAG_RESULT, "*");
  }

  @Test
  public void testAantalZettenWit() throws PgnException {
    var zettenwit = new int[] {39, 32, 18, 8, 50, 7, 18, 32, 16};
    var controle  =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + TestConstants.BST_DEFAULT_PGN);
    var i         = 0;
    for (var partij : controle) {
      assertEquals(zettenwit[i], partij.getAantalZettenWit());
      i++;
    }
  }

  @Test
  public void testAantalZettenZwart() throws PgnException {
    var zettenwit = new int[] {38, 31, 18, 7, 49, 6, 17, 32, 16};
    var controle  =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + TestConstants.BST_DEFAULT_PGN);
    var i         = 0;
    for (var partij : controle) {
      assertEquals(zettenwit[i], partij.getAantalZettenZwart());
      i++;
    }
  }

  @Test
  public void testHashCode() throws PgnException {
    assertEquals(1865614310, pgn.hashCode());
    pgn.setTag(PGN.PGNTAG_RESULT, "1-0");
    assertEquals(2063237628, pgn.hashCode());
  }

  @Test
  public void testInit() throws PgnException {
    var instance  = new PGN(pgn);

    assertEquals(7, instance.getTags().size());
    assertTrue(instance.isValid());
    assertEquals(pgn, instance);
    assertFalse(pgn.isBeeindigd());
    assertFalse(instance.isBeeindigd());
    instance.setTag(PGN.PGNTAG_RESULT, "1-0");
    assertNotEquals(pgn, instance);
    assertTrue(instance.isBeeindigd());
  }

  @Test
  public void testEquals() {
    var instance  = new PGN();

    assertEquals(pgn, pgn);
    assertNotEquals(pgn, null);
    assertNotEquals(pgn, PGN.PGNTAG_EVENT);
    assertNotEquals(pgn, instance);

    try {
      instance.setTag(PGN.PGNTAG_DATE,    pgn.getTag(PGN.PGNTAG_DATE));
      instance.setTag(PGN.PGNTAG_EVENT,   pgn.getTag(PGN.PGNTAG_EVENT));
      instance.setTag(PGN.PGNTAG_SITE,    pgn.getTag(PGN.PGNTAG_SITE));
      instance.setTag(PGN.PGNTAG_ROUND,   pgn.getTag(PGN.PGNTAG_ROUND));
      instance.setTag(PGN.PGNTAG_WHITE,   pgn.getTag(PGN.PGNTAG_WHITE));
      instance.setTag(PGN.PGNTAG_BLACK,   pgn.getTag(PGN.PGNTAG_BLACK));
      instance.setTag(PGN.PGNTAG_RESULT,  pgn.getTag(PGN.PGNTAG_RESULT));
      instance.setZetten(pgn.getZetten());
    } catch (PgnException e) {
      fail("Er had geen PgnException moeten wezen.");
    }

    assertEquals(pgn, instance);
  }

  @Test
  public void testLegeRound() {
    pgn.deleteTag(PGN.PGNTAG_ROUND);
    try {
      pgn.setTag(PGN.PGNTAG_ROUND, "");
    } catch (PgnException e) {
      fail("Er had geen PgnException moeten wezen.");
    }
    System.out.println(pgn.toString());
    assertTrue(pgn.isValid());
  }

  @Test
  public void testMissingBlack() {
    pgn.deleteTag(PGN.PGNTAG_BLACK);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingDate() {
    pgn.deleteTag(PGN.PGNTAG_DATE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingEvent() {
    pgn.deleteTag(PGN.PGNTAG_EVENT);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingResult() {
    pgn.deleteTag(PGN.PGNTAG_RESULT);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingRound() {
    pgn.deleteTag(PGN.PGNTAG_ROUND);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingSite() {
    pgn.deleteTag(PGN.PGNTAG_SITE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testMissingWhite() {
    pgn.deleteTag(PGN.PGNTAG_WHITE);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testOnbekendeTag1() {
    assertNull(pgn.getTag("DOOS"));
  }

  @Test
  public void testOnbekendeTag2() {
    int tags  = pgn.getTags().size();
    pgn.deleteTag("DOOS");
    assertTrue(pgn.isValid());
    assertEquals(tags, pgn.getTags().size());
  }

  @Test
  public void testRankedRated() {
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
  public void testRated() {
    assertTrue(pgn.isRated());
    pgn.setRated(false);
    assertFalse(pgn.isRated());
  }

  @Test
  public void testResultTag() {
    try {
      pgn.addTag(PGN.PGNTAG_RESULT, "fout");
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      assertEquals(resourceBundle.getString(PGN.ERR_PGN_UITSLAG),
                   e.getMessage());
    }
  }

  @Test
  public void testResultTags() {
    Map<String, String> tags  = new HashMap<>();

    tags.put(PGN.PGNTAG_EVENT,  "schaak evenement");
    tags.put(PGN.PGNTAG_SITE,   "tournooi ruimte");
    tags.put(PGN.PGNTAG_DATE,   "2009.05.17");
    tags.put(PGN.PGNTAG_ROUND,  "-");
    tags.put(PGN.PGNTAG_WHITE,  "witspeler");
    tags.put(PGN.PGNTAG_BLACK,  "zwartspeler");
    tags.put(PGN.PGNTAG_RESULT, "fout");

    try {
      pgn.setTags(tags);
      fail("Er had een PgnException moeten wezen.");
    } catch (PgnException e) {
      assertEquals(resourceBundle.getString(PGN.ERR_PGN_UITSLAG),
                   e.getMessage());
    }
  }

  @Test
  public void testSortByEvent() throws PgnException {
    Collection<PGN> partijen    = new TreeSet<>(new PGN.ByEventComparator());
    partijen.addAll(CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                                + TestConstants.BST_TEST_PGN));
    var             partijTabel = partijen.toArray(PGN[]::new);
    var             controle    =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + TestConstants.BST_EVENT_PGN);
    var             i           = 0;
    for (var partij : controle) {
      assertEquals(partij.toString(), partijTabel[i].toString());
      i++;
    }
  }

  @Test
  public void testSortDefault() throws PgnException {
    Collection<PGN> partijen    = new TreeSet<>();
    partijen.addAll(CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                                + TestConstants.BST_TEST_PGN));
    var             partijTabel = partijen.toArray(PGN[]::new);
    var             controle    =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + TestConstants.BST_DEFAULT_PGN);
    var             i           = 0;
    for (var partij : controle) {
      assertEquals(partij.toString(), partijTabel[i].toString());
      i++;
    }
  }

  @Test
  public void testStukken() {
    assertEquals(CaissaConstants.STUKKEN, pgn.getStukken());
    pgn.setStukken(Stukcodes.NL.getStukcodes());
    assertEquals(Stukcodes.NL.getStukcodes(), pgn.getStukken());
  }

  @Test
  public void testValidity() {
    assertTrue(pgn.isValid());
    pgn.deleteTag(PGN.PGNTAG_RESULT);
    assertFalse(pgn.isValid());
  }

  @Test
  public void testZetten2() {
    try {
      var partijEn  = CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                         + TestConstants.BST_PARTIJ_PGN);
      var partijNl  = CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                         + TestConstants.BST_PARTIJ_NL_PGN);

      assertEquals(partijEn.iterator().next().getZetten("nl"),
                   partijNl.iterator().next().getZetten());
    } catch (PgnException ex) {
      fail("Er had geen PgnException mogen wezen.");
    }
  }

  @Test
  public void testZuivereZetten1() {
    try {
      List<PGN> partijen      = new ArrayList<>();
      partijen
          .addAll(
              CaissaUtils.laadPgnBestand(getTemp() + File.separator
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
    } catch (PgnException ex) {
      fail("Er had geen PgnException mogen wezen.");
    }
  }

  @Test
  public void testZuivereZetten2() {
    try {
      List<PGN> partijen      = new ArrayList<>();
      partijen
              .addAll(
                      CaissaUtils.laadPgnBestand(getTemp() + File.separator
                              + TestConstants.BST_COMMENTAAR_PGN));
      var partij        = partijen.get(0);
      var zetten        = partijen.get(2).getZetten();
      var zuivereZetten = partij.getZuivereZetten();

      partij.setZetten(zetten);
      assertNotEquals(zuivereZetten, partij.getZuivereZetten());
      assertEquals(partijen.get(2).getZuivereZetten(),
                   partij.getZuivereZetten());
    } catch (PgnException ex) {
      fail("Er had geen PgnException mogen wezen.");
    }
  }
}
