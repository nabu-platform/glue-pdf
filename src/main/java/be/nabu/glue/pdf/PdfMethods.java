package be.nabu.glue.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import be.nabu.glue.ScriptRuntime;
import be.nabu.glue.images.ImageConfiguration;
import be.nabu.glue.images.ImageMethods;
import be.nabu.glue.impl.methods.ScriptMethods;
import be.nabu.glue.impl.methods.TestMethods;

public class PdfMethods {
	
	public static boolean validatePdf(String message, Object expected, Object actual) throws IOException, JAXBException {
		return validatePdf(message, expected, actual, null, null);
	}
	
	public static boolean validatePdf(String message, Object expected, Object actual, Object configuration) throws IOException, JAXBException {
		return validatePdf(message, expected, actual, configuration, null);
	}
	
	public static boolean validatePdf(String message, Object expected, Object actual, Object configuration, Double threshold) throws IOException, JAXBException {
		return checkPdf(message, expected, actual, configuration, threshold, false);
	}

	public static boolean confirmPdf(String message, Object expected, Object actual) throws IOException, JAXBException {
		return confirmPdf(message, expected, actual, null, null);
	}

	public static boolean confirmPdf(String message, Object expected, Object actual, Object configuration) throws IOException, JAXBException {
		return confirmPdf(message, expected, actual, configuration, null);
	}
	
	public static boolean confirmPdf(String message, Object expected, Object actual, Object configuration, Double threshold) throws IOException, JAXBException {
		return checkPdf(message, expected, actual, configuration, threshold, true);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean checkPdf(String message, Object expectedPdf, Object actualPdf, Object configurationContent, Double threshold, boolean fail) throws IOException, JAXBException {
		int dpi = Integer.parseInt(ScriptMethods.environment("pdf.dpi", "200"));
		PDDocument expected = PDDocument.load(new ByteArrayInputStream(ScriptMethods.bytes(expectedPdf))); 
		PDDocument actual = PDDocument.load(new ByteArrayInputStream(ScriptMethods.bytes(actualPdf)));
		List<PDPage> expectedPages = expected.getDocumentCatalog().getAllPages();
		List<PDPage> actualPages = actual.getDocumentCatalog().getAllPages();
		PdfConfiguration pdfConfiguration = null;
		if (configurationContent instanceof PdfConfiguration) {
			pdfConfiguration = (PdfConfiguration) configurationContent;
		}
		else if (configurationContent != null && !(configurationContent instanceof ImageConfiguration)) {
			pdfConfiguration = PdfConfiguration.parse(new ByteArrayInputStream(ScriptMethods.bytes(configurationContent)));
		}
		boolean correct = true;
		// first check amount of pages
		correct &= TestMethods.confirmEquals(message + " - amount of pages", expectedPages.size(), actualPages.size());
		List<byte[]> diffImages = new ArrayList<byte[]>();
		// then check all the pages
		for (int i = 0; i < expectedPages.size(); i++) {
			// only makes sense to compare if there is a target page
			if (actualPages.size() >= i - 1) {
				PageConfiguration pageConfiguration = pdfConfiguration == null ? null : pdfConfiguration.getPage(i);
				// allows you to skip a page entirely
				if (pageConfiguration != null && Boolean.TRUE.equals(pageConfiguration.getIgnore())) {
					continue;
				}
				PDPage expectedPage = expectedPages.get(i);
				PDPage actualPage = actualPages.get(i);
				BufferedImage expectedImage = expectedPage.convertToImage(BufferedImage.TYPE_INT_RGB, dpi);
				BufferedImage actualImage = actualPage.convertToImage(BufferedImage.TYPE_INT_RGB, dpi);
				ImageConfiguration imageConfiguration = pageConfiguration == null && configurationContent instanceof ImageConfiguration ? (ImageConfiguration) configurationContent : null;
				correct &= ImageMethods.checkImage("[page " + (i + 1) + "] " + message, expectedImage, actualImage, pageConfiguration == null ? imageConfiguration : pageConfiguration, threshold, fail);
				// add the diff image to the list, otherwise you only get the last page
				diffImages.add((byte[]) ScriptRuntime.getRuntime().getExecutionContext().getPipeline().get("$diffImage"));
			}
		}
		ScriptRuntime.getRuntime().getExecutionContext().getPipeline().put("$diffImages", diffImages.toArray());
		return correct;
	}
}
