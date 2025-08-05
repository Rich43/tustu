package v;

import G.dh;
import bH.C;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: v.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/e.class */
public class C1885e {

    /* renamed from: a, reason: collision with root package name */
    private HashMap f14021a = new HashMap();

    public void a(String str, Document document) throws V.a {
        File file = new File(str);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e2) {
                Logger.getLogger(C1885e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new V.a("Error writing to file. \n" + str);
            }
        }
        a(file, document);
    }

    public void a(File file, Document document) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
                DOMSource dOMSource = new DOMSource(document);
                StreamResult streamResult = new StreamResult(fileOutputStream);
                Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
                transformerNewTransformer.setOutputProperty("indent", "yes");
                transformerNewTransformer.transform(dOMSource, streamResult);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        Logger.getLogger(C1885e.class.getName()).log(Level.WARNING, "Failed to close stream after saving Document", (Throwable) e2);
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new V.a("Error Saving Document. Check Log file for details.");
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e4) {
                    Logger.getLogger(C1885e.class.getName()).log(Level.WARNING, "Failed to close stream after saving Document", (Throwable) e4);
                }
            }
            throw th;
        }
    }

    public Document c(String str) {
        FileInputStream fileInputStream = null;
        File file = new File(str);
        try {
            try {
                DocumentBuilder documentBuilderNewDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                fileInputStream = new FileInputStream(file);
                Document document = documentBuilderNewDocumentBuilder.parse(fileInputStream);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                        Logger.getLogger(C1885e.class.getName()).log(Level.WARNING, "Failed to close stream after reading Document", (Throwable) e2);
                    }
                }
                return document;
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new V.a("Problem loading " + str + ", error message:\n" + e3.getMessage() + "\nCheck log file for more details.");
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e4) {
                    Logger.getLogger(C1885e.class.getName()).log(Level.WARNING, "Failed to close stream after reading Document", (Throwable) e4);
                }
            }
            throw th;
        }
    }

    public Element a(Document document, Element element, Object obj) throws DOMException, V.a, SecurityException {
        element.setAttribute("type", C1881a.a(obj));
        for (Class<?> superclass = obj.getClass(); superclass.getPackage().getName().contains("efiAnalytics"); superclass = superclass.getSuperclass()) {
            Method[] declaredMethods = superclass.getDeclaredMethods();
            AccessibleObject.setAccessible(declaredMethods, true);
            for (Method method : declaredMethods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (Modifier.isPublic(method.getModifiers()) && parameterTypes.length == 0 && (method.getName().startsWith("get") || method.getName().startsWith(BeanAdapter.IS_PREFIX))) {
                    try {
                        Element elementCreateElement = document.createElement(method.getName().startsWith(BeanAdapter.IS_PREFIX) ? method.getName().substring(2) : method.getName().substring(3));
                        synchronized (elementCreateElement) {
                            String name = method.getReturnType().getName();
                            if (name.equals("java.awt.Color")) {
                                name = "Color";
                                Color color = (Color) method.invoke(obj, new Object[0]);
                                if (color != null) {
                                    elementCreateElement.setAttribute("red", "" + color.getRed());
                                    elementCreateElement.setAttribute("green", "" + color.getGreen());
                                    elementCreateElement.setAttribute("blue", "" + color.getBlue());
                                    elementCreateElement.setAttribute("alpha", "" + color.getAlpha());
                                    elementCreateElement.setTextContent("" + color.getRGB());
                                } else {
                                    elementCreateElement.setTextContent("Transparent");
                                }
                            } else if (name.equals("com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter") || name.equals("GaugePainter")) {
                                name = "GaugePainter";
                                elementCreateElement.setTextContent(com.efiAnalytics.apps.ts.dashboard.renderers.e.a(method.invoke(obj, new Object[0])));
                            } else if (name.equals("com.efiAnalytics.apps.ts.dashboard.renderers.IndicatorPainter") || name.equals("IndicatorPainter")) {
                                name = "IndicatorPainter";
                                elementCreateElement.setTextContent(com.efiAnalytics.apps.ts.dashboard.renderers.e.a(method.invoke(obj, new Object[0])));
                            } else if (method.getReturnType().equals(dh.class)) {
                                name = C1881a.f14009g;
                                Object objInvoke = method.invoke(obj, new Object[0]);
                                if (objInvoke == null) {
                                    objInvoke = "";
                                }
                                elementCreateElement.setTextContent(objInvoke.toString());
                            } else if (method.getReturnType().equals(Properties.class)) {
                                name = "Properties";
                                elementCreateElement.setTextContent(a((Properties) method.invoke(obj, new Object[0])));
                            } else {
                                elementCreateElement.setTextContent(method.invoke(obj, new Object[0]) + "");
                            }
                            if (name.equals("java.lang.String")) {
                                name = "String";
                            }
                            elementCreateElement.setAttribute("type", name);
                            if (elementCreateElement.getTextContent() != null) {
                                element.appendChild(elementCreateElement);
                            }
                        }
                    } catch (Exception e2) {
                        C.a("Error writing Object to XML\n" + obj);
                        e2.printStackTrace();
                        throw new V.a("Error writing Object to XML");
                    }
                }
            }
        }
        return element;
    }

    public String a(Node node, String str) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node nodeItem = attributes.item(i2);
            if (nodeItem.getNodeName() != null && nodeItem.getNodeName().equals(str)) {
                return nodeItem.getNodeValue();
            }
        }
        C.b("Attribute not found: " + str);
        return null;
    }

    public Object a(Node node) {
        String strA = a(node, "type");
        Method methodA = null;
        try {
            try {
                a();
                Object objNewInstance = C1881a.a(strA).newInstance();
                Method[] declaredMethods = objNewInstance.getClass().getDeclaredMethods();
                AccessibleObject.setAccessible(declaredMethods, true);
                NodeList childNodes = node.getChildNodes();
                for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                    Node nodeItem = childNodes.item(i2);
                    String nodeName = nodeItem.getNodeName();
                    String strA2 = a(nodeItem, "type");
                    if (strA2 != null) {
                        methodA = a(declaredMethods, "set" + nodeName);
                        if (methodA != null && nodeItem.getTextContent() != null) {
                            String textContent = nodeItem.getTextContent();
                            if (strA2.equals("int")) {
                                methodA.invoke(objNewInstance, Integer.valueOf(textContent));
                            } else if (strA2.equals(SchemaSymbols.ATTVAL_DOUBLE)) {
                                methodA.invoke(objNewInstance, Double.valueOf(textContent));
                            } else if (strA2.endsWith(C1881a.f14009g)) {
                                methodA.invoke(objNewInstance, textContent);
                            } else if (strA2.equals(SchemaSymbols.ATTVAL_FLOAT)) {
                                Class<?>[] parameterTypes = methodA.getParameterTypes();
                                if (parameterTypes != null && parameterTypes.length > 0) {
                                    Object[] objArr = new Object[1];
                                    if (parameterTypes[0].getName().equals("int")) {
                                        objArr[0] = Integer.valueOf(Math.round(Float.parseFloat(textContent)));
                                    } else if (parameterTypes[0].getName().equals(SchemaSymbols.ATTVAL_FLOAT)) {
                                        objArr[0] = Float.valueOf(textContent);
                                    } else {
                                        objArr[0] = Double.valueOf(textContent);
                                    }
                                    methodA.invoke(objNewInstance, objArr);
                                }
                            } else if (strA2.equals("java.awt.Color") || strA2.equals("Color")) {
                                if (textContent.equals("Transparent")) {
                                    methodA.invoke(objNewInstance, null);
                                } else {
                                    int i3 = Integer.parseInt(textContent);
                                    NamedNodeMap attributes = nodeItem.getAttributes();
                                    if (attributes.getNamedItem("alpha") != null) {
                                        methodA.invoke(objNewInstance, new Color(Integer.parseInt(attributes.getNamedItem("red").getNodeValue()), Integer.parseInt(attributes.getNamedItem("green").getNodeValue()), Integer.parseInt(attributes.getNamedItem("blue").getNodeValue()), Integer.parseInt(attributes.getNamedItem("alpha").getNodeValue())));
                                    } else {
                                        methodA.invoke(objNewInstance, new Color(i3));
                                    }
                                }
                            } else if (strA2.equals("java.lang.String") || strA2.equals("String")) {
                                if (textContent.equals(FXMLLoader.NULL_KEYWORD)) {
                                    textContent = null;
                                }
                                methodA.invoke(objNewInstance, textContent);
                            } else if (strA2.equals("com.efiAnalytics.tunerStudio.renderers.GaugePainter") || strA2.equals("GaugePainter")) {
                                methodA.invoke(objNewInstance, com.efiAnalytics.apps.ts.dashboard.renderers.e.a(textContent));
                            } else if (strA2.equals("com.efiAnalytics.tunerStudio.renderers.IndicatorPainter") || strA2.equals("IndicatorPainter")) {
                                methodA.invoke(objNewInstance, com.efiAnalytics.apps.ts.dashboard.renderers.e.b(textContent));
                            } else if (strA2.equals("boolean")) {
                                methodA.invoke(objNewInstance, Boolean.valueOf(textContent));
                            } else if (strA2.equals("Properties")) {
                                methodA.invoke(objNewInstance, !textContent.equals(FXMLLoader.NULL_KEYWORD) ? a(textContent) : new Properties());
                            }
                        }
                    }
                }
                return objNewInstance;
            } catch (Exception e2) {
                C.c("Failure on method: " + ((Object) methodA));
                e2.printStackTrace();
                throw new V.a("Could not Load Object: " + strA + ", \nerror message:" + e2.getMessage());
            }
        } finally {
            a();
        }
    }

    private void a() {
        this.f14021a.clear();
    }

    private void a(Method[] methodArr) throws SecurityException {
        Class<?> declaringClass = methodArr[0].getDeclaringClass();
        while (methodArr != null && methodArr[0] != null && declaringClass.getPackage().getName().indexOf("efiAnalytics") != -1) {
            for (int i2 = 0; i2 < methodArr.length; i2++) {
                this.f14021a.put(methodArr[i2].getName(), methodArr[i2]);
            }
            declaringClass = declaringClass.getSuperclass();
            methodArr = declaringClass.getMethods();
        }
    }

    private Method a(Method[] methodArr, String str) throws SecurityException {
        if (this.f14021a.isEmpty()) {
            a(methodArr);
        }
        return (Method) this.f14021a.get(str);
    }

    private String a(Properties properties) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                properties.store(byteArrayOutputStream, "");
                String string = byteArrayOutputStream.toString("UTF-8");
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e2) {
                }
                return string;
            } catch (IOException e3) {
                C.a("Failed to Save a properties to XML");
                Logger.getLogger(C1885e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e4) {
                }
                return "";
            }
        } catch (Throwable th) {
            try {
                byteArrayOutputStream.close();
            } catch (Exception e5) {
            }
            throw th;
        }
    }

    private Properties a(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        Properties properties = new Properties();
        try {
            try {
                properties.load(byteArrayInputStream);
                try {
                    byteArrayInputStream.close();
                } catch (Exception e2) {
                }
            } catch (Throwable th) {
                try {
                    byteArrayInputStream.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        } catch (IOException e4) {
            C.a("Failed to load Properties from String: " + str);
            Logger.getLogger(C1885e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            try {
                byteArrayInputStream.close();
            } catch (Exception e5) {
            }
        }
        return properties;
    }
}
