package xyz.xyz0z0.dolist.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import xyz.xyz0z0.dolist.data.Task;
import xyz.xyz0z0.dolist.data.source.TasksDataSource;
import xyz.xyz0z0.dolist.data.source.local.TasksPersistenceContract.TaskEntry;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDbHelper mDbHelper;

    private TasksLocalDataSource(@NonNull Context context) {
        mDbHelper = new TasksDbHelper(context);
    }

    public static TasksLocalDataSource getINSTANCE(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_COMPLETED
        };
        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, TaskEntry.COLUMN_NAME_COMPLETED);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                Task task = new Task(itemId, title, completed);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();
        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTaskLoaded(tasks);
        }

    }

    @Override
    public void saveTask(@NonNull Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isChecked());

        db.insert(TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void completeTask(@NonNull String completeTaskId,@NonNull String completeTaskTitle) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {completeTaskId};

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void activateTask(@NonNull String activateTaskId,@NonNull String activateTaskTitle) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {activateTaskId};

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void clearCompletedTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] seletionArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, selection, seletionArgs);
        db.close();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
