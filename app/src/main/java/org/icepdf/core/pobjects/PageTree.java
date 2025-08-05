package org.icepdf.core.pobjects;

import com.sun.org.glassfish.external.amx.AMX;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import org.icepdf.core.pobjects.graphics.WatermarkCallback;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PageTree.class */
public class PageTree extends Dictionary {
    public static final Name TYPE = new Name("Pages");
    public static final Name PARENT_KEY = new Name(AMX.ATTR_PARENT);
    public static final Name COUNT_KEY = new Name("Count");
    public static final Name MEDIABOX_KEY = new Name("MediaBox");
    public static final Name CROPBOX_KEY = new Name("CropBox");
    public static final Name KIDS_KEY = new Name("Kids");
    public static final Name ROTATE_KEY = new Name("Rotate");
    public static final Name RESOURCES_KEY = new Name("Resources");
    private int kidsCount;
    private List kidsReferences;
    private HashMap<Integer, WeakReference<Object>> kidsPageAndPages;
    private PageTree parent;
    private boolean inited;
    private PRectangle mediaBox;
    private PRectangle cropBox;
    private Resources resources;
    private boolean loadedResources;
    private WatermarkCallback watermarkCallback;
    protected float rotationFactor;
    protected boolean isRotationFactor;

    public PageTree(Library l2, HashMap h2) {
        super(l2, h2);
        this.kidsCount = 0;
        this.rotationFactor = 0.0f;
        this.isRotationFactor = false;
    }

    public void resetInitializedState() {
        this.inited = false;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        Object parentTree = this.library.getObject(this.entries, PARENT_KEY);
        if (parentTree instanceof PageTree) {
            this.parent = (PageTree) parentTree;
        }
        this.kidsCount = this.library.getNumber(this.entries, COUNT_KEY).intValue();
        this.kidsReferences = (List) this.library.getObject(this.entries, KIDS_KEY);
        this.kidsPageAndPages = new HashMap<>(this.kidsReferences.size());
        Object tmpRotation = this.library.getObject(this.entries, ROTATE_KEY);
        if (tmpRotation != null) {
            this.rotationFactor = ((Number) tmpRotation).floatValue();
            this.isRotationFactor = true;
        }
        this.inited = true;
    }

    public PRectangle getMediaBox() {
        if (!this.inited) {
            init();
        }
        if (this.mediaBox != null) {
            return this.mediaBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, MEDIABOX_KEY);
        if (boxDimensions != null) {
            this.mediaBox = new PRectangle(boxDimensions);
        }
        if (this.mediaBox == null) {
            PageTree pageTree = getParent();
            while (pageTree != null && this.mediaBox == null) {
                this.mediaBox = pageTree.getMediaBox();
                if (this.mediaBox == null) {
                    pageTree = pageTree.getParent();
                }
            }
        }
        return this.mediaBox;
    }

    public PRectangle getCropBox() {
        if (!this.inited) {
            init();
        }
        if (this.cropBox != null) {
            return this.cropBox;
        }
        List boxDimensions = (List) this.library.getObject(this.entries, CROPBOX_KEY);
        if (boxDimensions != null) {
            this.cropBox = new PRectangle(boxDimensions);
        }
        PRectangle mediaBox = getMediaBox();
        if (this.cropBox == null && mediaBox != null) {
            this.cropBox = (PRectangle) mediaBox.clone();
        } else if (mediaBox != null) {
            this.cropBox = mediaBox.createCartesianIntersection(this.cropBox);
        }
        return this.cropBox;
    }

    public synchronized Resources getResources() {
        if (!this.loadedResources) {
            this.loadedResources = true;
            this.resources = this.library.getResources(this.entries, RESOURCES_KEY);
        }
        return this.resources;
    }

