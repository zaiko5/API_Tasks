package com.taskList.Domain;
//POJO Class.
public class Task {

    //Tasks atributes
    private Integer id;
    private String petition;
    private String status;

    public Task(Integer id, String petition, String status) {
        this.id = id;
        this.petition = petition;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPetition() {
        return petition;
    }

    public void setPetition(String petition) {
        this.petition = petition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
