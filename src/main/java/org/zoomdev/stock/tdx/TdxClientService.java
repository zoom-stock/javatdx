package org.zoomdev.stock.tdx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.IndexQuote;
import org.zoomdev.stock.Quote;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TdxClientService {

    public static final Log log = LogFactory.getLog(TdxClientService.class);

    private final int threadCount;
    private IpInfo[] ipInfos;
    private final BlockingQueue<FutureTask> queue = new LinkedBlockingDeque<FutureTask>();
    private ServiceThread[] serviceThreads;

    public IpRecord getIpRecord() {
        return ipRecord;
    }

    public void setIpRecord(IpRecord ipRecord) {
        this.ipRecord = ipRecord;
    }

    private IpRecord ipRecord = new DefaultIpRecord();



    private AtomicInteger connectedCounter = new AtomicInteger(0);


    class TxdServiceThread extends ServiceThread{
        final TdxClient client;
        private boolean connected = false;

        public TxdServiceThread(){
            client = new TdxClient();
        }


        private void sendHeart(){

        }


        private void setConnected(boolean connected){
            this.connected = connected;
            if(connected){
                int connectCount = connectedCounter.incrementAndGet();
                if(connectCount==threadCount){
                    saveIpInfos();
                }
            }else{
                connectedCounter.decrementAndGet();
            }
        }

        @Override
        protected boolean repetitionRun() {
            if(!connected){
                try{
                    connectToHost();
                    setConnected(true);
                }catch (IOException e){
                    //仍然等待下次连接的机会
                    try {
                        Thread.sleep(1000);
                        return true;
                    } catch (InterruptedException ex) {
                        return false;
                    }
                }
            }



            try {
                FutureTask task = queue.poll(200,TimeUnit.MILLISECONDS);
                if(task==null){
                    //检查心跳
                    sendHeart();
                    return true;
                }
                task.run();
                try{
                    task.get();
                } catch (ExecutionException e) {
                    //发生了io异常
                    e.printStackTrace();
                    setConnected(false);
                    client.close();
                    return true;
                }


                return true;

            } catch (InterruptedException e) {
                return false;
            }

        }

        private void connectToHost() throws IOException{
            for(int j=0; j < 3;++j){
                try{
                    autoSelectHost(client);
                    return;
                }catch (IOException ex){
                    log.warn("服务器连接失败",ex);
                }
            }
            throw new IOException("Connect to host failed");
        }
    }




    public TdxClientService() {
        this(1);
    }

    public TdxClientService(int threadCount) {
        this.threadCount = threadCount;
    }



    public synchronized void saveIpInfos(){
        ipRecord.save(ipInfos);
    }

    protected void autoSelectHost(TdxClient client) throws IOException{
        IpInfo[] infos = this.ipInfos;
        for(int i=0; i < infos.length; ++i){
            IpInfo info = infos[i];
            try {
                log.info("Try to connect to host:"+info.host);
                client.setSocketAddress(new InetSocketAddress(info.host,info.port));
                client.connect();
                info.successCount++;
                break;
            } catch (IOException e) {
                log.warn("Connect to host "+info.host+" fail ",e);
                if(info.successCount>0){
                    info.successCount--;
                }
               continue;
            }
        }

    }

    private void createServiceThreads(){
        serviceThreads = new TxdServiceThread[threadCount];
        for(int i=0; i < threadCount; ++i){
            serviceThreads[i] = new TxdServiceThread() ;
        }



    }

    private void startSeviceThreads(){
        for(int i=0; i < threadCount; ++i){
            assert (serviceThreads[i]!=null);
            serviceThreads[i].start();
        }
    }

    private void stopServiceThreads(){
        for(int i=0; i < threadCount; ++i){
            assert (serviceThreads[i]!=null);
            serviceThreads[i].shutdown();
        }
    }

    public synchronized void start()  {
        ipInfos = Ips.getIpInfos(ipRecord);
        createServiceThreads();
        startSeviceThreads();
    }




    public synchronized void stop() {
        stopServiceThreads();

    }

    public Future<List<Quote>> getQuotes(
            final Category category,
            final Market market,
            final String code,
            final int start,
            final int count


    ){
        return submit(new Callable<List<Quote>>() {
            @Override
            public List<Quote> call() throws Exception {
                TdxClient client = getClient();
                return client.getQuotes(category,market,code,start,count);
            }
        });
    }

    private TdxClient getClient(){
        TxdServiceThread thread = (TxdServiceThread) Thread.currentThread();
        return thread.client;
    }


    protected <T> Future<T> submit(Callable<T> callable){
        FutureTask<T> future = new FutureTask<T>(callable);;
        queue.add(future);
        return future;
    }


    public Future<List<StockInfo>> getStockList(final Market market, final int start){
        return submit(new Callable<List<StockInfo>>() {
            @Override
            public List<StockInfo> call() throws Exception {
                TdxClient client = getClient();
                return client.getStockList(market,start);
            }
        });
    }

    public Future<List<BlockStock>> getBlockInfo(final BlockType type) {
        return submit(new Callable<List<BlockStock>>() {
            @Override
            public List<BlockStock> call() throws Exception {
                TdxClient client = getClient();
                return client.getBlockInfo(type);
            }
        });
    }

    public Future<List<IndexQuote>> getIndexQuotes(
            final Category category,
            final Market market,
            final String code,
            final int start,
            final int count
    ){
        return submit(new Callable<List<IndexQuote>>() {
            @Override
            public List<IndexQuote> call() throws Exception {
                TdxClient client = getClient();
                return client.getIndexQuotes(category,market,code,start,count);
            }
        });
    }



}
