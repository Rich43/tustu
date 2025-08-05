package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.impl.PrismSettings;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.CacheHint;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGNode.class */
public abstract class NGNode {
    private static final GraphicsPipeline pipeline;
    private static final Boolean effectsSupported;
    private String name;
    private static final BoxBounds TEMP_BOUNDS;
    private static final RectBounds TEMP_RECT_BOUNDS;
    protected static final Affine3D TEMP_TRANSFORM;
    static final int DIRTY_REGION_INTERSECTS_NODE_BOUNDS = 1;
    static final int DIRTY_REGION_CONTAINS_NODE_BOUNDS = 2;
    static final int DIRTY_REGION_CONTAINS_OR_INTERSECTS_NODE_BOUNDS = 3;
    private NGNode parent;
    private boolean isClip;
    private NGNode clipNode;
    private Blend.Mode nodeBlendMode;
    private CacheFilter cacheFilter;
    private EffectFilter effectFilter;
    protected static final int DIRTY_CHILDREN_ACCUMULATED_THRESHOLD = 12;
    private DirtyHint hint;
    private static Point2D[] TEMP_POINTS2D_4;
    static final /* synthetic */ boolean $assertionsDisabled;
    private BaseTransform transform = BaseTransform.IDENTITY_TRANSFORM;
    protected BaseBounds transformedBounds = new RectBounds();
    protected BaseBounds contentBounds = new RectBounds();
    BaseBounds dirtyBounds = new RectBounds();
    private boolean visible = true;
    protected DirtyFlag dirty = DirtyFlag.DIRTY;
    private float opacity = 1.0f;
    private boolean depthTest = true;
    protected boolean childDirty = false;
    protected int dirtyChildrenAccumulated = 0;
    protected int cullingBits = 0;
    private RectBounds opaqueRegion = null;
    private boolean opaqueRegionInvalid = true;
    private int painted = 0;

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGNode$DirtyFlag.class */
    public enum DirtyFlag {
        CLEAN,
        DIRTY_BY_TRANSLATION,
        DIRTY
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGNode$RenderRootResult.class */
    protected enum RenderRootResult {
        NO_RENDER_ROOT,
        HAS_RENDER_ROOT,
        HAS_RENDER_ROOT_AND_IS_CLEAN
    }

    protected abstract void renderContent(Graphics graphics);

    protected abstract boolean hasOverlappingContents();

    static {
        $assertionsDisabled = !NGNode.class.desiredAssertionStatus();
        pipeline = GraphicsPipeline.getPipeline();
        effectsSupported = Boolean.valueOf(pipeline == null ? false : pipeline.isEffectSupported());
        TEMP_BOUNDS = new BoxBounds();
        TEMP_RECT_BOUNDS = new RectBounds();
        TEMP_TRANSFORM = new Affine3D();
        TEMP_POINTS2D_4 = new Point2D[]{new Point2D(), new Point2D(), new Point2D(), new Point2D()};
    }

    protected NGNode() {
    }

    public void setVisible(boolean value) {
        if (this.visible != value) {
            this.visible = value;
            markDirty();
        }
    }

    public void setContentBounds(BaseBounds bounds) {
        this.contentBounds = this.contentBounds.deriveWithNewBounds(bounds);
    }

    public void setTransformedBounds(BaseBounds bounds, boolean byTransformChangeOnly) {
        if (this.transformedBounds.equals(bounds)) {
            return;
        }
        if (this.dirtyBounds.isEmpty()) {
            this.dirtyBounds = this.dirtyBounds.deriveWithNewBounds(this.transformedBounds);
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(bounds);
        } else {
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(this.transformedBounds);
        }
        this.transformedBounds = this.transformedBounds.deriveWithNewBounds(bounds);
        if (hasVisuals() && !byTransformChangeOnly) {
            markDirty();
        }
    }

    public void setTransformMatrix(BaseTransform tx) {
        if (this.transform.equals(tx)) {
            return;
        }
        boolean useHint = false;
        if (this.parent != null && this.parent.cacheFilter != null && PrismSettings.scrollCacheOpt) {
            if (this.hint == null) {
                this.hint = new DirtyHint();
            } else if (this.transform.getMxx() == tx.getMxx() && this.transform.getMxy() == tx.getMxy() && this.transform.getMyy() == tx.getMyy() && this.transform.getMyx() == tx.getMyx() && this.transform.getMxz() == tx.getMxz() && this.transform.getMyz() == tx.getMyz() && this.transform.getMzx() == tx.getMzx() && this.transform.getMzy() == tx.getMzy() && this.transform.getMzz() == tx.getMzz() && this.transform.getMzt() == tx.getMzt()) {
                useHint = true;
                this.hint.translateXDelta = tx.getMxt() - this.transform.getMxt();
                this.hint.translateYDelta = tx.getMyt() - this.transform.getMyt();
            }
        }
        this.transform = this.transform.deriveWithNewTransform(tx);
        if (useHint) {
            markDirtyByTranslation();
        } else {
            markDirty();
        }
        invalidateOpaqueRegion();
    }

    public void setClipNode(NGNode clipNode) {
        if (clipNode != this.clipNode) {
            if (this.clipNode != null) {
                this.clipNode.setParent(null);
            }
            if (clipNode != null) {
                clipNode.setParent(this, true);
            }
            this.clipNode = clipNode;
            visualsChanged();
            invalidateOpaqueRegion();
        }
    }

    public void setOpacity(float opacity) {
        if (opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("Internal Error: The opacity must be between 0 and 1");
        }
        if (opacity != this.opacity) {
            float old = this.opacity;
            this.opacity = opacity;
            markDirty();
            if (old >= 1.0f || (opacity != 1.0f && opacity != 0.0f)) {
                if (opacity >= 1.0f) {
                    return;
                }
                if (old != 1.0f && old != 0.0f) {
                    return;
                }
            }
            invalidateOpaqueRegion();
        }
    }

    public void setNodeBlendMode(Blend.Mode blendMode) {
        if (this.nodeBlendMode != blendMode) {
            this.nodeBlendMode = blendMode;
            markDirty();
            invalidateOpaqueRegion();
        }
    }

    public void setDepthTest(boolean depthTest) {
        if (depthTest != this.depthTest) {
            this.depthTest = depthTest;
            visualsChanged();
        }
    }

