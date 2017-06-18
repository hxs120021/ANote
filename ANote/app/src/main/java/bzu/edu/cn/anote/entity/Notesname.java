package bzu.edu.cn.anote.entity;

/**
 * Created by 李小宁 on 2017/homepage/28.
 */

public class Notesname {
    private long id;
    private String user;
    private String notesname;

    public Notesname(String notesname) {
        this.notesname = notesname;
    }

    public Notesname(String user, String notesname) {
        this.user = user;
        this.notesname = notesname;
    }

    public Notesname(long id, String user, String notesname) {
        this.id = id;
        this.user = user;
        this.notesname = notesname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNotesname() {
        return notesname;
    }

    public void setNotesname(String notesname) {
        this.notesname = notesname;
    }
}
