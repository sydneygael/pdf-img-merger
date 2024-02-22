package org.example;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FolderToPdfConverter {
    public static void main(String[] args) {
        String inputFolderPath = "C:\\Users\\sydney\\Downloads\\pdf";
        String outputPdfPath = "C:\\Users\\sydney\\Downloads\\merge.pdf";

        try {
            convertFolderToPdf(inputFolderPath, outputPdfPath);
            System.out.println("Conversion completed successfully.");
        } catch (IOException | ImageReadException e) {
            e.printStackTrace();
        }
    }

    public static void convertFolderToPdf(String inputFolderPath, String outputPdfPath) throws IOException, ImageReadException {
        // Create a new PDF document
        var document = new PDDocument();

        // Get all image files in the folder
        var folder = new File(inputFolderPath);
        File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg")
                || name.toLowerCase().endsWith(".png")
                || name.toLowerCase().endsWith(".jpeg"));

        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                // Load the image
                BufferedImage image = Imaging.getBufferedImage(imageFile);

                // Create a new page for each image
                var page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                // Create a content stream for the page
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Create an image XObject from the BufferedImage
                PDImageXObject imageXObject = PDImageXObject.createFromFileByContent(imageFile,document);

                // Draw the image on the page
                contentStream.drawImage(imageXObject, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());

                // Close the content stream
                contentStream.close();
            }

            // Save the document to a file
            document.save(outputPdfPath);

            // Close the document
            document.close();
        } else {
            System.out.println("No image files found in the specified folder.");
        }
    }
}
