/**
 * Copyright 2008 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
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

import java.io.IOException;


/**
 * @author Marco de Booij
 */
public class Caissa {
  static  boolean   mat           = false;
  static  boolean   wrk           = true;
  static  boolean   wrl           = true;
  static  boolean   zrk           = true;
  static  boolean   zrl           = true;
  static  boolean   korteRochade;
  static  boolean   langeRochade;

  static  int       stukNaar;
  static  int       stukVan;
  static  int       veldNaar;
  static  int       veldVan;
  static  int       koning        = 35;
  static  int       zetIndex      = 0;
  static  int       vry           = 0;

  static  int[]     bord          = new int[120];
  static  int[]     van           = new int[100];
  static  int[]     naar          = new int[100];
  static  int[]     zetWaarde     = new int[100];
  static  int[]     loop          = {9, 11, -9, -11, 10, 1, -1, -10, 19, 21,
                                     12, 8, -21, -19, -12, -8};
  static  int[]     waarde        = {-5000, -900, -500, -302, -300, -100, 0,
                                     100, 300, 302, 500, 900, 7000};
  static  char[]    stuk          = {'k', 'd', 't', 'l', 'p', 'o', '.',
                                     'O', 'P', 'L', 'T', 'D', 'K'};
  static  String[]  partij        = new String[255];

  /**
   * @param args
   */
  public static void main(String[] args) {
    init();
    printBord();
    zettenGenerator();
    besteZetten(100);
    bordwissel();
    String  invoer  = zoekZet();
    while (!mat && !"stop".equalsIgnoreCase(invoer)) {
      invoer  = zoekZet();
    }
    System.out.println("Stopped.");
  }
  
  private static boolean aangevallen (int stuk) {
    // Aangevallen door pion?
    if (bord[stuk +  9] == -1)
      return true;
    if (bord[stuk + 11] == -1)
      return true;
    // Aangevallen door koning?
    for (int i = 0; i < 8; i++) {
      if (bord[stuk + loop[i]] == -6)
        return true;
    }
    // Aangevallen door paard?
    for (int i = 8; i < 16; i++) {
      if (bord[stuk + loop[i]] == -2)
        return true;
    }
    int _stuk;
    // Aangevallen door loper of dame?
    for (int i = 0; i < 4; i++) {
      _stuk   = stuk + loop[i];
      while (bord[_stuk] == 0)
        _stuk = _stuk + loop[i];
      if (bord[_stuk] == -3)
        return true;
      if (bord[_stuk] == -5)
        return true;
    }
    // Aangevallen door toren of dame?
    for (int i = 4; i < 8; i++) {
      _stuk   = stuk + loop[i];
      while (bord[_stuk] == 0)
        _stuk = _stuk + loop[i];
      if (bord[_stuk] == -4)
        return true;
      if (bord[_stuk] == -5)
        return true;
    }
    return false;
  }

  private static void besteZetten(int aantal) {
    if (aantal > vry)
      aantal  = vry;
    for (int i = 0; i < aantal; i++) {
      waardeerZet(bord[naar[i]]);
      System.out.print((i+ 1) + ": ");
      System.out.print(van[i] + "-" + naar[i] + ": ");
      System.out.print((char) (van[i]%10 + 96));
      System.out.print((char) (van[i]/10 + 47));
      System.out.print((char) (naar[i]%10 + 96));
      System.out.println((char) (naar[i]/10 + 47));
    }
  }

  private static void bordwissel() {
    for (int i = 2; i < 6; i++) {
      for (int j = 1; j < 9; j++) {
        int hulp    = i * 10;
        int van     = hulp + j;
        int naar    = 110 - hulp + j;
        hulp        = bord[van];
        bord[van]   = -bord[naar];
        bord[naar]  = -hulp;
      }
    }
  }

  private static void dameZet(int veld) {
    for (int i = 0; i < 8; i++)
      ltdZet(veld, i);
  }

