package com.sun.jndi.ldap;

import java.util.Vector;
import javax.naming.ConfigurationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributeValueException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapSchemaParser.class */
final class LdapSchemaParser {
    private static final boolean debug = false;
    static final String OBJECTCLASSDESC_ATTR_ID = "objectClasses";
    static final String ATTRIBUTEDESC_ATTR_ID = "attributeTypes";
    static final String SYNTAXDESC_ATTR_ID = "ldapSyntaxes";
    static final String MATCHRULEDESC_ATTR_ID = "matchingRules";
    static final String OBJECTCLASS_DEFINITION_NAME = "ClassDefinition";
    private static final String[] CLASS_DEF_ATTRS = {"objectclass", OBJECTCLASS_DEFINITION_NAME};
    static final String ATTRIBUTE_DEFINITION_NAME = "AttributeDefinition";
    private static final String[] ATTR_DEF_ATTRS = {"objectclass", ATTRIBUTE_DEFINITION_NAME};
    static final String SYNTAX_DEFINITION_NAME = "SyntaxDefinition";
    private static final String[] SYNTAX_DEF_ATTRS = {"objectclass", SYNTAX_DEFINITION_NAME};
    static final String MATCHRULE_DEFINITION_NAME = "MatchingRule";
    private static final String[] MATCHRULE_DEF_ATTRS = {"objectclass", MATCHRULE_DEFINITION_NAME};
    private static final char SINGLE_QUOTE = '\'';
    private static final char WHSP = ' ';
    private static final char OID_LIST_BEGIN = '(';
    private static final char OID_LIST_END = ')';
    private static final char OID_SEPARATOR = '$';
    private static final String NUMERICOID_ID = "NUMERICOID";
    private static final String NAME_ID = "NAME";
    private static final String DESC_ID = "DESC";
    private static final String OBSOLETE_ID = "OBSOLETE";
    private static final String SUP_ID = "SUP";
    private static final String PRIVATE_ID = "X-";
    private static final String ABSTRACT_ID = "ABSTRACT";
    private static final String STRUCTURAL_ID = "STRUCTURAL";
    private static final String AUXILARY_ID = "AUXILIARY";
    private static final String MUST_ID = "MUST";
    private static final String MAY_ID = "MAY";
    private static final String EQUALITY_ID = "EQUALITY";
    private static final String ORDERING_ID = "ORDERING";
    private static final String SUBSTR_ID = "SUBSTR";
    private static final String SYNTAX_ID = "SYNTAX";
    private static final String SINGLE_VAL_ID = "SINGLE-VALUE";
    private static final String COLLECTIVE_ID = "COLLECTIVE";
    private static final String NO_USER_MOD_ID = "NO-USER-MODIFICATION";
    private static final String USAGE_ID = "USAGE";
    private static final String SCHEMA_TRUE_VALUE = "true";
    private boolean netscapeBug;

    LdapSchemaParser(boolean z2) {
        this.netscapeBug = z2;
    }

    static final void LDAP2JNDISchema(Attributes attributes, LdapSchemaCtx ldapSchemaCtx) throws NamingException {
        Attribute attribute = attributes.get(OBJECTCLASSDESC_ATTR_ID);
        if (attribute != null) {
            objectDescs2ClassDefs(attribute, ldapSchemaCtx);
        }
        Attribute attribute2 = attributes.get(ATTRIBUTEDESC_ATTR_ID);
        if (attribute2 != null) {
            attrDescs2AttrDefs(attribute2, ldapSchemaCtx);
        }
        Attribute attribute3 = attributes.get(SYNTAXDESC_ATTR_ID);
        if (attribute3 != null) {
            syntaxDescs2SyntaxDefs(attribute3, ldapSchemaCtx);
        }
        Attribute attribute4 = attributes.get(MATCHRULEDESC_ATTR_ID);
        if (attribute4 != null) {
            matchRuleDescs2MatchRuleDefs(attribute4, ldapSchemaCtx);
        }
    }

