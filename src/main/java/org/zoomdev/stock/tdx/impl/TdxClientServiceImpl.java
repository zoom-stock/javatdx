package org.zoomdev.stock.tdx.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.Quote;
import org.zoomdev.stock.tdx.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TdxClientServiceImpl implements TdxClientService {

    public static final Log log = LogFactory.getLog(TdxClientServiceImpl.class);

    public static final int DEFAULT_RECONNECT_TIMEOUT = 1000;

    private final int threadCount;
    private final BlockingQueue<FutureTask> queue = new LinkedBlockingDeque<FutureTask>();
    private IpInfo[] ipInfos;
    private ServiceThread[] serviceThreads;
    private IpRecord ipRecord = new DefaultIpRecord();
    private AtomicInteger connectedCounter = new AtomicInteger(0);

    private int reconnectTimeout = DEFAULT_RECONNECT_TIMEOUT;
    private List<InetSocketAddress> connectedAddress = new ArrayList<InetSocketAddress>();
    private String tdxRootDir;

    public TdxClientServiceImpl() {
        this(1);
    }

    public TdxClientServiceImpl(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getTdxRootDir() {
        return tdxRootDir;
    }

    public void setTdxRootDir(String tdxRootDir) {
        this.tdxRootDir = tdxRootDir;
    }

    public IpRecord getIpRecord() {
        return ipRecord;
    }

    public void setIpRecord(IpRecord ipRecord) {
        this.ipRecord = ipRecord;
    }

    public synchronized void saveIpInfos() {
        ipRecord.save(ipInfos);
    }

    protected void autoSelectHost(TdxClientImpl client) throws IOException {
        IpInfo[] infos = this.ipInfos;
        for (int i = 0; i < infos.length; ++i) {
            IpInfo info = infos[i];
            InetSocketAddress address = new InetSocketAddress(info.host, info.port);


            try {
                synchronized (connectedAddress) {
                    boolean found = false;
                    for (InetSocketAddress addr : connectedAddress) {
                        if (addr.equals(address)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        continue;
                    }

                    log.info("Try to connect to host:" + info.host);
                    client.setSocketAddress(address);
                    client.connect();
                    connectedAddress.add(client.getSocketAddress());
                    info.successCount++;
                }

                break;
            } catch (IOException e) {
                try {
                    Thread.sleep(reconnectTimeout);
                } catch (InterruptedException ex) {
                    return;
                }
                log.warn("Connect to host " + info.host + " fail ", e);
                if (info.successCount > 0) {
                    info.successCount--;
                }
                continue;
            }
        }

    }

    private void createServiceThreads() {
        serviceThreads = new TxdServiceThread[threadCount];
        for (int i = 0; i < threadCount; ++i) {
            serviceThreads[i] = new TxdServiceThread();
        }


    }

    private void startSeviceThreads() {
        for (int i = 0; i < threadCount; ++i) {
            assert (serviceThreads[i] != null);
            serviceThreads[i].start();
        }
    }

    private void stopServiceThreads() {
        for (int i = 0; i < threadCount; ++i) {
            assert (serviceThreads[i] != null);
            serviceThreads[i].shutdown();
        }
    }

    public synchronized void start() {
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


    ) {
        return submit(new Callable<List<Quote>>() {
            @Override
            public List<Quote> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getQuotes(category, market, code, start, count);
            }
        });
    }

    @Override
    public void setReconnectTimeout(int timeout) {
        reconnectTimeout = timeout;
    }

    private TdxClientImpl getClient() {
        TxdServiceThread thread = (TxdServiceThread) Thread.currentThread();
        return thread.client;
    }

    protected <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> future = new FutureTask<T>(callable);
        ;
        queue.add(future);
        return future;
    }

    public Future<Integer> getCount(final Market market) {
        return submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getCount(market);
            }
        });
    }

    public Future<List<StockInfo>> getStockList(final Market market, final int start) {
        return submit(new Callable<List<StockInfo>>() {
            @Override
            public List<StockInfo> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getStockList(market, start);
            }
        });
    }

    @Override
    public Future<Collection<BlockStock>> getBlockInfo(final BlockType type) {
        return submit(new Callable<Collection<BlockStock>>() {
            @Override
            public Collection<BlockStock> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getBlockInfo(type);
            }
        });
    }

    public Future<List<BlockStock>> getBlockInfo(final BlockFileType type) {
        return submit(new Callable<List<BlockStock>>() {
            @Override
            public List<BlockStock> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getBlockInfo(type);
            }
        });
    }

    public Future<List<Quote>> getIndexQuotes(
            final Category category,
            final Market market,
            final String code,
            final int start,
            final int count
    ) {
        return submit(new Callable<List<Quote>>() {
            @Override
            public List<Quote> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getIndexQuotes(category, market, code, start, count);
            }
        });
    }

    @Override
    public Future<List<StockInfo>> getStockList() {
        return submit(new Callable<List<StockInfo>>() {
            @Override
            public List<StockInfo> call() throws Exception {
                TdxClientImpl client = getClient();
                return client.getStockList();
            }
        });
    }

    class TxdServiceThread extends ServiceThread {
        final TdxClientImpl client;
        private boolean connected = false;
        private int count;


        public TxdServiceThread() {
            client = new TdxClientImpl();
            client.setTdxRootDir(tdxRootDir);
        }

        /// 1分钟一次
        private void sendHeart() throws IOException {
            ++count;
            if (count >= 5 * 30) {
                count = 0;
                client.getCount(Market.sh);
            }
        }


        private void setConnected(boolean connected) {
            this.connected = connected;
            if (connected) {
                int connectCount = connectedCounter.incrementAndGet();
                if (connectCount == threadCount) {
                    saveIpInfos();
                }
            } else {
                connectedCounter.decrementAndGet();
            }
        }

        @Override
        protected boolean repetitionRun() {
            if (!connected) {
                try {
                    connectToHost();
                    setConnected(true);
                } catch (IOException e) {
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
                FutureTask task = queue.poll(200, TimeUnit.MILLISECONDS);
                try {
                    if (task == null) {
                        //检查心跳
                        sendHeart();
                        return true;
                    }
                    task.run();
                    task.get();
                    return true;
                } catch (Exception e) {
                    log.info("An error has happened, close the socket and reconnect", e);
                    setConnected(false);
                    client.close();
                    return true;

                }

            } catch (InterruptedException e) {
                return false;
            }

        }

        private void connectToHost() throws IOException {
            for (int j = 0; j < 3; ++j) {
                try {
                    autoSelectHost(client);
                    return;
                } catch (IOException ex) {
                    log.warn("服务器连接失败", ex);
                }
            }
            throw new IOException("Connect to host failed");
        }
    }


}
