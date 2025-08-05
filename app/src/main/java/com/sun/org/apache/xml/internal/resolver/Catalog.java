package com.sun.org.apache.xml.internal.resolver;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import com.sun.org.apache.xml.internal.resolver.readers.CatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.SAXParserFactory;
import jdk.xml.internal.JdkXmlUtils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/Catalog.class */
public class Catalog {
    public static final int BASE = CatalogEntry.addEntryType("BASE", 1);
    public static final int CATALOG = CatalogEntry.addEntryType("CATALOG", 1);
    public static final int DOCUMENT = CatalogEntry.addEntryType("DOCUMENT", 1);
    public static final int OVERRIDE = CatalogEntry.addEntryType("OVERRIDE", 1);
    public static final int SGMLDECL = CatalogEntry.addEntryType("SGMLDECL", 1);
    public static final int DELEGATE_PUBLIC = CatalogEntry.addEntryType("DELEGATE_PUBLIC", 2);
    public static final int DELEGATE_SYSTEM = CatalogEntry.addEntryType("DELEGATE_SYSTEM", 2);
    public static final int DELEGATE_URI = CatalogEntry.addEntryType("DELEGATE_URI", 2);
    public static final int DOCTYPE = CatalogEntry.addEntryType("DOCTYPE", 2);
    public static final int DTDDECL = CatalogEntry.addEntryType("DTDDECL", 2);
    public static final int ENTITY = CatalogEntry.addEntryType(SchemaSymbols.ATTVAL_ENTITY, 2);
    public static final int LINKTYPE = CatalogEntry.addEntryType("LINKTYPE", 2);
    public static final int NOTATION = CatalogEntry.addEntryType(SchemaSymbols.ATTVAL_NOTATION, 2);
    public static final int PUBLIC = CatalogEntry.addEntryType("PUBLIC", 2);
    public static final int SYSTEM = CatalogEntry.addEntryType(Clipboard.SYSTEM, 2);
    public static final int URI = CatalogEntry.addEntryType(Constants._ATT_URI, 2);
    public static final int REWRITE_SYSTEM = CatalogEntry.addEntryType("REWRITE_SYSTEM", 2);
    public static final int REWRITE_URI = CatalogEntry.addEntryType("REWRITE_URI", 2);
    public static final int SYSTEM_SUFFIX = CatalogEntry.addEntryType("SYSTEM_SUFFIX", 2);
    public static final int URI_SUFFIX = CatalogEntry.addEntryType("URI_SUFFIX", 2);
    protected URL base;
    protected URL catalogCwd;
    protected Vector catalogEntries;
    protected boolean default_override;
    protected CatalogManager catalogManager;
    protected Vector catalogFiles;
    protected Vector localCatalogFiles;
    protected Vector catalogs;
    protected Vector localDelegate;
    protected Map<String, Integer> readerMap;
    protected Vector readerArr;

    public Catalog() {
        this.catalogEntries = new Vector();
        this.default_override = true;
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogFiles = new Vector();
        this.localCatalogFiles = new Vector();
        this.catalogs = new Vector();
        this.localDelegate = new Vector();
        this.readerMap = new HashMap();
        this.readerArr = new Vector();
    }

    public Catalog(CatalogManager manager) {
        this.catalogEntries = new Vector();
        this.default_override = true;
        this.catalogManager = CatalogManager.getStaticManager();
        this.catalogFiles = new Vector();
        this.localCatalogFiles = new Vector();
        this.catalogs = new Vector();
        this.localDelegate = new Vector();
        this.readerMap = new HashMap();
        this.readerArr = new Vector();
        this.catalogManager = manager;
    }

    public CatalogManager getCatalogManager() {
        return this.catalogManager;
    }

    public void setCatalogManager(CatalogManager manager) {
        this.catalogManager = manager;
    }

    public void setupReaders() {
        SAXParserFactory spf = JdkXmlUtils.getSAXFactory(this.catalogManager.overrideDefaultParser());
        spf.setValidating(false);
        SAXCatalogReader saxReader = new SAXCatalogReader(spf);
        saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
        saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName, "catalog", "com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
        addReader(XMLCodec.XML_APPLICATION_MIME_TYPE, saxReader);
        TR9401CatalogReader textReader = new TR9401CatalogReader();
        addReader(Clipboard.TEXT_TYPE, textReader);
    }

    public void addReader(String mimeType, CatalogReader reader) {
        if (this.readerMap.containsKey(mimeType)) {
            Integer pos = this.readerMap.get(mimeType);
            this.readerArr.set(pos.intValue(), reader);
        } else {
            this.readerArr.add(reader);
            Integer pos2 = Integer.valueOf(this.readerArr.size() - 1);
            this.readerMap.put(mimeType, pos2);
        }
    }

