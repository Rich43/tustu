package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/HTMLInputElementImpl.class */
public class HTMLInputElementImpl extends HTMLElementImpl implements HTMLInputElement {
    static native String getAcceptImpl(long j2);

    static native void setAcceptImpl(long j2, String str);

    static native String getAltImpl(long j2);

    static native void setAltImpl(long j2, String str);

    static native String getAutocompleteImpl(long j2);

    static native void setAutocompleteImpl(long j2, String str);

    static native boolean getAutofocusImpl(long j2);

    static native void setAutofocusImpl(long j2, boolean z2);

    static native boolean getDefaultCheckedImpl(long j2);

    static native void setDefaultCheckedImpl(long j2, boolean z2);

    static native boolean getCheckedImpl(long j2);

    static native void setCheckedImpl(long j2, boolean z2);

    static native String getDirNameImpl(long j2);

    static native void setDirNameImpl(long j2, String str);

    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getFormImpl(long j2);

    static native String getFormActionImpl(long j2);

    static native void setFormActionImpl(long j2, String str);

    static native String getFormEnctypeImpl(long j2);

    static native void setFormEnctypeImpl(long j2, String str);

    static native String getFormMethodImpl(long j2);

    static native void setFormMethodImpl(long j2, String str);

    static native boolean getFormNoValidateImpl(long j2);

    static native void setFormNoValidateImpl(long j2, boolean z2);

    static native String getFormTargetImpl(long j2);

    static native void setFormTargetImpl(long j2, String str);

    static native int getHeightImpl(long j2);

    static native void setHeightImpl(long j2, int i2);

    static native boolean getIndeterminateImpl(long j2);

    static native void setIndeterminateImpl(long j2, boolean z2);

    static native String getMaxImpl(long j2);

    static native void setMaxImpl(long j2, String str);

    static native int getMaxLengthImpl(long j2);

    static native void setMaxLengthImpl(long j2, int i2);

    static native String getMinImpl(long j2);

    static native void setMinImpl(long j2, String str);

    static native boolean getMultipleImpl(long j2);

