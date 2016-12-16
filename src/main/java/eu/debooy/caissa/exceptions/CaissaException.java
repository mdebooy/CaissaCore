/**
 * Copyright 2008 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/7330l5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.caissa.exceptions;


/**
 * @author Marco de Booij
 */
public class CaissaException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * @param message Explanation of the exception
   */
  public CaissaException(String message) {
    super(message);
  }

  /**
   * @param cause   Cause of the exception
   */
  public CaissaException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message Explanation of the exception
   * @param cause   Cause of the exception
   */
  public CaissaException(String message, Throwable cause) {
    super(message, cause);
  }
}
