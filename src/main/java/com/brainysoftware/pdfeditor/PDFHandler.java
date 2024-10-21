package com.brainysoftware.pdfeditor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFHandler {
	private static PDFHandler instance;
	private PDFHandler() {		
	}
	
	public static PDFHandler getInstance() {
		synchronized (PDFHandler.class) {
			if (instance == null) {
				instance = new PDFHandler();
			}			
		}
		return instance;
	}
	
	private PDDocument doc;
	private Path path;
	
	public void openDocument(Path path) {
		try {
			doc = Loader.loadPDF(Files.newInputStream(path));
			this.path = path;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public int getNumPages() {
		return doc.getNumberOfPages();
	}
	
	public void save(String path) {
		if (doc != null) {
			try {
				doc.save(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void save(PDDocument document, String path) {
		if (document != null) {
			try {
				document.save(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Open another instance of the original document for the purpose of saving it.
	 */
	public PDDocument clone() {
		try {
			return Loader.loadPDF(Files.newInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
//	
//	public void process() {
//		Path inputPath = Paths.get("src/main/resources/test.pdf");
//		String fileName = inputPath.getFileName().toString();
//		int dpi = 40;
//		Path saveDir = Paths.get("output");
//		try (PDDocument doc = Loader.loadPDF(Files.newInputStream(inputPath))) {
//			int numPages = doc.getNumberOfPages();
//			PDFRenderer renderer = new PDFRenderer(doc);
//			System.out.println(numPages);
//			for (int i = 0; i < numPages; i++) {
//				BufferedImage bufferedImage = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);
//				Path outputImagePath = saveDir.resolve(fileName + "-" + i + ".png");
//				ImageIO.write(bufferedImage, "png", Files.newOutputStream(outputImagePath));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
	public byte[] getPageAsPng(int pageNo) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			int numPages = doc.getNumberOfPages();
			if (pageNo > numPages) {
				return null;
			}
			PDFRenderer renderer = new PDFRenderer(doc);
			BufferedImage bufferedImage = renderer.renderImage(pageNo);
			ImageIO.write(bufferedImage, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toByteArray();
	}
	

	public List<byte[]> getThumbnails() {
		if (doc == null) {
			return null; // TODO throw exception instead
		}
		List<byte[]> thumbnails = new ArrayList<>();
		PDFRenderer renderer = new PDFRenderer(doc);
		int numPages = doc.getNumberOfPages();
		for (int pageNo = 0; pageNo < numPages; pageNo++) {
	        try {
				BufferedImage bufferedImage = renderer.renderImageWithDPI(pageNo, Constants.THUMBNAIL_DPI, ImageType.RGB);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", output);
				thumbnails.add(output.toByteArray());
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		}
		return thumbnails;
	}
	
	public void addImage(String imagePath, int zeroIndexedPageNo, float normalisedX, float normalisedY) throws IOException {
		// 0 <= normalisedX <= 1 and 0 <= normalisedY <=1, and (0, 0) is the bottom left corner;
		int numPages = doc.getNumberOfPages();
		if (zeroIndexedPageNo > numPages - 1) {
			return;
		}
		PDPage page = doc.getPage(zeroIndexedPageNo);
		// width and heigth are in point where 1 point = 1/72 inch = 25.4/72 mm
		float width = page.getMediaBox().getWidth();
		float height = page.getMediaBox().getHeight();
		float translatedX = normalisedX * width;
		float translatedY = normalisedY * height;
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
		boolean compress = false;
		PDPageContentStream contents = new PDPageContentStream(doc, page, AppendMode.APPEND, compress);
		contents.drawImage(pdImage, translatedX, translatedY);
		contents.close();		
	}
	
	public void addImageToDocument(PDDocument document, byte[] image, int zeroIndexedPageNo, float normalisedX, float normalisedY) throws IOException {
		// 0 <= normalisedX <= 1 and 0 <= normalisedY <=1, and (0, 0) is the bottom left corner;
		int numPages = document.getNumberOfPages();
		if (zeroIndexedPageNo > numPages - 1) {
			return;
		}
		PDPage page = document.getPage(zeroIndexedPageNo);
		// width and heigth are in point where 1 point = 1/72 inch = 25.4/72 mm
		float width = page.getMediaBox().getWidth();
		float height = page.getMediaBox().getHeight();
		float translatedX = normalisedX * width;
		float translatedY = normalisedY * height;
		PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, image, /*name=*/ null);
		PDPageContentStream contents = new PDPageContentStream(
				document, page, AppendMode.APPEND, /*compress=*/ false);
		contents.drawImage(pdImage, translatedX, translatedY);
		contents.close();
	}

	// TODO. This should be replaced by the overload that accepts byte array instead of imagePath
	public void addImageToDocument(PDDocument document, String imagePath, int zeroIndexedPageNo, 
	        float normalisedX, float normalisedY, float fitWidth, float fitHeight) throws IOException {
		// 0 <= normalisedX <= 1 and 0 <= normalisedY <=1, and (0, 0) is the bottom left corner;
		int numPages = document.getNumberOfPages();
		if (zeroIndexedPageNo > numPages - 1) {
			return;
		}
		PDPage page = document.getPage(zeroIndexedPageNo);
		System.out.println("pageNo:" + zeroIndexedPageNo);
		System.out.println("doc null?" + (document == null));
		System.out.println("imagePath:" + imagePath);
		// width and heigth are in point where 1 point = 1/72 inch = 25.4/72 mm
		float width = page.getMediaBox().getWidth();
		float height = page.getMediaBox().getHeight();
		float translatedX = normalisedX * width;
		float translatedY = normalisedY * height;
		System.out.println("translatedX:" + translatedX + ", translatedY:" + translatedY);
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
		boolean compress = false;
		PDPageContentStream contents = new PDPageContentStream(doc, page, AppendMode.APPEND, compress);
		contents.drawImage(pdImage, translatedX, translatedY, fitWidth, fitHeight);
		contents.close();		
	}

	
	public void close() {
		if (doc != null) {
			try {
				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String path = "./data/test.pdf";
		String savePath = "./data/test2.pdf";
		String signaturePath = "./data/welliaSignature-transparent.png";
		System.out.println("Start");
		float normalisedX = 0.6302521008403361F;
		float normalisedY = 0.7835909631391201F;
		int pageNo = 0;
		PDFHandler pdfHandler = PDFHandler.getInstance();
//		try {
//			pdfHandler.openDocument(path);
//			pdfHandler.addImage(signaturePath, pageNo, normalisedX, normalisedY);
//			//doc.save(savePath);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			pdfHandler.openDocument(Paths.get(path));
			PDDocument document = pdfHandler.clone();
			BufferedImage bImage = ImageIO.read(new File(signaturePath));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bImage, "png", baos);
			byte[] image = baos.toByteArray();
			pdfHandler.addImageToDocument(document, image, pageNo, normalisedX, normalisedY);
			document.save(savePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done2");
	}

}
