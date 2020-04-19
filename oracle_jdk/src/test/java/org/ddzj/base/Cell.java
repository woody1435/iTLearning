/**
 * Copyright (C), 2015-2020, yourchoice
 * FileName: Cell
 * Author:   woody
 * Date:     2020/4/12 12:33
 * Description: 细胞
 * Version: 0.0.1
 */
package org.ddzj.base;

public class Cell implements Cloneable{
    //细胞壁
    private String xiBaoBi;
    //细胞膜
    private String xiBaoMo;
    //细胞核
    private String xiBaoHe;
    //细胞质
    private String xiBaoZhi;
    //细胞器
    private String xiBaoQi;

    /**
    * @description 无参构造方法
    * @since 0.0.1
    * @param   ；
    * @return
    * @author woody
    * @修改人及修改内容
    */
    public Cell() {
    }

    /**
    * @description 全参构造方法
    * @since 0.0.1
    * @param xiBaoBi 参数0；xiBaoMo 参数1；xiBaoHe 参数2；xiBaoZhi 参数3；xiBaoQi 参数4；
    * @return
    * @author woody
    * @修改人及修改内容
    */
    public Cell(String xiBaoBi, String xiBaoMo, String xiBaoHe, String xiBaoZhi, String xiBaoQi) {
        this.xiBaoBi = xiBaoBi;
        this.xiBaoMo = xiBaoMo;
        this.xiBaoHe = xiBaoHe;
        this.xiBaoZhi = xiBaoZhi;
        this.xiBaoQi = xiBaoQi;
    }

    public String getXiBaoBi() {
        return xiBaoBi;
    }

    public void setXiBaoBi(String xiBaoBi) {
        this.xiBaoBi = xiBaoBi;
    }

    public String getXiBaoMo() {
        return xiBaoMo;
    }

    public void setXiBaoMo(String xiBaoMo) {
        this.xiBaoMo = xiBaoMo;
    }

    public String getXiBaoHe() {
        return xiBaoHe;
    }

    public void setXiBaoHe(String xiBaoHe) {
        this.xiBaoHe = xiBaoHe;
    }

    public String getXiBaoZhi() {
        return xiBaoZhi;
    }

    public void setXiBaoZhi(String xiBaoZhi) {
        this.xiBaoZhi = xiBaoZhi;
    }

    public String getXiBaoQi() {
        return xiBaoQi;
    }

    public void setXiBaoQi(String xiBaoQi) {
        this.xiBaoQi = xiBaoQi;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "xiBaoBi='" + xiBaoBi + '\'' +
                ", xiBaoMo='" + xiBaoMo + '\'' +
                ", xiBaoHe='" + xiBaoHe + '\'' +
                ", xiBaoZhi='" + xiBaoZhi + '\'' +
                ", xiBaoQi='" + xiBaoQi + '\'' +
                '}';
    }

    @Override
    public Cell clone() throws CloneNotSupportedException {
        return (Cell)super.clone();
    }
}
