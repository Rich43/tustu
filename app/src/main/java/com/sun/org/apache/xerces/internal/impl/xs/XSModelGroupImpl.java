package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSModelGroupImpl.class */
public class XSModelGroupImpl implements XSModelGroup {
    public static final short MODELGROUP_CHOICE = 101;
    public static final short MODELGROUP_SEQUENCE = 102;
    public static final short MODELGROUP_ALL = 103;
    public short fCompositor;
    public XSParticleDecl[] fParticles = null;
    public int fParticleCount = 0;
    public XSObjectList fAnnotations = null;
    private String fDescription = null;

    public boolean isEmpty() {
        for (int i2 = 0; i2 < this.fParticleCount; i2++) {
            if (!this.fParticles[i2].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int minEffectiveTotalRange() {
        if (this.fCompositor == 101) {
            return minEffectiveTotalRangeChoice();
        }
        return minEffectiveTotalRangeAllSeq();
    }

    private int minEffectiveTotalRangeAllSeq() {
        int total = 0;
        for (int i2 = 0; i2 < this.fParticleCount; i2++) {
            total += this.fParticles[i2].minEffectiveTotalRange();
        }
        return total;
    }

    private int minEffectiveTotalRangeChoice() {
        int min = 0;
        if (this.fParticleCount > 0) {
            min = this.fParticles[0].minEffectiveTotalRange();
        }
        for (int i2 = 1; i2 < this.fParticleCount; i2++) {
            int one = this.fParticles[i2].minEffectiveTotalRange();
            if (one < min) {
                min = one;
            }
        }
        return min;
    }

    public int maxEffectiveTotalRange() {
        if (this.fCompositor == 101) {
            return maxEffectiveTotalRangeChoice();
        }
        return maxEffectiveTotalRangeAllSeq();
    }

    private int maxEffectiveTotalRangeAllSeq() {
        int total = 0;
        for (int i2 = 0; i2 < this.fParticleCount; i2++) {
            int one = this.fParticles[i2].maxEffectiveTotalRange();
            if (one == -1) {
                return -1;
            }
            total += one;
        }
        return total;
    }

    private int maxEffectiveTotalRangeChoice() {
        int max = 0;
        if (this.fParticleCount > 0) {
            max = this.fParticles[0].maxEffectiveTotalRange();
            if (max == -1) {
                return -1;
            }
        }
        for (int i2 = 1; i2 < this.fParticleCount; i2++) {
            int one = this.fParticles[i2].maxEffectiveTotalRange();
            if (one == -1) {
                return -1;
            }
            if (one > max) {
                max = one;
            }
        }
        return max;
    }

    public String toString() {
        if (this.fDescription == null) {
            StringBuffer buffer = new StringBuffer();
            if (this.fCompositor == 103) {
                buffer.append("all(");
            } else {
                buffer.append('(');
            }
            if (this.fParticleCount > 0) {
                buffer.append(this.fParticles[0].toString());
            }
            for (int i2 = 1; i2 < this.fParticleCount; i2++) {
                if (this.fCompositor == 101) {
                    buffer.append('|');
                } else {
                    buffer.append(',');
                }
                buffer.append(this.fParticles[i2].toString());
            }
            buffer.append(')');
            this.fDescription = buffer.toString();
        }
        return this.fDescription;
    }

    public void reset() {
        this.fCompositor = (short) 102;
        this.fParticles = null;
        this.fParticleCount = 0;
        this.fDescription = null;
        this.fAnnotations = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 7;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroup
    public short getCompositor() {
        if (this.fCompositor == 101) {
            return (short) 2;
        }
        if (this.fCompositor == 102) {
            return (short) 1;
        }
        return (short) 3;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroup
    public XSObjectList getParticles() {
        return new XSObjectListImpl(this.fParticles, this.fParticleCount);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroup
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModelGroup
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }
}
