package javax.management.loading;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:javax/management/loading/MLetContent.class */
public class MLetContent {
    private Map<String, String> attributes;
    private List<String> types;
    private List<String> values;
    private URL documentURL;
    private URL baseURL;

    public MLetContent(URL url, Map<String, String> map, List<String> list, List<String> list2) {
        String file;
        int iLastIndexOf;
        this.documentURL = url;
        this.attributes = Collections.unmodifiableMap(map);
        this.types = Collections.unmodifiableList(list);
        this.values = Collections.unmodifiableList(list2);
        String parameter = getParameter(Constants.ATTRNAME_CODEBASE);
        if (parameter != null) {
            try {
                this.baseURL = new URL(this.documentURL, parameter.endsWith("/") ? parameter : parameter + "/");
            } catch (MalformedURLException e2) {
            }
        }
        if (this.baseURL == null && (iLastIndexOf = (file = this.documentURL.getFile()).lastIndexOf(47)) >= 0 && iLastIndexOf < file.length() - 1) {
            try {
                this.baseURL = new URL(this.documentURL, file.substring(0, iLastIndexOf + 1));
            } catch (MalformedURLException e3) {
            }
        }
        if (this.baseURL == null) {
            this.baseURL = this.documentURL;
        }
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public URL getDocumentBase() {
        return this.documentURL;
    }

    public URL getCodeBase() {
        return this.baseURL;
    }

    public String getJarFiles() {
        return getParameter(Constants.ATTRNAME_ARCHIVE);
    }

    public String getCode() {
        return getParameter("code");
    }

    public String getSerializedObject() {
        return getParameter("object");
    }

    public String getName() {
        return getParameter("name");
    }

    public String getVersion() {
        return getParameter("version");
    }

    public List<String> getParameterTypes() {
        return this.types;
    }

    public List<String> getParameterValues() {
        return this.values;
    }

    private String getParameter(String str) {
        return this.attributes.get(str.toLowerCase());
    }
}
