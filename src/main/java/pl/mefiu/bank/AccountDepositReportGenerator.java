package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public final class AccountDepositReportGenerator extends AccountVisitor {

    public AccountDepositReportGenerator(String absolutePath) {
        if((absolutePath == null) || (absolutePath.isEmpty())) {
            throw new IllegalArgumentException("absolutePath cannot be empty/null!");
        }
        this.absolutePath = absolutePath;
    }

    @Override
    public void visit(Account account) throws IOException, COSVisitorException, ParseException {
        final String header = "Deposits Report";
        PDDocument document;
        PDPageContentStream contentStream;
        File f = new File(absolutePath);
        if((f.exists()) && (f.isFile())) {
            document = PDDocument.load(absolutePath);
        } else {
            document = new PDDocument();
        }
        PDPage page = new PDPage();
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);
        if((f.exists()) && (f.isFile())) {
            contentStream = new PDPageContentStream(document, page, true, true);
        } else {
            contentStream = new PDPageContentStream(document, page);
        }
        printGeneralInfo(contentStream, rect, account, header);
        printCustomInfo(contentStream, rect, account);
        contentStream.close();
        document.save(absolutePath);
        document.close();
    }

    private void printCustomInfo(PDPageContentStream contentStream, PDRectangle rect, Account account) throws IOException {
        if((account.getTransactions() == null) || (account.getTransactions().size() == 0)) {
            contentStream.beginText();
            contentStream.setFont(FONT, FONT_SIZE);
            contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
            contentStream.drawString("WARNING: No transactions!");
            contentStream.endText();
        } else {
            Integer i = 0;
            for (Transaction transaction : account.getTransactions()) {
                if(transaction.getTransactionType() == Transaction.TransactionType.DEPOSIT) {
                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction ID: " + transaction.getId());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction state: " + transaction.getTransactionStatus());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction type: " + transaction.getTransactionType());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction started at: " + transaction.getStartedAt());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction completed at: " + transaction.getCompletedAt());
                    contentStream.endText();

                    contentStream.beginText();
                    contentStream.setFont(FONT, FONT_SIZE);
                    contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                    contentStream.drawString("Transaction description: " + transaction.getDescription());
                    contentStream.endText();
                    i++;
                }
            }
            if(i == 0) {
                contentStream.beginText();
                contentStream.setFont(FONT, FONT_SIZE);
                contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
                contentStream.drawString("WARNING: No deposits!");
                contentStream.endText();
            }
        }
    }

    private String absolutePath;

}
