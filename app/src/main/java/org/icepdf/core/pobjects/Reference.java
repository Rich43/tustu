package org.icepdf.core.pobjects;

import java.io.Serializable;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Reference.class */
public class Reference implements Serializable {
    int objf;
    int genf;

    public Reference(Number o2, Number g2) {
        this.objf = 0;
        this.genf = 0;
        if (o2 != null) {
            this.objf = o2.intValue();
        }
        if (g2 != null) {
            this.genf = g2.intValue();
        }
    }

    public Reference(int o2, int g2) {
        this.objf = 0;
        this.genf = 0;
        this.objf = o2;
        this.genf = g2;
    }

    public int hashCode() {
        return (this.objf * 1000) + this.genf;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && (obj instanceof Reference)) {
            Reference tmp = (Reference) obj;
            return tmp.objf == this.objf && tmp.genf == this.genf;
        }
        return false;
    }

    public int getObjectNumber() {
        return this.objf;
    }

    public int getGenerationNumber() {
        return this.genf;
    }

    public String toString() {
        return this.objf + " " + this.genf + "R";
    }
}
