/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: Consumer
 * Author:   woody
 * Date:     2020/4/13 16:59
 * Description: 消费者
 * Version: 0.0.1
 */
package org.ddzj.base;

public class Consumer implements Runnable{
    Product product;

    public Consumer(Product product) {
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
        synchronized (product){ //应该对product加锁，因为它是生产者和消费者对应的公共资源，否则会出现生产者还未生产完消费者就来消费或消费正在消费生产者又要开始生产了
            while (true) {
                //没有商品，就等待生产，先不消费，后面代码不执行
                if(!product.isFlag()){
                    try {
                        product.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //消费完后就没了商品
                product.setFlag(false);
                //通知生产者生产
                product.notify();
                System.out.println("消费者消费：" + product.getBrand() + ":" + product.getName());
            }
        }
    }
}
