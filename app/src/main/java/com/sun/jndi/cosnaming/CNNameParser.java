package com.sun.jndi.cosnaming;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.naming.CompositeName;
import javax.naming.CompoundName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import org.omg.CosNaming.NameComponent;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/CNNameParser.class */
public final class CNNameParser implements NameParser {
    private static final Properties mySyntax = new Properties();
    private static final char kindSeparator = '.';
    private static final char compSeparator = '/';
    private static final char escapeChar = '\\';

    static {
        mySyntax.put("jndi.syntax.direction", "left_to_right");
        mySyntax.put("jndi.syntax.separator", "/");
        mySyntax.put("jndi.syntax.escape", FXMLLoader.ESCAPE_PREFIX);
    }

    @Override // javax.naming.NameParser
    public Name parse(String str) throws NamingException {
        return new CNCompoundName(insStringToStringifiedComps(str).elements());
    }

    static NameComponent[] nameToCosName(Name name) throws InvalidNameException {
        int size = name.size();
        if (size == 0) {
            return new NameComponent[0];
        }
        NameComponent[] nameComponentArr = new NameComponent[size];
        for (int i2 = 0; i2 < size; i2++) {
            nameComponentArr[i2] = parseComponent(name.get(i2));
        }
        return nameComponentArr;
    }