    public void setCachedAsBitmap(boolean cached, CacheHint cacheHint) {
        if (cacheHint == null) {
            throw new IllegalArgumentException("Internal Error: cacheHint must not be null");
        }
        if (cached) {
            if (this.cacheFilter == null) {
                this.cacheFilter = new CacheFilter(this, cacheHint);
                markDirty();
                return;
            } else {
                if (!this.cacheFilter.matchesHint(cacheHint)) {
                    this.cacheFilter.setHint(cacheHint);
                    markDirty();
                    return;
                }
                return;
            }
        }
        if (this.cacheFilter != null) {
            this.cacheFilter.dispose();
            this.cacheFilter = null;
            markDirty();
        }
    }

    public void setEffect(Effect effect) {
        Effect old = getEffect();
        if (PrismSettings.disableEffects) {
            effect = null;
        }
        if (this.effectFilter == null && effect != null) {
            this.effectFilter = new EffectFilter(effect, this);
            visualsChanged();
        } else if (this.effectFilter != null && this.effectFilter.getEffect() != effect) {
            this.effectFilter.dispose();
            this.effectFilter = null;
            if (effect != null) {
                this.effectFilter = new EffectFilter(effect, this);
            }
            visualsChanged();
        }
        if (old != effect) {
            if (old == null || effect == null) {
                invalidateOpaqueRegion();
            }
        }
    }

    public void effectChanged() {
        visualsChanged();
    }

    public boolean isContentBounds2D() {
        return this.contentBounds.is2D() || (Affine3D.almostZero((double) this.contentBounds.getMaxZ()) && Affine3D.almostZero((double) this.contentBounds.getMinZ()));
    }

    public NGNode getParent() {
        return this.parent;
    }

    public void setParent(NGNode parent) {
        setParent(parent, false);
    }

    private void setParent(NGNode parent, boolean isClip) {
        this.parent = parent;
        this.isClip = isClip;
    }

    public final void setName(String value) {
        this.name = value;
    }

    public final String getName() {
        return this.name;
    }

    protected final Effect getEffect() {
        if (this.effectFilter == null) {
            return null;
        }
        return this.effectFilter.getEffect();
    }

    public boolean isVisible() {
        return this.visible;
    }

    public final BaseTransform getTransform() {
        return this.transform;
    }

    public final float getOpacity() {
        return this.opacity;
    }

    public final Blend.Mode getNodeBlendMode() {
        return this.nodeBlendMode;
    }

    public final boolean isDepthTest() {
        return this.depthTest;
    }

    public final CacheFilter getCacheFilter() {
        return this.cacheFilter;
    }

    public final EffectFilter getEffectFilter() {
        return this.effectFilter;
    }

    public final NGNode getClipNode() {
        return this.clipNode;
    }

    public BaseBounds getContentBounds(BaseBounds bounds, BaseTransform tx) {
        if (tx.isTranslateOrIdentity()) {
            BaseBounds bounds2 = bounds.deriveWithNewBounds(this.contentBounds);
            if (!tx.isIdentity()) {
                float translateX = (float) tx.getMxt();
                float translateY = (float) tx.getMyt();
                float translateZ = (float) tx.getMzt();
                bounds2 = bounds2.deriveWithNewBounds(bounds2.getMinX() + translateX, bounds2.getMinY() + translateY, bounds2.getMinZ() + translateZ, bounds2.getMaxX() + translateX, bounds2.getMaxY() + translateY, bounds2.getMaxZ() + translateZ);
            }
            return bounds2;
        }
        return computeBounds(bounds, tx);
    }

    private BaseBounds computeBounds(BaseBounds bounds, BaseTransform tx) {
        return tx.transform(this.contentBounds, bounds.deriveWithNewBounds(this.contentBounds));
    }

    public final BaseBounds getClippedBounds(BaseBounds bounds, BaseTransform tx) {
        BaseBounds effectBounds = getEffectBounds(bounds, tx);
        if (this.clipNode != null) {
            float ex1 = effectBounds.getMinX();
            float ey1 = effectBounds.getMinY();
            float ez1 = effectBounds.getMinZ();
            float ex2 = effectBounds.getMaxX();
            float ey2 = effectBounds.getMaxY();
            float ez2 = effectBounds.getMaxZ();
            effectBounds = this.clipNode.getCompleteBounds(effectBounds, tx);
            effectBounds.intersectWith(ex1, ey1, ez1, ex2, ey2, ez2);
        }
        return effectBounds;
    }

    public final BaseBounds getEffectBounds(BaseBounds bounds, BaseTransform tx) {
        if (this.effectFilter != null) {
            return this.effectFilter.getBounds(bounds, tx);
        }
        return getContentBounds(bounds, tx);
    }

