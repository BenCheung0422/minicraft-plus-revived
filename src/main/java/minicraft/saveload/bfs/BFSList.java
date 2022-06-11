package minicraft.saveload.bfs;

import java.util.ArrayList;
import java.util.List;

public class BFSList {
	private ArrayList<BFSObject> list = new ArrayList<>();

	private final TagType contentType;

	public BFSList(TagType type) {
		contentType = type;
	}

	public boolean add(BFSObject val) {
		if (val.tag.equals(contentType))
			return list.add(val);
		else
			return false;
	}

	public TagType getContentType() {
		return contentType;
	}

	public int size() { return list.size(); }

	public List<BFSObject> list() { return list; }

	// TODO for more


}
