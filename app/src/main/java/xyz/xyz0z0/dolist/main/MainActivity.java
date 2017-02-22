package xyz.xyz0z0.dolist.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;
import com.yalantis.beamazingtoday.util.TypefaceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.xyz0z0.dolist.R;
import xyz.xyz0z0.dolist.data.Task;
import xyz.xyz0z0.dolist.data.source.TasksRepository;
import xyz.xyz0z0.dolist.data.source.local.TasksLocalDataSource;

public class MainActivity extends AppCompatActivity implements MainContract.View, BatListener, OnItemClickListener, OnOutsideClickedListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mTasks;
    private BatItemAnimator mAnimator;

    private MainContract.Presenter mcPresenter;
    private MainPresenter mPresenter;
    private ActionBar actionBar;
    private TextView text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");


        text_title = ((TextView) findViewById(R.id.text_title));
        text_title.setTypeface(TypefaceUtil.getAvenirTypeface(this));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        showDate(str);


        mRecyclerView = (BatRecyclerView) findViewById(R.id.bat_recycler_view);
        mAnimator = new BatItemAnimator();


        mTasks = new ArrayList<BatModel>();
        mAdapter = new BatAdapter(mTasks, this, mAnimator);

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.getView().setAdapter(mAdapter.setOnItemClickListener(this).setOnOutsideClickListener(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });


        mPresenter = new MainPresenter(TasksRepository.getINSTANCE(TasksLocalDataSource.getINSTANCE(this)), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void add(String s) {
        Task newTask = new Task(s);
        mTasks.add(0, newTask);
        mcPresenter.addNewTask(newTask);
        mAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int i) {
        mcPresenter.deleteTask(mTasks.get(i).getId());
        mTasks.remove(i);
        mAdapter.notify(AnimationType.REMOVE, i);
    }

    @Override
    public void move(int from, int to) {
        if (from >= 0 && to >= 0) {
            mAnimator.setPosition(to);
            BatModel model = mTasks.get(from);

            if (model.isChecked()) {
                //true 当前是完成的，选中后会变成未完成
                mcPresenter.activateTask(mTasks.get(from).getId(), mTasks.get(from).getText());
            } else {
                mcPresenter.completeTask(mTasks.get(from).getId(), mTasks.get(from).getText());
            }
            mTasks.remove(model);
            mTasks.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);
            if (from == 0 || to == 0) {
                mRecyclerView.getView().scrollToPosition(Math.min(from, to));
            }
        }
    }

    @Override
    public void onClick(BatModel batModel, int i) {
        Toast.makeText(this, batModel.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }


    @Override
    public void showDate(String str) {
        text_title.setText(str);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        // present 初始化获取数据后会调用这里

        for (Task task : tasks) {
            mTasks.add(task);
        }


        //

    }

    @Override
    public void showTaskDetails(String taskTitle) {
        Toast.makeText(this, taskTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoTasks() {
//        text_title.setText("已经完成所有事项！");
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mcPresenter = presenter;
    }


}
