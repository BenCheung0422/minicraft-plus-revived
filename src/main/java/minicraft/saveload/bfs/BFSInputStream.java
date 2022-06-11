package minicraft.saveload.bfs;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public class BFSInputStream extends DataInputStream {
	public BFSInputStream(InputStream out) {
		super(out);
	}

	public static BFSInputStream Inflater(FileInputStream out) {
		return new BFSInputStream(new InflaterInputStream(out));
	}

	/**
     * Read an NBT compound from the inputStream
     *
     * @return A root TAG_Compound containing the InputStream's NBT data
     * @throws IOException If the inputStream could not be properly read as NBT data
     */
    public BFSCompound readFully() throws IOException {
        TagType rootType = readTagId();
        if (rootType == TagType.END) {
            return new BFSCompound();
        } else if (rootType != TagType.COMPOUND) {
            throw new IOException("Expected COMPOUND at NBT root, but got " + rootType);
        }
        readString(); // Skip root name; typically empty anyways.
        return readCompound();
    }

    /**
     * Read a TAG_Compound from the inputStream
     *
     * @throws IOException If the compound could not be read or did not contain valid NBT data
     */
    public BFSCompound readCompound() throws IOException {
        BFSCompound result = new BFSCompound();

        boolean reachedEnd = false;
        while (!reachedEnd) {
            TagType entryType = readTagId();

            if (entryType == null) {
                throw new IOException("Unknown tag ID for TAG_Compound");

            } else if (entryType == TagType.END) {
                reachedEnd = true;
                continue;
            }

            String entryName = readString();
            BFSObject entryValue = readValue(entryType);
            result.put(entryName, entryValue);
        }

        return result;
    }

    /**
     * Read a TAG_List from the inputStream
     *
     * @throws IOException If the list could not be read or did not contain valid NBT data
     */
    public BFSList readList() throws IOException {
        TagType typeOfContents = readTagId();
        if (typeOfContents == null) {
            throw new IOException("Unknown tag ID for TAG_List");
        }

        int length = readInt();
        if (length <= 0) {
            return new BFSList(typeOfContents);
        }

        BFSList result = new BFSList(typeOfContents);
        for (int i = 0; i < length; i++) {
            result.add(readValue(typeOfContents));
        }
        return result;
    }

    /**
     * Same as {@link #readString(boolean)}, but the resulting string will never be interned.
     *
     * @see #readString(boolean)
     * @see String#intern()
     */
    @SuppressWarnings("UnusedReturnValue")
    public String readString() throws IOException {
        return readString(false);
    }

    /**
     * Read a length-prefixed string from the inputStream
     *
     * @param intern Whether or not the string's {@link String#intern() interned} value will be
     *               returned. When deserializing lots of NBT data with the same properties, setting
     *               this to {@code true} can significantly lower memory consumption.
     * @throws IOException If the string could not be read or was not valid NBT data
     * @see String#intern()
     */
    public String readString(boolean intern) throws IOException {
        String utf = readUTF();
        if (intern) {
            return utf.intern();
        }
        return utf;
    }

    /**
     * Read a TAG_Long_Array from the inputStream
     *
     * @throws IOException If the long array could not be read or was not valid NBT data
     */
    public long[] readLongArray() throws IOException {
        int length = readInt();
        if (length < 0) {
            throw new IOException(
                new NegativeArraySizeException(
                    "TAG_Long_Array was prefixed with a negative length"));
        } else if (length == 0) {
            return new long[0];
        }

        byte[] bytes = new byte[length * 8];
        readFully(bytes);
        int byteIndex = 0;

        long[] longArray = new long[length];
        for (int i = 0; i < length; i++, byteIndex += 8) {
            long element = 0;
            int bitOffset = 56;
            for (int b = 0; b < 8; b++, bitOffset -= 8) {
                element |= ((long) (bytes[byteIndex + b] & 0xFF)) << bitOffset;
            }
            longArray[i] = element;
        }
        return longArray;
    }

    /**
     * Read a TAG_Int_Array from the inputStream
     *
     * @throws IOException If the integer array could not be read or was not valid NBT data
     */
    public int[] readIntArray() throws IOException {
        int length = readInt();
        if (length < 0) {
            throw new IOException(
                new NegativeArraySizeException(
                    "TAG_Int_Array was prefixed with a negative length"));
        }

        byte[] bytes = new byte[length * 4];
        readFully(bytes);
        int byteIndex = 0;

        int[] intArray = new int[length];
        for (int i = 0; i < length; i++, byteIndex += 4) {
            int element = 0;
            int bitOffset = 24;
            for (int b = 0; b < 4; b++, bitOffset -= 8) {
                element |= (bytes[byteIndex + b] & 0xFF) << bitOffset;
            }
            intArray[i] = element;
        }

        return intArray;
    }

    /**
     * Read a TAG_Byte_Array from the inputStream
     *
     * @throws IOException If the byte array could not be read or was not valid NBT data
     */
    public byte[] readByteArray() throws IOException {
        int length = readInt();
        if (length < 0) {
            throw new IOException(
                new NegativeArraySizeException(
                    "TAG_Byte_Array was prefixed with a negative length"));
        }

        byte[] bytes = new byte[length];
        readFully(bytes);

        return bytes;
    }

    /**
     * Read an NBT tag ID from the inputStream
     *
     * @throws IOException If the tag ID could not be read
     */
    public TagType readTagId() throws IOException {
        return TagType.fromId(read());
    }

    /**
     * Read a NBT value from the inputStream as the specified type
     *
     * @throws IOException If the value could not be read or was not valid NBT data
     */
    public BFSObject readValue(TagType tagType) throws IOException {
        switch (tagType) {
            case BYTE:
                return new BFSObject(TagType.BYTE, readByte());

            case SHORT:
                return new BFSObject(TagType.SHORT, readShort());

            case INT:
                return new BFSObject(TagType.INT, readInt());

            case LONG:
                return new BFSObject(TagType.LONG, readLong());

            case FLOAT:
                return new BFSObject(TagType.FLOAT, readFloat());

            case DOUBLE:
                return new BFSObject(TagType.DOUBLE, readDouble());

            case BYTE_ARRAY:
                return new BFSObject(TagType.BYTE_ARRAY, readByteArray());

            case STRING:
                return new BFSObject(TagType.STRING, readString());

            case LIST:
                return new BFSObject(TagType.LIST, readList());

            case COMPOUND:
                return new BFSObject(TagType.COMPOUND, readCompound());

            case INT_ARRAY:
                return new BFSObject(TagType.INT_ARRAY, readIntArray());

            case LONG_ARRAY:
                return new BFSObject(TagType.LONG_ARRAY, readLongArray());

            default:
                return null;
        }
    }
}
