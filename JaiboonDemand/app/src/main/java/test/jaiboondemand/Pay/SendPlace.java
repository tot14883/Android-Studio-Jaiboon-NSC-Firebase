package test.jaiboondemand.Pay;

/**
 * Created by User on 1/3/2018.
 */

public class SendPlace {
    private String NameSend,AddSend,PostSend,ProSend,PhoneSend;
    private boolean select;
    public SendPlace(){}
    public SendPlace(String PhoneSend,String NameSend,String AddSend,String PostSend,String ProSend,Boolean select){
        this.NameSend = NameSend;
        this.AddSend = AddSend;
        this.PostSend = PostSend;
        this.ProSend = ProSend;
        this.select = select;
        this.PhoneSend = PhoneSend;
    }

    public String getPhoneSend() {
        return PhoneSend;
    }

    public void setPhoneSend(String phoneSend) {
        PhoneSend = phoneSend;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
    public boolean getSelect(){return select;}


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
