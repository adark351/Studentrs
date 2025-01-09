package com.example.service;

import com.example.entity.Payment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class PaymentReceiptService {

    public void generateReceipt(Payment payment, HttpServletResponse response) throws IOException {
        // Set up the document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Set the response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt_" + payment.getId() + ".pdf");

        // Create a content stream to write to the page
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Add title
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_BOLD, 18);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Payment Receipt");
        contentStream.endText();

        // Add some space before content
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(100, 650);
        contentStream.showText("Payment ID: " + payment.getId());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Resident: " + payment.getResident().getName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Amount: " + payment.getAmount());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Status: " + payment.getStatus());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Due Date: " + payment.getDueDate());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Payment Date: " + payment.getPaymentDate());
        contentStream.endText();

        // Close the content stream and the document
        contentStream.close();
        document.save(response.getOutputStream());
        document.close();
    }
}
