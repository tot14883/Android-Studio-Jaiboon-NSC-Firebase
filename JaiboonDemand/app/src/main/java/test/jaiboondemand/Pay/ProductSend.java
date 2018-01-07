package test.jaiboondemand.Pay;

/**
 * Created by User on 1/3/2018.
 */

public class ProductSend {
   private String imageproduct,nameproduct,priceproduct,amount;
    public ProductSend(){
    }
    public ProductSend(String imageproduct,String nameproduct,String amount,String priceproduct){
        this.imageproduct = imageproduct;
        this.nameproduct = nameproduct;
        this.amount = amount;
        this.priceproduct = priceproduct;
    }

    public String getPriceproduct() {
        return priceproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public String getImageproduct() {
        return imageproduct;
    }

    public String getAmount() {
        return amount;
    }

    public void setPriceproduct(String priceproduct) {
        this.priceproduct = priceproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public void setImageproduct(String imageproduct) {
        this.imageproduct = imageproduct;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
