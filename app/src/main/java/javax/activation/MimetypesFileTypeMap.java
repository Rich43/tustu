package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MimeTypeFile;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

/* loaded from: rt.jar:javax/activation/MimetypesFileTypeMap.class */
public class MimetypesFileTypeMap extends FileTypeMap {
    private MimeTypeFile[] DB;
    private static final int PROG = 0;
    private static String defaultType = Locator.DEFAULT_CONTENT_TYPE;

    public MimetypesFileTypeMap() {
        Vector dbv = new Vector(5);
        dbv.addElement(null);
        LogSupport.log("MimetypesFileTypeMap: load HOME");
        try {
            String user_home = System.getProperty("user.home");
            if (user_home != null) {
                String path = user_home + File.separator + ".mime.types";
                MimeTypeFile mf = loadFile(path);
                if (mf != null) {
                    dbv.addElement(mf);
                }
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MimetypesFileTypeMap: load SYS");
        try {
            String system_mimetypes = System.getProperty("java.home") + File.separator + "lib" + File.separator + "mime.types";
            MimeTypeFile mf2 = loadFile(system_mimetypes);
            if (mf2 != null) {
                dbv.addElement(mf2);
            }
        } catch (SecurityException e3) {
        }
        LogSupport.log("MimetypesFileTypeMap: load JAR");
        loadAllResources(dbv, "META-INF/mime.types");
        LogSupport.log("MimetypesFileTypeMap: load DEF");
        MimeTypeFile mf3 = loadResource("/META-INF/mimetypes.default");
        if (mf3 != null) {
            dbv.addElement(mf3);
        }
        this.DB = new MimeTypeFile[dbv.size()];
        dbv.copyInto(this.DB);
    }

    private MimeTypeFile loadResource(String name) {
        InputStream clis = null;
        try {
            try {
                InputStream clis2 = SecuritySupport.getResourceAsStream(getClass(), name);
                if (clis2 != null) {
                    MimeTypeFile mf = new MimeTypeFile(clis2);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types file: " + name);
                    }
                    if (clis2 != null) {
                        try {
                            clis2.close();
                        } catch (IOException e2) {
                        }
                    }
                    return mf;
                }
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: not loading mime types file: " + name);
                }
                if (clis2 != null) {
                    try {
                        clis2.close();
                    } catch (IOException e3) {
                        return null;
                    }
                }
                return null;
            } catch (IOException e4) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: can't load " + name, e4);
                }
                if (0 != 0) {
                    try {
                        clis.close();
                    } catch (IOException e5) {
                        return null;
                    }
                }
                return null;
            } catch (SecurityException sex) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: can't load " + name, sex);
                }
                if (0 != 0) {
                    try {
                        clis.close();
                    } catch (IOException e6) {
                        return null;
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    clis.close();
                } catch (IOException e7) {
                    throw th;
                }
            }
            throw th;
        }
    }

    private void loadAllResources(Vector v2, String name) {
        boolean anyLoaded = false;
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            URL[] urls = cld != null ? SecuritySupport.getResources(cld, name) : SecuritySupport.getSystemResources(name);
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MimetypesFileTypeMap: URL " + ((Object) url));
                    }
                    try {
                        try {
                            clis = SecuritySupport.openStream(url);
                            if (clis != null) {
                                v2.addElement(new MimeTypeFile(clis));
                                anyLoaded = true;
                                if (LogSupport.isLoggable()) {
                                    LogSupport.log("MimetypesFileTypeMap: successfully loaded mime types from URL: " + ((Object) url));
                                }
                            } else if (LogSupport.isLoggable()) {
                                LogSupport.log("MimetypesFileTypeMap: not loading mime types from URL: " + ((Object) url));
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e2) {
                                }
                            }
                        } catch (IOException ioex) {
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MimetypesFileTypeMap: can't load " + ((Object) url), ioex);
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e3) {
                                }
                            }
                        } catch (SecurityException sex) {
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MimetypesFileTypeMap: can't load " + ((Object) url), sex);
                            }
                            if (clis != null) {
                                try {
                                    clis.close();
                                } catch (IOException e4) {
                                }
                            }
                        }
                    } catch (Throwable th) {
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e5) {
                                throw th;
                            }
                        }
                        throw th;
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
            }
        }
        if (anyLoaded) {
            return;
        }
        LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
        MimeTypeFile mf = loadResource("/" + name);
        if (mf != null) {
            v2.addElement(mf);
        }
    }

    private MimeTypeFile loadFile(String name) {
        MimeTypeFile mtf = null;
        try {
            mtf = new MimeTypeFile(name);
        } catch (IOException e2) {
        }
        return mtf;
    }

    public MimetypesFileTypeMap(String mimeTypeFileName) throws IOException {
        this();
        this.DB[0] = new MimeTypeFile(mimeTypeFileName);
    }

    public MimetypesFileTypeMap(InputStream is) {
        this();
        try {
            this.DB[0] = new MimeTypeFile(is);
        } catch (IOException e2) {
        }
    }

    public synchronized void addMimeTypes(String mime_types) {
        if (this.DB[0] == null) {
            this.DB[0] = new MimeTypeFile();
        }
        this.DB[0].appendToRegistry(mime_types);
    }

    @Override // javax.activation.FileTypeMap
    public String getContentType(File f2) {
        return getContentType(f2.getName());
    }

    @Override // javax.activation.FileTypeMap
    public synchronized String getContentType(String filename) {
        String result;
        int dot_pos = filename.lastIndexOf(".");
        if (dot_pos < 0) {
            return defaultType;
        }
        String file_ext = filename.substring(dot_pos + 1);
        if (file_ext.length() == 0) {
            return defaultType;
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (this.DB[i2] != null && (result = this.DB[i2].getMIMETypeString(file_ext)) != null) {
                return result;
            }
        }
        return defaultType;
    }
}
