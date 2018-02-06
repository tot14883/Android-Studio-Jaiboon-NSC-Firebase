package test.jaiboondemand.NewsFuction;

/**
 * Created by User on 1/30/2018.
 */

public class CommentFirebase {
    private String comment,User,timeCreated;
    public CommentFirebase(){}
    public CommentFirebase(String timeCreated,String comment,String User){
        this.timeCreated = timeCreated;
        this.comment = comment;
        this.User = User;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return User;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }
}
