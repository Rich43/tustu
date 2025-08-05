package org.icepdf.core.pobjects;

import sun.security.pkcs11.wrapper.Constants;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PObject.class */
public class PObject {
    private Object object;
    private Reference objectReference;
    private int linearTraversalOffset;

    public PObject(Object object, Number objectNumber, Number objectGeneration) {
        this.objectReference = null;
        this.object = object;
        this.objectReference = new Reference(objectNumber, objectGeneration);
    }

    public PObject(Object object, Reference objectReference) {
        this.objectReference = null;
        this.object = object;
        this.objectReference = objectReference;
    }

    public Reference getReference() {
        return this.objectReference;
    }

    public Object getObject() {
        return this.object;
    }

    public int hashCode() {
        int result = this.object != null ? this.object.hashCode() : 0;
        return (31 * result) + (this.objectReference != null ? this.objectReference.hashCode() : 0);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        PObject pObject = (PObject) o2;
        if (this.object != null) {
            if (!this.object.equals(pObject.object)) {
                return false;
            }
        } else if (pObject.object != null) {
            return false;
        }
        if (this.objectReference != null) {
            if (!this.objectReference.equals(pObject.objectReference)) {
                return false;
            }
            return true;
        }
        if (pObject.objectReference != null) {
            return false;
        }
        return true;
    }

    public int getLinearTraversalOffset() {
        return this.linearTraversalOffset;
    }

    public void setLinearTraversalOffset(int linearTraversalOffset) {
        this.linearTraversalOffset = linearTraversalOffset;
    }

    public String toString() {
        return this.objectReference.toString() + Constants.INDENT + this.object.toString();
    }
}
