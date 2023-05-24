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

import eu.debooy.caissa.exceptions.CaissaException;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ZettengeneratorTest extends TestCase {
  @Test
  public void testBeginstand() throws CaissaException {
    var zetten  = new Zettengenerator(new FEN());

    assertEquals(20, zetten.getAantalZetten());
    assertEquals(20, zetten.getZetten().size());
  }

  @Test
  public void testPromotie() throws CaissaException {
    var fen     = new FEN("4k3/P7/8/8/8/8/8/4K3 w - - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(9, zetten.getAantalZetten());
    assertEquals(9, zetten.getZetten().size());
  }

  @Test
  public void testRokade1() throws CaissaException {
    var fen     = new FEN("r3k2r/8/8/1p6/2P5/8/8/R3K2R b KQkq - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(28, zetten.getAantalZetten());
    assertEquals(28, zetten.getZetten().size());
  }

  @Test
  public void testRokade2() throws CaissaException {
    var fen     = new FEN("r3k2r/8/8/1p6/2P5/8/8/R3K2R b kq - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(28, zetten.getAantalZetten());
    assertEquals(28, zetten.getZetten().size());
  }

  @Test
  public void testRokade3() throws CaissaException {
    var fen     = new FEN("r3k2r/8/8/1p6/2P5/8/8/R3K2R b - - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(26, zetten.getAantalZetten());
    assertEquals(26, zetten.getZetten().size());
  }

  @Test
  public void testRokade4() throws CaissaException {
    var fen     = new FEN("5rkr/8/8/1p6/2P5/8/8/5RKR b FHfh - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(22, zetten.getAantalZetten());
    assertEquals(22, zetten.getZetten().size());
  }

  @Test
  public void testRokade5() throws CaissaException {
    var fen     = new FEN("rkr5/8/8/1p6/2P5/8/8/RKR5 b ACac - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(21, zetten.getAantalZetten());
    assertEquals(21, zetten.getZetten().size());
  }

  @Test
  public void testNotatielevel() throws CaissaException {
    var fen     =
        new FEN("k3b3/1n5p/3N1N2/2N1R1N1/1R1R1R2/2N1R1N1/3N1N2/K3R3 w - - 0 1");
    var zetten  = new Zettengenerator(fen);

    assertEquals(93, zetten.getAantalZetten());
    assertEquals(93, zetten.getZetten().size());
  }

  @Test
  public void testPionschaak() throws CaissaException {
    var fen     =
        new FEN("1r1r2k1/5p1p/p2p2P1/1p2p3/5nN1/P6P/1P3PP1/1BRb2K1 w - - 0 29");
    var zetten  = new Zettengenerator(fen);
    var zet     = new Zet(77, 88);
    zet.setSchaak(true);
    zet.setSlagzet(true);
    assertTrue(zetten.getZetten().toString().contains(zet.toString()));

    zet = new Zet(77, 86);
    zet.setSchaak(true);
    zet.setSlagzet(true);
    assertTrue(zetten.getZetten().toString().contains(zet.toString()));
  }
}
