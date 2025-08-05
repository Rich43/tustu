package com.intel.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.bluetooth.DataElement;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.util.locale.LanguageTag;

/* loaded from: bluecove-bluez-2.1.1.jar:com/intel/bluetooth/BlueZServiceRecordXML.class */
class BlueZServiceRecordXML {
    private static final Map<String, Integer> integerXMLtypes = new HashMap();
    private static final Map<Integer, String> allXMLtypes = new HashMap();

    BlueZServiceRecordXML() {
    }

    static {
        integerXMLtypes.put("uint8", 8);
        integerXMLtypes.put("uint16", 9);
        integerXMLtypes.put("uint32", 10);
        integerXMLtypes.put("int8", 16);
        integerXMLtypes.put("int16", 17);
        integerXMLtypes.put("int32", 18);
        integerXMLtypes.put("int64", 19);
        allXMLtypes.put(8, "uint8");
        allXMLtypes.put(9, "uint16");
        allXMLtypes.put(10, "uint32");
        allXMLtypes.put(11, "uint64");
        allXMLtypes.put(12, "uint128");
        allXMLtypes.put(16, "int8");
        allXMLtypes.put(17, "int16");
        allXMLtypes.put(18, "int32");
        allXMLtypes.put(19, "int64");
        allXMLtypes.put(20, "int128");
        allXMLtypes.put(40, "boolean");
        allXMLtypes.put(24, "uuid");
        allXMLtypes.put(32, "text");
        allXMLtypes.put(64, "url");
        allXMLtypes.put(0, "nil");
        allXMLtypes.put(56, "alternate");
        allXMLtypes.put(48, "sequence");
    }

    private static String toHexStringSigned(long l2) {
        if (l2 >= 0) {
            return "0x" + Long.toHexString(l2);
        }
        return "-0x" + Long.toHexString(-l2);
    }

    private static void appendUUID(StringBuffer buf, UUID uuid) {
        String str = uuid.toString().toUpperCase();
        int shortIdx = str.indexOf(BluetoothConsts.SHORT_UUID_BASE);
        if (shortIdx != -1 && shortIdx + BluetoothConsts.SHORT_UUID_BASE.length() == str.length()) {
            buf.append("0x").append(str.substring(0, shortIdx));
        } else {
            buf.append(str.substring(0, 8)).append('-').append(str.substring(8, 12)).append('-');
            buf.append(str.substring(12, 16)).append('-').append(str.substring(16, 20)).append('-').append(str.substring(20));
        }
    }

    static void appendDataElement(StringBuffer buf, DataElement d2) {
        int valueType = d2.getDataType();
        String type = allXMLtypes.get(Integer.valueOf(valueType));
        buf.append("<").append(type);
        switch (valueType) {
            case 0:
                buf.append(">\n");
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
            case 15:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalArgumentException("DataElement type " + valueType);
            case 8:
            case 9:
            case 10:
                buf.append(" value=\"0x").append(Long.toHexString(d2.getLong())).append("\">");
                break;
            case 11:
                byte[] b8 = (byte[]) d2.getValue();
                buf.append(" value=\"0x");
                for (int i2 = 0; i2 < b8.length; i2++) {
                    buf.append(Integer.toHexString((b8[i2] >> 4) & 15));
                    buf.append(Integer.toHexString(b8[i2] & 15));
                }
                buf.append("\">");
                break;
            case 12:
            case 20:
                byte[] b16 = (byte[]) d2.getValue();
                buf.append(" value=\"");
                for (int i3 = b16.length - 1; i3 >= 0; i3--) {
                    buf.append(Integer.toHexString((b16[i3] >> 4) & 15));
                    buf.append(Integer.toHexString(b16[i3] & 15));
                }
                buf.append("\">");
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                buf.append(" value=\"").append(toHexStringSigned(d2.getLong())).append("\">");
                break;
            case 24:
                buf.append(" value=\"");
                appendUUID(buf, (UUID) d2.getValue());
                buf.append("\">");
                break;
            case 32:
            case 64:
                buf.append(" value=\"").append(d2.getValue()).append("\">");
                break;
            case 40:
                buf.append(" value=\"").append(d2.getBoolean() ? "true" : "false").append("\">");
                break;
            case 48:
            case 56:
                buf.append(">\n");
                Enumeration en = (Enumeration) d2.getValue();
                while (en.hasMoreElements()) {
                    appendDataElement(buf, (DataElement) en.nextElement2());
                }
                break;
        }
        buf.append("</").append(type).append(">\n");
    }

