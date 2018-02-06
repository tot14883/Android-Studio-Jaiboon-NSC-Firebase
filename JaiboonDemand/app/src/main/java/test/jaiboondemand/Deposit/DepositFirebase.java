package test.jaiboondemand.Deposit;

/**
 * Created by User on 1/30/2018.
 */

public class DepositFirebase {
    private String Topic_Deposit,image,time_deposit;
    public DepositFirebase(){}
    public DepositFirebase(String Topic_Deposit,String image,String time_deposit){
        this.Topic_Deposit = Topic_Deposit;
        this.image = image;
        this.time_deposit = time_deposit;
    }

    public String getTime_deposit() {
        return time_deposit;
    }

    public void setTime_deposit(String time_deposit) {
        this.time_deposit = time_deposit;
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
