package com.sun.org.apache.xml.internal.resolver;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.SAXParserFactory;
import jdk.xml.internal.JdkXmlUtils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/Resolver.class */
public class Resolver extends Catalog {
    public static final int URISUFFIX = CatalogEntry.addEntryType("URISUFFIX", 2);
    public static final int SYSTEMSUFFIX = CatalogEntry.addEntryType("SYSTEMSUFFIX", 2);
    public static final int RESOLVER = CatalogEntry.addEntryType("RESOLVER", 1);
    public static final int SYSTEMREVERSE = CatalogEntry.addEntryType("SYSTEMREVERSE", 1);

    @Override // com.sun.org.apache.xml.internal.resolver.Catalog
    public void setupReaders() {
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        spf.setValidating(false);
        SAXCatalogReader saxReader = new SAXCatalogReader(spf);
        saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
        saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName, "catalog", "com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader");
        addReader(XMLCodec.XML_APPLICATION_MIME_TYPE, saxReader);
        TR9401CatalogReader textReader = new TR9401CatalogReader();
        addReader(Clipboard.TEXT_TYPE, textReader);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.Catalog
    public void addEntry(CatalogEntry entry) throws ArrayIndexOutOfBoundsException {
        int type = entry.getEntryType();
        if (type == URISUFFIX) {
            String suffix = normalizeURI(entry.getEntryArg(0));
            String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "URISUFFIX", suffix, fsi);
        } else if (type == SYSTEMSUFFIX) {
            String suffix2 = normalizeURI(entry.getEntryArg(0));
            String fsi2 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi2);
            this.catalogManager.debug.message(4, "SYSTEMSUFFIX", suffix2, fsi2);
        }
        super.addEntry(entry);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.Catalog
    public String resolveURI(String uri) throws IOException {
        String resolved = super.resolveURI(uri);
        if (resolved != null) {
            return resolved;
        }
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement2();
            if (e2.getEntryType() == RESOLVER) {
                String resolved2 = resolveExternalSystem(uri, e2.getEntryArg(0));
                if (resolved2 != null) {
                    return resolved2;
                }
            } else if (e2.getEntryType() == URISUFFIX) {
                String suffix = e2.getEntryArg(0);
                String result = e2.getEntryArg(1);
                if (suffix.length() <= uri.length() && uri.substring(uri.length() - suffix.length()).equals(suffix)) {
                    return result;
                }
            } else {
                continue;
            }
        }
        return resolveSubordinateCatalogs(Catalog.URI, null, null, uri);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.Catalog
    public String resolveSystem(String systemId) throws IOException {
        String resolved = super.resolveSystem(systemId);
        if (resolved != null) {
            return resolved;
        }
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement2();
            if (e2.getEntryType() == RESOLVER) {
                String resolved2 = resolveExternalSystem(systemId, e2.getEntryArg(0));
                if (resolved2 != null) {
                    return resolved2;
                }
            } else if (e2.getEntryType() == SYSTEMSUFFIX) {
                String suffix = e2.getEntryArg(0);
                String result = e2.getEntryArg(1);
                if (suffix.length() <= systemId.length() && systemId.substring(systemId.length() - suffix.length()).equals(suffix)) {
                    return result;
                }
            } else {
                continue;
            }
        }
        return resolveSubordinateCatalogs(Catalog.SYSTEM, null, null, systemId);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.Catalog
    public String resolvePublic(String publicId, String systemId) throws IOException {
        String resolved;
        String resolved2 = super.resolvePublic(publicId, systemId);
        if (resolved2 != null) {
            return resolved2;
        }
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement2();
            if (e2.getEntryType() == RESOLVER) {
                if (systemId != null && (resolved = resolveExternalSystem(systemId, e2.getEntryArg(0))) != null) {
                    return resolved;
                }
                String resolved3 = resolveExternalPublic(publicId, e2.getEntryArg(0));
                if (resolved3 != null) {
                    return resolved3;
                }
            }
        }
        return resolveSubordinateCatalogs(Catalog.PUBLIC, null, publicId, systemId);
    }

    protected String resolveExternalSystem(String systemId, String resolver) throws IOException {
        Resolver r2 = queryResolver(resolver, "i2l", systemId, null);
        if (r2 != null) {
            return r2.resolveSystem(systemId);
        }
        return null;
    }

    protected String resolveExternalPublic(String publicId, String resolver) throws IOException {
        Resolver r2 = queryResolver(resolver, "fpi2l", publicId, null);
        if (r2 != null) {
            return r2.resolvePublic(publicId, null);
        }
        return null;
    }

    protected Resolver queryResolver(String resolver, String command, String arg1, String arg2) {
        String RFC2483 = resolver + "?command=" + command + "&format=tr9401&uri=" + arg1 + "&uri2=" + arg2;
        try {
            URL url = new URL(RFC2483);
            URLConnection urlCon = url.openConnection();
            urlCon.setUseCaches(false);
            Resolver r2 = (Resolver) newCatalog();
            String cType = urlCon.getContentType();
            if (cType.indexOf(";") > 0) {
                cType = cType.substring(0, cType.indexOf(";"));
            }
            r2.parseCatalog(cType, urlCon.getInputStream());
            return r2;
        } catch (CatalogException cex) {
            if (cex.getExceptionType() == 6) {
                this.catalogManager.debug.message(1, "Unparseable catalog: " + RFC2483);
                return null;
            }
            if (cex.getExceptionType() == 5) {
                this.catalogManager.debug.message(1, "Unknown catalog format: " + RFC2483);
                return null;
            }
            return null;
        } catch (MalformedURLException e2) {
            this.catalogManager.debug.message(1, "Malformed resolver URL: " + RFC2483);
            return null;
        } catch (IOException e3) {
            this.catalogManager.debug.message(1, "I/O Exception opening resolver: " + RFC2483);
            return null;
        }
    }

    private Vector appendVector(Vector vec, Vector appvec) {
        if (appvec != null) {
            for (int count = 0; count < appvec.size(); count++) {
                vec.addElement(appvec.elementAt(count));
            }
        }
        return vec;
    }

    public Vector resolveAllSystemReverse(String systemId) throws IOException {
        Vector resolved = new Vector();
        if (systemId != null) {
            Vector localResolved = resolveLocalSystemReverse(systemId);
            resolved = appendVector(resolved, localResolved);
        }
        Vector subResolved = resolveAllSubordinateCatalogs(SYSTEMREVERSE, null, null, systemId);
        return appendVector(resolved, subResolved);
    }

    public String resolveSystemReverse(String systemId) throws IOException {
        Vector resolved = resolveAllSystemReverse(systemId);
        if (resolved != null && resolved.size() > 0) {
            return (String) resolved.elementAt(0);
        }
        return null;
    }

    public Vector resolveAllSystem(String systemId) throws IOException {
        Vector resolutions = new Vector();
        if (systemId != null) {
            Vector localResolutions = resolveAllLocalSystem(systemId);
            resolutions = appendVector(resolutions, localResolutions);
        }
        Vector subResolutions = resolveAllSubordinateCatalogs(SYSTEM, null, null, systemId);
        Vector resolutions2 = appendVector(resolutions, subResolutions);
        if (resolutions2.size() > 0) {
            return resolutions2;
        }
        return null;
    }

    private Vector resolveAllLocalSystem(String systemId) {
        Vector map = new Vector();
        String osname = SecuritySupport.getSystemProperty("os.name");
        boolean windows = osname.indexOf("Windows") >= 0;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement2();
            if (e2.getEntryType() == SYSTEM && (e2.getEntryArg(0).equals(systemId) || (windows && e2.getEntryArg(0).equalsIgnoreCase(systemId)))) {
                map.addElement(e2.getEntryArg(1));
            }
        }
        if (map.size() == 0) {
            return null;
        }
        return map;
    }

    private Vector resolveLocalSystemReverse(String systemId) {
        Vector map = new Vector();
        String osname = SecuritySupport.getSystemProperty("os.name");
        boolean windows = osname.indexOf("Windows") >= 0;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement2();
            if (e2.getEntryType() == SYSTEM && (e2.getEntryArg(1).equals(systemId) || (windows && e2.getEntryArg(1).equalsIgnoreCase(systemId)))) {
                map.addElement(e2.getEntryArg(0));
            }
        }
        if (map.size() == 0) {
            return null;
        }
        return map;
    }

    private synchronized Vector resolveAllSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId) throws IOException {
        Resolver c2;
        Vector resolutions = new Vector();
        int catPos = 0;
        while (true) {
            if (catPos >= this.catalogs.size()) {
                break;
            }
            try {
                c2 = (Resolver) this.catalogs.elementAt(catPos);
            } catch (ClassCastException e2) {
                String catfile = (String) this.catalogs.elementAt(catPos);
                c2 = (Resolver) newCatalog();
                try {
                    c2.parseCatalog(catfile);
                } catch (FileNotFoundException e3) {
                    this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
                } catch (MalformedURLException e4) {
                    this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
                } catch (IOException e5) {
                    this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
                }
                this.catalogs.setElementAt(c2, catPos);
            }
            if (entityType == DOCTYPE) {
                String resolved = c2.resolveDoctype(entityName, publicId, systemId);
                if (resolved != null) {
                    resolutions.addElement(resolved);
                    return resolutions;
                }
            } else if (entityType == DOCUMENT) {
                String resolved2 = c2.resolveDocument();
                if (resolved2 != null) {
                    resolutions.addElement(resolved2);
                    return resolutions;
                }
            } else if (entityType == ENTITY) {
                String resolved3 = c2.resolveEntity(entityName, publicId, systemId);
                if (resolved3 != null) {
                    resolutions.addElement(resolved3);
                    return resolutions;
                }
            } else if (entityType == NOTATION) {
                String resolved4 = c2.resolveNotation(entityName, publicId, systemId);
                if (resolved4 != null) {
                    resolutions.addElement(resolved4);
                    return resolutions;
                }
            } else if (entityType == PUBLIC) {
                String resolved5 = c2.resolvePublic(publicId, systemId);
                if (resolved5 != null) {
                    resolutions.addElement(resolved5);
                    return resolutions;
                }
            } else {
                if (entityType == SYSTEM) {
                    Vector localResolutions = c2.resolveAllSystem(systemId);
                    resolutions = appendVector(resolutions, localResolutions);
                    break;
                }
                if (entityType == SYSTEMREVERSE) {
                    Vector localResolutions2 = c2.resolveAllSystemReverse(systemId);
                    resolutions = appendVector(resolutions, localResolutions2);
                }
            }
            catPos++;
        }
        if (resolutions != null) {
            return resolutions;
        }
        return null;
    }
}
