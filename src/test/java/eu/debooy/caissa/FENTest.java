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
    assertTrue(fen.getAanZet() == 'b');
    assertTrue(fen.getEnPassant().equals("e3"));
    assertTrue(fen.getHalvezetten() == 0);
    assertTrue("KQkq".equals(fen.getRochade()));
    assertTrue("rnbqkbnrpppppppp20P11PPPP1PPPRNBQKBNR".equals(fen.getKortePositie()));
    assertTrue(fen.getZetnummer() == 1);
  }

  @Test
  public void testZetInfo() throws FenException {
    fen = new FEN(FEN_1E2E4);
    assertTrue(fen.getAanZet() == 'b');
    assertTrue(fen.getEnPassant().equals("e3"));
    assertTrue(fen.getHalvezetten() == 0);
    assertTrue("KQkq".equals(fen.getRochade()));
    assertTrue("rnbqkbnrpppppppp20P11PPPP1PPPRNBQKBNR".equals(fen.getKortePositie()));
    assertTrue(fen.getZetnummer() == 1);
  }

  @Test
  public void testDoeE2e4() throws FenException {
    fen = new FEN();
    fen.doeZet(new Zet(' ', 35, 55));
    assertTrue(fen.getAanZet() == 'b');
    assertTrue(fen.getEnPassant().equals("e3"));
    assertTrue(fen.getHalvezetten() == 0);
    assertTrue("KQkq".equals(fen.getRochade()));
    assertTrue(fen.getZetnummer() == 1);
  }

  @Test
  public void testDoeE7e5() throws FenException {
    fen = new FEN(FEN_1E2E4);
    fen.doeZet(new Zet(' ', 85, 65));
    assertTrue(fen.getAanZet() == 'w');
    assertTrue(fen.getEnPassant().equals("e6"));
    assertTrue(fen.getHalvezetten() == 0);
    assertTrue("KQkq".equals(fen.getRochade()));
    assertTrue(fen.getZetnummer() == 2);
  }

  @Test
  public void testDoeKe1e2() throws FenException {
    fen = new FEN("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2");
    fen.doeZet(new Zet('K', 25, 35));
    assertTrue(fen.getAanZet() == 'b');
    assertTrue(fen.getEnPassant().equals("-"));
    assertTrue(fen.getHalvezetten() == 1);
    assertTrue("kq".equals(fen.getRochade()));
    assertTrue(fen.getZetnummer() == 2);
  }

  @Test
  public void testDoeKe8e7() throws FenException {
    fen = new FEN("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2");
    fen.doeZet(new Zet('K', 95, 85));
    assertTrue(fen.getAanZet() == 'w');
    assertTrue(fen.getEnPassant().equals("-"));
    assertTrue(fen.getHalvezetten() == 2);
    assertTrue("-".equals(fen.getRochade()));
    assertTrue(fen.getZetnummer() == 3);
  }

  @Test
  public void testDoeD2d4() throws FenException {
    fen = new FEN("rnbq1bnr/ppppkppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR w - - 2 3");
    fen.doeZet(new Zet(' ', 34, 54));
    assertTrue(fen.getAanZet() == 'b');
    assertTrue(fen.getEnPassant().equals("d3"));
    assertTrue(fen.getHalvezetten() == 0);
    assertTrue("-".equals(fen.getRochade()));
    assertTrue(fen.getZetnummer() == 3);
  }
}
