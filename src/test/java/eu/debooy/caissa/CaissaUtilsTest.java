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
import eu.debooy.doosutils.access.JsonBestand;
import eu.debooy.doosutils.exception.BestandException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class CaissaUtilsTest extends TestCase {
  protected static final  ClassLoader CLASSLOADER =
      CaissaUtilsTest.class.getClassLoader();
  protected static final  String      JSON_COMPETITIE3      =
      "competitie3.json";
  protected static final  String      JSON_COMPETITIE4_1    =
      "competitie4-1.json";
  protected static final  String      JSON_COMPETITIE4      =
      "competitie4.json";
  protected static final  String      JSON_COMPETITIE4HEEN  =
      "competitie4heen.json";
  protected static final  String      JSON_COMPETITIE4TERUG =
      "competitie4terug.json";
  protected static final  String      JSON_COMPETITIE5_1    =
      "competitie5-1.json";
  protected static final  String      JSON_COMPETITIE5      =
      "competitie5.json";
  protected static final  String      JSON_COMPETITIE5HEEN  =
      "competitie5heen.json";
  protected static final  String      JSON_COMPETITIE5TERUG =
      "competitie5terug.json";

  public static final String[]  testrondes4   =
        {"1-4 2-3",
         "4-3 1-2",
         "2-4 3-1"};
    public static final String[]  testrondes12  =
        {"1-12 2-11 3-10 4-9 5-8 6-7",
         "12-7 8-6 9-5 10-4 11-3 1-2",
         "2-12 3-1 4-11 5-10 6-9 7-8",
         "12-8 9-7 10-6 11-5 1-4 2-3",
         "3-12 4-2 5-1 6-11 7-10 8-9",
         "12-9 10-8 11-7 1-6 2-5 3-4",
         "4-12 5-3 6-2 7-1 8-11 9-10",
         "12-10 11-9 1-8 2-7 3-6 4-5",
         "5-12 6-4 7-3 8-2 9-1 10-11",
         "12-11 1-10 2-9 3-8 4-7 5-6",
         "6-12 7-5 8-4 9-3 10-2 11-1"};

  private List<Spelerinfo> getSpelers(String jsonbestand) {
    JsonBestand       competitie  = null;
    List<Spelerinfo>  spelers     = new ArrayList<>();
    try {
      competitie  =
          new JsonBestand.Builder()
                         .setClassLoader(CLASSLOADER)
                         .setBestand(jsonbestand)
                         .build();
      JSONArray jsonArray = competitie.getArray("spelers");
      int       spelerId  = 1;
      for (Object jSpeler : jsonArray.toArray()) {
        Spelerinfo  speler  = new Spelerinfo((JSONObject) jSpeler);
        speler.setSpelerId(spelerId);
        spelers.add(speler);
        spelerId++;
      }
    } catch (BestandException e) {
      fail("BestandException: " + e.getLocalizedMessage());
    } finally {
      if (null != competitie) {
        try {
          competitie.close();
        } catch (BestandException e) {
          fail("BestandException: " + e.getLocalizedMessage());
        }
      }
    }

    return spelers;
  }

  private void testBergertabel(String[] rondes, String[] testrondes) {
    assertEquals(rondes.length, testrondes.length);
    for (var i = 0; i < testrondes.length; i++) {
      assertEquals("Ronde " + (i+1), rondes[i], testrondes[i]);
    }
  }

  @Test
  public void testBergertabel11() {
    testBergertabel(CaissaUtils.bergertabel(11), testrondes12);
  }

  @Test
  public void testBergertabel12() {
    testBergertabel(CaissaUtils.bergertabel(12), testrondes12);
  }

  @Test
  public void testBergertabel3() {
    testBergertabel(CaissaUtils.bergertabel(3), testrondes4);
  }

 @Test
  public void testBergertabel4() {
    testBergertabel(CaissaUtils.bergertabel(4), testrondes4);
  }

  @Test
  public void testGenereerSpeelschema1() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE3);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(6, byes);
  }

  @Test
  public void testGenereerSpeelschema2() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE4);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(0, byes);
  }

  @Test
  public void testGenereerSpeelschema3() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE4HEEN);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(3, byes);
  }

  @Test
  public void testGenereerSpeelschema4() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE4TERUG);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(3, byes);
  }

  @Test
  public void testGenereerSpeelschema5() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE4_1);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(6, byes);

    spelers     = getSpelers(JSON_COMPETITIE3);
    Set<Partij> partijen3 = CaissaUtils.genereerSpeelschema(spelers, false);

    assertEquals(partijen3.toString(), partijen.toString());
  }

  @Test
  public void testGenereerSpeelschema6() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE5);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(30, partijen.size());
    assertEquals(10, byes);
  }

  @Test
  public void testGenereerSpeelschema7() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE5HEEN);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(21, partijen.size());
    assertEquals(5, byes);
  }

  @Test
  public void testGenereerSpeelschema8() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE5TERUG);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(21, partijen.size());
    assertEquals(5, byes);
  }

  @Test
  public void testGenereerSpeelschema9() {
    List<Spelerinfo>  spelers     = getSpelers(JSON_COMPETITIE5_1);
    Set<Partij>       partijen    = CaissaUtils.genereerSpeelschema(spelers,
                                                                    false);
    long              byes        = partijen.stream()
                                            .filter(partij -> partij.isBye())
                                            .count();

    assertEquals(12, partijen.size());
    assertEquals(0, byes);

    spelers     = getSpelers(JSON_COMPETITIE4);
    Set<Partij> partijen4 = CaissaUtils.genereerSpeelschema(spelers, false);

    assertEquals(partijen4.toString(), partijen.toString());
  }

  @Test
  public void testPgnZettenToChessTheatre()
      throws FenException, PgnException, ZetException {
    var pgnZetten     =
        "1.e4 e5 2.d4 exd4 3.Nf3 Nc6 4.Bc4 d6 5.O-O Bg4 6.c3 dxc3 7.Nxc3 Nf6 "
        + "8.Bg5 Ne5 9.Be2 Nxf3+ 10.Bxf3 Bxf3 11.Qxf3 Be7 12.Rad1 Qc8 13.Rfe1 "
        + "O-O 14.Qd3 Re8 15.f4 Nh5 16.Bxe7 Rxe7 17.Qf3 Nf6 18.e5 dxe5 19.fxe5 "
        + "Nd7 20.Qg3 c6 21.Rc1 Qe8 22.Ne4 Kf8 23.Nd6 Qd8 24.Qf4 Nxe5 25.Rxe5 "
        + "Qxd6 26.Rce1 Rxe5 27.Rxe5 Rd8 28.Qe3 Qd1+ 29.Kf2 Qd2+ 30.Kf3 Qxe3+ "
        + "31.Kxe3 Re8 32.Rxe8+ Kxe8";
    var chessTheatre  =
        "36P15. 12.15p 35P15. 28.6p 45N16. 1.16n 34B26. 11.7p 60.RK. 2.35b "
        + "42P7. 35.6p 42N14. 6.14n 30B27. 18.9n 34.17B 28.16n 45B6. 38.6b "
        + "45Q13. 5.6b 56.2R 2q. 60R. 4.rk. 43Q1. 4r. 37P15. 21.9n 12B17. 4.7r "
        + "43.1Q 21n9. 28P7. 19.8p 28P8. 11n9. 45.Q 10.7p 58R. 2.1q 36N5. 5k. "
        + "19N16. 3q. 37Q8. 11.16n 28R31. 3.15q 58.1R 12.15r 28R31. .2r 37.6Q "
        + "19.39q 53K8. 51q7. 45K7. 44q6. 44K. 3.r 4R23. 4k.";
    assertEquals(chessTheatre, CaissaUtils.pgnZettenToChessTheatre(pgnZetten));
  }
}
