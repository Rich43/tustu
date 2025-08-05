package W;

import G.C0135r;
import G.aL;
import G.aM;
import G.bL;
import G.bS;
import G.bZ;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import sun.security.x509.X509CertImpl;

/* renamed from: W.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/aa.class */
public class C0171aa {

    /* renamed from: b, reason: collision with root package name */
    public static String f2042b = "http://www.msefi.com/:msq";

    /* renamed from: c, reason: collision with root package name */
    public static String f2043c = "msq";

    /* renamed from: d, reason: collision with root package name */
    public static boolean f2044d = false;

    /* renamed from: a, reason: collision with root package name */
    boolean f2041a = false;

    /* renamed from: e, reason: collision with root package name */
    private List f2045e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private boolean f2046f = false;

    public static boolean a(String str) {
        BufferedReader bufferedReaderA = null;
        try {
            try {
                bufferedReaderA = C0193s.a(new File(str), "SiJ6!EK&JC%@");
                for (int i2 = 0; i2 < 4; i2++) {
                    String line = bufferedReaderA.readLine();
                    if (line == null) {
                        if (bufferedReaderA != null) {
                            try {
                                bufferedReaderA.close();
                            } catch (Exception e2) {
                            }
                        }
                        return false;
                    }
                    if (line.contains(f2043c)) {
                        if (bufferedReaderA != null) {
                            try {
                                bufferedReaderA.close();
                            } catch (Exception e3) {
                            }
                        }
                        return true;
                    }
                }
                if (bufferedReaderA == null) {
                    return false;
                }
                try {
                    bufferedReaderA.close();
                    return false;
                } catch (Exception e4) {
                    return false;
                }
            } catch (FileNotFoundException e5) {
                Logger.getLogger(C0171aa.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                if (bufferedReaderA == null) {
                    return false;
                }
                try {
                    bufferedReaderA.close();
                    return false;
                } catch (Exception e6) {
                    return false;
                }
            } catch (IOException e7) {
                Logger.getLogger(C0171aa.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                if (bufferedReaderA == null) {
                    return false;
                }
                try {
                    bufferedReaderA.close();
                    return false;
                } catch (Exception e8) {
                    return false;
                }
            }
        } catch (Throwable th) {
            if (bufferedReaderA != null) {
                try {
                    bufferedReaderA.close();
                } catch (Exception e9) {
                }
            }
            throw th;
        }
    }

    public bS a(File file) throws aj, V.g {
        bS bSVar = new bS();
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(C0193s.a(file, "SiJ6!EK&JC%@"));
            NodeList elementsByTagName = documentBuilderNewDocumentBuilder.parse(inputSource).getElementsByTagName("*");
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("versionInfo")) {
                    NamedNodeMap attributes = nodeItem.getAttributes();
                    nodeItem.getNodeName();
                    for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                        Node nodeItem2 = attributes.item(i3);
                        if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals(X509CertImpl.SIGNATURE)) {
                            bSVar.a(nodeItem2.getNodeValue().getBytes());
                            return bSVar;
                        }
                        if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("firmwareInfo")) {
                            bSVar.b(URLDecoder.decode(nodeItem2.getNodeValue(), "UTF-8"));
                        }
                    }
                }
            }
            throw new V.g("Unable to get signature in file:\n" + file.getAbsolutePath());
        } catch (aj e2) {
            throw e2;
        } catch (Exception e3) {
            throw new V.g("Unable to get signature in file:\n" + file.getAbsolutePath());
        }
    }

    public void a(G.R r2, String str) throws aj, V.g {
        a(r2, str, (List) null);
    }

    public void a(G.R r2, String str, List list) throws aj, V.g {
        File file = new File(str);
        String strSubstring = str.indexOf(".") != -1 ? str.substring(str.lastIndexOf(".")) : "msq";
        aL.a(r2);
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(C0193s.a(file, "SiJ6!EK&JC%@"));
            Document document = documentBuilderNewDocumentBuilder.parse(inputSource);
            NodeList elementsByTagName = document.getElementsByTagName("*");
            if (list == null) {
                r2.D();
            }
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("pcVariable")) {
                    a(r2, nodeItem, list, strSubstring, false);
                }
            }
            NodeList elementsByTagName2 = document.getElementsByTagName("*");
            for (int i3 = 0; i3 < elementsByTagName2.getLength(); i3++) {
                Node nodeItem2 = elementsByTagName2.item(i3);
                if (nodeItem2.hasAttributes() && nodeItem2.getNodeName().equals("bibliography")) {
                    NamedNodeMap attributes = nodeItem2.getAttributes();
                    nodeItem2.getNodeName();
                    for (int i4 = 0; i4 < attributes.getLength(); i4++) {
                        Node nodeItem3 = attributes.item(i4);
                        if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("tuneComment")) {
                            r2.v(nodeItem3.getNodeValue());
                        }
                    }
                } else if (nodeItem2.hasAttributes() && nodeItem2.getNodeName().equals("versionInfo")) {
                    NamedNodeMap attributes2 = nodeItem2.getAttributes();
                    nodeItem2.getNodeName();
                    for (int i5 = 0; i5 < attributes2.getLength(); i5++) {
                        Node nodeItem4 = attributes2.item(i5);
                        if (nodeItem4.getNodeName() != null && nodeItem4.getNodeName().equals("firmwareInfo")) {
                            r2.A(URLDecoder.decode(nodeItem4.getNodeValue(), "UTF-8"));
                        }
                    }
                } else if (nodeItem2.hasAttributes() && nodeItem2.getNodeName().equals(FXMLLoader.FX_CONSTANT_ATTRIBUTE)) {
                    a(r2, nodeItem2, list, strSubstring, true);
                } else if (nodeItem2.hasAttributes() && nodeItem2.getNodeName().equals("userComment")) {
                    try {
                        NamedNodeMap attributes3 = nodeItem2.getAttributes();
                        String nodeValue = attributes3.getNamedItem("name").getNodeValue();
                        String nodeValue2 = attributes3.getNamedItem("value").getNodeValue();
                        if (nodeValue != null && nodeValue2 != null && (list == null || list.contains(nodeValue))) {
                            r2.b(nodeValue, nodeValue2);
                        }
                    } catch (Exception e2) {
                        b("Could not get comment from node:" + nodeItem2.toString());
                    }
                }
            }
        } catch (aj e3) {
            throw e3;
        } catch (ArrayIndexOutOfBoundsException e4) {
            e4.printStackTrace();
            throw new V.g("Error loading " + strSubstring + "\nA constant is defined to out of range memory addresses.\nCheck your offset and page sizes." + e4.getMessage(), e4);
        } catch (SAXParseException e5) {
            e5.printStackTrace();
            throw new V.g("Problem Parsing tune settings file (" + strSubstring + "):\n" + e5.getMessage(), e5);
        } catch (Exception e6) {
            e6.printStackTrace();
            throw new V.g("Error loading " + strSubstring + ": " + e6.getMessage(), e6);
        }
    }

    private void a(G.R r2, Node node, List list, String str, boolean z2) throws DOMException, NumberFormatException {
        aM aMVarC;
        NamedNodeMap attributes = node.getAttributes();
        String nodeName = node.getNodeName();
        String strC = null;
        String nodeValue = null;
        String nodeValue2 = null;
        String nodeValue3 = null;
        boolean zR = r2.R();
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node nodeItem = attributes.item(i2);
            if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("name")) {
                nodeName = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("rows")) {
                nodeValue = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("cols")) {
                nodeValue2 = nodeItem.getNodeValue();
            } else if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals("units")) {
                nodeValue3 = nodeItem.getNodeValue();
            }
        }
        if (node.getNodeName().equalsIgnoreCase("page")) {
            return;
        }
        if (list == null || list.contains(nodeName)) {
            NodeList childNodes = node.getChildNodes();
            for (int i3 = 0; i3 < childNodes.getLength(); i3++) {
                Node nodeItem2 = childNodes.item(i3);
                if (nodeItem2.getNodeType() == 3 && nodeItem2.getNodeValue() != null) {
                    strC = nodeItem2.getNodeValue();
                }
            }
            if (strC != null && nodeValue3 != null && (aMVarC = r2.c(nodeName)) != null && !aMVarC.G() && !a(aMVarC.o(), nodeValue3)) {
                if ((aMVarC.o().equals(PdfOps.F_TOKEN) || aMVarC.o().equals("°F")) && (nodeValue3.equals("C") || nodeValue3.equals("°C"))) {
                    strC = c(strC);
                    bH.C.d("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were converted to " + aMVarC.o() + ".");
                } else if ((nodeValue3.equals(PdfOps.F_TOKEN) || nodeValue3.equals("°F")) && (aMVarC.o().equals("C") || aMVarC.o().equals("°C"))) {
                    strC = d(strC);
                    bH.C.d("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were converted to " + aMVarC.o() + ".");
                } else if (nodeValue3.equalsIgnoreCase("AFR") && aMVarC.o().equalsIgnoreCase("Lambda")) {
                    strC = e(strC);
                    b("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were converted to Lambda based on Gasoline 14.7:1.");
                } else if (nodeValue3.equalsIgnoreCase("Lambda") && aMVarC.o().equalsIgnoreCase("AFR")) {
                    strC = f(strC);
                    b("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were converted to Afr based on Gasoline 14.7:1.");
                } else if (nodeValue3.equalsIgnoreCase(aMVarC.o()) || nodeValue3.equals("INVALID")) {
                    bH.C.d("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were not converted to new units.");
                } else {
                    b("MSQ Units Mismatch for " + nodeName + "! " + aMVarC.o() + " found in current configuration, " + nodeValue3 + " found in MSQ, values were not converted to new units.");
                }
            }
            aM aMVarC2 = r2.c(nodeName);
            if (strC != null && strC.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN) != -1) {
                try {
                    if (aMVarC2.B() && (!aMVarC2.M() || !aMVarC2.n(r2.h()) || !zR)) {
                        r2.a(nodeName, strC);
                        this.f2045e.add(aMVarC2);
                    }
                    return;
                } catch (V.g e2) {
                    b("Parameter in " + str + ", but not valid for current firmware: " + e2.getMessage());
                    return;
                } catch (Exception e3) {
                    if (!z2 || aMVarC2 == null) {
                        return;
                    }
                    b("Value in " + str + " '" + strC + "' for parameter: " + nodeName + " is of a type that is not compatible with the current firmware.This will be ignored, please correct manually.");
                    return;
                }
            }
            if (aMVarC2 != null && aMVarC2.i().equals("string")) {
                try {
                    r2.a(nodeName, strC);
                    this.f2045e.add(aMVarC2);
                    return;
                } catch (V.g e4) {
                    b("Failed to set value for " + nodeName + " " + str + " value is not valid for current configuration: " + e4.getMessage());
                    return;
                }
            }
            if (strC == null) {
                bH.C.c("An " + str + " value was null for " + nodeName + "??? This shouldn't happen.");
                return;
            }
            double[][] dArrB = new double[nodeValue == null ? aMVarC2 != null ? aMVarC2.a() : 1 : Integer.parseInt(nodeValue)][nodeValue2 == null ? aMVarC2 != null ? aMVarC2.m() : 1 : Integer.parseInt(nodeValue2)];
            if (aMVarC2 != null) {
                try {
                    if (aMVarC2.B() && !aMVarC2.i().equals("string")) {
                        try {
                            dArrB = bH.W.a(dArrB, strC);
                        } catch (Exception e5) {
                            if (!nodeName.startsWith("UNALLOCATED_SPACE")) {
                                bH.C.a("Invalid table data in Constant: " + nodeName + ", not loading values.");
                            }
                        }
                        G.A aC2 = aMVarC2.c();
                        boolean z3 = false;
                        if (aMVarC2.i().equals(ControllerParameter.PARAM_CLASS_ARRAY) && dArrB.length > 0 && aC2.b() > 0 && (dArrB.length != aC2.b() || dArrB[0].length != aC2.a())) {
                            if (aMVarC2.O()) {
                                z3 = true;
                                bH.C.d("Unallocated memory changed, skip loading filler Constants.");
                            } else if (aMVarC2.C()) {
                                b(nodeName + " array size does not match the currently loaded configuration,\n\trescaled " + str + " values to match configuration.");
                                dArrB = bH.H.a(dArrB, aC2.b(), aC2.a());
                            } else {
                                b(nodeName + " array size does not match the currently loaded configuration,\n\tnoSizeMutation set for parameter values transfered to new array size where possible.");
                                dArrB = bH.H.b(dArrB, aC2.b(), aC2.a());
                            }
                        }
                        if (!z3 && (!aMVarC2.M() || !aMVarC2.n(r2.h()) || !zR)) {
                            r2.a(nodeName, dArrB);
                            this.f2045e.add(aMVarC2);
                        }
                    }
                } catch (V.g e6) {
                    b("Failed to set value for " + nodeName + " " + str + " value is not valid for current configuration: " + e6.getMessage());
                } catch (V.j e7) {
                    if (g(nodeName)) {
                        return;
                    }
                    boolean z4 = false;
                    boolean z5 = false;
                    for (int i4 = 0; i4 < dArrB.length; i4++) {
                        try {
                            for (int i5 = 0; i5 < dArrB[0].length; i5++) {
                                if (dArrB[i4][i5] > aMVarC2.r()) {
                                    if (!z4) {
                                        if (bL.i(r2, nodeName)) {
                                            b(nodeName + " row:" + i4 + ", col:" + i5 + ", One or more value higher than maximum: " + dArrB[i4][i5] + ", Set to the maximum value: " + aMVarC2.r());
                                        }
                                        z4 = true;
                                    }
                                    dArrB[i4][i5] = aMVarC2.r();
                                } else if (dArrB[i4][i5] < aMVarC2.a(i4)) {
                                    if (!z5) {
                                        b(nodeName + " row:" + i4 + ", col:" + i5 + ", One or more value below minimum: " + dArrB[i4][i5] + ", Set to the minimum value: " + aMVarC2.a(i4));
                                        z5 = true;
                                    }
                                    dArrB[i4][i5] = aMVarC2.a(i4);
                                }
                            }
                        } catch (Exception e8) {
                            b("Failed to set value for " + nodeName + " " + str + " value is not valid for current configuration: " + e8.getMessage());
                            return;
                        }
                    }
                    r2.a(nodeName, dArrB);
                }
            }
        }
    }

    private void b(String str) {
        if (this.f2046f) {
            return;
        }
        bH.C.b(str);
    }

    public ag b(File file) throws V.g {
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(C0193s.a(file));
            NodeList elementsByTagName = documentBuilderNewDocumentBuilder.parse(inputSource).getElementsByTagName("*");
            ag agVar = new ag();
            agVar.a(file);
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("bibliography")) {
                    NamedNodeMap attributes = nodeItem.getAttributes();
                    nodeItem.getNodeName();
                    for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                        Node nodeItem2 = attributes.item(i3);
                        if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("tuneComment")) {
                            agVar.a(nodeItem2.getNodeValue());
                        } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("writeDate")) {
                            agVar.b(nodeItem2.getNodeValue());
                        }
                    }
                } else if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("versionInfo")) {
                    NamedNodeMap attributes2 = nodeItem.getAttributes();
                    nodeItem.getNodeName();
                    for (int i4 = 0; i4 < attributes2.getLength(); i4++) {
                        Node nodeItem3 = attributes2.item(i4);
                        if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals(X509CertImpl.SIGNATURE)) {
                            agVar.c(nodeItem3.getNodeValue());
                        } else if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("firmwareInfo")) {
                            String nodeValue = nodeItem3.getNodeValue();
                            if (nodeValue != null && !nodeValue.isEmpty()) {
                                nodeValue = URLDecoder.decode(nodeValue, "UTF-8");
                            }
                            agVar.e(nodeValue);
                        }
                    }
                }
            }
            return agVar;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.g("Error loading " + ((Object) file) + ": " + e2.getMessage(), e2);
        }
    }

    private String c(String str) throws NumberFormatException {
        if (str.indexOf("\n") == -1) {
            return "" + (((Double.parseDouble(str) * 9.0d) / 5.0d) + 32.0d);
        }
        String[] strArrC = bH.W.c(str, "\n");
        String str2 = "";
        for (int i2 = 0; i2 < strArrC.length; i2++) {
            if (strArrC[i2].length() > 0) {
                str2 = str2 + (((Double.parseDouble(strArrC[i2]) * 9.0d) / 5.0d) + 32.0d) + " ";
            }
            if (i2 < strArrC.length - 1) {
                str2 = str2 + "\n";
            }
        }
        return str2;
    }

    private String d(String str) throws NumberFormatException {
        if (str.indexOf("\n") == -1) {
            return "" + (((Double.parseDouble(str) - 32.0d) * 5.0d) / 9.0d);
        }
        String[] strArrC = bH.W.c(str, "\n");
        String str2 = "";
        for (int i2 = 0; i2 < strArrC.length; i2++) {
            if (strArrC[i2].length() > 0) {
                str2 = str2 + (((Double.parseDouble(strArrC[i2]) - 32.0d) * 5.0d) / 9.0d) + " ";
            }
            if (i2 < strArrC.length - 1) {
                str2 = str2 + "\n";
            }
        }
        return str2;
    }

    private String e(String str) throws NumberFormatException {
        if (str.indexOf("\n") == -1 && str.indexOf(" ") == -1) {
            return "" + (Double.parseDouble(str) / 14.7d);
        }
        String[] strArrC = bH.W.c(str, "\n");
        String str2 = "";
        for (int i2 = 0; i2 < strArrC.length; i2++) {
            String[] strArrC2 = bH.W.c(strArrC[i2], " ");
            for (int i3 = 0; strArrC2.length > i3; i3++) {
                str2 = str2 + (Double.parseDouble(strArrC2[i3]) / 14.7d) + " ";
            }
            if (i2 < strArrC.length - 1) {
                str2 = str2 + "\n";
            }
        }
        return str2;
    }

    private String f(String str) throws NumberFormatException {
        if (str.indexOf("\n") == -1 && str.indexOf(" ") == -1) {
            return "" + (Double.parseDouble(str) * 14.7d);
        }
        String[] strArrC = bH.W.c(str, "\n");
        String str2 = "";
        for (int i2 = 0; i2 < strArrC.length; i2++) {
            String[] strArrC2 = bH.W.c(strArrC[i2], " ");
            for (int i3 = 0; strArrC2.length > i3; i3++) {
                str2 = str2 + (Double.parseDouble(strArrC2[i3]) * 14.7d) + " ";
            }
            if (i2 < strArrC.length - 1) {
                str2 = str2 + "\n";
            }
        }
        return str2;
    }

    public String[] c(File file) {
        try {
            ArrayList arrayList = new ArrayList();
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("*");
            for (int i2 = 0; i2 < elementsByTagName.getLength(); i2++) {
                Node nodeItem = elementsByTagName.item(i2);
                if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("setting")) {
                    try {
                        arrayList.add(nodeItem.getAttributes().getNamedItem("name").getNodeValue());
                    } catch (Exception e2) {
                        b("Could not get setting from node:" + nodeItem.toString());
                    }
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } catch (Exception e3) {
            throw new V.g("Error retrieving settings from " + ((Object) file) + ": " + e3.getMessage(), e3);
        }
    }

    public void a(File file, String str) throws IOException {
        String line;
        String line2;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FTP.DEFAULT_CONTROL_ENCODING));
        StringBuilder sb = new StringBuilder();
        String line3 = bufferedReader.readLine();
        while (true) {
            String str2 = line3;
            if (str2 == null || str2.contains("<page>")) {
                break;
            }
            sb.append(str2).append("\n");
            line3 = bufferedReader.readLine();
        }
        sb.append(str);
        do {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
        } while (!line.contains("</page>"));
        do {
            line2 = bufferedReader.readLine();
            if (line2 != null) {
                sb.append(line2).append("\n");
            }
        } while (line2 != null);
        bufferedReader.close();
        file.delete();
        ByteBuffer byteBufferEncode = Charset.forName(FTP.DEFAULT_CONTROL_ENCODING).encode(sb.toString());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        bufferedOutputStream.write(byteBufferEncode.array());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public String d(File file) throws IOException {
        String line;
        String line2;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FTP.DEFAULT_CONTROL_ENCODING));
        StringBuilder sb = new StringBuilder();
        do {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
        } while (!line.contains("<page>"));
        sb.append(line).append("\n");
        do {
            line2 = bufferedReader.readLine();
            sb.append(line2).append("\n");
            if (line2 == null) {
                break;
            }
        } while (!line2.contains("</page>"));
        bufferedReader.close();
        return sb.toString();
    }

    private boolean a(String str, String str2) {
        if ((str == null || str.equals("")) && (str2 == null || str2.equals(""))) {
            return true;
        }
        if (str == null || str2 == null) {
            return false;
        }
        return str.equals(str2);
    }

    public void a(G.R r2, String str, A a2) {
        a(r2, str, a2, null);
    }

    public void a(G.R r2, String str, A a2, List list) {
        int length;
        int length2;
        bH.Z z2 = new bH.Z();
        z2.a();
        try {
            Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement(f2043c);
            elementCreateElement.setAttribute("xmlns", f2042b);
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("author", a2.a() + " " + a2.b() + " - EFI Analytics, Inc.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement2.setAttribute("tuneComment", r2.Q());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "5.0");
            elementCreateElement3.setAttribute("nPages", "" + r2.p().e());
            elementCreateElement3.setAttribute(X509CertImpl.SIGNATURE, r2.i());
            String strP = r2.P() != null ? r2.P() : "";
            if (strP.isEmpty() && r2.Z() != null) {
                strP = r2.Z();
            }
            try {
                strP = strP.length() == 2 ? (Integer.toHexString(strP.charAt(0)) + Integer.toHexString(strP.charAt(1))).toUpperCase() : URLEncoder.encode(bH.W.h(strP), "UTF-8");
            } catch (Exception e2) {
                Logger.getLogger(C0171aa.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            elementCreateElement3.setAttribute("firmwareInfo", strP);
            elementCreateElement.appendChild(elementCreateElement3);
            int iE = r2.p().e();
            int i2 = -1;
            while (i2 < iE) {
                Element elementCreateElement4 = documentNewDocument.createElement("page");
                if (i2 >= 0) {
                    elementCreateElement4.setAttribute("number", "" + i2);
                    elementCreateElement4.setAttribute("size", "" + r2.p().b(i2).length);
                }
                ArrayList<aM> arrayList = new ArrayList();
                Iterator itA = r2.a(i2);
                while (itA.hasNext()) {
                    arrayList.add(itA.next());
                }
                for (aM aMVar : arrayList) {
                    if (aMVar.n(r2.p()) && (list == null || list.contains(aMVar.aJ()))) {
                        Element elementCreateElement5 = i2 < 0 ? documentNewDocument.createElement("pcVariable") : documentNewDocument.createElement(FXMLLoader.FX_CONSTANT_ATTRIBUTE);
                        elementCreateElement5.setAttribute("name", aMVar.aJ());
                        if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                            elementCreateElement5.setTextContent(aMVar.f(r2.p()));
                        } else if (aMVar.i().equals("string")) {
                            elementCreateElement5.setTextContent(aMVar.d(r2.p()));
                        } else if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) || aMVar.i().equals(bZ.f884e) || aMVar.i().equals(bZ.f883d)) {
                            if (aMVar.o() != null && !aMVar.o().equals("")) {
                                elementCreateElement5.setAttribute("units", aMVar.o());
                            }
                            if (!aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                                elementCreateElement5.setAttribute("digits", aMVar.u() + "");
                            }
                            double dJ = aMVar.j(r2.p());
                            if (aMVar.H()) {
                                elementCreateElement5.setTextContent(Float.toString((float) dJ));
                            } else {
                                elementCreateElement5.setTextContent(Double.toString(Math.round(dJ * 1.0E7d) / 1.0E7d));
                            }
                        } else if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                            String[][] strArr = new String[aMVar.a()][aMVar.m()];
                            if (aMVar.o() != null && !aMVar.o().equals("")) {
                                elementCreateElement5.setAttribute("units", aMVar.o());
                            }
                            if (!aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                                elementCreateElement5.setAttribute("digits", aMVar.u() + "");
                            }
                            if (aMVar.b() > 1) {
                                elementCreateElement5.setAttribute("rows", "" + aMVar.c().b());
                            }
                            if (aMVar.c().b() > 1) {
                                elementCreateElement5.setAttribute("cols", "" + aMVar.c().a());
                            }
                            double[][] dArrI = aMVar.i(r2.p());
                            for (int i3 = 0; i3 < dArrI.length; i3++) {
                                for (int i4 = 0; i4 < dArrI[0].length; i4++) {
                                    if (aMVar.K()) {
                                        length = (dArrI.length - i3) - 1;
                                        length2 = (dArrI[0].length - i4) - 1;
                                    } else {
                                        length = i3;
                                        length2 = i4;
                                    }
                                    double d2 = dArrI[i3][i4];
                                    if (aMVar.H()) {
                                        strArr[length][length2] = "" + ((float) d2);
                                    } else {
                                        strArr[length][length2] = "" + (Math.round(d2 * 1.0E7d) / 1.0E7d);
                                    }
                                }
                            }
                            elementCreateElement5.setTextContent(a(strArr));
                        }
                        elementCreateElement4.appendChild(elementCreateElement5);
                    }
                }
                elementCreateElement.appendChild(elementCreateElement4);
                i2++;
            }
            Element elementCreateElement6 = documentNewDocument.createElement("settings");
            elementCreateElement6.setAttribute("Comment", "These setting are only used if this msq is opened without a project.");
            for (C0135r c0135r : r2.m().values()) {
                Element elementCreateElement7 = documentNewDocument.createElement("setting");
                elementCreateElement7.setAttribute("name", c0135r.aJ());
                elementCreateElement7.setAttribute("value", c0135r.aJ());
                elementCreateElement6.appendChild(elementCreateElement7);
            }
            elementCreateElement.appendChild(elementCreateElement6);
            Element elementCreateElement8 = documentNewDocument.createElement("userComments");
            elementCreateElement8.setAttribute("Comment", "These are user comments that can be related to a particular setting or dialog.");
            Iterator itE = r2.E();
            while (itE.hasNext()) {
                String str2 = (String) itE.next();
                if (list == null || list.contains(str2)) {
                    String strO = r2.o(str2);
                    Element elementCreateElement9 = documentNewDocument.createElement("userComment");
                    elementCreateElement9.setAttribute("name", str2);
                    elementCreateElement9.setAttribute("value", strO);
                    elementCreateElement8.appendChild(elementCreateElement9);
                }
            }
            elementCreateElement.appendChild(elementCreateElement8);
            try {
                a(str, documentNewDocument);
            } catch (Exception e3) {
                try {
                    Thread.sleep(100L);
                } catch (Exception e4) {
                }
                a(str, documentNewDocument);
                bH.C.d("Saved on retry.");
            }
            bH.C.d("Time to save msq: " + z2.d());
        } catch (ParserConfigurationException e5) {
            e5.printStackTrace();
            throw new V.g("Invalid XML parser option. \n" + e5.getMessage(), e5);
        }
    }

    public void b(G.R r2, String str, A a2) {
        try {
            Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement(f2043c);
            elementCreateElement.setAttribute("xmlns", f2042b);
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("author", a2.a() + " " + a2.b() + " - EFI Analytics, Inc.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "4.0");
            elementCreateElement3.setAttribute(X509CertImpl.SIGNATURE, r2.i());
            String strEncode = "";
            try {
                if (strEncode.length() == 2) {
                    strEncode = (Integer.toHexString(strEncode.charAt(0)) + Integer.toHexString(strEncode.charAt(1))).toUpperCase();
                } else if (r2.P() != null) {
                    strEncode = URLEncoder.encode(r2.P(), "UTF-8");
                }
            } catch (Exception e2) {
                Logger.getLogger(C0171aa.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            elementCreateElement3.setAttribute("firmwareInfo", strEncode);
            elementCreateElement.appendChild(elementCreateElement3);
            for (int i2 = -1; i2 < 0; i2++) {
                Element elementCreateElement4 = documentNewDocument.createElement("page");
                elementCreateElement4.setAttribute("number", "" + i2);
                Iterator itA = r2.a(i2);
                while (itA.hasNext()) {
                    aM aMVar = (aM) itA.next();
                    if (aMVar.n(r2.p())) {
                        Element elementCreateElement5 = documentNewDocument.createElement(FXMLLoader.FX_CONSTANT_ATTRIBUTE);
                        elementCreateElement5.setAttribute("name", aMVar.aJ());
                        if (!aMVar.i().equals("string") && aMVar.o() != null && !aMVar.o().equals("")) {
                            elementCreateElement5.setAttribute("units", aMVar.o());
                        }
                        if (!aMVar.i().equals("string") && !aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                            elementCreateElement5.setAttribute("digits", aMVar.u() + "");
                        }
                        if (!aMVar.i().equals("string") && aMVar.b() > 1) {
                            elementCreateElement5.setAttribute("rows", "" + aMVar.c().b());
                        }
                        if (!aMVar.i().equals("string") && aMVar.c().b() > 1) {
                            elementCreateElement5.setAttribute("cols", "" + aMVar.c().a());
                        }
                        if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                            elementCreateElement5.setTextContent(aMVar.f(r2.p()));
                        } else if (aMVar.i().equals("string")) {
                            elementCreateElement5.setTextContent(aMVar.d(r2.p()));
                        } else if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) || aMVar.i().equals(bZ.f884e) || aMVar.i().equals(bZ.f883d)) {
                            elementCreateElement5.setTextContent((Math.round(aMVar.j(r2.p()) * 1.0E7d) / 1.0E7d) + "");
                        } else if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                            String[][] strArr = new String[aMVar.a()][aMVar.m()];
                            double[][] dArrI = aMVar.i(r2.p());
                            for (int i3 = 0; i3 < dArrI.length; i3++) {
                                for (int i4 = 0; i4 < dArrI[0].length; i4++) {
                                    strArr[i3][i4] = "" + (Math.round(dArrI[i3][i4] * 1.0E7d) / 1.0E7d);
                                }
                            }
                            elementCreateElement5.setTextContent(a(strArr));
                        }
                        elementCreateElement4.appendChild(elementCreateElement5);
                    }
                }
                elementCreateElement.appendChild(elementCreateElement4);
            }
            a(str, documentNewDocument);
        } catch (ParserConfigurationException e3) {
            e3.printStackTrace();
            throw new V.g("Invalid XML parser option. \n" + e3.getMessage(), e3);
        }
    }

    private String a(String[][] strArr) {
        return bH.W.a(strArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(String str, Document document) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                File file = new File(str);
                if (!file.exists()) {
                    bH.C.c("File does not exist, creating new:\n" + str);
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(str);
                if (f2044d) {
                    C0199y c0199y = new C0199y(fileOutputStream);
                    c0199y.a("SiJ6!EK&JC%@");
                    fileOutputStream = c0199y;
                }
                DOMSource dOMSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(fileOutputStream);
                fileOutputStream.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n".getBytes());
                Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
                transformerNewTransformer.setOutputProperty("omit-xml-declaration", "yes");
                transformerNewTransformer.setOutputProperty("encoding", FTP.DEFAULT_CONTROL_ENCODING);
                transformerNewTransformer.setOutputProperty("indent", "yes");
                transformerNewTransformer.transform(dOMSource, streamResult);
                fileOutputStream.close();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new V.g("Error Saving MSQ. Check Log file for details.");
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e4) {
                }
            }
            throw th;
        }
    }

    private boolean g(String str) {
        return str.equals("KPaTarg60") || str.equals("KPaTarg40") || str.equals("KPaTarg10") || str.equals("KPaTarg100") || str.equals("ipwmTable");
    }

    public List a() {
        return this.f2045e;
    }

    public void a(boolean z2) {
        this.f2046f = z2;
    }
}
