package java.awt.color;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import sun.java2d.cmm.CMSManager;
import sun.java2d.cmm.PCMM;
import sun.java2d.cmm.Profile;
import sun.java2d.cmm.ProfileActivator;
import sun.java2d.cmm.ProfileDataVerifier;
import sun.java2d.cmm.ProfileDeferralInfo;
import sun.java2d.cmm.ProfileDeferralMgr;
import sun.misc.IOUtils;

/* loaded from: rt.jar:java/awt/color/ICC_Profile.class */
public class ICC_Profile implements Serializable {
    private static final long serialVersionUID = -3938515861990936766L;
    private transient Profile cmmProfile;
    private transient ProfileDeferralInfo deferralInfo;
    private transient ProfileActivator profileActivator;
    private static ICC_Profile sRGBprofile;
    private static ICC_Profile XYZprofile;
    private static ICC_Profile PYCCprofile;
    private static ICC_Profile GRAYprofile;
    private static ICC_Profile LINEAR_RGBprofile;
    public static final int CLASS_INPUT = 0;
    public static final int CLASS_DISPLAY = 1;
    public static final int CLASS_OUTPUT = 2;
    public static final int CLASS_DEVICELINK = 3;
    public static final int CLASS_COLORSPACECONVERSION = 4;
    public static final int CLASS_ABSTRACT = 5;
    public static final int CLASS_NAMEDCOLOR = 6;
    public static final int icSigXYZData = 1482250784;
    public static final int icSigLabData = 1281450528;
    public static final int icSigLuvData = 1282766368;
    public static final int icSigYCbCrData = 1497588338;
    public static final int icSigYxyData = 1501067552;
    public static final int icSigRgbData = 1380401696;
    public static final int icSigGrayData = 1196573017;
    public static final int icSigHsvData = 1213421088;
    public static final int icSigHlsData = 1212961568;
    public static final int icSigCmykData = 1129142603;
    public static final int icSigCmyData = 1129142560;
    public static final int icSigSpace2CLR = 843271250;
    public static final int icSigSpace3CLR = 860048466;
    public static final int icSigSpace4CLR = 876825682;
    public static final int icSigSpace5CLR = 893602898;
    public static final int icSigSpace6CLR = 910380114;
    public static final int icSigSpace7CLR = 927157330;
    public static final int icSigSpace8CLR = 943934546;
    public static final int icSigSpace9CLR = 960711762;
    public static final int icSigSpaceACLR = 1094929490;
    public static final int icSigSpaceBCLR = 1111706706;
    public static final int icSigSpaceCCLR = 1128483922;
    public static final int icSigSpaceDCLR = 1145261138;
    public static final int icSigSpaceECLR = 1162038354;
    public static final int icSigSpaceFCLR = 1178815570;
    public static final int icSigInputClass = 1935896178;
    public static final int icSigDisplayClass = 1835955314;
    public static final int icSigOutputClass = 1886549106;
    public static final int icSigLinkClass = 1818848875;
    public static final int icSigAbstractClass = 1633842036;
    public static final int icSigColorSpaceClass = 1936744803;
    public static final int icSigNamedColorClass = 1852662636;
    public static final int icPerceptual = 0;
    public static final int icRelativeColorimetric = 1;
    public static final int icMediaRelativeColorimetric = 1;
    public static final int icSaturation = 2;
    public static final int icAbsoluteColorimetric = 3;
    public static final int icICCAbsoluteColorimetric = 3;
    public static final int icSigHead = 1751474532;
    public static final int icSigAToB0Tag = 1093812784;
    public static final int icSigAToB1Tag = 1093812785;
    public static final int icSigAToB2Tag = 1093812786;
    public static final int icSigBlueColorantTag = 1649957210;
    public static final int icSigBlueMatrixColumnTag = 1649957210;
    public static final int icSigBlueTRCTag = 1649693251;
    public static final int icSigBToA0Tag = 1110589744;
    public static final int icSigBToA1Tag = 1110589745;
    public static final int icSigBToA2Tag = 1110589746;
    public static final int icSigCalibrationDateTimeTag = 1667329140;
    public static final int icSigCharTargetTag = 1952543335;
    public static final int icSigCopyrightTag = 1668313716;
    public static final int icSigCrdInfoTag = 1668441193;
    public static final int icSigDeviceMfgDescTag = 1684893284;
    public static final int icSigDeviceModelDescTag = 1684890724;
    public static final int icSigDeviceSettingsTag = 1684371059;
    public static final int icSigGamutTag = 1734438260;
    public static final int icSigGrayTRCTag = 1800688195;
    public static final int icSigGreenColorantTag = 1733843290;
    public static final int icSigGreenMatrixColumnTag = 1733843290;
    public static final int icSigGreenTRCTag = 1733579331;
    public static final int icSigLuminanceTag = 1819635049;
    public static final int icSigMeasurementTag = 1835360627;
    public static final int icSigMediaBlackPointTag = 1651208308;
    public static final int icSigMediaWhitePointTag = 2004119668;
    public static final int icSigNamedColor2Tag = 1852009522;
    public static final int icSigOutputResponseTag = 1919251312;
    public static final int icSigPreview0Tag = 1886545200;
    public static final int icSigPreview1Tag = 1886545201;
    public static final int icSigPreview2Tag = 1886545202;
    public static final int icSigProfileDescriptionTag = 1684370275;
    public static final int icSigProfileSequenceDescTag = 1886610801;
    public static final int icSigPs2CRD0Tag = 1886610480;
    public static final int icSigPs2CRD1Tag = 1886610481;
    public static final int icSigPs2CRD2Tag = 1886610482;
    public static final int icSigPs2CRD3Tag = 1886610483;
    public static final int icSigPs2CSATag = 1886597747;
    public static final int icSigPs2RenderingIntentTag = 1886597737;
    public static final int icSigRedColorantTag = 1918392666;
    public static final int icSigRedMatrixColumnTag = 1918392666;
    public static final int icSigRedTRCTag = 1918128707;
    public static final int icSigScreeningDescTag = 1935897188;
    public static final int icSigScreeningTag = 1935897198;
    public static final int icSigTechnologyTag = 1952801640;
    public static final int icSigUcrBgTag = 1650877472;
    public static final int icSigViewingCondDescTag = 1987405156;
    public static final int icSigViewingConditionsTag = 1986618743;
    public static final int icSigChromaticityTag = 1667789421;
    public static final int icSigChromaticAdaptationTag = 1667785060;
    public static final int icSigColorantOrderTag = 1668051567;
    public static final int icSigColorantTableTag = 1668051572;
    public static final int icHdrSize = 0;
    public static final int icHdrCmmId = 4;
    public static final int icHdrVersion = 8;
    public static final int icHdrDeviceClass = 12;
    public static final int icHdrColorSpace = 16;
    public static final int icHdrPcs = 20;
    public static final int icHdrDate = 24;
    public static final int icHdrMagic = 36;
    public static final int icHdrPlatform = 40;
    public static final int icHdrFlags = 44;
    public static final int icHdrManufacturer = 48;
    public static final int icHdrModel = 52;
    public static final int icHdrAttributes = 56;
    public static final int icHdrRenderingIntent = 64;
    public static final int icHdrIlluminant = 68;
    public static final int icHdrCreator = 80;
    public static final int icHdrProfileID = 84;
    public static final int icTagType = 0;
    public static final int icTagReserved = 4;
    public static final int icCurveCount = 8;
    public static final int icCurveData = 12;
    public static final int icXYZNumberX = 8;
    private int iccProfileSerializedDataVersion;
    private transient ICC_Profile resolvedDeserializedProfile;

