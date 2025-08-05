package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLTextAreaElementImpl.class */
public class HTMLTextAreaElementImpl extends HTMLElementImpl implements HTMLTextAreaElement {
    static native boolean getAutofocusImpl(long j2);

    static native void setAutofocusImpl(long j2, boolean z2);

    static native String getDirNameImpl(long j2);

    static native void setDirNameImpl(long j2, String str);

    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native int getMaxLengthImpl(long j2);

    static native void setMaxLengthImpl(long j2, int i2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getPlaceholderImpl(long j2);

    static native void setPlaceholderImpl(long j2, String str);

    static native boolean getReadOnlyImpl(long j2);

    static native void setReadOnlyImpl(long j2, boolean z2);

    static native boolean getRequiredImpl(long j2);

    static native void setRequiredImpl(long j2, boolean z2);

    static native int getRowsImpl(long j2);

    static native void setRowsImpl(long j2, int i2);

    static native int getColsImpl(long j2);

    static native void setColsImpl(long j2, int i2);

    static native String getWrapImpl(long j2);

    static native void setWrapImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native String getDefaultValueImpl(long j2);

    static native void setDefaultValueImpl(long j2, String str);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native int getTextLengthImpl(long j2);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native long getLabelsImpl(long j2);

    static native int getSelectionStartImpl(long j2);

    static native void setSelectionStartImpl(long j2, int i2);

    static native int getSelectionEndImpl(long j2);

    static native void setSelectionEndImpl(long j2, int i2);

    static native String getSelectionDirectionImpl(long j2);

    static native void setSelectionDirectionImpl(long j2, String str);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    static native String getAutocompleteImpl(long j2);

    static native void setAutocompleteImpl(long j2, String str);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    static native void selectImpl(long j2);

    static native void setRangeTextImpl(long j2, String str);

    static native void setRangeTextExImpl(long j2, String str, int i2, int i3, String str2);

    static native void setSelectionRangeImpl(long j2, int i2, int i3, String str);

    HTMLTextAreaElementImpl(long peer) {
        super(peer);
    }

    static HTMLTextAreaElement getImpl(long peer) {
        return (HTMLTextAreaElement) create(peer);
    }

    public boolean getAutofocus() {
        return getAutofocusImpl(getPeer());
    }

    public void setAutofocus(boolean value) {
        setAutofocusImpl(getPeer(), value);
    }

    public String getDirName() {
        return getDirNameImpl(getPeer());
    }

    public void setDirName(String value) {
        setDirNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    public int getMaxLength() {
        return getMaxLengthImpl(getPeer());
    }

    public void setMaxLength(int value) throws DOMException {
        setMaxLengthImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public String getPlaceholder() {
        return getPlaceholderImpl(getPeer());
    }

    public void setPlaceholder(String value) {
        setPlaceholderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public boolean getReadOnly() {
        return getReadOnlyImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setReadOnly(boolean value) {
        setReadOnlyImpl(getPeer(), value);
    }

    public boolean getRequired() {
        return getRequiredImpl(getPeer());
    }

    public void setRequired(boolean value) {
        setRequiredImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public int getRows() {
        return getRowsImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setRows(int value) {
        setRowsImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public int getCols() {
        return getColsImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setCols(int value) {
        setColsImpl(getPeer(), value);
    }

    public String getWrap() {
        return getWrapImpl(getPeer());
    }

    public void setWrap(String value) {
        setWrapImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public String getDefaultValue() {
        return getDefaultValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setDefaultValue(String value) {
        setDefaultValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    public int getTextLength() {
        return getTextLengthImpl(getPeer());
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

    public int getSelectionStart() {
        return getSelectionStartImpl(getPeer());
    }

    public void setSelectionStart(int value) {
        setSelectionStartImpl(getPeer(), value);
    }

    public int getSelectionEnd() {
        return getSelectionEndImpl(getPeer());
    }

    public void setSelectionEnd(int value) {
        setSelectionEndImpl(getPeer(), value);
    }

    public String getSelectionDirection() {
        return getSelectionDirectionImpl(getPeer());
    }

    public void setSelectionDirection(String value) {
        setSelectionDirectionImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }

    public String getAutocomplete() {
        return getAutocompleteImpl(getPeer());
    }

    public void setAutocomplete(String value) {
        setAutocompleteImpl(getPeer(), value);
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }

    @Override // org.w3c.dom.html.HTMLTextAreaElement
    public void select() {
        selectImpl(getPeer());
    }

    public void setRangeText(String replacement) throws DOMException {
        setRangeTextImpl(getPeer(), replacement);
    }

    public void setRangeTextEx(String replacement, int start, int end, String selectionMode) throws DOMException {
        setRangeTextExImpl(getPeer(), replacement, start, end, selectionMode);
    }

    public void setSelectionRange(int start, int end, String direction) {
        setSelectionRangeImpl(getPeer(), start, end, direction);
    }
}
