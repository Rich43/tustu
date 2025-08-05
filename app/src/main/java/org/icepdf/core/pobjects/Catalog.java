package org.icepdf.core.pobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Catalog.class */
public class Catalog extends Dictionary {
    private static final Logger logger = Logger.getLogger(Catalog.class.toString());
    public static final Name TYPE = new Name("Catalog");
    public static final Name DESTS_KEY = new Name("Dests");
    public static final Name VIEWERPREFERENCES_KEY = new Name("ViewerPreferences");
    public static final Name NAMES_KEY = new Name("Names");
    public static final Name OUTLINES_KEY = new Name("Outlines");
    public static final Name OCPROPERTIES_KEY = new Name("OCProperties");
    public static final Name PAGES_KEY = new Name("Pages");
    public static final Name PAGELAYOUT_KEY = new Name("PageLayout");
    public static final Name PAGEMODE_KEY = new Name("PageMode");
    public static final Name COLLECTION_KEY = new Name("Collection");
    private PageTree pageTree;
    private Outlines outlines;
    private Names names;
    private OptionalContent optionalContent;
    private Dictionary dests;
    private ViewerPreferences viewerPref;
    private boolean outlinesInited;
    private boolean namesTreeInited;
    private boolean destsInited;
    private boolean viewerPrefInited;
    private boolean optionalContentInited;

    static {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("ICEsoft ICEpdf Core " + Document.getLibraryVersion());
        }
    }

    public Catalog(Library l2, HashMap<Object, Object> h2) {
        super(l2, h2);
        this.outlinesInited = false;
        this.namesTreeInited = false;
        this.destsInited = false;
        this.viewerPrefInited = false;
        this.optionalContentInited = false;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        Object tmp = this.library.getObject(this.entries, PAGES_KEY);
        this.pageTree = null;
        if (tmp instanceof PageTree) {
            this.pageTree = (PageTree) tmp;
        } else if (tmp instanceof HashMap) {
            this.pageTree = new PageTree(this.library, (HashMap) tmp);
        } else if (tmp instanceof Page) {
            Page tmpPage = (Page) tmp;
            HashMap<String, Object> tmpPages = new HashMap<>();
            List<Reference> kids = new ArrayList<>();
            kids.add(tmpPage.getPObjectReference());
            tmpPages.put("Kids", kids);
            tmpPages.put("Count", 1);
            this.pageTree = new PageTree(this.library, tmpPages);
        }
        if (this.pageTree != null) {
            this.pageTree.init();
        }
        Object tmp2 = this.library.getObject(this.entries, NAMES_KEY);
        if (tmp2 != null) {
            this.names = new Names(this.library, (HashMap) tmp2);
            this.names.init();
        }
    }

    public PageTree getPageTree() {
        return this.pageTree;
    }

    public Outlines getOutlines() {
        if (!this.outlinesInited) {
            this.outlinesInited = true;
            Object o2 = this.library.getObject(this.entries, OUTLINES_KEY);
            if (o2 != null) {
                this.outlines = new Outlines(this.library, (HashMap) o2);
            }
        }
        return this.outlines;
    }

    public Names getNames() {
        return this.names;
    }

    public Dictionary getDestinations() {
        if (!this.destsInited) {
            this.destsInited = true;
            Object o2 = this.library.getObject(this.entries, DESTS_KEY);
            if (o2 != null) {
                this.dests = new Dictionary(this.library, (HashMap) o2);
                this.dests.init();
            }
        }
        return this.dests;
    }

    public ViewerPreferences getViewerPreferences() {
        if (!this.viewerPrefInited) {
            this.viewerPrefInited = true;
            Object o2 = this.library.getObject(this.entries, VIEWERPREFERENCES_KEY);
            if (o2 != null) {
                this.viewerPref = new ViewerPreferences(this.library, (HashMap) o2);
                this.viewerPref.init();
            }
        }
        return this.viewerPref;
    }

    public OptionalContent getOptionalContent() {
        if (!this.optionalContentInited) {
            this.optionalContentInited = true;
            Object o2 = this.library.getObject(this.entries, OCPROPERTIES_KEY);
            if (o2 != null && (o2 instanceof HashMap)) {
                this.optionalContent = new OptionalContent(this.library, (HashMap) o2);
                this.optionalContent.init();
            } else {
                this.optionalContent = new OptionalContent(this.library, new HashMap());
                this.optionalContent.init();
            }
        }
        return this.optionalContent;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "CATALOG= " + this.entries.toString();
    }
}
