package io.javaside.treesitter4j;

import io.javaside.treesitter4j.internal.TSInputEdit;
import org.jspecify.annotations.NullMarked;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

/** An edit to a text document. */
@NullMarked
public record InputEdit(
        @Unsigned int startByte,
        @Unsigned int oldEndByte,
        @Unsigned int newEndByte,
        Point startPoint,
        Point oldEndPoint,
        Point newEndPoint) {

    MemorySegment into(SegmentAllocator allocator) {
        var inputEdit = TSInputEdit.allocate(allocator);
        TSInputEdit.start_byte(inputEdit, startByte);
        TSInputEdit.old_end_byte(inputEdit, oldEndByte);
        TSInputEdit.new_end_byte(inputEdit, newEndByte);
        TSInputEdit.start_point(inputEdit, startPoint.into(allocator));
        TSInputEdit.old_end_point(inputEdit, oldEndPoint.into(allocator));
        TSInputEdit.new_end_point(inputEdit, newEndPoint.into(allocator));
        return inputEdit;
    }

    @Override
    public String toString() {
        return String.format(
                "InputEdit[startByte=%s, oldEndByte=%s, newEndByte=%s, "
                        + "startPoint=%s, oldEndPoint=%s, newEndPoint=%s]",
                Integer.toUnsignedString(startByte),
                Integer.toUnsignedString(oldEndByte),
                Integer.toUnsignedString(newEndByte),
                startPoint,
                oldEndPoint,
                newEndPoint);
    }
}
