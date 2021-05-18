package com.college.pojo;

public class AssignTaskList {
    private String t_id;
    private String e_id;
    private String t_title;
    private String t_description;
    private String t_address;
    private String t_status;
    private String t_time;

    public AssignTaskList(String t_id, String e_id, String t_title, String t_description, String t_address, String t_status, String t_time) {
        this.t_id = t_id;
        this.e_id = e_id;
        this.t_title = t_title;
        this.t_description = t_description;
        this.t_address = t_address;
        this.t_status = t_status;
        this.t_time = t_time;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getT_title() {
        return t_title;
    }

    public void setT_title(String t_title) {
        this.t_title = t_title;
    }

    public String getT_description() {
        return t_description;
    }

    public void setT_description(String t_description) {
        this.t_description = t_description;
    }

    public String getT_address() {
        return t_address;
    }

    public void setT_address(String t_address) {
        this.t_address = t_address;
    }

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }
}
