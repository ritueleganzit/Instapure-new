package com.eleganzit.instapure.model;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SampleSearchModel implements Searchable {
    private String comp_id,comp_name;

    public SampleSearchModel(String comp_id, String comp_name) {
        this.comp_id = comp_id;
        this.comp_name = comp_name;
    }

    @Override
    public String getTitle() {
        return comp_name;
    }

    public SampleSearchModel setTitle(String comp_name) {
        comp_name = comp_name;
        return this;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

}