    public final BaseBounds getCompleteBounds(BaseBounds bounds, BaseTransform tx) {
        if (tx.isIdentity()) {
            return bounds.deriveWithNewBounds(this.transformedBounds);
        }
        if (this.transform.isIdentity()) {
            return getClippedBounds(bounds, tx);
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
        BaseTransform boundsTx = tx.deriveWithConcatenation(this.transform);
        BaseBounds bounds2 = getClippedBounds(bounds, tx);
        if (boundsTx == tx) {
            tx.restoreTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        }
        return bounds2;
    }

    protected void visualsChanged() {
        invalidateCache();
        markDirty();
    }

    protected void geometryChanged() {
        invalidateCache();
        invalidateOpaqueRegion();
        if (hasVisuals()) {
            markDirty();
        }
    }

    public final void markDirty() {
        if (this.dirty != DirtyFlag.DIRTY) {
            this.dirty = DirtyFlag.DIRTY;
            markTreeDirty();
        }
    }

    private void markDirtyByTranslation() {
        if (this.dirty == DirtyFlag.CLEAN) {
            if (this.parent != null && this.parent.dirty == DirtyFlag.CLEAN && !this.parent.childDirty) {
                this.dirty = DirtyFlag.DIRTY_BY_TRANSLATION;
                this.parent.childDirty = true;
                this.parent.dirtyChildrenAccumulated++;
                this.parent.invalidateCacheByTranslation(this.hint);
                this.parent.markTreeDirty();
                return;
            }
            markDirty();
        }
    }

    protected final void markTreeDirtyNoIncrement() {
        if (this.parent != null) {
            if (!this.parent.childDirty || this.dirty == DirtyFlag.DIRTY_BY_TRANSLATION) {
                markTreeDirty();
            }
        }
    }

    protected final void markTreeDirty() {
        NGNode p2 = this.parent;
        boolean atClip = this.isClip;
        boolean byTranslation = this.dirty == DirtyFlag.DIRTY_BY_TRANSLATION;
        while (p2 != null && p2.dirty != DirtyFlag.DIRTY && (!p2.childDirty || atClip || byTranslation)) {
            if (atClip) {
                p2.dirty = DirtyFlag.DIRTY;
            } else if (!byTranslation) {
                p2.childDirty = true;
                p2.dirtyChildrenAccumulated++;
            }
            p2.invalidateCache();
            atClip = p2.isClip;
            byTranslation = p2.dirty == DirtyFlag.DIRTY_BY_TRANSLATION;
            p2 = p2.parent;
        }
        if (p2 != null && p2.dirty == DirtyFlag.CLEAN && !atClip && !byTranslation) {
            p2.dirtyChildrenAccumulated++;
        }
        if (p2 != null) {
            p2.invalidateCache();
        }
    }

    public final boolean isClean() {
        return this.dirty == DirtyFlag.CLEAN && !this.childDirty;
    }

    protected void clearDirty() {
        this.dirty = DirtyFlag.CLEAN;
        this.childDirty = false;
        this.dirtyBounds.makeEmpty();
        this.dirtyChildrenAccumulated = 0;
    }

    public void clearPainted() {
        this.painted = 0;
        if (this instanceof NGGroup) {
            List<NGNode> children = ((NGGroup) this).getChildren();
            for (int i2 = 0; i2 < children.size(); i2++) {
                children.get(i2).clearPainted();
            }
        }
    }

    public void clearDirtyTree() {
        clearDirty();
        if (getClipNode() != null) {
            getClipNode().clearDirtyTree();
        }
        if (this instanceof NGGroup) {
            List<NGNode> children = ((NGGroup) this).getChildren();
            for (int i2 = 0; i2 < children.size(); i2++) {
                NGNode child = children.get(i2);
                if (child.dirty != DirtyFlag.CLEAN || child.childDirty) {
                    child.clearDirtyTree();
                }
            }
        }
    }

    protected final void invalidateCache() {
        if (this.cacheFilter != null) {
            this.cacheFilter.invalidate();
        }
    }

    protected final void invalidateCacheByTranslation(DirtyHint hint) {
        if (this.cacheFilter != null) {
            this.cacheFilter.invalidateByTranslation(hint.translateXDelta, hint.translateYDelta);
        }
    }

    public int accumulateDirtyRegions(RectBounds clip, RectBounds dirtyRegionTemp, DirtyRegionPool regionPool, DirtyRegionContainer dirtyRegionContainer, BaseTransform tx, GeneralTransform3D pvTx) {
        if (clip == null || dirtyRegionTemp == null || regionPool == null || dirtyRegionContainer == null || tx == null || pvTx == null) {
            throw new NullPointerException();
        }
        if (this.dirty == DirtyFlag.CLEAN && !this.childDirty) {
            return 1;
        }
        if (this.dirty != DirtyFlag.CLEAN) {
            return accumulateNodeDirtyRegion(clip, dirtyRegionTemp, dirtyRegionContainer, tx, pvTx);
        }
        if ($assertionsDisabled || this.childDirty) {
            return accumulateGroupDirtyRegion(clip, dirtyRegionTemp, regionPool, dirtyRegionContainer, tx, pvTx);
        }
        throw new AssertionError();
    }

    int accumulateNodeDirtyRegion(RectBounds clip, RectBounds dirtyRegionTemp, DirtyRegionContainer dirtyRegionContainer, BaseTransform tx, GeneralTransform3D pvTx) {
        BaseBounds bb2 = computeDirtyRegion(dirtyRegionTemp, tx, pvTx);
        if (bb2 != dirtyRegionTemp) {
            bb2.flattenInto(dirtyRegionTemp);
        }
        if (dirtyRegionTemp.isEmpty() || clip.disjoint(dirtyRegionTemp)) {
            return 1;
        }
        if (dirtyRegionTemp.contains(clip)) {
            return 0;
        }
        dirtyRegionTemp.intersectWith(clip);
        dirtyRegionContainer.addDirtyRegion(dirtyRegionTemp);
        return 1;
    }

    int accumulateGroupDirtyRegion(RectBounds clip, RectBounds dirtyRegionTemp, DirtyRegionPool regionPool, DirtyRegionContainer dirtyRegionContainer, BaseTransform tx, GeneralTransform3D pvTx) {
        if (!$assertionsDisabled && !this.childDirty) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.dirty != DirtyFlag.CLEAN) {
            throw new AssertionError();
        }
        int status = 1;
        if (this.dirtyChildrenAccumulated > 12) {
            return accumulateNodeDirtyRegion(clip, dirtyRegionTemp, dirtyRegionContainer, tx, pvTx);
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
        BaseTransform renderTx = tx;
        if (this.transform != null) {
            renderTx = renderTx.deriveWithConcatenation(this.transform);
        }
        RectBounds myClip = clip;
        DirtyRegionContainer originalDirtyRegion = null;
        BaseTransform originalRenderTx = null;
        if (this.effectFilter != null) {
            try {
                myClip = new RectBounds();
                BaseBounds myClipBaseBounds = renderTx.inverseTransform(clip, TEMP_BOUNDS);
                myClipBaseBounds.flattenInto(myClip);
                originalRenderTx = renderTx;
                renderTx = BaseTransform.IDENTITY_TRANSFORM;
                originalDirtyRegion = dirtyRegionContainer;
                dirtyRegionContainer = regionPool.checkOut();
            } catch (NoninvertibleTransformException e2) {
                return 1;
            }
        } else if (this.clipNode != null) {
            originalDirtyRegion = dirtyRegionContainer;
            myClip = new RectBounds();
            BaseBounds clipBounds = this.clipNode.getCompleteBounds(myClip, renderTx);
            pvTx.transform(clipBounds, clipBounds);
            clipBounds.flattenInto(myClip);
            myClip.intersectWith(clip);
            dirtyRegionContainer = regionPool.checkOut();
        }
        List<NGNode> removed = ((NGGroup) this).getRemovedChildren();
        if (removed != null) {
            for (int i2 = removed.size() - 1; i2 >= 0; i2--) {
                NGNode removedChild = removed.get(i2);
                removedChild.dirty = DirtyFlag.DIRTY;
                status = removedChild.accumulateDirtyRegions(myClip, dirtyRegionTemp, regionPool, dirtyRegionContainer, renderTx, pvTx);
                if (status == 0) {
                    break;
                }
            }
        }
        List<NGNode> children = ((NGGroup) this).getChildren();
        int num = children.size();
        for (int i3 = 0; i3 < num && status == 1; i3++) {
            NGNode child = children.get(i3);
            status = child.accumulateDirtyRegions(myClip, dirtyRegionTemp, regionPool, dirtyRegionContainer, renderTx, pvTx);
            if (status == 0) {
                break;
            }
        }
        if (this.effectFilter != null && status == 1) {
            applyEffect(this.effectFilter, dirtyRegionContainer, regionPool);
            if (this.clipNode != null) {
                RectBounds myClip2 = new RectBounds();
                applyClip(this.clipNode.getCompleteBounds(myClip2, renderTx), dirtyRegionContainer);
            }
            applyTransform(originalRenderTx, dirtyRegionContainer);
            renderTx = originalRenderTx;
            originalDirtyRegion.merge(dirtyRegionContainer);
            regionPool.checkIn(dirtyRegionContainer);
        }
        if (renderTx == tx) {
            tx.restoreTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        }
        if (this.clipNode != null && this.effectFilter == null) {
            if (status == 0) {
                status = accumulateNodeDirtyRegion(clip, dirtyRegionTemp, originalDirtyRegion, tx, pvTx);
            } else {
                originalDirtyRegion.merge(dirtyRegionContainer);
            }
            regionPool.checkIn(dirtyRegionContainer);
        }
        return status;
    }

    private BaseBounds computeDirtyRegion(RectBounds dirtyRegionTemp, BaseTransform tx, GeneralTransform3D pvTx) {
        BaseBounds region;
        if (this.cacheFilter != null) {
            return this.cacheFilter.computeDirtyBounds(dirtyRegionTemp, tx, pvTx);
        }
        if (!this.dirtyBounds.isEmpty()) {
            region = dirtyRegionTemp.deriveWithNewBounds(this.dirtyBounds);
        } else {
            region = dirtyRegionTemp.deriveWithNewBounds(this.transformedBounds);
        }
        if (!region.isEmpty()) {
            BaseBounds region2 = computePadding(region);
            BaseBounds region3 = tx.transform(region2, region2);
            region = pvTx.transform(region3, region3);
        }
        return region;
    }

    protected BaseBounds computePadding(BaseBounds region) {
        return region;
    }

    protected boolean hasVisuals() {
        return true;
    }

    public final void doPreCulling(DirtyRegionContainer drc, BaseTransform tx, GeneralTransform3D pvTx) {
        if (drc == null || tx == null || pvTx == null) {
            throw new NullPointerException();
        }
        markCullRegions(drc, -1, tx, pvTx);
    }

    void markCullRegions(DirtyRegionContainer drc, int cullingRegionsBitsOfParent, BaseTransform tx, GeneralTransform3D pvTx) {
        RectBounds region;
        if (tx.isIdentity()) {
            TEMP_BOUNDS.deriveWithNewBounds(this.transformedBounds);
        } else {
            tx.transform(this.transformedBounds, TEMP_BOUNDS);
        }
        if (!pvTx.isIdentity()) {
            pvTx.transform(TEMP_BOUNDS, TEMP_BOUNDS);
        }
        TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
        this.cullingBits = 0;
        int mask = 1;
        for (int i2 = 0; i2 < drc.size() && (region = drc.getDirtyRegion(i2)) != null && !region.isEmpty(); i2++) {
            if ((cullingRegionsBitsOfParent == -1 || (cullingRegionsBitsOfParent & mask) != 0) && region.intersects(TEMP_RECT_BOUNDS)) {
                int b2 = 1;
                if (region.contains(TEMP_RECT_BOUNDS)) {
                    b2 = 2;
                }
                this.cullingBits |= b2 << (2 * i2);
            }
            mask <<= 2;
        }
        if (this.cullingBits == 0) {
            if (this.dirty != DirtyFlag.CLEAN || this.childDirty) {
                clearDirtyTree();
            }
        }
    }

    public final void printDirtyOpts(StringBuilder s2, List<NGNode> roots) {
        s2.append("\n*=Render Root\n");
        s2.append("d=Dirty\n");
        s2.append("dt=Dirty By Translation\n");
        s2.append("i=Dirty Region Intersects the NGNode\n");
        s2.append("c=Dirty Region Contains the NGNode\n");
        s2.append("ef=Effect Filter\n");
        s2.append("cf=Cache Filter\n");
        s2.append("cl=This node is a clip node\n");
        s2.append("b=Blend mode is set\n");
        s2.append("or=Opaque Region\n");
        printDirtyOpts(s2, this, BaseTransform.IDENTITY_TRANSFORM, "", roots);
    }

    private final void printDirtyOpts(StringBuilder s2, NGNode node, BaseTransform tx, String prefix, List<NGNode> roots) {
        if (!node.isVisible() || node.getOpacity() == 0.0f) {
            return;
        }
        BaseTransform copy = tx.copy().deriveWithConcatenation(node.getTransform());
        List<String> stuff = new ArrayList<>();
        for (int i2 = 0; i2 < roots.size(); i2++) {
            NGNode root = roots.get(i2);
            if (node == root) {
                stuff.add("*" + i2);
            }
        }
        if (node.dirty != DirtyFlag.CLEAN) {
            stuff.add(node.dirty == DirtyFlag.DIRTY ? PdfOps.d_TOKEN : "dt");
        }
        if (node.cullingBits != 0) {
            int mask = 17;
            for (int i3 = 0; i3 < 15; i3++) {
                int bits = node.cullingBits & mask;
                if (bits != 0) {
                    stuff.add(bits == 1 ? PdfOps.i_TOKEN + i3 : bits == 0 ? PdfOps.c_TOKEN + i3 : "ci" + i3);
                }
                mask <<= 2;
            }
        }
        if (node.effectFilter != null) {
            stuff.add("ef");
        }
        if (node.cacheFilter != null) {
            stuff.add("cf");
        }
        if (node.nodeBlendMode != null) {
            stuff.add(PdfOps.b_TOKEN);
        }
        RectBounds opaqueRegion = node.getOpaqueRegion();
        if (opaqueRegion != null) {
            RectBounds or = new RectBounds();
            copy.transform(opaqueRegion, or);
            stuff.add("or=" + or.getMinX() + ", " + or.getMinY() + ", " + or.getWidth() + ", " + or.getHeight());
        }
        if (stuff.isEmpty()) {
            s2.append(prefix + node.name + "\n");
        } else {
            String postfix = " [";
            for (int i4 = 0; i4 < stuff.size(); i4++) {
                postfix = postfix + stuff.get(i4);
                if (i4 < stuff.size() - 1) {
                    postfix = postfix + " ";
                }
            }
            s2.append(prefix + node.name + postfix + "]\n");
        }
        if (node.getClipNode() != null) {
            printDirtyOpts(s2, node.getClipNode(), copy, prefix + "  cl ", roots);
        }
        if (node instanceof NGGroup) {
            NGGroup g2 = (NGGroup) node;
            for (int i5 = 0; i5 < g2.getChildren().size(); i5++) {
                printDirtyOpts(s2, g2.getChildren().get(i5), copy, prefix + Constants.INDENT, roots);
            }
        }
    }

    public void drawDirtyOpts(BaseTransform tx, GeneralTransform3D pvTx, Rectangle clipBounds, int[] colorBuffer, int dirtyRegionIndex) {
        if ((this.painted & (1 << (dirtyRegionIndex * 2))) != 0) {
            tx.copy().deriveWithConcatenation(getTransform()).transform(this.contentBounds, TEMP_BOUNDS);
            if (pvTx != null) {
                pvTx.transform(TEMP_BOUNDS, TEMP_BOUNDS);
            }
            RectBounds bounds = new RectBounds();
            TEMP_BOUNDS.flattenInto(bounds);
            if (!$assertionsDisabled && clipBounds.width * clipBounds.height != colorBuffer.length) {
                throw new AssertionError();
            }
            bounds.intersectWith(clipBounds);
            int x2 = ((int) bounds.getMinX()) - clipBounds.f11913x;
            int y2 = ((int) bounds.getMinY()) - clipBounds.f11914y;
            int w2 = (int) (bounds.getWidth() + 0.5d);
            int h2 = (int) (bounds.getHeight() + 0.5d);
            if (w2 == 0 || h2 == 0) {
                return;
            }
            for (int i2 = y2; i2 < y2 + h2; i2++) {
                for (int j2 = x2; j2 < x2 + w2; j2++) {
                    int index = (i2 * clipBounds.width) + j2;
                    int color = colorBuffer[index];
                    if (color == 0) {
                        color = 134250240;
                    } else if ((this.painted & (3 << (dirtyRegionIndex * 2))) == 3) {
                        switch (color) {
                            case -2147451136:
                                color = -2147450880;
                                break;
                            case -2147450880:
                                color = -2139128064;
                                break;
                            case -2139128064:
                                color = -2139062272;
                                break;
                            case -2139062272:
                                color = -2139160576;
                                break;
                            default:
                                color = -2139095040;
                                break;
                        }
                    }
                    colorBuffer[index] = color;
                }
            }
        }
    }

    public final void getRenderRoot(NodePath path, RectBounds dirtyRegion, int cullingIndex, BaseTransform tx, GeneralTransform3D pvTx) {
        if (path == null || dirtyRegion == null || tx == null || pvTx == null) {
            throw new NullPointerException();
        }
        if (cullingIndex < -1 || cullingIndex > 15) {
            throw new IllegalArgumentException("cullingIndex cannot be < -1 or > 15");
        }
        RenderRootResult result = computeRenderRoot(path, dirtyRegion, cullingIndex, tx, pvTx);
        if (result == RenderRootResult.NO_RENDER_ROOT) {
            path.add(this);
        } else if (result == RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) {
            path.clear();
        }
    }

    RenderRootResult computeRenderRoot(NodePath path, RectBounds dirtyRegion, int cullingIndex, BaseTransform tx, GeneralTransform3D pvTx) {
        return computeNodeRenderRoot(path, dirtyRegion, cullingIndex, tx, pvTx);
    }

    private static int ccw(double px, double py, Point2D a2, Point2D b2) {
        return (int) Math.signum(((b2.f11907x - a2.f11907x) * (py - a2.f11908y)) - ((b2.f11908y - a2.f11908y) * (px - a2.f11907x)));
    }

    private static boolean pointInConvexQuad(double x2, double y2, Point2D[] rect) {
        int ccw01 = ccw(x2, y2, rect[0], rect[1]);
        int ccw12 = ccw(x2, y2, rect[1], rect[2]);
        int ccw23 = ccw(x2, y2, rect[2], rect[3]);
        int ccw31 = ccw(x2, y2, rect[3], rect[0]);
        int ccw012 = ccw01 ^ (ccw01 >>> 1);
        int ccw122 = ccw12 ^ (ccw12 >>> 1);
        int union = ccw012 | ccw122 | (ccw23 ^ (ccw23 >>> 1)) | (ccw31 ^ (ccw31 >>> 1));
        return union == Integer.MIN_VALUE || union == 1;
    }

    final RenderRootResult computeNodeRenderRoot(NodePath path, RectBounds dirtyRegion, int cullingIndex, BaseTransform tx, GeneralTransform3D pvTx) {
        if (cullingIndex != -1) {
            int bits = this.cullingBits >> (cullingIndex * 2);
            if ((bits & 3) == 0) {
                return RenderRootResult.NO_RENDER_ROOT;
            }
        }
        if (!isVisible()) {
            return RenderRootResult.NO_RENDER_ROOT;
        }
        RectBounds opaqueRegion = getOpaqueRegion();
        if (opaqueRegion == null) {
            return RenderRootResult.NO_RENDER_ROOT;
        }
        BaseTransform localToParentTx = getTransform();
        BaseTransform localToSceneTx = TEMP_TRANSFORM.deriveWithNewTransform(tx).deriveWithConcatenation(localToParentTx);
        if (checkBoundsInQuad(opaqueRegion, dirtyRegion, localToSceneTx, pvTx)) {
            path.add(this);
            return isClean() ? RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN : RenderRootResult.HAS_RENDER_ROOT;
        }
        return RenderRootResult.NO_RENDER_ROOT;
    }

    static boolean checkBoundsInQuad(RectBounds untransformedQuad, RectBounds innerBounds, BaseTransform tx, GeneralTransform3D pvTx) {
        if (pvTx.isIdentity() && (tx.getType() & (-16)) == 0) {
            if (tx.isIdentity()) {
                TEMP_BOUNDS.deriveWithNewBounds(untransformedQuad);
            } else {
                tx.transform(untransformedQuad, TEMP_BOUNDS);
            }
            TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
            return TEMP_RECT_BOUNDS.contains(innerBounds);
        }
        TEMP_POINTS2D_4[0].setLocation(untransformedQuad.getMinX(), untransformedQuad.getMinY());
        TEMP_POINTS2D_4[1].setLocation(untransformedQuad.getMaxX(), untransformedQuad.getMinY());
        TEMP_POINTS2D_4[2].setLocation(untransformedQuad.getMaxX(), untransformedQuad.getMaxY());
        TEMP_POINTS2D_4[3].setLocation(untransformedQuad.getMinX(), untransformedQuad.getMaxY());
        for (Point2D p2 : TEMP_POINTS2D_4) {
            tx.transform(p2, p2);
            if (!pvTx.isIdentity()) {
                pvTx.transform(p2, p2);
            }
        }
        return pointInConvexQuad((double) innerBounds.getMinX(), (double) innerBounds.getMinY(), TEMP_POINTS2D_4) && pointInConvexQuad((double) innerBounds.getMaxX(), (double) innerBounds.getMinY(), TEMP_POINTS2D_4) && pointInConvexQuad((double) innerBounds.getMaxX(), (double) innerBounds.getMaxY(), TEMP_POINTS2D_4) && pointInConvexQuad((double) innerBounds.getMinX(), (double) innerBounds.getMaxY(), TEMP_POINTS2D_4);
    }

    protected final void invalidateOpaqueRegion() {
        this.opaqueRegionInvalid = true;
        if (this.isClip) {
            this.parent.invalidateOpaqueRegion();
        }
    }

    final boolean isOpaqueRegionInvalid() {
        return this.opaqueRegionInvalid;
    }

    public final RectBounds getOpaqueRegion() {
        if (this.opaqueRegionInvalid || getEffect() != null) {
            this.opaqueRegionInvalid = false;
            if (supportsOpaqueRegions() && hasOpaqueRegion()) {
                this.opaqueRegion = computeOpaqueRegion(this.opaqueRegion == null ? new RectBounds() : this.opaqueRegion);
                if (!$assertionsDisabled && this.opaqueRegion == null) {
                    throw new AssertionError();
                }
                if (this.opaqueRegion == null) {
                    return null;
                }
                NGNode clip = getClipNode();
                if (clip != null) {
                    RectBounds clipOpaqueRegion = clip.getOpaqueRegion();
                    if (clipOpaqueRegion == null || (clip.getTransform().getType() & (-8)) != 0) {
                        this.opaqueRegion = null;
                        return null;
                    }
                    BaseBounds b2 = clip.getTransform().transform(clipOpaqueRegion, TEMP_BOUNDS);
                    b2.flattenInto(TEMP_RECT_BOUNDS);
                    this.opaqueRegion.intersectWith(TEMP_RECT_BOUNDS);
                }
            } else {
                this.opaqueRegion = null;
            }
        }
        return this.opaqueRegion;
    }

    protected boolean supportsOpaqueRegions() {
        return false;
    }

    protected boolean hasOpaqueRegion() {
        NGNode clip = getClipNode();
        Effect effect = getEffect();
        return (effect == null || !effect.reducesOpaquePixels()) && getOpacity() == 1.0f && (this.nodeBlendMode == null || this.nodeBlendMode == Blend.Mode.SRC_OVER) && (clip == null || (clip.supportsOpaqueRegions() && clip.hasOpaqueRegion()));
    }

    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        return null;
    }

