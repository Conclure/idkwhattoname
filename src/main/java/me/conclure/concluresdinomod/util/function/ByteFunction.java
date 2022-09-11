package me.conclure.concluresdinomod.util.function;

@FunctionalInterface
public interface ByteFunction<R> {
    R apply(byte value);
}
