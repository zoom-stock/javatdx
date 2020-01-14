import junit.framework.TestCase;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.utils.DataInputStream;
import org.zoomdev.stock.tdx.utils.DataOutputStream;
import org.zoomdev.stock.tdx.reader.TdxQuoteReader;
import org.zoomdev.stock.tdx.writer.TdxQuoteWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestReaderWriter extends TestCase {

    public void test() throws IOException {
        Quote quote = new Quote();
        quote.setDate("20190101");
        quote.setOpen(1);
        quote.setClose(2);
        quote.setHigh(3);
        quote.setLow(4);
        quote.setAmt(5);
        quote.setVol(6);
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(byteArrayOutputStream);
        TdxQuoteWriter.writeForDay(quote,os);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream();
        dataInputStream.setBuf(bytes);
        Quote quote1=TdxQuoteReader.parseForDay(dataInputStream);


        System.out.println(quote1);


        assertEquals(quote1.getDate(),quote.getDate());

    }

    public void test2() throws IOException {
        Quote quote = new Quote();
        quote.setDate("201901010302");
        quote.setOpen(1);
        quote.setClose(2);
        quote.setHigh(3);
        quote.setLow(4);
        quote.setAmt(5);
        quote.setVol(6);
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(byteArrayOutputStream);
        TdxQuoteWriter.writeForMin(quote,os);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream();
        dataInputStream.setBuf(bytes);
        Quote quote1=TdxQuoteReader.parseForMin(dataInputStream);


        System.out.println(quote1);


        assertEquals(quote1.getDate(),quote.getDate());

    }
}
