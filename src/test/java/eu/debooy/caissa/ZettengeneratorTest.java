/**
 * Copyright 2011 Marco de Booij
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

import eu.debooy.caissa.exceptions.CaissaException;

import org.junit.Test;

import junit.framework.TestCase;


/**
 * @author Marco de Booij
 */
public class ZettengeneratorTest extends TestCase {
  
  @Test
  public void testBeginstand() throws CaissaException {
    Zettengenerator zetten  = new Zettengenerator(new FEN());
    assertTrue((zetten.getAantalZetten() == 20));
  }
  
  @Test
  public void testPromotie() throws CaissaException {
    Zettengenerator zetten  = new Zettengenerator(new FEN("4k3/P7/8/8/8/8/8/4K3 w - - 0 1"));
    assertTrue((zetten.getAantalZetten() == 9));
  }
  
  @Test
  public void testRochade() throws CaissaException {
    Zettengenerator zetten  = new Zettengenerator(new FEN("r3k2r/8/8/1p6/2P5/8/8/R3K2R b KQkq - 0 1"));
    assertTrue((zetten.getAantalZetten() == 28));
  }
}
