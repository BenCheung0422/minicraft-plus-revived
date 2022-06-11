package minicraft.saveload.bfs;

/**
 * BFS stands for Binary File Streaming (File writer and reader).
 */
public class BFSObject {
	public final TagType tag;
	public final Object value;

	public BFSObject(TagType tag, Object value) {
		this.tag = tag;
		this.value = value;
	}
}