  private static void init() {
    for (int i = 1; i<120; i++)
      bord[i]  = 7;

    bord[21] = 4;
    bord[22] = 2;
    bord[23] = 3;
    bord[24] = 5;
    bord[25] = 6;
    bord[26] = 3;
    bord[27] = 2;
    bord[28] = 4;
    for (int i = 31; i<39; i++) {
      bord[i]       = 1;
      bord[i + 10]  = 0;
      bord[i + 20]  = 0;
      bord[i + 30]  = 0;
      bord[i + 40]  = 0;
      bord[i + 50]  = -1;
      bord[i + 60]  = -bord[i - 10];
    }
  }

  private static void koningZet(int veld) {
    for (int i = 0; i < 8; i++) {
      if (bord[veld + loop[i]] < 1) {
        boolean aan = aangevallen((veld + loop[i]));
        if (!aan) {
          van[vry]  = veld;
          naar[vry] = veld + loop[i];
          vry++;
        }
      }
    }
  }

  private static void loperZet(int veld) {
    for (int i = 0; i < 4; i++)
      ltdZet(veld, i);
  }

  private static void ltdZet(int _van, int index) {
    boolean aan;
    int     _naar = _van + loop[index];
    while (bord[_naar] == 0) {
      zetHeen(_van, _naar);
      aan = aangevallen(koning);
      zetTerug();
      if (!aan) {
        van[vry]  = _van;
        naar[vry] = _naar;
        vry++;
      }
      _naar = _naar + loop[index];
    }
    if (bord[_naar] < 0) {
      zetHeen(_van, _naar);
      aan = aangevallen(koning);
      zetTerug();
      if (!aan) {
        van[vry]  = _van;
        naar[vry] = _naar;
        vry++;
      }
    }
  }

  private static void paardZet(int veld) {
    for (int i = 8; i < 16; i++) {
      if (bord[veld + loop[i]] < 1) {
        zetHeen(veld, (veld + loop[i]));
        boolean aan = aangevallen(koning);
        zetTerug();
        if (!aan) {
          van[vry]  = veld;
          naar[vry] = veld + loop[i];
          vry++;
        }
      }
    }
  }

  private static void pionZet(int veld) {
    boolean aan;

    if (bord[veld + 10] == 0) {
      zetHeen(veld, (veld + 10));
      aan = aangevallen(koning);
      zetTerug();
      if (!aan) {
        van[vry]  = veld;
        naar[vry] = veld + 10;
        vry++;
        if (veld < 40) {
          if (bord[veld + 20] == 0) {
            zetHeen(veld, (veld + 20));
            aan = aangevallen(koning);
            zetTerug();
            if (!aan) {
              van[vry]  = veld;
              naar[vry] = veld + 20;
              vry++;
            }
          }
        }
      }
    }
    if (bord[veld + 9] < 0) {
      zetHeen(veld, (veld + 9));
      aan = aangevallen(koning);
      zetTerug();
      if (!aan) {
        van[vry]  = veld;
        naar[vry] = veld + 9;
        vry++;
      }
    }
    if (bord[veld + 11] < 0) {
      zetHeen(veld, (veld + 11));
      aan = aangevallen(koning);
      zetTerug();
      if (!aan) {
        van[vry]  = veld;
        naar[vry] = veld + 11;
        vry++;
      }
    }
  }

  private static void printBord() {
    for (int i = 9; i>1; i--) {
      System.out.print((i - 1) + " ");
      for (int j = 1; j < 9; j++)
        printStuk((i * 10 + j));
      System.out.println();
    }
    System.out.println("  ABCDEFGH");
  }

  private static void printStuk(int _stuk) {
    System.out.print(stuk[bord[_stuk] + 6]);
  }

