package sun.print;

import java.util.ArrayList;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;

/* loaded from: rt.jar:sun/print/CustomMediaTray.class */
class CustomMediaTray extends MediaTray {
    private static ArrayList customStringTable = new ArrayList();
    private static ArrayList customEnumTable = new ArrayList();
    private String choiceName;
    private static final long serialVersionUID = 1019451298193987013L;

    private CustomMediaTray(int i2) {
        super(i2);
    }

    private static synchronized int nextValue(String str) {
        customStringTable.add(str);
        return customStringTable.size() - 1;
    }

    public CustomMediaTray(String str, String str2) {
        super(nextValue(str));
        this.choiceName = str2;
        customEnumTable.add(this);
    }

    public String getChoiceName() {
        return this.choiceName;
    }

    public Media[] getSuperEnumTable() {
        return (Media[]) super.getEnumValueTable();
    }

    @Override // javax.print.attribute.standard.MediaTray, javax.print.attribute.EnumSyntax
    protected String[] getStringTable() {
        return (String[]) customStringTable.toArray(new String[customStringTable.size()]);
    }

    @Override // javax.print.attribute.standard.MediaTray, javax.print.attribute.EnumSyntax
    protected EnumSyntax[] getEnumValueTable() {
        return (MediaTray[]) customEnumTable.toArray(new MediaTray[customEnumTable.size()]);
    }
}
