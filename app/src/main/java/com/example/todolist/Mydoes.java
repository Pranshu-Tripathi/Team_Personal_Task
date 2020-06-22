package com.example.todolist;

public class Mydoes {

    String titledoes;
    String datedoes;
    String descdoes;
    String keydoes;
    String creationdoes;


    public Mydoes() {
    }

    public Mydoes(String titledoes, String datedoes, String descdoes, String keydoes, String creationdoes) {
        this.titledoes = titledoes;
        this.datedoes = datedoes;
        this.descdoes = descdoes;
        this.keydoes = keydoes;
        this.creationdoes = creationdoes;
    }

    public String getCreationdoes() {
        return creationdoes;
    }

    public void setCreationdoes(String creationdoes) {
        this.creationdoes = creationdoes;
    }

    public String getTitledoes() {
        return titledoes;
    }

    public void setTitledoes(String titledoes) {
        this.titledoes = titledoes;
    }

    public String getDatedoes() {
        return datedoes;
    }

    public void setDatedoes(String datedoes) {
        this.datedoes = datedoes;
    }

    public String getDescdoes() {
        return descdoes;
    }

    public void setDescdoes(String descdoes) {
        this.descdoes = descdoes;
    }

    public String getKeydoes() {
        return keydoes;
    }

    public void setKeydoes(String keydoes) {
        this.keydoes = keydoes;
    }


}
