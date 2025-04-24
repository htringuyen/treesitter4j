package io.javaside.treesitter4j.internal;

import java.lang.foreign.Arena;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ServiceLoader;

import io.javaside.treesitter4j.NativeLibraryLookup;
import io.javaside.treesitter4j.util.Utils;

@SuppressWarnings("unused")
public final class ChainedLibraryLookup implements NativeLibraryLookup {

    private static final String TREE_SITTER_LIB = "/treesitter4j-lib/libtree-sitter";

    private ChainedLibraryLookup() {}

    static ChainedLibraryLookup INSTANCE = new ChainedLibraryLookup();

    @Override
    public SymbolLookup get(Arena arena) {
        var serviceLoader = ServiceLoader.load(NativeLibraryLookup.class);
        // NOTE: can't use _ because of palantir/palantir-java-format#934
        //SymbolLookup lookup = (name) -> Optional.empty();

        var firstLookup = serviceLoader.findFirst().orElse(null);
        if (firstLookup == null) {
            return Utils.findLibraryFromClassPath(TREE_SITTER_LIB, arena)
                    .or(Linker.nativeLinker().defaultLookup());
        }

        var lookup = firstLookup.get(arena);
        for (var libraryLookup : serviceLoader) {
            if (libraryLookup != firstLookup) {
                lookup.or(libraryLookup.get(arena));
            }
        }
        return lookup.or(Utils.findLibraryFromClassPath(TREE_SITTER_LIB, arena))
                .or(Linker.nativeLinker().defaultLookup());
    }


}


















