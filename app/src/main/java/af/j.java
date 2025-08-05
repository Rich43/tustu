package af;

import G.C0123f;
import G.J;
import G.bS;
import ad.C0493a;
import ad.C0494b;
import ad.C0495c;
import ad.C0496d;
import ae.C0500d;
import ae.p;
import ae.q;
import ae.u;
import ae.v;
import bH.C;
import bH.C0995c;
import bH.W;
import com.sun.imageio.plugins.jpeg.JPEG;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:af/j.class */
public class j implements InterfaceC0504a {

    /* renamed from: d, reason: collision with root package name */
    static C0123f f4484d = new C0123f();

    /* renamed from: e, reason: collision with root package name */
    private static byte f4485e = 0;

    public static C0500d a(p pVar) {
        C0500d c0500d = new C0500d();
        byte[] bArr = {13};
        for (int i2 = 0; i2 < 10; i2++) {
            byte[] bArrA = null;
            try {
                bArrA = pVar.a(bArr, 3);
            } catch (v e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Unexpected Protocol Error", (Throwable) e2);
                c0500d.a(C0500d.f4347b);
                c0500d.a("Unexpected Protocol Error");
            }
            if (bArrA == null || bArrA.length < 3) {
                C.c("wakeup s12 failed.");
                c0500d.a(C0500d.f4347b);
            } else {
                int iA = C0995c.a(bArrA[0]);
                int iA2 = C0995c.a(bArrA[1]);
                int iA3 = C0995c.a(bArrA[2]);
                switch (iA) {
                    case 224:
                    case 225:
                        break;
                    default:
                        C.c("Error code 0x" + Integer.toHexString(iA));
                        break;
                }
                switch (iA2) {
                    case 0:
                        break;
                    default:
                        C.c("Status code 0x" + Integer.toHexString(iA2));
                        break;
                }
                if (iA3 == 62) {
                    C.c("Got Prompt, continuing...");
                    c0500d.a(C0500d.f4346a);
                    return c0500d;
                }
                C.c("Prompt was 0x" + Integer.toHexString(iA3));
                if (iA == 0 && iA2 == 1 && (iA3 & 128) == 128) {
                    C.c("Looks like this was really new serial... Try again.  Aborting.");
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Attempt to wake up using old serial, but looks like controller is running new serial");
                    c0500d.a(false);
                    return c0500d;
                }
            }
            a(250);
        }
        c0500d.a(C0500d.f4347b);
        c0500d.a("Could not wake up processor.");
        return c0500d;
    }

