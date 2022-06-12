package minicraft.saveload.bfs;

import org.json.JSONObject;

/**
 * BFS stands for Binary File Streaming (File writer and reader).
 */
public class BFS {
	public static BFSObject getBFSObject(byte value) { return new BFSObject(TagType.BYTE, value); }
	public static BFSObject getBFSObject(short value) { return new BFSObject(TagType.SHORT, value); }
	public static BFSObject getBFSObject(int value) { return new BFSObject(TagType.INT, value); }
	public static BFSObject getBFSObject(long value) { return new BFSObject(TagType.LONG, value); }
	public static BFSObject getBFSObject(float value) { return new BFSObject(TagType.FLOAT, value); }
	public static BFSObject getBFSObject(double value) { return new BFSObject(TagType.DOUBLE, value); }
	public static BFSObject getBFSObject(byte[] value) { return new BFSObject(TagType.BYTE_ARRAY, value); }
	public static BFSObject getBFSObject(String value) { return new BFSObject(TagType.STRING, value); }
	public static BFSObject getBFSObject(BFSArray value) { return new BFSObject(TagType.ARRAY, value); }
	public static BFSObject getBFSObject(JSONObject value) { return new BFSObject(TagType.OBJECT, value); }
	public static BFSObject getBFSObject(int[] value) { return new BFSObject(TagType.INT_ARRAY, value); }
	public static BFSObject getBFSObject(long[] value) { return new BFSObject(TagType.LONG_ARRAY, value); }
}
