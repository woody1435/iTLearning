/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: Producer
 * Author:   woody
 * Date:     2020/4/13 16:55
 * Description: 生产者
 * Version: 0.01
 */
package org.ddzj.base;

public class Producer implements Runnable{
    Product product; //产品

    public Producer(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void run() {
        int index = 0;
        while (true){
            //应该对product加锁，因为它是生产者和消费者对应的公共资源，否则会出现生产者还未生产完消费者就来消费或消费正在消费生产者又要开始生产了
            //但如果只有同步代码块，会出现一直生产商品，或一直消费商品，这样与现实不符，此时需要使用wait、notify、notifyAll方法
            synchronized (product){
                //如何有商品，就等待消费，先不生产，后面代码不执行
                if(product.isFlag()){
                    try {
                        product.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(index%2 == 0){ //轮替生产两种商品
                    product.setBrand("娃哈哈");
                    product.setName("冰红茶");
                }else {
                    product.setBrand("康师傅");
                    product.setName("牛肉面");
                }
                System.out.println("生产者生产："+product.getBrand()+":"+product.getName());
                //生产完后有了商品
                product.setFlag(true);
                //生产完后应通知其它线程（目前只有两个线程，那就是通知消费者来消费）
                product.notify();
                index++;
            }
        }
    }
}
