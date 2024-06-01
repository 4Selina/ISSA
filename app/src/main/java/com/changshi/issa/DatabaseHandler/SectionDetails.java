package com.changshi.issa.DatabaseHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionDetails implements Serializable
{
    Long ID;
    String DocumentID;
    private String heading;
    private ArrayList<Details> details;

    public SectionDetails(String heading, ArrayList<Details> details)
    {
        this.heading = heading;
        this.details = details;
    }

    public SectionDetails()
    {

    }

    public Long getID(){return this.ID;}
    public void setID(Long id){this.ID = id;}
    public String getDocumentID(){return this.DocumentID;}
    public void setDocumentID(String docID){this.DocumentID = docID;}
    public String getSectionHeading() {return heading;}
    public ArrayList<Details> getSectionDetails() {return details;}
    public void setSectionHeading(String heading) {this.heading = heading;}
    public void setSectionDetails(ArrayList<Details> newDetails) {this.details = newDetails;}
}

