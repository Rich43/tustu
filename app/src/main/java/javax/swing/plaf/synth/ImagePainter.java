package javax.swing.plaf.synth;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import sun.swing.plaf.synth.Paint9Painter;

/* loaded from: rt.jar:javax/swing/plaf/synth/ImagePainter.class */
class ImagePainter extends SynthPainter {
    private static final StringBuffer CACHE_KEY = new StringBuffer("SynthCacheKey");
    private Image image;
    private Insets sInsets;
    private Insets dInsets;
    private URL path;
    private boolean tiles;
    private boolean paintCenter;
    private Paint9Painter imageCache;
    private boolean center;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023 A[Catch: all -> 0x0045, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x0017, B:10:0x0043, B:8:0x0023), top: B:17:0x0006 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static sun.swing.plaf.synth.Paint9Painter getPaint9Painter() {
        /*
            java.lang.StringBuffer r0 = javax.swing.plaf.synth.ImagePainter.CACHE_KEY
            r1 = r0
            r4 = r1
            monitor-enter(r0)
            sun.awt.AppContext r0 = sun.awt.AppContext.getAppContext()     // Catch: java.lang.Throwable -> L45
            java.lang.StringBuffer r1 = javax.swing.plaf.synth.ImagePainter.CACHE_KEY     // Catch: java.lang.Throwable -> L45
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L45
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0     // Catch: java.lang.Throwable -> L45
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L23
            r0 = r5
            java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> L45
            sun.swing.plaf.synth.Paint9Painter r0 = (sun.swing.plaf.synth.Paint9Painter) r0     // Catch: java.lang.Throwable -> L45
            r1 = r0
            r6 = r1
            if (r0 != 0) goto L41
        L23:
            sun.swing.plaf.synth.Paint9Painter r0 = new sun.swing.plaf.synth.Paint9Painter     // Catch: java.lang.Throwable -> L45
            r1 = r0
            r2 = 30
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L45
            r6 = r0
            java.lang.ref.WeakReference r0 = new java.lang.ref.WeakReference     // Catch: java.lang.Throwable -> L45
            r1 = r0
            r2 = r6
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L45
            r5 = r0
            sun.awt.AppContext r0 = sun.awt.AppContext.getAppContext()     // Catch: java.lang.Throwable -> L45
            java.lang.StringBuffer r1 = javax.swing.plaf.synth.ImagePainter.CACHE_KEY     // Catch: java.lang.Throwable -> L45
            r2 = r5
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Throwable -> L45
        L41:
            r0 = r6
            r1 = r4
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L45
            return r0
        L45:
            r7 = move-exception
            r0 = r4
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L45
            r0 = r7
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.synth.ImagePainter.getPaint9Painter():sun.swing.plaf.synth.Paint9Painter");
    }

    ImagePainter(boolean z2, boolean z3, Insets insets, Insets insets2, URL url, boolean z4) {
        if (insets != null) {
            this.sInsets = (Insets) insets.clone();
        }
        if (insets2 == null) {
            this.dInsets = this.sInsets;
        } else {
            this.dInsets = (Insets) insets2.clone();
        }
        this.tiles = z2;
        this.paintCenter = z3;
        this.imageCache = getPaint9Painter();
        this.path = url;
        this.center = z4;
    }

    public boolean getTiles() {
        return this.tiles;
    }

    public boolean getPaintsCenter() {
        return this.paintCenter;
    }

    public boolean getCenter() {
        return this.center;
    }

    public Insets getInsets(Insets insets) {
        if (insets == null) {
            return (Insets) this.dInsets.clone();
        }
        insets.left = this.dInsets.left;
        insets.right = this.dInsets.right;
        insets.top = this.dInsets.top;
        insets.bottom = this.dInsets.bottom;
        return insets;
    }

    public Image getImage() {
        if (this.image == null) {
            this.image = new ImageIcon(this.path, (String) null).getImage();
        }
        return this.image;
    }

    private void paint(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        Paint9Painter.PaintType paintType;
        Image image = getImage();
        if (Paint9Painter.validImage(image)) {
            if (getCenter()) {
                paintType = Paint9Painter.PaintType.CENTER;
            } else if (!getTiles()) {
                paintType = Paint9Painter.PaintType.PAINT9_STRETCH;
            } else {
                paintType = Paint9Painter.PaintType.PAINT9_TILE;
            }
            int i6 = 512;
            if (!getCenter() && !getPaintsCenter()) {
                i6 = 512 | 16;
            }
            this.imageCache.paint(synthContext.getComponent(), graphics, i2, i3, i4, i5, image, this.sInsets, this.dInsets, paintType, i6);
        }
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintArrowButtonForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintCheckBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintColorChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintColorChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintComboBoxBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintComboBoxBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopIconBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopIconBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintDesktopPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintEditorPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintEditorPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFileChooserBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFileChooserBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFormattedTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintFormattedTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameTitlePaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameTitlePaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintInternalFrameBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintLabelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintLabelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintListBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintListBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintOptionPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintOptionPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPanelBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPanelBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPasswordFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPasswordFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPopupMenuBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintPopupMenuBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintProgressBarForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonMenuItemBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonMenuItemBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRadioButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRootPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintRootPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollBarTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintScrollPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSeparatorForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderThumbBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderThumbBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSliderTrackBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSpinnerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSpinnerBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDividerForeground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneDragDivider(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintSplitPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneTabBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTabbedPaneContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableHeaderBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableHeaderBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTableBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextAreaBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextAreaBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextPaneBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextPaneBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextFieldBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTextFieldBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToggleButtonBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToggleButtonBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarContentBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolBarDragWindowBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5, int i6) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolTipBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintToolTipBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintTreeCellFocus(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintViewportBackground(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.synth.SynthPainter
    public void paintViewportBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        paint(synthContext, graphics, i2, i3, i4, i5);
    }
}
