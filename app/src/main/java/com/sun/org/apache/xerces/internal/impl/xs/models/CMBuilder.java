package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xs.XSTerm;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/CMBuilder.class */
public class CMBuilder {
    private XSDeclarationPool fDeclPool;
    private static XSEmptyCM fEmptyCM = new XSEmptyCM();
    private int fLeafCount;
    private int fParticleCount;
    private CMNodeFactory fNodeFactory;

    public CMBuilder(CMNodeFactory nodeFactory) {
        this.fDeclPool = null;
        this.fDeclPool = null;
        this.fNodeFactory = nodeFactory;
    }

    public void setDeclPool(XSDeclarationPool declPool) {
        this.fDeclPool = declPool;
    }

    public XSCMValidator getContentModel(XSComplexTypeDecl typeDecl) {
        XSCMValidator cmValidator;
        short contentType = typeDecl.getContentType();
        if (contentType == 1 || contentType == 0) {
            return null;
        }
        XSParticleDecl particle = (XSParticleDecl) typeDecl.getParticle();
        if (particle == null) {
            return fEmptyCM;
        }
        if (particle.fType == 3 && ((XSModelGroupImpl) particle.fValue).fCompositor == 103) {
            cmValidator = createAllCM(particle);
        } else {
            cmValidator = createDFACM(particle);
        }
        this.fNodeFactory.resetNodeCount();
        if (cmValidator == null) {
            cmValidator = fEmptyCM;
        }
        return cmValidator;
    }

    XSCMValidator createAllCM(XSParticleDecl particle) {
        if (particle.fMaxOccurs == 0) {
            return null;
        }
        XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
        XSAllCM allContent = new XSAllCM(particle.fMinOccurs == 0, group.fParticleCount);
        for (int i2 = 0; i2 < group.fParticleCount; i2++) {
            allContent.addElement((XSElementDecl) group.fParticles[i2].fValue, group.fParticles[i2].fMinOccurs == 0);
        }
        return allContent;
    }

    XSCMValidator createDFACM(XSParticleDecl particle) {
        this.fLeafCount = 0;
        this.fParticleCount = 0;
        CMNode node = useRepeatingLeafNodes(particle) ? buildCompactSyntaxTree(particle) : buildSyntaxTree(particle, true);
        if (node == null) {
            return null;
        }
        return new XSDFACM(node, this.fLeafCount);
    }

    private CMNode buildSyntaxTree(XSParticleDecl particle, boolean optimize) throws XNIException {
        int maxOccurs = particle.fMaxOccurs;
        int minOccurs = particle.fMinOccurs;
        short type = particle.fType;
        CMNode nodeRet = null;
        if (type == 2 || type == 1) {
            CMNodeFactory cMNodeFactory = this.fNodeFactory;
            short s2 = particle.fType;
            XSTerm xSTerm = particle.fValue;
            int i2 = this.fParticleCount;
            this.fParticleCount = i2 + 1;
            int i3 = this.fLeafCount;
            this.fLeafCount = i3 + 1;
            nodeRet = expandContentModel(cMNodeFactory.getCMLeafNode(s2, xSTerm, i2, i3), minOccurs, maxOccurs, optimize);
        } else if (type == 3) {
            XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
            boolean twoChildren = false;
            for (int i4 = 0; i4 < group.fParticleCount; i4++) {
                CMNode temp = buildSyntaxTree(group.fParticles[i4], optimize && minOccurs == 1 && maxOccurs == 1 && (group.fCompositor == 102 || group.fParticleCount == 1));
                if (temp != null) {
                    if (nodeRet == null) {
                        nodeRet = temp;
                    } else {
                        nodeRet = this.fNodeFactory.getCMBinOpNode(group.fCompositor, nodeRet, temp);
                        twoChildren = true;
                    }
                }
            }
            if (nodeRet != null) {
                if (group.fCompositor == 101 && !twoChildren && group.fParticleCount > 1) {
                    nodeRet = this.fNodeFactory.getCMUniOpNode(5, nodeRet);
                }
                nodeRet = expandContentModel(nodeRet, minOccurs, maxOccurs, false);
            }
        }
        return nodeRet;
    }

