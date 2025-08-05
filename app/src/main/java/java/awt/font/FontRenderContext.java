package java.awt.font;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/* loaded from: rt.jar:java/awt/font/FontRenderContext.class */
public class FontRenderContext {
    private transient AffineTransform tx;
    private transient Object aaHintValue;
    private transient Object fmHintValue;
    private transient boolean defaulting;

    protected FontRenderContext() {
        this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
        this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
        this.defaulting = true;
    }

    public FontRenderContext(AffineTransform affineTransform, boolean z2, boolean z3) {
        if (affineTransform != null && !affineTransform.isIdentity()) {
            this.tx = new AffineTransform(affineTransform);
        }
        if (z2) {
            this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
        } else {
            this.aaHintValue = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
        }
        if (z3) {
            this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
        } else {
            this.fmHintValue = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
        }
    }

    public FontRenderContext(AffineTransform affineTransform, Object obj, Object obj2) {
        if (affineTransform != null && !affineTransform.isIdentity()) {
            this.tx = new AffineTransform(affineTransform);
        }
        try {
            if (RenderingHints.KEY_TEXT_ANTIALIASING.isCompatibleValue(obj)) {
                this.aaHintValue = obj;
                try {
                    if (RenderingHints.KEY_FRACTIONALMETRICS.isCompatibleValue(obj2)) {
                        this.fmHintValue = obj2;
                        return;
                    }
                    throw new IllegalArgumentException("FM hint:" + obj2);
                } catch (Exception e2) {
                    throw new IllegalArgumentException("FM hint:" + obj2);
                }
            }
            throw new IllegalArgumentException("AA hint:" + obj);
        } catch (Exception e3) {
            throw new IllegalArgumentException("AA hint:" + obj);
        }
    }

    public boolean isTransformed() {
        return !this.defaulting ? this.tx != null : !getTransform().isIdentity();
    }

    public int getTransformType() {
        if (!this.defaulting) {
            if (this.tx == null) {
                return 0;
            }
            return this.tx.getType();
        }
        return getTransform().getType();
    }

    public AffineTransform getTransform() {
        return this.tx == null ? new AffineTransform() : new AffineTransform(this.tx);
    }

    public boolean isAntiAliased() {
        return (this.aaHintValue == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || this.aaHintValue == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) ? false : true;
    }

    public boolean usesFractionalMetrics() {
        return (this.fmHintValue == RenderingHints.VALUE_FRACTIONALMETRICS_OFF || this.fmHintValue == RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT) ? false : true;
    }

    public Object getAntiAliasingHint() {
        if (this.defaulting) {
            if (isAntiAliased()) {
                return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            }
            return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
        }
        return this.aaHintValue;
    }

    public Object getFractionalMetricsHint() {
        if (this.defaulting) {
            if (usesFractionalMetrics()) {
                return RenderingHints.VALUE_FRACTIONALMETRICS_ON;
            }
            return RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
        }
        return this.fmHintValue;
    }

    public boolean equals(Object obj) {
        try {
            return equals((FontRenderContext) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public boolean equals(FontRenderContext fontRenderContext) {
        if (this == fontRenderContext) {
            return true;
        }
        if (fontRenderContext == null) {
            return false;
        }
        if (fontRenderContext.defaulting || this.defaulting) {
            return fontRenderContext.getAntiAliasingHint() == getAntiAliasingHint() && fontRenderContext.getFractionalMetricsHint() == getFractionalMetricsHint() && fontRenderContext.getTransform().equals(getTransform());
        }
        if (fontRenderContext.aaHintValue == this.aaHintValue && fontRenderContext.fmHintValue == this.fmHintValue) {
            return this.tx == null ? fontRenderContext.tx == null : this.tx.equals(fontRenderContext.tx);
        }
        return false;
    }

    public int hashCode() {
        int iHashCode;
        int iHashCode2 = this.tx == null ? 0 : this.tx.hashCode();
        if (this.defaulting) {
            iHashCode = iHashCode2 + getAntiAliasingHint().hashCode() + getFractionalMetricsHint().hashCode();
        } else {
            iHashCode = iHashCode2 + this.aaHintValue.hashCode() + this.fmHintValue.hashCode();
        }
        return iHashCode;
    }
}
