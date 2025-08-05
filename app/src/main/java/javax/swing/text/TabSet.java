package javax.swing.text;

import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/text/TabSet.class */
public class TabSet implements Serializable {
    private TabStop[] tabs;
    private int hashCode = Integer.MAX_VALUE;

    public TabSet(TabStop[] tabStopArr) {
        if (tabStopArr != null) {
            int length = tabStopArr.length;
            this.tabs = new TabStop[length];
            System.arraycopy(tabStopArr, 0, this.tabs, 0, length);
            return;
        }
        this.tabs = null;
    }

    public int getTabCount() {
        if (this.tabs == null) {
            return 0;
        }
        return this.tabs.length;
    }

    public TabStop getTab(int i2) {
        int tabCount = getTabCount();
        if (i2 < 0 || i2 >= tabCount) {
            throw new IllegalArgumentException(i2 + " is outside the range of tabs");
        }
        return this.tabs[i2];
    }

    public TabStop getTabAfter(float f2) {
        int tabIndexAfter = getTabIndexAfter(f2);
        if (tabIndexAfter == -1) {
            return null;
        }
        return this.tabs[tabIndexAfter];
    }

    public int getTabIndex(TabStop tabStop) {
        for (int tabCount = getTabCount() - 1; tabCount >= 0; tabCount--) {
            if (getTab(tabCount) == tabStop) {
                return tabCount;
            }
        }
        return -1;
    }

    public int getTabIndexAfter(float f2) {
        int i2 = 0;
        int tabCount = getTabCount();
        while (i2 != tabCount) {
            int i3 = ((tabCount - i2) / 2) + i2;
            if (f2 > this.tabs[i3].getPosition()) {
                if (i2 == i3) {
                    i2 = tabCount;
                } else {
                    i2 = i3;
                }
            } else {
                if (i3 == 0 || f2 > this.tabs[i3 - 1].getPosition()) {
                    return i3;
                }
                tabCount = i3;
            }
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TabSet) {
            TabSet tabSet = (TabSet) obj;
            int tabCount = getTabCount();
            if (tabSet.getTabCount() != tabCount) {
                return false;
            }
            for (int i2 = 0; i2 < tabCount; i2++) {
                TabStop tab = getTab(i2);
                TabStop tab2 = tabSet.getTab(i2);
                if (tab != null || tab2 == null) {
                    if (tab != null && !getTab(i2).equals(tabSet.getTab(i2))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == Integer.MAX_VALUE) {
            this.hashCode = 0;
            int tabCount = getTabCount();
            for (int i2 = 0; i2 < tabCount; i2++) {
                this.hashCode ^= getTab(i2) != null ? getTab(i2).hashCode() : 0;
            }
            if (this.hashCode == Integer.MAX_VALUE) {
                this.hashCode--;
            }
        }
        return this.hashCode;
    }

    public String toString() {
        int tabCount = getTabCount();
        StringBuilder sb = new StringBuilder("[ ");
        for (int i2 = 0; i2 < tabCount; i2++) {
            if (i2 > 0) {
                sb.append(" - ");
            }
            sb.append(getTab(i2).toString());
        }
        sb.append(" ]");
        return sb.toString();
    }
}
