import junit.framework.TestCase;
import org.zoomdev.stock.IndexQuote;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class TestSocketClient extends TestCase {
    private void printBytes(byte[] bytes, int len) {
        for (int i = 0; i < len; ++i) {
            System.out.print(bytes[i] + " ,");
        }
    }

    public void test() throws IOException {

        TdxClient client = new TdxClient();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();

        String code = "399001";
        int start = 0;
        int count = 1;
     List<IndexQuote> quotes = client.getIndexQuotes(Category.day, Market.sz,code,start,3);
      System.out.println(quotes);

        // client.getList(Market.sh,0);


    //    System.out.println(client.getStockList(Market.sh,0));

    //  System.out.println(client.getStockList(Market.sz,0));
    }


}
