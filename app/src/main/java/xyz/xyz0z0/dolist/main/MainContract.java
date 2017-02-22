package xyz.xyz0z0.dolist.main;

import java.util.List;

import xyz.xyz0z0.dolist.BasePresenter;
import xyz.xyz0z0.dolist.BaseView;
import xyz.xyz0z0.dolist.data.Task;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public interface MainContract {

    interface Presenter extends BasePresenter {

        void loadTasks();

        void addNewTask(Task newTask);

        void deleteTask(String deleteTaskId);

        void completeTask(String completeTaskId,String completeTaskTitle);

        void activateTask(String activateTaskId,String activateTaskTitle);

        void clearCompletedTask();
    }

    interface View extends BaseView<Presenter> {

        void showDate(String str);

        void showTasks(List<Task> tasks);

        void showTaskDetails(String taskTitle);

        void showNoTasks();
    }
}
