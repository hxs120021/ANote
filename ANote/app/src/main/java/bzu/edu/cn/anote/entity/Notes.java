package bzu.edu.cn.anote.entity;

/**
 * Created by 李小宁 on 2017/homepage/25.
 */

public class Notes {
    private long id;
    private String user;
    private String notesname;
    private String subarea;
    private String title;
    private String data;
    private String content;
    private String time;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Notes(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Notes(long id, String user, String notesname, String subarea, String title, String data, String content, String time) {
        this.id = id;
        this.user = user;
        this.notesname = notesname;
        this.subarea = subarea;
        this.title = title;
        this.data = data;
        this.content = content;
        this.time = time;
    }

    public Notes(String user, String notesname, String subarea, String title, String data, String content, String time) {
        this.user = user;
        this.notesname = notesname;
        this.subarea = subarea;
        this.title = title;
        this.data = data;
        this.content = content;
        this.time = time;
    }

    public Notes(String title, String data, String content, String time) {
        this.title = title;
        this.data = data;
        this.content = content;
        this.time = time;
    }

    public Notes(String title, long id) {
        this.title = title;
        this.id = id;
    }
}

