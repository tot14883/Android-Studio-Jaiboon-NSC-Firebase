package test.jaiboondemand;

/**
 * Created by User on 11/11/2017.
 */

public class Insta {
    private String title,desc,image,nameproduct,priceproduct,imageproduct,uid,Name;
    public Insta(){
    }
    public Insta(String title,String desc,String image,String nameproduct,String priceproduct,String imageproduct,String uid,String Name){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.Name = Name;
        this.nameproduct = nameproduct;
        this.priceproduct = priceproduct;
        this.imageproduct = imageproduct;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
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
