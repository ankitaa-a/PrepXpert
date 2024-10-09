package com.example.prepxpert;

public class JobDetails {

    String username,jobrole,jobdesc,interviewid;

    public String getInterviewid() {
        return interviewid;
    }

    public void setInterviewid(String interviewid) {
        this.interviewid = interviewid;
    }

    long createdDate;

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    String ans1,ans2,ans3,ans4,ans5;
    String userans1,userans2,userans3,userans4,userans5;


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

    String que1,que2,que3,que4,que5;
    int yrsofexp;

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

    public String getQue1() {
        return que1;
    }

    public void setQue1(String que1) {
        this.que1 = que1;
    }

    public String getQue2() {
        return que2;
    }

    public void setQue2(String que2) {
        this.que2 = que2;
    }

    public String getQue3() {
        return que3;
    }

    public void setQue3(String que3) {
        this.que3 = que3;
    }

    public String getQue4() {
        return que4;
    }

    public void setQue4(String que4) {
        this.que4 = que4;
    }

    public String getQue5() {
        return que5;
    }

    public void setQue5(String que5) {
        this.que5 = que5;
    }

    public void JobQues(String que1,String que2,String que3,String que4,String que5){
        this.que1=que1;
        this.que2=que2;
        this.que3=que3;
        this.que4=que4;
        this.que5=que5;
    }

    public void JobAns(String ans1,String ans2,String ans3,String ans4,String ans5){
        this.ans1=ans1;
        this.ans2=ans2;
        this.ans3=ans3;
        this.ans4=ans4;
        this.ans5=ans5;
    }

    public void JobUserAns(String userans1,String userans2,String userans3,String userans4,String userans5){
        this.userans1=userans1;
        this.userans2=userans2;
        this.userans3=userans3;
        this.userans4=userans4;
        this.userans5=userans5;
    }

    public JobDetails(String username,String jobrole,String jobdesc,int yrsofexp,long createdDate,String interviewid){
        this.username=username;
        this.jobrole=jobrole;
        this.jobdesc=jobdesc;
        this.yrsofexp=yrsofexp;
        this.createdDate = createdDate;
        this.interviewid=interviewid;
    }

    public JobDetails(){}

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

    public int getYrsofexp() {
        return yrsofexp;
    }

    public void setYrsofexp(int yrsofexp) {
        this.yrsofexp = yrsofexp;
    }
}
