package me.conclure.concluresdinomod.util;

import me.conclure.concluresdinomod.util.function.ByteConsumer;
import me.conclure.concluresdinomod.util.function.ByteFunction;
import me.conclure.concluresdinomod.util.function.ByteSupplier;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class OptionalByte {

    private static final OptionalByte EMPTY = new OptionalByte();

    private final boolean isPresent;
    private final byte value;
    private OptionalByte() {
        this.isPresent = false;
        this.value = 0;
    }

    public static OptionalByte empty() {
        return EMPTY;
    }

    private OptionalByte(byte value) {
        this.isPresent = true;
        this.value = value;
    }

    public static OptionalByte of(byte value) {
        return new OptionalByte(value);
    }

    public byte getAsByte() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public boolean isEmpty() {
        return !this.isPresent;
    }

    public void ifPresent(ByteConsumer action) {
        if (this.isPresent) {
            action.accept(this.value);
        }
    }

    public void ifPresentOrElse(ByteConsumer action, Runnable emptyAction) {
        if (this.isPresent) {
            action.accept(this.value);
        } else {
            emptyAction.run();
        }
    }

    public IntStream stream() {
        if (this.isPresent) {
            return IntStream.of(this.value);
        } else {
            return IntStream.empty();
        }
    }

    public <U> Optional<U> map(ByteFunction<U> mapper) {
        Objects.requireNonNull(mapper);
        if (!this.isPresent()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(mapper.apply(this.value));
        }
    }

    public byte orElse(byte other) {
        return this.isPresent ? this.value : other;
    }

    public byte orElseGet(ByteSupplier supplier) {
        return this.isPresent ? this.value : supplier.getAsByte();
    }

    public byte orElseThrow() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public<X extends Throwable> int orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isPresent) {
            return this.value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return obj instanceof OptionalByte other
                && (this.isPresent && other.isPresent
                ? this.value == other.value
                : this.isPresent == other.isPresent);
    }

    @Override
    public int hashCode() {
        return this.isPresent ? Byte.hashCode(this.value) : (byte) 0;
    }

    @Override
    public String toString() {
        return this.isPresent
                ? String.format("OptionalByte[%s]", this.value)
                : "OptionalByte.empty";
    }
}
