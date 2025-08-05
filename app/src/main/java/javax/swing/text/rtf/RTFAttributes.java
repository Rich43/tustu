package javax.swing.text.rtf;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFAttributes.class */
class RTFAttributes {
    static RTFAttribute[] attributes;

    RTFAttributes() {
    }

    static {
        Vector vector = new Vector();
        vector.addElement(new BooleanAttribute(0, StyleConstants.Italic, PdfOps.i_TOKEN));
        vector.addElement(new BooleanAttribute(0, StyleConstants.Bold, PdfOps.b_TOKEN));
        vector.addElement(new BooleanAttribute(0, StyleConstants.Underline, "ul"));
        vector.addElement(NumericAttribute.NewTwips(1, StyleConstants.LeftIndent, "li", 0.0f, 0));
        vector.addElement(NumericAttribute.NewTwips(1, StyleConstants.RightIndent, PdfOps.ri_TOKEN, 0.0f, 0));
        vector.addElement(NumericAttribute.NewTwips(1, StyleConstants.FirstLineIndent, "fi", 0.0f, 0));
        vector.addElement(new AssertiveAttribute(1, StyleConstants.Alignment, "ql", 0));
        vector.addElement(new AssertiveAttribute(1, StyleConstants.Alignment, "qr", 2));
        vector.addElement(new AssertiveAttribute(1, StyleConstants.Alignment, "qc", 1));
        vector.addElement(new AssertiveAttribute(1, StyleConstants.Alignment, "qj", 3));
        vector.addElement(NumericAttribute.NewTwips(1, StyleConstants.SpaceAbove, "sa", 0));
        vector.addElement(NumericAttribute.NewTwips(1, StyleConstants.SpaceBelow, "sb", 0));
        vector.addElement(new AssertiveAttribute(4, "tab_alignment", "tqr", 1));
        vector.addElement(new AssertiveAttribute(4, "tab_alignment", "tqc", 2));
        vector.addElement(new AssertiveAttribute(4, "tab_alignment", "tqdec", 4));
        vector.addElement(new AssertiveAttribute(4, "tab_leader", "tldot", 1));
        vector.addElement(new AssertiveAttribute(4, "tab_leader", "tlhyph", 2));
        vector.addElement(new AssertiveAttribute(4, "tab_leader", "tlul", 3));
        vector.addElement(new AssertiveAttribute(4, "tab_leader", "tlth", 4));
        vector.addElement(new AssertiveAttribute(4, "tab_leader", "tleq", 5));
        vector.addElement(new BooleanAttribute(0, "caps", "caps"));
        vector.addElement(new BooleanAttribute(0, "outl", "outl"));
        vector.addElement(new BooleanAttribute(0, "scaps", "scaps"));
        vector.addElement(new BooleanAttribute(0, "shad", "shad"));
        vector.addElement(new BooleanAttribute(0, PdfOps.v_TOKEN, PdfOps.v_TOKEN));
        vector.addElement(new BooleanAttribute(0, "strike", "strike"));
        vector.addElement(new BooleanAttribute(0, "deleted", "deleted"));
        vector.addElement(new AssertiveAttribute(3, "saveformat", "defformat", "RTF"));
        vector.addElement(new AssertiveAttribute(3, "landscape", "landscape"));
        vector.addElement(NumericAttribute.NewTwips(3, "paperw", "paperw", 12240));
        vector.addElement(NumericAttribute.NewTwips(3, "paperh", "paperh", 15840));
        vector.addElement(NumericAttribute.NewTwips(3, "margl", "margl", 1800));
        vector.addElement(NumericAttribute.NewTwips(3, "margr", "margr", 1800));
        vector.addElement(NumericAttribute.NewTwips(3, "margt", "margt", 1440));
        vector.addElement(NumericAttribute.NewTwips(3, "margb", "margb", 1440));
        vector.addElement(NumericAttribute.NewTwips(3, "gutter", "gutter", 0));
        vector.addElement(new AssertiveAttribute(1, (Object) "widowctrl", "nowidctlpar", (Object) false));
        vector.addElement(new AssertiveAttribute(1, (Object) "widowctrl", "widctlpar", (Object) true));
        vector.addElement(new AssertiveAttribute(3, (Object) "widowctrl", "widowctrl", (Object) true));
        RTFAttribute[] rTFAttributeArr = new RTFAttribute[vector.size()];
        vector.copyInto(rTFAttributeArr);
        attributes = rTFAttributeArr;
    }

