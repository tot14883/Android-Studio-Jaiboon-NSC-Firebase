package test.jaiboondemand;

/**
 * Created by User on 11/11/2017.
 */

public class Insta {
    private String title,desc,image,nameproduct,priceproduct,imageproduct;
    public Insta(){
    }
    public Insta(String title,String desc,String image,String nameproduct,String priceproduct,String imageproduct){
        this.title = title;
        this.desc = desc;
        this.image = image;

        this.nameproduct = nameproduct;
        this.priceproduct = priceproduct;
        this.imageproduct = imageproduct;
    }

    public String getImageproduct() {
        return imageproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public String getPriceproduct() {
        return priceproduct;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public void setImageproduct(String imageproduct) {
        this.imageproduct = imageproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public void setPriceproduct(String priceproduct) {
        this.priceproduct = priceproduct;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
