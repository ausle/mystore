package com.asule.zookeeper.client;

public class BaseClient {

    public static void addConfig(String url) throws InterruptedException {
        System.out.println("监听到新增的配置，准备下载...");
        // ... 连接ftp服务器，根据url找到相应的配置
        Thread.sleep(500);
        System.out.println("开始下载新的配置文件，下载路径为<" + url + ">");
        // ... 下载配置到你指定的目录
        Thread.sleep(1000);
        System.out.println("下载成功，已经添加到项目中");
        // ... 拷贝文件到项目目录
    }


    public static void deleteConfig(){
        System.out.println("监听到需要删除配置");
        System.out.println("删除项目中原配置文件...");
    }

    public static void updateConfig(String url) throws InterruptedException {
        System.out.println("监听到更新的配置，准备下载...");
        // ... 连接ftp服务器，根据url找到相应的配置
        Thread.sleep(500);
        System.out.println("开始下载配置文件，下载路径为<" + url + ">");
        // ... 下载配置到你指定的目录
        Thread.sleep(1000);
        System.out.println("下载成功...");
        System.out.println("删除项目中原配置文件...");
        Thread.sleep(100);
        // ... 删除原文件
        System.out.println("拷贝配置文件到项目目录...");
        // ... 拷贝文件到项目目录
    }





}
