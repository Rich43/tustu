package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xpath.internal.NodeSet;
import java.util.StringTokenizer;
import javax.swing.JSplitPane;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltStrings.class */
public class ExsltStrings extends ExsltBase {
    public static String align(String targetStr, String paddingStr, String type) {
        if (targetStr.length() >= paddingStr.length()) {
            return targetStr.substring(0, paddingStr.length());
        }
        if (type.equals(JSplitPane.RIGHT)) {
            return paddingStr.substring(0, paddingStr.length() - targetStr.length()) + targetStr;
        }
        if (type.equals("center")) {
            int startIndex = (paddingStr.length() - targetStr.length()) / 2;
            return paddingStr.substring(0, startIndex) + targetStr + paddingStr.substring(startIndex + targetStr.length());
        }
        return targetStr + paddingStr.substring(targetStr.length());
    }

    public static String align(String targetStr, String paddingStr) {
        return align(targetStr, paddingStr, JSplitPane.LEFT);
    }

    public static String concat(NodeList nl) {
        StringBuffer sb = new StringBuffer();
        for (int i2 = 0; i2 < nl.getLength(); i2++) {
            Node node = nl.item(i2);
            String value = toString(node);
            if (value != null && value.length() > 0) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    public static String padding(double length, String pattern) {
        if (pattern == null || pattern.length() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int len = (int) length;
        int index = 0;
        for (int numAdded = 0; numAdded < len; numAdded++) {
            if (index == pattern.length()) {
                index = 0;
            }
            sb.append(pattern.charAt(index));
            index++;
        }
        return sb.toString();
    }

    public static String padding(double length) {
        return padding(length, " ");
    }

    public static NodeList split(String str, String pattern) {
        String token;
        NodeSet resultSet = new NodeSet();
        resultSet.setShouldCacheNodes(true);
        boolean done = false;
        int fromIndex = 0;
        while (!done && fromIndex < str.length()) {
            int matchIndex = str.indexOf(pattern, fromIndex);
            if (matchIndex >= 0) {
                token = str.substring(fromIndex, matchIndex);
                fromIndex = matchIndex + pattern.length();
            } else {
                done = true;
                token = str.substring(fromIndex);
            }
            Document doc = JdkXmlUtils.getDOMDocument();
            synchronized (doc) {
                Element element = doc.createElement(SchemaSymbols.ATTVAL_TOKEN);
                Text text = doc.createTextNode(token);
                element.appendChild(text);
                resultSet.addNode(element);
            }
        }
        return resultSet;
    }

    public static NodeList split(String str) {
        return split(str, " ");
    }

    public static NodeList tokenize(String toTokenize, String delims) {
        NodeSet resultSet = new NodeSet();
        if (delims != null && delims.length() > 0) {
            StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
            Document doc = JdkXmlUtils.getDOMDocument();
            synchronized (doc) {
                while (lTokenizer.hasMoreTokens()) {
                    Element element = doc.createElement(SchemaSymbols.ATTVAL_TOKEN);
                    element.appendChild(doc.createTextNode(lTokenizer.nextToken()));
                    resultSet.addNode(element);
                }
            }
        } else {
            Document doc2 = JdkXmlUtils.getDOMDocument();
            synchronized (doc2) {
                for (int i2 = 0; i2 < toTokenize.length(); i2++) {
                    Element element2 = doc2.createElement(SchemaSymbols.ATTVAL_TOKEN);
                    element2.appendChild(doc2.createTextNode(toTokenize.substring(i2, i2 + 1)));
                    resultSet.addNode(element2);
                }
            }
        }
        return resultSet;
    }

    public static NodeList tokenize(String toTokenize) {
        return tokenize(toTokenize, " \t\n\r");
    }
}
