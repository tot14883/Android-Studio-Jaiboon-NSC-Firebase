package test.jaiboondemand.post_activity;

/**
 * Created by User on 12/28/2017.
 */

public class Comment {
    private String comment,User,timeCreated;
    public Comment(){}
    private Comment(String timeCreated,String comment,String User){
     this.timeCreated = timeCreated;
     this.comment = comment;
     this.User = User;
    }


    public String getComment() {
        return comment;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUser() {
        return User;
    }




    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(String user) {
        User = user;
    }
}
