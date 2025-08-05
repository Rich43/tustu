package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: jfxrt.jar:com/sun/javafx/font/AndroidFontFinder.class */
class AndroidFontFinder {
    private static final String SYSTEM_FONT_NAME = "sans serif";
    private static final float SYSTEM_FONT_SIZE = 16.0f;
    static final String fontDescriptor_2_X_Path = "/com/sun/javafx/font/android_system_fonts.xml";
    static final String fontDescriptor_4_X_Path = "/system/etc/system_fonts.xml";
    static final String systemFontsDir = "/system/fonts";

    AndroidFontFinder() {
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }

    public static String getSystemFont() {
        return SYSTEM_FONT_NAME;
    }

    public static float getSystemFontSize() {
        return SYSTEM_FONT_SIZE;
    }

    public static String getSystemFontsDir() {
        return systemFontsDir;
    }

    private static boolean parse_2_X_SystemDefaultFonts(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap) {
        InputStream is = AndroidFontFinder.class.getResourceAsStream(fontDescriptor_2_X_Path);
        if (is == null) {
            System.err.println("Resource not found: /com/sun/javafx/font/android_system_fonts.xml");
            return false;
        }
        return parseSystemDefaultFonts(is, fontToFileMap, fontToFamilyNameMap, familyToFontListMap);
    }

    private static boolean parse_4_X_SystemDefaultFonts(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap) {
        File iFile = new File(fontDescriptor_4_X_Path);
        try {
            return parseSystemDefaultFonts(new FileInputStream(iFile), fontToFileMap, fontToFamilyNameMap, familyToFontListMap);
        } catch (FileNotFoundException e2) {
            System.err.println("File not found: /system/etc/system_fonts.xml");
            return false;
        }
    }

    private static boolean parseSystemDefaultFonts(InputStream is, final HashMap<String, String> fontToFileMap, final HashMap<String, String> fontToFamilyNameMap, final HashMap<String, ArrayList<String>> familyToFontListMap) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() { // from class: com.sun.javafx.font.AndroidFontFinder.1
                private static final char DASH = '-';
                private static final String FAMILY = "family";
                private static final String FILE = "file";
                private static final String FILESET = "fileset";
                private static final String NAME = "name";
                private static final String NAMESET = "nameset";
                private static final char SPACE = ' ';
                final List<String> filesets = new ArrayList();
                boolean inFamily = false;
                boolean inFile = false;
                boolean inFileset = false;
                boolean inName = false;
                boolean inNameset = false;
                private final List<String> namesets = new ArrayList();
                private final String[] styles = {"regular", "bold", "italic", "bold italic"};

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (this.inName) {
                        String nameset = new String(ch, start, length).toLowerCase();
                        this.namesets.add(nameset);
                    } else if (this.inFile) {
                        String fileset = new String(ch, start, length);
                        this.filesets.add(fileset);
                    }
                }

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase(FAMILY)) {
                        for (String family : this.namesets) {
                            int i2 = 0;
                            String familyName = family.replace('-', ' ');
                            for (String file : this.filesets) {
                                String fullName = familyName + " " + this.styles[i2];
                                String fullFile = AndroidFontFinder.systemFontsDir + File.separator + file;
                                File f2 = new File(fullFile);
                                if (f2.exists() && f2.canRead()) {
                                    fontToFileMap.put(fullName, fullFile);
                                    fontToFamilyNameMap.put(fullName, familyName);
                                    ArrayList<String> list = (ArrayList) familyToFontListMap.get(familyName);
                                    if (list == null) {
                                        list = new ArrayList<>();
                                        familyToFontListMap.put(familyName, list);
                                    }
                                    list.add(fullName);
                                    i2++;
                                }
                            }
                        }
                        this.inFamily = false;
                        return;
                    }
                    if (qName.equalsIgnoreCase(NAMESET)) {
                        this.inNameset = false;
                        return;
                    }
                    if (qName.equalsIgnoreCase(FILESET)) {
                        this.inFileset = false;
                    } else if (qName.equalsIgnoreCase("name")) {
                        this.inName = false;
                    } else if (qName.equalsIgnoreCase("file")) {
                        this.inFile = false;
                    }
                }

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase(FAMILY)) {
                        this.inFamily = true;
                        this.namesets.clear();
                        this.filesets.clear();
                    } else {
                        if (qName.equalsIgnoreCase(NAMESET)) {
                            this.inNameset = true;
                            return;
                        }
                        if (qName.equalsIgnoreCase(FILESET)) {
                            this.inFileset = true;
                        } else if (qName.equalsIgnoreCase("name")) {
                            this.inName = true;
                        } else if (qName.equalsIgnoreCase("file")) {
                            this.inFile = true;
                        }
                    }
                }
            };
            saxParser.parse(is, handler);
            return true;
        } catch (IOException e2) {
            System.err.println("Failed to load default fonts descriptor: /system/etc/system_fonts.xml");
            return false;
        } catch (Exception e3) {
            System.err.println("Failed parsing default fonts descriptor;");
            e3.printStackTrace();
            return false;
        }
    }

    public static void populateFontFileNameMap(HashMap<String, String> fontToFileMap, HashMap<String, String> fontToFamilyNameMap, HashMap<String, ArrayList<String>> familyToFontListMap, Locale locale) {
        if (fontToFileMap == null || fontToFamilyNameMap == null || familyToFontListMap == null) {
            return;
        }
        if (locale == null) {
            Locale locale2 = Locale.ENGLISH;
        }
        boolean systemFonts_4_X_DescriptorFound = parse_4_X_SystemDefaultFonts(fontToFileMap, fontToFamilyNameMap, familyToFontListMap);
        if (!systemFonts_4_X_DescriptorFound) {
            parse_2_X_SystemDefaultFonts(fontToFileMap, fontToFamilyNameMap, familyToFontListMap);
        }
    }
}
