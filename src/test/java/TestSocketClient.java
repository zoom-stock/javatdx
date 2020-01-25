import junit.framework.TestCase;
import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.TdxClient;
import org.zoomdev.stock.tdx.impl.TdxClientImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

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

        //        List<Quote> quotes = client.getQuotes(Category.day, Market.sz,code,start,100);
//        System.out.println(quotes);
//
        System.out.println(client.getStockList(Market.sh, 0));
//
//        System.out.println(client.getIndexQuotes(Category._d,Market.sh,code,start,100));

//        System.out.println(client.getBlockInfo(BlockType.BLOCK_DEFAULT));
    }


    public void test1() throws IOException {
        TdxClient client = new TdxClientImpl();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();

        System.out.println(client.getTimePrice(Market.sz, "000001"));
        System.out.println(client.getHistoryTimePrice(Market.sz, "000001", "20190115"));

    }


}
