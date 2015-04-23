package be.nabu.glue.pdf;

import javax.xml.bind.annotation.XmlAttribute;

import be.nabu.glue.images.ImageConfiguration;

public class PageConfiguration extends ImageConfiguration {
	private Boolean ignore;
	private int page;

	@XmlAttribute
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@XmlAttribute
	public Boolean getIgnore() {
		return ignore;
	}

	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
}
