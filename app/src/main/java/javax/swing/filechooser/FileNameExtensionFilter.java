package javax.swing.filechooser;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: rt.jar:javax/swing/filechooser/FileNameExtensionFilter.class */
public final class FileNameExtensionFilter extends FileFilter {
    private final String description;
    private final String[] extensions;
    private final String[] lowerCaseExtensions;

    public FileNameExtensionFilter(String str, String... strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalArgumentException("Extensions must be non-null and not empty");
        }
        this.description = str;
        this.extensions = new String[strArr.length];
        this.lowerCaseExtensions = new String[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] == null || strArr[i2].length() == 0) {
                throw new IllegalArgumentException("Each extension must be non-null and not empty");
            }
            this.extensions[i2] = strArr[i2];
            this.lowerCaseExtensions[i2] = strArr[i2].toLowerCase(Locale.ENGLISH);
        }
    }

    @Override // javax.swing.filechooser.FileFilter
    public boolean accept(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                return true;
            }
            String name = file.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            if (iLastIndexOf > 0 && iLastIndexOf < name.length() - 1) {
                String lowerCase = name.substring(iLastIndexOf + 1).toLowerCase(Locale.ENGLISH);
                for (String str : this.lowerCaseExtensions) {
                    if (lowerCase.equals(str)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override // javax.swing.filechooser.FileFilter
    public String getDescription() {
        return this.description;
    }

    public String[] getExtensions() {
        String[] strArr = new String[this.extensions.length];
        System.arraycopy(this.extensions, 0, strArr, 0, this.extensions.length);
        return strArr;
    }

    public String toString() {
        return super.toString() + "[description=" + getDescription() + " extensions=" + ((Object) Arrays.asList(getExtensions())) + "]";
    }
}
