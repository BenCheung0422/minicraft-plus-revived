package minicraft.saveload.bfs;

import java.util.Arrays;
import java.util.Comparator;

import org.json.JSONObject;

public enum TagType {
    END(0x00, null),
    BYTE(0x01, Byte.class),
    SHORT(0x02, Short.class),
    INT(0x03, Integer.class),
    LONG(0x04, Long.class),
    FLOAT(0x05, Float.class),
    DOUBLE(0x06, Double.class),
    BYTE_ARRAY(0x07, byte[].class),
    STRING(0x08, String.class),
    ARRAY(0x09, BFSArray.class),
    OBJECT(0x0A, JSONObject.class),
    INT_ARRAY(0x0B, int[].class),
    LONG_ARRAY(0x0C, long[].class);

	private static final TagType[] values;

    static {
        values = values();
        Arrays.sort(values, Comparator.comparingInt(TagType::getId));
    }

    private final int id;
    private final Class<?> runtimeType;

    TagType(int id, Class<?> runtimeType) {
        this.id = id;
        this.runtimeType = runtimeType;
    }

    /**
     * @return The one-byte ID used to mark tags using the type.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The class used in-memory to represent tags with the type.
     */
    public Class<?> getRuntimeType() {
        return runtimeType;
    }

    /**
     * @deprecated Renamed to {@link #getRuntimeType}.
     */
    @Deprecated
    public Class<?> getClazz() {
        return getRuntimeType();
    }

    /**
     * Searches for a {@link TagType} that uses the provided {@link #getId() ID}.
     *
     * @return The tag type associated with the {@code id}, or {@code null} if no type uses that ID.
     */
    public static TagType fromId(int id) {
        if (id < 0 || id >= values.length) {
            return null;
        }
        return values[id];
    }

	public static TagType fromObject(Object obj) {
        if (obj instanceof Byte) {
            return BYTE;

        } else if (obj instanceof Short) {
            return SHORT;

        } else if (obj instanceof Integer) {
            return INT;

        } else if (obj instanceof Long) {
            return LONG;

        } else if (obj instanceof Float) {
            return FLOAT;

        } else if (obj instanceof Double) {
            return DOUBLE;

        } else if (obj instanceof byte[]) {
            return BYTE_ARRAY;

        } else if (obj instanceof String) {
            return STRING;

        } else if (obj instanceof BFSArray) {
            return ARRAY;

        } else if (obj instanceof JSONObject) {
            return OBJECT;

        } else if (obj instanceof int[]) {
            return INT_ARRAY;

        } else if (obj instanceof long[]) {
            return LONG_ARRAY;

        } else if (obj == null) {
            return END;

        } else {
            throw new IllegalArgumentException(obj.getClass().getSimpleName() + " has no NBT type");
        }
    }
}
