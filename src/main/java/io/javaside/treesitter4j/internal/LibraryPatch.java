package io.javaside.treesitter4j.internal;

import static io.javaside.treesitter4j.internal.TreeSitter.*;

final class LibraryPatch extends TreeSitter {

    static {
        SYMBOL_LOOKUP.or(ChainedLibraryLookup.INSTANCE.get(LIBRARY_ARENA));
        ts_set_allocator(malloc$address(), calloc$address(), realloc$address(), free$address());
    }
}
