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
import eu.debooy.caissa.exceptions.ZetException;
import eu.debooy.doosutils.exception.BestandException;
import eu.debooy.doosutils.test.BatchTest;
import java.util.Locale;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class FENTest extends BatchTest {
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
  public static final String  FEN_FOUT   =
      "1nbq1bnr/ppppkppp/8/4p3/3PP3/8/PPP1KPPP/RNBQ1BNR b - d3 0 3";

  private FEN fen;

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale(TestConstants.TST_TAAL));
    resourceBundle   = ResourceBundle.getBundle("CaissaCore",
                                                Locale.getDefault());
  }

  @Test
  public void testGeefZet1() throws ZetException, FenException {
    fen = new FEN();
    Zet e2e4    = new Zet(' ', 35, 55);
    FEN fenE2e4 = new FEN(FEN_1E2E4);

    Zet zet;
    try {
      zet = fen.geefZet(fenE2e4);
      assertEquals(e2e4, zet);
    } catch (FenException e) {
      fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testGeefZet2() throws ZetException, FenException {
    fen = new FEN();
    Zet e2e4    = new Zet(' ', 35, 55);
    FEN fenE2e4 = new FEN(FEN_1E2E4);

    Zet zet;
    try {
      zet = fenE2e4.geefZet(fen);
      assertEquals(e2e4, zet);
    } catch (FenException e) {
      fail(e.getLocalizedMessage());
    }
  }

  @Test
  public void testGeefZet3() throws FenException {
    fen = new FEN();
    FEN fen2Ke8e7 = new FEN(FEN_2KE8E7);

    try {
      var zet = fen2Ke8e7.geefZet(fen);
      fail("Er had een FenException moeten wezen.");
    } catch (FenException e) {
      assertEquals(resourceBundle.getString(FEN.ERR_ZET),
                   e.getMessage());
    }
  }

  @Test
  public void testGeefZet4() throws FenException {
    fen = new FEN();
    FEN fen3d2d4 = new FEN(FEN_3D2D4);

    try {
      var zet = fen3d2d4.geefZet(fen);
      fail("Er had een FenException moeten wezen.");
    } catch (FenException e) {
      assertEquals(resourceBundle.getString(FEN.ERR_ZET),
                   e.getMessage());
    }
  }

  @Test
  public void testGeefZet5() throws FenException {
    fen = new FEN();
    FEN fenFout = new FEN(FEN_FOUT);

    try {
      var zet = fenFout.geefZet(fen);
      fail("Er had een FenException moeten wezen.");
    } catch (FenException e) {
      assertEquals(resourceBundle.getString(FEN.ERR_ZET),
                   e.getMessage());
    }
  }

  @Test
  public void testRokade() {
    fen = new FEN();

    assertTrue(fen.getWitKorteRokade());
    assertTrue(fen.getWitLangeRokade());
    assertTrue(fen.getZwartKorteRokade());
    assertTrue(fen.getZwartLangeRokade());
  }

  @Test
  public void testSetFEN() throws FenException {
    fen = new FEN();
    fen.setFen(FEN_1E2E4);
    assertEquals('b', fen.getAanZet());
    assertEquals("e3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRokade());
    assertEquals("rnbqkbnrpppppppp20P11PPPP1PPPRNBQKBNR",
                 fen.getKortePositie());
    assertEquals(Integer.valueOf(1), fen.getZetnummer());
  }

  @Test
  public void testDoeE2e4() throws FenException, ZetException {
    fen = new FEN();
    fen.doeZet(new Zet(' ', 35, 55));
    assertEquals('b', fen.getAanZet());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRokade());
    assertEquals(Integer.valueOf(1), fen.getZetnummer());
    assertEquals(FEN_1E2E4, fen.getFen());
  }

  @Test
  public void testDoeE7e5() throws FenException, ZetException {
    fen = new FEN(FEN_1E2E4);
    fen.doeZet(new Zet(' ', 85, 65));
    assertEquals('w', fen.getAanZet());
    assertEquals("e6", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("KQkq", fen.getRokade());
    assertEquals(Integer.valueOf(2), fen.getZetnummer());
    assertEquals(FEN_1E7E5, fen.getFen());
  }

  @Test
  public void testDoeKe1e2() throws FenException, ZetException {
    fen = new FEN(FEN_1E7E5);
    fen.doeZet(new Zet('K', 25, 35));
    assertEquals('b', fen.getAanZet());
    assertEquals("-", fen.getEnPassant());
    assertEquals(Integer.valueOf(1), fen.getHalvezetten());
    assertEquals("kq", fen.getRokade());
    assertEquals(Integer.valueOf(2), fen.getZetnummer());
    assertEquals(FEN_2KE1E2, fen.getFen());
  }

  @Test
  public void testDoeKe8e7() throws FenException, ZetException {
    fen = new FEN(FEN_2KE1E2);
    fen.doeZet(new Zet('K', 95, 85));
    assertEquals('w', fen.getAanZet());
    assertEquals("-", fen.getEnPassant());
    assertEquals(Integer.valueOf(2), fen.getHalvezetten());
    assertEquals("-", fen.getRokade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_2KE8E7, fen.getFen());
  }

  @Test
  public void testDoeD2d4() throws FenException, ZetException {
    fen = new FEN(FEN_2KE8E7);
    fen.doeZet(new Zet(' ', 34, 54));
    assertEquals('b', fen.getAanZet());
    assertEquals("d3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("-", fen.getRokade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_3D2D4, fen.getFen());
  }

  @Test
  public void testDoeZetten() throws FenException, ZetException {
    fen = new FEN();
    fen.doeZet(new Zet(' ', 35, 55));
    fen.doeZet(new Zet(' ', 85, 65));
    fen.doeZet(new Zet('K', 25, 35));
    fen.doeZet(new Zet('K', 95, 85));
    fen.doeZet(new Zet(' ', 34, 54));
    assertEquals('b', fen.getAanZet());
    assertEquals("d3", fen.getEnPassant());
    assertEquals(Integer.valueOf(0), fen.getHalvezetten());
    assertEquals("-", fen.getRokade());
    assertEquals(Integer.valueOf(3), fen.getZetnummer());
    assertEquals(FEN_3D2D4, fen.getFen());
  }
}
