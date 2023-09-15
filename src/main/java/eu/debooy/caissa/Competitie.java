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

import eu.debooy.caissa.exceptions.CompetitieException;
import eu.debooy.doosutils.Datum;
import eu.debooy.doosutils.access.JsonBestand;
import eu.debooy.doosutils.exception.BestandException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * @author Marco de Booij
 */
public class Competitie implements Comparable<Competitie>, Serializable {
  private static final  long    serialVersionUID  = 1L;

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle(CaissaConstants.RESOURCEBUNDLE,
                               Locale.getDefault());

  public static final String  ERR_DATUM         = "cmp.error.datum";
  public static final String  ERR_KALENDER      = "cmp.error.kalender";
  public static final String  ERR_MATCH         = "cmp.error.match";
  public static final String  ERR_MATCHSPELERS  = "cmp.error.match.spelers";
  public static final String  ERR_RONDES        = "cmp.error.rondes";
  public static final String  ERR_PUNTEN        = "cmp.error.punten";
  public static final String  ERR_SPELERS       = "cmp.error.spelers";
  public static final String  ERR_TIEBREAK      = "cmp.error.tiebreak";
  public static final String  ERR_TYPE          = "cmp.error.type";

  public static final String  JSON_TAG_EVENTDATE          = "eventdate";
  public static final String  JSON_TAG_EVENT              = "event";
  public static final String  JSON_TAG_INHALEN            = "inhalen";
  public static final String  JSON_TAG_KALENDER           = "kalender";
  public static final String  JSON_TAG_KALENDER_DATUM     = "datum";
  public static final String  JSON_TAG_KALENDER_EXTRA     = "extra";
  public static final String  JSON_TAG_KALENDER_INHAAL    = "inhaal";
  public static final String  JSON_TAG_KALENDER_RONDE     = "ronde";
  public static final String  JSON_TAG_RONDES             = "rondes";
  public static final String  JSON_TAG_SITE               = "site";
  public static final String  JSON_TAG_SPELERS            = "spelers";
  public static final String  JSON_TAG_SPELER_ALIAS       = "alias";
  public static final String  JSON_TAG_SPELER_ELO         = "elo";
  public static final String  JSON_TAG_SPELER_EMAIL       = "email";
  public static final String  JSON_TAG_SPELER_HEENRONDE   = "heenronde";
  public static final String  JSON_TAG_SPELER_LANDKODE    = "landkode";
  public static final String  JSON_TAG_SPELER_NAAM        = "naam";
  public static final String  JSON_TAG_PUNTEN             = "punten";
  public static final String  JSON_TAG_SPELER_SPELERID    = "id";
  public static final String  JSON_TAG_SPELER_SPELERSEQ   = "seq";
  public static final String  JSON_TAG_SPELER_TELEFOON    = "telefoon";
  public static final String  JSON_TAG_SPELER_TERUGRONDE  = "terugronde";
  public static final String  JSON_TAG_TIEBREAK           = "tiebreak";
  public static final String  JSON_TAG_TOERNOOITYPE       = "toernooitype";

  public static final String  MIS_EVENT     = "cmp.mis.event";
  public static final String  MIS_EVENTDATE = "cmp.mis.eventdate";
  public static final String  MIS_RONDES    = "cmp.mis.rondes";
  public static final String  MIS_SPELERS   = "cmp.mis.spelers";
  public static final String  MIS_TAG       = "cmp.mis.jsontag";
  public static final String  MIS_TYPE      = "cmp.mis.type";

  public static final Integer TOERNOOI_MATCH            = 0;
  public static final Integer TOERNOOI_ENKEL            = 1;
  public static final Integer TOERNOOI_DUBBEL           = 2;
  public static final Integer TOERNOOI_ZWITSERS         = 3;
  public static final Integer TOERNOOI_DUBBEL_ZWITSERS  = 4;

