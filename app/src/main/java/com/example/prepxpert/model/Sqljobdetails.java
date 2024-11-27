package com.example.prepxpert.model;

public class Sqljobdetails {
    private String email,username,jobrole,jobdesc,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private long createdDate;
    private String ans1,ans2,ans3,ans4,ans5,feed1,feed2,feed3,feed4,feed5;
    private String userans1,userans2,userans3,userans4,userans5;
    private String ques1,ques2,ques3,ques4,ques5;
    private int rat1,rat2,rat3,rat4,rat5;
    private int yrsofexp;

    public Sqljobdetails(){}

    public Sqljobdetails(String id,String email, String username, String jobrole, String jobdesc, long createdDate, String ans1, String ans2, String ans3, String ans4, String ans5, String feed1, String feed2, String feed3, String feed4, String feed5, String userans1, String userans2, String userans3, String userans4, String userans5, String ques1, String ques2, String ques3, String ques4, String ques5, int rat1, int rat2, int rat3, int rat4, int rat5, int yrsofexp) {
        this.email = email;
        this.id=id;
        this.username = username;
        this.jobrole = jobrole;
        this.jobdesc = jobdesc;
        this.createdDate = createdDate;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.ans5 = ans5;
        this.feed1 = feed1;
        this.feed2 = feed2;
        this.feed3 = feed3;
        this.feed4 = feed4;
        this.feed5 = feed5;
        this.userans1 = userans1;
        this.userans2 = userans2;
        this.userans3 = userans3;
        this.userans4 = userans4;
        this.userans5 = userans5;
        this.ques1 = ques1;
        this.ques2 = ques2;
        this.ques3 = ques3;
        this.ques4 = ques4;
        this.ques5 = ques5;
        this.rat1 = rat1;
        this.rat2 = rat2;
        this.rat3 = rat3;
        this.rat4 = rat4;
        this.rat5 = rat5;
        this.yrsofexp = yrsofexp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobrole() {
        return jobrole;
    }

    public void setJobrole(String jobrole) {
        this.jobrole = jobrole;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public void setAns4(String ans4) {
        this.ans4 = ans4;
    }

    public String getAns5() {
        return ans5;
    }

    public void setAns5(String ans5) {
        this.ans5 = ans5;
    }

    public String getFeed1() {
        return feed1;
    }

    public void setFeed1(String feed1) {
        this.feed1 = feed1;
    }

    public String getFeed2() {
        return feed2;
    }

    public void setFeed2(String feed2) {
        this.feed2 = feed2;
    }

    public String getFeed3() {
        return feed3;
    }

    public void setFeed3(String feed3) {
        this.feed3 = feed3;
    }

    public String getFeed4() {
        return feed4;
    }

    public void setFeed4(String feed4) {
        this.feed4 = feed4;
    }

    public String getFeed5() {
        return feed5;
    }

    public void setFeed5(String feed5) {
        this.feed5 = feed5;
    }

    public String getUserans1() {
        return userans1;
    }

    public void setUserans1(String userans1) {
        this.userans1 = userans1;
    }

    public String getUserans2() {
        return userans2;
    }

    public void setUserans2(String userans2) {
        this.userans2 = userans2;
    }

    public String getUserans3() {
        return userans3;
    }

    public void setUserans3(String userans3) {
        this.userans3 = userans3;
    }

    public String getUserans4() {
        return userans4;
    }

    public void setUserans4(String userans4) {
        this.userans4 = userans4;
    }

    public String getUserans5() {
        return userans5;
    }

    public void setUserans5(String userans5) {
        this.userans5 = userans5;
    }

    public String getQues1() {
        return ques1;
    }

    public void setQues1(String ques1) {
        this.ques1 = ques1;
    }

    public String getQues2() {
        return ques2;
    }

    public void setQues2(String ques2) {
        this.ques2 = ques2;
    }

    public String getQues3() {
        return ques3;
    }

    public void setQues3(String ques3) {
        this.ques3 = ques3;
    }

    public String getQues4() {
        return ques4;
    }

    public void setQues4(String ques4) {
        this.ques4 = ques4;
    }

    public String getQues5() {
        return ques5;
    }

    public void setQues5(String ques5) {
        this.ques5 = ques5;
    }

    public int getRat1() {
        return rat1;
    }

    public void setRat1(int rat1) {
        this.rat1 = rat1;
    }

    public int getRat2() {
        return rat2;
    }

    public void setRat2(int rat2) {
        this.rat2 = rat2;
    }

    public int getRat3() {
        return rat3;
    }

    public void setRat3(int rat3) {
        this.rat3 = rat3;
    }

    public int getRat4() {
        return rat4;
    }

    public void setRat4(int rat4) {
        this.rat4 = rat4;
    }

    public int getRat5() {
        return rat5;
    }

    public void setRat5(int rat5) {
        this.rat5 = rat5;
    }

    public int getYrsofexp() {
        return yrsofexp;
    }

    public void setYrsofexp(int yrsofexp) {
        this.yrsofexp = yrsofexp;
    }
}
