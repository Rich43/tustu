package javax.naming;

import java.io.Serializable;

/* loaded from: rt.jar:javax/naming/NameClassPair.class */
public class NameClassPair implements Serializable {
    private String name;
    private String className;
    private String fullName;
    private boolean isRel;
    private static final long serialVersionUID = 5620776610160863339L;

    public NameClassPair(String str, String str2) {
        this.fullName = null;
        this.isRel = true;
        this.name = str;
        this.className = str2;
    }

    public NameClassPair(String str, String str2, boolean z2) {
        this.fullName = null;
        this.isRel = true;
        this.name = str;
        this.className = str2;
        this.isRel = z2;
    }

    public String getClassName() {
        return this.className;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setClassName(String str) {
        this.className = str;
    }

    public boolean isRelative() {
        return this.isRel;
    }

    public void setRelative(boolean z2) {
        this.isRel = z2;
    }

    public String getNameInNamespace() {
        if (this.fullName == null) {
            throw new UnsupportedOperationException();
        }
        return this.fullName;
    }

    public void setNameInNamespace(String str) {
        this.fullName = str;
    }

    public String toString() {
        return (isRelative() ? "" : "(not relative)") + getName() + ": " + getClassName();
    }
}
