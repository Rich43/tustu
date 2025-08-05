package org.icepdf.core.pobjects;

import java.util.HashMap;
import java.util.logging.Logger;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/Names.class */
public class Names extends Dictionary {
    private static final Logger logger = Logger.getLogger(Names.class.toString());
    public static final Name DEST_KEY = new Name("Dests");
    public static final Name ANNOTATION_APPEARANCE_KEY = new Name("AP");
    public static final Name JAVASCRIPT_KEY = new Name("JavaScript");
    public static final Name PAGES_KEY = new Name("Pages");
    public static final Name TEMPLATES_KEY = new Name("Templates");
    public static final Name IDS_KEY = new Name("IDS");
    public static final Name EMBEDDED_FILES_KEY = new Name("EmbeddedFiles");
    public static final Name ALTERNATE_PRESENTATIONS_KEY = new Name("AlternatePresentations");
    public static final Name RENDITIONS_KEY = new Name("Renditions");
    private NameTree destsNameTree;
    private NameTree javaScriptNameTree;
    private NameTree pagesNameTree;
    private NameTree templatesNameTree;
    private NameTree idsNameTree;
    private NameTree embeddedFilesNameTree;
    private NameTree alternatePresentationsNameTree;
    private NameTree renditionsNameTree;
    private NameTree annotationAppearanceNameTree;

    public Names(Library l2, HashMap h2) {
        super(l2, h2);
        if (!this.inited) {
            Object tmp = this.library.getObject(this.entries, DEST_KEY);
            if (tmp != null && (tmp instanceof HashMap)) {
                this.destsNameTree = new NameTree(this.library, (HashMap) tmp);
                this.destsNameTree.init();
            }
            Object tmp2 = this.library.getObject(this.entries, JAVASCRIPT_KEY);
            if (tmp2 != null && (tmp2 instanceof HashMap)) {
                this.javaScriptNameTree = new NameTree(this.library, (HashMap) tmp2);
                this.javaScriptNameTree.init();
            }
            Object tmp3 = this.library.getObject(this.entries, PAGES_KEY);
            if (tmp3 != null && (tmp3 instanceof HashMap)) {
                this.pagesNameTree = new NameTree(this.library, (HashMap) tmp3);
                this.pagesNameTree.init();
            }
            Object tmp4 = this.library.getObject(this.entries, TEMPLATES_KEY);
            if (tmp4 != null && (tmp4 instanceof HashMap)) {
                this.templatesNameTree = new NameTree(this.library, (HashMap) tmp4);
                this.templatesNameTree.init();
            }
            Object tmp5 = this.library.getObject(this.entries, IDS_KEY);
            if (tmp5 != null && (tmp5 instanceof HashMap)) {
                this.idsNameTree = new NameTree(this.library, (HashMap) tmp5);
                this.idsNameTree.init();
            }
            Object tmp6 = this.library.getObject(this.entries, EMBEDDED_FILES_KEY);
            if (tmp6 != null && (tmp6 instanceof HashMap)) {
                this.embeddedFilesNameTree = new NameTree(this.library, (HashMap) tmp6);
                this.embeddedFilesNameTree.init();
            }
            Object tmp7 = this.library.getObject(this.entries, ALTERNATE_PRESENTATIONS_KEY);
            if (tmp7 != null && (tmp7 instanceof HashMap)) {
                this.alternatePresentationsNameTree = new NameTree(this.library, (HashMap) tmp7);
                this.alternatePresentationsNameTree.init();
            }
            Object tmp8 = this.library.getObject(this.entries, RENDITIONS_KEY);
            if (tmp8 != null && (tmp8 instanceof HashMap)) {
                this.renditionsNameTree = new NameTree(this.library, (HashMap) tmp8);
                this.renditionsNameTree.init();
            }
        }
    }

    public NameTree getDestsNameTree() {
        return this.destsNameTree;
    }

    public NameTree getAnnotationAppearanceNameTree() {
        return this.annotationAppearanceNameTree;
    }

    public NameTree getJavaScriptNameTree() {
        return this.javaScriptNameTree;
    }

    public NameTree getPagesNameTree() {
        return this.pagesNameTree;
    }

    public NameTree getTemplatesNameTree() {
        return this.templatesNameTree;
    }

    public NameTree getIdsNameTree() {
        return this.idsNameTree;
    }

    public NameTree getEmbeddedFilesNameTree() {
        return this.embeddedFilesNameTree;
    }

    public NameTree getAlternatePresentationsNameTree() {
        return this.alternatePresentationsNameTree;
    }

    public NameTree getRenditionsNameTree() {
        return this.renditionsNameTree;
    }
}
