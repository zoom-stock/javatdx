import junit.framework.TestCase;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestSocketClientService extends TestCase {

    public void test() throws ExecutionException, InterruptedException, IOException {
        TdxClientService service = new TdxClientService();
        service.start();


//        list = service.getStockList(Market.sh,1000);
//        System.out.println(list.get());

//        list = service.getStockList(Market.sh,2000);
//        System.out.println(list.get());
//
//        list = service.getStockList(Market.sh,3000);
//        System.out.println(list.get());
//
//        list = service.getStockList(Market.sh,4000);
//        System.out.println(list.get());
//
//        list = service.getStockList(Market.sh,5000);
//        System.out.println(list.get());






//        Future<List<BlockStock>> list = service.getBlockInfo(BlockType.BLOCK_SZ);
//        System.out.println(list.get());
//        service.stop();
        Future<List<Quote>>  quotes = service.getQuotes(Category._d, Market.sh,"000001",0,1);

        System.out.println(quotes.get());

        quotes = service.getQuotes(Category._d, Market.sh,"000001",0,1);

        System.out.println(quotes.get());


        Future<List<StockInfo>>  list = service.getStockList(Market.sz,1000);
        List<StockInfo> result = list.get();
        System.out.println(result);


        quotes = service.getQuotes(Category._d, Market.sh,"000001",0,1);

        System.out.println(quotes.get());
    }
}
