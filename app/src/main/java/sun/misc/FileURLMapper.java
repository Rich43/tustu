package sun.misc;

import java.io.File;
import java.net.URL;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:sun/misc/FileURLMapper.class */
public class FileURLMapper {
    URL url;
    String file;

    public FileURLMapper(URL url) {
        this.url = url;
    }

    public String getPath() {
        if (this.file != null) {
            return this.file;
        }
        String host = this.url.getHost();
        if (host != null && !host.equals("") && !"localhost".equalsIgnoreCase(host)) {
            this.url.getFile();
            this.file = "\\\\" + (host + ParseUtil.decode(this.url.getFile())).replace('/', '\\');
            return this.file;
        }
        this.file = ParseUtil.decode(this.url.getFile().replace('/', '\\'));
        return this.file;
    }

    public boolean exists() {
        return new File(getPath()).exists();
    }
}
