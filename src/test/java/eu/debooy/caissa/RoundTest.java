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

import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class RoundTest extends TestCase {
  @Test
  public void testEquality() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1");
    round2.setRound("1");
    assertTrue(round1.equals(round2));
  }

  @Test
  public void testSort1() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1");
    round2.setRound("2");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);

    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort2() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1.1");
    round2.setRound("1.2");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort3() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1.1");
    round2.setRound("1.1.1");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort4() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1.0.1");
    round2.setRound("1.1");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort5() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("-");
    round2.setRound("?");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort6() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1");
    round2.setRound("?");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort7() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1");
    round2.setRound("-");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }

  @Test
  public void testSort8() {
    Round round1  = new Round();
    Round round2  = new Round();

    round1.setRound("1.-");
    round2.setRound("1.?");
    Round[] rounds  = {round2, round1};
   
    Arrays.sort(rounds);
   
    assertTrue(rounds[0].compareTo(rounds[1]) <= 0);
  }
}
