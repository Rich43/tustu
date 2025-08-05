package org.icepdf.ri.common;

import java.io.File;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/ViewModel.class */
public class ViewModel {
    private static File defaultFile = null;
    private static String defaultURL = null;
    private boolean isShrinkToPrintableArea = true;
    private int printCopies = 1;
    private PrintHelper printHelper;

    static File getDefaultFile() {
        return defaultFile;
    }

    public static String getDefaultFilePath() {
        if (defaultFile == null) {
            return null;
        }
        return defaultFile.getAbsolutePath();
    }

    public static String getDefaultURL() {
        return defaultURL;
    }

    static void setDefaultFile(File f2) {
        defaultFile = f2;
    }

    public static void setDefaultFilePath(String defFilePath) {
        if (defFilePath == null || defFilePath.length() == 0) {
            defaultFile = null;
        } else {
            defaultFile = new File(defFilePath);
        }
    }

    public static void setDefaultURL(String defURL) {
        if (defURL == null || defURL.length() == 0) {
            defaultURL = null;
        } else {
            defaultURL = defURL;
        }
    }

    public PrintHelper getPrintHelper() {
        return this.printHelper;
    }

    public void setPrintHelper(PrintHelper printHelper) {
        this.printHelper = printHelper;
    }

    public boolean isShrinkToPrintableArea() {
        return this.isShrinkToPrintableArea;
    }

    public void setShrinkToPrintableArea(boolean shrinkToPrintableArea) {
        this.isShrinkToPrintableArea = shrinkToPrintableArea;
    }

    public int getPrintCopies() {
        return this.printCopies;
    }

    public void setPrintCopies(int printCopies) {
        this.printCopies = printCopies;
    }
}
