package it.uni.sim.studymato.model;

import java.util.Date;

public class Exam {

    private String name;
    private int credits;
    private Date dueDate;

    public Exam(String name, int credits, Date dueDate) {
        this.name = name;
        this.credits = credits;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
