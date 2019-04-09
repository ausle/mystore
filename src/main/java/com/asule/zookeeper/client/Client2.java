package com.asule.zookeeper.client;

import com.asule.zookeeper.JsonUtils;
import com.asule.zookeeper.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

public class Client2 extends BaseClient{

	public CuratorFramework client = null;
	public static final String zkServerPath = "192.168.98.129:2181";

	public Client2() {
		RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
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
	
//	public final static String CONFIG_NODE = "/super/imooc/redis-config";
	public final static String CONFIG_NODE_PATH = "/super/asule";
	public final static String SUB_PATH = "/redis-config";
	public static CountDownLatch countDown = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		Client2 cto = new Client2();
		System.out.println("client2 启动成功...");
		
		final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
		childrenCache.start(StartMode.BUILD_INITIAL_CACHE);
		
		// 添加监听事件
		childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				// 监听节点变化
				if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
					String configNodePath = event.getData().getPath();
					if (configNodePath.equals(CONFIG_NODE_PATH + SUB_PATH)) {
						System.out.println("监听到配置发生变化，节点路径为:" + configNodePath);
						
						// 读取节点数据
						String jsonConfig = new String(event.getData().getData());
						System.out.println("节点" + CONFIG_NODE_PATH + "的数据为: " + jsonConfig);
						
						// 从json转换配置
						RedisConfig redisConfig = null;
						if (StringUtils.isNotBlank(jsonConfig)) {
							redisConfig = JsonUtils.jsonToPojo(jsonConfig, RedisConfig.class);
						}
						
						// 配置不为空则进行相应操作
						if (redisConfig != null) {
							String type = redisConfig.getType();
							String url = redisConfig.getUrl();
							String remark = redisConfig.getRemark();
							// 判断事件
							if (type.equals("add")) {
								addConfig(url);
							} else if (type.equals("update")) {
								updateConfig(url);
							} else if (type.equals("delete")) {
								deleteConfig();
							}
							
							// TODO 视情况统一重启服务
						}
					}
				}
			}
		});
		
		countDown.await();
		
		cto.closeZKClient();
	}
	
}

