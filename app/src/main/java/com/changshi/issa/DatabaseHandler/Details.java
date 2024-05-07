package com.changshi.issa.DatabaseHandler;

import java.io.Serializable;

public class Details implements Serializable
{
    private String Detail;

    public String getDetail()
    {
        return  Detail;
    }

    public void setDetail(String NewDetail)
    {
        Detail = NewDetail;
    }
}
