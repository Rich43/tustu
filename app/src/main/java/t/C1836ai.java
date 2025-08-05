package t;

import G.dh;
import G.di;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1419r;
import com.efiAnalytics.apps.ts.dashboard.DashLabel;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent;
import com.efiAnalytics.ui.bV;
import d.InterfaceC1712d;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.TransferHandler;

/* renamed from: t.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ai.class */
public class C1836ai {

    /* renamed from: a, reason: collision with root package name */
    private ArrayList f13786a = null;

    public ArrayList a() {
        return this.f13786a;
    }

    public void a(ArrayList arrayList) {
        this.f13786a = arrayList;
    }

    protected boolean b(ArrayList arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((AbstractC1420s) it.next()) instanceof Indicator) {
                return true;
            }
        }
        return false;
    }

    protected boolean c(ArrayList arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((AbstractC1420s) it.next()) instanceof Gauge) {
                return true;
            }
        }
        return false;
    }

    protected boolean d(ArrayList arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((AbstractC1420s) it.next()) instanceof DashLabel) {
                return true;
            }
        }
        return false;
    }

    public void a(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setFontFamily(str);
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    public void a(double d2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof SingleChannelDashComponent) {
                ((SingleChannelDashComponent) abstractC1420s).setValue(d2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void a(boolean z2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setItalicFont(z2);
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    public void b(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof SingleChannelDashComponent) {
                SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) abstractC1420s;
                singleChannelDashComponent.setOutputChannel(str);
                try {
                    singleChannelDashComponent.subscribeToOutput();
                } catch (V.a e2) {
                    bV.d(e2.getMessage(), abstractC1420s);
                }
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void c(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setEcuConfigurationName(str);
            if (abstractC1420s instanceof SingleChannelDashComponent) {
                try {
                    ((SingleChannelDashComponent) abstractC1420s).subscribeToOutput();
                } catch (V.a e2) {
                    bV.d(e2.getMessage(), abstractC1420s);
                }
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void a(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setFaceAngle(i2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void d(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setBackgroundImageFileName(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void e(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setNeedleImageFileName(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void f(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOnImageFileName(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void g(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOffImageFileName(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void h(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.invalidate();
            abstractC1420s.setId(str);
        }
    }

    public void b(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setStartAngle(i2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void c(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setSweepBeginDegree(i2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void d(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setNeedleSmoothing(i2);
            }
        }
    }

    public void e(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setSweepAngle(i2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void f(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setRelativeBorderWidth2(i2 / gauge.getShortestSize());
                gauge.invalidate();
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void g(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setFontSizeAdjustment(i2);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void i(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setTitle(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void j(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setUnits(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void a(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setTrimColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void b(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setBackColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void c(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setFontColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void d(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setNeedleColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void e(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setWarnColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void f(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setCriticalColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void h(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setBounds(i2, abstractC1420s.getY(), abstractC1420s.getWidth(), abstractC1420s.getHeight());
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    public void i(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setBounds(abstractC1420s.getX(), i2, abstractC1420s.getWidth(), abstractC1420s.getHeight());
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    public void j(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setBounds(abstractC1420s.getX(), abstractC1420s.getY(), i2, abstractC1420s.getHeight());
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    public void k(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            abstractC1420s.setBounds(abstractC1420s.getX(), abstractC1420s.getY(), abstractC1420s.getWidth(), i2);
            abstractC1420s.updateRelativeBoundsToCurrent();
            abstractC1420s.invalidate();
            abstractC1420s.repaint();
        }
    }

    private void a(dh dhVar) throws V.a {
        if (Double.isNaN(dhVar.a())) {
            throw new V.a("Invalid value: " + dhVar.toString());
        }
    }

    public void k(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setMinVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void l(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setMaxVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void m(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setLowWarningVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void n(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setLowCriticalVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void o(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setHighWarningVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void p(String str) throws V.a {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                try {
                    dh dhVarA = di.a(C1419r.a(abstractC1420s), str);
                    a(dhVarA);
                    ((Gauge) abstractC1420s).setHighCriticalVP(dhVarA);
                    abstractC1420s.invalidate();
                    abstractC1420s.repaint();
                } catch (V.g e2) {
                    throw new V.a(e2.getLocalizedMessage());
                }
            }
        }
    }

    public void l(int i2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setValueDigits(Integer.valueOf(i2));
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void q(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOnText(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void r(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOffText(str);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void s(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof DashLabel) {
                ((DashLabel) abstractC1420s).setText(str);
                abstractC1420s.invalidatePainter();
                abstractC1420s.repaint();
            }
        }
    }

    public void g(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof DashLabel) {
                ((DashLabel) abstractC1420s).setBackgroundColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void h(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof DashLabel) {
                ((DashLabel) abstractC1420s).setTextColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void i(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOnTextColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void j(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOffTextColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void k(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOnBackgroundColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void l(Color color) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Indicator) {
                ((Indicator) abstractC1420s).setOffBackgroundColor(color);
                abstractC1420s.invalidate();
                abstractC1420s.repaint();
            }
        }
    }

    public void b(boolean z2) throws NumberFormatException {
        int i2 = 0;
        if (z2) {
            String strA = bV.a("{Number of seconds to remember history. }", true, "Set Guage History Tell Tail Timeout.\nDefault value 15 seconds.", true, (Component) bV.c());
            if (strA == null || strA.equals("")) {
                return;
            } else {
                i2 = Integer.parseInt(strA + "000");
            }
        }
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setShowHistory(z2);
                gauge.setHistoryDelay(i2);
            }
        }
    }

    public void c(boolean z2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setCounterClockwise(z2);
                abstractC1420s.invalidate();
                gauge.repaint();
            }
        }
    }

    public void d(boolean z2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setDisplayValueAt180(z2);
                abstractC1420s.invalidate();
                gauge.repaint();
            }
        }
    }

    public void e(boolean z2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                Gauge gauge = (Gauge) abstractC1420s;
                gauge.setPegLimits(z2);
                abstractC1420s.invalidate();
                gauge.repaint();
            }
        }
    }

    public void b(double d2) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof Gauge) {
                ((Gauge) abstractC1420s).setHistoricalPeakValue(d2);
            }
        }
    }

    public SingleChannelDashComponent b() {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
            if (abstractC1420s instanceof SingleChannelDashComponent) {
                return (SingleChannelDashComponent) abstractC1420s;
            }
        }
        return null;
    }

    public boolean c() {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            if (((AbstractC1420s) it.next()) instanceof SingleChannelDashComponent) {
                return true;
            }
        }
        return false;
    }

    public void t(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            TransferHandler.HasGetTransferHandler hasGetTransferHandler = (AbstractC1420s) it.next();
            if (hasGetTransferHandler instanceof InterfaceC1712d) {
                ((InterfaceC1712d) hasGetTransferHandler).setShortClickAction(str);
            }
        }
    }

    public void u(String str) {
        Iterator it = this.f13786a.iterator();
        while (it.hasNext()) {
            TransferHandler.HasGetTransferHandler hasGetTransferHandler = (AbstractC1420s) it.next();
            if (hasGetTransferHandler instanceof InterfaceC1712d) {
                ((InterfaceC1712d) hasGetTransferHandler).setLongClickAction(str);
            }
        }
    }
}
