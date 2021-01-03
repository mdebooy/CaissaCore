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

import eu.debooy.caissa.exceptions.FenException;
import eu.debooy.caissa.exceptions.PgnException;
import eu.debooy.caissa.exceptions.ZetException;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.access.TekstBestand;
import eu.debooy.doosutils.exception.BestandException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * @author Marco de Booij
 */
public final class CaissaUtils {
  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("CaissaCore");

  private static final  char[]  stuk  = {'k', 'q', 'r', 'b', 'n', 'p', '.',
                                         'P', 'N', 'B', 'R', 'Q', 'K'};

  private CaissaUtils() {
  }

  private static void berekenSBscore(Spelerinfo[] speler, double[][] matrix,
                                     int toernooiType) {
    int noSpelers = speler.length;
    int kolommen  = matrix[0].length;
    for (int i = 0; i < noSpelers; i++) {
      Double  sbScore = 0.0;
      for (int j = 0; j < kolommen; j++) {
        if (toernooiType > 0 && matrix[i][j] > 0.0) {
          sbScore += speler[j / toernooiType].getPunten()
                              * matrix[i][j];
        }
      }
      speler[i].setTieBreakScore(sbScore);
    }
  }

  public static void berekenTieBreakScore(String tieBreakType,
                                          Spelerinfo[] speler,
                                          double[][] matrix,
                                          int toernooiType) {
    switch (tieBreakType) {
      case CaissaConstants.TIEBREAK_SB:
                berekenSBscore(speler, matrix, toernooiType);
                break;
      case CaissaConstants.TIEBREAK_WP:
                berekenWeerstandspunten(speler, matrix, toernooiType);
                break;
      default:
        break;
    }
  }

  private static void berekenWeerstandspunten(Spelerinfo[] speler,
                                             double[][] matrix,
                                             int toernooiType) {
    int noSpelers = speler.length;
    int kolommen  = matrix[0].length;
    for (int i = 0; i < noSpelers; i++) {
      Double weerstandspunten = 0.0;
      for (int j = 0; j < kolommen; j++) {
        if (toernooiType > 0) {
          weerstandspunten += speler[j / toernooiType].getPunten();
        }
      }
      speler[i].setTieBreakScore(weerstandspunten);
    }
  }

  public static String[] bergertabel(int spelers) {
    int       aantal    = spelers + (spelers%2);
    int       rondes    = aantal - 1;
    String[]  resultaat = new String[rondes];

    int   speler  = 1;
    int[] paring  = new int[aantal];
    for (int i = 0; i < rondes; i++) {
      for (int j = 0; j < rondes; j++) {
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

      StringBuilder ronde = new StringBuilder();
      for (int j = 0; j < aantal/2; j++) {
        ronde.append(" ").append(paring[j])
             .append("-").append(paring[rondes-j]);
      }
      speler        = paring[(aantal/2)];
      resultaat[i]  = ronde.toString().trim();
    }

    return resultaat;
  }

  public static int externToIntern(String veld) {
    return (veld.charAt(0) - 96) + (veld.charAt(1) - 47) * 10;
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
      enkel = CaissaConstants.TOERNOOI_ENKEL;
      break;
    case DoosConstants.ONWAAR:
      enkel = CaissaConstants.TOERNOOI_DUBBEL;
      break;
    default:
      enkel = CaissaConstants.TOERNOOI_MATCH;
      break;
    }

    return enkel;
  }

  public static String internToExtern(int veld) {
    return "" + (char) (veld%10 + 96) + (char) (veld/10 + 47);
  }

  public static boolean isMat(FEN fen) throws ZetException {
    int zetten  = new Zettengenerator(fen).getAantalZetten();

    return (zetten == 0);
  }

  public static Collection<PGN> laadPgnBestand(String bestand)
      throws PgnException {
    return laadPgnBestand(bestand, Charset.defaultCharset().name());
  }

