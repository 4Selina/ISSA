package com.changshi.issa.DatabaseHandler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DbReference {
       public static DatabaseReference push() {
        String path = null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        DatabaseReference pushedRef = reference.push();
        Object value;
        value = null;
        pushedRef.setValue(value);
        return pushedRef;
    }
}
