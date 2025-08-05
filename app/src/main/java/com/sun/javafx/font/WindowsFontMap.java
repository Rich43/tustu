package com.sun.javafx.font;

import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/WindowsFontMap.class */
class WindowsFontMap {
    static HashMap<String, FamilyDescription> platformFontMap;

    WindowsFontMap() {
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/WindowsFontMap$FamilyDescription.class */
    static class FamilyDescription {
        public String familyName;
        public String plainFullName;
        public String boldFullName;
        public String italicFullName;
        public String boldItalicFullName;
        public String plainFileName;
        public String boldFileName;
        public String italicFileName;
        public String boldItalicFileName;

        FamilyDescription() {
        }
    }

    static HashMap<String, FamilyDescription> populateHardcodedFileNameMap() {
        HashMap<String, FamilyDescription> platformFontMap2 = new HashMap<>();
        FamilyDescription fd = new FamilyDescription();
        fd.familyName = "Segoe UI";
        fd.plainFullName = "Segoe UI";
        fd.plainFileName = "segoeui.ttf";
        fd.boldFullName = "Segoe UI Bold";
        fd.boldFileName = "segoeuib.ttf";
        fd.italicFullName = "Segoe UI Italic";
        fd.italicFileName = "segoeuii.ttf";
        fd.boldItalicFullName = "Segoe UI Bold Italic";
        fd.boldItalicFileName = "segoeuiz.ttf";
        platformFontMap2.put("segoe", fd);
        FamilyDescription fd2 = new FamilyDescription();
        fd2.familyName = "Tahoma";
        fd2.plainFullName = "Tahoma";
        fd2.plainFileName = "tahoma.ttf";
        fd2.boldFullName = "Tahoma Bold";
        fd2.boldFileName = "tahomabd.ttf";
        platformFontMap2.put("tahoma", fd2);
        FamilyDescription fd3 = new FamilyDescription();
        fd3.familyName = "Verdana";
        fd3.plainFullName = "Verdana";
        fd3.plainFileName = "verdana.TTF";
        fd3.boldFullName = "Verdana Bold";
        fd3.boldFileName = "verdanab.TTF";
        fd3.italicFullName = "Verdana Italic";
        fd3.italicFileName = "verdanai.TTF";
        fd3.boldItalicFullName = "Verdana Bold Italic";
        fd3.boldItalicFileName = "verdanaz.TTF";
        platformFontMap2.put("verdana", fd3);
        FamilyDescription fd4 = new FamilyDescription();
        fd4.familyName = "Arial";
        fd4.plainFullName = "Arial";
        fd4.plainFileName = "ARIAL.TTF";
        fd4.boldFullName = "Arial Bold";
        fd4.boldFileName = "ARIALBD.TTF";
        fd4.italicFullName = "Arial Italic";
        fd4.italicFileName = "ARIALI.TTF";
        fd4.boldItalicFullName = "Arial Bold Italic";
        fd4.boldItalicFileName = "ARIALBI.TTF";
        platformFontMap2.put("arial", fd4);
        FamilyDescription fd5 = new FamilyDescription();
        fd5.familyName = "Times New Roman";
        fd5.plainFullName = "Times New Roman";
        fd5.plainFileName = "times.ttf";
        fd5.boldFullName = "Times New Roman Bold";
        fd5.boldFileName = "timesbd.ttf";
        fd5.italicFullName = "Times New Roman Italic";
        fd5.italicFileName = "timesi.ttf";
        fd5.boldItalicFullName = "Times New Roman Bold Italic";
        fd5.boldItalicFileName = "timesbi.ttf";
        platformFontMap2.put("times", fd5);
        FamilyDescription fd6 = new FamilyDescription();
        fd6.familyName = "Courier New";
        fd6.plainFullName = "Courier New";
        fd6.plainFileName = "cour.ttf";
        fd6.boldFullName = "Courier New Bold";
        fd6.boldFileName = "courbd.ttf";
        fd6.italicFullName = "Courier New Italic";
        fd6.italicFileName = "couri.ttf";
        fd6.boldItalicFullName = "Courier New Bold Italic";
        fd6.boldItalicFileName = "courbi.ttf";
        platformFontMap2.put("courier", fd6);
        return platformFontMap2;
    }

    static String getPathName(String filename) {
        return PrismFontFactory.getPathNameWindows(filename);
    }

    static String findFontFile(String lcName, int style) {
        if (platformFontMap == null) {
            platformFontMap = populateHardcodedFileNameMap();
        }
        if (platformFontMap == null || platformFontMap.size() == 0) {
            return null;
        }
        int spaceIndex = lcName.indexOf(32);
        String firstWord = lcName;
        if (spaceIndex > 0) {
            firstWord = lcName.substring(0, spaceIndex);
        }
        FamilyDescription fd = platformFontMap.get(firstWord);
        if (fd == null) {
            return null;
        }
        String file = null;
        if (style < 0) {
            if (lcName.equalsIgnoreCase(fd.plainFullName)) {
                file = fd.plainFileName;
            } else if (lcName.equalsIgnoreCase(fd.boldFullName)) {
                file = fd.boldFileName;
            } else if (lcName.equalsIgnoreCase(fd.italicFullName)) {
                file = fd.italicFileName;
            } else if (lcName.equalsIgnoreCase(fd.boldItalicFullName)) {
                file = fd.boldItalicFileName;
            }
            if (file != null) {
                return getPathName(file);
            }
            return null;
        }
        if (!lcName.equalsIgnoreCase(fd.familyName)) {
            return null;
        }
        switch (style) {
            case 0:
                file = fd.plainFileName;
                break;
            case 1:
                file = fd.boldFileName;
                if (file == null) {
                    file = fd.plainFileName;
                    break;
                }
                break;
            case 2:
                file = fd.italicFileName;
                if (file == null) {
                    file = fd.plainFileName;
                    break;
                }
                break;
            case 3:
                file = fd.boldItalicFileName;
                if (file == null) {
                    file = fd.italicFileName;
                }
                if (file == null) {
                    file = fd.boldFileName;
                }
                if (file == null) {
                    file = fd.plainFileName;
                    break;
                }
                break;
        }
        if (file != null) {
            return getPathName(file);
        }
        return null;
    }
}