    static native void setMultipleImpl(long j2, boolean z2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getPatternImpl(long j2);

    static native void setPatternImpl(long j2, String str);

    static native String getPlaceholderImpl(long j2);

    static native void setPlaceholderImpl(long j2, String str);

    static native boolean getReadOnlyImpl(long j2);

    static native void setReadOnlyImpl(long j2, boolean z2);

    static native boolean getRequiredImpl(long j2);

    static native void setRequiredImpl(long j2, boolean z2);

    static native String getSizeImpl(long j2);

    static native void setSizeImpl(long j2, String str);

    static native String getSrcImpl(long j2);

    static native void setSrcImpl(long j2, String str);

    static native String getStepImpl(long j2);

    static native void setStepImpl(long j2, String str);

    static native String getTypeImpl(long j2);

    static native void setTypeImpl(long j2, String str);

    static native String getDefaultValueImpl(long j2);

    static native void setDefaultValueImpl(long j2, String str);

    static native String getValueImpl(long j2);

    static native void setValueImpl(long j2, String str);

    static native long getValueAsDateImpl(long j2);

    static native void setValueAsDateImpl(long j2, long j3);

    static native double getValueAsNumberImpl(long j2);

    static native void setValueAsNumberImpl(long j2, double d2);

    static native int getWidthImpl(long j2);

    static native void setWidthImpl(long j2, int i2);

    static native boolean getWillValidateImpl(long j2);

    static native String getValidationMessageImpl(long j2);

    static native long getLabelsImpl(long j2);

    static native String getAlignImpl(long j2);

    static native void setAlignImpl(long j2, String str);

    static native String getUseMapImpl(long j2);

    static native void setUseMapImpl(long j2, String str);

    static native boolean getIncrementalImpl(long j2);

    static native void setIncrementalImpl(long j2, boolean z2);

    static native String getAccessKeyImpl(long j2);

    static native void setAccessKeyImpl(long j2, String str);

    static native void stepUpImpl(long j2, int i2);

    static native void stepDownImpl(long j2, int i2);

    static native boolean checkValidityImpl(long j2);

    static native void setCustomValidityImpl(long j2, String str);

    static native void selectImpl(long j2);

    static native void setRangeTextImpl(long j2, String str);

    static native void setRangeTextExImpl(long j2, String str, int i2, int i3, String str2);

    static native void clickImpl(long j2);

    static native void setValueForUserImpl(long j2, String str);

    HTMLInputElementImpl(long peer) {
        super(peer);
    }

    static HTMLInputElement getImpl(long peer) {
        return (HTMLInputElement) create(peer);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getAccept() {
        return getAcceptImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setAccept(String value) {
        setAcceptImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getAlt() {
        return getAltImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setAlt(String value) {
        setAltImpl(getPeer(), value);
    }

    public String getAutocomplete() {
        return getAutocompleteImpl(getPeer());
    }

    public void setAutocomplete(String value) {
        setAutocompleteImpl(getPeer(), value);
    }

    public boolean getAutofocus() {
        return getAutofocusImpl(getPeer());
    }

    public void setAutofocus(boolean value) {
        setAutofocusImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public boolean getDefaultChecked() {
        return getDefaultCheckedImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setDefaultChecked(boolean value) {
        setDefaultCheckedImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public boolean getChecked() {
        return getCheckedImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setChecked(boolean value) {
        setCheckedImpl(getPeer(), value);
    }

    public String getDirName() {
        return getDirNameImpl(getPeer());
    }

    public void setDirName(String value) {
        setDirNameImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public HTMLFormElement getForm() {
        return HTMLFormElementImpl.getImpl(getFormImpl(getPeer()));
    }

    public String getFormAction() {
        return getFormActionImpl(getPeer());
    }

    public void setFormAction(String value) {
        setFormActionImpl(getPeer(), value);
    }

    public String getFormEnctype() {
        return getFormEnctypeImpl(getPeer());
    }

    public void setFormEnctype(String value) {
        setFormEnctypeImpl(getPeer(), value);
    }

    public String getFormMethod() {
        return getFormMethodImpl(getPeer());
    }

    public void setFormMethod(String value) {
        setFormMethodImpl(getPeer(), value);
    }

    public boolean getFormNoValidate() {
        return getFormNoValidateImpl(getPeer());
    }

    public void setFormNoValidate(boolean value) {
        setFormNoValidateImpl(getPeer(), value);
    }

    public String getFormTarget() {
        return getFormTargetImpl(getPeer());
    }

    public void setFormTarget(String value) {
        setFormTargetImpl(getPeer(), value);
    }

    public int getHeight() {
        return getHeightImpl(getPeer());
    }

    public void setHeight(int value) {
        setHeightImpl(getPeer(), value);
    }

    public boolean getIndeterminate() {
        return getIndeterminateImpl(getPeer());
    }

    public void setIndeterminate(boolean value) {
        setIndeterminateImpl(getPeer(), value);
    }

    public String getMax() {
        return getMaxImpl(getPeer());
    }

    public void setMax(String value) {
        setMaxImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public int getMaxLength() {
        return getMaxLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setMaxLength(int value) throws DOMException {
        setMaxLengthImpl(getPeer(), value);
    }

    public String getMin() {
        return getMinImpl(getPeer());
    }

    public void setMin(String value) {
        setMinImpl(getPeer(), value);
    }

    public boolean getMultiple() {
        return getMultipleImpl(getPeer());
    }

    public void setMultiple(boolean value) {
        setMultipleImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getName() {
        return getNameImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public String getPattern() {
        return getPatternImpl(getPeer());
    }

    public void setPattern(String value) {
        setPatternImpl(getPeer(), value);
    }

    public String getPlaceholder() {
        return getPlaceholderImpl(getPeer());
    }

    public void setPlaceholder(String value) {
        setPlaceholderImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public boolean getReadOnly() {
        return getReadOnlyImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setReadOnly(boolean value) {
        setReadOnlyImpl(getPeer(), value);
    }

    public boolean getRequired() {
        return getRequiredImpl(getPeer());
    }

    public void setRequired(boolean value) {
        setRequiredImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getSize() {
        return getSizeImpl(getPeer()) + "";
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setSize(String value) {
        setSizeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getSrc() {
        return getSrcImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setSrc(String value) {
        setSrcImpl(getPeer(), value);
    }

    public String getStep() {
        return getStepImpl(getPeer());
    }

    public void setStep(String value) {
        setStepImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getType() {
        return getTypeImpl(getPeer());
    }

    public void setType(String value) {
        setTypeImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getDefaultValue() {
        return getDefaultValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setDefaultValue(String value) {
        setDefaultValueImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getValue() {
        return getValueImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setValue(String value) {
        setValueImpl(getPeer(), value);
    }

    public long getValueAsDate() {
        return getValueAsDateImpl(getPeer());
    }

    public void setValueAsDate(long value) throws DOMException {
        setValueAsDateImpl(getPeer(), value);
    }

    public double getValueAsNumber() {
        return getValueAsNumberImpl(getPeer());
    }

    public void setValueAsNumber(double value) throws DOMException {
        setValueAsNumberImpl(getPeer(), value);
    }

    public int getWidth() {
        return getWidthImpl(getPeer());
    }

    public void setWidth(int value) {
        setWidthImpl(getPeer(), value);
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

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getAlign() {
        return getAlignImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setAlign(String value) {
        setAlignImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public String getUseMap() {
        return getUseMapImpl(getPeer());
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void setUseMap(String value) {
        setUseMapImpl(getPeer(), value);
    }

    public boolean getIncremental() {
        return getIncrementalImpl(getPeer());
    }

    public void setIncremental(boolean value) {
        setIncrementalImpl(getPeer(), value);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public String getAccessKey() {
        return getAccessKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl, org.w3c.dom.html.HTMLAreaElement
    public void setAccessKey(String value) {
        setAccessKeyImpl(getPeer(), value);
    }

    public void stepUp(int n2) throws DOMException {
        stepUpImpl(getPeer(), n2);
    }

    public void stepDown(int n2) throws DOMException {
        stepDownImpl(getPeer(), n2);
    }

    public boolean checkValidity() {
        return checkValidityImpl(getPeer());
    }

    public void setCustomValidity(String error) {
        setCustomValidityImpl(getPeer(), error);
    }

    @Override // org.w3c.dom.html.HTMLInputElement
    public void select() {
        selectImpl(getPeer());
    }

    public void setRangeText(String replacement) throws DOMException {
        setRangeTextImpl(getPeer(), replacement);
    }

    public void setRangeTextEx(String replacement, int start, int end, String selectionMode) throws DOMException {
        setRangeTextExImpl(getPeer(), replacement, start, end, selectionMode);
    }

    @Override // com.sun.webkit.dom.HTMLElementImpl
    public void click() {
        clickImpl(getPeer());
    }

    public void setValueForUser(String value) {
        setValueForUserImpl(getPeer(), value);
    }
}
