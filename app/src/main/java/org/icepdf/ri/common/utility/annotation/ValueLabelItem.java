package org.icepdf.ri.common.utility.annotation;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/ValueLabelItem.class */
public class ValueLabelItem {
    private Object value;
    private String label;

    public ValueLabelItem(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}
