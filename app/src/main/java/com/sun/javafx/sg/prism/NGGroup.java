package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGGroup.class */
public class NGGroup extends NGNode {
    private Blend.Mode blendMode = Blend.Mode.SRC_OVER;
    private List<NGNode> children = new ArrayList(1);
    private List<NGNode> unmod = Collections.unmodifiableList(this.children);
    private List<NGNode> removed;
    private static final int REGION_INTERSECTS_MASK = 357913941;

    public List<NGNode> getChildren() {
        return this.unmod;
    }

    public void add(int index, NGNode node) {
        if (index < -1 || index > this.children.size()) {
            throw new IndexOutOfBoundsException("invalid index");
        }
        node.setParent(this);
        this.childDirty = true;
        if (index == -1) {
            this.children.add(node);
        } else {
            this.children.add(index, node);
        }
        node.markDirty();
        markTreeDirtyNoIncrement();
        geometryChanged();
    }

    public void clearFrom(int fromIndex) {
        if (fromIndex < this.children.size()) {
            this.children.subList(fromIndex, this.children.size()).clear();
            geometryChanged();
            this.childDirty = true;
            markTreeDirtyNoIncrement();
        }
    }

    public List<NGNode> getRemovedChildren() {
        return this.removed;
    }

    public void addToRemoved(NGNode n2) {
        if (this.removed == null) {
            this.removed = new ArrayList();
        }
        if (this.dirtyChildrenAccumulated > 12) {
            return;
        }
        this.removed.add(n2);
        this.dirtyChildrenAccumulated++;
        if (this.dirtyChildrenAccumulated > 12) {
            this.removed.clear();
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void clearDirty() {
        super.clearDirty();
        if (this.removed != null) {
            this.removed.clear();
        }
    }

    public void remove(NGNode node) {
        this.children.remove(node);
        geometryChanged();
        this.childDirty = true;
        markTreeDirtyNoIncrement();
    }

    public void remove(int index) {
        this.children.remove(index);
        geometryChanged();
        this.childDirty = true;
        markTreeDirtyNoIncrement();
    }

    public void clear() {
        this.children.clear();
        this.childDirty = false;
        geometryChanged();
        markTreeDirtyNoIncrement();
    }

    public void setBlendMode(Object blendMode) {
        if (blendMode == null) {
            throw new IllegalArgumentException("Mode must be non-null");
        }
        if (this.blendMode != blendMode) {
            this.blendMode = (Blend.Mode) blendMode;
            visualsChanged();
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void renderForcedContent(Graphics gOptional) {
        if (this.children == null) {
            return;
        }
        for (int i2 = 0; i2 < this.children.size(); i2++) {
            this.children.get(i2).renderForcedContent(gOptional);
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        NGNode child;
        ImageData imageData;
        if (this.children == null) {
            return;
        }
        NodePath renderRoot = g2.getRenderRoot();
        int startPos = 0;
        if (renderRoot != null) {
            if (renderRoot.hasNext()) {
                renderRoot.next();
                startPos = this.children.indexOf(renderRoot.getCurrentNode());
                for (int i2 = 0; i2 < startPos; i2++) {
                    this.children.get(i2).clearDirtyTree();
                }
            } else {
                g2.setRenderRoot(null);
            }
        }
        if (this.blendMode == Blend.Mode.SRC_OVER || this.children.size() < 2) {
            for (int i3 = startPos; i3 < this.children.size(); i3++) {
                try {
                    child = this.children.get(i3);
                } catch (Exception e2) {
                    child = null;
                }
                if (child != null) {
                    child.render(g2);
                }
            }
            return;
        }
        Blend b2 = new Blend(this.blendMode, null, null);
        FilterContext fctx = getFilterContext(g2);
        ImageData bot = null;
        boolean idValid = true;
        while (true) {
            BaseTransform transform = g2.getTransformNoClone().copy();
            if (bot != null) {
                bot.unref();
                bot = null;
            }
            Rectangle rclip = PrEffectHelper.getGraphicsClipNoClone(g2);
            for (int i4 = startPos; i4 < this.children.size(); i4++) {
                NGNode child2 = this.children.get(i4);
                ImageData top = NodeEffectInput.getImageDataForNode(fctx, child2, false, transform, rclip);
                if (bot == null) {
                    imageData = top;
                } else {
                    ImageData newbot = b2.filterImageDatas(fctx, transform, rclip, null, new ImageData[]{bot, top});
                    bot.unref();
                    top.unref();
                    imageData = newbot;
                }
                bot = imageData;
            }
            if (bot != null) {
                boolean zValidate = bot.validate(fctx);
                idValid = zValidate;
                if (zValidate) {
                    Rectangle r2 = bot.getUntransformedBounds();
                    PrDrawable botimg = (PrDrawable) bot.getUntransformedImage();
                    g2.setTransform(bot.getTransform());
                    g2.drawTexture(botimg.getTextureObject(), r2.f11913x, r2.f11914y, r2.width, r2.height);
                }
            }
            if (bot != null && idValid) {
                break;
            }
        }
        if (bot != null) {
            bot.unref();
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        if (this.blendMode != Blend.Mode.SRC_OVER) {
            return false;
        }
        int n2 = this.children == null ? 0 : this.children.size();
        if (n2 == 1) {
            return this.children.get(0).hasOverlappingContents();
        }
        return n2 != 0;
    }

    public boolean isEmpty() {
        return this.children == null || this.children.isEmpty();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasVisuals() {
        return false;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean needsBlending() {
        Blend.Mode mode = getNodeBlendMode();
        return mode != null;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected NGNode.RenderRootResult computeRenderRoot(NodePath path, RectBounds dirtyRegion, int cullingIndex, BaseTransform tx, GeneralTransform3D pvTx) {
        if (cullingIndex != -1) {
            int bits = this.cullingBits >> (cullingIndex * 2);
            if ((bits & 3) == 0) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            if ((bits & 2) != 0) {
                cullingIndex = -1;
            }
        }
        if (!isVisible()) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if (getOpacity() != 1.0d || ((getEffect() != null && getEffect().reducesOpaquePixels()) || needsBlending())) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if (getClipNode() != null) {
            NGNode clip = getClipNode();
            RectBounds clipBounds = clip.getOpaqueRegion();
            if (clipBounds == null) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            TEMP_TRANSFORM.deriveWithNewTransform(tx).deriveWithConcatenation(getTransform()).deriveWithConcatenation(clip.getTransform());
            if (!checkBoundsInQuad(clipBounds, dirtyRegion, TEMP_TRANSFORM, pvTx)) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
        }
        double mxx = tx.getMxx();
        double mxy = tx.getMxy();
        double mxz = tx.getMxz();
        double mxt = tx.getMxt();
        double myx = tx.getMyx();
        double myy = tx.getMyy();
        double myz = tx.getMyz();
        double myt = tx.getMyt();
        double mzx = tx.getMzx();
        double mzy = tx.getMzy();
        double mzz = tx.getMzz();
        double mzt = tx.getMzt();
        BaseTransform chTx = tx.deriveWithConcatenation(getTransform());
        NGNode.RenderRootResult result = NGNode.RenderRootResult.NO_RENDER_ROOT;
        boolean followingChildrenClean = true;
        int resultIdx = this.children.size() - 1;
        while (true) {
            if (resultIdx < 0) {
                break;
            }
            NGNode child = this.children.get(resultIdx);
            result = child.computeRenderRoot(path, dirtyRegion, cullingIndex, chTx, pvTx);
            followingChildrenClean &= child.isClean();
            if (result == NGNode.RenderRootResult.HAS_RENDER_ROOT) {
                path.add(this);
                break;
            }
            if (result != NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) {
                resultIdx--;
            } else {
                path.add(this);
                if (!followingChildrenClean) {
                    result = NGNode.RenderRootResult.HAS_RENDER_ROOT;
                }
            }
        }
        tx.restoreTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        return result;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void markCullRegions(DirtyRegionContainer drc, int cullingRegionsBitsOfParent, BaseTransform tx, GeneralTransform3D pvTx) {
        super.markCullRegions(drc, cullingRegionsBitsOfParent, tx, pvTx);
        if (this.cullingBits == -1 || (this.cullingBits != 0 && (this.cullingBits & REGION_INTERSECTS_MASK) != 0)) {
            double mxx = tx.getMxx();
            double mxy = tx.getMxy();
            double mxz = tx.getMxz();
            double mxt = tx.getMxt();
            double myx = tx.getMyx();
            double myy = tx.getMyy();
            double myz = tx.getMyz();
            double myt = tx.getMyt();
            double mzx = tx.getMzx();
            double mzy = tx.getMzy();
            double mzz = tx.getMzz();
            double mzt = tx.getMzt();
            BaseTransform chTx = tx.deriveWithConcatenation(getTransform());
            for (int chldIdx = 0; chldIdx < this.children.size(); chldIdx++) {
                NGNode child = this.children.get(chldIdx);
                child.markCullRegions(drc, this.cullingBits, chTx, pvTx);
            }
            tx.restoreTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void drawDirtyOpts(BaseTransform tx, GeneralTransform3D pvTx, Rectangle clipBounds, int[] countBuffer, int dirtyRegionIndex) {
        super.drawDirtyOpts(tx, pvTx, clipBounds, countBuffer, dirtyRegionIndex);
        BaseTransform clone = tx.copy();
        BaseTransform clone2 = clone.deriveWithConcatenation(getTransform());
        for (int childIndex = 0; childIndex < this.children.size(); childIndex++) {
            NGNode child = this.children.get(childIndex);
            child.drawDirtyOpts(clone2, pvTx, clipBounds, countBuffer, dirtyRegionIndex);
        }
    }
}
