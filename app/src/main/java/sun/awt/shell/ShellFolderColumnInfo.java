package sun.awt.shell;

import java.util.Comparator;
import javax.swing.SortOrder;

/* loaded from: rt.jar:sun/awt/shell/ShellFolderColumnInfo.class */
public class ShellFolderColumnInfo {
    private String title;
    private Integer width;
    private boolean visible;
    private Integer alignment;
    private SortOrder sortOrder;
    private Comparator comparator;
    private boolean compareByColumn;

    public ShellFolderColumnInfo(String str, Integer num, Integer num2, boolean z2, SortOrder sortOrder, Comparator comparator, boolean z3) {
        this.title = str;
        this.width = num;
        this.alignment = num2;
        this.visible = z2;
        this.sortOrder = sortOrder;
        this.comparator = comparator;
        this.compareByColumn = z3;
    }

    public ShellFolderColumnInfo(String str, Integer num, Integer num2, boolean z2, SortOrder sortOrder, Comparator comparator) {
        this(str, num, num2, z2, sortOrder, comparator, false);
    }

    public ShellFolderColumnInfo(String str, int i2, int i3, boolean z2) {
        this(str, Integer.valueOf(i2), Integer.valueOf(i3), z2, null, null);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer num) {
        this.width = num;
    }

    public Integer getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Integer num) {
        this.alignment = num;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean z2) {
        this.visible = z2;
    }

    public SortOrder getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Comparator getComparator() {
        return this.comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public boolean isCompareByColumn() {
        return this.compareByColumn;
    }

    public void setCompareByColumn(boolean z2) {
        this.compareByColumn = z2;
    }
}
