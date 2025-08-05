package com.sun.org.apache.xml.internal.security.c14n.implementations;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/XmlAttrStack.class */
class XmlAttrStack {
    private static final Logger LOG = LoggerFactory.getLogger(XmlAttrStack.class);
    private XmlsStackElement cur;
    private boolean c14n11;
    private int currentLevel = 0;
    private int lastlevel = 0;
    private List<XmlsStackElement> levels = new ArrayList();

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/XmlAttrStack$XmlsStackElement.class */
    static class XmlsStackElement {
        int level;
        boolean rendered = false;
        List<Attr> nodes = new ArrayList();

        XmlsStackElement() {
        }
    }

    public XmlAttrStack(boolean z2) {
        this.c14n11 = z2;
    }

    void push(int i2) {
        this.currentLevel = i2;
        if (this.currentLevel == -1) {
            return;
        }
        this.cur = null;
        while (this.lastlevel >= this.currentLevel) {
            this.levels.remove(this.levels.size() - 1);
            int size = this.levels.size();
            if (size == 0) {
                this.lastlevel = 0;
                return;
            }
            this.lastlevel = this.levels.get(size - 1).level;
        }
    }

    void addXmlnsAttr(Attr attr) {
        if (this.cur == null) {
            this.cur = new XmlsStackElement();
            this.cur.level = this.currentLevel;
            this.levels.add(this.cur);
            this.lastlevel = this.currentLevel;
        }
        this.cur.nodes.add(attr);
    }

