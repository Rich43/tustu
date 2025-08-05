package com.sun.management;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;
import jdk.Exported;
import sun.management.GarbageCollectionNotifInfoCompositeData;

@Exported
/* loaded from: rt.jar:com/sun/management/GarbageCollectionNotificationInfo.class */
public class GarbageCollectionNotificationInfo implements CompositeDataView {
    private final String gcName;
    private final String gcAction;
    private final String gcCause;
    private final GcInfo gcInfo;
    private final CompositeData cdata;
    public static final String GARBAGE_COLLECTION_NOTIFICATION = "com.sun.management.gc.notification";

    public GarbageCollectionNotificationInfo(String str, String str2, String str3, GcInfo gcInfo) {
        if (str == null) {
            throw new NullPointerException("Null gcName");
        }
        if (str2 == null) {
            throw new NullPointerException("Null gcAction");
        }
        if (str3 == null) {
            throw new NullPointerException("Null gcCause");
        }
        this.gcName = str;
        this.gcAction = str2;
        this.gcCause = str3;
        this.gcInfo = gcInfo;
        this.cdata = new GarbageCollectionNotifInfoCompositeData(this);
    }

    GarbageCollectionNotificationInfo(CompositeData compositeData) {
        GarbageCollectionNotifInfoCompositeData.validateCompositeData(compositeData);
        this.gcName = GarbageCollectionNotifInfoCompositeData.getGcName(compositeData);
        this.gcAction = GarbageCollectionNotifInfoCompositeData.getGcAction(compositeData);
        this.gcCause = GarbageCollectionNotifInfoCompositeData.getGcCause(compositeData);
        this.gcInfo = GarbageCollectionNotifInfoCompositeData.getGcInfo(compositeData);
        this.cdata = compositeData;
    }

    public String getGcName() {
        return this.gcName;
    }

    public String getGcAction() {
        return this.gcAction;
    }

    public String getGcCause() {
        return this.gcCause;
    }

    public GcInfo getGcInfo() {
        return this.gcInfo;
    }

    public static GarbageCollectionNotificationInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof GarbageCollectionNotifInfoCompositeData) {
            return ((GarbageCollectionNotifInfoCompositeData) compositeData).getGarbageCollectionNotifInfo();
        }
        return new GarbageCollectionNotificationInfo(compositeData);
    }

    @Override // javax.management.openmbean.CompositeDataView
    public CompositeData toCompositeData(CompositeType compositeType) {
        return this.cdata;
    }
}
