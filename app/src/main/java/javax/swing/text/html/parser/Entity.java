package javax.swing.text.html.parser;

import com.sun.glass.ui.Clipboard;
import java.util.Hashtable;

/* loaded from: rt.jar:javax/swing/text/html/parser/Entity.class */
public final class Entity implements DTDConstants {
    public String name;
    public int type;
    public char[] data;
    static Hashtable<String, Integer> entityTypes = new Hashtable<>();

    public Entity(String str, int i2, char[] cArr) {
        this.name = str;
        this.type = i2;
        this.data = cArr;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type & 65535;
    }

    public boolean isParameter() {
        return (this.type & 262144) != 0;
    }

    public boolean isGeneral() {
        return (this.type & 65536) != 0;
    }

    public char[] getData() {
        return this.data;
    }

    public String getString() {
        return new String(this.data, 0, this.data.length);
    }

    static {
        entityTypes.put("PUBLIC", 10);
        entityTypes.put("CDATA", 1);
        entityTypes.put("SDATA", 11);
        entityTypes.put("PI", 12);
        entityTypes.put("STARTTAG", 13);
        entityTypes.put("ENDTAG", 14);
        entityTypes.put("MS", 15);
        entityTypes.put("MD", 16);
        entityTypes.put(Clipboard.SYSTEM, 17);
    }

    public static int name2type(String str) {
        Integer num = entityTypes.get(str);
        if (num == null) {
            return 1;
        }
        return num.intValue();
    }
}
