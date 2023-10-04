/**
 * Copyright (c) 2008 Marco de Booij
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
 * limitations under the Licence.
 */
package eu.debooy.caissa;

import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.doosutils.Datum;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.access.BestandConstants;
import eu.debooy.doosutils.access.TekstBestand;
import eu.debooy.doosutils.exception.BestandException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;


/**
 * @author Marco de Booij
 */
public final class CaissaUtils {
  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private static final  char[]  stuk  = {'k', 'q', 'r', 'b', 'n', 'p', '.',
                                         'P', 'N', 'B', 'R', 'Q', 'K'};

  private CaissaUtils() {}

  private static void berekenSBscore(double[][] matrix,
                                     Competitie competitie) {
    var deelnemers  = competitie.getDeelnemers();
    var dubbel      = competitie.getHeenTerug();
    var kolommen    = matrix[0].length;
    var noSpelers   = competitie.getDeelnemers().size();

    for (var i = 0; i < noSpelers; i++) {
      Double  sbScore = 0.0;
      for (var j = 0; j < kolommen; j++) {
        if (!competitie.isMatch()
            && matrix[i][j] == competitie.getPuntenWinst()) {
          sbScore += deelnemers.get(j / dubbel).getPunten();
        }
        if (!competitie.isMatch()
            && matrix[i][j] == competitie.getPuntenRemise()) {
          sbScore += deelnemers.get(j / dubbel).getPunten() * 0.5;
        }
      }
      deelnemers.get(i).setTieBreakScore(sbScore);
    }
  }

  public static void berekenTieBreakScore(double[][] matrix,
                                          Competitie competitie) {
    switch (competitie.getTiebreak()) {
      case CaissaConstants.TIEBREAK_SB:
                berekenSBscore(matrix, competitie);
                break;
      case CaissaConstants.TIEBREAK_WP:
                berekenWeerstandspunten(matrix, competitie);
                break;
      default:
        break;
    }
  }

  private static void berekenWeerstandspunten(double[][] matrix,
                                             Competitie competitie) {
    var dubbel    = competitie.getHeenTerug();
    var kolommen  = matrix[0].length;
    var noSpelers = competitie.getSpelers().size();

    if (competitie.metBye()) {
      kolommen--;
    }

    for (var i = 0; i < noSpelers; i++) {
      Double weerstandspunten = 0.0;
      for (var j = 0; j < kolommen; j++) {
        if (!competitie.isMatch()) {
          weerstandspunten += competitie.getSpeler(j / dubbel).getPunten();
        }
      }
      competitie.getSpeler(i).setTieBreakScore(weerstandspunten);
    }
  }

  public static String[] bergertabel(int spelers) {
    var aantal    = spelers + (spelers%2);
    var rondes    = aantal - 1;
    var resultaat = new String[rondes];

    var speler  = 1;
    var paring  = new int[aantal];
    for (var i = 0; i < rondes; i++) {
      for (var j = 0; j < rondes; j++) {
        paring[j] = speler;
        speler++;
        if (speler > rondes) {
          speler = 1;
        }
        paring[rondes]  = aantal;
      }

      if (i%2 == 1) {
        paring[0]       = paring[0] + paring[rondes];
        paring[rondes]  = paring[0] - paring[rondes];
        paring[0]       = paring[0] - paring[rondes];
      }

      var ronde = new StringBuilder();
      for (var j = 0; j < aantal/2; j++) {
        ronde.append(" ").append(paring[j])
             .append("-").append(paring[rondes-j]);
      }
      speler        = paring[(aantal/2)];
      resultaat[i]  = ronde.toString().trim();
    }

    return resultaat;
  }

  public static void draaiBord(int[] bord) {
    for (var i = 2; i < 6; i++) {
      for (var j = 1; j < 9; j++) {
        var hulp    = i * 10;
        var van     = hulp + j;
        var naar    = 110 - hulp + j;
        hulp        = bord[van];
        bord[van]   = -bord[naar];
        bord[naar]  = -hulp;
      }
    }
  }

  public static int externToIntern(String veld) {
    return (veld.charAt(0) - 96) + (veld.charAt(1) - 47) * 10;
  }

