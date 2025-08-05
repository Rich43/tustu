package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xerces.internal.util.IntStack;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/XPathMatcher.class */
public class XPathMatcher {
    protected static final boolean DEBUG_ALL = false;
    protected static final boolean DEBUG_METHODS = false;
    protected static final boolean DEBUG_METHODS2 = false;
    protected static final boolean DEBUG_METHODS3 = false;
    protected static final boolean DEBUG_MATCH = false;
    protected static final boolean DEBUG_STACK = false;
    protected static final boolean DEBUG_ANY = false;
    protected static final int MATCHED = 1;
    protected static final int MATCHED_ATTRIBUTE = 3;
    protected static final int MATCHED_DESCENDANT = 5;
    protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;
    private XPath.LocationPath[] fLocationPaths;
    private int[] fMatched;
    protected Object fMatchedString;
    private IntStack[] fStepIndexes;
    private int[] fCurrentStep;
    private int[] fNoMatchDepth;
    final QName fQName = new QName();

    public XPathMatcher(XPath xpath) {
        this.fLocationPaths = xpath.getLocationPaths();
        this.fStepIndexes = new IntStack[this.fLocationPaths.length];
        for (int i2 = 0; i2 < this.fStepIndexes.length; i2++) {
            this.fStepIndexes[i2] = new IntStack();
        }
        this.fCurrentStep = new int[this.fLocationPaths.length];
        this.fNoMatchDepth = new int[this.fLocationPaths.length];
        this.fMatched = new int[this.fLocationPaths.length];
    }

    public boolean isMatched() {
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            if ((this.fMatched[i2] & 1) == 1 && (this.fMatched[i2] & 13) != 13 && (this.fNoMatchDepth[i2] == 0 || (this.fMatched[i2] & 5) == 5)) {
                return true;
            }
        }
        return false;
    }

    protected void handleContent(XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType) {
    }

    protected void matched(Object actualValue, short valueType, ShortList itemValueType, boolean isNil) {
    }

    public void startDocumentFragment() {
        this.fMatchedString = null;
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            this.fStepIndexes[i2].clear();
            this.fCurrentStep[i2] = 0;
            this.fNoMatchDepth[i2] = 0;
            this.fMatched[i2] = 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startElement(com.sun.org.apache.xerces.internal.xni.QName r7, com.sun.org.apache.xerces.internal.xni.XMLAttributes r8) {
        /*
            Method dump skipped, instructions count: 710
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher.startElement(com.sun.org.apache.xerces.internal.xni.QName, com.sun.org.apache.xerces.internal.xni.XMLAttributes):void");
    }

    public void endElement(QName element, XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType) {
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            this.fCurrentStep[i2] = this.fStepIndexes[i2].pop();
            if (this.fNoMatchDepth[i2] > 0) {
                int[] iArr = this.fNoMatchDepth;
                int i3 = i2;
                iArr[i3] = iArr[i3] - 1;
            } else {
                int j2 = 0;
                while (j2 < i2 && (this.fMatched[j2] & 1) != 1) {
                    j2++;
                }
                if (j2 >= i2 && this.fMatched[j2] != 0 && (this.fMatched[j2] & 3) != 3) {
                    handleContent(type, nillable, value, valueType, itemValueType);
                    this.fMatched[i2] = 0;
                }
            }
        }
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        String s2 = super.toString();
        int index2 = s2.lastIndexOf(46);
        if (index2 != -1) {
            s2 = s2.substring(index2 + 1);
        }
        str.append(s2);
        for (int i2 = 0; i2 < this.fLocationPaths.length; i2++) {
            str.append('[');
            XPath.Step[] steps = this.fLocationPaths[i2].steps;
            for (int j2 = 0; j2 < steps.length; j2++) {
                if (j2 == this.fCurrentStep[i2]) {
                    str.append('^');
                }
                str.append(steps[j2].toString());
                if (j2 < steps.length - 1) {
                    str.append('/');
                }
            }
            if (this.fCurrentStep[i2] == steps.length) {
                str.append('^');
            }
            str.append(']');
            str.append(',');
        }
        return str.toString();
    }

    private String normalize(String s2) {
        StringBuffer str = new StringBuffer();
        int length = s2.length();
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = s2.charAt(i2);
            switch (c2) {
                case '\n':
                    str.append("\\n");
                    break;
                default:
                    str.append(c2);
                    break;
            }
        }
        return str.toString();
    }
}
