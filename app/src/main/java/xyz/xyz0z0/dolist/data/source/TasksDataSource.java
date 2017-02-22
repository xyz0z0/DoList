package xyz.xyz0z0.dolist.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import xyz.xyz0z0.dolist.data.Task;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public interface TasksDataSource {

    void getTasks(@NonNull LoadTasksCallback callback);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull String completeTaskId, @NonNull String completeTaskTitle);

    void activateTask(@NonNull String activateTaskId,@NonNull String activateTaskTitle);

    void clearCompletedTasks();

    void deleteTask(@NonNull String taskId);

    interface LoadTasksCallback {

        void onTaskLoaded(List<Task> tasks);

        void onDataNotAvailable();

    }
}
