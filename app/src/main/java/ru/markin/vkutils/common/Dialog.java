package ru.markin.vkutils.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(exclude = "id")
public class Dialog implements Comparable<Dialog> {

    @Getter private final int id;
    @Getter @Setter private String name;
    @Getter @Setter private String photo;
    @Getter @Setter private long date;
    @Getter @Setter private String dateText;

    public Dialog(int id) {
        this.id = id;
    }

    public Dialog(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public Dialog(int id, String name, String photo, long date) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.date = date;
    }

    public Dialog(int id, String name, String photo, long date, String dateText) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.date = date;
        this.dateText = dateText;
    }

    @Override
    public int compareTo(Dialog o) {
        return this.id < o.id ? -1 : this.id == o.id ? 0 : 1;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", date=" + date +
                ", dateText='" + dateText + '\'' +
                '}';
    }
}
