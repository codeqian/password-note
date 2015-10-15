package codepig.passnote.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import codepig.passnote.R;
import codepig.passnote.Utils.accountData;
import codepig.passnote.data.sqlCenter;

/**
 * 内容单页view
 * Created by QZD on 2015/9/17.
 */
public class expandPaper extends LinearLayout {
    private ImageView openBtn,editBtn,saveBtn;
    private TextView index_t,title_t,account_t,password_t,info_t;
    private EditText title_edit,account_edit,password_edit,info_edit;
    private boolean opened=true;
    private int durationMillis = 200;
    private LinearLayout mainBody,contentPage,title_plan;
    private Animation mExpandAnimation,mCollapseAnimation;
    private accountData mData=null;
    private long id;
    public expandPaper(Context context) {
        super(context);
        init(context);
    }

    public expandPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.expandpaper_l, this);
        mainBody=(LinearLayout) findViewById(R.id.mainBody);
        contentPage=(LinearLayout) findViewById(R.id.contentPage);
        title_plan=(LinearLayout) findViewById(R.id.title_plan);
        openBtn=(ImageView) findViewById(R.id.openBtn);
        editBtn=(ImageView) findViewById(R.id.editBtn);
        saveBtn=(ImageView) findViewById(R.id.saveBtn);
        index_t=(TextView) findViewById(R.id.index_t);
        title_t=(TextView) findViewById(R.id.title_t);
        account_t=(TextView) findViewById(R.id.account_t);
        password_t=(TextView) findViewById(R.id.password_t);
        info_t=(TextView) findViewById(R.id.info_t);
        title_edit=(EditText) findViewById(R.id.title_edit);
        account_edit=(EditText) findViewById(R.id.account_edit);
        password_edit=(EditText) findViewById(R.id.password_edit);
        info_edit=(EditText) findViewById(R.id.info_edit);

        editBtn.setVisibility(GONE);
        saveBtn.setVisibility(GONE);
        openBtn.setOnClickListener(btnClick);
        editBtn.setOnClickListener(btnClick);
        saveBtn.setOnClickListener(btnClick);

        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                contentPage.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                contentPage.clearAnimation();
                editBtn.setVisibility(VISIBLE);
            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentPage.clearAnimation();
                contentPage.setVisibility(View.GONE);
                editAble(false);
                editBtn.setVisibility(GONE);
                saveBtn.setVisibility(GONE);
            }
        });

        editAble(false);
        editBtn.setVisibility(GONE);
        opened=false;
        contentPage.setVisibility(View.GONE);
    }

    /**
     * 填写信息
     */
    public void setData(accountData _data){
        mData=_data;
        id=_data.paperId;
        index_t.setText(String.valueOf(mData.paperId+1));
        title_t.setText(mData.paperName);
        account_t.setText(mData.account);
        password_t.setText(mData.password);
        info_t.setText(mData.info);
    }

    /**
     * 按钮事件
     */
    private View.OnClickListener btnClick = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.openBtn:
                    final int startValue = contentPage.getHeight();//起始高度
                    //展开或关闭内容
                    contentPage.clearAnimation();
                    expandMe(opened);
                    break;
                case R.id.saveBtn://保存编辑
                    saveData();
                    editAble(false);
                    break;
                case R.id.editBtn://进入编辑模式
                    editAble(true);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 保存信息
     */
    private void saveData(){
        mData.paperName=title_edit.getText().toString();
        mData.account=account_edit.getText().toString();
        mData.password=password_edit.getText().toString();
        mData.info=info_edit.getText().toString();
        title_t.setText(mData.paperName);
        account_t.setText(mData.account);
        password_t.setText(mData.password);
        info_t.setText(mData.info);
        if(sqlCenter.updataInDB(String.valueOf(mData.paperId),mData.paperName,mData.account,mData.password,mData.info)>0){
            editAble(false);
        }else {
            Log.d("LOGCAT","保存失败");
        }
    }

    /**
     * 展开或收缩
     */
    public void expandMe(Boolean _open){
        if(_open){
            opened=false;
            RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(durationMillis);
            animation.setFillAfter(true);
            openBtn.startAnimation(animation);
//                        mCollapseAnimation.setDuration(durationMillis);
            contentPage.startAnimation(mCollapseAnimation);
        }else {
            opened=true;
            RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(durationMillis);
            animation.setFillAfter(true);
            openBtn.startAnimation(animation);
//                        mExpandAnimation.setDuration(durationMillis);
            contentPage.startAnimation(mExpandAnimation);
        }
    }

    /**
     * 切换编辑模式
     */
    public void editAble(boolean _edit){
        if(_edit){
            title_edit.setText(title_t.getText().toString());
            account_edit.setText(account_t.getText().toString());
            password_edit.setText(password_t.getText().toString());
            info_edit.setText(info_t.getText().toString());
            saveBtn.setVisibility(VISIBLE);
            editBtn.setVisibility(GONE);
            title_edit.setVisibility(VISIBLE);
            account_edit.setVisibility(VISIBLE);
            password_edit.setVisibility(VISIBLE);
            info_edit.setVisibility(VISIBLE);
            title_t.setVisibility(GONE);
            account_t.setVisibility(GONE);
            password_t.setVisibility(GONE);
            info_t.setVisibility(GONE);
        }else {
            saveBtn.setVisibility(GONE);
            editBtn.setVisibility(VISIBLE);
            title_edit.setVisibility(GONE);
            account_edit.setVisibility(GONE);
            password_edit.setVisibility(GONE);
            info_edit.setVisibility(GONE);
            title_t.setVisibility(VISIBLE);
            account_t.setVisibility(VISIBLE);
            password_t.setVisibility(VISIBLE);
            info_t.setVisibility(VISIBLE);
        }
    }
}
