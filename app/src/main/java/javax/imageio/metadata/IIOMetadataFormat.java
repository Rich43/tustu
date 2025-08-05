package javax.imageio.metadata;

import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;

/* loaded from: rt.jar:javax/imageio/metadata/IIOMetadataFormat.class */
public interface IIOMetadataFormat {
    public static final int CHILD_POLICY_EMPTY = 0;
    public static final int CHILD_POLICY_ALL = 1;
    public static final int CHILD_POLICY_SOME = 2;
    public static final int CHILD_POLICY_CHOICE = 3;
    public static final int CHILD_POLICY_SEQUENCE = 4;
    public static final int CHILD_POLICY_REPEAT = 5;
    public static final int CHILD_POLICY_MAX = 5;
    public static final int VALUE_NONE = 0;
    public static final int VALUE_ARBITRARY = 1;
    public static final int VALUE_RANGE = 2;
    public static final int VALUE_RANGE_MIN_INCLUSIVE_MASK = 4;
    public static final int VALUE_RANGE_MAX_INCLUSIVE_MASK = 8;
    public static final int VALUE_RANGE_MIN_INCLUSIVE = 6;
    public static final int VALUE_RANGE_MAX_INCLUSIVE = 10;
    public static final int VALUE_RANGE_MIN_MAX_INCLUSIVE = 14;
    public static final int VALUE_ENUMERATION = 16;
    public static final int VALUE_LIST = 32;
    public static final int DATATYPE_STRING = 0;
    public static final int DATATYPE_BOOLEAN = 1;
    public static final int DATATYPE_INTEGER = 2;
    public static final int DATATYPE_FLOAT = 3;
    public static final int DATATYPE_DOUBLE = 4;

    String getRootName();

    boolean canNodeAppear(String str, ImageTypeSpecifier imageTypeSpecifier);

    int getElementMinChildren(String str);

    int getElementMaxChildren(String str);

    String getElementDescription(String str, Locale locale);

    int getChildPolicy(String str);

    String[] getChildNames(String str);

    String[] getAttributeNames(String str);

    int getAttributeValueType(String str, String str2);

    int getAttributeDataType(String str, String str2);

    boolean isAttributeRequired(String str, String str2);

    String getAttributeDefaultValue(String str, String str2);

    String[] getAttributeEnumerations(String str, String str2);

    String getAttributeMinValue(String str, String str2);

    String getAttributeMaxValue(String str, String str2);

    int getAttributeListMinLength(String str, String str2);

    int getAttributeListMaxLength(String str, String str2);

    String getAttributeDescription(String str, String str2, Locale locale);

    int getObjectValueType(String str);

    Class<?> getObjectClass(String str);

    Object getObjectDefaultValue(String str);

    Object[] getObjectEnumerations(String str);

    Comparable<?> getObjectMinValue(String str);

    Comparable<?> getObjectMaxValue(String str);

    int getObjectArrayMinLength(String str);

    int getObjectArrayMaxLength(String str);
}
