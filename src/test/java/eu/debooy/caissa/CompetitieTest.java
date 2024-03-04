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

import eu.debooy.caissa.exceptions.CompetitieException;
import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.doosutils.exception.BestandException;
import eu.debooy.doosutils.test.BatchTest;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class CompetitieTest extends BatchTest {
  protected static final  ClassLoader CLASSLOADER =
      CompetitieTest.class.getClassLoader();

  private static final  String  BST_COMP_DZ   = "competitieDZ.json";
  private static final  String  BST_COMP_M    = "competitiem.json";
  private static final  String  BST_COMP_MF   = "competitiemf.json";
  private static final  String  BST_COMP_3    = "competitie3.json";
  private static final  String  BST_COMP_31   = "competitie3_1.json";
  private static final  String  BST_COMP_32   = "competitie3_2.json";
  private static final  String  BST_COMP_33   = "competitie3_3.json";
  private static final  String  BST_COMP_34   = "competitie3_4.json";
  private static final  String  BST_COMP_35   = "competitie3_5.json";
  private static final  String  BST_COMP_36   = "competitie3_6.json";
  private static final  String  BST_COMP_3E   = "competitie3e.json";
  private static final  String  BST_COMP_3M   = "competitie3m.json";
  private static final  String  BST_COMP_4    = "competitie4.json";
  private static final  String  BST_COMP_41   = "competitie4-1.json";
  private static final  String  BST_COMP_4E   = "competitie4e.json";
  private static final  String  BST_COMP_4H   = "competitie4heen.json";
  private static final  String  BST_COMP_4T   = "competitie4terug.json";
  private static final  String  BST_COMP_5    = "competitie5.json";
  private static final  String  BST_COMP_51   = "competitie5-1.json";
  private static final  String  BST_COMP_5H   = "competitie5heen.json";
  private static final  String  BST_COMP_5T   = "competitie5terug.json";
  private static final  String  BST_COMP_PGN  = "competitie_pgn.pgn";

  private static final  String  TST_INHAALPARTIJ1 =
      "{\"datum\":\"19\\/10\\/2021\",\"wit\":\"Speler, Bob\",\"ronde\":1,\"zwart\":\"Speler, Carol\"}";
  private static final  String  TST_INHAALPARTIJ2 =
      "{\"datum\":\"16\\/11\\/2021\",\"wit\":\"Speler, Carol\",\"ronde\":1,\"zwart\":\"Speler, Bob\"}";

  @AfterClass
  public static void afterClass() {
    verwijderBestanden(getTemp() + File.separator,
                       new String[] {BST_COMP_3, BST_COMP_31, BST_COMP_32,
                                     BST_COMP_33, BST_COMP_34, BST_COMP_35,
                                     BST_COMP_36, BST_COMP_3E, BST_COMP_3M,
                                     BST_COMP_4, BST_COMP_41, BST_COMP_4E,
                                     BST_COMP_4H, BST_COMP_4T, BST_COMP_5,
                                     BST_COMP_51, BST_COMP_5H, BST_COMP_5T,
                                     BST_COMP_M, BST_COMP_MF, BST_COMP_DZ,
                                     BST_COMP_PGN});
  }

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale(TestConstants.TST_TAAL));
    resourceBundle   = ResourceBundle.getBundle("CaissaCore",
                                                Locale.getDefault());
    try {
      kopieerBestand(CLASSLOADER, BST_COMP_3,
                     getTemp() + File.separator + BST_COMP_3);
      kopieerBestand(CLASSLOADER, BST_COMP_31,
                     getTemp() + File.separator + BST_COMP_31);
      kopieerBestand(CLASSLOADER, BST_COMP_32,
                     getTemp() + File.separator + BST_COMP_32);
      kopieerBestand(CLASSLOADER, BST_COMP_33,
                     getTemp() + File.separator + BST_COMP_33);
      kopieerBestand(CLASSLOADER, BST_COMP_34,
                     getTemp() + File.separator + BST_COMP_34);
      kopieerBestand(CLASSLOADER, BST_COMP_35,
                     getTemp() + File.separator + BST_COMP_35);
      kopieerBestand(CLASSLOADER, BST_COMP_36,
                     getTemp() + File.separator + BST_COMP_36);
      kopieerBestand(CLASSLOADER, BST_COMP_3E,
                     getTemp() + File.separator + BST_COMP_3E);
      kopieerBestand(CLASSLOADER, BST_COMP_3M,
                     getTemp() + File.separator + BST_COMP_3M);
      kopieerBestand(CLASSLOADER, BST_COMP_4,
                     getTemp() + File.separator + BST_COMP_4);
      kopieerBestand(CLASSLOADER, BST_COMP_41,
                     getTemp() + File.separator + BST_COMP_41);
      kopieerBestand(CLASSLOADER, BST_COMP_4E,
                     getTemp() + File.separator + BST_COMP_4E);
      kopieerBestand(CLASSLOADER, BST_COMP_4H,
                     getTemp() + File.separator + BST_COMP_4H);
      kopieerBestand(CLASSLOADER, BST_COMP_4T,
                     getTemp() + File.separator + BST_COMP_4T);
      kopieerBestand(CLASSLOADER, BST_COMP_5,
                     getTemp() + File.separator + BST_COMP_5);
      kopieerBestand(CLASSLOADER, BST_COMP_51,
                     getTemp() + File.separator + BST_COMP_51);
      kopieerBestand(CLASSLOADER, BST_COMP_5H,
                     getTemp() + File.separator + BST_COMP_5H);
      kopieerBestand(CLASSLOADER, BST_COMP_5T,
                     getTemp() + File.separator + BST_COMP_5T);
      kopieerBestand(CLASSLOADER, BST_COMP_M,
                     getTemp() + File.separator + BST_COMP_M);
      kopieerBestand(CLASSLOADER, BST_COMP_MF,
                     getTemp() + File.separator + BST_COMP_MF);
      kopieerBestand(CLASSLOADER, BST_COMP_DZ,
                     getTemp() + File.separator + BST_COMP_DZ);
      kopieerBestand(CLASSLOADER, BST_COMP_PGN,
                     getTemp() + File.separator + BST_COMP_PGN);
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
      throw new BestandException(e);
    }
  }

  @Test
  public void testDubbelrondig3() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_3);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(3),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
      assertEquals(1, competitie.getInhaalpartijen().size());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig3E() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_3E);

      assertFalse(competitie.isDubbel());
      assertTrue(competitie.isEnkel());
      assertEquals(Integer.valueOf(3),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(3),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(3), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig3M() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_3M);

      fail("Er had een CompetitieException moeten wezen.");
    } catch (CompetitieException e) {
      var errno = resourceBundle.getString(Competitie.ERR_MATCHSPELERS)
                                .split(" ")[0];
      assertTrue(e.getLocalizedMessage().startsWith(errno));
    }
  }

  @Test
  public void testDubbelrondig4() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_4);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(4),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig4E() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_4E);

      assertFalse(competitie.isDubbel());
      assertTrue(competitie.isEnkel());
      assertEquals(Integer.valueOf(4),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(3),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(3), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig41() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_41);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(4),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig4H() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_4H);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(4),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig4T() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_4T);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(4),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig5() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_5);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(5),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(10),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(10), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig51() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_51);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(5),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig5H() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_5H);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(5),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(8),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(8), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig5HSpelers() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_5H);

      var spelersH  = competitie.getSpelersHeenronde();
      var spelersT  = competitie.getSpelersTerugronde();
      var rondesH   = competitie.getAantalHeenrondes();
      var rondesT   = competitie.getAantalTerugrondes();

      assertEquals(5, spelersH.size());
      assertEquals(4, spelersT.size());
      assertEquals(5, rondesH.intValue());
      assertEquals(3, rondesT.intValue());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig5T() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_5T);

      assertTrue(competitie.isDubbel());
      assertEquals(Integer.valueOf(5),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(8),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(8), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondig5TSpelers() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_5T);

      var spelersH  = competitie.getSpelersHeenronde();
      var spelersT  = competitie.getSpelersTerugronde();
      var rondesH   = competitie.getAantalHeenrondes();
      var rondesT   = competitie.getAantalTerugrondes();

      assertEquals(4, spelersH.size());
      assertEquals(5, spelersT.size());
      assertEquals(3, rondesH.intValue());
      assertEquals(5, rondesT.intValue());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondigM() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_M);

      assertFalse(competitie.isDubbel());
      assertFalse(competitie.isEnkel());
      assertEquals(Integer.valueOf(2),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(6),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(6), competitie.getRondes());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testDubbelrondigMF() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_MF);

      fail("Er had een CompetitieException moeten wezen.");
    } catch (CompetitieException e) {
      var errno = resourceBundle.getString(Competitie.ERR_KALENDER)
                                .split(" ")[0];
      assertTrue(e.getLocalizedMessage().startsWith(errno));
    }
  }

  @Test
  public void testDubbelZwitsers() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_DZ);

      assertTrue(competitie.isDubbel());
      assertFalse(competitie.isEnkel());
      assertEquals(Integer.valueOf(7),
                   Integer.valueOf(competitie.getSpelers().size()));
      assertEquals(Integer.valueOf(3),
                   Integer.valueOf(competitie.getSpeeldata().size()));
      assertEquals(Integer.valueOf(3), competitie.getRondes());
      assertEquals(4.0, competitie.getPuntenWinst());
      assertEquals(2.0, competitie.getPuntenRemise());
      assertEquals(1.0, competitie.getPuntenVerlies());
      assertEquals(3.0, competitie.getPuntenBye());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal1() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_31);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal2() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_32);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal3() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_33);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal4() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_34);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal5() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_35);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
      assertEquals(TST_INHAALPARTIJ2,
                   competitie.getInhaalpartijen().get(1).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testInhaal6() {
    try {
      var competitie  =
          new Competitie(getTemp() + File.separator + BST_COMP_36);

      assertEquals(2, competitie.getInhaalpartijen().size());
      assertEquals(TST_INHAALPARTIJ1,
                   competitie.getInhaalpartijen().get(0).toString());
      assertEquals(TST_INHAALPARTIJ2,
                   competitie.getInhaalpartijen().get(1).toString());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testConstructor2() throws PgnException {
    var partijen  =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + BST_COMP_PGN);

    try {
      var competitie  = new Competitie(partijen, 2);

      assertTrue(competitie.getInhaalpartijen().isEmpty());
      assertTrue(competitie.getKalender().isEmpty());
      assertEquals(3, competitie.getSpelers().size());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testConstructors1() throws PgnException {
    var partijen  =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + BST_COMP_PGN);

    try {
      var metPgn  = new Competitie(partijen, 2);
      var metJson = new Competitie(getTemp() + File.separator + BST_COMP_3);

      assertEquals(metPgn.getDeelnemers().size(),
                   metJson.getDeelnemers().size());
      assertEquals(metPgn.getAantalHeenrondes(), metJson.getAantalHeenrondes());
      assertEquals(metPgn.getAantalTerugrondes(),
                   metJson.getAantalTerugrondes());
      assertEquals(metPgn.getEvent(), metJson.getEvent());
      assertEquals(metPgn.getEventdate(), metJson.getEventdate());
      assertEquals(metPgn.getSite(), metJson.getSite());
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }

  @Test
  public void testConstructors2() throws PgnException {
    var partijen  =
        CaissaUtils.laadPgnBestand(getTemp() + File.separator
                                    + BST_COMP_PGN);

    try {
      var metPgn  = new Competitie(partijen, 2);
      var metJson = new Competitie(getTemp() + File.separator + BST_COMP_3);

      assertEquals(metPgn.getDeelnemers().size(),
                   metJson.getDeelnemers().size());

      var deelnemers  = metPgn.getDeelnemers();
      deelnemers.forEach(deelnemer -> {
        assertEquals(deelnemer.getNaam(),
                     metJson.getSpeler(deelnemer.getNaam()).getNaam());
      });
    } catch (CompetitieException e) {
      fail("Er had geen CompetitieException moeten wezen. "
            + e.getLocalizedMessage());
    }
  }
}