    protected boolean isRectClip(BaseTransform xform, boolean permitRoundedRectangle) {
        return false;
    }

    public final void render(Graphics g2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("Nodes visited during render");
        }
        clearDirty();
        if (!this.visible || this.opacity == 0.0f) {
            return;
        }
        doRender(g2);
    }

    public void renderForcedContent(Graphics gOptional) {
    }

    boolean isShape3D() {
        return false;
    }

    protected void doRender(Graphics g2) {
        g2.setState3D(isShape3D());
        boolean preCullingTurnedOff = false;
        if (PrismSettings.dirtyOptsEnabled && g2.hasPreCullingBits()) {
            int bits = this.cullingBits >> (g2.getClipRectIndex() * 2);
            if ((bits & 3) == 0) {
                return;
            }
            if ((bits & 2) != 0) {
                g2.setHasPreCullingBits(false);
                preCullingTurnedOff = true;
            }
        }
        boolean prevDepthTest = g2.isDepthTest();
        g2.setDepthTest(isDepthTest());
        BaseTransform prevXform = g2.getTransformNoClone();
        double mxx = prevXform.getMxx();
        double mxy = prevXform.getMxy();
        double mxz = prevXform.getMxz();
        double mxt = prevXform.getMxt();
        double myx = prevXform.getMyx();
        double myy = prevXform.getMyy();
        double myz = prevXform.getMyz();
        double myt = prevXform.getMyt();
        double mzx = prevXform.getMzx();
        double mzy = prevXform.getMzy();
        double mzz = prevXform.getMzz();
        double mzt = prevXform.getMzt();
        g2.transform(getTransform());
        boolean p2 = false;
        if (!isShape3D() && (g2 instanceof ReadbackGraphics) && needsBlending()) {
            renderNodeBlendMode(g2);
            p2 = true;
        } else if (!isShape3D() && getOpacity() < 1.0f) {
            renderOpacity(g2);
            p2 = true;
        } else if (!isShape3D() && getCacheFilter() != null) {
            renderCached(g2);
            p2 = true;
        } else if (!isShape3D() && getClipNode() != null) {
            renderClip(g2);
            p2 = true;
        } else if (!isShape3D() && getEffectFilter() != null && effectsSupported.booleanValue()) {
            renderEffect(g2);
            p2 = true;
        } else {
            renderContent(g2);
            if (PrismSettings.showOverdraw) {
                p2 = (this instanceof NGRegion) || !(this instanceof NGGroup);
            }
        }
        if (preCullingTurnedOff) {
            g2.setHasPreCullingBits(true);
        }
        g2.setTransform3D(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        g2.setDepthTest(prevDepthTest);
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("Nodes rendered");
        }
        if (PrismSettings.showOverdraw) {
            if (p2) {
                this.painted |= 3 << (g2.getClipRectIndex() * 2);
            } else {
                this.painted |= 1 << (g2.getClipRectIndex() * 2);
            }
        }
    }

