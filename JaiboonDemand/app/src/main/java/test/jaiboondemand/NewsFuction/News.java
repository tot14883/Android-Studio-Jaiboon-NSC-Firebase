package test.jaiboondemand.NewsFuction;

/**
 * Created by User on 1/4/2018.
 */

public class News {
    private String topicname,descname,imagenews;
    public News(){}
    public News(String topicname,String descname,String imagenews){
        this.topicname = topicname;
        this.descname = descname;
        this.imagenews = imagenews;
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
