package com.sun.org.apache.xml.internal.resolver;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import org.icepdf.core.util.PdfOps;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/CatalogManager.class */
public class CatalogManager {
    private BootstrapResolver bResolver = new BootstrapResolver();
    private boolean ignoreMissingProperties;
    private ResourceBundle resources;
    private String propertyFile;
    private URL propertyFileURI;
    private String defaultCatalogFiles;
    private String catalogFiles;
    private boolean fromPropertiesFile;
    private int defaultVerbosity;
    private Integer verbosity;
    private boolean defaultPreferPublic;
    private Boolean preferPublic;
    private boolean defaultUseStaticCatalog;
    private Boolean useStaticCatalog;
    private boolean defaultOasisXMLCatalogPI;
    private Boolean oasisXMLCatalogPI;
    private boolean defaultRelativeCatalogs;
    private Boolean relativeCatalogs;
    private String catalogClassName;
    private boolean overrideDefaultParser;
    public Debug debug;
    private static String pFiles = "xml.catalog.files";
    private static String pVerbosity = "xml.catalog.verbosity";
    private static String pPrefer = "xml.catalog.prefer";
    private static String pStatic = "xml.catalog.staticCatalog";
    private static String pAllowPI = "xml.catalog.allowPI";
    private static String pClassname = "xml.catalog.className";
    private static String pIgnoreMissing = "xml.catalog.ignoreMissing";
    private static CatalogManager staticManager = new CatalogManager();
    private static Catalog staticCatalog = null;

    public CatalogManager() {
        this.ignoreMissingProperties = (SecuritySupport.getSystemProperty(pIgnoreMissing) == null && SecuritySupport.getSystemProperty(pFiles) == null) ? false : true;
        this.propertyFile = "CatalogManager.properties";
        this.propertyFileURI = null;
        this.defaultCatalogFiles = "./xcatalog";
        this.catalogFiles = null;
        this.fromPropertiesFile = false;
        this.defaultVerbosity = 1;
        this.verbosity = null;
        this.defaultPreferPublic = true;
        this.preferPublic = null;
        this.defaultUseStaticCatalog = true;
        this.useStaticCatalog = null;
        this.defaultOasisXMLCatalogPI = true;
        this.oasisXMLCatalogPI = null;
        this.defaultRelativeCatalogs = true;
        this.relativeCatalogs = null;
        this.catalogClassName = null;
        this.debug = null;
        init();
    }

    public CatalogManager(String propertyFile) {
        this.ignoreMissingProperties = (SecuritySupport.getSystemProperty(pIgnoreMissing) == null && SecuritySupport.getSystemProperty(pFiles) == null) ? false : true;
        this.propertyFile = "CatalogManager.properties";
        this.propertyFileURI = null;
        this.defaultCatalogFiles = "./xcatalog";
        this.catalogFiles = null;
        this.fromPropertiesFile = false;
        this.defaultVerbosity = 1;
        this.verbosity = null;
        this.defaultPreferPublic = true;
        this.preferPublic = null;
        this.defaultUseStaticCatalog = true;
        this.useStaticCatalog = null;
        this.defaultOasisXMLCatalogPI = true;
        this.oasisXMLCatalogPI = null;
        this.defaultRelativeCatalogs = true;
        this.relativeCatalogs = null;
        this.catalogClassName = null;
        this.debug = null;
        this.propertyFile = propertyFile;
        init();
    }

    private void init() {
        this.debug = new Debug();
        if (System.getSecurityManager() == null) {
            this.overrideDefaultParser = true;
        }
    }

    public void setBootstrapResolver(BootstrapResolver resolver) {
        this.bResolver = resolver;
    }

    public BootstrapResolver getBootstrapResolver() {
        return this.bResolver;
    }

    private synchronized void readProperties() {
        InputStream in;
        try {
            this.propertyFileURI = CatalogManager.class.getResource("/" + this.propertyFile);
            in = CatalogManager.class.getResourceAsStream("/" + this.propertyFile);
        } catch (IOException e2) {
            if (!this.ignoreMissingProperties) {
                System.err.println("Failure trying to read " + this.propertyFile);
            }
        } catch (MissingResourceException e3) {
            if (!this.ignoreMissingProperties) {
                System.err.println("Cannot read " + this.propertyFile);
            }
        }
        if (in == null) {
            if (!this.ignoreMissingProperties) {
                System.err.println("Cannot find " + this.propertyFile);
                this.ignoreMissingProperties = true;
                return;
            }
            return;
        }
        this.resources = new PropertyResourceBundle(in);
        if (this.verbosity == null) {
            try {
                String verbStr = this.resources.getString("verbosity");
                int verb = Integer.parseInt(verbStr.trim());
                this.debug.setDebug(verb);
                this.verbosity = new Integer(verb);
            } catch (Exception e4) {
            }
        }
    }

