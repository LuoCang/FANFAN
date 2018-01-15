package com.fanfan.robot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fanfan.novel.activity.AddNavigationActivity;
import com.fanfan.novel.activity.FaceDataActivity;
import com.fanfan.novel.common.activity.BarBaseActivity;
import com.fanfan.novel.model.NavigationBean;
import com.fanfan.novel.utils.DialogUtils;
import com.fanfan.robot.R;
import com.fanfan.robot.db.DanceDBManager;
import com.fanfan.robot.model.Dance;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by android on 2018/1/11.
 */

public class DanceAddActivity extends BarBaseActivity {

    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.tv_dance)
    TextView tvDance;

    public static final String DANCE_ID = "dance_id";
    public static final int ADD_DANCE_REQUESTCODE = 225;
    public static final int ADD_DANCE_RESULTCODE = 225;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, DanceAddActivity.class);
        context.startActivity(intent);
    }

    public static void newInstance(Context context, Fragment fragment, int requestCode) {
        Intent intent = new Intent(context, DanceAddActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void newInstance(Activity context, Fragment fragment, long id, int requestCode) {
        Intent intent = new Intent(context, DanceAddActivity.class);
        intent.putExtra(DANCE_ID, id);
        fragment.startActivityForResult(intent, requestCode);
    }

    private long saveLocalId;

    private DanceDBManager mDanceDBManager;

    private Dance dance;

    private int curDance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_dance;
    }

    @Override
    protected void initData() {
        saveLocalId = getIntent().getLongExtra(DANCE_ID, -1);

        mDanceDBManager = new DanceDBManager();

        if (saveLocalId != -1) {
            dance = mDanceDBManager.selectByPrimaryKey(saveLocalId);
            etQuestion.setText(dance.getTitle());
            curDance = valueForArray(R.array.dance, dance.getOrder());
        }
        tvDance.setText(resArray(R.array.dance)[curDance]);
    }

    @OnClick({R.id.tv_dance})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dance:
                DialogUtils.showLongListDialog(DanceAddActivity.this, "舞蹈名称", R.array.dance, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        curDance = position;
                        tvDance.setText(text);
                    }
                });
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.finish_white, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:

                if (isEmpty(etQuestion)) {
                    showToast("名称不能为空！");
                    break;
                }
                if (saveLocalId == -1) {//直接添加，判断是否存在
                    List<Dance> been = mDanceDBManager.queryDanceByTitle(etQuestion.getText().toString().trim());
                    if (!been.isEmpty()) {
                        showToast("请不要添加相同的名称！");
                        break;
                    }
                }
                danceIsexit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void danceIsexit() {
        if (dance == null) {
            dance = new Dance();
        }
        dance.setTime(System.currentTimeMillis());
        dance.setTitle(getText(etQuestion));
        dance.setOrder(resArray(R.array.dance)[curDance]);
        dance.setOrderData(resArray(R.array.dance_data)[curDance]);
        if (saveLocalId == -1) {
            mDanceDBManager.insert(dance);
        } else {
            dance.setId(saveLocalId);
            mDanceDBManager.update(dance);
        }
        setResult(ADD_DANCE_RESULTCODE);
        finish();
    }


    private int valueForArray(int resId, String compare) {
        String[] arrays = resArray(resId);
        return Arrays.binarySearch(arrays, compare);
    }

    private boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().equals("") || textView.getText().toString().trim().equals("");
    }

    private String[] resArray(int resId) {
        return getResources().getStringArray(resId);
    }

    private String getText(TextView textView) {
        return textView.getText().toString().trim();
    }


}