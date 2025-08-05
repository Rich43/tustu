package jdk.nashorn.internal.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/DumpBytecode.class */
public final class DumpBytecode {
    public static void dumpBytecode(ScriptEnvironment env, DebugLogger logger, byte[] bytecode, String className) throws IOException {
        File file;
        File dir = null;
        try {
            if (env._print_code) {
                StringBuilder sb = new StringBuilder();
                sb.append("class: " + className).append('\n').append(ClassEmitter.disassemble(bytecode)).append("=====");
                if (env._print_code_dir != null) {
                    String name = className;
                    int dollar = name.lastIndexOf(36);
                    if (dollar != -1) {
                        name = name.substring(dollar + 1);
                    }
                    dir = new File(env._print_code_dir);
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new IOException(dir.toString());
                    }
                    int uniqueId = 0;
                    do {
                        file = new File(env._print_code_dir, name + (uniqueId == 0 ? "" : "_" + uniqueId) + ".bytecode");
                        uniqueId++;
                    } while (file.exists());
                    PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                    Throwable th = null;
                    try {
                        pw.print(sb.toString());
                        pw.flush();
                        if (pw != null) {
                            if (0 != 0) {
                                try {
                                    pw.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                pw.close();
                            }
                        }
                    } finally {
                    }
                } else {
                    env.getErr().println(sb);
                }
            }
            if (env._dest_dir != null) {
                String fileName = className.replace('.', File.separatorChar) + ".class";
                int index = fileName.lastIndexOf(File.separatorChar);
                if (index != -1) {
                    dir = new File(env._dest_dir, fileName.substring(0, index));
                } else {
                    dir = new File(env._dest_dir);
                }
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new IOException(dir.toString());
                }
                File file2 = new File(env._dest_dir, fileName);
                FileOutputStream fos = new FileOutputStream(file2);
                Throwable th3 = null;
                try {
                    try {
                        fos.write(bytecode);
                        if (fos != null) {
                            if (0 != 0) {
                                try {
                                    fos.close();
                                } catch (Throwable th4) {
                                    th3.addSuppressed(th4);
                                }
                            } else {
                                fos.close();
                            }
                        }
                        logger.info("Wrote class to '" + file2.getAbsolutePath() + '\'');
                    } finally {
                    }
                } catch (Throwable th5) {
                    th3 = th5;
                    throw th5;
                }
            }
        } catch (IOException e2) {
            logger.warning("Skipping class dump for ", className, ": ", ECMAErrors.getMessage("io.error.cant.write", dir.toString()));
        }
    }
}
