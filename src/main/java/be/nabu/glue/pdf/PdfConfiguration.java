/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
