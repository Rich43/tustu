package sun.management;

import java.io.Serializable;

/* loaded from: rt.jar:sun/management/MethodInfo.class */
public class MethodInfo implements Serializable {
    private String name;
    private long type;
    private int compileSize;
    private static final long serialVersionUID = 6992337162326171013L;

    MethodInfo(String str, long j2, int i2) {
        this.name = str;
        this.type = j2;
        this.compileSize = i2;
    }

    public String getName() {
        return this.name;
    }

    public long getType() {
        return this.type;
    }

    public int getCompileSize() {
        return this.compileSize;
    }

    public String toString() {
        return getName() + " type = " + getType() + " compileSize = " + getCompileSize();
    }
}