  private static void genereerRonde(List<Spelerinfo> spelers,
                                    Set<Partij> schema,
                                    List<String> heenronde,
                                    int rondenummer) {
    var noSpelers = spelers.size();
    var heen      = heenronde.isEmpty();

    if (noSpelers%2 == 1) {
      var naam    = CaissaConstants.BYE;
      var speler  = new Spelerinfo();
      speler.setSpelerId(999999);
      speler.setNaam(naam);
      spelers.add(speler);
      noSpelers++;
    }

    var rondetabel  = CaissaUtils.bergertabel(noSpelers);
    for (var ronde : rondetabel) {
      var partijen      = ronde.split(" ");
      var noPartijen    = partijen.length - 1;
      var partijnummer  = 1;
      for (var i = noPartijen; i >= 0; i--) {
        var speler  = partijen[i].split("-");
        var partij  = new Partij();
        Integer wit;
        Integer zwart;
        if (heen) {
          wit   = Integer.parseInt(speler[0])-1;
          zwart = Integer.parseInt(speler[1])-1;
        } else {
          wit   = Integer.parseInt(speler[1])-1;
          zwart = Integer.parseInt(speler[0])-1;
        }
        var speler1 = new Spelerinfo(spelers.get(wit));
        var speler2 = new Spelerinfo(spelers.get(zwart));
        var info    = speler1.getSpelerId() + "-" + speler2.getSpelerId();
        partij.setRonde(rondenummer, partijnummer);
        if (heenronde.contains(info)) {
          partij.setWitspeler(speler2);
          partij.setZwartspeler(speler1);
        } else {
          partij.setWitspeler(speler1);
          partij.setZwartspeler(speler2);
          heenronde.add(info);
        }
        partij.setUitslag(CaissaConstants.PARTIJ_BEZIG);
        schema.add(partij);
        partijnummer++;
      }
      rondenummer++;
    }
  }

  public static Set<Partij> genereerSpeelschema(Competitie competitie) {
    Set<Partij>       schema    = new TreeSet<>();

    if (competitie.isZwitsers()) {
      return schema;
    }

    List<Spelerinfo>  dezeronde = new ArrayList<>();
    List<String>      heenronde = new ArrayList<>();
    var               spelers   = competitie.getSpelers();

    spelers.sort(new Spelerinfo.BySpelerSeqComparator());
    spelers.stream()
           .filter(Spelerinfo::inHeenronde)
           .forEach(dezeronde::add);
    genereerRonde(dezeronde, schema, heenronde, 1);

    if (competitie.isEnkel()) {
      return schema;
    }

    var rondenummer = dezeronde.size();
    dezeronde.clear();
    spelers.stream()
           .filter(Spelerinfo::inTerugronde)
           .forEach(dezeronde::add);
    genereerRonde(dezeronde, schema, heenronde, rondenummer);

    return schema;
  }

  public static Set<Partij> genereerSpeelschema(Competitie competitie,
                                                Collection<PGN> partijen) {
    if (competitie.isZwitsers()) {
      return genereerZwitsersSchema(competitie.getSpelers(), partijen);
    }

    var schema  = genereerSpeelschema(competitie);
    partijen.forEach(partij -> {
      var game  = schema.stream()
                        .filter(ptij -> ptij.getWitspeler().getNaam()
                                            .equals(partij.getWhite()))
                        .filter(ptij -> ptij.getZwartspeler().getNaam()
                                            .equals(partij.getBlack()))
                        .findFirst().orElse(null);
      if (null != game) {
        game.setForfait(!partij.isRated());
        game.setRanked(partij.isRanked());
        game.setUitslag(partij.getTag(PGN.PGNTAG_RESULT));
      }
    });

    return schema;
  }

  private static Set<Partij> genereerZwitsersSchema(List<Spelerinfo> spelers,
                                                    Collection<PGN> partijen) {
    var         opRonde       = new TreeSet<PGN>(new PGN.ByEventComparator());
    var         partijnummer  = 0;
    Set<Partij> schema        = new TreeSet<>();
    var         vorigeRonde   = 0;

    opRonde.addAll(partijen);
    for (var pgn : opRonde) {
      var ronde   =
            Integer.parseInt(pgn.getTag(PGN.PGNTAG_ROUND).split("\\.")[0]);
      if (ronde != vorigeRonde) {
        vorigeRonde   = ronde;
        partijnummer  = 0;
      }

      partijnummer++;
      var partij  = new Partij();
      var wit     = spelers.stream()
                           .filter(speler -> speler.getNaam()
                                                   .equals(pgn.getWhite()))
                           .findFirst().orElse(getBye());
      var zwart   = spelers.stream()
                           .filter(speler -> speler.getNaam()
                                                   .equals(pgn.getBlack()))
                           .findFirst().orElse(getBye());

      partij.setWitspeler(wit);
      partij.setZwartspeler(zwart);
      partij.setRonde(ronde, partijnummer);
      partij.setUitslag(pgn.getTag(PGN.PGNTAG_RESULT));

      schema.add(partij);
    }

    return schema;
  }

