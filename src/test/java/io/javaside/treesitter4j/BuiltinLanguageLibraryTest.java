package io.javaside.treesitter4j;

import org.junit.jupiter.api.Test;

import java.lang.foreign.MemorySegment;

import static org.junit.jupiter.api.Assertions.*;

public class BuiltinLanguageLibraryTest {

    @Test
    void testLoadLanguage_Devicetree() {
        assertNotNull(loadLanguage(BuiltinLanguageLibrary.TS_DEVICETREE));
    }

    @Test
    void testLoadLanguage_KConfig() {
        assertNotNull(loadLanguage(BuiltinLanguageLibrary.TS_KCONFIG));
    }

    /*@Test
    void testLoadLanguage_Java() {
        assertNotNull(loadLanguage(BuiltinLanguageLibrary.TS_JAVA));
    }*/

    private MemorySegment loadLanguage(String libName) {
        var lib = BuiltinLanguageLibrary.getLibrary(libName);
        return lib.loadLanguage();
    }
}
