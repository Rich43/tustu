package com.sun.org.apache.xerces.internal.impl.dtd.models;

import com.sun.org.apache.xerces.internal.xni.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/MixedContentModel.class */
public class MixedContentModel implements ContentModelValidator {
    private int fCount;
    private QName[] fChildren;
    private int[] fChildrenType;
    private boolean fOrdered;

    public MixedContentModel(QName[] children, int[] type, int offset, int length, boolean ordered) {
        this.fCount = length;
        this.fChildren = new QName[this.fCount];
        this.fChildrenType = new int[this.fCount];
        for (int i2 = 0; i2 < this.fCount; i2++) {
            this.fChildren[i2] = new QName(children[offset + i2]);
            this.fChildrenType[i2] = type[offset + i2];
        }
        this.fOrdered = ordered;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator
    public int validate(QName[] children, int offset, int length) {
        if (this.fOrdered) {
            int inIndex = 0;
            for (int outIndex = 0; outIndex < length; outIndex++) {
                if (children[offset + outIndex].localpart != null) {
                    int type = this.fChildrenType[inIndex];
                    if (type == 0) {
                        if (this.fChildren[inIndex].rawname != children[offset + outIndex].rawname) {
                            return outIndex;
                        }
                    } else if (type == 6) {
                        String uri = this.fChildren[inIndex].uri;
                        if (uri != null && uri != children[outIndex].uri) {
                            return outIndex;
                        }
                    } else if (type == 8) {
                        if (children[outIndex].uri != null) {
                            return outIndex;
                        }
                    } else if (type == 7 && this.fChildren[inIndex].uri == children[outIndex].uri) {
                        return outIndex;
                    }
                    inIndex++;
                }
            }
            return -1;
        }
        for (int outIndex2 = 0; outIndex2 < length; outIndex2++) {
            QName curChild = children[offset + outIndex2];
            if (curChild.localpart != null) {
                int inIndex2 = 0;
                while (inIndex2 < this.fCount) {
                    int type2 = this.fChildrenType[inIndex2];
                    if (type2 == 0) {
                        if (curChild.rawname == this.fChildren[inIndex2].rawname) {
                            break;
                        }
                        inIndex2++;
                    } else if (type2 == 6) {
                        String uri2 = this.fChildren[inIndex2].uri;
                        if (uri2 == null || uri2 == children[outIndex2].uri) {
                            break;
                        }
                        inIndex2++;
                    } else if (type2 == 8) {
                        if (children[outIndex2].uri == null) {
                            break;
                        }
                        inIndex2++;
                    } else {
                        if (type2 == 7 && this.fChildren[inIndex2].uri != children[outIndex2].uri) {
                            break;
                        }
                        inIndex2++;
                    }
                }
                if (inIndex2 == this.fCount) {
                    return outIndex2;
                }
            }
        }
        return -1;
    }
}