  public static Collection<PGN> laadPgnBestand(String bestand, String charSet)
      throws PgnException {
    TekstBestand    input       = null;
    int             lijnnummer  = 0;
    Collection<PGN> partijen    = new ArrayList<>();

    try {
      input = new TekstBestand.Builder()
                              .setBestand(bestand
                                  + (bestand.endsWith(".pgn") ? "" : ".pgn"))
                              .setCharset(charSet).build();

      boolean eof   = false;
      String  lijn  = "";
      if (input.hasNext()) {
        lijn  = input.next();
        lijnnummer++;
      } else {
        eof = true;
      }
      while (!eof) {
        PGN     partij  = new PGN();

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
        String        uitslag = partij.getTag("Result");
        StringBuilder zetten  = new StringBuilder();
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
          String  resultaat = zetten.toString().trim();
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
      input.close();
    } catch (BestandException e) {
      throw new PgnException(MessageFormat.format(
          resourceBundle.getString(PGN.ERR_BESTAND_EXCEPTION),
          e.getLocalizedMessage()));
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (BestandException e) {
        throw new PgnException(MessageFormat.format(
            resourceBundle.getString(PGN.ERR_BESTAND_EXCEPTION),
            e.getLocalizedMessage()));
      }
    }
    return partijen;
  }

  public static void maakUniek(List<Zet> zetten) {
    Map<String, List<Zet>>  uniekeZetten  = new HashMap<>();

    zetten.forEach(zet -> {
      String    korteNotatie  = zet.getZet();
      List<Zet> gelijk        = new LinkedList<>();
      if (uniekeZetten.containsKey(korteNotatie)) {
        gelijk  = uniekeZetten.get(korteNotatie);
      }
      gelijk.add(zet);
      uniekeZetten.put(korteNotatie, gelijk);
    });

    if (zetten.size() == uniekeZetten.size()) {
      return;
    }

    // Zet de KorteNotatie 'niveau' 1 hoger.
    for (List<Zet> lijst: uniekeZetten.values()) {
      if (lijst.size() > 1) {
        zetten.forEach(zet -> {
          zet.setKorteNotatieLevel(zet.getKorteNotatieLevel()+1);
        });
      }
      maakUniek(lijst);
    }
  }

  public static String pgnZettenToChessTheatre(String pgnZetten)
      throws FenException, PgnException {
    return pgnZettenToChessTheatre(new FEN(), pgnZetten);
  }

  public static String pgnZettenToChessTheatre(FEN fen, String pgnZetten)
      throws FenException, PgnException {
    String        pgnZet        = "";
    String[]      halveZetten   = pgnZetten.split(" ");
    StringBuilder chessTheatre  = new StringBuilder();

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
      Zet juisteZet = vindZet(fen, pgnZet);
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
    String tag    = line.substring(1, line.indexOf(' '));
    String value  = line.substring(line.indexOf('"') + 1,
        line.lastIndexOf('"'));
    partij.addTag(tag, value);
  }

  public static String vertaalStukken(String zetten,
                                      String vanStukken, String naarStukken)
      throws PgnException {
    char[]  result  = zetten.toCharArray();

    if (vanStukken.length() == naarStukken.length()) {
      // Voorkom dat de O van de rochade wordt aangezien voor een stuk.
      if (vanStukken.contains("O")) {
        zetten  = zetten.replace("O-O-O", "@-@-@").replace("O-O", "@-@");
      }
      for (int i = 0; i < vanStukken.length(); i++) {
        if (vanStukken.charAt(i) != naarStukken.charAt(i)) {
          int fromIndex = zetten.indexOf(vanStukken.charAt(i), 0);
          while (fromIndex >= 0) {
            result[fromIndex] = naarStukken.charAt(i);
            fromIndex         = zetten.indexOf(vanStukken.charAt(i),
                                               fromIndex+1);
          }
        }
      }
    } else {
      throw new PgnException(resourceBundle.getString(PGN.ERR_STUKKEN));
    }

    return String.valueOf(result);
  }

  public static Zet vindZet(FEN fen, String pgnZet) throws PgnException {
    Zettengenerator zettengenerator = new Zettengenerator(fen);
    List<Zet>       zetten          = zettengenerator.getZetten();
    // Mat wordt pas 'bekend' als er een tweede zettengeneratie wordt gedaan.
    // Deze manier vermijdt dit. Mat is tenslotte een 'speciale' schaak.
    String          teVinden        = pgnZet.replace('#', '+');

    for (Zet zet : zetten) {
      if (teVinden.equals(zet.getPgnNotatie())) {
        return zet;
      }
    }

    throw new PgnException(MessageFormat.format(
        resourceBundle.getString(PGN.ERR_ONGELDIGEZET), pgnZet, fen.getFen()));
  }

  public static void vulToernooiMatrix(Collection<PGN> partijen,
                                       Spelerinfo[] speler, String[] halve,
                                       double[][] matrix, int toernooiType,
                                       boolean sorteerOpStand,
                                       String tieBreakType) {
    int       aantalIteraties = sorteerOpStand ? 2 : 1;
    int       noSpelers       = speler.length;
    String[]  namen           = new String[noSpelers];

    // Vul een array op metde namen en sorteer deze zodat er een binary search
    // op gedaan kan worden.
    for (int i = 0; i < noSpelers; i++) {
      namen[i]  = speler[i].getNaam();
    }
    Arrays.sort(namen, String.CASE_INSENSITIVE_ORDER);

    for (int i = 0; i < aantalIteraties; i++) {
      // Initialiseer de matrix.
      int kolommen  = matrix[0].length;
      for (int j = 0; j < noSpelers; j++) {
        for (int k = 0; k < kolommen; k++) {
          matrix[j][k]  = -1.0;
        }
      }

      // Sorteer de speler array op plaats in de stand. Dit enkel in de 2e
      // iteratie.
      if (i == 1) {
        Arrays.sort(speler);
      }

      // Zet positie in de matrix.
      int[] stand = new int[noSpelers];
      for (int j = 0; j < noSpelers; j++) {
        stand[Arrays.binarySearch(namen, speler[j].getNaam(),
                                  String.CASE_INSENSITIVE_ORDER)] = j;
      }

      for (PGN partij: partijen) {
        String  wit     = partij.getTag(CaissaConstants.PGNTAG_WHITE);
        String  zwart   = partij.getTag(CaissaConstants.PGNTAG_BLACK);
        if (partij.isRanked()
            && !partij.isBye()) {
          int   ronde;
          try {
            ronde =
                Integer.parseInt(partij.getTag(CaissaConstants.PGNTAG_ROUND));
          } catch (NumberFormatException e) {
            ronde = 1;
          }
          String  uitslag = partij.getTag(CaissaConstants.PGNTAG_RESULT);
          if (ronde > noSpelers
              && (Arrays.binarySearch(halve, wit,
                                      String.CASE_INSENSITIVE_ORDER) > -1
                  || Arrays.binarySearch(halve, zwart,
                                         String.CASE_INSENSITIVE_ORDER) > -1)) {
            continue;
          }
          int   iWit    =
              stand[Arrays.binarySearch(namen, wit,
                                        String.CASE_INSENSITIVE_ORDER)];
          int   iZwart  =
              stand[Arrays.binarySearch(namen, zwart,
                                        String.CASE_INSENSITIVE_ORDER)];
          int   kolomW  = ronde - 1;
          int   kolomZ  = ronde - 1;
          if (toernooiType > 0) {
            kolomW  = iZwart * toernooiType;
            kolomZ  = iWit * toernooiType + toernooiType - 1;
          }
          if (null != uitslag) {
            switch (uitslag) {
              case CaissaConstants.PARTIJ_WIT_WINT:
                matrix[iWit][kolomW]    =
                    Math.max(matrix[iWit][kolomW], 0.0) + 1.0;
                matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0);
                if (i == 0) {
                  speler[iWit].addPartij();
                  speler[iWit].addPunt(1.0);
                  speler[iZwart].addPartij();
                }
                break;
              case CaissaConstants.PARTIJ_REMISE:
                matrix[iWit][kolomW]    =
                    Math.max(matrix[iWit][kolomW], 0.0) + 0.5;
                matrix[iZwart][kolomZ]  =
                    Math.max(matrix[iZwart][kolomZ], 0.0) + 0.5;
                if (i == 0) {
                  speler[iWit].addPartij();
                  speler[iWit].addPunt(0.5);
                  speler[iZwart].addPartij();
                  speler[iZwart].addPunt(0.5);
                }
                break;
              case CaissaConstants.PARTIJ_ZWART_WINT:
                matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0);
                matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0)
                        + 1.0;
                if (i == 0) {
                  speler[iWit].addPartij();
                  speler[iZwart].addPartij();
                  speler[iZwart].addPunt(1.0);
                }
                break;
              default:
                break;
            }
          }
        }
      }

      // Bereken de Tie-Break Score na de eerste iteratie.
      if (i == 0) {
        berekenTieBreakScore(tieBreakType, speler, matrix, toernooiType);
      }
    }
  }

  public static int zoekStuk(char stukcode) {
    for (int i = 0; i < stuk.length; i++) {
      if (stuk[i] == stukcode) {
        return i - 6;
      }
    }

    return -7;
  }
}
