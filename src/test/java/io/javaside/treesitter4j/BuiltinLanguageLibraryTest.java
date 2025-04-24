package io.javaside.treesitter4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuiltinLanguageLibraryTest {

    private static LanguageLibrary languageLibrary;

    @BeforeAll
    static void setup() {
        languageLibrary = BuiltinLanguageLibrary.getLibrary(BuiltinLanguageLibrary.DEVICETREE_LIB);
    }

    @Test
    void testLoadLanguage() {
        assertDoesNotThrow(() -> languageLibrary.loadLanguage());
    }
}
