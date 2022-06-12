package minicraft.saveload.bfs;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;

import org.json.JSONObject;

import java.util.zip.Deflater;

public class BFSOutputStream extends DataOutputStream {
	BFSOutputStream(OutputStream out) {
		super(out);
	}

	public static BFSOutputStream Deflater(FileOutputStream out) {
		return new BFSOutputStream(new DeflaterOutputStream(out, new Deflater(9)));
	}

    public void writeFully(JSONObject object) throws IOException {
        if (object == null) {
            writeTagType(TagType.END);
        } else {
            writeTagType(TagType.OBJECT);
            writeString("");
            writeObject(object);

            if (out instanceof DeflaterOutputStream) {
                ((DeflaterOutputStream) out).finish();
            }
        }
    }

    /**
     * Write a tag ID (1 byte) to the stream
     *
     * @throws IOException If the tag ID could not be written
     */
    public void writeTagType(TagType type) throws IOException {
        if (type == null) {
            writeTagType(TagType.END);
            return;
        }
        writeByte((byte) type.getId());
    }

    /**
     * Write a value to the stream without knowing its type
     *
     * @throws IOException If the value could not be written for an io-related reason
     */
    public void writeValue(BFSObject value) throws IOException {
        switch (value.tag) {
            case BYTE:
                writeByte((Byte) value.value);
                break;

            case SHORT:
                writeShort((Short) value.value);
                break;

            case INT:
                writeInt((Integer) value.value);
                break;

            case LONG:
                writeLong((Long) value.value);
                break;

            case FLOAT:
                writeFloat((Float) value.value);
                break;

            case DOUBLE:
                writeDouble((Double) value.value);
                break;

            case STRING:
                writeString((String) value.value);
                break;

            case ARRAY:
                writeArray((BFSArray) value.value);
                break;

            case OBJECT:
                writeObject((JSONObject) value.value);
                break;

            case BYTE_ARRAY:
                writeByteArray((byte[]) value.value);
                break;

            case INT_ARRAY:
                writeIntArray((int[]) value.value);
                break;

            case LONG_ARRAY:
                writeLongArray((long[]) value.value);
                break;

            case END:
                throw new IOException("Tag END cannot be written as a value");
        }
    }

    public void writeObject(JSONObject object) throws IOException {
        writeObject(object, true);
    }

    public void writeObject(JSONObject object, boolean close) throws IOException {
        for (String key : object.keySet()) {
            writeTagType(TagType.fromObject(object.get(key))); // Tag type
            writeString(key); // Tag name
            writeValue((BFSObject) object.get(key)); // Tag value
        }

        if (close) {
            writeTagType(TagType.END);
        }
    }

    /**
     * Write a length-prefixed list of tags (all of the same type) to the stream
     *
     * @throws IOException If the list could not be written
     */
    public void writeArray(BFSArray list) throws IOException {
        if (list == null) {
            writeTagType(TagType.END);
            writeInt(0); // Length of zero
            return;
        }

		writeTagType(list.getContentType()); // Type of list contents
        writeInt(list.size()); // Size of lise
        for (BFSObject item : list.list()) { // List items
            writeValue(item);
        }
    }

    /**
     * Write a length-prefixed long array to the stream
     *
     * @throws IOException If the value could not be written
     */
    public void writeLongArray(long[] longs) throws IOException {
        if (longs == null) {
            writeInt(0); // Length of zero
            return;
        }

        writeInt(longs.length);
        for (Long item : longs) {
            writeLong(item);
        }
    }

    /**
     * Write a length-prefixed integer array to the stream
     *
     * @throws IOException If the value could not be written
     */
    public void writeIntArray(int[] ints) throws IOException {
        if (ints == null) {
            writeInt(0); // Length of zero
            return;
        }

        writeInt(ints.length);
        for (Integer item : ints) {
            writeInt(item);
        }
    }

    /**
     * Write a length-prefixed byte array to the stream
     *
     * @throws IOException If the value could not be written
     */
    public void writeByteArray(byte[] bytes) throws IOException {
        if (bytes == null) {
            writeInt(0); // Length of zero
            return;
        }

        writeInt(bytes.length);
        for (Byte item : bytes) {
            writeByte(item);
        }
    }

    /**
     * Write an length-prefixed string to the stream
     *
     * @throws IOException If the value could not be written
     */
    public void writeString(String value) throws IOException {
        if (value == null) {
            writeInt(0); // Length of zero
            return;
        }

        byte[] strBytes = value.getBytes(StandardCharsets.UTF_8);
        writeUnsignedShort(strBytes.length);
        write(strBytes);
    }

    /**
     * Write an unsigned short (2 bytes) to the stream
     *
     * @throws IOException If the value could not be written
     */
    protected void writeUnsignedShort(int value) throws IOException {
        writeChar(value);
    }
}
