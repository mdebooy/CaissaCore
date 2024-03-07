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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class SpelerinfoTest extends TestCase {
  public static final String  ADAMS       = "Adams";
  public static final String  ADAMS_JAN   = "Adams, Jan";
  public static final String  DEVRIES_JAN = "de Vries, Jan";
  public static final String  JAN         = "Jan";
  public static final String  JAN_ADAMS   = "Jan Adams";
  public static final String  JANSEN_JAN  = "Jansen, Jan";

  private final Date  datum       = new Date();

  private Spelerinfo  spelerinfo0;
  private Spelerinfo  spelerinfo1;
  private Spelerinfo  spelerinfo2;
  private Spelerinfo  spelerinfo3;
  private Spelerinfo  spelerinfo4;
  private Spelerinfo  spelerinfo5;
  private Spelerinfo  spelerinfo6;
  private Spelerinfo  spelerinfo7;
  private Spelerinfo  spelerinfo8;
  private Spelerinfo  spelerinfo9;

  @Before
  @Override
  public void setUp() {
    spelerinfo0 = new Spelerinfo();
    spelerinfo1 = new Spelerinfo();
    spelerinfo2 = new Spelerinfo();
    spelerinfo3 = new Spelerinfo();
    spelerinfo4 = new Spelerinfo();
    spelerinfo5 = new Spelerinfo();
    spelerinfo6 = new Spelerinfo();
    spelerinfo7 = new Spelerinfo();
    spelerinfo8 = new Spelerinfo();
    spelerinfo9 = new Spelerinfo();

    spelerinfo1.setNaam(JANSEN_JAN);
    spelerinfo1.setPartijen(10);
    spelerinfo1.setPunten(8.5);
    spelerinfo1.setSpelerId(1);
    spelerinfo1.setSpelerSeq(1);
    spelerinfo1.setTelefoon("+32/2/21.12.00");
    spelerinfo1.setTieBreakScore(20.0);

    spelerinfo2.setNaam(ADAMS_JAN);
    spelerinfo2.setPartijen(10);
    spelerinfo2.setPunten(8.5);
    spelerinfo2.setTieBreakScore(20.0);

    spelerinfo3.setNaam(DEVRIES_JAN);
    spelerinfo3.setPartijen(10);
    spelerinfo3.setPunten(8.5);
    spelerinfo3.setTieBreakScore(20.0);

    spelerinfo4.setNaam(JANSEN_JAN);
    spelerinfo4.setPartijen(9);
    spelerinfo4.setPunten(8.5);
    spelerinfo4.setTieBreakScore(20.0);

    spelerinfo5.setNaam(JANSEN_JAN);
    spelerinfo5.setPartijen(10);
    spelerinfo5.setPunten(7.5);
    spelerinfo5.setTieBreakScore(20.0);

    spelerinfo6.setNaam(JANSEN_JAN);
    spelerinfo6.setPartijen(10);
    spelerinfo6.setPunten(8.5);
    spelerinfo6.setTieBreakScore(19.75);

    spelerinfo7.setPartijen(0);
    spelerinfo7.setPunten(0.0);
    spelerinfo7.setTieBreakScore(0.0);

    spelerinfo8.setNaam(JANSEN_JAN);
    spelerinfo8.setPartijen(10);
    spelerinfo8.setPunten(8.5);
    spelerinfo8.setTieBreakScore(20.0);

    spelerinfo9.setPartijen(1);
    spelerinfo9.setPunten(0.0);
    spelerinfo9.setSpelerId(9);
    spelerinfo9.setSpelerSeq(9);
    spelerinfo9.setTieBreakScore(0.0);
  }

  @Test
  public void testAdd() {
    var spelerinfo  = new Spelerinfo();

    spelerinfo.setPartijen(1);
    spelerinfo.addPartij(14);

    assertTrue(15==spelerinfo.getPartijen());

    spelerinfo.addPartij();

    assertTrue(16==spelerinfo.getPartijen());
  }

  @Test
  public void testAlias() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getAlias());
    spelerinfo.setAlias("alias");
    assertEquals("alias", spelerinfo.getAlias());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setAlias(null);
    assertNull(spelerinfo.getAlias());
  }

  @Test
  public void testCompareToGelijk() {
    assertEquals(0, spelerinfo0.compareTo(spelerinfo7));
    assertEquals(0, spelerinfo1.compareTo(spelerinfo1));
    assertEquals(0, spelerinfo1.compareTo(spelerinfo8));
  }

  @Test
  public void testCompareToGroter() {
    assertTrue(spelerinfo1.compareTo(spelerinfo2) > 0);
    assertTrue(spelerinfo1.compareTo(spelerinfo3) > 0);
    assertTrue(spelerinfo1.compareTo(spelerinfo4) > 0);
  }

  @Test
  public void testCompareToKleiner() {
    assertTrue(spelerinfo1.compareTo(spelerinfo0) < 0);
    assertTrue(spelerinfo1.compareTo(spelerinfo5) < 0);
    assertTrue(spelerinfo1.compareTo(spelerinfo6) < 0);
  }

  @Test
  public void testEerstePartij() {
    var spelerinfo    = new Spelerinfo();
    var eerstepartij  = new Date();

    assertNull(spelerinfo.getEerstePartij());
    spelerinfo.setEerstePartij(eerstepartij);
    assertEquals(eerstepartij, spelerinfo.getEerstePartij());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setEerstePartij(null);
    assertNull(spelerinfo.getEerstePartij());
    spelerinfo.setEerstePartij(eerstepartij);
    assertEquals(eerstepartij, spelerinfo.getEerstePartij());
    spelerinfo.setEerstePartij(datum);
    assertNotEquals(eerstepartij, spelerinfo.getEerstePartij());
    spelerinfo.setEerstePartij(eerstepartij);
    eerstepartij  = datum;
    assertNotEquals(eerstepartij, spelerinfo.getEerstePartij());
  }

  @Test
  public void testElo() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getElo());
    spelerinfo.setElo(2112);
    assertEquals(Integer.valueOf(2112), spelerinfo.getElo());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setElo(null);
    assertNull(spelerinfo.getElo());
  }

  @Test
  public void testElogroei() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getElogroei());
    spelerinfo.setElogroei(12);
    assertEquals(Integer.valueOf(12), spelerinfo.getElogroei());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setElogroei(null);
    assertNull(spelerinfo.getElogroei());
  }

  @Test
  public void testEmail() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getEmail());
    spelerinfo.setEmail("e-mail");
    assertEquals("e-mail", spelerinfo.getEmail());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setEmail(null);
    assertNull(spelerinfo.getEmail());
  }

  @Test
  public void testHashCode() {
    assertEquals(1, spelerinfo1.hashCode());
  }

  @Test
  public void testHeenronde() {
    var spelerinfo  = new Spelerinfo();

    assertTrue(spelerinfo.inHeenronde());
    spelerinfo.setHeenronde(false);
    assertFalse(spelerinfo.inHeenronde());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setHeenronde(null);
    assertNull(spelerinfo.inHeenronde());
  }

  @Test
  public void testInit1() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getAlias());
    assertNull(spelerinfo.getEerstePartij());
    assertNull(spelerinfo.getElo());
    assertNull(spelerinfo.getElogroei());
    assertNull(spelerinfo.getEmail());
    assertTrue(spelerinfo.inHeenronde());
    assertNull(spelerinfo.getLaatstePartij());
    assertNull(spelerinfo.getLandKode());
    assertNull(spelerinfo.getMaxDatum());
    assertNull(spelerinfo.getMaxElo());
    assertNull(spelerinfo.getMinDatum());
    assertNull(spelerinfo.getMinElo());
    assertEquals("", spelerinfo.getNaam());
    assertNull(spelerinfo.getOfficieel());
    assertEquals(Integer.valueOf(0), spelerinfo.getPartijen());
    assertEquals(0.0, spelerinfo.getPunten());
    assertNull(spelerinfo.getSpelerId());
    assertTrue(spelerinfo.inTerugronde());
    assertEquals(0.0, spelerinfo.getTieBreakScore());
  }

  @Test
  public void testInit2() {
    var spelerinfo  = new Spelerinfo(spelerinfo8);

    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo8.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo8.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo8.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo8.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo8.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo8.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo8.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo8.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo8.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo8.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo8.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo8.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo8.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo8.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo8.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo8.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo8.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo8.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo8.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo8.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo8.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo8.inTerugronde())
                                  .isEquals());
  }

  @Test
  public void testInit3() {
    var         minJson = "{\"naam\": \"Speler, Alice\"}";
    JSONParser  parser  = new JSONParser();
    try {
      JSONObject  json        = (JSONObject) parser.parse(minJson);
     var         spelerinfo  = new Spelerinfo(json);
      assertNull(spelerinfo.getAlias());
      assertNull(spelerinfo.getEerstePartij());
      assertNull(spelerinfo.getElo());
      assertNull(spelerinfo.getElogroei());
      assertNull(spelerinfo.getEmail());
      assertTrue(spelerinfo.inHeenronde());
      assertNull(spelerinfo.getLaatstePartij());
      assertNull(spelerinfo.getLandKode());
      assertNull(spelerinfo.getMaxDatum());
      assertNull(spelerinfo.getMaxElo());
      assertNull(spelerinfo.getMinDatum());
      assertNull(spelerinfo.getMinElo());
      assertEquals("Speler, Alice", spelerinfo.getNaam());
      assertNull(spelerinfo.getOfficieel());
      assertEquals(Integer.valueOf(0), spelerinfo.getPartijen());
      assertEquals(0.0, spelerinfo.getPunten());
      assertNull(spelerinfo.getSpelerId());
      assertTrue(spelerinfo.inTerugronde());
      assertEquals(0.0, spelerinfo.getTieBreakScore());
    } catch (ParseException e) {
      fail("JSON parse exception: " + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInit4() {
    var         minJson = "{\"alias\": \"Caissa\", "
                            + "\"elo\": 2112, "
                            + "\"email\": \"alice@caissa.chess\", "
                            + "\"heenronde\": false, "
                            + "\"landkode\": \"bel\", "
                            + "\"naam\": \"Speler, Alice\", "
                            + "\"id\": 1221, "
                            + "\"terugronde\": true}";
    JSONParser  parser  = new JSONParser();
    try {
      JSONObject  json        = (JSONObject) parser.parse(minJson);
     var         spelerinfo  = new Spelerinfo(json);
      assertEquals("Caissa", spelerinfo.getAlias());
      assertNull(spelerinfo.getEerstePartij());
      assertEquals(Integer.valueOf(2112), spelerinfo.getElo());
      assertNull(spelerinfo.getElogroei());
      assertEquals("alice@caissa.chess", spelerinfo.getEmail());
      assertFalse(spelerinfo.inHeenronde());
      assertNull(spelerinfo.getLaatstePartij());
      assertEquals("bel", spelerinfo.getLandKode());
      assertNull(spelerinfo.getMaxDatum());
      assertNull(spelerinfo.getMaxElo());
      assertNull(spelerinfo.getMinDatum());
      assertNull(spelerinfo.getMinElo());
      assertEquals("Speler, Alice", spelerinfo.getNaam());
      assertNull(spelerinfo.getOfficieel());
      assertEquals(Integer.valueOf(0), spelerinfo.getPartijen());
      assertEquals(0.0, spelerinfo.getPunten());
      assertEquals(Integer.valueOf(1221), spelerinfo.getSpelerId());
      assertTrue(spelerinfo.inTerugronde());
      assertEquals(0.0, spelerinfo.getTieBreakScore());
    } catch (ParseException e) {
      fail("JSON parse exception: " + e.getLocalizedMessage());
    }
  }

  @Test
  public void testLaatstePartij() {
    var spelerinfo    = new Spelerinfo();
    var laatstepartij = new Date();

    assertNull(spelerinfo.getLaatstePartij());
    spelerinfo.setLaatstePartij(laatstepartij);
    assertEquals(laatstepartij, spelerinfo.getLaatstePartij());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setLaatstePartij(null);
    assertNull(spelerinfo.getLaatstePartij());
    spelerinfo.setLaatstePartij(laatstepartij);
    assertEquals(laatstepartij, spelerinfo.getLaatstePartij());
    spelerinfo.setLaatstePartij(datum);
    assertNotEquals(laatstepartij, spelerinfo.getLaatstePartij());
    spelerinfo.setLaatstePartij(laatstepartij);
    laatstepartij  = datum;
    assertNotEquals(laatstepartij, spelerinfo.getLaatstePartij());
  }

  @Test
  public void testLandKode() {
    var spelerinfo    = new Spelerinfo();

    assertNull(spelerinfo.getLandKode());
    spelerinfo.setLandKode("EU");
    assertEquals("EU", spelerinfo.getLandKode());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setLandKode(null);
    assertNull(spelerinfo.getLandKode());
  }

  @Test
  public void testMaxDatum() {
    var spelerinfo  = new Spelerinfo();
    var maxDatum    = new Date();

    assertNull(spelerinfo.getMaxDatum());
    spelerinfo.setMaxDatum(maxDatum);
    assertEquals(maxDatum, spelerinfo.getMaxDatum());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setMaxDatum(null);
    assertNull(spelerinfo.getMaxDatum());
    spelerinfo.setMaxDatum(maxDatum);
    assertEquals(maxDatum, spelerinfo.getMaxDatum());
    spelerinfo.setMaxDatum(datum);
    assertNotEquals(maxDatum, spelerinfo.getMaxDatum());
    spelerinfo.setMaxDatum(maxDatum);
    maxDatum  = datum;
    assertNotEquals(maxDatum, spelerinfo.getMaxDatum());
  }

  @Test
  public void testMaxElo() {
    var spelerinfo    = new Spelerinfo();

    assertNull(spelerinfo.getMaxElo());
    spelerinfo.setMaxElo(2112);
    assertEquals(Integer.valueOf(2112), spelerinfo.getMaxElo());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setMaxElo(null);
    assertNull(spelerinfo.getMaxElo());
  }

  @Test
  public void testMinDatum() {
    var spelerinfo  = new Spelerinfo();
    var minDatum    = new Date();

    assertNull(spelerinfo.getMinDatum());
    spelerinfo.setMinDatum(minDatum);
    assertEquals(minDatum, spelerinfo.getMinDatum());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setMinDatum(null);
    assertNull(spelerinfo.getMinDatum());
    spelerinfo.setMinDatum(minDatum);
    assertEquals(minDatum, spelerinfo.getMinDatum());
    spelerinfo.setMinDatum(datum);
    assertNotEquals(minDatum, spelerinfo.getMinDatum());
    spelerinfo.setMinDatum(minDatum);
    minDatum  = datum;
    assertNotEquals(minDatum, spelerinfo.getMinDatum());
  }

  @Test
  public void testMinElo() {
    var spelerinfo    = new Spelerinfo();

    assertNull(spelerinfo.getMinElo());
    spelerinfo.setMinElo(1221);
    assertEquals(Integer.valueOf(1221), spelerinfo.getMinElo());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setMinElo(null);
    assertNull(spelerinfo.getMinElo());
  }

  @Test
  public void testNaam() {
    var spelerinfo    = new Spelerinfo();

    assertEquals("", spelerinfo.getNaam());
    assertEquals("", spelerinfo.getVoornaam());
    assertEquals("", spelerinfo.getVolledigenaam());
    spelerinfo.setNaam(ADAMS_JAN);
    assertEquals(ADAMS, spelerinfo.getAchternaam());
    assertEquals(ADAMS_JAN, spelerinfo.getNaam());
    assertEquals(JAN, spelerinfo.getVoornaam());
    assertEquals(JAN_ADAMS, spelerinfo.getVolledigenaam());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setNaam(JAN);
    assertEquals(JAN, spelerinfo.getAchternaam());
    assertEquals(JAN, spelerinfo.getNaam());
    assertEquals(JAN, spelerinfo.getVoornaam());
    assertEquals(JAN, spelerinfo.getVolledigenaam());
    spelerinfo.setNaam(null);
    assertEquals("", spelerinfo.getAchternaam());
    assertEquals("", spelerinfo.getNaam());
    assertEquals("", spelerinfo.getVoornaam());
    assertEquals("", spelerinfo.getVolledigenaam());
  }

  @Test
  public void testOfficieel() {
    var spelerinfo  = new Spelerinfo();
    var officieel   = new Date();

    assertNull(spelerinfo.getOfficieel());
    spelerinfo.setOfficieel(officieel);
    assertEquals(officieel, spelerinfo.getOfficieel());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setOfficieel(null);
    assertNull(spelerinfo.getOfficieel());
    spelerinfo.setOfficieel(officieel);
    assertEquals(officieel, spelerinfo.getOfficieel());
    spelerinfo.setOfficieel(datum);
    assertNotEquals(officieel, spelerinfo.getOfficieel());
    spelerinfo.setOfficieel(officieel);
    officieel = datum;
    assertNotEquals(officieel, spelerinfo.getOfficieel());
  }

  @Test
  public void testPartijen() {
    var spelerinfo    = new Spelerinfo();

    assertEquals(Integer.valueOf(0), spelerinfo.getPartijen());
    spelerinfo.setPartijen(12);
    assertEquals(Integer.valueOf(12), spelerinfo.getPartijen());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setPartijen(null);
    assertEquals(Integer.valueOf(0), spelerinfo.getPartijen());
  }

  @Test
  public void testPunten() {
    var spelerinfo    = new Spelerinfo();

    assertEquals(0.0d, spelerinfo.getPunten());
    spelerinfo.setPunten(8.5d);
    assertEquals(8.5d, spelerinfo.getPunten());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setPunten(null);
    assertEquals(0.0d, spelerinfo.getPunten());
  }

  @Test
  public void testRonde1() {
    var spelerinfo    = new Spelerinfo();

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_DUBBEL));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_DUBBEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_ENKEL));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_ENKEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_MATCH));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_MATCH));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_MATCH));

    assertFalse(spelerinfo.inRonde(1, 4, 3));
    assertFalse(spelerinfo.inRonde(3, 4, 3));
    assertFalse(spelerinfo.inRonde(5, 4, 3));
  }

  @Test
  public void testRonde2() {
    var spelerinfo    = new Spelerinfo();

    spelerinfo.setHeenronde(false);

    assertFalse(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_DUBBEL));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_DUBBEL));

    assertFalse(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_ENKEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_MATCH));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_MATCH));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_MATCH));

    assertFalse(spelerinfo.inRonde(1, 4, 3));
    assertFalse(spelerinfo.inRonde(3, 4, 3));
    assertFalse(spelerinfo.inRonde(5, 4, 3));
  }

  @Test
  public void testRonde3() {
    var spelerinfo    = new Spelerinfo();

    spelerinfo.setTerugronde(false);

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_DUBBEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_ENKEL));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_ENKEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_MATCH));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_MATCH));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_MATCH));

    assertFalse(spelerinfo.inRonde(1, 4, 3));
    assertFalse(spelerinfo.inRonde(3, 4, 3));
    assertFalse(spelerinfo.inRonde(5, 4, 3));
  }

  @Test
  public void testRonde4() {
    var spelerinfo    = new Spelerinfo();

    spelerinfo.setHeenronde(false);
    spelerinfo.setTerugronde(false);

    assertFalse(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_DUBBEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_DUBBEL));

    assertFalse(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_ENKEL));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_ENKEL));

    assertTrue(spelerinfo.inRonde(1, 4, Competitie.TOERNOOI_MATCH));
    assertTrue(spelerinfo.inRonde(3, 4, Competitie.TOERNOOI_MATCH));
    assertFalse(spelerinfo.inRonde(5, 4, Competitie.TOERNOOI_MATCH));

    assertFalse(spelerinfo.inRonde(1, 4, 3));
    assertFalse(spelerinfo.inRonde(3, 4, 3));
    assertFalse(spelerinfo.inRonde(5, 4, 3));
  }

  @Test
  public void testSorteer1() {
    List<Spelerinfo>  spelers = new ArrayList<>();

    spelers.add(spelerinfo0);
    spelers.add(spelerinfo1);
    spelers.add(spelerinfo2);
    spelers.add(spelerinfo3);
    spelers.add(spelerinfo4);
    spelers.add(spelerinfo5);
    spelers.add(spelerinfo6);
    spelers.add(spelerinfo7);
    spelers.add(spelerinfo8);
    spelers.add(spelerinfo9);

    Collections.sort(spelers);

    assertEquals(spelerinfo1.getSpelerId(), spelers.get(3).getSpelerId());
    assertEquals(spelerinfo9.getSpelerId(), spelers.get(7).getSpelerId());
  }

  @Test
  public void testSpelerId() {
    var spelerinfo    = new Spelerinfo();

    assertNull(spelerinfo.getSpelerId());
    spelerinfo.setSpelerId(1221);
    assertEquals(Integer.valueOf(1221), spelerinfo.getSpelerId());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setSpelerId(null);
    assertNull(spelerinfo.getSpelerId());
  }

  @Test
  public void testSpelerSeq() {
    var spelerinfo    = new Spelerinfo();

    assertNull(spelerinfo.getSpelerSeq());
    spelerinfo.setSpelerSeq(1221);
    assertEquals(Integer.valueOf(1221), spelerinfo.getSpelerSeq());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setSpelerSeq(null);
    assertNull(spelerinfo.getSpelerSeq());
  }

  @Test
  public void testTerugronde() {
    var spelerinfo  = new Spelerinfo();

    assertTrue(spelerinfo.inTerugronde());
    spelerinfo.setTerugronde(false);
    assertFalse(spelerinfo.inTerugronde());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .isEquals());
    spelerinfo.setTerugronde(null);
    assertNull(spelerinfo.inTerugronde());
  }

  @Test
  public void testTelefoon() {
    var spelerinfo  = new Spelerinfo();

    assertNull(spelerinfo.getTelefoon());
    spelerinfo.setTelefoon("+32/2/21.12.00");
    assertEquals("+32/2/21.12.00", spelerinfo.getTelefoon());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTieBreakScore(),
                                          spelerinfo0.getTieBreakScore())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .isEquals());
    spelerinfo.setTelefoon(null);
    assertEquals("", spelerinfo.getTelefoon());
  }

  @Test
  public void testTieBreakScore() {
    var spelerinfo    = new Spelerinfo();

    assertEquals(0.0d, spelerinfo.getTieBreakScore());
    spelerinfo.setTieBreakScore(11.0d);
    assertEquals(11.0d, spelerinfo.getTieBreakScore());
    assertTrue(new EqualsBuilder().append(spelerinfo.getAchternaam(),
                                          spelerinfo0.getAchternaam())
                                  .append(spelerinfo.getAlias(),
                                          spelerinfo0.getAlias())
                                  .append(spelerinfo.getElo(),
                                          spelerinfo0.getElo())
                                  .append(spelerinfo.getElogroei(),
                                          spelerinfo0.getElogroei())
                                  .append(spelerinfo.getEmail(),
                                          spelerinfo0.getEmail())
                                  .append(spelerinfo.getEerstePartij(),
                                          spelerinfo0.getEerstePartij())
                                  .append(spelerinfo.getLaatstePartij(),
                                          spelerinfo0.getLaatstePartij())
                                  .append(spelerinfo.getLandKode(),
                                          spelerinfo0.getLandKode())
                                  .append(spelerinfo.getMaxDatum(),
                                          spelerinfo0.getMaxDatum())
                                  .append(spelerinfo.getMaxElo(),
                                          spelerinfo0.getMaxElo())
                                  .append(spelerinfo.getMinDatum(),
                                          spelerinfo0.getMinDatum())
                                  .append(spelerinfo.getMinElo(),
                                          spelerinfo0.getMinElo())
                                  .append(spelerinfo.getNaam(),
                                          spelerinfo0.getNaam())
                                  .append(spelerinfo.getOfficieel(),
                                          spelerinfo0.getOfficieel())
                                  .append(spelerinfo.getPartijen(),
                                          spelerinfo0.getPartijen())
                                  .append(spelerinfo.getPunten(),
                                          spelerinfo0.getPunten())
                                  .append(spelerinfo.getSpelerId(),
                                          spelerinfo0.getSpelerId())
                                  .append(spelerinfo.getSpelerSeq(),
                                          spelerinfo0.getSpelerSeq())
                                  .append(spelerinfo.getTelefoon(),
                                          spelerinfo0.getTelefoon())
                                  .append(spelerinfo.getVolledigenaam(),
                                          spelerinfo0.getVolledigenaam())
                                  .append(spelerinfo.getVoornaam(),
                                          spelerinfo0.getVoornaam())
                                  .append(spelerinfo.inHeenronde(),
                                          spelerinfo0.inHeenronde())
                                  .append(spelerinfo.inTerugronde(),
                                          spelerinfo0.inTerugronde())
                                  .isEquals());
    spelerinfo.setTieBreakScore(null);
    assertEquals(0.0d, spelerinfo.getTieBreakScore());
  }

  @Test
  public void testToString() {
    assertEquals("Spelerinfo data (SpelerID: 1, SpelerSeq: 1, naam: [Jansen, Jan], landkode: null, ELO: null, punten: 8.5, partijen: 10, byeScore: 0.0, tieBreakScore: 20.0, alias: null, email: null)", spelerinfo1.toString());
  }
}
