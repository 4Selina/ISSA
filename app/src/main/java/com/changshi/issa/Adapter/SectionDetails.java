package com.changshi.issa.Adapter;

import android.telecom.Call;

import com.changshi.issa.SupportDetails;

import java.util.ArrayList;
import java.util.List;

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
