package com.example.android.redbriks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shital on 10/9/16.
 */
public class House implements Parcelable{
    private int image_id;
    private String city;
    private int house_id;
    private String house_address;
    private int build_year;
    private int no_of_bedroom;
    private int property_area;
    private int price;


    public House(int image_id,String city,int house_id,String house_address,int build_year, int no_of_bedroom, int property_area,int price) {
        this.setImage_id(image_id);
        this.setCity(city);
        this.setHouse_id(house_id);
        this.setHouse_address(house_address);
        this.setBuild_year(build_year);
        this.setNo_of_bedroom(no_of_bedroom);
        this.setProperty_area(property_area);
        this.setPrice(price);
    }

    public House(){

    }
    public House(Parcel in) {
        this.setImage_id(in.readInt());
        this.setCity(in.readString());
        this.setHouse_id(in.readInt());
        this.setHouse_address(in.readString());
        this.setBuild_year(in.readInt());
        this.setNo_of_bedroom(in.readInt());
        this.setProperty_area(in.readInt());
        this.setPrice(in.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(image_id);
        dest.writeString(city);
        dest.writeInt(house_id);
        dest.writeString(house_address);
        dest.writeInt(build_year);
        dest.writeInt(no_of_bedroom);
        dest.writeInt(property_area);
        dest.writeInt(price);
    }

    public static final Parcelable.Creator<House> CREATOR = new Parcelable.Creator<House>() {
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        public House[] newArray(int size) {
            return new House[size];
        }
    };

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHouse_id() {
        return house_id;
    }

    public void setHouse_id(int house_id) {
        this.house_id = house_id;
    }

    public String getHouse_address() {
        return house_address;
    }

    public void setHouse_address(String house_address) {
        this.house_address = house_address;
    }

    public int getBuild_year() {
        return build_year;
    }

    public void setBuild_year(int build_year) {
        this.build_year = build_year;
    }

    public int getNo_of_bedroom() {
        return no_of_bedroom;
    }

    public void setNo_of_bedroom(int no_of_bedroom) {
        this.no_of_bedroom = no_of_bedroom;
    }

    public int getProperty_area() {
        return property_area;
    }

    public void setProperty_area(int property_area) {
        this.property_area = property_area;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
