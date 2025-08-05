package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TextBinding;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledSkinBase.class */
public abstract class LabeledSkinBase<C extends Labeled, B extends BehaviorBase<C>> extends BehaviorSkinBase<C, B> {
    LabeledText text;
    boolean invalidText;
    Node graphic;
    double textWidth;
    double ellipsisWidth;
    final InvalidationListener graphicPropertyChangedListener;
    private Rectangle textClip;
    private double wrapWidth;
    private double wrapHeight;
    public TextBinding bindings;
    Line mnemonic_underscore;
    private boolean containsMnemonic;
    private Scene mnemonicScene;
    private KeyCombination mnemonicCode;
    private Node labeledNode;

    public LabeledSkinBase(C labeled, B behavior) {
        super(labeled, behavior);
        this.invalidText = true;
        this.textWidth = Double.NEGATIVE_INFINITY;
        this.ellipsisWidth = Double.NEGATIVE_INFINITY;
        this.graphicPropertyChangedListener = valueModel -> {
            this.invalidText = true;
            if (getSkinnable() != null) {
                getSkinnable().requestLayout();
            }
        };
        this.containsMnemonic = false;
        this.mnemonicScene = null;
        this.labeledNode = null;
        this.text = new LabeledText(labeled);
        updateChildren();
        registerChangeListener(labeled.ellipsisStringProperty(), "ELLIPSIS_STRING");
        registerChangeListener(labeled.widthProperty(), "WIDTH");
        registerChangeListener(labeled.heightProperty(), "HEIGHT");
        registerChangeListener(labeled.textFillProperty(), "TEXT_FILL");
        registerChangeListener(labeled.fontProperty(), "FONT");
        registerChangeListener(labeled.graphicProperty(), "GRAPHIC");
        registerChangeListener(labeled.contentDisplayProperty(), "CONTENT_DISPLAY");
        registerChangeListener(labeled.labelPaddingProperty(), "LABEL_PADDING");
        registerChangeListener(labeled.graphicTextGapProperty(), "GRAPHIC_TEXT_GAP");
        registerChangeListener(labeled.alignmentProperty(), "ALIGNMENT");
        registerChangeListener(labeled.mnemonicParsingProperty(), "MNEMONIC_PARSING");
        registerChangeListener(labeled.textProperty(), "TEXT");
        registerChangeListener(labeled.textAlignmentProperty(), "TEXT_ALIGNMENT");
        registerChangeListener(labeled.textOverrunProperty(), "TEXT_OVERRUN");
        registerChangeListener(labeled.wrapTextProperty(), "WRAP_TEXT");
        registerChangeListener(labeled.underlineProperty(), "UNDERLINE");
        registerChangeListener(labeled.lineSpacingProperty(), "LINE_SPACING");
        registerChangeListener(labeled.sceneProperty(), "SCENE");
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("WIDTH".equals(p2)) {
            updateWrappingWidth();
            this.invalidText = true;
            return;
        }
        if ("HEIGHT".equals(p2)) {
            this.invalidText = true;
            return;
        }
        if ("FONT".equals(p2)) {
            textMetricsChanged();
            invalidateWidths();
            this.ellipsisWidth = Double.NEGATIVE_INFINITY;
            return;
        }
        if ("GRAPHIC".equals(p2)) {
            updateChildren();
            textMetricsChanged();
            return;
        }
        if ("CONTENT_DISPLAY".equals(p2)) {
            updateChildren();
            textMetricsChanged();
            return;
        }
        if ("LABEL_PADDING".equals(p2)) {
            textMetricsChanged();
            return;
        }
        if ("GRAPHIC_TEXT_GAP".equals(p2)) {
            textMetricsChanged();
            return;
        }
        if ("ALIGNMENT".equals(p2)) {
            getSkinnable().requestLayout();
            return;
        }
        if ("MNEMONIC_PARSING".equals(p2)) {
            this.containsMnemonic = false;
            textMetricsChanged();
            return;
        }
        if ("TEXT".equals(p2)) {
            updateChildren();
            textMetricsChanged();
            invalidateWidths();
            return;
        }
        if (!"TEXT_ALIGNMENT".equals(p2)) {
            if ("TEXT_OVERRUN".equals(p2)) {
                textMetricsChanged();
                return;
            }
            if ("ELLIPSIS_STRING".equals(p2)) {
                textMetricsChanged();
                invalidateWidths();
                this.ellipsisWidth = Double.NEGATIVE_INFINITY;
            } else if ("WRAP_TEXT".equals(p2)) {
                updateWrappingWidth();
                textMetricsChanged();
            } else if ("UNDERLINE".equals(p2)) {
                textMetricsChanged();
            } else if ("LINE_SPACING".equals(p2)) {
                textMetricsChanged();
            } else if ("SCENE".equals(p2)) {
                sceneChanged();
            }
        }
    }

