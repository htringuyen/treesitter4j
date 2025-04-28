# Tree-sitter for Java
This is a fork of [java-tree-sitter](https://github.com/tree-sitter/java-tree-sitter) with the following changes:
- Better cross-platform support, e.g. fixed open issues related to loading native libraries
- Automatically build core lib and parsers into classpath
- Easily adding support for new languages
- Convenient and reliable loading and using of pre-built parsers

## Building

### Requirements
- Java 22+ and a compatible maven
- python (tested in python 3.11) for scripting
- cmake and GCC compiler (e.g. MinGW GCC for Windows) for auto build of native libraries
- [jextract-22](https://jdk.java.net/jextract/) for generating of Java bindings

```bash
git clone https://github.com/htringuyen/treesitter4j
cd treesitter4j
git submodule update --init --recursive
mvn test
```

## Document
The API only have a little changes from original repo. 
So, original document is absolutely valid for reference:
- [Java API docs for java-tree-sitter](https://tree-sitter.github.io/java-tree-sitter/)

The new convenient API for loading pre-built parsers:

```java
    LanguageLibrary lib = BuiltinLanguageLibrary.getLibrary(
                BuiltinLanguageLibrary.DEVICETREE_LIB);

    Language language = new Language(lib.loadLanguage());

    try (Parser parser = new Parser(language);
         Tree syntaxTree = parser.parse(SAMPLE_SOURCE_STR).orElseThrow()) {
        /*
         ...
         using syntax tree to do useful stuff
         ...
        */
    }
```
