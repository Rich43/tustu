package az;

import bH.aa;

/* renamed from: az.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:az/c.class */
public class C0942c {

    /* renamed from: a, reason: collision with root package name */
    public static String f6468a = "Your Registration Successfully Activated. Thank You!";

    /* renamed from: b, reason: collision with root package name */
    public static String f6469b = "Valid Activation.";

    /* renamed from: c, reason: collision with root package name */
    public static String f6470c = "Invalid Activation.";

    /* renamed from: d, reason: collision with root package name */
    public static String f6471d = "Invalid Server Activation Code.";

    /* renamed from: e, reason: collision with root package name */
    public static String f6472e = "No identifiers available.";

    /* renamed from: f, reason: collision with root package name */
    public static String f6473f = "Unable to access hardware identifiers for this computer. Can not perform Activation.";

    /* renamed from: g, reason: collision with root package name */
    public static String f6474g = "OS X and Linux require a network interface to be installed even if the Internet is unavailable.";

    /* renamed from: h, reason: collision with root package name */
    public static String f6475h = "Unable to activate registration, failed to retrieve computer identification.";

    /* renamed from: i, reason: collision with root package name */
    public static String f6476i = "Unable to activate registration, failed to retrieve computer identification.";

    /* renamed from: j, reason: collision with root package name */
    public static String f6477j = "The Hardware Identifiers have changed since the registration was activated.\nInternet is needed to refresh Activation";

    /* renamed from: k, reason: collision with root package name */
    public static String f6478k = "Current Activation Count";

    /* renamed from: l, reason: collision with root package name */
    public static String f6479l = "An Offline Activation May be required, check help menu.";

    public static String a(aa aaVar) {
        return aaVar.a("Registration activation Failed, Network unavailable.") + "\n" + aaVar.a("Unable to connect to the Internet.");
    }

    public static String b(aa aaVar) {
        return aaVar.a("Registration activation Failed, Activation Server is unavailable.") + "\n" + aaVar.a("Try again later or perform an offline activation from the help menu.");
    }

    public static String c(aa aaVar) {
        return aaVar.a("You have reached the maximum number of activations for this registration.") + "\n" + aaVar.a("To activate more computers, you must purchase an additional registration or different class licensing.");
    }

    public static String d(aa aaVar) {
        return aaVar.a("Error: Either this computer is not connected to the Internet or there is a Firewall blocking Internet access.") + "\n\n" + aaVar.a("To activate your registration either") + ":\n\n- " + aaVar.a("Click Yes to connect to the Internet for seamless activation") + "\n" + aaVar.a("  Or") + "\n- " + aaVar.a("Click No to perform an offline activation.") + "\n  " + aaVar.a("This will require a USB drive or similar medium to save the activation file and a computer with Internet access.");
    }
}
