package io.javaside.treesitter4j.util;

import io.javaside.treesitter4j.internal.ChainedLibraryLookup;

import java.lang.foreign.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Utils {

    private static final Linker LINKER = Linker.nativeLinker();

    private static final ValueLayout VOID_PTR =
            ValueLayout.ADDRESS.withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));

    private static final FunctionDescriptor VOID_PTR_NO_ARGS_DESC = FunctionDescriptor.of(VOID_PTR);


    public static SymbolLookup findLibraryFromClassPath(String libNameWithoutExtension, Arena arena) {
        try {
            var libName = System.mapLibraryName(libNameWithoutExtension);
            var library = ChainedLibraryLookup.class.getResource(libName);
            if (library == null) {
                throw new RuntimeException("Library not found in classpath: " + libName);
            }
            var libPath = Paths.get(library.toURI()).toAbsolutePath();
            return SymbolLookup.libraryLookup(libPath, arena);
        } catch (IllegalArgumentException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static MemorySegment voidPtrNoArgsCall(SymbolLookup lookup, String functionName) {
        var address = lookup.find(functionName).orElseThrow(
                () -> unresolved("Cannot find symbol for " + functionName));
        try {
            var function = LINKER.downcallHandle(address, VOID_PTR_NO_ARGS_DESC);
            return (MemorySegment) function.invokeExact();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static UnsatisfiedLinkError unresolved(String msg) {
        throw new UnsatisfiedLinkError(msg);
    }
}
