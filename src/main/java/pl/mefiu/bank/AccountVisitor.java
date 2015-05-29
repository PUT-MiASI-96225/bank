package pl.mefiu.bank;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AccountVisitor {

    public abstract void visit(Account account) throws IOException, COSVisitorException, ParseException;

    protected void printGeneralInfo(PDPageContentStream contentStream, PDRectangle rect, Account account, final String header) throws IOException, COSVisitorException, ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date dateOfBirth = account.getCustomer().getDateOfBirth();
        Date dateOfBirthWithoutTime = formatter.parse(formatter.format(dateOfBirth));

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * line))));
        contentStream.drawString(header);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Customer ID: " + account.getCustomer().getId());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Customer first name: " + account.getCustomer().getFirstName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Customer second name: " + account.getCustomer().getSecondName());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Customer date of birth: " + dateOfBirthWithoutTime);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("City: " + account.getCustomer().getAddress().getCity());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("State: " + account.getCustomer().getAddress().getState());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Postal code: " + account.getCustomer().getAddress().getPostalCode());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Street: " + account.getCustomer().getAddress().getStreet());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Apartment number: " + account.getCustomer().getAddress().getApartmentNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Email: " + account.getCustomer().getContact().getEmail());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Phone number: " + account.getCustomer().getContact().getPhoneNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Account ID: " + account.getId());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Account number: " + account.getAccountNumber());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("IBAN: " + account.getIban());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Balance: " + account.getBalance());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Created at: " + account.getCreatedAt());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.moveTextPositionByAmount(INDENT + INC, (rect.getHeight() - (INDENT + ((SPACE + FONT_SIZE) * (++line)))));
        contentStream.drawString("Expires at: " + account.getExpiresAt());
        contentStream.endText();
    }

    protected static final PDFont FONT = PDType1Font.HELVETICA_BOLD;

    protected static final Integer FONT_SIZE = 10;

    protected static final Integer INDENT = 80;

    protected static final Integer SPACE = 5;

    protected static final Integer INC = 10;

    protected Integer line = 0;

}
