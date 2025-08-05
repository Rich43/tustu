package java.text;

import java.io.Serializable;
import java.text.AttributedCharacterIterator;

/* loaded from: rt.jar:java/text/Format.class */
public abstract class Format implements Serializable, Cloneable {
    private static final long serialVersionUID = -299282585814624189L;

    /* loaded from: rt.jar:java/text/Format$FieldDelegate.class */
    interface FieldDelegate {
        void formatted(Field field, Object obj, int i2, int i3, StringBuffer stringBuffer);

        void formatted(int i2, Field field, Object obj, int i3, int i4, StringBuffer stringBuffer);
    }

    public abstract StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Object parseObject(String str, ParsePosition parsePosition);

    protected Format() {
    }

    public final String format(Object obj) {
        return format(obj, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return createAttributedCharacterIterator(format(obj));
    }

    public Object parseObject(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Object object = parseObject(str, parsePosition);
        if (parsePosition.index == 0) {
            throw new ParseException("Format.parseObject(String) failed", parsePosition.errorIndex);
        }
        return object;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    AttributedCharacterIterator createAttributedCharacterIterator(String str) {
        return new AttributedString(str).getIterator();
    }

    AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator[] attributedCharacterIteratorArr) {
        return new AttributedString(attributedCharacterIteratorArr).getIterator();
    }

    AttributedCharacterIterator createAttributedCharacterIterator(String str, AttributedCharacterIterator.Attribute attribute, Object obj) {
        AttributedString attributedString = new AttributedString(str);
        attributedString.addAttribute(attribute, obj);
        return attributedString.getIterator();
    }

    AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator attributedCharacterIterator, AttributedCharacterIterator.Attribute attribute, Object obj) {
        AttributedString attributedString = new AttributedString(attributedCharacterIterator);
        attributedString.addAttribute(attribute, obj);
        return attributedString.getIterator();
    }

    /* loaded from: rt.jar:java/text/Format$Field.class */
    public static class Field extends AttributedCharacterIterator.Attribute {
        private static final long serialVersionUID = 276966692217360283L;

        protected Field(String str) {
            super(str);
        }
    }
}