    ICC_Profile(Profile profile) {
        this.iccProfileSerializedDataVersion = 1;
        this.cmmProfile = profile;
    }

    ICC_Profile(ProfileDeferralInfo profileDeferralInfo) {
        this.iccProfileSerializedDataVersion = 1;
        this.deferralInfo = profileDeferralInfo;
        this.profileActivator = new ProfileActivator() { // from class: java.awt.color.ICC_Profile.1
            @Override // sun.java2d.cmm.ProfileActivator
            public void activate() throws ProfileDataException {
                ICC_Profile.this.activateDeferredProfile();
            }
        };
        ProfileDeferralMgr.registerDeferral(this.profileActivator);
    }

    protected void finalize() {
        if (this.cmmProfile != null) {
            CMSManager.getModule().freeProfile(this.cmmProfile);
        } else if (this.profileActivator != null) {
            ProfileDeferralMgr.unregisterDeferral(this.profileActivator);
        }
    }

    public static ICC_Profile getInstance(byte[] bArr) {
        ICC_Profile iCC_Profile;
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
        ProfileDataVerifier.verify(bArr);
        try {
            Profile profileLoadProfile = CMSManager.getModule().loadProfile(bArr);
            try {
                if (getColorSpaceType(profileLoadProfile) == 6 && getData(profileLoadProfile, icSigMediaWhitePointTag) != null && getData(profileLoadProfile, icSigGrayTRCTag) != null) {
                    iCC_Profile = new ICC_ProfileGray(profileLoadProfile);
                } else if (getColorSpaceType(profileLoadProfile) == 5 && getData(profileLoadProfile, icSigMediaWhitePointTag) != null && getData(profileLoadProfile, 1918392666) != null && getData(profileLoadProfile, 1733843290) != null && getData(profileLoadProfile, 1649957210) != null && getData(profileLoadProfile, icSigRedTRCTag) != null && getData(profileLoadProfile, icSigGreenTRCTag) != null && getData(profileLoadProfile, icSigBlueTRCTag) != null) {
                    iCC_Profile = new ICC_ProfileRGB(profileLoadProfile);
                } else {
                    iCC_Profile = new ICC_Profile(profileLoadProfile);
                }
            } catch (CMMException e2) {
                iCC_Profile = new ICC_Profile(profileLoadProfile);
            }
            return iCC_Profile;
        } catch (CMMException e3) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
    }

