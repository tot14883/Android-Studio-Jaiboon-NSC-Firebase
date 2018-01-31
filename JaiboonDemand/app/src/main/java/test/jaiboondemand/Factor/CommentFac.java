package test.jaiboondemand.Factor;

/**
 * Created by User on 1/30/2018.
 */

public class CommentFac {
    private String comment,User,timeCreated;
    public CommentFac(){

    }
    public CommentFac(String comment,String User,String timeCreated){
        this.comment = comment;
        this.User = User;
        this.timeCreated = timeCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getUser() {
        return User;
    }

    public String getComment() {
        return comment;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
