package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import java.sql.Types;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.swing.JInternalFrame;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/DatePickerContent.class */
public class DatePickerContent extends VBox {
    protected DatePicker datePicker;
    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;
    private Label monthLabel;
    private Label yearLabel;
    protected GridPane gridPane;
    private int daysPerWeek;
    private LocalDate[] dayCellDates;
    static final /* synthetic */ boolean $assertionsDisabled;
    private List<DateCell> dayNameCells = new ArrayList();
    private List<DateCell> weekNumberCells = new ArrayList();
    protected List<DateCell> dayCells = new ArrayList();
    private DateCell lastFocusedDayCell = null;
    final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    final DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL");
    final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern(PdfOps.y_TOKEN);
    final DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy");
    final DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern(PdfOps.w_TOKEN);
    final DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc");
    final DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern(PdfOps.d_TOKEN);
    private ObjectProperty<YearMonth> displayedYearMonth = new SimpleObjectProperty(this, "displayedYearMonth");

    static {
        $assertionsDisabled = !DatePickerContent.class.desiredAssertionStatus();
    }

    static String getString(String key) {
        return ControlResources.getString("DatePicker." + key);
    }

    DatePickerContent(final DatePicker datePicker) {
        this.datePicker = datePicker;
        getStyleClass().add("date-picker-popup");
        this.daysPerWeek = getDaysPerWeek();
        LocalDate date = datePicker.getValue();
        this.displayedYearMonth.set(date != null ? YearMonth.from(date) : YearMonth.now());
        this.displayedYearMonth.addListener((observable, oldValue, newValue) -> {
            updateValues();
        });
        getChildren().add(createMonthYearPane());
        this.gridPane = new GridPane() { // from class: com.sun.javafx.scene.control.skin.DatePickerContent.1
            @Override // javafx.scene.layout.GridPane, javafx.scene.layout.Region, javafx.scene.Parent
            protected double computePrefWidth(double height) {
                double width = super.computePrefWidth(height);
                int nCols = DatePickerContent.this.daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);
                double snaphgap = snapSpace(getHgap());
                double left = snapSpace(getInsets().getLeft());
                double right = snapSpace(getInsets().getRight());
                double hgaps = snaphgap * (nCols - 1);
                double contentWidth = ((width - left) - right) - hgaps;
                return (snapSize(contentWidth / nCols) * nCols) + left + right + hgaps;
            }

            @Override // javafx.scene.layout.GridPane, javafx.scene.Parent
            protected void layoutChildren() {
                if (getWidth() > 0.0d && getHeight() > 0.0d) {
                    super.layoutChildren();
                }
            }
        };
        this.gridPane.setFocusTraversable(true);
        this.gridPane.getStyleClass().add("calendar-grid");
        this.gridPane.setVgap(-1.0d);
        this.gridPane.setHgap(-1.0d);
        WeakChangeListener<Node> weakFocusOwnerListener = new WeakChangeListener<>((ov2, oldFocusOwner, newFocusOwner) -> {
            if (newFocusOwner == this.gridPane) {
                if (oldFocusOwner instanceof DateCell) {
                    this.gridPane.impl_traverse(Direction.PREVIOUS);
                } else if (this.lastFocusedDayCell != null) {
                    Platform.runLater(() -> {
                        this.lastFocusedDayCell.requestFocus();
                    });
                } else {
                    clearFocus();
                }
            }
        });
        this.gridPane.sceneProperty().addListener(new WeakChangeListener((ov, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.focusOwnerProperty().removeListener(weakFocusOwnerListener);
            }
            if (newScene != null) {
                newScene.focusOwnerProperty().addListener(weakFocusOwnerListener);
            }
        }));
        if (this.gridPane.getScene() != null) {
            this.gridPane.getScene().focusOwnerProperty().addListener(weakFocusOwnerListener);
        }
        for (int i2 = 0; i2 < this.daysPerWeek; i2++) {
            DateCell cell = new DateCell();
            cell.getStyleClass().add("day-name-cell");
            this.dayNameCells.add(cell);
        }
        for (int i3 = 0; i3 < 6; i3++) {
            DateCell cell2 = new DateCell();
            cell2.getStyleClass().add("week-number-cell");
            this.weekNumberCells.add(cell2);
        }
        createDayCells();
        updateGrid();
        getChildren().add(this.gridPane);
        refresh();
        addEventHandler(KeyEvent.ANY, e2 -> {
            Node node = getScene().getFocusOwner();
            if (node instanceof DateCell) {
                this.lastFocusedDayCell = (DateCell) node;
            }
            if (e2.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (e2.getCode()) {
                    case HOME:
                        goToDate(LocalDate.now(), true);
                        e2.consume();
                        break;
                    case PAGE_UP:
                        if ((PlatformUtil.isMac() && e2.isMetaDown()) || (!PlatformUtil.isMac() && e2.isControlDown())) {
                            if (!this.backYearButton.isDisabled()) {
                                forward(-1, ChronoUnit.YEARS, true);
                            }
                        } else if (!this.backMonthButton.isDisabled()) {
                            forward(-1, ChronoUnit.MONTHS, true);
                        }
                        e2.consume();
                        break;
                    case PAGE_DOWN:
                        if ((PlatformUtil.isMac() && e2.isMetaDown()) || (!PlatformUtil.isMac() && e2.isControlDown())) {
                            if (!this.forwardYearButton.isDisabled()) {
                                forward(1, ChronoUnit.YEARS, true);
                            }
                        } else if (!this.forwardMonthButton.isDisabled()) {
                            forward(1, ChronoUnit.MONTHS, true);
                        }
                        e2.consume();
                        break;
                }
                Node node2 = getScene().getFocusOwner();
                if (node2 instanceof DateCell) {
                    this.lastFocusedDayCell = (DateCell) node2;
                }
            }
            switch (e2.getCode()) {
                case F4:
                case F10:
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                case TAB:
                    break;
                case ESCAPE:
                    datePicker.hide();
                    e2.consume();
                    break;
                default:
                    e2.consume();
                    break;
            }
        });
    }

    ObjectProperty<YearMonth> displayedYearMonthProperty() {
        return this.displayedYearMonth;
    }

    protected BorderPane createMonthYearPane() {
        BorderPane monthYearPane = new BorderPane();
        monthYearPane.getStyleClass().add("month-year-pane");
        HBox monthSpinner = new HBox();
        monthSpinner.getStyleClass().add("spinner");
        this.backMonthButton = new Button();
        this.backMonthButton.getStyleClass().add("left-button");
        this.forwardMonthButton = new Button();
        this.forwardMonthButton.getStyleClass().add("right-button");
        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.getStyleClass().add("left-arrow");
        leftMonthArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.backMonthButton.setGraphic(leftMonthArrow);
        StackPane rightMonthArrow = new StackPane();
        rightMonthArrow.getStyleClass().add("right-arrow");
        rightMonthArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.forwardMonthButton.setGraphic(rightMonthArrow);
        this.backMonthButton.setOnAction(t2 -> {
            forward(-1, ChronoUnit.MONTHS, false);
        });
        this.monthLabel = new Label();
        this.monthLabel.getStyleClass().add("spinner-label");
        this.forwardMonthButton.setOnAction(t3 -> {
            forward(1, ChronoUnit.MONTHS, false);
        });
        monthSpinner.getChildren().addAll(this.backMonthButton, this.monthLabel, this.forwardMonthButton);
        monthYearPane.setLeft(monthSpinner);
        HBox yearSpinner = new HBox();
        yearSpinner.getStyleClass().add("spinner");
        this.backYearButton = new Button();
        this.backYearButton.getStyleClass().add("left-button");
        this.forwardYearButton = new Button();
        this.forwardYearButton.getStyleClass().add("right-button");
        StackPane leftYearArrow = new StackPane();
        leftYearArrow.getStyleClass().add("left-arrow");
        leftYearArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.backYearButton.setGraphic(leftYearArrow);
        StackPane rightYearArrow = new StackPane();
        rightYearArrow.getStyleClass().add("right-arrow");
        rightYearArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.forwardYearButton.setGraphic(rightYearArrow);
        this.backYearButton.setOnAction(t4 -> {
            forward(-1, ChronoUnit.YEARS, false);
        });
        this.yearLabel = new Label();
        this.yearLabel.getStyleClass().add("spinner-label");
        this.forwardYearButton.setOnAction(t5 -> {
            forward(1, ChronoUnit.YEARS, false);
        });
        yearSpinner.getChildren().addAll(this.backYearButton, this.yearLabel, this.forwardYearButton);
        yearSpinner.setFillHeight(false);
        monthYearPane.setRight(yearSpinner);
        return monthYearPane;
    }

    private void refresh() {
        updateMonthLabelWidth();
        updateDayNameCells();
        updateValues();
    }

    void updateValues() {
        updateWeeknumberDateCells();
        updateDayCells();
        updateMonthYearPane();
    }

    void updateGrid() {
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getChildren().clear();
        int nCols = this.daysPerWeek + (this.datePicker.isShowWeekNumbers() ? 1 : 0);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.0d);
        for (int i2 = 0; i2 < nCols; i2++) {
            this.gridPane.getColumnConstraints().add(columnConstraints);
        }
        for (int i3 = 0; i3 < this.daysPerWeek; i3++) {
            this.gridPane.add(this.dayNameCells.get(i3), (i3 + nCols) - this.daysPerWeek, 1);
        }
        if (this.datePicker.isShowWeekNumbers()) {
            for (int i4 = 0; i4 < 6; i4++) {
                this.gridPane.add(this.weekNumberCells.get(i4), 0, i4 + 2);
            }
        }
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < this.daysPerWeek; col++) {
                this.gridPane.add(this.dayCells.get((row * this.daysPerWeek) + col), (col + nCols) - this.daysPerWeek, row + 2);
            }
        }
    }

    void updateDayNameCells() {
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        LocalDate date = LocalDate.of(Types.SQLXML, 7, 12 + firstDayOfWeek);
        for (int i2 = 0; i2 < this.daysPerWeek; i2++) {
            String name = this.weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i2, (TemporalUnit) ChronoUnit.DAYS));
            this.dayNameCells.get(i2).setText(titleCaseWord(name));
        }
    }

    void updateWeeknumberDateCells() {
        if (this.datePicker.isShowWeekNumbers()) {
            Locale locale = getLocale();
            LocalDate firstOfMonth = this.displayedYearMonth.get().atDay(1);
            for (int i2 = 0; i2 < 6; i2++) {
                LocalDate date = firstOfMonth.plus(i2, (TemporalUnit) ChronoUnit.WEEKS);
                String cellText = this.weekNumberFormatter.withLocale(locale).withDecimalStyle(DecimalStyle.of(locale)).format(date);
                this.weekNumberCells.get(i2).setText(cellText);
            }
        }
    }

    void updateDayCells() {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
        YearMonth curMonth = this.displayedYearMonth.get();
        YearMonth prevMonth = null;
        YearMonth nextMonth = null;
        int daysInCurMonth = -1;
        int daysInPrevMonth = -1;
        for (int i2 = 0; i2 < 6 * this.daysPerWeek; i2++) {
            DateCell dayCell = this.dayCells.get(i2);
            dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
            dayCell.setDisable(false);
            dayCell.setStyle(null);
            dayCell.setGraphic(null);
            dayCell.setTooltip(null);
            if (daysInCurMonth == -1) {
                try {
                    daysInCurMonth = curMonth.lengthOfMonth();
                } catch (DateTimeException e2) {
                    dayCell.setText(" ");
                    dayCell.setDisable(true);
                }
            }
            YearMonth month = curMonth;
            int day = (i2 - firstOfMonthIdx) + 1;
            if (i2 < firstOfMonthIdx) {
                if (prevMonth == null) {
                    prevMonth = curMonth.minusMonths(1L);
                    daysInPrevMonth = prevMonth.lengthOfMonth();
                }
                month = prevMonth;
                day = ((i2 + daysInPrevMonth) - firstOfMonthIdx) + 1;
                dayCell.getStyleClass().add("previous-month");
            } else if (i2 >= firstOfMonthIdx + daysInCurMonth) {
                if (nextMonth == null) {
                    nextMonth = curMonth.plusMonths(1L);
                    nextMonth.lengthOfMonth();
                }
                month = nextMonth;
                day = ((i2 - daysInCurMonth) - firstOfMonthIdx) + 1;
                dayCell.getStyleClass().add("next-month");
            }
            LocalDate date = month.atDay(day);
            this.dayCellDates[i2] = date;
            ChronoLocalDate cDate = chrono.date(date);
            dayCell.setDisable(false);
            if (isToday(date)) {
                dayCell.getStyleClass().add("today");
            }
            if (date.equals(this.datePicker.getValue())) {
                dayCell.getStyleClass().add(JInternalFrame.IS_SELECTED_PROPERTY);
            }
            String cellText = this.dayCellFormatter.withLocale(locale).withChronology(chrono).withDecimalStyle(DecimalStyle.of(locale)).format(cDate);
            dayCell.setText(cellText);
            dayCell.updateItem(date, false);
        }
    }

    private int getDaysPerWeek() {
        ValueRange range = getPrimaryChronology().range(ChronoField.DAY_OF_WEEK);
        return (int) ((range.getMaximum() - range.getMinimum()) + 1);
    }

    private int getMonthsPerYear() {
        ValueRange range = getPrimaryChronology().range(ChronoField.MONTH_OF_YEAR);
        return (int) ((range.getMaximum() - range.getMinimum()) + 1);
    }

    private void updateMonthLabelWidth() {
        if (this.monthLabel != null) {
            int monthsPerYear = getMonthsPerYear();
            double width = 0.0d;
            for (int i2 = 0; i2 < monthsPerYear; i2++) {
                YearMonth yearMonth = this.displayedYearMonth.get().withMonth(i2 + 1);
                String name = this.monthFormatterSO.withLocale(getLocale()).format(yearMonth);
                if (Character.isDigit(name.charAt(0))) {
                    name = this.monthFormatter.withLocale(getLocale()).format(yearMonth);
                }
                width = Math.max(width, Utils.computeTextWidth(this.monthLabel.getFont(), name, 0.0d));
            }
            this.monthLabel.setMinWidth(width);
        }
    }

    protected void updateMonthYearPane() {
        YearMonth yearMonth = this.displayedYearMonth.get();
        this.monthLabel.setText(formatMonth(yearMonth));
        String str = formatYear(yearMonth);
        this.yearLabel.setText(str);
        double width = Utils.computeTextWidth(this.yearLabel.getFont(), str, 0.0d);
        if (width > this.yearLabel.getMinWidth()) {
            this.yearLabel.setMinWidth(width);
        }
        Chronology chrono = this.datePicker.getChronology();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        this.backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, ChronoUnit.DAYS));
        this.forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, 1, ChronoUnit.MONTHS));
        this.backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, ChronoUnit.YEARS));
        this.forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, 1, ChronoUnit.YEARS));
    }

    private String formatMonth(YearMonth yearMonth) {
        getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
            String str = this.monthFormatterSO.withLocale(getLocale()).withChronology(chrono).format(cDate);
            if (Character.isDigit(str.charAt(0))) {
                str = this.monthFormatter.withLocale(getLocale()).withChronology(chrono).format(cDate);
            }
            return titleCaseWord(str);
        } catch (DateTimeException e2) {
            return "";
        }
    }

    private String formatYear(YearMonth yearMonth) {
        getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            DateTimeFormatter formatter = this.yearFormatter;
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
            int era = cDate.getEra().getValue();
            int nEras = chrono.eras().size();
            if ((nEras == 2 && era == 0) || nEras > 2) {
                formatter = this.yearWithEraFormatter;
            }
            String str = formatter.withLocale(getLocale()).withChronology(chrono).withDecimalStyle(DecimalStyle.of(getLocale())).format(cDate);
            return str;
        } catch (DateTimeException e2) {
            return "";
        }
    }

    private String titleCaseWord(String str) {
        if (str.length() > 0) {
            int firstChar = str.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                str = new String(new int[]{Character.toTitleCase(firstChar)}, 0, 1) + str.substring(Character.offsetByCodePoints(str, 0, 1));
            }
        }
        return str;
    }

    private int determineFirstOfMonthDayOfWeek() {
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        int firstOfMonthIdx = this.displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
        if (firstOfMonthIdx < 0) {
            firstOfMonthIdx += this.daysPerWeek;
        }
        return firstOfMonthIdx;
    }

    private boolean isToday(LocalDate localDate) {
        return localDate.equals(LocalDate.now());
    }

    protected LocalDate dayCellDate(DateCell dateCell) {
        if ($assertionsDisabled || this.dayCellDates != null) {
            return this.dayCellDates[this.dayCells.indexOf(dateCell)];
        }
        throw new AssertionError();
    }

    public void goToDayCell(DateCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
        goToDate(dayCellDate(dateCell).plus(offset, (TemporalUnit) unit), focusDayCell);
    }

    protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
        YearMonth yearMonth = this.displayedYearMonth.get();
        DateCell dateCell = this.lastFocusedDayCell;
        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = findDayCellForDate(yearMonth.atDay(1));
        }
        goToDayCell(dateCell, offset, unit, focusDayCell);
    }

    public void goToDate(LocalDate date, boolean focusDayCell) {
        if (isValidDate(this.datePicker.getChronology(), date)) {
            this.displayedYearMonth.set(YearMonth.from(date));
            if (focusDayCell) {
                findDayCellForDate(date).requestFocus();
            }
        }
    }

    public void selectDayCell(DateCell dateCell) {
        this.datePicker.setValue(dayCellDate(dateCell));
        this.datePicker.hide();
    }

    private DateCell findDayCellForDate(LocalDate date) {
        for (int i2 = 0; i2 < this.dayCellDates.length; i2++) {
            if (date.equals(this.dayCellDates[i2])) {
                return this.dayCells.get(i2);
            }
        }
        return this.dayCells.get((this.dayCells.size() / 2) + 1);
    }

    void clearFocus() {
        LocalDate focusDate = this.datePicker.getValue();
        if (focusDate == null) {
            focusDate = LocalDate.now();
        }
        if (YearMonth.from(focusDate).equals(this.displayedYearMonth.get())) {
            goToDate(focusDate, true);
        } else {
            this.backMonthButton.requestFocus();
        }
        if (this.backMonthButton.getWidth() == 0.0d) {
            this.backMonthButton.requestLayout();
            this.forwardMonthButton.requestLayout();
            this.backYearButton.requestLayout();
            this.forwardYearButton.requestLayout();
        }
    }

    protected void createDayCells() {
        EventHandler<MouseEvent> dayCellActionHandler = ev -> {
            if (ev.getButton() != MouseButton.PRIMARY) {
                return;
            }
            DateCell dayCell = (DateCell) ev.getSource();
            selectDayCell(dayCell);
            this.lastFocusedDayCell = dayCell;
        };
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < this.daysPerWeek; col++) {
                DateCell dayCell = createDayCell();
                dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
                this.dayCells.add(dayCell);
            }
        }
        this.dayCellDates = new LocalDate[6 * this.daysPerWeek];
    }

    private DateCell createDayCell() {
        DateCell cell = null;
        if (this.datePicker.getDayCellFactory() != null) {
            cell = this.datePicker.getDayCellFactory().call(this.datePicker);
        }
        if (cell == null) {
            cell = new DateCell();
        }
        return cell;
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    protected Chronology getPrimaryChronology() {
        return this.datePicker.getChronology();
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date, int offset, ChronoUnit unit) {
        if (date != null) {
            try {
                return isValidDate(chrono, date.plus(offset, (TemporalUnit) unit));
            } catch (DateTimeException e2) {
                return false;
            }
        }
        return false;
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date) {
        if (date != null) {
            try {
                chrono.date(date);
                return true;
            } catch (DateTimeException e2) {
                return false;
            }
        }
        return true;
    }
}
