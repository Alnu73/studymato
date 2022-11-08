package it.uni.sim.studymato.model;


public class StudySession {
    private Exam exam;
    private long date;
    private long duration;

    public StudySession(Exam exam, long date, long duration) {
        this.exam = exam;
        this.date = date;
        this.duration = duration;
    }

    public Exam getExam() {
        return exam;
    }

    public long getDate() {
        return date;
    }

    public long getDuration() {
        return duration;
    }
}