    public PageTree getParent() {
        return this.parent;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ae, code lost:
    
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getPageNumber(org.icepdf.core.pobjects.Reference r4) {
        /*
            r3 = this;
            r0 = r3
            org.icepdf.core.util.Library r0 = r0.library
            r1 = r4
            java.lang.Object r0 = r0.getObject(r1)
            org.icepdf.core.pobjects.Page r0 = (org.icepdf.core.pobjects.Page) r0
            r5 = r0
            r0 = r5
            if (r0 != 0) goto L12
            r0 = -1
            return r0
        L12:
            r0 = 0
            r6 = r0
            r0 = r4
            r7 = r0
            r0 = r5
            org.icepdf.core.pobjects.Reference r0 = r0.getParentReference()
            r8 = r0
            r0 = r5
            org.icepdf.core.pobjects.PageTree r0 = r0.getParent()
            r9 = r0
        L23:
            r0 = r8
            if (r0 == 0) goto Lad
            r0 = r9
            if (r0 == 0) goto Lad
            r0 = r9
            r0.init()
            r0 = r9
            r1 = r7
            int r0 = r0.indexOfKidReference(r1)
            r10 = r0
            r0 = r10
            if (r0 >= 0) goto L42
            r0 = -1
            return r0
        L42:
            r0 = 0
            r11 = r0
            r0 = 0
            r12 = r0
        L48:
            r0 = r12
            r1 = r10
            if (r0 >= r1) goto L8a
            r0 = r9
            r1 = r12
            java.lang.Object r0 = r0.getPageOrPagesPotentiallyNotInitedFromReferenceAt(r1)
            r13 = r0
            r0 = r13
            boolean r0 = r0 instanceof org.icepdf.core.pobjects.Page
            if (r0 == 0) goto L66
            int r11 = r11 + 1
            goto L84
        L66:
            r0 = r13
            boolean r0 = r0 instanceof org.icepdf.core.pobjects.PageTree
            if (r0 == 0) goto L84
            r0 = r13
            org.icepdf.core.pobjects.PageTree r0 = (org.icepdf.core.pobjects.PageTree) r0
            r14 = r0
            r0 = r14
            r0.init()
            r0 = r11
            r1 = r14
            int r1 = r1.getNumberOfPages()
            int r0 = r0 + r1
            r11 = r0
        L84:
            int r12 = r12 + 1
            goto L48
        L8a:
            r0 = r6
            r1 = r11
            int r0 = r0 + r1
            r6 = r0
            r0 = r8
            r7 = r0
            r0 = r9
            java.util.HashMap<java.lang.Object, java.lang.Object> r0 = r0.entries
            org.icepdf.core.pobjects.Name r1 = org.icepdf.core.pobjects.PageTree.PARENT_KEY
            java.lang.Object r0 = r0.get(r1)
            org.icepdf.core.pobjects.Reference r0 = (org.icepdf.core.pobjects.Reference) r0
            r8 = r0
            r0 = r9
            org.icepdf.core.pobjects.PageTree r0 = r0.parent
            r9 = r0
            goto L23
        Lad:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.core.pobjects.PageTree.getPageNumber(org.icepdf.core.pobjects.Reference):int");
    }

    private int indexOfKidReference(Reference r2) {
        for (int i2 = 0; i2 < this.kidsReferences.size(); i2++) {
            Reference ref = (Reference) this.kidsReferences.get(i2);
            if (ref.equals(r2)) {
                return i2;
            }
        }
        return -1;
    }

    private Object getPageOrPagesPotentiallyNotInitedFromReferenceAt(int index) {
        WeakReference<Object> pageOrPages = this.kidsPageAndPages.get(Integer.valueOf(index));
        if (pageOrPages == null || pageOrPages.get() == null) {
            Reference ref = (Reference) this.kidsReferences.get(index);
            Object tmp = this.library.getObject(ref);
            this.kidsPageAndPages.put(Integer.valueOf(index), new WeakReference<>(tmp));
            return tmp;
        }
        return pageOrPages.get();
    }

    private Page getPagePotentiallyNotInitedByRecursiveIndex(int globalIndex) {
        int globalIndexSoFar = 0;
        int numLocalKids = this.kidsReferences.size();
        for (int i2 = 0; i2 < numLocalKids; i2++) {
            Object pageOrPages = getPageOrPagesPotentiallyNotInitedFromReferenceAt(i2);
            if (pageOrPages instanceof Page) {
                if (globalIndex == globalIndexSoFar) {
                    return (Page) pageOrPages;
                }
                globalIndexSoFar++;
            } else if (pageOrPages instanceof PageTree) {
                PageTree childPageTree = (PageTree) pageOrPages;
                childPageTree.init();
                int numChildPages = childPageTree.getNumberOfPages();
                if (globalIndex >= globalIndexSoFar && globalIndex < globalIndexSoFar + numChildPages) {
                    return childPageTree.getPagePotentiallyNotInitedByRecursiveIndex(globalIndex - globalIndexSoFar);
                }
                globalIndexSoFar += numChildPages;
            } else if (pageOrPages instanceof HashMap) {
                HashMap dictionary = (HashMap) pageOrPages;
                if (dictionary.containsKey(new Name("Kids"))) {
                    PageTree childPageTree2 = new PageTree(this.library, dictionary);
                    childPageTree2.init();
                    int numChildPages2 = childPageTree2.getNumberOfPages();
                    if (globalIndex >= globalIndexSoFar && globalIndex < globalIndexSoFar + numChildPages2) {
                        return childPageTree2.getPagePotentiallyNotInitedByRecursiveIndex(globalIndex - globalIndexSoFar);
                    }
                    globalIndexSoFar += numChildPages2;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    protected void setWatermarkCallback(WatermarkCallback watermarkCallback) {
        this.watermarkCallback = watermarkCallback;
    }

    public int getNumberOfPages() {
        return this.kidsCount;
    }

    public Page getPage(int pageNumber) {
        if (pageNumber < 0) {
            return null;
        }
        Page page = getPagePotentiallyNotInitedByRecursiveIndex(pageNumber);
        if (page != null) {
            page.setWatermarkCallback(this.watermarkCallback);
            page.setPageIndex(pageNumber);
        }
        return getPagePotentiallyNotInitedByRecursiveIndex(pageNumber);
    }

    public Reference getPageReference(int pageNumber) {
        Page p2;
        if (pageNumber >= 0 && (p2 = getPagePotentiallyNotInitedByRecursiveIndex(pageNumber)) != null) {
            return p2.getPObjectReference();
        }
        return null;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "PAGES= " + this.entries.toString();
    }
}
