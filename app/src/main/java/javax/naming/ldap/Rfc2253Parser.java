package javax.naming.ldap;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InvalidNameException;

/* loaded from: rt.jar:javax/naming/ldap/Rfc2253Parser.class */
final class Rfc2253Parser {
    private final String name;
    private final char[] chars;
    private final int len;
    private int cur = 0;

    Rfc2253Parser(String str) {
        this.name = str;
        this.len = str.length();
        this.chars = str.toCharArray();
    }

    List<Rdn> parseDn() throws InvalidNameException {
        this.cur = 0;
        ArrayList arrayList = new ArrayList((this.len / 3) + 10);
        if (this.len == 0) {
            return arrayList;
        }
        arrayList.add(doParse(new Rdn()));
        while (this.cur < this.len) {
            if (this.chars[this.cur] == ',' || this.chars[this.cur] == ';') {
                this.cur++;
                arrayList.add(0, doParse(new Rdn()));
            } else {
                throw new InvalidNameException("Invalid name: " + this.name);
            }
        }
        return arrayList;
    }

    Rdn parseRdn() throws InvalidNameException {
        return parseRdn(new Rdn());
    }

    Rdn parseRdn(Rdn rdn) throws InvalidNameException {
        Rdn rdnDoParse = doParse(rdn);
        if (this.cur < this.len) {
            throw new InvalidNameException("Invalid RDN: " + this.name);
        }
        return rdnDoParse;
    }

    private Rdn doParse(Rdn rdn) throws InvalidNameException {
        while (this.cur < this.len) {
            consumeWhitespace();
            String attrType = parseAttrType();
            consumeWhitespace();
            if (this.cur >= this.len || this.chars[this.cur] != '=') {
                throw new InvalidNameException("Invalid name: " + this.name);
            }
            this.cur++;
            consumeWhitespace();
            String attrValue = parseAttrValue();
            consumeWhitespace();
            rdn.put(attrType, Rdn.unescapeValue(attrValue));
            if (this.cur >= this.len || this.chars[this.cur] != '+') {
                break;
            }
            this.cur++;
        }
        rdn.sort();
        return rdn;
    }

    private String parseAttrType() throws InvalidNameException {
        int i2 = this.cur;
        while (this.cur < this.len) {
            char c2 = this.chars[this.cur];
            if (!Character.isLetterOrDigit(c2) && c2 != '.' && c2 != '-' && c2 != ' ') {
                break;
            }
            this.cur++;
        }
        while (this.cur > i2 && this.chars[this.cur - 1] == ' ') {
            this.cur--;
        }
        if (i2 == this.cur) {
            throw new InvalidNameException("Invalid name: " + this.name);
        }
        return new String(this.chars, i2, this.cur - i2);
    }

    private String parseAttrValue() throws InvalidNameException {
        if (this.cur < this.len && this.chars[this.cur] == '#') {
            return parseBinaryAttrValue();
        }
        if (this.cur < this.len && this.chars[this.cur] == '\"') {
            return parseQuotedAttrValue();
        }
        return parseStringAttrValue();
    }

    private String parseBinaryAttrValue() throws InvalidNameException {
        int i2 = this.cur;
        this.cur++;
        while (this.cur < this.len && Character.isLetterOrDigit(this.chars[this.cur])) {
            this.cur++;
        }
        return new String(this.chars, i2, this.cur - i2);
    }

    private String parseQuotedAttrValue() throws InvalidNameException {
        int i2 = this.cur;
        this.cur++;
        while (this.cur < this.len && this.chars[this.cur] != '\"') {
            if (this.chars[this.cur] == '\\') {
                this.cur++;
            }
            this.cur++;
        }
        if (this.cur >= this.len) {
            throw new InvalidNameException("Invalid name: " + this.name);
        }
        this.cur++;
        return new String(this.chars, i2, this.cur - i2);
    }

    private String parseStringAttrValue() throws InvalidNameException {
        int i2 = this.cur;
        int i3 = -1;
        while (this.cur < this.len && !atTerminator()) {
            if (this.chars[this.cur] == '\\') {
                this.cur++;
                i3 = this.cur;
            }
            this.cur++;
        }
        if (this.cur > this.len) {
            throw new InvalidNameException("Invalid name: " + this.name);
        }
        int i4 = this.cur;
        while (i4 > i2 && isWhitespace(this.chars[i4 - 1]) && i3 != i4 - 1) {
            i4--;
        }
        return new String(this.chars, i2, i4 - i2);
    }

    private void consumeWhitespace() {
        while (this.cur < this.len && isWhitespace(this.chars[this.cur])) {
            this.cur++;
        }
    }

    private boolean atTerminator() {
        return this.cur < this.len && (this.chars[this.cur] == ',' || this.chars[this.cur] == ';' || this.chars[this.cur] == '+');
    }

    private static boolean isWhitespace(char c2) {
        return c2 == ' ' || c2 == '\r';
    }
}
