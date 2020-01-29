import junit.framework.TestCase;
import org.zoomdev.stock.tdx.*;
import org.zoomdev.stock.tdx.impl.TdxClientImpl;
import org.zoomdev.stock.tdx.reader.TdxBlockReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;

public class TestSocketClient extends TestCase {
    private void printBytes(byte[] bytes, int len) {
        for (int i = 0; i < len; ++i) {
            System.out.print(bytes[i] + " ,");
        }
    }

    public void testAddress(){
        InetSocketAddress address1 = new InetSocketAddress("localhost",8080);
        InetSocketAddress address2 = new InetSocketAddress("localhost",8080);
        assertEquals(address1,address2);

    }

//    public void testAsync() throws IOException, InterruptedException, ExecutionException {
//
//        final BlockingQueue queue = new LinkedBlockingQueue();
//
//        final TdxClientImpl[] clients = new TdxClientImpl[2];
//        List<FutureTask> tasks = new ArrayList<FutureTask>();
//
//
//
//        for(int i=0; i < 2; ++i){
//            TdxClientImpl client = new TdxClientImpl();
//            client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
//            client.connect();
//            clients[i] = client;
//            Thread thread= new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//
//                        String code = "000001";
//                        int start = 0;
//
//
//                        while (true){
//                            FutureTask data = (FutureTask)queue.poll(20000, TimeUnit.MILLISECONDS);
//                            if(data==null){
//                                break;
//                            }
//
//                            data.run();
//                            data.get();
//
//
//                        }
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.setName(String.valueOf(i));
//            thread.start();
//        }
//
//
//        long now = System.currentTimeMillis();
//        for(int i=0; i < 100; ++i){
//            FutureTask task = new FutureTask(new Callable() {
//                @Override
//                public Object call() throws Exception {
//
//                    Thread thread = Thread.currentThread();
//                    String name = thread.getName();
//                    int index = Integer.parseInt(name);
//                    TdxClientImpl  client = clients[index];
//                    List<Quote> quotes = client.getIndexQuotes(Category.day, Market.sh,"000001",0,1);
//                    return null;
//                }
//            });
//            queue.add(task);
//            tasks.add(task);
//        }
//
//        for(int i=0; i < 100; ++i){
//            tasks.get(i).get();
//        }
//
//        System.out.println("耗时"+(System.currentTimeMillis()-now));
//    }

    public void test() throws IOException {
        TdxClientImpl client = new TdxClientImpl();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();

//        String code = "000001";
//        int start = 0;
//        int count = 1;
//
//        System.out.println(client.getCount(Market.sh));
//        System.out.println(client.getCount(Market.sz));

        //        List<Quote> quotes = client.getQuotes(Category.day, Market.sz,code,start,100);
//        System.out.println(quotes);
//
//        System.out.println(client.getStockList(Market.sh, 0));
//
//        System.out.println(client.getIndexQuotes(Category._d,Market.sh,code,start,100));

//
//        for(char i='a'; i <= 'z'; ++i){
//            for(char j='a'; j <= 'z'; ++j){
//                try{
//                    System.out.println(client.getBlockInfo(String.format("block_%c%c.dat",i,j)));
//                    System.out.println(String.format("%c%c",i,j));
//                }catch (Exception e){
//
//                }
//            }
//        }
       // System.out.println(client.getBlockInfo("spblock.dat"));
    }

    static final String path = "/Users/jzoom/Downloads/new_tdx";

    public void testReadFile() throws IOException {
        TdxClientImpl client = new TdxClientImpl();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();
        client.setTdxRootDir(path);

        Collection<BlockStock> stocks = client.getBlockInfo(BlockType.TdxIndustry);
        System.out.println(stocks);

        stocks = client.getBlockInfo(BlockType.Concept);
        System.out.println(stocks);

        stocks = client.getBlockInfo(BlockType.Index);
        System.out.println(stocks);

        stocks = client.getBlockInfo(BlockType.Style);
        System.out.println(stocks);
    }

    public void test1() throws IOException {
        TdxClient client = new TdxClientImpl();
        client.setSocketAddress(new InetSocketAddress("119.147.212.81", 7709));
        client.connect();

        System.out.println(client.getTimePrice(Market.sz, "000001"));
        System.out.println(client.getHistoryTimePrice(Market.sz, "000001", "20190115"));

    }


}
