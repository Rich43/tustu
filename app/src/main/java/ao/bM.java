package ao;

import W.C0188n;
import g.C1726d;
import g.C1733k;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.security.x509.X509CertImpl;

/* loaded from: TunerStudioMS.jar:ao/bM.class */
public class bM implements hF {

    /* renamed from: j, reason: collision with root package name */
    private static int f5336j = -1;

    /* renamed from: a, reason: collision with root package name */
    Document f5328a = null;

    /* renamed from: b, reason: collision with root package name */
    HashMap f5329b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    HashMap f5330c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    HashMap f5331d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    HashMap f5332e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f5333f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    ArrayList f5334g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    boolean f5335h = false;

    /* renamed from: i, reason: collision with root package name */
    String f5337i = "";

    public void a(String str) {
        this.f5334g.add(str);
    }

    @Override // ao.hF
    public void a() {
        Iterator it = this.f5332e.values().iterator();
        while (it.hasNext()) {
            ((hH) it.next()).q();
        }
    }

    @Override // ao.hF
    public boolean b() {
        Iterator it = this.f5332e.values().iterator();
        while (it.hasNext()) {
            if (((hH) it.next()).r()) {
                return true;
            }
        }
        return false;
    }

    @Override // ao.hF
    public hH b(String str) {
        if (g(str) == null) {
            return null;
        }
        return (hH) this.f5332e.get(str);
    }

    private hH l(String str) {
        bN bNVar = (bN) this.f5331d.get(str);
        if (bNVar == null) {
            return null;
        }
        hH hHVar = new hH();
        hHVar.a(e(str));
        hHVar.b(d(str));
        hHVar.a(hHVar.c().length, hHVar.d().length);
        a(hHVar, (Double[][]) bNVar.a());
        hHVar.f(str);
        hHVar.d(f(str));
        hHVar.a(this.f5333f.contains(str));
        int i2 = 0;
        if (str.startsWith("veBins") || str.startsWith("advTable")) {
            i2 = 0;
        } else if (str.startsWith("advanceTable")) {
            i2 = 1;
        } else if (str.startsWith("pwBTable") || str.startsWith("pwATable")) {
            i2 = 1;
            hHVar.a(100.0d);
            hHVar.b(2);
            if (str.endsWith("2")) {
                hHVar.d("PW In2");
            } else {
                hHVar.d("PW In1");
            }
        } else if (str.startsWith("afrBTable") || str.startsWith("afrATable")) {
            i2 = 1;
            hHVar.b(2);
            if (str.endsWith("2")) {
                hHVar.d("PW In2");
            } else {
                hHVar.d("PW In1");
            }
        } else if (str.startsWith("afr")) {
            i2 = 1;
        } else if (str.startsWith("lambda")) {
            i2 = 2;
        } else if (str.startsWith("veTable") && f()) {
            i2 = 1;
        }
        if (bNVar.f5341d > f5336j) {
            i2 = bNVar.f5341d;
        }
        hHVar.a(i2);
        return hHVar;
    }

    private boolean f() {
        return (this.f5337i == null || this.f5337i.indexOf("MS3") == -1) ? false : true;
    }

    private void a(hH hHVar, Double[][] dArr) {
        for (int i2 = 0; i2 < dArr.length; i2++) {
            for (int i3 = 0; i3 < dArr[0].length; i3++) {
                hHVar.setValueAt(dArr[i2][i3], i2, i3);
            }
        }
    }

    public boolean c(String str) {
        return this.f5333f.contains(str);
    }

    public String[] d(String str) {
        return (String[]) ((bN) this.f5329b.get(h(str))).a();
    }

    public String[] e(String str) {
        return (String[]) ((bN) this.f5330c.get(h(str))).a();
    }

    public String f(String str) {
        return ((bN) this.f5330c.get(h(str))).c();
    }

    public Double[][] g(String str) {
        return this.f5331d.get(str) == null ? (Double[][]) null : (Double[][]) ((bN) this.f5331d.get(str)).a();
    }

    @Override // ao.hF
    public Iterator c() {
        return this.f5331d.keySet().iterator();
    }

    @Override // ao.hF
    public int d() {
        return this.f5331d.size();
    }

