package javafx.util.converter;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.text.Format;
import java.text.ParsePosition;
import javafx.beans.NamedArg;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/FormatStringConverter.class */
public class FormatStringConverter<T> extends StringConverter<T> {
    final Format format;

    public FormatStringConverter(@NamedArg(Constants.ATTRNAME_FORMAT) Format format) {
        this.format = format;
    }

    @Override // javafx.util.StringConverter
    public T fromString(String str) {
        if (str == null) {
            return null;
        }
        String strTrim = str.trim();
        if (strTrim.length() < 1) {
            return null;
        }
        Format format = getFormat();
        ParsePosition parsePosition = new ParsePosition(0);
        T t2 = (T) format.parseObject(strTrim, parsePosition);
        if (parsePosition.getIndex() != strTrim.length()) {
            throw new RuntimeException("Parsed string not according to the format");
        }
        return t2;
    }

    @Override // javafx.util.StringConverter
    public String toString(T value) {
        if (value == null) {
            return "";
        }
        Format _format = getFormat();
        return _format.format(value);
    }

    protected Format getFormat() {
        return this.format;
    }
}
