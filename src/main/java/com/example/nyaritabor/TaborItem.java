package com.example.nyaritabor;

import java.util.ArrayList;
import java.util.List;

public class TaborItem {

    private String id;
    private String nev;
    private String info;
    private String ar;
    private float csillag;
    private int kepforrasa;
    private int ferohely;
    private int jelentkezett_db;

    private String jelentkezettemail;


    public TaborItem() {}



    public TaborItem(String nev, String info, String ar, float csillag, int kepforrasa, int ferohely, int jelentkezett_db, String jelentkezettemail) {

        this.nev = nev;
        this.info = info;
        this.ar = ar;
        this.csillag = csillag;
        this.kepforrasa = kepforrasa;
        this.ferohely = ferohely;
        this.jelentkezett_db =jelentkezett_db;
        this.jelentkezettemail = jelentkezettemail;

    }

    public String getNev() {
        return nev;
    }
    public String getInfo() {
        return info;
    }
    public String getAr() {
        return ar;
    }
    public float getCsillag() {
        return csillag;
    }
    public int getKepforrasa() {
        return kepforrasa;
    }

    public int getFerohely() {
        return ferohely;
    }

    public int getJelentkezett_db() {
        return jelentkezett_db;
    }

    public void setJelentkezett_db(int jelentkezett_db) {
        this.jelentkezett_db = jelentkezett_db;
    }

    public String _getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getJelentkezettemail() {
        return jelentkezettemail;
    }

    public void setJelentkezettemail(String jelentkezettemail) {
        this.jelentkezettemail = jelentkezettemail;
    }
}
