import junit.framework.TestCase;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.Category;
import org.zoomdev.stock.tdx.Market;
import org.zoomdev.stock.tdx.StockInfo;
import org.zoomdev.stock.tdx.impl.TdxClientServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestSocketClientService extends TestCase {

    public void test() throws ExecutionException, InterruptedException, IOException {
        TdxClientServiceImpl service = new TdxClientServiceImpl();
        service.start();

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
