/**
 * Copyright 2010 Marco de Booij
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

import eu.debooy.caissa.exceptions.FenException;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class FENTest extends TestCase {
  public static final String  FEN_1E2E4  =
      "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
  public static final String  FEN_1E7E5  =
      "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
  public static final String  FEN_2KE1E2 =
      "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2";
  public static final String  FEN_2KE8E7 =
      "rnbq1bnr/ppppkppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR w - - 2 3";
  public static final String  FEN_3D2D4  =
      "rnbq1bnr/ppppkppp/8/4p3/3PP3/8/PPP1KPPP/RNBQ1BNR b - d3 0 3";

  private FEN fen;

  @Test
  public void testRochade() {
    fen = new FEN();

    assertTrue(fen.getWitKorteRochade());
    assertTrue(fen.getWitLangeRochade());
    assertTrue(fen.getZwartKorteRochade());
    assertTrue(fen.getZwartLangeRochade());
  }

  @Test
  public void testSetFEN() throws FenException {
    fen = new FEN();
    fen.setFen(FEN_1E2E4);
    assertEquals('b', fen.getAanZet());
    assertEquals("e3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRochade());
    assertEquals("rnbqkbnrpppppppp20P11PPPP1PPPRNBQKBNR", fen.getKortePositie());
    assertEquals(Integer.valueOf(1), fen.getZetnummer());
  }

  @Test
  public void testDoeE2e4() throws FenException {
    fen = new FEN();
    fen.doeZet(new Zet(' ', 35, 55));
    assertEquals('b', fen.getAanZet());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRochade());
    assertEquals(Integer.valueOf(1), fen.getZetnummer());
    assertEquals(FEN_1E2E4, fen.getFen());
  }

  @Test
  public void testDoeE7e5() throws FenException {
    fen = new FEN(FEN_1E2E4);
    fen.doeZet(new Zet(' ', 85, 65));
    assertEquals('w', fen.getAanZet());
    assertEquals("e6", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRochade());
    assertEquals(Integer.valueOf(2), fen.getZetnummer());
    assertEquals(FEN_1E7E5, fen.getFen());
  }

  @Test
  public void testDoeKe1e2() throws FenException {
    fen = new FEN(FEN_1E7E5);
    fen.doeZet(new Zet('K', 25, 35));
    assertEquals('b', fen.getAanZet());
    assertEquals("-", fen.getEnPassant());
    assertEquals(Integer.valueOf(1), fen.getHalvezetten());
    assertEquals("kq", fen.getRochade());
    assertEquals(Integer.valueOf(2), fen.getZetnummer());
    assertEquals(FEN_2KE1E2, fen.getFen());
  }

  @Test
  public void testDoeKe8e7() throws FenException {
    fen = new FEN(FEN_2KE1E2);
    fen.doeZet(new Zet('K', 95, 85));
    assertEquals('w', fen.getAanZet());
    assertEquals("-", fen.getEnPassant());
    assertEquals(Integer.valueOf(2), fen.getHalvezetten());
    assertEquals("-", fen.getRochade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_2KE8E7, fen.getFen());
  }

  @Test
  public void testDoeD2d4() throws FenException {
    fen = new FEN(FEN_2KE8E7);
    fen.doeZet(new Zet(' ', 34, 54));
    assertEquals('b', fen.getAanZet());
    assertEquals("d3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("-", fen.getRochade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_3D2D4, fen.getFen());
  }

  @Test
  public void testDoeZetten() throws FenException {
    fen = new FEN();
    fen.doeZet(new Zet(' ', 35, 55));
    fen.doeZet(new Zet(' ', 85, 65));
    fen.doeZet(new Zet('K', 25, 35));
    fen.doeZet(new Zet('K', 95, 85));
    fen.doeZet(new Zet(' ', 34, 54));
    assertEquals('b', fen.getAanZet());
    assertEquals("d3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("-", fen.getRochade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_3D2D4, fen.getFen());
  }
}
