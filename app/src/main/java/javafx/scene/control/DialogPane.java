package javafx.scene.control;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javax.swing.text.AbstractDocument;

@DefaultProperty("buttonTypes")
/* loaded from: jfxrt.jar:javafx/scene/control/DialogPane.class */
public class DialogPane extends Pane {
    private final GridPane headerTextPanel;
    private final Label contentLabel;
    private final StackPane graphicContainer;
    private final Node buttonBar;
    private Node detailsButton;
    private Dialog<?> dialog;
    private final ObservableList<ButtonType> buttons = FXCollections.observableArrayList();
    private final Map<ButtonType, Node> buttonNodes = new WeakHashMap();
    private final ObjectProperty<Node> graphicProperty = new StyleableObjectProperty<Node>() { // from class: javafx.scene.control.DialogPane.1
        WeakReference<Node> graphicRef = new WeakReference<>(null);

        @Override // javafx.css.StyleableProperty
        public CssMetaData getCssMetaData() {
            return StyleableProperties.GRAPHIC;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return DialogPane.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "graphic";
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Node oldGraphic = this.graphicRef.get();
            if (oldGraphic != null) {
                DialogPane.this.getChildren().remove(oldGraphic);
            }
            Node newGraphic = DialogPane.this.getGraphic();
            this.graphicRef = new WeakReference<>(newGraphic);
            DialogPane.this.updateHeaderArea();
        }
    };
    private StyleableStringProperty imageUrl = null;
    private final ObjectProperty<Node> header = new SimpleObjectProperty<Node>(null) { // from class: javafx.scene.control.DialogPane.3
        WeakReference<Node> headerRef = new WeakReference<>(null);

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Node oldHeader = this.headerRef.get();
            if (oldHeader != null) {
                DialogPane.this.getChildren().remove(oldHeader);
            }
            Node newHeader = DialogPane.this.getHeader();
            this.headerRef = new WeakReference<>(newHeader);
            DialogPane.this.updateHeaderArea();
        }
    };
    private final StringProperty headerText = new SimpleStringProperty(this, "headerText") { // from class: javafx.scene.control.DialogPane.4
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            DialogPane.this.updateHeaderArea();
            DialogPane.this.requestLayout();
        }
    };
    private final ObjectProperty<Node> content = new SimpleObjectProperty<Node>(null) { // from class: javafx.scene.control.DialogPane.5
        WeakReference<Node> contentRef = new WeakReference<>(null);

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Node oldContent = this.contentRef.get();
            if (oldContent != null) {
                DialogPane.this.getChildren().remove(oldContent);
            }
            Node newContent = DialogPane.this.getContent();
            this.contentRef = new WeakReference<>(newContent);
            DialogPane.this.updateContentArea();
        }
    };
    private final StringProperty contentText = new SimpleStringProperty(this, "contentText") { // from class: javafx.scene.control.DialogPane.6
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            DialogPane.this.updateContentArea();
            DialogPane.this.requestLayout();
        }
    };
    private final ObjectProperty<Node> expandableContentProperty = new SimpleObjectProperty<Node>(null) { // from class: javafx.scene.control.DialogPane.7
        WeakReference<Node> expandableContentRef = new WeakReference<>(null);

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            Node oldExpandableContent = this.expandableContentRef.get();
            if (oldExpandableContent != null) {
                DialogPane.this.getChildren().remove(oldExpandableContent);
            }
            Node newExpandableContent = DialogPane.this.getExpandableContent();
            this.expandableContentRef = new WeakReference<>(newExpandableContent);
            if (newExpandableContent != null) {
                newExpandableContent.setVisible(DialogPane.this.isExpanded());
                newExpandableContent.setManaged(DialogPane.this.isExpanded());
                if (!newExpandableContent.getStyleClass().contains("expandable-content")) {
                    newExpandableContent.getStyleClass().add("expandable-content");
                }
                DialogPane.this.getChildren().add(newExpandableContent);
            }
        }
    };
    private final BooleanProperty expandedProperty = new SimpleBooleanProperty(this, "expanded", false) { // from class: javafx.scene.control.DialogPane.8
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            Node expandableContent = DialogPane.this.getExpandableContent();
            if (expandableContent != null) {
                expandableContent.setVisible(DialogPane.this.isExpanded());
            }
            DialogPane.this.requestLayout();
        }
    };
    private double oldHeight = -1.0d;

    static Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add(AbstractDocument.ContentElementName);
        label.setWrapText(true);
        label.setPrefWidth(360.0d);
        return label;
    }

    public DialogPane() {
        getStyleClass().add("dialog-pane");
        setAccessibleRole(AccessibleRole.DIALOG);
        this.headerTextPanel = new GridPane();
        getChildren().add(this.headerTextPanel);
        this.graphicContainer = new StackPane();
        this.contentLabel = createContentLabel("");
        getChildren().add(this.contentLabel);
        this.buttons.addListener(c2 -> {
            while (c2.next()) {
                if (c2.wasRemoved()) {
                    Iterator it = c2.getRemoved().iterator();
                    while (it.hasNext()) {
                        this.buttonNodes.remove((ButtonType) it.next());
                    }
                }
                if (c2.wasAdded()) {
                    for (ButtonType cmd : c2.getAddedSubList()) {
                        if (!this.buttonNodes.containsKey(cmd)) {
                            this.buttonNodes.put(cmd, createButton(cmd));
                        }
                    }
                }
            }
        });
        this.buttonBar = createButtonBar();
        if (this.buttonBar != null) {
            getChildren().add(this.buttonBar);
        }
    }

    public final ObjectProperty<Node> graphicProperty() {
        return this.graphicProperty;
    }

    public final Node getGraphic() {
        return this.graphicProperty.get();
    }

    public final void setGraphic(Node graphic) {
        this.graphicProperty.set(graphic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableStringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty() { // from class: javafx.scene.control.DialogPane.2
                StyleOrigin origin = StyleOrigin.USER;

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public void applyStyle(StyleOrigin origin, String v2) {
                    this.origin = origin;
                    if (DialogPane.this.graphicProperty == null || !DialogPane.this.graphicProperty.isBound()) {
                        super.applyStyle(origin, v2);
                    }
                    this.origin = StyleOrigin.USER;
                }

                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    String url = super.get();
                    if (url == null) {
                        ((StyleableProperty) DialogPane.this.graphicProperty()).applyStyle(this.origin, null);
                        return;
                    }
                    Node graphicNode = DialogPane.this.getGraphic();
                    if (graphicNode instanceof ImageView) {
                        ImageView imageView = (ImageView) graphicNode;
                        Image image = imageView.getImage();
                        if (image != null) {
                            String imageViewUrl = image.impl_getUrl();
                            if (url.equals(imageViewUrl)) {
                                return;
                            }
                        }
                    }
                    Image img = StyleManager.getInstance().getCachedImage(url);
                    if (img != null) {
                        ((StyleableProperty) DialogPane.this.graphicProperty()).applyStyle(this.origin, new ImageView(img));
                    }
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.ObservableObjectValue
                public String get() {
                    Image image;
                    Node graphic = DialogPane.this.getGraphic();
                    if ((graphic instanceof ImageView) && (image = ((ImageView) graphic).getImage()) != null) {
                        return image.impl_getUrl();
                    }
                    return null;
                }

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public StyleOrigin getStyleOrigin() {
                    if (DialogPane.this.graphicProperty != null) {
                        return ((StyleableProperty) DialogPane.this.graphicProperty).getStyleOrigin();
                    }
                    return null;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DialogPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "imageUrl";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return StyleableProperties.GRAPHIC;
                }
            };
        }
        return this.imageUrl;
    }

    public final Node getHeader() {
        return this.header.get();
    }

    public final void setHeader(Node header) {
        this.header.setValue(header);
    }

    public final ObjectProperty<Node> headerProperty() {
        return this.header;
    }

    public final void setHeaderText(String headerText) {
        this.headerText.set(headerText);
    }

    public final String getHeaderText() {
        return this.headerText.get();
    }

    public final StringProperty headerTextProperty() {
        return this.headerText;
    }

    public final Node getContent() {
        return this.content.get();
    }

    public final void setContent(Node content) {
        this.content.setValue(content);
    }

    public final ObjectProperty<Node> contentProperty() {
        return this.content;
    }

    public final void setContentText(String contentText) {
        this.contentText.set(contentText);
    }

    public final String getContentText() {
        return this.contentText.get();
    }

    public final StringProperty contentTextProperty() {
        return this.contentText;
    }

    public final ObjectProperty<Node> expandableContentProperty() {
        return this.expandableContentProperty;
    }

    public final Node getExpandableContent() {
        return this.expandableContentProperty.get();
    }

    public final void setExpandableContent(Node content) {
        this.expandableContentProperty.set(content);
    }

    public final BooleanProperty expandedProperty() {
        return this.expandedProperty;
    }

    public final boolean isExpanded() {
        return expandedProperty().get();
    }

    public final void setExpanded(boolean value) {
        expandedProperty().set(value);
    }

    public final ObservableList<ButtonType> getButtonTypes() {
        return this.buttons;
    }

    public final Node lookupButton(ButtonType buttonType) {
        return this.buttonNodes.get(buttonType);
    }

    protected Node createButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setMaxWidth(Double.MAX_VALUE);
        updateButtons(buttonBar);
        getButtonTypes().addListener(c2 -> {
            updateButtons(buttonBar);
        });
        expandableContentProperty().addListener(o2 -> {
            updateButtons(buttonBar);
        });
        return buttonBar;
    }

    protected Node createButton(ButtonType buttonType) {
        Button button = new Button(buttonType.getText());
        ButtonBar.ButtonData buttonData = buttonType.getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonType != null && buttonData.isDefaultButton());
        button.setCancelButton(buttonType != null && buttonData.isCancelButton());
        button.addEventHandler(ActionEvent.ACTION, ae2 -> {
            if (!ae2.isConsumed() && this.dialog != null) {
                this.dialog.impl_setResultAndClose(buttonType, true);
            }
        });
        return button;
    }

    protected Node createDetailsButton() {
        Hyperlink detailsButton = new Hyperlink();
        String moreText = ControlResources.getString("Dialog.detail.button.more");
        String lessText = ControlResources.getString("Dialog.detail.button.less");
        InvalidationListener expandedListener = o2 -> {
            boolean isExpanded = isExpanded();
            detailsButton.setText(isExpanded ? lessText : moreText);
            ObservableList<String> styleClass = detailsButton.getStyleClass();
            String[] strArr = new String[2];
            strArr[0] = "details-button";
            strArr[1] = isExpanded ? "less" : "more";
            styleClass.setAll(strArr);
        };
        expandedListener.invalidated(null);
        expandedProperty().addListener(expandedListener);
        detailsButton.setOnAction(ae2 -> {
            setExpanded(!isExpanded());
        });
        return detailsButton;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        double h2;
        double expandableContentPrefHeight;
        double contentAreaHeight;
        double contentAndGraphicHeight;
        boolean hasHeader = hasHeader();
        double w2 = Math.max(minWidth(-1.0d), getWidth());
        double minHeight = minHeight(w2);
        double prefHeight = prefHeight(w2);
        double maxHeight = maxHeight(w2);
        double currentHeight = getHeight();
        double dialogHeight = this.dialog == null ? 0.0d : this.dialog.dialog.getSceneHeight();
        if (prefHeight > currentHeight && prefHeight > minHeight && (prefHeight <= dialogHeight || dialogHeight == 0.0d)) {
            h2 = prefHeight;
            resize(w2, h2);
        } else {
            boolean isDialogGrowing = currentHeight > this.oldHeight;
            if (isDialogGrowing) {
                double _h = currentHeight < prefHeight ? Math.min(prefHeight, currentHeight) : Math.max(prefHeight, dialogHeight);
                h2 = Utils.boundedSize(_h, minHeight, maxHeight);
            } else {
                h2 = Utils.boundedSize(Math.min(currentHeight, dialogHeight), minHeight, maxHeight);
            }
            resize(w2, h2);
        }
        double h3 = h2 - (snappedTopInset() + snappedBottomInset());
        this.oldHeight = h3;
        double leftPadding = snappedLeftInset();
        double topPadding = snappedTopInset();
        double rightPadding = snappedRightInset();
        snappedBottomInset();
        Node header = getActualHeader();
        Node content = getActualContent();
        Node graphic = getActualGraphic();
        Node expandableContent = getExpandableContent();
        double graphicPrefWidth = (hasHeader || graphic == null) ? 0.0d : graphic.prefWidth(-1.0d);
        double headerPrefHeight = hasHeader ? header.prefHeight(w2) : 0.0d;
        double buttonBarPrefHeight = this.buttonBar == null ? 0.0d : this.buttonBar.prefHeight(w2);
        double graphicPrefHeight = (hasHeader || graphic == null) ? 0.0d : graphic.prefHeight(-1.0d);
        double availableContentWidth = ((w2 - graphicPrefWidth) - leftPadding) - rightPadding;
        if (isExpanded()) {
            contentAreaHeight = isExpanded() ? content.prefHeight(availableContentWidth) : 0.0d;
            contentAndGraphicHeight = hasHeader ? contentAreaHeight : Math.max(graphicPrefHeight, contentAreaHeight);
            expandableContentPrefHeight = h3 - ((headerPrefHeight + contentAndGraphicHeight) + buttonBarPrefHeight);
        } else {
            expandableContentPrefHeight = isExpanded() ? expandableContent.prefHeight(w2) : 0.0d;
            contentAreaHeight = h3 - ((headerPrefHeight + expandableContentPrefHeight) + buttonBarPrefHeight);
            contentAndGraphicHeight = hasHeader ? contentAreaHeight : Math.max(graphicPrefHeight, contentAreaHeight);
        }
        double x2 = leftPadding;
        double y2 = topPadding;
        if (!hasHeader) {
            if (graphic != null) {
                graphic.resizeRelocate(x2, y2, graphicPrefWidth, graphicPrefHeight);
                x2 += graphicPrefWidth;
            }
        } else {
            header.resizeRelocate(x2, y2, w2 - (leftPadding + rightPadding), headerPrefHeight);
            y2 += headerPrefHeight;
        }
        content.resizeRelocate(x2, y2, availableContentWidth, contentAreaHeight);
        double y3 = y2 + (hasHeader ? contentAreaHeight : contentAndGraphicHeight);
        if (expandableContent != null) {
            expandableContent.resizeRelocate(leftPadding, y3, w2 - rightPadding, expandableContentPrefHeight);
            y3 += expandableContentPrefHeight;
        }
        if (this.buttonBar != null) {
            this.buttonBar.resizeRelocate(leftPadding, y3, w2 - (leftPadding + rightPadding), buttonBarPrefHeight);
        }
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        double headerMinWidth = hasHeader() ? getActualHeader().minWidth(height) + 10.0d : 0.0d;
        double contentMinWidth = getActualContent().minWidth(height);
        double buttonBarMinWidth = this.buttonBar == null ? 0.0d : this.buttonBar.minWidth(height);
        double graphicMinWidth = getActualGraphic().minWidth(height);
        double expandableContentMinWidth = 0.0d;
        Node expandableContent = getExpandableContent();
        if (isExpanded() && expandableContent != null) {
            expandableContentMinWidth = expandableContent.minWidth(height);
        }
        double minWidth = snappedLeftInset() + (hasHeader() ? 0.0d : graphicMinWidth) + Math.max(Math.max(headerMinWidth, expandableContentMinWidth), Math.max(contentMinWidth, buttonBarMinWidth)) + snappedRightInset();
        return snapSize(minWidth);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        boolean hasHeader = hasHeader();
        double headerMinHeight = hasHeader ? getActualHeader().minHeight(width) : 0.0d;
        double buttonBarMinHeight = this.buttonBar == null ? 0.0d : this.buttonBar.minHeight(width);
        Node graphic = getActualGraphic();
        double graphicMinWidth = hasHeader ? 0.0d : graphic.minWidth(-1.0d);
        double graphicMinHeight = hasHeader ? 0.0d : graphic.minHeight(width);
        Node content = getActualContent();
        double contentAvailableWidth = width == -1.0d ? -1.0d : hasHeader ? width : width - graphicMinWidth;
        double contentMinHeight = content.minHeight(contentAvailableWidth);
        double expandableContentMinHeight = 0.0d;
        Node expandableContent = getExpandableContent();
        if (isExpanded() && expandableContent != null) {
            expandableContentMinHeight = expandableContent.minHeight(width);
        }
        double minHeight = snappedTopInset() + headerMinHeight + Math.max(graphicMinHeight, contentMinHeight) + expandableContentMinHeight + buttonBarMinHeight + snappedBottomInset();
        return snapSize(minHeight);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double headerPrefWidth = hasHeader() ? getActualHeader().prefWidth(height) + 10.0d : 0.0d;
        double contentPrefWidth = getActualContent().prefWidth(height);
        double buttonBarPrefWidth = this.buttonBar == null ? 0.0d : this.buttonBar.prefWidth(height);
        double graphicPrefWidth = getActualGraphic().prefWidth(height);
        double expandableContentPrefWidth = 0.0d;
        Node expandableContent = getExpandableContent();
        if (isExpanded() && expandableContent != null) {
            expandableContentPrefWidth = expandableContent.prefWidth(height);
        }
        double prefWidth = snappedLeftInset() + (hasHeader() ? 0.0d : graphicPrefWidth) + Math.max(Math.max(headerPrefWidth, expandableContentPrefWidth), Math.max(contentPrefWidth, buttonBarPrefWidth)) + snappedRightInset();
        return snapSize(prefWidth);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        boolean hasHeader = hasHeader();
        double headerPrefHeight = hasHeader ? getActualHeader().prefHeight(width) : 0.0d;
        double buttonBarPrefHeight = this.buttonBar == null ? 0.0d : this.buttonBar.prefHeight(width);
        Node graphic = getActualGraphic();
        double graphicPrefWidth = hasHeader ? 0.0d : graphic.prefWidth(-1.0d);
        double graphicPrefHeight = hasHeader ? 0.0d : graphic.prefHeight(width);
        Node content = getActualContent();
        double contentAvailableWidth = width == -1.0d ? -1.0d : hasHeader ? width : width - graphicPrefWidth;
        double contentPrefHeight = content.prefHeight(contentAvailableWidth);
        double expandableContentPrefHeight = 0.0d;
        Node expandableContent = getExpandableContent();
        if (isExpanded() && expandableContent != null) {
            expandableContentPrefHeight = expandableContent.prefHeight(width);
        }
        double prefHeight = snappedTopInset() + headerPrefHeight + Math.max(graphicPrefHeight, contentPrefHeight) + expandableContentPrefHeight + buttonBarPrefHeight + snappedBottomInset();
        return snapSize(prefHeight);
    }

    private void updateButtons(ButtonBar buttonBar) {
        buttonBar.getButtons().clear();
        if (hasExpandableContent()) {
            if (this.detailsButton == null) {
                this.detailsButton = createDetailsButton();
            }
            ButtonBar.setButtonData(this.detailsButton, ButtonBar.ButtonData.HELP_2);
            buttonBar.getButtons().add(this.detailsButton);
            ButtonBar.setButtonUniformSize(this.detailsButton, false);
        }
        boolean hasDefault = false;
        for (ButtonType cmd : getButtonTypes()) {
            Node button = this.buttonNodes.get(cmd);
            if (button instanceof Button) {
                ButtonBar.ButtonData buttonType = cmd.getButtonData();
                ((Button) button).setDefaultButton((hasDefault || buttonType == null || !buttonType.isDefaultButton()) ? false : true);
                ((Button) button).setCancelButton(buttonType != null && buttonType.isCancelButton());
                hasDefault |= buttonType != null && buttonType.isDefaultButton();
            }
            buttonBar.getButtons().add(button);
        }
    }

    private Node getActualContent() {
        Node content = getContent();
        return content == null ? this.contentLabel : content;
    }

    private Node getActualHeader() {
        Node header = getHeader();
        return header == null ? this.headerTextPanel : header;
    }

    private Node getActualGraphic() {
        return this.headerTextPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderArea() {
        Node header = getHeader();
        if (header != null) {
            if (!getChildren().contains(header)) {
                getChildren().add(header);
            }
            this.headerTextPanel.setVisible(false);
            this.headerTextPanel.setManaged(false);
            return;
        }
        String headerText = getHeaderText();
        this.headerTextPanel.getChildren().clear();
        this.headerTextPanel.getStyleClass().clear();
        this.headerTextPanel.setMaxWidth(Double.MAX_VALUE);
        if (headerText != null && !headerText.isEmpty()) {
            this.headerTextPanel.getStyleClass().add("header-panel");
        }
        Label headerLabel = new Label(headerText);
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_LEFT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        this.headerTextPanel.add(headerLabel, 0, 0);
        this.graphicContainer.getChildren().clear();
        if (!this.graphicContainer.getStyleClass().contains("graphic-container")) {
            this.graphicContainer.getStyleClass().add("graphic-container");
        }
        Node graphic = getGraphic();
        if (graphic != null) {
            this.graphicContainer.getChildren().add(graphic);
        }
        this.headerTextPanel.add(this.graphicContainer, 1, 0);
        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);
        this.headerTextPanel.getColumnConstraints().setAll(textColumn, graphicColumn);
        this.headerTextPanel.setVisible(true);
        this.headerTextPanel.setManaged(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContentArea() {
        Node content = getContent();
        if (content != null) {
            if (!getChildren().contains(content)) {
                getChildren().add(content);
            }
            if (!content.getStyleClass().contains(AbstractDocument.ContentElementName)) {
                content.getStyleClass().add(AbstractDocument.ContentElementName);
            }
            this.contentLabel.setVisible(false);
            this.contentLabel.setManaged(false);
            return;
        }
        String contentText = getContentText();
        boolean visible = (contentText == null || contentText.isEmpty()) ? false : true;
        this.contentLabel.setText(visible ? contentText : "");
        this.contentLabel.setVisible(visible);
        this.contentLabel.setManaged(visible);
    }

    boolean hasHeader() {
        return getHeader() != null || isTextHeader();
    }

    private boolean isTextHeader() {
        String headerText = getHeaderText();
        return (headerText == null || headerText.isEmpty()) ? false : true;
    }

    boolean hasExpandableContent() {
        return getExpandableContent() != null;
    }

    void setDialog(Dialog<?> dialog) {
        this.dialog = dialog;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/DialogPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<DialogPane, String> GRAPHIC = new CssMetaData<DialogPane, String>("-fx-graphic", StringConverter.getInstance()) { // from class: javafx.scene.control.DialogPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(DialogPane n2) {
                return n2.graphicProperty == null || !n2.graphicProperty.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(DialogPane n2) {
                return n2.imageUrlProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            Collections.addAll(styleables, GRAPHIC);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}
