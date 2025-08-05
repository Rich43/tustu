package com.oracle.webservices.internal.api.databinding;

import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.model.ExternalMetadataReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/ExternalMetadataFeature.class */
public class ExternalMetadataFeature extends WebServiceFeature {
    private static final String ID = "com.oracle.webservices.internal.api.databinding.ExternalMetadataFeature";
    private boolean enabled = true;
    private List<String> resourceNames;
    private List<File> files;
    private MetadataReader reader;

    private ExternalMetadataFeature() {
    }

    public void addResources(String... resourceNames) {
        if (this.resourceNames == null) {
            this.resourceNames = new ArrayList();
        }
        Collections.addAll(this.resourceNames, resourceNames);
    }

    public List<String> getResourceNames() {
        return this.resourceNames;
    }

    public void addFiles(File... files) {
        if (this.files == null) {
            this.files = new ArrayList();
        }
        Collections.addAll(this.files, files);
    }

    public List<File> getFiles() {
        return this.files;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public boolean isEnabled() {
        return this.enabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabled(boolean x2) {
        this.enabled = x2;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ID;
    }

    public MetadataReader getMetadataReader(ClassLoader classLoader, boolean disableXmlSecurity) {
        if (this.reader != null && this.enabled) {
            return this.reader;
        }
        if (this.enabled) {
            return new ExternalMetadataReader(this.files, this.resourceNames, classLoader, true, disableXmlSecurity);
        }
        return null;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        ExternalMetadataFeature that = (ExternalMetadataFeature) o2;
        if (this.enabled != that.enabled) {
            return false;
        }
        if (this.files != null) {
            if (!this.files.equals(that.files)) {
                return false;
            }
        } else if (that.files != null) {
            return false;
        }
        if (this.resourceNames != null) {
            if (!this.resourceNames.equals(that.resourceNames)) {
                return false;
            }
            return true;
        }
        if (that.resourceNames != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.enabled ? 1 : 0;
        return (31 * ((31 * result) + (this.resourceNames != null ? this.resourceNames.hashCode() : 0))) + (this.files != null ? this.files.hashCode() : 0);
    }

    public String toString() {
        return "[" + getID() + ", enabled=" + this.enabled + ", resourceNames=" + ((Object) this.resourceNames) + ", files=" + ((Object) this.files) + ']';
    }

    public static Builder builder() {
        return new Builder(new ExternalMetadataFeature());
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/ExternalMetadataFeature$Builder.class */
    public static final class Builder {

        /* renamed from: o, reason: collision with root package name */
        private final ExternalMetadataFeature f11799o;

        Builder(ExternalMetadataFeature x2) {
            this.f11799o = x2;
        }

        public ExternalMetadataFeature build() {
            return this.f11799o;
        }

        public Builder addResources(String... res) {
            this.f11799o.addResources(res);
            return this;
        }

        public Builder addFiles(File... files) {
            this.f11799o.addFiles(files);
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.f11799o.setEnabled(enabled);
            return this;
        }

        public Builder setReader(MetadataReader r2) {
            this.f11799o.reader = r2;
            return this;
        }
    }
}
