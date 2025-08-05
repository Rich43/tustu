package com.sun.javafx.application;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import javafx.application.Application;

/* loaded from: jfxrt.jar:com/sun/javafx/application/HostServicesDelegate.class */
public abstract class HostServicesDelegate {
    public abstract String getCodeBase();

    public abstract String getDocumentBase();

    public abstract void showDocument(String str);

    public static HostServicesDelegate getInstance(Application app) {
        return StandaloneHostService.getInstance(app);
    }

    protected HostServicesDelegate() {
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/application/HostServicesDelegate$StandaloneHostService.class */
    private static class StandaloneHostService extends HostServicesDelegate {
        private Class appClass;
        private static HostServicesDelegate instance = null;
        static final String[] browsers = {"google-chrome", "firefox", "opera", "konqueror", "mozilla"};

        public static HostServicesDelegate getInstance(Application app) {
            HostServicesDelegate hostServicesDelegate;
            synchronized (StandaloneHostService.class) {
                if (instance == null) {
                    instance = new StandaloneHostService(app);
                }
                hostServicesDelegate = instance;
            }
            return hostServicesDelegate;
        }

        private StandaloneHostService(Application app) {
            this.appClass = null;
            this.appClass = app.getClass();
        }

        @Override // com.sun.javafx.application.HostServicesDelegate
        public String getCodeBase() {
            String codebase;
            String theClassFile = this.appClass.getName();
            int idx = theClassFile.lastIndexOf(".");
            if (idx >= 0) {
                theClassFile = theClassFile.substring(idx + 1);
            }
            String classUrlString = this.appClass.getResource(theClassFile + ".class").toString();
            if (!classUrlString.startsWith("jar:file:") || classUrlString.indexOf("!") == -1) {
                return "";
            }
            String urlString = classUrlString.substring(4, classUrlString.lastIndexOf("!"));
            File jarFile = null;
            try {
                jarFile = new File(new URI(urlString).getPath());
            } catch (Exception e2) {
            }
            if (jarFile != null && (codebase = jarFile.getParent()) != null) {
                return toURIString(codebase);
            }
            return "";
        }

        private String toURIString(String filePath) {
            try {
                return new File(filePath).toURI().toString();
            } catch (Exception e2) {
                e2.printStackTrace();
                return "";
            }
        }

        @Override // com.sun.javafx.application.HostServicesDelegate
        public String getDocumentBase() {
            return toURIString(System.getProperty("user.dir"));
        }

        @Override // com.sun.javafx.application.HostServicesDelegate
        public void showDocument(String uri) throws Exception {
            String osName = System.getProperty("os.name");
            try {
                if (osName.startsWith("Mac OS")) {
                    Desktop.getDesktop().browse(URI.create(uri));
                } else if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
                } else {
                    String browser = null;
                    for (String b2 : browsers) {
                        if (browser == null && Runtime.getRuntime().exec(new String[]{"which", b2}).getInputStream().read() != -1) {
                            browser = b2;
                            Runtime.getRuntime().exec(new String[]{b2, uri});
                        }
                    }
                    if (browser == null) {
                        throw new Exception("No web browser found");
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
