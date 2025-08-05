package javax.management.openmbean;

import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

/* loaded from: rt.jar:javax/management/openmbean/CompositeType.class */
public class CompositeType extends OpenType<CompositeData> {
    static final long serialVersionUID = -5366242454346948798L;
    private TreeMap<String, String> nameToDescription;
    private TreeMap<String, OpenType<?>> nameToType;
    private transient Integer myHashCode;
    private transient String myToString;
    private transient Set<String> myNamesSet;

    public CompositeType(String str, String str2, String[] strArr, String[] strArr2, OpenType<?>[] openTypeArr) throws OpenDataException {
        super(CompositeData.class.getName(), str, str2, false);
        this.myHashCode = null;
        this.myToString = null;
        this.myNamesSet = null;
        checkForNullElement(strArr, "itemNames");
        checkForNullElement(strArr2, "itemDescriptions");
        checkForNullElement(openTypeArr, "itemTypes");
        checkForEmptyString(strArr, "itemNames");
        checkForEmptyString(strArr2, "itemDescriptions");
        if (strArr.length != strArr2.length || strArr.length != openTypeArr.length) {
            throw new IllegalArgumentException("Array arguments itemNames[], itemDescriptions[] and itemTypes[] should be of same length (got " + strArr.length + ", " + strArr2.length + " and " + openTypeArr.length + ").");
        }
        this.nameToDescription = new TreeMap<>();
        this.nameToType = new TreeMap<>();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String strTrim = strArr[i2].trim();
            if (this.nameToDescription.containsKey(strTrim)) {
                throw new OpenDataException("Argument's element itemNames[" + i2 + "]=\"" + strArr[i2] + "\" duplicates a previous item names.");
            }
            this.nameToDescription.put(strTrim, strArr2[i2].trim());
            this.nameToType.put(strTrim, openTypeArr[i2]);
        }
    }

    private static void checkForNullElement(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException("Argument " + str + "[] cannot be null or empty.");
        }
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] == null) {
                throw new IllegalArgumentException("Argument's element " + str + "[" + i2 + "] cannot be null.");
            }
        }
    }

    private static void checkForEmptyString(String[] strArr, String str) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].trim().equals("")) {
                throw new IllegalArgumentException("Argument's element " + str + "[" + i2 + "] cannot be an empty string.");
            }
        }
    }

    public boolean containsKey(String str) {
        if (str == null) {
            return false;
        }
        return this.nameToDescription.containsKey(str);
    }

    public String getDescription(String str) {
        if (str == null) {
            return null;
        }
        return this.nameToDescription.get(str);
    }

    public OpenType<?> getType(String str) {
        if (str == null) {
            return null;
        }
        return this.nameToType.get(str);
    }

    public Set<String> keySet() {
        if (this.myNamesSet == null) {
            this.myNamesSet = Collections.unmodifiableSet(this.nameToDescription.keySet());
        }
        return this.myNamesSet;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (!(obj instanceof CompositeData)) {
            return false;
        }
        return isAssignableFrom(((CompositeData) obj).getCompositeType());
    }

    @Override // javax.management.openmbean.OpenType
    boolean isAssignableFrom(OpenType<?> openType) {
        if (!(openType instanceof CompositeType)) {
            return false;
        }
        CompositeType compositeType = (CompositeType) openType;
        if (!compositeType.getTypeName().equals(getTypeName())) {
            return false;
        }
        for (String str : keySet()) {
            OpenType<?> type = compositeType.getType(str);
            OpenType<?> type2 = getType(str);
            if (type == null || !type2.isAssignableFrom(type)) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            CompositeType compositeType = (CompositeType) obj;
            if (!getTypeName().equals(compositeType.getTypeName()) || !this.nameToType.equals(compositeType.nameToType)) {
                return false;
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            int iHashCode = 0 + getTypeName().hashCode();
            for (String str : this.nameToDescription.keySet()) {
                iHashCode = iHashCode + str.hashCode() + this.nameToType.get(str).hashCode();
            }
            this.myHashCode = Integer.valueOf(iHashCode);
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getName());
            sb.append("(name=");
            sb.append(getTypeName());
            sb.append(",items=(");
            int i2 = 0;
            for (String str : this.nameToType.keySet()) {
                if (i2 > 0) {
                    sb.append(",");
                }
                sb.append("(itemName=");
                sb.append(str);
                sb.append(",itemType=");
                sb.append(this.nameToType.get(str).toString() + ")");
                i2++;
            }
            sb.append("))");
            this.myToString = sb.toString();
        }
        return this.myToString;
    }
}