    protected void copyReaders(Catalog newCatalog) {
        Vector mapArr = new Vector(this.readerMap.size());
        for (int count = 0; count < this.readerMap.size(); count++) {
            mapArr.add(null);
        }
        for (Map.Entry<String, Integer> entry : this.readerMap.entrySet()) {
            mapArr.set(entry.getValue().intValue(), entry.getKey());
        }
        for (int count2 = 0; count2 < mapArr.size(); count2++) {
            String mimeType = (String) mapArr.get(count2);
            Integer pos = this.readerMap.get(mimeType);
            newCatalog.addReader(mimeType, (CatalogReader) this.readerArr.get(pos.intValue()));
        }
    }

    protected Catalog newCatalog() {
        String catalogClass = getClass().getName();
        try {
            Catalog c2 = (Catalog) Class.forName(catalogClass).newInstance();
            c2.setCatalogManager(this.catalogManager);
            copyReaders(c2);
            return c2;
        } catch (ClassCastException e2) {
            this.catalogManager.debug.message(1, "Class Cast Exception: " + catalogClass);
            Catalog c3 = new Catalog();
            c3.setCatalogManager(this.catalogManager);
            copyReaders(c3);
            return c3;
        } catch (ClassNotFoundException e3) {
            this.catalogManager.debug.message(1, "Class Not Found Exception: " + catalogClass);
            Catalog c32 = new Catalog();
            c32.setCatalogManager(this.catalogManager);
            copyReaders(c32);
            return c32;
        } catch (IllegalAccessException e4) {
            this.catalogManager.debug.message(1, "Illegal Access Exception: " + catalogClass);
            Catalog c322 = new Catalog();
            c322.setCatalogManager(this.catalogManager);
            copyReaders(c322);
            return c322;
        } catch (InstantiationException e5) {
            this.catalogManager.debug.message(1, "Instantiation Exception: " + catalogClass);
            Catalog c3222 = new Catalog();
            c3222.setCatalogManager(this.catalogManager);
            copyReaders(c3222);
            return c3222;
        } catch (Exception e6) {
            this.catalogManager.debug.message(1, "Other Exception: " + catalogClass);
            Catalog c32222 = new Catalog();
            c32222.setCatalogManager(this.catalogManager);
            copyReaders(c32222);
            return c32222;
        }
    }

    public String getCurrentBase() {
        return this.base.toString();
    }

    public String getDefaultOverride() {
        if (this.default_override) {
            return "yes";
        }
        return "no";
    }

    public void loadSystemCatalogs() throws IOException {
        Vector catalogs = this.catalogManager.getCatalogFiles();
        if (catalogs != null) {
            for (int count = 0; count < catalogs.size(); count++) {
                this.catalogFiles.addElement(catalogs.elementAt(count));
            }
        }
        if (this.catalogFiles.size() > 0) {
            String catfile = (String) this.catalogFiles.lastElement();
            this.catalogFiles.removeElement(catfile);
            parseCatalog(catfile);
        }
    }

    public synchronized void parseCatalog(String fileName) throws IOException {
        this.default_override = this.catalogManager.getPreferPublic();
        this.catalogManager.debug.message(4, "Parse catalog: " + fileName);
        this.catalogFiles.addElement(fileName);
        parsePendingCatalogs();
    }

    public synchronized void parseCatalog(String mimeType, InputStream is) throws CatalogException, IOException {
        this.default_override = this.catalogManager.getPreferPublic();
        this.catalogManager.debug.message(4, "Parse " + mimeType + " catalog on input stream");
        CatalogReader reader = null;
        if (this.readerMap.containsKey(mimeType)) {
            int arrayPos = this.readerMap.get(mimeType).intValue();
            reader = (CatalogReader) this.readerArr.get(arrayPos);
        }
        if (reader == null) {
            String msg = "No CatalogReader for MIME type: " + mimeType;
            this.catalogManager.debug.message(2, msg);
            throw new CatalogException(6, msg);
        }
        reader.readCatalog(this, is);
        parsePendingCatalogs();
    }

    public synchronized void parseCatalog(URL aUrl) throws IOException {
        this.catalogCwd = aUrl;
        this.base = aUrl;
        this.default_override = this.catalogManager.getPreferPublic();
        this.catalogManager.debug.message(4, "Parse catalog: " + aUrl.toString());
        boolean parsed = false;
        for (int count = 0; !parsed && count < this.readerArr.size(); count++) {
            CatalogReader reader = (CatalogReader) this.readerArr.get(count);
            try {
                DataInputStream inStream = new DataInputStream(aUrl.openStream());
                try {
                    reader.readCatalog(this, inStream);
                    parsed = true;
                } catch (CatalogException ce) {
                    if (ce.getExceptionType() == 7) {
                        break;
                    }
                }
                try {
                    inStream.close();
                } catch (IOException e2) {
                }
            } catch (FileNotFoundException e3) {
            }
        }
        if (parsed) {
            parsePendingCatalogs();
        }
    }

