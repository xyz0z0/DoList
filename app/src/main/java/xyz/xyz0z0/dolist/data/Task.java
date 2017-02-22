package xyz.xyz0z0.dolist.data;

import android.support.annotation.NonNull;

import com.yalantis.beamazingtoday.interfaces.BatModel;

import java.util.UUID;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class Task implements BatModel {

    @NonNull
    private String id;

    private String title;

    private boolean isChecked;

    public Task(String title) {
        this(UUID.randomUUID().toString(), title, false);
    }

    public Task(String id, String title, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.isChecked = isChecked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean b) {
        isChecked = b;
    }

    @Override
    public String getText() {
        return getTitle();
    }
}
