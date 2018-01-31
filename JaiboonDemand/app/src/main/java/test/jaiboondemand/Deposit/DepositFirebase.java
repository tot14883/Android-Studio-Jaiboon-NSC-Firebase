package test.jaiboondemand.Deposit;

/**
 * Created by User on 1/30/2018.
 */

public class DepositFirebase {
    private String Topic_Deposit,image;
    public DepositFirebase(){}
    public DepositFirebase(String Topic_Deposit,String image){
        this.Topic_Deposit = Topic_Deposit;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getTopic_Deposit() {
        return Topic_Deposit;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTopic_Deposit(String topic_Deposit) {
        Topic_Deposit = topic_Deposit;
    }
}
