package test.jaiboondemand.must_product;

/**
 * Created by User on 12/11/2017.
 */

public class NeedDonate {
    private String namedonate,pricedonate,imagedonate,amountdonate;
    public NeedDonate(){

    }
    public NeedDonate(String namedonate,String pricedonate,String imagedonate,String amountdonate){
        this.namedonate = namedonate;
        this.pricedonate = pricedonate;
        this.imagedonate = imagedonate;
        this.amountdonate = amountdonate;
    }

    public void setAmountdonate(String amountdonate) {
        this.amountdonate = amountdonate;
    }

    public void setImagedonate(String imagedonate) {
        this.imagedonate = imagedonate;
    }

    public void setNamedonate(String namedonate) {
        this.namedonate = namedonate;
    }

    public void setPricedonate(String pricedonate) {
        this.pricedonate = pricedonate;
    }

    public String getAmountdonate() {
        return amountdonate;
    }

    public String getImagedonate() {
        return imagedonate;
    }

    public String getNamedonate() {
        return namedonate;
    }

    public String getPricedonate() {
        return pricedonate;
    }
}
