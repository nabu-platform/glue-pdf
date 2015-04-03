package be.nabu.glue.pdf;

import javax.xml.bind.annotation.XmlAttribute;

import be.nabu.glue.images.ImageConfiguration;

public class PageConfiguration extends ImageConfiguration {
	private int page;

	@XmlAttribute
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