  public static Spelerinfo getBye() {
    var bye = new Spelerinfo();

    bye.setNaam(CaissaConstants.BYE);
    bye.setSpelerId(Integer.MAX_VALUE);
    bye.setSpelerSeq(Integer.MAX_VALUE);

    return bye;
  }

  public static int getSpelerIndex(String naam, List<Spelerinfo> spelers) {
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

  public static char getStuk(int stukcode) {
    return stuk[stukcode + 6];
  }

  /**
   * enkel: 0 = Tweekamp, 1 = Enkelrondig, 2 = Dubbelrondig
   * 1 is default waarde.
   *
   * @param enkelrondig
   * @return
   */
  public static int getToernooitype(String enkelrondig) {
    int enkel;

    switch (enkelrondig) {
      case DoosConstants.WAAR:
        enkel = Competitie.TOERNOOI_ENKEL;
        break;
      case DoosConstants.ONWAAR:
        enkel = Competitie.TOERNOOI_DUBBEL;
        break;
      default:
        enkel = Competitie.TOERNOOI_MATCH;
        break;
    }

    return enkel;
  }

  public static void initMatrix(double[][] matrix, int noSpelers) {
    int kolommen  = matrix[0].length;

    for (var i = 0; i < noSpelers; i++) {
      for (var j = 0; j < kolommen; j++) {
        matrix[i][j]  = -1.0;
      }
    }
  }

  public static String internToExtern(int veld) {
    return "" + (char) (veld%10 + 96) + (char) (veld/10 + 47);
  }

  public static boolean isDatum(String datum) {
    if (datum.length() != 10) {
      return false;
    }

    if (datum.charAt(4) != '.'
        && datum.charAt(7) != '.') {
      return false;
    }

    if (datum.substring(0,4)
             .replaceAll("[\\d\\?]", "").length() != 0) {
      return false;
    }

    if (datum.substring(5,7)
             .replaceAll("[0-1\\?][\\d\\?]", "").length() != 0) {
      return false;
    }

    if (datum.substring(8)
             .replaceAll("[0-3\\?][\\d\\?]", "").length() != 0) {
      return false;
    }

    if (datum.indexOf('?') == -1) {
      try {
        Datum.toDate(datum, PGN.PGN_DATUM_FORMAAT);
      } catch (ParseException e) {
        return false;
      }

      return true;
    }

    return datum.replaceAll("[\\d\\?\\.]", "").length() == 0;
  }

  public static boolean isMat(FEN fen) {
    var zetten  = new Zettengenerator(fen).getAantalZetten();

    return (zetten == 0);
  }

  public static Collection<PGN> laadPgnBestand(String bestand)
      throws PgnException {
    return laadPgnBestand(bestand, Charset.defaultCharset().name());
  }

  public static Collection<PGN> laadPgnBestand(String bestand, String charSet)
      throws PgnException {
    var             lijnnummer  = 0;
    Collection<PGN> partijen    = new ArrayList<>();

    try (var input = new TekstBestand.Builder()
                              .setBestand(bestand
                                  + (bestand.endsWith(BestandConstants.EXT_PGN)
                                        ? "" : BestandConstants.EXT_PGN))
                              .setCharset(charSet).build()) {
      var eof   = false;
      var lijn  = "";
      if (input.hasNext()) {
        lijn  = input.next();
        lijnnummer++;
      } else {
        eof = true;
      }
      while (!eof) {
        var partij  = new PGN();

        // Zoek naar de eerste TAG
        while (!eof && !lijn.startsWith("[")) {
          if (input.hasNext()) {
            lijn  = input.next();
            lijnnummer++;
          } else {
            eof = true;
          }
        }

        // Verwerk de TAGs
        while (!eof && lijn.startsWith("[")) {
          schrijfTag(partij, lijn);
          if (input.hasNext()) {
            lijn  = input.next();
            lijnnummer++;
          } else {
            eof = true;
          }
        }

        // Verwerk de zetten
        var uitslag = partij.getTag(PGN.PGNTAG_RESULT);
        var zetten  = new StringBuilder();
        while (!eof && !lijn.trim().endsWith(uitslag)) {
          if (lijn.startsWith("[")) {
            throw new PgnException(MessageFormat.format(
                resourceBundle.getString(PGN.ERR_BESTAND),
                                   lijnnummer));
          }
          zetten.append(lijn.trim());
          if (!lijn.endsWith(".")) {
            zetten.append(" ");
          }
          if (input.hasNext()) {
            lijn  = input.next();
            lijnnummer++;
          } else {
            eof = true;
          }
        }

        if (!eof && lijn.trim().endsWith(uitslag)) {
          zetten.append(lijn.trim());
        }

        if (null != uitslag) {
          var resultaat = zetten.toString().trim();
          partij.setZetten(
              resultaat.substring(0, resultaat.length() - uitslag.length())
                       .trim());

          if (partij.isValid()) {
            partijen.add(partij);
          } else {
            throw
                new PgnException(resourceBundle.getString(PGN.ERR_PGN_INVALID));
          }
        }

        if (input.hasNext()) {
          lijn  = input.next();
          lijnnummer++;
        } else {
          eof = true;
        }
      }
    } catch (BestandException e) {
      throw new PgnException(MessageFormat.format(
          resourceBundle.getString(PGN.ERR_BESTAND_EXCEPTION),
          e.getLocalizedMessage()));
    }

    return partijen;
  }

  public static void maakUniek(List<Zet> zetten) {
    Map<String, List<Zet>>  uniekeZetten  = new HashMap<>();
    List<Zet>               gelijk;

    for (var zet : zetten) {
      var       korteNotatie  = zet.getZet();
      if (uniekeZetten.containsKey(korteNotatie)) {
        gelijk  = uniekeZetten.get(korteNotatie);
      } else {
        gelijk        = new LinkedList<>();
      }
      gelijk.add(zet);
      uniekeZetten.put(korteNotatie, gelijk);
    }

    if (zetten.size() == uniekeZetten.size()) {
      return;
    }

    // Zet de KorteNotatie 'niveau' 1 hoger.
    uniekeZetten.values()
                .stream()
                .filter(lijst -> (lijst.size() > 1))
                .forEachOrdered(lijst ->
      lijst.forEach(zet ->
          zet.setKorteNotatieLevel(zet.getKorteNotatieLevel()+1)));

    maakUniek(zetten);
  }

  public static String pgnZettenToChessTheatre(String pgnZetten)
      throws PgnException {
    return pgnZettenToChessTheatre(new FEN(), pgnZetten);
  }

  public static String pgnZettenToChessTheatre(FEN fen, String pgnZetten)
      throws PgnException {
    var pgnZet        = "";
    var halveZetten   = pgnZetten.split(" ");
    var chessTheatre  = new StringBuilder();

    for (String halveZet : halveZetten) {
      if (halveZet.indexOf('.') >= 0) {
        if (halveZet.indexOf('.') == (halveZet.length() - 1)) {
          throw new PgnException(MessageFormat.format(
              resourceBundle.getString(PGN.ERR_HALVEZET),
              halveZet, pgnZetten));
        }
        pgnZet  = halveZet.substring(halveZet.lastIndexOf('.') + 1);
      } else {
        pgnZet  = halveZet;
      }
      var juisteZet = vindZet(fen, pgnZet);
      // Zwart moet in lowercase.
      if (halveZet.indexOf('.') >= 0) {
        chessTheatre.append(juisteZet.getChessTheatreZet()).append(" ");
      } else {
        chessTheatre.append(juisteZet.getChessTheatreZet().toLowerCase())
                    .append(" ");
      }
      fen.doeZet(juisteZet);
    }

    return chessTheatre.toString().trim();
  }

  private static void schrijfTag(PGN partij, String line) throws PgnException {
    var tag   = line.substring(1, line.indexOf(' '));
    var value = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
    partij.addTag(tag, value);
  }

  public static String vertaalStukken(String zetten,
                                      String vanStukken, String naarStukken)
      throws PgnException {
    if (vanStukken.length() != naarStukken.length()) {
      throw new PgnException(resourceBundle.getString(PGN.ERR_STUKKEN));
    }

    var result  = zetten.toCharArray();

    // Voorkom dat de O van de rochade wordt aangezien voor een stuk.
    if (vanStukken.contains("O")) {
      zetten  = zetten.replace("O-O-O", "@-@-@").replace("O-O", "@-@");
    }
    for (var i = 0; i < vanStukken.length(); i++) {
      if (vanStukken.charAt(i) != naarStukken.charAt(i)) {
        var fromIndex = zetten.indexOf(vanStukken.charAt(i), 0);
        while (fromIndex >= 0) {
          result[fromIndex] = naarStukken.charAt(i);
          fromIndex         = zetten.indexOf(vanStukken.charAt(i),
                                             fromIndex+1);
        }
      }
    }

    return String.valueOf(result);
  }

  private static void verwerkPartijInMatrix(PGN partij, double[][] matrix,
                                            Competitie competitie,
                                            boolean telUitslag) {
    var dubbel    = competitie.getHeenTerug();
    var spelers   = competitie.getSpelers();
    var noSpelers = spelers.size();
    var rondes    = matrix[0].length;
    var wit       = partij.getTag(PGN.PGNTAG_WHITE);
    var zwart     = partij.getTag(PGN.PGNTAG_BLACK);
    var iWit      = competitie.getSpelerIndex(wit);
    var iZwart    = competitie.getSpelerIndex(zwart);

    int ronde;
    try {
      ronde =
          Integer.parseInt(partij.getTag(PGN.PGNTAG_ROUND));
    } catch (NumberFormatException e) {
      ronde = 1;
    }
    var uitslag = partij.getTag(PGN.PGNTAG_RESULT);

    if (null == uitslag) {
      return;
    }

    if (partij.isBye()) {
      if (competitie.metBye() && telUitslag) {
        if (competitie.getPuntenBye() != 0.0) {
          if (uitslag.equals(CaissaConstants.PARTIJ_WIT_WINT)) {
            competitie.getSpeler(iWit).addByeScore(competitie.getPuntenBye());
            telPunten(telUitslag, spelers, iWit, competitie.getPuntenBye(),
                      iZwart, 0.0);
          }
          if (uitslag.equals(CaissaConstants.PARTIJ_ZWART_WINT)) {
            competitie.getSpeler(iZwart).addByeScore(competitie.getPuntenBye());
            telPunten(telUitslag, spelers, iWit, 0.0,
                      iZwart, competitie.getPuntenBye());
          }
        }
      }
      return;
    }

    if (ronde > noSpelers
        && (!competitie.getSpeler(iWit).inRonde(ronde, rondes,
                                                competitie.getType())
            || !competitie.getSpeler(iZwart).inRonde(ronde, rondes,
                                                     competitie.getType()))) {
      return;
    }

    var kolomW  = iZwart;
    var kolomZ  = iWit;
    spelers = competitie.getDeelnemers();
    iWit    = getSpelerIndex(wit, spelers);
    iZwart  = getSpelerIndex(zwart, spelers);
    if (competitie.isDubbel()) {
      kolomW  = iZwart * dubbel;
      kolomZ  = iWit * dubbel + dubbel - 1;
    }

    switch (uitslag) {
      case CaissaConstants.PARTIJ_WIT_WINT:
        matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0)
                                    + competitie.getPuntenWinst();
        matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ],
                                           competitie.getPuntenVerlies());
        telPunten(telUitslag, spelers, iWit, competitie.getPuntenWinst(),
                  iZwart, competitie.getPuntenVerlies());
        break;
      case CaissaConstants.PARTIJ_REMISE:
        matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0)
                                    + competitie.getPuntenRemise();
        matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0)
                                    + competitie.getPuntenRemise();
        telPunten(telUitslag, spelers, iWit, competitie.getPuntenRemise(),
                  iZwart, competitie.getPuntenRemise());
        break;
      case CaissaConstants.PARTIJ_ZWART_WINT:
        matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW],
                                           competitie.getPuntenVerlies());
        matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0)
                                    + competitie.getPuntenWinst();
        telPunten(telUitslag, spelers, iWit, competitie.getPuntenVerlies(),
                  iZwart, competitie.getPuntenWinst());
        break;
      default:
        break;
    }
  }

  private static void telPunten(boolean telUitslag, List<Spelerinfo> spelers,
                                int iWit, double pWit,
                                int iZwart, double pZwart) {
    if (telUitslag) {
      if (iWit > -1) {
        if (iZwart > -1) {
          spelers.get(iWit).addPartij();
        }
        spelers.get(iWit).addPunt(pWit);
      }
      if (iZwart > -1) {
        if (iWit > -1) {
          spelers.get(iZwart).addPartij();
        }
        spelers.get(iZwart).addPunt(pZwart);
      }
    }
  }

  public static double[][] verwijderNietActief(double[][] matrix,
                                               Competitie competitie) {
    var enkel         = competitie.getHeenTerug();
    var spelers       = competitie.getSpelers();
    var zonderPartij  = spelers.stream()
                               .filter(speler -> speler.getPartijen().equals(0))
                               .count();
    if (zonderPartij == 0) {
      return matrix;
    }

    var noSpelers     = spelers.size();
    var laatsteKolom  = noSpelers * enkel - enkel;
    var laatsteSpeler = noSpelers - 1;

    for (var i = 0; i < noSpelers; i++) {
      if (spelers.get(i).getPartijen() == 0) {
        for (var j = i; j < laatsteSpeler; j++) {
          matrix[j] = matrix[j+1];
        }
        for (var j = i*enkel; j < laatsteKolom; j++) {
          for (var k = 0; k < laatsteSpeler; k++) {
            matrix[k][j]  = matrix[k][j+enkel];
          }
        }
        laatsteKolom  -= enkel;
        laatsteSpeler--;
      }
    }

    spelers.removeIf(speler -> speler.getPartijen().equals(0));
    noSpelers   = spelers.size();
    var schoon  = new double[noSpelers][noSpelers * enkel];
    for (var i = 0; i < noSpelers; i++) {
      schoon[i] = Arrays.copyOf(matrix[i], noSpelers * enkel);
    }

    return schoon;
  }

  public static Zet vindZet(FEN fen, String pgnZet) throws PgnException {
    var       zettengenerator = new Zettengenerator(fen);
    List<Zet> zetten          = zettengenerator.getZetten();
    // Mat wordt pas 'bekend' als er een tweede zettengeneratie wordt gedaan.
    // Deze manier vermijdt dit. Mat is tenslotte een 'speciale' schaak.
    var       teVinden        = pgnZet.replace('#', '+');

    for (Zet zet : zetten) {
      if (teVinden.equals(zet.getPgnNotatie())) {
        return zet;
      }
    }

    throw new PgnException(MessageFormat.format(
        resourceBundle.getString(PGN.ERR_ONGELDIGEZET), pgnZet, fen.getFen()));
  }

  public static double[][] vulToernooiMatrix(Collection<PGN> partijen,
                                             Competitie competitie,
                                             boolean sorteerOpStand) {
    var noSpelers = competitie.getDeelnemers().size();
    var kolommen  =
        (competitie.isMatch() ? partijen.size()
                              : noSpelers * competitie.getHeenTerug());
    var matrix    = new double[noSpelers][kolommen];

    initMatrix(matrix, noSpelers);
    zetInMatrix(partijen, matrix, competitie, true);
    berekenTieBreakScore(matrix, competitie);

    if (sorteerOpStand) {
      competitie.sorteerOpStand();
      initMatrix(matrix, noSpelers);
      zetInMatrix(partijen, matrix, competitie, false);
    }

    return matrix;
  }

  private static void zetInMatrix(Collection<PGN> partijen,
                                  double[][] matrix,
                                  Competitie competitie,
                                  boolean telUitslag) {
    for (PGN partij: partijen) {
      if (partij.isRanked()
          && (!partij.isBye() || competitie.metBye())) {
        verwerkPartijInMatrix(partij, matrix, competitie, telUitslag);
      }
    }
  }

  public static int zoekStuk(char stukcode) {
    for (var i = 0; i < stuk.length; i++) {
      if (stuk[i] == stukcode) {
        return i - 6;
      }
    }

    return -7;
  }
}
