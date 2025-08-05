package p;

import G.T;
import d.C1710b;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* renamed from: p.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/b.class */
public class C1776b {
    public static List a() {
        G.R rC = T.a().c();
        ArrayList arrayList = new ArrayList();
        d.m mVar = new d.m();
        mVar.d("highTempWarning");
        mVar.c("Display High CLT Warning");
        C1710b c1710b = new C1710b();
        c1710b.setProperty("Warning_Message", "Coolant Temperature High!");
        mVar.a(c1710b);
        mVar.a("globalWarning");
        mVar.b("Shows a warning message the CLT temperatures are too high");
        arrayList.add(mVar);
        String property = System.getProperty("javafx.runtime.version");
        if (property != null && property.length() > 0) {
            d.m mVar2 = new d.m();
            mVar2.d("playBurnout");
            mVar2.c("Play burnout WAV");
            C1710b c1710b2 = new C1710b();
            File file = new File("./help/sound/burnoutHotRod.wav");
            c1710b2.setProperty("Sound_File_Path", file.getAbsolutePath());
            mVar2.a(c1710b2);
            mVar2.a("playSoundFile");
            mVar2.b("Plays a WAV file of a Car doing a burnout");
            if (file.exists()) {
                arrayList.add(mVar2);
            }
            d.m mVar3 = new d.m();
            mVar3.d("playRacecar");
            mVar3.c("Play Race Car MP3");
            C1710b c1710b3 = new C1710b();
            File file2 = new File("./help/sound/racing01.mp3");
            c1710b3.setProperty("Sound_File_Path", file2.getAbsolutePath());
            mVar3.a(c1710b3);
            mVar3.a("playSoundFile");
            mVar3.b("Plays an MP3 file of a race car passing");
            if (file2.exists()) {
                arrayList.add(mVar3);
            }
        }
        d.m mVar4 = new d.m();
        mVar4.d("killEngine");
        mVar4.c("Stop Engine From Running");
        C1710b c1710b4 = new C1710b();
        c1710b4.setProperty("commandName", "cmdengineshutdown");
        mVar4.a(c1710b4);
        mVar4.a("sendControllerCommand");
        mVar4.b("Sends a Controller Command for MS3 to kill the engine.");
        if (rC != null && rC.O().b("cmdengineshutdown") != null) {
            arrayList.add(mVar4);
        }
        d.m mVar5 = new d.m();
        mVar5.d("showVeTable");
        mVar5.c("Show VE Table");
        C1710b c1710b5 = new C1710b();
        c1710b5.setProperty("settingsPanelName", "veTable1Tbl");
        mVar5.a(c1710b5);
        mVar5.a("showSettingsDialog");
        mVar5.b("Open VE Table 1");
        if (rC != null && rC.e().c("veTable1Tbl") != null) {
            arrayList.add(mVar5);
        }
        d.m mVar6 = new d.m();
        mVar6.d("startExplorer");
        mVar6.c("Open File Browser");
        C1710b c1710b6 = new C1710b();
        c1710b6.setProperty("Shell_Command", "explorer");
        mVar6.a(c1710b6);
        mVar6.a("shellCommand");
        mVar6.b("Shell a command to the OS to open a File Browser");
        if (bH.I.a()) {
            arrayList.add(mVar6);
        }
        d.m mVar7 = new d.m();
        mVar7.d("displayEngineStarted");
        mVar7.c("Display Engine Started");
        C1710b c1710b7 = new C1710b();
        c1710b7.setProperty("Message", "Engine Started");
        mVar7.a(c1710b7);
        mVar7.a("passiveMessage");
        mVar7.b("Displays the Message Engine Started in the lower left message area.");
        arrayList.add(mVar7);
        d.m mVar8 = new d.m();
        mVar8.d("syncLossMark");
        mVar8.c("Sync Loss Mark");
        C1710b c1710b8 = new C1710b();
        c1710b8.setProperty("MARK_Comment", "Lost Sync Increments");
        mVar8.a(c1710b8);
        mVar8.a("markDataLogComment");
        mVar8.b("Places a Sync Loss MARK in any active data log.");
        arrayList.add(mVar8);
        return arrayList;
    }

    public static List b() {
        G.R rC = T.a().c();
        ArrayList arrayList = new ArrayList();
        S.n nVar = new S.n();
        nVar.g("cltTempWarning");
        nVar.a(true);
        nVar.e("coolant > 230");
        nVar.f("coolant < 200");
        nVar.a(-1);
        nVar.j("highTempWarning");
        if (rC != null && rC.g("coolant") != null) {
            arrayList.add(nVar);
        }
        String property = System.getProperty("javafx.runtime.version");
        if (property != null && property.length() > 0) {
            S.n nVar2 = new S.n();
            nVar2.g("playRacecarWOT");
            nVar2.a(true);
            nVar2.e("tps > 92");
            nVar2.f("tps < 5");
            nVar2.a(-1);
            nVar2.j("playRacecar");
            if (rC != null && rC.g("tps") != null) {
                arrayList.add(nVar2);
            }
        }
        S.n nVar3 = new S.n();
        nVar3.g("syncLossDetect");
        nVar3.a(true);
        nVar3.e("synccnt > lastValue(synccnt)");
        nVar3.f("TimedResetSeconds:1");
        nVar3.a(1000);
        nVar3.j("syncLossMark");
        if (rC != null && rC.g("synccnt") != null) {
            arrayList.add(nVar3);
        }
        return arrayList;
    }
}