    protected static void a(int i2) {
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    protected static int a(byte[] bArr) {
        if (bArr == null || bArr.length < 3) {
            return 255;
        }
        int i2 = 0 + 1;
        int iA = C0995c.a(bArr[0]);
        if (iA == 236 && J.I()) {
            int i3 = i2 + 1;
            C.c("\nAdditional bytes FSTAT=0x" + Integer.toHexString(C0995c.a(bArr[i2])));
            i2 = i3 + 1;
            C.c("FERSTAT=0x" + Integer.toHexString(C0995c.a(bArr[i3])) + "\nRX ");
        }
        int i4 = i2;
        int i5 = i2 + 1;
        int iA2 = C0995c.a(bArr[i4]);
        if (J.I()) {
            C.c("Response status code: " + Integer.toHexString(iA2));
        }
        switch (iA2) {
            case 0:
                break;
            case 1:
                C.a("Program running, download failed, statusCode=0x" + Integer.toHexString(iA2));
                break;
            case 2:
                C.a("Program halted, download failed, statusCode=0x" + Integer.toHexString(iA2));
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
            default:
                C.a("Unknown status received, statusCode=0x" + Integer.toHexString(iA2));
                break;
            case 4:
                C.a("Program trace detected, download failed, statusCode=0x" + Integer.toHexString(iA2));
                break;
            case 8:
                C.d("Cold reset detected, statusCode=0x" + Integer.toHexString(iA2));
                break;
            case 12:
                C.d("Warm reset detected, statusCode=0x" + Integer.toHexString(iA2));
                break;
        }
        int i6 = i5 + 1;
        byte b2 = bArr[i5];
        if (iA != 224) {
            if (J.I()) {
                C.c(b(iA));
            }
            if (iA == 0) {
                iA = 255;
            }
            return iA;
        }
        if (iA2 != 0 && J.I()) {
            C.c("Status code 0x" + Integer.toHexString(iA2));
        }
        if (b2 == 62 || !J.I()) {
            return 0;
        }
        C.c("Prompt was " + ((char) b2));
        return 0;
    }

    private static String b(int i2) {
        String str;
        switch (i2) {
            case 224:
                str = "Successful";
                break;
            case 225:
                str = "Command not recognized, e=0x" + Integer.toHexString(i2);
                break;
            case 226:
                str = "Command not allowed in run mode, e=0x" + Integer.toHexString(i2);
                break;
            case 227:
                str = "Stack pointer out of range, e=0x" + Integer.toHexString(i2);
                break;
            case 228:
                str = "Stack pointer invalid, e=0x" + Integer.toHexString(i2);
                break;
            case 229:
                str = "Attempt to write read-only memory, e=0x" + Integer.toHexString(i2);
                break;
            case 230:
                str = "Access error when writing FLASH/EEPROM memory, e=0x" + Integer.toHexString(i2);
                break;
            case 231:
            case JPEG.APP8 /* 232 */:
            default:
                str = "Error code 0x" + Integer.toHexString(i2);
                break;
            case 233:
                str = "Memory protection violation writing EEPROM memory, e=0x" + Integer.toHexString(i2);
                break;
        }
        return str;
    }

    public static C0500d a(p pVar, boolean z2) throws IOException {
        C0500d c0500d = new C0500d();
        if (J.I()) {
            C.c("Sending jumperless flash command");
        }
        if (J.I()) {
            C.c("TX: !!!SafetyFirst");
        }
        byte[] bArrA = pVar.a(z2 ? f4434a : f4436c, 1, 250);
        if (bArrA != null && bArrA.length >= 1 && bArrA[0] >= 0) {
            a(250);
            return c0500d;
        }
        C.c("Error: enter_boot_mode(): Error sending SafetyFirst command");
        c0500d.a(C0500d.f4347b);
        c0500d.a("Failed to enter bootload mode.");
        return c0500d;
    }

    protected static boolean a(p pVar, q qVar) throws IOException {
        int i2;
        byte[] bArrB;
        byte[] bArr = {70};
        int i3 = 0;
        do {
            try {
                bArrB = pVar.b(bArr, 3);
            } catch (IOException e2) {
            }
            if (bArrB == null || bArrB.length == 0) {
                C.c("No reply, assuming old style serial.\n");
                return false;
            }
            if (bArrB[0] == 48 && bArrB[1] == 48 && bArrB[2] >= 49) {
                C.c("New serial format >= 001 detected\n");
                return true;
            }
            if (bArrB[0] == 48 && bArrB[1] == 48 && bArrB[2] == 48) {
                C.c("Old style serial detected\n");
                return false;
            }
            if (bArrB[0] == 0 && bArrB[1] == 0 && bArrB[2] == 0) {
                C.c("No reply, assuming old style serial.\n");
                return false;
            }
            C.c("Garbled reply, " + C0995c.d(bArrB));
            i2 = i3;
            i3++;
        } while (i2 < 3);
        throw new IOException("Unable to detect Serial Protocol.");
    }

    public static boolean b(p pVar) throws IOException {
        int i2;
        byte[] bArrB;
        byte[] bArr = {13};
        int i3 = 0;
        do {
            try {
                bArrB = pVar.b(bArr, 3);
            } catch (v e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Protocol Error??? Should not happen here.", (Throwable) e2);
            } catch (IOException e3) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Can not communicate with controller", (Throwable) e3);
            }
            if (bArrB == null || bArrB.length == 0) {
                C.c("No reply, assuming old style serial, not in bootload mode");
                return false;
            }
            if (bArrB[2] == Byte.MIN_VALUE) {
                C.c("Looks like new serial, not in bootload mode.");
                return false;
            }
            if (bArrB[2] == 62) {
                C.c("Got a prompt, seems to be bootload");
                return true;
            }
            C.c("Garbled reply, " + C0995c.d(bArrB));
            i2 = i3;
            i3++;
        } while (i2 < 3);
        throw new IOException("Unable to detect if in bootload.");
    }