    protected boolean needsBlending() {
        Blend.Mode mode = getNodeBlendMode();
        return (mode == null || mode == Blend.Mode.SRC_OVER) ? false : true;
    }

    private void renderNodeBlendMode(Graphics g2) {
        BaseTransform curXform = g2.getTransformNoClone();
        BaseBounds clipBounds = getClippedBounds(new RectBounds(), curXform);
        if (clipBounds.isEmpty()) {
            clearDirtyTree();
            return;
        }
        if (!isReadbackSupported(g2)) {
            if (getOpacity() < 1.0f) {
                renderOpacity(g2);
                return;
            } else if (getClipNode() != null) {
                renderClip(g2);
                return;
            } else {
                renderContent(g2);
                return;
            }
        }
        Rectangle clipRect = new Rectangle(clipBounds);
        clipRect.intersectWith(PrEffectHelper.getGraphicsClipNoClone(g2));
        FilterContext fctx = getFilterContext(g2);
        PrDrawable contentImg = (PrDrawable) Effect.getCompatibleImage(fctx, clipRect.width, clipRect.height);
        if (contentImg == null) {
            clearDirtyTree();
            return;
        }
        Graphics gContentImg = contentImg.createGraphics();
        gContentImg.setHasPreCullingBits(g2.hasPreCullingBits());
        gContentImg.setClipRectIndex(g2.getClipRectIndex());
        gContentImg.translate(-clipRect.f11913x, -clipRect.f11914y);
        gContentImg.transform(curXform);
        if (getOpacity() < 1.0f) {
            renderOpacity(gContentImg);
        } else if (getCacheFilter() != null) {
            renderCached(gContentImg);
        } else if (getClipNode() != null) {
            renderClip(g2);
        } else if (getEffectFilter() != null) {
            renderEffect(gContentImg);
        } else {
            renderContent(gContentImg);
        }
        RTTexture bgRTT = ((ReadbackGraphics) g2).readBack(clipRect);
        PrDrawable bgPrD = PrDrawable.create(fctx, bgRTT);
        Blend blend = new Blend(getNodeBlendMode(), new PassThrough(bgPrD, clipRect), new PassThrough(contentImg, clipRect));
        CompositeMode oldmode = g2.getCompositeMode();
        g2.setTransform(null);
        g2.setCompositeMode(CompositeMode.SRC);
        PrEffectHelper.render(blend, g2, 0.0f, 0.0f, null);
        g2.setCompositeMode(oldmode);
        Effect.releaseCompatibleImage(fctx, contentImg);
        ((ReadbackGraphics) g2).releaseReadBackBuffer(bgRTT);
    }

