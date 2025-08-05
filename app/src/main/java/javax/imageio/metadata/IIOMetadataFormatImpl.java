package javax.imageio.metadata;

import com.sun.imageio.plugins.common.StandardMetadataFormat;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.imageio.ImageTypeSpecifier;

/* loaded from: rt.jar:javax/imageio/metadata/IIOMetadataFormatImpl.class */
public abstract class IIOMetadataFormatImpl implements IIOMetadataFormat {
    public static final String standardMetadataFormatName = "javax_imageio_1.0";
    private static IIOMetadataFormat standardFormat = null;
    private String rootName;
    private String resourceBaseName = getClass().getName() + "Resources";
    private HashMap elementMap = new HashMap();

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public abstract boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier);

    /* loaded from: rt.jar:javax/imageio/metadata/IIOMetadataFormatImpl$Element.class */
    class Element {
        String elementName;
        int childPolicy;
        int minChildren = 0;
        int maxChildren = 0;
        List childList = new ArrayList();
        List parentList = new ArrayList();
        List attrList = new ArrayList();
        Map attrMap = new HashMap();
        ObjectValue objectValue;

        Element() {
        }
    }

    /* loaded from: rt.jar:javax/imageio/metadata/IIOMetadataFormatImpl$Attribute.class */
    class Attribute {
        String attrName;
        int dataType;
        boolean required;
        List enumeratedValues;
        String minValue;
        String maxValue;
        int listMinLength;
        int listMaxLength;
        int valueType = 1;
        String defaultValue = null;

        Attribute() {
        }
    }

    /* loaded from: rt.jar:javax/imageio/metadata/IIOMetadataFormatImpl$ObjectValue.class */
    class ObjectValue {
        int valueType = 0;
        Class classType = null;
        Object defaultValue = null;
        List enumeratedValues = null;
        Comparable minValue = null;
        Comparable maxValue = null;
        int arrayMinLength = 0;
        int arrayMaxLength = 0;

        ObjectValue() {
        }
    }

    public IIOMetadataFormatImpl(String str, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("rootName == null!");
        }
        if (i2 < 0 || i2 > 5 || i2 == 5) {
            throw new IllegalArgumentException("Invalid value for childPolicy!");
        }
        this.rootName = str;
        Element element = new Element();
        element.elementName = str;
        element.childPolicy = i2;
        this.elementMap.put(str, element);
    }

    public IIOMetadataFormatImpl(String str, int i2, int i3) {
        if (str == null) {
            throw new IllegalArgumentException("rootName == null!");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("minChildren < 0!");
        }
        if (i2 > i3) {
            throw new IllegalArgumentException("minChildren > maxChildren!");
        }
        Element element = new Element();
        element.elementName = str;
        element.childPolicy = 5;
        element.minChildren = i2;
        element.maxChildren = i3;
        this.rootName = str;
        this.elementMap.put(str, element);
    }

    protected void setResourceBaseName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("resourceBaseName == null!");
        }
        this.resourceBaseName = str;
    }

    protected String getResourceBaseName() {
        return this.resourceBaseName;
    }

    private Element getElement(String str, boolean z2) {
        if (z2 && str == null) {
            throw new IllegalArgumentException("element name is null!");
        }
        Element element = (Element) this.elementMap.get(str);
        if (z2 && element == null) {
            throw new IllegalArgumentException("No such element: " + str);
        }
        return element;
    }

    private Element getElement(String str) {
        return getElement(str, true);
    }

    private Attribute getAttribute(String str, String str2) {
        Attribute attribute = (Attribute) getElement(str).attrMap.get(str2);
        if (attribute == null) {
            throw new IllegalArgumentException("No such attribute \"" + str2 + "\"!");
        }
        return attribute;
    }

    protected void addElement(String str, String str2, int i2) {
        Element element = getElement(str2);
        if (i2 < 0 || i2 > 5 || i2 == 5) {
            throw new IllegalArgumentException("Invalid value for childPolicy!");
        }
        Element element2 = new Element();
        element2.elementName = str;
        element2.childPolicy = i2;
        element.childList.add(str);
        element2.parentList.add(str2);
        this.elementMap.put(str, element2);
    }

    protected void addElement(String str, String str2, int i2, int i3) {
        Element element = getElement(str2);
        if (i2 < 0) {
            throw new IllegalArgumentException("minChildren < 0!");
        }
        if (i2 > i3) {
            throw new IllegalArgumentException("minChildren > maxChildren!");
        }
        Element element2 = new Element();
        element2.elementName = str;
        element2.childPolicy = 5;
        element2.minChildren = i2;
        element2.maxChildren = i3;
        element.childList.add(str);
        element2.parentList.add(str2);
        this.elementMap.put(str, element2);
    }

    protected void addChildElement(String str, String str2) {
        Element element = getElement(str2);
        Element element2 = getElement(str);
        element.childList.add(str);
        element2.parentList.add(str2);
    }

    protected void removeElement(String str) {
        Element element = getElement(str, false);
        if (element != null) {
            Iterator it = element.parentList.iterator();
            while (it.hasNext()) {
                Element element2 = getElement((String) it.next(), false);
                if (element2 != null) {
                    element2.childList.remove(str);
                }
            }
            this.elementMap.remove(str);
        }
    }

    protected void addAttribute(String str, String str2, int i2, boolean z2, String str3) {
        Element element = getElement(str);
        if (str2 == null) {
            throw new IllegalArgumentException("attrName == null!");
        }
        if (i2 < 0 || i2 > 4) {
            throw new IllegalArgumentException("Invalid value for dataType!");
        }
        Attribute attribute = new Attribute();
        attribute.attrName = str2;
        attribute.valueType = 1;
        attribute.dataType = i2;
        attribute.required = z2;
        attribute.defaultValue = str3;
        element.attrList.add(str2);
        element.attrMap.put(str2, attribute);
    }

    protected void addAttribute(String str, String str2, int i2, boolean z2, String str3, List<String> list) {
        Element element = getElement(str);
        if (str2 == null) {
            throw new IllegalArgumentException("attrName == null!");
        }
        if (i2 < 0 || i2 > 4) {
            throw new IllegalArgumentException("Invalid value for dataType!");
        }
        if (list == null) {
            throw new IllegalArgumentException("enumeratedValues == null!");
        }
        if (list.size() == 0) {
            throw new IllegalArgumentException("enumeratedValues is empty!");
        }
        for (String str4 : list) {
            if (str4 == null) {
                throw new IllegalArgumentException("enumeratedValues contains a null!");
            }
            if (!(str4 instanceof String)) {
                throw new IllegalArgumentException("enumeratedValues contains a non-String value!");
            }
        }
        Attribute attribute = new Attribute();
        attribute.attrName = str2;
        attribute.valueType = 16;
        attribute.dataType = i2;
        attribute.required = z2;
        attribute.defaultValue = str3;
        attribute.enumeratedValues = list;
        element.attrList.add(str2);
        element.attrMap.put(str2, attribute);
    }

    protected void addAttribute(String str, String str2, int i2, boolean z2, String str3, String str4, String str5, boolean z3, boolean z4) {
        Element element = getElement(str);
        if (str2 == null) {
            throw new IllegalArgumentException("attrName == null!");
        }
        if (i2 < 0 || i2 > 4) {
            throw new IllegalArgumentException("Invalid value for dataType!");
        }
        Attribute attribute = new Attribute();
        attribute.attrName = str2;
        attribute.valueType = 2;
        if (z3) {
            attribute.valueType |= 4;
        }
        if (z4) {
            attribute.valueType |= 8;
        }
        attribute.dataType = i2;
        attribute.required = z2;
        attribute.defaultValue = str3;
        attribute.minValue = str4;
        attribute.maxValue = str5;
        element.attrList.add(str2);
        element.attrMap.put(str2, attribute);
    }

    protected void addAttribute(String str, String str2, int i2, boolean z2, int i3, int i4) {
        Element element = getElement(str);
        if (str2 == null) {
            throw new IllegalArgumentException("attrName == null!");
        }
        if (i2 < 0 || i2 > 4) {
            throw new IllegalArgumentException("Invalid value for dataType!");
        }
        if (i3 < 0 || i3 > i4) {
            throw new IllegalArgumentException("Invalid list bounds!");
        }
        Attribute attribute = new Attribute();
        attribute.attrName = str2;
        attribute.valueType = 32;
        attribute.dataType = i2;
        attribute.required = z2;
        attribute.listMinLength = i3;
        attribute.listMaxLength = i4;
        element.attrList.add(str2);
        element.attrMap.put(str2, attribute);
    }

    protected void addBooleanAttribute(String str, String str2, boolean z2, boolean z3) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("TRUE");
        arrayList.add("FALSE");
        String str3 = null;
        if (z2) {
            str3 = z3 ? "TRUE" : "FALSE";
        }
        addAttribute(str, str2, 1, true, str3, (List<String>) arrayList);
    }

    protected void removeAttribute(String str, String str2) {
        Element element = getElement(str);
        element.attrList.remove(str2);
        element.attrMap.remove(str2);
    }

    protected <T> void addObjectValue(String str, Class<T> cls, boolean z2, T t2) {
        Element element = getElement(str);
        ObjectValue objectValue = new ObjectValue();
        objectValue.valueType = 1;
        objectValue.classType = cls;
        objectValue.defaultValue = t2;
        element.objectValue = objectValue;
    }

    protected <T> void addObjectValue(String str, Class<T> cls, boolean z2, T t2, List<? extends T> list) {
        Element element = getElement(str);
        if (list == null) {
            throw new IllegalArgumentException("enumeratedValues == null!");
        }
        if (list.size() == 0) {
            throw new IllegalArgumentException("enumeratedValues is empty!");
        }
        for (T t3 : list) {
            if (t3 == null) {
                throw new IllegalArgumentException("enumeratedValues contains a null!");
            }
            if (!cls.isInstance(t3)) {
                throw new IllegalArgumentException("enumeratedValues contains a value not of class classType!");
            }
        }
        ObjectValue objectValue = new ObjectValue();
        objectValue.valueType = 16;
        objectValue.classType = cls;
        objectValue.defaultValue = t2;
        objectValue.enumeratedValues = list;
        element.objectValue = objectValue;
    }

    protected <T extends Comparable<? super T>> void addObjectValue(String str, Class<T> cls, T t2, Comparable<? super T> comparable, Comparable<? super T> comparable2, boolean z2, boolean z3) {
        Element element = getElement(str);
        ObjectValue objectValue = new ObjectValue();
        objectValue.valueType = 2;
        if (z2) {
            objectValue.valueType |= 4;
        }
        if (z3) {
            objectValue.valueType |= 8;
        }
        objectValue.classType = cls;
        objectValue.defaultValue = t2;
        objectValue.minValue = comparable;
        objectValue.maxValue = comparable2;
        element.objectValue = objectValue;
    }

    protected void addObjectValue(String str, Class<?> cls, int i2, int i3) {
        Element element = getElement(str);
        ObjectValue objectValue = new ObjectValue();
        objectValue.valueType = 32;
        objectValue.classType = cls;
        objectValue.arrayMinLength = i2;
        objectValue.arrayMaxLength = i3;
        element.objectValue = objectValue;
    }

    protected void removeObjectValue(String str) {
        getElement(str).objectValue = null;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getRootName() {
        return this.rootName;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getElementMinChildren(String str) {
        Element element = getElement(str);
        if (element.childPolicy != 5) {
            throw new IllegalArgumentException("Child policy not CHILD_POLICY_REPEAT!");
        }
        return element.minChildren;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getElementMaxChildren(String str) {
        Element element = getElement(str);
        if (element.childPolicy != 5) {
            throw new IllegalArgumentException("Child policy not CHILD_POLICY_REPEAT!");
        }
        return element.maxChildren;
    }

    private String getResource(String str, Locale locale) {
        ResourceBundle bundle;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            bundle = ResourceBundle.getBundle(this.resourceBaseName, locale, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.metadata.IIOMetadataFormatImpl.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            }));
        } catch (MissingResourceException e2) {
            try {
                bundle = ResourceBundle.getBundle(this.resourceBaseName, locale);
            } catch (MissingResourceException e3) {
                return null;
            }
        }
        try {
            return bundle.getString(str);
        } catch (MissingResourceException e4) {
            return null;
        }
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getElementDescription(String str, Locale locale) {
        getElement(str);
        return getResource(str, locale);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getChildPolicy(String str) {
        return getElement(str).childPolicy;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String[] getChildNames(String str) {
        Element element = getElement(str);
        if (element.childPolicy == 0) {
            return null;
        }
        return (String[]) element.childList.toArray(new String[0]);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String[] getAttributeNames(String str) {
        List list = getElement(str).attrList;
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getAttributeValueType(String str, String str2) {
        return getAttribute(str, str2).valueType;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getAttributeDataType(String str, String str2) {
        return getAttribute(str, str2).dataType;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public boolean isAttributeRequired(String str, String str2) {
        return getAttribute(str, str2).required;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getAttributeDefaultValue(String str, String str2) {
        return getAttribute(str, str2).defaultValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String[] getAttributeEnumerations(String str, String str2) {
        Attribute attribute = getAttribute(str, str2);
        if (attribute.valueType != 16) {
            throw new IllegalArgumentException("Attribute not an enumeration!");
        }
        List list = attribute.enumeratedValues;
        list.iterator();
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getAttributeMinValue(String str, String str2) {
        Attribute attribute = getAttribute(str, str2);
        if (attribute.valueType != 2 && attribute.valueType != 6 && attribute.valueType != 10 && attribute.valueType != 14) {
            throw new IllegalArgumentException("Attribute not a range!");
        }
        return attribute.minValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getAttributeMaxValue(String str, String str2) {
        Attribute attribute = getAttribute(str, str2);
        if (attribute.valueType != 2 && attribute.valueType != 6 && attribute.valueType != 10 && attribute.valueType != 14) {
            throw new IllegalArgumentException("Attribute not a range!");
        }
        return attribute.maxValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getAttributeListMinLength(String str, String str2) {
        Attribute attribute = getAttribute(str, str2);
        if (attribute.valueType != 32) {
            throw new IllegalArgumentException("Attribute not a list!");
        }
        return attribute.listMinLength;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getAttributeListMaxLength(String str, String str2) {
        Attribute attribute = getAttribute(str, str2);
        if (attribute.valueType != 32) {
            throw new IllegalArgumentException("Attribute not a list!");
        }
        return attribute.listMaxLength;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public String getAttributeDescription(String str, String str2, Locale locale) {
        Element element = getElement(str);
        if (str2 == null) {
            throw new IllegalArgumentException("attrName == null!");
        }
        if (((Attribute) element.attrMap.get(str2)) == null) {
            throw new IllegalArgumentException("No such attribute!");
        }
        return getResource(str + "/" + str2, locale);
    }

    private ObjectValue getObjectValue(String str) {
        ObjectValue objectValue = getElement(str).objectValue;
        if (objectValue == null) {
            throw new IllegalArgumentException("No object within element " + str + "!");
        }
        return objectValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getObjectValueType(String str) {
        ObjectValue objectValue = getElement(str).objectValue;
        if (objectValue == null) {
            return 0;
        }
        return objectValue.valueType;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public Class<?> getObjectClass(String str) {
        return getObjectValue(str).classType;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public Object getObjectDefaultValue(String str) {
        return getObjectValue(str).defaultValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public Object[] getObjectEnumerations(String str) {
        ObjectValue objectValue = getObjectValue(str);
        if (objectValue.valueType != 16) {
            throw new IllegalArgumentException("Not an enumeration!");
        }
        List list = objectValue.enumeratedValues;
        return list.toArray(new Object[list.size()]);
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public Comparable<?> getObjectMinValue(String str) {
        ObjectValue objectValue = getObjectValue(str);
        if ((objectValue.valueType & 2) != 2) {
            throw new IllegalArgumentException("Not a range!");
        }
        return objectValue.minValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public Comparable<?> getObjectMaxValue(String str) {
        ObjectValue objectValue = getObjectValue(str);
        if ((objectValue.valueType & 2) != 2) {
            throw new IllegalArgumentException("Not a range!");
        }
        return objectValue.maxValue;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getObjectArrayMinLength(String str) {
        ObjectValue objectValue = getObjectValue(str);
        if (objectValue.valueType != 32) {
            throw new IllegalArgumentException("Not a list!");
        }
        return objectValue.arrayMinLength;
    }

    @Override // javax.imageio.metadata.IIOMetadataFormat
    public int getObjectArrayMaxLength(String str) {
        ObjectValue objectValue = getObjectValue(str);
        if (objectValue.valueType != 32) {
            throw new IllegalArgumentException("Not a list!");
        }
        return objectValue.arrayMaxLength;
    }

    private static synchronized void createStandardFormat() {
        if (standardFormat == null) {
            standardFormat = new StandardMetadataFormat();
        }
    }

    public static IIOMetadataFormat getStandardFormatInstance() {
        createStandardFormat();
        return standardFormat;
    }
}
