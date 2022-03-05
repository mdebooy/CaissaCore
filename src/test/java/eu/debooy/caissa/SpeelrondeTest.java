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

import eu.debooy.caissa.exceptions.CaissaException;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class SpeelrondeTest extends TestCase {
  private Spelerinfo  speler01;
  private Spelerinfo  speler02;
  private Spelerinfo  speler03;
  private Spelerinfo  speler04;
  private Spelerinfo  speler05;
  private Spelerinfo  speler06;
  private Spelerinfo  spelerbye;

  @Before
  @Override
  public void setUp() {
    speler01  = new Spelerinfo();
    speler02  = new Spelerinfo();
    speler03  = new Spelerinfo();
    speler04  = new Spelerinfo();
    speler05  = new Spelerinfo();
    speler06  = new Spelerinfo();
    spelerbye = new Spelerinfo();

    speler01.setNaam("Speler, Alice");
    speler02.setNaam("Speler, Bob");
    speler03.setNaam("Speler, Carol");
    speler04.setNaam("Speler, Dave");
    speler05.setNaam("Speler, Eve");
    speler06.setNaam("Speler, Frank");
    spelerbye.setNaam(CaissaConstants.BYE);
  }

  @Test
  public void testAddPartijen1() throws CaissaException {
    var inhaalronde = new Speelronde();
    var partij1     = new Partij();
    var partij2     = new Partij();
    var partij3     = new Partij();

    partij1.setRonde(new Round("1"));
    partij1.setWitspeler(speler01);
    partij1.setZwartspeler(speler02);
    partij2.setRonde(new Round("1"));
    partij2.setWitspeler(speler03);
    partij2.setZwartspeler(speler04);
    partij3.setRonde(new Round("1"));
    partij3.setWitspeler(speler05);
    partij3.setZwartspeler(speler06);

    inhaalronde.addPartij(partij1);
    inhaalronde.addPartij(partij2);
    inhaalronde.addPartij(partij3);
    inhaalronde.setRonde(1);
    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(3), inhaalronde.getAantalPartijen());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testAddPartijen2() {
    var inhaalronde = new Speelronde();
    var partij1     = new Partij();
    var partij2     = new Partij();
    var partij3     = new Partij();

    partij1.setRonde(new Round("1"));
    partij1.setWitspeler(speler01);
    partij1.setZwartspeler(speler02);
    partij2.setRonde(new Round("1"));
    partij2.setWitspeler(speler03);
    partij2.setZwartspeler(speler04);
    partij3.setRonde(new Round("1"));
    partij3.setWitspeler(speler05);
    partij3.setZwartspeler(speler01);

    try {
      inhaalronde.addPartij(partij1);
      inhaalronde.addPartij(partij2);
    } catch (CaissaException e) {
      fail("Geen CaissaException verwacht.");
    }
    try {
      inhaalronde.addPartij(partij3);
      fail("CaissaException verwacht.");
    } catch (CaissaException e) {
      assertEquals(Speelronde.COD_INH_SPELER,
                   e.getLocalizedMessage().split(":")[0]);
      assertTrue(e.getLocalizedMessage().contains(speler01.getNaam()));
    }
    inhaalronde.setRonde(1);
    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(2), inhaalronde.getAantalPartijen());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testAddPartijen3() {
    var inhaalronde = new Speelronde();
    var partij1     = new Partij();
    var partij2     = new Partij();
    var partij3     = new Partij();

    partij1.setRonde(new Round("1"));
    partij1.setWitspeler(speler01);
    partij1.setZwartspeler(speler02);
    partij2.setRonde(new Round("1"));
    partij2.setWitspeler(speler03);
    partij2.setZwartspeler(speler04);
    partij3.setRonde(new Round("1"));
    partij3.setWitspeler(speler01);
    partij3.setZwartspeler(speler06);

    try {
      inhaalronde.addPartij(partij1);
      inhaalronde.addPartij(partij2);
    } catch (CaissaException e) {
      fail("Geen CaissaException verwacht.");
    }
    try {
      inhaalronde.addPartij(partij3);
      fail("CaissaException verwacht.");
    } catch (CaissaException e) {
      assertEquals(Speelronde.COD_INH_SPELER,
                   e.getLocalizedMessage().split(":")[0]);
      assertTrue(e.getLocalizedMessage().contains(speler01.getNaam()));
    }
    inhaalronde.setRonde(1);
    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(2), inhaalronde.getAantalPartijen());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testAddPartijen4() {
    var inhaalronde = new Speelronde();
    var partij1     = new Partij();
    var partij2     = new Partij();
    var partij3     = new Partij();

    partij1.setRonde(new Round("1"));
    partij1.setWitspeler(speler01);
    partij1.setZwartspeler(speler02);
    partij2.setRonde(new Round("1"));
    partij2.setWitspeler(speler03);
    partij2.setZwartspeler(speler04);
    partij3.setRonde(new Round("1"));
    partij3.setWitspeler(speler02);
    partij3.setZwartspeler(speler01);

    try {
      inhaalronde.addPartij(partij1);
      inhaalronde.addPartij(partij2);
    } catch (CaissaException e) {
      fail("Geen CaissaException verwacht.");
    }
    try {
      inhaalronde.addPartij(partij3);
      fail("CaissaException verwacht.");
    } catch (CaissaException e) {
      assertEquals(Speelronde.COD_INH_SPELERS,
                   e.getLocalizedMessage().split(":")[0]);
      assertTrue(e.getLocalizedMessage().contains(speler01.getNaam()));
      assertTrue(e.getLocalizedMessage().contains(speler02.getNaam()));
      assertTrue(e.getLocalizedMessage().indexOf(speler02.getNaam()) <
                 e.getLocalizedMessage().indexOf(speler01.getNaam()));
    }

    inhaalronde.setRonde(1);
    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(2), inhaalronde.getAantalPartijen());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testAddPartijen5() throws CaissaException {
    var inhaalronde = new Speelronde();
    var partij1     = new Partij();
    var partij2     = new Partij();
    var partij3     = new Partij();

    partij1.setRonde(new Round("1"));
    partij1.setWitspeler(speler01);
    partij1.setZwartspeler(speler02);
    partij2.setRonde(new Round("1"));
    partij2.setWitspeler(speler03);
    partij2.setZwartspeler(speler04);
    partij3.setRonde(new Round("1"));
    partij3.setWitspeler(speler05);
    partij3.setZwartspeler(spelerbye);

    inhaalronde.addPartij(partij1);
    inhaalronde.addPartij(partij2);
    inhaalronde.addPartij(partij3);
    inhaalronde.setRonde(1);
    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(2), inhaalronde.getAantalPartijen());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testCompareTo() {
    var gelijk      = new Speelronde();
    var groter      = new Speelronde();
    var kleiner     = new Speelronde();
    var inhaalronde = new Speelronde();

    inhaalronde.setRonde(2);
    gelijk.setRonde(2);
    groter.setRonde(3);
    kleiner.setRonde(1);

    assertTrue(inhaalronde.compareTo(kleiner) > 0);
    assertEquals(0, inhaalronde.compareTo(gelijk));
    assertTrue(inhaalronde.compareTo(groter) < 0);
  }

  @Test
  public void testEquals1() {
    var inhaalronde1  = new Speelronde();
    var inhaalronde2  = new Speelronde();
    var inhaalronde3  = new Speelronde();

    inhaalronde1.setRonde(1);
    inhaalronde2.setRonde(1);
    inhaalronde3.setRonde(2);

    assertEquals(inhaalronde1, inhaalronde1);
    assertEquals(inhaalronde1, inhaalronde2);
    assertNotEquals(inhaalronde1, null);
    assertNotEquals(inhaalronde1, inhaalronde3);
  }

  @Test
  public void testEquals2() {
    var inhaalronde1  = new Speelronde();
    var inhaalronde2  = new Speelronde();
    var inhaalronde3  = new Speelronde();

    inhaalronde1.setRonde(1);
    inhaalronde1.setSpeeldatum(TestConstants.RUSHDATUM);
    inhaalronde2.setRonde(1);
    inhaalronde2.setSpeeldatum(TestConstants.RUSHDATUM);
    inhaalronde3.setRonde(1);
    inhaalronde3.setSpeeldatum(TestConstants.RUSHJAAR);

    assertEquals(inhaalronde1, inhaalronde1);
    assertEquals(inhaalronde1, inhaalronde2);
    assertNotEquals(inhaalronde1, null);
    assertNotEquals(inhaalronde1, inhaalronde3);
  }

  @Test
  public void testHashCode() {
    var inhaalronde1  = new Speelronde();
    var inhaalronde2  = new Speelronde();
    var inhaalronde3  = new Speelronde();

    inhaalronde1.setRonde(1);
    inhaalronde1.setSpeeldatum(TestConstants.RUSHDATUM);
    inhaalronde2.setRonde(2);
    inhaalronde2.setSpeeldatum(TestConstants.RUSHDATUM);
    inhaalronde3.setRonde(1);
    inhaalronde3.setSpeeldatum(TestConstants.RUSHJAAR);

    assertNotEquals(inhaalronde1.hashCode(), inhaalronde2.hashCode());
    assertNotEquals(inhaalronde1.hashCode(), inhaalronde3.hashCode());
    assertNotEquals(inhaalronde2.hashCode(), inhaalronde3.hashCode());
  }

  @Test
  public void testInit() {
    var inhaalronde = new Speelronde();

    assertEquals(Integer.valueOf(0), inhaalronde.getAantalPartijen());
    assertTrue(inhaalronde.getPartijen().isEmpty());
    assertNull(inhaalronde.getRonde());
    assertEquals(CaissaConstants.DEF_EINDDATUM, inhaalronde.getSpeeldatum());
    assertEquals("Speelronde (ronde: null, speeldatum: 9999.99.99, "
                  + "spelers: [])",
                 inhaalronde.toString());
  }

  @Test
  public void testRonde() {
    var inhaalronde = new Speelronde();

    inhaalronde.setRonde(1);

    assertEquals(Integer.valueOf(0), inhaalronde.getAantalPartijen());
    assertTrue(inhaalronde.getPartijen().isEmpty());
    assertEquals(Integer.valueOf(1), inhaalronde.getRonde());
    assertEquals(CaissaConstants.DEF_EINDDATUM, inhaalronde.getSpeeldatum());
  }

  @Test
  public void testSpeeldatum() {
    var inhaalronde = new Speelronde();

    inhaalronde.setSpeeldatum(TestConstants.RUSHDATUM);

    assertEquals(Integer.valueOf(0), inhaalronde.getAantalPartijen());
    assertTrue(inhaalronde.getPartijen().isEmpty());
    assertNull(inhaalronde.getRonde());
    assertEquals(TestConstants.RUSHDATUM, inhaalronde.getSpeeldatum());
  }
}
