package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSDeclarationPool.class */
public final class XSDeclarationPool {
    private static final int CHUNK_SHIFT = 8;
    private static final int CHUNK_SIZE = 256;
    private static final int CHUNK_MASK = 255;
    private static final int INITIAL_CHUNK_COUNT = 4;
    private XSElementDecl[][] fElementDecl = new XSElementDecl[4];
    private int fElementDeclIndex = 0;
    private XSParticleDecl[][] fParticleDecl = new XSParticleDecl[4];
    private int fParticleDeclIndex = 0;
    private XSModelGroupImpl[][] fModelGroup = new XSModelGroupImpl[4];
    private int fModelGroupIndex = 0;
    private XSAttributeDecl[][] fAttrDecl = new XSAttributeDecl[4];
    private int fAttrDeclIndex = 0;
    private XSComplexTypeDecl[][] fCTDecl = new XSComplexTypeDecl[4];
    private int fCTDeclIndex = 0;
    private XSSimpleTypeDecl[][] fSTDecl = new XSSimpleTypeDecl[4];
    private int fSTDeclIndex = 0;
    private XSAttributeUseImpl[][] fAttributeUse = new XSAttributeUseImpl[4];
    private int fAttributeUseIndex = 0;
    private SchemaDVFactoryImpl dvFactory;

    public void setDVFactory(SchemaDVFactoryImpl dvFactory) {
        this.dvFactory = dvFactory;
    }

    public final XSElementDecl getElementDecl() {
        int chunk = this.fElementDeclIndex >> 8;
        int index = this.fElementDeclIndex & 255;
        ensureElementDeclCapacity(chunk);
        if (this.fElementDecl[chunk][index] == null) {
            this.fElementDecl[chunk][index] = new XSElementDecl();
        } else {
            this.fElementDecl[chunk][index].reset();
        }
        this.fElementDeclIndex++;
        return this.fElementDecl[chunk][index];
    }

    public final XSAttributeDecl getAttributeDecl() {
        int chunk = this.fAttrDeclIndex >> 8;
        int index = this.fAttrDeclIndex & 255;
        ensureAttrDeclCapacity(chunk);
        if (this.fAttrDecl[chunk][index] == null) {
            this.fAttrDecl[chunk][index] = new XSAttributeDecl();
        } else {
            this.fAttrDecl[chunk][index].reset();
        }
        this.fAttrDeclIndex++;
        return this.fAttrDecl[chunk][index];
    }

    public final XSAttributeUseImpl getAttributeUse() {
        int chunk = this.fAttributeUseIndex >> 8;
        int index = this.fAttributeUseIndex & 255;
        ensureAttributeUseCapacity(chunk);
        if (this.fAttributeUse[chunk][index] == null) {
            this.fAttributeUse[chunk][index] = new XSAttributeUseImpl();
        } else {
            this.fAttributeUse[chunk][index].reset();
        }
        this.fAttributeUseIndex++;
        return this.fAttributeUse[chunk][index];
    }

    public final XSComplexTypeDecl getComplexTypeDecl() {
        int chunk = this.fCTDeclIndex >> 8;
        int index = this.fCTDeclIndex & 255;
        ensureCTDeclCapacity(chunk);
        if (this.fCTDecl[chunk][index] == null) {
            this.fCTDecl[chunk][index] = new XSComplexTypeDecl();
        } else {
            this.fCTDecl[chunk][index].reset();
        }
        this.fCTDeclIndex++;
        return this.fCTDecl[chunk][index];
    }

    public final XSSimpleTypeDecl getSimpleTypeDecl() {
        int chunk = this.fSTDeclIndex >> 8;
        int index = this.fSTDeclIndex & 255;
        ensureSTDeclCapacity(chunk);
        if (this.fSTDecl[chunk][index] == null) {
            this.fSTDecl[chunk][index] = this.dvFactory.newXSSimpleTypeDecl();
        } else {
            this.fSTDecl[chunk][index].reset();
        }
        this.fSTDeclIndex++;
        return this.fSTDecl[chunk][index];
    }

    public final XSParticleDecl getParticleDecl() {
        int chunk = this.fParticleDeclIndex >> 8;
        int index = this.fParticleDeclIndex & 255;
        ensureParticleDeclCapacity(chunk);
        if (this.fParticleDecl[chunk][index] == null) {
            this.fParticleDecl[chunk][index] = new XSParticleDecl();
        } else {
            this.fParticleDecl[chunk][index].reset();
        }
        this.fParticleDeclIndex++;
        return this.fParticleDecl[chunk][index];
    }

