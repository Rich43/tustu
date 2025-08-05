package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.MBeanException;
import javax.management.RuntimeOperationsException;
import org.icepdf.core.util.PdfOps;
import sun.reflect.misc.ReflectUtil;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: rt.jar:javax/management/modelmbean/DescriptorSupport.class */
public class DescriptorSupport implements Descriptor {
    private static final long oldSerialVersionUID = 8071560848919417985L;
    private static final long newSerialVersionUID = -6292969195866300415L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("descriptor", HashMap.class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("descriptor", HashMap.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final String serialForm;
    private transient SortedMap<String, Object> descriptorMap;
    private static final String currClass = "DescriptorSupport";
    private static final String[] entities;
    private static final Map<String, Character> entityToCharMap;
    private static final String[] charToEntityMap;

    static {
        String str = null;
        boolean zEquals = false;
        try {
            str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            zEquals = "1.0".equals(str);
        } catch (Exception e2) {
        }
        serialForm = str;
        if (zEquals) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
        entities = new String[]{" &#32;", "\"&quot;", "<&lt;", ">&gt;", "&&amp;", "\r&#13;", "\t&#9;", "\n&#10;", "\f&#12;"};
        entityToCharMap = new HashMap();
        char c2 = 0;
        for (int i2 = 0; i2 < entities.length; i2++) {
            char cCharAt = entities[i2].charAt(0);
            if (cCharAt > c2) {
                c2 = cCharAt;
            }
        }
        charToEntityMap = new String[c2 + 1];
        for (int i3 = 0; i3 < entities.length; i3++) {
            char cCharAt2 = entities[i3].charAt(0);
            String strSubstring = entities[i3].substring(1);
            charToEntityMap[cCharAt2] = strSubstring;
            entityToCharMap.put(strSubstring, Character.valueOf(cCharAt2));
        }
    }

    public DescriptorSupport() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "DescriptorSupport()", "Constructor");
        }
        init(null);
    }

    public DescriptorSupport(int i2) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(initNumFields = " + i2 + ")", "Constructor");
        }
        if (i2 <= 0) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(initNumFields)", "Illegal arguments: initNumFields <= 0");
            }
            String str = "Descriptor field limit invalid: " + i2;
            throw new RuntimeOperationsException(new IllegalArgumentException(str), str);
        }
        init(null);
    }

    public DescriptorSupport(DescriptorSupport descriptorSupport) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(Descriptor)", "Constructor");
        }
        if (descriptorSupport == null) {
            init(null);
        } else {
            init(descriptorSupport.descriptorMap);
        }
    }

    public DescriptorSupport(String str) throws XMLParseException, MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String = '" + str + "')", "Constructor");
        }
        if (str == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String = null)", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("String in parameter is null"), "String in parameter is null");
        }
        String lowerCase = str.toLowerCase();
        if (!lowerCase.startsWith("<descriptor>") || !lowerCase.endsWith("</descriptor>")) {
            throw new XMLParseException("No <descriptor>, </descriptor> pair");
        }
        init(null);
        StringTokenizer stringTokenizer = new StringTokenizer(str, "<> \t\n\r\f");
        boolean z2 = false;
        boolean z3 = false;
        String str2 = null;
        String str3 = null;
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.equalsIgnoreCase("FIELD")) {
                z2 = true;
            } else if (strNextToken.equalsIgnoreCase("/FIELD")) {
                if (str2 != null && str3 != null) {
                    setField(str2.substring(str2.indexOf(34) + 1, str2.lastIndexOf(34)), parseQuotedFieldValue(str3));
                }
                str2 = null;
                str3 = null;
                z2 = false;
            } else if (strNextToken.equalsIgnoreCase("DESCRIPTOR")) {
                z3 = true;
            } else if (strNextToken.equalsIgnoreCase("/DESCRIPTOR")) {
                z3 = false;
                str2 = null;
                str3 = null;
                z2 = false;
            } else if (z2 && z3) {
                int iIndexOf = strNextToken.indexOf("=");
                if (iIndexOf > 0) {
                    String strSubstring = strNextToken.substring(0, iIndexOf);
                    String strSubstring2 = strNextToken.substring(iIndexOf + 1);
                    if (strSubstring.equalsIgnoreCase("NAME")) {
                        str2 = strSubstring2;
                    } else if (strSubstring.equalsIgnoreCase("VALUE")) {
                        str3 = strSubstring2;
                    } else {
                        throw new XMLParseException("Expected `name' or `value', got `" + strNextToken + PdfOps.SINGLE_QUOTE_TOKEN);
                    }
                } else {
                    throw new XMLParseException("Expected `keyword=value', got `" + strNextToken + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(XMLString)", ToolWindow.QUIT);
        }
    }

    public DescriptorSupport(String[] strArr, Object[] objArr) throws RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", "Constructor");
        }
        if (strArr == null || objArr == null || strArr.length != objArr.length) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Null or invalid fieldNames or fieldValues"), "Null or invalid fieldNames or fieldValues");
        }
        init(null);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            setField(strArr[i2], objArr[i2]);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", ToolWindow.QUIT);
        }
    }

    public DescriptorSupport(String... strArr) throws RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Constructor");
        }
        init(null);
        if (strArr == null || strArr.length == 0) {
            return;
        }
        init(null);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] != null && !strArr[i2].equals("")) {
                int iIndexOf = strArr[i2].indexOf("=");
                if (iIndexOf < 0) {
                    if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Illegal arguments: field does not have '=' as a name and value separator");
                    }
                    throw new RuntimeOperationsException(new IllegalArgumentException("Field in invalid format: no equals sign"), "Field in invalid format: no equals sign");
                }
                String strSubstring = strArr[i2].substring(0, iIndexOf);
                String strSubstring2 = iIndexOf < strArr[i2].length() ? strArr[i2].substring(iIndexOf + 1) : null;
                if (strSubstring.equals("")) {
                    if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Illegal arguments: fieldName is empty");
                    }
                    throw new RuntimeOperationsException(new IllegalArgumentException("Field in invalid format: no fieldName"), "Field in invalid format: no fieldName");
                }
                setField(strSubstring, strSubstring2);
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", ToolWindow.QUIT);
        }
    }

    private void init(Map<String, ?> map) {
        this.descriptorMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        if (map != null) {
            this.descriptorMap.putAll(map);
        }
    }

    @Override // javax.management.Descriptor
    public synchronized Object getFieldValue(String str) throws RuntimeOperationsException {
        if (str == null || str.equals("")) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValue(String fieldName)", "Illegal arguments: null field name");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Fieldname requested is null"), "Fieldname requested is null");
        }
        Object obj = this.descriptorMap.get(str);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValue(String fieldName = " + str + ")", "Returns '" + obj + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return obj;
    }

    @Override // javax.management.Descriptor
    public synchronized void setField(String str, Object obj) throws RuntimeOperationsException {
        if (str == null || str.equals("")) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Illegal arguments: null or empty field name");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("Field name to be set is null or empty"), "Field name to be set is null or empty");
        }
        if (!validateField(str, obj)) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Illegal arguments");
            }
            String str2 = "Field value invalid: " + str + "=" + obj;
            throw new RuntimeOperationsException(new IllegalArgumentException(str2), str2);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Entry: setting '" + str + "' to '" + obj + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        this.descriptorMap.put(str, obj);
    }

    @Override // javax.management.Descriptor
    public synchronized String[] getFields() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Entry");
        }
        int size = this.descriptorMap.size();
        String[] strArr = new String[size];
        Set<Map.Entry<String, Object>> setEntrySet = this.descriptorMap.entrySet();
        int i2 = 0;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Returning " + size + " fields");
        }
        for (Map.Entry<String, Object> entry : setEntrySet) {
            if (entry == null) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Element is null");
                }
            } else {
                Object value = entry.getValue();
                if (value == null) {
                    strArr[i2] = entry.getKey() + "=";
                } else if (value instanceof String) {
                    strArr[i2] = entry.getKey() + "=" + value.toString();
                } else {
                    strArr[i2] = entry.getKey() + "=(" + value.toString() + ")";
                }
            }
            i2++;
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", ToolWindow.QUIT);
        }
        return strArr;
    }

    @Override // javax.management.Descriptor
    public synchronized String[] getFieldNames() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Entry");
        }
        int size = this.descriptorMap.size();
        String[] strArr = new String[size];
        Set<Map.Entry<String, Object>> setEntrySet = this.descriptorMap.entrySet();
        int i2 = 0;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Returning " + size + " fields");
        }
        for (Map.Entry<String, Object> entry : setEntrySet) {
            if (entry == null || entry.getKey() == null) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Field is null");
                }
            } else {
                strArr[i2] = entry.getKey().toString();
            }
            i2++;
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", ToolWindow.QUIT);
        }
        return strArr;
    }

    @Override // javax.management.Descriptor
    public synchronized Object[] getFieldValues(String... strArr) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", "Entry");
        }
        int size = strArr == null ? this.descriptorMap.size() : strArr.length;
        Object[] objArr = new Object[size];
        int i2 = 0;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", "Returning " + size + " fields");
        }
        if (strArr == null) {
            Iterator<Object> it = this.descriptorMap.values().iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                objArr[i3] = it.next();
            }
        } else {
            for (int i4 = 0; i4 < strArr.length; i4++) {
                if (strArr[i4] == null || strArr[i4].equals("")) {
                    objArr[i4] = null;
                } else {
                    objArr[i4] = getFieldValue(strArr[i4]);
                }
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", ToolWindow.QUIT);
        }
        return objArr;
    }

    @Override // javax.management.Descriptor
    public synchronized void setFields(String[] strArr, Object[] objArr) throws RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Entry");
        }
        if (strArr == null || objArr == null || strArr.length != objArr.length) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Illegal arguments");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("fieldNames and fieldValues are null or invalid"), "fieldNames and fieldValues are null or invalid");
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2] == null || strArr[i2].equals("")) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Null field name encountered at element " + i2);
                }
                throw new RuntimeOperationsException(new IllegalArgumentException("fieldNames is null or invalid"), "fieldNames is null or invalid");
            }
            setField(strArr[i2], objArr[i2]);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.Descriptor
    public synchronized Object clone() throws RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "clone()", "Entry");
        }
        return new DescriptorSupport(this);
    }

    @Override // javax.management.Descriptor
    public synchronized void removeField(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        this.descriptorMap.remove(str);
    }

    @Override // javax.management.Descriptor
    public synchronized boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Descriptor)) {
            return false;
        }
        if (obj instanceof ImmutableDescriptor) {
            return obj.equals(this);
        }
        return new ImmutableDescriptor(this.descriptorMap).equals(obj);
    }

    @Override // javax.management.Descriptor
    public synchronized int hashCode() {
        int size = this.descriptorMap.size();
        return Util.hashCode((String[]) this.descriptorMap.keySet().toArray(new String[size]), this.descriptorMap.values().toArray(new Object[size]));
    }

    @Override // javax.management.Descriptor
    public synchronized boolean isValid() throws RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Entry");
        }
        Set<Map.Entry<String, Object>> setEntrySet = this.descriptorMap.entrySet();
        if (setEntrySet == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Returns false (null set)");
                return false;
            }
            return false;
        }
        String str = (String) getFieldValue("name");
        String str2 = (String) getFieldValue("descriptorType");
        if (str == null || str2 == null || str.equals("") || str2.equals("")) {
            return false;
        }
        for (Map.Entry<String, Object> entry : setEntrySet) {
            if (entry != null && entry.getValue() != null && !validateField(entry.getKey().toString(), entry.getValue().toString())) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Field " + entry.getKey() + "=" + entry.getValue() + " is not valid");
                    return false;
                }
                return false;
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Returns true");
            return true;
        }
        return true;
    }

    private boolean validateField(String str, Object obj) {
        long jLongValue;
        long jIntValue;
        long jIntValue2;
        if (str == null || str.equals("")) {
            return false;
        }
        String str2 = "";
        boolean z2 = false;
        if (obj != null && (obj instanceof String)) {
            str2 = (String) obj;
            z2 = true;
        }
        boolean z3 = str.equalsIgnoreCase("Name") || str.equalsIgnoreCase("DescriptorType");
        if (z3 || str.equalsIgnoreCase("SetMethod") || str.equalsIgnoreCase("GetMethod") || str.equalsIgnoreCase("Role") || str.equalsIgnoreCase("Class")) {
            if (obj == null || !z2) {
                return false;
            }
            if (z3 && str2.equals("")) {
                return false;
            }
            return true;
        }
        if (str.equalsIgnoreCase("visibility")) {
            if (obj != null && z2) {
                jIntValue2 = toNumeric(str2);
            } else if (obj instanceof Integer) {
                jIntValue2 = ((Integer) obj).intValue();
            } else {
                return false;
            }
            if (jIntValue2 >= 1 && jIntValue2 <= 4) {
                return true;
            }
            return false;
        }
        if (str.equalsIgnoreCase("severity")) {
            if (obj != null && z2) {
                jIntValue = toNumeric(str2);
            } else if (obj instanceof Integer) {
                jIntValue = ((Integer) obj).intValue();
            } else {
                return false;
            }
            return jIntValue >= 0 && jIntValue <= 6;
        }
        if (str.equalsIgnoreCase("PersistPolicy")) {
            return obj != null && z2 && (str2.equalsIgnoreCase("OnUpdate") || str2.equalsIgnoreCase("OnTimer") || str2.equalsIgnoreCase("NoMoreOftenThan") || str2.equalsIgnoreCase("Always") || str2.equalsIgnoreCase("Never") || str2.equalsIgnoreCase("OnUnregister"));
        }
        if (!str.equalsIgnoreCase("PersistPeriod") && !str.equalsIgnoreCase("CurrencyTimeLimit") && !str.equalsIgnoreCase("LastUpdatedTimeStamp") && !str.equalsIgnoreCase("LastReturnedTimeStamp")) {
            return !str.equalsIgnoreCase("log") || (obj instanceof Boolean) || (z2 && (str2.equalsIgnoreCase("T") || str2.equalsIgnoreCase("true") || str2.equalsIgnoreCase(PdfOps.F_TOKEN) || str2.equalsIgnoreCase("false")));
        }
        if (obj != null && z2) {
            jLongValue = toNumeric(str2);
        } else if (obj instanceof Number) {
            jLongValue = ((Number) obj).longValue();
        } else {
            return false;
        }
        return jLongValue >= -1;
    }

    public synchronized String toXMLString() {
        StringBuilder sb = new StringBuilder("<Descriptor>");
        for (Map.Entry<String, Object> entry : this.descriptorMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String strMakeFieldValue = null;
            if (value instanceof String) {
                String str = (String) value;
                if (!str.startsWith("(") || !str.endsWith(")")) {
                    strMakeFieldValue = quote(str);
                }
            }
            if (strMakeFieldValue == null) {
                strMakeFieldValue = makeFieldValue(value);
            }
            sb.append("<field name=\"").append(key).append("\" value=\"").append(strMakeFieldValue).append("\"></field>");
        }
        sb.append("</Descriptor>");
        return sb.toString();
    }

    private static boolean isMagic(char c2) {
        return c2 < charToEntityMap.length && charToEntityMap[c2] != null;
    }

    private static String quote(String str) {
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= str.length()) {
                break;
            }
            if (!isMagic(str.charAt(i2))) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (!z2) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < str.length(); i3++) {
            char cCharAt = str.charAt(i3);
            if (isMagic(cCharAt)) {
                sb.append(charToEntityMap[cCharAt]);
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    private static String unquote(String str) throws XMLParseException {
        int iIndexOf;
        Character ch;
        if (!str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || !str.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            throw new XMLParseException("Value must be quoted: <" + str + ">");
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length() - 1;
        int i2 = 1;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '&' && (iIndexOf = str.indexOf(59, i2 + 1)) >= 0 && (ch = entityToCharMap.get(str.substring(i2, iIndexOf + 1))) != null) {
                sb.append((Object) ch);
                i2 = iIndexOf;
            } else {
                sb.append(cCharAt);
            }
            i2++;
        }
        return sb.toString();
    }

    private static String makeFieldValue(Object obj) {
        if (obj == null) {
            return "(null)";
        }
        Class<?> cls = obj.getClass();
        try {
            cls.getConstructor(String.class);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Class " + ((Object) cls) + " does not have a public constructor with a single string arg"), "Cannot make XML descriptor");
        } catch (SecurityException e3) {
        }
        return "(" + cls.getName() + "/" + quote(obj.toString()) + ")";
    }

    private static Object parseQuotedFieldValue(String str) throws XMLParseException {
        String strUnquote = unquote(str);
        if (strUnquote.equalsIgnoreCase("(null)")) {
            return null;
        }
        if (!strUnquote.startsWith("(") || !strUnquote.endsWith(")")) {
            return strUnquote;
        }
        int iIndexOf = strUnquote.indexOf(47);
        if (iIndexOf < 0) {
            return strUnquote.substring(1, strUnquote.length() - 1);
        }
        String strSubstring = strUnquote.substring(1, iIndexOf);
        try {
            ReflectUtil.checkPackageAccess(strSubstring);
            try {
                return Class.forName(strSubstring, false, Thread.currentThread().getContextClassLoader()).getConstructor(String.class).newInstance(strUnquote.substring(iIndexOf + 1, strUnquote.length() - 1));
            } catch (Exception e2) {
                throw new XMLParseException(e2, "Cannot construct instance of " + strSubstring + " with arg: <" + strUnquote + ">");
            }
        } catch (Exception e3) {
            throw new XMLParseException(e3, "Cannot parse value: <" + strUnquote + ">");
        }
    }

    public synchronized String toString() {
        String strConcat;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Entry");
        }
        String str = "";
        String[] fields = getFields();
        if (fields == null || fields.length == 0) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Empty Descriptor");
            }
            return str;
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Printing " + fields.length + " fields");
        }
        for (int i2 = 0; i2 < fields.length; i2++) {
            if (i2 == fields.length - 1) {
                strConcat = str.concat(fields[i2]);
            } else {
                strConcat = str.concat(fields[i2] + ", ");
            }
            str = strConcat;
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Exit returning " + str);
        }
        return str;
    }

    private long toNumeric(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e2) {
            return -2L;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Map<? extends String, ? extends Object> map = (Map) Util.cast(objectInputStream.readFields().get("descriptor", (Object) null));
        init(null);
        if (map != null) {
            this.descriptorMap.putAll(map);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        HashMap map;
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        boolean zEquals = "1.0".equals(serialForm);
        if (zEquals) {
            putFieldPutFields.put("currClass", currClass);
        }
        SortedMap<String, Object> treeMap = this.descriptorMap;
        if (treeMap.containsKey("targetObject")) {
            treeMap = new TreeMap((SortedMap<String, ? extends Object>) this.descriptorMap);
            treeMap.remove("targetObject");
        }
        if (zEquals || "1.2.0".equals(serialForm) || "1.2.1".equals(serialForm)) {
            map = new HashMap();
            for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
                map.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        } else {
            map = new HashMap(treeMap);
        }
        putFieldPutFields.put("descriptor", map);
        objectOutputStream.writeFields();
    }
}