    public String h(String str) {
        return (str.startsWith("veTable") || str.startsWith("afrTable") || str.startsWith("alphaMAPtable") || str.startsWith("advanceTable")) ? (this.f5330c.get(str) == null && this.f5329b.get(str) == null) ? str.substring(0, str.length() - 1) : str : str;
    }

    public void i(String str) throws V.h {
        this.f5335h = false;
        p(str);
        try {
            this.f5328a = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(str));
            NodeList elementsByTagName = this.f5328a.getElementsByTagName("*");
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            String strA = h.i.a("yAxisField", "MAP");
            String strA2 = h.i.a("yAxisField", "Load");
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.getNodeName() != null && ((nodeItem.getNodeName().equals(FXMLLoader.FX_CONSTANT_ATTRIBUTE) || nodeItem.getNodeName().equals("versionInfo") || nodeItem.getNodeName().equals("page")) && nodeItem.hasAttributes())) {
                    NamedNodeMap attributes = nodeItem.getAttributes();
                    for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                        Node nodeItem2 = attributes.item(i3);
                        if (nodeItem2.getNodeValue() != null && o(nodeItem2.getNodeValue())) {
                            arrayList.add(nodeItem);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("rpmBins2") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "advTable1");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("mapBins1") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "advTable1", strA2);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("fuelLoad2Bins") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "veTable2", strA2);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("fuelRPM2Bins") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "veTable2");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("fuelLoadBins") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "veTable", strA2);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("rpmBins") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "veTable");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("loadBinsAFR") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "afrTable", strA2);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("rpmBinsAFR") && this.f5337i.startsWith("speeduino")) {
                            a(nodeItem, "afrTable");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("rpmBinsVE")) {
                            a(nodeItem, "veTable");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("mapBinsVE")) {
                            a(nodeItem, "veTable", strA);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("rpmBinsspark")) {
                            a(nodeItem, "sparkTable");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("mapBinsspark")) {
                            a(nodeItem, "sparkTable", strA);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("rpmBinsLambda")) {
                            a(nodeItem, "lambdaTable");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equalsIgnoreCase("mapBinsLambda")) {
                            a(nodeItem, "lambdaTable", strA);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("fpw_table")) {
                            a(nodeItem, "pwBTable1", strA2);
                            a(nodeItem, "pwBTable2", strA2);
                            a(nodeItem, "pwATable1", strA2);
                            a(nodeItem, "pwATable2", strA2);
                            a(nodeItem, "afrBTable1", strA2);
                            a(nodeItem, "afrBTable2", strA2);
                            a(nodeItem, "afrATable1", strA2);
                            a(nodeItem, "afrATable2", strA2);
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("frpm_Btable")) {
                            a(nodeItem, "pwBTable1");
                            a(nodeItem, "pwBTable2");
                            a(nodeItem, "afrBTable1");
                            a(nodeItem, "afrBTable2");
                        } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().equals("frpm_Atable")) {
                            a(nodeItem, "pwATable1");
                            a(nodeItem, "pwATable2");
                            a(nodeItem, "afrATable1");
                            a(nodeItem, "afrATable2");
                        } else if (nodeItem2.getNodeValue() == null || (!nodeItem2.getNodeValue().equals("mapBinsBRel") && !nodeItem2.getNodeValue().equals("rpmBinsSpark_0") && !nodeItem2.getNodeValue().equals("rpmBinsVEmax"))) {
                            if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("rpmBin")) {
                                arrayList3.add(nodeItem);
                            } else if (nodeItem2.getNodeValue() == null || !nodeItem2.getNodeValue().startsWith("frpm_table")) {
                                if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("arpm_table") && (this.f5337i.indexOf("MS2E") > -1 || this.f5337i.indexOf("MS3") > -1)) {
                                    a(nodeItem, "afrTable" + m(nodeItem2.getNodeValue()));
                                } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("srpm_table")) {
                                    a(nodeItem, "advanceTable" + m(nodeItem2.getNodeValue()));
                                } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("amap_rpm")) {
                                    a(nodeItem, "alphaMAPtable" + m(nodeItem2.getNodeValue()));
                                } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("mapBin")) {
                                    arrayList2.add(new bO(this, nodeItem, strA));
                                } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("afmBin")) {
                                    arrayList2.add(new bO(this, nodeItem, "MAF Volts"));
                                } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("tpsBin")) {
                                    arrayList2.add(new bO(this, nodeItem, strA));
                                } else if (nodeItem2.getNodeValue() == null || !nodeItem2.getNodeValue().startsWith("fmap_table")) {
                                    if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("amap_table") && (this.f5337i.indexOf("MS2E") > -1 || this.f5337i.indexOf("MS3") > -1)) {
                                        a(nodeItem, "afrTable" + m(nodeItem2.getNodeValue()), strA2);
                                    } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("smap_table")) {
                                        a(nodeItem, "advanceTable" + m(nodeItem2.getNodeValue()), strA2);
                                    } else if (nodeItem2.getNodeValue() != null && nodeItem2.getNodeValue().startsWith("amap_tps")) {
                                        a(nodeItem, "alphaMAPtable" + m(nodeItem2.getNodeValue()), strA);
                                    } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("number")) {
                                        nodeItem2.getNodeValue();
                                    } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals(X509CertImpl.SIGNATURE)) {
                                        this.f5337i = nodeItem2.getNodeValue();
                                    }
                                } else if (nodeItem2.getNodeValue().endsWith("doz")) {
                                    a(nodeItem, "veTable" + m(nodeItem2.getNodeValue()) + "dozen", strA2);
                                } else {
                                    a(nodeItem, "veTable" + m(nodeItem2.getNodeValue()), strA2);
                                    a(nodeItem, "xTauTable", "MAP");
                                    if (this.f5337i.indexOf("MS2E") == -1 && this.f5337i.indexOf("MS3") == -1) {
                                        a(nodeItem, "afrTable" + m(nodeItem2.getNodeValue()), strA2);
                                    }
                                }
                            } else if (nodeItem2.getNodeValue().endsWith("doz")) {
                                a(nodeItem, "veTable" + m(nodeItem2.getNodeValue()) + "dozen");
                            } else {
                                a(nodeItem, "veTable" + m(nodeItem2.getNodeValue()));
                                a(nodeItem, "xTauTable");
                                if (this.f5337i.indexOf("MS2E") == -1 && this.f5337i.indexOf("MS3") == -1) {
                                    a(nodeItem, "afrTable" + m(nodeItem2.getNodeValue()));
                                }
                            }
                        }
                    }
                }
            }
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                String strA3 = a((Node) arrayList.get(i4));
                b((Node) arrayList.get(i4), strA3);
                if (i4 < arrayList3.size() && arrayList3.get(i4) != null && !this.f5329b.containsKey(strA3)) {
                    a((Node) arrayList3.get(i4), strA3);
                }
                if (i4 < arrayList2.size() && arrayList2.get(i4) != null && !this.f5330c.containsKey(strA3)) {
                    bO bOVar = (bO) arrayList2.get(i4);
                    a(bOVar.f5344a, strA3, bOVar.f5345b);
                }
            }
            if (this.f5335h) {
                q(str);
            }
            ArrayList arrayList4 = new ArrayList();
            Iterator itC = c();
            while (itC.hasNext()) {
                String str2 = (String) itC.next();
                try {
                    this.f5332e.put(str2, l(str2));
                } catch (Exception e2) {
                    arrayList4.add(str2);
                    System.out.println("unable to build table:" + str2);
                }
            }
            Iterator it = arrayList4.iterator();
            while (it.hasNext()) {
                this.f5331d.remove(it.next());
            }
            a();
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.h("Error Opening MSQ File. \nFile appears to be corrupt.");
        }
    }

    private String m(String str) {
        try {
            int i2 = 1;
            String str2 = "" + str.charAt(str.length() - 1);
            while (!n(str2)) {
                i2++;
                if (str.length() - i2 < 0) {
                    return "";
                }
                str2 = "" + str.charAt(str.length() - i2);
            }
            return str2;
        } catch (Exception e2) {
            return "";
        }
    }

    private boolean n(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private String a(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node nodeItem = attributes.item(i2);
            if (nodeItem.getNodeValue() != null && o(nodeItem.getNodeValue())) {
                return nodeItem.getNodeValue();
            }
        }
        return "";
    }

    private boolean o(String str) {
        return this.f5334g.contains(str);
    }

    @Override // ao.hF
    public void j(String str) throws V.h, DOMException {
        h();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            DOMSource dOMSource = new DOMSource(this.f5328a);
            StreamResult streamResult = new StreamResult(fileOutputStream);
            fileOutputStream.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n".getBytes());
            Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
            transformerNewTransformer.setOutputProperty("omit-xml-declaration", "yes");
            transformerNewTransformer.setOutputProperty("encoding", FTP.DEFAULT_CONTROL_ENCODING);
            transformerNewTransformer.transform(dOMSource, streamResult);
            fileOutputStream.close();
            if (this.f5335h) {
                q(str);
            }
            a();
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.h("Error Saving MSQ. Check Log file for details.");
        }
    }

    private void a(Node node, String str) throws DOMException, NumberFormatException {
        String nodeValue = "";
        int i2 = f5336j;
        String[] strArr = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int i3 = 0; i3 < attributes.getLength(); i3++) {
            Node nodeItem = attributes.item(i3);
            if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("name")) {
                nodeValue = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("rows")) {
                strArr = new String[Integer.parseInt(nodeItem.getNodeValue())];
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("digits")) {
                i2 = Integer.parseInt(nodeItem.getNodeValue());
            }
        }
        StringTokenizer stringTokenizer = new StringTokenizer(b(node).getNodeValue(), "\n");
        for (int i4 = 0; i4 < strArr.length; i4++) {
            strArr[i4] = stringTokenizer.nextToken().trim();
        }
        this.f5329b.put(str, new bN(this, nodeValue, str, "RPM", strArr, i2));
    }

    private void a(Node node, String str, String str2) throws DOMException, NumberFormatException {
        String nodeValue = "";
        int i2 = f5336j;
        String[] strArr = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int i3 = 0; i3 < attributes.getLength(); i3++) {
            Node nodeItem = attributes.item(i3);
            if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("name")) {
                nodeValue = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("units")) {
                nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("rows")) {
                strArr = new String[Integer.parseInt(nodeItem.getNodeValue())];
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("digits")) {
                i2 = Integer.parseInt(nodeItem.getNodeValue());
            }
        }
        StringTokenizer stringTokenizer = new StringTokenizer(b(node).getNodeValue(), "\n");
        for (int length = strArr.length - 1; length >= 0; length--) {
            strArr[length] = stringTokenizer.nextToken().trim();
        }
        this.f5330c.put(str, new bN(this, nodeValue, str, str2, strArr, i2));
    }

    private void b(Node node, String str) throws DOMException, NumberFormatException {
        int i2 = 0;
        int i3 = 0;
        int i4 = f5336j;
        boolean zEquals = false;
        boolean zEquals2 = false;
        String nodeValue = "";
        String nodeValue2 = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int i5 = 0; i5 < attributes.getLength(); i5++) {
            Node nodeItem = attributes.item(i5);
            if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("name")) {
                nodeValue = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("rows")) {
                i2 = Integer.parseInt(nodeItem.getNodeValue());
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("cols")) {
                i3 = Integer.parseInt(nodeItem.getNodeValue());
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("units")) {
                nodeValue2 = nodeItem.getNodeValue();
                zEquals = nodeItem.getNodeValue().equals("Volts");
                zEquals2 = nodeItem.getNodeValue().equals("Lambda");
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("digits")) {
                i4 = Integer.parseInt(nodeItem.getNodeValue());
            }
        }
        Double[][] dArr = new Double[i2][i3];
        Node nodeB = b(node);
        String nodeValue3 = nodeB != null ? nodeB.getNodeValue() : "";
        String strE = "";
        if (zEquals) {
            strE = h.i.e("APPEND_FIELD_AFR(WBO2)", "");
        } else if (!zEquals2) {
            this.f5333f.add(nodeValue);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(nodeValue3, "\n");
        for (int length = dArr.length - 1; length >= 0; length--) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(C1733k.a(stringTokenizer.nextToken().trim(), " ", CallSiteDescriptor.OPERATOR_DELIMITER), CallSiteDescriptor.OPERATOR_DELIMITER);
            for (int i6 = 0; i6 < dArr[length].length; i6++) {
                String strNextToken = stringTokenizer2.nextToken();
                if (zEquals && !strE.equals("")) {
                    String strA = C1733k.a(strE, "[Field.O2Volts]", strNextToken);
                    try {
                        if (strA.indexOf(".inc") != -1) {
                            strA = C1726d.a((C0188n) null, C1733k.a(strE, "Field.O2Volts", strNextToken), 0);
                        }
                        strNextToken = C1733k.a(bH.F.g(strA));
                    } catch (V.h e2) {
                        System.out.println("Error converting afrVolts, using formula:" + strA);
                    } catch (IOException e3) {
                        System.out.println("Error loading Mapping File");
                    } catch (Exception e4) {
                        System.out.println("Error converting afrVolts, using formula:" + strA);
                    }
                } else if (zEquals2) {
                }
                dArr[length][i6] = Double.valueOf(strNextToken);
            }
        }
        if (nodeValue.startsWith("veBins")) {
            i4 = 0;
        }
        this.f5331d.put(nodeValue, new bN(this, nodeValue, str, nodeValue2, dArr, i4));
    }

    private Node b(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
            Node nodeItem = childNodes.item(i2);
            if (nodeItem.getNodeType() == 3) {
                return nodeItem;
            }
        }
        return null;
    }

    private void a(Document document, bN bNVar, hH hHVar) throws DOMException {
        Double[][] dArr = (Double[][]) bNVar.f5342e;
        for (int i2 = 0; i2 < hHVar.getRowCount(); i2++) {
            for (int i3 = 0; i3 < hHVar.getColumnCount(); i3++) {
                dArr[i2][i3] = hHVar.getValueAt(i2, i3);
            }
        }
        String strB = bNVar.b();
        bN bNVar2 = (bN) this.f5329b.get(strB);
        bN bNVar3 = (bN) this.f5330c.get(strB);
        NodeList elementsByTagName = document.getElementsByTagName("*");
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (int i4 = 0; i4 < elementsByTagName.getLength(); i4++) {
            Node nodeItem = elementsByTagName.item(i4);
            String nodeName = nodeItem.getNodeName();
            if (nodeName != null && FXMLLoader.FX_CONSTANT_ATTRIBUTE.equals(nodeName) && nodeItem.hasAttributes()) {
                NamedNodeMap attributes = nodeItem.getAttributes();
                for (int i5 = 0; i5 < attributes.getLength(); i5++) {
                    Node nodeItem2 = attributes.item(i5);
                    String nodeName2 = nodeItem2.getNodeName();
                    String nodeValue = nodeItem2.getNodeValue();
                    if ("name".equals(nodeName2) && nodeValue != null) {
                        if (nodeValue.equals(strB)) {
                            a(nodeItem, (Double[][]) bNVar.a(), bNVar.f5341d == 0 ? hHVar.g() : bNVar.f5341d);
                            z4 = true;
                        } else if (bNVar2 == null || bNVar3 == null) {
                            if (!z2 || !z3) {
                                bH.C.b("Unable to update X&Y Bins for table: " + strB + ", they don't appear to have been loaded.");
                            }
                            z2 = true;
                            z3 = true;
                        } else if (nodeValue.equals(bNVar2.f5339b)) {
                            a(nodeItem, a(hHVar.d(), true), bNVar2.f5341d);
                            z2 = true;
                        } else if (nodeValue.equals(bNVar3.f5339b)) {
                            a(nodeItem, a(hHVar.c(), false), bNVar3.f5341d);
                            z3 = true;
                        }
                    }
                }
                if (z2 && z3 && z4) {
                    return;
                }
            }
        }
    }

    private Double[][] a(String[] strArr, boolean z2) {
        Double[][] dArr = new Double[strArr.length][1];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            dArr[z2 ? (strArr.length - 1) - i2 : i2][0] = Double.valueOf(Double.parseDouble(strArr[i2]));
        }
        return dArr;
    }

    private void a(Node node, Double[][] dArr, int i2) throws DOMException {
        String str = "\n";
        for (int length = dArr.length - 1; length >= 0; length--) {
            String str2 = str + "         ";
            for (int i3 = 0; i3 < dArr[length].length; i3++) {
                String strSubstring = ((Object) dArr[length][i3]) + "";
                if (i2 >= 0) {
                    strSubstring = bH.W.b(dArr[length][i3].doubleValue(), i2);
                } else if (strSubstring.indexOf(".000") != -1) {
                    strSubstring = strSubstring.substring(0, strSubstring.indexOf(".0"));
                }
                str2 = str2 + strSubstring + " ";
            }
            str = str2 + "\n";
        }
        b(node).setNodeValue(str + GoToActionDialog.EMPTY_DESTINATION);
    }

    private void g() {
        for (hH hHVar : this.f5332e.values()) {
            if (hHVar.z().endsWith("dozen") && this.f5332e.get(C1733k.a(hHVar.z(), "dozen", "")) != null) {
                hH hHVar2 = (hH) this.f5332e.get(C1733k.a(hHVar.z(), "dozen", ""));
                if (hHVar2.r()) {
                    if (hHVar2.z().endsWith("1")) {
                        for (int i2 = 0; i2 < 144; i2++) {
                            hHVar.setValueAt(hHVar2.getValueAt(15 - (i2 / 16), i2 % 16), 11 - (i2 / 12), i2 % 12);
                        }
                        hH hHVar3 = (hH) this.f5332e.get("veTable2dozen");
                        if (hHVar3 != null) {
                            for (int i3 = 144; i3 < 256; i3++) {
                                int i4 = i3 - 144;
                                hHVar3.setValueAt(hHVar2.getValueAt(15 - (i3 / 16), i3 % 16), 11 - (i4 / 12), i4 % 12);
                            }
                        }
                    }
                    if (hHVar2.z().endsWith("2")) {
                        for (int i5 = 0; i5 < 32; i5++) {
                            int i6 = 112 + i5;
                            hHVar.setValueAt(hHVar2.getValueAt(15 - (i5 / 16), i5 % 16), 11 - (i6 / 12), i6 % 12);
                        }
                        hH hHVar4 = (hH) this.f5332e.get("veTable3dozen");
                        if (hHVar4 != null) {
                            for (int i7 = 32; i7 < 176; i7++) {
                                int i8 = i7 - 32;
                                hHVar4.setValueAt(hHVar2.getValueAt(15 - (i7 / 16), i7 % 16), 11 - (i8 / 12), i8 % 12);
                            }
                        }
                    }
                } else if (hHVar.r()) {
                    for (int i9 = 0; i9 < 144; i9++) {
                        hHVar2.setValueAt(hHVar.getValueAt(11 - (i9 / 12), i9 % 12), 15 - (i9 / 16), i9 % 16);
                    }
                }
            }
        }
    }

    private void h() throws DOMException {
        g();
        for (Map.Entry entry : this.f5331d.entrySet()) {
            String str = (String) entry.getKey();
            if (c(str)) {
                bN bNVar = (bN) entry.getValue();
                String strC = bNVar.c();
                hH hHVar = (hH) this.f5332e.get(str);
                if (hHVar == null || ((strC != null && strC.equals("Volts")) || !(bNVar.a() instanceof Double[][]))) {
                    System.out.println("Can not update table: " + str + ", invalid data");
                } else {
                    a(this.f5328a, bNVar, hHVar);
                }
            }
        }
    }

    @Override // ao.hF
    public void e() {
    }

    private void p(String str) {
        File file = new File(str);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] bArr = new byte[(int) file.length()];
            bufferedInputStream.read(bArr);
            for (int i2 = 0; i2 < bArr.length; i2++) {
                if (bArr[i2] == 20) {
                    bArr[i2] = 32;
                    this.f5335h = true;
                }
            }
            bufferedInputStream.close();
            if (this.f5335h) {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
                for (byte b2 : bArr) {
                    fileOutputStream.write(b2);
                }
                fileOutputStream.close();
            }
        } catch (Exception e2) {
            System.out.println("Error cleaning msq");
        }
    }

    private void q(String str) throws V.h {
        try {
            C1733k.b(str, "signature=\" \"", "signature=\"\u0014\"");
        } catch (V.h e2) {
            System.out.println("Error writing dirty msq");
            throw e2;
        }
    }

    @Override // ao.hF
    public boolean k(String str) {
        return (this.f5329b.get(str) == null || this.f5330c.get(str) == null) ? false : true;
    }
}
