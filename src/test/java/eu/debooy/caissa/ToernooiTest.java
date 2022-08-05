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

import static eu.debooy.caissa.Toernooi.ERR_RONDES;
import static eu.debooy.caissa.Toernooi.resourceBundle;
import eu.debooy.caissa.exceptions.ToernooiException;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ToernooiTest extends TestCase {
  private     Toernooi  toernooi;

  @Test
  public void testDatum() {
    try {
      toernooi  = new Toernooi.Builder().setEinddatum("2022.12.21")
                                        .setStartdatum("2112.12.21")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertTrue(toernooi.getType().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(Toernooi.ERR_DATUM),
                   e.getLocalizedMessage());
    }

    try {
      toernooi  = new Toernooi.Builder().setEinddatum("2112.12.21")
                                        .setStartdatum("2112.12.??")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertEquals("2112.12.21", toernooi.getEinddatum().get());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertEquals("2112.12.??", toernooi.getStartdatum().get());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try {
      toernooi  = new Toernooi.Builder().setEinddatum("2112.12.21")
                                        .setStartdatum("2112.??.??")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertEquals("2112.12.21", toernooi.getEinddatum().get());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertEquals("2112.??.??", toernooi.getStartdatum().get());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testEmptyToernooi() {
    try {
      toernooi  = new Toernooi.Builder().build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testFoutiefToernooi() {
    try {
      toernooi  = new Toernooi.Builder().setAantalSpelers(0)
                                        .setRondes(0)
                                        .setType(-1)
                                        .build();

      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      String  exception = e.getLocalizedMessage();
      assertTrue(exception.contains(
              resourceBundle.getString(Toernooi.ERR_TYPE)));
      assertTrue(exception.contains(
              resourceBundle.getString(Toernooi.ERR_AANTALSPELERS)));
      assertTrue(exception.contains(
              resourceBundle.getString(Toernooi.ERR_RONDES)));
    }
  }

  @Test
  public void testGetAantalSpelers() {
    try {
      toernooi  = new Toernooi.Builder().setAantalSpelers(10)
                                        .setType(Toernooi.TOERNOOI_DUBBEL)
                                        .build();

      assertEquals(Integer.valueOf(10), toernooi.getAantalSpelers().get());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertEquals(Integer.valueOf(18), toernooi.getRondes().get());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertEquals(Toernooi.TOERNOOI_DUBBEL, toernooi.getType().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try{
      toernooi  = new Toernooi.Builder().setAantalSpelers(0).build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(Toernooi.ERR_AANTALSPELERS),
                   e.getLocalizedMessage());
    }

    try{
      toernooi  = new Toernooi.Builder().setAantalSpelers(-1).build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(Toernooi.ERR_AANTALSPELERS),
                   e.getLocalizedMessage());
    }

    try{
      toernooi  = new Toernooi.Builder().setRondes(10)
                                        .setType(Toernooi.TOERNOOI_DUBBEL)
                                        .build();

      assertEquals(Integer.valueOf(6), toernooi.getAantalSpelers().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try{
      toernooi  = new Toernooi.Builder().setRondes(10)
                                        .setType(Toernooi.TOERNOOI_ENKEL)
                                        .build();

      assertEquals(Integer.valueOf(11), toernooi.getAantalSpelers().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetEinddatum() {
    try{
      toernooi  = new Toernooi.Builder().setEinddatum("2112.12.21")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertEquals("2112.12.21", toernooi.getEinddatum().get());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try{
      toernooi  = new Toernooi.Builder().setEinddatum("2112.??.??")
                                        .build();

      assertEquals("2112.??.??", toernooi.getEinddatum().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetEvent() {
    try{
      toernooi  = new Toernooi.Builder().setEvent("Event")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertEquals("Event", toernooi.getEvent().get());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetRondes() {
    try {
      toernooi  = new Toernooi.Builder().setRondes(10)
                                        .setType(Toernooi.TOERNOOI_ENKEL)
                                        .build();

      assertEquals(Integer.valueOf(11), toernooi.getAantalSpelers().get());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertEquals(Integer.valueOf(10), toernooi.getRondes().get());
      assertTrue(toernooi.getSite().isEmpty());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertEquals(Toernooi.TOERNOOI_ENKEL, toernooi.getType().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try {
      toernooi  = new Toernooi.Builder().setRondes(0)
                                        .setType(Toernooi.TOERNOOI_ENKEL)
                                        .build();

      assertTrue(toernooi.getRondes().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(Toernooi.ERR_RONDES),
                   e.getLocalizedMessage());
    }

    try {
      toernooi  = new Toernooi.Builder().setRondes(-1)
                                        .setType(Toernooi.TOERNOOI_ENKEL)
                                        .build();

      assertTrue(toernooi.getRondes().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(ERR_RONDES),
                   e.getLocalizedMessage());
    }

    try {
      toernooi  = new Toernooi.Builder().setAantalSpelers(6)
                                        .setType(Toernooi.TOERNOOI_DUBBEL)
                                        .build();

      assertEquals(Integer.valueOf(10), toernooi.getRondes().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try {
      toernooi  = new Toernooi.Builder().setAantalSpelers(11)
                                        .setType(Toernooi.TOERNOOI_ENKEL)
                                        .build();

      assertEquals(Integer.valueOf(10), toernooi.getRondes().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetSite() {
    try {
      toernooi  = new Toernooi.Builder().setSite("Site")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertEquals("Site", toernooi.getSite().get());
      assertTrue(toernooi.getStartdatum().isEmpty());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetStartdatum() {
    try {
      toernooi  = new Toernooi.Builder().setStartdatum("2112.12.21")
                                        .build();

      assertTrue(toernooi.getAantalSpelers().isEmpty());
      assertTrue(toernooi.getEinddatum().isEmpty());
      assertTrue(toernooi.getEvent().isEmpty());
      assertTrue(toernooi.getRondes().isEmpty());
      assertTrue(toernooi.getSite().isEmpty());
      assertEquals("2112.12.21", toernooi.getStartdatum().get());
      assertTrue(toernooi.getType().isEmpty());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try {
      toernooi  = new Toernooi.Builder().setEinddatum("2112.??.??")
                                        .build();

      assertEquals("2112.??.??", toernooi.getEinddatum().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }
  }

  @Test
  public void testGetType() {
    try {
      toernooi  = new Toernooi.Builder()
                              .setType(Toernooi.TOERNOOI_DUBBEL)
                              .build();

      assertEquals(Toernooi.TOERNOOI_DUBBEL, toernooi.getType().get());
    } catch (ToernooiException e) {
      fail("Er had geen ToernooiException moeten wezen.");
    }

    try {
      toernooi  = new Toernooi.Builder().setType(-1)
                                        .build();

      assertTrue(toernooi.getType().isEmpty());
      fail("Er had een ToernooiException moeten wezen.");
    } catch (ToernooiException e) {
      assertEquals(resourceBundle.getString(Toernooi.ERR_TYPE),
                   e.getLocalizedMessage());
    }
  }

  @Test
  public void testIsDubbel() throws ToernooiException {
    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_DUBBEL).build();
    assertTrue(toernooi.isDubbel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_ENKEL).build();
    assertFalse(toernooi.isDubbel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_MATCH).build();
    assertFalse(toernooi.isDubbel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_ZWITSERS).build();
    assertFalse(toernooi.isDubbel());
  }

  @Test
  public void testIsEnkel() throws ToernooiException {
    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_DUBBEL).build();
    assertFalse(toernooi.isEnkel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_ENKEL).build();
    assertTrue(toernooi.isEnkel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_MATCH).build();
    assertFalse(toernooi.isEnkel());

    toernooi  = new Toernooi.Builder()
                            .setType(Toernooi.TOERNOOI_ZWITSERS).build();
    assertTrue(toernooi.isEnkel());
  }
}
