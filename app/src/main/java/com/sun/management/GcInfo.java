package com.sun.management;

import java.lang.management.MemoryUsage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;
import jdk.Exported;
import sun.management.GcInfoBuilder;
import sun.management.GcInfoCompositeData;

@Exported
/* loaded from: rt.jar:com/sun/management/GcInfo.class */
public class GcInfo implements CompositeData, CompositeDataView {
    private final long index;
    private final long startTime;
    private final long endTime;
    private final Map<String, MemoryUsage> usageBeforeGc;
    private final Map<String, MemoryUsage> usageAfterGc;
    private final Object[] extAttributes;
    private final CompositeData cdata;
    private final GcInfoBuilder builder;

    private GcInfo(GcInfoBuilder gcInfoBuilder, long j2, long j3, long j4, MemoryUsage[] memoryUsageArr, MemoryUsage[] memoryUsageArr2, Object[] objArr) {
        this.builder = gcInfoBuilder;
        this.index = j2;
        this.startTime = j3;
        this.endTime = j4;
        String[] poolNames = gcInfoBuilder.getPoolNames();
        this.usageBeforeGc = new HashMap(poolNames.length);
        this.usageAfterGc = new HashMap(poolNames.length);
        for (int i2 = 0; i2 < poolNames.length; i2++) {
            this.usageBeforeGc.put(poolNames[i2], memoryUsageArr[i2]);
            this.usageAfterGc.put(poolNames[i2], memoryUsageArr2[i2]);
        }
        this.extAttributes = objArr;
        this.cdata = new GcInfoCompositeData(this, gcInfoBuilder, objArr);
    }

    private GcInfo(CompositeData compositeData) {
        GcInfoCompositeData.validateCompositeData(compositeData);
        this.index = GcInfoCompositeData.getId(compositeData);
        this.startTime = GcInfoCompositeData.getStartTime(compositeData);
        this.endTime = GcInfoCompositeData.getEndTime(compositeData);
        this.usageBeforeGc = GcInfoCompositeData.getMemoryUsageBeforeGc(compositeData);
        this.usageAfterGc = GcInfoCompositeData.getMemoryUsageAfterGc(compositeData);
        this.extAttributes = null;
        this.builder = null;
        this.cdata = compositeData;
    }

    public long getId() {
        return this.index;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public long getDuration() {
        return this.endTime - this.startTime;
    }

    public Map<String, MemoryUsage> getMemoryUsageBeforeGc() {
        return Collections.unmodifiableMap(this.usageBeforeGc);
    }

    public Map<String, MemoryUsage> getMemoryUsageAfterGc() {
        return Collections.unmodifiableMap(this.usageAfterGc);
    }

    public static GcInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof GcInfoCompositeData) {
            return ((GcInfoCompositeData) compositeData).getGcInfo();
        }
        return new GcInfo(compositeData);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsKey(String str) {
        return this.cdata.containsKey(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsValue(Object obj) {
        return this.cdata.containsValue(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean equals(Object obj) {
        return this.cdata.equals(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public Object get(String str) {
        return this.cdata.get(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public Object[] getAll(String[] strArr) {
        return this.cdata.getAll(strArr);
    }

    @Override // javax.management.openmbean.CompositeData
    public CompositeType getCompositeType() {
        return this.cdata.getCompositeType();
    }

    @Override // javax.management.openmbean.CompositeData
    public int hashCode() {
        return this.cdata.hashCode();
    }

    @Override // javax.management.openmbean.CompositeData
    public String toString() {
        return this.cdata.toString();
    }

    @Override // javax.management.openmbean.CompositeData
    public Collection values() {
        return this.cdata.values();
    }

    @Override // javax.management.openmbean.CompositeDataView
    public CompositeData toCompositeData(CompositeType compositeType) {
        return this.cdata;
    }
}
