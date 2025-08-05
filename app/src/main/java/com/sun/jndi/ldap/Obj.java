package com.sun.jndi.ldap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.spi.DirStateFactory;
import javax.naming.spi.DirectoryManager;
import javax.swing.JSplitPane;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/* loaded from: rt.jar:com/sun/jndi/ldap/Obj.class */
final class Obj {
    static final int OBJECT_CLASS = 0;
    static final int SERIALIZED_DATA = 1;
    static final int CLASSNAME = 2;
    static final int FACTORY = 3;
    static final int CODEBASE = 4;
    static final int REF_ADDR = 5;
    static final int TYPENAME = 6;

    @Deprecated
    private static final int REMOTE_LOC = 7;
    static final int STRUCTURAL = 0;
    static final int BASE_OBJECT = 1;
    static final int REF_OBJECT = 2;
    static final int SER_OBJECT = 3;
    static final int MAR_OBJECT = 4;
    static VersionHelper helper = VersionHelper.getVersionHelper();
    static final String[] JAVA_ATTRIBUTES = {"objectClass", "javaSerializedData", "javaClassName", "javaFactory", "javaCodeBase", "javaReferenceAddress", "javaClassNames", "javaRemoteLocation"};
    static final String[] JAVA_OBJECT_CLASSES = {"javaContainer", "javaObject", "javaNamingReference", "javaSerializedObject", "javaMarshalledObject"};
    static final String[] JAVA_OBJECT_CLASSES_LOWER = {"javacontainer", "javaobject", "javanamingreference", "javaserializedobject", "javamarshalledobject"};

    private Obj() {
    }

    private static Attributes encodeObject(char c2, Object obj, Attributes attributes, Attribute attribute, boolean z2) throws NamingException {
        Attribute attributeCreateTypeNameAttr;
        if (attribute.size() == 0 || (attribute.size() == 1 && attribute.contains(JSplitPane.TOP))) {
            attribute.add(JAVA_OBJECT_CLASSES[0]);
        }
        if (obj instanceof Referenceable) {
            attribute.add(JAVA_OBJECT_CLASSES[1]);
            attribute.add(JAVA_OBJECT_CLASSES[2]);
            if (!z2) {
                attributes = (Attributes) attributes.clone();
            }
            attributes.put(attribute);
            return encodeReference(c2, ((Referenceable) obj).getReference(), attributes, obj);
        }
        if (obj instanceof Reference) {
            attribute.add(JAVA_OBJECT_CLASSES[1]);
            attribute.add(JAVA_OBJECT_CLASSES[2]);
            if (!z2) {
                attributes = (Attributes) attributes.clone();
            }
            attributes.put(attribute);
            return encodeReference(c2, (Reference) obj, attributes, null);
        }
        if (obj instanceof Serializable) {
            attribute.add(JAVA_OBJECT_CLASSES[1]);
            if (!attribute.contains(JAVA_OBJECT_CLASSES[4]) && !attribute.contains(JAVA_OBJECT_CLASSES_LOWER[4])) {
                attribute.add(JAVA_OBJECT_CLASSES[3]);
            }
            if (!z2) {
                attributes = (Attributes) attributes.clone();
            }
            attributes.put(attribute);
            attributes.put(new BasicAttribute(JAVA_ATTRIBUTES[1], serializeObject(obj)));
            if (attributes.get(JAVA_ATTRIBUTES[2]) == null) {
                attributes.put(JAVA_ATTRIBUTES[2], obj.getClass().getName());
            }
            if (attributes.get(JAVA_ATTRIBUTES[6]) == null && (attributeCreateTypeNameAttr = LdapCtxFactory.createTypeNameAttr(obj.getClass())) != null) {
                attributes.put(attributeCreateTypeNameAttr);
            }
        } else if (!(obj instanceof DirContext)) {
            throw new IllegalArgumentException("can only bind Referenceable, Serializable, DirContext");
        }
        return attributes;
    }

