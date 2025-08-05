package com.efiAnalytics.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eY.class */
public class eY {
    public void a(String str, C1701s c1701s) {
        try {
            Document documentNewDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element elementCreateElement = documentNewDocument.createElement("tableData");
            elementCreateElement.setAttribute("xmlns", "http://www.EFIAnalytics.com/:table");
            documentNewDocument.appendChild(elementCreateElement);
            Element elementCreateElement2 = documentNewDocument.createElement("bibliography");
            elementCreateElement2.setAttribute("author", "EFI Analytics - philip.tobin@yahoo.com");
            elementCreateElement2.setAttribute("company", "EFI Analytics, copyright 2010, All Rights Reserved.");
            elementCreateElement2.setAttribute("writeDate", new Date().toString());
            elementCreateElement.appendChild(elementCreateElement2);
            Element elementCreateElement3 = documentNewDocument.createElement("versionInfo");
            elementCreateElement3.setAttribute("fileFormat", "1.0");
            elementCreateElement.appendChild(elementCreateElement3);
            Element elementCreateElement4 = documentNewDocument.createElement("table");
            elementCreateElement4.setAttribute("rows", "" + c1701s.a().length);
            elementCreateElement4.setAttribute("cols", "" + c1701s.b().length);
            elementCreateElement.appendChild(elementCreateElement4);
            Element elementCreateElement5 = documentNewDocument.createElement("xAxis");
            elementCreateElement5.setAttribute("cols", "" + c1701s.b().length);
            elementCreateElement5.setAttribute("name", "" + c1701s.w());
            elementCreateElement5.setTextContent(bH.W.a(a(c1701s.b(), false)));
            elementCreateElement4.appendChild(elementCreateElement5);
            Element elementCreateElement6 = documentNewDocument.createElement("yAxis");
            elementCreateElement6.setAttribute("rows", "" + c1701s.a().length);
            elementCreateElement6.setAttribute("name", "" + c1701s.v());
            elementCreateElement6.setTextContent(bH.W.a(a(c1701s.a(), true)));
            elementCreateElement4.appendChild(elementCreateElement6);
            Element elementCreateElement7 = documentNewDocument.createElement("zValues");
            elementCreateElement7.setAttribute("cols", "" + c1701s.b().length);
            elementCreateElement7.setAttribute("rows", "" + c1701s.a().length);
            elementCreateElement7.setTextContent(bH.W.a(a(c1701s)));
            elementCreateElement4.appendChild(elementCreateElement7);
            a(str, documentNewDocument);
        } catch (V.a e2) {
            throw e2;
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new V.a("Error Saving Table. Check Log file for details.");
        }
    }

    private String[][] a(String[] strArr, boolean z2) {
        String[][] strArr2 = new String[strArr.length][1];
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            if (z2) {
                strArr2[(strArr2.length - 1) - i2][0] = strArr[i2];
            } else {
                strArr2[i2][0] = strArr[i2];
            }
        }
        return strArr2;
    }

    private String[][] a(W.an anVar) {
        String[][] strArr = new String[anVar.a().length][anVar.b().length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            for (int i3 = 0; i3 < strArr[0].length; i3++) {
                Object valueAt = anVar.getValueAt(i2, i3);
                if (valueAt != null) {
                    strArr[(strArr.length - 1) - i2][i3] = valueAt.toString();
                } else {
                    strArr[(strArr.length - 1) - i2][i3] = "";
                }
            }
        }
        return strArr;
    }

    public C1701s a(String str) {
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(new File(str));
                C1701s c1701sA = a(fileInputStream);
                try {
                    fileInputStream.close();
                } catch (IOException e2) {
                }
                return c1701sA;
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
                throw new V.a("Problem loading " + str + ", error message:\n" + e3.getMessage() + "\nCheck log file for more details.");
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (IOException e4) {
            }
            throw th;
        }
    }

    public C1701s a(InputStream inputStream) {
        String nodeValue;
        String nodeValue2;
        C1701s c1701s = new C1701s();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream).getElementsByTagName("*");
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < elementsByTagName.getLength(); i4++) {
                Node nodeItem = elementsByTagName.item(i4);
                if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("table")) {
                    NamedNodeMap attributes = nodeItem.getAttributes();
                    for (int i5 = 0; i5 < attributes.getLength(); i5++) {
                        Node nodeItem2 = attributes.item(i5);
                        if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("rows")) {
                            i2 = Integer.parseInt(nodeItem2.getNodeValue());
                        } else if (nodeItem2.getNodeName() != null && nodeItem2.getNodeName().equals("cols")) {
                            i3 = Integer.parseInt(nodeItem2.getNodeValue());
                        }
                    }
                    c1701s.a(i2, i3);
                } else if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("xAxis")) {
                    NamedNodeMap attributes2 = nodeItem.getAttributes();
                    for (int i6 = 0; i6 < attributes2.getLength(); i6++) {
                        Node nodeItem3 = attributes2.item(i6);
                        if (nodeItem3.getNodeName() != null && nodeItem3.getNodeName().equals("name") && (nodeValue2 = nodeItem3.getNodeValue()) != null && nodeValue2.length() > 0) {
                            c1701s.e(nodeValue2);
                        }
                    }
                    String textContent = nodeItem.getTextContent();
                    double[][] dArr = new double[i3][1];
                    bH.W.a(dArr, textContent);
                    c1701s.c(a(dArr));
                } else if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("yAxis")) {
                    NamedNodeMap attributes3 = nodeItem.getAttributes();
                    for (int i7 = 0; i7 < attributes3.getLength(); i7++) {
                        Node nodeItem4 = attributes3.item(i7);
                        if (nodeItem4.getNodeName() != null && nodeItem4.getNodeName().equals("name") && (nodeValue = nodeItem4.getNodeValue()) != null && nodeValue.length() > 0) {
                            c1701s.d(nodeValue);
                        }
                    }
                    String textContent2 = nodeItem.getTextContent();
                    double[][] dArr2 = new double[i2][1];
                    bH.W.a(dArr2, textContent2);
                    c1701s.d(a(dArr2));
                } else if (nodeItem.hasAttributes() && nodeItem.getNodeName().equals("zValues")) {
                    String textContent3 = nodeItem.getTextContent();
                    double[][] dArr3 = new double[i2][i3];
                    bH.W.a(dArr3, textContent3);
                    c1701s.a(dArr3);
                }
            }
            return c1701s;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Problem loading table, error message:\n" + e2.getMessage() + "\nCheck log file for more details.");
        }
    }

    public C1701s b(String str, C1701s c1701s) {
        C1677fh.a(a(str), c1701s);
        return c1701s;
    }

    private String[] a(double[][] dArr) {
        String[] strArr = new String[dArr.length];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            strArr[i2] = dArr[i2][0] + "";
        }
        return strArr;
    }

    public void a(String str, Document document) throws V.a {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DOMSource dOMSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(fileOutputStream);
            Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
            transformerNewTransformer.setOutputProperty("indent", "yes");
            transformerNewTransformer.transform(dOMSource, streamResult);
            fileOutputStream.close();
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error Saving Document. Check Log file for details.");
        }
    }
}
