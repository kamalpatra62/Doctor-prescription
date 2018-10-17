package net.chrysaetos.myreports;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class dataStore {

    private static dataStore single_instance = null;

    public String s;
    private List lUsers = null;
    private List lUsersKey = null;


    private dataStore()
    {
        s = "Hello I am a string part of Singleton class";
        lUsers = new ArrayList<>();
        lUsersKey = new ArrayList<>();
    }

    public static dataStore getInstance()
    {
        if (single_instance == null)
            single_instance = new dataStore();

        return single_instance;
    }

    public List getlUsers() {
        return this.lUsers;
    }

    public List getlUsersKey() {
        return this.lUsersKey;
    }

    public void addTolUsersKey(String value) {
        lUsersKey.add(value);
    }

    public void addTolUsers(addProfile.users user) {
        lUsers.add(user);
    }
}
