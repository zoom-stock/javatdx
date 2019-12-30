import junit.framework.TestCase;
import org.zoomdev.stock.tdx.BlockStock;
import org.zoomdev.stock.tdx.BlockType;
import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.TdxClient;

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

        String code = "000001";
        int start = 0;
        int count = 1;
//       List<Quote> quotes = client.getQuotes(Category.day, Market.sz,code,start,480);
//       System.out.println(quotes);

        // client.getList(Market.sh,0);


        System.out.println(client.getStockList(Market.sh,0));

        System.out.println(client.getStockList(Market.sz,0));
    }


}
