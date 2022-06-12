package minicraft.saveload.bfs;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import org.json.JSONObject;

public class BFSInputStream extends DataInputStream {
	public BFSInputStream(InputStream out) {
		super(out);
	}

	public static BFSInputStream Inflater(FileInputStream out) {
		return new BFSInputStream(new InflaterInputStream(out));
	}

    public JSONObject readFully() throws IOException {
        TagType rootType = readTagId();
        if (rootType == TagType.END) {
            return new JSONObject();
        } else if (rootType != TagType.OBJECT) {
            throw new IOException("Expected COMPOUND at NBT root, but got " + rootType);
        }
        readString(); // Skip root name; typically empty anyways.
        return readObject();
    }

    public JSONObject readObject() throws IOException {
        JSONObject result = new JSONObject();

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

    public BFSArray readArray() throws IOException {
        TagType typeOfContents = readTagId();
        if (typeOfContents == null) {
            throw new IOException("Unknown tag ID for Array Content");
        }

        int length = readInt();
        if (length <= 0) {
            return new BFSArray(typeOfContents);
        }

        BFSArray result = new BFSArray(typeOfContents);
        for (int i = 0; i < length; i++) {
            result.add(readValue(typeOfContents));
        }
        return result;
    }

    @SuppressWarnings("UnusedReturnValue")
    public String readString() throws IOException {
        return readString(false);
    }

    public String readString(boolean intern) throws IOException {
        String utf = readUTF();
        if (intern) {
            return utf.intern();
        }
        return utf;
    }

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

    public TagType readTagId() throws IOException {
        return TagType.fromId(read());
    }

    public BFSObject readValue(TagType tagType) throws IOException {
        switch (tagType) {
            case BYTE:
                return BFS.getBFSObject(readByte());

            case SHORT:
                return BFS.getBFSObject(readShort());

            case INT:
                return BFS.getBFSObject(readInt());

            case LONG:
                return BFS.getBFSObject(readLong());

            case FLOAT:
                return BFS.getBFSObject(readFloat());

            case DOUBLE:
                return BFS.getBFSObject(readDouble());

            case BYTE_ARRAY:
                return BFS.getBFSObject(readByteArray());

            case STRING:
                return BFS.getBFSObject(readString());

            case ARRAY:
                return BFS.getBFSObject(readArray());

            case OBJECT:
                return BFS.getBFSObject(readObject());

            case INT_ARRAY:
                return BFS.getBFSObject(readIntArray());

            case LONG_ARRAY:
                return BFS.getBFSObject(readLongArray());

            default:
                return null;
        }
    }
}
