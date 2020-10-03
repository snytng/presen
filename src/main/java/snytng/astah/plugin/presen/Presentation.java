package snytng.astah.plugin.presen;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Presentation {

	List<Slide> slides = new ArrayList<>();
	int slideIndex = -1;

	public void init() {
		slides.clear();
		slideIndex = -1;
	}

	public void add(double zoomFactor, Point2D originCoord, Map<String, String> props) {
		System.out.println("ZoomFactor=" + zoomFactor);
		System.out.println("OriginCoord=" + originCoord);
		slides.add(new Slide(zoomFactor, originCoord.getX(), originCoord.getY(), props));
	}

	public void insert(double zoomFactor, Point2D originCoord, Map<String, String> props) {
		System.out.println("ZoomFactor=" + zoomFactor);
		System.out.println("OriginCoord=" + originCoord);
		if(slideIndex == -1) {
			slides.add(new Slide(zoomFactor, originCoord.getX(), originCoord.getY(), props));
		} else {
			slides.add(slideIndex, new Slide(zoomFactor, originCoord.getX(), originCoord.getY(), props));
		}
	}

	public void remove() {
		if(slideIndex == -1) {
			return;
		}
		slides.remove(slideIndex);
	}

	public void clear() {
		slides.clear();
		slideIndex = -1;
	}

	public boolean showFirstSlide() {
		if(slides.isEmpty()) {
			return false;
		}
		slideIndex = 0;
		return true;
	}

	public boolean showNextSlide() {
		if(slides.isEmpty()) {
			return false;
		}

		slideIndex++;
		if(slideIndex >= slides.size()) {
			showLastSlide();
		}
		return true;
	}

	public boolean showPrevSlide() {
		if(slides.isEmpty()) {
			return false;
		}

		slideIndex--;
		if(slideIndex < 0) {
			showFirstSlide();
		}
		return true;
	}

	public boolean showLastSlide() {
		if(slides.isEmpty()) {
			return false;
		}
		slideIndex = slides.size() - 1;
		return true;
	}

	public double getZoomFactor() {
		return slides.get(slideIndex).zoomFactor;
	}

	public Point2D getTargetCoord() {
		Slide s = slides.get(slideIndex);
		return new Point2D.Double(s.originX, s.originY);
	}

	public Map<String, String> getNodeVisibilities() {
		return slides.get(slideIndex).nodeVisibilities;
	}


	public int getNumberOfSlides() {
		return slides.size();
	}

	public int getCurrentSlideIndex() {
		return slideIndex + 1;
	}

	public String saveString() throws IOException, ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		for(Slide s : slides) {
			sb.append("<slide>" + System.lineSeparator());
			sb.append(s.toString());
			sb.append("</slide>" + System.lineSeparator());
		}
		return sb.toString();
	}

	public void loadString(String savedString) {
		if(savedString == null || savedString.isEmpty()) {
			return;
		}

		ArrayList<Slide> newSlides = new ArrayList<>();

		String[] lines = savedString.split(System.lineSeparator());
		StringBuilder sb = new StringBuilder();
		boolean readSlide = false;
		for(String line : lines) {
			if(line.equals("<slide>")) {
				sb = new StringBuilder();
				readSlide = true;
			}
			else
			if(line.equals("</slide>")) {
				Slide s = Slide.fromString(sb.toString());
				newSlides.add(s);
				readSlide = false;
			}
			else
			if(readSlide) {
				sb.append(line + System.lineSeparator());
			}
		}

		this.slides = newSlides;
		this.slideIndex = -1;
	}

}
