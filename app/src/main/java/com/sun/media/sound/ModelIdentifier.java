package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/ModelIdentifier.class */
public final class ModelIdentifier {
    private String object;
    private String variable;
    private int instance;

    public ModelIdentifier(String str) {
        this.object = null;
        this.variable = null;
        this.instance = 0;
        this.object = str;
    }

    public ModelIdentifier(String str, int i2) {
        this.object = null;
        this.variable = null;
        this.instance = 0;
        this.object = str;
        this.instance = i2;
    }

    public ModelIdentifier(String str, String str2) {
        this.object = null;
        this.variable = null;
        this.instance = 0;
        this.object = str;
        this.variable = str2;
    }

    public ModelIdentifier(String str, String str2, int i2) {
        this.object = null;
        this.variable = null;
        this.instance = 0;
        this.object = str;
        this.variable = str2;
        this.instance = i2;
    }

    public int getInstance() {
        return this.instance;
    }

    public void setInstance(int i2) {
        this.instance = i2;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String str) {
        this.object = str;
    }

    public String getVariable() {
        return this.variable;
    }

    public void setVariable(String str) {
        this.variable = str;
    }

    public int hashCode() {
        int iHashCode = this.instance;
        if (this.object != null) {
            iHashCode |= this.object.hashCode();
        }
        if (this.variable != null) {
            iHashCode |= this.variable.hashCode();
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ModelIdentifier)) {
            return false;
        }
        ModelIdentifier modelIdentifier = (ModelIdentifier) obj;
        if ((this.object == null) != (modelIdentifier.object == null)) {
            return false;
        }
        if ((this.variable == null) != (modelIdentifier.variable == null) || modelIdentifier.getInstance() != getInstance()) {
            return false;
        }
        if (this.object != null && !this.object.equals(modelIdentifier.object)) {
            return false;
        }
        if (this.variable != null && !this.variable.equals(modelIdentifier.variable)) {
            return false;
        }
        return true;
    }

    public String toString() {
        if (this.variable == null) {
            return this.object + "[" + this.instance + "]";
        }
        return this.object + "[" + this.instance + "]." + this.variable;
    }
}