    public static ICC_Profile getInstance(int i2) {
        ICC_Profile iCC_Profile;
        switch (i2) {
            case 1000:
                synchronized (ICC_Profile.class) {
                    if (sRGBprofile == null) {
                        sRGBprofile = getDeferredInstance(new ProfileDeferralInfo("sRGB.pf", 5, 3, 1));
                    }
                    iCC_Profile = sRGBprofile;
                }
                break;
            case 1001:
                synchronized (ICC_Profile.class) {
                    if (XYZprofile == null) {
                        XYZprofile = getDeferredInstance(new ProfileDeferralInfo("CIEXYZ.pf", 0, 3, 1));
                    }
                    iCC_Profile = XYZprofile;
                }
                break;
            case 1002:
                synchronized (ICC_Profile.class) {
                    if (PYCCprofile == null) {
                        if (standardProfileExists("PYCC.pf")) {
                            PYCCprofile = getDeferredInstance(new ProfileDeferralInfo("PYCC.pf", 13, 3, 1));
                        } else {
                            throw new IllegalArgumentException("Can't load standard profile: PYCC.pf");
                        }
                    }
                    iCC_Profile = PYCCprofile;
                }
                break;
            case 1003:
                synchronized (ICC_Profile.class) {
                    if (GRAYprofile == null) {
                        GRAYprofile = getDeferredInstance(new ProfileDeferralInfo("GRAY.pf", 6, 1, 1));
                    }
                    iCC_Profile = GRAYprofile;
                }
                break;
            case 1004:
                synchronized (ICC_Profile.class) {
                    if (LINEAR_RGBprofile == null) {
                        LINEAR_RGBprofile = getDeferredInstance(new ProfileDeferralInfo("LINEAR_RGB.pf", 5, 3, 1));
                    }
                    iCC_Profile = LINEAR_RGBprofile;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown color space");
        }
        return iCC_Profile;
    }

    private static ICC_Profile getStandardProfile(final String str) {
        return (ICC_Profile) AccessController.doPrivileged(new PrivilegedAction<ICC_Profile>() { // from class: java.awt.color.ICC_Profile.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ICC_Profile run() {
                try {
                    return ICC_Profile.getInstance(str);
                } catch (IOException e2) {
                    throw new IllegalArgumentException("Can't load standard profile: " + str);
                }
            }
        });
    }

