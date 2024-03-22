/*
 * Copyright (c) 2024 Marco de Booij
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

package eu.debooy.caissa.exceptions;

import static junit.framework.TestCase.assertEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class PgnExceptionTest {
  private static final  String    MSG   = "PgnException.init";
  private static final  String    NAAM  = PgnException.class.getName();
  private static final  Throwable TE    = new Throwable("ThrowableException");

  @Test
  public void testInit1() {
    var pe  = new PgnException(MSG);

    assertEquals(NAAM, pe.getClass().getName());
    assertEquals(MSG, pe.getLocalizedMessage());
  }

  @Test
  public void testInit2() {
    var pe  = new PgnException(TE);

    assertEquals(NAAM, pe.getClass().getName());
    assertEquals("java.lang.Throwable: ThrowableException",
                 pe.getLocalizedMessage());
  }

  @Test
  public void testInit3() {
    var pe  = new PgnException(MSG, TE);

    assertEquals(NAAM, pe.getClass().getName());
    assertEquals(MSG, pe.getLocalizedMessage());
  }
}
