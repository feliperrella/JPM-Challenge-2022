package com.feliperrella.jpmorgan.ChallengeJP;

public class CSVModel {

    public String TraderCode;
    public String TraderName;
    public int OrderID;
    public String Ticker;
    public float Quantity;
    public float Price;

    CSVModel(String TraderCode, String TraderName, int OrderID, String Ticker, float Quantity, float Price){
        this.TraderCode = TraderCode;
        this.TraderName = TraderName;
        this.OrderID = OrderID;
        this.Quantity = Quantity;
        this.Price = Price;
    }
    
}
