package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSStyleDeclarationImpl.class */
public class CSSStyleDeclarationImpl implements CSSStyleDeclaration {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String getCssTextImpl(long j2);

    static native void setCssTextImpl(long j2, String str);

    static native int getLengthImpl(long j2);

    static native long getParentRuleImpl(long j2);

    static native String getPropertyValueImpl(long j2, String str);

    static native long getPropertyCSSValueImpl(long j2, String str);

    static native String removePropertyImpl(long j2, String str);

    static native String getPropertyPriorityImpl(long j2, String str);

    static native void setPropertyImpl(long j2, String str, String str2, String str3);

    static native String itemImpl(long j2, int i2);

    static native String getPropertyShorthandImpl(long j2, String str);

    static native boolean isPropertyImplicitImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSStyleDeclarationImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            CSSStyleDeclarationImpl.dispose(this.peer);
        }
    }

    CSSStyleDeclarationImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static CSSStyleDeclaration create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new CSSStyleDeclarationImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof CSSStyleDeclarationImpl) && this.peer == ((CSSStyleDeclarationImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(CSSStyleDeclaration arg) {
        if (arg == null) {
            return 0L;
        }
        return ((CSSStyleDeclarationImpl) arg).getPeer();
    }

    static CSSStyleDeclaration getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public String getCssText() {
        return getCssTextImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public void setCssText(String value) throws DOMException {
        setCssTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public CSSRule getParentRule() {
        return CSSRuleImpl.getImpl(getParentRuleImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public String getPropertyValue(String propertyName) {
        return getPropertyValueImpl(getPeer(), propertyName);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public CSSValue getPropertyCSSValue(String propertyName) {
        return CSSValueImpl.getImpl(getPropertyCSSValueImpl(getPeer(), propertyName));
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public String removeProperty(String propertyName) throws DOMException {
        return removePropertyImpl(getPeer(), propertyName);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public String getPropertyPriority(String propertyName) {
        return getPropertyPriorityImpl(getPeer(), propertyName);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public void setProperty(String propertyName, String value, String priority) throws DOMException {
        setPropertyImpl(getPeer(), propertyName, value, priority);
    }

    @Override // org.w3c.dom.css.CSSStyleDeclaration
    public String item(int index) {
        return itemImpl(getPeer(), index);
    }

    public String getPropertyShorthand(String propertyName) {
        return getPropertyShorthandImpl(getPeer(), propertyName);
    }

    public boolean isPropertyImplicit(String propertyName) {
        return isPropertyImplicitImpl(getPeer(), propertyName);
    }
}
