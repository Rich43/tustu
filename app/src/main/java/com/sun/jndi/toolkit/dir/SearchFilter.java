package com.sun.jndi.toolkit.dir;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InvalidSearchFilterException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/jndi/toolkit/dir/SearchFilter.class */
public class SearchFilter implements AttrFilter {
    String filter;
    int pos = 0;
    private StringFilter rootFilter;
    protected static final boolean debug = false;
    protected static final char BEGIN_FILTER_TOKEN = '(';
    protected static final char END_FILTER_TOKEN = ')';
    protected static final char AND_TOKEN = '&';
    protected static final char OR_TOKEN = '|';
    protected static final char NOT_TOKEN = '!';
    protected static final char EQUAL_TOKEN = '=';
    protected static final char APPROX_TOKEN = '~';
    protected static final char LESS_TOKEN = '<';
    protected static final char GREATER_TOKEN = '>';
    protected static final char EXTEND_TOKEN = ':';
    protected static final char WILDCARD_TOKEN = '*';
    static final int EQUAL_MATCH = 1;
    static final int APPROX_MATCH = 2;
    static final int GREATER_MATCH = 3;
    static final int LESS_MATCH = 4;

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/SearchFilter$StringFilter.class */
    interface StringFilter extends AttrFilter {
        void parse() throws InvalidSearchFilterException;
    }

    public SearchFilter(String str) throws InvalidSearchFilterException {
        this.filter = str;
        normalizeFilter();
        this.rootFilter = createNextFilter();
    }

    @Override // com.sun.jndi.toolkit.dir.AttrFilter
    public boolean check(Attributes attributes) throws NamingException {
        if (attributes == null) {
            return false;
        }
        return this.rootFilter.check(attributes);
    }

