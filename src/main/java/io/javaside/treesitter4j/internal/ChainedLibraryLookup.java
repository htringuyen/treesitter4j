package io.javaside.treesitter4j.internal;

import java.lang.foreign.Arena;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.util.Optional;
import java.util.ServiceLoader;

import io.javaside.treesitter4j.NativeLibraryLookup;

@SuppressWarnings("unused")
final class ChainedLibraryLookup implements NativeLibraryLookup {
    private ChainedLibraryLookup() {}

    static ChainedLibraryLookup INSTANCE = new ChainedLibraryLookup();

    @Override
    public SymbolLookup get(Arena arena) {
        var serviceLoader = ServiceLoader.load(NativeLibraryLookup.class);
        // NOTE: can't use _ because of palantir/palantir-java-format#934
        SymbolLookup lookup = (name) -> Optional.empty();
        for (var libraryLookup : serviceLoader) {
            lookup = lookup.or(libraryLookup.get(arena));
        }
        return lookup.or(findLibrary(arena)).or(Linker.nativeLinker().defaultLookup());
    }

    private static SymbolLookup findLibrary(Arena arena) {
        try {
            var library = System.mapLibraryName("tree-sitter");
            return SymbolLookup.libraryLookup(library, arena);
        } catch (IllegalArgumentException e) {
            return SymbolLookup.loaderLookup();
        }
    }
}
