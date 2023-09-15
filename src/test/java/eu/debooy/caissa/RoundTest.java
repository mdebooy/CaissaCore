/**
 * Copyright 2011 Marco de Booij
 *
 * Licensed under the EUPL, Round 1.1 or - as soon they will be approved by
 * the European Commission - subsequent rounds of the EUPL (the "Licence");
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

import java.util.Arrays;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class RoundTest extends TestCase {
  @Test
  public void testCompareTo1() {
    var gelijk  = new Round("2");
    var groter  = new Round("3");
    var kleiner = new Round("1");
    var round   = new Round("2");

    assertTrue(round.compareTo(kleiner) > 0);
    assertEquals(0, round.compareTo(gelijk));
    assertTrue(round.compareTo(groter) < 0);
  }

  @Test
  public void testCompareTo2() {
    var gelijk  = new Round("1.2");
    var groter  = new Round("1.3");
    var kleiner = new Round("1.1");
    var round   = new Round("1.2");

    assertTrue(round.compareTo(kleiner) > 0);
    assertEquals(0, round.compareTo(gelijk));
    assertTrue(round.compareTo(groter) < 0);
  }

  @Test
  public void testCompareTo3() {
    var gelijk  = new Round("-");
    var groter  = new Round("1");
    var kleiner = new Round("?");
    var round   = new Round("-");

    assertTrue(round.compareTo(kleiner) > 0);
    assertEquals(0, round.compareTo(gelijk));
    assertTrue(round.compareTo(groter) < 0);
  }

  @Test
  public void testEquals() {
    var round1  = new Round();
    var round2  = new Round();
    var round3  = new Round();

    round1.setRound("1");
    round2.setRound("1");
    round3.setRound("2");

    assertEquals(round1, round1);
    assertEquals(round1, round2);
    assertNotEquals(round1, null);
    assertNotEquals(round1, round3);
  }

  @Test
  public void testGetRonde() {
    var round1  = new Round("1.2");
    var round2  = new Round("2");
    var round3  = new Round("3.2");

    assertEquals(1, round1.getRonde().intValue());
    assertEquals(2, round2.getRonde().intValue());
    assertEquals(3, round3.getRonde().intValue());
  }

  @Test
  public void testGetRound() {
    var round = new Round("1");

    assertEquals("1", round.getRound());
  }

  @Test
  public void testHashCode() {
    var round = new Round("1");

    assertEquals(49, round.hashCode());
  }

  @Test
  public void testSetRound() {
    var round = new Round();

    round.setRound("1");
    assertEquals("1", round.getRound());

    round.setRound("?");
    assertEquals("?", round.getRound());
  }

  public void testSort(String round1, String round2) {
    var rounds  = new Round[]{new Round(round2), new Round(round1)};

    Arrays.sort(rounds);

    assertEquals(round1, rounds[0].getRound());
    assertEquals(round2, rounds[1].getRound());
  }

  @Test
  public void testSort1() {
    testSort("1", "2");
  }

  @Test
  public void testSort2() {
    testSort("1.1", "1.2");
  }

  @Test
  public void testSort3() {
    testSort("1.1", "1.1.1");
  }

  @Test
  public void testSort4() {
    testSort("1.0.1", "1.1");
  }

  @Test
  public void testSort5() {
    testSort("?", "-");
  }

  @Test
  public void testSort6() {
    testSort("?", "1");
  }

  @Test
  public void testSort7() {
    testSort("-", "1");
  }

  @Test
  public void testSort8() {
    testSort("1.?", "1.-");
  }

  @Test
  public void testToString() {
    var round = new Round("1");

    assertEquals("Round data (round: [1])", round.toString());
  }
}