    void getXmlnsAttr(Collection<Attr> collection) throws DOMException {
        int size = this.levels.size() - 1;
        if (this.cur == null) {
            this.cur = new XmlsStackElement();
            this.cur.level = this.currentLevel;
            this.lastlevel = this.currentLevel;
            this.levels.add(this.cur);
        }
        boolean z2 = false;
        if (size == -1) {
            z2 = true;
        } else {
            XmlsStackElement xmlsStackElement = this.levels.get(size);
            if (xmlsStackElement.rendered && xmlsStackElement.level + 1 == this.currentLevel) {
                z2 = true;
            }
        }
        if (z2) {
            collection.addAll(this.cur.nodes);
            this.cur.rendered = true;
            return;
        }
        HashMap map = new HashMap();
        if (this.c14n11) {
            ArrayList<Attr> arrayList = new ArrayList();
            boolean z3 = true;
            while (size >= 0) {
                XmlsStackElement xmlsStackElement2 = this.levels.get(size);
                if (xmlsStackElement2.rendered) {
                    z3 = false;
                }
                Iterator<Attr> it = xmlsStackElement2.nodes.iterator();
                while (it.hasNext() && z3) {
                    Attr next = it.next();
                    if (next.getLocalName().equals("base") && !xmlsStackElement2.rendered) {
                        arrayList.add(next);
                    } else if (!map.containsKey(next.getName())) {
                        map.put(next.getName(), next);
                    }
                }
                size--;
            }
            if (!arrayList.isEmpty()) {
                Iterator<Attr> it2 = collection.iterator();
                String value = null;
                Attr attr = null;
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Attr next2 = it2.next();
                    if (next2.getLocalName().equals("base")) {
                        value = next2.getValue();
                        attr = next2;
                        break;
                    }
                }
                for (Attr attr2 : arrayList) {
                    if (value == null) {
                        value = attr2.getValue();
                        attr = attr2;
                    } else {
                        try {
                            value = joinURI(attr2.getValue(), value);
                        } catch (URISyntaxException e2) {
                            LOG.debug(e2.getMessage(), e2);
                        }
                    }
                }
                if (value != null && value.length() != 0) {
                    attr.setValue(value);
                    collection.add(attr);
                }
            }
        } else {
            while (size >= 0) {
                for (Attr attr3 : this.levels.get(size).nodes) {
                    if (!map.containsKey(attr3.getName())) {
                        map.put(attr3.getName(), attr3);
                    }
                }
                size--;
            }
        }
        this.cur.rendered = true;
        collection.addAll(map.values());
    }

    private static String joinURI(String str, String str2) throws URISyntaxException {
        String str3;
        String strRemoveDotSegments;
        String str4;
        String str5;
        String str6;
        String scheme = null;
        String authority = null;
        String path = "";
        String query = null;
        if (str != null) {
            if (str.endsWith(Constants.ATTRVAL_PARENT)) {
                str = str + "/";
            }
            URI uri = new URI(str);
            scheme = uri.getScheme();
            authority = uri.getAuthority();
            path = uri.getPath();
            query = uri.getQuery();
        }
        URI uri2 = new URI(str2);
        String scheme2 = uri2.getScheme();
        String authority2 = uri2.getAuthority();
        String path2 = uri2.getPath();
        String query2 = uri2.getQuery();
        if (scheme2 != null && scheme2.equals(scheme)) {
            scheme2 = null;
        }
        if (scheme2 != null) {
            str6 = scheme2;
            str5 = authority2;
            strRemoveDotSegments = removeDotSegments(path2);
            str4 = query2;
        } else {
            if (authority2 != null) {
                str5 = authority2;
                strRemoveDotSegments = removeDotSegments(path2);
                str4 = query2;
            } else {
                if (path2.length() == 0) {
                    strRemoveDotSegments = path;
                    if (query2 != null) {
                        str4 = query2;
                    } else {
                        str4 = query;
                    }
                } else {
                    if (path2.startsWith("/")) {
                        strRemoveDotSegments = removeDotSegments(path2);
                    } else {
                        if (authority != null && path.length() == 0) {
                            str3 = "/" + path2;
                        } else {
                            int iLastIndexOf = path.lastIndexOf(47);
                            if (iLastIndexOf == -1) {
                                str3 = path2;
                            } else {
                                str3 = path.substring(0, iLastIndexOf + 1) + path2;
                            }
                        }
                        strRemoveDotSegments = removeDotSegments(str3);
                    }
                    str4 = query2;
                }
                str5 = authority;
            }
            str6 = scheme;
        }
        return new URI(str6, str5, strRemoveDotSegments, str4, null).toString();
    }

    private static String removeDotSegments(String str) {
        String strSubstring;
        int iIndexOf;
        String strSubstring2;
        String strSubstring3;
        LOG.debug("STEP OUTPUT BUFFER\t\tINPUT BUFFER");
        String strReplaceAll = str;
        while (true) {
            strSubstring = strReplaceAll;
            if (strSubstring.indexOf("//") <= -1) {
                break;
            }
            strReplaceAll = strSubstring.replaceAll("//", "/");
        }
        StringBuilder sb = new StringBuilder();
        if (strSubstring.charAt(0) == '/') {
            sb.append("/");
            strSubstring = strSubstring.substring(1);
        }
        printStep("1 ", sb.toString(), strSubstring);
        while (strSubstring.length() != 0) {
            if (strSubstring.startsWith("./")) {
                strSubstring = strSubstring.substring(2);
                printStep("2A", sb.toString(), strSubstring);
            } else if (strSubstring.startsWith("../")) {
                strSubstring = strSubstring.substring(3);
                if (!sb.toString().equals("/")) {
                    sb.append("../");
                }
                printStep("2A", sb.toString(), strSubstring);
            } else if (strSubstring.startsWith("/./")) {
                strSubstring = strSubstring.substring(2);
                printStep("2B", sb.toString(), strSubstring);
            } else if (strSubstring.equals("/.")) {
                strSubstring = strSubstring.replaceFirst("/.", "/");
                printStep("2B", sb.toString(), strSubstring);
            } else if (strSubstring.startsWith("/../")) {
                strSubstring = strSubstring.substring(3);
                if (sb.length() == 0) {
                    sb.append("/");
                } else if (sb.toString().endsWith("../")) {
                    sb.append(Constants.ATTRVAL_PARENT);
                } else if (sb.toString().endsWith(Constants.ATTRVAL_PARENT)) {
                    sb.append("/..");
                } else {
                    int iLastIndexOf = sb.lastIndexOf("/");
                    if (iLastIndexOf == -1) {
                        sb = new StringBuilder();
                        if (strSubstring.charAt(0) == '/') {
                            strSubstring = strSubstring.substring(1);
                        }
                    } else {
                        sb = sb.delete(iLastIndexOf, sb.length());
                    }
                }
                printStep("2C", sb.toString(), strSubstring);
            } else if (strSubstring.equals("/..")) {
                strSubstring = strSubstring.replaceFirst("/..", "/");
                if (sb.length() == 0) {
                    sb.append("/");
                } else if (sb.toString().endsWith("../")) {
                    sb.append(Constants.ATTRVAL_PARENT);
                } else if (sb.toString().endsWith(Constants.ATTRVAL_PARENT)) {
                    sb.append("/..");
                } else {
                    int iLastIndexOf2 = sb.lastIndexOf("/");
                    if (iLastIndexOf2 == -1) {
                        sb = new StringBuilder();
                        if (strSubstring.charAt(0) == '/') {
                            strSubstring = strSubstring.substring(1);
                        }
                    } else {
                        sb = sb.delete(iLastIndexOf2, sb.length());
                    }
                }
                printStep("2C", sb.toString(), strSubstring);
            } else if (strSubstring.equals(".")) {
                strSubstring = "";
                printStep("2D", sb.toString(), strSubstring);
            } else if (strSubstring.equals(Constants.ATTRVAL_PARENT)) {
                if (!sb.toString().equals("/")) {
                    sb.append(Constants.ATTRVAL_PARENT);
                }
                strSubstring = "";
                printStep("2D", sb.toString(), strSubstring);
            } else {
                int iIndexOf2 = strSubstring.indexOf(47);
                if (iIndexOf2 == 0) {
                    iIndexOf = strSubstring.indexOf(47, 1);
                } else {
                    iIndexOf = iIndexOf2;
                    iIndexOf2 = 0;
                }
                if (iIndexOf == -1) {
                    strSubstring2 = strSubstring.substring(iIndexOf2);
                    strSubstring3 = "";
                } else {
                    strSubstring2 = strSubstring.substring(iIndexOf2, iIndexOf);
                    strSubstring3 = strSubstring.substring(iIndexOf);
                }
                strSubstring = strSubstring3;
                sb.append(strSubstring2);
                printStep("2E", sb.toString(), strSubstring);
            }
        }
        if (sb.toString().endsWith(Constants.ATTRVAL_PARENT)) {
            sb.append("/");
            printStep("3 ", sb.toString(), strSubstring);
        }
        return sb.toString();
    }

    private static void printStep(String str, String str2, String str3) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" " + str + ":   " + str2);
            if (str2.length() == 0) {
                LOG.debug("\t\t\t\t" + str3);
            } else {
                LOG.debug("\t\t\t" + str3);
            }
        }
    }
}
