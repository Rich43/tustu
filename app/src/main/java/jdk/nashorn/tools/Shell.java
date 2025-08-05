package jdk.nashorn.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.debug.ASTWriter;
import jdk.nashorn.internal.ir.debug.PrintVisitor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;
import sun.security.validator.Validator;
import sun.util.locale.LanguageTag;

/* loaded from: nashorn.jar:jdk/nashorn/tools/Shell.class */
public class Shell {
    private static final String MESSAGE_RESOURCE = "jdk.nashorn.tools.resources.Shell";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, Locale.getDefault());
    public static final int SUCCESS = 0;
    public static final int COMMANDLINE_ERROR = 100;
    public static final int COMPILATION_ERROR = 101;
    public static final int RUNTIME_ERROR = 102;
    public static final int IO_ERROR = 103;
    public static final int INTERNAL_ERROR = 104;

    protected Shell() {
    }

    public static void main(String[] args) {
        try {
            int exitCode = main(System.in, System.out, System.err, args);
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        } catch (IOException e2) {
            System.err.println(e2);
            System.exit(103);
        }
    }

    public static int main(InputStream in, OutputStream out, OutputStream err, String[] args) throws IOException {
        return new Shell().run(in, out, err, args);
    }

    protected final int run(InputStream in, OutputStream out, OutputStream err, String[] args) throws IOException {
        Context context = makeContext(in, out, err, args);
        if (context == null) {
            return 100;
        }
        Global global = context.createGlobal();
        ScriptEnvironment env = context.getEnv();
        List<String> files = env.getFiles();
        if (files.isEmpty()) {
            return readEvalPrint(context, global);
        }
        if (env._compile_only) {
            return compileScripts(context, global, files);
        }
        if (env._fx) {
            return runFXScripts(context, global, files);
        }
        return runScripts(context, global, files);
    }

    private static Context makeContext(InputStream in, OutputStream out, OutputStream err, String[] args) {
        PrintStream pout = out instanceof PrintStream ? (PrintStream) out : new PrintStream(out);
        PrintStream perr = err instanceof PrintStream ? (PrintStream) err : new PrintStream(err);
        PrintWriter wout = new PrintWriter((OutputStream) pout, true);
        PrintWriter werr = new PrintWriter((OutputStream) perr, true);
        ErrorManager errors = new ErrorManager(werr);
        Options options = new Options("nashorn", werr);
        if (args != null) {
            try {
                String[] prepArgs = preprocessArgs(args);
                options.process(prepArgs);
            } catch (IllegalArgumentException e2) {
                werr.println(bundle.getString("shell.usage"));
                options.displayHelp(e2);
                return null;
            }
        }
        if (!options.getBoolean("scripting")) {
            for (String fileName : options.getFiles()) {
                File firstFile = new File(fileName);
                if (firstFile.isFile()) {
                    try {
                        FileReader fr = new FileReader(firstFile);
                        Throwable th = null;
                        try {
                            try {
                                int firstChar = fr.read();
                                if (firstChar == 35) {
                                    options.set("scripting", true);
                                    if (fr != null) {
                                        if (0 != 0) {
                                            try {
                                                fr.close();
                                            } catch (Throwable th2) {
                                                th.addSuppressed(th2);
                                            }
                                        } else {
                                            fr.close();
                                        }
                                    }
                                    break;
                                }
                                if (fr != null) {
                                    if (0 != 0) {
                                        try {
                                            fr.close();
                                        } catch (Throwable th3) {
                                            th.addSuppressed(th3);
                                        }
                                    } else {
                                        fr.close();
                                    }
                                }
                            } finally {
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            throw th4;
                        }
                    } catch (IOException e3) {
                    }
                }
            }
        }
        return new Context(options, errors, wout, werr, Thread.currentThread().getContextClassLoader());
    }

    private static String[] preprocessArgs(String[] args) {
        if (args.length == 0) {
            return args;
        }
        List<String> processedArgs = new ArrayList<>();
        processedArgs.addAll(Arrays.asList(args));
        if (args[0].startsWith(LanguageTag.SEP) && !System.getProperty("os.name", Validator.VAR_GENERIC).startsWith("Mac OS X")) {
            processedArgs.addAll(0, ScriptingFunctions.tokenizeString(processedArgs.remove(0)));
        }
        int shebangFilePos = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= processedArgs.size()) {
                break;
            }
            String a2 = processedArgs.get(i2);
            if (a2.startsWith(LanguageTag.SEP)) {
                i2++;
            } else {
                Path p2 = Paths.get(a2, new String[0]);
                String l2 = "";
                try {
                    BufferedReader r2 = Files.newBufferedReader(p2);
                    Throwable th = null;
                    try {
                        try {
                            l2 = r2.readLine();
                            if (r2 != null) {
                                if (0 != 0) {
                                    try {
                                        r2.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    r2.close();
                                }
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (IOException e2) {
                }
                if (l2 != null && l2.startsWith("#!")) {
                    shebangFilePos = i2;
                }
            }
        }
        if (shebangFilePos != -1) {
            processedArgs.add(shebangFilePos + 1, "--");
        }
        return (String[]) processedArgs.toArray(new String[0]);
    }

    private static int compileScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        ScriptEnvironment env = context.getEnv();
        if (globalChanged) {
            try {
                Context.setGlobal(global);
            } finally {
                env.getOut().flush();
                env.getErr().flush();
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
            }
        }
        ErrorManager errors = context.getErrorManager();
        for (String fileName : files) {
            FunctionNode functionNode = new Parser(env, Source.sourceFor(fileName, new File(fileName)), errors, env._strict, 0, context.getLogger(Parser.class)).parse();
            if (errors.getNumberOfErrors() != 0) {
                env.getOut().flush();
                env.getErr().flush();
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                return 101;
            }
            Compiler.forNoInstallerCompilation(context, functionNode.getSource(), env._strict | functionNode.isStrict()).compile(functionNode, Compiler.CompilationPhases.COMPILE_ALL_NO_INSTALL);
            if (env._print_ast) {
                context.getErr().println(new ASTWriter(functionNode));
            }
            if (env._print_parse) {
                context.getErr().println(new PrintVisitor(functionNode));
            }
            if (errors.getNumberOfErrors() != 0) {
                return 101;
            }
        }
        env.getOut().flush();
        env.getErr().flush();
        if (!globalChanged) {
            return 0;
        }
        Context.setGlobal(oldGlobal);
        return 0;
    }

    private int runScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        if (globalChanged) {
            try {
                Context.setGlobal(global);
            } catch (Throwable th) {
                context.getOut().flush();
                context.getErr().flush();
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                throw th;
            }
        }
        ErrorManager errors = context.getErrorManager();
        for (String fileName : files) {
            if (LanguageTag.SEP.equals(fileName)) {
                int res = readEvalPrint(context, global);
                if (res != 0) {
                    context.getOut().flush();
                    context.getErr().flush();
                    if (globalChanged) {
                        Context.setGlobal(oldGlobal);
                    }
                    return res;
                }
            } else {
                File file = new File(fileName);
                ScriptFunction script = context.compileScript(Source.sourceFor(fileName, file), global);
                if (script == null || errors.getNumberOfErrors() != 0) {
                    context.getOut().flush();
                    context.getErr().flush();
                    if (globalChanged) {
                        Context.setGlobal(oldGlobal);
                    }
                    return 101;
                }
                try {
                    apply(script, global);
                } catch (NashornException e2) {
                    errors.error(e2.toString());
                    if (context.getEnv()._dump_on_error) {
                        e2.printStackTrace(context.getErr());
                    }
                    context.getOut().flush();
                    context.getErr().flush();
                    if (globalChanged) {
                        Context.setGlobal(oldGlobal);
                    }
                    return 102;
                }
            }
        }
        context.getOut().flush();
        context.getErr().flush();
        if (!globalChanged) {
            return 0;
        }
        Context.setGlobal(oldGlobal);
        return 0;
    }

    private static int runFXScripts(Context context, Global global, List<String> files) throws IOException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                try {
                    Context.setGlobal(global);
                } catch (NashornException e2) {
                    context.getErrorManager().error(e2.toString());
                    if (context.getEnv()._dump_on_error) {
                        e2.printStackTrace(context.getErr());
                    }
                    context.getOut().flush();
                    context.getErr().flush();
                    if (globalChanged) {
                        Context.setGlobal(oldGlobal);
                    }
                    return 102;
                }
            }
            global.addOwnProperty("$GLOBAL", 2, global);
            global.addOwnProperty("$SCRIPTS", 2, files);
            context.load(global, "fx:bootstrap.js");
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
                return 0;
            }
            return 0;
        } catch (Throwable th) {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
            throw th;
        }
    }

    protected Object apply(ScriptFunction target, Object self) {
        return ScriptRuntime.apply(target, self, new Object[0]);
    }

    private static int readEvalPrint(Context context, Global global) {
        String prompt = bundle.getString("shell.prompt");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter err = context.getErr();
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != global;
        ScriptEnvironment env = context.getEnv();
        if (globalChanged) {
            try {
                Context.setGlobal(global);
            } finally {
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
            }
        }
        global.addShellBuiltins();
        while (true) {
            err.print(prompt);
            err.flush();
            String source = "";
            try {
                source = in.readLine();
            } catch (IOException ioe) {
                err.println(ioe.toString());
            }
            if (source == null) {
                break;
            }
            if (!source.isEmpty()) {
                try {
                    Object res = context.eval(global, source, global, "<shell>");
                    if (res != ScriptRuntime.UNDEFINED) {
                        err.println(JSType.toString(res));
                    }
                } catch (Exception e2) {
                    err.println(e2);
                    if (env._dump_on_error) {
                        e2.printStackTrace(err);
                    }
                }
            }
        }
    }
}
