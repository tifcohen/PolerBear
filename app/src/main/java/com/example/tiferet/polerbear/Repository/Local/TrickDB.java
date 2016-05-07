package com.example.tiferet.polerbear.Repository.Local;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by TIFERET on 08-Apr-16.
 */
public class TrickDB {
    private final static TrickDB instance = new TrickDB();
    List<Trick> db = new LinkedList<Trick>();


    private TrickDB() {
        init();
    }

    public static TrickDB getInstance() {
        return instance;
    }

    private void init(){
        for (int i = 0; i < 10; i++) {
            Trick t = new Trick(i,"trick "+i,i,null,null);
            addTrick(t);
        }
    }

    public void addTrick(Trick trick){
        db.add(trick);
    }

    public List<Trick> getAllTricks(){
        return db;
    }
}