    protected double topLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getTop());
    }

    protected double bottomLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getBottom());
    }

    protected double leftLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getLeft());
    }

    protected double rightLabelPadding() {
        return snapSize(getSkinnable().getLabelPadding().getRight());
    }

    private void textMetricsChanged() {
        this.invalidText = true;
        getSkinnable().requestLayout();
    }

    protected void mnemonicTargetChanged() {
        if (this.containsMnemonic) {
            removeMnemonic();
            Control control = getSkinnable();
            if (control instanceof Label) {
                this.labeledNode = ((Label) control).getLabelFor();
                addMnemonic();
            } else {
                this.labeledNode = null;
            }
        }
    }

    private void sceneChanged() {
        Labeled labeled = getSkinnable();
        Scene scene = labeled.getScene();
        if (scene != null && this.containsMnemonic) {
            addMnemonic();
        }
    }

    private void invalidateWidths() {
        this.textWidth = Double.NEGATIVE_INFINITY;
    }

    void updateDisplayedText() {
        updateDisplayedText(-1.0d, -1.0d);
    }

    private void updateDisplayedText(double w2, double h2) {
        String s2;
        String result;
        int i2;
        if (this.invalidText) {
            Labeled labeled = getSkinnable();
            String s3 = labeled.getText();
            int mnemonicIndex = -1;
            if (s3 != null && s3.length() > 0) {
                this.bindings = new TextBinding(s3);
                if (!PlatformUtil.isMac() && getSkinnable().isMnemonicParsing()) {
                    if (labeled instanceof Label) {
                        this.labeledNode = ((Label) labeled).getLabelFor();
                    } else {
                        this.labeledNode = labeled;
                    }
                    if (this.labeledNode == null) {
                        this.labeledNode = labeled;
                    }
                    mnemonicIndex = this.bindings.getMnemonicIndex();
                }
            }
            if (this.containsMnemonic) {
                if (this.mnemonicScene != null && (mnemonicIndex == -1 || (this.bindings != null && !this.bindings.getMnemonicKeyCombination().equals(this.mnemonicCode)))) {
                    removeMnemonic();
                    this.containsMnemonic = false;
                }
            } else {
                removeMnemonic();
            }
            if (s3 != null && s3.length() > 0 && mnemonicIndex >= 0 && !this.containsMnemonic) {
                this.containsMnemonic = true;
                this.mnemonicCode = this.bindings.getMnemonicKeyCombination();
                addMnemonic();
            }
            if (this.containsMnemonic) {
                s2 = this.bindings.getText();
                if (this.mnemonic_underscore == null) {
                    this.mnemonic_underscore = new Line();
                    this.mnemonic_underscore.setStartX(0.0d);
                    this.mnemonic_underscore.setStartY(0.0d);
                    this.mnemonic_underscore.setEndY(0.0d);
                    this.mnemonic_underscore.getStyleClass().clear();
                    this.mnemonic_underscore.getStyleClass().setAll("mnemonic-underline");
                }
                if (!getChildren().contains(this.mnemonic_underscore)) {
                    getChildren().add(this.mnemonic_underscore);
                }
            } else {
                if (getSkinnable().isMnemonicParsing() && PlatformUtil.isMac() && this.bindings != null) {
                    s2 = this.bindings.getText();
                } else {
                    s2 = labeled.getText();
                }
                if (this.mnemonic_underscore != null && getChildren().contains(this.mnemonic_underscore)) {
                    Platform.runLater(() -> {
                        getChildren().remove(this.mnemonic_underscore);
                        this.mnemonic_underscore = null;
                    });
                }
            }
            int len = s2 != null ? s2.length() : 0;
            boolean multiline = false;
            if (s2 != null && len > 0 && (i2 = s2.indexOf(10)) > -1 && i2 < len - 1) {
                multiline = true;
            }
            boolean horizontalPosition = labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT;
            double availableWidth = Math.max((((labeled.getWidth() - snappedLeftInset()) - leftLabelPadding()) - snappedRightInset()) - rightLabelPadding(), 0.0d);
            if (w2 == -1.0d) {
                w2 = availableWidth;
            }
            double minW = Math.min(computeMinLabeledPartWidth(-1.0d, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset()), availableWidth);
            if (horizontalPosition && !isIgnoreGraphic()) {
                double graphicW = labeled.getGraphic().getLayoutBounds().getWidth() + labeled.getGraphicTextGap();
                w2 -= graphicW;
                minW -= graphicW;
            }
            this.wrapWidth = Math.max(minW, w2);
            boolean verticalPosition = labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM;
            double availableHeight = Math.max((((labeled.getHeight() - snappedTopInset()) - topLabelPadding()) - snappedBottomInset()) - bottomLabelPadding(), 0.0d);
            if (h2 == -1.0d) {
                h2 = availableHeight;
            }
            double minH = Math.min(computeMinLabeledPartHeight(this.wrapWidth, snappedTopInset(), snappedRightInset(), snappedBottomInset(), snappedLeftInset()), availableHeight);
            if (verticalPosition && labeled.getGraphic() != null) {
                double graphicH = labeled.getGraphic().getLayoutBounds().getHeight() + labeled.getGraphicTextGap();
                h2 -= graphicH;
                minH -= graphicH;
            }
            this.wrapHeight = Math.max(minH, h2);
            updateWrappingWidth();
            Font font = this.text.getFont();
            OverrunStyle truncationStyle = labeled.getTextOverrun();
            String ellipsisString = labeled.getEllipsisString();
            if (labeled.isWrapText()) {
                result = Utils.computeClippedWrappedText(font, s2, this.wrapWidth, this.wrapHeight, truncationStyle, ellipsisString, this.text.getBoundsType());
            } else if (multiline) {
                StringBuilder sb = new StringBuilder();
                String[] splits = s2.split("\n");
                for (int i3 = 0; i3 < splits.length; i3++) {
                    sb.append(Utils.computeClippedText(font, splits[i3], this.wrapWidth, truncationStyle, ellipsisString));
                    if (i3 < splits.length - 1) {
                        sb.append('\n');
                    }
                }
                result = sb.toString();
            } else {
                result = Utils.computeClippedText(font, s2, this.wrapWidth, truncationStyle, ellipsisString);
            }
            if (result != null && result.endsWith("\n")) {
                result = result.substring(0, result.length() - 1);
            }
            this.text.setText(result);
            updateWrappingWidth();
            this.invalidText = false;
        }
    }

    private void addMnemonic() {
        if (this.labeledNode != null) {
            this.mnemonicScene = this.labeledNode.getScene();
            if (this.mnemonicScene != null) {
                this.mnemonicScene.addMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            }
        }
    }

    private void removeMnemonic() {
        if (this.mnemonicScene != null && this.labeledNode != null) {
            this.mnemonicScene.removeMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            this.mnemonicScene = null;
        }
    }

    private void updateWrappingWidth() {
        Labeled labeled = getSkinnable();
        this.text.setWrappingWidth(0.0d);
        if (labeled.isWrapText()) {
            double w2 = Math.min(this.text.prefWidth(-1.0d), this.wrapWidth);
            this.text.setWrappingWidth(w2);
        }
    }

    protected void updateChildren() {
        Labeled labeled = getSkinnable();
        if (this.graphic != null) {
            this.graphic.layoutBoundsProperty().removeListener(this.graphicPropertyChangedListener);
        }
        this.graphic = labeled.getGraphic();
        if (this.graphic instanceof ImageView) {
            this.graphic.setMouseTransparent(true);
        }
        if (isIgnoreGraphic()) {
            if (labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
                getChildren().clear();
                return;
            } else {
                getChildren().setAll(this.text);
                return;
            }
        }
        this.graphic.layoutBoundsProperty().addListener(this.graphicPropertyChangedListener);
        if (isIgnoreText()) {
            getChildren().setAll(this.graphic);
        } else {
            getChildren().setAll(this.graphic, this.text);
        }
    }

    protected boolean isIgnoreGraphic() {
        return this.graphic == null || !this.graphic.isManaged() || getSkinnable().getContentDisplay() == ContentDisplay.TEXT_ONLY;
    }

    protected boolean isIgnoreText() {
        Labeled labeled = getSkinnable();
        String txt = labeled.getText();
        return txt == null || txt.equals("") || labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computeMinLabeledPartWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    private double computeMinLabeledPartWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double width;
        Labeled labeled = getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double gap = labeled.getGraphicTextGap();
        double minTextWidth = 0.0d;
        Font font = this.text.getFont();
        OverrunStyle truncationStyle = labeled.getTextOverrun();
        String ellipsisString = labeled.getEllipsisString();
        String string = labeled.getText();
        boolean emptyText = string == null || string.isEmpty();
        if (!emptyText) {
            if (truncationStyle == OverrunStyle.CLIP) {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string.substring(0, 1), 0.0d);
                }
                minTextWidth = this.textWidth;
            } else {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string, 0.0d);
                }
                if (this.ellipsisWidth == Double.NEGATIVE_INFINITY) {
                    this.ellipsisWidth = Utils.computeTextWidth(font, ellipsisString, 0.0d);
                }
                minTextWidth = Math.min(this.textWidth, this.ellipsisWidth);
            }
        }
        Node graphic = labeled.getGraphic();
        if (isIgnoreGraphic()) {
            width = minTextWidth;
        } else if (isIgnoreText()) {
            width = graphic.minWidth(-1.0d);
        } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
            width = minTextWidth + graphic.minWidth(-1.0d) + gap;
        } else {
            width = Math.max(minTextWidth, graphic.minWidth(-1.0d));
        }
        return width + leftInset + leftLabelPadding() + rightInset + rightLabelPadding();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computeMinLabeledPartHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    private double computeMinLabeledPartHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        int newlineIndex;
        Labeled labeled = getSkinnable();
        Font font = this.text.getFont();
        String str = labeled.getText();
        if (str != null && str.length() > 0 && (newlineIndex = str.indexOf(10)) >= 0) {
            str = str.substring(0, newlineIndex);
        }
        double s2 = labeled.getLineSpacing();
        double textHeight = Utils.computeTextHeight(font, str, 0.0d, s2, this.text.getBoundsType());
        double h2 = textHeight;
        if (!isIgnoreGraphic()) {
            Node graphic = labeled.getGraphic();
            if (labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM) {
                h2 = graphic.minHeight(width) + labeled.getGraphicTextGap() + textHeight;
            } else {
                h2 = Math.max(textHeight, graphic.minHeight(width));
            }
        }
        return (((topInset + h2) + bottomInset) + topLabelPadding()) - bottomLabelPadding();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        Labeled labeled = getSkinnable();
        Font font = this.text.getFont();
        String string = labeled.getText();
        boolean emptyText = string == null || string.isEmpty();
        double widthPadding = leftInset + leftLabelPadding() + rightInset + rightLabelPadding();
        double textWidth = emptyText ? 0.0d : Utils.computeTextWidth(font, string, 0.0d);
        double graphicWidth = this.graphic == null ? 0.0d : Utils.boundedSize(this.graphic.prefWidth(-1.0d), this.graphic.minWidth(-1.0d), this.graphic.maxWidth(-1.0d));
        labeled.getGraphic();
        if (isIgnoreGraphic()) {
            return textWidth + widthPadding;
        }
        if (isIgnoreText()) {
            return graphicWidth + widthPadding;
        }
        if (labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT) {
            return textWidth + labeled.getGraphicTextGap() + graphicWidth + widthPadding;
        }
        return Math.max(textWidth, graphicWidth) + widthPadding;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        Labeled labeled = getSkinnable();
        Font font = this.text.getFont();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double gap = labeled.getGraphicTextGap();
        double width2 = width - (((leftInset + leftLabelPadding()) + rightInset) + rightLabelPadding());
        String str = labeled.getText();
        if (str != null && str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        double textWidth = width2;
        if (!isIgnoreGraphic() && (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT)) {
            textWidth -= this.graphic.prefWidth(-1.0d) + gap;
        }
        double textHeight = Utils.computeTextHeight(font, str, labeled.isWrapText() ? textWidth : 0.0d, labeled.getLineSpacing(), this.text.getBoundsType());
        double h2 = textHeight;
        if (!isIgnoreGraphic()) {
            Node graphic = labeled.getGraphic();
            if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
                h2 = graphic.prefHeight(width2) + gap + textHeight;
            } else {
                h2 = Math.max(textHeight, graphic.prefHeight(width2));
            }
        }
        return topInset + h2 + bottomInset + topLabelPadding() + bottomLabelPadding();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefWidth(height);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    @Override // javafx.scene.control.SkinBase
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        double textBaselineOffset = this.text.getBaselineOffset();
        double h2 = textBaselineOffset;
        Labeled labeled = getSkinnable();
        Node g2 = labeled.getGraphic();
        if (!isIgnoreGraphic()) {
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            if (contentDisplay == ContentDisplay.TOP) {
                h2 = g2.prefHeight(-1.0d) + labeled.getGraphicTextGap() + textBaselineOffset;
            } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
                h2 = textBaselineOffset + ((g2.prefHeight(-1.0d) - this.text.prefHeight(-1.0d)) / 2.0d);
            }
        }
        return topInset + topLabelPadding() + h2;
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        layoutLabelInArea(x2, y2, w2, h2);
    }

    protected void layoutLabelInArea(double x2, double y2, double w2, double h2) {
        layoutLabelInArea(x2, y2, w2, h2, null);
    }

    protected void layoutLabelInArea(double x2, double y2, double w2, double h2, Pos alignment) {
        double graphicWidth;
        double graphicHeight;
        double textWidth;
        double textHeight;
        double contentX;
        double contentY;
        Labeled labeled = getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        if (alignment == null) {
            alignment = labeled.getAlignment();
        }
        HPos hpos = alignment == null ? HPos.LEFT : alignment.getHpos();
        VPos vpos = alignment == null ? VPos.CENTER : alignment.getVpos();
        boolean ignoreGraphic = isIgnoreGraphic();
        boolean ignoreText = isIgnoreText();
        double x3 = x2 + leftLabelPadding();
        double y3 = y2 + topLabelPadding();
        double w3 = w2 - (leftLabelPadding() + rightLabelPadding());
        double d2 = topLabelPadding();
        double dBottomLabelPadding = bottomLabelPadding();
        double h3 = h2 - (d2 + dBottomLabelPadding);
        if (ignoreGraphic) {
            graphicHeight = 0.0d;
            graphicWidth = 0.0d;
        } else if (ignoreText) {
            if (this.graphic.isResizable()) {
                Orientation contentBias = this.graphic.getContentBias();
                if (contentBias == Orientation.HORIZONTAL) {
                    graphicWidth = Utils.boundedSize(w3, this.graphic.minWidth(-1.0d), this.graphic.maxWidth(-1.0d));
                    graphicHeight = Utils.boundedSize(h3, this.graphic.minHeight(graphicWidth), this.graphic.maxHeight(graphicWidth));
                } else if (contentBias == Orientation.VERTICAL) {
                    graphicHeight = Utils.boundedSize(h3, this.graphic.minHeight(-1.0d), this.graphic.maxHeight(-1.0d));
                    graphicWidth = Utils.boundedSize(w3, this.graphic.minWidth(graphicHeight), this.graphic.maxWidth(graphicHeight));
                } else {
                    graphicWidth = Utils.boundedSize(w3, this.graphic.minWidth(-1.0d), this.graphic.maxWidth(-1.0d));
                    graphicHeight = Utils.boundedSize(h3, this.graphic.minHeight(-1.0d), this.graphic.maxHeight(-1.0d));
                }
                dBottomLabelPadding = graphicHeight;
                this.graphic.resize(graphicWidth, dBottomLabelPadding);
            } else {
                graphicWidth = this.graphic.getLayoutBounds().getWidth();
                graphicHeight = this.graphic.getLayoutBounds().getHeight();
            }
        } else {
            this.graphic.autosize();
            graphicWidth = this.graphic.getLayoutBounds().getWidth();
            graphicHeight = this.graphic.getLayoutBounds().getHeight();
        }
        if (ignoreText) {
            textHeight = 0.0d;
            textWidth = dBottomLabelPadding;
            this.text.setText("");
        } else {
            updateDisplayedText(w3, h3);
            textWidth = snapSize(Math.min(this.text.getLayoutBounds().getWidth(), this.wrapWidth));
            textHeight = snapSize(Math.min(this.text.getLayoutBounds().getHeight(), this.wrapHeight));
        }
        double gap = (ignoreText || ignoreGraphic) ? 0.0d : labeled.getGraphicTextGap();
        double contentWidth = Math.max(graphicWidth, textWidth);
        double contentHeight = Math.max(graphicHeight, textHeight);
        if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
            contentHeight = graphicHeight + gap + textHeight;
        } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
            contentWidth = graphicWidth + gap + textWidth;
        }
        if (hpos == HPos.LEFT) {
            contentX = x3;
        } else if (hpos == HPos.RIGHT) {
            contentX = x3 + (w3 - contentWidth);
        } else {
            contentX = x3 + ((w3 - contentWidth) / 2.0d);
        }
        if (vpos == VPos.TOP) {
            contentY = y3;
        } else if (vpos == VPos.BOTTOM) {
            contentY = y3 + (h3 - contentHeight);
        } else {
            contentY = y3 + ((h3 - contentHeight) / 2.0d);
        }
        double preMnemonicWidth = 0.0d;
        double mnemonicWidth = 0.0d;
        double mnemonicHeight = 0.0d;
        if (this.containsMnemonic) {
            Font font = this.text.getFont();
            String preSt = this.bindings.getText();
            preMnemonicWidth = Utils.computeTextWidth(font, preSt.substring(0, this.bindings.getMnemonicIndex()), 0.0d);
            mnemonicWidth = Utils.computeTextWidth(font, preSt.substring(this.bindings.getMnemonicIndex(), this.bindings.getMnemonicIndex() + 1), 0.0d);
            mnemonicHeight = Utils.computeTextHeight(font, "_", 0.0d, this.text.getBoundsType());
        }
        if ((!ignoreGraphic || !ignoreText) && !this.text.isManaged()) {
            this.text.setManaged(true);
        }
        if (ignoreGraphic && ignoreText) {
            if (this.text.isManaged()) {
                this.text.setManaged(false);
            }
            this.text.relocate(snapPosition(contentX), snapPosition(contentY));
        } else if (ignoreGraphic) {
            this.text.relocate(snapPosition(contentX), snapPosition(contentY));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(mnemonicWidth - 2.0d);
                this.mnemonic_underscore.relocate(contentX + preMnemonicWidth, (contentY + mnemonicHeight) - 1.0d);
            }
        } else if (ignoreText) {
            this.text.relocate(snapPosition(contentX), snapPosition(contentY));
            this.graphic.relocate(snapPosition(contentX), snapPosition(contentY));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(mnemonicWidth);
                this.mnemonic_underscore.setStrokeWidth(mnemonicHeight / 10.0d);
                this.mnemonic_underscore.relocate(contentX + preMnemonicWidth, (contentY + mnemonicHeight) - 1.0d);
            }
        } else {
            double graphicX = 0.0d;
            double graphicY = 0.0d;
            double textX = 0.0d;
            double textY = 0.0d;
            if (contentDisplay == ContentDisplay.TOP) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0d);
                textX = contentX + ((contentWidth - textWidth) / 2.0d);
                graphicY = contentY;
                textY = graphicY + graphicHeight + gap;
            } else if (contentDisplay == ContentDisplay.RIGHT) {
                textX = contentX;
                graphicX = textX + textWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0d);
                textY = contentY + ((contentHeight - textHeight) / 2.0d);
            } else if (contentDisplay == ContentDisplay.BOTTOM) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0d);
                textX = contentX + ((contentWidth - textWidth) / 2.0d);
                textY = contentY;
                graphicY = textY + textHeight + gap;
            } else if (contentDisplay == ContentDisplay.LEFT) {
                graphicX = contentX;
                textX = graphicX + graphicWidth + gap;
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0d);
                textY = contentY + ((contentHeight - textHeight) / 2.0d);
            } else if (contentDisplay == ContentDisplay.CENTER) {
                graphicX = contentX + ((contentWidth - graphicWidth) / 2.0d);
                textX = contentX + ((contentWidth - textWidth) / 2.0d);
                graphicY = contentY + ((contentHeight - graphicHeight) / 2.0d);
                textY = contentY + ((contentHeight - textHeight) / 2.0d);
            }
            this.text.relocate(snapPosition(textX), snapPosition(textY));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(mnemonicWidth);
                this.mnemonic_underscore.setStrokeWidth(mnemonicHeight / 10.0d);
                this.mnemonic_underscore.relocate(snapPosition(textX + preMnemonicWidth), snapPosition((textY + mnemonicHeight) - 1.0d));
            }
            this.graphic.relocate(snapPosition(graphicX), snapPosition(graphicY));
        }
        if (this.text != null && (this.text.getLayoutBounds().getHeight() > this.wrapHeight || this.text.getLayoutBounds().getWidth() > this.wrapWidth)) {
            if (this.textClip == null) {
                this.textClip = new Rectangle();
            }
            if (labeled.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) {
                this.textClip.setX(this.text.getLayoutBounds().getMinX());
            } else {
                this.textClip.setX(this.text.getLayoutBounds().getMaxX() - this.wrapWidth);
            }
            this.textClip.setY(this.text.getLayoutBounds().getMinY());
            this.textClip.setWidth(this.wrapWidth);
            this.textClip.setHeight(this.wrapHeight);
            if (this.text.getClip() == null) {
                this.text.setClip(this.textClip);
                return;
            }
            return;
        }
        if (this.text.getClip() != null) {
            this.text.setClip(null);
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        Object result;
        String text;
        String text2;
        switch (attribute) {
            case TEXT:
                Labeled labeled = getSkinnable();
                String accText = labeled.getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                if (this.bindings != null && (text2 = this.bindings.getText()) != null && !text2.isEmpty()) {
                    return text2;
                }
                if (labeled != null && (text = labeled.getText()) != null && !text.isEmpty()) {
                    return text;
                }
                if (this.graphic == null || (result = this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) == null) {
                    return null;
                }
                return result;
            case MNEMONIC:
                if (this.bindings != null) {
                    return this.bindings.getMnemonic();
                }
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
