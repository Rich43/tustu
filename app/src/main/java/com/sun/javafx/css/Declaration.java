package com.sun.javafx.css;

import com.sun.javafx.css.converters.URLConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Declaration.class */
public final class Declaration {
    final String property;
    final ParsedValueImpl parsedValue;
    final boolean important;
    Rule rule;

    public Declaration(String propertyName, ParsedValueImpl parsedValue, boolean important) {
        this.property = propertyName;
        this.parsedValue = parsedValue;
        this.important = important;
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName cannot be null");
        }
        if (parsedValue == null) {
            throw new IllegalArgumentException("parsedValue cannot be null");
        }
    }

    public ParsedValue getParsedValue() {
        return this.parsedValue;
    }

    ParsedValueImpl getParsedValueImpl() {
        return this.parsedValue;
    }

    public String getProperty() {
        return this.property;
    }

    public Rule getRule() {
        return this.rule;
    }

    public boolean isImportant() {
        return this.important;
    }

    private StyleOrigin getOrigin() {
        Rule rule = getRule();
        if (rule != null) {
            return rule.getOrigin();
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Declaration other = (Declaration) obj;
        if (this.important != other.important || getOrigin() != other.getOrigin()) {
            return false;
        }
        if (this.property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!this.property.equals(other.property)) {
            return false;
        }
        if (this.parsedValue == other.parsedValue) {
            return true;
        }
        if (this.parsedValue == null || !this.parsedValue.equals(other.parsedValue)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = (89 * 5) + (this.property != null ? this.property.hashCode() : 0);
        return (89 * ((89 * hash) + (this.parsedValue != null ? this.parsedValue.hashCode() : 0))) + (this.important ? 1 : 0);
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder(this.property);
        sbuf.append(": ");
        sbuf.append((Object) this.parsedValue);
        if (this.important) {
            sbuf.append(" !important");
        }
        return sbuf.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void fixUrl(String stylesheetUrl) {
        if (stylesheetUrl == null) {
            return;
        }
        StyleConverter converter = this.parsedValue.getConverter();
        if (converter == URLConverter.getInstance()) {
            ParsedValue[] values = (ParsedValue[]) this.parsedValue.getValue();
            values[1] = new ParsedValueImpl(stylesheetUrl, null);
        } else if (converter == URLConverter.SequenceConverter.getInstance()) {
            ParsedValue<ParsedValue[], String>[] layers = (ParsedValue[]) this.parsedValue.getValue();
            for (ParsedValue<ParsedValue[], String> parsedValue : layers) {
                ParsedValue[] values2 = parsedValue.getValue();
                values2[1] = new ParsedValueImpl(stylesheetUrl, null);
            }
        }
    }

    final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        os.writeShort(stringStore.addString(getProperty()));
        getParsedValueImpl().writeBinary(os, stringStore);
        os.writeBoolean(isImportant());
    }

    static Declaration readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        String propertyName = strings[is.readShort()];
        ParsedValueImpl parsedValue = ParsedValueImpl.readBinary(bssVersion, is, strings);
        boolean important = is.readBoolean();
        return new Declaration(propertyName, parsedValue, important);
    }
}