    private void renderRectClip(Graphics g2, NGRectangle clipNode) {
        BaseBounds newClip = clipNode.getShape().getBounds();
        if (!clipNode.getTransform().isIdentity()) {
            newClip = clipNode.getTransform().transform(newClip, newClip);
        }
        BaseTransform curXform = g2.getTransformNoClone();
        Rectangle curClip = g2.getClipRectNoClone();
        BaseBounds newClip2 = curXform.transform(newClip, newClip);
        newClip2.intersectWith(PrEffectHelper.getGraphicsClipNoClone(g2));
        if (newClip2.isEmpty() || newClip2.getWidth() == 0.0f || newClip2.getHeight() == 0.0f) {
            clearDirtyTree();
            return;
        }
        g2.setClipRect(new Rectangle(newClip2));
        renderForClip(g2);
        g2.setClipRect(curClip);
        clipNode.clearDirty();
    }

    void renderClip(Graphics g2) {
        if (getClipNode().getOpacity() == 0.0d) {
            clearDirtyTree();
            return;
        }
        BaseTransform curXform = g2.getTransformNoClone();
        BaseBounds clipBounds = getClippedBounds(new RectBounds(), curXform);
        if (clipBounds.isEmpty()) {
            clearDirtyTree();
            return;
        }
        if (getClipNode() instanceof NGRectangle) {
            NGRectangle rectNode = (NGRectangle) getClipNode();
            if (rectNode.isRectClip(curXform, false)) {
                renderRectClip(g2, rectNode);
                return;
            }
        }
        Rectangle clipRect = new Rectangle(clipBounds);
        clipRect.intersectWith(PrEffectHelper.getGraphicsClipNoClone(g2));
        if (!curXform.is2D()) {
            Rectangle savedClip = g2.getClipRect();
            g2.setClipRect(clipRect);
            NodeEffectInput clipInput = new NodeEffectInput(getClipNode(), NodeEffectInput.RenderType.FULL_CONTENT);
            NodeEffectInput nodeInput = new NodeEffectInput(this, NodeEffectInput.RenderType.CLIPPED_CONTENT);
            Blend blend = new Blend(Blend.Mode.SRC_IN, clipInput, nodeInput);
            PrEffectHelper.render(blend, g2, 0.0f, 0.0f, null);
            clipInput.flush();
            nodeInput.flush();
            g2.setClipRect(savedClip);
            clearDirtyTree();
            return;
        }
        FilterContext fctx = getFilterContext(g2);
        PrDrawable contentImg = (PrDrawable) Effect.getCompatibleImage(fctx, clipRect.width, clipRect.height);
        if (contentImg == null) {
            clearDirtyTree();
            return;
        }
        Graphics gContentImg = contentImg.createGraphics();
        gContentImg.setExtraAlpha(g2.getExtraAlpha());
        gContentImg.setHasPreCullingBits(g2.hasPreCullingBits());
        gContentImg.setClipRectIndex(g2.getClipRectIndex());
        gContentImg.translate(-clipRect.f11913x, -clipRect.f11914y);
        gContentImg.transform(curXform);
        renderForClip(gContentImg);
        PrDrawable clipImg = (PrDrawable) Effect.getCompatibleImage(fctx, clipRect.width, clipRect.height);
        if (clipImg == null) {
            getClipNode().clearDirtyTree();
            Effect.releaseCompatibleImage(fctx, contentImg);
            return;
        }
        Graphics gClipImg = clipImg.createGraphics();
        gClipImg.translate(-clipRect.f11913x, -clipRect.f11914y);
        gClipImg.transform(curXform);
        getClipNode().render(gClipImg);
        g2.setTransform(null);
        Blend blend2 = new Blend(Blend.Mode.SRC_IN, new PassThrough(clipImg, clipRect), new PassThrough(contentImg, clipRect));
        PrEffectHelper.render(blend2, g2, 0.0f, 0.0f, null);
        Effect.releaseCompatibleImage(fctx, contentImg);
        Effect.releaseCompatibleImage(fctx, clipImg);
    }

