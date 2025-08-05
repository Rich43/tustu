package javax.swing.plaf.basic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ProgressBarUI;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicProgressBarUI.class */
public class BasicProgressBarUI extends ProgressBarUI {
    private int cachedPercent;
    private int cellLength;
    private int cellSpacing;
    private Color selectionForeground;
    private Color selectionBackground;
    private Animator animator;
    protected JProgressBar progressBar;
    protected ChangeListener changeListener;
    private Handler handler;
    private int numFrames;
    private int repaintInterval;
    private int cycleTime;
    private static boolean ADJUSTTIMER = true;
    protected Rectangle boxRect;
    private Rectangle nextPaintRect;
    private Rectangle componentInnards;
    private Rectangle oldComponentInnards;
    private int animationIndex = 0;
    private double delta = 0.0d;
    private int maxPosition = 0;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicProgressBarUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.progressBar = (JProgressBar) jComponent;
        installDefaults();
        installListeners();
        if (this.progressBar.isIndeterminate()) {
            initIndeterminateValues();
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        if (this.progressBar.isIndeterminate()) {
            cleanUpIndeterminateValues();
        }
        uninstallDefaults();
        uninstallListeners();
        this.progressBar = null;
    }

    protected void installDefaults() {
        LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.TRUE);
        LookAndFeel.installBorder(this.progressBar, "ProgressBar.border");
        LookAndFeel.installColorsAndFont(this.progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font");
        this.cellLength = UIManager.getInt("ProgressBar.cellLength");
        if (this.cellLength == 0) {
            this.cellLength = 1;
        }
        this.cellSpacing = UIManager.getInt("ProgressBar.cellSpacing");
        this.selectionForeground = UIManager.getColor("ProgressBar.selectionForeground");
        this.selectionBackground = UIManager.getColor("ProgressBar.selectionBackground");
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.progressBar);
    }

    protected void installListeners() {
        this.changeListener = getHandler();
        this.progressBar.addChangeListener(this.changeListener);
        this.progressBar.addPropertyChangeListener(getHandler());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected void startAnimationTimer() {
        if (this.animator == null) {
            this.animator = new Animator();
        }
        this.animator.start(getRepaintInterval());
    }

    protected void stopAnimationTimer() {
        if (this.animator == null) {
            return;
        }
        this.animator.stop();
    }

    protected void uninstallListeners() {
        this.progressBar.removeChangeListener(this.changeListener);
        this.progressBar.removePropertyChangeListener(getHandler());
        this.handler = null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        if (this.progressBar.isStringPainted() && this.progressBar.getOrientation() == 0) {
            FontMetrics fontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
            Insets insets = this.progressBar.getInsets();
            return insets.top + ((((((i3 - insets.top) - insets.bottom) + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2);
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        if (this.progressBar.isStringPainted() && this.progressBar.getOrientation() == 0) {
            return Component.BaselineResizeBehavior.CENTER_OFFSET;
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    protected Dimension getPreferredInnerHorizontal() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.progressBar, this, "ProgressBar.horizontalSize");
        if (dimension == null) {
            dimension = new Dimension(146, 12);
        }
        return dimension;
    }

    protected Dimension getPreferredInnerVertical() {
        Dimension dimension = (Dimension) DefaultLookup.get(this.progressBar, this, "ProgressBar.verticalSize");
        if (dimension == null) {
            dimension = new Dimension(12, 146);
        }
        return dimension;
    }

    protected Color getSelectionForeground() {
        return this.selectionForeground;
    }

    protected Color getSelectionBackground() {
        return this.selectionBackground;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCachedPercent() {
        return this.cachedPercent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCachedPercent(int i2) {
        this.cachedPercent = i2;
    }

    protected int getCellLength() {
        if (this.progressBar.isStringPainted()) {
            return 1;
        }
        return this.cellLength;
    }

    protected void setCellLength(int i2) {
        this.cellLength = i2;
    }

    protected int getCellSpacing() {
        if (this.progressBar.isStringPainted()) {
            return 0;
        }
        return this.cellSpacing;
    }

    protected void setCellSpacing(int i2) {
        this.cellSpacing = i2;
    }

    protected int getAmountFull(Insets insets, int i2, int i3) {
        int iRound = 0;
        BoundedRangeModel model = this.progressBar.getModel();
        if (model.getMaximum() - model.getMinimum() != 0) {
            if (this.progressBar.getOrientation() == 0) {
                iRound = (int) Math.round(i2 * this.progressBar.getPercentComplete());
            } else {
                iRound = (int) Math.round(i3 * this.progressBar.getPercentComplete());
            }
        }
        return iRound;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (this.progressBar.isIndeterminate()) {
            paintIndeterminate(graphics, jComponent);
        } else {
            paintDeterminate(graphics, jComponent);
        }
    }

    protected Rectangle getBox(Rectangle rectangle) {
        int animationIndex = getAnimationIndex();
        int i2 = this.numFrames / 2;
        if (sizeChanged() || this.delta == 0.0d || this.maxPosition == 0.0d) {
            updateSizes();
        }
        Rectangle genericBox = getGenericBox(rectangle);
        if (genericBox == null || i2 <= 0) {
            return null;
        }
        if (this.progressBar.getOrientation() == 0) {
            if (animationIndex < i2) {
                genericBox.f12372x = this.componentInnards.f12372x + ((int) Math.round(this.delta * animationIndex));
            } else {
                genericBox.f12372x = this.maxPosition - ((int) Math.round(this.delta * (animationIndex - i2)));
            }
        } else if (animationIndex < i2) {
            genericBox.f12373y = this.componentInnards.f12373y + ((int) Math.round(this.delta * animationIndex));
        } else {
            genericBox.f12373y = this.maxPosition - ((int) Math.round(this.delta * (animationIndex - i2)));
        }
        return genericBox;
    }

    private void updateSizes() {
        if (this.progressBar.getOrientation() == 0) {
            this.maxPosition = (this.componentInnards.f12372x + this.componentInnards.width) - getBoxLength(this.componentInnards.width, this.componentInnards.height);
        } else {
            this.maxPosition = (this.componentInnards.f12373y + this.componentInnards.height) - getBoxLength(this.componentInnards.height, this.componentInnards.width);
        }
        this.delta = (2.0d * this.maxPosition) / this.numFrames;
    }

    private Rectangle getGenericBox(Rectangle rectangle) {
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        if (this.progressBar.getOrientation() == 0) {
            rectangle.width = getBoxLength(this.componentInnards.width, this.componentInnards.height);
            if (rectangle.width < 0) {
                rectangle = null;
            } else {
                rectangle.height = this.componentInnards.height;
                rectangle.f12373y = this.componentInnards.f12373y;
            }
        } else {
            rectangle.height = getBoxLength(this.componentInnards.height, this.componentInnards.width);
            if (rectangle.height < 0) {
                rectangle = null;
            } else {
                rectangle.width = this.componentInnards.width;
                rectangle.f12372x = this.componentInnards.f12372x;
            }
        }
        return rectangle;
    }

    protected int getBoxLength(int i2, int i3) {
        return (int) Math.round(i2 / 6.0d);
    }

    protected void paintIndeterminate(Graphics graphics, JComponent jComponent) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }
        Insets insets = this.progressBar.getInsets();
        int width = this.progressBar.getWidth() - (insets.right + insets.left);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        if (width <= 0 || height <= 0) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        this.boxRect = getBox(this.boxRect);
        if (this.boxRect != null) {
            graphics2D.setColor(this.progressBar.getForeground());
            graphics2D.fillRect(this.boxRect.f12372x, this.boxRect.f12373y, this.boxRect.width, this.boxRect.height);
        }
        if (this.progressBar.isStringPainted()) {
            if (this.progressBar.getOrientation() == 0) {
                paintString(graphics2D, insets.left, insets.top, width, height, this.boxRect.f12372x, this.boxRect.width, insets);
            } else {
                paintString(graphics2D, insets.left, insets.top, width, height, this.boxRect.f12373y, this.boxRect.height, insets);
            }
        }
    }

    protected void paintDeterminate(Graphics graphics, JComponent jComponent) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }
        Insets insets = this.progressBar.getInsets();
        int width = this.progressBar.getWidth() - (insets.right + insets.left);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        if (width <= 0 || height <= 0) {
            return;
        }
        int cellLength = getCellLength();
        int cellSpacing = getCellSpacing();
        int amountFull = getAmountFull(insets, width, height);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(this.progressBar.getForeground());
        if (this.progressBar.getOrientation() == 0) {
            if (cellSpacing == 0 && amountFull > 0) {
                graphics2D.setStroke(new BasicStroke(height, 0, 2));
            } else {
                graphics2D.setStroke(new BasicStroke(height, 0, 2, 0.0f, new float[]{cellLength, cellSpacing}, 0.0f));
            }
            if (BasicGraphicsUtils.isLeftToRight(jComponent)) {
                graphics2D.drawLine(insets.left, (height / 2) + insets.top, amountFull + insets.left, (height / 2) + insets.top);
            } else {
                graphics2D.drawLine(width + insets.left, (height / 2) + insets.top, (width + insets.left) - amountFull, (height / 2) + insets.top);
            }
        } else {
            if (cellSpacing == 0 && amountFull > 0) {
                graphics2D.setStroke(new BasicStroke(width, 0, 2));
            } else {
                graphics2D.setStroke(new BasicStroke(width, 0, 2, 0.0f, new float[]{cellLength, cellSpacing}, 0.0f));
            }
            graphics2D.drawLine((width / 2) + insets.left, insets.top + height, (width / 2) + insets.left, (insets.top + height) - amountFull);
        }
        if (this.progressBar.isStringPainted()) {
            paintString(graphics, insets.left, insets.top, width, height, amountFull, insets);
        }
    }

    protected void paintString(Graphics graphics, int i2, int i3, int i4, int i5, int i6, Insets insets) {
        if (this.progressBar.getOrientation() == 0) {
            if (BasicGraphicsUtils.isLeftToRight(this.progressBar)) {
                if (this.progressBar.isIndeterminate()) {
                    this.boxRect = getBox(this.boxRect);
                    paintString(graphics, i2, i3, i4, i5, this.boxRect.f12372x, this.boxRect.width, insets);
                    return;
                } else {
                    paintString(graphics, i2, i3, i4, i5, i2, i6, insets);
                    return;
                }
            }
            paintString(graphics, i2, i3, i4, i5, (i2 + i4) - i6, i6, insets);
            return;
        }
        if (this.progressBar.isIndeterminate()) {
            this.boxRect = getBox(this.boxRect);
            paintString(graphics, i2, i3, i4, i5, this.boxRect.f12373y, this.boxRect.height, insets);
        } else {
            paintString(graphics, i2, i3, i4, i5, (i3 + i5) - i6, i6, insets);
        }
    }

    private void paintString(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, Insets insets) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        String string = this.progressBar.getString();
        graphics2D.setFont(this.progressBar.getFont());
        Point stringPlacement = getStringPlacement(graphics2D, string, i2, i3, i4, i5);
        Rectangle clipBounds = graphics2D.getClipBounds();
        if (this.progressBar.getOrientation() == 0) {
            graphics2D.setColor(getSelectionBackground());
            SwingUtilities2.drawString(this.progressBar, graphics2D, string, stringPlacement.f12370x, stringPlacement.f12371y);
            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(i6, i3, i7, i5);
            SwingUtilities2.drawString(this.progressBar, graphics2D, string, stringPlacement.f12370x, stringPlacement.f12371y);
        } else {
            graphics2D.setColor(getSelectionBackground());
            graphics2D.setFont(this.progressBar.getFont().deriveFont(AffineTransform.getRotateInstance(1.5707963267948966d)));
            Point stringPlacement2 = getStringPlacement(graphics2D, string, i2, i3, i4, i5);
            SwingUtilities2.drawString(this.progressBar, graphics2D, string, stringPlacement2.f12370x, stringPlacement2.f12371y);
            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(i2, i6, i4, i7);
            SwingUtilities2.drawString(this.progressBar, graphics2D, string, stringPlacement2.f12370x, stringPlacement2.f12371y);
        }
        graphics2D.setClip(clipBounds);
    }

    protected Point getStringPlacement(Graphics graphics, String str, int i2, int i3, int i4, int i5) {
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.progressBar, graphics, this.progressBar.getFont());
        int iStringWidth = SwingUtilities2.stringWidth(this.progressBar, fontMetrics, str);
        if (this.progressBar.getOrientation() == 0) {
            return new Point(i2 + Math.round((i4 / 2) - (iStringWidth / 2)), i3 + ((((i5 + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2));
        }
        return new Point(i2 + ((((i4 - fontMetrics.getAscent()) + fontMetrics.getLeading()) + fontMetrics.getDescent()) / 2), i3 + Math.round((i5 / 2) - (iStringWidth / 2)));
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension;
        Insets insets = this.progressBar.getInsets();
        FontMetrics fontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
        if (this.progressBar.getOrientation() == 0) {
            dimension = new Dimension(getPreferredInnerHorizontal());
            if (this.progressBar.isStringPainted()) {
                int iStringWidth = SwingUtilities2.stringWidth(this.progressBar, fontMetrics, this.progressBar.getString());
                if (iStringWidth > dimension.width) {
                    dimension.width = iStringWidth;
                }
                int height = fontMetrics.getHeight() + fontMetrics.getDescent();
                if (height > dimension.height) {
                    dimension.height = height;
                }
            }
        } else {
            dimension = new Dimension(getPreferredInnerVertical());
            if (this.progressBar.isStringPainted()) {
                String string = this.progressBar.getString();
                int height2 = fontMetrics.getHeight() + fontMetrics.getDescent();
                if (height2 > dimension.width) {
                    dimension.width = height2;
                }
                int iStringWidth2 = SwingUtilities2.stringWidth(this.progressBar, fontMetrics, string);
                if (iStringWidth2 > dimension.height) {
                    dimension.height = iStringWidth2;
                }
            }
        }
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(this.progressBar);
        if (this.progressBar.getOrientation() == 0) {
            preferredSize.width = 10;
        } else {
            preferredSize.height = 10;
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(this.progressBar);
        if (this.progressBar.getOrientation() == 0) {
            preferredSize.width = Short.MAX_VALUE;
        } else {
            preferredSize.height = Short.MAX_VALUE;
        }
        return preferredSize;
    }

    protected int getAnimationIndex() {
        return this.animationIndex;
    }

    protected final int getFrameCount() {
        return this.numFrames;
    }

    protected void setAnimationIndex(int i2) {
        if (this.animationIndex != i2) {
            if (sizeChanged()) {
                this.animationIndex = i2;
                this.maxPosition = 0;
                this.delta = 0.0d;
                this.progressBar.repaint();
                return;
            }
            this.nextPaintRect = getBox(this.nextPaintRect);
            this.animationIndex = i2;
            if (this.nextPaintRect != null) {
                this.boxRect = getBox(this.boxRect);
                if (this.boxRect != null) {
                    this.nextPaintRect.add(this.boxRect);
                }
            }
            if (this.nextPaintRect != null) {
                this.progressBar.repaint(this.nextPaintRect);
            } else {
                this.progressBar.repaint();
            }
        }
    }

    private boolean sizeChanged() {
        if (this.oldComponentInnards == null || this.componentInnards == null) {
            return true;
        }
        this.oldComponentInnards.setRect(this.componentInnards);
        this.componentInnards = SwingUtilities.calculateInnerArea(this.progressBar, this.componentInnards);
        return !this.oldComponentInnards.equals(this.componentInnards);
    }

    protected void incrementAnimationIndex() {
        int animationIndex = getAnimationIndex() + 1;
        if (animationIndex < this.numFrames) {
            setAnimationIndex(animationIndex);
        } else {
            setAnimationIndex(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRepaintInterval() {
        return this.repaintInterval;
    }

    private int initRepaintInterval() {
        this.repaintInterval = DefaultLookup.getInt(this.progressBar, this, "ProgressBar.repaintInterval", 50);
        return this.repaintInterval;
    }

    private int getCycleTime() {
        return this.cycleTime;
    }

    private int initCycleTime() {
        this.cycleTime = DefaultLookup.getInt(this.progressBar, this, "ProgressBar.cycleTime", 3000);
        return this.cycleTime;
    }

    private void initIndeterminateDefaults() {
        initRepaintInterval();
        initCycleTime();
        if (this.repaintInterval <= 0) {
            this.repaintInterval = 100;
        }
        if (this.repaintInterval > this.cycleTime) {
            this.cycleTime = this.repaintInterval * 20;
        } else {
            this.cycleTime = this.repaintInterval * ((int) Math.ceil(this.cycleTime / (this.repaintInterval * 2.0d))) * 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initIndeterminateValues() {
        initIndeterminateDefaults();
        this.numFrames = this.cycleTime / this.repaintInterval;
        initAnimationIndex();
        this.boxRect = new Rectangle();
        this.nextPaintRect = new Rectangle();
        this.componentInnards = new Rectangle();
        this.oldComponentInnards = new Rectangle();
        this.progressBar.addHierarchyListener(getHandler());
        if (this.progressBar.isDisplayable()) {
            startAnimationTimer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpIndeterminateValues() {
        if (this.progressBar.isDisplayable()) {
            stopAnimationTimer();
        }
        this.repaintInterval = 0;
        this.cycleTime = 0;
        this.animationIndex = 0;
        this.numFrames = 0;
        this.maxPosition = 0;
        this.delta = 0.0d;
        this.nextPaintRect = null;
        this.boxRect = null;
        this.oldComponentInnards = null;
        this.componentInnards = null;
        this.progressBar.removeHierarchyListener(getHandler());
    }

    private void initAnimationIndex() {
        if (this.progressBar.getOrientation() == 0 && BasicGraphicsUtils.isLeftToRight(this.progressBar)) {
            setAnimationIndex(0);
        } else {
            setAnimationIndex(this.numFrames / 2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicProgressBarUI$Animator.class */
    private class Animator implements ActionListener {
        private Timer timer;
        private long previousDelay;
        private int interval;
        private long lastCall;
        private int MINIMUM_DELAY;

        private Animator() {
            this.MINIMUM_DELAY = 5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void start(int i2) {
            this.previousDelay = i2;
            this.lastCall = 0L;
            if (this.timer == null) {
                this.timer = new Timer(i2, this);
            } else {
                this.timer.setDelay(i2);
            }
            if (BasicProgressBarUI.ADJUSTTIMER) {
                this.timer.setRepeats(false);
                this.timer.setCoalesce(false);
            }
            this.timer.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            this.timer.stop();
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicProgressBarUI.ADJUSTTIMER) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (this.lastCall > 0) {
                    int repaintInterval = (int) ((this.previousDelay - jCurrentTimeMillis) + this.lastCall + BasicProgressBarUI.this.getRepaintInterval());
                    if (repaintInterval < this.MINIMUM_DELAY) {
                        repaintInterval = this.MINIMUM_DELAY;
                    }
                    this.timer.setInitialDelay(repaintInterval);
                    this.previousDelay = repaintInterval;
                }
                this.timer.start();
                this.lastCall = jCurrentTimeMillis;
            }
            BasicProgressBarUI.this.incrementAnimationIndex();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicProgressBarUI$ChangeHandler.class */
    public class ChangeHandler implements ChangeListener {
        public ChangeHandler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            BasicProgressBarUI.this.getHandler().stateChanged(changeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicProgressBarUI$Handler.class */
    private class Handler implements ChangeListener, PropertyChangeListener, HierarchyListener {
        private Handler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            int value;
            BoundedRangeModel model = BasicProgressBarUI.this.progressBar.getModel();
            int maximum = model.getMaximum() - model.getMinimum();
            int cachedPercent = BasicProgressBarUI.this.getCachedPercent();
            if (maximum > 0) {
                value = (int) ((100 * model.getValue()) / maximum);
            } else {
                value = 0;
            }
            if (value != cachedPercent) {
                BasicProgressBarUI.this.setCachedPercent(value);
                BasicProgressBarUI.this.progressBar.repaint();
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("indeterminate" == propertyChangeEvent.getPropertyName()) {
                if (BasicProgressBarUI.this.progressBar.isIndeterminate()) {
                    BasicProgressBarUI.this.initIndeterminateValues();
                } else {
                    BasicProgressBarUI.this.cleanUpIndeterminateValues();
                }
                BasicProgressBarUI.this.progressBar.repaint();
            }
        }

        @Override // java.awt.event.HierarchyListener
        public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
            if ((hierarchyEvent.getChangeFlags() & 2) != 0 && BasicProgressBarUI.this.progressBar.isIndeterminate()) {
                if (BasicProgressBarUI.this.progressBar.isDisplayable()) {
                    BasicProgressBarUI.this.startAnimationTimer();
                } else {
                    BasicProgressBarUI.this.stopAnimationTimer();
                }
            }
        }
    }
}
