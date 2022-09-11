package me.conclure.concluresdinomod.util.function;

import java.util.Objects;

@FunctionalInterface
public interface ByteConsumer {
    void accept(byte value);

    default ByteConsumer andThen(ByteConsumer after) {
        Objects.requireNonNull(after);
        return (byte t) -> {
            this.accept(t); after.accept(t); };
    }
}
