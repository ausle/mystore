package com.asule.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

public class CuratorOperator {

    public CuratorFramework client = null;
    public static final String zkServerPath = "192.168.98.128:2181";

    public CuratorOperator() {
        /**
         * 同步创建zk示例，原生api是异步的
         *
         * curator链接zookeeper的策略:ExponentialBackoffRetry
         * baseSleepTimeMs：初始sleep的时间
         * maxRetries：最大重试次数
         * maxSleepMs：最大重试时间
         */
//		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        /**
         * curator链接zookeeper的策略:RetryNTimes
         * n：重试的次数
         * sleepMsBetweenRetries：每次重试间隔的时间
         */
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);


        /**
         * 永远重试，不推荐使用。
         */
//		RetryPolicy retryPolicy3 = new RetryForever(retryIntervalMs)

        /**
         * curator链接zookeeper的策略:RetryUntilElapsed
         * maxElapsedTimeMs:最大重试时间
         * sleepMsBetweenRetries:每次重试间隔
         * 重试时间超过maxElapsedTimeMs后，就不再重试
         */
//		RetryPolicy retryPolicy4 = new RetryUntilElapsed(2000, 3000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args)throws Exception{
        CuratorOperator cto = new CuratorOperator();
        /*boolean isZkCuratorStarted = cto.client.isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "连接中" : "已关闭"));*/

        String path="/super/asule/redis-config";
        String data="redis-config";

        //创建节点
        cto.client.create()
                .creatingParentsIfNeeded()              //是否创建父节点
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)   //
                .forPath(path,data.getBytes());

        //更新节点数据
//        cto.client.setData().withVersion(0).forPath(path,"asule-oppo".getBytes());


        //删除节点
/*        cto.client.delete()
                    .guaranteed()                   //如果删除失败，那么在后端还是继续删除，直到成功
                    .deletingChildrenIfNeeded()     //有子节点也删除
                    .withVersion(1)
                    .forPath(path);*/


        /*
        List<ACL> acls=new ArrayList<>();
        acls.add(new ACL(31, ANYONE_ID_UNSAFE));
        cto.client.setACL().withACL(acls).forPath(path);*/


        //获取节点数据
        /*Stat stat = new Stat();
        //传入Stat用于接收节点数据
        byte[] bytes = cto.client.getData().storingStatIn(stat).forPath(path);
        System.out.println("节点数据："+new String(bytes).toString());
        System.out.println("该节点版本号为："+stat.getVersion());*/


        //获取子节点
//        List<String> list = cto.client.getChildren().forPath(path);
//        for (String node:list) {
//            System.out.println(node);
//        }

        //判断节点是否存在,不存在为null
/*        Stat stat1 = cto.client.checkExists().forPath(path);
        System.out.println(stat1);*/


        //使用usingWatcher，监控只会触发一次，监听完毕后就销毁
        /*cto.client.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("Watcher："+watchedEvent.getPath());
            }
        }).forPath(path);

        cto.client.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println("CuratorWatcher："+watchedEvent.getPath());
            }
        }).forPath(path);*/


        /*//实现注册一次watcher，监听多次
        //NodeCache监听数据节点的变更，会触发事件
        final NodeCache nodeCache = new NodeCache(cto.client, path);
        //start(true)会让NodeCache获取节点数据，缓存在本地。
        nodeCache.start(true);
        if (nodeCache.getCurrentData()!=null){
            System.out.println("节点数据为"+new String(nodeCache.getCurrentData().getData()));
        }else{
            System.out.println("节点数据为null");
        }
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点数据为："+new String(nodeCache.getCurrentData().getData()));
                System.out.println("节点路径为："+new String(nodeCache.getCurrentData().getPath()));

            }
        });*/


        /*//子节点监听
        PathChildrenCache childrenCache = new PathChildrenCache(cto.client, path, true);
        *//*
            StartMode指定初始化的方式
                NORMAL：异步初始化
                POST_INITIALIZED_EVENT：异步初始化。

                BUILD_INITIAL_CACHE：同步初始化
        *//*
        //为POST_INITIALIZED_EVENT，初始化子节点会发送通知
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        List<ChildData> currentData = childrenCache.getCurrentData();
        for (ChildData cd:currentData) {
            String childData=new String(cd.getData());
            System.out.println(childData);
        }

        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event)
                    throws Exception {
                if(event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)){
                    System.out.println("子节点初始化");
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    String path = event.getData().getPath();
                    System.out.println("新增一个子节点:"+path);
                    System.out.println("新增子节点数据:" + new String(event.getData().getData()));
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    System.out.println("删除子节点:" + event.getData().getPath());
                }else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                    System.out.println("修改子节点路径:" + event.getData().getPath());
                    System.out.println("修改子节点数据:" + new String(event.getData().getData()));
                }
            }
        });

        Thread.sleep(100000);*/
    }
}
