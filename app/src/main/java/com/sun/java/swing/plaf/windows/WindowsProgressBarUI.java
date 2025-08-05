package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsProgressBarUI.class */
public class WindowsProgressBarUI extends BasicProgressBarUI {
    private Rectangle previousFullBox;
    private Insets indeterminateInsets;

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsProgressBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void installDefaults() {
        super.installDefaults();
        if (XPStyle.getXP() != null) {
            LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.FALSE);
            this.progressBar.setBorder(null);
            this.indeterminateInsets = UIManager.getInsets("ProgressBar.indeterminateInsets");
        }
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int i4;
        int i5;
        int baseline = super.getBaseline(jComponent, i2, i3);
        if (XPStyle.getXP() != null && this.progressBar.isStringPainted() && this.progressBar.getOrientation() == 0) {
            FontMetrics fontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
            int i6 = this.progressBar.getInsets().top;
            if (this.progressBar.isIndeterminate()) {
                i4 = -1;
                i5 = i3 - 1;
            } else {
                i4 = 0;
                i5 = i3 - 3;
            }
            baseline = i4 + ((((i5 + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2);
        }
        return baseline;
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Dimension getPreferredInnerHorizontal() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            return new Dimension((int) super.getPreferredInnerHorizontal().getWidth(), xp.getSkin(this.progressBar, TMSchema.Part.PP_BAR).getHeight());
        }
        return super.getPreferredInnerHorizontal();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Dimension getPreferredInnerVertical() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            return new Dimension(xp.getSkin(this.progressBar, TMSchema.Part.PP_BARVERT).getWidth(), (int) super.getPreferredInnerVertical().getHeight());
        }
        return super.getPreferredInnerVertical();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void paintDeterminate(Graphics graphics, JComponent jComponent) {
        int i2;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            boolean z2 = this.progressBar.getOrientation() == 1;
            boolean zIsLeftToRight = WindowsGraphicsUtils.isLeftToRight(jComponent);
            int width = this.progressBar.getWidth();
            int height = this.progressBar.getHeight() - 1;
            int amountFull = getAmountFull(null, width, height);
            paintXPBackground(graphics, z2, width, height);
            if (this.progressBar.isStringPainted()) {
                graphics.setColor(this.progressBar.getForeground());
                int i3 = height - 2;
                int i4 = width - 2;
                if (i4 <= 0 || i3 <= 0) {
                    return;
                }
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setStroke(new BasicStroke(z2 ? i4 : i3, 0, 2));
                if (!z2) {
                    if (zIsLeftToRight) {
                        graphics2D.drawLine(2, (i3 / 2) + 1, amountFull - 2, (i3 / 2) + 1);
                    } else {
                        graphics2D.drawLine(2 + i4, (i3 / 2) + 1, (2 + i4) - (amountFull - 2), (i3 / 2) + 1);
                    }
                    paintString(graphics, 0, 0, i4, i3, amountFull, null);
                    return;
                }
                graphics2D.drawLine((i4 / 2) + 1, i3 + 1, (i4 / 2) + 1, ((i3 + 1) - amountFull) + 2);
                paintString(graphics, 2, 2, i4, i3, amountFull, null);
                return;
            }
            XPStyle.Skin skin = xp.getSkin(this.progressBar, z2 ? TMSchema.Part.PP_CHUNKVERT : TMSchema.Part.PP_CHUNK);
            if (z2) {
                i2 = width - 5;
            } else {
                i2 = height - 5;
            }
            int i5 = xp.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSCHUNKSIZE, 2);
            int i6 = xp.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
            int i7 = (amountFull - 4) / (i5 + i6);
            if (i6 > 0 && (i7 * (i5 + i6)) + i5 < amountFull - 4) {
                i7++;
            }
            for (int i8 = 0; i8 < i7; i8++) {
                if (z2) {
                    skin.paintSkin(graphics, 3, ((height - (i8 * (i5 + i6))) - i5) - 2, i2, i5, null);
                } else if (zIsLeftToRight) {
                    skin.paintSkin(graphics, 4 + (i8 * (i5 + i6)), 2, i5, i2, null);
                } else {
                    skin.paintSkin(graphics, width - (2 + ((i8 + 1) * (i5 + i6))), 2, i5, i2, null);
                }
            }
            return;
        }
        super.paintDeterminate(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void setAnimationIndex(int i2) {
        super.setAnimationIndex(i2);
        if (XPStyle.getXP() != null) {
            if (this.boxRect != null) {
                Rectangle fullChunkBounds = getFullChunkBounds(this.boxRect);
                if (this.previousFullBox != null) {
                    fullChunkBounds.add(this.previousFullBox);
                }
                this.progressBar.repaint(fullChunkBounds);
                return;
            }
            this.progressBar.repaint();
        }
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected int getBoxLength(int i2, int i3) {
        if (XPStyle.getXP() != null) {
            return 6;
        }
        return super.getBoxLength(i2, i3);
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected Rectangle getBox(Rectangle rectangle) {
        Rectangle box = super.getBox(rectangle);
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            boolean z2 = this.progressBar.getOrientation() == 1;
            TMSchema.Part part = z2 ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR;
            Insets insets = this.indeterminateInsets;
            int animationIndex = getAnimationIndex();
            int frameCount = getFrameCount() / 2;
            int i2 = xp.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
            int i3 = animationIndex % frameCount;
            if (!z2) {
                box.f12373y += insets.top;
                box.height = (this.progressBar.getHeight() - insets.top) - insets.bottom;
                box.f12372x = ((int) (((((this.progressBar.getWidth() - insets.left) - insets.right) + ((box.width + i2) * 2)) / frameCount) * i3)) + insets.left;
            } else {
                box.f12372x += insets.left;
                box.width = (this.progressBar.getWidth() - insets.left) - insets.right;
                box.f12373y = ((int) (((((this.progressBar.getHeight() - insets.top) - insets.bottom) + ((box.height + i2) * 2)) / frameCount) * i3)) + insets.top;
            }
        }
        return box;
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    protected void paintIndeterminate(Graphics graphics, JComponent jComponent) {
        if (XPStyle.getXP() != null) {
            boolean z2 = this.progressBar.getOrientation() == 1;
            int width = this.progressBar.getWidth();
            int height = this.progressBar.getHeight();
            paintXPBackground(graphics, z2, width, height);
            this.boxRect = getBox(this.boxRect);
            if (this.boxRect != null) {
                graphics.setColor(this.progressBar.getForeground());
                if (!(graphics instanceof Graphics2D)) {
                    return;
                }
                paintIndeterminateFrame(this.boxRect, (Graphics2D) graphics, z2, width, height);
                if (this.progressBar.isStringPainted()) {
                    if (!z2) {
                        paintString(graphics, -1, -1, width, height, 0, null);
                        return;
                    } else {
                        paintString(graphics, 1, 1, width, height, 0, null);
                        return;
                    }
                }
                return;
            }
            return;
        }
        super.paintIndeterminate(graphics, jComponent);
    }

    private Rectangle getFullChunkBounds(Rectangle rectangle) {
        boolean z2 = this.progressBar.getOrientation() == 1;
        XPStyle xp = XPStyle.getXP();
        int i2 = xp != null ? xp.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0) : 0;
        if (!z2) {
            int i3 = rectangle.width + i2;
            return new Rectangle(rectangle.f12372x - (i3 * 2), rectangle.f12373y, i3 * 3, rectangle.height);
        }
        int i4 = rectangle.height + i2;
        return new Rectangle(rectangle.f12372x, rectangle.f12373y - (i4 * 2), rectangle.width, i4 * 3);
    }

    private void paintIndeterminateFrame(Rectangle rectangle, Graphics2D graphics2D, boolean z2, int i2, int i3) {
        int i4;
        int i5;
        XPStyle xp = XPStyle.getXP();
        if (xp == null) {
            return;
        }
        Graphics2D graphics2D2 = (Graphics2D) graphics2D.create();
        TMSchema.Part part = z2 ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR;
        TMSchema.Part part2 = z2 ? TMSchema.Part.PP_CHUNKVERT : TMSchema.Part.PP_CHUNK;
        int i6 = xp.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
        if (!z2) {
            i4 = (-rectangle.width) - i6;
            i5 = 0;
        } else {
            i4 = 0;
            i5 = (-rectangle.height) - i6;
        }
        Rectangle fullChunkBounds = getFullChunkBounds(rectangle);
        this.previousFullBox = fullChunkBounds;
        Insets insets = this.indeterminateInsets;
        graphics2D2.clip(new Rectangle(insets.left, insets.top, (i2 - insets.left) - insets.right, (i3 - insets.top) - insets.bottom).intersection(fullChunkBounds));
        XPStyle.Skin skin = xp.getSkin(this.progressBar, part2);
        graphics2D2.setComposite(AlphaComposite.getInstance(3, 0.8f));
        skin.paintSkin(graphics2D2, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, null);
        rectangle.translate(i4, i5);
        graphics2D2.setComposite(AlphaComposite.getInstance(3, 0.5f));
        skin.paintSkin(graphics2D2, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, null);
        rectangle.translate(i4, i5);
        graphics2D2.setComposite(AlphaComposite.getInstance(3, 0.2f));
        skin.paintSkin(graphics2D2, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, null);
        graphics2D2.dispose();
    }

    private void paintXPBackground(Graphics graphics, boolean z2, int i2, int i3) {
        XPStyle xp = XPStyle.getXP();
        if (xp == null) {
            return;
        }
        xp.getSkin(this.progressBar, z2 ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR).paintSkin(graphics, 0, 0, i2, i3, null);
    }
}
