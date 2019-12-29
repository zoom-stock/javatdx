import junit.framework.TestCase;
import org.zoomdev.stock.txd.Market;
import org.zoomdev.stock.txd.StockInfo;
import org.zoomdev.stock.txd.TdxClientService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestSocketClientService extends TestCase {

    public void test() throws ExecutionException, InterruptedException, IOException {
        TdxClientService service = new TdxClientService();
        service.start();

        Future<List<StockInfo>>  list = service.getStockList(Market.sh,0);
        List<StockInfo> result = list.get();
        System.out.println(result);

        service.stop();
    }
}