    private static String[] getCodebases(Attribute attribute) throws NamingException {
        if (attribute == null) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer((String) attribute.get());
        Vector vector = new Vector(10);
        while (stringTokenizer.hasMoreTokens()) {
            vector.addElement(stringTokenizer.nextToken());
        }
        String[] strArr = new String[vector.size()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) vector.elementAt(i2);
        }
        return strArr;
    }

    static Object decodeObject(Attributes attributes) throws NamingException {
        String[] codebases = getCodebases(attributes.get(JAVA_ATTRIBUTES[4]));
        try {
            Attribute attribute = attributes.get(JAVA_ATTRIBUTES[1]);
            if (attribute != null) {
                if (!VersionHelper12.isSerialDataAllowed()) {
                    throw new NamingException("Object deserialization is not allowed");
                }
                return deserializeObject((byte[]) attribute.get(), helper.getURLClassLoader(codebases));
            }
            Attribute attribute2 = attributes.get(JAVA_ATTRIBUTES[7]);
            if (attribute2 != null) {
                return decodeRmiObject((String) attributes.get(JAVA_ATTRIBUTES[2]).get(), (String) attribute2.get(), codebases);
            }
            Attribute attribute3 = attributes.get(JAVA_ATTRIBUTES[0]);
            if (attribute3 != null) {
                if (attribute3.contains(JAVA_OBJECT_CLASSES[2]) || attribute3.contains(JAVA_OBJECT_CLASSES_LOWER[2])) {
                    return decodeReference(attributes, codebases);
                }
                return null;
            }
            return null;
        } catch (IOException e2) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e2);
            throw namingException;
        }
    }

    private static Attributes encodeReference(char c2, Reference reference, Attributes attributes, Object obj) throws NamingException {
        Attribute attributeCreateTypeNameAttr;
        if (reference == null) {
            return attributes;
        }
        String className = reference.getClassName();
        if (className != null) {
            attributes.put(new BasicAttribute(JAVA_ATTRIBUTES[2], className));
        }
        String factoryClassName = reference.getFactoryClassName();
        if (factoryClassName != null) {
            attributes.put(new BasicAttribute(JAVA_ATTRIBUTES[3], factoryClassName));
        }
        String factoryClassLocation = reference.getFactoryClassLocation();
        if (factoryClassLocation != null) {
            attributes.put(new BasicAttribute(JAVA_ATTRIBUTES[4], factoryClassLocation));
        }
        if (obj != null && attributes.get(JAVA_ATTRIBUTES[6]) != null && (attributeCreateTypeNameAttr = LdapCtxFactory.createTypeNameAttr(obj.getClass())) != null) {
            attributes.put(attributeCreateTypeNameAttr);
        }
        int size = reference.size();
        if (size > 0) {
            BasicAttribute basicAttribute = new BasicAttribute(JAVA_ATTRIBUTES[5]);
            BASE64Encoder bASE64Encoder = null;
            for (int i2 = 0; i2 < size; i2++) {
                RefAddr refAddr = reference.get(i2);
                if (refAddr instanceof StringRefAddr) {
                    basicAttribute.add("" + c2 + i2 + c2 + refAddr.getType() + c2 + refAddr.getContent());
                } else {
                    if (bASE64Encoder == null) {
                        bASE64Encoder = new BASE64Encoder();
                    }
                    basicAttribute.add("" + c2 + i2 + c2 + refAddr.getType() + c2 + c2 + bASE64Encoder.encodeBuffer(serializeObject(refAddr)));
                }
            }
            attributes.put(basicAttribute);
        }
        return attributes;
    }

    private static Object decodeRmiObject(String str, String str2, String[] strArr) throws NamingException {
        return new Reference(str, new StringRefAddr("URL", str2));
    }

    private static Reference decodeReference(Attributes attributes, String[] strArr) throws IOException, NamingException {
        String str = null;
        Attribute attribute = attributes.get(JAVA_ATTRIBUTES[2]);
        if (attribute != null) {
            String str2 = (String) attribute.get();
            Attribute attribute2 = attributes.get(JAVA_ATTRIBUTES[3]);
            if (attribute2 != null) {
                str = (String) attribute2.get();
            }
            Reference reference = new Reference(str2, str, strArr != null ? strArr[0] : null);
            Attribute attribute3 = attributes.get(JAVA_ATTRIBUTES[5]);
            if (attribute3 != null) {
                BASE64Decoder bASE64Decoder = null;
                ClassLoader uRLClassLoader = helper.getURLClassLoader(strArr);
                Vector vector = new Vector();
                vector.setSize(attribute3.size());
                NamingEnumeration<?> all = attribute3.getAll();
                while (all.hasMore()) {
                    String str3 = (String) all.next();
                    if (str3.length() == 0) {
                        throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - empty attribute value");
                    }
                    char cCharAt = str3.charAt(0);
                    int iIndexOf = str3.indexOf(cCharAt, 1);
                    if (iIndexOf < 0) {
                        throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - separator '" + cCharAt + "'not found");
                    }
                    String strSubstring = str3.substring(1, iIndexOf);
                    if (strSubstring == null) {
                        throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - empty RefAddr position");
                    }
                    try {
                        int i2 = Integer.parseInt(strSubstring);
                        int i3 = iIndexOf + 1;
                        int iIndexOf2 = str3.indexOf(cCharAt, i3);
                        if (iIndexOf2 < 0) {
                            throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - RefAddr type not found");
                        }
                        String strSubstring2 = str3.substring(i3, iIndexOf2);
                        if (strSubstring2 == null) {
                            throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - empty RefAddr type");
                        }
                        int i4 = iIndexOf2 + 1;
                        if (i4 == str3.length()) {
                            vector.setElementAt(new StringRefAddr(strSubstring2, null), i2);
                        } else if (str3.charAt(i4) == cCharAt) {
                            if (!VersionHelper12.isSerialDataAllowed()) {
                                throw new NamingException("Object deserialization is not allowed");
                            }
                            int i5 = i4 + 1;
                            if (bASE64Decoder == null) {
                                bASE64Decoder = new BASE64Decoder();
                            }
                            vector.setElementAt((RefAddr) deserializeObject(bASE64Decoder.decodeBuffer(str3.substring(i5)), uRLClassLoader), i2);
                        } else {
                            vector.setElementAt(new StringRefAddr(strSubstring2, str3.substring(i4)), i2);
                        }
                    } catch (NumberFormatException e2) {
                        throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - RefAddr position not an integer");
                    }
                }
                for (int i6 = 0; i6 < vector.size(); i6++) {
                    reference.add((RefAddr) vector.elementAt(i6));
                }
            }
            return reference;
        }
        throw new InvalidAttributesException(JAVA_ATTRIBUTES[2] + " attribute is required");
    }

    private static byte[] serializeObject(Object obj) throws NamingException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            Throwable th = null;
            try {
                objectOutputStream.writeObject(obj);
                if (objectOutputStream != null) {
                    if (0 != 0) {
                        try {
                            objectOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        objectOutputStream.close();
                    }
                }
                return byteArrayOutputStream.toByteArray();
            } finally {
            }
        } catch (IOException e2) {
            NamingException namingException = new NamingException();
            namingException.setRootCause(e2);
            throw namingException;
        }
    }

    private static Object deserializeObject(byte[] bArr, ClassLoader classLoader) throws NamingException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                ObjectInput objectInputStream = classLoader == null ? new ObjectInputStream(byteArrayInputStream) : new LoaderInputStream(byteArrayInputStream, classLoader);
                Throwable th = null;
                try {
                    try {
                        Object object = objectInputStream.readObject();
                        if (objectInputStream != null) {
                            if (0 != 0) {
                                try {
                                    objectInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                objectInputStream.close();
                            }
                        }
                        return object;
                    } catch (Throwable th3) {
                        if (objectInputStream != null) {
                            if (th != null) {
                                try {
                                    objectInputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                objectInputStream.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            } catch (ClassNotFoundException e2) {
                NamingException namingException = new NamingException();
                namingException.setRootCause(e2);
                throw namingException;
            }
        } catch (IOException e3) {
            NamingException namingException2 = new NamingException();
            namingException2.setRootCause(e3);
            throw namingException2;
        }
    }

    static Attributes determineBindAttrs(char c2, Object obj, Attributes attributes, boolean z2, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        Attribute basicAttribute;
        DirStateFactory.Result stateToBind = DirectoryManager.getStateToBind(obj, name, context, hashtable, attributes);
        Object object = stateToBind.getObject();
        Attributes attributes2 = stateToBind.getAttributes();
        if (object == null) {
            return attributes2;
        }
        if (attributes2 == null && (object instanceof DirContext)) {
            z2 = true;
            attributes2 = ((DirContext) object).getAttributes("");
        }
        if (attributes2 == null || attributes2.size() == 0) {
            attributes2 = new BasicAttributes(true);
            z2 = true;
            basicAttribute = new BasicAttribute("objectClass", JSplitPane.TOP);
        } else {
            basicAttribute = attributes2.get("objectClass");
            if (basicAttribute == null && !attributes2.isCaseIgnored()) {
                basicAttribute = attributes2.get("objectclass");
            }
            if (basicAttribute == null) {
                basicAttribute = new BasicAttribute("objectClass", JSplitPane.TOP);
            } else if (0 != 0 || !z2) {
                basicAttribute = (Attribute) basicAttribute.clone();
            }
        }
        return encodeObject(c2, object, attributes2, basicAttribute, z2);
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/Obj$LoaderInputStream.class */
    private static final class LoaderInputStream extends ObjectInputStream {
        private ClassLoader classLoader;

        LoaderInputStream(InputStream inputStream, ClassLoader classLoader) throws IOException {
            super(inputStream);
            this.classLoader = classLoader;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
            try {
                return this.classLoader.loadClass(objectStreamClass.getName());
            } catch (ClassNotFoundException e2) {
                return super.resolveClass(objectStreamClass);
            }
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveProxyClass(String[] strArr) throws ClassNotFoundException, IOException {
            ClassLoader classLoader;
            ClassLoader classLoader2 = null;
            boolean z2 = false;
            Class[] clsArr = new Class[strArr.length];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                Class<?> cls = Class.forName(strArr[i2], false, this.classLoader);
                if ((cls.getModifiers() & 1) == 0) {
                    if (z2) {
                        if (classLoader2 != cls.getClassLoader()) {
                            throw new IllegalAccessError("conflicting non-public interface class loaders");
                        }
                    } else {
                        classLoader2 = cls.getClassLoader();
                        z2 = true;
                    }
                }
                clsArr[i2] = cls;
            }
            if (z2) {
                classLoader = classLoader2;
            } else {
                try {
                    classLoader = this.classLoader;
                } catch (IllegalArgumentException e2) {
                    throw new ClassNotFoundException(null, e2);
                }
            }
            return Proxy.getProxyClass(classLoader, clsArr);
        }
    }
}
