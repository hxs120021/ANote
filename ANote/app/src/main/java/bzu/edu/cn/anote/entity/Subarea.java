package bzu.edu.cn.anote.entity;

/**
 * Created by 李小宁 on 2017/homepage/29.
 */

public class Subarea {
    private long id;
    private String user;
    private String notesname;
    private String subarea;

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

    public String getSubarea() {
        return subarea;
    }

    public void setSubarea(String subarea) {
        this.subarea = subarea;
    }

    public Subarea(String user, String subarea, String notesname) {
        this.user = user;
        this.subarea = subarea;
        this.notesname = notesname;
    }

    public Subarea(long id, String user, String subarea, String notesname) {
        this.id = id;
        this.user = user;
        this.notesname = notesname;
        this.subarea = subarea;
    }

    public Subarea(String subarea) {
        this.subarea = subarea;
    }
}
