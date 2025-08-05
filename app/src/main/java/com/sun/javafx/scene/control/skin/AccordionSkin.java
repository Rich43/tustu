package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.AccordionBehavior;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Rectangle;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/AccordionSkin.class */
public class AccordionSkin extends BehaviorSkinBase<Accordion, AccordionBehavior> {
    private TitledPane firstTitledPane;
    private Rectangle clipRect;
    private boolean forceRelayout;
    private boolean relayout;
    private double previousHeight;
    private TitledPane expandedPane;
    private TitledPane previousPane;
    private Map<TitledPane, ChangeListener<Boolean>> listeners;

    /* JADX WARN: Multi-variable type inference failed */
    public AccordionSkin(Accordion accordion) {
        super(accordion, new AccordionBehavior(accordion));
        this.forceRelayout = false;
        this.relayout = false;
        this.previousHeight = 0.0d;
        this.expandedPane = null;
        this.previousPane = null;
        this.listeners = new HashMap();
        accordion.getPanes().addListener(c2 -> {
            if (this.firstTitledPane != null) {
                this.firstTitledPane.getStyleClass().remove("first-titled-pane");
            }
            if (!accordion.getPanes().isEmpty()) {
                this.firstTitledPane = accordion.getPanes().get(0);
                this.firstTitledPane.getStyleClass().add("first-titled-pane");
            }
            getChildren().setAll(accordion.getPanes());
            while (c2.next()) {
                removeTitledPaneListeners(c2.getRemoved());
                initTitledPaneListeners(c2.getAddedSubList());
            }
            this.forceRelayout = true;
        });
        if (!accordion.getPanes().isEmpty()) {
            this.firstTitledPane = accordion.getPanes().get(0);
            this.firstTitledPane.getStyleClass().add("first-titled-pane");
        }
        this.clipRect = new Rectangle(accordion.getWidth(), accordion.getHeight());
        ((Accordion) getSkinnable()).setClip(this.clipRect);
        initTitledPaneListeners(accordion.getPanes());
        getChildren().setAll(accordion.getPanes());
        ((Accordion) getSkinnable()).requestLayout();
        registerChangeListener(((Accordion) getSkinnable()).widthProperty(), "WIDTH");
        registerChangeListener(((Accordion) getSkinnable()).heightProperty(), "HEIGHT");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("WIDTH".equals(property)) {
            this.clipRect.setWidth(((Accordion) getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(property)) {
            this.clipRect.setHeight(((Accordion) getSkinnable()).getHeight());
            this.relayout = true;
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double h2 = 0.0d;
        if (this.expandedPane != null) {
            h2 = 0.0d + this.expandedPane.minHeight(width);
        }
        if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
            h2 += this.previousPane.minHeight(width);
        }
        for (Node child : getChildren()) {
            TitledPane pane = (TitledPane) child;
            if (!pane.equals(this.expandedPane) && !pane.equals(this.previousPane)) {
                Skin<?> skin = ((TitledPane) child).getSkin();
                if (skin instanceof TitledPaneSkin) {
                    TitledPaneSkin childSkin = (TitledPaneSkin) skin;
                    h2 += childSkin.getTitleRegionSize(width);
                } else {
                    h2 += pane.minHeight(width);
                }
            }
        }
        return h2 + topInset + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double h2 = 0.0d;
        if (this.expandedPane != null) {
            h2 = 0.0d + this.expandedPane.prefHeight(width);
        }
        if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
            h2 += this.previousPane.prefHeight(width);
        }
        for (Node child : getChildren()) {
            TitledPane pane = (TitledPane) child;
            if (!pane.equals(this.expandedPane) && !pane.equals(this.previousPane)) {
                Skin<?> skin = ((TitledPane) child).getSkin();
                if (skin instanceof TitledPaneSkin) {
                    TitledPaneSkin childSkin = (TitledPaneSkin) skin;
                    h2 += childSkin.getTitleRegionSize(width);
                } else {
                    h2 += pane.prefHeight(width);
                }
            }
        }
        return h2 + topInset + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double ph;
        boolean rebuild = this.forceRelayout || (this.relayout && this.previousHeight != h2);
        this.forceRelayout = false;
        this.previousHeight = h2;
        double collapsedPanesHeight = 0.0d;
        for (TitledPane tp : ((Accordion) getSkinnable()).getPanes()) {
            if (!tp.equals(this.expandedPane)) {
                TitledPaneSkin childSkin = (TitledPaneSkin) tp.getSkin();
                collapsedPanesHeight += snapSize(childSkin.getTitleRegionSize(w2));
            }
        }
        double maxTitledPaneHeight = h2 - collapsedPanesHeight;
        Iterator<TitledPane> it = ((Accordion) getSkinnable()).getPanes().iterator();
        while (it.hasNext()) {
            TitledPane tp2 = it.next();
            Skin<?> skin = tp2.getSkin();
            if (skin instanceof TitledPaneSkin) {
                ((TitledPaneSkin) skin).setMaxTitledPaneHeightForAccordion(maxTitledPaneHeight);
                ph = snapSize(((TitledPaneSkin) skin).getTitledPaneHeightForAccordion());
            } else {
                ph = tp2.prefHeight(w2);
            }
            tp2.resize(w2, ph);
            boolean needsRelocate = true;
            if (!rebuild && this.previousPane != null && this.expandedPane != null) {
                List<TitledPane> panes = ((Accordion) getSkinnable()).getPanes();
                int previousPaneIndex = panes.indexOf(this.previousPane);
                int expandedPaneIndex = panes.indexOf(this.expandedPane);
                int currentPaneIndex = panes.indexOf(tp2);
                if (previousPaneIndex < expandedPaneIndex) {
                    if (currentPaneIndex <= expandedPaneIndex) {
                        tp2.relocate(x2, y2);
                        y2 += ph;
                        needsRelocate = false;
                    }
                } else if (previousPaneIndex <= expandedPaneIndex || currentPaneIndex <= previousPaneIndex) {
                    tp2.relocate(x2, y2);
                    y2 += ph;
                    needsRelocate = false;
                }
            }
            if (needsRelocate) {
                tp2.relocate(x2, y2);
                y2 += ph;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initTitledPaneListeners(List<? extends TitledPane> list) {
        Iterator<? extends TitledPane> it = list.iterator();
        while (it.hasNext()) {
            TitledPane tp = it.next();
            tp.setExpanded(tp == ((Accordion) getSkinnable()).getExpandedPane());
            if (tp.isExpanded()) {
                this.expandedPane = tp;
            }
            ChangeListener<Boolean> changeListener = expandedPropertyListener(tp);
            tp.expandedProperty().addListener(changeListener);
            this.listeners.put(tp, changeListener);
        }
    }

    private void removeTitledPaneListeners(List<? extends TitledPane> list) {
        for (TitledPane tp : list) {
            if (this.listeners.containsKey(tp)) {
                tp.expandedProperty().removeListener(this.listeners.get(tp));
                this.listeners.remove(tp);
            }
        }
    }

    private ChangeListener<Boolean> expandedPropertyListener(TitledPane tp) {
        return (observable, wasExpanded, expanded) -> {
            this.previousPane = this.expandedPane;
            Accordion accordion = (Accordion) getSkinnable();
            if (expanded.booleanValue()) {
                if (this.expandedPane != null) {
                    this.expandedPane.setExpanded(false);
                }
                if (tp != null) {
                    accordion.setExpandedPane(tp);
                }
                this.expandedPane = accordion.getExpandedPane();
                return;
            }
            this.expandedPane = null;
            accordion.setExpandedPane(null);
        };
    }
}
