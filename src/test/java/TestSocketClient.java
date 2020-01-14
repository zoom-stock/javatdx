import junit.framework.TestCase;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.BlockType;
import org.zoomdev.stock.tdx.Category;
import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.impl.TdxClientImpl;

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

        TdxClientImpl client = new TdxClientImpl();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();

        String code = "000001";
        int start = 0;
        int count = 1;

        System.out.println(client.getCount(Market.sh));
        System.out.println(client.getCount(Market.sz));

         List<Quote> quotes = client.getQuotes(Category.day, Market.sz,code,start,100);
        System.out.println(quotes);

        System.out.println( client.getStockList(Market.sh,0));

        System.out.println(client.getIndexQuotes(Category._d,Market.sh,code,start,100));

        System.out.println(client.getBlockInfo(BlockType.BLOCK_DEFAULT));
    }


}
