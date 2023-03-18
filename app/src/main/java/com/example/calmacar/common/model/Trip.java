package com.example.calmacar.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.calmacar.utils.Formatter;

import java.util.Comparator;

public class Trip implements Parcelable {
    private String id, startCity, endCity, date, startTime, endTime, description;
    private float price;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public Trip() {}

    public Trip(String startCity, String endCity, String date, String startTime, String endTime, String price, String description) {
        this.id = Formatter.getInstance().generateID(5);
        this.startCity = startCity;
        this.endCity = endCity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = Float.parseFloat(price.replace("€", ""));
        this.description = description;
    }

    /**
     * Tells if the trip will occur after the time given in the argument
     * @param other the time to compare to
     * @return
     */
    public boolean isStartTimeAfter(String other){
        if (this.startTime == null)
            return false;
        Formatter formatter = Formatter.getInstance();
        return formatter.timeToInt(this.startTime) >= formatter.timeToInt(other);
    }

    // Parcelable Implementation
    protected Trip(Parcel in) {
        id = in.readString();
        startCity = in.readString();
        endCity = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        description = in.readString();
        price = in.readFloat();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(startCity);
        parcel.writeString(endCity);
        parcel.writeString(date);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(description);
        parcel.writeFloat(price);
    }

    // Comparators
    public static Comparator<Trip> PriceAscComparator = new Comparator<Trip>() {
        @Override
        public int compare(Trip trip1, Trip trip2) {
            return Float.compare(trip1.getPrice(), trip2.getPrice());
        }
    };

    public static Comparator<? super Trip> PriceDscComparator = new Comparator<Trip>() {
        @Override
        public int compare(Trip trip1, Trip trip2) {
            return Float.compare(trip2.getPrice(), trip1.getPrice());
        }
    };

    public static Comparator<Trip> StartTimeAscComparator = new Comparator<Trip>() {
        @Override
        public int compare(Trip trip1, Trip trip2) {
            Formatter formatter = Formatter.getInstance();
            return formatter.timeToInt(trip1.getStartTime()) - formatter.timeToInt(trip2.getStartTime());
        }
    };

    public static Comparator<Trip> StartTimeDscComparator = new Comparator<Trip>() {
        @Override
        public int compare(Trip trip1, Trip trip2) {
            Formatter formatter = Formatter.getInstance();
            return formatter.timeToInt(trip2.getStartTime()) - formatter.timeToInt(trip1.getStartTime());
        }
    };

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", startCity='" + startCity + '\'' +
                ", endCity='" + endCity + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
