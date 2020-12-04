package com.zhou.demo.excel.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.cache.selector.SimpleReadCacheSelector;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EasyExcelTest {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Thread.sleep(5000L);
//        File file = new File("/Users/hfzhou/Desktop/excel内存测试/test-07.xlsx");
        FileInputStream file = new FileInputStream("/Users/hfzhou/Desktop/excel内存测试/test-07.xlsx");
        MyDataListener listener = new MyDataListener();
        System.out.println("开始读取excel" + new Date());
        //        EasyExcel.read(file, listener).doReadAll();

        // 第一个参数的意思是 多少M共享字符串以后 采用文件存储 单位MB 默认5M
        // 第二个参数 文件存储时，内存存放多少M缓存数据 默认20M

//        EasyExcel.read(file, listener).readCacheSelector(new SimpleReadCacheSelector(5, 100)).sheet().doRead();
        EasyExcel.read(file, listener).readCacheSelector(new SimpleReadCacheSelector(5, 100)).sheet().doRead();
//        EasyExcelFactory.readBySax(file, new Sheet(1, 1),
        System.out.println("读取excel结束," + new Date());
        System.out.println(1);
        Thread.sleep(5000L);
    }


    public static class MyDataListener<T> extends AnalysisEventListener<T> {

        public List<T> dataList = new LinkedList<>();

        @Override
        public void invoke(T data, AnalysisContext context) {
            dataList.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {

        }
    }

}
