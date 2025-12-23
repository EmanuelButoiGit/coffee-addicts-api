package com.emanuel.coffee.addicts.dtos;

import java.time.LocalDateTime;

public class ServerTestDto {
    private String status;
    private String date;

    public ServerTestDto(){}

    public ServerTestDto(String status) {
        this.status = status;
        this.date = LocalDateTime.now().toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}
