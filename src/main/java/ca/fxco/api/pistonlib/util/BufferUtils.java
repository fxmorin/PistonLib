package ca.fxco.api.pistonlib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

import java.lang.reflect.Array;
import java.util.BitSet;

// TODO: Remove in 1.20.5, and replace with Codec's
public class BufferUtils {

    /**
     * Attempt to write an object to the buffer.
     *
     * @param buffer The buffer to write the object into
     * @param object The object to write into the buffer
     */
    public static void writeToBuffer(FriendlyByteBuf buffer, Object object) {
        if (object instanceof Boolean bool) {
            buffer.writeBoolean(bool);
        } else if (object instanceof Integer i) {
            buffer.writeVarInt(i);
        } else if (object instanceof Double d) {
            buffer.writeDouble(d);
        } else if (object instanceof Float f) {
            buffer.writeFloat(f);
        } else if (object instanceof Long l) {
            buffer.writeLong(l);
        } else if (object instanceof String s) {
            buffer.writeUtf(s);
        } else if (object instanceof Character c) {
            buffer.writeChar(c);
        } else if (object instanceof Byte b) {
            buffer.writeByte(b);
        } else if (object instanceof Short s) {
            buffer.writeShort(s);
        } else if (object instanceof BlockPos b) {
            buffer.writeBlockPos(b);
        } else if (object instanceof Enum<?> e) {
            buffer.writeEnum(e);
        } else if (object instanceof BitSet s) {
            buffer.writeBitSet(s);
        } else if (object instanceof ChunkPos c) {
            buffer.writeChunkPos(c);
        } else if (object instanceof byte[] a) {
            buffer.writeByteArray(a);
        } else if (object instanceof long[] a) {
            buffer.writeLongArray(a);
        } else if (object instanceof int[] a) {
            buffer.writeVarIntArray(a);
        } else if (object instanceof boolean[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (boolean value : a) {
                buffer.writeBoolean(value);
            }
        } else if (object instanceof short[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (short value : a) {
                buffer.writeShort(value);
            }
        } else if (object instanceof char[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (char value : a) {
                buffer.writeChar(value);
            }
        } else if (object instanceof float[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (float value : a) {
                buffer.writeFloat(value);
            }
        } else if (object instanceof double[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (double value : a) {
                buffer.writeDouble(value);
            }
        } else if (object instanceof Object[] a) {
            buffer.writeVarInt(a.length); // Array size
            for (Object value : a) {
                writeToBuffer(buffer, value);
            }
        } else {
            throw new IllegalArgumentException("Failed to write to buffer - Unsupported type: " +
                    object.getClass().getSimpleName());
        }
    }

    /**
     * Attempt to read an object from the buffer.
     *
     * @param buffer The buffer to read the object from
     * @param clazz  The class of the type we want to extract from the buffer
     * @return A new object read from the buffer
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T readFromBuffer(FriendlyByteBuf buffer, Class<T> clazz) {
        if (Boolean.class.isAssignableFrom(clazz)) {
            return (T) (Boolean) buffer.readBoolean();
        } else if (Integer.class.isAssignableFrom(clazz)) {
            return (T) (Integer) buffer.readVarInt();
        } else if (Double.class.isAssignableFrom(clazz)) {
            return (T) (Double) buffer.readDouble();
        } else if (Float.class.isAssignableFrom(clazz)) {
            return (T) (Float) buffer.readFloat();
        } else if (Long.class.isAssignableFrom(clazz)) {
            return (T) (Long) buffer.readLong();
        } else if (String.class.isAssignableFrom(clazz)) {
            return (T) buffer.readUtf();
        } else if (Character.class.isAssignableFrom(clazz)) {
            return (T) (Character) buffer.readChar();
        } else if (Byte.class.isAssignableFrom(clazz)) {
            return (T) (Byte) buffer.readByte();
        } else if (Short.class.isAssignableFrom(clazz)) {
            return (T) (Short) buffer.readShort();
        } else if (BlockPos.class.isAssignableFrom(clazz)) {
            return (T) buffer.readBlockPos();
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return (T) buffer.readEnum((Class<? extends Enum>)clazz);
        } else if (BitSet.class.isAssignableFrom(clazz)) {
            return (T) buffer.readBitSet();
        } else if (ChunkPos.class.isAssignableFrom(clazz)) {
            return (T) buffer.readChunkPos();
        } else if (byte[].class.isAssignableFrom(clazz)) {
            return (T) buffer.readByteArray();
        } else if (long[].class.isAssignableFrom(clazz)) {
            return (T) buffer.readLongArray();
        } else if (int[].class.isAssignableFrom(clazz)) {
            return (T) buffer.readVarIntArray();
        } else if (boolean[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            boolean[] array = new boolean[size];
            for (int i = 0; i < size; i++) {
                array[i] = buffer.readBoolean();
            }
            return (T) array;
        } else if (short[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            short[] array = new short[size];
            for (int i = 0; i < size; i++) {
                array[i] = buffer.readShort();
            }
            return (T) array;
        } else if (char[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            char[] array = new char[size];
            for (int i = 0; i < size; i++) {
                array[i] = buffer.readChar();
            }
            return (T) array;
        } else if (float[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            float[] array = new float[size];
            for (int i = 0; i < size; i++) {
                array[i] = buffer.readFloat();
            }
            return (T) array;
        } else if (double[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            double[] array = new double[size];
            for (int i = 0; i < size; i++) {
                array[i] = buffer.readDouble();
            }
            return (T) array;
        } else if (Object[].class.isAssignableFrom(clazz)) {
            int size = buffer.readVarInt();
            Class<?> componentType = clazz.getComponentType();
            Object[] array = new Object[size];
            for (int i = 0; i < size; i++) {
                array[i] = readFromBuffer(buffer, componentType);
            }
            Object[] copy = (clazz == Object[].class)
                    ? new Object[array.length]
                    : (Object[]) Array.newInstance(componentType, array.length);
            System.arraycopy(array, 0, copy, 0, array.length);
            return (T) copy;
        } else {
            throw new IllegalArgumentException("Failed to read from buffer - Unsupported type: " +
                    clazz.getSimpleName());
        }
    }
}