    protected synchronized void parsePendingCatalogs() throws IOException {
        if (!this.localCatalogFiles.isEmpty()) {
            Vector newQueue = new Vector();
            Enumeration q2 = this.localCatalogFiles.elements();
            while (q2.hasMoreElements()) {
                newQueue.addElement(q2.nextElement());
            }
            for (int curCat = 0; curCat < this.catalogFiles.size(); curCat++) {
                newQueue.addElement((String) this.catalogFiles.elementAt(curCat));
            }
            this.catalogFiles = newQueue;
            this.localCatalogFiles.clear();
        }
        if (this.catalogFiles.isEmpty() && !this.localDelegate.isEmpty()) {
            Enumeration e2 = this.localDelegate.elements();
            while (e2.hasMoreElements()) {
                this.catalogEntries.addElement(e2.nextElement());
            }
            this.localDelegate.clear();
        }
        while (!this.catalogFiles.isEmpty()) {
            String catfile = (String) this.catalogFiles.elementAt(0);
            try {
                this.catalogFiles.remove(0);
            } catch (ArrayIndexOutOfBoundsException e3) {
            }
            if (this.catalogEntries.size() == 0 && this.catalogs.size() == 0) {
                try {
                    parseCatalogFile(catfile);
                } catch (CatalogException ce) {
                    System.out.println("FIXME: " + ce.toString());
                }
            } else {
                this.catalogs.addElement(catfile);
            }
            if (!this.localCatalogFiles.isEmpty()) {
                Vector newQueue2 = new Vector();
                Enumeration q3 = this.localCatalogFiles.elements();
                while (q3.hasMoreElements()) {
                    newQueue2.addElement(q3.nextElement());
                }
                for (int curCat2 = 0; curCat2 < this.catalogFiles.size(); curCat2++) {
                    newQueue2.addElement((String) this.catalogFiles.elementAt(curCat2));
                }
                this.catalogFiles = newQueue2;
                this.localCatalogFiles.clear();
            }
            if (!this.localDelegate.isEmpty()) {
                Enumeration e4 = this.localDelegate.elements();
                while (e4.hasMoreElements()) {
                    this.catalogEntries.addElement(e4.nextElement());
                }
                this.localDelegate.clear();
            }
        }
        this.catalogFiles.clear();
    }

    protected synchronized void parseCatalogFile(String fileName) throws CatalogException, IOException {
        try {
            this.catalogCwd = FileURL.makeURL("basename");
        } catch (MalformedURLException e2) {
            this.catalogManager.debug.message(1, "Malformed URL on cwd", "user.dir");
            this.catalogCwd = null;
        }
        try {
            this.base = new URL(this.catalogCwd, fixSlashes(fileName));
        } catch (MalformedURLException e3) {
            try {
                this.base = new URL("file:" + fixSlashes(fileName));
            } catch (MalformedURLException e4) {
                this.catalogManager.debug.message(1, "Malformed URL on catalog filename", fixSlashes(fileName));
                this.base = null;
            }
        }
        this.catalogManager.debug.message(2, "Loading catalog", fileName);
        this.catalogManager.debug.message(4, "Default BASE", this.base.toString());
        String fileName2 = this.base.toString();
        boolean parsed = false;
        boolean notFound = false;
        for (int count = 0; !parsed && count < this.readerArr.size(); count++) {
            CatalogReader reader = (CatalogReader) this.readerArr.get(count);
            try {
                notFound = false;
                DataInputStream inStream = new DataInputStream(this.base.openStream());
                try {
                    reader.readCatalog(this, inStream);
                    parsed = true;
                } catch (CatalogException ce) {
                    if (ce.getExceptionType() == 7) {
                        break;
                    }
                }
                try {
                    inStream.close();
                } catch (IOException e5) {
                }
            } catch (FileNotFoundException e6) {
                notFound = true;
            }
        }
        if (!parsed) {
            if (notFound) {
                this.catalogManager.debug.message(3, "Catalog does not exist", fileName2);
            } else {
                this.catalogManager.debug.message(1, "Failed to parse catalog", fileName2);
            }
        }
    }

