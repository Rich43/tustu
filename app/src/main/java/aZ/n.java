package aZ;

import G.C0135r;
import G.C0136s;
import W.ai;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import r.C1807j;
import v.C1885e;

/* loaded from: TunerStudioMS.jar:aZ/n.class */
public class n extends C1885e implements ai {
    @Override // W.ai
    public C0136s[] a() {
        return a(C1807j.f13465a);
    }

    public C0136s[] a(String str) throws V.a {
        ArrayList arrayList = new ArrayList();
        try {
            NodeList elementsByTagName = c(str).getElementsByTagName("*");
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.getNodeName().equals("settingGroup")) {
                    try {
                        arrayList.add(b(nodeItem));
                    } catch (Exception e2) {
                        C.b("Failed to load Option Group:" + nodeItem.toString());
                        e2.printStackTrace();
                    }
                }
            }
            C0136s[] c0136sArr = new C0136s[arrayList.size()];
            int i3 = 0;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int i4 = i3;
                i3++;
                c0136sArr[i4] = (C0136s) it.next();
            }
            return c0136sArr;
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Failed to load Gauge Cluster " + e3.getMessage(), e3);
        }
    }

    private C0136s b(Node node) {
        C0136s c0136s = new C0136s();
        NamedNodeMap attributes = node.getAttributes();
        c0136s.b(attributes.getNamedItem("name").getTextContent());
        c0136s.c(attributes.getNamedItem("displayName").getTextContent());
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node nodeItem = childNodes.item(i2);
            if (nodeItem.getNodeName().equals("configurationOption")) {
                C0135r c0135r = new C0135r();
                NamedNodeMap attributes2 = nodeItem.getAttributes();
                c0135r.v(attributes2.getNamedItem("name").getTextContent());
                c0135r.a(attributes2.getNamedItem("displayName").getTextContent());
                Node namedItem = attributes2.getNamedItem("infoLink");
                if (namedItem != null) {
                    c0135r.b(namedItem.getTextContent());
                }
                Node namedItem2 = attributes2.getNamedItem("default");
                if (namedItem2 != null) {
                    c0135r.a(namedItem2.getTextContent() != null && namedItem2.getTextContent().equals("true"));
                }
                c0136s.a(c0135r);
            }
        }
        return c0136s;
    }
}
