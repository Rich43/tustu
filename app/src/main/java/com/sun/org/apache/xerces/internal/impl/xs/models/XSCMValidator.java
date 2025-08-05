package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
import com.sun.org.apache.xerces.internal.xni.QName;
import java.util.ArrayList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSCMValidator.class */
public interface XSCMValidator {
    public static final short FIRST_ERROR = -1;
    public static final short SUBSEQUENT_ERROR = -2;

    int[] startContentModel();

    Object oneTransition(QName qName, int[] iArr, SubstitutionGroupHandler substitutionGroupHandler);

    boolean endContentModel(int[] iArr);

    boolean checkUniqueParticleAttribution(SubstitutionGroupHandler substitutionGroupHandler) throws XMLSchemaException;

    Vector whatCanGoHere(int[] iArr);

    ArrayList checkMinMaxBounds();
}
