package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.ArrayList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSAllCM.class */
public class XSAllCM implements XSCMValidator {
    private static final short STATE_START = 0;
    private static final short STATE_VALID = 1;
    private static final short STATE_CHILD = 1;
    private XSElementDecl[] fAllElements;
    private boolean[] fIsOptionalElement;
    private boolean fHasOptionalContent;
    private int fNumElements = 0;

    public XSAllCM(boolean hasOptionalContent, int size) {
        this.fHasOptionalContent = false;
        this.fHasOptionalContent = hasOptionalContent;
        this.fAllElements = new XSElementDecl[size];
        this.fIsOptionalElement = new boolean[size];
    }

    public void addElement(XSElementDecl element, boolean isOptional) {
        this.fAllElements[this.fNumElements] = element;
        this.fIsOptionalElement[this.fNumElements] = isOptional;
        this.fNumElements++;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public int[] startContentModel() {
        int[] state = new int[this.fNumElements + 1];
        for (int i2 = 0; i2 <= this.fNumElements; i2++) {
            state[i2] = 0;
        }
        return state;
    }

    Object findMatchingDecl(QName elementName, SubstitutionGroupHandler subGroupHandler) {
        Object matchingDecl = null;
        for (int i2 = 0; i2 < this.fNumElements; i2++) {
            matchingDecl = subGroupHandler.getMatchingElemDecl(elementName, this.fAllElements[i2]);
            if (matchingDecl != null) {
                break;
            }
        }
        return matchingDecl;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public Object oneTransition(QName elementName, int[] currentState, SubstitutionGroupHandler subGroupHandler) {
        Object matchingDecl;
        if (currentState[0] < 0) {
            currentState[0] = -2;
            return findMatchingDecl(elementName, subGroupHandler);
        }
        currentState[0] = 1;
        for (int i2 = 0; i2 < this.fNumElements; i2++) {
            if (currentState[i2 + 1] == 0 && (matchingDecl = subGroupHandler.getMatchingElemDecl(elementName, this.fAllElements[i2])) != null) {
                currentState[i2 + 1] = 1;
                return matchingDecl;
            }
        }
        currentState[0] = -1;
        return findMatchingDecl(elementName, subGroupHandler);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public boolean endContentModel(int[] currentState) {
        int state = currentState[0];
        if (state == -1 || state == -2) {
            return false;
        }
        if (this.fHasOptionalContent && state == 0) {
            return true;
        }
        for (int i2 = 0; i2 < this.fNumElements; i2++) {
            if (!this.fIsOptionalElement[i2] && currentState[i2 + 1] == 0) {
                return false;
            }
        }
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler) throws XMLSchemaException {
        for (int i2 = 0; i2 < this.fNumElements; i2++) {
            for (int j2 = i2 + 1; j2 < this.fNumElements; j2++) {
                if (XSConstraints.overlapUPA(this.fAllElements[i2], this.fAllElements[j2], subGroupHandler)) {
                    throw new XMLSchemaException("cos-nonambig", new Object[]{this.fAllElements[i2].toString(), this.fAllElements[j2].toString()});
                }
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public Vector whatCanGoHere(int[] state) {
        Vector ret = new Vector();
        for (int i2 = 0; i2 < this.fNumElements; i2++) {
            if (state[i2 + 1] == 0) {
                ret.addElement(this.fAllElements[i2]);
            }
        }
        return ret;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public ArrayList checkMinMaxBounds() {
        return null;
    }
}
