package v;

import bH.C;
import bH.C1011s;
import bH.W;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1390ac;
import com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent;
import com.efiAnalytics.apps.ts.dashboard.Z;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import r.C1798a;
import r.C1807j;

/* renamed from: v.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/c.class */
public class C1883c extends C1885e {

    /* renamed from: a, reason: collision with root package name */
    File f14015a;

    /* renamed from: b, reason: collision with root package name */
    File f14016b = C1807j.F();

    /* renamed from: c, reason: collision with root package name */
    private static boolean f14017c = false;

    public C1883c(File file) {
        this.f14015a = file;
    }

    public void a(String str, Z z2) {
        bH.Z z3 = new bH.Z();
        z3.a();
        File file = new File(str);
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        System.currentTimeMillis();
        try {
            Document documentNewDocument = documentBuilderFactoryNewInstance.newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement("dsh");
            elementCreateElement.setAttribute("xmlns", "http://www.EFIAnalytics.com/:dsh");
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("author", C1798a.f13268b + " " + C1798a.f13267a + " - EFI Analytics - support@efianalytics.com");
            elementCreateElement2.setAttribute("company", "EFI Analytics, © 2007 - " + Calendar.getInstance().get(1) + ", All Rights Reserved.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "3.0");
            elementCreateElement3.setAttribute("firmwareSignature", z2.d());
            elementCreateElement.appendChild(elementCreateElement3);
            Element elementCreateElement4 = documentNewDocument.createElement("gaugeCluster");
            elementCreateElement4.setAttribute("clusterBackgroundColor", "" + z2.a().getRGB());
            elementCreateElement4.setAttribute("backgroundDitherColor", "" + (z2.g() != null ? Integer.valueOf(z2.g().getRGB()) : ""));
            if (z2.b() == null || z2.b().equals("")) {
                elementCreateElement4.setAttribute("clusterBackgroundImageFileName", "");
            } else {
                a(documentNewDocument, elementCreateElement4, "mainDashBackgroundImage", new File(z2.b()));
                elementCreateElement4.setAttribute("clusterBackgroundImageFileName", "mainDashBackgroundImage");
            }
            elementCreateElement4.setAttribute("clusterBackgroundImageStyle", "" + z2.e());
            elementCreateElement4.setAttribute("antiAliasing", Boolean.toString(z2.f()));
            elementCreateElement4.setAttribute("forceAspect", Boolean.toString(z2.h()));
            elementCreateElement4.setAttribute("forceAspectWidth", Double.toString(z2.i()));
            elementCreateElement4.setAttribute("forceAspectHeight", Double.toString(z2.j()));
            Component[] componentArrC = z2.c();
            HashMap mapA = C1882b.a(componentArrC);
            for (String str2 : mapA.keySet()) {
                a(documentNewDocument, elementCreateElement4, (String) mapA.get(str2), new File(str2));
            }
            for (int i2 = 0; i2 < componentArrC.length; i2++) {
                if (componentArrC[i2] instanceof AbstractC1420s) {
                    AbstractC1420s abstractC1420s = (AbstractC1420s) componentArrC[i2];
                    synchronized (abstractC1420s) {
                        a(documentNewDocument, elementCreateElement4, abstractC1420s);
                    }
                }
            }
            elementCreateElement.appendChild(elementCreateElement4);
            a(str, documentNewDocument);
            C1882b.a(mapA, componentArrC);
            C.d("Time to load non-cached dashboard: " + z3.d() + " ms. file:" + file.getName());
            a(file, z2);
        } catch (V.a e2) {
            throw e2;
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Error Saving Dashboard. Check Log file for details.");
        }
    }

    private void a(File file, Z z2) {
        if (f14017c) {
            bH.Z z3 = new bH.Z();
            z3.a();
            synchronized (z2) {
                try {
                    String strC = C1011s.c(file);
                    C.c("Time to get file checksum: " + z3.d());
                    InterfaceC1390ac interfaceC1390acMyGaugeContainer = null;
                    for (Component component : z2.c()) {
                        if (component instanceof AbstractC1420s) {
                            AbstractC1420s abstractC1420s = (AbstractC1420s) component;
                            abstractC1420s.invalidatePainter();
                            if (interfaceC1390acMyGaugeContainer == null && abstractC1420s.myGaugeContainer() != null) {
                                interfaceC1390acMyGaugeContainer = abstractC1420s.myGaugeContainer();
                            }
                            abstractC1420s.myGaugeContainer(null);
                        }
                    }
                    z3.a();
                    X.c.a().a(z2, strC, file.getParentFile());
                    for (Component component2 : z2.c()) {
                        if (component2 instanceof AbstractC1420s) {
                            ((AbstractC1420s) component2).myGaugeContainer(interfaceC1390acMyGaugeContainer);
                        }
                    }
                } catch (IOException e2) {
                    Logger.getLogger(C1425x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                C.c("Time to save cache file: " + z3.d() + "ms, file: " + file.getName());
            }
        }
    }

    public void a(String str, ArrayList arrayList) {
        try {
            Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement("gauge");
            elementCreateElement.setAttribute("xmlns", "http://www.EFIAnalytics.com/:gauge");
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("author", C1798a.f13268b + " " + C1798a.f13267a + " - Phil Tobin - p_tobin@yahoo.com");
            elementCreateElement2.setAttribute("company", "EFI Analytics, © 2007 - 2015, All Rights Reserved.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "1.0");
            elementCreateElement.appendChild(elementCreateElement3);
            AbstractC1420s[] abstractC1420sArr = (AbstractC1420s[]) arrayList.toArray(new AbstractC1420s[arrayList.size()]);
            HashMap mapA = C1882b.a(abstractC1420sArr);
            for (String str2 : mapA.keySet()) {
                a(documentNewDocument, elementCreateElement, (String) mapA.get(str2), new File(str2));
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                Element elementCreateElement4 = documentNewDocument.createElement("gauge");
                a(documentNewDocument, elementCreateElement4, abstractC1420s);
                elementCreateElement.appendChild(elementCreateElement4);
            }
            a(str, documentNewDocument);
            C1882b.a(mapA, abstractC1420sArr);
        } catch (V.a e2) {
            throw e2;
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Error Saving Dashboard. Check Log file for details.");
        }
    }

    public Document a(Document document, Element element, AbstractC1420s abstractC1420s) throws V.a {
        try {
            element.appendChild(a(document, document.createElement("dashComp"), (Object) abstractC1420s));
            return document;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error Saving DashboardComponent. Check Log file for details.");
        }
    }

    private Z a(File file) {
        if (!f14017c) {
            return null;
        }
        bH.Z z2 = new bH.Z();
        z2.a();
        try {
            try {
                Z z3 = (Z) X.c.a().a(C1011s.c(file), file.getParentFile());
                for (Component component : z3.c()) {
                    if (component instanceof Gauge) {
                        ((Gauge) component).initializeExpressionMonitors();
                    }
                }
                C.d("Time to check / load cached dash: " + z2.d() + ", file: " + file.getName());
                return z3;
            } catch (Exception e2) {
                C.d("Did not load cached dash: " + e2.getMessage());
                C.d("Time to check / load cached dash: " + z2.d() + ", file: " + file.getName());
                return null;
            }
        } catch (Throwable th) {
            C.d("Time to check / load cached dash: " + z2.d() + ", file: " + file.getName());
            throw th;
        }
    }

    public Z a(String str) {
        File file = new File(str);
        Z zA = a(file);
        if (zA != null) {
            return zA;
        }
        Z z2 = new Z();
        ArrayList arrayList = new ArrayList();
        bH.Z z3 = new bH.Z();
        z3.a();
        try {
            try {
                NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("*");
                HashMap map = new HashMap();
                for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                    Node nodeItem = elementsByTagName.item(i2);
                    if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("gaugeCluster")) {
                        NamedNodeMap attributes = nodeItem.getAttributes();
                        nodeItem.getNodeName();
                        for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                            Node nodeItem2 = attributes.item(i3);
                            if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("clusterBackgroundColor")) {
                                z2.a(e(nodeItem2.getNodeValue()));
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("backgroundDitherColor")) {
                                String nodeValue = nodeItem2.getNodeValue();
                                if (nodeValue != null && !nodeValue.equals("")) {
                                    z2.b(e(nodeValue));
                                }
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("clusterBackgroundImageFileName")) {
                                z2.a(nodeItem2.getNodeValue());
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("clusterBackgroundImageStyle")) {
                                z2.c(nodeItem2.getNodeValue());
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("antiAliasing")) {
                                z2.a(nodeItem2.getNodeValue().equals("true"));
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("forceAspect")) {
                                z2.b(nodeItem2.getNodeValue().equals("true"));
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("forceAspectWidth")) {
                                z2.a(Double.parseDouble(nodeItem2.getNodeValue()));
                            } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("forceAspectHeight")) {
                                z2.b(Double.parseDouble(nodeItem2.getNodeValue()));
                            }
                        }
                    }
                    if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("versionInfo")) {
                        NamedNodeMap attributes2 = nodeItem.getAttributes();
                        nodeItem.getNodeName();
                        for (int i4 = 0; i4 < attributes2.getLength(); i4++) {
                            Node nodeItem3 = attributes2.item(i4);
                            if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("firmwareSignature")) {
                                z2.b(nodeItem3.getNodeValue());
                            }
                            if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("fileFormat")) {
                                try {
                                    double d2 = Double.parseDouble(nodeItem3.getNodeValue());
                                    if (d2 > 3.0d) {
                                        C.a("Unsupported Dash File Version.", new V.a("The format version of Dash file: " + d2 + " is higher than \nthe maximum supported by this version application: 3.0\nWill continue loading, but there may be issues.\nFile name:\n" + str), null);
                                    }
                                } catch (Exception e2) {
                                    C.a(e2);
                                    C.a("Error parsing .dash format version: " + nodeItem3.getNodeValue());
                                }
                            }
                        }
                    }
                    if (nodeItem.getNodeName().equals("imageFile")) {
                        try {
                            C1884d c1884dC = c(nodeItem);
                            map.put(c1884dC.f14018a, c1884dC.f14019b.getAbsolutePath());
                        } catch (IOException e3) {
                            C.a("Failed to load Dashboard Image. ", e3, null);
                        }
                    }
                    if (nodeItem.getNodeName().equals("dashComp")) {
                        try {
                            AbstractC1420s abstractC1420sB = b(nodeItem);
                            if (abstractC1420sB instanceof SingleChannelDashComponent) {
                                SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) abstractC1420sB;
                                singleChannelDashComponent.setOutputChannel(d(singleChannelDashComponent.getOutputChannel()));
                            }
                            arrayList.add(abstractC1420sB);
                        } catch (Exception e4) {
                            C.b("Failed to load Gauge:" + nodeItem.toString());
                        }
                    }
                }
                Component[] componentArr = new Component[arrayList.size()];
                int i5 = 0;
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    int i6 = i5;
                    i5++;
                    componentArr[i6] = (Component) it.next();
                }
                Component[] componentArrB = C1882b.b(map, componentArr);
                if (z2.b() != null && !z2.b().equals("")) {
                    z2.a((String) map.get(z2.b()));
                }
                z2.a(componentArrB);
                C.d("Time to load uncached dash: " + z3.d() + "ms, file: " + file.getName());
                a(file, z2);
                return z2;
            } catch (Exception e5) {
                e5.printStackTrace();
                throw new V.a("Failed to load Gauge Cluster " + e5.getMessage(), e5);
            }
        } catch (Throwable th) {
            C.d("Time to load uncached dash: " + z3.d() + "ms, file: " + file.getName());
            throw th;
        }
    }

    public ArrayList b(String str) {
        BufferedInputStream bufferedInputStream = null;
        try {
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(str)));
                ArrayList arrayListA = a(bufferedInputStream);
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C1883c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return arrayListA;
            } catch (Throwable th) {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e3) {
                        Logger.getLogger(C1883c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        throw th;
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
            throw new V.a("File Not Found:\n" + str);
        }
    }

    public ArrayList a(InputStream inputStream) {
        ArrayList arrayList = new ArrayList();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getElementsByTagName("*");
            HashMap map = new HashMap();
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.getNodeName().equals("imageFile")) {
                    try {
                        C1884d c1884dC = c(nodeItem);
                        map.put(c1884dC.f14018a, c1884dC.f14019b.getAbsolutePath());
                    } catch (IOException e2) {
                        C.a("Failed to load Dashboard Image. ", e2, null);
                    }
                }
                if (nodeItem.getNodeName().equals("dashComp")) {
                    try {
                        AbstractC1420s abstractC1420sB = b(nodeItem);
                        if (abstractC1420sB instanceof SingleChannelDashComponent) {
                            SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) abstractC1420sB;
                            singleChannelDashComponent.setOutputChannel(d(singleChannelDashComponent.getOutputChannel()));
                        }
                        arrayList.add(abstractC1420sB);
                    } catch (Exception e3) {
                        C.b("Failed to load Gauge:" + nodeItem.toString());
                    }
                }
            }
            Component[] componentArr = new Component[arrayList.size()];
            int i3 = 0;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int i4 = i3;
                i3++;
                componentArr[i4] = (Component) it.next();
            }
            C1882b.b(map, componentArr);
            if (arrayList.size() > 0) {
                return arrayList;
            }
            return null;
        } catch (Exception e4) {
            e4.printStackTrace();
            throw new V.a("Failed to load Gauge Cluster " + e4.getMessage(), e4);
        }
    }

    private AbstractC1420s b(Node node) {
        return (AbstractC1420s) a(node);
    }

    private String d(String str) {
        return W.e(str);
    }

    private Color e(String str) {
        try {
            return new Color(Integer.parseInt(str));
        } catch (Exception e2) {
            C.b("Error loading Color, intVal=" + str + ", lightGray returned.");
            return Color.lightGray;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0068 A[Catch: Exception -> 0x008e, TryCatch #0 {Exception -> 0x008e, blocks: (B:4:0x0005, B:6:0x000d, B:7:0x0068), top: B:14:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.w3c.dom.Document a(org.w3c.dom.Document r5, org.w3c.dom.Element r6, java.lang.String r7, java.io.File r8) throws V.a {
        /*
            r4 = this;
            r0 = r8
            if (r0 == 0) goto L68
            r0 = r8
            boolean r0 = r0.exists()     // Catch: java.lang.Exception -> L8e
            if (r0 == 0) goto L68
            r0 = r8
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L8e
            r1 = r8
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> L8e
            java.lang.String r2 = "."
            int r1 = r1.lastIndexOf(r2)     // Catch: java.lang.Exception -> L8e
            r2 = 1
            int r1 = r1 + r2
            java.lang.String r0 = r0.substring(r1)     // Catch: java.lang.Exception -> L8e
            r9 = r0
            r0 = r5
            java.lang.String r1 = "imageFile"
            org.w3c.dom.Element r0 = r0.createElement(r1)     // Catch: java.lang.Exception -> L8e
            r10 = r0
            r0 = r10
            java.lang.String r1 = "type"
            r2 = r9
            r0.setAttribute(r1, r2)     // Catch: java.lang.Exception -> L8e
            r0 = r10
            java.lang.String r1 = "imageId"
            r2 = r7
            r0.setAttribute(r1, r2)     // Catch: java.lang.Exception -> L8e
            r0 = r10
            java.lang.String r1 = "fileName"
            r2 = r8
            java.lang.String r2 = r2.getName()     // Catch: java.lang.Exception -> L8e
            r0.setAttribute(r1, r2)     // Catch: java.lang.Exception -> L8e
            r0 = r10
            r1 = r8
            java.lang.String r1 = bI.d.a(r1)     // Catch: java.lang.Exception -> L8e
            r0.setTextContent(r1)     // Catch: java.lang.Exception -> L8e
            r0 = r6
            r1 = r10
            org.w3c.dom.Node r0 = r0.appendChild(r1)     // Catch: java.lang.Exception -> L8e
            goto L8b
        L68:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L8e
            r1 = r0
            r1.<init>()     // Catch: java.lang.Exception -> L8e
            java.lang.String r1 = "Image not found for Image ID: "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L8e
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L8e
            java.lang.String r1 = ", looking in: "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L8e
            r1 = r8
            java.lang.String r1 = r1.getAbsolutePath()     // Catch: java.lang.Exception -> L8e
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L8e
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L8e
            bH.C.b(r0)     // Catch: java.lang.Exception -> L8e
        L8b:
            goto L9f
        L8e:
            r9 = move-exception
            r0 = r9
            r0.printStackTrace()
            V.a r0 = new V.a
            r1 = r0
            java.lang.String r2 = "Error Saving Dashboard Image. Check Log file for details."
            r1.<init>(r2)
            throw r0
        L9f:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: v.C1883c.a(org.w3c.dom.Document, org.w3c.dom.Element, java.lang.String, java.io.File):org.w3c.dom.Document");
    }

    private C1884d c(Node node) {
        String strA = a(node, "type");
        String strA2 = a(node, "fileName");
        String strA3 = a(node, "imageId");
        File file = f(strA) ? new File(this.f14016b, strA2) : new File(this.f14015a, strA2);
        bI.d.a(node.getTextContent(), file);
        return new C1884d(this, strA3, file);
    }

    private boolean f(String str) {
        return str.toLowerCase().startsWith("ttf");
    }

    public static void a(boolean z2) {
        f14017c = z2;
    }
}
