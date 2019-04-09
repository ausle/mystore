package com.asule.test;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Test4 extends TestCase implements Watcher{


    Logger logger=Logger.getLogger(Test4.class);



    private static final String path="47.98.212.84:2181";
    private ZooKeeper zooKeeper;
    private Stat stat=new Stat();


    @Override
    protected void setUp() throws Exception {
        zooKeeper = new ZooKeeper(path, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                logger.info("watcher process");
            }
        });
    }

    public void test0(){
        try {
//            testCreateNode(zooKeeper);
//                zooKeeper.setData("/testNode","thumder".getBytes(),2);
//                deleteNode(zooKeeper);
            byte[] data = zooKeeper.getData("/testNode", true, stat);
            String result = new String(data);

            System.out.println(result);

            countDown.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CountDownLatch countDown = new CountDownLatch(1);


    public void test1(){
        try {
            byte[] data = zooKeeper.getData("/testNode", true,stat);
            String s = new String(data);
            System.out.println(s);
            countDown.await();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    private void deleteNode(ZooKeeper zooKeeper) {
        //同步删除节点
//                zooKeeper.delete("/testNode",3);
        //异步删除节点，要使用异步的方法，需要去通知用户。
        String ctx="{delete,success}";
        zooKeeper.delete("/testNode", 0, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
                System.out.println(i+"-----"+s+"-----"+o);
            }
        },ctx);
        try {
            new Thread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testCreateNode(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        //同步方法创建节点
        String node=zooKeeper.create("/testNode","testNode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//                logger.info("节点创建成功---->"+node);
        /**
         * 异步方式创建节点
         */
//                String ctx="{create,success}";
//                zooKeeper.create("/testNode", "testNode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
//                        new AsyncCallback.StringCallback() {
//                            @Override
//                            public void processResult(int i, String s, Object ctx, String name) {
//
//                                System.out.println(i+"-----"+s+"-----"+ctx+"-----"+name+"-----");
//
//                            }
//                        },ctx);
//                new Thread().sleep(2000);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType()== Event.EventType.NodeDataChanged){
            byte[] resByte = new byte[0];
            try {
                resByte = zooKeeper.getData("/testNode", false, stat);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String result = new String(resByte);
            System.out.println("更改后的值:" + result);
            System.out.println("版本号变化dversion：" + stat.getVersion());
            countDown.countDown();
        }else if(watchedEvent.getType() == Event.EventType.NodeCreated) {

        } else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {

        } else if(watchedEvent.getType() == Event.EventType.NodeDeleted) {

        }
    }
}
