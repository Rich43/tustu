package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLSelectElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLSelectElementImpl.class */
public class HTMLSelectElementImpl extends HTMLElementImpl implements HTMLSelectElement {
    static native boolean getAutofocusImpl(long j2);

    static native void setAutofocusImpl(long j2, boolean z2);

    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native boolean getMultipleImpl(long j2);

    static native void setMultipleImpl(long j2, boolean z2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native boolean getRequiredImpl(long j2);

    static native void setRequiredImpl(long j2, boolean z2);

    static native int getSizeImpl(long j2);

    static native void setSizeImpl(long j2, int i2);

    static native String getTypeImpl(long j2);

    static native long getOptionsImpl(long j2);

    static native int getLengthImpl(long j2);

    static native long getSelectedOptionsImpl(long j2);

    static native int getSelectedIndexImpl(long j2);

    static native void setSelectedIndexImpl(long j2, int i2);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native long getLabelsImpl(long j2);

    static native String getAutocompleteImpl(long j2);

    static native void setAutocompleteImpl(long j2, String str);

    static native long itemImpl(long j2, int i2);

    static native long namedItemImpl(long j2, String str);

    static native void addImpl(long j2, long j3, long j4);

    static native void removeImpl(long j2, int i2);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    HTMLSelectElementImpl(long peer) {
        super(peer);
    }

    static HTMLSelectElement getImpl(long peer) {
        return (HTMLSelectElement) create(peer);
    }

    public boolean getAutofocus() {
        return getAutofocusImpl(getPeer());
    }

    public void setAutofocus(boolean value) {
        setAutofocusImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public boolean getMultiple() {
        return getMultipleImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setMultiple(boolean value) {
        setMultipleImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public boolean getRequired() {
        return getRequiredImpl(getPeer());
    }

    public void setRequired(boolean value) {
        setRequiredImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public int getSize() {
        return getSizeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setSize(int value) {
        setSizeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public HTMLOptionsCollectionImpl getOptions() {
        return HTMLOptionsCollectionImpl.getImpl(getOptionsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    public HTMLCollection getSelectedOptions() {
        return HTMLCollectionImpl.getImpl(getSelectedOptionsImpl(getPeer()));
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public int getSelectedIndex() {
        return getSelectedIndexImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setSelectedIndex(int value) {
        setSelectedIndexImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    public boolean getWillValidate() {
        return getWillValidateImpl(getPeer());
    }

    public String getValidationMessage() {
        return getValidationMessageImpl(getPeer());
    }

    public NodeList getLabels() {
        return NodeListImpl.getImpl(getLabelsImpl(getPeer()));
    }

    public String getAutocomplete() {
        return getAutocompleteImpl(getPeer());
    }

    public void setAutocomplete(String value) {
        setAutocompleteImpl(getPeer(), value);
    }

    public Node item(int index) {
        return NodeImpl.getImpl(itemImpl(getPeer(), index));
    }

    public Node namedItem(String name) {
        return NodeImpl.getImpl(namedItemImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void add(HTMLElement element, HTMLElement before) throws DOMException {
        addImpl(getPeer(), HTMLElementImpl.getPeer(element), HTMLElementImpl.getPeer(before));
    }

    @Override // org.w3c.dom.html.HTMLSelectElement
    public void remove(int index) {
        removeImpl(getPeer(), index);
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }
}
