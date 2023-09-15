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

  private static final  String  BST_COMP_DZ = "competitieDZ.json";
  private static final  String  BST_COMP_M  = "competitiem.json";
  private static final  String  BST_COMP_MF = "competitiemf.json";
  private static final  String  BST_COMP_3  = "competitie3.json";
  private static final  String  BST_COMP_3E = "competitie3e.json";
  private static final  String  BST_COMP_3M = "competitie3m.json";
  private static final  String  BST_COMP_4  = "competitie4.json";
  private static final  String  BST_COMP_41 = "competitie4-1.json";
  private static final  String  BST_COMP_4E = "competitie4e.json";
  private static final  String  BST_COMP_4H = "competitie4heen.json";
  private static final  String  BST_COMP_4T = "competitie4terug.json";
  private static final  String  BST_COMP_5  = "competitie5.json";
  private static final  String  BST_COMP_51 = "competitie5-1.json";
  private static final  String  BST_COMP_5H = "competitie5heen.json";
  private static final  String  BST_COMP_5T = "competitie5terug.json";

  @AfterClass
  public static void afterClass() {
    verwijderBestanden(getTemp() + File.separator,
                       new String[] {BST_COMP_3, BST_COMP_3E, BST_COMP_3M,
                                     BST_COMP_4, BST_COMP_41, BST_COMP_4E,
                                     BST_COMP_4H, BST_COMP_4T, BST_COMP_5,
                                     BST_COMP_51, BST_COMP_5H, BST_COMP_5T,
                                     BST_COMP_M, BST_COMP_MF, BST_COMP_DZ});
  }

  @BeforeClass
  public static void beforeClass() throws BestandException {
    Locale.setDefault(new Locale(TestConstants.TST_TAAL));
    resourceBundle   = ResourceBundle.getBundle("CaissaCore",
                                                Locale.getDefault());
    try {
      kopieerBestand(CLASSLOADER, BST_COMP_3,
                     getTemp() + File.separator + BST_COMP_3);
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
}
