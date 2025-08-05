package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DatePickerBehavior;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.HijrahChronology;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DatePickerSkin.class */
public class DatePickerSkin extends ComboBoxPopupControl<LocalDate> {
    private DatePicker datePicker;
    private TextField displayNode;
    private DatePickerContent datePickerContent;

    public DatePickerSkin(DatePicker datePicker) {
        super(datePicker, new DatePickerBehavior(datePicker));
        this.datePicker = datePicker;
        this.arrow.paddingProperty().addListener(new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.DatePickerSkin.1
            private boolean rounding = false;

            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable observable) {
                if (!this.rounding) {
                    Insets padding = DatePickerSkin.this.arrow.getPadding();
                    Insets rounded = new Insets(Math.round(padding.getTop()), Math.round(padding.getRight()), Math.round(padding.getBottom()), Math.round(padding.getLeft()));
                    if (!rounded.equals(padding)) {
                        this.rounding = true;
                        DatePickerSkin.this.arrow.setPadding(rounded);
                        this.rounding = false;
                    }
                }
            }
        });
        registerChangeListener(datePicker.chronologyProperty(), "CHRONOLOGY");
        registerChangeListener(datePicker.converterProperty(), "CONVERTER");
        registerChangeListener(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
        registerChangeListener(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
        registerChangeListener(datePicker.valueProperty(), "VALUE");
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    public Node getPopupContent() {
        if (this.datePickerContent == null) {
            if (this.datePicker.getChronology() instanceof HijrahChronology) {
                this.datePickerContent = new DatePickerHijrahContent(this.datePicker);
            } else {
                this.datePickerContent = new DatePickerContent(this.datePicker);
            }
        }
        return this.datePickerContent;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 50.0d;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    protected void focusLost() {
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl, com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public void show() {
        super.show();
        this.datePickerContent.clearFocus();
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        if ("CHRONOLOGY".equals(p2) || "DAY_CELL_FACTORY".equals(p2)) {
            updateDisplayNode();
            this.datePickerContent = null;
            this.popup = null;
            return;
        }
        if ("CONVERTER".equals(p2)) {
            updateDisplayNode();
            return;
        }
        if ("EDITOR".equals(p2)) {
            getEditableInputNode();
            return;
        }
        if ("SHOWING".equals(p2)) {
            if (this.datePicker.isShowing()) {
                if (this.datePickerContent != null) {
                    LocalDate date = this.datePicker.getValue();
                    this.datePickerContent.displayedYearMonthProperty().set(date != null ? YearMonth.from(date) : YearMonth.now());
                    this.datePickerContent.updateValues();
                }
                show();
                return;
            }
            hide();
            return;
        }
        if ("SHOW_WEEK_NUMBERS".equals(p2)) {
            if (this.datePickerContent != null) {
                this.datePickerContent.updateGrid();
                this.datePickerContent.updateWeeknumberDateCells();
                return;
            }
            return;
        }
        if ("VALUE".equals(p2)) {
            updateDisplayNode();
            if (this.datePickerContent != null) {
                LocalDate date2 = this.datePicker.getValue();
                this.datePickerContent.displayedYearMonthProperty().set(date2 != null ? YearMonth.from(date2) : YearMonth.now());
                this.datePickerContent.updateValues();
            }
            this.datePicker.fireEvent(new ActionEvent());
            return;
        }
        super.handleControlPropertyChanged(p2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected TextField getEditor() {
        return ((DatePicker) getSkinnable()).getEditor();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected StringConverter<LocalDate> getConverter() {
        return ((DatePicker) getSkinnable()).getConverter();
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public Node getDisplayNode() {
        if (this.displayNode == null) {
            this.displayNode = getEditableInputNode();
            this.displayNode.getStyleClass().add("date-picker-display-node");
            updateDisplayNode();
        }
        this.displayNode.setEditable(this.datePicker.isEditable());
        return this.displayNode;
    }

    public void syncWithAutoUpdate() {
        if (!getPopup().isShowing() && this.datePicker.isShowing()) {
            this.datePicker.hide();
        }
    }
}
