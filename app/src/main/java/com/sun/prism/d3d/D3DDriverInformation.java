package com.sun.prism.d3d;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DDriverInformation.class */
class D3DDriverInformation {
    public String deviceName;
    public String deviceDescription;
    public String driverName;
    public String warningMessage;
    public int product;
    public int version;
    public int subVersion;
    public int buildID;
    public int psVersionMajor;
    public int psVersionMinor;
    public int maxSamples = 0;
    public int vendorID;
    public int deviceID;
    public int subSysId;
    public int osMajorVersion;
    public int osMinorVersion;
    public int osBuildNumber;

    D3DDriverInformation() {
    }

    public String getDriverVersion() {
        return String.format("%d.%d.%d.%d", Integer.valueOf(this.product), Integer.valueOf(this.version), Integer.valueOf(this.subVersion), Integer.valueOf(this.buildID));
    }

    public String getDeviceID() {
        return String.format("ven_%04X, dev_%04X, subsys_%08X", Integer.valueOf(this.vendorID), Integer.valueOf(this.deviceID), Integer.valueOf(this.subSysId));
    }

    public String getOsVersion() {
        switch (this.osMajorVersion) {
            case 5:
                switch (this.osMinorVersion) {
                    case 0:
                        return "Windows 2000";
                    case 1:
                        return "Windows XP";
                    case 2:
                        return "Windows Server 2003";
                }
            case 6:
                switch (this.osMinorVersion) {
                    case 0:
                        return "Windows Vista";
                    case 1:
                        return "Windows 7";
                    case 2:
                        return "Windows 8.0";
                    case 3:
                        return "Windows 8.1";
                }
        }
        return "Windows version " + this.osMajorVersion + "." + this.osMinorVersion;
    }
}