    private CMNode expandContentModel(CMNode node, int minOccurs, int maxOccurs, boolean optimize) throws XNIException {
        CMNode nodeRet = null;
        if (minOccurs == 1 && maxOccurs == 1) {
            nodeRet = node;
        } else if (minOccurs == 0 && maxOccurs == 1) {
            nodeRet = this.fNodeFactory.getCMUniOpNode(5, node);
        } else if (minOccurs == 0 && maxOccurs == -1) {
            nodeRet = this.fNodeFactory.getCMUniOpNode(4, node);
        } else if (minOccurs == 1 && maxOccurs == -1) {
            nodeRet = this.fNodeFactory.getCMUniOpNode(6, node);
        } else if ((optimize && node.type() == 1) || node.type() == 2) {
            nodeRet = this.fNodeFactory.getCMUniOpNode(minOccurs == 0 ? 4 : 6, node);
            nodeRet.setUserData(new int[]{minOccurs, maxOccurs});
        } else if (maxOccurs == -1) {
            CMNode nodeRet2 = this.fNodeFactory.getCMUniOpNode(6, node);
            nodeRet = this.fNodeFactory.getCMBinOpNode(102, multiNodes(node, minOccurs - 1, true), nodeRet2);
        } else {
            if (minOccurs > 0) {
                nodeRet = multiNodes(node, minOccurs, false);
            }
            if (maxOccurs > minOccurs) {
                CMNode node2 = this.fNodeFactory.getCMUniOpNode(5, node);
                if (nodeRet == null) {
                    nodeRet = multiNodes(node2, maxOccurs - minOccurs, false);
                } else {
                    nodeRet = this.fNodeFactory.getCMBinOpNode(102, nodeRet, multiNodes(node2, maxOccurs - minOccurs, true));
                }
            }
        }
        return nodeRet;
    }

    private CMNode multiNodes(CMNode node, int num, boolean copyFirst) {
        if (num == 0) {
            return null;
        }
        if (num == 1) {
            return copyFirst ? copyNode(node) : node;
        }
        int num1 = num / 2;
        return this.fNodeFactory.getCMBinOpNode(102, multiNodes(node, num1, copyFirst), multiNodes(node, num - num1, true));
    }

    private CMNode copyNode(CMNode node) throws XNIException {
        int type = node.type();
        if (type == 101 || type == 102) {
            XSCMBinOp bin = (XSCMBinOp) node;
            node = this.fNodeFactory.getCMBinOpNode(type, copyNode(bin.getLeft()), copyNode(bin.getRight()));
        } else if (type == 4 || type == 6 || type == 5) {
            XSCMUniOp uni = (XSCMUniOp) node;
            node = this.fNodeFactory.getCMUniOpNode(type, copyNode(uni.getChild()));
        } else if (type == 1 || type == 2) {
            XSCMLeaf leaf = (XSCMLeaf) node;
            CMNodeFactory cMNodeFactory = this.fNodeFactory;
            int iType = leaf.type();
            Object leaf2 = leaf.getLeaf();
            int particleId = leaf.getParticleId();
            int i2 = this.fLeafCount;
            this.fLeafCount = i2 + 1;
            node = cMNodeFactory.getCMLeafNode(iType, leaf2, particleId, i2);
        }
        return node;
    }

    private CMNode buildCompactSyntaxTree(XSParticleDecl particle) throws XNIException {
        int maxOccurs = particle.fMaxOccurs;
        int minOccurs = particle.fMinOccurs;
        short type = particle.fType;
        CMNode nodeRet = null;
        if (type == 2 || type == 1) {
            return buildCompactSyntaxTree2(particle, minOccurs, maxOccurs);
        }
        if (type == 3) {
            XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
            if (group.fParticleCount == 1 && (minOccurs != 1 || maxOccurs != 1)) {
                return buildCompactSyntaxTree2(group.fParticles[0], minOccurs, maxOccurs);
            }
            int count = 0;
            for (int i2 = 0; i2 < group.fParticleCount; i2++) {
                CMNode temp = buildCompactSyntaxTree(group.fParticles[i2]);
                if (temp != null) {
                    count++;
                    if (nodeRet == null) {
                        nodeRet = temp;
                    } else {
                        nodeRet = this.fNodeFactory.getCMBinOpNode(group.fCompositor, nodeRet, temp);
                    }
                }
            }
            if (nodeRet != null && group.fCompositor == 101 && count < group.fParticleCount) {
                nodeRet = this.fNodeFactory.getCMUniOpNode(5, nodeRet);
            }
        }
        return nodeRet;
    }

