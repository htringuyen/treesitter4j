package io.javaside.treesitter4j.util;

import io.javaside.treesitter4j.internal.ChainedLibraryLookup;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Utils {

    public static SymbolLookup findLibraryFromClassPath(String libNameWithoutExtension, Arena arena) {
        try {
            var library = ChainedLibraryLookup.class.getResource(
                    System.mapLibraryName(libNameWithoutExtension));
            var libPath = Paths.get(library.toURI()).toAbsolutePath();
            return SymbolLookup.libraryLookup(libPath, arena);
        } catch (IllegalArgumentException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
