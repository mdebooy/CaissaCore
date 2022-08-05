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
package eu.debooy.caissa.exceptions;


/**
 * @author Marco de Booij
 */
public class ToernooiException extends CaissaException {
  private static final long serialVersionUID = 1L;

  public ToernooiException(String message) {
    super(message);
  }

  public ToernooiException(Throwable cause) {
    super(cause);
  }

  public ToernooiException(String message, Throwable cause) {
    super(message, cause);
  }
}
