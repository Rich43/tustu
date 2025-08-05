package java.text;

import java.text.Format;

/* loaded from: rt.jar:java/text/FieldPosition.class */
public class FieldPosition {
    int field;
    int endIndex;
    int beginIndex;
    private Format.Field attribute;

    public FieldPosition(int i2) {
        this.field = 0;
        this.endIndex = 0;
        this.beginIndex = 0;
        this.field = i2;
    }

    public FieldPosition(Format.Field field) {
        this(field, -1);
    }

    public FieldPosition(Format.Field field, int i2) {
        this.field = 0;
        this.endIndex = 0;
        this.beginIndex = 0;
        this.attribute = field;
        this.field = i2;
    }

    public Format.Field getFieldAttribute() {
        return this.attribute;
    }

    public int getField() {
        return this.field;
    }

    public int getBeginIndex() {
        return this.beginIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setBeginIndex(int i2) {
        this.beginIndex = i2;
    }

    public void setEndIndex(int i2) {
        this.endIndex = i2;
    }

    Format.FieldDelegate getFieldDelegate() {
        return new Delegate();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FieldPosition)) {
            return false;
        }
        FieldPosition fieldPosition = (FieldPosition) obj;
        if (this.attribute == null) {
            if (fieldPosition.attribute != null) {
                return false;
            }
        } else if (!this.attribute.equals(fieldPosition.attribute)) {
            return false;
        }
        return this.beginIndex == fieldPosition.beginIndex && this.endIndex == fieldPosition.endIndex && this.field == fieldPosition.field;
    }

    public int hashCode() {
        return (this.field << 24) | (this.beginIndex << 16) | this.endIndex;
    }

    public String toString() {
        return getClass().getName() + "[field=" + this.field + ",attribute=" + ((Object) this.attribute) + ",beginIndex=" + this.beginIndex + ",endIndex=" + this.endIndex + ']';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean matchesField(Format.Field field) {
        if (this.attribute != null) {
            return this.attribute.equals(field);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean matchesField(Format.Field field, int i2) {
        if (this.attribute != null) {
            return this.attribute.equals(field);
        }
        return i2 == this.field;
    }

    /* loaded from: rt.jar:java/text/FieldPosition$Delegate.class */
    private class Delegate implements Format.FieldDelegate {
        private boolean encounteredField;

        private Delegate() {
        }

        @Override // java.text.Format.FieldDelegate
        public void formatted(Format.Field field, Object obj, int i2, int i3, StringBuffer stringBuffer) {
            if (!this.encounteredField && FieldPosition.this.matchesField(field)) {
                FieldPosition.this.setBeginIndex(i2);
                FieldPosition.this.setEndIndex(i3);
                this.encounteredField = i2 != i3;
            }
        }

        @Override // java.text.Format.FieldDelegate
        public void formatted(int i2, Format.Field field, Object obj, int i3, int i4, StringBuffer stringBuffer) {
            if (!this.encounteredField && FieldPosition.this.matchesField(field, i2)) {
                FieldPosition.this.setBeginIndex(i3);
                FieldPosition.this.setEndIndex(i4);
                this.encounteredField = i3 != i4;
            }
        }
    }
}
