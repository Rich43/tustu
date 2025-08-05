package de.muntjak.tinylookandfeel;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/ThemeDescription.class */
public class ThemeDescription {
    private URI uri;
    private URL url;
    private File file;
    private boolean fileHasBeenSet = false;
    private String name = null;

    public ThemeDescription(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        try {
            if (DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
                this.uri = new URI(url.toExternalForm());
            } else {
                this.uri = new URI(url.getPath());
            }
            this.url = url;
        } catch (URISyntaxException e2) {
            this.uri = null;
        }
    }

    public ThemeDescription(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri may not be null");
        }
        try {
            this.url = uri.toURL();
            this.uri = uri;
        } catch (MalformedURLException e2) {
            this.url = null;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ThemeDescription)) {
            return false;
        }
        ThemeDescription themeDescription = (ThemeDescription) obj;
        if (isValid() != themeDescription.isValid()) {
            return false;
        }
        if (isValid()) {
            return this.uri.equals(themeDescription.uri);
        }
        return true;
    }

    public boolean isValid() {
        return (this.uri == null || this.url == null) ? false : true;
    }

    public String getName() {
        if (this.uri == null) {
            return "? URI == null ?";
        }
        if (this.name == null) {
            this.name = getName(this.uri.getPath());
        }
        return this.name;
    }

    private static String getName(String str) {
        if (str == null) {
            return "? uri.getPath() == null ?";
        }
        String strSubstring = str.substring(str.lastIndexOf("/") + 1);
        int iIndexOf = strSubstring.indexOf(".");
        return iIndexOf == -1 ? strSubstring : strSubstring.substring(0, iIndexOf);
    }

    public URL getURL() {
        return this.url;
    }

    public boolean isFile() {
        if (isValid() && !Theme.YQ_URL.equals(this.url)) {
            return DeploymentDescriptorParser.ATTR_FILE.equals(this.url.getProtocol());
        }
        return false;
    }

    public File getFile() {
        if (this.fileHasBeenSet) {
            return this.file;
        }
        this.fileHasBeenSet = true;
        if (!isFile()) {
            return null;
        }
        try {
            this.file = new File(this.uri);
        } catch (IllegalArgumentException e2) {
            System.err.println(new StringBuffer().append(getClass().getName()).append(".getFile() ").append(e2.toString()).toString());
            System.err.println(new StringBuffer().append("URI=").append((Object) this.uri).append("\nURL=").append((Object) this.url).toString());
        }
        return this.file;
    }

    public String toString() {
        return getName();
    }
}
