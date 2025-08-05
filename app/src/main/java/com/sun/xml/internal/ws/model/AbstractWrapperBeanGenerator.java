package com.sun.xml.internal.ws.model;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.spi.db.BindingHelper;
import com.sun.xml.internal.ws.util.StringUtils;
import java.lang.Comparable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.xml.bind.annotation.XmlAttachmentRef;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/AbstractWrapperBeanGenerator.class */
public abstract class AbstractWrapperBeanGenerator<T, C, M, A extends Comparable> {
    private static final String RETURN = "return";
    private static final String EMTPY_NAMESPACE_ID = "";
    private final AnnotationReader<T, C, ?, M> annReader;
    private final Navigator<T, C, ?, M> nav;
    private final BeanMemberFactory<T, A> factory;
    private static final Map<String, String> reservedWords;
    private static final Logger LOGGER = Logger.getLogger(AbstractWrapperBeanGenerator.class.getName());
    private static final Class[] jaxbAnns = {XmlAttachmentRef.class, XmlMimeType.class, XmlJavaTypeAdapter.class, XmlList.class, XmlElement.class};
    private static final Set<String> skipProperties = new HashSet();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/AbstractWrapperBeanGenerator$BeanMemberFactory.class */
    public interface BeanMemberFactory<T, A> {
        A createWrapperBeanMember(T t2, String str, List<Annotation> list);
    }

    protected abstract T getSafeType(T t2);

    protected abstract T getHolderValueType(T t2);

    protected abstract boolean isVoidType(T t2);

    static {
        skipProperties.add("getCause");
        skipProperties.add("getLocalizedMessage");
        skipProperties.add("getClass");
        skipProperties.add("getStackTrace");
        skipProperties.add("getSuppressed");
        reservedWords = new HashMap();
        reservedWords.put("abstract", "_abstract");
        reservedWords.put("assert", "_assert");
        reservedWords.put("boolean", "_boolean");
        reservedWords.put("break", "_break");
        reservedWords.put(SchemaSymbols.ATTVAL_BYTE, "_byte");
        reservedWords.put("case", "_case");
        reservedWords.put("catch", "_catch");
        reservedWords.put("char", "_char");
        reservedWords.put(Constants.ATTRNAME_CLASS, "_class");
        reservedWords.put("const", "_const");
        reservedWords.put("continue", "_continue");
        reservedWords.put("default", "_default");
        reservedWords.put("do", "_do");
        reservedWords.put(SchemaSymbols.ATTVAL_DOUBLE, "_double");
        reservedWords.put("else", "_else");
        reservedWords.put("extends", "_extends");
        reservedWords.put("false", "_false");
        reservedWords.put("final", "_final");
        reservedWords.put("finally", "_finally");
        reservedWords.put(SchemaSymbols.ATTVAL_FLOAT, "_float");
        reservedWords.put("for", "_for");
        reservedWords.put("goto", "_goto");
        reservedWords.put(Constants.ELEMNAME_IF_STRING, "_if");
        reservedWords.put("implements", "_implements");
        reservedWords.put("import", "_import");
        reservedWords.put("instanceof", "_instanceof");
        reservedWords.put("int", "_int");
        reservedWords.put("interface", "_interface");
        reservedWords.put(SchemaSymbols.ATTVAL_LONG, "_long");
        reservedWords.put("native", "_native");
        reservedWords.put("new", "_new");
        reservedWords.put(FXMLLoader.NULL_KEYWORD, "_null");
        reservedWords.put("package", "_package");
        reservedWords.put(PolicyConstants.VISIBILITY_VALUE_PRIVATE, "_private");
        reservedWords.put("protected", "_protected");
        reservedWords.put("public", "_public");
        reservedWords.put("return", "_return");
        reservedWords.put(SchemaSymbols.ATTVAL_SHORT, "_short");
        reservedWords.put("static", "_static");
        reservedWords.put("strictfp", "_strictfp");
        reservedWords.put("super", "_super");
        reservedWords.put("switch", "_switch");
        reservedWords.put("synchronized", "_synchronized");
        reservedWords.put("this", "_this");
        reservedWords.put("throw", "_throw");
        reservedWords.put("throws", "_throws");
        reservedWords.put("transient", "_transient");
        reservedWords.put("true", "_true");
        reservedWords.put("try", "_try");
        reservedWords.put("void", "_void");
        reservedWords.put("volatile", "_volatile");
        reservedWords.put("while", "_while");
        reservedWords.put("enum", "_enum");
    }

