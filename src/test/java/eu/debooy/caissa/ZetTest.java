/**
 * Copyright 2011 Marco de Booij
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
 */
package eu.debooy.caissa;

import eu.debooy.caissa.exceptions.ZetException;
import eu.debooy.doosutils.exception.BestandException;
import eu.debooy.doosutils.test.BatchTest;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ZetTest extends BatchTest {
  private Zet zet;

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale(TestConstants.TST_TAAL));
    resourceBundle   = ResourceBundle.getBundle("CaissaCore",
                                                Locale.getDefault());
  }

  @Test
  public void testCompareTo1() throws ZetException {
    zet = new Zet('P', 35, 55, 'Q');

    var gelijk  = new Zet('P', 35, 55, 'Q');
    var groter  = new Zet('P', 35, 56, 'Q');
    var kleiner = new Zet('P', 35, 54, 'Q');
    assertTrue(zet.compareTo(groter) < 0);
    assertEquals(0, zet.compareTo(gelijk));
    assertTrue(zet.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo2() throws ZetException {
    zet = new Zet('N', 35, 55, ' ');

    var gelijk  = new Zet('N', 35, 55, ' ');
    var groter  = new Zet('R', 35, 55, ' ');
    var kleiner = new Zet('K', 35, 55, ' ');
    assertTrue(zet.compareTo(groter) < 0);
    assertEquals(0, zet.compareTo(gelijk));
    assertTrue(zet.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo3() throws ZetException {
    zet = new Zet('P', 35, 55, 'Q');

    var gelijk  = new Zet('P', 35, 55, 'Q');
    var groter  = new Zet('P', 36, 55, 'Q');
    var kleiner = new Zet('P', 34, 55, 'Q');
    assertTrue(zet.compareTo(groter) < 0);
    assertEquals(0, zet.compareTo(gelijk));
    assertTrue(zet.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo4() throws ZetException {
    zet = new Zet('P', 35, 55, 'Q');

    var gelijk  = new Zet(' ', 35, 55, 'Q');
    var groter  = new Zet(' ', 35, 55, 'R');
    var kleiner = new Zet(' ', 35, 55, 'B');
    assertTrue(zet.compareTo(groter) < 0);
    assertEquals(0, zet.compareTo(gelijk));
    assertTrue(zet.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo5() throws ZetException {
    zet = new Zet('P', 35, 55, 'Q');

    var gelijk  = new Zet(' ', 35, 55, 'Q');
    var groter  = new Zet('R', 36, 55, 'Q');
    var kleiner = new Zet('N', 34, 55, 'Q');
    assertTrue(zet.compareTo(groter) < 0);
    assertEquals(0, zet.compareTo(gelijk));
    assertTrue(zet.compareTo(kleiner) > 0);
  }

  @Test
  public void testE2e4() throws ZetException {
    zet = new Zet(' ', 35, 55);

    assertEquals("36P15.", zet.getChessTheatreZet());
    assertEquals("5254", zet.getCorrespondentieZet());
    assertEquals("e4", zet.getKorteNotatie());
    assertEquals("e2-e4", zet.getLangeNotatie());
    assertEquals("e4", zet.getPgnNotatie());
    assertEquals("e2e4", zet.getUciNotatie());
  }

  @Test
  public void testE4d3ep() throws ZetException {
    zet = new Zet(' ', 55, 44);
    zet.setEp(true);

    assertEquals("35..6p", zet.getChessTheatreZet());
    assertEquals("5443", zet.getCorrespondentieZet());
    assertEquals("exd3 e.p.", zet.getKorteNotatie());
    assertEquals("e4xd3 e.p.", zet.getLangeNotatie());
    assertEquals("exd3", zet.getPgnNotatie());
    assertEquals("e4d3", zet.getUciNotatie());
  }

  @Test
  public void testE4f3ep() throws ZetException {
    zet = new Zet(' ', 55, 46);
    zet.setEp(true);

    assertEquals("36..7p", zet.getChessTheatreZet());
    assertEquals("5463", zet.getCorrespondentieZet());
    assertEquals("exf3 e.p.", zet.getKorteNotatie());
    assertEquals("e4xf3 e.p.", zet.getLangeNotatie());
    assertEquals("exf3", zet.getPgnNotatie());
    assertEquals("e4f3", zet.getUciNotatie());
  }

  @Test
  public void testE5d6ep() throws ZetException {
    zet = new Zet(' ', 65, 74);
    zet.setEp(true);

    assertEquals("19P7..", zet.getChessTheatreZet());
    assertEquals("5546", zet.getCorrespondentieZet());
    assertEquals("exd6 e.p.", zet.getKorteNotatie());
    assertEquals("e5xd6 e.p.", zet.getLangeNotatie());
    assertEquals("exd6", zet.getPgnNotatie());
    assertEquals("e5d6", zet.getUciNotatie());
  }

  @Test
  public void testE5f6ep() throws ZetException {
    zet = new Zet(' ', 65, 76);
    zet.setEp(true);

    assertEquals("21P6..", zet.getChessTheatreZet());
    assertEquals("5566", zet.getCorrespondentieZet());
    assertEquals("exf6 e.p.", zet.getKorteNotatie());
    assertEquals("e5xf6 e.p.", zet.getLangeNotatie());
    assertEquals("exf6", zet.getPgnNotatie());
    assertEquals("e5f6", zet.getUciNotatie());
  }

  @Test
  public void testE7e8R() throws ZetException {
    zet = new Zet(' ', 85, 95, 'R');

    assertEquals("4R7.", zet.getChessTheatreZet());
    assertEquals("5758", zet.getCorrespondentieZet());
    assertEquals("e8R", zet.getKorteNotatie());
    assertEquals("e7-e8R", zet.getLangeNotatie());
    assertEquals("e8=R", zet.getPgnNotatie());
    assertEquals("e7e8r", zet.getUciNotatie());
  }

  @Test
  public void testEp() {
    zet = new Zet();
    assertFalse(zet.isEp());

    zet.setEp(true);
    assertTrue(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertTrue(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());

    zet.setEp(false);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testEquals() throws ZetException {
    var partij1 = new Zet(' ', 85, 95, 'R');
    var partij2 = new Zet(' ', 85, 95, 'R');
    var partij3 = new Zet(' ', 85, 95, 'Q');

    assertEquals(partij1, partij1);
    assertEquals(partij1, partij2);
    assertNotEquals(partij1, null);
    assertNotEquals(partij1, partij3);
  }

  @Test
  public void testHashCode() throws ZetException {
    zet = new Zet(' ', 85, 95, 'Q');
    assertEquals(981062, zet.hashCode());
    zet = new Zet(' ', 85, 95, 'R');
    assertEquals(981063, zet.hashCode());
  }

  @Test
  public void testInit1() {
    zet = new Zet();

    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testInit2() throws ZetException {
    zet = new Zet(35, 55);

    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(55, zet.getNaar());
    assertEquals(35, zet.getVan());
  }

  @Test
  public void testInit3() throws ZetException {
    zet = new Zet('N', 27, 46);

    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals('N', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(46, zet.getNaar());
    assertEquals(27, zet.getVan());
  }

  @Test
  public void testInit4() throws ZetException {
    zet = new Zet(' ', 85, 95, 'Q');

    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals('Q', zet.getPromotieStuk());
    assertEquals(95, zet.getNaar());
    assertEquals(85, zet.getVan());
    assertEquals("e8Q", zet.getKorteNotatie());
  }

  @Test
  public void testInit5() throws ZetException {
    zet           = new Zet(' ', 85, 95, 'Q');
    var instance  = new Zet(zet);
    zet.setSchaak(true);

    assertFalse(instance.isEp());
    assertFalse(instance.isMat());
    assertFalse(instance.isSchaak());
    assertFalse(instance.isSlagzet());
    assertEquals(0, instance.getKorteNotatieLevel());
    assertEquals(' ', instance.getStuk());
    assertEquals('Q', instance.getPromotieStuk());
    assertEquals(95, instance.getNaar());
    assertEquals(85, instance.getVan());
    assertEquals("e8Q", instance.getKorteNotatie());
  }

  @Test
  public void testInit6() {
    try {
      zet = new Zet('X', 85, 95, ' ');
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(MessageFormat.format(resourceBundle.getString(Zet.ERR_STUK),
                                        'X'),
                   e.getMessage());
    }
    try {
      zet = new Zet(' ', 85, 95, 'X');
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(
          MessageFormat.format(resourceBundle.getString(Zet.ERR_PROMOTIESTUK),
                               'X'),
                   e.getMessage());
    }
  }

  @Test
  public void testKe1e2() throws ZetException {
    zet = new Zet('K', 25, 35);

    assertEquals("52K7.", zet.getChessTheatreZet());
    assertEquals("5152", zet.getCorrespondentieZet());
    assertEquals("Ke2", zet.getKorteNotatie());
    assertEquals("Ke1-e2", zet.getLangeNotatie());
    assertEquals("Ke2", zet.getPgnNotatie());
  }

  @Test
  public void testKorteNotatieLevel() {
    zet = new Zet();
    assertEquals(0, zet.getKorteNotatieLevel());

    zet.setKorteNotatieLevel(1);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(1, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
    zet.setKorteNotatieLevel(0);
    assertEquals(0, zet.getKorteNotatieLevel());
    zet.setKorteNotatieLevel(2);
    assertEquals(2, zet.getKorteNotatieLevel());
    zet.setKorteNotatieLevel(3);
    assertEquals(3, zet.getKorteNotatieLevel());
    zet.setKorteNotatieLevel(-1);
    assertEquals(0, zet.getKorteNotatieLevel());
    zet.setKorteNotatieLevel(4);
    assertEquals(3, zet.getKorteNotatieLevel());
  }

  @Test
  public void testMat() {
    zet = new Zet();
    assertFalse(zet.isMat());

    zet.setMat(true);
    assertFalse(zet.isEp());
    assertTrue(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testNaar1() throws ZetException {
    zet = new Zet();
    assertEquals(0, zet.getNaar());

    zet.setNaar(55);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(55, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testNaar2() {
    zet = new Zet();
    assertEquals(0, zet.getNaar());

    try {
      zet.setNaar(21);
      zet.setNaar(35);
      zet.setNaar(98);
    } catch (ZetException ex) {
      fail("Geen ZetException verwacht.");
    }
    try {
      zet.setNaar(20);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_NAAR),
                   e.getMessage());
    }
    try {
      zet.setNaar(30);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_NAAR),
                   e.getMessage());
    }
    try {
      zet.setNaar(49);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_NAAR),
                   e.getMessage());
    }
    try {
      zet.setNaar(99);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_NAAR),
                   e.getMessage());
    }
  }

  @Test
  public void testOO() throws ZetException {
    zet = new Zet('K', 95, 97);

    assertEquals("4.rk.", zet.getChessTheatreZet());
    assertEquals("5878", zet.getCorrespondentieZet());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getKorteNotatie());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getLangeNotatie());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getPgnNotatie());
  }

  @Test
  public void testOOO() throws ZetException {
    zet = new Zet('K', 25, 23);

    assertEquals("56.1KR.", zet.getChessTheatreZet());
    assertEquals("5131", zet.getCorrespondentieZet());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getKorteNotatie());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getLangeNotatie());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getPgnNotatie());
  }

  @Test
  public void testOOOm() throws ZetException {
    zet = new Zet('K', 95, 93);
    zet.setMat(true);

    assertEquals(".1kr.", zet.getChessTheatreZet());
    assertEquals("5838", zet.getCorrespondentieZet());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"#"), zet.getKorteNotatie());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"#"), zet.getLangeNotatie());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"#"), zet.getPgnNotatie());
  }

  @Test
  public void testOOOs() throws ZetException {
    zet = new Zet('K', 95, 93);
    zet.setSchaak(true);

    assertEquals(".1kr.", zet.getChessTheatreZet());
    assertEquals("5838", zet.getCorrespondentieZet());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"+"), zet.getKorteNotatie());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"+"), zet.getLangeNotatie());
    assertEquals(
        (CaissaConstants.LANGE_ROCHADE+"+"), zet.getPgnNotatie());
  }

  @Test
  public void testOOm() throws ZetException {
    zet = new Zet('K', 25, 27);
    zet.setMat(true);

    assertEquals("60.RK.", zet.getChessTheatreZet());
    assertEquals("5171", zet.getCorrespondentieZet());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"#"), zet.getKorteNotatie());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"#"), zet.getLangeNotatie());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"#"), zet.getPgnNotatie());
  }

  @Test
  public void testOOs() throws ZetException {
    zet = new Zet('K', 25, 27);
    zet.setSchaak(true);

    assertEquals("60.RK.", zet.getChessTheatreZet());
    assertEquals("5171", zet.getCorrespondentieZet());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"+"), zet.getKorteNotatie());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"+"), zet.getLangeNotatie());
    assertEquals(
        (CaissaConstants.KORTE_ROCHADE+"+"), zet.getPgnNotatie());
  }

  @Test
  public void testPg1f3m() throws ZetException {
    zet = new Zet('N', 27, 46);
    zet.setMat(true);

    assertEquals("45N16.", zet.getChessTheatreZet());
    assertEquals("7163", zet.getCorrespondentieZet());
    assertEquals("Nf3#", zet.getKorteNotatie());
    assertEquals("Ng1-f3#", zet.getLangeNotatie());
    assertEquals("Nf3#", zet.getPgnNotatie());
  }

  @Test
  public void testPg1xf3() throws ZetException {
    zet = new Zet('N', 27, 46);
    zet.setSlagzet(true);

    assertEquals("45N16.", zet.getChessTheatreZet());
    assertEquals("7163", zet.getCorrespondentieZet());
    assertEquals("Nxf3", zet.getKorteNotatie());
    assertEquals("Ng1xf3", zet.getLangeNotatie());
    assertEquals("Nxf3", zet.getPgnNotatie());
  }

  @Test
  public void testPg8f6() throws ZetException {
    zet = new Zet('N', 97, 76);

    assertEquals("6.14N", zet.getChessTheatreZet());
    assertEquals("7866", zet.getCorrespondentieZet());
    assertEquals("Nf6", zet.getKorteNotatie());
    assertEquals("Ng8-f6", zet.getLangeNotatie());
    assertEquals("Nf6", zet.getPgnNotatie());
  }

  @Test
  public void testPg8f6s() throws ZetException {
    zet = new Zet('N', 97, 76);
    zet.setSchaak(true);

    assertEquals("6.14N", zet.getChessTheatreZet());
    assertEquals("7866", zet.getCorrespondentieZet());
    assertEquals("Nf6+", zet.getKorteNotatie());
    assertEquals("Ng8-f6+", zet.getLangeNotatie());
    assertEquals("Nf6+", zet.getPgnNotatie());
  }

  @Test
  public void testPromotieStuk1() throws ZetException {
    zet = new Zet();
    assertEquals(' ', zet.getPromotieStuk());

    zet.setPromotieStuk('B');
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals('B', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testPromotieStuk2() {
    zet = new Zet();
    assertEquals(' ', zet.getPromotieStuk());

    try {
      zet.setPromotieStuk('B');
      zet.setPromotieStuk('N');
      zet.setPromotieStuk('Q');
      zet.setPromotieStuk('R');
      zet.setPromotieStuk(' ');
    } catch (ZetException ex) {
      fail("Geen ZetException verwacht.");
    }
    try {
      zet.setPromotieStuk('K');
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(
          MessageFormat.format(resourceBundle.getString(Zet.ERR_PROMOTIESTUK),
                               'K'),
                   e.getMessage());
    }
    try {
      zet.setPromotieStuk('X');
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(
          MessageFormat.format(resourceBundle.getString(Zet.ERR_PROMOTIESTUK),
                               'X'),
                   e.getMessage());
    }
  }

  @Test
  public void testSchaak() {
    zet = new Zet();
    assertFalse(zet.isSchaak());

    zet.setSchaak(true);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertTrue(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testSlagzet() {
    zet = new Zet();
    assertFalse(zet.isSlagzet());

    zet.setSlagzet(true);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertTrue(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testStuk1() throws ZetException {
    zet = new Zet();
    assertEquals(' ', zet.getStuk());

    zet.setStuk('B');
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals('B', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(0, zet.getVan());
  }

  @Test
  public void testStuk2() {
    zet = new Zet();
    assertEquals(' ', zet.getStuk());

    try {
      zet.setStuk('B');
      zet.setStuk('K');
      zet.setStuk('N');
      zet.setStuk('Q');
      zet.setStuk('R');
      zet.setStuk(' ');
    } catch (ZetException ex) {
      fail("Geen ZetException verwacht.");
    }
    try {
      zet.setStuk('X');
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(MessageFormat.format(resourceBundle.getString(Zet.ERR_STUK),
                                        'X'),
                   e.getMessage());
    }
  }

  @Test
  public void testToString() throws ZetException {
    zet = new Zet(' ', 85, 95, 'Q');

    assertEquals("e7-e8Q|e8Q", zet.toString());
  }

  @Test
  public void testVan1() throws ZetException {
    zet = new Zet();
    assertEquals(0, zet.getVan());

    zet.setVan(35);
    assertFalse(zet.isEp());
    assertFalse(zet.isMat());
    assertFalse(zet.isSchaak());
    assertFalse(zet.isSlagzet());
    assertEquals(0, zet.getKorteNotatieLevel());
    assertEquals(' ', zet.getStuk());
    assertEquals(' ', zet.getPromotieStuk());
    assertEquals(0, zet.getNaar());
    assertEquals(35, zet.getVan());
  }

  @Test
  public void testVan2() {
    zet = new Zet();
    assertEquals(0, zet.getVan());

    try {
      zet.setVan(21);
      zet.setVan(35);
      zet.setVan(98);
    } catch (ZetException ex) {
      fail("Geen ZetException verwacht.");
    }
    try {
      zet.setVan(20);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_VAN),
                   e.getMessage());
    }
    try {
      zet.setVan(30);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_VAN),
                   e.getMessage());
    }
    try {
      zet.setVan(49);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_VAN),
                   e.getMessage());
    }
    try {
      zet.setVan(99);
      fail("Een ZetException verwacht.");
    } catch (ZetException e) {
      assertEquals(resourceBundle.getString(Zet.ERR_VAN),
                   e.getMessage());
    }
  }
}
