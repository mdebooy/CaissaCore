/**
 * Copyright 2009 Marco de Booij
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


/**
 * @author Marco de Booij
 */
public final class CaissaConstants {
  public static final String  STUKKEN           = "PNBRQK";
  public static final String  NOTATIE_STUKKEN   = " NBRQK";
  public static final String  PROMOTIE_STUKKEN  = "NBRQ";

  public static final String  DEF_DATUMFORMAAT  = "dd/MM/yyyy";
  public static final String  DEF_EINDDATUM     = "9999.99.99";
  public static final String  DEF_STARTDATUM    = "0000.00.00";

  public static final String  FEN_KORTEROKADE = "cdefgh";
  public static final String  FEN_LANGEROKADE = "abcdef";

  public static final String  TIEBREAK_SB       = "SB";
  public static final String  TIEBREAK_WP       = "WP";

  public static final int     PION              = 1;
  public static final int     PAARD             = 2;
  public static final int     LOPER             = 3;
  public static final int     TOREN             = 4;
  public static final int     DAME              = 5;
  public static final int     KONING            = 6;
  public static final int     ZPION             = -1;
  public static final int     ZPAARD            = -2;
  public static final int     ZLOPER            = -3;
  public static final int     ZTOREN            = -4;
  public static final int     ZDAME             = -5;
  public static final int     ZKONING           = -6;

  public static final int     WINST             = 2;
  public static final int     REMISE            = 1;
  public static final int     VERLIES           = 0;

  public static final String  KORTE_ROCHADE     = "O-O";
  public static final String  LANGE_ROCHADE     = "O-O-O";
  public static final String  EN_PASSANT        = " e.p.";

  public static final String  PARTIJ_BEZIG      = "*";
  public static final String  PARTIJ_REMISE     = "1/2-1/2";
  public static final String  PARTIJ_UNRANKED   = "unranked";
  public static final String  PARTIJ_UNRATED    = "unrated";
  public static final String  PARTIJ_WIT_WINT   = "1-0";
  public static final String  PARTIJ_ZWART_WINT = "0-1";

  public static final String  BYE = "bye";

  private static final  String  BSLTDK  = "BSLTDK";

  public static final String  RESOURCEBUNDLE  = "CaissaCore";

  public static final char  WIT   = 'w';
  public static final char  ZWART = 'b';

  private CaissaConstants() {
  }

  public enum  Stukcodes {
    CZ("PJSVDK"), DA(BSLTDK)  , DE(BSLTDK)  , EN(STUKKEN) , ES("PCATDR"),
    ET("PROVLK"), FI("PRLTDK"), FR("PCFTDR"), HU("GHFBVK"), IS("PRBHDK"),
    IT("PCATDR"), NL("OPLTDK"), NO(BSLTDK)  , PL("PSGWHK"), PT("PCBTDR"),
    RO("PCNTDR"), SV(BSLTDK)  , UTF8("♙♘♗♖♕♔");

    private Stukcodes(String stukcodes) { stukken = stukcodes; }
    public  String getStukcodes() { return stukken; }

    private final String stukken;
  }
}