    static String cosNameToInsString(NameComponent[] nameComponentArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < nameComponentArr.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append('/');
            }
            stringBuffer.append(stringifyComponent(nameComponentArr[i2]));
        }
        return stringBuffer.toString();
    }

    static Name cosNameToName(NameComponent[] nameComponentArr) {
        CompositeName compositeName = new CompositeName();
        for (int i2 = 0; nameComponentArr != null && i2 < nameComponentArr.length; i2++) {
            try {
                compositeName.add(stringifyComponent(nameComponentArr[i2]));
            } catch (InvalidNameException e2) {
            }
        }
        return compositeName;
    }

    private static Vector<String> insStringToStringifiedComps(String str) throws InvalidNameException {
        int length = str.length();
        Vector<String> vector = new Vector<>(10);
        char[] cArr = new char[length];
        char[] cArr2 = new char[length];
        int i2 = 0;
        while (i2 < length) {
            int i3 = 0;
            int i4 = 0;
            boolean z2 = true;
            while (i2 < length && str.charAt(i2) != '/') {
                if (str.charAt(i2) == '\\') {
                    if (i2 + 1 >= length) {
                        throw new InvalidNameException(str + ": unescaped \\ at end of component");
                    }
                    if (isMeta(str.charAt(i2 + 1))) {
                        int i5 = i2 + 1;
                        if (z2) {
                            int i6 = i4;
                            i4++;
                            i2 = i5 + 1;
                            cArr[i6] = str.charAt(i5);
                        } else {
                            int i7 = i3;
                            i3++;
                            i2 = i5 + 1;
                            cArr2[i7] = str.charAt(i5);
                        }
                    } else {
                        throw new InvalidNameException(str + ": invalid character being escaped");
                    }
                } else if (z2 && str.charAt(i2) == '.') {
                    i2++;
                    z2 = false;
                } else if (z2) {
                    int i8 = i4;
                    i4++;
                    int i9 = i2;
                    i2++;
                    cArr[i8] = str.charAt(i9);
                } else {
                    int i10 = i3;
                    i3++;
                    int i11 = i2;
                    i2++;
                    cArr2[i10] = str.charAt(i11);
                }
            }
            vector.addElement(stringifyComponent(new NameComponent(new String(cArr, 0, i4), new String(cArr2, 0, i3))));
            if (i2 < length) {
                i2++;
            }
        }
        return vector;
    }

    private static NameComponent parseComponent(String str) throws InvalidNameException {
        NameComponent nameComponent = new NameComponent();
        int i2 = -1;
        int length = str.length();
        int i3 = 0;
        char[] cArr = new char[length];
        boolean z2 = false;
        for (int i4 = 0; i4 < length && i2 < 0; i4++) {
            if (z2) {
                int i5 = i3;
                i3++;
                cArr[i5] = str.charAt(i4);
                z2 = false;
            } else if (str.charAt(i4) == '\\') {
                if (i4 + 1 >= length) {
                    throw new InvalidNameException(str + ": unescaped \\ at end of component");
                }
                if (isMeta(str.charAt(i4 + 1))) {
                    z2 = true;
                } else {
                    throw new InvalidNameException(str + ": invalid character being escaped");
                }
            } else if (str.charAt(i4) == '.') {
                i2 = i4;
            } else {
                int i6 = i3;
                i3++;
                cArr[i6] = str.charAt(i4);
            }
        }
        nameComponent.id = new String(cArr, 0, i3);
        if (i2 < 0) {
            nameComponent.kind = "";
        } else {
            int i7 = 0;
            boolean z3 = false;
            for (int i8 = i2 + 1; i8 < length; i8++) {
                if (z3) {
                    int i9 = i7;
                    i7++;
                    cArr[i9] = str.charAt(i8);
                    z3 = false;
                } else if (str.charAt(i8) == '\\') {
                    if (i8 + 1 >= length) {
                        throw new InvalidNameException(str + ": unescaped \\ at end of component");
                    }
                    if (isMeta(str.charAt(i8 + 1))) {
                        z3 = true;
                    } else {
                        throw new InvalidNameException(str + ": invalid character being escaped");
                    }
                } else {
                    int i10 = i7;
                    i7++;
                    cArr[i10] = str.charAt(i8);
                }
            }
            nameComponent.kind = new String(cArr, 0, i7);
        }
        return nameComponent;
    }

    private static String stringifyComponent(NameComponent nameComponent) {
        StringBuffer stringBuffer = new StringBuffer(escape(nameComponent.id));
        if (nameComponent.kind != null && !nameComponent.kind.equals("")) {
            stringBuffer.append('.' + escape(nameComponent.kind));
        }
        if (stringBuffer.length() == 0) {
            return ".";
        }
        return stringBuffer.toString();
    }

    private static String escape(String str) {
        if (str.indexOf(46) < 0 && str.indexOf(47) < 0 && str.indexOf(92) < 0) {
            return str;
        }
        int length = str.length();
        int i2 = 0;
        char[] cArr = new char[length + length];
        for (int i3 = 0; i3 < length; i3++) {
            if (isMeta(str.charAt(i3))) {
                int i4 = i2;
                i2++;
                cArr[i4] = '\\';
            }
            int i5 = i2;
            i2++;
            cArr[i5] = str.charAt(i3);
        }
        return new String(cArr, 0, i2);
    }

    private static boolean isMeta(char c2) {
        switch (c2) {
            case '.':
            case '/':
            case '\\':
                return true;
            default:
                return false;
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/cosnaming/CNNameParser$CNCompoundName.class */
    static final class CNCompoundName extends CompoundName {
        private static final long serialVersionUID = -6599252802678482317L;

        CNCompoundName(Enumeration<String> enumeration) {
            super(enumeration, CNNameParser.mySyntax);
        }

        @Override // javax.naming.CompoundName, javax.naming.Name
        public Object clone() {
            return new CNCompoundName(getAll());
        }

        @Override // javax.naming.CompoundName, javax.naming.Name
        public Name getPrefix(int i2) {
            return new CNCompoundName(super.getPrefix(i2).getAll());
        }

        @Override // javax.naming.CompoundName, javax.naming.Name
        public Name getSuffix(int i2) {
            return new CNCompoundName(super.getSuffix(i2).getAll());
        }

        @Override // javax.naming.CompoundName
        public String toString() {
            try {
                return CNNameParser.cosNameToInsString(CNNameParser.nameToCosName(this));
            } catch (InvalidNameException e2) {
                return super.toString();
            }
        }
    }
}
