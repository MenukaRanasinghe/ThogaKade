package com.seekerscloud.Pos.view.tm;

public class ItemDetailsTm {
    private String code;
    private double unitPrice;
    private int qty;
    private double total;

    public ItemDetailsTm(){

    }

    public ItemDetailsTm(String code, double unitPrice, int qty, double total) {
        this.setCode(code);
        this.setUnitPrice(unitPrice);
        this.setQty(qty);
        this.setTotal(total);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
