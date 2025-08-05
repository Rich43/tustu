package com.efiAnalytics.remotefileaccess;

import java.io.Serializable;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/remotefileaccess/DirectoryFiles.class */
public class DirectoryFiles implements Serializable {
    private DirectoryInformation directoryInformation = null;
    private List files = null;

    public DirectoryInformation getDirectoryInformation() {
        return this.directoryInformation;
    }

    public void setDirectoryInformation(DirectoryInformation directoryInformation) {
        this.directoryInformation = directoryInformation;
    }

    public List getFiles() {
        return this.files;
    }

    public void setFiles(List list) {
        this.files = list;
    }
}
