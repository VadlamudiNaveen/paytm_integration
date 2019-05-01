package com.example.integration.models;

public class Student {
    public String htno,branch,subject;

    public Student(String htno, String branch, String subject) {
        this.htno = htno;
        this.branch = branch;
        this.subject = subject;
    }
    public Student (){}


    public String getHtno() {
        return htno;
    }

    public void setHtno(String htno) {
        this.htno = htno;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
