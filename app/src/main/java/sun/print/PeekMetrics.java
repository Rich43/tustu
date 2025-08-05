package sun.print;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.font.TextLayout;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;

/* loaded from: rt.jar:sun/print/PeekMetrics.class */
public class PeekMetrics {
    private boolean mHasNonSolidColors;
    private boolean mHasCompositing;
    private boolean mHasText;
    private boolean mHasImages;

    public boolean hasNonSolidColors() {
        return this.mHasNonSolidColors;
    }

    public boolean hasCompositing() {
        return this.mHasCompositing;
    }

    public boolean hasText() {
        return this.mHasText;
    }

    public boolean hasImages() {
        return this.mHasImages;
    }

    public void fill(Graphics2D graphics2D) {
        checkDrawingMode(graphics2D);
    }

    public void draw(Graphics2D graphics2D) {
        checkDrawingMode(graphics2D);
    }

    public void clear(Graphics2D graphics2D) {
        checkPaint(graphics2D.getBackground());
    }

    public void drawText(Graphics2D graphics2D) {
        this.mHasText = true;
        checkDrawingMode(graphics2D);
    }

    public void drawText(Graphics2D graphics2D, TextLayout textLayout) {
        this.mHasText = true;
        checkDrawingMode(graphics2D);
    }

    public void drawImage(Graphics2D graphics2D, Image image) {
        this.mHasImages = true;
    }

    public void drawImage(Graphics2D graphics2D, RenderedImage renderedImage) {
        this.mHasImages = true;
    }

    public void drawImage(Graphics2D graphics2D, RenderableImage renderableImage) {
        this.mHasImages = true;
    }

    private void checkDrawingMode(Graphics2D graphics2D) {
        checkPaint(graphics2D.getPaint());
        checkAlpha(graphics2D.getComposite());
    }

    private void checkPaint(Paint paint) {
        if (paint instanceof Color) {
            if (((Color) paint).getAlpha() < 255) {
                this.mHasNonSolidColors = true;
                return;
            }
            return;
        }
        this.mHasNonSolidColors = true;
    }

    private void checkAlpha(Composite composite) {
        if (composite instanceof AlphaComposite) {
            AlphaComposite alphaComposite = (AlphaComposite) composite;
            float alpha = alphaComposite.getAlpha();
            int rule = alphaComposite.getRule();
            if (alpha != 1.0d || (rule != 2 && rule != 3)) {
                this.mHasCompositing = true;
                return;
            }
            return;
        }
        this.mHasCompositing = true;
    }
}