    void renderForClip(Graphics g2) {
        if (getEffectFilter() != null) {
            renderEffect(g2);
        } else {
            renderContent(g2);
        }
    }

    private void renderOpacity(Graphics g2) {
        if (getEffectFilter() != null || getCacheFilter() != null || getClipNode() != null || !hasOverlappingContents()) {
            float ea = g2.getExtraAlpha();
            g2.setExtraAlpha(ea * getOpacity());
            if (getCacheFilter() != null) {
                renderCached(g2);
            } else if (getClipNode() != null) {
                renderClip(g2);
            } else if (getEffectFilter() != null) {
                renderEffect(g2);
            } else {
                renderContent(g2);
            }
            g2.setExtraAlpha(ea);
            return;
        }
        FilterContext fctx = getFilterContext(g2);
        BaseTransform curXform = g2.getTransformNoClone();
        BaseBounds bounds = getContentBounds(new RectBounds(), curXform);
        Rectangle r2 = new Rectangle(bounds);
        r2.intersectWith(PrEffectHelper.getGraphicsClipNoClone(g2));
        PrDrawable img = (PrDrawable) Effect.getCompatibleImage(fctx, r2.width, r2.height);
        if (img == null) {
            return;
        }
        Graphics gImg = img.createGraphics();
        gImg.setHasPreCullingBits(g2.hasPreCullingBits());
        gImg.setClipRectIndex(g2.getClipRectIndex());
        gImg.translate(-r2.f11913x, -r2.f11914y);
        gImg.transform(curXform);
        renderContent(gImg);
        g2.setTransform(null);
        float ea2 = g2.getExtraAlpha();
        g2.setExtraAlpha(getOpacity() * ea2);
        g2.drawTexture(img.getTextureObject(), r2.f11913x, r2.f11914y, r2.width, r2.height);
        g2.setExtraAlpha(ea2);
        Effect.releaseCompatibleImage(fctx, img);
    }

