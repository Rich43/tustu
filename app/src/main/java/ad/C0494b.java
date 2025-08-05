package ad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ad.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ad/b.class */
public class C0494b {
    public C0493a a(File file) {
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                C0493a c0493aA = a(fileInputStream);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                        Logger.getLogger(C0494b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return c0493aA;
            } catch (FileNotFoundException e3) {
                Logger.getLogger(C0494b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new C0495c("File not found.\n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                    Logger.getLogger(C0494b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    throw th;
                }
            }
            throw th;
        }
    }

    public C0493a a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        C0493a c0493a = new C0493a();
        C0496d c0496dA = null;
        int i2 = 0;
        int iD = Integer.MAX_VALUE;
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    c0493a.a(iD);
                    break;
                }
                i2++;
                if ((line == null || line.isEmpty()) && c0496dA.a() != 9 && c0496dA.a() != 8 && c0496dA.a() != 7) {
                    throw new C0495c("Truncated s19 File, line: " + i2);
                }
                if (c0496dA == null || line == null || !(c0496dA.a() == 9 || c0496dA.a() == 8 || c0496dA.a() == 7)) {
                    try {
                        try {
                            c0496dA = a(line, i2);
                            if (c0496dA.a() == 1 || c0496dA.a() == 2 || c0496dA.a() == 3) {
                                if (c0496dA.d() < iD) {
                                    iD = c0496dA.d();
                                }
                                if (!a(line, c0496dA)) {
                                    throw new C0495c("Failed Checksum, line: " + i2);
                                }
                                c0493a.b(c0496dA);
                            } else if (c0496dA.a() == 0) {
                                c0493a.a(c0496dA);
                            }
                        } catch (C0495c e2) {
                            throw e2;
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        throw new C0495c("Unhandled exception, line: " + i2);
                    }
                } else {
                    c0493a.a(line);
                }
            } catch (IOException e4) {
                Logger.getLogger(C0494b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
        return c0493a;
    }

    private boolean a(String str, C0496d c0496d) {
        boolean z2 = false;
        byte b2 = c0496d.b();
        if (b2 == str.substring(4).length() / 2) {
            int i2 = b2;
            int length = c0496d.c().length;
            for (int i3 = 0; i3 < length; i3++) {
                i2 += c0496d.c()[i3];
            }
            for (int i4 = 0; i4 < (b2 - 1) - length; i4++) {
                i2 += c0496d.e()[i4];
            }
            if (c0496d.f() == ((i2 ^ (-1)) & 255)) {
                z2 = true;
            }
        }
        return z2;
    }

    public C0496d a(String str, int i2) throws C0495c {
        if (str == null) {
            throw new C0495c("Invalid Record, line: " + i2);
        }
        if (str.length() < 2) {
            throw new C0495c("Record Type Not Defined , line: " + i2);
        }
        C0496d c0496d = new C0496d();
        c0496d.b(i2);
        c0496d.a(Byte.parseByte(str.substring(1, 2), 16));
        int iA = a(c0496d.a());
        c0496d.b(Byte.parseByte(str.substring(2, 4), 16));
        String strSubstring = str.substring(4, 4 + iA);
        if (strSubstring.length() < iA) {
            throw new C0495c("Address out of range. To Short, line: " + i2);
        }
        if (strSubstring.length() > iA) {
            throw new C0495c("Address out of range. To Long, line: " + i2);
        }
        c0496d.a(a(strSubstring));
        c0496d.b(a(str.substring(4 + iA, str.length() - 2)));
        c0496d.a(Integer.parseInt(str.substring(str.length() - 2, str.length()), 16));
        return c0496d;
    }

    private int[] a(String str) {
        int i2 = 0;
        int i3 = 2;
        int[] iArr = new int[str.length() / 2];
        for (int i4 = 0; i4 < str.length() / 2; i4++) {
            iArr[i4] = Integer.parseInt(str.substring(i2, i3), 16);
            i2 += 2;
            i3 += 2;
        }
        return iArr;
    }

    private int a(byte b2) {
        int i2 = -1;
        if (b2 == 0 || b2 == 1 || b2 == 9) {
            i2 = 4;
        }
        if (b2 == 2 || b2 == 8) {
            i2 = 6;
        }
        if (b2 == 3 || b2 == 7) {
            i2 = 8;
        }
        return i2;
    }
}
