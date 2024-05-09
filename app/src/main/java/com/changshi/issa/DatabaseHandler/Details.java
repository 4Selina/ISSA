package com.changshi.issa.DatabaseHandler;

import java.io.Serializable;

public class Details implements Serializable
{
    private Long ID;
    private String DocumentID;
    private String detail;
    private String link;

    public Long getID(){return this.ID;}
    public void setID(Long id){this.ID = id;}

    public String getDocumentID(){return this.DocumentID;}
    public void setDocumentID(String docID){this.DocumentID = docID;}

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
