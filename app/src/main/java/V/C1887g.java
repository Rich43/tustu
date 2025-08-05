package v;

import W.C0178d;
import W.C0193s;
import bH.C;
import com.efiAnalytics.apps.ts.tuningViews.F;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import r.C1798a;

/* renamed from: v.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/g.class */
public class C1887g extends C1885e {

    /* renamed from: a, reason: collision with root package name */
    double f14022a = 1.0d;

    public void a(F f2, File file) throws V.a {
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        System.currentTimeMillis();
        try {
            Document documentNewDocument = documentBuilderFactoryNewInstance.newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement("tuneView");
            elementCreateElement.setAttribute("xmlns", "http://www.EFIAnalytics.com/:tuneView");
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("viewName", f2.b());
            elementCreateElement2.setAttribute("author", C1798a.f13268b + " " + C1798a.f13267a + " - EFI Analytics, Inc");
            elementCreateElement2.setAttribute("company", "EFI Analytics, © 2007 - 2018, All Rights Reserved.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "" + this.f14022a);
            elementCreateElement3.setAttribute("firmwareSignature", f2.a());
            elementCreateElement3.setAttribute("enabledCondition", f2.e());
            elementCreateElement.appendChild(elementCreateElement3);
            String strC = f2.c();
            if (strC != null && !strC.isEmpty()) {
                Node nodeCreateElement = documentNewDocument.createElement("previewImage");
                nodeCreateElement.setTextContent(strC);
                elementCreateElement.appendChild(nodeCreateElement);
            }
            Element elementCreateElement4 = documentNewDocument.createElement("tuningView");
            Iterator it = f2.iterator();
            while (it.hasNext()) {
                a(documentNewDocument, elementCreateElement4, (TuneViewComponent) it.next());
            }
            elementCreateElement.appendChild(elementCreateElement4);
            a(file, documentNewDocument);
        } catch (V.a e2) {
            throw e2;
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Error Saving TuningView. Check Log file for details.");
        }
    }

    private Document a(Document document, Element element, TuneViewComponent tuneViewComponent) throws V.a {
        try {
            element.appendChild(a(document, document.createElement("tuneComp"), (Object) tuneViewComponent));
            return document;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error Saving Component to root node. Check Log file for details.");
        }
    }

    public F a(File file) throws V.a {
        try {
            return a(C0193s.a(file, "SiJ6!EK&JC%@"));
        } catch (Exception e2) {
            Logger.getLogger(C1887g.class.getName()).log(Level.SEVERE, "Failed to load TuningView", (Throwable) e2);
            throw new V.a("Failed to load TuneView " + e2.getMessage(), e2);
        }
    }

    public F a(Reader reader) {
        String textContent;
        F f2 = new F();
        try {
            try {
                InputSource inputSource = new InputSource();
                inputSource.setCharacterStream(reader);
                NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource).getElementsByTagName("*");
                for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                    Node nodeItem = elementsByTagName.item(i2);
                    if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("bibliography")) {
                        NamedNodeMap attributes = nodeItem.getAttributes();
                        for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                            Node nodeItem2 = attributes.item(i3);
                            if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("viewName")) {
                                f2.b(nodeItem2.getNodeValue());
                            }
                        }
                    }
                    if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("versionInfo")) {
                        NamedNodeMap attributes2 = nodeItem.getAttributes();
                        nodeItem.getNodeName();
                        for (int i4 = 0; i4 < attributes2.getLength(); i4++) {
                            Node nodeItem3 = attributes2.item(i4);
                            if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("firmwareSignature")) {
                                f2.a(nodeItem3.getNodeValue());
                            } else if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("enabledCondition")) {
                                f2.d(nodeItem3.getNodeValue());
                            }
                            if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("fileFormat")) {
                                try {
                                    double d2 = Double.parseDouble(nodeItem3.getNodeValue());
                                    if (d2 > this.f14022a) {
                                        C.a("Unsupported TuneView File Version.", new V.a("The format version of Tuning View: " + d2 + " is higher than \nthe maximum supported by this version application: " + this.f14022a + "\nWill continue loading, but there may be issues."), null);
                                    }
                                } catch (Exception e2) {
                                    C.a(e2);
                                    C.a("Error parsing .tuneView format version: " + nodeItem3.getNodeValue());
                                }
                            }
                        }
                    }
                    if (nodeItem.getNodeName().equals("previewImage") && (textContent = nodeItem.getTextContent()) != null && !textContent.isEmpty()) {
                        f2.c(textContent);
                    }
                    if (nodeItem.getNodeName().equals("tuneComp")) {
                        try {
                            f2.add(b(nodeItem));
                        } catch (Exception e3) {
                            C.b("Failed to load TuneViewComponent:" + nodeItem.toString());
                        }
                    }
                }
                return f2;
            } finally {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
        } catch (Exception e5) {
            e5.printStackTrace();
            throw new V.a("Failed to load TuneView " + e5.getMessage(), e5);
        }
    }

    private TuneViewComponent b(Node node) {
        return (TuneViewComponent) a(node);
    }

    public static String a(F f2) {
        C1887g c1887g = new C1887g();
        File fileCreateTempFile = File.createTempFile("tsTuningView", "tuneView");
        c1887g.a(f2, fileCreateTempFile);
        return new String(C0178d.a(fileCreateTempFile), "UTF-8");
    }
}
