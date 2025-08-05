package aP;

import W.C0171aa;
import W.C0194t;
import bh.C1165y;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.tunerStudio.panels.TriggerLoggerPanel;
import g.C1724b;
import java.awt.Color;
import r.C1798a;
import r.C1806i;
import z.C1899c;

/* renamed from: aP.bw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bw.class */
public class C0254bw {
    public void a(String[] strArr, String str) {
        C1798a.f13275i = str;
        new C0255bx(this).start();
        if ((strArr.length > 0 && strArr[0].equals("QJ")) || str.equals("TunerStudioQJ.jar")) {
            C1798a.a();
            C1798a.f13269c = "QJ";
            if (strArr.length > 0 && strArr[0].equals("QJ")) {
                strArr[0] = "";
            }
        } else if ((strArr.length > 0 && strArr[0].equals("TuneMonster")) || str.equals("TuneMonsterEFI.jar")) {
            C1798a.f13268b = C1798a.f13340av;
            C1798a.a();
            C1798a.f13269c = "EFI";
            C1798a.f13274h = "https://www.efianalytics.com/register/register.jsp?appName=TuneMonster";
            if (strArr.length > 0 && strArr[0].equals("TuneMonster")) {
                strArr[0] = "";
            }
            gW.f3465p = "Auto Tune";
            C1798a.f0do = new String[]{C1806i.f13448j};
            C1798a.cP = true;
            C1798a.f13371ba = C1899c.f14084e;
            C1798a.f13375be = false;
        } else if ((strArr.length > 0 && strArr[0].equals("FuelMonster")) || str.equals("FuelMonsterTuner.jar")) {
            C1798a.f13268b = C1798a.f13341aw;
            C1798a.a();
            C1798a.f13269c = "Tuner";
            C1798a.f13274h = "https://www.efianalytics.com/register/register.jsp?appName=FuelMonster";
            if (strArr.length > 0 && strArr[0].equals("FuelMonster")) {
                strArr[0] = "";
            }
            gW.f3465p = "Auto Tune";
            String[] strArr2 = {C1806i.f13449k};
            C1798a.cP = true;
            C1798a.f0do = strArr2;
            C1798a.f13371ba = C1899c.f14084e;
            C1798a.f13375be = false;
        } else if ((strArr.length > 0 && strArr[0].equals("GrassRoots")) || str.equals("GrassRootsTuner.jar")) {
            C1798a.f13268b = C1798a.f13339au;
            C1798a.a();
            C1798a.f13269c = "Tuner";
            C1798a.f13274h = "https://www.efianalytics.com/register/register.jsp?appName=GrassRootsTuner";
            C1798a.f13288v = "registerGr.html";
            C1798a.f13289w = "registerAppNotConnectedGr.html";
            if (strArr.length > 0 && strArr[0].equals("GrassRoots")) {
                strArr[0] = "";
            }
            gW.f3465p = "Auto Tune";
        } else if ((strArr.length > 0 && strArr[0].equals("BigComm")) || str.equals("BigCommPro.jar")) {
            C1798a.f13268b = C1798a.f13337as;
            C1798a.a();
            if (C1798a.f13269c.contains("Beta")) {
                C1798a.a();
                C1798a.f13269c = "Pro(Beta)";
            } else {
                C1798a.a();
                C1798a.f13269c = "Pro";
            }
            C1798a.f13274h = "https://www.efianalytics.com/register/browseProducts.jsp?ecuFamily=BigStuff&productCategory=Software";
            C1798a.f13288v = "registerBC.html";
            C1798a.f13289w = "registerAppNotConnectedBC.html";
            C0171aa.f2042b = "http://www.efianalytics.com/:bsq";
            C0171aa.f2043c = "bsq";
            C1798a.f13286t = "bsproj";
            C1798a.f13295C = "big";
            C1798a.cs = "csv";
            C1798a.cB = C1798a.cr;
            C1388aa.f9453b = "BigStuff-Template.dash";
            C1388aa.f9452a = 7;
            C1798a.f13305M = "https://www.efianalytics.com/register/browseProducts.jsp?ecuFamily=BigStuff&productCategory=Upgrades";
            h.i.f12280A = "./config/dashSplashBigComm.png";
            if (strArr.length > 0 && strArr[0].equals("BigComm")) {
                strArr[0] = "";
            }
            gW.f3465p = "Auto-Tune";
            C1388aa.f9462d = Color.BLACK;
            h.i.f12299T = "withLabels";
            aD.a.f2297c = "9600";
            Gauge.f9305s = new Color(8, 8, 8);
            Gauge.f9307u = new Color(241, 255, 255);
            Gauge.f9306t = new Color(37, 37, 46);
            Gauge.f9308v = new Color(255, 44, 20);
            h.i.f12263j = "LogViewerBC.properties";
            h.i.f12262i = "mlvEmBC.properties";
            C0403hk.f3585b = new String[3];
            C0403hk.f3585b[0] = "C:\\Program Files (x86)\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C0403hk.f3585b[1] = "C:\\Program Files(x86)\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C0403hk.f3585b[2] = "C:\\Program Files\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C1798a.f13296D = "http://www.bigcommpro.com/software/megalog";
            h.g.f12240a = "FieldMaps/BigStuffReplay.properties";
            h.i.f12277x = false;
            h.i.f12273t = 5;
            h.i.f12274u = 20;
            C0293dh.f3228b = true;
            C0194t.f2183a = "./config/ecuDef/BigStuff3-097.ecu";
            C1798a.ce = C1798a.f13420bX;
            C1798a.f13272f = "BigStuff3";
            C1798a.f13273g = "BigStuff3";
            C1798a.f13282p = "BigStuff3 Serial Number";
            C1798a.cf = C1798a.f13420bX;
            C1798a.cb = true;
            C1798a.a().a("Pro Single");
            C1388aa.f9454c = "/com/efiAnalytics/apps/ts/dashboard/default_BC.gauge";
            C1798a.f0do = new String[]{C1806i.f13453o, C1806i.f13456r, C1806i.f13455q};
            C1798a.f13371ba = C1899c.f14084e;
            TriggerLoggerPanel.f10021Q = true;
        } else if ((strArr.length <= 0 || !strArr[0].equals("BigCommGen4")) && !str.equals("BigCommGen4.jar")) {
            C1798a.f13272f = "MegaSquirt";
            C1798a.f13273g = "megasquirt";
            h.i.f12263j = "LogViewer.properties";
            h.i.f12262i = "mlvEm.properties";
            h.i.f12280A = "./config/dashSplash.png";
            C1798a.f13371ba = C1899c.f14084e;
        } else {
            C1798a.f13268b = C1798a.f13338at;
            C1798a.a();
            if (C1798a.f13269c.contains("Beta")) {
                C1798a.a();
                C1798a.f13269c = C1806i.f13453o + "(Beta)";
            } else {
                C1798a.a();
                C1798a.f13269c = C1806i.f13453o;
            }
            C1798a.f13274h = "https://www.efianalytics.com/register/browseProducts.jsp?ecuFamily=BigStuff&productCategory=Software";
            C1798a.f13288v = "registerBC.html";
            C1798a.f13289w = "registerAppNotConnectedBC.html";
            C0171aa.f2042b = "http://www.efianalytics.com/:bsq";
            C0171aa.f2043c = "bsq";
            C1798a.f13286t = "bsproj";
            C1798a.f13295C = "big";
            C1798a.cs = "csv";
            C1798a.cB = C1798a.cr;
            C1798a.f13395by = true;
            C1388aa.f9453b = "BigStuff_Gen4-Template.dash";
            C1388aa.f9452a = 7;
            C1798a.f13305M = "https://www.efianalytics.com/register/browseProducts.jsp?ecuFamily=BigStuff&productCategory=Upgrades";
            h.i.f12280A = "./config/dashSplashBigCommGen4.png";
            h.i.f12273t = 5;
            h.i.f12274u = 20;
            if (strArr.length > 0 && strArr[0].equals("BigComm")) {
                strArr[0] = "";
            }
            gW.f3465p = "Auto-Tune";
            C1388aa.f9462d = Color.BLACK;
            h.i.f12299T = "withLabels";
            Gauge.f9305s = new Color(8, 8, 8);
            Gauge.f9307u = new Color(241, 255, 255);
            Gauge.f9306t = new Color(37, 37, 46);
            Gauge.f9308v = new Color(255, 44, 20);
            h.i.f12263j = "LogViewerBC.properties";
            h.i.f12262i = "mlvEmBC.properties";
            C0403hk.f3585b = new String[3];
            C0403hk.f3585b[0] = "C:\\Program Files (x86)\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C0403hk.f3585b[1] = "C:\\Program Files(x86)\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C0403hk.f3585b[2] = "C:\\Program Files\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe";
            C1798a.f13296D = "http://www.bigcommpro.com/software/megalog";
            h.g.f12240a = "FieldMaps/BigStuffReplay.properties";
            h.i.f12277x = false;
            C0293dh.f3228b = true;
            C0194t.f2183a = "./config/ecuDef/BigStuff3-097.ecu";
            C1798a.ce = C1798a.f13420bX;
            C1798a.f13272f = "BigStuff Gen4";
            C1798a.f13273g = "BigStuff Gen4";
            C1798a.f13282p = "BigStuff Serial Number";
            C1798a.cf = C1798a.f13420bX;
            C1798a.cb = true;
            C1388aa.f9454c = "/com/efiAnalytics/apps/ts/dashboard/default_BC.gauge";
            C1798a.f0do = new String[]{C1806i.f13453o};
            C1798a.f13334ap = "bcGen4.reg";
            C1798a.f13335aq = "bcGen4User.properties";
            C1798a.f13371ba = C1899c.f14085f;
            aV.w.a(B.b.c());
            bH.V.f7026b = 3500;
            aE.a.f2345e = true;
            aE.a.f2347g = true;
            G.F.f337a = 100;
            aE.a.f2342b = "100";
            TriggerLoggerPanel.f10021Q = true;
        }
        C1724b.a(new C1165y());
    }
}
