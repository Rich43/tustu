package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SplitPaneSkin.class */
public class SplitPaneSkin extends BehaviorSkinBase<SplitPane, BehaviorBase<SplitPane>> {
    private ObservableList<Content> contentRegions;
    private ObservableList<ContentDivider> contentDividers;
    private boolean horizontal;
    private double previousSize;
    private int lastDividerUpdate;
    private boolean resize;
    private boolean checkDividerPos;

    /* JADX WARN: Multi-variable type inference failed */
    public SplitPaneSkin(SplitPane splitPane) {
        super(splitPane, new BehaviorBase(splitPane, Collections.emptyList()));
        this.previousSize = -1.0d;
        this.lastDividerUpdate = 0;
        this.resize = false;
        this.checkDividerPos = true;
        this.horizontal = ((SplitPane) getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
        this.contentRegions = FXCollections.observableArrayList();
        this.contentDividers = FXCollections.observableArrayList();
        int index = 0;
        for (Node n2 : ((SplitPane) getSkinnable()).getItems()) {
            int i2 = index;
            index++;
            addContent(i2, n2);
        }
        initializeContentListener();
        for (SplitPane.Divider d2 : ((SplitPane) getSkinnable()).getDividers()) {
            addDivider(d2);
        }
        registerChangeListener(splitPane.orientationProperty(), "ORIENTATION");
        registerChangeListener(splitPane.widthProperty(), "WIDTH");
        registerChangeListener(splitPane.heightProperty(), "HEIGHT");
    }

    private void addContent(int index, Node n2) {
        Content c2 = new Content(n2);
        this.contentRegions.add(index, c2);
        getChildren().add(index, c2);
    }

    private void removeContent(Node n2) {
        for (Content c2 : this.contentRegions) {
            if (c2.getContent().equals(n2)) {
                getChildren().remove(c2);
                this.contentRegions.remove(c2);
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initializeContentListener() {
        ((SplitPane) getSkinnable()).getItems().addListener(c2 -> {
            while (c2.next()) {
                if (c2.wasPermutated() || c2.wasUpdated()) {
                    getChildren().clear();
                    this.contentRegions.clear();
                    int index = 0;
                    for (Node n2 : c2.getList()) {
                        int i2 = index;
                        index++;
                        addContent(i2, n2);
                    }
                } else {
                    for (Node n3 : c2.getRemoved()) {
                        removeContent(n3);
                    }
                    int index2 = c2.getFrom();
                    for (Node n4 : c2.getAddedSubList()) {
                        int i3 = index2;
                        index2++;
                        addContent(i3, n4);
                    }
                }
            }
            removeAllDividers();
            for (SplitPane.Divider d2 : ((SplitPane) getSkinnable()).getDividers()) {
                addDivider(d2);
            }
        });
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SplitPaneSkin$PosPropertyListener.class */
    class PosPropertyListener implements ChangeListener<Number> {
        ContentDivider divider;

        public PosPropertyListener(ContentDivider divider) {
            this.divider = divider;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (SplitPaneSkin.this.checkDividerPos) {
                this.divider.posExplicit = true;
            }
            ((SplitPane) SplitPaneSkin.this.getSkinnable()).requestLayout();
        }
    }

    private void checkDividerPosition(ContentDivider divider, double newPos, double oldPos) {
        double dividerWidth = divider.prefWidth(-1.0d);
        Content left = getLeft(divider);
        Content right = getRight(divider);
        double minLeft = left == null ? 0.0d : this.horizontal ? left.minWidth(-1.0d) : left.minHeight(-1.0d);
        double minRight = right == null ? 0.0d : this.horizontal ? right.minWidth(-1.0d) : right.minHeight(-1.0d);
        double dMaxWidth = (left == null || left.getContent() == null) ? 0.0d : this.horizontal ? left.getContent().maxWidth(-1.0d) : left.getContent().maxHeight(-1.0d);
        double maxLeft = dMaxWidth;
        double dMaxWidth2 = (right == null || right.getContent() == null) ? 0.0d : this.horizontal ? right.getContent().maxWidth(-1.0d) : right.getContent().maxHeight(-1.0d);
        double maxRight = dMaxWidth2;
        double previousDividerPos = 0.0d;
        double nextDividerPos = getSize();
        int index = this.contentDividers.indexOf(divider);
        if (index - 1 >= 0) {
            previousDividerPos = this.contentDividers.get(index - 1).getDividerPos();
            if (previousDividerPos == -1.0d) {
                previousDividerPos = getAbsoluteDividerPos(this.contentDividers.get(index - 1));
            }
        }
        if (index + 1 < this.contentDividers.size()) {
            nextDividerPos = this.contentDividers.get(index + 1).getDividerPos();
            if (nextDividerPos == -1.0d) {
                nextDividerPos = getAbsoluteDividerPos(this.contentDividers.get(index + 1));
            }
        }
        this.checkDividerPos = false;
        if (newPos > oldPos) {
            double max = previousDividerPos == 0.0d ? maxLeft : previousDividerPos + dividerWidth + maxLeft;
            double min = (nextDividerPos - minRight) - dividerWidth;
            double stopPos = Math.min(max, min);
            if (newPos >= stopPos) {
                setAbsoluteDividerPos(divider, stopPos);
            } else {
                double rightMax = (nextDividerPos - maxRight) - dividerWidth;
                if (newPos <= rightMax) {
                    setAbsoluteDividerPos(divider, rightMax);
                } else {
                    setAbsoluteDividerPos(divider, newPos);
                }
            }
        } else {
            double max2 = (nextDividerPos - maxRight) - dividerWidth;
            double min2 = previousDividerPos == 0.0d ? minLeft : previousDividerPos + minLeft + dividerWidth;
            double stopPos2 = Math.max(max2, min2);
            if (newPos <= stopPos2) {
                setAbsoluteDividerPos(divider, stopPos2);
            } else {
                double leftMax = previousDividerPos + maxLeft + dividerWidth;
                if (newPos >= leftMax) {
                    setAbsoluteDividerPos(divider, leftMax);
                } else {
                    setAbsoluteDividerPos(divider, newPos);
                }
            }
        }
        this.checkDividerPos = true;
    }

    private void addDivider(SplitPane.Divider d2) {
        ContentDivider c2 = new ContentDivider(d2);
        c2.setInitialPos(d2.getPosition());
        c2.setDividerPos(-1.0d);
        PosPropertyListener posPropertyListener = new PosPropertyListener(c2);
        c2.setPosPropertyListener(posPropertyListener);
        d2.positionProperty().addListener(posPropertyListener);
        initializeDivderEventHandlers(c2);
        this.contentDividers.add(c2);
        getChildren().add(c2);
    }

    private void removeAllDividers() {
        ListIterator<ContentDivider> dividers = this.contentDividers.listIterator();
        while (dividers.hasNext()) {
            ContentDivider c2 = dividers.next();
            getChildren().remove(c2);
            c2.getDivider().positionProperty().removeListener(c2.getPosPropertyListener());
            dividers.remove();
        }
        this.lastDividerUpdate = 0;
    }

    private void initializeDivderEventHandlers(ContentDivider divider) {
        divider.addEventHandler(MouseEvent.ANY, event -> {
            event.consume();
        });
        divider.setOnMousePressed(e2 -> {
            if (this.horizontal) {
                divider.setInitialPos(divider.getDividerPos());
                divider.setPressPos(e2.getSceneX());
                divider.setPressPos(((SplitPane) getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane) getSkinnable()).getWidth() - e2.getSceneX() : e2.getSceneX());
            } else {
                divider.setInitialPos(divider.getDividerPos());
                divider.setPressPos(e2.getSceneY());
            }
            e2.consume();
        });
        divider.setOnMouseDragged(e3 -> {
            double delta;
            if (this.horizontal) {
                delta = ((SplitPane) getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? ((SplitPane) getSkinnable()).getWidth() - e3.getSceneX() : e3.getSceneX();
            } else {
                delta = e3.getSceneY();
            }
            setAndCheckAbsoluteDividerPos(divider, Math.ceil(divider.getInitialPos() + (delta - divider.getPressPos())));
            e3.consume();
        });
    }

    private Content getLeft(ContentDivider d2) {
        int index = this.contentDividers.indexOf(d2);
        if (index != -1) {
            return this.contentRegions.get(index);
        }
        return null;
    }

    private Content getRight(ContentDivider d2) {
        int index = this.contentDividers.indexOf(d2);
        if (index != -1) {
            return this.contentRegions.get(index + 1);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("ORIENTATION".equals(property)) {
            this.horizontal = ((SplitPane) getSkinnable()).getOrientation() == Orientation.HORIZONTAL;
            this.previousSize = -1.0d;
            for (ContentDivider c2 : this.contentDividers) {
                c2.setGrabberStyle(this.horizontal);
            }
            ((SplitPane) getSkinnable()).requestLayout();
            return;
        }
        if ("WIDTH".equals(property) || "HEIGHT".equals(property)) {
            ((SplitPane) getSkinnable()).requestLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setAbsoluteDividerPos(ContentDivider divider, double value) {
        if (((SplitPane) getSkinnable()).getWidth() > 0.0d && ((SplitPane) getSkinnable()).getHeight() > 0.0d && divider != null) {
            SplitPane.Divider paneDivider = divider.getDivider();
            divider.setDividerPos(value);
            double size = getSize();
            if (size != 0.0d) {
                double pos = value + (divider.prefWidth(-1.0d) / 2.0d);
                paneDivider.setPosition(pos / size);
            } else {
                paneDivider.setPosition(0.0d);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private double getAbsoluteDividerPos(ContentDivider divider) {
        if (((SplitPane) getSkinnable()).getWidth() > 0.0d && ((SplitPane) getSkinnable()).getHeight() > 0.0d && divider != null) {
            SplitPane.Divider paneDivider = divider.getDivider();
            double newPos = posToDividerPos(divider, paneDivider.getPosition());
            divider.setDividerPos(newPos);
            return newPos;
        }
        return 0.0d;
    }

    private double posToDividerPos(ContentDivider divider, double pos) {
        double newPos;
        double newPos2 = getSize() * pos;
        if (pos == 1.0d) {
            newPos = newPos2 - divider.prefWidth(-1.0d);
        } else {
            newPos = newPos2 - (divider.prefWidth(-1.0d) / 2.0d);
        }
        return Math.round(newPos);
    }

    private double totalMinSize() {
        double dividerWidth = !this.contentDividers.isEmpty() ? this.contentDividers.size() * this.contentDividers.get(0).prefWidth(-1.0d) : 0.0d;
        double minSize = 0.0d;
        for (Content c2 : this.contentRegions) {
            if (this.horizontal) {
                minSize += c2.minWidth(-1.0d);
            } else {
                minSize += c2.minHeight(-1.0d);
            }
        }
        return minSize + dividerWidth;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private double getSize() {
        SplitPane s2 = (SplitPane) getSkinnable();
        double size = totalMinSize();
        if (this.horizontal) {
            if (s2.getWidth() > size) {
                size = (s2.getWidth() - snappedLeftInset()) - snappedRightInset();
            }
        } else if (s2.getHeight() > size) {
            size = (s2.getHeight() - snappedTopInset()) - snappedBottomInset();
        }
        return size;
    }

    private double distributeTo(List<Content> available, double size) {
        if (available.isEmpty()) {
            return size;
        }
        double size2 = snapSize(size);
        int portion = ((int) size2) / available.size();
        while (size2 > 0.0d && !available.isEmpty()) {
            Iterator<Content> i2 = available.iterator();
            while (i2.hasNext()) {
                Content c2 = i2.next();
                double max = Math.min(this.horizontal ? c2.maxWidth(-1.0d) : c2.maxHeight(-1.0d), Double.MAX_VALUE);
                double min = this.horizontal ? c2.minWidth(-1.0d) : c2.minHeight(-1.0d);
                if (c2.getArea() >= max) {
                    c2.setAvailable(c2.getArea() - min);
                    i2.remove();
                } else {
                    if (portion >= max - c2.getArea()) {
                        size2 -= max - c2.getArea();
                        c2.setArea(max);
                        c2.setAvailable(max - min);
                        i2.remove();
                    } else {
                        c2.setArea(c2.getArea() + portion);
                        c2.setAvailable(c2.getArea() - min);
                        size2 -= portion;
                    }
                    if (((int) size2) == 0) {
                        return size2;
                    }
                }
            }
            if (available.isEmpty()) {
                return size2;
            }
            portion = ((int) size2) / available.size();
            int remainder = ((int) size2) % available.size();
            if (portion == 0 && remainder != 0) {
                portion = remainder;
            }
        }
        return size2;
    }

    private double distributeFrom(double size, List<Content> available) {
        if (available.isEmpty()) {
            return size;
        }
        double size2 = snapSize(size);
        int portion = ((int) size2) / available.size();
        while (size2 > 0.0d && !available.isEmpty()) {
            Iterator<Content> i2 = available.iterator();
            while (i2.hasNext()) {
                Content c2 = i2.next();
                if (portion >= c2.getAvailable()) {
                    c2.setArea(c2.getArea() - c2.getAvailable());
                    size2 -= c2.getAvailable();
                    c2.setAvailable(0.0d);
                    i2.remove();
                } else {
                    c2.setArea(c2.getArea() - portion);
                    c2.setAvailable(c2.getAvailable() - portion);
                    size2 -= portion;
                }
                if (((int) size2) == 0) {
                    return size2;
                }
            }
            if (available.isEmpty()) {
                return size2;
            }
            portion = ((int) size2) / available.size();
            int remainder = ((int) size2) % available.size();
            if (portion == 0 && remainder != 0) {
                portion = remainder;
            }
        }
        return size2;
    }

    private void setupContentAndDividerForLayout() {
        double dividerWidth = this.contentDividers.isEmpty() ? 0.0d : this.contentDividers.get(0).prefWidth(-1.0d);
        double startX = 0.0d;
        double startY = 0.0d;
        for (Content c2 : this.contentRegions) {
            if (this.resize && !c2.isResizableWithParent()) {
                c2.setArea(c2.getResizableWithParentArea());
            }
            c2.setX(startX);
            c2.setY(startY);
            if (this.horizontal) {
                startX += c2.getArea() + dividerWidth;
            } else {
                startY += c2.getArea() + dividerWidth;
            }
        }
        double startX2 = 0.0d;
        double startY2 = 0.0d;
        this.checkDividerPos = false;
        int i2 = 0;
        while (i2 < this.contentDividers.size()) {
            ContentDivider d2 = this.contentDividers.get(i2);
            if (this.horizontal) {
                startX2 += getLeft(d2).getArea() + (i2 == 0 ? 0.0d : dividerWidth);
            } else {
                startY2 += getLeft(d2).getArea() + (i2 == 0 ? 0.0d : dividerWidth);
            }
            d2.setX(startX2);
            d2.setY(startY2);
            setAbsoluteDividerPos(d2, this.horizontal ? d2.getX() : d2.getY());
            d2.posExplicit = false;
            i2++;
        }
        this.checkDividerPos = true;
    }

    private void layoutDividersAndContent(double width, double height) {
        double paddingX = snappedLeftInset();
        double paddingY = snappedTopInset();
        double dividerWidth = this.contentDividers.isEmpty() ? 0.0d : this.contentDividers.get(0).prefWidth(-1.0d);
        for (Content c2 : this.contentRegions) {
            if (this.horizontal) {
                c2.setClipSize(c2.getArea(), height);
                layoutInArea(c2, c2.getX() + paddingX, c2.getY() + paddingY, c2.getArea(), height, 0.0d, HPos.CENTER, VPos.CENTER);
            } else {
                c2.setClipSize(width, c2.getArea());
                layoutInArea(c2, c2.getX() + paddingX, c2.getY() + paddingY, width, c2.getArea(), 0.0d, HPos.CENTER, VPos.CENTER);
            }
        }
        for (ContentDivider c3 : this.contentDividers) {
            if (this.horizontal) {
                c3.resize(dividerWidth, height);
                positionInArea(c3, c3.getX() + paddingX, c3.getY() + paddingY, dividerWidth, height, 0.0d, HPos.CENTER, VPos.CENTER);
            } else {
                c3.resize(width, dividerWidth);
                positionInArea(c3, c3.getX() + paddingX, c3.getY() + paddingY, width, dividerWidth, 0.0d, HPos.CENTER, VPos.CENTER);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02d5  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0287 A[EDGE_INSN: B:298:0x0287->B:93:0x0287 BREAK  A[LOOP:1: B:51:0x0152->B:303:0x0152], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0152 A[SYNTHETIC] */
    @Override // javafx.scene.control.SkinBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void layoutChildren(double r8, double r10, double r12, double r14) {
        /*
            Method dump skipped, instructions count: 2134
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.SplitPaneSkin.layoutChildren(double, double, double, double):void");
    }

    private void setAndCheckAbsoluteDividerPos(ContentDivider divider, double value) {
        double oldPos = divider.getDividerPos();
        setAbsoluteDividerPos(divider, value);
        checkDividerPosition(divider, value, oldPos);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minWidth = 0.0d;
        double maxMinWidth = 0.0d;
        for (Content c2 : this.contentRegions) {
            minWidth += c2.minWidth(-1.0d);
            maxMinWidth = Math.max(maxMinWidth, c2.minWidth(-1.0d));
        }
        for (ContentDivider d2 : this.contentDividers) {
            minWidth += d2.prefWidth(-1.0d);
        }
        if (this.horizontal) {
            return minWidth + leftInset + rightInset;
        }
        return maxMinWidth + leftInset + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double minHeight = 0.0d;
        double maxMinHeight = 0.0d;
        for (Content c2 : this.contentRegions) {
            minHeight += c2.minHeight(-1.0d);
            maxMinHeight = Math.max(maxMinHeight, c2.minHeight(-1.0d));
        }
        for (ContentDivider d2 : this.contentDividers) {
            minHeight += d2.prefWidth(-1.0d);
        }
        if (this.horizontal) {
            return maxMinHeight + topInset + bottomInset;
        }
        return minHeight + topInset + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefWidth = 0.0d;
        double prefMaxWidth = 0.0d;
        for (Content c2 : this.contentRegions) {
            prefWidth += c2.prefWidth(-1.0d);
            prefMaxWidth = Math.max(prefMaxWidth, c2.prefWidth(-1.0d));
        }
        for (ContentDivider d2 : this.contentDividers) {
            prefWidth += d2.prefWidth(-1.0d);
        }
        if (this.horizontal) {
            return prefWidth + leftInset + rightInset;
        }
        return prefMaxWidth + leftInset + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefHeight = 0.0d;
        double maxPrefHeight = 0.0d;
        for (Content c2 : this.contentRegions) {
            prefHeight += c2.prefHeight(-1.0d);
            maxPrefHeight = Math.max(maxPrefHeight, c2.prefHeight(-1.0d));
        }
        for (ContentDivider d2 : this.contentDividers) {
            prefHeight += d2.prefWidth(-1.0d);
        }
        if (this.horizontal) {
            return maxPrefHeight + topInset + bottomInset;
        }
        return prefHeight + topInset + bottomInset;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SplitPaneSkin$ContentDivider.class */
    class ContentDivider extends StackPane {
        private double initialPos;
        private double dividerPos;
        private double pressPos;

        /* renamed from: d, reason: collision with root package name */
        private SplitPane.Divider f11946d;
        private StackPane grabber;

        /* renamed from: x, reason: collision with root package name */
        private double f11947x;

        /* renamed from: y, reason: collision with root package name */
        private double f11948y;
        private boolean posExplicit;
        private ChangeListener<Number> listener;

        public ContentDivider(SplitPane.Divider d2) {
            getStyleClass().setAll("split-pane-divider");
            this.f11946d = d2;
            this.initialPos = 0.0d;
            this.dividerPos = 0.0d;
            this.pressPos = 0.0d;
            this.grabber = new StackPane() { // from class: com.sun.javafx.scene.control.skin.SplitPaneSkin.ContentDivider.1
                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computeMinWidth(double height) {
                    return 0.0d;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computeMinHeight(double width) {
                    return 0.0d;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefWidth(double height) {
                    return snappedLeftInset() + snappedRightInset();
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefHeight(double width) {
                    return snappedTopInset() + snappedBottomInset();
                }

                @Override // javafx.scene.layout.Region
                protected double computeMaxWidth(double height) {
                    return computePrefWidth(-1.0d);
                }

                @Override // javafx.scene.layout.Region
                protected double computeMaxHeight(double width) {
                    return computePrefHeight(-1.0d);
                }
            };
            setGrabberStyle(SplitPaneSkin.this.horizontal);
            getChildren().add(this.grabber);
        }

        public SplitPane.Divider getDivider() {
            return this.f11946d;
        }

        public final void setGrabberStyle(boolean horizontal) {
            this.grabber.getStyleClass().clear();
            this.grabber.getStyleClass().setAll("vertical-grabber");
            setCursor(Cursor.V_RESIZE);
            if (horizontal) {
                this.grabber.getStyleClass().setAll("horizontal-grabber");
                setCursor(Cursor.H_RESIZE);
            }
        }

        public double getInitialPos() {
            return this.initialPos;
        }

        public void setInitialPos(double initialPos) {
            this.initialPos = initialPos;
        }

        public double getDividerPos() {
            return this.dividerPos;
        }

        public void setDividerPos(double dividerPos) {
            this.dividerPos = dividerPos;
        }

        public double getPressPos() {
            return this.pressPos;
        }

        public void setPressPos(double pressPos) {
            this.pressPos = pressPos;
        }

        public double getX() {
            return this.f11947x;
        }

        public void setX(double x2) {
            this.f11947x = x2;
        }

        public double getY() {
            return this.f11948y;
        }

        public void setY(double y2) {
            this.f11948y = y2;
        }

        public ChangeListener<Number> getPosPropertyListener() {
            return this.listener;
        }

        public void setPosPropertyListener(ChangeListener<Number> listener) {
            this.listener = listener;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinWidth(double height) {
            return computePrefWidth(height);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinHeight(double width) {
            return computePrefHeight(width);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            return snappedLeftInset() + snappedRightInset();
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            return snappedTopInset() + snappedBottomInset();
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxWidth(double height) {
            return computePrefWidth(height);
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxHeight(double width) {
            return computePrefHeight(width);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double grabberWidth = this.grabber.prefWidth(-1.0d);
            double grabberHeight = this.grabber.prefHeight(-1.0d);
            double grabberX = (getWidth() - grabberWidth) / 2.0d;
            double grabberY = (getHeight() - grabberHeight) / 2.0d;
            this.grabber.resize(grabberWidth, grabberHeight);
            positionInArea(this.grabber, grabberX, grabberY, grabberWidth, grabberHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/SplitPaneSkin$Content.class */
    static class Content extends StackPane {
        private Node content;
        private Rectangle clipRect = new Rectangle();

        /* renamed from: x, reason: collision with root package name */
        private double f11944x;

        /* renamed from: y, reason: collision with root package name */
        private double f11945y;
        private double area;
        private double resizableWithParentArea;
        private double available;

        public Content(Node n2) {
            setClip(this.clipRect);
            this.content = n2;
            if (n2 != null) {
                getChildren().add(n2);
            }
            this.f11944x = 0.0d;
            this.f11945y = 0.0d;
        }

        public Node getContent() {
            return this.content;
        }

        public double getX() {
            return this.f11944x;
        }

        public void setX(double x2) {
            this.f11944x = x2;
        }

        public double getY() {
            return this.f11945y;
        }

        public void setY(double y2) {
            this.f11945y = y2;
        }

        public double getArea() {
            return this.area;
        }

        public void setArea(double area) {
            this.area = area;
        }

        public double getAvailable() {
            return this.available;
        }

        public void setAvailable(double available) {
            this.available = available;
        }

        public boolean isResizableWithParent() {
            return SplitPane.isResizableWithParent(this.content).booleanValue();
        }

        public double getResizableWithParentArea() {
            return this.resizableWithParentArea;
        }

        public void setResizableWithParentArea(double resizableWithParentArea) {
            if (!isResizableWithParent()) {
                this.resizableWithParentArea = resizableWithParentArea;
            } else {
                this.resizableWithParentArea = 0.0d;
            }
        }

        protected void setClipSize(double w2, double h2) {
            this.clipRect.setWidth(w2);
            this.clipRect.setHeight(h2);
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxWidth(double height) {
            return snapSize(this.content.maxWidth(height));
        }

        @Override // javafx.scene.layout.Region
        protected double computeMaxHeight(double width) {
            return snapSize(this.content.maxHeight(width));
        }
    }
}
