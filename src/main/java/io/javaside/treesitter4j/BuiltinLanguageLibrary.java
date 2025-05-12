package io.javaside.treesitter4j;

import io.javaside.treesitter4j.util.Utils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.util.HashMap;

public class BuiltinLanguageLibrary implements LanguageLibrary {

    public static final String TS_DEVICETREE = "libtree-sitter-devicetree";

    public static final String TS_KCONFIG = "libtree-sitter-kconfig";

    public static final String TS_JAVA = "libtree-sitter-java";

    private static final HashMap<String, LanguageLibrary> LIBRARY_CACHE = new HashMap<>();

    private static final HashMap<String, String> LIB_INIT_FUNCTIONS = new HashMap<>();

    private static final String BUILTIN_LIB_DIR = "/treesitter4j-lib";

    private final String libraryName;

    private final String initFunction;

    private final Arena arena = Arena.ofAuto();

    private final SymbolLookup symbolLookup;

    private BuiltinLanguageLibrary(String libraryName, String initFunction) {

        symbolLookup = Utils.findLibraryFromClassPath(BUILTIN_LIB_DIR + "/" + libraryName, arena);

        this.libraryName = libraryName;

        this.initFunction = initFunction;
    }

    static {
        LIB_INIT_FUNCTIONS.put(TS_DEVICETREE, "tree_sitter_devicetree");
        LIB_INIT_FUNCTIONS.put(TS_KCONFIG, "tree_sitter_kconfig");
        LIB_INIT_FUNCTIONS.put(TS_JAVA, "tree_sitter_java");
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public SymbolLookup getSymbolLookup() {
        return symbolLookup;
    }

    @Override
    public MemorySegment loadLanguage() {
        return Utils.voidPtrNoArgsCall(symbolLookup, initFunction);
    }

    public static LanguageLibrary getLibrary(String name) {
        return LIBRARY_CACHE.computeIfAbsent(name, _ ->
                new BuiltinLanguageLibrary(name, LIB_INIT_FUNCTIONS.get(name)));
    }
}
