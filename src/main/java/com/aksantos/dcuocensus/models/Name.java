package com.aksantos.dcuocensus.models;

import java.io.Serializable;

public class Name implements Serializable {
    private static final long serialVersionUID = -73454851917165230L;

    private String en;
    private String it;
    private String de;
    private String es;
    private String fr;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

}
