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

import eu.debooy.caissa.exceptions.FenException;
import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.caissa.exceptions.ZetException;

import junit.framework.TestCase;

import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class CaissaUtilsTest extends TestCase {
//  @Test
//  public void testChessTheatre() throws FenException {
//    String  pgnZetten     = "1.e4 e5 2.Nf3 Nc6 3.Bc4 Bc5 4.c3 Nf6 5.d4 exd4 6.cxd4 Bb6 7.O-O O-O 8.Bg5 h6 9.Bh4 d6 10.Nc3 Bg4 11.d5 Nd4 12.Be2 Bxf3 13.Bxf3 Qd7 14.Bxf6 Nxf3+ 15.Qxf3 g5 16.Qh5 Kh7 17.Bxg5 f5 18.Qxh6+ Kg8 19.Bf6 Qh7 20.Qg5+ Kf7 21.exf5 Rg8 22.Qh4 Qxf5 23.Bd4 Bxd4 24.Qxd4 Qh3 25.Qf4+ Kg6 26.gxh3 Raf8 27.Qe4+ Kh5+ 28.Kh1 Rg7 29.f3 Rg5 30.Rg1 Rfg8 31.Rxg5+ Rxg5 32.Qh7#";
//    try {
//      try {
//        System.out.println(CaissaUtils.pgnZettenToChessTheatre(pgnZetten));
//        System.out.println("Test OK");
//      } catch (ZetException e) {
//        System.out.println(e.getMessage());
//      }
//    } catch (PgnException e) {
//      System.out.println(e.getMessage());
//    }
//  }

  @Test
  public void testPgnZettenToChessTheatre()
      throws FenException, PgnException, ZetException {
    String  pgnZetten     = "1.e4 e5 2.d4 exd4 3.Nf3 Nc6 4.Bc4 d6 5.O-O Bg4 6.c3 dxc3 7.Nxc3 Nf6 8.Bg5 Ne5 9.Be2 Nxf3+ 10.Bxf3 Bxf3 11.Qxf3 Be7 12.Rad1 Qc8 13.Rfe1 O-O 14.Qd3 Re8 15.f4 Nh5 16.Bxe7 Rxe7 17.Qf3 Nf6 18.e5 dxe5 19.fxe5 Nd7 20.Qg3 c6 21.Rc1 Qe8 22.Ne4 Kf8 23.Nd6 Qd8 24.Qf4 Nxe5 25.Rxe5 Qxd6 26.Rce1 Rxe5 27.Rxe5 Rd8 28.Qe3 Qd1+ 29.Kf2 Qd2+ 30.Kf3 Qxe3+ 31.Kxe3 Re8 32.Rxe8+ Kxe8";
    String  chessTheatre  = "36P15. 12.15p 35P15. 28.6p 45N16. 1.16n 34B26. 11.7p 60.RK. 2.35b 42P7. 35.6p 42N14. 6.14n 30B27. 18.9n 34.17B 28.16n 45B6. 38.6b 45Q13. 5.6b 56.2R 2q. 60R. 4.rk. 43Q1. 4r. 37P15. 21.9n 12B17. 4.7r 43.1Q 21n9. 28P7. 19.8p 28P8. 11n9. 45.Q 10.7p 58R. 2.1q 36N5. 5k. 19N16. 3q. 37Q8. 11.16n 28R31. 3.15q 58.1R 12.15r 28R31. .2r 37.6Q 19.39q 53K8. 51q7. 45K7. 44q6. 44K. 3.r 4R23. 4k.";
//    System.out.println(chessTheatre);
//    System.out.println(CaissaUtils.pgnZettenToChessTheatre(pgnZetten));
    assertTrue(chessTheatre.equals(CaissaUtils.pgnZettenToChessTheatre(pgnZetten)));
  }
}
