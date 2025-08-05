package com.sun.xml.internal.ws.assembler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroConfigNameImpl.class */
public class MetroConfigNameImpl implements MetroConfigName {
    private final String defaultFileName;
    private final String appFileName;

    public MetroConfigNameImpl(String defaultFileName, String appFileName) {
        this.defaultFileName = defaultFileName;
        this.appFileName = appFileName;
    }

    @Override // com.sun.xml.internal.ws.assembler.MetroConfigName
    public String getDefaultFileName() {
        return this.defaultFileName;
    }

    @Override // com.sun.xml.internal.ws.assembler.MetroConfigName
    public String getAppFileName() {
        return this.appFileName;
    }
}
