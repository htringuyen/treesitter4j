package io.javaside.treesitter4j.util;

import io.javaside.treesitter4j.internal.ChainedLibraryLookup;

import java.io.IOException;
import java.lang.foreign.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {

    private static final Linker LINKER = Linker.nativeLinker();

    private static final ValueLayout VOID_PTR =
            ValueLayout.ADDRESS.withTargetLayout(MemoryLayout.sequenceLayout(Long.MAX_VALUE, ValueLayout.JAVA_BYTE));

    private static final FunctionDescriptor VOID_PTR_NO_ARGS_DESC = FunctionDescriptor.of(VOID_PTR);


    public static SymbolLookup findLibraryFromClassPath(String libNameWithoutExtension, Arena arena) {
        try {
            var libName = System.mapLibraryName(libNameWithoutExtension);
            Path libPath;
            try (var binaryIn = ChainedLibraryLookup.class.getResourceAsStream(libName)) {
                if (binaryIn == null) {
                    throw new IllegalArgumentException("Library not found in classpath: " + libName);
                }
                var tempFile = Files.createTempFile(
                        "_treesitter4j_" + libName.replace("/", "."), null);
                Files.copy(binaryIn, tempFile, StandardCopyOption.REPLACE_EXISTING);
                tempFile.toFile().deleteOnExit();
                libPath = tempFile.toAbsolutePath();
            }
            return SymbolLookup.libraryLookup(libPath, arena);
        } catch (IOException e) {
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
