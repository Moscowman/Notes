package ru.varasoft.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Note implements Parcelable {
    private String title;
    private String text;
    private Date creationDateTime;
    private Date modificationDateTime;
    private String creator;

    public Note(String title, String text, String creator) {
        this.title = title;
        this.text = text;
        this.creator = creator;
        creationDateTime = Calendar.getInstance().getTime();
        modificationDateTime = creationDateTime;
    }

    public Note(String title, String text, String creator, Date creationDateTime, Date modificationDateTime) {
        this.title = title;
        this.text = text;
        this.creator = creator;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
    }

    protected Note(Parcel in) {
        title = in.readString();
        text = in.readString();
        creator = in.readString();
        creationDateTime = new Date(in.readLong());
        modificationDateTime = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public Date getModificationDateTime() {
        return modificationDateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setModificationDateTime(Date modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(creator);
        dest.writeLong(creationDateTime.getTime());
        dest.writeLong(modificationDateTime.getTime());
    }
}
