package org.apache.commons.math3.optimization.linear;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/linear/Relationship.class */
public enum Relationship {
    EQ("="),
    LEQ("<="),
    GEQ(">=");

    private final String stringValue;

    Relationship(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.stringValue;
    }

    public Relationship oppositeRelationship() {
        switch (this) {
            case LEQ:
                return GEQ;
            case GEQ:
                return LEQ;
            default:
                return EQ;
        }
    }
}
