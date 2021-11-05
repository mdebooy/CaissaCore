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

import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ZetTest extends TestCase {
  private Zet zet;

  @Test
  public void testE2e4() {
    zet = new Zet(' ', 35, 55);

    assertEquals("36P15.", zet.getChessTheatreZet());
    assertEquals("5254", zet.getCorrespondentieZet());
    assertEquals("e4", zet.getKorteNotatie());
    assertEquals("e2-e4", zet.getLangeNotatie());
    assertEquals("e4", zet.getPgnNotatie());
  }

  @Test
  public void testE7e8R() {
    zet = new Zet(' ', 85, 95, 'R');

    assertEquals("4R7.", zet.getChessTheatreZet());
    assertEquals("5758", zet.getCorrespondentieZet());
    assertEquals("e8R", zet.getKorteNotatie());
    assertEquals("e7-e8R", zet.getLangeNotatie());
    assertEquals("e8=R", zet.getPgnNotatie());
  }

  @Test
  public void testE5d6ep() {
    zet = new Zet(' ', 65, 74);
    zet.setEp(true);

    assertEquals("19P7..", zet.getChessTheatreZet());
    assertEquals("5546", zet.getCorrespondentieZet());
    assertEquals("exd6 e.p.", zet.getKorteNotatie());
    assertEquals("e5xd6 e.p.", zet.getLangeNotatie());
    assertEquals("exd6", zet.getPgnNotatie());
  }

  @Test
  public void testE5f6ep() {
    zet = new Zet(' ', 65, 76);
    zet.setEp(true);

    assertEquals("21P6..", zet.getChessTheatreZet());
    assertEquals("5566", zet.getCorrespondentieZet());
    assertEquals("exf6 e.p.", zet.getKorteNotatie());
    assertEquals("e5xf6 e.p.", zet.getLangeNotatie());
    assertEquals("exf6", zet.getPgnNotatie());
  }

  @Test
  public void testE4d3ep() {
    zet = new Zet(' ', 55, 44);
    zet.setEp(true);

    assertEquals("35..6p", zet.getChessTheatreZet());
    assertEquals("5443", zet.getCorrespondentieZet());
    assertEquals("exd3 e.p.", zet.getKorteNotatie());
    assertEquals("e4xd3 e.p.", zet.getLangeNotatie());
    assertEquals("exd3", zet.getPgnNotatie());
  }

  @Test
  public void testE4f3ep() {
    zet = new Zet(' ', 55, 46);
    zet.setEp(true);

    assertEquals("36..7p", zet.getChessTheatreZet());
    assertEquals("5463", zet.getCorrespondentieZet());
    assertEquals("exf3 e.p.", zet.getKorteNotatie());
    assertEquals("e4xf3 e.p.", zet.getLangeNotatie());
    assertEquals("exf3", zet.getPgnNotatie());
  }

  @Test
  public void testKe1e2() {
    zet = new Zet('K', 25, 35);

    assertEquals("52K7.", zet.getChessTheatreZet());
    assertEquals("5152", zet.getCorrespondentieZet());
    assertEquals("Ke2", zet.getKorteNotatie());
    assertEquals("Ke1-e2", zet.getLangeNotatie());
    assertEquals("Ke2", zet.getPgnNotatie());
  }

  @Test
  public void testPg1f3m() {
    zet = new Zet('N', 27, 46);
    zet.setMat(true);

    assertEquals("45N16.", zet.getChessTheatreZet());
    assertEquals("7163", zet.getCorrespondentieZet());
    assertEquals("Nf3#", zet.getKorteNotatie());
    assertEquals("Ng1-f3#", zet.getLangeNotatie());
    assertEquals("Nf3#", zet.getPgnNotatie());
  }

  @Test
  public void testPg1xf3() {
    zet = new Zet('N', 27, 46);
    zet.setSlagzet(true);

    assertEquals("45N16.", zet.getChessTheatreZet());
    assertEquals("7163", zet.getCorrespondentieZet());
    assertEquals("Nxf3", zet.getKorteNotatie());
    assertEquals("Ng1xf3", zet.getLangeNotatie());
    assertEquals("Nxf3", zet.getPgnNotatie());
  }

  @Test
  public void testPg8f6() {
    zet = new Zet('N', 97, 76);

    assertEquals("6.14N", zet.getChessTheatreZet());
    assertEquals("7866", zet.getCorrespondentieZet());
    assertEquals("Nf6", zet.getKorteNotatie());
    assertEquals("Ng8-f6", zet.getLangeNotatie());
    assertEquals("Nf6", zet.getPgnNotatie());
  }

  @Test
  public void testPg8f6s() {
    zet = new Zet('N', 97, 76);
    zet.setSchaak(true);

    assertEquals("6.14N", zet.getChessTheatreZet());
    assertEquals("7866", zet.getCorrespondentieZet());
    assertEquals("Nf6+", zet.getKorteNotatie());
    assertEquals("Ng8-f6+", zet.getLangeNotatie());
    assertEquals("Nf6+", zet.getPgnNotatie());
  }

  @Test
  public void testOO() {
    zet = new Zet('K', 95, 97);

    assertEquals("4.rk.", zet.getChessTheatreZet());
    assertEquals("5878", zet.getCorrespondentieZet());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getKorteNotatie());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getLangeNotatie());
    assertEquals(CaissaConstants.KORTE_ROCHADE, zet.getPgnNotatie());
  }

  @Test
  public void testOOO() {
    zet = new Zet('K', 25, 23);

    assertEquals("56.1KR.", zet.getChessTheatreZet());
    assertEquals("5131", zet.getCorrespondentieZet());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getKorteNotatie());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getLangeNotatie());
    assertEquals(CaissaConstants.LANGE_ROCHADE, zet.getPgnNotatie());
  }

  @Test
  public void testOOOm() {
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
  public void testOOOs() {
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
  public void testOOm() {
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
  public void testOOs() {
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
}
