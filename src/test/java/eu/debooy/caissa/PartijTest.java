/*
 * Copyright (c) 2021 Marco de Booij
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

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class PartijTest extends TestCase {
  @Test
  public void testCompareTo() {
    var gelijk  = new Partij();
    var groter  = new Partij();
    var kleiner = new Partij();
    var partij  = new Partij();

    partij.setRonde(new Round("2"));
    gelijk.setRonde(new Round("2"));
    groter.setRonde(new Round("3"));
    kleiner.setRonde(new Round("1"));

    assertTrue(partij.compareTo(kleiner) > 0);
    assertEquals(0, partij.compareTo(gelijk));
    assertTrue(partij.compareTo(groter) < 0);
  }

  @Test
  public void testEquals() {
    var partij1 = new Partij();
    var partij2 = new Partij();
    var partij3 = new Partij();

    partij1.setRonde(new Round("1"));
    partij2.setRonde(new Round("1"));
    partij3.setRonde(new Round("2"));

    assertEquals(partij1, partij1);
    assertEquals(partij1, partij2);
    assertNotEquals(partij1, null);
    assertNotEquals(partij1, partij3);
  }

  @Test
  public void testHashCode() {
    var partij  = new Partij();

    partij.setRonde(new Round("1"));
    assertEquals(49, partij.hashCode());
  }
}