    public static ICC_Profile getInstance(String str) throws IOException {
        FileInputStream fileInputStream = null;
        File profileFile = getProfileFile(str);
        if (profileFile != null) {
            fileInputStream = new FileInputStream(profileFile);
        }
        if (fileInputStream == null) {
            throw new IOException("Cannot open file " + str);
        }
        ICC_Profile iCC_Profile = getInstance(fileInputStream);
        fileInputStream.close();
        return iCC_Profile;
    }

    public static ICC_Profile getInstance(InputStream inputStream) throws IOException {
        if (inputStream instanceof ProfileDeferralInfo) {
            return getDeferredInstance((ProfileDeferralInfo) inputStream);
        }
        byte[] profileDataFromStream = getProfileDataFromStream(inputStream);
        if (profileDataFromStream == null) {
            throw new IllegalArgumentException("Invalid ICC Profile Data");
        }
        return getInstance(profileDataFromStream);
    }

    static byte[] getProfileDataFromStream(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(128);
        byte[] nBytes = IOUtils.readNBytes(bufferedInputStream, 128);
        if (nBytes.length < 128 || nBytes[36] != 97 || nBytes[37] != 99 || nBytes[38] != 115 || nBytes[39] != 112) {
            return null;
        }
        int i2 = ((nBytes[0] & 255) << 24) | ((nBytes[1] & 255) << 16) | ((nBytes[2] & 255) << 8) | (nBytes[3] & 255);
        bufferedInputStream.reset();
        try {
            return IOUtils.readNBytes(bufferedInputStream, i2);
        } catch (OutOfMemoryError e2) {
            throw new IOException("Color profile is too big");
        }
    }

    static ICC_Profile getDeferredInstance(ProfileDeferralInfo profileDeferralInfo) {
        if (!ProfileDeferralMgr.deferring) {
            return getStandardProfile(profileDeferralInfo.filename);
        }
        if (profileDeferralInfo.colorSpaceType == 5) {
            return new ICC_ProfileRGB(profileDeferralInfo);
        }
        if (profileDeferralInfo.colorSpaceType == 6) {
            return new ICC_ProfileGray(profileDeferralInfo);
        }
        return new ICC_Profile(profileDeferralInfo);
    }

