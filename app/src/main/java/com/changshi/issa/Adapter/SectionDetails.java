package com.changshi.issa.Adapter;

import com.changshi.issa.DatabaseHandler.Details;

import java.util.ArrayList;

public class SectionDetails
{
    private String Heading;
    private ArrayList<Details> SectionDetails;

    public String getSectionHeading()
    {
        return Heading;
    }

    public ArrayList<Details> getSectionDetails()
    {
        return SectionDetails;
    }

    public void setSectionHeading(String NewHeading)
    {
        Heading = NewHeading;
    }

    public void setSectionDetails(ArrayList<Details> NewSectionDetails)
    {
        SectionDetails = NewSectionDetails;
    }

}