    private void renderCached(Graphics g2) {
        if (isContentBounds2D() && g2.getTransformNoClone().is2D() && !(g2 instanceof PrinterGraphics)) {
            getCacheFilter().render(g2);
        } else {
            renderContent(g2);
        }
    }

    protected void renderEffect(Graphics g2) {
        getEffectFilter().render(g2);
    }

    boolean isReadbackSupported(Graphics g2) {
        return (g2 instanceof ReadbackGraphics) && ((ReadbackGraphics) g2).canReadBack();
    }

    static FilterContext getFilterContext(Graphics g2) {
        Screen s2 = g2.getAssociatedScreen();
        if (s2 == null) {
            return PrFilterContext.getPrinterContext(g2.getResourceFactory());
        }
        return PrFilterContext.getInstance(s2);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGNode$PassThrough.class */
    private static class PassThrough extends Effect {
        private PrDrawable img;
        private Rectangle bounds;

        PassThrough(PrDrawable img, Rectangle bounds) {
            this.img = img;
            this.bounds = bounds;
        }

        @Override // com.sun.scenario.effect.Effect
        public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
            this.img.lock();
            ImageData id = new ImageData(fctx, this.img, new Rectangle(this.bounds));
            id.setReusable(true);
            return id;
        }

        @Override // com.sun.scenario.effect.Effect
        public RectBounds getBounds(BaseTransform transform, Effect defaultInput) {
            return new RectBounds(this.bounds);
        }

        @Override // com.sun.scenario.effect.Effect
        public Effect.AccelType getAccelType(FilterContext fctx) {
            return Effect.AccelType.INTRINSIC;
        }

        @Override // com.sun.scenario.effect.Effect
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override // com.sun.scenario.effect.Effect
        public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
            return null;
        }
    }

    public void release() {
    }

    public String toString() {
        return this.name == null ? super.toString() : this.name;
    }

    public void applyTransform(BaseTransform tx, DirtyRegionContainer drc) {
        int i2 = 0;
        while (i2 < drc.size()) {
            drc.setDirtyRegion(i2, (RectBounds) tx.transform(drc.getDirtyRegion(i2), drc.getDirtyRegion(i2)));
            if (drc.checkAndClearRegion(i2)) {
                i2--;
            }
            i2++;
        }
    }

    public void applyClip(BaseBounds clipBounds, DirtyRegionContainer drc) {
        int i2 = 0;
        while (i2 < drc.size()) {
            drc.getDirtyRegion(i2).intersectWith(clipBounds);
            if (drc.checkAndClearRegion(i2)) {
                i2--;
            }
            i2++;
        }
    }

    public void applyEffect(EffectFilter effectFilter, DirtyRegionContainer drc, DirtyRegionPool regionPool) {
        Effect effect = effectFilter.getEffect();
        EffectDirtyBoundsHelper helper = EffectDirtyBoundsHelper.getInstance();
        helper.setInputBounds(this.contentBounds);
        helper.setDirtyRegions(drc);
        DirtyRegionContainer effectDrc = effect.getDirtyRegions(helper, regionPool);
        drc.deriveWithNewContainer(effectDrc);
        regionPool.checkIn(effectDrc);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGNode$EffectDirtyBoundsHelper.class */
    private static class EffectDirtyBoundsHelper extends Effect {
        private BaseBounds bounds;
        private static EffectDirtyBoundsHelper instance = null;
        private DirtyRegionContainer drc;

        private EffectDirtyBoundsHelper() {
        }

        public void setInputBounds(BaseBounds inputBounds) {
            this.bounds = inputBounds;
        }

        @Override // com.sun.scenario.effect.Effect
        public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.scenario.effect.Effect
        public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
            if (this.bounds.getBoundsType() == BaseBounds.BoundsType.RECTANGLE) {
                return this.bounds;
            }
            return new RectBounds(this.bounds.getMinX(), this.bounds.getMinY(), this.bounds.getMaxX(), this.bounds.getMaxY());
        }

        @Override // com.sun.scenario.effect.Effect
        public Effect.AccelType getAccelType(FilterContext fctx) {
            return null;
        }

        public static EffectDirtyBoundsHelper getInstance() {
            if (instance == null) {
                instance = new EffectDirtyBoundsHelper();
            }
            return instance;
        }

        @Override // com.sun.scenario.effect.Effect
        public boolean reducesOpaquePixels() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDirtyRegions(DirtyRegionContainer drc) {
            this.drc = drc;
        }

        @Override // com.sun.scenario.effect.Effect
        public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
            DirtyRegionContainer ret = regionPool.checkOut();
            ret.deriveWithNewContainer(this.drc);
            return ret;
        }
    }
}
