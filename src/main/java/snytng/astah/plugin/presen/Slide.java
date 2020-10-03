package snytng.astah.plugin.presen;

import java.util.HashMap;
import java.util.Map;

public class Slide {
	double zoomFactor;
	double originX;
	double originY;
	Map<String, String> nodeVisibilities;

	public Slide(double zoomFactor, double originX, double originY, Map<String, String> nodeVisibilities) {
		this.zoomFactor = zoomFactor;
		this.originX = originX;
		this.originY = originY;
		this.nodeVisibilities = nodeVisibilities;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(zoomFactor + System.lineSeparator());
		sb.append(originX + System.lineSeparator());
		sb.append(originY + System.lineSeparator());
		for(String key : nodeVisibilities.keySet()) {
			sb.append(key + "=" + nodeVisibilities.get(key) + System.lineSeparator());
		}
		return sb.toString();
	}

	public static Slide fromString(String str) {
		String[] lines = str.split(System.lineSeparator());
		double z  = Double.parseDouble(lines[0]);
		double ox = Double.parseDouble(lines[1]);
		double oy = Double.parseDouble(lines[2]);
		HashMap<String, String> nv = new HashMap<>();
		for(int i = 3; i < lines.length; i++) {
			String[] kv = lines[i].split("=");
			nv.put(kv[0],  kv[1]);
		}
		return new Slide(z, ox, oy, nv);
	}
}