    public static CatalogManager getStaticManager() {
        return staticManager;
    }

    public boolean getIgnoreMissingProperties() {
        return this.ignoreMissingProperties;
    }

    public void setIgnoreMissingProperties(boolean ignore) {
        this.ignoreMissingProperties = ignore;
    }

    public void ignoreMissingProperties(boolean ignore) {
        setIgnoreMissingProperties(ignore);
    }

    private int queryVerbosity() {
        String defaultVerbStr = Integer.toString(this.defaultVerbosity);
        String verbStr = SecuritySupport.getSystemProperty(pVerbosity);
        if (verbStr == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources != null) {
                try {
                    verbStr = this.resources.getString("verbosity");
                } catch (MissingResourceException e2) {
                    verbStr = defaultVerbStr;
                }
            } else {
                verbStr = defaultVerbStr;
            }
        }
        int verb = this.defaultVerbosity;
        try {
            verb = Integer.parseInt(verbStr.trim());
        } catch (Exception e3) {
            System.err.println("Cannot parse verbosity: \"" + verbStr + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (this.verbosity == null) {
            this.debug.setDebug(verb);
            this.verbosity = new Integer(verb);
        }
        return verb;
    }

    public int getVerbosity() {
        if (this.verbosity == null) {
            this.verbosity = new Integer(queryVerbosity());
        }
        return this.verbosity.intValue();
    }

    public void setVerbosity(int verbosity) {
        this.verbosity = new Integer(verbosity);
        this.debug.setDebug(verbosity);
    }

    public int verbosity() {
        return getVerbosity();
    }

    private boolean queryRelativeCatalogs() {
        if (this.resources == null) {
            readProperties();
        }
        if (this.resources == null) {
            return this.defaultRelativeCatalogs;
        }
        try {
            String allow = this.resources.getString("relative-catalogs");
            if (!allow.equalsIgnoreCase("true") && !allow.equalsIgnoreCase("yes")) {
                if (!allow.equalsIgnoreCase("1")) {
                    return false;
                }
            }
            return true;
        } catch (MissingResourceException e2) {
            return this.defaultRelativeCatalogs;
        }
    }

    public boolean getRelativeCatalogs() {
        if (this.relativeCatalogs == null) {
            this.relativeCatalogs = new Boolean(queryRelativeCatalogs());
        }
        return this.relativeCatalogs.booleanValue();
    }

    public void setRelativeCatalogs(boolean relative) {
        this.relativeCatalogs = new Boolean(relative);
    }

    public boolean relativeCatalogs() {
        return getRelativeCatalogs();
    }

    private String queryCatalogFiles() {
        String catalogList = SecuritySupport.getSystemProperty(pFiles);
        this.fromPropertiesFile = false;
        if (catalogList == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources != null) {
                try {
                    catalogList = this.resources.getString("catalogs");
                    this.fromPropertiesFile = true;
                } catch (MissingResourceException e2) {
                    System.err.println(this.propertyFile + ": catalogs not found.");
                    catalogList = null;
                }
            }
        }
        if (catalogList == null) {
            catalogList = this.defaultCatalogFiles;
        }
        return catalogList;
    }

    public Vector getCatalogFiles() {
        if (this.catalogFiles == null) {
            this.catalogFiles = queryCatalogFiles();
        }
        StringTokenizer files = new StringTokenizer(this.catalogFiles, ";");
        Vector catalogs = new Vector();
        while (files.hasMoreTokens()) {
            String catalogFile = files.nextToken();
            if (this.fromPropertiesFile && !relativeCatalogs()) {
                try {
                    URL absURI = new URL(this.propertyFileURI, catalogFile);
                    catalogFile = absURI.toString();
                } catch (MalformedURLException e2) {
                }
            }
            catalogs.add(catalogFile);
        }
        return catalogs;
    }

    public void setCatalogFiles(String fileList) {
        this.catalogFiles = fileList;
        this.fromPropertiesFile = false;
    }

    public Vector catalogFiles() {
        return getCatalogFiles();
    }