    static Dictionary<String, RTFAttribute> attributesByKeyword() {
        Hashtable hashtable = new Hashtable(attributes.length);
        for (RTFAttribute rTFAttribute : attributes) {
            hashtable.put(rTFAttribute.rtfName(), rTFAttribute);
        }
        return hashtable;
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFAttributes$GenericAttribute.class */
    static abstract class GenericAttribute {
        int domain;
        Object swingName;
        String rtfName;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract boolean set(MutableAttributeSet mutableAttributeSet);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract boolean set(MutableAttributeSet mutableAttributeSet, int i2);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract boolean setDefault(MutableAttributeSet mutableAttributeSet);

        protected GenericAttribute(int i2, Object obj, String str) {
            this.domain = i2;
            this.swingName = obj;
            this.rtfName = str;
        }

        public int domain() {
            return this.domain;
        }

        public Object swingName() {
            return this.swingName;
        }

        public String rtfName() {
            return this.rtfName;
        }

        public boolean write(AttributeSet attributeSet, RTFGenerator rTFGenerator, boolean z2) throws IOException {
            return writeValue(attributeSet.getAttribute(this.swingName), rTFGenerator, z2);
        }

        public boolean writeValue(Object obj, RTFGenerator rTFGenerator, boolean z2) throws IOException {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFAttributes$BooleanAttribute.class */
    static class BooleanAttribute extends GenericAttribute implements RTFAttribute {
        boolean rtfDefault;
        boolean swingDefault;
        protected static final Boolean True = true;
        protected static final Boolean False = false;

        public BooleanAttribute(int i2, Object obj, String str, boolean z2, boolean z3) {
            super(i2, obj, str);
            this.swingDefault = z2;
            this.rtfDefault = z3;
        }

        public BooleanAttribute(int i2, Object obj, String str) {
            super(i2, obj, str);
            this.swingDefault = false;
            this.rtfDefault = false;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet) {
            mutableAttributeSet.addAttribute(this.swingName, True);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet, int i2) {
            mutableAttributeSet.addAttribute(this.swingName, i2 != 0 ? True : False);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean setDefault(MutableAttributeSet mutableAttributeSet) {
            if (this.swingDefault != this.rtfDefault || mutableAttributeSet.getAttribute(this.swingName) != null) {
                mutableAttributeSet.addAttribute(this.swingName, Boolean.valueOf(this.rtfDefault));
                return true;
            }
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean writeValue(Object obj, RTFGenerator rTFGenerator, boolean z2) throws IOException {
            Boolean boolValueOf;
            if (obj == null) {
                boolValueOf = Boolean.valueOf(this.swingDefault);
            } else {
                boolValueOf = (Boolean) obj;
            }
            if (z2 || boolValueOf.booleanValue() != this.rtfDefault) {
                if (boolValueOf.booleanValue()) {
                    rTFGenerator.writeControlWord(this.rtfName);
                    return true;
                }
                rTFGenerator.writeControlWord(this.rtfName, 0);
                return true;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFAttributes$AssertiveAttribute.class */
    static class AssertiveAttribute extends GenericAttribute implements RTFAttribute {
        Object swingValue;

        public AssertiveAttribute(int i2, Object obj, String str) {
            super(i2, obj, str);
            this.swingValue = true;
        }

        public AssertiveAttribute(int i2, Object obj, String str, Object obj2) {
            super(i2, obj, str);
            this.swingValue = obj2;
        }

        public AssertiveAttribute(int i2, Object obj, String str, int i3) {
            super(i2, obj, str);
            this.swingValue = Integer.valueOf(i3);
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet) {
            if (this.swingValue == null) {
                mutableAttributeSet.removeAttribute(this.swingName);
                return true;
            }
            mutableAttributeSet.addAttribute(this.swingName, this.swingValue);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet, int i2) {
            return false;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean setDefault(MutableAttributeSet mutableAttributeSet) {
            mutableAttributeSet.removeAttribute(this.swingName);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean writeValue(Object obj, RTFGenerator rTFGenerator, boolean z2) throws IOException {
            if (obj == null) {
                return !z2;
            }
            if (!obj.equals(this.swingValue)) {
                return !z2;
            }
            rTFGenerator.writeControlWord(this.rtfName);
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/rtf/RTFAttributes$NumericAttribute.class */
    static class NumericAttribute extends GenericAttribute implements RTFAttribute {
        int rtfDefault;
        Number swingDefault;
        float scale;

        protected NumericAttribute(int i2, Object obj, String str) {
            super(i2, obj, str);
            this.rtfDefault = 0;
            this.swingDefault = null;
            this.scale = 1.0f;
        }

        public NumericAttribute(int i2, Object obj, String str, int i3, int i4) {
            this(i2, obj, str, Integer.valueOf(i3), i4, 1.0f);
        }

        public NumericAttribute(int i2, Object obj, String str, Number number, int i3, float f2) {
            super(i2, obj, str);
            this.swingDefault = number;
            this.rtfDefault = i3;
            this.scale = f2;
        }

        public static NumericAttribute NewTwips(int i2, Object obj, String str, float f2, int i3) {
            return new NumericAttribute(i2, obj, str, new Float(f2), i3, 20.0f);
        }

        public static NumericAttribute NewTwips(int i2, Object obj, String str, int i3) {
            return new NumericAttribute(i2, obj, str, null, i3, 20.0f);
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet) {
            return false;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean set(MutableAttributeSet mutableAttributeSet, int i2) {
            Object f2;
            if (this.scale == 1.0f) {
                f2 = Integer.valueOf(i2);
            } else {
                f2 = new Float(i2 / this.scale);
            }
            mutableAttributeSet.addAttribute(this.swingName, f2);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean setDefault(MutableAttributeSet mutableAttributeSet) {
            Number number = (Number) mutableAttributeSet.getAttribute(this.swingName);
            if (number == null) {
                number = this.swingDefault;
            }
            if (number != null && ((this.scale == 1.0f && number.intValue() == this.rtfDefault) || Math.round(number.floatValue() * this.scale) == this.rtfDefault)) {
                return true;
            }
            set(mutableAttributeSet, this.rtfDefault);
            return true;
        }

        @Override // javax.swing.text.rtf.RTFAttributes.GenericAttribute, javax.swing.text.rtf.RTFAttribute
        public boolean writeValue(Object obj, RTFGenerator rTFGenerator, boolean z2) throws IOException {
            Number number = (Number) obj;
            if (number == null) {
                number = this.swingDefault;
            }
            if (number == null) {
                return true;
            }
            int iRound = Math.round(number.floatValue() * this.scale);
            if (z2 || iRound != this.rtfDefault) {
                rTFGenerator.writeControlWord(this.rtfName, iRound);
                return true;
            }
            return true;
        }
    }
}
