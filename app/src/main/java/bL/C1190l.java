package bl;

import G.T;
import bH.C;
import com.efiAnalytics.plugin.ApplicationPlugin;
import com.efiAnalytics.plugin.ecu.ControllerAccess;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;
import r.C1807j;

/* renamed from: bl.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/l.class */
public class C1190l {

    /* renamed from: a, reason: collision with root package name */
    static HashMap f8255a = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private static C1190l f8256e = null;

    /* renamed from: b, reason: collision with root package name */
    Map f8257b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    Map f8258c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    Map f8259d = new HashMap();

    private C1190l() {
    }

    public static C1190l a() {
        if (f8256e == null) {
            f8256e = new C1190l();
        }
        return f8256e;
    }

    public static void b() throws V.a {
        try {
            C1181c c1181c = new C1181c();
            C1179a c1179a = new C1179a();
            T.a().a(c1179a);
            ControllerAccess.initialize(c1179a, c1181c);
            ControllerAccess.getInstance().setConfigurationNameProvider(new C1191m());
            ControllerAccess.getInstance().setMathExpressionEvaluator(new C1192n());
            ControllerAccess.getInstance().setUiComponentServerProvider(C1195q.a());
            ControllerAccess.getInstance().setBurnExecutor(new C1184f());
        } catch (Exception e2) {
            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Failed to initialize Plugin Server, error:\n" + e2.getMessage());
        }
    }

    public ApplicationPlugin a(String str) {
        try {
            ApplicationPlugin applicationPlugin = (ApplicationPlugin) ((Class) this.f8257b.get(str)).newInstance();
            applicationPlugin.initialize(ControllerAccess.getInstance());
            return applicationPlugin;
        } catch (IllegalAccessException e2) {
            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Unable to create Plugin: " + str);
        } catch (InstantiationException e3) {
            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new V.a("Unable to create Plugin: " + str);
        } catch (Exception e4) {
            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            throw new V.a("Plugin Not Found: " + str);
        }
    }

    public Collection c() {
        return this.f8258c.values();
    }

    public ApplicationPlugin b(String str) {
        return (ApplicationPlugin) this.f8258c.get(str);
    }

