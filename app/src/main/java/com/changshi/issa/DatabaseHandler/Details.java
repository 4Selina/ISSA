package com.changshi.issa.DatabaseHandler;

import java.io.Serializable;

public class Details implements Serializable
{
    private String Detail;
    private String Link;

    public String getDetail()
    {
        return  Detail;
    }

    public void setDetail(String NewDetail)
    {
        Detail = NewDetail;
    }

    public String getLink() { return Link;  }
    public void setLink(String NewLink) { Link = NewLink; }

}
