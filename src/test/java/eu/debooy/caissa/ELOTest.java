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

import junit.framework.TestCase;

import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ELOTest extends TestCase {
  @Test
  public void testBerekenTPR01() {
    assertNull(ELO.berekenTPR(0, 0, 0, 0));
  }

  @Test
  public void testBerekenTPR02() {
    Integer tpr = ELO.berekenTPR(1668, 13, 5, 4);
    
    assertTrue(1806 == tpr);
  }

  @Test
  public void testBerekenTPR03() {
    Integer tpr = ELO.berekenTPR(1668, 22, 0, 0);
    
    assertTrue(2068 == tpr);
  }

  @Test
  public void testBerekenTPR04() {
    Integer tpr = ELO.berekenTPR(1668, 0, 0, 22);
    
    assertTrue(1268 == tpr);
  }

  @Test
  public void testBerekenELO01() {
    Integer elo = ELO.berekenELO(1500, CaissaConstants.WINST, 1700, 1);

    assertTrue(1523 == elo);
  }

  @Test
  public void testBerekenELO02() {
    Integer elo = ELO.berekenELO(1500, CaissaConstants.REMISE, 1700, 1);

    assertTrue(1508 == elo);
  }

  @Test
  public void testBerekenELO03() {
    Integer elo = ELO.berekenELO(1500, CaissaConstants.VERLIES, 1700, 1);

    assertTrue(1493 == elo);
  }

  @Test
  public void testBerekenELO04() {
    Integer elo = ELO.berekenELO(1600, CaissaConstants.WINST, 1600, 32, 400);

    assertTrue(1616 == elo);
  }

  @Test
  public void testBerekenELO05() {
    Integer elo = ELO.berekenELO(1600, CaissaConstants.REMISE, 1600, 32, 400);

    assertTrue(1600 == elo);
  }

  @Test
  public void testBerekenELO06() {
    Integer elo = ELO.berekenELO(1600, CaissaConstants.VERLIES, 1600, 32, 400);

    assertTrue(1584 == elo);
  }

  @Test
  public void testBerekenELO07() {
    Integer elo = ELO.berekenELO(1700, CaissaConstants.WINST, 1500, 32, 400);

    assertTrue(1708 == elo);
  }

  @Test
  public void testBerekenELO08() {
    Integer elo = ELO.berekenELO(1700, CaissaConstants.REMISE, 1500, 32, 400);

    assertTrue(1692 == elo);
  }

  @Test
  public void testBerekenELO09() {
    Integer elo = ELO.berekenELO(1700, CaissaConstants.VERLIES, 1500, 32, 400);

    assertTrue(1676 == elo);
  }
}
