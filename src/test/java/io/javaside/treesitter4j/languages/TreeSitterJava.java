package io.javaside.treesitter4j.languages;

import java.lang.foreign.*;

public final class TreeSitterJava {
    private static final ValueLayout VOID_PTR =
            ValueLayout.ADDRESS.withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));
    private static final FunctionDescriptor FUNC_DESC = FunctionDescriptor.of(VOID_PTR);
    private static final Linker LINKER = Linker.nativeLinker();
    private static final TreeSitterJava INSTANCE = new TreeSitterJava();

    private final Arena arena = Arena.ofAuto();
    private final SymbolLookup symbols = findLibrary();

    /**
     * {@snippet lang=c :
     * const TSLanguage *tree_sitter_java()
     * }
     */
    public static MemorySegment language() {
        return INSTANCE.call("tree_sitter_java");
    }

    private SymbolLookup findLibrary() {
        try {
            var library = System.mapLibraryName("tree-sitter-java");
            return SymbolLookup.libraryLookup(library, arena);
        } catch (IllegalArgumentException e) {
            return SymbolLookup.loaderLookup();
        }
    }

    private static UnsatisfiedLinkError unresolved(String name) {
        return new UnsatisfiedLinkError("Unresolved symbol: %s".formatted(name));
    }

    @SuppressWarnings("SameParameterValue")
    private MemorySegment call(String name) throws UnsatisfiedLinkError {
        var address = symbols.find(name).orElseThrow(() -> unresolved(name));
        try {
            var function = LINKER.downcallHandle(address, FUNC_DESC);
            return (MemorySegment) function.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Call to %s failed".formatted(name), e);
        }
    }
}