  private static final  List<Integer> roundrobin  =
      Arrays.asList(TOERNOOI_ENKEL, TOERNOOI_DUBBEL);
  private static final  List<String>  tiebreaks   =
      Arrays.asList(CaissaConstants.TIEBREAK_SB, CaissaConstants.TIEBREAK_WP);
  private static final  List<Integer> types       =
      Arrays.asList(TOERNOOI_MATCH, TOERNOOI_ENKEL, TOERNOOI_DUBBEL,
                    TOERNOOI_ZWITSERS, TOERNOOI_DUBBEL_ZWITSERS);

  private Spelerinfo        bye           = new Spelerinfo();
  private Double            puntenBye     = 0.0;
  private Double            puntenRemise  = 0.5;
  private Double            puntenVerlies = 0.0;
  private Double            puntenWinst   = 1.0;
  private Integer           rondes;
  private List<Date>        speeldata;
  private List<Spelerinfo>  spelers;
  private JSONObject        toernooi;
  private Integer           type;

  public Competitie(String jsonbestand) throws CompetitieException {
    try (var invoer = new JsonBestand.Builder()
                                     .setBestand(jsonbestand)
                                     .build()) {
      toernooi  = invoer.read();
      speeldata = new ArrayList<>();
      spelers   = new ArrayList<>();

      vulSpelers();
      vulSpeeldata();

      validate();
    } catch (BestandException | ParseException e) {
      throw new CompetitieException(e.getLocalizedMessage());
    }
    if (metBye()) {
      bye.setNaam(CaissaConstants.BYE);
      bye.setSpelerId(spelers.get(spelers.size() - 1).getSpelerId() + 1);
      bye.setSpelerSeq(spelers.get(spelers.size() - 1)
                                     .getSpelerSeq() + 1);
    }
  }

  private Integer berekenRondes() {
    Integer teSpelen;

    if (isRoundrobin()) {
      teSpelen  = getAantalHeenrondes() + getAantalTerugrondes();
    } else {
      teSpelen  = rondes;
    }

    return teSpelen;
  }

  @Override
  public int compareTo(Competitie other) {
    return getEvent().compareTo(other.getEvent());
  }