    public final XSModelGroupImpl getModelGroup() {
        int chunk = this.fModelGroupIndex >> 8;
        int index = this.fModelGroupIndex & 255;
        ensureModelGroupCapacity(chunk);
        if (this.fModelGroup[chunk][index] == null) {
            this.fModelGroup[chunk][index] = new XSModelGroupImpl();
        } else {
            this.fModelGroup[chunk][index].reset();
        }
        this.fModelGroupIndex++;
        return this.fModelGroup[chunk][index];
    }

    private boolean ensureElementDeclCapacity(int chunk) {
        if (chunk >= this.fElementDecl.length) {
            this.fElementDecl = resize(this.fElementDecl, this.fElementDecl.length * 2);
        } else if (this.fElementDecl[chunk] != null) {
            return false;
        }
        this.fElementDecl[chunk] = new XSElementDecl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl[], com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl[][], java.lang.Object] */
    private static XSElementDecl[][] resize(XSElementDecl[][] array, int newsize) {
        ?? r0 = new XSElementDecl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean ensureParticleDeclCapacity(int chunk) {
        if (chunk >= this.fParticleDecl.length) {
            this.fParticleDecl = resize(this.fParticleDecl, this.fParticleDecl.length * 2);
        } else if (this.fParticleDecl[chunk] != null) {
            return false;
        }
        this.fParticleDecl[chunk] = new XSParticleDecl[256];
        return true;
    }

    private boolean ensureModelGroupCapacity(int chunk) {
        if (chunk >= this.fModelGroup.length) {
            this.fModelGroup = resize(this.fModelGroup, this.fModelGroup.length * 2);
        } else if (this.fModelGroup[chunk] != null) {
            return false;
        }
        this.fModelGroup[chunk] = new XSModelGroupImpl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl[], com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl[][], java.lang.Object] */
    private static XSParticleDecl[][] resize(XSParticleDecl[][] array, int newsize) {
        ?? r0 = new XSParticleDecl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl[], com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl[][], java.lang.Object] */
    private static XSModelGroupImpl[][] resize(XSModelGroupImpl[][] array, int newsize) {
        ?? r0 = new XSModelGroupImpl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean ensureAttrDeclCapacity(int chunk) {
        if (chunk >= this.fAttrDecl.length) {
            this.fAttrDecl = resize(this.fAttrDecl, this.fAttrDecl.length * 2);
        } else if (this.fAttrDecl[chunk] != null) {
            return false;
        }
        this.fAttrDecl[chunk] = new XSAttributeDecl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl[], com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl[][], java.lang.Object] */
    private static XSAttributeDecl[][] resize(XSAttributeDecl[][] array, int newsize) {
        ?? r0 = new XSAttributeDecl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean ensureAttributeUseCapacity(int chunk) {
        if (chunk >= this.fAttributeUse.length) {
            this.fAttributeUse = resize(this.fAttributeUse, this.fAttributeUse.length * 2);
        } else if (this.fAttributeUse[chunk] != null) {
            return false;
        }
        this.fAttributeUse[chunk] = new XSAttributeUseImpl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl[], com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl[][], java.lang.Object] */
    private static XSAttributeUseImpl[][] resize(XSAttributeUseImpl[][] array, int newsize) {
        ?? r0 = new XSAttributeUseImpl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean ensureSTDeclCapacity(int chunk) {
        if (chunk >= this.fSTDecl.length) {
            this.fSTDecl = resize(this.fSTDecl, this.fSTDecl.length * 2);
        } else if (this.fSTDecl[chunk] != null) {
            return false;
        }
        this.fSTDecl[chunk] = new XSSimpleTypeDecl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl[], com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl[][], java.lang.Object] */
    private static XSSimpleTypeDecl[][] resize(XSSimpleTypeDecl[][] array, int newsize) {
        ?? r0 = new XSSimpleTypeDecl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean ensureCTDeclCapacity(int chunk) {
        if (chunk >= this.fCTDecl.length) {
            this.fCTDecl = resize(this.fCTDecl, this.fCTDecl.length * 2);
        } else if (this.fCTDecl[chunk] != null) {
            return false;
        }
        this.fCTDecl[chunk] = new XSComplexTypeDecl[256];
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl[], com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl[][], java.lang.Object] */
    private static XSComplexTypeDecl[][] resize(XSComplexTypeDecl[][] array, int newsize) {
        ?? r0 = new XSComplexTypeDecl[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    public void reset() {
        this.fElementDeclIndex = 0;
        this.fParticleDeclIndex = 0;
        this.fModelGroupIndex = 0;
        this.fSTDeclIndex = 0;
        this.fCTDeclIndex = 0;
        this.fAttrDeclIndex = 0;
        this.fAttributeUseIndex = 0;
    }
}
