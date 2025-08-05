package com.sun.javafx.scene.control.skin;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DatePickerHijrahContent.class */
class DatePickerHijrahContent extends DatePickerContent {
    private Label hijrahMonthYearLabel;

    DatePickerHijrahContent(DatePicker datePicker) {
        super(datePicker);
    }

    @Override // com.sun.javafx.scene.control.skin.DatePickerContent
    protected Chronology getPrimaryChronology() {
        return IsoChronology.INSTANCE;
    }

    @Override // com.sun.javafx.scene.control.skin.DatePickerContent
    protected BorderPane createMonthYearPane() {
        BorderPane monthYearPane = super.createMonthYearPane();
        this.hijrahMonthYearLabel = new Label();
        this.hijrahMonthYearLabel.getStyleClass().add("secondary-label");
        monthYearPane.setBottom(this.hijrahMonthYearLabel);
        BorderPane.setAlignment(this.hijrahMonthYearLabel, Pos.CENTER);
        return monthYearPane;
    }

    @Override // com.sun.javafx.scene.control.skin.DatePickerContent
    protected void updateMonthYearPane() {
        super.updateMonthYearPane();
        Locale locale = getLocale();
        HijrahChronology chrono = HijrahChronology.INSTANCE;
        long firstMonth = -1;
        long firstYear = -1;
        String firstMonthStr = null;
        String firstYearStr = null;
        String hijrahStr = null;
        YearMonth displayedYearMonth = displayedYearMonthProperty().get();
        Iterator<DateCell> it = this.dayCells.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            DateCell dayCell = it.next();
            LocalDate date = dayCellDate(dayCell);
            if (displayedYearMonth.equals(YearMonth.from(date))) {
                try {
                    HijrahDate cDate = chrono.date((TemporalAccessor) date);
                    long month = cDate.getLong(ChronoField.MONTH_OF_YEAR);
                    long year = cDate.getLong(ChronoField.YEAR);
                    if (hijrahStr == null || month != firstMonth) {
                        String monthStr = this.monthFormatter.withLocale(locale).withChronology(chrono).withDecimalStyle(DecimalStyle.of(locale)).format(cDate);
                        String yearStr = this.yearFormatter.withLocale(locale).withChronology(chrono).withDecimalStyle(DecimalStyle.of(locale)).format(cDate);
                        if (hijrahStr == null) {
                            firstMonth = month;
                            firstYear = year;
                            firstMonthStr = monthStr;
                            firstYearStr = yearStr;
                            hijrahStr = firstMonthStr + " " + firstYearStr;
                        } else if (year > firstYear) {
                            hijrahStr = firstMonthStr + " " + firstYearStr + " - " + monthStr + " " + yearStr;
                        } else {
                            hijrahStr = firstMonthStr + " - " + monthStr + " " + firstYearStr;
                        }
                    }
                } catch (DateTimeException e2) {
                }
            }
        }
        this.hijrahMonthYearLabel.setText(hijrahStr);
    }

    @Override // com.sun.javafx.scene.control.skin.DatePickerContent
    protected void createDayCells() {
        super.createDayCells();
        for (DateCell dayCell : this.dayCells) {
            Text secondaryText = new Text();
            dayCell.getProperties().put("DateCell.secondaryText", secondaryText);
        }
    }

    @Override // com.sun.javafx.scene.control.skin.DatePickerContent
    void updateDayCells() {
        super.updateDayCells();
        Locale locale = getLocale();
        HijrahChronology chrono = HijrahChronology.INSTANCE;
        for (DateCell dayCell : this.dayCells) {
            Text secondaryText = (Text) dayCell.getProperties().get("DateCell.secondaryText");
            dayCell.getStyleClass().add("hijrah-day-cell");
            secondaryText.getStyleClass().setAll("text", "secondary-text");
            try {
                HijrahDate cDate = chrono.date((TemporalAccessor) dayCellDate(dayCell));
                String hijrahStr = this.dayCellFormatter.withLocale(locale).withChronology(chrono).withDecimalStyle(DecimalStyle.of(locale)).format(cDate);
                secondaryText.setText(hijrahStr);
                dayCell.requestLayout();
            } catch (DateTimeException e2) {
                secondaryText.setText(" ");
                dayCell.setDisable(true);
            }
        }
    }
}