  private static void rochade() {
    if (bord[26] == 6
        && bord[27] == 0
        && bord[28] == 0
        && bord[29] == 4
        && korteRochade) {
      if (!aangevallen(26)
          && !aangevallen(27)
          && !aangevallen(28)) {
        van[vry]  = 26;
        naar[vry] = 28;
        vry++;
      }
    }
    if (bord[26] == 6
        && bord[25] == 0
        && bord[24] == 0
        && bord[23] == 0
        && bord[22] == 4
        && langeRochade) {
      if (!aangevallen(26)
          && !aangevallen(25)
          && !aangevallen(24)) {
        van[vry]  = 26;
        naar[vry] = 24;
        vry++;
      }
    }
  }

  private static void torenZet(int veld) {
    for (int i = 4; i < 8; i++)
      ltdZet(veld, i);
  }

  private static void waardeerZet(int stuk) {
    int zetwaarde = -waarde[stuk + 7];
    for (int i = 1; i < 9; i++) {
      if (bord[20 + i] == 2)
        zetwaarde = zetwaarde - 3;
      if (bord[20 + i] == 3)
        zetwaarde = zetwaarde - 2;
      if (bord[10 * i + 2] > 0)
        zetwaarde = zetwaarde - 1;
      if (bord[10 * i + 9] > 0)
        zetwaarde = zetwaarde - 1;
    }

    for (int i = 3; i < 7; i++)
      for (int j = 3; j < 7; j++)
        if (bord[i * 10 + j] > 0)
          zetwaarde = zetwaarde + 1;

    if (bord[54] > 0)
      zetwaarde = zetwaarde + 1;
    if (bord[55] > 0)
      zetwaarde = zetwaarde + 3;
    if (bord[64] > 1)
      zetwaarde = zetwaarde + 2;
    if (bord[65] > 1)
      zetwaarde = zetwaarde + 2;
    if (bord[34] == 0)
      zetwaarde = zetwaarde + 2;
    if (bord[35] == 0)
      zetwaarde = zetwaarde + 3;
    if (bord[62] == 3)
      if (bord[73] == -2)
        zetwaarde = zetwaarde + 1;
    if (bord[67] == 3)
      if (bord[78] == -2)
        zetwaarde = zetwaarde + 1;
    if (bord[53] == 3)
      zetwaarde = zetwaarde + 2;
    if (bord[46] == 2)
      zetwaarde = zetwaarde + 2;
    if (bord[65] > 0)
      zetwaarde = zetwaarde + 1;
    for (int i = 21; i < 99; i++) {
      if (bord[i] == 5)
        if (aangevallen(i))
          zetwaarde = zetwaarde - 500;
    }
  }

  private static void zetHeen(int van, int naar) {
    stukVan         = bord[van];
    stukNaar        = bord[naar];
    veldVan         = van;
    veldNaar        = naar;
    bord[veldVan]   = 0;
    bord[veldNaar]  = stukVan;
  }

  private static void zettenGenerator() {
    vry     = 0;
    // Zoek de koning.
    koning  = 21;
    while (bord[koning] != 6)
      koning++;

    for (int i = 21; i < 99; i++) {
      switch (bord[i]) {
        case 1 : pionZet(i);
                 break;
        case 2 : paardZet(i);
                 break;
        case 3 : loperZet(i);
                 break;
        case 4 : torenZet(i);
                 break;
        case 5 : dameZet(i);
                 break;
        case 6 : koningZet(i);
      }
    }

    if (korteRochade
        || langeRochade)
      rochade();
  }

  private static void zetTerug() {
    bord[veldVan]   = stukVan;
    bord[veldNaar]  = stukNaar;
  }

