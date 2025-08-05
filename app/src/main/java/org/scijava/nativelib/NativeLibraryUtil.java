package org.scijava.nativelib;

import com.intel.bluetooth.BlueCoveImpl;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.locale.LanguageTag;

/* loaded from: jssc.jar:org/scijava/nativelib/NativeLibraryUtil.class */
public class NativeLibraryUtil {
    public static final String DELIM = "/";
    public static final String DEFAULT_SEARCH_PATH = "natives/";
    private static Architecture architecture = Architecture.UNKNOWN;
    private static String archStr = null;
    private static final Logger LOGGER = LoggerFactory.getLogger("org.scijava.nativelib.NativeLibraryUtil");

    /* loaded from: jssc.jar:org/scijava/nativelib/NativeLibraryUtil$Architecture.class */
    public enum Architecture {
        UNKNOWN,
        LINUX_32,
        LINUX_64,
        LINUX_ARM,
        LINUX_ARM64,
        WINDOWS_32,
        WINDOWS_64,
        OSX_32,
        OSX_64,
        OSX_PPC,
        OSX_ARM64,
        AIX_32,
        AIX_64
    }

    /* loaded from: jssc.jar:org/scijava/nativelib/NativeLibraryUtil$Processor.class */
    private enum Processor {
        UNKNOWN,
        INTEL_32,
        INTEL_64,
        PPC,
        PPC_64,
        ARM,
        AARCH_64
    }

    public static Architecture getArchitecture() {
        Processor processor;
        if (Architecture.UNKNOWN == architecture && Processor.UNKNOWN != (processor = getProcessor())) {
            String name = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
            if (name.contains("nix") || name.contains("nux")) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.LINUX_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.LINUX_64;
                } else if (Processor.ARM == processor) {
                    architecture = Architecture.LINUX_ARM;
                } else if (Processor.AARCH_64 == processor) {
                    architecture = Architecture.LINUX_ARM64;
                }
            } else if (name.contains("aix")) {
                if (Processor.PPC == processor) {
                    architecture = Architecture.AIX_32;
                } else if (Processor.PPC_64 == processor) {
                    architecture = Architecture.AIX_64;
                }
            } else if (name.contains("win")) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.WINDOWS_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.WINDOWS_64;
                }
            } else if (name.contains(BlueCoveImpl.STACK_OSX)) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.OSX_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.OSX_64;
                } else if (Processor.AARCH_64 == processor) {
                    architecture = Architecture.OSX_ARM64;
                } else if (Processor.PPC == processor) {
                    architecture = Architecture.OSX_PPC;
                }
            }
        }
        LOGGER.debug("architecture is " + ((Object) architecture) + " os.name is " + System.getProperty("os.name").toLowerCase(Locale.ENGLISH));
        return architecture;
    }

    private static Processor getProcessor() {
        Processor processor = Processor.UNKNOWN;
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
        if (arch.contains("arm")) {
            processor = Processor.ARM;
        } else if (arch.contains("aarch64")) {
            processor = Processor.AARCH_64;
        } else if (arch.contains("ppc")) {
            int bits = 32;
            if (arch.contains("64")) {
                bits = 64;
            }
            processor = 32 == bits ? Processor.PPC : Processor.PPC_64;
        } else if (arch.contains("86") || arch.contains("amd")) {
            int bits2 = 32;
            if (arch.contains("64")) {
                bits2 = 64;
            }
            processor = 32 == bits2 ? Processor.INTEL_32 : Processor.INTEL_64;
        }
        LOGGER.debug("processor is " + ((Object) processor) + " os.arch is " + System.getProperty("os.arch").toLowerCase(Locale.ENGLISH));
        return processor;
    }

    public static String getPlatformLibraryPath(String searchPath) {
        if (archStr == null) {
            archStr = getArchitecture().name().toLowerCase(Locale.ENGLISH);
        }
        String fullSearchPath = ((searchPath.equals("") || searchPath.endsWith("/")) ? searchPath : searchPath + "/") + archStr + "/";
        LOGGER.debug("platform specific path is " + fullSearchPath);
        return fullSearchPath;
    }

    public static String getPlatformLibraryName(String libName) {
        String name = null;
        switch (getArchitecture()) {
            case AIX_32:
            case AIX_64:
            case LINUX_32:
            case LINUX_64:
            case LINUX_ARM:
            case LINUX_ARM64:
                name = "lib" + libName + ".so";
                break;
            case WINDOWS_32:
            case WINDOWS_64:
                name = libName + ".dll";
                break;
            case OSX_32:
            case OSX_64:
            case OSX_ARM64:
                name = "lib" + libName + ".dylib";
                break;
        }
        LOGGER.debug("native library name " + name);
        return name;
    }

    public static String getVersionedLibraryName(Class<?> libraryJarClass, String libName) {
        String version = libraryJarClass.getPackage().getImplementationVersion();
        if (null != version && version.length() > 0) {
            libName = libName + LanguageTag.SEP + version;
        }
        return libName;
    }

    public static boolean loadVersionedNativeLibrary(Class<?> libraryJarClass, String libName) {
        return loadNativeLibrary(libraryJarClass, getVersionedLibraryName(libraryJarClass, libName));
    }

    public static boolean loadNativeLibrary(JniExtractor jniExtractor, String libName, String... searchPaths) {
        if (Architecture.UNKNOWN == getArchitecture()) {
            LOGGER.warn("No native library available for this platform.");
            return false;
        }
        try {
            List<String> libPaths = searchPaths == null ? new LinkedList<>() : new LinkedList<>(Arrays.asList(searchPaths));
            libPaths.add(0, DEFAULT_SEARCH_PATH);
            libPaths.add(1, "");
            libPaths.add(2, "META-INF/lib");
            for (String libPath : libPaths) {
                File extracted = jniExtractor.extractJni(getPlatformLibraryPath(libPath), libName);
                if (extracted != null) {
                    System.load(extracted.getAbsolutePath());
                    return true;
                }
            }
            return false;
        } catch (IOException e2) {
            LOGGER.debug("Problem with extracting the library", (Throwable) e2);
            return false;
        } catch (UnsatisfiedLinkError e3) {
            LOGGER.debug("Problem with library", (Throwable) e3);
            return false;
        }
    }

    @Deprecated
    public static boolean loadNativeLibrary(Class<?> libraryJarClass, String libName) {
        try {
            return loadNativeLibrary(new DefaultJniExtractor(libraryJarClass), libName, new String[0]);
        } catch (IOException e2) {
            LOGGER.debug("IOException creating DefaultJniExtractor", (Throwable) e2);
            return false;
        }
    }
}
