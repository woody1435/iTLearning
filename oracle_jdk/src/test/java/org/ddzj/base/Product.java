/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: Product
 * Author:   woody
 * Date:     2020/4/13 16:54
 * Description: 产品
 * Version: 0.0.1
 */
package org.ddzj.base;

public class Product {
    private String brand; //品牌
    private String name; //名称
    private boolean flag; //是否有商品

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