    public static C0500d c(p pVar) {
        C0500d c0500d = new C0500d();
        byte[] bArr = {-74};
        if (J.I()) {
            C.c("Erasing main flash!");
        }
        a(250);
        try {
            if (a(pVar.a(bArr, 3, 1500)) != 0) {
                C.c("Erase_s12 got an error code!\n\nReflashing failed - please try again.\n");
                c0500d.a(C0500d.f4347b);
                c0500d.a("Erase_s12 got an error code! Reflashing failed - please try again.");
                return c0500d;
            }
            C.c("Erased s12.");
            c0500d.a(C0500d.f4346a);
            c0500d.a("Erase Successful");
            return c0500d;
        } catch (v e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            c0500d.a(C0500d.f4347b);
            c0500d.a("Unexpected Protocol Error");
            return c0500d;
        }
    }

    public static C0500d a(p pVar, List list, u uVar, e eVar) {
        new C0500d();
        byte[] bArr = {-62};
        C0500d c0500dD = d(pVar);
        if (c0500dD.a() == C0500d.f4347b) {
            return c0500dD;
        }
        bArr[0] = -74;
        if (J.I()) {
            C.c("Erasing main flash!");
        }
        try {
            uVar.a(0.15d);
            if (a(pVar.a(bArr, 3, 10000)) != 0) {
                C.c("Erase_s12x got an error code!\n\nReflashing failed - please try again.\n");
                c0500dD.a(C0500d.f4347b);
                c0500dD.a("Erase_s12x got an error code! Reflashing failed - please try again.");
                return c0500dD;
            }
            if (list.isEmpty()) {
                uVar.a(0.95d);
            } else {
                uVar.a(0.7d);
            }
            float size = list.size();
            float f2 = 0.0f;
            Iterator it = list.iterator();
            while (it.hasNext()) {
                l lVar = (l) it.next();
                c0500dD = a(pVar, lVar);
                if (c0500dD.a() != C0500d.f4346a) {
                    uVar.a("Erase 1K failed, Eraser Required");
                    C.d("Erase 1K Failed, assuming not supported by monitor. Loading Eraser.");
                    uVar.b("Your MS3 requires a full erase.\n Please remove boot-jumper if fitted.");
                    return a(pVar, eVar, uVar);
                }
                C.d("Erased page 1k at a time: 0x" + Integer.toHexString(lVar.a()));
                f2 += 1.0f;
                uVar.a(0.699999988079071d + ((0.3d * f2) / size));
            }
            c0500dD.a(C0500d.f4346a);
            c0500dD.a("Erase Successful");
            return c0500dD;
        } catch (v e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            c0500dD.a(C0500d.f4347b);
            c0500dD.a("Unexpected Protocol Error");
            return c0500dD;
        }
    }

    public static C0500d a(p pVar, l lVar) {
        C0500d c0500dD = d(pVar);
        if (c0500dD.a() == C0500d.f4347b) {
            return c0500dD;
        }
        int iB = lVar.b();
        while (true) {
            int i2 = iB;
            if (i2 >= lVar.c()) {
                return c0500dD;
            }
            int iA = (lVar.a() << 16) + i2;
            c0500dD = a(pVar, iA);
            if (c0500dD.a() == C0500d.f4347b) {
                return c0500dD;
            }
            if (J.I()) {
                C.d("Erased Sector: 0x00" + Integer.toHexString(iA).toUpperCase());
            }
            iB = i2 + 1024;
        }
    }

