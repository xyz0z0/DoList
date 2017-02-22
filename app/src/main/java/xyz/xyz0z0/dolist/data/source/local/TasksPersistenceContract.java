package xyz.xyz0z0.dolist.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class TasksPersistenceContract {

    private TasksPersistenceContract(){

    }

    public static abstract class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
