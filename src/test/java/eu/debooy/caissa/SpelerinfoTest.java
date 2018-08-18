/**
 * Copyright 2009 Marco de Booij
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
 * limitations under the Licence.
 */
package eu.debooy.caissa;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class SpelerinfoTest extends TestCase {
  public static final String  ADAMS_JAN   = "Adams, Jan";
  public static final String  DEVRIES_JAN = "de Vries, Jan";
  public static final String  JANSEN_JAN  = "Jansen, Jan";

  private Spelerinfo  spelerinfo0;
  private Spelerinfo  spelerinfo1;
  private Spelerinfo  spelerinfo2;
  private Spelerinfo  spelerinfo3;
  private Spelerinfo  spelerinfo4;
  private Spelerinfo  spelerinfo5;
  private Spelerinfo  spelerinfo6;
  private Spelerinfo  spelerinfo7;
  private Spelerinfo  spelerinfo8;
  
  @Before
  public void setUp() { 
    spelerinfo0 = new Spelerinfo();
    spelerinfo1 = new Spelerinfo();
    spelerinfo2 = new Spelerinfo();
    spelerinfo3 = new Spelerinfo();
    spelerinfo4 = new Spelerinfo();
    spelerinfo5 = new Spelerinfo();
    spelerinfo6 = new Spelerinfo();
    spelerinfo7 = new Spelerinfo();
    spelerinfo8 = new Spelerinfo();

    spelerinfo4.setNaam(JANSEN_JAN);
    spelerinfo4.setPartijen(9);
    spelerinfo4.setPunten(8.5);
    spelerinfo4.setTieBreakScore(20.0);

    spelerinfo2.setNaam(ADAMS_JAN);
    spelerinfo2.setPartijen(10);
    spelerinfo2.setPunten(8.5);
    spelerinfo2.setTieBreakScore(20.0);

    spelerinfo3.setNaam(DEVRIES_JAN);
    spelerinfo3.setPartijen(10);
    spelerinfo3.setPunten(8.5);
    spelerinfo3.setTieBreakScore(20.0);

    spelerinfo1.setNaam(JANSEN_JAN);
    spelerinfo1.setPartijen(10);
    spelerinfo1.setPunten(8.5);
    spelerinfo1.setTieBreakScore(20.0);

    spelerinfo8.setNaam(JANSEN_JAN);
    spelerinfo8.setPartijen(10);
    spelerinfo8.setPunten(8.5);
    spelerinfo8.setTieBreakScore(20.0);

    spelerinfo6.setNaam(JANSEN_JAN);
    spelerinfo6.setPartijen(10);
    spelerinfo6.setPunten(8.5);
    spelerinfo6.setTieBreakScore(19.75);

    spelerinfo5.setNaam(JANSEN_JAN);
    spelerinfo5.setPartijen(10);
    spelerinfo5.setPunten(7.5);
    spelerinfo5.setTieBreakScore(20.0);

    spelerinfo7.setPartijen(0);
    spelerinfo7.setPunten(0.0);
    spelerinfo7.setTieBreakScore(0.0);
  }

  @Test
  public void testCompareGelijk() {
    assertTrue(velden(spelerinfo0) + " = " + velden(spelerinfo7),
               spelerinfo0.compareTo(spelerinfo7) == 0);
    assertTrue(velden(spelerinfo1) + " = " + velden(spelerinfo1),
               spelerinfo1.compareTo(spelerinfo1) == 0);
    assertTrue(velden(spelerinfo1) + " = " + velden(spelerinfo8),
               spelerinfo1.compareTo(spelerinfo8) == 0);
  }

  @Test
  public void testCompareGroter() {
    assertTrue(velden(spelerinfo1) + " > " + velden(spelerinfo2),
               spelerinfo1.compareTo(spelerinfo2) > 0);
    assertTrue(velden(spelerinfo1) + " > " + velden(spelerinfo3),
               spelerinfo1.compareTo(spelerinfo3) > 0);
    assertTrue(velden(spelerinfo1) + " > " + velden(spelerinfo4),
               spelerinfo1.compareTo(spelerinfo4) > 0);
  }

  @Test
  public void testCompareKleiner() {
    assertTrue(velden(spelerinfo1) + " < " + velden(spelerinfo0),
               spelerinfo1.compareTo(spelerinfo0) < 0);
    assertTrue(velden(spelerinfo1) + " < " + velden(spelerinfo5),
               spelerinfo1.compareTo(spelerinfo5) < 0);
    assertTrue(velden(spelerinfo1) + " < " + velden(spelerinfo6),
               spelerinfo1.compareTo(spelerinfo6) < 0);
  }

  private String velden(Spelerinfo speler) {
    return speler.getPunten() + " - " + speler.getPartijen() + " - "
           + speler.getTieBreakScore() + " - " + speler.getNaam();
  }
}
