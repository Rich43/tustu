package com.efiAnalytics.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fF.class */
public class fF {
    public C1701s a(C1701s c1701s, String str) {
        C1701s c1701s2 = new C1701s();
        try {
            FileReader fileReader = new FileReader(new File(str));
            double d2 = 1.0d;
            double d3 = 0.0d;
            try {
                String strA = a(fileReader);
                while (strA != null) {
                    if (strA.startsWith("Scale")) {
                        d2 = Double.parseDouble(bH.W.b(strA, "Scale", "").trim());
                    } else if (strA.startsWith("Translate")) {
                        d3 = Double.parseDouble(bH.W.b(strA, "Translate", "").trim());
                    } else if (strA.startsWith("VE Table RPM")) {
                        String[] strArr = new String[Integer.parseInt(strA.substring(strA.indexOf("[") + 1, strA.indexOf("]")).trim())];
                        for (int i2 = 0; i2 < strArr.length; i2++) {
                            String strA2 = a(fileReader);
                            int i3 = Integer.parseInt(strA2.substring(strA2.indexOf("=") + 1).trim());
                            strArr[i2] = (i3 * 100) + "";
                            if (i2 > 0 && i3 * 100 < Integer.parseInt(strArr[i2 - 1])) {
                                throw new V.a("Invalid RPM: \n" + (i3 * 100) + " less than " + strArr[i2 - 1] + "\nRecheck your Vex file.");
                            }
                        }
                        c1701s2.e("");
                        c1701s2.c(strArr);
                    } else if (strA.startsWith("VE Table Load Range")) {
                        String strSubstring = strA.substring(strA.indexOf("[") + 1, strA.indexOf("]"));
                        c1701s2.d(strA.substring(strA.indexOf("(") + 1, strA.indexOf(")")));
                        String[] strArr2 = new String[Integer.parseInt(strSubstring.trim())];
                        for (int i4 = 0; i4 < strArr2.length; i4++) {
                            String strA3 = a(fileReader);
                            int i5 = Integer.parseInt(strA3.substring(strA3.indexOf("=") + 1).trim());
                            strArr2[i4] = i5 + "";
                            if (i4 > 0 && i5 < Integer.parseInt(strArr2[i4 - 1])) {
                                throw new V.a("Invalid Y Axis value. \n" + i5 + " less than " + strArr2[i4 + 1] + "\nRecheck your Vex file.");
                            }
                        }
                        c1701s2.d(strArr2);
                    } else if (strA.startsWith("VE Table")) {
                        int i6 = Integer.parseInt(strA.substring(strA.indexOf("[") + 1, strA.indexOf("]")).trim());
                        int i7 = Integer.parseInt(strA.substring(strA.lastIndexOf("[") + 1, strA.lastIndexOf("]")).trim());
                        c1701s2.a(i7, i6);
                        a(fileReader);
                        for (int i8 = 0; i8 < i7; i8++) {
                            String strA4 = a(fileReader);
                            StringTokenizer stringTokenizer = new StringTokenizer(strA4.substring(strA4.indexOf("=") + 1), " ");
                            for (int i9 = 0; i9 < i6; i9++) {
                                c1701s2.a((Object) Double.valueOf((Integer.parseInt(stringTokenizer.nextToken().trim()) + d3) * d2), i8, i9);
                            }
                        }
                    }
                    strA = a(fileReader);
                }
                return C1677fh.a(c1701s2, c1701s);
            } catch (V.a e2) {
                throw e2;
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new V.a("Corrupt Vex!\nUnable to read file:" + str);
            }
        } catch (FileNotFoundException e4) {
            throw new V.a("File not found:\n" + str);
        }
    }

    private String a(FileReader fileReader) {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = fileReader.read();
        while (true) {
            int i3 = i2;
            if (i3 == 10) {
                return stringBuffer.toString();
            }
            if (i3 == -1) {
                return null;
            }
            char c2 = (char) i3;
            if (c2 == '\n') {
                return stringBuffer.toString();
            }
            stringBuffer.append(c2);
            i2 = fileReader.read();
        }
    }

    public void a(C1701s c1701s, String str, int i2) {
        a(c1701s, str, Math.pow(10.0d, -i2), 0.0d);
    }

    public void a(C1701s c1701s, String str, double d2, double d3) throws V.a {
        try {
            FileWriter fileWriter = new FileWriter(new File(str));
            fileWriter.write("EVEME 1.0\n");
            fileWriter.write("UserRev: 1.00\n");
            fileWriter.write("UserComment: Exported by EFI Analytics\n");
            Calendar calendar = Calendar.getInstance();
            fileWriter.write("Date: " + calendar.get(2) + LanguageTag.SEP + calendar.get(5) + LanguageTag.SEP + calendar.get(1) + "\n");
            fileWriter.write("Time: " + calendar.get(11) + CallSiteDescriptor.TOKEN_DELIMITER + calendar.get(12) + "\n");
            fileWriter.write("Page 0\n");
            if (d2 > 0.0d) {
                fileWriter.write("Scale " + d2 + "\n");
            }
            a(fileWriter, "VE Table RPM Range", "", c1701s.b(), true);
            a(fileWriter, "VE Table Load Range", c1701s.v(), c1701s.a(), false);
            a(fileWriter, c1701s, d2);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e2) {
            throw new V.a("Unable to write " + str + "\n" + e2.getMessage());
        }
    }

    private void a(FileWriter fileWriter, String str, String str2, String[] strArr, boolean z2) {
        if (str2 != null && !str2.equals("")) {
            str = str + " (" + str2 + ")";
        }
        fileWriter.write((a(str, 32, true) + "[" + strArr.length + "]") + "\n");
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (z2) {
                fileWriter.write("   [" + a(i2 + "", 3, false) + "] =" + a(((int) Math.round(Double.parseDouble(strArr[i2]) / 100.0d)) + "", 4, false) + "\n");
            } else {
                fileWriter.write("   [" + a(i2 + "", 3, false) + "] =" + a(((int) Double.parseDouble(strArr[(strArr.length - i2) - 1])) + "", 4, false) + "\n");
            }
        }
    }

    private void a(FileWriter fileWriter, C1701s c1701s, double d2) {
        int length = c1701s.a().length;
        int length2 = c1701s.b().length;
        fileWriter.write((a("VE Table", 32, true) + "[" + a(length + "", 3, false) + "][" + a(length2 + "", 3, false) + "]") + "\n");
        fileWriter.write("          ");
        for (int i2 = 0; i2 < length; i2++) {
            fileWriter.write(" [" + a(i2 + "", 3, false) + "]");
        }
        fileWriter.write("\n");
        for (int i3 = 0; i3 < length2; i3++) {
            fileWriter.write("   [" + a(i3 + "", 3, false) + "] =");
            for (int i4 = 0; i4 < length; i4++) {
                fileWriter.write(a(((int) (c1701s.getValueAt((length2 - i3) - 1, i4).doubleValue() / d2)) + "", 5, false) + " ");
            }
            fileWriter.write("\n");
        }
    }

    private String a(String str, int i2, boolean z2) {
        while (str.length() < i2) {
            str = z2 ? str + " " : " " + str;
        }
        return str;
    }
}
