package org.icepdf.core.pobjects.fonts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/AFM.class */
public class AFM {
    public static final int COURIER = 0;
    public static final int COURIER_BOLD = 1;
    public static final int COURIER_OBLIQUE = 2;
    public static final int COURIER_BOLD_OBLIQUE = 3;
    public static final int HELVETICA = 4;
    public static final int HELVETICA_BOLD = 5;
    public static final int HELVETICA_OBLIQUE = 6;
    public static final int HELVETICA_BOLD_OBLIQUE = 7;
    public static final int TIMES_ROMAN = 8;
    public static final int TIMES_BOLD = 9;
    public static final int TIMES_ITALIC = 10;
    public static final int TIMES_BOLD_ITALIC = 11;
    public static final int ZAPF_DINGBATS = 12;
    public static final int SYMBOL = 13;
    private String fontName;
    private String familyName;
    private String fullName;
    private float[] widths = new float[255];
    private int[] fontBBox = new int[4];
    private float italicAngle = 0.0f;
    private float maxWidth = 0.0f;
    private int avgWidth = 0;
    private int flags = 0;
    private static final Logger logger = Logger.getLogger(AFM.class.toString());
    public static String[] AFMnames = {"Courier.afm", "Courier-Bold.afm", "Courier-Oblique.afm", "Courier-BoldOblique.afm", "Helvetica.afm", "Helvetica-Bold.afm", "Helvetica-Oblique.afm", "Helvetica-BoldOblique.afm", "Times-Roman.afm", "Times-Bold.afm", "Times-Italic.afm", "Times-BoldItalic.afm", "ZapfDingbats.afm", "Symbol.afm"};
    private static int[] AFMFlags = {35, 35, 99, 99, 32, 32, 96, 96, 34, 34, 98, 98, 4, 4};
    public static HashMap<String, AFM> AFMs = new HashMap<>(14);

    static {
        for (int i2 = 0; i2 < AFMnames.length; i2++) {
            try {
                AFM afm = loadFont("afm/" + AFMnames[i2]);
                if (afm != null) {
                    afm.setFlags(AFMFlags[i2]);
                    AFMs.put(afm.fontName.toLowerCase(), afm);
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Error load AFM CMap files", (Throwable) ex);
                return;
            }
        }
    }

    public static AFM loadFont(String resource) throws IOException, NumberFormatException {
        InputStream in = AFM.class.getResourceAsStream(resource);
        if (in != null) {
            AFM afm = new AFM();
            afm.parse(new InputStreamReader(in));
            return afm;
        }
        if (logger.isLoggable(Level.WARNING)) {
            logger.warning("Could not find AFM File: " + resource);
            return null;
        }
        return null;
    }

    private AFM() {
    }

    public String getFontName() {
        return this.fontName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public int[] getFontBBox() {
        return this.fontBBox;
    }

    public float getItalicAngle() {
        return this.italicAngle;
    }

    public float[] getWidths() {
        return this.widths;
    }

    public float getMaxWidth() {
        return this.maxWidth;
    }

    public int getAvgWidth() {
        return this.avgWidth;
    }

    public int getFlags() {
        return this.flags;
    }

    private void setFlags(int value) {
        this.flags = value;
    }

    private void parse(Reader i2) throws IOException, NumberFormatException {
        BufferedReader r2 = new BufferedReader(i2);
        int count = 0;
        this.avgWidth = 0;
        this.maxWidth = 0.0f;
        while (true) {
            String s2 = r2.readLine();
            if (s2 != null) {
                StringTokenizer st = new StringTokenizer(s2, " ;\t\n\r\f");
                String s1 = st.nextToken();
                if (s1.equalsIgnoreCase("FontName")) {
                    this.fontName = st.nextToken();
                } else if (s1.equalsIgnoreCase("FullName")) {
                    this.fullName = st.nextToken();
                } else if (s1.equalsIgnoreCase("FamilyName")) {
                    this.familyName = st.nextToken();
                } else if (s1.equalsIgnoreCase("FontBBox")) {
                    this.fontBBox[0] = new Integer(st.nextToken()).intValue();
                    this.fontBBox[1] = new Integer(st.nextToken()).intValue();
                    this.fontBBox[2] = new Integer(st.nextToken()).intValue();
                    this.fontBBox[3] = new Integer(st.nextToken()).intValue();
                } else if (s1.equalsIgnoreCase("ItalicAngle")) {
                    this.italicAngle = new Float(st.nextToken()).floatValue();
                } else if (s1.equalsIgnoreCase("C")) {
                    int c2 = Integer.parseInt(st.nextToken());
                    while (!st.nextToken().equals("WX")) {
                    }
                    float wx = Integer.parseInt(st.nextToken()) / 1000.0f;
                    if (c2 >= 0 && c2 < 255) {
                        this.widths[count] = wx;
                        if (wx > this.maxWidth) {
                            this.maxWidth = wx;
                        }
                        this.avgWidth = (int) (this.avgWidth + wx);
                        count++;
                    }
                }
            } else {
                this.avgWidth /= count;
                return;
            }
        }
    }
}
