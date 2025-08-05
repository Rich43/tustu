package sun.print;

import java.util.ArrayList;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.standard.MediaTray;

/* loaded from: rt.jar:sun/print/Win32MediaTray.class */
public class Win32MediaTray extends MediaTray {
    public int winID;
    static final Win32MediaTray ENVELOPE_MANUAL = new Win32MediaTray(0, 6);
    static final Win32MediaTray AUTO = new Win32MediaTray(1, 7);
    static final Win32MediaTray TRACTOR = new Win32MediaTray(2, 8);
    static final Win32MediaTray SMALL_FORMAT = new Win32MediaTray(3, 9);
    static final Win32MediaTray LARGE_FORMAT = new Win32MediaTray(4, 10);
    static final Win32MediaTray FORMSOURCE = new Win32MediaTray(5, 15);
    private static ArrayList winStringTable = new ArrayList();
    private static ArrayList winEnumTable = new ArrayList();
    private static final String[] myStringTable = {"Manual-Envelope", "Automatic-Feeder", "Tractor-Feeder", "Small-Format", "Large-Format", "Form-Source"};
    private static final MediaTray[] myEnumValueTable = {ENVELOPE_MANUAL, AUTO, TRACTOR, SMALL_FORMAT, LARGE_FORMAT, FORMSOURCE};

    private Win32MediaTray(int i2, int i3) {
        super(i2);
        this.winID = i3;
    }

    private static synchronized int nextValue(String str) {
        winStringTable.add(str);
        return getTraySize() - 1;
    }

    protected Win32MediaTray(int i2, String str) {
        super(nextValue(str));
        this.winID = i2;
        winEnumTable.add(this);
    }

    public int getDMBinID() {
        return this.winID;
    }

    protected static int getTraySize() {
        return myStringTable.length + winStringTable.size();
    }

    @Override // javax.print.attribute.standard.MediaTray, javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < myStringTable.length; i2++) {
            arrayList.add(myStringTable[i2]);
        }
        arrayList.addAll(winStringTable);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Override // javax.print.attribute.standard.MediaTray, javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < myEnumValueTable.length; i2++) {
            arrayList.add(myEnumValueTable[i2]);
        }
        arrayList.addAll(winEnumTable);
        return (MediaTray[]) arrayList.toArray(new MediaTray[arrayList.size()]);
    }
}
