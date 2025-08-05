package jdk.jfr.internal;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.EntityResolver;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import jdk.internal.util.xml.impl.SAXParserImpl;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Enabled;
import jdk.jfr.Experimental;
import jdk.jfr.Label;
import jdk.jfr.Period;
import jdk.jfr.Relational;
import jdk.jfr.StackTrace;
import jdk.jfr.Threshold;
import jdk.jfr.TransitionFrom;
import jdk.jfr.TransitionTo;
import jdk.jfr.Unsigned;

/* loaded from: jfr.jar:jdk/jfr/internal/MetadataHandler.class */
final class MetadataHandler extends DefaultHandler implements EntityResolver {
    final Map<String, TypeElement> types = new LinkedHashMap(200);
    final Map<String, XmlType> xmlTypes = new HashMap(20);
    final Map<String, List<AnnotationElement>> xmlContentTypes = new HashMap(20);
    final List<String> relations = new ArrayList();
    long eventTypeId = 255;
    long structTypeId = 33;
    FieldElement currentField;
    TypeElement currentType;

    /*  JADX ERROR: Failed to decode insn: 0x0070: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    private long nextTypeId(java.lang.String r9) {
        /*
            r8 = this;
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.THREAD
            java.lang.String r0 = r0.getName()
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L14
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.THREAD
            long r0 = r0.getId()
            return r0
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.STRING
            java.lang.String r0 = r0.getName()
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L28
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.STRING
            long r0 = r0.getId()
            return r0
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.CLASS
            java.lang.String r0 = r0.getName()
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3c
            jdk.jfr.internal.Type r0 = jdk.jfr.internal.Type.CLASS
            long r0 = r0.getId()
            return r0
            java.util.Collection r0 = jdk.jfr.internal.Type.getKnownTypes()
            java.util.Iterator r0 = r0.iterator()
            r10 = r0
            r0 = r10
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6b
            r0 = r10
            java.lang.Object r0 = r0.next()
            jdk.jfr.internal.Type r0 = (jdk.jfr.internal.Type) r0
            r11 = r0
            r0 = r11
            java.lang.String r0 = r0.getName()
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L68
            r0 = r11
            long r0 = r0.getId()
            return r0
            goto L45
            r0 = r8
            r1 = r0
            long r1 = r1.structTypeId
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 + r2
            r0.structTypeId = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.jfr.internal.MetadataHandler.nextTypeId(java.lang.String):long");
    }

    MetadataHandler() {
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/MetadataHandler$TypeElement.class */
    static class TypeElement {
        List<FieldElement> fields = new ArrayList();
        String name;
        String label;
        String description;
        String category;
        String superType;
        String period;
        boolean thread;
        boolean startTime;
        boolean stackTrace;
        boolean cutoff;
        boolean isEvent;
        boolean experimental;
        boolean valueType;