    void activateDeferredProfile() throws ProfileDataException {
        final String str = this.deferralInfo.filename;
        this.profileActivator = null;
        this.deferralInfo = null;
        FileInputStream fileInputStream = (FileInputStream) AccessController.doPrivileged(new PrivilegedAction<FileInputStream>() { // from class: java.awt.color.ICC_Profile.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public FileInputStream run() {
                File standardProfileFile = ICC_Profile.getStandardProfileFile(str);
                if (standardProfileFile != null) {
                    try {
                        return new FileInputStream(standardProfileFile);
                    } catch (FileNotFoundException e2) {
                        return null;
                    }
                }
                return null;
            }
        });
        if (fileInputStream == null) {
            throw new ProfileDataException("Cannot open file " + str);
        }
        try {
            byte[] profileDataFromStream = getProfileDataFromStream(fileInputStream);
            fileInputStream.close();
            if (profileDataFromStream == null) {
                throw new ProfileDataException("Invalid ICC Profile Data" + str);
            }
            try {
                this.cmmProfile = CMSManager.getModule().loadProfile(profileDataFromStream);
            } catch (CMMException e2) {
                ProfileDataException profileDataException = new ProfileDataException("Invalid ICC Profile Data" + str);
                profileDataException.initCause(e2);
                throw profileDataException;
            }
        } catch (IOException e3) {
            ProfileDataException profileDataException2 = new ProfileDataException("Invalid ICC Profile Data" + str);
            profileDataException2.initCause(e3);
            throw profileDataException2;
        }
    }

    public int getMajorVersion() {
        return getData(1751474532)[8];
    }

    public int getMinorVersion() {
        return getData(1751474532)[9];
    }

    public int getProfileClass() {
        int i2;
        if (this.deferralInfo != null) {
            return this.deferralInfo.profileClass;
        }
        switch (intFromBigEndian(getData(1751474532), 12)) {
            case icSigAbstractClass /* 1633842036 */:
                i2 = 5;
                break;
            case icSigLinkClass /* 1818848875 */:
                i2 = 3;
                break;
            case icSigDisplayClass /* 1835955314 */:
                i2 = 1;
                break;
            case icSigNamedColorClass /* 1852662636 */:
                i2 = 6;
                break;
            case icSigOutputClass /* 1886549106 */:
                i2 = 2;
                break;
            case icSigInputClass /* 1935896178 */:
                i2 = 0;
                break;
            case icSigColorSpaceClass /* 1936744803 */:
                i2 = 4;
                break;
            default:
                throw new IllegalArgumentException("Unknown profile class");
        }
        return i2;
    }

    public int getColorSpaceType() {
        if (this.deferralInfo != null) {
            return this.deferralInfo.colorSpaceType;
        }
        return getColorSpaceType(this.cmmProfile);
    }

    static int getColorSpaceType(Profile profile) {
        return iccCStoJCS(intFromBigEndian(getData(profile, 1751474532), 16));
    }

    public int getPCSType() {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
        return getPCSType(this.cmmProfile);
    }

    static int getPCSType(Profile profile) {
        return iccCStoJCS(intFromBigEndian(getData(profile, 1751474532), 20));
    }

    public void write(String str) throws IOException {
        byte[] data = getData();
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }

    public void write(OutputStream outputStream) throws IOException {
        outputStream.write(getData());
    }

    public byte[] getData() {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
        PCMM module = CMSManager.getModule();
        byte[] bArr = new byte[module.getProfileSize(this.cmmProfile)];
        module.getProfileData(this.cmmProfile, bArr);
        return bArr;
    }

    public byte[] getData(int i2) {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
        return getData(this.cmmProfile, i2);
    }

    static byte[] getData(Profile profile, int i2) {
        byte[] bArr;
        try {
            PCMM module = CMSManager.getModule();
            bArr = new byte[module.getTagSize(profile, i2)];
            module.getTagData(profile, i2, bArr);
        } catch (CMMException e2) {
            bArr = null;
        }
        return bArr;
    }

    public void setData(int i2, byte[] bArr) {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
        CMSManager.getModule().setTagData(this.cmmProfile, i2, bArr);
    }

    void setRenderingIntent(int i2) {
        byte[] data = getData(1751474532);
        intToBigEndian(i2, data, 64);
        setData(1751474532, data);
    }

    int getRenderingIntent() {
        return 65535 & intFromBigEndian(getData(1751474532), 64);
    }

    public int getNumComponents() {
        int i2;
        if (this.deferralInfo != null) {
            return this.deferralInfo.numComponents;
        }
        switch (intFromBigEndian(getData(1751474532), 16)) {
            case icSigSpace2CLR /* 843271250 */:
                i2 = 2;
                break;
            case icSigSpace3CLR /* 860048466 */:
            case icSigCmyData /* 1129142560 */:
            case icSigHlsData /* 1212961568 */:
            case icSigHsvData /* 1213421088 */:
            case icSigLabData /* 1281450528 */:
            case icSigLuvData /* 1282766368 */:
            case icSigRgbData /* 1380401696 */:
            case icSigXYZData /* 1482250784 */:
            case icSigYCbCrData /* 1497588338 */:
            case icSigYxyData /* 1501067552 */:
                i2 = 3;
                break;
            case icSigSpace4CLR /* 876825682 */:
            case icSigCmykData /* 1129142603 */:
                i2 = 4;
                break;
            case icSigSpace5CLR /* 893602898 */:
                i2 = 5;
                break;
            case icSigSpace6CLR /* 910380114 */:
                i2 = 6;
                break;
            case icSigSpace7CLR /* 927157330 */:
                i2 = 7;
                break;
            case icSigSpace8CLR /* 943934546 */:
                i2 = 8;
                break;
            case icSigSpace9CLR /* 960711762 */:
                i2 = 9;
                break;
            case icSigSpaceACLR /* 1094929490 */:
                i2 = 10;
                break;
            case icSigSpaceBCLR /* 1111706706 */:
                i2 = 11;
                break;
            case icSigSpaceCCLR /* 1128483922 */:
                i2 = 12;
                break;
            case icSigSpaceDCLR /* 1145261138 */:
                i2 = 13;
                break;
            case icSigSpaceECLR /* 1162038354 */:
                i2 = 14;
                break;
            case icSigSpaceFCLR /* 1178815570 */:
                i2 = 15;
                break;
            case icSigGrayData /* 1196573017 */:
                i2 = 1;
                break;
            default:
                throw new ProfileDataException("invalid ICC color space");
        }
        return i2;
    }

    float[] getMediaWhitePoint() {
        return getXYZTag(icSigMediaWhitePointTag);
    }

    float[] getXYZTag(int i2) {
        byte[] data = getData(i2);
        float[] fArr = new float[3];
        int i3 = 0;
        int i4 = 8;
        while (i3 < 3) {
            fArr[i3] = intFromBigEndian(data, i4) / 65536.0f;
            i3++;
            i4 += 4;
        }
        return fArr;
    }

    float getGamma(int i2) {
        if (intFromBigEndian(getData(i2), 8) != 1) {
            throw new ProfileDataException("TRC is not a gamma");
        }
        return (shortFromBigEndian(r0, 12) & 65535) / 256.0f;
    }

    short[] getTRC(int i2) {
        byte[] data = getData(i2);
        int iIntFromBigEndian = intFromBigEndian(data, 8);
        if (iIntFromBigEndian == 1) {
            throw new ProfileDataException("TRC is not a table");
        }
        short[] sArr = new short[iIntFromBigEndian];
        int i3 = 0;
        int i4 = 12;
        while (i3 < iIntFromBigEndian) {
            sArr[i3] = shortFromBigEndian(data, i4);
            i3++;
            i4 += 2;
        }
        return sArr;
    }

    static int iccCStoJCS(int i2) {
        int i3;
        switch (i2) {
            case icSigSpace2CLR /* 843271250 */:
                i3 = 12;
                break;
            case icSigSpace3CLR /* 860048466 */:
                i3 = 13;
                break;
            case icSigSpace4CLR /* 876825682 */:
                i3 = 14;
                break;
            case icSigSpace5CLR /* 893602898 */:
                i3 = 15;
                break;
            case icSigSpace6CLR /* 910380114 */:
                i3 = 16;
                break;
            case icSigSpace7CLR /* 927157330 */:
                i3 = 17;
                break;
            case icSigSpace8CLR /* 943934546 */:
                i3 = 18;
                break;
            case icSigSpace9CLR /* 960711762 */:
                i3 = 19;
                break;
            case icSigSpaceACLR /* 1094929490 */:
                i3 = 20;
                break;
            case icSigSpaceBCLR /* 1111706706 */:
                i3 = 21;
                break;
            case icSigSpaceCCLR /* 1128483922 */:
                i3 = 22;
                break;
            case icSigCmyData /* 1129142560 */:
                i3 = 11;
                break;
            case icSigCmykData /* 1129142603 */:
                i3 = 9;
                break;
            case icSigSpaceDCLR /* 1145261138 */:
                i3 = 23;
                break;
            case icSigSpaceECLR /* 1162038354 */:
                i3 = 24;
                break;
            case icSigSpaceFCLR /* 1178815570 */:
                i3 = 25;
                break;
            case icSigGrayData /* 1196573017 */:
                i3 = 6;
                break;
            case icSigHlsData /* 1212961568 */:
                i3 = 8;
                break;
            case icSigHsvData /* 1213421088 */:
                i3 = 7;
                break;
            case icSigLabData /* 1281450528 */:
                i3 = 1;
                break;
            case icSigLuvData /* 1282766368 */:
                i3 = 2;
                break;
            case icSigRgbData /* 1380401696 */:
                i3 = 5;
                break;
            case icSigXYZData /* 1482250784 */:
                i3 = 0;
                break;
            case icSigYCbCrData /* 1497588338 */:
                i3 = 3;
                break;
            case icSigYxyData /* 1501067552 */:
                i3 = 4;
                break;
            default:
                throw new IllegalArgumentException("Unknown color space");
        }
        return i3;
    }

    static int intFromBigEndian(byte[] bArr, int i2) {
        return ((bArr[i2] & 255) << 24) | ((bArr[i2 + 1] & 255) << 16) | ((bArr[i2 + 2] & 255) << 8) | (bArr[i2 + 3] & 255);
    }

    static void intToBigEndian(int i2, byte[] bArr, int i3) {
        bArr[i3] = (byte) (i2 >> 24);
        bArr[i3 + 1] = (byte) (i2 >> 16);
        bArr[i3 + 2] = (byte) (i2 >> 8);
        bArr[i3 + 3] = (byte) i2;
    }

    static short shortFromBigEndian(byte[] bArr, int i2) {
        return (short) (((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255));
    }

    static void shortToBigEndian(short s2, byte[] bArr, int i2) {
        bArr[i2] = (byte) (s2 >> 8);
        bArr[i2 + 1] = (byte) s2;
    }

    private static File getProfileFile(String str) {
        String property;
        String property2;
        File file = new File(str);
        if (file.isAbsolute()) {
            if (file.isFile()) {
                return file;
            }
            return null;
        }
        if (!file.isFile() && (property2 = System.getProperty("java.iccprofile.path")) != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property2, File.pathSeparator);
            while (stringTokenizer.hasMoreTokens() && (file == null || !file.isFile())) {
                String strNextToken = stringTokenizer.nextToken();
                file = new File(strNextToken + File.separatorChar + str);
                if (!isChildOf(file, strNextToken)) {
                    file = null;
                }
            }
        }
        if ((file == null || !file.isFile()) && (property = System.getProperty("java.class.path")) != null) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(property, File.pathSeparator);
            while (stringTokenizer2.hasMoreTokens() && (file == null || !file.isFile())) {
                file = new File(stringTokenizer2.nextToken() + File.separatorChar + str);
            }
        }
        if (file == null || !file.isFile()) {
            file = getStandardProfileFile(str);
        }
        if (file != null && file.isFile()) {
            return file;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getStandardProfileFile(String str) {
        String str2 = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "cmm";
        File file = new File(str2 + File.separatorChar + str);
        if (file.isFile() && isChildOf(file, str2)) {
            return file;
        }
        return null;
    }

    private static boolean isChildOf(File file, String str) {
        try {
            String canonicalPath = new File(str).getCanonicalPath();
            if (!canonicalPath.endsWith(File.separator)) {
                canonicalPath = canonicalPath + File.separator;
            }
            return file.getCanonicalPath().startsWith(canonicalPath);
        } catch (IOException e2) {
            return false;
        }
    }

    private static boolean standardProfileExists(final String str) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.awt.color.ICC_Profile.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                return Boolean.valueOf(ICC_Profile.getStandardProfileFile(str) != null);
            }
        })).booleanValue();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Object obj = null;
        if (this == sRGBprofile) {
            obj = "CS_sRGB";
        } else if (this == XYZprofile) {
            obj = "CS_CIEXYZ";
        } else if (this == PYCCprofile) {
            obj = "CS_PYCC";
        } else if (this == GRAYprofile) {
            obj = "CS_GRAY";
        } else if (this == LINEAR_RGBprofile) {
            obj = "CS_LINEAR_RGB";
        }
        byte[] data = null;
        if (obj == null) {
            data = getData();
        }
        objectOutputStream.writeObject(obj);
        objectOutputStream.writeObject(data);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        String str = (String) objectInputStream.readObject();
        byte[] bArr = (byte[]) objectInputStream.readObject();
        int i2 = 0;
        boolean z2 = false;
        if (str != null) {
            z2 = true;
            if (str.equals("CS_sRGB")) {
                i2 = 1000;
            } else if (str.equals("CS_CIEXYZ")) {
                i2 = 1001;
            } else if (str.equals("CS_PYCC")) {
                i2 = 1002;
            } else if (str.equals("CS_GRAY")) {
                i2 = 1003;
            } else if (str.equals("CS_LINEAR_RGB")) {
                i2 = 1004;
            } else {
                z2 = false;
            }
        }
        if (z2) {
            this.resolvedDeserializedProfile = getInstance(i2);
        } else {
            this.resolvedDeserializedProfile = getInstance(bArr);
        }
    }

    protected Object readResolve() throws ObjectStreamException {
        return this.resolvedDeserializedProfile;
    }
}
