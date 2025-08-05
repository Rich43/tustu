package org.icepdf.ri.common;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/FileExtensionUtils.class */
public class FileExtensionUtils {
    public static final String pdf = "pdf";
    public static final String svg = "svg";
    public static final String ps = "ps";
    public static final String txt = "txt";

    public static FileFilter getPDFFileFilter() {
        return new ExtensionFileFilter("Adobe PDF Files (*.pdf)", pdf);
    }

    public static FileFilter getTextFileFilter() {
        return new ExtensionFileFilter("Text Files (*.txt)", txt);
    }

    public static FileFilter getSVGFileFilter() {
        return new ExtensionFileFilter("SVG Files (*.svg)", svg);
    }

    public static String getExtension(File f2) {
        return getExtension(f2.getName());
    }

    public static String getExtension(String s2) {
        String ext = null;
        int i2 = s2.lastIndexOf(46);
        if (i2 > 0 && i2 < s2.length() - 1) {
            ext = s2.substring(i2 + 1).toLowerCase();
        }
        return ext;
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/FileExtensionUtils$ExtensionFileFilter.class */
    private static class ExtensionFileFilter extends FileFilter {
        private String description;
        private String extension;

        ExtensionFileFilter(String desc, String ext) {
            this.description = desc;
            this.extension = ext;
        }

        @Override // javax.swing.filechooser.FileFilter
        public boolean accept(File f2) {
            if (f2.isDirectory()) {
                return true;
            }
            String ext = FileExtensionUtils.getExtension(f2);
            if (ext != null && ext.equals(this.extension)) {
                return true;
            }
            return false;
        }

        @Override // javax.swing.filechooser.FileFilter
        public String getDescription() {
            return this.description;
        }
    }
}
