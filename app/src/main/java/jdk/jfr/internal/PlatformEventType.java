package jdk.jfr.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jdk.jfr.SettingDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/PlatformEventType.class */
public final class PlatformEventType extends Type {
    private final boolean isJVM;
    private final boolean isJDK;
    private final boolean isMethodSampling;
    private final List<SettingDescriptor> settings;
    private final boolean dynamicSettings;
    private final int stackTraceOffset;
    private boolean enabled;
    private boolean stackTraceEnabled;
    private long thresholdTicks;
    private long period;
    private boolean hasHook;
    private boolean beginChunk;
    private boolean endChunk;
    private boolean hasStackTrace;
    private boolean hasDuration;
    private boolean hasPeriod;
    private boolean hasCutoff;
    private boolean isInstrumented;
    private boolean markForInstrumentation;
    private boolean registered;
    private boolean commitable;

    PlatformEventType(String str, long j2, boolean z2, boolean z3) {
        super(str, Type.SUPER_TYPE_EVENT, j2);
        this.settings = new ArrayList(5);
        this.enabled = false;
        this.stackTraceEnabled = true;
        this.thresholdTicks = 0L;
        this.period = 0L;
        this.hasStackTrace = true;
        this.hasDuration = true;
        this.hasPeriod = true;
        this.hasCutoff = false;
        this.registered = true;
        this.commitable = this.enabled && this.registered;
        this.dynamicSettings = z3;
        this.isJVM = Type.isDefinedByJVM(j2);
        this.isMethodSampling = str.equals("jdk.ExecutionSample") || str.equals("jdk.NativeMethodSample");
        this.isJDK = z2;
        this.stackTraceOffset = stackTraceOffset(str, z2);
    }

    private static int stackTraceOffset(String str, boolean z2) {
        if (z2) {
            if (str.equals("jdk.JavaExceptionThrow") || str.equals("jdk.JavaErrorThrow")) {
                return 5;
            }
            return 4;
        }
        return 4;
    }

    public void add(SettingDescriptor settingDescriptor) {
        Objects.requireNonNull(settingDescriptor);
        this.settings.add(settingDescriptor);
    }

    public List<SettingDescriptor> getSettings() {
        if (this.dynamicSettings) {
            ArrayList arrayList = new ArrayList(this.settings.size());
            for (SettingDescriptor settingDescriptor : this.settings) {
                if (Utils.isSettingVisible(settingDescriptor.getTypeId(), this.hasHook)) {
                    arrayList.add(settingDescriptor);
                }
            }
            return arrayList;
        }
        return this.settings;
    }

    public List<SettingDescriptor> getAllSettings() {
        return this.settings;
    }

    public void setHasStackTrace(boolean z2) {
        this.hasStackTrace = z2;
    }

    public void setHasDuration(boolean z2) {
        this.hasDuration = z2;
    }

    public void setHasCutoff(boolean z2) {
        this.hasCutoff = z2;
    }

    public void setCutoff(long j2) {
        if (this.isJVM) {
            JVM.getJVM().setCutoff(getId(), Utils.nanosToTicks(j2));
        }
    }

    public void setHasPeriod(boolean z2) {
        this.hasPeriod = z2;
    }

    public boolean hasStackTrace() {
        return this.hasStackTrace;
    }

    public boolean hasDuration() {
        return this.hasDuration;
    }

    public boolean hasPeriod() {
        return this.hasPeriod;
    }

    public boolean hasCutoff() {
        return this.hasCutoff;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isJVM() {
        return this.isJVM;
    }

    public boolean isJDK() {
        return this.isJDK;
    }

    public void setEnabled(boolean z2) {
        this.enabled = z2;
        updateCommitable();
        if (this.isJVM) {
            if (this.isMethodSampling) {
                JVM.getJVM().setMethodSamplingInterval(getId(), z2 ? this.period : 0L);
            } else {
                JVM.getJVM().setEnabled(getId(), z2);
            }
        }
    }

    public void setPeriod(long j2, boolean z2, boolean z3) {
        if (this.isMethodSampling) {
            JVM.getJVM().setMethodSamplingInterval(getId(), this.enabled ? j2 : 0L);
        }
        this.beginChunk = z2;
        this.endChunk = z3;
        this.period = j2;
    }

    public void setStackTraceEnabled(boolean z2) {
        this.stackTraceEnabled = z2;
        if (this.isJVM) {
            JVM.getJVM().setStackTraceEnabled(getId(), z2);
        }
    }

    public void setThreshold(long j2) {
        this.thresholdTicks = Utils.nanosToTicks(j2);
        if (this.isJVM) {
            JVM.getJVM().setThreshold(getId(), this.thresholdTicks);
        }
    }

    public boolean isEveryChunk() {
        return this.period == 0;
    }

    public boolean getStackTraceEnabled() {
        return this.stackTraceEnabled;
    }

    public long getThresholdTicks() {
        return this.thresholdTicks;
    }

    public long getPeriod() {
        return this.period;
    }

    public boolean hasEventHook() {
        return this.hasHook;
    }

    public void setEventHook(boolean z2) {
        this.hasHook = z2;
    }

    public boolean isBeginChunk() {
        return this.beginChunk;
    }

    public boolean isEndChunk() {
        return this.endChunk;
    }

    public boolean isInstrumented() {
        return this.isInstrumented;
    }

    public void setInstrumented() {
        this.isInstrumented = true;
    }

    public void markForInstrumentation(boolean z2) {
        this.markForInstrumentation = z2;
    }

    public boolean isMarkedForInstrumentation() {
        return this.markForInstrumentation;
    }

    public boolean setRegistered(boolean z2) {
        if (this.registered != z2) {
            this.registered = z2;
            updateCommitable();
            LogTag logTag = (isJVM() || isJDK()) ? LogTag.JFR_SYSTEM_EVENT : LogTag.JFR_EVENT;
            if (z2) {
                Logger.log(logTag, LogLevel.INFO, "Registered " + getLogName());
            } else {
                Logger.log(logTag, LogLevel.INFO, "Unregistered " + getLogName());
            }
            if (!z2) {
                MetadataRepository.getInstance().setUnregistered();
                return true;
            }
            return true;
        }
        return false;
    }

    private void updateCommitable() {
        this.commitable = this.enabled && this.registered;
    }

    public final boolean isRegistered() {
        return this.registered;
    }

    public boolean isCommitable() {
        return this.commitable;
    }

    public int getStackTraceOffset() {
        return this.stackTraceOffset;
    }
}
