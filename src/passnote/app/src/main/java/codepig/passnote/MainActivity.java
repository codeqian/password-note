package codepig.passnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import codepig.passnote.Utils.accountData;
import codepig.passnote.math.sqlHelper;
import codepig.passnote.view.expandPaper;

/**
 * 主页
 * Created by QZD on 2015/9/15.
 */
public class MainActivity extends ActionBarActivity {
    private int alertCode=0;//警告框类型码,0=退出提示，1=删除提示
    private AlertDialog alertDialog;
    private LinearLayout contentList,pageTools,deletTools;
    private ImageView searchBtn,newBtn,allBtn,delBtn,closeBtn;
    private sqlHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 设置view
     */
    private void initView(){
        contentList=(LinearLayout) findViewById(R.id.contentList);
        deletTools=(LinearLayout) findViewById(R.id.deletTools);
        pageTools=(LinearLayout) findViewById(R.id.pageTools);
        searchBtn=(ImageView) findViewById(R.id.searchBtn);
        newBtn=(ImageView) findViewById(R.id.newBtn);
        allBtn=(ImageView) findViewById(R.id.allBtn);
        delBtn=(ImageView) findViewById(R.id.delBtn);
        closeBtn=(ImageView) findViewById(R.id.closeBtn);
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", alertListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", alertListener);

        deletTools.setVisibility(View.INVISIBLE);
        //test
        creatList();

        //获取DB管理
        if(mDBHelper==null){
            //获取数据库对象，如果数据库不存在，则创建数据库，messageCode.APPDBNAME为数据库名
            mDBHelper=new sqlHelper(this);
        }
    }

    /**
     * 创建便签
     */
    private void creatList(){
        for(int i=0;i<5;i++){
            expandPaper paper=new expandPaper(this);
            contentList.addView(paper);
            accountData _data=new accountData();
            _data.paperId=i;
            _data.paperName="name"+i;
            _data.account="test";
            _data.password="123456";
            _data.info="give me more";
            paper.showData(_data);
        }
    }
    /**
     * 打开设置界面
     */
    public void openSetting(){
//        Intent intent=new Intent(getApplication(), settingActivity.class);
//        startActivity(intent);
    }

    //显示警告消息框
    private void showalert(String _msg,int _code){
        alertCode=_code;
        alertDialog.setMessage(_msg);
        alertDialog.show();
    }

    /**
     * 警告框监听
     */
    DialogInterface.OnClickListener alertListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if(alertCode==0) {
                        quitApp();
                    }else if(alertCode==1){
                        //
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    alertDialog.hide();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 退出app
     */
    private void quitApp(){
        alertDialog.hide();
        System.exit(0);
    }

    /**
     * 监听返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            showalert("您这就要走了吗？",0);
            return true;
        }
        return false;
    }

    /**
     * 设置
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