  private static String zoekZet() {
    byte[]  zet         = new byte[4];
    System.out.print("Zet: ");
    try {
      System.in.read(zet);
    } catch (IOException e) {
      System.out.println("Error: " + e.getLocalizedMessage());
      return "stop";
    }
    
    return new String(zet);
  }

/*
PROCEDURE opscherm;
VAR  index1, index2 : INTEGER;
BEGIN
  index2 := zetindex - 10;
  IF zetindex < 12 THEN
  BEGIN
    index2 :=  1;
  END;
  
  FOR index1 := index2 TO index2 + 10 DO
  BEGIN
    GOTOXY(1,index1 - index2 + 11);
    WRITELN(index1,' ',partij[index1]);
  END;
END;

PROCEDURE sorteer;
VAR  eindesort,index,hulp : INTEGER;
     verwisseld : boolean;
BEGIN
  eindesort := ply1e;
  verwisseld := TRUE;
  while (eindesort > 1) and verwisseld do
  BEGIN
    verwisseld := FALSE;
    FOR index := 2 TO eindesort do
    BEGIN
      IF zetwrd[index] > zetwrd[index - 1] THEN
      BEGIN
        hulp              := zetwrd[index - 1];
        zetwrd[index - 1] := zetwrd[index];
        zetwrd[index]     := hulp;
        hulp           := van[index - 1];
        van[index - 1] := van[index];
        van[index]     := hulp;
        hulp            := naar[index - 1];
        naar[index - 1] := naar[index];
        naar[index]     := hulp;
        verwisseld := TRUE;
      END;
    END;
    eindesort := eindesort - 1;
  END;
END;

PROCEDURE zoekzet;
VAR  index1,index2 : INTEGER;
BEGIN
  vry := 1;
  rk := wrk;
  rl := wrl;
  zettengenerator;
  IF vry > 1 THEN
  BEGIN
    ply1e := vry - 1;
    ply2b := vry;
    FOR index1 := 1 TO ply1e do
    BEGIN
      wveldv := van[index1];
      wveldn := naar[index1];
      wstukv := bord[wveldv];
      wstukn := bord[wveldn];
      bord[wveldv] := 0;
      bord[wveldn] := wstukv;
      IF    (wveldv = 26)
        and (wveldn = 28)
        and (wstukv = 06) THEN
      BEGIN
        bord[29] := 0;
        bord[27] := 4;
      END;
      IF    (wveldv = 26)
        and (wveldn = 24)
        and (wstukv = 06) THEN
      BEGIN
        bord[22] := 0;
        bord[25] := 4;
      END;
      waardeerzet;
      bordwissel;
      vry := ply2b;
      rk := zrk;
      rl := zrl;
      zettengenerator;
      IF vry > ply2b THEN
      BEGIN
        ply2e := vry - 1;
        zetwaarde1 := -20000;
        FOR index2 := ply2b TO ply2e do
        BEGIN
          zveldv := van[index2];
          zveldn := naar[index2];
          zstukv := bord[zveldv];
          zstukn := bord[zveldn];
          bord[zveldv] := 0;
          bord[zveldn] := zstukv;
          IF    (zveldv = 26)
            and (zveldn = 28)
            and (zstukv = 06) THEN
          BEGIN
            bord[29] := 0;
            bord[27] := 4;
          END;
          IF    (zveldv = 26)
            and (zveldn = 24)
            and (zstukv = 06) THEN
          BEGIN
            bord[22] := 0;
            bord[25] := 4;
          END;
          ruilwaarde := -waarde[zstukn + 7];
          aangevallen(zveldn);
          IF aan THEN ruilwaarde := ruilwaarde - waarde[zstukv + 7];
          veld := 22;
          WHILE bord[veld] <> -6 DO
          BEGIN
            veld := veld + 1;
          END;
          aangevallen(veld);
          IF aan THEN ruilwaarde := ruilwaarde - waarde[zstukn + 7];
          IF ruilwaarde > zetwaarde1 THEN zetwaarde1 := ruilwaarde;
          bord[zveldv] := zstukv;
          bord[zveldn] := zstukn;
          IF    (zveldv = 26)
            and (zveldn = 28)
            and (zstukv = 06) THEN
          BEGIN
            bord[27] := 0;
            bord[29] := 4;
          END;
          IF    (zveldv = 26)
            and (zveldn = 24)
            and (zstukv = 06) THEN
          BEGIN
            bord[25] := 0;
            bord[22] := 4;
          END;
        END;
      END;
      bordwissel;
      bord[wveldv] := wstukv;
      bord[wveldn] := wstukn;
      IF    (wveldv = 26)
        and (wveldn = 28)
        and (wstukv = 06) THEN
      BEGIN
        bord[27] := 0;
        bord[29] := 4;
        zetwaarde := zetwaarde + 25;
      END;
      IF    (wveldv = 26)
        and (wveldn = 24)
        and (wstukv = 06) THEN
      BEGIN
        bord[25] := 0;
        bord[22] := 4;
        zetwaarde := zetwaarde +  20;
      END;
      zetwrd[index1] := zetwaarde - zetwaarde1;
    END;
  END;
  IF vry > 1 THEN
  BEGIN
    sorteer;
    GOTOXY(17,01);
    WRITE('-->');
    beste48;
    bord[naar[1]] := bord[van[1]];
    bord[van[1]]  := 0;
    veldvan  := van[1];
    veldnaar := naar[1];
    printstuk(veldvan);
    printstuk(veldnaar);
    IF    (veldvan  = 26)
      and (veldnaar = 28)
      and (bord[veldnaar] = 6) THEN
    BEGIN
      bord[29] := 0;
      bord[27] := 4;
      printstuk(27);
      printstuk(29);
    END;
    IF    (veldvan  = 26)
      and (veldnaar = 24)
      and (bord[veldnaar] = 6) THEN
    BEGIN
      bord[22] := 0;
      bord[25] := 4;
      printstuk(22);
      printstuk(25);
    END;
  END;
  IF   (vry = 1)
    or (vry = ply2b) THEN
  BEGIN
    WRITELN('*****  M A T  *****');
    mat := TRUE;
  END;
END;


PROCEDURE invoer;
VAR  index,zvan,znaar,wvan,wnaar : INTEGER;
BEGIN
  zetok := FALSE;
  REPEAT
  BEGIN
    GOTOXY(01,24);
    WRITE('     zet     ');
    GOTOXY(10,24);
    READLN(zet[1],zet[2],zet[3],zet[4]);
    zvan  := (ORD(zet[2]) - 47) * 10 + ORD(zet[1]) - 95;
    znaar := (ORD(zet[4]) - 47) * 10 + ORD(zet[3]) - 95;
    wvan  := 110 - (ORD(zet[2]) - 47) * 10 + ORD(zet[1]) - 95;
    wnaar := 110 - (ORD(zet[4]) - 47) * 10 + ORD(zet[3]) - 95;
    IF zet = 'stop' THEN zetok := true;
    bordwissel;
    rk := zrk;
    rl := zrl;
    zettengenerator;
    bordwissel;
    FOR index := 1 TO vry - 1 DO
    BEGIN
      IF    (van[index]  = wvan )
        and (naar[index] = wnaar) THEN zetok := TRUE;
    END;
  END;
  UNTIL zetok;

  partij[zetindex][05] := ' ';
  partij[zetindex][06] := '-';
  partij[zetindex][07] := ' ';
  partij[zetindex][08] := zet[1];
  partij[zetindex][09] := zet[2];
  partij[zetindex][10] := zet[3];
  partij[zetindex][11] := zet[4];
  opscherm;
  zetindex := zetindex + 1;

  bord[znaar] := bord[zvan];
  bord[zvan ] := 0;
   veldvan  := zvan;
   veldnaar := znaar;
   printstuk(veldvan );
   printstuk(veldnaar);
  IF    (zvan  = 96)
    and (znaar = 98)
    and (bord[znaar] = -6) THEN
  BEGIN
    bord[97] := -4;
    bord[99] :=  0;
    printstuk(97);
    printstuk(99);
  END;
  IF    (zvan  = 96)
    and (znaar = 94)
    and (bord[znaar] = -6) THEN
  BEGIN
    bord[92] :=  0;
    bord[95] := -4;
    printstuk(92);
    printstuk(95);
  END;
END;
 */
}
