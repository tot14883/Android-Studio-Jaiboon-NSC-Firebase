package test.jaiboondemand.Factor;

/**
 * Created by User on 1/26/2018.
 */

public class factordonate {
    private String image, title_factor, desc_factor, posted_factor, Category,posted,fac_time;

    public factordonate() {
    }

    public factordonate(String image, String title_factor, String desc_factor, String posted_factor, String Category,String fac_time) {
        this.image = image;
        this.title_factor = title_factor;
        this.desc_factor = desc_factor;
        this.posted_factor = posted_factor;
        this.Category = Category;
        this.fac_time = fac_time;
    }

    public void setFac_time(String fac_time) {
        this.fac_time = fac_time;
    }

    public String getFac_time() {
        return fac_time;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPosted_factor() {
        return posted_factor;
    }

    public String getTitle_factor() {
        return title_factor;
    }

    public String getDesc_factor() {
        return desc_factor;
    }

    public String getImage() {
        return image;
    }


    public void setPosted_factor(String posted_factor) {
        this.posted_factor = posted_factor;
    }

    public void setTitle_factor(String title_factor) {
        this.title_factor = title_factor;
    }

    public void setDesc_factor(String desc_factor) {
        this.desc_factor = desc_factor;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
