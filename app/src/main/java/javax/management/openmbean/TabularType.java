package javax.management.openmbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:javax/management/openmbean/TabularType.class */
public class TabularType extends OpenType<TabularData> {
    static final long serialVersionUID = 6554071860220659261L;
    private CompositeType rowType;
    private List<String> indexNames;
    private transient Integer myHashCode;
    private transient String myToString;

    public TabularType(String str, String str2, CompositeType compositeType, String[] strArr) throws OpenDataException {
        super(TabularData.class.getName(), str, str2, false);
        this.myHashCode = null;
        this.myToString = null;
        if (compositeType == null) {
            throw new IllegalArgumentException("Argument rowType cannot be null.");
        }
        checkForNullElement(strArr, "indexNames");
        checkForEmptyString(strArr, "indexNames");
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (!compositeType.containsKey(strArr[i2])) {
                throw new OpenDataException("Argument's element value indexNames[" + i2 + "]=\"" + strArr[i2] + "\" is not a valid item name for rowType.");
            }
        }
        this.rowType = compositeType;
        ArrayList arrayList = new ArrayList(strArr.length + 1);
        for (String str3 : strArr) {
            arrayList.add(str3);
        }
        this.indexNames = Collections.unmodifiableList(arrayList);
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

    public CompositeType getRowType() {
        return this.rowType;
    }

    public List<String> getIndexNames() {
        return this.indexNames;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (!(obj instanceof TabularData)) {
            return false;
        }
        return isAssignableFrom(((TabularData) obj).getTabularType());
    }

    @Override // javax.management.openmbean.OpenType
    boolean isAssignableFrom(OpenType<?> openType) {
        if (!(openType instanceof TabularType)) {
            return false;
        }
        TabularType tabularType = (TabularType) openType;
        if (!getTypeName().equals(tabularType.getTypeName()) || !getIndexNames().equals(tabularType.getIndexNames())) {
            return false;
        }
        return getRowType().isAssignableFrom(tabularType.getRowType());
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            TabularType tabularType = (TabularType) obj;
            if (!getTypeName().equals(tabularType.getTypeName()) || !this.rowType.equals(tabularType.rowType) || !this.indexNames.equals(tabularType.indexNames)) {
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
            int iHashCode = 0 + getTypeName().hashCode() + this.rowType.hashCode();
            Iterator<String> it = this.indexNames.iterator();
            while (it.hasNext()) {
                iHashCode += it.next().hashCode();
            }
            this.myHashCode = Integer.valueOf(iHashCode);
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            StringBuilder sbAppend = new StringBuilder().append(getClass().getName()).append("(name=").append(getTypeName()).append(",rowType=").append(this.rowType.toString()).append(",indexNames=(");
            String str = "";
            Iterator<String> it = this.indexNames.iterator();
            while (it.hasNext()) {
                sbAppend.append(str).append(it.next());
                str = ",";
            }
            sbAppend.append("))");
            this.myToString = sbAppend.toString();
        }
        return this.myToString;
    }
}
