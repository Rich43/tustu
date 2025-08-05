package com.sun.org.apache.xalan.internal.xsltc.runtime;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.dom.ArrayNodeListIterator;
import com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM;
import com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import com.sun.org.apache.xml.internal.serializer.NamespaceMappings;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/BasisLibrary.class */
public final class BasisLibrary {
    private static final String EMPTYSTRING = "";
    private static final int DOUBLE_FRACTION_DIGITS = 340;
    private static final double lowerBounds = 0.001d;
    private static final double upperBounds = 1.0E7d;
    private static DecimalFormat defaultFormatter;
    private static DecimalFormat xpathFormatter;
    private static FieldPosition _fieldPosition;
    private static char[] _characterArray;
    private static final ThreadLocal<AtomicInteger> threadLocalPrefixIndex;
    public static final String RUN_TIME_INTERNAL_ERR = "RUN_TIME_INTERNAL_ERR";
    public static final String RUN_TIME_COPY_ERR = "RUN_TIME_COPY_ERR";
    public static final String DATA_CONVERSION_ERR = "DATA_CONVERSION_ERR";
    public static final String EXTERNAL_FUNC_ERR = "EXTERNAL_FUNC_ERR";
    public static final String EQUALITY_EXPR_ERR = "EQUALITY_EXPR_ERR";
    public static final String INVALID_ARGUMENT_ERR = "INVALID_ARGUMENT_ERR";
    public static final String FORMAT_NUMBER_ERR = "FORMAT_NUMBER_ERR";
    public static final String ITERATOR_CLONE_ERR = "ITERATOR_CLONE_ERR";
    public static final String AXIS_SUPPORT_ERR = "AXIS_SUPPORT_ERR";
    public static final String TYPED_AXIS_SUPPORT_ERR = "TYPED_AXIS_SUPPORT_ERR";
    public static final String STRAY_ATTRIBUTE_ERR = "STRAY_ATTRIBUTE_ERR";
    public static final String STRAY_NAMESPACE_ERR = "STRAY_NAMESPACE_ERR";
    public static final String NAMESPACE_PREFIX_ERR = "NAMESPACE_PREFIX_ERR";
    public static final String DOM_ADAPTER_INIT_ERR = "DOM_ADAPTER_INIT_ERR";
    public static final String PARSER_DTD_SUPPORT_ERR = "PARSER_DTD_SUPPORT_ERR";
    public static final String NAMESPACES_SUPPORT_ERR = "NAMESPACES_SUPPORT_ERR";
    public static final String CANT_RESOLVE_RELATIVE_URI_ERR = "CANT_RESOLVE_RELATIVE_URI_ERR";
    public static final String UNSUPPORTED_XSL_ERR = "UNSUPPORTED_XSL_ERR";
    public static final String UNSUPPORTED_EXT_ERR = "UNSUPPORTED_EXT_ERR";
    public static final String UNKNOWN_TRANSLET_VERSION_ERR = "UNKNOWN_TRANSLET_VERSION_ERR";
    public static final String INVALID_QNAME_ERR = "INVALID_QNAME_ERR";
    public static final String INVALID_NCNAME_ERR = "INVALID_NCNAME_ERR";
    public static final String UNALLOWED_EXTENSION_FUNCTION_ERR = "UNALLOWED_EXTENSION_FUNCTION_ERR";
    public static final String UNALLOWED_EXTENSION_ELEMENT_ERR = "UNALLOWED_EXTENSION_ELEMENT_ERR";
    private static ResourceBundle m_bundle;
    public static final String ERROR_MESSAGES_KEY = "error-messages";
    private static final ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<StringBuilder>() { // from class: com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public StringBuilder initialValue() {
            return new StringBuilder();
        }
    };
    private static final ThreadLocal<StringBuffer> threadLocalStringBuffer = new ThreadLocal<StringBuffer>() { // from class: com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public StringBuffer initialValue() {
            return new StringBuffer();
        }
    };
    private static String defaultPattern = "";

    static {
        NumberFormat f2 = NumberFormat.getInstance(Locale.getDefault());
        defaultFormatter = f2 instanceof DecimalFormat ? (DecimalFormat) f2 : new DecimalFormat();
        defaultFormatter.setMaximumFractionDigits(340);
        defaultFormatter.setMinimumFractionDigits(0);
        defaultFormatter.setMinimumIntegerDigits(1);
        defaultFormatter.setGroupingUsed(false);
        xpathFormatter = new DecimalFormat("", new DecimalFormatSymbols(Locale.US));
        xpathFormatter.setMaximumFractionDigits(340);
        xpathFormatter.setMinimumFractionDigits(0);
        xpathFormatter.setMinimumIntegerDigits(1);
        xpathFormatter.setGroupingUsed(false);
        _fieldPosition = new FieldPosition(0);
        _characterArray = new char[32];
        threadLocalPrefixIndex = new ThreadLocal<AtomicInteger>() { // from class: com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.4
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public AtomicInteger initialValue() {
                return new AtomicInteger();
            }
        };
        m_bundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages");
    }

    public static int countF(DTMAxisIterator iterator) {
        return iterator.getLast();
    }

    public static int positionF(DTMAxisIterator iterator) {
        if (iterator.isReverse()) {
            return (iterator.getLast() - iterator.getPosition()) + 1;
        }
        return iterator.getPosition();
    }

    public static double sumF(DTMAxisIterator iterator, DOM dom) {
        double result = 0.0d;
        while (true) {
            try {
                int node = iterator.next();
                if (node != -1) {
                    result += Double.parseDouble(dom.getStringValueX(node));
                } else {
                    return result;
                }
            } catch (NumberFormatException e2) {
                return Double.NaN;
            }
        }
    }

    public static String stringF(int node, DOM dom) {
        return dom.getStringValueX(node);
    }

    public static String stringF(Object obj, DOM dom) {
        if (obj instanceof DTMAxisIterator) {
            return dom.getStringValueX(((DTMAxisIterator) obj).reset().next());
        }
        if (obj instanceof Node) {
            return dom.getStringValueX(((Node) obj).node);
        }
        if (obj instanceof DOM) {
            return ((DOM) obj).getStringValue();
        }
        return obj.toString();
    }

    public static String stringF(Object obj, int node, DOM dom) {
        if (obj instanceof DTMAxisIterator) {
            return dom.getStringValueX(((DTMAxisIterator) obj).reset().next());
        }
        if (obj instanceof Node) {
            return dom.getStringValueX(((Node) obj).node);
        }
        if (obj instanceof DOM) {
            return ((DOM) obj).getStringValue();
        }
        if (!(obj instanceof Double)) {
            return obj != null ? obj.toString() : "";
        }
        Double d2 = (Double) obj;
        String result = d2.toString();
        int length = result.length();
        if (result.charAt(length - 2) == '.' && result.charAt(length - 1) == '0') {
            return result.substring(0, length - 2);
        }
        return result;
    }

    public static double numberF(int node, DOM dom) {
        return stringToReal(dom.getStringValueX(node));
    }

    public static double numberF(Object obj, DOM dom) {
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue();
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1.0d : 0.0d;
        }
        if (obj instanceof String) {
            return stringToReal((String) obj);
        }
        if (obj instanceof DTMAxisIterator) {
            DTMAxisIterator iter = (DTMAxisIterator) obj;
            return stringToReal(dom.getStringValueX(iter.reset().next()));
        }
        if (obj instanceof Node) {
            return stringToReal(dom.getStringValueX(((Node) obj).node));
        }
        if (obj instanceof DOM) {
            return stringToReal(((DOM) obj).getStringValue());
        }
        String className = obj.getClass().getName();
        runTimeError(INVALID_ARGUMENT_ERR, className, "number()");
        return 0.0d;
    }

    public static double roundF(double d2) {
        return (d2 < -0.5d || d2 > 0.0d) ? Math.floor(d2 + 0.5d) : d2 == 0.0d ? d2 : Double.isNaN(d2) ? Double.NaN : -0.0d;
    }

    public static boolean booleanF(Object obj) {
        if (obj instanceof Double) {
            double temp = ((Double) obj).doubleValue();
            return (temp == 0.0d || Double.isNaN(temp)) ? false : true;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue() != 0.0d;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof String) {
            return !((String) obj).equals("");
        }
        if (obj instanceof DTMAxisIterator) {
            DTMAxisIterator iter = (DTMAxisIterator) obj;
            return iter.reset().next() != -1;
        }
        if (obj instanceof Node) {
            return true;
        }
        if (obj instanceof DOM) {
            return !((DOM) obj).getStringValue().equals("");
        }
        String className = obj.getClass().getName();
        runTimeError(INVALID_ARGUMENT_ERR, className, "boolean()");
        return false;
    }

    public static String substringF(String value, double start) {
        if (Double.isNaN(start)) {
            return "";
        }
        int strlen = getStringLength(value);
        int istart = ((int) Math.round(start)) - 1;
        if (istart > strlen) {
            return "";
        }
        if (istart < 1) {
            istart = 0;
        }
        try {
            return value.substring(value.offsetByCodePoints(0, istart));
        } catch (IndexOutOfBoundsException e2) {
            runTimeError(RUN_TIME_INTERNAL_ERR, "substring()");
            return null;
        }
    }

    public static String substringF(String value, double start, double length) {
        int isum;
        if (Double.isInfinite(start) || Double.isNaN(start) || Double.isNaN(length) || length < 0.0d) {
            return "";
        }
        int istart = ((int) Math.round(start)) - 1;
        int ilength = (int) Math.round(length);
        if (Double.isInfinite(length)) {
            isum = Integer.MAX_VALUE;
        } else {
            isum = istart + ilength;
        }
        int strlen = getStringLength(value);
        if (isum < 0 || istart > strlen) {
            return "";
        }
        if (istart < 0) {
            ilength += istart;
            istart = 0;
        }
        try {
            int istart2 = value.offsetByCodePoints(0, istart);
            if (isum > strlen) {
                return value.substring(istart2);
            }
            int offset = value.offsetByCodePoints(istart2, ilength);
            return value.substring(istart2, offset);
        } catch (IndexOutOfBoundsException e2) {
            runTimeError(RUN_TIME_INTERNAL_ERR, "substring()");
            return null;
        }
    }

    public static String substring_afterF(String value, String substring) {
        int index = value.indexOf(substring);
        if (index >= 0) {
            return value.substring(index + substring.length());
        }
        return "";
    }

    public static String substring_beforeF(String value, String substring) {
        int index = value.indexOf(substring);
        if (index >= 0) {
            return value.substring(0, index);
        }
        return "";
    }

    public static String translateF(String value, String from, String to) {
        int tol = to.length();
        int froml = from.length();
        int valuel = value.length();
        StringBuilder result = threadLocalStringBuilder.get();
        result.setLength(0);
        for (int i2 = 0; i2 < valuel; i2++) {
            char ch = value.charAt(i2);
            int j2 = 0;
            while (true) {
                if (j2 >= froml) {
                    break;
                }
                if (ch != from.charAt(j2)) {
                    j2++;
                } else if (j2 < tol) {
                    result.append(to.charAt(j2));
                }
            }
            if (j2 == froml) {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String normalize_spaceF(int node, DOM dom) {
        return normalize_spaceF(dom.getStringValueX(node));
    }

    public static String normalize_spaceF(String value) {
        int i2 = 0;
        int n2 = value.length();
        StringBuilder result = threadLocalStringBuilder.get();
        result.setLength(0);
        while (i2 < n2 && isWhiteSpace(value.charAt(i2))) {
            i2++;
        }
        while (true) {
            if (i2 < n2 && !isWhiteSpace(value.charAt(i2))) {
                int i3 = i2;
                i2++;
                result.append(value.charAt(i3));
            } else if (i2 != n2) {
                while (i2 < n2 && isWhiteSpace(value.charAt(i2))) {
                    i2++;
                }
                if (i2 < n2) {
                    result.append(' ');
                }
            } else {
                return result.toString();
            }
        }
    }

    public static String generate_idF(int node) {
        if (node > 0) {
            return "N" + node;
        }
        return "";
    }

    public static String getLocalName(String value) {
        int idx = value.lastIndexOf(58);
        if (idx >= 0) {
            value = value.substring(idx + 1);
        }
        int idx2 = value.lastIndexOf(64);
        if (idx2 >= 0) {
            value = value.substring(idx2 + 1);
        }
        return value;
    }

    public static void unresolved_externalF(String name) {
        runTimeError(EXTERNAL_FUNC_ERR, name);
    }

    public static void unallowed_extension_functionF(String name) {
        runTimeError(UNALLOWED_EXTENSION_FUNCTION_ERR, name);
    }

    public static void unallowed_extension_elementF(String name) {
        runTimeError(UNALLOWED_EXTENSION_ELEMENT_ERR, name);
    }

    public static void unsupported_ElementF(String qname, boolean isExtension) {
        if (isExtension) {
            runTimeError("UNSUPPORTED_EXT_ERR", qname);
        } else {
            runTimeError("UNSUPPORTED_XSL_ERR", qname);
        }
    }

    public static String namespace_uriF(DTMAxisIterator iter, DOM dom) {
        return namespace_uriF(iter.next(), dom);
    }

    public static String system_propertyF(String name) {
        if (name.equals("xsl:version")) {
            return "1.0";
        }
        if (name.equals("xsl:vendor")) {
            return "Apache Software Foundation (Xalan XSLTC)";
        }
        if (name.equals("xsl:vendor-url")) {
            return "http://xml.apache.org/xalan-j";
        }
        runTimeError(INVALID_ARGUMENT_ERR, name, "system-property()");
        return "";
    }

    public static String namespace_uriF(int node, DOM dom) {
        String value = dom.getNodeName(node);
        int colon = value.lastIndexOf(58);
        if (colon >= 0) {
            return value.substring(0, colon);
        }
        return "";
    }

    public static String objectTypeF(Object obj) {
        if (obj instanceof String) {
            return "string";
        }
        if (obj instanceof Boolean) {
            return "boolean";
        }
        if (obj instanceof Number) {
            return "number";
        }
        if (obj instanceof DOM) {
            return "RTF";
        }
        if (obj instanceof DTMAxisIterator) {
            return "node-set";
        }
        return "unknown";
    }

    public static DTMAxisIterator nodesetF(Object obj) {
        if (obj instanceof DOM) {
            DOM dom = (DOM) obj;
            return new SingletonIterator(dom.getDocument(), true);
        }
        if (obj instanceof DTMAxisIterator) {
            return (DTMAxisIterator) obj;
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", "node-set", className);
        return null;
    }

    private static boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    private static boolean compareStrings(String lstring, String rstring, int op, DOM dom) {
        switch (op) {
            case 0:
                return lstring.equals(rstring);
            case 1:
                return !lstring.equals(rstring);
            case 2:
                return numberF(lstring, dom) > numberF(rstring, dom);
            case 3:
                return numberF(lstring, dom) < numberF(rstring, dom);
            case 4:
                return numberF(lstring, dom) >= numberF(rstring, dom);
            case 5:
                return numberF(lstring, dom) <= numberF(rstring, dom);
            default:
                runTimeError(RUN_TIME_INTERNAL_ERR, "compare()");
                return false;
        }
    }

    public static boolean compare(DTMAxisIterator left, DTMAxisIterator right, int op, DOM dom) {
        left.reset();
        while (true) {
            int lnode = left.next();
            if (lnode != -1) {
                String lvalue = dom.getStringValueX(lnode);
                right.reset();
                while (true) {
                    int rnode = right.next();
                    if (rnode != -1) {
                        if (lnode == rnode) {
                            if (op == 0) {
                                return true;
                            }
                            if (op == 1) {
                                continue;
                            }
                        }
                        if (compareStrings(lvalue, dom.getStringValueX(rnode), op, dom)) {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean compare(int node, DTMAxisIterator iterator, int op, DOM dom) {
        int rnode;
        int rnode2;
        int next;
        switch (op) {
            case 0:
                int rnode3 = iterator.next();
                if (rnode3 != -1) {
                    String value = dom.getStringValueX(node);
                    while (node != rnode3 && !value.equals(dom.getStringValueX(rnode3))) {
                        int next2 = iterator.next();
                        rnode3 = next2;
                        if (next2 == -1) {
                            break;
                        }
                    }
                }
                break;
            case 1:
                int rnode4 = iterator.next();
                if (rnode4 != -1) {
                    String value2 = dom.getStringValueX(node);
                    do {
                        if (node != rnode4 && !value2.equals(dom.getStringValueX(rnode4))) {
                            break;
                        } else {
                            next = iterator.next();
                            rnode4 = next;
                        }
                    } while (next != -1);
                }
                break;
            case 2:
                do {
                    rnode = iterator.next();
                    if (rnode == -1) {
                        break;
                    }
                } while (rnode >= node);
            case 3:
                do {
                    rnode2 = iterator.next();
                    if (rnode2 == -1) {
                        break;
                    }
                } while (rnode2 <= node);
        }
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean compare(DTMAxisIterator left, double rnumber, int op, DOM dom) {
        int node;
        int node2;
        int node3;
        int node4;
        int node5;
        int node6;
        switch (op) {
            case 0:
                do {
                    node6 = left.next();
                    if (node6 == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node6), dom) != rnumber);
                return true;
            case 1:
                do {
                    node5 = left.next();
                    if (node5 == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node5), dom) == rnumber);
                return true;
            case 2:
                do {
                    node4 = left.next();
                    if (node4 == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node4), dom) <= rnumber);
                return true;
            case 3:
                do {
                    node3 = left.next();
                    if (node3 == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node3), dom) >= rnumber);
                return true;
            case 4:
                do {
                    node2 = left.next();
                    if (node2 == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node2), dom) < rnumber);
                return true;
            case 5:
                do {
                    node = left.next();
                    if (node == -1) {
                        return false;
                    }
                } while (numberF(dom.getStringValueX(node), dom) > rnumber);
                return true;
            default:
                runTimeError(RUN_TIME_INTERNAL_ERR, "compare()");
                return false;
        }
    }

    public static boolean compare(DTMAxisIterator left, String rstring, int op, DOM dom) {
        int node;
        do {
            node = left.next();
            if (node == -1) {
                return false;
            }
        } while (!compareStrings(dom.getStringValueX(node), rstring, op, dom));
        return true;
    }

    public static boolean compare(Object left, Object right, int op, DOM dom) {
        boolean result = false;
        boolean hasSimpleArgs = hasSimpleType(left) && hasSimpleType(right);
        if (op != 0 && op != 1) {
            if ((left instanceof Node) || (right instanceof Node)) {
                if (left instanceof Boolean) {
                    right = new Boolean(booleanF(right));
                    hasSimpleArgs = true;
                }
                if (right instanceof Boolean) {
                    left = new Boolean(booleanF(left));
                    hasSimpleArgs = true;
                }
            }
            if (hasSimpleArgs) {
                switch (op) {
                    case 2:
                        return numberF(left, dom) > numberF(right, dom);
                    case 3:
                        return numberF(left, dom) < numberF(right, dom);
                    case 4:
                        return numberF(left, dom) >= numberF(right, dom);
                    case 5:
                        return numberF(left, dom) <= numberF(right, dom);
                    default:
                        runTimeError(RUN_TIME_INTERNAL_ERR, "compare()");
                        break;
                }
            }
        }
        if (hasSimpleArgs) {
            if ((left instanceof Boolean) || (right instanceof Boolean)) {
                result = booleanF(left) == booleanF(right);
            } else if ((left instanceof Double) || (right instanceof Double) || (left instanceof Integer) || (right instanceof Integer)) {
                result = numberF(left, dom) == numberF(right, dom);
            } else {
                result = stringF(left, dom).equals(stringF(right, dom));
            }
            if (op == 1) {
                result = !result;
            }
        } else {
            if (left instanceof Node) {
                left = new SingletonIterator(((Node) left).node);
            }
            if (right instanceof Node) {
                right = new SingletonIterator(((Node) right).node);
            }
            if (hasSimpleType(left) || ((left instanceof DOM) && (right instanceof DTMAxisIterator))) {
                Object temp = right;
                right = left;
                left = temp;
                op = Operators.swapOp(op);
            }
            if (left instanceof DOM) {
                if (right instanceof Boolean) {
                    return ((Boolean) right).booleanValue() == (op == 0);
                }
                String sleft = ((DOM) left).getStringValue();
                if (right instanceof Number) {
                    result = ((Number) right).doubleValue() == stringToReal(sleft);
                } else if (right instanceof String) {
                    result = sleft.equals((String) right);
                } else if (right instanceof DOM) {
                    result = sleft.equals(((DOM) right).getStringValue());
                }
                if (op == 1) {
                    result = !result;
                }
                return result;
            }
            DTMAxisIterator iter = ((DTMAxisIterator) left).reset();
            if (right instanceof DTMAxisIterator) {
                result = compare(iter, (DTMAxisIterator) right, op, dom);
            } else if (right instanceof String) {
                result = compare(iter, (String) right, op, dom);
            } else if (right instanceof Number) {
                double temp2 = ((Number) right).doubleValue();
                result = compare(iter, temp2, op, dom);
            } else if (right instanceof Boolean) {
                boolean temp3 = ((Boolean) right).booleanValue();
                result = (iter.reset().next() != -1) == temp3;
            } else if (right instanceof DOM) {
                result = compare(iter, ((DOM) right).getStringValue(), op, dom);
            } else {
                if (right == null) {
                    return false;
                }
                String className = right.getClass().getName();
                runTimeError(INVALID_ARGUMENT_ERR, className, "compare()");
            }
        }
        return result;
    }

    public static boolean testLanguage(String testLang, DOM dom, int node) {
        String nodeLang = dom.getLanguage(node);
        if (nodeLang == null) {
            return false;
        }
        String nodeLang2 = nodeLang.toLowerCase();
        String testLang2 = testLang.toLowerCase();
        if (testLang2.length() == 2) {
            return nodeLang2.startsWith(testLang2);
        }
        return nodeLang2.equals(testLang2);
    }

    private static boolean hasSimpleType(Object obj) {
        return (obj instanceof Boolean) || (obj instanceof Double) || (obj instanceof Integer) || (obj instanceof String) || (obj instanceof Node) || (obj instanceof DOM);
    }

    public static double stringToReal(String s2) {
        try {
            return Double.valueOf(s2).doubleValue();
        } catch (NumberFormatException e2) {
            return Double.NaN;
        }
    }

    public static int stringToInt(String s2) {
        try {
            return Integer.parseInt(s2);
        } catch (NumberFormatException e2) {
            return -1;
        }
    }

    public static String realToString(double d2) {
        double m2 = Math.abs(d2);
        if (m2 >= 0.001d && m2 < upperBounds) {
            String result = Double.toString(d2);
            int length = result.length();
            if (result.charAt(length - 2) == '.' && result.charAt(length - 1) == '0') {
                return result.substring(0, length - 2);
            }
            return result;
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2)) {
            return Double.toString(d2);
        }
        StringBuffer result2 = threadLocalStringBuffer.get();
        result2.setLength(0);
        xpathFormatter.format(d2 + 0.0d, result2, _fieldPosition);
        return result2.toString();
    }

    public static int realToInt(double d2) {
        return (int) d2;
    }

    public static String formatNumber(double number, String pattern, DecimalFormat formatter) {
        if (formatter == null) {
            formatter = defaultFormatter;
        }
        try {
            StringBuffer result = threadLocalStringBuffer.get();
            result.setLength(0);
            if (pattern != defaultPattern) {
                formatter.applyLocalizedPattern(pattern);
            }
            formatter.format(number, result, _fieldPosition);
            return result.toString();
        } catch (IllegalArgumentException e2) {
            runTimeError(FORMAT_NUMBER_ERR, Double.toString(number), pattern);
            return "";
        }
    }

    public static DTMAxisIterator referenceToNodeSet(Object obj) {
        if (obj instanceof Node) {
            return new SingletonIterator(((Node) obj).node);
        }
        if (obj instanceof DTMAxisIterator) {
            return ((DTMAxisIterator) obj).cloneIterator().reset();
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, "node-set");
        return null;
    }

    public static NodeList referenceToNodeList(Object obj, DOM dom) {
        if ((obj instanceof Node) || (obj instanceof DTMAxisIterator)) {
            DTMAxisIterator iter = referenceToNodeSet(obj);
            return dom.makeNodeList(iter);
        }
        if (obj instanceof DOM) {
            DOM dom2 = (DOM) obj;
            return dom2.makeNodeList(0);
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, "org.w3c.dom.NodeList");
        return null;
    }

    public static org.w3c.dom.Node referenceToNode(Object obj, DOM dom) {
        if ((obj instanceof Node) || (obj instanceof DTMAxisIterator)) {
            DTMAxisIterator iter = referenceToNodeSet(obj);
            return dom.makeNode(iter);
        }
        if (obj instanceof DOM) {
            DOM dom2 = (DOM) obj;
            DTMAxisIterator iter2 = dom2.getChildren(0);
            return dom2.makeNode(iter2);
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, "org.w3c.dom.Node");
        return null;
    }

    public static long referenceToLong(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, Long.TYPE);
        return 0L;
    }

    public static double referenceToDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, Double.TYPE);
        return 0.0d;
    }

    public static boolean referenceToBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, Boolean.TYPE);
        return false;
    }

    public static String referenceToString(Object obj, DOM dom) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof DTMAxisIterator) {
            return dom.getStringValueX(((DTMAxisIterator) obj).reset().next());
        }
        if (obj instanceof Node) {
            return dom.getStringValueX(((Node) obj).node);
        }
        if (obj instanceof DOM) {
            return ((DOM) obj).getStringValue();
        }
        String className = obj.getClass().getName();
        runTimeError("DATA_CONVERSION_ERR", className, String.class);
        return null;
    }

    public static DTMAxisIterator node2Iterator(final org.w3c.dom.Node node, Translet translet, DOM dom) {
        NodeList nodelist = new NodeList() { // from class: com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.3
            @Override // org.w3c.dom.NodeList
            public int getLength() {
                return 1;
            }

            @Override // org.w3c.dom.NodeList
            public org.w3c.dom.Node item(int index) {
                if (index == 0) {
                    return node;
                }
                return null;
            }
        };
        return nodeList2Iterator(nodelist, translet, dom);
    }

    private static DTMAxisIterator nodeList2IteratorUsingHandleFromNode(NodeList nodeList, Translet translet, DOM dom) {
        int dTMNodeNumber;
        int n2 = nodeList.getLength();
        int[] dtmHandles = new int[n2];
        DTMManager dtmManager = null;
        if (dom instanceof MultiDOM) {
            dtmManager = ((MultiDOM) dom).getDTMManager();
        }
        for (int i2 = 0; i2 < n2; i2++) {
            org.w3c.dom.Node node = nodeList.item(i2);
            if (dtmManager != null) {
                dTMNodeNumber = dtmManager.getDTMHandleFromNode(node);
            } else if ((node instanceof DTMNodeProxy) && ((DTMNodeProxy) node).getDTM() == dom) {
                dTMNodeNumber = ((DTMNodeProxy) node).getDTMNodeNumber();
            } else {
                runTimeError(RUN_TIME_INTERNAL_ERR, "need MultiDOM");
                return null;
            }
            int handle = dTMNodeNumber;
            dtmHandles[i2] = handle;
            System.out.println("Node " + i2 + " has handle 0x" + Integer.toString(handle, 16));
        }
        return new ArrayNodeListIterator(dtmHandles);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.sun.org.apache.xml.internal.dtm.DTMAxisIterator nodeList2Iterator(org.w3c.dom.NodeList r8, com.sun.org.apache.xalan.internal.xsltc.Translet r9, com.sun.org.apache.xalan.internal.xsltc.DOM r10) throws org.w3c.dom.DOMException {
        /*
            Method dump skipped, instructions count: 846
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary.nodeList2Iterator(org.w3c.dom.NodeList, com.sun.org.apache.xalan.internal.xsltc.Translet, com.sun.org.apache.xalan.internal.xsltc.DOM):com.sun.org.apache.xml.internal.dtm.DTMAxisIterator");
    }

    public static DOM referenceToResultTree(Object obj) {
        try {
            return (DOM) obj;
        } catch (IllegalArgumentException e2) {
            String className = obj.getClass().getName();
            runTimeError("DATA_CONVERSION_ERR", FXMLLoader.REFERENCE_TAG, className);
            return null;
        }
    }

    public static DTMAxisIterator getSingleNode(DTMAxisIterator iterator) {
        int node = iterator.next();
        return new SingletonIterator(node);
    }

    public static void copy(Object obj, SerializationHandler handler, int node, DOM dom) {
        try {
            if (obj instanceof DTMAxisIterator) {
                DTMAxisIterator iter = (DTMAxisIterator) obj;
                dom.copy(iter.reset(), handler);
            } else if (obj instanceof Node) {
                dom.copy(((Node) obj).node, handler);
            } else if (obj instanceof DOM) {
                DOM newDom = (DOM) obj;
                newDom.copy(newDom.getDocument(), handler);
            } else {
                String string = obj.toString();
                int length = string.length();
                if (length > _characterArray.length) {
                    _characterArray = new char[length];
                }
                string.getChars(0, length, _characterArray, 0);
                handler.characters(_characterArray, 0, length);
            }
        } catch (SAXException e2) {
            runTimeError(RUN_TIME_COPY_ERR);
        }
    }

    public static void checkAttribQName(String name) {
        int firstOccur = name.indexOf(58);
        int lastOccur = name.lastIndexOf(58);
        String localName = name.substring(lastOccur + 1);
        if (firstOccur > 0) {
            String newPrefix = name.substring(0, firstOccur);
            if (firstOccur != lastOccur) {
                String oriPrefix = name.substring(firstOccur + 1, lastOccur);
                if (!XML11Char.isXML11ValidNCName(oriPrefix)) {
                    runTimeError("INVALID_QNAME_ERR", oriPrefix + CallSiteDescriptor.TOKEN_DELIMITER + localName);
                }
            }
            if (!XML11Char.isXML11ValidNCName(newPrefix)) {
                runTimeError("INVALID_QNAME_ERR", newPrefix + CallSiteDescriptor.TOKEN_DELIMITER + localName);
            }
        }
        if (!XML11Char.isXML11ValidNCName(localName) || localName.equals("xmlns")) {
            runTimeError("INVALID_QNAME_ERR", localName);
        }
    }

    public static void checkNCName(String name) {
        if (!XML11Char.isXML11ValidNCName(name)) {
            runTimeError("INVALID_NCNAME_ERR", name);
        }
    }

    public static void checkQName(String name) {
        if (!XML11Char.isXML11ValidQName(name)) {
            runTimeError("INVALID_QNAME_ERR", name);
        }
    }

    public static String startXslElement(String qname, String namespace, SerializationHandler handler, DOM dom, int node) {
        try {
            int index = qname.indexOf(58);
            if (index > 0) {
                String prefix = qname.substring(0, index);
                if (namespace == null || namespace.length() == 0) {
                    try {
                        namespace = dom.lookupNamespace(node, prefix);
                    } catch (RuntimeException e2) {
                        handler.flushPending();
                        NamespaceMappings nm = handler.getNamespaceMappings();
                        namespace = nm.lookupNamespace(prefix);
                        if (namespace == null) {
                            runTimeError(NAMESPACE_PREFIX_ERR, prefix);
                        }
                    }
                }
                handler.startElement(namespace, qname.substring(index + 1), qname);
                handler.namespaceAfterStartElement(prefix, namespace);
            } else if (namespace != null && namespace.length() > 0) {
                String prefix2 = generatePrefix();
                qname = prefix2 + ':' + qname;
                handler.startElement(namespace, qname, qname);
                handler.namespaceAfterStartElement(prefix2, namespace);
            } else {
                handler.startElement(null, null, qname);
            }
            return qname;
        } catch (SAXException e3) {
            throw new RuntimeException(e3.getMessage());
        }
    }

    public static String getPrefix(String qname) {
        int index = qname.indexOf(58);
        if (index > 0) {
            return qname.substring(0, index);
        }
        return null;
    }

    public static String generatePrefix() {
        return com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_NS + threadLocalPrefixIndex.get().getAndIncrement();
    }

    public static void resetPrefixIndex() {
        threadLocalPrefixIndex.get().set(0);
    }

    public static void runTimeError(String code) {
        throw new RuntimeException(m_bundle.getString(code));
    }

    public static void runTimeError(String code, Object[] args) {
        String message = MessageFormat.format(m_bundle.getString(code), args);
        throw new RuntimeException(message);
    }

    public static void runTimeError(String code, Object arg0) {
        runTimeError(code, new Object[]{arg0});
    }

    public static void runTimeError(String code, Object arg0, Object arg1) {
        runTimeError(code, new Object[]{arg0, arg1});
    }

    public static void consoleOutput(String msg) {
        System.out.println(msg);
    }

    public static String replace(String base, char ch, String str) {
        return base.indexOf(ch) < 0 ? base : replace(base, String.valueOf(ch), new String[]{str});
    }

    public static String replace(String base, String delim, String[] str) {
        int len = base.length();
        StringBuilder result = threadLocalStringBuilder.get();
        result.setLength(0);
        for (int i2 = 0; i2 < len; i2++) {
            char ch = base.charAt(i2);
            int k2 = delim.indexOf(ch);
            if (k2 >= 0) {
                result.append(str[k2]);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String mapQNameToJavaName(String base) {
        return replace(base, ".-:/{}?#%*", new String[]{"$dot$", "$dash$", "$colon$", "$slash$", "", "$colon$", "$ques$", "$hash$", "$per$", "$aster$"});
    }

    public static int getStringLength(String str) {
        return str.codePointCount(0, str.length());
    }
}
