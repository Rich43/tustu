package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TitledPaneBehavior;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TitledPaneSkin.class */
public class TitledPaneSkin extends LabeledSkinBase<TitledPane, TitledPaneBehavior> {
    public static final Duration TRANSITION_DURATION = new Duration(350.0d);
    private static final boolean CACHE_ANIMATION = PlatformUtil.isEmbedded();
    private final TitleRegion titleRegion;
    private final StackPane contentContainer;
    private Node content;
    private Timeline timeline;
    private double transitionStartValue;
    private Rectangle clipRect;
    private Pos pos;
    private HPos hpos;
    private VPos vpos;
    private DoubleProperty transition;
    private double prefHeightFromAccordion;

    /* JADX WARN: Multi-variable type inference failed */
    public TitledPaneSkin(TitledPane titledPane) {
        super(titledPane, new TitledPaneBehavior(titledPane));
        this.prefHeightFromAccordion = 0.0d;
        this.clipRect = new Rectangle();
        this.transitionStartValue = 0.0d;
        this.titleRegion = new TitleRegion();
        this.content = ((TitledPane) getSkinnable()).getContent();
        this.contentContainer = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TitledPaneSkin.1
            {
                getStyleClass().setAll(AbstractDocument.ContentElementName);
                if (TitledPaneSkin.this.content != null) {
                    getChildren().setAll(TitledPaneSkin.this.content);
                }
            }
        };
        this.contentContainer.setClip(this.clipRect);
        if (titledPane.isExpanded()) {
            setTransition(1.0d);
            setExpanded(titledPane.isExpanded());
        } else {
            setTransition(0.0d);
            if (this.content != null) {
                this.content.setVisible(false);
            }
        }
        getChildren().setAll(this.contentContainer, this.titleRegion);
        registerChangeListener(titledPane.contentProperty(), "CONTENT");
        registerChangeListener(titledPane.expandedProperty(), "EXPANDED");
        registerChangeListener(titledPane.collapsibleProperty(), "COLLAPSIBLE");
        registerChangeListener(titledPane.alignmentProperty(), "ALIGNMENT");
        registerChangeListener(titledPane.widthProperty(), "WIDTH");
        registerChangeListener(titledPane.heightProperty(), "HEIGHT");
        registerChangeListener(this.titleRegion.alignmentProperty(), "TITLE_REGION_ALIGNMENT");
        this.pos = titledPane.getAlignment();
        this.hpos = this.pos == null ? HPos.LEFT : this.pos.getHpos();
        this.vpos = this.pos == null ? VPos.CENTER : this.pos.getVpos();
    }

    public StackPane getContentContainer() {
        return this.contentContainer;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("CONTENT".equals(property)) {
            this.content = ((TitledPane) getSkinnable()).getContent();
            if (this.content == null) {
                this.contentContainer.getChildren().clear();
                return;
            } else {
                this.contentContainer.getChildren().setAll(this.content);
                return;
            }
        }
        if ("EXPANDED".equals(property)) {
            setExpanded(((TitledPane) getSkinnable()).isExpanded());
            return;
        }
        if (!"COLLAPSIBLE".equals(property)) {
            if ("ALIGNMENT".equals(property)) {
                this.pos = ((TitledPane) getSkinnable()).getAlignment();
                this.hpos = this.pos.getHpos();
                this.vpos = this.pos.getVpos();
                return;
            } else if ("TITLE_REGION_ALIGNMENT".equals(property)) {
                this.pos = this.titleRegion.getAlignment();
                this.hpos = this.pos.getHpos();
                this.vpos = this.pos.getVpos();
                return;
            } else if ("WIDTH".equals(property)) {
                this.clipRect.setWidth(((TitledPane) getSkinnable()).getWidth());
                return;
            } else if ("HEIGHT".equals(property)) {
                this.clipRect.setHeight(this.contentContainer.getHeight());
                return;
            } else {
                if ("GRAPHIC_TEXT_GAP".equals(property)) {
                    this.titleRegion.requestLayout();
                    return;
                }
                return;
            }
        }
        this.titleRegion.update();
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase
    protected void updateChildren() {
        if (this.titleRegion == null) {
            return;
        }
        this.titleRegion.update();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setExpanded(boolean expanded) {
        if (!((TitledPane) getSkinnable()).isCollapsible()) {
            setTransition(1.0d);
            return;
        }
        if (((TitledPane) getSkinnable()).isAnimated()) {
            this.transitionStartValue = getTransition();
            doAnimationTransition();
            return;
        }
        if (expanded) {
            setTransition(1.0d);
        } else {
            setTransition(0.0d);
        }
        if (this.content != null) {
            this.content.setVisible(expanded);
        }
        ((TitledPane) getSkinnable()).requestLayout();
    }

    private void setTransition(double value) {
        transitionProperty().set(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getTransition() {
        if (this.transition == null) {
            return 0.0d;
        }
        return this.transition.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DoubleProperty transitionProperty() {
        if (this.transition == null) {
            this.transition = new SimpleDoubleProperty(this, "transition", 0.0d) { // from class: com.sun.javafx.scene.control.skin.TitledPaneSkin.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    TitledPaneSkin.this.contentContainer.requestLayout();
                }
            };
        }
        return this.transition;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isInsideAccordion() {
        return ((TitledPane) getSkinnable()).getParent() != null && (((TitledPane) getSkinnable()).getParent() instanceof Accordion);
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double headerHeight = snapSize(this.titleRegion.prefHeight(-1.0d));
        this.titleRegion.resize(w2, headerHeight);
        positionInArea(this.titleRegion, x2, y2, w2, headerHeight, 0.0d, HPos.LEFT, VPos.CENTER);
        double contentHeight = (h2 - headerHeight) * getTransition();
        if (isInsideAccordion() && this.prefHeightFromAccordion != 0.0d) {
            contentHeight = (this.prefHeightFromAccordion - headerHeight) * getTransition();
        }
        double contentHeight2 = snapSize(contentHeight);
        double y3 = y2 + snapSize(headerHeight);
        this.contentContainer.resize(w2, contentHeight2);
        this.clipRect.setHeight(contentHeight2);
        positionInArea(this.contentContainer, x2, y3, w2, contentHeight2, 0.0d, HPos.CENTER, VPos.CENTER);
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double titleWidth = snapSize(this.titleRegion.prefWidth(height));
        double contentWidth = snapSize(this.contentContainer.minWidth(height));
        return Math.max(titleWidth, contentWidth) + leftInset + rightInset;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double headerHeight = snapSize(this.titleRegion.prefHeight(width));
        double contentHeight = this.contentContainer.minHeight(width) * getTransition();
        return headerHeight + snapSize(contentHeight) + topInset + bottomInset;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double titleWidth = snapSize(this.titleRegion.prefWidth(height));
        double contentWidth = snapSize(this.contentContainer.prefWidth(height));
        return Math.max(titleWidth, contentWidth) + leftInset + rightInset;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double headerHeight = snapSize(this.titleRegion.prefHeight(width));
        double contentHeight = this.contentContainer.prefHeight(width) * getTransition();
        return headerHeight + snapSize(contentHeight) + topInset + bottomInset;
    }

    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }

    double getTitleRegionSize(double width) {
        return snapSize(this.titleRegion.prefHeight(width)) + snappedTopInset() + snappedBottomInset();
    }

    void setMaxTitledPaneHeightForAccordion(double height) {
        this.prefHeightFromAccordion = height;
    }

    double getTitledPaneHeightForAccordion() {
        double headerHeight = snapSize(this.titleRegion.prefHeight(-1.0d));
        double contentHeight = (this.prefHeightFromAccordion - headerHeight) * getTransition();
        return headerHeight + snapSize(contentHeight) + snappedTopInset() + snappedBottomInset();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void doAnimationTransition() {
        Duration duration;
        KeyFrame k1;
        KeyFrame k2;
        if (this.content == null) {
            return;
        }
        if (this.timeline != null && this.timeline.getStatus() != Animation.Status.STOPPED) {
            duration = this.timeline.getCurrentTime();
            this.timeline.stop();
        } else {
            duration = TRANSITION_DURATION;
        }
        this.timeline = new Timeline();
        this.timeline.setCycleCount(1);
        if (((TitledPane) getSkinnable()).isExpanded()) {
            k1 = new KeyFrame(Duration.ZERO, (EventHandler<ActionEvent>) event -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(true);
                }
                this.content.setVisible(true);
            }, new KeyValue(transitionProperty(), Double.valueOf(this.transitionStartValue)));
            k2 = new KeyFrame(duration, (EventHandler<ActionEvent>) event2 -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(false);
                }
            }, new KeyValue(transitionProperty(), 1, Interpolator.LINEAR));
        } else {
            k1 = new KeyFrame(Duration.ZERO, (EventHandler<ActionEvent>) event3 -> {
                if (CACHE_ANIMATION) {
                    this.content.setCache(true);
                }
            }, new KeyValue(transitionProperty(), Double.valueOf(this.transitionStartValue)));
            k2 = new KeyFrame(duration, (EventHandler<ActionEvent>) event4 -> {
                this.content.setVisible(false);
                if (CACHE_ANIMATION) {
                    this.content.setCache(false);
                }
            }, new KeyValue(transitionProperty(), 0, Interpolator.LINEAR));
        }
        this.timeline.getKeyFrames().setAll(k1, k2);
        this.timeline.play();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TitledPaneSkin$TitleRegion.class */
    class TitleRegion extends StackPane {
        private final StackPane arrowRegion;

        public TitleRegion() {
            getStyleClass().setAll("title");
            this.arrowRegion = new StackPane();
            this.arrowRegion.setId("arrowRegion");
            this.arrowRegion.getStyleClass().setAll("arrow-button");
            StackPane arrow = new StackPane();
            arrow.setId("arrow");
            arrow.getStyleClass().setAll("arrow");
            this.arrowRegion.getChildren().setAll(arrow);
            arrow.rotateProperty().bind(new DoubleBinding() { // from class: com.sun.javafx.scene.control.skin.TitledPaneSkin.TitleRegion.1
                {
                    bind(TitledPaneSkin.this.transitionProperty());
                }

                @Override // javafx.beans.binding.DoubleBinding
                protected double computeValue() {
                    return (-90.0d) * (1.0d - TitledPaneSkin.this.getTransition());
                }
            });
            setAlignment(Pos.CENTER_LEFT);
            setOnMouseReleased(e2 -> {
                if (e2.getButton() != MouseButton.PRIMARY) {
                    return;
                }
                ContextMenu contextMenu = ((TitledPane) TitledPaneSkin.this.getSkinnable()).getContextMenu();
                if (contextMenu != null) {
                    contextMenu.hide();
                }
                if (((TitledPane) TitledPaneSkin.this.getSkinnable()).isCollapsible() && ((TitledPane) TitledPaneSkin.this.getSkinnable()).isFocused()) {
                    ((TitledPaneBehavior) TitledPaneSkin.this.getBehavior()).toggle();
                }
            });
            update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void update() {
            getChildren().clear();
            TitledPane titledPane = (TitledPane) TitledPaneSkin.this.getSkinnable();
            if (titledPane.isCollapsible()) {
                getChildren().add(this.arrowRegion);
            }
            if (TitledPaneSkin.this.graphic != null) {
                TitledPaneSkin.this.graphic.layoutBoundsProperty().removeListener(TitledPaneSkin.this.graphicPropertyChangedListener);
            }
            TitledPaneSkin.this.graphic = titledPane.getGraphic();
            if (TitledPaneSkin.this.isIgnoreGraphic()) {
                if (titledPane.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
                    getChildren().clear();
                    getChildren().add(this.arrowRegion);
                } else {
                    getChildren().add(TitledPaneSkin.this.text);
                }
            } else {
                TitledPaneSkin.this.graphic.layoutBoundsProperty().addListener(TitledPaneSkin.this.graphicPropertyChangedListener);
                if (TitledPaneSkin.this.isIgnoreText()) {
                    getChildren().add(TitledPaneSkin.this.graphic);
                } else {
                    getChildren().addAll(TitledPaneSkin.this.graphic, TitledPaneSkin.this.text);
                }
            }
            setCursor(((TitledPane) TitledPaneSkin.this.getSkinnable()).isCollapsible() ? Cursor.HAND : Cursor.DEFAULT);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double arrowWidth = 0.0d;
            double labelPrefWidth = labelPrefWidth(height);
            if (this.arrowRegion != null) {
                arrowWidth = snapSize(this.arrowRegion.prefWidth(height));
            }
            return left + arrowWidth + labelPrefWidth + right;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double arrowHeight = 0.0d;
            double labelPrefHeight = labelPrefHeight(width);
            if (this.arrowRegion != null) {
                arrowHeight = snapSize(this.arrowRegion.prefHeight(width));
            }
            return top + Math.max(arrowHeight, labelPrefHeight) + bottom;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double top = snappedTopInset();
            double bottom = snappedBottomInset();
            double left = snappedLeftInset();
            double right = snappedRightInset();
            double width = getWidth() - (left + right);
            double height = getHeight() - (top + bottom);
            double arrowWidth = snapSize(this.arrowRegion.prefWidth(-1.0d));
            double arrowHeight = snapSize(this.arrowRegion.prefHeight(-1.0d));
            double labelWidth = snapSize(Math.min(width - (arrowWidth / 2.0d), labelPrefWidth(-1.0d)));
            double labelHeight = snapSize(labelPrefHeight(-1.0d));
            double x2 = left + arrowWidth + Utils.computeXOffset(width - arrowWidth, labelWidth, TitledPaneSkin.this.hpos);
            if (HPos.CENTER == TitledPaneSkin.this.hpos) {
                x2 = left + Utils.computeXOffset(width, labelWidth, TitledPaneSkin.this.hpos);
            }
            double y2 = top + Utils.computeYOffset(height, Math.max(arrowHeight, labelHeight), TitledPaneSkin.this.vpos);
            this.arrowRegion.resize(arrowWidth, arrowHeight);
            positionInArea(this.arrowRegion, left, top, arrowWidth, height, 0.0d, HPos.CENTER, VPos.CENTER);
            TitledPaneSkin.this.layoutLabelInArea(x2, y2, labelWidth, height, TitledPaneSkin.this.pos);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private double labelPrefWidth(double height) {
            Labeled labeled = (Labeled) TitledPaneSkin.this.getSkinnable();
            Font font = TitledPaneSkin.this.text.getFont();
            String string = labeled.getText();
            boolean emptyText = string == null || string.isEmpty();
            Insets labelPadding = labeled.getLabelPadding();
            double widthPadding = labelPadding.getLeft() + labelPadding.getRight();
            double textWidth = emptyText ? 0.0d : Utils.computeTextWidth(font, string, 0.0d);
            Node graphic = labeled.getGraphic();
            if (TitledPaneSkin.this.isIgnoreGraphic()) {
                return textWidth + widthPadding;
            }
            if (TitledPaneSkin.this.isIgnoreText()) {
                return graphic.prefWidth(-1.0d) + widthPadding;
            }
            if (labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT) {
                return textWidth + labeled.getGraphicTextGap() + graphic.prefWidth(-1.0d) + widthPadding;
            }
            return Math.max(textWidth, graphic.prefWidth(-1.0d)) + widthPadding;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private double labelPrefHeight(double width) {
            Labeled labeled = (Labeled) TitledPaneSkin.this.getSkinnable();
            Font font = TitledPaneSkin.this.text.getFont();
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            double gap = labeled.getGraphicTextGap();
            Insets labelPadding = labeled.getLabelPadding();
            double widthPadding = snappedLeftInset() + snappedRightInset() + labelPadding.getLeft() + labelPadding.getRight();
            String str = labeled.getText();
            if (str != null && str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (!TitledPaneSkin.this.isIgnoreGraphic() && (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT)) {
                width -= TitledPaneSkin.this.graphic.prefWidth(-1.0d) + gap;
            }
            double textHeight = Utils.computeTextHeight(font, str, labeled.isWrapText() ? width - widthPadding : 0.0d, TitledPaneSkin.this.text.getBoundsType());
            double h2 = textHeight;
            if (!TitledPaneSkin.this.isIgnoreGraphic()) {
                Node graphic = labeled.getGraphic();
                if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
                    h2 = graphic.prefHeight(-1.0d) + gap + textHeight;
                } else {
                    h2 = Math.max(textHeight, graphic.prefHeight(-1.0d));
                }
            }
            return h2 + labelPadding.getTop() + labelPadding.getBottom();
        }
    }
}