    public static String exportXMLRecord(ServiceRecord serviceRecord) {
        StringBuffer b2 = new StringBuffer();
        b2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        b2.append("<record>\n");
        int[] ids = serviceRecord.getAttributeIDs();
        Vector<Integer> sorted = new Vector<>();
        for (int i2 : ids) {
            sorted.addElement(new Integer(i2));
        }
        Collections.sort(sorted);
        Iterator i$ = sorted.iterator();
        while (i$.hasNext()) {
            Integer id = i$.next();
            b2.append("<attribute id=\"0x").append(Long.toHexString(id.intValue())).append("\" >\n");
            appendDataElement(b2, serviceRecord.getAttributeValue(id.intValue()));
            b2.append("</attribute>\n");
        }
        b2.append("</record>");
        return b2.toString();
    }

    public static Map<Integer, DataElement> parsXMLRecord(String xml) throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            Element root = doc.getDocumentElement();
            if (!"record".equals(root.getTagName())) {
                throw new IOException("SDP xml record expected, got " + root.getTagName());
            }
            Map<Integer, DataElement> elements = new HashMap<>();
            NodeList nodes = root.getElementsByTagName("attribute");
            for (int i2 = 0; i2 < nodes.getLength(); i2++) {
                Node node = nodes.item(i2);
                Node idNode = node.getAttributes().getNamedItem("id");
                int id = parsInt(idNode.getNodeValue());
                NodeList children = node.getChildNodes();
                int j2 = 0;
                while (true) {
                    if (children == null || j2 >= children.getLength()) {
                        break;
                    }
                    Node child = children.item(j2);
                    if (child.getNodeType() != 1) {
                        j2++;
                    } else {
                        elements.put(Integer.valueOf(id), parsDataElement(child));
                        break;
                    }
                }
            }
            return elements;
        } catch (ParserConfigurationException e2) {
            throw ((IOException) UtilsJavaSE.initCause(new IOException(e2.getMessage()), e2));
        } catch (SAXException e3) {
            throw ((IOException) UtilsJavaSE.initCause(new IOException(e3.getMessage()), e3));
        }
    }

    private static int parsInt(String value) {
        if (value.startsWith("0x")) {
            return Integer.valueOf(value.substring(2), 16).intValue();
        }
        return Integer.valueOf(value).intValue();
    }

    private static long parsLong(String value) {
        if (value.startsWith("0x")) {
            return Long.valueOf(value.substring(2), 16).longValue();
        }
        if (value.startsWith("-0x")) {
            return -Long.valueOf(value.substring(3), 16).longValue();
        }
        return Long.valueOf(value).longValue();
    }

    private static long getLongValue(Node node) throws IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        return parsLong(valueNode.getNodeValue());
    }

    private static boolean getBoolValue(Node node) throws IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        return "true".equals(valueNode.getNodeValue());
    }

    private static UUID getUUIDValue(Node node) throws DOMException, IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        String value = valueNode.getNodeValue();
        if (value.length() == 32) {
            return new UUID(value.replace(LanguageTag.SEP, ""), false);
        }
        if (value.startsWith("0x")) {
            return new UUID(Long.valueOf(value.substring(2), 16).longValue());
        }
        String value2 = value.replace(LanguageTag.SEP, "");
        if (value2.length() == 32) {
            return new UUID(value2, false);
        }
        throw new IOException("Unknown UUID format " + value);
    }

    private static byte[] getByteArrayValue(Node node, int length) throws DOMException, IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        String value = valueNode.getNodeValue();
        if (length != value.length() / 2) {
            throw new IOException("value attribute invalid length " + value.length());
        }
        byte[] result = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            result[(length - 1) - i2] = (byte) (Integer.parseInt(value.substring(i2 * 2, (i2 * 2) + 2), 16) & 255);
        }
        return result;
    }

    private static byte[] getByteArrayUINT8(Node node, int length) throws DOMException, IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        String value = valueNode.getNodeValue();
        int radix = 10;
        if (value.startsWith("0x")) {
            value = value.substring(2);
            radix = 16;
        }
        if (length != value.length() / 2) {
            throw new IOException("value attribute invalid length " + value.length());
        }
        byte[] result = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            result[i2] = (byte) (Integer.parseInt(value.substring(i2 * 2, (i2 * 2) + 2), radix) & 255);
        }
        return result;
    }

    private static String getTextValue(Node node) throws DOMException, IOException {
        Node valueNode = node.getAttributes().getNamedItem("value");
        if (valueNode == null) {
            throw new IOException("value attribute expected in " + node.getNodeName());
        }
        Node encodingNode = node.getAttributes().getNamedItem("encoding");
        if (encodingNode == null) {
            return valueNode.getNodeValue();
        }
        if ("hex".equals(encodingNode.getNodeValue())) {
            StringBuffer b2 = new StringBuffer();
            String value = valueNode.getNodeValue();
            for (int i2 = 0; i2 < value.length() / 2; i2++) {
                b2.append((char) Integer.parseInt(value.substring(i2 * 2, (i2 * 2) + 2), 16));
            }
            return b2.toString();
        }
        throw new IOException("Unknown text encoding " + encodingNode.getNodeValue());
    }

    private static DataElement parsDataElement(Node node) throws IOException {
        String name = node.getNodeName();
        Integer intValueType = integerXMLtypes.get(name);
        if (intValueType != null) {
            return new DataElement(intValueType.intValue(), getLongValue(node));
        }
        if ("sequence".equals(name)) {
            DataElement seq = new DataElement(48);
            NodeList children = node.getChildNodes();
            for (int j2 = 0; children != null && j2 < children.getLength(); j2++) {
                Node child = children.item(j2);
                if (child.getNodeType() == 1) {
                    seq.addElement(parsDataElement(child));
                }
            }
            return seq;
        }
        if ("alternate".equals(name)) {
            DataElement seq2 = new DataElement(56);
            NodeList children2 = node.getChildNodes();
            for (int j3 = 0; children2 != null && j3 < children2.getLength(); j3++) {
                Node child2 = children2.item(j3);
                if (child2.getNodeType() == 1) {
                    seq2.addElement(parsDataElement(child2));
                }
            }
            return seq2;
        }
        if ("uuid".equals(name)) {
            return new DataElement(24, getUUIDValue(node));
        }
        if ("text".equals(name)) {
            return new DataElement(32, getTextValue(node));
        }
        if ("url".equals(name)) {
            return new DataElement(64, getTextValue(node));
        }
        if ("nil".equals(name)) {
            return new DataElement(0);
        }
        if ("boolean".equals(name)) {
            return new DataElement(getBoolValue(node));
        }
        if ("uint64".equals(name)) {
            return new DataElement(11, getByteArrayUINT8(node, 8));
        }
        if ("int128".equals(name)) {
            return new DataElement(20, getByteArrayValue(node, 16));
        }
        if ("uint128".equals(name)) {
            return new DataElement(12, getByteArrayValue(node, 16));
        }
        throw new IOException("Unrecognized DataElement " + name);
    }
}
