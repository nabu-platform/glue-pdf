package be.nabu.glue.pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pdf")
public class PdfConfiguration {
	
	private List<PageConfiguration> pages = new ArrayList<PageConfiguration>();

	public List<PageConfiguration> getPages() {
		return pages;
	}

	public void setPages(List<PageConfiguration> pages) {
		this.pages = pages;
	}
	
	public PageConfiguration getPage(int page) {
		for (PageConfiguration configuration : pages) {
			if (configuration.getPage() == page) {
				return configuration;
			}
		}
		return null;
	}
	
	public static PdfConfiguration parse(InputStream input) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PdfConfiguration.class);
		return (PdfConfiguration) context.createUnmarshaller().unmarshal(input);
	}
	
	public void write(OutputStream output) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(PdfConfiguration.class);
		context.createMarshaller().marshal(this, output);
	}
}
