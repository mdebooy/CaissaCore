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

//    System.out.println("ChessTheatre   : " + zet.getChessTheatreZet());
//    System.out.println("Correspondentie: " + zet.getCorrespondentieZet());
//    System.out.println("Korte Notatie  : " + zet.getKorteNotatie());
//    System.out.println("Lange Notatie  : " + zet.getLangeNotatie());
//    System.out.println("PGN Notatie    : " + zet.getPgnNotatie());
    assertTrue("36P15.".equals(zet.getChessTheatreZet()));
    assertTrue("5254".equals(zet.getCorrespondentieZet()));
    assertTrue("e4".equals(zet.getKorteNotatie()));
    assertTrue("e2-e4".equals(zet.getLangeNotatie()));
    assertTrue("e4".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testE7e8R() {
    zet = new Zet(' ', 85, 95, 'R');

    assertTrue("4R7.".equals(zet.getChessTheatreZet()));
    assertTrue("5758".equals(zet.getCorrespondentieZet()));
    assertTrue("e8R".equals(zet.getKorteNotatie()));
    assertTrue("e7-e8R".equals(zet.getLangeNotatie()));
    assertTrue("e8=R".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testE5d6ep() {
    zet = new Zet(' ', 65, 74);
    zet.setEp(true);

    assertTrue("19P7..".equals(zet.getChessTheatreZet()));
    assertTrue("5546".equals(zet.getCorrespondentieZet()));
    assertTrue("exd6 e.p.".equals(zet.getKorteNotatie()));
    assertTrue("e5xd6 e.p.".equals(zet.getLangeNotatie()));
    assertTrue("exd6".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testE5f6ep() {
    zet = new Zet(' ', 65, 76);
    zet.setEp(true);

    assertTrue("21P8..".equals(zet.getChessTheatreZet()));
    assertTrue("5566".equals(zet.getCorrespondentieZet()));
    assertTrue("exf6 e.p.".equals(zet.getKorteNotatie()));
    assertTrue("e5xf6 e.p.".equals(zet.getLangeNotatie()));
    assertTrue("exf6".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testE4d3ep() {
    zet = new Zet(' ', 55, 44);
    zet.setEp(true);

    assertTrue("35..6p".equals(zet.getChessTheatreZet()));
    assertTrue("5443".equals(zet.getCorrespondentieZet()));
    assertTrue("exd3 e.p.".equals(zet.getKorteNotatie()));
    assertTrue("e4xd3 e.p.".equals(zet.getLangeNotatie()));
    assertTrue("exd3".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testE4f3ep() {
    zet = new Zet(' ', 55, 46);
    zet.setEp(true);

    assertTrue("36..7p".equals(zet.getChessTheatreZet()));
    assertTrue("5463".equals(zet.getCorrespondentieZet()));
    assertTrue("exf3 e.p.".equals(zet.getKorteNotatie()));
    assertTrue("e4xf3 e.p.".equals(zet.getLangeNotatie()));
    assertTrue("exf3".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testKe1e2() {
    zet = new Zet('K', 25, 35);

    assertTrue("52K7.".equals(zet.getChessTheatreZet()));
    assertTrue("5152".equals(zet.getCorrespondentieZet()));
    assertTrue("Ke2".equals(zet.getKorteNotatie()));
    assertTrue("Ke1-e2".equals(zet.getLangeNotatie()));
    assertTrue("Ke2".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testPg1f3m() {
    zet = new Zet('N', 27, 46);
    zet.setMat(true);

    assertTrue("45N16.".equals(zet.getChessTheatreZet()));
    assertTrue("7163".equals(zet.getCorrespondentieZet()));
    assertTrue("Nf3#".equals(zet.getKorteNotatie()));
    assertTrue("Ng1-f3#".equals(zet.getLangeNotatie()));
    assertTrue("Nf3#".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testPg1xf3() {
    zet = new Zet('N', 27, 46);
    zet.setSlagzet(true);

    assertTrue("45N16.".equals(zet.getChessTheatreZet()));
    assertTrue("7163".equals(zet.getCorrespondentieZet()));
    assertTrue("Nxf3".equals(zet.getKorteNotatie()));
    assertTrue("Ng1xf3".equals(zet.getLangeNotatie()));
    assertTrue("Nxf3".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testPg8f6() {
    zet = new Zet('N', 97, 76);

    assertTrue("6.14N".equals(zet.getChessTheatreZet()));
    assertTrue("7866".equals(zet.getCorrespondentieZet()));
    assertTrue("Nf6".equals(zet.getKorteNotatie()));
    assertTrue("Ng8-f6".equals(zet.getLangeNotatie()));
    assertTrue("Nf6".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testPg8f6s() {
    zet = new Zet('N', 97, 76);
    zet.setSchaak(true);

    assertTrue("6.14N".equals(zet.getChessTheatreZet()));
    assertTrue("7866".equals(zet.getCorrespondentieZet()));
    assertTrue("Nf6+".equals(zet.getKorteNotatie()));
    assertTrue("Ng8-f6+".equals(zet.getLangeNotatie()));
    assertTrue("Nf6+".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testOOO() {
    zet = new Zet('K', 25, 23);

    assertTrue("56.1KR.".equals(zet.getChessTheatreZet()));
    assertTrue("5131".equals(zet.getCorrespondentieZet()));
    assertTrue("O-O-O".equals(zet.getKorteNotatie()));
    assertTrue("O-O-O".equals(zet.getLangeNotatie()));
    assertTrue("O-O-O".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testOO() {
    zet = new Zet('K', 95, 97);

    assertTrue("4.rk.".equals(zet.getChessTheatreZet()));
    assertTrue("5878".equals(zet.getCorrespondentieZet()));
    assertTrue("O-O".equals(zet.getKorteNotatie()));
    assertTrue("O-O".equals(zet.getLangeNotatie()));
    assertTrue("O-O".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testOOs() {
    zet = new Zet('K', 25, 27);
    zet.setSchaak(true);

    assertTrue("60.RK.".equals(zet.getChessTheatreZet()));
    assertTrue("5171".equals(zet.getCorrespondentieZet()));
    assertTrue("O-O+".equals(zet.getKorteNotatie()));
    assertTrue("O-O+".equals(zet.getLangeNotatie()));
    assertTrue("O-O+".equals(zet.getPgnNotatie()));
  }

  @Test
  public void testOOOm() {
    zet = new Zet('K', 95, 93);
    zet.setMat(true);

    assertTrue(".1kr.".equals(zet.getChessTheatreZet()));
    assertTrue("5838".equals(zet.getCorrespondentieZet()));
    assertTrue("O-O-O#".equals(zet.getKorteNotatie()));
    assertTrue("O-O-O#".equals(zet.getLangeNotatie()));
    assertTrue("O-O-O#".equals(zet.getPgnNotatie()));
  }
}
