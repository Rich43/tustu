package javafx.scene.media;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:javafx/scene/media/NGMediaView.class */
class NGMediaView extends NGNode {
    private boolean smooth = true;
    private final RectBounds dimension = new RectBounds();
    private final RectBounds viewport = new RectBounds();
    private PrismMediaFrameHandler handler;
    private MediaPlayer player;
    private MediaFrameTracker frameTracker;

    NGMediaView() {
    }

    public void renderNextFrame() {
        visualsChanged();
    }

    public boolean isSmooth() {
        return this.smooth;
    }

    public void setSmooth(boolean smooth) {
        if (smooth != this.smooth) {
            this.smooth = smooth;
            visualsChanged();
        }
    }

    public void setX(float x2) {
        if (x2 != this.dimension.getMinX()) {
            float width = this.dimension.getWidth();
            this.dimension.setMinX(x2);
            this.dimension.setMaxX(x2 + width);
            geometryChanged();
        }
    }

    public void setY(float y2) {
        if (y2 != this.dimension.getMinY()) {
            float height = this.dimension.getHeight();
            this.dimension.setMinY(y2);
            this.dimension.setMaxY(y2 + height);
            geometryChanged();
        }
    }

    public void setMediaProvider(Object provider) {
        if (provider == null) {
            this.player = null;
            this.handler = null;
            geometryChanged();
        } else if (provider instanceof MediaPlayer) {
            this.player = (MediaPlayer) provider;
            this.handler = PrismMediaFrameHandler.getHandler(this.player);
            geometryChanged();
        }
    }

    public void setViewport(float fitWidth, float fitHeight, float vx, float vy, float vw, float vh, boolean preserveRatio) {
        float w2 = 0.0f;
        float h2 = 0.0f;
        float newW = fitWidth;
        float newH = fitHeight;
        if (null != this.player) {
            Media m2 = this.player.getMedia();
            w2 = m2.getWidth();
            h2 = m2.getHeight();
        }
        if (vw > 0.0f && vh > 0.0f) {
            this.viewport.setBounds(vx, vy, vx + vw, vy + vh);
            w2 = vw;
            h2 = vh;
        } else {
            this.viewport.setBounds(0.0f, 0.0f, w2, h2);
        }
        if (fitWidth <= 0.0f && fitHeight <= 0.0f) {
            newW = w2;
            newH = h2;
        } else if (preserveRatio) {
            if (fitWidth <= 0.0d) {
                newW = h2 > 0.0f ? w2 * (fitHeight / h2) : 0.0f;
                newH = fitHeight;
            } else if (fitHeight <= 0.0d) {
                newW = fitWidth;
                newH = w2 > 0.0f ? h2 * (fitWidth / w2) : 0.0f;
            } else {
                if (w2 == 0.0f) {
                    w2 = fitWidth;
                }
                if (h2 == 0.0f) {
                    h2 = fitHeight;
                }
                float scale = Math.min(fitWidth / w2, fitHeight / h2);
                newW = w2 * scale;
                newH = h2 * scale;
            }
        } else if (fitHeight <= 0.0d) {
            newH = h2;
        } else if (fitWidth <= 0.0d) {
            newW = w2;
        }
        if (newH < 1.0f) {
            newH = 1.0f;
        }
        if (newW < 1.0f) {
            newW = 1.0f;
        }
        this.dimension.setMaxX(this.dimension.getMinX() + newW);
        this.dimension.setMaxY(this.dimension.getMinY() + newH);
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        VideoDataBuffer frame;
        if (null == this.handler || null == this.player || null == (frame = this.player.impl_getLatestFrame())) {
            return;
        }
        Texture texture = this.handler.getTexture(g2, frame);
        if (texture != null) {
            float iw = this.viewport.getWidth();
            float ih = this.viewport.getHeight();
            boolean dimensionsSet = !this.dimension.isEmpty();
            boolean doScale = dimensionsSet && !(iw == this.dimension.getWidth() && ih == this.dimension.getHeight());
            g2.translate(this.dimension.getMinX(), this.dimension.getMinY());
            if (doScale && iw != 0.0f && ih != 0.0f) {
                float scaleW = this.dimension.getWidth() / iw;
                float scaleH = this.dimension.getHeight() / ih;
                g2.scale(scaleW, scaleH);
            }
            float sx1 = this.viewport.getMinX();
            float sy1 = this.viewport.getMinY();
            float sx2 = sx1 + iw;
            float sy2 = sy1 + ih;
            g2.drawTexture(texture, 0.0f, 0.0f, iw, ih, sx1, sy1, sx2, sy2);
            texture.unlock();
            if (null != this.frameTracker) {
                this.frameTracker.incrementRenderedFrameCount(1);
            }
        }
        frame.releaseFrame();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setFrameTracker(MediaFrameTracker t2) {
        this.frameTracker = t2;
    }
}
