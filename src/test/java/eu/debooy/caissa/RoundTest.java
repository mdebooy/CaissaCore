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
  public void testGet() {
    var round = new Round("1");

    assertEquals("1", round.getRound());
  }

  @Test
  public void testHashCode() {
    var round = new Round("1");

    assertEquals(49, round.hashCode());
  }

  @Test
  public void testSort1() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1");
    round2.setRound("2");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort2() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1.1");
    round2.setRound("1.2");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort3() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1.1");
    round2.setRound("1.1.1");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort4() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1.0.1");
    round2.setRound("1.1");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort5() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("-");
    round2.setRound("?");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort6() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1");
    round2.setRound("?");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort7() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1");
    round2.setRound("-");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort8() {
    var round1  = new Round();
    var round2  = new Round();

    round1.setRound("1.-");
    round2.setRound("1.?");
    var rounds  = new Round[]{round2, round1};

    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSet() {
    var round = new Round();

    round.setRound("1");
    assertEquals("1", round.getRound());

    round.setRound("?");
    assertEquals("?", round.getRound());
  }

  @Test
  public void testToString() {
    var round = new Round("1");

    assertEquals("Round data (round: [1])", round.toString());
  }
}