    private boolean queryPreferPublic() {
        String prefer = SecuritySupport.getSystemProperty(pPrefer);
        if (prefer == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources == null) {
                return this.defaultPreferPublic;
            }
            try {
                prefer = this.resources.getString("prefer");
            } catch (MissingResourceException e2) {
                return this.defaultPreferPublic;
            }
        }
        if (prefer == null) {
            return this.defaultPreferPublic;
        }
        return prefer.equalsIgnoreCase("public");
    }

    public boolean getPreferPublic() {
        if (this.preferPublic == null) {
            this.preferPublic = new Boolean(queryPreferPublic());
        }
        return this.preferPublic.booleanValue();
    }

    public void setPreferPublic(boolean preferPublic) {
        this.preferPublic = new Boolean(preferPublic);
    }

    public boolean preferPublic() {
        return getPreferPublic();
    }

    private boolean queryUseStaticCatalog() {
        String staticCatalog2 = SecuritySupport.getSystemProperty(pStatic);
        if (staticCatalog2 == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources == null) {
                return this.defaultUseStaticCatalog;
            }
            try {
                staticCatalog2 = this.resources.getString("static-catalog");
            } catch (MissingResourceException e2) {
                return this.defaultUseStaticCatalog;
            }
        }
        if (staticCatalog2 == null) {
            return this.defaultUseStaticCatalog;
        }
        return staticCatalog2.equalsIgnoreCase("true") || staticCatalog2.equalsIgnoreCase("yes") || staticCatalog2.equalsIgnoreCase("1");
    }

    public boolean getUseStaticCatalog() {
        if (this.useStaticCatalog == null) {
            this.useStaticCatalog = new Boolean(queryUseStaticCatalog());
        }
        return this.useStaticCatalog.booleanValue();
    }

    public void setUseStaticCatalog(boolean useStatic) {
        this.useStaticCatalog = new Boolean(useStatic);
    }

    public boolean staticCatalog() {
        return getUseStaticCatalog();
    }

    public Catalog getPrivateCatalog() {
        Catalog catalog = staticCatalog;
        if (this.useStaticCatalog == null) {
            this.useStaticCatalog = new Boolean(getUseStaticCatalog());
        }
        if (catalog == null || !this.useStaticCatalog.booleanValue()) {
            try {
                String catalogClassName = getCatalogClassName();
                if (catalogClassName == null) {
                    catalog = new Catalog();
                } else {
                    try {
                        try {
                            catalog = (Catalog) ReflectUtil.forName(catalogClassName).newInstance();
                        } catch (ClassNotFoundException e2) {
                            this.debug.message(1, "Catalog class named '" + catalogClassName + "' could not be found. Using default.");
                            catalog = new Catalog();
                        }
                    } catch (ClassCastException e3) {
                        this.debug.message(1, "Class named '" + catalogClassName + "' is not a Catalog. Using default.");
                        catalog = new Catalog();
                    }
                }
                catalog.setCatalogManager(this);
                catalog.setupReaders();
                catalog.loadSystemCatalogs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (this.useStaticCatalog.booleanValue()) {
                staticCatalog = catalog;
            }
        }
        return catalog;
    }

    public Catalog getCatalog() {
        Catalog catalog = staticCatalog;
        if (this.useStaticCatalog == null) {
            this.useStaticCatalog = new Boolean(getUseStaticCatalog());
        }
        if (catalog == null || !this.useStaticCatalog.booleanValue()) {
            catalog = getPrivateCatalog();
            if (this.useStaticCatalog.booleanValue()) {
                staticCatalog = catalog;
            }
        }
        return catalog;
    }

    public boolean queryAllowOasisXMLCatalogPI() {
        String allow = SecuritySupport.getSystemProperty(pAllowPI);
        if (allow == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources == null) {
                return this.defaultOasisXMLCatalogPI;
            }
            try {
                allow = this.resources.getString("allow-oasis-xml-catalog-pi");
            } catch (MissingResourceException e2) {
                return this.defaultOasisXMLCatalogPI;
            }
        }
        if (allow == null) {
            return this.defaultOasisXMLCatalogPI;
        }
        return allow.equalsIgnoreCase("true") || allow.equalsIgnoreCase("yes") || allow.equalsIgnoreCase("1");
    }

    public boolean getAllowOasisXMLCatalogPI() {
        if (this.oasisXMLCatalogPI == null) {
            this.oasisXMLCatalogPI = new Boolean(queryAllowOasisXMLCatalogPI());
        }
        return this.oasisXMLCatalogPI.booleanValue();
    }

    public boolean overrideDefaultParser() {
        return this.overrideDefaultParser;
    }

    public void setAllowOasisXMLCatalogPI(boolean allowPI) {
        this.oasisXMLCatalogPI = new Boolean(allowPI);
    }

    public boolean allowOasisXMLCatalogPI() {
        return getAllowOasisXMLCatalogPI();
    }

    public String queryCatalogClassName() {
        String className = SecuritySupport.getSystemProperty(pClassname);
        if (className == null) {
            if (this.resources == null) {
                readProperties();
            }
            if (this.resources == null) {
                return null;
            }
            try {
                return this.resources.getString("catalog-class-name");
            } catch (MissingResourceException e2) {
                return null;
            }
        }
        return className;
    }

    public String getCatalogClassName() {
        if (this.catalogClassName == null) {
            this.catalogClassName = queryCatalogClassName();
        }
        return this.catalogClassName;
    }

    public void setCatalogClassName(String className) {
        this.catalogClassName = className;
    }

    public String catalogClassName() {
        return getCatalogClassName();
    }
}
