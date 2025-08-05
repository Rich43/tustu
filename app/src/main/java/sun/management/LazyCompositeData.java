package sun.management;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.TabularType;

/* loaded from: rt.jar:sun/management/LazyCompositeData.class */
public abstract class LazyCompositeData implements CompositeData, Serializable {
    private CompositeData compositeData;
    private static final long serialVersionUID = -2190411934472666714L;

    protected abstract CompositeData getCompositeData();

    @Override // javax.management.openmbean.CompositeData
    public boolean containsKey(String str) {
        return compositeData().containsKey(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsValue(Object obj) {
        return compositeData().containsValue(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean equals(Object obj) {
        return compositeData().equals(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public Object get(String str) {
        return compositeData().get(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public Object[] getAll(String[] strArr) {
        return compositeData().getAll(strArr);
    }

    @Override // javax.management.openmbean.CompositeData
    public CompositeType getCompositeType() {
        return compositeData().getCompositeType();
    }

    @Override // javax.management.openmbean.CompositeData
    public int hashCode() {
        return compositeData().hashCode();
    }

    @Override // javax.management.openmbean.CompositeData
    public String toString() {
        return compositeData().toString();
    }

    @Override // javax.management.openmbean.CompositeData
    public Collection<?> values() {
        return compositeData().values();
    }

    private synchronized CompositeData compositeData() {
        if (this.compositeData != null) {
            return this.compositeData;
        }
        this.compositeData = getCompositeData();
        return this.compositeData;
    }

    protected Object writeReplace() throws ObjectStreamException {
        return compositeData();
    }

    static String getString(CompositeData compositeData, String str) {
        if (compositeData == null) {
            throw new IllegalArgumentException("Null CompositeData");
        }
        return (String) compositeData.get(str);
    }

    static boolean getBoolean(CompositeData compositeData, String str) {
        if (compositeData == null) {
            throw new IllegalArgumentException("Null CompositeData");
        }
        return ((Boolean) compositeData.get(str)).booleanValue();
    }

    static long getLong(CompositeData compositeData, String str) {
        if (compositeData == null) {
            throw new IllegalArgumentException("Null CompositeData");
        }
        return ((Long) compositeData.get(str)).longValue();
    }

    static int getInt(CompositeData compositeData, String str) {
        if (compositeData == null) {
            throw new IllegalArgumentException("Null CompositeData");
        }
        return ((Integer) compositeData.get(str)).intValue();
    }

    protected static boolean isTypeMatched(CompositeType compositeType, CompositeType compositeType2) {
        if (compositeType == compositeType2) {
            return true;
        }
        Set<String> setKeySet = compositeType.keySet();
        if (!compositeType2.keySet().containsAll(setKeySet)) {
            return false;
        }
        return setKeySet.stream().allMatch(str -> {
            return isTypeMatched(compositeType.getType(str), compositeType2.getType(str));
        });
    }

    protected static boolean isTypeMatched(TabularType tabularType, TabularType tabularType2) {
        if (tabularType == tabularType2) {
            return true;
        }
        if (!tabularType.getIndexNames().equals(tabularType2.getIndexNames())) {
            return false;
        }
        return isTypeMatched(tabularType.getRowType(), tabularType2.getRowType());
    }

    protected static boolean isTypeMatched(ArrayType<?> arrayType, ArrayType<?> arrayType2) {
        if (arrayType == arrayType2) {
            return true;
        }
        if (arrayType.getDimension() != arrayType2.getDimension()) {
            return false;
        }
        return isTypeMatched(arrayType.getElementOpenType(), arrayType2.getElementOpenType());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTypeMatched(OpenType<?> openType, OpenType<?> openType2) {
        if (openType instanceof CompositeType) {
            if (!(openType2 instanceof CompositeType) || !isTypeMatched((CompositeType) openType, (CompositeType) openType2)) {
                return false;
            }
            return true;
        }
        if (openType instanceof TabularType) {
            if (!(openType2 instanceof TabularType) || !isTypeMatched((TabularType) openType, (TabularType) openType2)) {
                return false;
            }
            return true;
        }
        if (openType instanceof ArrayType) {
            if (!(openType2 instanceof ArrayType) || !isTypeMatched((ArrayType<?>) openType, (ArrayType<?>) openType2)) {
                return false;
            }
            return true;
        }
        if (!openType.equals(openType2)) {
            return false;
        }
        return true;
    }
}
