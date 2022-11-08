package it.uni.sim.studymato.model;


public class Exam {

    private String name;
    private int credits;
    private long dueDate;

    public Exam() {
    }

    public Exam(String name, int credits, Long dueDate) {
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

    public Long getDueDate() {
        return dueDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return name;
    }
}
