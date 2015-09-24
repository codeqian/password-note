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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import codepig.passnote.R;
import codepig.passnote.Utils.accountData;

/**
 * 内容单页view
 * Created by QZD on 2015/9/17.
 */
public class expandPaper extends LinearLayout {
    private ImageView openBtn;
    private TextView index_t,title_t,account_t,password_t,info_t;
    private boolean opened=true;
    private int durationMillis = 200;
    private LinearLayout mainBody,contentPage,title_plan;
    private Animation mExpandAnimation,mCollapseAnimation;
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
        index_t=(TextView) findViewById(R.id.index_t);
        title_t=(TextView) findViewById(R.id.title_t);
        account_t=(TextView) findViewById(R.id.account_t);
        password_t=(TextView) findViewById(R.id.password_t);
        info_t=(TextView) findViewById(R.id.info_t);

        openBtn.setOnClickListener(btnClick);

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
            }
        });

        opened=false;
        contentPage.setVisibility(View.GONE);
    }

    /**
     * 设置具体信息
     * @param _data
     */
    public void showData(accountData _data){
        index_t.setText(Integer.toString(_data.paperId+1));
        title_t.setText(_data.paperName);
        account_t.setText(_data.account);
        password_t.setText(_data.password);
        info_t.setText(_data.info);
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
                    Log.d("LOGCAT","opened:"+opened);
                    contentPage.clearAnimation();
                    if(opened){
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
                    break;
                default:
                    break;
            }
        }
    };
}
