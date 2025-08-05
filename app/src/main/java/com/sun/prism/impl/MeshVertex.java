package com.sun.prism.impl;

import com.sun.javafx.geom.Vec3f;

/* loaded from: jfxrt.jar:com/sun/prism/impl/MeshVertex.class */
class MeshVertex {
    int smGroup;
    int pVert;
    int tVert;
    int fIdx;
    int index;
    static final int IDX_UNDEFINED = -1;
    static final int IDX_SET_SMOOTH = -2;
    static final int IDX_UNITE = -3;
    static final /* synthetic */ boolean $assertionsDisabled;
    MeshVertex next = null;
    Vec3f[] norm = new Vec3f[3];

    static {
        $assertionsDisabled = !MeshVertex.class.desiredAssertionStatus();
    }

    MeshVertex() {
        for (int i2 = 0; i2 < this.norm.length; i2++) {
            this.norm[i2] = new Vec3f();
        }
    }

    static void avgSmNormals(MeshVertex v2) {
        Vec3f normalSum = MeshTempState.getInstance().vec3f1;
        while (v2 != null) {
            if (v2.index == -1) {
                normalSum.set(v2.norm[0]);
                int sm = v2.smGroup;
                MeshVertex meshVertex = v2.next;
                while (true) {
                    MeshVertex i2 = meshVertex;
                    if (i2 != null) {
                        if (i2.smGroup == sm) {
                            if (!$assertionsDisabled && i2.index != -1) {
                                throw new AssertionError();
                            }
                            i2.index = -2;
                            normalSum.add(i2.norm[0]);
                        }
                        meshVertex = i2.next;
                    } else if (MeshUtil.isNormalOkAfterWeld(normalSum)) {
                        normalSum.normalize();
                        MeshVertex meshVertex2 = v2;
                        while (true) {
                            MeshVertex i3 = meshVertex2;
                            if (i3 != null) {
                                if (i3.smGroup == sm) {
                                    i3.norm[0].set(normalSum);
                                }
                                meshVertex2 = i3.next;
                            }
                        }
                    }
                }
            }
            v2 = v2.next;
        }
    }

    static boolean okToWeldVertsTB(MeshVertex a2, MeshVertex b2) {
        return a2.tVert == b2.tVert && MeshUtil.isTangentOk(a2.norm, b2.norm);
    }

    static int weldWithTB(MeshVertex v2, int index) {
        Vec3f[] nSum = MeshTempState.getInstance().triNormals;
        while (v2 != null) {
            if (v2.index < 0) {
                int nuLocal = 0;
                for (int i2 = 0; i2 < 3; i2++) {
                    nSum[i2].set(v2.norm[i2]);
                }
                MeshVertex meshVertex = v2.next;
                while (true) {
                    MeshVertex i3 = meshVertex;
                    if (i3 == null) {
                        break;
                    }
                    if (i3.index < 0 && okToWeldVertsTB(v2, i3)) {
                        i3.index = -3;
                        nuLocal++;
                        for (int j2 = 0; j2 < 3; j2++) {
                            nSum[j2].add(i3.norm[j2]);
                        }
                    }
                    meshVertex = i3.next;
                }
                if (nuLocal != 0) {
                    if (MeshUtil.isTangentOK(nSum)) {
                        MeshUtil.fixTSpace(nSum);
                        v2.index = index;
                        for (int i4 = 0; i4 < 3; i4++) {
                            v2.norm[i4].set(nSum[i4]);
                        }
                        MeshVertex meshVertex2 = v2.next;
                        while (true) {
                            MeshVertex i5 = meshVertex2;
                            if (i5 == null) {
                                break;
                            }
                            if (i5.index == -3) {
                                i5.index = index;
                                i5.norm[0].set(0.0f, 0.0f, 0.0f);
                            }
                            meshVertex2 = i5.next;
                        }
                    } else {
                        nuLocal = 0;
                    }
                }
                if (nuLocal == 0) {
                    MeshUtil.fixTSpace(v2.norm);
                    v2.index = index;
                }
                index++;
            }
            v2 = v2.next;
        }
        return index;
    }

