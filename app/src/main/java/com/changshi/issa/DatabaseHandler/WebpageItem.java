package com.changshi.issa.DatabaseHandler;

public class WebpageItem
{
    private Long id;
    private String department;
    private String DocumentID;
    private String email;
    private String address;
    private String contact;
    private String imageUrl;


    public WebpageItem(){

    }

    public WebpageItem( Long id, String department, String email, String contact, String address){

        this.id = id;
        this.department = department;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }
    public Long getId() {
        return id;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String docID){ DocumentID = docID; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