    private CMNode buildCompactSyntaxTree2(XSParticleDecl particle, int minOccurs, int maxOccurs) throws XNIException {
        CMNode nodeRet;
        if (minOccurs == 1 && maxOccurs == 1) {
            CMNodeFactory cMNodeFactory = this.fNodeFactory;
            short s2 = particle.fType;
            XSTerm xSTerm = particle.fValue;
            int i2 = this.fParticleCount;
            this.fParticleCount = i2 + 1;
            int i3 = this.fLeafCount;
            this.fLeafCount = i3 + 1;
            nodeRet = cMNodeFactory.getCMLeafNode(s2, xSTerm, i2, i3);
        } else if (minOccurs == 0 && maxOccurs == 1) {
            CMNodeFactory cMNodeFactory2 = this.fNodeFactory;
            short s3 = particle.fType;
            XSTerm xSTerm2 = particle.fValue;
            int i4 = this.fParticleCount;
            this.fParticleCount = i4 + 1;
            int i5 = this.fLeafCount;
            this.fLeafCount = i5 + 1;
            nodeRet = this.fNodeFactory.getCMUniOpNode(5, cMNodeFactory2.getCMLeafNode(s3, xSTerm2, i4, i5));
        } else if (minOccurs == 0 && maxOccurs == -1) {
            CMNodeFactory cMNodeFactory3 = this.fNodeFactory;
            short s4 = particle.fType;
            XSTerm xSTerm3 = particle.fValue;
            int i6 = this.fParticleCount;
            this.fParticleCount = i6 + 1;
            int i7 = this.fLeafCount;
            this.fLeafCount = i7 + 1;
            nodeRet = this.fNodeFactory.getCMUniOpNode(4, cMNodeFactory3.getCMLeafNode(s4, xSTerm3, i6, i7));
        } else if (minOccurs == 1 && maxOccurs == -1) {
            CMNodeFactory cMNodeFactory4 = this.fNodeFactory;
            short s5 = particle.fType;
            XSTerm xSTerm4 = particle.fValue;
            int i8 = this.fParticleCount;
            this.fParticleCount = i8 + 1;
            int i9 = this.fLeafCount;
            this.fLeafCount = i9 + 1;
            nodeRet = this.fNodeFactory.getCMUniOpNode(6, cMNodeFactory4.getCMLeafNode(s5, xSTerm4, i8, i9));
        } else {
            CMNodeFactory cMNodeFactory5 = this.fNodeFactory;
            short s6 = particle.fType;
            XSTerm xSTerm5 = particle.fValue;
            int i10 = this.fParticleCount;
            this.fParticleCount = i10 + 1;
            int i11 = this.fLeafCount;
            this.fLeafCount = i11 + 1;
            CMNode nodeRet2 = cMNodeFactory5.getCMRepeatingLeafNode(s6, xSTerm5, minOccurs, maxOccurs, i10, i11);
            if (minOccurs == 0) {
                nodeRet = this.fNodeFactory.getCMUniOpNode(4, nodeRet2);
            } else {
                nodeRet = this.fNodeFactory.getCMUniOpNode(6, nodeRet2);
            }
        }
        return nodeRet;
    }

    private boolean useRepeatingLeafNodes(XSParticleDecl particle) {
        int maxOccurs = particle.fMaxOccurs;
        int minOccurs = particle.fMinOccurs;
        short type = particle.fType;
        if (type == 3) {
            XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
            if (minOccurs != 1 || maxOccurs != 1) {
                if (group.fParticleCount != 1) {
                    return group.fParticleCount == 0;
                }
                XSParticleDecl particle2 = group.fParticles[0];
                short type2 = particle2.fType;
                return (type2 == 1 || type2 == 2) && particle2.fMinOccurs == 1 && particle2.fMaxOccurs == 1;
            }
            for (int i2 = 0; i2 < group.fParticleCount; i2++) {
                if (!useRepeatingLeafNodes(group.fParticles[i2])) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
