package sun.print;

import java.util.ArrayList;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

/* compiled from: Win32PrintService.java */
/* loaded from: rt.jar:sun/print/Win32MediaSize.class */
class Win32MediaSize extends MediaSizeName {
    private static ArrayList winStringTable = new ArrayList();
    private static ArrayList winEnumTable = new ArrayList();
    private static MediaSize[] predefMedia;
    private int dmPaperID;

    static {
        MediaSizeName[] superEnumTable = new Win32MediaSize(-1).getSuperEnumTable();
        if (superEnumTable != null) {
            predefMedia = new MediaSize[superEnumTable.length];
            for (int i2 = 0; i2 < superEnumTable.length; i2++) {
                predefMedia[i2] = MediaSize.getMediaSizeForName(superEnumTable[i2]);
            }
        }
    }

    private Win32MediaSize(int i2) {
        super(i2);
    }

    private static synchronized int nextValue(String str) {
        winStringTable.add(str);
        return winStringTable.size() - 1;
    }

    public static synchronized Win32MediaSize findMediaName(String str) {
        int iIndexOf = winStringTable.indexOf(str);
        if (iIndexOf != -1) {
            return (Win32MediaSize) winEnumTable.get(iIndexOf);
        }
        return null;
    }

    public static MediaSize[] getPredefMedia() {
        return predefMedia;
    }

    public Win32MediaSize(String str, int i2) {
        super(nextValue(str));
        this.dmPaperID = i2;
        winEnumTable.add(this);
    }

    private MediaSizeName[] getSuperEnumTable() {
        return (MediaSizeName[]) super.getEnumValueTable();
    }

    int getDMPaper() {
        return this.dmPaperID;
    }

    @Override // javax.print.attribute.standard.MediaSizeName, javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return (String[]) winStringTable.toArray(new String[winStringTable.size()]);
    }

    @Override // javax.print.attribute.standard.MediaSizeName, javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return (MediaSizeName[]) winEnumTable.toArray(new MediaSizeName[winEnumTable.size()]);
    }
}
