package com.sun.javafx.charts;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;

/* loaded from: jfxrt.jar:com/sun/javafx/charts/Legend.class */
public class Legend extends TilePane {
    private static final int GAP = 5;
    private ListChangeListener<LegendItem> itemsListener;
    private BooleanProperty vertical;
    private ObjectProperty<ObservableList<LegendItem>> items;

    public final boolean isVertical() {
        return this.vertical.get();
    }

    public final void setVertical(boolean value) {
        this.vertical.set(value);
    }

    public final BooleanProperty verticalProperty() {
        return this.vertical;
    }

    public final void setItems(ObservableList<LegendItem> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<LegendItem> getItems() {
        return this.items.get();
    }

    public final ObjectProperty<ObservableList<LegendItem>> itemsProperty() {
        return this.items;
    }

    public Legend() {
        super(5.0d, 5.0d);
        this.itemsListener = c2 -> {
            getChildren().clear();
            for (LegendItem item : getItems()) {
                getChildren().add(item.label);
            }
            if (isVisible()) {
                requestLayout();
            }
        };
        this.vertical = new BooleanPropertyBase(false) { // from class: com.sun.javafx.charts.Legend.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                Legend.this.setOrientation(get() ? Orientation.VERTICAL : Orientation.HORIZONTAL);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Legend.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "vertical";
            }
        };
        this.items = new ObjectPropertyBase<ObservableList<LegendItem>>() { // from class: com.sun.javafx.charts.Legend.2
            ObservableList<LegendItem> oldItems = null;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (this.oldItems != null) {
                    this.oldItems.removeListener(Legend.this.itemsListener);
                }
                Legend.this.getChildren().clear();
                ObservableList<LegendItem> newItems = get();
                if (newItems != null) {
                    newItems.addListener(Legend.this.itemsListener);
                    for (LegendItem item : newItems) {
                        Legend.this.getChildren().add(item.label);
                    }
                }
                this.oldItems = get();
                Legend.this.requestLayout();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Legend.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "items";
            }
        };
        setTileAlignment(Pos.CENTER_LEFT);
        setItems(FXCollections.observableArrayList());
        getStyleClass().setAll("chart-legend");
    }

    @Override // javafx.scene.layout.TilePane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double forHeight) {
        if (getItems().size() > 0) {
            return super.computePrefWidth(forHeight);
        }
        return 0.0d;
    }

    @Override // javafx.scene.layout.TilePane, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double forWidth) {
        if (getItems().size() > 0) {
            return super.computePrefHeight(forWidth);
        }
        return 0.0d;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/charts/Legend$LegendItem.class */
    public static class LegendItem {
        private Label label;
        private StringProperty text;
        private ObjectProperty<Node> symbol;

        public final String getText() {
            return this.text.getValue2();
        }

        public final void setText(String value) {
            this.text.setValue(value);
        }

        public final StringProperty textProperty() {
            return this.text;
        }

        public final Node getSymbol() {
            return this.symbol.getValue2();
        }

        public final void setSymbol(Node value) {
            this.symbol.setValue(value);
        }

        public final ObjectProperty<Node> symbolProperty() {
            return this.symbol;
        }

        public LegendItem(String text) {
            this.label = new Label();
            this.text = new StringPropertyBase() { // from class: com.sun.javafx.charts.Legend.LegendItem.1
                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    LegendItem.this.label.setText(get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return LegendItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "text";
                }
            };
            this.symbol = new ObjectPropertyBase<Node>(new Region()) { // from class: com.sun.javafx.charts.Legend.LegendItem.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Node symbol = get();
                    if (symbol != null) {
                        symbol.getStyleClass().setAll("chart-legend-item-symbol");
                    }
                    LegendItem.this.label.setGraphic(symbol);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return LegendItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "symbol";
                }
            };
            setText(text);
            this.label.getStyleClass().add("chart-legend-item");
            this.label.setAlignment(Pos.CENTER_LEFT);
            this.label.setContentDisplay(ContentDisplay.LEFT);
            this.label.setGraphic(getSymbol());
            getSymbol().getStyleClass().setAll("chart-legend-item-symbol");
        }

        public LegendItem(String text, Node symbol) {
            this(text);
            setSymbol(symbol);
        }
    }
}