    protected void normalizeFilter() {
        skipWhiteSpace();
        if (getCurrentChar() != '(') {
            this.filter = '(' + this.filter + ')';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skipWhiteSpace() {
        while (Character.isWhitespace(getCurrentChar())) {
            consumeChar();
        }
    }

    protected StringFilter createNextFilter() throws InvalidSearchFilterException {
        StringFilter atomicFilter;
        skipWhiteSpace();
        try {
            if (getCurrentChar() != '(') {
                throw new InvalidSearchFilterException("expected \"(\" at position " + this.pos);
            }
            consumeChar();
            skipWhiteSpace();
            switch (getCurrentChar()) {
                case '!':
                    atomicFilter = new NotFilter();
                    atomicFilter.parse();
                    break;
                case '&':
                    atomicFilter = new CompoundFilter(true);
                    atomicFilter.parse();
                    break;
                case '|':
                    atomicFilter = new CompoundFilter(false);
                    atomicFilter.parse();
                    break;
                default:
                    atomicFilter = new AtomicFilter();
                    atomicFilter.parse();
                    break;
            }
            skipWhiteSpace();
            if (getCurrentChar() != ')') {
                throw new InvalidSearchFilterException("expected \")\" at position " + this.pos);
            }
            consumeChar();
            return atomicFilter;
        } catch (InvalidSearchFilterException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new InvalidSearchFilterException("Unable to parse character " + this.pos + " in \"" + this.filter + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    protected char getCurrentChar() {
        return this.filter.charAt(this.pos);
    }

    protected char relCharAt(int i2) {
        return this.filter.charAt(this.pos + i2);
    }

    protected void consumeChar() {
        this.pos++;
    }

    protected void consumeChars(int i2) {
        this.pos += i2;
    }

    protected int relIndexOf(int i2) {
        return this.filter.indexOf(i2, this.pos) - this.pos;
    }

    protected String relSubstring(int i2, int i3) {
        return this.filter.substring(i2 + this.pos, i3 + this.pos);
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/SearchFilter$CompoundFilter.class */
    final class CompoundFilter implements StringFilter {
        private Vector<StringFilter> subFilters = new Vector<>();
        private boolean polarity;

        CompoundFilter(boolean z2) {
            this.polarity = z2;
        }

        @Override // com.sun.jndi.toolkit.dir.SearchFilter.StringFilter
        public void parse() throws InvalidSearchFilterException {
            SearchFilter.this.consumeChar();
            while (SearchFilter.this.getCurrentChar() != ')') {
                this.subFilters.addElement(SearchFilter.this.createNextFilter());
                SearchFilter.this.skipWhiteSpace();
            }
        }

        @Override // com.sun.jndi.toolkit.dir.AttrFilter
        public boolean check(Attributes attributes) throws NamingException {
            for (int i2 = 0; i2 < this.subFilters.size(); i2++) {
                if (this.subFilters.elementAt(i2).check(attributes) != this.polarity) {
                    return !this.polarity;
                }
            }
            return this.polarity;
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/SearchFilter$NotFilter.class */
    final class NotFilter implements StringFilter {
        private StringFilter filter;

        NotFilter() {
        }

        @Override // com.sun.jndi.toolkit.dir.SearchFilter.StringFilter
        public void parse() throws InvalidSearchFilterException {
            SearchFilter.this.consumeChar();
            this.filter = SearchFilter.this.createNextFilter();
        }

        @Override // com.sun.jndi.toolkit.dir.AttrFilter
        public boolean check(Attributes attributes) throws NamingException {
            return !this.filter.check(attributes);
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/toolkit/dir/SearchFilter$AtomicFilter.class */
    final class AtomicFilter implements StringFilter {
        private String attrID;
        private String value;
        private int matchType;

        AtomicFilter() {
        }

        @Override // com.sun.jndi.toolkit.dir.SearchFilter.StringFilter
        public void parse() throws OperationNotSupportedException, InvalidSearchFilterException {
            SearchFilter.this.skipWhiteSpace();
            try {
                int iRelIndexOf = SearchFilter.this.relIndexOf(41);
                int iRelIndexOf2 = SearchFilter.this.relIndexOf(61);
                switch (SearchFilter.this.relCharAt(iRelIndexOf2 - 1)) {
                    case ':':
                        throw new OperationNotSupportedException("Extensible match not supported");
                    case '<':
                        this.matchType = 4;
                        this.attrID = SearchFilter.this.relSubstring(0, iRelIndexOf2 - 1);
                        this.value = SearchFilter.this.relSubstring(iRelIndexOf2 + 1, iRelIndexOf);
                        break;
                    case '>':
                        this.matchType = 3;
                        this.attrID = SearchFilter.this.relSubstring(0, iRelIndexOf2 - 1);
                        this.value = SearchFilter.this.relSubstring(iRelIndexOf2 + 1, iRelIndexOf);
                        break;
                    case '~':
                        this.matchType = 2;
                        this.attrID = SearchFilter.this.relSubstring(0, iRelIndexOf2 - 1);
                        this.value = SearchFilter.this.relSubstring(iRelIndexOf2 + 1, iRelIndexOf);
                        break;
                    default:
                        this.matchType = 1;
                        this.attrID = SearchFilter.this.relSubstring(0, iRelIndexOf2);
                        this.value = SearchFilter.this.relSubstring(iRelIndexOf2 + 1, iRelIndexOf);
                        break;
                }
                this.attrID = this.attrID.trim();
                this.value = this.value.trim();
                SearchFilter.this.consumeChars(iRelIndexOf);
            } catch (Exception e2) {
                InvalidSearchFilterException invalidSearchFilterException = new InvalidSearchFilterException("Unable to parse character " + SearchFilter.this.pos + " in \"" + SearchFilter.this.filter + PdfOps.DOUBLE_QUOTE__TOKEN);
                invalidSearchFilterException.setRootCause(e2);
                throw invalidSearchFilterException;
            }
        }

        @Override // com.sun.jndi.toolkit.dir.AttrFilter
        public boolean check(Attributes attributes) {
            try {
                Attribute attribute = attributes.get(this.attrID);
                if (attribute == null) {
                    return false;
                }
                NamingEnumeration<?> all = attribute.getAll();
                while (all.hasMoreElements()) {
                    String string = all.nextElement2().toString();
                    switch (this.matchType) {
                        case 1:
                        case 2:
                            if (!substringMatch(this.value, string)) {
                                break;
                            } else {
                                return true;
                            }
                        case 3:
                            if (string.compareTo(this.value) < 0) {
                                break;
                            } else {
                                return true;
                            }
                        case 4:
                            if (string.compareTo(this.value) > 0) {
                                break;
                            } else {
                                return true;
                            }
                    }
                }
                return false;
            } catch (NamingException e2) {
                return false;
            }
        }

        private boolean substringMatch(String str, String str2) {
            if (str.equals(new Character('*').toString())) {
                return true;
            }
            if (str.indexOf(42) == -1) {
                return str.equalsIgnoreCase(str2);
            }
            int length = 0;
            StringTokenizer stringTokenizer = new StringTokenizer(str, "*", false);
            if (str.charAt(0) != '*' && !str2.toLowerCase(Locale.ENGLISH).startsWith(stringTokenizer.nextToken().toLowerCase(Locale.ENGLISH))) {
                return false;
            }
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                int iIndexOf = str2.toLowerCase(Locale.ENGLISH).indexOf(strNextToken.toLowerCase(Locale.ENGLISH), length);
                if (iIndexOf == -1) {
                    return false;
                }
                length = iIndexOf + strNextToken.length();
            }
            if (str.charAt(str.length() - 1) != '*' && length != str2.length()) {
                return false;
            }
            return true;
        }
    }

    public static String format(Attributes attributes) throws NamingException {
        if (attributes == null || attributes.size() == 0) {
            return "objectClass=*";
        }
        String str = "(& ";
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        while (all.hasMore()) {
            Attribute next = all.next();
            if (next.size() == 0 || (next.size() == 1 && next.get() == null)) {
                str = str + "(" + next.getID() + "=*)";
            } else {
                NamingEnumeration<?> all2 = next.getAll();
                while (all2.hasMore()) {
                    String encodedStringRep = getEncodedStringRep(all2.next());
                    if (encodedStringRep != null) {
                        str = str + "(" + next.getID() + "=" + encodedStringRep + ")";
                    }
                }
            }
        }
        return str + ")";
    }

    private static void hexDigit(StringBuffer stringBuffer, byte b2) {
        char c2;
        char c3;
        char c4 = (char) ((b2 >> 4) & 15);
        if (c4 > '\t') {
            c2 = (char) ((c4 - '\n') + 65);
        } else {
            c2 = (char) (c4 + '0');
        }
        stringBuffer.append(c2);
        char c5 = (char) (b2 & 15);
        if (c5 > '\t') {
            c3 = (char) ((c5 - '\n') + 65);
        } else {
            c3 = (char) (c5 + '0');
        }
        stringBuffer.append(c3);
    }

    private static String getEncodedStringRep(Object obj) throws NamingException {
        String string;
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            StringBuffer stringBuffer = new StringBuffer(bArr.length * 3);
            for (byte b2 : bArr) {
                stringBuffer.append('\\');
                hexDigit(stringBuffer, b2);
            }
            return stringBuffer.toString();
        }
        if (!(obj instanceof String)) {
            string = obj.toString();
        } else {
            string = (String) obj;
        }
        int length = string.length();
        StringBuffer stringBuffer2 = new StringBuffer(length);
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = string.charAt(i2);
            switch (cCharAt) {
                case 0:
                    stringBuffer2.append("\\00");
                    break;
                case '(':
                    stringBuffer2.append("\\28");
                    break;
                case ')':
                    stringBuffer2.append("\\29");
                    break;
                case '*':
                    stringBuffer2.append("\\2a");
                    break;
                case '\\':
                    stringBuffer2.append("\\5c");
                    break;
                default:
                    stringBuffer2.append(cCharAt);
                    break;
            }
        }
        return stringBuffer2.toString();
    }

    public static int findUnescaped(char c2, String str, int i2) {
        int length = str.length();
        while (i2 < length) {
            int iIndexOf = str.indexOf(c2, i2);
            if (iIndexOf == i2 || iIndexOf == -1 || str.charAt(iIndexOf - 1) != '\\') {
                return iIndexOf;
            }
            i2 = iIndexOf + 1;
        }
        return -1;
    }

    public static String format(String str, Object[] objArr) throws NamingException {
        int i2 = 0;
        StringBuffer stringBuffer = new StringBuffer(str.length());
        while (true) {
            int iFindUnescaped = findUnescaped('{', str, i2);
            if (iFindUnescaped >= 0) {
                int i3 = iFindUnescaped + 1;
                int iIndexOf = str.indexOf(125, i3);
                if (iIndexOf < 0) {
                    throw new InvalidSearchFilterException("unbalanced {: " + str);
                }
                try {
                    int i4 = Integer.parseInt(str.substring(i3, iIndexOf));
                    if (i4 >= objArr.length) {
                        throw new InvalidSearchFilterException("number exceeds argument list: " + i4);
                    }
                    stringBuffer.append(str.substring(i2, iFindUnescaped)).append(getEncodedStringRep(objArr[i4]));
                    i2 = iIndexOf + 1;
                } catch (NumberFormatException e2) {
                    throw new InvalidSearchFilterException("integer expected inside {}: " + str);
                }
            } else {
                if (i2 < str.length()) {
                    stringBuffer.append(str.substring(i2));
                }
                return stringBuffer.toString();
            }
        }
    }

    public static Attributes selectAttributes(Attributes attributes, String[] strArr) throws NamingException {
        if (strArr == null) {
            return attributes;
        }
        BasicAttributes basicAttributes = new BasicAttributes();
        for (String str : strArr) {
            Attribute attribute = attributes.get(str);
            if (attribute != null) {
                basicAttributes.put(attribute);
            }
        }
        return basicAttributes;
    }
}
