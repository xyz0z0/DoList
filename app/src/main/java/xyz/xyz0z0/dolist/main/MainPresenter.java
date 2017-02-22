package xyz.xyz0z0.dolist.main;

import java.util.List;

import xyz.xyz0z0.dolist.data.Task;
import xyz.xyz0z0.dolist.data.source.TasksDataSource;
import xyz.xyz0z0.dolist.data.source.TasksRepository;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private final TasksRepository mTasksRespository;

    private final MainContract.View mTasksView;

    public MainPresenter(TasksRepository tasksRepository, MainContract.View mainView) {
        mTasksRespository = tasksRepository;
        mTasksView = mainView;
        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks();
    }

    @Override
    public void loadTasks() {
        mTasksRespository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                mTasksView.showTasks(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksView.showNoTasks();
            }
        });
    }

    @Override
    public void addNewTask(Task newTask) {
        mTasksRespository.saveTask(newTask);
    }

    @Override
    public void deleteTask(String deleteTaskId) {
        mTasksRespository.deleteTask(deleteTaskId);
    }

    @Override
    public void completeTask(String completeTaskId, String completeTaskTitle) {
        mTasksRespository.completeTask(completeTaskId, completeTaskTitle);
    }

    @Override
    public void activateTask(String activateTaskId, String activateTaskTitle) {
        mTasksRespository.activateTask(activateTaskId, activateTaskTitle);
    }

    @Override
    public void clearCompletedTask() {
        mTasksRespository.clearCompletedTasks();
    }

}
