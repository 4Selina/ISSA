package com.changshi.issa.DatabaseHandler;

import com.changshi.issa.SupportContent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHandler {

    private DatabaseReference mDatabase;

    public DBHandler() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("support_contents");
    }

    public void addSupportContent(SupportContent supportContent) {
        String key = mDatabase.push().getKey();
        mDatabase.child(key).setValue(supportContent);
    }

    public void deleteSupportContent(String key) {
        mDatabase.child(key).removeValue();
    }

    public void updateSupportContent(String key, SupportContent supportContent) {
        mDatabase.child(key).setValue(supportContent);
    }

    public DatabaseReference getAllSupportContents() {
        return mDatabase;
    }

    public DatabaseReference getSupportContent(String key) {
        return mDatabase.child(key);
    }


}
