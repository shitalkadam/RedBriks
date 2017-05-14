package com.example.android.redbriks;

/**
 * Created by shital on 10/11/16.
 */
public class SearchPreference {

    //to check on the github again
    private String cityPreference,ageRangePreference,familySizePreference, rentSizePreference;

    public String getCityPreference() {
        return cityPreference;
    }

    public void setCityPreference(String cityPreference) {
        this.cityPreference = cityPreference;
    }

    public String getAgeRangePreference() {
        return ageRangePreference;
    }

    public void setAgeRangePreference(String ageRangePreference) {
        this.ageRangePreference = ageRangePreference;
    }

    public String getRentSizePreference() {
        return rentSizePreference;
    }

    public void setRentSizePreference(String rentSizePreference) {
        this.rentSizePreference = rentSizePreference;
    }

    public String getFamilySizePreference() {
        return familySizePreference;
    }

    public void setFamilySizePreference(String familySizePreference) {
        this.familySizePreference = familySizePreference;
    }
}
