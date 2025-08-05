package sun.management;

import com.sun.management.GcInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

/* loaded from: rt.jar:sun/management/GcInfoBuilder.class */
public class GcInfoBuilder {
    private final GarbageCollectorMXBean gc;
    private final String[] poolNames;
    private String[] allItemNames;
    private CompositeType gcInfoCompositeType;
    private final int gcExtItemCount;
    private final String[] gcExtItemNames;
    private final String[] gcExtItemDescs;
    private final char[] gcExtItemTypes;

    private native int getNumGcExtAttributes(GarbageCollectorMXBean garbageCollectorMXBean);

    private native void fillGcAttributeInfo(GarbageCollectorMXBean garbageCollectorMXBean, int i2, String[] strArr, char[] cArr, String[] strArr2);

    private native GcInfo getLastGcInfo0(GarbageCollectorMXBean garbageCollectorMXBean, int i2, Object[] objArr, char[] cArr, MemoryUsage[] memoryUsageArr, MemoryUsage[] memoryUsageArr2);

    GcInfoBuilder(GarbageCollectorMXBean garbageCollectorMXBean, String[] strArr) {
        this.gc = garbageCollectorMXBean;
        this.poolNames = strArr;
        this.gcExtItemCount = getNumGcExtAttributes(garbageCollectorMXBean);
        this.gcExtItemNames = new String[this.gcExtItemCount];
        this.gcExtItemDescs = new String[this.gcExtItemCount];
        this.gcExtItemTypes = new char[this.gcExtItemCount];
        fillGcAttributeInfo(garbageCollectorMXBean, this.gcExtItemCount, this.gcExtItemNames, this.gcExtItemTypes, this.gcExtItemDescs);
        this.gcInfoCompositeType = null;
    }

    GcInfo getLastGcInfo() {
        MemoryUsage[] memoryUsageArr = new MemoryUsage[this.poolNames.length];
        MemoryUsage[] memoryUsageArr2 = new MemoryUsage[this.poolNames.length];
        return getLastGcInfo0(this.gc, this.gcExtItemCount, new Object[this.gcExtItemCount], this.gcExtItemTypes, memoryUsageArr, memoryUsageArr2);
    }

    public String[] getPoolNames() {
        return this.poolNames;
    }

    int getGcExtItemCount() {
        return this.gcExtItemCount;
    }

    synchronized CompositeType getGcInfoCompositeType() {
        if (this.gcInfoCompositeType != null) {
            return this.gcInfoCompositeType;
        }
        String[] baseGcInfoItemNames = GcInfoCompositeData.getBaseGcInfoItemNames();
        OpenType[] baseGcInfoItemTypes = GcInfoCompositeData.getBaseGcInfoItemTypes();
        int length = baseGcInfoItemNames.length;
        int i2 = length + this.gcExtItemCount;
        this.allItemNames = new String[i2];
        String[] strArr = new String[i2];
        OpenType[] openTypeArr = new OpenType[i2];
        System.arraycopy(baseGcInfoItemNames, 0, this.allItemNames, 0, length);
        System.arraycopy(baseGcInfoItemNames, 0, strArr, 0, length);
        System.arraycopy(baseGcInfoItemTypes, 0, openTypeArr, 0, length);
        if (this.gcExtItemCount > 0) {
            fillGcAttributeInfo(this.gc, this.gcExtItemCount, this.gcExtItemNames, this.gcExtItemTypes, this.gcExtItemDescs);
            System.arraycopy(this.gcExtItemNames, 0, this.allItemNames, length, this.gcExtItemCount);
            System.arraycopy(this.gcExtItemDescs, 0, strArr, length, this.gcExtItemCount);
            int i3 = length;
            for (int i4 = 0; i4 < this.gcExtItemCount; i4++) {
                switch (this.gcExtItemTypes[i4]) {
                    case 'B':
                        openTypeArr[i3] = SimpleType.BYTE;
                        break;
                    case 'C':
                        openTypeArr[i3] = SimpleType.CHARACTER;
                        break;
                    case 'D':
                        openTypeArr[i3] = SimpleType.DOUBLE;
                        break;
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        throw new AssertionError((Object) ("Unsupported type [" + this.gcExtItemTypes[i3] + "]"));
                    case 'F':
                        openTypeArr[i3] = SimpleType.FLOAT;
                        break;
                    case 'I':
                        openTypeArr[i3] = SimpleType.INTEGER;
                        break;
                    case 'J':
                        openTypeArr[i3] = SimpleType.LONG;
                        break;
                    case 'S':
                        openTypeArr[i3] = SimpleType.SHORT;
                        break;
                    case 'Z':
                        openTypeArr[i3] = SimpleType.BOOLEAN;
                        break;
                }
                i3++;
            }
        }
        try {
            this.gcInfoCompositeType = new CompositeType("sun.management." + this.gc.getName() + ".GcInfoCompositeType", "CompositeType for GC info for " + this.gc.getName(), this.allItemNames, strArr, openTypeArr);
            return this.gcInfoCompositeType;
        } catch (OpenDataException e2) {
            throw Util.newException(e2);
        }
    }

    synchronized String[] getItemNames() {
        if (this.allItemNames == null) {
            getGcInfoCompositeType();
        }
        return this.allItemNames;
    }
}
