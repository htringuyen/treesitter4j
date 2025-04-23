package io.javaside.treesitter4j;

import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

/** A function that logs parsing results. */
@FunctionalInterface
public interface Logger extends BiConsumer<Logger.Type, String> {
    /**
     * {@inheritDoc}
     *
     * @param type the log type
     * @param message the log message
     */
    @Override
    void accept(@NonNull Type type, @NonNull String message);

    /** The type of a log message. */
    enum Type {
        /** Lexer message. */
        LEX,
        /** Parser message. */
        PARSE
    }
}