  public boolean containsKey(String sleutel) {
    return toernooi.containsKey(sleutel);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Competitie)) {
      return false;
    }

    if (this == other) {
      return true;
    }

    var competitie = (Competitie) other;
    return getEvent().equals(competitie.getEvent());
  }

  public Object get(String sleutel) {
    if (toernooi.containsKey(sleutel)) {
      return toernooi.get(sleutel);
    }

    return new Object();
  }

  public Integer getAantalHeenrondes() {
    var aantal  = spelers.stream().filter(Spelerinfo::inHeenronde).count();

    return (int) (aantal + (aantal%2) - 1);
  }

  public Integer getAantalTerugrondes() {
    if (!isDubbel()) {
      return 0;
    }

    var aantal  = spelers.stream().filter(Spelerinfo::inTerugronde).count();

    return (int) (aantal + (aantal%2) - 1);
  }

  public String getEvent() {
    return get(JSON_TAG_EVENT).toString();
  }

  public String getEventdate() {
    return get(JSON_TAG_EVENTDATE).toString();
  }

  public int getHeenTerug() {
    return isDubbel() ? 2 : 1;
  }

  public JSONArray getInhaalpartijen() {
    if (toernooi.containsKey(JSON_TAG_INHALEN)) {
      return (JSONArray) toernooi.get(JSON_TAG_INHALEN);
    }

    return new JSONArray();
  }

  public Integer getInteger(String sleutel) {
    if (toernooi.containsKey(sleutel)) {
      return ((Long) toernooi.get(sleutel)).intValue();
    }

    return 0;
  }

  public JSONArray getKalender() {
    if (toernooi.containsKey(JSON_TAG_KALENDER)) {
      return (JSONArray) toernooi.get(JSON_TAG_KALENDER);
    }

    return new JSONArray();
  }

  public String getMissingTag(String tag) {
    return MessageFormat.format(resourceBundle.getString(MIS_TAG), tag);
  }

  public Double getPuntenBye() {
    return puntenBye;
  }

  public Double getPuntenRemise() {
    return puntenRemise;
  }

  public Double getPuntenVerlies() {
    return puntenVerlies;
  }

  public Double getPuntenWinst() {
    return puntenWinst;
  }

  public Integer getRondes() {
    return rondes;
  }

  public String getSite() {
    return get(JSON_TAG_SITE).toString();
  }

  public List<Date> getSpeeldata() {
    return new ArrayList<>(speeldata);
  }

  public Spelerinfo getSpeler(Integer seq) {
    if (seq == spelers.size()) {
      return bye;
    }

    return spelers.get(seq);
  }

  public Spelerinfo getSpeler(String naam) {
    if (CaissaConstants.BYE.equalsIgnoreCase(naam)) {
      return bye;
    }

    return spelers.stream()
                  .filter(speler -> speler.getNaam().equals(naam))
                  .findFirst()
                  .orElse(new Spelerinfo());
  }

  public int getSpelerIndex(String naam) {
    if (CaissaConstants.BYE.equalsIgnoreCase(naam)) {
      return -1;
    }

    var index = -1;

    for (var i = 0; i < spelers.size(); i++) {
      if (spelers.get(i).getNaam().equals(naam)) {
        index = i;
      }
    }

    return index;
  }

  public List<Spelerinfo> getSpelers() {
    return new ArrayList<>(spelers);
  }

  public List<Spelerinfo> getSpelersHeenronde() {
    return spelers.stream().filter(Spelerinfo::inHeenronde)
                  .collect(Collectors.toList());
  }

  public List<Spelerinfo> getSpelersTerugronde() {
    if (!isDubbel()) {
      return new ArrayList();
    }

    return spelers.stream().filter(Spelerinfo::inTerugronde)
                  .collect(Collectors.toList());
  }

  public String getString(String sleutel) {
    if (toernooi.containsKey(sleutel)) {
      return toernooi.get(sleutel).toString();
    }

    return "";
  }

  public String getTiebreak() {
    if (!toernooi.containsKey(JSON_TAG_TIEBREAK)) {
      return CaissaConstants.TIEBREAK_SB;
    }

    return (String) toernooi.get(JSON_TAG_TIEBREAK);
  }

  public Integer getType() {
    return type;
  }

  public String getUitslag(String uitslag) {
    return getUitslag(uitslag, false);
  }

  public String getUitslag(String uitslag, boolean bye) {
    if (uitslag.equals(CaissaConstants.PARTIJ_BEZIG)) {
      return uitslag;
    }

    if (puntenWinst.equals(1.0)
        && puntenRemise.equals(0.5)
        && puntenVerlies.equals(0.0)
        && !metBye()) {
      return uitslag;
    }

    var wit   = 0;
    var zwart = 0;
    if (uitslag.equals(CaissaConstants.PARTIJ_WIT_WINT)) {
      if (bye) {
        wit   = puntenBye.intValue();
        zwart = 0;
      } else {
        wit   = puntenWinst.intValue();
        zwart = puntenVerlies.intValue();
    }
    }
    if (uitslag.equals(CaissaConstants.PARTIJ_REMISE)) {
      wit   = puntenRemise.intValue();
      zwart = puntenRemise.intValue();
    }
    if (uitslag.equals(CaissaConstants.PARTIJ_ZWART_WINT)) {
      if (bye) {
        wit   = 0;
        zwart = puntenBye.intValue();
      } else {
        wit   = puntenVerlies.intValue();
        zwart = puntenWinst.intValue();
      }
    }

    return String.format("%d-%d", wit, zwart);
  }

  @Override
  public int hashCode() {
    return getEvent().hashCode();
  }

  public boolean isDubbel() {
    return type.equals(TOERNOOI_DUBBEL)
        || type.equals(TOERNOOI_DUBBEL_ZWITSERS);
  }

  public boolean isEnkel() {
    return type.equals(TOERNOOI_ENKEL)
        || type.equals(TOERNOOI_ZWITSERS);
  }

  public boolean isMatch() {
    return type.equals(TOERNOOI_MATCH);
  }

  public boolean isRoundrobin() {
    return roundrobin.contains(type);
  }

  public boolean isZwitsers() {
    return type.equals(TOERNOOI_ZWITSERS)
        || type.equals(TOERNOOI_DUBBEL_ZWITSERS);
  }

  public final boolean metBye() {
    return !puntenBye.equals(0.0);
  }

  public void sorteerOpNaam() {
    spelers.sort(new Spelerinfo.ByNaamComparator());
  }

  public void sorteerOpSeq() {
    spelers.sort(new Spelerinfo.BySpelerSeqComparator());
  }

  public void sorteerOpStand() {
    Collections.sort(spelers);
  }

  private void validate() throws CompetitieException {
    List<String>  fouten  = new ArrayList<>();

    validateEvent(fouten);
    validateEventdate(fouten);
    validatePunten(fouten);
    validateTiebreak(fouten);

    var correct = validateType(fouten);

    if (correct) {
      validateRondes(fouten);
      validateSpelers(fouten);
    }

    if (!fouten.isEmpty()) {
      Collections.sort(fouten);
      throw
          new CompetitieException(String.join(System.lineSeparator(),
                                  fouten));
    }
  }

  private void validateEvent(List<String> fouten) {
    if (!toernooi.containsKey(JSON_TAG_EVENT)) {
      fouten.add(resourceBundle.getString(MIS_EVENT));
    }
  }

  private void validateEventdate(List<String> fouten) {
    if (!toernooi.containsKey(JSON_TAG_EVENTDATE)) {
      fouten.add(resourceBundle.getString(MIS_EVENTDATE));
      return;
    }

    var eventdate = toernooi.get(JSON_TAG_EVENTDATE).toString();

    if (!CaissaUtils.isDatum(eventdate)) {
        fouten.add(resourceBundle.getString(ERR_DATUM));
    }
  }

  private void validatePunten(List<String> fouten) {
     if (!toernooi.containsKey(JSON_TAG_PUNTEN)) {
      return;
    }

    var punten  = ((String) toernooi.get(JSON_TAG_PUNTEN)).split(";");
    if (punten.length < 3
        || punten.length > 4) {
      fouten.add(resourceBundle.getString(ERR_PUNTEN));
      return;
    }

    try {
      puntenWinst   = Double.valueOf(punten[0]);
      puntenRemise  = Double.valueOf(punten[1]);
      puntenVerlies = Double.valueOf(punten[2]);
      if (punten.length == 4) {
        puntenBye   = Double.valueOf(punten[3]);
      }
    } catch (NumberFormatException e) {
      fouten.add(resourceBundle.getString(ERR_PUNTEN));
    }
  }

  private void validateRondes(List<String> fouten) {
    if (toernooi.containsKey(JSON_TAG_RONDES)) {
      rondes  = getInteger(JSON_TAG_RONDES);
    } else {
      rondes  = null;
    }

    if (isMatch()) {
        validateRondesMatch(fouten);
    }
    if (isRoundrobin()) {
        validateRondesRoundrobin(fouten);
    }
    if (isZwitsers()) {
        validateRondesZwitsers(fouten);
    }
  }

  private void validateRondesMatch(List<String> fouten) {
    if (null == rondes) {
      rondes  = speeldata.size();
    }

    if (rondes.compareTo(1) < 0) {
      fouten.add(resourceBundle.getString(ERR_RONDES));
      return;
    }

    if (!speeldata.isEmpty()
        && !rondes.equals(getSpeeldata().size())) {
      fouten.add(MessageFormat.format(
          resourceBundle.getString(ERR_KALENDER),
          getSpeeldata().size(), rondes));
    }
  }

  private void validateRondesRoundrobin(List<String> fouten) {
    if (null == rondes) {
      rondes  = berekenRondes();
    }

    if (rondes.compareTo(1) < 0) {
      fouten.add(resourceBundle.getString(ERR_RONDES));
      return;
    }

    if (getSpeeldata().isEmpty()) {
      return;
    }

    if (!rondes.equals(getSpeeldata().size())) {
      fouten.add(MessageFormat.format(
          resourceBundle.getString(ERR_KALENDER),
          getSpeeldata().size(), rondes));
    }
  }

  private void validateRondesZwitsers(List<String> fouten) {
    if (null == rondes) {
      rondes  = speeldata.size();
    }

    if (rondes.compareTo(1) < 0) {
      fouten.add(resourceBundle.getString(ERR_RONDES));
    }
  }

  private void validateSpelers(List<String> fouten) {
    if (spelers.isEmpty()) {
      fouten.add(resourceBundle.getString(MIS_SPELERS));
    }

    if (isMatch()
        && spelers.size() != 2) {
      fouten.add(
          MessageFormat.format(resourceBundle.getString(ERR_MATCHSPELERS),
                               spelers.size()));
    }

    var teSpelen  = berekenRondes();
    if (!rondes.equals(teSpelen)) {
      fouten.add(MessageFormat.format(resourceBundle.getString(ERR_SPELERS),
                                      teSpelen, rondes));
    }
  }

  private void validateTiebreak(List<String> fouten) {
     if (!toernooi.containsKey(JSON_TAG_TIEBREAK)) {
      return;
    }

    var tiebreak  = (String) toernooi.get(JSON_TAG_TIEBREAK);

    if (!tiebreaks.contains(tiebreak)) {
      fouten.add(
          MessageFormat.format(resourceBundle.getString(ERR_TIEBREAK),
                               tiebreak));
    }
  }

  private boolean validateType(List<String> fouten) {
    if (!toernooi.containsKey(JSON_TAG_TOERNOOITYPE)) {
      fouten.add(resourceBundle.getString(MIS_TYPE));
      return false;
    }

    type  = ((Long) toernooi.get(JSON_TAG_TOERNOOITYPE)).intValue();
    if (!types.contains(type)) {
      fouten.add(resourceBundle.getString(ERR_TYPE));
      return false;
    }

    return true;
  }

  private void vulSpeeldata() throws ParseException {
     if (!toernooi.containsKey(JSON_TAG_KALENDER)) {
      return;
    }

    for (var item: (JSONArray) toernooi.get(JSON_TAG_KALENDER)) {
      var ronde  = (JSONObject) item;
      if (ronde.containsKey(JSON_TAG_KALENDER_RONDE)
          && ronde.containsKey(JSON_TAG_KALENDER_DATUM)) {
        speeldata.add(Datum.toDate(ronde.get(Competitie.JSON_TAG_KALENDER_DATUM)
                                        .toString(),
                                   CaissaConstants.DEF_DATUMFORMAAT));
      }
    }
  }

  private void vulSpelers() {
    if (!toernooi.containsKey(JSON_TAG_SPELERS)) {
      return;
    }

    var spelerId  = 1;
    for (var jSpeler : (JSONArray)  toernooi.get(JSON_TAG_SPELERS)) {
      var speler  = new Spelerinfo((JSONObject) jSpeler);
      if (null == speler.getSpelerId()) {
        speler.setSpelerId(spelerId);
      }
      if (null == speler.getSpelerSeq()) {
        speler.setSpelerSeq(spelerId);
      }
      spelers.add(speler);
      spelerId++;
    }
  }
}