    private static final DirContext objectDescs2ClassDefs(Attribute attribute, LdapSchemaCtx ldapSchemaCtx) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        basicAttributes.put(CLASS_DEF_ATTRS[0], CLASS_DEF_ATTRS[1]);
        LdapSchemaCtx upVar = ldapSchemaCtx.setup(2, OBJECTCLASS_DEFINITION_NAME, basicAttributes);
        NamingEnumeration<?> all = attribute.getAll();
        while (all.hasMore()) {
            try {
                Object[] objArrDesc2Def = desc2Def((String) all.next());
                upVar.setup(6, (String) objArrDesc2Def[0], (Attributes) objArrDesc2Def[1]);
            } catch (NamingException e2) {
            }
        }
        return upVar;
    }

    private static final DirContext attrDescs2AttrDefs(Attribute attribute, LdapSchemaCtx ldapSchemaCtx) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        basicAttributes.put(ATTR_DEF_ATTRS[0], ATTR_DEF_ATTRS[1]);
        LdapSchemaCtx upVar = ldapSchemaCtx.setup(3, ATTRIBUTE_DEFINITION_NAME, basicAttributes);
        NamingEnumeration<?> all = attribute.getAll();
        while (all.hasMore()) {
            try {
                Object[] objArrDesc2Def = desc2Def((String) all.next());
                upVar.setup(7, (String) objArrDesc2Def[0], (Attributes) objArrDesc2Def[1]);
            } catch (NamingException e2) {
            }
        }
        return upVar;
    }

    private static final DirContext syntaxDescs2SyntaxDefs(Attribute attribute, LdapSchemaCtx ldapSchemaCtx) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        basicAttributes.put(SYNTAX_DEF_ATTRS[0], SYNTAX_DEF_ATTRS[1]);
        LdapSchemaCtx upVar = ldapSchemaCtx.setup(4, SYNTAX_DEFINITION_NAME, basicAttributes);
        NamingEnumeration<?> all = attribute.getAll();
        while (all.hasMore()) {
            try {
                Object[] objArrDesc2Def = desc2Def((String) all.next());
                upVar.setup(8, (String) objArrDesc2Def[0], (Attributes) objArrDesc2Def[1]);
            } catch (NamingException e2) {
            }
        }
        return upVar;
    }

    private static final DirContext matchRuleDescs2MatchRuleDefs(Attribute attribute, LdapSchemaCtx ldapSchemaCtx) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        basicAttributes.put(MATCHRULE_DEF_ATTRS[0], MATCHRULE_DEF_ATTRS[1]);
        LdapSchemaCtx upVar = ldapSchemaCtx.setup(5, MATCHRULE_DEFINITION_NAME, basicAttributes);
        NamingEnumeration<?> all = attribute.getAll();
        while (all.hasMore()) {
            try {
                Object[] objArrDesc2Def = desc2Def((String) all.next());
                upVar.setup(9, (String) objArrDesc2Def[0], (Attributes) objArrDesc2Def[1]);
            } catch (NamingException e2) {
            }
        }
        return upVar;
    }

    private static final Object[] desc2Def(String str) throws NamingException {
        BasicAttributes basicAttributes = new BasicAttributes(true);
        int[] iArr = {1};
        boolean z2 = true;
        Attribute numericOID = readNumericOID(str, iArr);
        String str2 = (String) numericOID.get(0);
        basicAttributes.put(numericOID);
        skipWhitespace(str, iArr);
        while (z2) {
            Attribute nextTag = readNextTag(str, iArr);
            basicAttributes.put(nextTag);
            if (nextTag.getID().equals(NAME_ID)) {
                str2 = (String) nextTag.get(0);
            }
            skipWhitespace(str, iArr);
            if (iArr[0] >= str.length() - 1) {
                z2 = false;
            }
        }
        return new Object[]{str2, basicAttributes};
    }

    private static final int findTrailingWhitespace(String str, int i2) {
        for (int i3 = i2; i3 > 0; i3--) {
            if (str.charAt(i3) != ' ') {
                return i3 + 1;
            }
        }
        return 0;
    }

    private static final void skipWhitespace(String str, int[] iArr) {
        for (int i2 = iArr[0]; i2 < str.length(); i2++) {
            if (str.charAt(i2) != ' ') {
                iArr[0] = i2;
                return;
            }
        }
    }

    private static final Attribute readNumericOID(String str, int[] iArr) throws NamingException {
        skipWhitespace(str, iArr);
        int i2 = iArr[0];
        int iIndexOf = str.indexOf(32, i2);
        if (iIndexOf == -1 || iIndexOf - i2 < 1) {
            throw new InvalidAttributeValueException("no numericoid found: " + str);
        }
        String strSubstring = str.substring(i2, iIndexOf);
        iArr[0] = iArr[0] + strSubstring.length();
        return new BasicAttribute(NUMERICOID_ID, strSubstring);
    }

    private static final Attribute readNextTag(String str, int[] iArr) throws NamingException {
        String strSubstring;
        skipWhitespace(str, iArr);
        int iIndexOf = str.indexOf(32, iArr[0]);
        if (iIndexOf < 0) {
            strSubstring = str.substring(iArr[0], str.length() - 1);
        } else {
            strSubstring = str.substring(iArr[0], iIndexOf);
        }
        String[] tag = readTag(strSubstring, str, iArr);
        if (tag.length < 0) {
            throw new InvalidAttributeValueException("no values for attribute \"" + strSubstring + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        BasicAttribute basicAttribute = new BasicAttribute(strSubstring, tag[0]);
        for (int i2 = 1; i2 < tag.length; i2++) {
            basicAttribute.add(tag[i2]);
        }
        return basicAttribute;
    }

    private static final String[] readTag(String str, String str2, int[] iArr) throws NamingException {
        iArr[0] = iArr[0] + str.length();
        skipWhitespace(str2, iArr);
        if (str.equals(NAME_ID)) {
            return readQDescrs(str2, iArr);
        }
        if (str.equals(DESC_ID)) {
            return readQDString(str2, iArr);
        }
        if (str.equals(EQUALITY_ID) || str.equals(ORDERING_ID) || str.equals(SUBSTR_ID) || str.equals(SYNTAX_ID)) {
            return readWOID(str2, iArr);
        }
        if (str.equals(OBSOLETE_ID) || str.equals(ABSTRACT_ID) || str.equals(STRUCTURAL_ID) || str.equals(AUXILARY_ID) || str.equals(SINGLE_VAL_ID) || str.equals(COLLECTIVE_ID) || str.equals(NO_USER_MOD_ID)) {
            return new String[]{"true"};
        }
        if (str.equals(SUP_ID) || str.equals(MUST_ID) || str.equals(MAY_ID) || str.equals(USAGE_ID)) {
            return readOIDs(str2, iArr);
        }
        return readQDStrings(str2, iArr);
    }

    private static final String[] readQDString(String str, int[] iArr) throws NamingException {
        int iIndexOf = str.indexOf(39, iArr[0]) + 1;
        int iIndexOf2 = str.indexOf(39, iIndexOf);
        if (iIndexOf == -1 || iIndexOf2 == -1 || iIndexOf == iIndexOf2) {
            throw new InvalidAttributeIdentifierException("malformed QDString: " + str);
        }
        if (str.charAt(iIndexOf - 1) != '\'') {
            throw new InvalidAttributeIdentifierException("qdstring has no end mark: " + str);
        }
        iArr[0] = iIndexOf2 + 1;
        return new String[]{str.substring(iIndexOf, iIndexOf2)};
    }

    private static final String[] readQDStrings(String str, int[] iArr) throws NamingException {
        return readQDescrs(str, iArr);
    }

    private static final String[] readQDescrs(String str, int[] iArr) throws NamingException {
        skipWhitespace(str, iArr);
        switch (str.charAt(iArr[0])) {
            case '\'':
                return readQDString(str, iArr);
            case '(':
                return readQDescrList(str, iArr);
            default:
                throw new InvalidAttributeValueException("unexpected oids string: " + str);
        }
    }

    private static final String[] readQDescrList(String str, int[] iArr) throws NamingException {
        Vector vector = new Vector(5);
        iArr[0] = iArr[0] + 1;
        skipWhitespace(str, iArr);
        int i2 = iArr[0];
        int iIndexOf = str.indexOf(41, i2);
        if (iIndexOf == -1) {
            throw new InvalidAttributeValueException("oidlist has no end mark: " + str);
        }
        while (i2 < iIndexOf) {
            vector.addElement(readQDString(str, iArr)[0]);
            skipWhitespace(str, iArr);
            i2 = iArr[0];
        }
        iArr[0] = iIndexOf + 1;
        String[] strArr = new String[vector.size()];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            strArr[i3] = (String) vector.elementAt(i3);
        }
        return strArr;
    }

    private static final String[] readWOID(String str, int[] iArr) throws NamingException {
        skipWhitespace(str, iArr);
        if (str.charAt(iArr[0]) == '\'') {
            return readQDString(str, iArr);
        }
        int i2 = iArr[0];
        int iIndexOf = str.indexOf(32, i2);
        if (iIndexOf == -1 || i2 == iIndexOf) {
            throw new InvalidAttributeIdentifierException("malformed OID: " + str);
        }
        iArr[0] = iIndexOf + 1;
        return new String[]{str.substring(i2, iIndexOf)};
    }

    private static final String[] readOIDs(String str, int[] iArr) throws NamingException {
        skipWhitespace(str, iArr);
        if (str.charAt(iArr[0]) != '(') {
            return readWOID(str, iArr);
        }
        Vector vector = new Vector(5);
        iArr[0] = iArr[0] + 1;
        skipWhitespace(str, iArr);
        int i2 = iArr[0];
        int iIndexOf = str.indexOf(41, i2);
        int iIndexOf2 = str.indexOf(36, i2);
        if (iIndexOf == -1) {
            throw new InvalidAttributeValueException("oidlist has no end mark: " + str);
        }
        if (iIndexOf2 == -1 || iIndexOf < iIndexOf2) {
            iIndexOf2 = iIndexOf;
        }
        while (iIndexOf2 < iIndexOf && iIndexOf2 > 0) {
            vector.addElement(str.substring(i2, findTrailingWhitespace(str, iIndexOf2 - 1)));
            iArr[0] = iIndexOf2 + 1;
            skipWhitespace(str, iArr);
            i2 = iArr[0];
            iIndexOf2 = str.indexOf(36, i2);
        }
        vector.addElement(str.substring(i2, findTrailingWhitespace(str, iIndexOf - 1)));
        iArr[0] = iIndexOf + 1;
        String[] strArr = new String[vector.size()];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            strArr[i3] = (String) vector.elementAt(i3);
        }
        return strArr;
    }

    private final String classDef2ObjectDesc(Attributes attributes) throws NamingException {
        StringBuffer stringBuffer = new StringBuffer("( ");
        Attribute attribute = attributes.get(NUMERICOID_ID);
        if (attribute != null) {
            stringBuffer.append(writeNumericOID(attribute));
            int i2 = 0 + 1;
            Attribute attribute2 = attributes.get(NAME_ID);
            if (attribute2 != null) {
                stringBuffer.append(writeQDescrs(attribute2));
                i2++;
            }
            Attribute attribute3 = attributes.get(DESC_ID);
            if (attribute3 != null) {
                stringBuffer.append(writeQDString(attribute3));
                i2++;
            }
            Attribute attribute4 = attributes.get(OBSOLETE_ID);
            if (attribute4 != null) {
                stringBuffer.append(writeBoolean(attribute4));
                i2++;
            }
            Attribute attribute5 = attributes.get(SUP_ID);
            if (attribute5 != null) {
                stringBuffer.append(writeOIDs(attribute5));
                i2++;
            }
            Attribute attribute6 = attributes.get(ABSTRACT_ID);
            if (attribute6 != null) {
                stringBuffer.append(writeBoolean(attribute6));
                i2++;
            }
            Attribute attribute7 = attributes.get(STRUCTURAL_ID);
            if (attribute7 != null) {
                stringBuffer.append(writeBoolean(attribute7));
                i2++;
            }
            Attribute attribute8 = attributes.get(AUXILARY_ID);
            if (attribute8 != null) {
                stringBuffer.append(writeBoolean(attribute8));
                i2++;
            }
            Attribute attribute9 = attributes.get(MUST_ID);
            if (attribute9 != null) {
                stringBuffer.append(writeOIDs(attribute9));
                i2++;
            }
            Attribute attribute10 = attributes.get(MAY_ID);
            if (attribute10 != null) {
                stringBuffer.append(writeOIDs(attribute10));
                i2++;
            }
            if (i2 < attributes.size()) {
                NamingEnumeration<? extends Attribute> all = attributes.getAll();
                while (all.hasMoreElements()) {
                    Attribute next = all.next();
                    String id = next.getID();
                    if (!id.equals(NUMERICOID_ID) && !id.equals(NAME_ID) && !id.equals(SUP_ID) && !id.equals(MAY_ID) && !id.equals(MUST_ID) && !id.equals(STRUCTURAL_ID) && !id.equals(DESC_ID) && !id.equals(AUXILARY_ID) && !id.equals(ABSTRACT_ID) && !id.equals(OBSOLETE_ID)) {
                        stringBuffer.append(writeQDStrings(next));
                    }
                }
            }
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
        throw new ConfigurationException("Class definition doesn'thave a numeric OID");
    }

    private final String attrDef2AttrDesc(Attributes attributes) throws NamingException {
        StringBuffer stringBuffer = new StringBuffer("( ");
        Attribute attribute = attributes.get(NUMERICOID_ID);
        if (attribute != null) {
            stringBuffer.append(writeNumericOID(attribute));
            int i2 = 0 + 1;
            Attribute attribute2 = attributes.get(NAME_ID);
            if (attribute2 != null) {
                stringBuffer.append(writeQDescrs(attribute2));
                i2++;
            }
            Attribute attribute3 = attributes.get(DESC_ID);
            if (attribute3 != null) {
                stringBuffer.append(writeQDString(attribute3));
                i2++;
            }
            Attribute attribute4 = attributes.get(OBSOLETE_ID);
            if (attribute4 != null) {
                stringBuffer.append(writeBoolean(attribute4));
                i2++;
            }
            Attribute attribute5 = attributes.get(SUP_ID);
            if (attribute5 != null) {
                stringBuffer.append(writeWOID(attribute5));
                i2++;
            }
            Attribute attribute6 = attributes.get(EQUALITY_ID);
            if (attribute6 != null) {
                stringBuffer.append(writeWOID(attribute6));
                i2++;
            }
            Attribute attribute7 = attributes.get(ORDERING_ID);
            if (attribute7 != null) {
                stringBuffer.append(writeWOID(attribute7));
                i2++;
            }
            Attribute attribute8 = attributes.get(SUBSTR_ID);
            if (attribute8 != null) {
                stringBuffer.append(writeWOID(attribute8));
                i2++;
            }
            Attribute attribute9 = attributes.get(SYNTAX_ID);
            if (attribute9 != null) {
                stringBuffer.append(writeWOID(attribute9));
                i2++;
            }
            Attribute attribute10 = attributes.get(SINGLE_VAL_ID);
            if (attribute10 != null) {
                stringBuffer.append(writeBoolean(attribute10));
                i2++;
            }
            Attribute attribute11 = attributes.get(COLLECTIVE_ID);
            if (attribute11 != null) {
                stringBuffer.append(writeBoolean(attribute11));
                i2++;
            }
            Attribute attribute12 = attributes.get(NO_USER_MOD_ID);
            if (attribute12 != null) {
                stringBuffer.append(writeBoolean(attribute12));
                i2++;
            }
            Attribute attribute13 = attributes.get(USAGE_ID);
            if (attribute13 != null) {
                stringBuffer.append(writeQDString(attribute13));
                i2++;
            }
            if (i2 < attributes.size()) {
                NamingEnumeration<? extends Attribute> all = attributes.getAll();
                while (all.hasMoreElements()) {
                    Attribute next = all.next();
                    String id = next.getID();
                    if (!id.equals(NUMERICOID_ID) && !id.equals(NAME_ID) && !id.equals(SYNTAX_ID) && !id.equals(DESC_ID) && !id.equals(SINGLE_VAL_ID) && !id.equals(EQUALITY_ID) && !id.equals(ORDERING_ID) && !id.equals(SUBSTR_ID) && !id.equals(NO_USER_MOD_ID) && !id.equals(USAGE_ID) && !id.equals(SUP_ID) && !id.equals(COLLECTIVE_ID) && !id.equals(OBSOLETE_ID)) {
                        stringBuffer.append(writeQDStrings(next));
                    }
                }
            }
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
        throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
    }

    private final String syntaxDef2SyntaxDesc(Attributes attributes) throws NamingException {
        StringBuffer stringBuffer = new StringBuffer("( ");
        Attribute attribute = attributes.get(NUMERICOID_ID);
        if (attribute != null) {
            stringBuffer.append(writeNumericOID(attribute));
            int i2 = 0 + 1;
            Attribute attribute2 = attributes.get(DESC_ID);
            if (attribute2 != null) {
                stringBuffer.append(writeQDString(attribute2));
                i2++;
            }
            if (i2 < attributes.size()) {
                NamingEnumeration<? extends Attribute> all = attributes.getAll();
                while (all.hasMoreElements()) {
                    Attribute next = all.next();
                    String id = next.getID();
                    if (!id.equals(NUMERICOID_ID) && !id.equals(DESC_ID)) {
                        stringBuffer.append(writeQDStrings(next));
                    }
                }
            }
            stringBuffer.append(")");
            return stringBuffer.toString();
        }
        throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
    }

    private final String matchRuleDef2MatchRuleDesc(Attributes attributes) throws NamingException {
        StringBuffer stringBuffer = new StringBuffer("( ");
        Attribute attribute = attributes.get(NUMERICOID_ID);
        if (attribute != null) {
            stringBuffer.append(writeNumericOID(attribute));
            int i2 = 0 + 1;
            Attribute attribute2 = attributes.get(NAME_ID);
            if (attribute2 != null) {
                stringBuffer.append(writeQDescrs(attribute2));
                i2++;
            }
            Attribute attribute3 = attributes.get(DESC_ID);
            if (attribute3 != null) {
                stringBuffer.append(writeQDString(attribute3));
                i2++;
            }
            Attribute attribute4 = attributes.get(OBSOLETE_ID);
            if (attribute4 != null) {
                stringBuffer.append(writeBoolean(attribute4));
                i2++;
            }
            Attribute attribute5 = attributes.get(SYNTAX_ID);
            if (attribute5 != null) {
                stringBuffer.append(writeWOID(attribute5));
                if (i2 + 1 < attributes.size()) {
                    NamingEnumeration<? extends Attribute> all = attributes.getAll();
                    while (all.hasMoreElements()) {
                        Attribute next = all.next();
                        String id = next.getID();
                        if (!id.equals(NUMERICOID_ID) && !id.equals(NAME_ID) && !id.equals(SYNTAX_ID) && !id.equals(DESC_ID) && !id.equals(OBSOLETE_ID)) {
                            stringBuffer.append(writeQDStrings(next));
                        }
                    }
                }
                stringBuffer.append(")");
                return stringBuffer.toString();
            }
            throw new ConfigurationException("Attribute type doesn'thave a syntax OID");
        }
        throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
    }

    private final String writeNumericOID(Attribute attribute) throws NamingException {
        if (attribute.size() != 1) {
            throw new InvalidAttributeValueException("A class definition must have exactly one numeric OID");
        }
        return ((String) attribute.get()) + ' ';
    }

    private final String writeWOID(Attribute attribute) throws NamingException {
        if (this.netscapeBug) {
            return writeQDString(attribute);
        }
        return attribute.getID() + ' ' + attribute.get() + ' ';
    }

    private final String writeQDString(Attribute attribute) throws NamingException {
        if (attribute.size() != 1) {
            throw new InvalidAttributeValueException(attribute.getID() + " must have exactly one value");
        }
        return attribute.getID() + " '" + attribute.get() + "' ";
    }

    private final String writeQDStrings(Attribute attribute) throws NamingException {
        return writeQDescrs(attribute);
    }

    private final String writeQDescrs(Attribute attribute) throws NamingException {
        switch (attribute.size()) {
            case 0:
                throw new InvalidAttributeValueException(attribute.getID() + "has no values");
            case 1:
                return writeQDString(attribute);
            default:
                StringBuffer stringBuffer = new StringBuffer(attribute.getID());
                stringBuffer.append(' ');
                stringBuffer.append('(');
                NamingEnumeration<?> all = attribute.getAll();
                while (all.hasMore()) {
                    stringBuffer.append(' ');
                    stringBuffer.append('\'');
                    stringBuffer.append((String) all.next());
                    stringBuffer.append('\'');
                    stringBuffer.append(' ');
                }
                stringBuffer.append(')');
                stringBuffer.append(' ');
                return stringBuffer.toString();
        }
    }

    private final String writeOIDs(Attribute attribute) throws NamingException {
        switch (attribute.size()) {
            case 0:
                throw new InvalidAttributeValueException(attribute.getID() + "has no values");
            case 1:
                if (!this.netscapeBug) {
                    return writeWOID(attribute);
                }
                break;
        }
        StringBuffer stringBuffer = new StringBuffer(attribute.getID());
        stringBuffer.append(' ');
        stringBuffer.append('(');
        NamingEnumeration<?> all = attribute.getAll();
        stringBuffer.append(' ');
        stringBuffer.append(all.next());
        while (all.hasMore()) {
            stringBuffer.append(' ');
            stringBuffer.append('$');
            stringBuffer.append(' ');
            stringBuffer.append((String) all.next());
        }
        stringBuffer.append(' ');
        stringBuffer.append(')');
        stringBuffer.append(' ');
        return stringBuffer.toString();
    }

    private final String writeBoolean(Attribute attribute) throws NamingException {
        return attribute.getID() + ' ';
    }

    final Attribute stringifyObjDesc(Attributes attributes) throws NamingException {
        BasicAttribute basicAttribute = new BasicAttribute(OBJECTCLASSDESC_ATTR_ID);
        basicAttribute.add(classDef2ObjectDesc(attributes));
        return basicAttribute;
    }

    final Attribute stringifyAttrDesc(Attributes attributes) throws NamingException {
        BasicAttribute basicAttribute = new BasicAttribute(ATTRIBUTEDESC_ATTR_ID);
        basicAttribute.add(attrDef2AttrDesc(attributes));
        return basicAttribute;
    }

    final Attribute stringifySyntaxDesc(Attributes attributes) throws NamingException {
        BasicAttribute basicAttribute = new BasicAttribute(SYNTAXDESC_ATTR_ID);
        basicAttribute.add(syntaxDef2SyntaxDesc(attributes));
        return basicAttribute;
    }

    final Attribute stringifyMatchRuleDesc(Attributes attributes) throws NamingException {
        BasicAttribute basicAttribute = new BasicAttribute(MATCHRULEDESC_ATTR_ID);
        basicAttribute.add(matchRuleDef2MatchRuleDesc(attributes));
        return basicAttribute;
    }
}
