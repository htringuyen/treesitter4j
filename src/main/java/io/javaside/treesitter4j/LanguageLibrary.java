package io.javaside.treesitter4j;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;

public interface LanguageLibrary {

    MemorySegment loadLanguage();

    SymbolLookup getSymbolLookup();

    Arena getArena();

}
