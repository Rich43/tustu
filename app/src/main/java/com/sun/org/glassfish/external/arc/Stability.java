package com.sun.org.glassfish.external.arc;

/* loaded from: rt.jar:com/sun/org/glassfish/external/arc/Stability.class */
public enum Stability {
    COMMITTED("Committed"),
    UNCOMMITTED("Uncommitted"),
    VOLATILE("Volatile"),
    NOT_AN_INTERFACE("Not-An-Interface"),
    PRIVATE("Private"),
    EXPERIMENTAL("Experimental"),
    UNSPECIFIED("Unspecified");

    private final String mName;

    Stability(String name) {
        this.mName = name;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.mName;
    }
}
