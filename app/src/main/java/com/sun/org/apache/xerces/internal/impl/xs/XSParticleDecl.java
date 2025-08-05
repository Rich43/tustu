package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSParticle;
import com.sun.org.apache.xerces.internal.xs.XSTerm;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSParticleDecl.class */
public class XSParticleDecl implements XSParticle {
    public static final short PARTICLE_EMPTY = 0;
    public static final short PARTICLE_ELEMENT = 1;
    public static final short PARTICLE_WILDCARD = 2;
    public static final short PARTICLE_MODELGROUP = 3;
    public static final short PARTICLE_ZERO_OR_MORE = 4;
    public static final short PARTICLE_ZERO_OR_ONE = 5;
    public static final short PARTICLE_ONE_OR_MORE = 6;
    public short fType = 0;
    public XSTerm fValue = null;
    public int fMinOccurs = 1;
    public int fMaxOccurs = 1;
    public XSObjectList fAnnotations = null;
    private String fDescription = null;

    public XSParticleDecl makeClone() {
        XSParticleDecl particle = new XSParticleDecl();
        particle.fType = this.fType;
        particle.fMinOccurs = this.fMinOccurs;
        particle.fMaxOccurs = this.fMaxOccurs;
        particle.fDescription = this.fDescription;
        particle.fValue = this.fValue;
        particle.fAnnotations = this.fAnnotations;
        return particle;
    }

    public boolean emptiable() {
        return minEffectiveTotalRange() == 0;
    }

    public boolean isEmpty() {
        if (this.fType == 0) {
            return true;
        }
        if (this.fType == 1 || this.fType == 2) {
            return false;
        }
        return ((XSModelGroupImpl) this.fValue).isEmpty();
    }

    public int minEffectiveTotalRange() {
        if (this.fType == 0) {
            return 0;
        }
        if (this.fType == 3) {
            return ((XSModelGroupImpl) this.fValue).minEffectiveTotalRange() * this.fMinOccurs;
        }
        return this.fMinOccurs;
    }

    public int maxEffectiveTotalRange() {
        if (this.fType == 0) {
            return 0;
        }
        if (this.fType == 3) {
            int max = ((XSModelGroupImpl) this.fValue).maxEffectiveTotalRange();
            if (max == -1) {
                return -1;
            }
            if (max != 0 && this.fMaxOccurs == -1) {
                return -1;
            }
            return max * this.fMaxOccurs;
        }
        return this.fMaxOccurs;
    }

    public String toString() {
        if (this.fDescription == null) {
            StringBuffer buffer = new StringBuffer();
            appendParticle(buffer);
            if ((this.fMinOccurs != 0 || this.fMaxOccurs != 0) && (this.fMinOccurs != 1 || this.fMaxOccurs != 1)) {
                buffer.append('{').append(this.fMinOccurs);
                if (this.fMaxOccurs == -1) {
                    buffer.append("-UNBOUNDED");
                } else if (this.fMinOccurs != this.fMaxOccurs) {
                    buffer.append('-').append(this.fMaxOccurs);
                }
                buffer.append('}');
            }
            this.fDescription = buffer.toString();
        }
        return this.fDescription;
    }

    void appendParticle(StringBuffer buffer) {
        switch (this.fType) {
            case 0:
                buffer.append("EMPTY");
                break;
            case 1:
                buffer.append(this.fValue.toString());
                break;
            case 2:
                buffer.append('(');
                buffer.append(this.fValue.toString());
                buffer.append(')');
                break;
            case 3:
                buffer.append(this.fValue.toString());
                break;
        }
    }

    public void reset() {
        this.fType = (short) 0;
        this.fValue = null;
        this.fMinOccurs = 1;
        this.fMaxOccurs = 1;
        this.fDescription = null;
        this.fAnnotations = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 8;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSParticle
    public int getMinOccurs() {
        return this.fMinOccurs;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSParticle
    public boolean getMaxOccursUnbounded() {
        return this.fMaxOccurs == -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSParticle
    public int getMaxOccurs() {
        return this.fMaxOccurs;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSParticle
    public XSTerm getTerm() {
        return this.fValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSParticle
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }
}
