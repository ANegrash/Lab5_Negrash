package src;

import java.time.LocalDate;

public class Person {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.LocalDate birthday; //Поле может быть null
    private float height; //Значение поля должно быть больше 0
    private Country nationality; //Поле может быть null

    public Person(String nameP, LocalDate birthdayP, float heightP, Country nationalityP) {
        name=nameP;
        birthday=birthdayP;
        height=heightP;
        nationality=nationalityP;
    }
    String getInfoPerson(){
        return name+";"+birthday+";"+height+";"+nationality+";";
    }
    public void setAName(String aName){
        name=aName;
    }

    public void setABirth(LocalDate bd){
        birthday=bd;
    }
    public void setAHeight(float h){
        height=h;
    }
    public void setACountry(Country nat){
        nationality=nat;
    }
}
