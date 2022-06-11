package minicraft.saveload.bfs;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class BFSCompound {
	private HashMap<String, BFSObject> map = new HashMap<>();

	public BFSObject put(String key, BFSObject val) {
		return map.put(key, val);
	}

	public BFSObject putString(String key, String val) {
		return map.put(key, new BFSObject(TagType.STRING, val));
	}

	// TODO for more

	public Set<Entry<String, BFSObject>> entrySet() {
		return map.entrySet();
	}
}
