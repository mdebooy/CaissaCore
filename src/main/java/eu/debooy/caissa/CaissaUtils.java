/**
 * Copyright 2008 Marco de Booij
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
import eu.debooy.doosutils.access.TekstBestand;
import eu.debooy.doosutils.exception.BestandException;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

  public static int externToIntern(String veld) {
    return (veld.charAt(0) - 96) + (veld.charAt(1) - 47) * 10;
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
    Collection<PGN> partijen    = new ArrayList<PGN>();

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
    Map<String, List<Zet>>  uniekeZetten  = new HashMap<String, List<Zet>>();

    for (Zet zet: zetten) {
      String    korteNotatie  = zet.getZet();
      List<Zet> gelijk        = new LinkedList<Zet>();
      if (uniekeZetten.containsKey(korteNotatie)) {
        gelijk  = uniekeZetten.get(korteNotatie);
      }
      gelijk.add(zet);
      uniekeZetten.put(korteNotatie, gelijk);
    }

    if (zetten.size() == uniekeZetten.size()) {
      return;
    }

    // Zet de KorteNotatie 'niveau' 1 hoger.
    for (List<Zet> lijst: uniekeZetten.values()) {
      if (lijst.size() > 1) {
        for (Zet zet: zetten) {
          zet.setKorteNotatieLevel(zet.getKorteNotatieLevel()+1);
        }
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
    String[]      halveZet      = pgnZetten.split(" ");
    StringBuilder chessTheatre  = new StringBuilder();

    for (int i = 0; i < halveZet.length; i++) {
      Zettengenerator zettengenerator = new Zettengenerator(fen);
      List<Zet>       zetten          = zettengenerator.getZetten();
      Zet             juisteZet       = null;
      if (halveZet[i].indexOf('.') >= 0) {
        if (halveZet[i].indexOf('.') == (halveZet[i].length() - 1)) {
          throw new PgnException(MessageFormat.format(
              resourceBundle.getString(PGN.ERR_HALVEZET),
              halveZet[i], pgnZetten));
        }
        pgnZet  = halveZet[i].substring(halveZet[i].lastIndexOf('.') + 1);
      } else {
        pgnZet  = halveZet[i];
      }
      // Een mat zet kan alleen bepaald worden als men de volgende zetten gaat
      // genereren. Om de verwerking sneller te laten gebeuren wordt de mat
      // aanduiding veranderd in een schaak aanduiding. Mat is tenslotte een
      // schaak die niet kan worden opgeheven.
      pgnZet  = pgnZet.replace('#', '+');
      Iterator<?> iter  = zetten.iterator();
      while (null == juisteZet
          && iter.hasNext()) {
        Zet zet = (Zet) iter.next();
        if (pgnZet.equals(zet.getPgnNotatie())) {
          juisteZet = zet;
        }
      }
      if (null != juisteZet) {
        if (halveZet[i].indexOf('.') >= 0) {
          chessTheatre.append(juisteZet.getChessTheatreZet()).append(" ");
        } else {
          chessTheatre.append(juisteZet.getChessTheatreZet().toLowerCase())
                      .append(" ");
        }
        if (!zetten.isEmpty()) {
          fen.doeZet(juisteZet);
        }
      } else {
        throw new PgnException(MessageFormat.format(
            resourceBundle.getString(PGN.ERR_ONGELDIGEZET),
            pgnZet, pgnZetten));
      }
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
      // Zet de rochade weer terug.
      if (vanStukken.contains("O")) {
        zetten  = zetten.replace("@-@-@", "O-O-O").replace("@-@", "O-O");
      }
    } else {
      throw new PgnException(resourceBundle.getString(PGN.ERR_STUKKEN));
    }

    return String.valueOf(result);
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
          int     ronde   = 1;
          try {
            ronde = Integer.valueOf(partij.getTag(CaissaConstants.PGNTAG_ROUND))
                           .intValue();
          } catch (NumberFormatException nfe) {
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
          if (CaissaConstants.PARTIJ_WIT_WINT.equals(uitslag)) {
            matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0) + 1.0;
            matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0);
            if (i == 0) {
              speler[iWit].addPartij();
              speler[iWit].addPunt(1.0);
              speler[iZwart].addPartij();
            }
          } else if (CaissaConstants.PARTIJ_REMISE.equals(uitslag)) {
            matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0) + 0.5;
            matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0)
                                      + 0.5;
            if (i == 0) {
              speler[iWit].addPartij();
              speler[iWit].addPunt(0.5);
              speler[iZwart].addPartij();
              speler[iZwart].addPunt(0.5);
            }
          } else if (CaissaConstants.PARTIJ_ZWART_WINT.equals(uitslag)) {
            matrix[iWit][kolomW]    = Math.max(matrix[iWit][kolomW], 0.0);
            matrix[iZwart][kolomZ]  = Math.max(matrix[iZwart][kolomZ], 0.0)
                                      + 1.0;
            if (i == 0) {
              speler[iWit].addPartij();
              speler[iZwart].addPartij();
              speler[iZwart].addPunt(1.0);
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
}