    public void d() {
        File[] fileArrListFiles = C1807j.b().listFiles(new C1193o(this));
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            try {
                c(fileArrListFiles[i2]);
            } catch (Exception e2) {
                C.a("Failed to initialize plugin in file:\n" + fileArrListFiles[i2].getAbsolutePath(), e2, null);
            }
        }
    }

    private void c(File file) {
        AutoCloseable autoCloseable = null;
        C.c("Loading Plugin from " + file.getName());
        try {
            try {
                try {
                    try {
                        try {
                            JarInputStream jarInputStream = new JarInputStream(new BufferedInputStream(new FileInputStream(file)));
                            Manifest manifest = jarInputStream.getManifest();
                            String value = manifest.getMainAttributes().getValue("ApplicationPlugin");
                            if (value == null || value.length() == 0) {
                                value = manifest.getMainAttributes().getValue("Main-Class");
                            }
                            if (value == null || value.length() == 0) {
                                throw new V.a("ApplicationPlugin not defined in Manifest for jar:\n" + file.getAbsolutePath());
                            }
                            try {
                                Class<? extends U> clsAsSubclass = Class.forName(value, true, URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader())).asSubclass(ApplicationPlugin.class);
                                try {
                                    ApplicationPlugin applicationPlugin = (ApplicationPlugin) clsAsSubclass.newInstance();
                                    if (applicationPlugin.getRequiredPluginSpec() > 1.0d) {
                                        throw new V.a("Plugin requires Specification: " + applicationPlugin.getRequiredPluginSpec() + "\nCurrently using: 1.0");
                                    }
                                    this.f8257b.put(applicationPlugin.getIdName(), clsAsSubclass);
                                    this.f8258c.put(applicationPlugin.getIdName(), applicationPlugin);
                                    this.f8259d.put(applicationPlugin.getIdName(), file);
                                    if (jarInputStream != null) {
                                        try {
                                            jarInputStream.close();
                                        } catch (IOException e2) {
                                            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                                        }
                                    }
                                } catch (IllegalAccessException e3) {
                                    Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                                    throw new V.a("Failed to instantiate '" + value + "' in jar file:\n" + file.getAbsolutePath());
                                } catch (InstantiationException e4) {
                                    Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                                    throw new V.a("Failed to instantiate '" + value + "' in jar file:\n" + file.getAbsolutePath());
                                }
                            } catch (ClassNotFoundException e5) {
                                throw new V.a("ApplicationPlugin class '" + value + "' not found in jar file:\n" + file.getAbsolutePath());
                            }
                        } catch (FileNotFoundException e6) {
                            throw new V.a("Plugin file not found: " + file.getAbsolutePath());
                        } catch (IOException e7) {
                            throw new V.a("Error reading Plugin file: " + file.getAbsolutePath());
                        }
                    } catch (Exception e8) {
                        e8.printStackTrace();
                        throw new V.a("Error loading Plugin file: " + file.getAbsolutePath());
                    }
                } catch (AbstractMethodError e9) {
                    e9.printStackTrace();
                    throw new V.a("Error calling core Plugin API method: \n" + e9.getMessage() + "\nOn Plugin in file:\n" + file.getName());
                }
            } catch (Error e10) {
                e10.printStackTrace();
                throw new V.a("This jar file does not meet minimum Plugin API Specifications: \n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (IOException e11) {
                    Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e11);
                    throw th;
                }
            }
            throw th;
        }
    }

    public ApplicationPlugin a(File file) {
        AutoCloseable autoCloseable = null;
        C.c("Loading Plugin from " + file.getName());
        try {
            try {
                JarInputStream jarInputStream = new JarInputStream(new BufferedInputStream(new FileInputStream(file)));
                Manifest manifest = jarInputStream.getManifest();
                String value = manifest.getMainAttributes().getValue("ApplicationPlugin");
                if (value == null || value.length() == 0) {
                    value = manifest.getMainAttributes().getValue("Main-Class");
                }
                if (value == null || value.length() == 0) {
                    throw new V.a("File does not appear to be a valid " + C1798a.f13268b + " Plugin\n ApplicationPlugin not defined in Manifest of jar file:\n" + file.getAbsolutePath());
                }
                try {
                    try {
                        ApplicationPlugin applicationPlugin = (ApplicationPlugin) Class.forName(value, true, URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader())).asSubclass(ApplicationPlugin.class).newInstance();
                        try {
                            jarInputStream.close();
                        } catch (IOException e2) {
                            Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                        }
                        return applicationPlugin;
                    } catch (IllegalAccessException e3) {
                        Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        throw new V.a("Failed to instantiate '" + value + "' in jar file:\n" + file.getAbsolutePath());
                    } catch (InstantiationException e4) {
                        Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                        throw new V.a("Failed to instantiate '" + value + "' in jar file:\n" + file.getAbsolutePath());
                    }
                } catch (ClassNotFoundException e5) {
                    throw new V.a("ApplicationPlugin class '" + value + "' not found in jar file:\n" + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e6) {
                throw new V.a("Plugin file not found: " + file.getAbsolutePath());
            } catch (IOException e7) {
                throw new V.a("Error reading Plugin file: " + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            try {
                autoCloseable.close();
            } catch (IOException e8) {
                Logger.getLogger(C1190l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            }
            throw th;
        }
    }

    public boolean c(String str) {
        return this.f8257b.get(str) != null;
    }

    public void d(String str) {
        File file = (File) this.f8259d.get(str);
        if (file == null || !file.exists()) {
            throw new V.a("Plugin " + str + " not installed or file name changed.");
        }
        this.f8257b.remove(str);
        this.f8258c.remove(str);
        c(file);
    }

    public File e(String str) {
        return (File) this.f8259d.get(str);
    }

    public boolean b(File file) {
        return this.f8259d.containsValue(file);
    }
}
