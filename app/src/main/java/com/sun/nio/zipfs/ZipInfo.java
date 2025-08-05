package com.sun.nio.zipfs;

import java.nio.file.Paths;
import java.util.Collections;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipInfo.class */
public class ZipInfo {
    public static void main(String[] strArr) throws Throwable {
        if (strArr.length < 1) {
            print("Usage: java ZipInfo zfname", new Object[0]);
            return;
        }
        ZipFileSystem zipFileSystem = (ZipFileSystem) new ZipFileSystemProvider().newFileSystem(Paths.get(strArr[0], new String[0]), Collections.emptyMap());
        byte[] bArr = zipFileSystem.cen;
        if (bArr == null) {
            print("zip file is empty%n", new Object[0]);
            return;
        }
        byte[] bArr2 = new byte[1024];
        int i2 = 1;
        for (int iCENNAM = 0; iCENNAM + 46 < bArr.length; iCENNAM += 46 + ZipConstants.CENNAM(bArr, iCENNAM) + ZipConstants.CENEXT(bArr, iCENNAM) + ZipConstants.CENCOM(bArr, iCENNAM)) {
            int i3 = i2;
            i2++;
            print("----------------#%d--------------------%n", Integer.valueOf(i3));
            printCEN(bArr, iCENNAM);
            long jCENNAM = 30 + ZipConstants.CENNAM(bArr, iCENNAM) + ZipConstants.CENEXT(bArr, iCENNAM) + 46;
            if (zipFileSystem.readFullyAt(bArr2, 0, jCENNAM, locoff(bArr, iCENNAM)) != jCENNAM) {
                ZipFileSystem.zerror("read loc header failed");
            }
            if (ZipConstants.LOCEXT(bArr2) > ZipConstants.CENEXT(bArr, iCENNAM) + 46) {
                long jLOCNAM = 30 + ZipConstants.LOCNAM(bArr2) + ZipConstants.LOCEXT(bArr2);
                if (zipFileSystem.readFullyAt(bArr2, 0, jLOCNAM, locoff(bArr, iCENNAM)) != jLOCNAM) {
                    ZipFileSystem.zerror("read loc header failed");
                }
            }
            printLOC(bArr2);
        }
        zipFileSystem.close();
    }

    static void print(String str, Object... objArr) {
        System.out.printf(str, objArr);
    }

    static void printLOC(byte[] bArr) {
        print("%n", new Object[0]);
        print("[Local File Header]%n", new Object[0]);
        print("    Signature   :   %#010x%n", Long.valueOf(ZipConstants.LOCSIG(bArr)));
        if (ZipConstants.LOCSIG(bArr) != ZipConstants.LOCSIG) {
            print("    Wrong signature!", new Object[0]);
            return;
        }
        print("    Version     :       %#6x    [%d.%d]%n", Integer.valueOf(ZipConstants.LOCVER(bArr)), Integer.valueOf(ZipConstants.LOCVER(bArr) / 10), Integer.valueOf(ZipConstants.LOCVER(bArr) % 10));
        print("    Flag        :       %#6x%n", Integer.valueOf(ZipConstants.LOCFLG(bArr)));
        print("    Method      :       %#6x%n", Integer.valueOf(ZipConstants.LOCHOW(bArr)));
        print("    LastMTime   :   %#10x    [%tc]%n", Long.valueOf(ZipConstants.LOCTIM(bArr)), Long.valueOf(ZipUtils.dosToJavaTime(ZipConstants.LOCTIM(bArr))));
        print("    CRC         :   %#10x%n", Long.valueOf(ZipConstants.LOCCRC(bArr)));
        print("    CSize       :   %#10x%n", Long.valueOf(ZipConstants.LOCSIZ(bArr)));
        print("    Size        :   %#10x%n", Long.valueOf(ZipConstants.LOCLEN(bArr)));
        print("    NameLength  :       %#6x    [%s]%n", Integer.valueOf(ZipConstants.LOCNAM(bArr)), new String(bArr, 30, ZipConstants.LOCNAM(bArr)));
        print("    ExtraLength :       %#6x%n", Integer.valueOf(ZipConstants.LOCEXT(bArr)));
        if (ZipConstants.LOCEXT(bArr) != 0) {
            printExtra(bArr, 30 + ZipConstants.LOCNAM(bArr), ZipConstants.LOCEXT(bArr));
        }
    }

