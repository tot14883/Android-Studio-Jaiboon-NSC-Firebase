package test.jaiboondemand.Pay;

/**
 * Created by User on 1/3/2018.
 */

public class SendPlace {
    private String NameSend,AddSend,PostSend,ProSend;
    public SendPlace(){}
    public SendPlace(String NameSend,String AddSend,String PostSend,String ProSend){
        this.NameSend = NameSend;
        this.AddSend = AddSend;
        this.PostSend = PostSend;
        this.ProSend = ProSend;
    }

    public String getAddSend() {
        return AddSend;
    }

    public String getNameSend() {
        return NameSend;
    }

    public String getPostSend() {
        return PostSend;
    }

    public String getProSend() {
        return ProSend;
    }

    public void setAddSend(String addSend) {
        AddSend = addSend;
    }

    public void setNameSend(String nameSend) {
        NameSend = nameSend;
    }

    public void setPostSend(String postSend) {
        PostSend = postSend;
    }

    public void setProSend(String proSend) {
        ProSend = proSend;
    }
}