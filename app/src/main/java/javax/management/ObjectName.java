package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/ObjectName.class */
public class ObjectName implements Comparable<ObjectName>, QueryExp {
    private static final long oldSerialVersionUID = -5467795090068647408L;
    private static final long newSerialVersionUID = 1081892073854801359L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("domain", String.class), new ObjectStreamField("propertyList", Hashtable.class), new ObjectStreamField("propertyListString", String.class), new ObjectStreamField("canonicalName", String.class), new ObjectStreamField("pattern", Boolean.TYPE), new ObjectStreamField("propertyPattern", Boolean.TYPE)};
    private static final ObjectStreamField[] newSerialPersistentFields = new ObjectStreamField[0];
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private static final Property[] _Empty_property_array;
    private transient String _canonicalName;
    private transient Property[] _kp_array;
    private transient Property[] _ca_array;
    private transient Map<String, String> _propertyList;
    public static final ObjectName WILDCARD;
    private transient int _domain_length = 0;
    private transient boolean _domain_pattern = false;
    private transient boolean _property_list_pattern = false;
    private transient boolean _property_value_pattern = false;

    /* loaded from: rt.jar:javax/management/ObjectName$Property.class */
    private static class Property {
        int _key_index;
        int _key_length;
        int _value_length;

        Property(int i2, int i3, int i4) {
            this._key_index = i2;
            this._key_length = i3;
            this._value_length = i4;
        }

        void setKeyIndex(int i2) {
            this._key_index = i2;
        }

        String getKeyString(String str) {
            return str.substring(this._key_index, this._key_index + this._key_length);
        }

        String getValueString(String str) {
            int i2 = this._key_index + this._key_length + 1;
            return str.substring(i2, i2 + this._value_length);
        }
    }

    /* loaded from: rt.jar:javax/management/ObjectName$PatternProperty.class */
    private static class PatternProperty extends Property {
        PatternProperty(int i2, int i3, int i4) {
            super(i2, i3, i4);
        }
    }

    static {
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e2) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
        _Empty_property_array = new Property[0];
        WILDCARD = Util.newObjectName("*:*");
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0323, code lost:
    
        switch(r0) {
            case 42: goto L178;
            case 63: goto L178;
            default: goto L181;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x033c, code lost:
    
        r28 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0345, code lost:
    
        if (r23 != r0) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0351, code lost:
    
        throw new javax.management.MalformedObjectNameException("Unterminated quoted value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0352, code lost:
    
        r23 = r23 + 1;
        r27 = r23 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x035f, code lost:
    
        r21 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0365, code lost:
    
        if (r23 >= r0) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0368, code lost:
    
        r0 = r0[r23];
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0371, code lost:
    
        if (r0 == ',') goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0376, code lost:
    
        switch(r0) {
            case 10: goto L167;
            case 34: goto L167;
            case 42: goto L187;
            case 58: goto L167;
            case 61: goto L167;
            case 63: goto L187;
            default: goto L185;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x03b0, code lost:
    
        r28 = true;
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x03bd, code lost:
    
        if (r0 != '\n') goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x03c0, code lost:
    
        r0 = "\\n";
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x03c5, code lost:
    
        r0 = "" + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x03fb, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid character '" + r0 + "' in value part of property");
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x03fc, code lost:
    
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0402, code lost:
    
        r27 = r23 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x040e, code lost:
    
        if (r23 != (r0 - 1)) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0413, code lost:
    
        if (r21 == false) goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0438, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid ending character `" + r0[r23] + org.icepdf.core.util.PdfOps.SINGLE_QUOTE_TOKEN);
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0442, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid ending comma");
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0443, code lost:
    
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0448, code lost:
    
        if (r28 != false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x044b, code lost:
    
        r17 = new javax.management.ObjectName.Property(r23, r0, r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x045d, code lost:
    
        r8._property_value_pattern = true;
        r17 = new javax.management.ObjectName.PatternProperty(r23, r0, r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0471, code lost:
    
        r0 = r9.substring(r23, r23 + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0483, code lost:
    
        if (r22 != r19.length) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0486, code lost:
    
        r0 = new java.lang.String[r22 + 10];
        java.lang.System.arraycopy(r19, 0, r0, 0, r22);
        r19 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x049f, code lost:
    
        r19[r22] = r0;
        addProperty(r17, r22, r0, r0);
        r22 = r22 + 1;
        r14 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x04bc, code lost:
    
        setCanonicalName(r0, r0, r19, r0, r0, r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x04cb, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0056, code lost:
    
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e7, code lost:
    
        if (r14 != r0) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00f3, code lost:
    
        throw new javax.management.MalformedObjectNameException("Key properties cannot be empty");
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00f4, code lost:
    
        java.lang.System.arraycopy(r0, 0, r0, 0, r8._domain_length);
        r0[r8._domain_length] = ':';
        r0 = r8._domain_length + 1;
        r0 = new java.util.HashMap();
        r22 = 0;
        r19 = new java.lang.String[10];
        r8._kp_array = new javax.management.ObjectName.Property[10];
        r8._property_list_pattern = false;
        r8._property_value_pattern = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x013a, code lost:
    
        if (r14 >= r0) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0147, code lost:
    
        if (r0[r14] != '*') goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x014e, code lost:
    
        if (r8._property_list_pattern == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x015a, code lost:
    
        throw new javax.management.MalformedObjectNameException("Cannot have several '*' characters in pattern property list");
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x015b, code lost:
    
        r8._property_list_pattern = true;
        r14 = r14 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0166, code lost:
    
        if (r14 >= r0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x016f, code lost:
    
        if (r0[r14] == ',') goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x017b, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid character found after '*': end of name or ',' expected");
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x017f, code lost:
    
        if (r14 != r0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0184, code lost:
    
        if (r22 != 0) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0187, code lost:
    
        r8._kp_array = javax.management.ObjectName._Empty_property_array;
        r8._ca_array = javax.management.ObjectName._Empty_property_array;
        r8._propertyList = java.util.Collections.emptyMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x019f, code lost:
    
        r14 = r14 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01a5, code lost:
    
        r23 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01b3, code lost:
    
        if (r0[r23] != '=') goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01bf, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid key (empty)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01c3, code lost:
    
        if (r23 >= r0) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01c6, code lost:
    
        r1 = r23;
        r23 = r23 + 1;
        r0 = r0[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01d2, code lost:
    
        if (r0 == '=') goto L173;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01d7, code lost:
    
        switch(r0) {
            case 10: goto L161;
            case 42: goto L161;
            case 44: goto L161;
            case 58: goto L161;
            case 63: goto L161;
            default: goto L174;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x020c, code lost:
    
        if (r0 != '\n') goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x020f, code lost:
    
        r0 = "\\n";
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0214, code lost:
    
        r0 = "" + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x024a, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid character '" + r0 + "' in key part of property");
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0256, code lost:
    
        if (r0[r23 - 1] == '=') goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0262, code lost:
    
        throw new javax.management.MalformedObjectNameException("Unterminated key property part");
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0263, code lost:
    
        r0 = r23;
        r0 = (r0 - r23) - 1;
        r28 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0276, code lost:
    
        if (r23 >= r0) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x027f, code lost:
    
        if (r0[r23] != '\"') goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0282, code lost:
    
        r21 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0285, code lost:
    
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x028b, code lost:
    
        if (r23 >= r0) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x028e, code lost:
    
        r0 = r0[r23];
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0297, code lost:
    
        if (r0 == '\"') goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x029e, code lost:
    
        if (r0 != '\\') goto L177;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02a1, code lost:
    
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02a7, code lost:
    
        if (r23 != r0) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02b3, code lost:
    
        throw new javax.management.MalformedObjectNameException("Unterminated quoted value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02b4, code lost:
    
        r0 = r0[r23];
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x02bb, code lost:
    
        switch(r0) {
            case 34: goto L183;
            case 42: goto L183;
            case 63: goto L183;
            case 92: goto L183;
            case 110: goto L183;
            default: goto L164;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x030f, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid escape sequence '\\" + r0 + "' in quoted value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0314, code lost:
    
        if (r0 != '\n') goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0320, code lost:
    
        throw new javax.management.MalformedObjectNameException("Newline in quoted value");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void construct(java.lang.String r9) throws javax.management.MalformedObjectNameException {
        /*
            Method dump skipped, instructions count: 1228
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.ObjectName.construct(java.lang.String):void");
    }

    private void construct(String str, Map<String, String> map) throws MalformedObjectNameException {
        Property patternProperty;
        if (str == null) {
            throw new NullPointerException("domain cannot be null");
        }
        if (map == null) {
            throw new NullPointerException("key property list cannot be null");
        }
        if (map.isEmpty()) {
            throw new MalformedObjectNameException("key property list cannot be empty");
        }
        if (!isDomain(str)) {
            throw new MalformedObjectNameException("Invalid domain: " + str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(':');
        this._domain_length = str.length();
        int size = map.size();
        this._kp_array = new Property[size];
        String[] strArr = new String[size];
        HashMap map2 = new HashMap();
        int i2 = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            String key = entry.getKey();
            try {
                String value = entry.getValue();
                int length = sb.length();
                checkKey(key);
                sb.append(key);
                strArr[i2] = key;
                sb.append("=");
                boolean zCheckValue = checkValue(value);
                sb.append(value);
                if (!zCheckValue) {
                    patternProperty = new Property(length, key.length(), value.length());
                } else {
                    this._property_value_pattern = true;
                    patternProperty = new PatternProperty(length, key.length(), value.length());
                }
                addProperty(patternProperty, i2, map2, key);
                i2++;
            } catch (ClassCastException e2) {
                throw new MalformedObjectNameException(e2.getMessage());
            }
        }
        int length2 = sb.length();
        char[] cArr = new char[length2];
        sb.getChars(0, length2, cArr, 0);
        char[] cArr2 = new char[length2];
        System.arraycopy(cArr, 0, cArr2, 0, this._domain_length + 1);
        setCanonicalName(cArr, cArr2, strArr, map2, this._domain_length + 1, this._kp_array.length);
    }

    private void addProperty(Property property, int i2, Map<String, Property> map, String str) throws MalformedObjectNameException {
        if (map.containsKey(str)) {
            throw new MalformedObjectNameException("key `" + str + "' already defined");
        }
        if (i2 == this._kp_array.length) {
            Property[] propertyArr = new Property[i2 + 10];
            System.arraycopy(this._kp_array, 0, propertyArr, 0, i2);
            this._kp_array = propertyArr;
        }
        this._kp_array[i2] = property;
        map.put(str, property);
    }

    private void setCanonicalName(char[] cArr, char[] cArr2, String[] strArr, Map<String, Property> map, int i2, int i3) {
        if (this._kp_array != _Empty_property_array) {
            String[] strArr2 = new String[i3];
            Property[] propertyArr = new Property[i3];
            System.arraycopy(strArr, 0, strArr2, 0, i3);
            Arrays.sort(strArr2);
            System.arraycopy(this._kp_array, 0, propertyArr, 0, i3);
            this._kp_array = propertyArr;
            this._ca_array = new Property[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                this._ca_array[i4] = map.get(strArr2[i4]);
            }
            int i5 = i3 - 1;
            for (int i6 = 0; i6 <= i5; i6++) {
                Property property = this._ca_array[i6];
                int i7 = property._key_length + property._value_length + 1;
                System.arraycopy(cArr, property._key_index, cArr2, i2, i7);
                property.setKeyIndex(i2);
                i2 += i7;
                if (i6 != i5) {
                    cArr2[i2] = ',';
                    i2++;
                }
            }
        }
        if (this._property_list_pattern) {
            if (this._kp_array != _Empty_property_array) {
                int i8 = i2;
                i2++;
                cArr2[i8] = ',';
            }
            int i9 = i2;
            i2++;
            cArr2[i9] = '*';
        }
        this._canonicalName = new String(cArr2, 0, i2).intern();
    }

    private static int parseKey(char[] cArr, int i2) throws MalformedObjectNameException {
        int i3 = i2;
        int i4 = i2;
        int length = cArr.length;
        while (true) {
            if (i3 < length) {
                int i5 = i3;
                i3++;
                char c2 = cArr[i5];
                switch (c2) {
                    case '\n':
                    case '*':
                    case ',':
                    case ':':
                    case '?':
                        throw new MalformedObjectNameException("Invalid character in key: `" + (c2 == '\n' ? "\\n" : "" + c2) + PdfOps.SINGLE_QUOTE_TOKEN);
                    case '=':
                        i4 = i3 - 1;
                        break;
                    default:
                        if (i3 >= length) {
                            i4 = i3;
                            break;
                        }
                }
            }
        }
        return i4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0117, code lost:
    
        r9 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x011d, code lost:
    
        if (r8 >= r0) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0120, code lost:
    
        r1 = r8;
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0128, code lost:
    
        if (r5[r1] == ',') goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0134, code lost:
    
        throw new javax.management.MalformedObjectNameException("Invalid quote");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int[] parseValue(char[] r5, int r6) throws javax.management.MalformedObjectNameException {
        /*
            Method dump skipped, instructions count: 512
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.ObjectName.parseValue(char[], int):int[]");
    }

    private static boolean checkValue(String str) throws MalformedObjectNameException {
        if (str == null) {
            throw new NullPointerException("Invalid value (null)");
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        char[] charArray = str.toCharArray();
        int[] value = parseValue(charArray, 0);
        int i2 = value[0];
        boolean z2 = value[1] == 1;
        if (i2 < length) {
            throw new MalformedObjectNameException("Invalid character in value: `" + charArray[i2] + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return z2;
    }

    private static void checkKey(String str) throws MalformedObjectNameException {
        if (str == null) {
            throw new NullPointerException("Invalid key (null)");
        }
        int length = str.length();
        if (length == 0) {
            throw new MalformedObjectNameException("Invalid key (empty)");
        }
        char[] charArray = str.toCharArray();
        int key = parseKey(charArray, 0);
        if (key < length) {
            throw new MalformedObjectNameException("Invalid character in value: `" + charArray[key] + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    private boolean isDomain(String str) {
        if (str == null) {
            return true;
        }
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2;
            i2++;
            switch (str.charAt(i3)) {
                case '\n':
                case ':':
                    return false;
                case '*':
                case '?':
                    this._domain_pattern = true;
                    break;
            }
        }
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str;
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            String str2 = (String) fields.get("propertyListString", "");
            if (fields.get("propertyPattern", false)) {
                str2 = str2.length() == 0 ? "*" : str2 + ",*";
            }
            str = ((String) fields.get("domain", "default")) + CallSiteDescriptor.TOKEN_DELIMITER + str2;
        } else {
            objectInputStream.defaultReadObject();
            str = (String) objectInputStream.readObject();
        }
        try {
            construct(str);
        } catch (NullPointerException e2) {
            throw new InvalidObjectException(e2.toString());
        } catch (MalformedObjectNameException e3) {
            throw new InvalidObjectException(e3.toString());
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("domain", this._canonicalName.substring(0, this._domain_length));
            putFieldPutFields.put("propertyList", getKeyPropertyList());
            putFieldPutFields.put("propertyListString", getKeyPropertyListString());
            putFieldPutFields.put("canonicalName", this._canonicalName);
            putFieldPutFields.put("pattern", this._domain_pattern || this._property_list_pattern);
            putFieldPutFields.put("propertyPattern", this._property_list_pattern);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(getSerializedNameString());
    }

    public static ObjectName getInstance(String str) throws MalformedObjectNameException, NullPointerException {
        return new ObjectName(str);
    }

    public static ObjectName getInstance(String str, String str2, String str3) throws MalformedObjectNameException {
        return new ObjectName(str, str2, str3);
    }

    public static ObjectName getInstance(String str, Hashtable<String, String> hashtable) throws MalformedObjectNameException {
        return new ObjectName(str, hashtable);
    }

    public static ObjectName getInstance(ObjectName objectName) {
        if (objectName.getClass().equals(ObjectName.class)) {
            return objectName;
        }
        return Util.newObjectName(objectName.getSerializedNameString());
    }

    public ObjectName(String str) throws MalformedObjectNameException {
        construct(str);
    }

    public ObjectName(String str, String str2, String str3) throws MalformedObjectNameException {
        construct(str, Collections.singletonMap(str2, str3));
    }

    public ObjectName(String str, Hashtable<String, String> hashtable) throws MalformedObjectNameException {
        construct(str, hashtable);
    }

    public boolean isPattern() {
        return this._domain_pattern || this._property_list_pattern || this._property_value_pattern;
    }

    public boolean isDomainPattern() {
        return this._domain_pattern;
    }

    public boolean isPropertyPattern() {
        return this._property_list_pattern || this._property_value_pattern;
    }

    public boolean isPropertyListPattern() {
        return this._property_list_pattern;
    }

    public boolean isPropertyValuePattern() {
        return this._property_value_pattern;
    }

    public boolean isPropertyValuePattern(String str) {
        if (str == null) {
            throw new NullPointerException("key property can't be null");
        }
        for (int i2 = 0; i2 < this._ca_array.length; i2++) {
            Property property = this._ca_array[i2];
            if (property.getKeyString(this._canonicalName).equals(str)) {
                return property instanceof PatternProperty;
            }
        }
        throw new IllegalArgumentException("key property not found");
    }

    public String getCanonicalName() {
        return this._canonicalName;
    }

    public String getDomain() {
        return this._canonicalName.substring(0, this._domain_length);
    }

    public String getKeyProperty(String str) {
        return _getKeyPropertyList().get(str);
    }

    private Map<String, String> _getKeyPropertyList() {
        synchronized (this) {
            if (this._propertyList == null) {
                this._propertyList = new HashMap();
                for (int length = this._ca_array.length - 1; length >= 0; length--) {
                    Property property = this._ca_array[length];
                    this._propertyList.put(property.getKeyString(this._canonicalName), property.getValueString(this._canonicalName));
                }
            }
        }
        return this._propertyList;
    }

    public Hashtable<String, String> getKeyPropertyList() {
        return new Hashtable<>(_getKeyPropertyList());
    }

    public String getKeyPropertyListString() {
        if (this._kp_array.length == 0) {
            return "";
        }
        char[] cArr = new char[((this._canonicalName.length() - this._domain_length) - 1) - (this._property_list_pattern ? 2 : 0)];
        writeKeyPropertyListString(this._canonicalName.toCharArray(), cArr, 0);
        return new String(cArr);
    }

    private String getSerializedNameString() {
        char[] cArr = new char[this._canonicalName.length()];
        char[] charArray = this._canonicalName.toCharArray();
        int i2 = this._domain_length + 1;
        System.arraycopy(charArray, 0, cArr, 0, i2);
        int iWriteKeyPropertyListString = writeKeyPropertyListString(charArray, cArr, i2);
        if (this._property_list_pattern) {
            if (iWriteKeyPropertyListString == i2) {
                cArr[iWriteKeyPropertyListString] = '*';
            } else {
                cArr[iWriteKeyPropertyListString] = ',';
                cArr[iWriteKeyPropertyListString + 1] = '*';
            }
        }
        return new String(cArr);
    }

    private int writeKeyPropertyListString(char[] cArr, char[] cArr2, int i2) {
        if (this._kp_array.length == 0) {
            return i2;
        }
        int i3 = i2;
        int length = this._kp_array.length;
        int i4 = length - 1;
        for (int i5 = 0; i5 < length; i5++) {
            Property property = this._kp_array[i5];
            int i6 = property._key_length + property._value_length + 1;
            System.arraycopy(cArr, property._key_index, cArr2, i3, i6);
            i3 += i6;
            if (i5 < i4) {
                i3++;
                cArr2[i3] = ',';
            }
        }
        return i3;
    }

    public String getCanonicalKeyPropertyListString() {
        if (this._ca_array.length == 0) {
            return "";
        }
        int length = this._canonicalName.length();
        if (this._property_list_pattern) {
            length -= 2;
        }
        return this._canonicalName.substring(this._domain_length + 1, length);
    }

    public String toString() {
        return getSerializedNameString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ObjectName) {
            return this._canonicalName == ((ObjectName) obj)._canonicalName;
        }
        return false;
    }

    public int hashCode() {
        return this._canonicalName.hashCode();
    }

    public static String quote(String str) {
        StringBuilder sb = new StringBuilder(PdfOps.DOUBLE_QUOTE__TOKEN);
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            switch (cCharAt) {
                case '\n':
                    cCharAt = 'n';
                    sb.append('\\');
                    break;
                case '\"':
                case '*':
                case '?':
                case '\\':
                    sb.append('\\');
                    break;
            }
            sb.append(cCharAt);
        }
        sb.append('\"');
        return sb.toString();
    }

    public static String unquote(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        if (length < 2 || str.charAt(0) != '\"' || str.charAt(length - 1) != '\"') {
            throw new IllegalArgumentException("Argument not quoted");
        }
        int i2 = 1;
        while (i2 < length - 1) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\') {
                if (i2 == length - 2) {
                    throw new IllegalArgumentException("Trailing backslash");
                }
                i2++;
                cCharAt = str.charAt(i2);
                switch (cCharAt) {
                    case '\"':
                    case '*':
                    case '?':
                    case '\\':
                        break;
                    case 'n':
                        cCharAt = '\n';
                        break;
                    default:
                        throw new IllegalArgumentException("Bad character '" + cCharAt + "' after backslash");
                }
            } else {
                switch (cCharAt) {
                    case '\n':
                    case '\"':
                    case '*':
                    case '?':
                        throw new IllegalArgumentException("Invalid unescaped character '" + cCharAt + "' in the string to unquote");
                }
            }
            sb.append(cCharAt);
            i2++;
        }
        return sb.toString();
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) {
        if (objectName == null) {
            throw new NullPointerException();
        }
        if (objectName._domain_pattern || objectName._property_list_pattern || objectName._property_value_pattern) {
            return false;
        }
        if (this._domain_pattern || this._property_list_pattern || this._property_value_pattern) {
            return matchDomains(objectName) && matchKeys(objectName);
        }
        return this._canonicalName.equals(objectName._canonicalName);
    }

    private final boolean matchDomains(ObjectName objectName) {
        if (this._domain_pattern) {
            return Util.wildmatch(objectName.getDomain(), getDomain());
        }
        return getDomain().equals(objectName.getDomain());
    }

    private final boolean matchKeys(ObjectName objectName) {
        if (this._property_value_pattern && !this._property_list_pattern && objectName._ca_array.length != this._ca_array.length) {
            return false;
        }
        if (this._property_value_pattern || this._property_list_pattern) {
            Map<String, String> map_getKeyPropertyList = objectName._getKeyPropertyList();
            Property[] propertyArr = this._ca_array;
            String str = this._canonicalName;
            for (int length = propertyArr.length - 1; length >= 0; length--) {
                Property property = propertyArr[length];
                String str2 = map_getKeyPropertyList.get(property.getKeyString(str));
                if (str2 == null) {
                    return false;
                }
                if (this._property_value_pattern && (property instanceof PatternProperty)) {
                    if (!Util.wildmatch(str2, property.getValueString(str))) {
                        return false;
                    }
                } else if (!str2.equals(property.getValueString(str))) {
                    return false;
                }
            }
            return true;
        }
        return objectName.getCanonicalKeyPropertyListString().equals(getCanonicalKeyPropertyListString());
    }

    @Override // javax.management.QueryExp
    public void setMBeanServer(MBeanServer mBeanServer) {
    }

    @Override // java.lang.Comparable
    public int compareTo(ObjectName objectName) {
        if (objectName == this) {
            return 0;
        }
        int iCompareTo = getDomain().compareTo(objectName.getDomain());
        if (iCompareTo != 0) {
            return iCompareTo;
        }
        String keyProperty = getKeyProperty("type");
        String keyProperty2 = objectName.getKeyProperty("type");
        if (keyProperty == null) {
            keyProperty = "";
        }
        if (keyProperty2 == null) {
            keyProperty2 = "";
        }
        int iCompareTo2 = keyProperty.compareTo(keyProperty2);
        if (iCompareTo2 != 0) {
            return iCompareTo2;
        }
        return getCanonicalName().compareTo(objectName.getCanonicalName());
    }
}