    protected AbstractWrapperBeanGenerator(AnnotationReader<T, C, ?, M> annReader, Navigator<T, C, ?, M> nav, BeanMemberFactory<T, A> factory) {
        this.annReader = annReader;
        this.nav = nav;
        this.factory = factory;
    }

    private List<Annotation> collectJAXBAnnotations(M method) {
        List<Annotation> jaxbAnnotation = new ArrayList<>();
        for (Class jaxbClass : jaxbAnns) {
            Annotation ann = this.annReader.getMethodAnnotation(jaxbClass, method, null);
            if (ann != null) {
                jaxbAnnotation.add(ann);
            }
        }
        return jaxbAnnotation;
    }

    private List<Annotation> collectJAXBAnnotations(M method, int paramIndex) {
        List<Annotation> jaxbAnnotation = new ArrayList<>();
        for (Class jaxbClass : jaxbAnns) {
            Annotation ann = this.annReader.getMethodParameterAnnotation(jaxbClass, method, paramIndex, null);
            if (ann != null) {
                jaxbAnnotation.add(ann);
            }
        }
        return jaxbAnnotation;
    }

    public List<A> collectRequestBeanMembers(M m2) {
        ArrayList arrayList = new ArrayList();
        int i2 = -1;
        for (T t2 : this.nav.getMethodParameters(m2)) {
            i2++;
            WebParam webParam = (WebParam) this.annReader.getMethodParameterAnnotation(WebParam.class, m2, i2, null);
            if (webParam == null || (!webParam.header() && !webParam.mode().equals(WebParam.Mode.OUT))) {
                T holderValueType = getHolderValueType(t2);
                T safeType = holderValueType != null ? holderValueType : getSafeType(t2);
                String strName = (webParam == null || webParam.name().length() <= 0) ? Constants.ELEMNAME_ARG_STRING + i2 : webParam.name();
                String strTargetNamespace = (webParam == null || webParam.targetNamespace().length() <= 0) ? "" : webParam.targetNamespace();
                List<Annotation> listCollectJAXBAnnotations = collectJAXBAnnotations(m2, i2);
                processXmlElement(listCollectJAXBAnnotations, strName, strTargetNamespace, safeType);
                arrayList.add((Comparable) this.factory.createWrapperBeanMember(safeType, getPropertyName(strName), listCollectJAXBAnnotations));
            }
        }
        return arrayList;
    }

    public List<A> collectResponseBeanMembers(M m2) {
        ArrayList arrayList = new ArrayList();
        String strName = "return";
        String strTargetNamespace = "";
        boolean zHeader = false;
        WebResult webResult = (WebResult) this.annReader.getMethodAnnotation(WebResult.class, m2, null);
        if (webResult != null) {
            if (webResult.name().length() > 0) {
                strName = webResult.name();
            }
            if (webResult.targetNamespace().length() > 0) {
                strTargetNamespace = webResult.targetNamespace();
            }
            zHeader = webResult.header();
        }
        T safeType = getSafeType(this.nav.getReturnType(m2));
        if (!isVoidType(safeType) && !zHeader) {
            List<Annotation> listCollectJAXBAnnotations = collectJAXBAnnotations(m2);
            processXmlElement(listCollectJAXBAnnotations, strName, strTargetNamespace, safeType);
            arrayList.add(this.factory.createWrapperBeanMember(safeType, getPropertyName(strName), listCollectJAXBAnnotations));
        }
        int i2 = -1;
        for (T t2 : this.nav.getMethodParameters(m2)) {
            i2++;
            T holderValueType = getHolderValueType(t2);
            WebParam webParam = (WebParam) this.annReader.getMethodParameterAnnotation(WebParam.class, m2, i2, null);
            if (holderValueType != null && (webParam == null || !webParam.header())) {
                String strName2 = (webParam == null || webParam.name().length() <= 0) ? Constants.ELEMNAME_ARG_STRING + i2 : webParam.name();
                String strTargetNamespace2 = (webParam == null || webParam.targetNamespace().length() <= 0) ? "" : webParam.targetNamespace();
                List<Annotation> listCollectJAXBAnnotations2 = collectJAXBAnnotations(m2, i2);
                processXmlElement(listCollectJAXBAnnotations2, strName2, strTargetNamespace2, holderValueType);
                arrayList.add((Comparable) this.factory.createWrapperBeanMember(holderValueType, getPropertyName(strName2), listCollectJAXBAnnotations2));
            }
        }
        return arrayList;
    }