    public void addEntry(CatalogEntry entry) {
        URL newbase;
        int type = entry.getEntryType();
        if (type == BASE) {
            String value = entry.getEntryArg(0);
            if (this.base == null) {
                this.catalogManager.debug.message(5, "BASE CUR", FXMLLoader.NULL_KEYWORD);
            } else {
                this.catalogManager.debug.message(5, "BASE CUR", this.base.toString());
            }
            this.catalogManager.debug.message(4, "BASE STR", value);
            try {
                value = fixSlashes(value);
                newbase = new URL(this.base, value);
            } catch (MalformedURLException e2) {
                try {
                    newbase = new URL("file:" + value);
                } catch (MalformedURLException e3) {
                    this.catalogManager.debug.message(1, "Malformed URL on base", value);
                    newbase = null;
                }
            }
            if (newbase != null) {
                this.base = newbase;
            }
            this.catalogManager.debug.message(5, "BASE NEW", this.base.toString());
            return;
        }
        if (type == CATALOG) {
            String fsi = makeAbsolute(entry.getEntryArg(0));
            this.catalogManager.debug.message(4, "CATALOG", fsi);
            this.localCatalogFiles.addElement(fsi);
            return;
        }
        if (type == PUBLIC) {
            String publicid = PublicId.normalize(entry.getEntryArg(0));
            String systemid = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, publicid);
            entry.setEntryArg(1, systemid);
            this.catalogManager.debug.message(4, "PUBLIC", publicid, systemid);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == SYSTEM) {
            String systemid2 = normalizeURI(entry.getEntryArg(0));
            String fsi2 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi2);
            this.catalogManager.debug.message(4, Clipboard.SYSTEM, systemid2, fsi2);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == URI) {
            String uri = normalizeURI(entry.getEntryArg(0));
            String altURI = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, altURI);
            this.catalogManager.debug.message(4, Constants._ATT_URI, uri, altURI);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == DOCUMENT) {
            String fsi3 = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
            entry.setEntryArg(0, fsi3);
            this.catalogManager.debug.message(4, "DOCUMENT", fsi3);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == OVERRIDE) {
            this.catalogManager.debug.message(4, "OVERRIDE", entry.getEntryArg(0));
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == SGMLDECL) {
            String fsi4 = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
            entry.setEntryArg(0, fsi4);
            this.catalogManager.debug.message(4, "SGMLDECL", fsi4);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == DELEGATE_PUBLIC) {
            String ppi = PublicId.normalize(entry.getEntryArg(0));
            String fsi5 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, ppi);
            entry.setEntryArg(1, fsi5);
            this.catalogManager.debug.message(4, "DELEGATE_PUBLIC", ppi, fsi5);
            addDelegate(entry);
            return;
        }
        if (type == DELEGATE_SYSTEM) {
            String psi = normalizeURI(entry.getEntryArg(0));
            String fsi6 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, psi);
            entry.setEntryArg(1, fsi6);
            this.catalogManager.debug.message(4, "DELEGATE_SYSTEM", psi, fsi6);
            addDelegate(entry);
            return;
        }
        if (type == DELEGATE_URI) {
            String pui = normalizeURI(entry.getEntryArg(0));
            String fsi7 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, pui);
            entry.setEntryArg(1, fsi7);
            this.catalogManager.debug.message(4, "DELEGATE_URI", pui, fsi7);
            addDelegate(entry);
            return;
        }
        if (type == REWRITE_SYSTEM) {
            String psi2 = normalizeURI(entry.getEntryArg(0));
            String rpx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, psi2);
            entry.setEntryArg(1, rpx);
            this.catalogManager.debug.message(4, "REWRITE_SYSTEM", psi2, rpx);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == REWRITE_URI) {
            String pui2 = normalizeURI(entry.getEntryArg(0));
            String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, pui2);
            entry.setEntryArg(1, upx);
            this.catalogManager.debug.message(4, "REWRITE_URI", pui2, upx);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == SYSTEM_SUFFIX) {
            String pui3 = normalizeURI(entry.getEntryArg(0));
            String upx2 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, pui3);
            entry.setEntryArg(1, upx2);
            this.catalogManager.debug.message(4, "SYSTEM_SUFFIX", pui3, upx2);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == URI_SUFFIX) {
            String pui4 = normalizeURI(entry.getEntryArg(0));
            String upx3 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, pui4);
            entry.setEntryArg(1, upx3);
            this.catalogManager.debug.message(4, "URI_SUFFIX", pui4, upx3);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == DOCTYPE) {
            String fsi8 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi8);
            this.catalogManager.debug.message(4, "DOCTYPE", entry.getEntryArg(0), fsi8);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == DTDDECL) {
            String fpi = PublicId.normalize(entry.getEntryArg(0));
            entry.setEntryArg(0, fpi);
            String fsi9 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi9);
            this.catalogManager.debug.message(4, "DTDDECL", fpi, fsi9);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == ENTITY) {
            String fsi10 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi10);
            this.catalogManager.debug.message(4, SchemaSymbols.ATTVAL_ENTITY, entry.getEntryArg(0), fsi10);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == LINKTYPE) {
            String fsi11 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi11);
            this.catalogManager.debug.message(4, "LINKTYPE", entry.getEntryArg(0), fsi11);
            this.catalogEntries.addElement(entry);
            return;
        }
        if (type == NOTATION) {
            String fsi12 = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi12);
            this.catalogManager.debug.message(4, SchemaSymbols.ATTVAL_NOTATION, entry.getEntryArg(0), fsi12);
            this.catalogEntries.addElement(entry);
            return;
        }
        this.catalogEntries.addElement(entry);
    }

    public void unknownEntry(Vector strings) {
        if (strings != null && strings.size() > 0) {
            String keyword = (String) strings.elementAt(0);
            this.catalogManager.debug.message(2, "Unrecognized token parsing catalog", keyword);
        }
    }

    public void parseAllCatalogs() throws IOException {
        for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
            try {
            } catch (ClassCastException e2) {
                String catfile = (String) this.catalogs.elementAt(catPos);
                Catalog c2 = newCatalog();
                c2.parseCatalog(catfile);
                this.catalogs.setElementAt(c2, catPos);
                c2.parseAllCatalogs();
            }
        }
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e3 = (CatalogEntry) en.nextElement();
            if (e3.getEntryType() == DELEGATE_PUBLIC || e3.getEntryType() == DELEGATE_SYSTEM || e3.getEntryType() == DELEGATE_URI) {
                Catalog dcat = newCatalog();
                dcat.parseCatalog(e3.getEntryArg(1));
            }
        }
    }

    public String resolveDoctype(String entityName, String publicId, String systemId) throws IOException {
        String resolved;
        String resolved2;
        this.catalogManager.debug.message(3, "resolveDoctype(" + entityName + "," + publicId + "," + systemId + ")");
        String systemId2 = normalizeURI(systemId);
        if (publicId != null && publicId.startsWith("urn:publicid:")) {
            publicId = PublicId.decodeURN(publicId);
        }
        if (systemId2 != null && systemId2.startsWith("urn:publicid:")) {
            String systemId3 = PublicId.decodeURN(systemId2);
            if (publicId != null && !publicId.equals(systemId3)) {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
                systemId2 = null;
            } else {
                publicId = systemId3;
                systemId2 = null;
            }
        }
        if (systemId2 != null && (resolved2 = resolveLocalSystem(systemId2)) != null) {
            return resolved2;
        }
        if (publicId != null && (resolved = resolveLocalPublic(DOCTYPE, entityName, publicId, systemId2)) != null) {
            return resolved;
        }
        boolean over = this.default_override;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == OVERRIDE) {
                over = e2.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (e2.getEntryType() == DOCTYPE && e2.getEntryArg(0).equals(entityName) && (over || systemId2 == null)) {
                return e2.getEntryArg(1);
            }
        }
        return resolveSubordinateCatalogs(DOCTYPE, entityName, publicId, systemId2);
    }

    public String resolveDocument() throws IOException {
        this.catalogManager.debug.message(3, "resolveDocument");
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == DOCUMENT) {
                return e2.getEntryArg(0);
            }
        }
        return resolveSubordinateCatalogs(DOCUMENT, null, null, null);
    }

    public String resolveEntity(String entityName, String publicId, String systemId) throws IOException {
        String resolved;
        String resolved2;
        this.catalogManager.debug.message(3, "resolveEntity(" + entityName + "," + publicId + "," + systemId + ")");
        String systemId2 = normalizeURI(systemId);
        if (publicId != null && publicId.startsWith("urn:publicid:")) {
            publicId = PublicId.decodeURN(publicId);
        }
        if (systemId2 != null && systemId2.startsWith("urn:publicid:")) {
            String systemId3 = PublicId.decodeURN(systemId2);
            if (publicId != null && !publicId.equals(systemId3)) {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
                systemId2 = null;
            } else {
                publicId = systemId3;
                systemId2 = null;
            }
        }
        if (systemId2 != null && (resolved2 = resolveLocalSystem(systemId2)) != null) {
            return resolved2;
        }
        if (publicId != null && (resolved = resolveLocalPublic(ENTITY, entityName, publicId, systemId2)) != null) {
            return resolved;
        }
        boolean over = this.default_override;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == OVERRIDE) {
                over = e2.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (e2.getEntryType() == ENTITY && e2.getEntryArg(0).equals(entityName) && (over || systemId2 == null)) {
                return e2.getEntryArg(1);
            }
        }
        return resolveSubordinateCatalogs(ENTITY, entityName, publicId, systemId2);
    }

    public String resolveNotation(String notationName, String publicId, String systemId) throws IOException {
        String resolved;
        String resolved2;
        this.catalogManager.debug.message(3, "resolveNotation(" + notationName + "," + publicId + "," + systemId + ")");
        String systemId2 = normalizeURI(systemId);
        if (publicId != null && publicId.startsWith("urn:publicid:")) {
            publicId = PublicId.decodeURN(publicId);
        }
        if (systemId2 != null && systemId2.startsWith("urn:publicid:")) {
            String systemId3 = PublicId.decodeURN(systemId2);
            if (publicId != null && !publicId.equals(systemId3)) {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
                systemId2 = null;
            } else {
                publicId = systemId3;
                systemId2 = null;
            }
        }
        if (systemId2 != null && (resolved2 = resolveLocalSystem(systemId2)) != null) {
            return resolved2;
        }
        if (publicId != null && (resolved = resolveLocalPublic(NOTATION, notationName, publicId, systemId2)) != null) {
            return resolved;
        }
        boolean over = this.default_override;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == OVERRIDE) {
                over = e2.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (e2.getEntryType() == NOTATION && e2.getEntryArg(0).equals(notationName) && (over || systemId2 == null)) {
                return e2.getEntryArg(1);
            }
        }
        return resolveSubordinateCatalogs(NOTATION, notationName, publicId, systemId2);
    }

    public String resolvePublic(String publicId, String systemId) throws IOException {
        String resolved;
        this.catalogManager.debug.message(3, "resolvePublic(" + publicId + "," + systemId + ")");
        String systemId2 = normalizeURI(systemId);
        if (publicId != null && publicId.startsWith("urn:publicid:")) {
            publicId = PublicId.decodeURN(publicId);
        }
        if (systemId2 != null && systemId2.startsWith("urn:publicid:")) {
            String systemId3 = PublicId.decodeURN(systemId2);
            if (publicId != null && !publicId.equals(systemId3)) {
                this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
                systemId2 = null;
            } else {
                publicId = systemId3;
                systemId2 = null;
            }
        }
        if (systemId2 != null && (resolved = resolveLocalSystem(systemId2)) != null) {
            return resolved;
        }
        String resolved2 = resolveLocalPublic(PUBLIC, null, publicId, systemId2);
        if (resolved2 != null) {
            return resolved2;
        }
        return resolveSubordinateCatalogs(PUBLIC, null, publicId, systemId2);
    }

    protected synchronized String resolveLocalPublic(int entityType, String entityName, String publicId, String systemId) throws IOException {
        String resolved;
        String publicId2 = PublicId.normalize(publicId);
        if (systemId != null && (resolved = resolveLocalSystem(systemId)) != null) {
            return resolved;
        }
        boolean over = this.default_override;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == OVERRIDE) {
                over = e2.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (e2.getEntryType() == PUBLIC && e2.getEntryArg(0).equals(publicId2) && (over || systemId == null)) {
                return e2.getEntryArg(1);
            }
        }
        boolean over2 = this.default_override;
        Enumeration en2 = this.catalogEntries.elements();
        Vector delCats = new Vector();
        while (en2.hasMoreElements()) {
            CatalogEntry e3 = (CatalogEntry) en2.nextElement();
            if (e3.getEntryType() == OVERRIDE) {
                over2 = e3.getEntryArg(0).equalsIgnoreCase("YES");
            } else if (e3.getEntryType() == DELEGATE_PUBLIC && (over2 || systemId == null)) {
                String p2 = e3.getEntryArg(0);
                if (p2.length() <= publicId2.length() && p2.equals(publicId2.substring(0, p2.length()))) {
                    delCats.addElement(e3.getEntryArg(1));
                }
            }
        }
        if (delCats.size() > 0) {
            Enumeration enCats = delCats.elements();
            if (this.catalogManager.debug.getDebug() > 1) {
                this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
                while (enCats.hasMoreElements()) {
                    String delegatedCatalog = (String) enCats.nextElement();
                    this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
                }
            }
            Catalog dcat = newCatalog();
            Enumeration enCats2 = delCats.elements();
            while (enCats2.hasMoreElements()) {
                String delegatedCatalog2 = (String) enCats2.nextElement();
                dcat.parseCatalog(delegatedCatalog2);
            }
            return dcat.resolvePublic(publicId2, null);
        }
        return null;
    }

    public String resolveSystem(String systemId) throws IOException {
        String resolved;
        this.catalogManager.debug.message(3, "resolveSystem(" + systemId + ")");
        String systemId2 = normalizeURI(systemId);
        if (systemId2 != null && systemId2.startsWith("urn:publicid:")) {
            return resolvePublic(PublicId.decodeURN(systemId2), null);
        }
        if (systemId2 != null && (resolved = resolveLocalSystem(systemId2)) != null) {
            return resolved;
        }
        return resolveSubordinateCatalogs(SYSTEM, null, null, systemId2);
    }

    protected String resolveLocalSystem(String systemId) throws IOException {
        String osname = SecuritySupport.getSystemProperty("os.name");
        boolean windows = osname.indexOf("Windows") >= 0;
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == SYSTEM && (e2.getEntryArg(0).equals(systemId) || (windows && e2.getEntryArg(0).equalsIgnoreCase(systemId)))) {
                return e2.getEntryArg(1);
            }
        }
        Enumeration en2 = this.catalogEntries.elements();
        String startString = null;
        String prefix = null;
        while (en2.hasMoreElements()) {
            CatalogEntry e3 = (CatalogEntry) en2.nextElement();
            if (e3.getEntryType() == REWRITE_SYSTEM) {
                String p2 = e3.getEntryArg(0);
                if (p2.length() <= systemId.length() && p2.equals(systemId.substring(0, p2.length())) && (startString == null || p2.length() > startString.length())) {
                    startString = p2;
                    prefix = e3.getEntryArg(1);
                }
            }
        }
        if (prefix != null) {
            return prefix + systemId.substring(startString.length());
        }
        Enumeration en3 = this.catalogEntries.elements();
        String suffixString = null;
        String suffixURI = null;
        while (en3.hasMoreElements()) {
            CatalogEntry e4 = (CatalogEntry) en3.nextElement();
            if (e4.getEntryType() == SYSTEM_SUFFIX) {
                String p3 = e4.getEntryArg(0);
                if (p3.length() <= systemId.length() && systemId.endsWith(p3) && (suffixString == null || p3.length() > suffixString.length())) {
                    suffixString = p3;
                    suffixURI = e4.getEntryArg(1);
                }
            }
        }
        if (suffixURI != null) {
            return suffixURI;
        }
        Enumeration en4 = this.catalogEntries.elements();
        Vector delCats = new Vector();
        while (en4.hasMoreElements()) {
            CatalogEntry e5 = (CatalogEntry) en4.nextElement();
            if (e5.getEntryType() == DELEGATE_SYSTEM) {
                String p4 = e5.getEntryArg(0);
                if (p4.length() <= systemId.length() && p4.equals(systemId.substring(0, p4.length()))) {
                    delCats.addElement(e5.getEntryArg(1));
                }
            }
        }
        if (delCats.size() > 0) {
            Enumeration enCats = delCats.elements();
            if (this.catalogManager.debug.getDebug() > 1) {
                this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
                while (enCats.hasMoreElements()) {
                    String delegatedCatalog = (String) enCats.nextElement();
                    this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
                }
            }
            Catalog dcat = newCatalog();
            Enumeration enCats2 = delCats.elements();
            while (enCats2.hasMoreElements()) {
                String delegatedCatalog2 = (String) enCats2.nextElement();
                dcat.parseCatalog(delegatedCatalog2);
            }
            return dcat.resolveSystem(systemId);
        }
        return null;
    }

    public String resolveURI(String uri) throws IOException {
        String resolved;
        this.catalogManager.debug.message(3, "resolveURI(" + uri + ")");
        String uri2 = normalizeURI(uri);
        if (uri2 != null && uri2.startsWith("urn:publicid:")) {
            return resolvePublic(PublicId.decodeURN(uri2), null);
        }
        if (uri2 != null && (resolved = resolveLocalURI(uri2)) != null) {
            return resolved;
        }
        return resolveSubordinateCatalogs(URI, null, null, uri2);
    }

    protected String resolveLocalURI(String uri) throws IOException {
        Enumeration en = this.catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e2 = (CatalogEntry) en.nextElement();
            if (e2.getEntryType() == URI && e2.getEntryArg(0).equals(uri)) {
                return e2.getEntryArg(1);
            }
        }
        Enumeration en2 = this.catalogEntries.elements();
        String startString = null;
        String prefix = null;
        while (en2.hasMoreElements()) {
            CatalogEntry e3 = (CatalogEntry) en2.nextElement();
            if (e3.getEntryType() == REWRITE_URI) {
                String p2 = e3.getEntryArg(0);
                if (p2.length() <= uri.length() && p2.equals(uri.substring(0, p2.length())) && (startString == null || p2.length() > startString.length())) {
                    startString = p2;
                    prefix = e3.getEntryArg(1);
                }
            }
        }
        if (prefix != null) {
            return prefix + uri.substring(startString.length());
        }
        Enumeration en3 = this.catalogEntries.elements();
        String suffixString = null;
        String suffixURI = null;
        while (en3.hasMoreElements()) {
            CatalogEntry e4 = (CatalogEntry) en3.nextElement();
            if (e4.getEntryType() == URI_SUFFIX) {
                String p3 = e4.getEntryArg(0);
                if (p3.length() <= uri.length() && uri.endsWith(p3) && (suffixString == null || p3.length() > suffixString.length())) {
                    suffixString = p3;
                    suffixURI = e4.getEntryArg(1);
                }
            }
        }
        if (suffixURI != null) {
            return suffixURI;
        }
        Enumeration en4 = this.catalogEntries.elements();
        Vector delCats = new Vector();
        while (en4.hasMoreElements()) {
            CatalogEntry e5 = (CatalogEntry) en4.nextElement();
            if (e5.getEntryType() == DELEGATE_URI) {
                String p4 = e5.getEntryArg(0);
                if (p4.length() <= uri.length() && p4.equals(uri.substring(0, p4.length()))) {
                    delCats.addElement(e5.getEntryArg(1));
                }
            }
        }
        if (delCats.size() > 0) {
            Enumeration enCats = delCats.elements();
            if (this.catalogManager.debug.getDebug() > 1) {
                this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
                while (enCats.hasMoreElements()) {
                    String delegatedCatalog = (String) enCats.nextElement();
                    this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
                }
            }
            Catalog dcat = newCatalog();
            Enumeration enCats2 = delCats.elements();
            while (enCats2.hasMoreElements()) {
                String delegatedCatalog2 = (String) enCats2.nextElement();
                dcat.parseCatalog(delegatedCatalog2);
            }
            return dcat.resolveURI(uri);
        }
        return null;
    }

    protected synchronized String resolveSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId) throws IOException {
        Catalog c2;
        for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
            try {
                c2 = (Catalog) this.catalogs.elementAt(catPos);
            } catch (ClassCastException e2) {
                String catfile = (String) this.catalogs.elementAt(catPos);
                c2 = newCatalog();
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
            String resolved = null;
            if (entityType == DOCTYPE) {
                resolved = c2.resolveDoctype(entityName, publicId, systemId);
            } else if (entityType == DOCUMENT) {
                resolved = c2.resolveDocument();
            } else if (entityType == ENTITY) {
                resolved = c2.resolveEntity(entityName, publicId, systemId);
            } else if (entityType == NOTATION) {
                resolved = c2.resolveNotation(entityName, publicId, systemId);
            } else if (entityType == PUBLIC) {
                resolved = c2.resolvePublic(publicId, systemId);
            } else if (entityType == SYSTEM) {
                resolved = c2.resolveSystem(systemId);
            } else if (entityType == URI) {
                resolved = c2.resolveURI(systemId);
            }
            if (resolved != null) {
                return resolved;
            }
        }
        return null;
    }

    protected String fixSlashes(String sysid) {
        return sysid.replace('\\', '/');
    }

    protected String makeAbsolute(String sysid) {
        URL local = null;
        String sysid2 = fixSlashes(sysid);
        try {
            local = new URL(this.base, sysid2);
        } catch (MalformedURLException e2) {
            this.catalogManager.debug.message(1, "Malformed URL on system identifier", sysid2);
        }
        if (local != null) {
            return local.toString();
        }
        return sysid2;
    }

    protected String normalizeURI(String uriref) {
        if (uriref == null) {
            return null;
        }
        try {
            byte[] bytes = uriref.getBytes("UTF-8");
            StringBuilder newRef = new StringBuilder(bytes.length);
            for (int count = 0; count < bytes.length; count++) {
                int ch = bytes[count] & 255;
                if (ch <= 32 || ch > 127 || ch == 34 || ch == 60 || ch == 62 || ch == 92 || ch == 94 || ch == 96 || ch == 123 || ch == 124 || ch == 125 || ch == 127) {
                    newRef.append(encodedByte(ch));
                } else {
                    newRef.append((char) bytes[count]);
                }
            }
            return newRef.toString();
        } catch (UnsupportedEncodingException e2) {
            this.catalogManager.debug.message(1, "UTF-8 is an unsupported encoding!?");
            return uriref;
        }
    }

    protected String encodedByte(int b2) {
        String hex = Integer.toHexString(b2).toUpperCase();
        if (hex.length() < 2) {
            return "%0" + hex;
        }
        return FXMLLoader.RESOURCE_KEY_PREFIX + hex;
    }

    protected void addDelegate(CatalogEntry entry) {
        int pos = 0;
        String partial = entry.getEntryArg(0);
        Enumeration local = this.localDelegate.elements();
        while (local.hasMoreElements()) {
            CatalogEntry dpe = (CatalogEntry) local.nextElement();
            String dp = dpe.getEntryArg(0);
            if (dp.equals(partial)) {
                return;
            }
            if (dp.length() > partial.length()) {
                pos++;
            }
            if (dp.length() < partial.length()) {
                break;
            }
        }
        if (this.localDelegate.size() == 0) {
            this.localDelegate.addElement(entry);
        } else {
            this.localDelegate.insertElementAt(entry, pos);
        }
    }
}