    public static C0500d a(p pVar, int i2) {
        a(250);
        C0500d c0500d = new C0500d();
        try {
            if (a(pVar.a(new byte[]{-60, (byte) (255 & (i2 >>> 16)), (byte) (255 & (i2 >>> 8)), (byte) (255 & i2)}, 3, 1500)) == 0) {
                return c0500d;
            }
            C.a("erase1K12x(): Error with C_ERASE_1K command.\n");
            c0500d.a(C0500d.f4347b);
            c0500d.a("Erase 1k failed, assumed monitor doesn't support.");
            return c0500d;
        } catch (v e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Protocol Exception?? doesn't appear to be in bootload mode.", (Throwable) e2);
            c0500d.a(C0500d.f4347b);
            c0500d.a("Unexpected Protocol Error");
            return c0500d;
        }
    }

    public static C0500d a(p pVar, e eVar, u uVar) {
        C0500d c0500d;
        C0500d c0500d2 = new C0500d();
        try {
            C0493a c0493aA = new C0494b().a(new ByteArrayInputStream("S00D00006572617365722E73313965\nS2147FC000CF3FFF16C1B9CEC31ECD2800CC00002778\nS2147FC01007180A30700434F9CC000A2708CE2800A7\nS2147FC02069300434FB16C0A916C31820FBC006C0AF\nS2147FC030187B00CF180B0800CBCE00CCE6002AFC7E\nS2147FC0403D1B9F6B8054545454C1092204CB30202F\nS2147FC05002CB3707DCE680C40FC1092204CB302031\nS2147FC06002CB3707CC1B813D1801AE28067C2806FD\nS2147FC07087F6280607CBF6280707C61805B12806D1\nS2147FC0803D1B9F6B807900C8180B1B00C9C63A07FB\nS2147FC090A0C630079CC6780798E68007A4C6200708\nS2147FC0A090F60106079B1B813D1C000CFC180BC0FD\nS2147FC0B0001C1D001E401D000810CE003A0D0040DB\nS2147FC0C0180BD80034180B0300357900360C004067\nS2147FC0D0CE0037E60087C40827F9180B800039871B\nS2147FC0E0C7C300018C00772FF8180B1F0017FCFEC4\nS2147FC0F0FE8C0304220C180C080F0272180C081012\nS2147FC100027379000679000779000D180B300264F8\nS2147FC110790265180B80026C79025C180BFF024C63\nS2147FC120CE0100E6002B0418080007CE010618088B\nS2147FC130003079004879004979004A79004B7900C8\nS2147FC14040180BFF006C790102180B14010A790165\nS2147FC1500B1808008016C20C878C00012F0316C0B0\nS2147FC16081180B523FF0180B753FF1180B6E3FF29C\nS2147FC170180B4D3FF3180B6F3FF4180B6E3FF518F7\nS2147FC1800B693FF6180B743FF7180B6F3FF8180BC9\nS2147FC190723FF9180B41003C20FE1801AE280018AC\nS2147FC1A001AE28021801AE28041805B12804180528\nS2147FC1B0B128021805B128000B3D4D533320466F3A\nS2147FC1C0726D617420303030302E303020004D5309\nS2147FC1D033206669726D77617265206572617365FB\nS2147FC1E07220202032303136303231372031363AA5\nS2147FC1F03031474D5420286329204A534D2F4B43D7\nS2147FC200202A2A2A2A2A2A2A2A00A7A7180B300198\nS2147FC21006180B780010188718ED0002263A0808D3\nS2147FC22026F6180B790010188718ED0002262908C5\nS2147FC2300826F6180B7A0010188718ED00022618C5\nS2147FC240080826F6180B7B0010188718ED000226C4\nS2147FC25007080826F606C2EB1887E6E2C3086BE2F5\nS2147FC2603000088E000D26F2B60106858026071858\nS2147FC270C7A7A70226FB180B300106B60106853036\nS2147FC280267585802779180B000102180B09010A8D\nS2147FC290180B78010B180B01010218030000010A26\nS2147FC2A0163000B6010685302645180B3001061875\nS2147FC2B00B300106B6010685302640858027441858\nS2147FC2C00B000102180B09010A180B7A010B180BD9\nS2147FC2D001010218030000010A163000B601068528\nS2147FC2E0302610180B300106C7201CC6012018C642\nS2147FC2F0022014C6032010C604200CC6052008C6DC\nS2147FC300062004C60720003D180B800106A71F01E4\nS2127FC3100680FB3DA700000010EF3E20FBA737\nS2147FEF10C19AC19AC19AC19AC19AC19AC19AC19A95\nS2147FEF20C19AC19AC19AC19AC19AC19AC19AC19A85\nS2147FEF30C19AC19AC19AC19AC19AC19AC19AC19A75\nS2147FEF40C19AC19AC19AC19AC19AC19AC19AC19A65\nS2147FEF50C19AC19AC19AC19AC19AC19AC19AC19A55\nS2147FEF60C19AC19AC19AC19AC19AC19AC19AC19A45\nS2147FEF70C19AC19AC19AC19AC19AC19AC19AC19A35\nS2147FEF80C19AC19AC19AC19AC19AC19AC19AC19A25\nS2147FEF90C19AC19AC19AC19AC19AC19AC19AC19A15\nS2147FEFA0C19AC19AC19AC19AC19AC19AC19AC19A05\nS2147FEFB0C19AC19AC19AC19AC19AC19AC19AC19AF5\nS2147FEFC0C19AC19AC19AC19AC19AC19AC19AC19AE5\nS2147FEFD0C19AC19AC19AC19AC19AC19AC19AC19AD5\nS2147FEFE0C19AC19AC19AC19AC19AC19AC19AC19AC5\nS2147FEFF0C19AC19AC19AC19AC19AC19AC19AC00050\nS80400C0003B".getBytes()));
            C0500d c0500dA = a(pVar);
            if (c0500dA.a() == C0500d.f4347b) {
                return c0500dA;
            }
            if (uVar != null) {
                try {
                    uVar.a("Loading Eraser for older monitors.");
                } catch (v e2) {
                    Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    c0500d = new C0500d();
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Failed to load Eraser!");
                }
            }
            c0500d = b(c0493aA, pVar, uVar, null);
            if (uVar != null) {
                uVar.a("Eraser Loaded and running, Please wait..");
            }
            if (c0500d.a() != C0500d.f4346a) {
                return c0500d;
            }
            if (eVar != null && eVar.b() && eVar.b(pVar, null).a() != C0500d.f4346a) {
                C.b("Could not set Port states after Eraser.");
            }
            e(pVar);
            a(2000);
            byte[] bArr = new byte[50];
            InputStream inputStreamI = pVar.a().i();
            try {
                for (int i2 = inputStreamI.read(bArr); i2 > 0; i2 = inputStreamI.read(bArr)) {
                    C.c("Cleared random Eraser 0's: " + i2);
                    a(5);
                }
            } catch (Exception e3) {
                C.c("Handled read exception: " + e3.getMessage());
            }
            a(pVar);
            return a(pVar, new ArrayList(), uVar, eVar);
        } catch (C0495c e4) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Failed to parse eraser", (Throwable) e4);
            c0500d2.a(C0500d.f4347b);
            c0500d2.a("Failed to parse bootstrap");
            return c0500d2;
        }
    }

    public static C0500d d(p pVar) {
        C0500d c0500d = null;
        byte[] bArr = {-62};
        if (J.I()) {
            C.c("Checking flash status.");
        }
        try {
            if (a(pVar.a(bArr, 3, 1500)) == 0) {
                C.c("Status ok.");
                return new C0500d();
            }
            C.a("erase_s12x(): error with command C_QUERY.\n");
            c0500d.a(C0500d.f4347b);
            c0500d.a("Aborting - bad status erase_S12() 1.");
            return null;
        } catch (v e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Protocol Exception?? doesn't appear to be in bootload mode.", (Throwable) e2);
            c0500d.a(C0500d.f4347b);
            c0500d.a("Unexpected Protocol Error");
            return null;
        }
    }

    public static C0500d a(C0493a c0493a, p pVar, u uVar, o oVar) {
        C0500d c0500d = new C0500d();
        uVar.a(0.0d);
        List<C0496d> listB = c0493a.b();
        double size = listB.size();
        int i2 = 0;
        for (C0496d c0496d : listB) {
            if (oVar == null || oVar.a(c0496d)) {
                int[] iArrC = c0496d.c();
                int[] iArrE = c0496d.e();
                int iA = -1;
                for (int i3 = 0; i3 < 3 && iA != 0; i3++) {
                    iA = a(pVar, iArrC, iArrE);
                    if (iA != 0 && i3 == 2) {
                        c0500d.a(C0500d.f4347b);
                        c0500d.a("Error reported.\n" + b(iA));
                        C.a("Error reported on record " + i2 + " of " + size);
                        uVar.a(1.0d);
                        uVar.a("Error reported, try again with boot jumper.");
                        return c0500d;
                    }
                    if (iA != 0) {
                        C.a("Error reported on record " + i2 + " of " + size + "\n" + b(C0995c.a((byte) iA)) + "\nRetrying....");
                        a(250);
                    }
                }
            }
            int i4 = i2;
            i2++;
            uVar.a(i4 / size);
        }
        return c0500d;
    }

    public static C0500d b(C0493a c0493a, p pVar, u uVar, o oVar) {
        C0500d c0500d = new C0500d();
        uVar.a(0.0d);
        List<C0496d> listB = c0493a.b();
        double size = listB.size();
        int i2 = 0;
        for (C0496d c0496d : listB) {
            if (oVar == null || oVar.a(c0496d)) {
                int[] iArrC = c0496d.c();
                int[] iArrE = c0496d.e();
                int iB = -1;
                for (int i3 = 0; i3 < 3 && iB != 0; i3++) {
                    iB = b(pVar, iArrC, iArrE);
                    if (iB != 0 && i3 == 2) {
                        c0500d.a(C0500d.f4347b);
                        c0500d.a("Error reported.\n" + b(iB));
                        C.a("Error reported on record " + i2 + " of " + size);
                        uVar.a(1.0d);
                        uVar.a("Error reported, try again with boot jumper.");
                        return c0500d;
                    }
                    if (iB != 0) {
                        C.a("Error reported on record " + i2 + " of " + size + "\n" + b(C0995c.a((byte) iB)) + "\nRetrying....");
                        a(250);
                    }
                }
                int i4 = i2;
                i2++;
                uVar.a(i4 / size);
            } else if (J.I()) {
                C.d("Skipping record: " + i2);
            }
        }
        return c0500d;
    }

    public static int a(p pVar, byte[] bArr, byte[] bArr2) {
        return a(pVar, C0995c.b(bArr), C0995c.b(bArr2));
    }

    public static int a(p pVar, int[] iArr, int[] iArr2) {
        if (a(pVar, C0995c.b(iArr, 0, iArr.length, true, false), true).a() == C0500d.f4347b) {
            return 1;
        }
        if (iArr.length > 2) {
            iArr = new int[]{iArr[iArr.length - 2], iArr[iArr.length - 1]};
        }
        byte[] bArrB = f4484d.b(1 + iArr.length + 1);
        int i2 = 0 + 1;
        bArrB[0] = -88;
        for (int i3 : iArr) {
            int i4 = i2;
            i2++;
            bArrB[i4] = (byte) i3;
        }
        int i5 = i2;
        int i6 = i2 + 1;
        bArrB[i5] = (byte) (iArr2.length - 1);
        pVar.b(bArrB, 0);
        byte[] bArrB2 = pVar.b(C0995c.a(iArr2), 3);
        f4484d.a(bArrB);
        return a(bArrB2);
    }

    public static int b(p pVar, int[] iArr, int[] iArr2) {
        byte[] bArrB = f4484d.b(1 + iArr.length + 1);
        int i2 = 0 + 1;
        bArrB[0] = -63;
        for (int i3 : iArr) {
            int i4 = i2;
            i2++;
            bArrB[i4] = (byte) i3;
        }
        int i5 = i2;
        int i6 = i2 + 1;
        bArrB[i5] = (byte) (iArr2.length - 1);
        pVar.b(bArrB, 0);
        byte[] bArrB2 = pVar.b(C0995c.a(iArr2), 3);
        f4484d.a(bArrB);
        return a(bArrB2);
    }

    public static void a() {
        f4485e = (byte) 0;
    }

    public static C0500d a(p pVar, int i2, boolean z2) throws IOException {
        C0500d c0500d = new C0500d();
        byte[] bArr = new byte[4];
        int i3 = 65535 & (i2 >> 16);
        if (i3 == f4485e) {
            c0500d.a("page change not required");
            c0500d.a(C0500d.f4346a);
            return c0500d;
        }
        try {
            f4485e = (byte) (i3 & 255);
            if (J.I()) {
                C.c("Setting page to " + Integer.toHexString(f4485e));
            }
            bArr[0] = -94;
            bArr[1] = 0;
            bArr[2] = 48;
            bArr[3] = f4485e;
            byte[] bArrA = pVar.a(bArr, 3);
            if (bArrA == null || bArrA.length < 3) {
                C.a("sendPPAGE() failed on command C_WRITE_BYTE");
                c0500d.a(C0500d.f4347b);
                c0500d.a("sendPPAGE() failed on command C_WRITE_BYTE: Page:" + Integer.toHexString(f4485e));
                return c0500d;
            }
            if (a(bArrA) != 0) {
                C.a("sendPPAGE Got an error code!\nLikely the wrong monitor was running.\nReflashing failed - please try again. page=" + Integer.toHexString(f4485e) + ", response:" + C0995c.d(bArrA));
                c0500d.a(C0500d.f4347b);
                c0500d.a("sendPPage failed, bad status. Try again.");
                return c0500d;
            }
            byte[] bArr2 = {-72};
            if (z2) {
                C.c("erasing page 0x:" + Integer.toHexString(f4485e));
                byte[] bArrA2 = pVar.a(bArr2, 3, 1500);
                if (bArrA2 == null || bArrA2.length < 3) {
                    C.a("sendPPAGE() failed on command C_ERASE_PAGE, no response");
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("sendPPAGE() failed on command C_ERASE_PAGE, No response from controller. Try again.");
                    return c0500d;
                }
                if (a(bArrA2) != 0) {
                    c0500d.a(C0500d.f4347b);
                    c0500d.a("Erase page got an error code!\n\nReflashing failed - please try again.");
                    return c0500d;
                }
                C.c("Erased 0x" + Integer.toHexString(f4485e));
            }
            c0500d.a("page change successful 0x" + Integer.toHexString(f4485e));
            c0500d.a(C0500d.f4346a);
            return c0500d;
        } catch (v e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, "Unexpected Protocol Error", (Throwable) e2);
            c0500d.a(C0500d.f4347b);
            c0500d.a("Unexpected Protocol Error");
            return c0500d;
        }
    }

    public static void e(p pVar) {
        try {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
                Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            pVar.a(new byte[]{-76}, 0);
        } catch (v e3) {
            e3.printStackTrace();
        }
    }

    public static bS f(p pVar) throws IOException {
        byte[] bArrA = null;
        try {
            bArrA = pVar.a(new byte[]{81}, 20, 600);
        } catch (v e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Protocol Error querying signature", (Throwable) e2);
        }
        if (bArrA == null || bArrA.length == 0) {
            return null;
        }
        bS bSVar = new bS();
        bSVar.a(bArrA);
        try {
            bSVar.b(W.k(new String(pVar.a(new byte[]{83}, 20, 600))));
        } catch (Exception e3) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Protocol Error querying signature", (Throwable) e3);
        }
        return bSVar;
    }
}
