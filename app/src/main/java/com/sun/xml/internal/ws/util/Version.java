package com.sun.xml.internal.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/Version.class */
public final class Version {
    public final String BUILD_ID;
    public final String BUILD_VERSION;
    public final String MAJOR_VERSION;
    public final String SVN_REVISION;
    public static final Version RUNTIME_VERSION = create(Version.class.getResourceAsStream("version.properties"));

    private Version(String buildId, String buildVersion, String majorVersion, String svnRev) {
        this.BUILD_ID = fixNull(buildId);
        this.BUILD_VERSION = fixNull(buildVersion);
        this.MAJOR_VERSION = fixNull(majorVersion);
        this.SVN_REVISION = fixNull(svnRev);
    }

    public static Version create(InputStream is) {
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e2) {
        } catch (Exception e3) {
        }
        return new Version(props.getProperty("build-id"), props.getProperty("build-version"), props.getProperty("major-version"), props.getProperty("svn-revision"));
    }

    private String fixNull(String v2) {
        return v2 == null ? "unknown" : v2;
    }

    public String toString() {
        return this.BUILD_VERSION + " svn-revision#" + this.SVN_REVISION;
    }
}
