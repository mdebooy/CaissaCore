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
package eu.debooy.caissa;

import eu.debooy.caissa.exceptions.ToernooiException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.commons.lang3.builder.CompareToBuilder;


/**
 * @author Marco de Booij
 */
public class Toernooi implements Comparable<Toernooi>, Serializable {
  private static final  long    serialVersionUID  = 1L;

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  public static final String  ERR_AANTALSPELERS =
      "toernooi.aantalspelers.incorrect";
  public static final String  ERR_DATUM         = "toernooi.datum.incorrect";
  public static final String  ERR_EINDDATUM     =
      "toernooi.einddatum.incorrect";
  public static final String  ERR_RONDES        = "toernooi.rondes.incorrect";
  public static final String  ERR_STARTDATUM    =
      "toernooi.startdatum.incorrect";
  public static final String  ERR_TYPE          = "toernooi.type.incorrect";

  public static final Integer TOERNOOI_MATCH    = 0;
  public static final Integer TOERNOOI_ENKEL    = 1;
  public static final Integer TOERNOOI_DUBBEL   = 2;
  public static final Integer TOERNOOI_ZWITSERS = 3;

  private static final  List<Integer> types =
      Arrays.asList(TOERNOOI_MATCH, TOERNOOI_ENKEL, TOERNOOI_DUBBEL,
                    TOERNOOI_ZWITSERS);

  private final Optional<Integer> aantalSpelers;
  private final Optional<String>  einddatum;
  private final Optional<String>  event;
  private final Optional<String>  site;
  private final Optional<String>  startdatum;
  private final Optional<Integer> rondes;
  private final Optional<Integer> type;

  private Toernooi(Builder builder) {
    aantalSpelers = builder.getAantalspelers();
    einddatum     = builder.getEinddatum();
    event         = builder.getEvent();
    site          = builder.getSite();
    startdatum    = builder.getStartdatum();
    rondes        = builder.getRondes();
    type          = builder.getType();
  }

  public static final class Builder {
    private Optional<Integer> aantalSpelers = Optional.empty();
    private Optional<String>  einddatum     = Optional.empty();
    private Optional<String>  event         = Optional.empty();
    private Optional<String>  site          = Optional.empty();
    private Optional<String>  startdatum    = Optional.empty();
    private Optional<Integer> rondes        = Optional.empty();
    private Optional<Integer> type          = Optional.empty();

    public Toernooi build() throws ToernooiException {
      List<String>  fouten  = new ArrayList<>();
      if (type.isPresent()
          && !types.contains(type.get())) {
        fouten.add(resourceBundle.getString(ERR_TYPE));
      }
      if (aantalSpelers.isPresent()
          && aantalSpelers.get().compareTo(1) < 1) {
        fouten.add(resourceBundle.getString(ERR_AANTALSPELERS));
      }
      if (rondes.isPresent()
          && rondes.get().compareTo(1) < 1) {
        fouten.add(resourceBundle.getString(ERR_RONDES));
      }
      var correct = true;
      if (einddatum.isPresent()
          && !CaissaUtils.isDatum(einddatum.get())) {
        fouten.add(resourceBundle.getString(ERR_EINDDATUM));
        correct = false;
      }
      if (startdatum.isPresent()
          && !CaissaUtils.isDatum(startdatum.get())) {
        fouten.add(resourceBundle.getString(ERR_STARTDATUM));
        correct = false;
      }
      if (correct && einddatum.isPresent() && startdatum.isPresent()
          && einddatum.get()
                      .replace('?', '0')
                      .compareTo(startdatum.get().replace('?', '0')) < 0) {
        fouten.add(resourceBundle.getString(ERR_DATUM));
      }

      if (!fouten.isEmpty()) {
        throw new ToernooiException(String.join(System.lineSeparator(),
                                                fouten));
      }

      return new Toernooi(this);
    }

    public Optional<Integer> getAantalspelers() {
      if (!aantalSpelers.isPresent()
          && rondes.isPresent()
          && type.isPresent()) {
        return Optional.of((rondes.get() / (isDubbel() ? 2 : 1)) + 1);
      }

      return aantalSpelers;
    }

    public Optional<String> getEinddatum() {
      return einddatum;
    }

    public Optional<String> getEvent() {
      return event;
    }

    public Optional<String> getSite() {
      return site;
    }

    public Optional<String> getStartdatum() {
      return startdatum;
    }

    public Optional<Integer> getRondes() {
      if (!rondes.isPresent()
          && aantalSpelers.isPresent()
          && type.isPresent()) {
        return Optional.of((aantalSpelers.get() -1) * (isDubbel() ? 2 : 1));
      }

      return rondes;
    }

    public Optional<Integer> getType() {
      return type;
    }

    private boolean isDubbel() {
      if (!type.isPresent()) {
        return false;
      }

      return type.get().equals(TOERNOOI_DUBBEL);
    }

    public Builder setAantalSpelers(int aantalSpelers) {
      this.aantalSpelers = Optional.of(aantalSpelers);
      return this;
    }

    public Builder setEinddatum(int aantalSpelers) {
      this.aantalSpelers    = Optional.of(aantalSpelers);
      return this;
    }

    public Builder setEinddatum(String einddatum) {
      this.einddatum        = Optional.of(einddatum);
      return this;
    }

    public Builder setEvent(String event) {
      this.event            = Optional.of(event);
      return this;
    }

    public Builder setSite(String site) {
      this.site             = Optional.of(site);
      return this;
    }

    public Builder setStartdatum(String startdatum) {
      this.startdatum       = Optional.of(startdatum);
      return this;
    }

    public Builder setRondes(int rondes) {
      this.rondes         = Optional.of(rondes);
      return this;
    }

    public Builder setType(Integer type) {
      this.type           = Optional.of(type);
      return this;
    }
  }

  @Override
  public int compareTo(Toernooi other) {
    return new CompareToBuilder().append(getStartdatum().get(),
                                         other.getStartdatum().get())
                                 .append(getEinddatum().get(),
                                         other.getEinddatum().get())
                                 .append(getEvent().get(),
                                         other.getEvent().get())
                                 .append(getSite().get(),
                                         other.getSite().get())
                                 .toComparison();
  }

  public Optional<Integer> getAantalSpelers() {
    return aantalSpelers;
  }

  public Optional<String> getEinddatum() {
    return einddatum;
  }

  public Optional<String> getEvent() {
    return event;
  }

  public Optional<String> getSite() {
    return site;
  }

  public Optional<String> getStartdatum() {
    return startdatum;
  }

  public Optional<Integer> getRondes() {
    return rondes;
  }

  public Optional<Integer> getType() {
    return type;
  }

  public boolean isDubbel() {
    if (!type.isPresent()) {
      return false;
    }

    return type.get().equals(TOERNOOI_DUBBEL);
  }

  public boolean isEnkel() {
    if (!type.isPresent()) {
      return false;
    }

    return type.get().equals(TOERNOOI_ENKEL)
        || type.get().equals(TOERNOOI_ZWITSERS);
  }
}