    static void mergeSmIndexes(MeshVertex n2) {
        MeshVertex l2 = n2;
        while (l2 != null) {
            boolean change = false;
            MeshVertex meshVertex = l2.next;
            while (true) {
                MeshVertex i2 = meshVertex;
                if (i2 == null) {
                    break;
                }
                if ((l2.smGroup & i2.smGroup) != 0 && l2.smGroup != i2.smGroup) {
                    l2.smGroup = i2.smGroup | l2.smGroup;
                    i2.smGroup = l2.smGroup;
                    change = true;
                }
                meshVertex = i2.next;
            }
            if (!change) {
                l2 = l2.next;
            }
        }
    }

    static void correctSmNormals(MeshVertex n2) {
        MeshVertex meshVertex = n2;
        while (true) {
            MeshVertex l2 = meshVertex;
            if (l2 != null) {
                if (l2.smGroup != 0) {
                    MeshVertex meshVertex2 = l2.next;
                    while (true) {
                        MeshVertex i2 = meshVertex2;
                        if (i2 != null) {
                            if ((i2.smGroup & l2.smGroup) == 0 || !MeshUtil.isOppositeLookingNormals(i2.norm, l2.norm)) {
                                meshVertex2 = i2.next;
                            } else {
                                l2.smGroup = 0;
                                i2.smGroup = 0;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                meshVertex = l2.next;
            } else {
                return;
            }
        }
    }

    static int processVertices(MeshVertex[] pVerts, int nVertex, boolean allHardEdges, boolean allSameSmoothing) {
        int nNewVerts = 0;
        Vec3f normalSum = MeshTempState.getInstance().vec3f1;
        for (int i2 = 0; i2 < nVertex; i2++) {
            if (pVerts[i2] != null) {
                if (!allHardEdges) {
                    if (allSameSmoothing) {
                        normalSum.set(pVerts[i2].norm[0]);
                        MeshVertex meshVertex = pVerts[i2].next;
                        while (true) {
                            MeshVertex v2 = meshVertex;
                            if (v2 == null) {
                                break;
                            }
                            normalSum.add(v2.norm[0]);
                            meshVertex = v2.next;
                        }
                        if (MeshUtil.isNormalOkAfterWeld(normalSum)) {
                            normalSum.normalize();
                            MeshVertex meshVertex2 = pVerts[i2];
                            while (true) {
                                MeshVertex v3 = meshVertex2;
                                if (v3 == null) {
                                    break;
                                }
                                v3.norm[0].set(normalSum);
                                meshVertex2 = v3.next;
                            }
                        }
                    } else {
                        mergeSmIndexes(pVerts[i2]);
                        avgSmNormals(pVerts[i2]);
                    }
                }
                nNewVerts = weldWithTB(pVerts[i2], nNewVerts);
            }
        }
        return nNewVerts;
    }

    public String toString() {
        return "MeshVertex : " + getClass().getName() + "@0x" + Integer.toHexString(hashCode()) + ":: smGroup = " + this.smGroup + "\n\tnorm[0] = " + ((Object) this.norm[0]) + "\n\tnorm[1] = " + ((Object) this.norm[1]) + "\n\tnorm[2] = " + ((Object) this.norm[2]) + "\n\ttIndex = " + this.tVert + ", fIndex = " + this.fIdx + "\n\tpIdx = " + this.index + "\n\tnext = " + (this.next == null ? this.next : this.next.getClass().getName() + "@0x" + Integer.toHexString(this.next.hashCode())) + "\n";
    }

    static void dumpInfo(MeshVertex v2) {
        System.err.println("** dumpInfo: ");
        MeshVertex meshVertex = v2;
        while (true) {
            MeshVertex q2 = meshVertex;
            if (q2 != null) {
                System.err.println(q2);
                meshVertex = q2.next;
            } else {
                System.err.println("***********************************");
                return;
            }
        }
    }
}