        TypeElement() {
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/MetadataHandler$FieldElement.class */
    static class FieldElement {
        TypeElement referenceType;
        String name;
        String label;
        String description;
        String contentType;
        String typeName;
        String transition;
        String relation;
        boolean struct;
        boolean array;
        boolean experimental;
        boolean unsigned;

        FieldElement() {
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/MetadataHandler$XmlType.class */
    static class XmlType {
        String name;
        String javaType;
        String contentType;
        boolean unsigned;

        XmlType() {
        }
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        switch (str3) {
            case "XmlType":
                XmlType xmlType = new XmlType();
                xmlType.name = attributes.getValue("name");
                xmlType.javaType = attributes.getValue("javaType");
                xmlType.contentType = attributes.getValue("contentType");
                xmlType.unsigned = Boolean.valueOf(attributes.getValue("unsigned")).booleanValue();
                this.xmlTypes.put(xmlType.name, xmlType);
                break;
            case "Type":
            case "Event":
                this.currentType = new TypeElement();
                this.currentType.name = attributes.getValue("name");
                this.currentType.label = attributes.getValue("label");
                this.currentType.description = attributes.getValue("description");
                this.currentType.category = attributes.getValue("category");
                this.currentType.thread = getBoolean(attributes, "thread", false);
                this.currentType.stackTrace = getBoolean(attributes, "stackTrace", false);
                this.currentType.startTime = getBoolean(attributes, "startTime", true);
                this.currentType.period = attributes.getValue("period");
                this.currentType.cutoff = getBoolean(attributes, Cutoff.NAME, false);
                this.currentType.experimental = getBoolean(attributes, "experimental", false);
                this.currentType.isEvent = str3.equals("Event");
                break;
            case "Field":
                this.currentField = new FieldElement();
                this.currentField.struct = getBoolean(attributes, "struct", false);
                this.currentField.array = getBoolean(attributes, ControllerParameter.PARAM_CLASS_ARRAY, false);
                this.currentField.name = attributes.getValue("name");
                this.currentField.label = attributes.getValue("label");
                this.currentField.typeName = attributes.getValue("type");
                this.currentField.description = attributes.getValue("description");
                this.currentField.experimental = getBoolean(attributes, "experimental", false);
                this.currentField.contentType = attributes.getValue("contentType");
                this.currentField.relation = attributes.getValue("relation");
                this.currentField.transition = attributes.getValue("transition");
                break;
            case "XmlContentType":
                this.xmlContentTypes.put(attributes.getValue("name"), createAnnotationElements(attributes.getValue("annotation")));
                break;
            case "Relation":
                this.relations.add(attributes.getValue("name"));
                break;
        }
    }

    private List<AnnotationElement> createAnnotationElements(String str) throws InternalError {
        String[] strArrSplit = str.split(",");
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArrSplit) {
            String strTrim = str2.trim();
            int iIndexOf = strTrim.indexOf("(");
            if (iIndexOf == -1) {
                arrayList.add(new AnnotationElement(createAnnotationClass(strTrim)));
            } else {
                int iLastIndexOf = strTrim.lastIndexOf(")");
                if (iLastIndexOf == -1) {
                    throw new InternalError("Expected closing parenthesis for 'XMLContentType'");
                }
                arrayList.add(new AnnotationElement(createAnnotationClass(strTrim.substring(0, iIndexOf)), strTrim.substring(iIndexOf + 1, iLastIndexOf)));
            }
        }
        return arrayList;
    }

    private Class<? extends Annotation> createAnnotationClass(String str) {
        try {
            if (!str.startsWith("jdk.jfr.")) {
                throw new IllegalStateException("Incorrect type " + str + ". Annotation class must be located in jdk.jfr package.");
            }
            return Class.forName(str, true, null);
        } catch (ClassNotFoundException e2) {
            throw new IllegalStateException(e2);
        }
    }

    private boolean getBoolean(Attributes attributes, String str, boolean z2) {
        String value = attributes.getValue(str);
        return value == null ? z2 : Boolean.valueOf(value).booleanValue();
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) {
        switch (str3) {
            case "Type":
            case "Event":
                this.types.put(this.currentType.name, this.currentType);
                this.currentType = null;
                break;
            case "Field":
                this.currentType.fields.add(this.currentField);
                this.currentField = null;
                break;
        }
    }

    public static List<Type> createTypes() throws IOException {
        SAXParserImpl sAXParserImpl = new SAXParserImpl();
        MetadataHandler metadataHandler = new MetadataHandler();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(SecuritySupport.getResourceAsStream("/jdk/jfr/internal/types/metadata.xml"));
        Throwable th = null;
        try {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, (Supplier<String>) () -> {
                return "Parsing metadata.xml";
            });
            try {
                sAXParserImpl.parse(bufferedInputStream, metadataHandler);
                List<Type> listBuildTypes = metadataHandler.buildTypes();
                if (bufferedInputStream != null) {
                    if (0 != 0) {
                        try {
                            bufferedInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        bufferedInputStream.close();
                    }
                }
                return listBuildTypes;
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new IOException(e2);
            }
        } catch (Throwable th3) {
            if (bufferedInputStream != null) {
                if (0 != 0) {
                    try {
                        bufferedInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    bufferedInputStream.close();
                }
            }
            throw th3;
        }
    }

    private List<Type> buildTypes() throws SecurityException {
        removeXMLConvenience();
        Map<String, Type> mapBuildTypeMap = buildTypeMap();
        addFields(mapBuildTypeMap, buildRelationMap(mapBuildTypeMap));
        return trimTypes(mapBuildTypeMap);
    }

    private Map<String, AnnotationElement> buildRelationMap(Map<String, Type> map) {
        HashMap map2 = new HashMap();
        for (String str : this.relations) {
            String str2 = Type.TYPES_PREFIX + str;
            String str3 = Type.SUPER_TYPE_ANNOTATION;
            long j2 = this.eventTypeId;
            this.eventTypeId = j2 + 1;
            Type type = new Type(str2, str3, j2);
            type.setAnnotations(Collections.singletonList(new AnnotationElement(Relational.class)));
            map2.put(str, PrivateAccess.getInstance().newAnnotation(type, Collections.emptyList(), true));
            map.put(type.getName(), type);
        }
        return map2;
    }

    private List<Type> trimTypes(Map<String, Type> map) {
        ArrayList arrayList = new ArrayList(map.size());
        for (Type type : map.values()) {
            type.trimFields();
            arrayList.add(type);
        }
        return arrayList;
    }

    private void addFields(Map<String, Type> map, Map<String, AnnotationElement> map2) throws SecurityException {
        for (TypeElement typeElement : this.types.values()) {
            Type type = map.get(typeElement.name);
            if (typeElement.isEvent) {
                boolean z2 = typeElement.period != null;
                TypeLibrary.addImplicitFields(type, z2, typeElement.startTime && !z2, typeElement.thread, typeElement.stackTrace && !z2, typeElement.cutoff);
            }
            for (FieldElement fieldElement : typeElement.fields) {
                Type knownType = Type.getKnownType(fieldElement.typeName);
                if (knownType == null) {
                    knownType = (Type) Objects.requireNonNull(map.get(fieldElement.referenceType.name));
                }
                ArrayList arrayList = new ArrayList();
                if (fieldElement.unsigned) {
                    arrayList.add(new AnnotationElement(Unsigned.class));
                }
                if (fieldElement.contentType != null) {
                    arrayList.addAll((Collection) Objects.requireNonNull(this.xmlContentTypes.get(fieldElement.contentType)));
                }
                if (fieldElement.relation != null) {
                    arrayList.add(Objects.requireNonNull(map2.get(fieldElement.relation)));
                }
                if (fieldElement.label != null) {
                    arrayList.add(new AnnotationElement((Class<? extends Annotation>) Label.class, fieldElement.label));
                }
                if (fieldElement.experimental) {
                    arrayList.add(new AnnotationElement(Experimental.class));
                }
                if (fieldElement.description != null) {
                    arrayList.add(new AnnotationElement((Class<? extends Annotation>) Description.class, fieldElement.description));
                }
                if (Constants.ATTRNAME_FROM.equals(fieldElement.transition)) {
                    arrayList.add(new AnnotationElement(TransitionFrom.class));
                }
                if ("to".equals(fieldElement.transition)) {
                    arrayList.add(new AnnotationElement(TransitionTo.class));
                }
                type.add(PrivateAccess.getInstance().newValueDescriptor(fieldElement.name, knownType, arrayList, fieldElement.array ? 1 : 0, (fieldElement.struct || fieldElement.referenceType == null) ? false : true, null));
            }
        }
    }

    private Map<String, Type> buildTypeMap() {
        Type type;
        long jNextTypeId;
        HashMap map = new HashMap();
        for (Type type2 : Type.getKnownTypes()) {
            map.put(type2.getName(), type2);
        }
        for (TypeElement typeElement : this.types.values()) {
            ArrayList arrayList = new ArrayList();
            if (typeElement.category != null) {
                arrayList.add(new AnnotationElement((Class<? extends Annotation>) Category.class, buildCategoryArray(typeElement.category)));
            }
            if (typeElement.label != null) {
                arrayList.add(new AnnotationElement((Class<? extends Annotation>) Label.class, typeElement.label));
            }
            if (typeElement.description != null) {
                arrayList.add(new AnnotationElement((Class<? extends Annotation>) Description.class, typeElement.description));
            }
            if (typeElement.isEvent) {
                if (typeElement.period != null) {
                    arrayList.add(new AnnotationElement((Class<? extends Annotation>) Period.class, typeElement.period));
                } else {
                    if (typeElement.startTime) {
                        arrayList.add(new AnnotationElement((Class<? extends Annotation>) Threshold.class, "0 ns"));
                    }
                    if (typeElement.stackTrace) {
                        arrayList.add(new AnnotationElement((Class<? extends Annotation>) StackTrace.class, (Object) true));
                    }
                }
                if (typeElement.cutoff) {
                    arrayList.add(new AnnotationElement((Class<? extends Annotation>) Cutoff.class, "infinity"));
                }
            }
            if (typeElement.experimental) {
                arrayList.add(new AnnotationElement(Experimental.class));
            }
            if (typeElement.isEvent) {
                arrayList.add(new AnnotationElement((Class<? extends Annotation>) Enabled.class, (Object) false));
                String str = typeElement.name;
                long j2 = this.eventTypeId;
                this.eventTypeId = j2 + 1;
                type = new PlatformEventType(str, j2, false, true);
            } else {
                boolean z2 = typeElement.name.endsWith("StackFrame") || typeElement.valueType;
                String str2 = typeElement.name;
                if (z2) {
                    long j3 = this.eventTypeId;
                    jNextTypeId = j3;
                    this.eventTypeId = j3 + 1;
                } else {
                    jNextTypeId = nextTypeId(typeElement.name);
                }
                type = new Type(str2, null, jNextTypeId, false);
            }
            Type type3 = type;
            type3.setAnnotations(arrayList);
            map.put(typeElement.name, type3);
        }
        return map;
    }

    private String[] buildCategoryArray(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (char c2 : str.toCharArray()) {
            if (c2 == ',') {
                arrayList.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c2);
            }
        }
        arrayList.add(sb.toString().trim());
        return (String[]) arrayList.toArray(new String[0]);
    }

    private void removeXMLConvenience() {
        for (TypeElement typeElement : this.types.values()) {
            XmlType xmlType = this.xmlTypes.get(typeElement.name);
            if (xmlType != null && xmlType.javaType != null) {
                typeElement.name = xmlType.javaType;
            } else if (typeElement.isEvent) {
                typeElement.name = Type.EVENT_NAME_PREFIX + typeElement.name;
            } else {
                typeElement.name = Type.TYPES_PREFIX + typeElement.name;
            }
        }
        Iterator<TypeElement> it = this.types.values().iterator();
        while (it.hasNext()) {
            for (FieldElement fieldElement : it.next().fields) {
                fieldElement.referenceType = this.types.get(fieldElement.typeName);
                XmlType xmlType2 = this.xmlTypes.get(fieldElement.typeName);
                if (xmlType2 != null) {
                    if (xmlType2.javaType != null) {
                        fieldElement.typeName = xmlType2.javaType;
                    }
                    if (xmlType2.contentType != null) {
                        fieldElement.contentType = xmlType2.contentType;
                    }
                    if (xmlType2.unsigned) {
                        fieldElement.unsigned = true;
                    }
                }
                if (fieldElement.struct && fieldElement.referenceType != null) {
                    fieldElement.referenceType.valueType = true;
                }
            }
        }
    }
}
