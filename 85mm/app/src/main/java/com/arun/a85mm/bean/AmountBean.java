package com.arun.a85mm.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AmountBean implements Parcelable {
    public int coin;
    public String paidText;
    public List<UploadImageBean> paidImageList;

    public AmountBean(int coin, String paidText, List<UploadImageBean> paidImageList) {
        this.coin = coin;
        this.paidText = paidText;
        this.paidImageList = paidImageList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(coin);
        dest.writeString(paidText);
        dest.writeTypedList(paidImageList);
        dest.writeList(paidImageList);
    }

    public static final Parcelable.Creator<AmountBean> CREATOR = new Creator<AmountBean>() {
        @Override
        public AmountBean[] newArray(int size) {
            return new AmountBean[size];
        }

        @Override
        public AmountBean createFromParcel(Parcel in) {
            return new AmountBean(in);
        }
    };

    public AmountBean(Parcel in) {
        coin = in.readInt();
        paidText = in.readString();
        List<UploadImageBean> paidImageList = new ArrayList<>();
        in.readTypedList(paidImageList, UploadImageBean.CREATOR);
        this.paidImageList = paidImageList;
    }
}