    private void processXmlElement(List<Annotation> jaxb, String elemName, String elemNS, T type) {
        XmlElement elemAnn = null;
        Iterator<Annotation> it = jaxb.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Annotation a2 = it.next();
            if (a2.annotationType() == XmlElement.class) {
                elemAnn = (XmlElement) a2;
                jaxb.remove(a2);
                break;
            }
        }
        String name = (elemAnn == null || elemAnn.name().equals("##default")) ? elemName : elemAnn.name();
        String ns = (elemAnn == null || elemAnn.namespace().equals("##default")) ? elemNS : elemAnn.namespace();
        boolean nillable = this.nav.isArray(type) || (elemAnn != null && elemAnn.nillable());
        boolean required = elemAnn != null && elemAnn.required();
        XmlElementHandler handler = new XmlElementHandler(name, ns, nillable, required);
        XmlElement elem = (XmlElement) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{XmlElement.class}, handler);
        jaxb.add(elem);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/AbstractWrapperBeanGenerator$XmlElementHandler.class */
    private static class XmlElementHandler implements InvocationHandler {
        private String name;
        private String namespace;
        private boolean nillable;
        private boolean required;

        XmlElementHandler(String name, String namespace, boolean nillable, boolean required) {
            this.name = name;
            this.namespace = namespace;
            this.nillable = nillable;
            this.required = required;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (methodName.equals("name")) {
                return this.name;
            }
            if (methodName.equals(Constants.ATTRNAME_NAMESPACE)) {
                return this.namespace;
            }
            if (methodName.equals("nillable")) {
                return Boolean.valueOf(this.nillable);
            }
            if (methodName.equals(SchemaSymbols.ATTVAL_REQUIRED)) {
                return Boolean.valueOf(this.required);
            }
            throw new WebServiceException("Not handling " + methodName);
        }
    }

    public Collection<A> collectExceptionBeanMembers(C exception) {
        return collectExceptionBeanMembers(exception, true);
    }

    public Collection<A> collectExceptionBeanMembers(C exception, boolean decapitalize) {
        TreeMap<String, A> fields = new TreeMap<>();
        getExceptionProperties(exception, fields, decapitalize);
        XmlType xmlType = (XmlType) this.annReader.getClassAnnotation(XmlType.class, exception, null);
        if (xmlType != null) {
            String[] propOrder = xmlType.propOrder();
            if (propOrder.length > 0 && propOrder[0].length() != 0) {
                List<A> list = new ArrayList<>();
                for (String prop : propOrder) {
                    A a2 = fields.get(prop);
                    if (a2 != null) {
                        list.add(a2);
                    } else {
                        throw new WebServiceException("Exception " + ((Object) exception) + " has @XmlType and its propOrder contains unknown property " + prop);
                    }
                }
                return list;
            }
        }
        return fields.values();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getExceptionProperties(C c2, TreeMap<String, A> treeMap, boolean z2) {
        C superClass = this.nav.getSuperClass(c2);
        if (superClass != null) {
            getExceptionProperties(superClass, treeMap, z2);
        }
        for (M m2 : this.nav.getDeclaredMethods(c2)) {
            if (this.nav.isPublicMethod(m2) && (!this.nav.isStaticMethod(m2) || !this.nav.isFinalMethod(m2))) {
                if (this.nav.isPublicMethod(m2)) {
                    String methodName = this.nav.getMethodName(m2);
                    if (methodName.startsWith("get") || methodName.startsWith(BeanAdapter.IS_PREFIX)) {
                        if (!skipProperties.contains(methodName) && !methodName.equals("get") && !methodName.equals(BeanAdapter.IS_PREFIX)) {
                            T safeType = getSafeType(this.nav.getReturnType(m2));
                            if (this.nav.getMethodParameters(m2).length == 0) {
                                String strSubstring = methodName.startsWith("get") ? methodName.substring(3) : methodName.substring(2);
                                if (z2) {
                                    strSubstring = StringUtils.decapitalize(strSubstring);
                                }
                                treeMap.put(strSubstring, this.factory.createWrapperBeanMember(safeType, strSubstring, Collections.emptyList()));
                            }
                        }
                    }
                }
            }
        }
    }

    private static String getPropertyName(String name) {
        String propertyName = BindingHelper.mangleNameToVariableName(name);
        return getJavaReservedVarialbeName(propertyName);
    }

    @NotNull
    private static String getJavaReservedVarialbeName(@NotNull String name) {
        String reservedName = reservedWords.get(name);
        return reservedName == null ? name : reservedName;
    }
}
