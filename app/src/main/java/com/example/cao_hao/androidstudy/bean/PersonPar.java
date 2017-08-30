package com.example.cao_hao.androidstudy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cao-hao on 17-8-24.
 */

public class PersonPar implements Parcelable {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 序列化实体类
     */
    public static final Parcelable.Creator<PersonPar> CREATOR = new Creator<PersonPar>() {
        public PersonPar createFromParcel(Parcel source) {
            PersonPar personPar = new PersonPar();
            personPar.name = source.readString();
            personPar.age = source.readInt();
            return personPar;
        }

        public PersonPar[] newArray(int size) {
            return new PersonPar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将实体类数据写入Parcel
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(age);
    }

}
