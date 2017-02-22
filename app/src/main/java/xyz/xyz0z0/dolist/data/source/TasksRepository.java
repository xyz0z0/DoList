package xyz.xyz0z0.dolist.data.source;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import xyz.xyz0z0.dolist.data.Task;
import xyz.xyz0z0.dolist.data.source.local.TasksLocalDataSource;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;
    private final TasksLocalDataSource mTaskLocalDataSource;
    Map<String, Task> mCachedTasks;
    boolean mCacheIsDirty = false;

    public TasksRepository(@NonNull TasksLocalDataSource tasksLocalDataSource) {
        mTaskLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getINSTANCE(TasksLocalDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        mTaskLocalDataSource.getTasks(callback);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        mTaskLocalDataSource.saveTask(task);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull String completeTaskId, @NonNull String completeTaskTitle) {
        mTaskLocalDataSource.completeTask(completeTaskId, completeTaskTitle);

        Task completedTask = new Task(completeTaskId, completeTaskTitle, true);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(completeTaskId, completedTask);
    }

    @Override
    public void activateTask(@NonNull String activateTaskId, @NonNull String activateTaskTitle) {
        mTaskLocalDataSource.activateTask(activateTaskId, activateTaskTitle);

        Task activeTask = new Task(activateTaskId, activateTaskTitle, false);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(activateTaskId, activeTask);
    }

    @Override
    public void clearCompletedTasks() {
        mTaskLocalDataSource.clearCompletedTasks();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isChecked()) {
                it.remove();
            }
        }
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTaskLocalDataSource.deleteTask(taskId);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.remove(taskId);
    }
}
