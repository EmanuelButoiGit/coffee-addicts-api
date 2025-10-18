package com.emanuel.coffee.addicts;

import java.time.LocalDateTime;

public class ServerTest {
    private String status;
    private String date;

    public ServerTest(){}

    public ServerTest(String status) {
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
