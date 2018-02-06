package test.jaiboondemand.NewsFuction;

/**
 * Created by User on 1/4/2018.
 */

public class News {
    private String topicname,descname,imagenews,timenews;
    public News(){}
    public News(String topicname,String descname,String imagenews,String timenews){
        this.topicname = topicname;
        this.descname = descname;
        this.imagenews = imagenews;
        this.timenews = timenews;
    }

    public String getTimenews() {
        return timenews;
    }

    public void setTimenews(String timenews) {
        this.timenews = timenews;
    }

    public String getDescname() {
        return descname;
    }

    public String getImagenews() {
        return imagenews;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setDescname(String descname) {
        this.descname = descname;
    }

    public void setImagenews(String imagenews) {
        this.imagenews = imagenews;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }
}
