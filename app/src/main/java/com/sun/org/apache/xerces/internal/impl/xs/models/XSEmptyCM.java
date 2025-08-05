package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.ArrayList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSEmptyCM.class */
public class XSEmptyCM implements XSCMValidator {
    private static final short STATE_START = 0;
    private static final Vector EMPTY = new Vector(0);

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public int[] startContentModel() {
        return new int[]{0};
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public Object oneTransition(QName elementName, int[] currentState, SubstitutionGroupHandler subGroupHandler) {
        if (currentState[0] < 0) {
            currentState[0] = -2;
            return null;
        }
        currentState[0] = -1;
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public boolean endContentModel(int[] currentState) {
        int state = currentState[0];
        if (state < 0) {
            return false;
        }
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler) throws XMLSchemaException {
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public Vector whatCanGoHere(int[] state) {
        return EMPTY;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator
    public ArrayList checkMinMaxBounds() {
        return null;
    }
}
