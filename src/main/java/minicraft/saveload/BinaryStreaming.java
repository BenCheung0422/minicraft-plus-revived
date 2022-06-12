package minicraft.saveload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.Deflater;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;

public class BinaryStreaming {
	public static ObjectNode JSONObjectToObjectNode(JSONObject json) {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		for (String key : json.keySet()) {
			node.set(key, getJsonNode(json.get(key)));
		}

		return node;
	}
	public static ArrayNode JSONArrayToArrayNode(JSONArray array) {
		ArrayNode node = JsonNodeFactory.instance.arrayNode();
		array.forEach(e -> node.add(getJsonNode(e)));
		return node;
	}
	public static JsonNode getJsonNode(Object obj) {
		if (JSONObject.NULL.equals(obj) || obj == null) {
			return JsonNodeFactory.instance.nullNode();
		} else if (obj instanceof Byte) {
			return JsonNodeFactory.instance.numberNode((Byte) obj);
		} else if (obj instanceof Short) {
			return JsonNodeFactory.instance.numberNode((Short) obj);
		} else if (obj instanceof Integer) {
			return JsonNodeFactory.instance.numberNode((Integer) obj);
		} else if (obj instanceof Long) {
			return JsonNodeFactory.instance.numberNode((Long) obj);
		} else if (obj instanceof Float) {
			return JsonNodeFactory.instance.numberNode((Float) obj);
		} else if (obj instanceof Double) {
			return JsonNodeFactory.instance.numberNode((Double) obj);
		} else if (obj instanceof Boolean) {
			return JsonNodeFactory.instance.booleanNode((Boolean) obj);
		} else if (obj instanceof String) {
			return JsonNodeFactory.instance.textNode((String) obj);
		} else if (obj instanceof JSONArray) {
			return JSONArrayToArrayNode((JSONArray) obj);
		} else if (obj instanceof JSONObject) {
			return JSONObjectToObjectNode((JSONObject) obj);
		} else {
			return JsonNodeFactory.instance.nullNode();
		}
	}

	public static CBORGenerator Deflater(FileOutputStream out) {
		return CBORGenerator(new DeflaterOutputStream(out, new Deflater(9)));
	}
	private static CBORGenerator CBORGenerator(OutputStream out) {
		CBORFactory cborFactory = new CBORFactory();
		try {
			return cborFactory.createGenerator(out);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static CBORParser Inflater(FileInputStream in) {
		return CBORParser(new InflaterInputStream(in));
	}
	private static CBORParser CBORParser(InputStream in) {
		CBORFactory cborFactory = new CBORFactory();
		try {
			return cborFactory.createParser(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