    static void printCEN(byte[] bArr, int i2) {
        print("[Central Directory Header]%n", new Object[0]);
        print("    Signature   :   %#010x%n", Long.valueOf(ZipConstants.CENSIG(bArr, i2)));
        if (ZipConstants.CENSIG(bArr, i2) != ZipConstants.CENSIG) {
            print("    Wrong signature!", new Object[0]);
            return;
        }
        print("    VerMadeby   :       %#6x    [%d, %d.%d]%n", Integer.valueOf(ZipConstants.CENVEM(bArr, i2)), Integer.valueOf(ZipConstants.CENVEM(bArr, i2) >> 8), Integer.valueOf((ZipConstants.CENVEM(bArr, i2) & 255) / 10), Integer.valueOf((ZipConstants.CENVEM(bArr, i2) & 255) % 10));
        print("    VerExtract  :       %#6x    [%d.%d]%n", Integer.valueOf(ZipConstants.CENVER(bArr, i2)), Integer.valueOf(ZipConstants.CENVER(bArr, i2) / 10), Integer.valueOf(ZipConstants.CENVER(bArr, i2) % 10));
        print("    Flag        :       %#6x%n", Integer.valueOf(ZipConstants.CENFLG(bArr, i2)));
        print("    Method      :       %#6x%n", Integer.valueOf(ZipConstants.CENHOW(bArr, i2)));
        print("    LastMTime   :   %#10x    [%tc]%n", Long.valueOf(ZipConstants.CENTIM(bArr, i2)), Long.valueOf(ZipUtils.dosToJavaTime(ZipConstants.CENTIM(bArr, i2))));
        print("    CRC         :   %#10x%n", Long.valueOf(ZipConstants.CENCRC(bArr, i2)));
        print("    CSize       :   %#10x%n", Long.valueOf(ZipConstants.CENSIZ(bArr, i2)));
        print("    Size        :   %#10x%n", Long.valueOf(ZipConstants.CENLEN(bArr, i2)));
        print("    NameLen     :       %#6x    [%s]%n", Integer.valueOf(ZipConstants.CENNAM(bArr, i2)), new String(bArr, i2 + 46, ZipConstants.CENNAM(bArr, i2)));
        print("    ExtraLen    :       %#6x%n", Integer.valueOf(ZipConstants.CENEXT(bArr, i2)));
        if (ZipConstants.CENEXT(bArr, i2) != 0) {
            printExtra(bArr, i2 + 46 + ZipConstants.CENNAM(bArr, i2), ZipConstants.CENEXT(bArr, i2));
        }
        print("    CommentLen  :       %#6x%n", Integer.valueOf(ZipConstants.CENCOM(bArr, i2)));
        print("    DiskStart   :       %#6x%n", Integer.valueOf(ZipConstants.CENDSK(bArr, i2)));
        print("    Attrs       :       %#6x%n", Integer.valueOf(ZipConstants.CENATT(bArr, i2)));
        print("    AttrsEx     :   %#10x%n", Long.valueOf(ZipConstants.CENATX(bArr, i2)));
        print("    LocOff      :   %#10x%n", Long.valueOf(ZipConstants.CENOFF(bArr, i2)));
    }

    static long locoff(byte[] bArr, int i2) {
        long jCENOFF = ZipConstants.CENOFF(bArr, i2);
        if (jCENOFF == 4294967295L) {
            int iCENNAM = i2 + 46 + ZipConstants.CENNAM(bArr, i2);
            int iCENEXT = iCENNAM + ZipConstants.CENEXT(bArr, i2);
            while (iCENNAM + 4 < iCENEXT) {
                int iSH = ZipConstants.SH(bArr, iCENNAM);
                int iSH2 = ZipConstants.SH(bArr, iCENNAM + 2);
                if (iSH != 1) {
                    iCENNAM += 4 + iSH2;
                } else {
                    int i3 = iCENNAM + 4;
                    if (ZipConstants.CENLEN(bArr, i2) == 4294967295L) {
                        i3 += 8;
                    }
                    if (ZipConstants.CENSIZ(bArr, i2) == 4294967295L) {
                        i3 += 8;
                    }
                    return ZipConstants.LL(bArr, i3);
                }
            }
        }
        return jCENOFF;
    }

    static void printExtra(byte[] bArr, int i2, int i3) {
        int i4 = i2 + i3;
        while (i2 + 4 <= i4) {
            int iSH = ZipConstants.SH(bArr, i2);
            int iSH2 = ZipConstants.SH(bArr, i2 + 2);
            print("        [tag=0x%04x, sz=%d, data= ", Integer.valueOf(iSH), Integer.valueOf(iSH2));
            if (i2 + iSH2 > i4) {
                print("    Error: Invalid extra data, beyond extra length", new Object[0]);
                return;
            }
            int i5 = i2 + 4;
            for (int i6 = 0; i6 < iSH2; i6++) {
                print("%02x ", Byte.valueOf(bArr[i5 + i6]));
            }
            print("]%n", new Object[0]);
            switch (iSH) {
                case 1:
                    print("         ->ZIP64: ", new Object[0]);
                    for (int i7 = i5; i7 + 8 <= i5 + iSH2; i7 += 8) {
                        print(" *0x%x ", Long.valueOf(ZipConstants.LL(bArr, i7)));
                    }
                    print("%n", new Object[0]);
                    break;
                case 10:
                    print("         ->PKWare NTFS%n", new Object[0]);
                    if (ZipConstants.SH(bArr, i5 + 4) != 1 || ZipConstants.SH(bArr, i5 + 6) != 24) {
                        print("    Error: Invalid NTFS sub-tag or subsz", new Object[0]);
                    }
                    print("            mtime:%tc%n", Long.valueOf(ZipUtils.winToJavaTime(ZipConstants.LL(bArr, i5 + 8))));
                    print("            atime:%tc%n", Long.valueOf(ZipUtils.winToJavaTime(ZipConstants.LL(bArr, i5 + 16))));
                    print("            ctime:%tc%n", Long.valueOf(ZipUtils.winToJavaTime(ZipConstants.LL(bArr, i5 + 24))));
                    break;
                case 21589:
                    print("         ->Info-ZIP Extended Timestamp: flag=%x%n", Byte.valueOf(bArr[i5]));
                    for (int i8 = i5 + 1; i8 + 4 <= i5 + iSH2; i8 += 4) {
                        print("            *%tc%n", Long.valueOf(ZipUtils.unixToJavaTime(ZipConstants.LG(bArr, i8))));
                    }
                    break;
                default:
                    print("         ->[tag=%x, size=%d]%n", Integer.valueOf(iSH), Integer.valueOf(iSH2));
                    break;
            }
            i2 = i5 + iSH2;
        }
    }
}
