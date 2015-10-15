package codepig.passnote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codepig.passnote.Utils.accountData;
import codepig.passnote.Utils.dataCenter;
import codepig.passnote.data.sqlCenter;
import codepig.passnote.data.sqlHelper;
import codepig.passnote.view.expandPaper;

/**
 * 主页
 * Created by QZD on 2015/9/15.
 */
public class MainActivity extends ActionBarActivity {
    private Context context;
    private int alertCode=0;//警告框类型码,0=退出提示，1=删除提示
    private AlertDialog alertDialog;
    private TextView title_t,info_t;
    private LinearLayout contentList,pageTools;
    private ImageView searchBtn,newBtn,allBtn,delBtn,closeBtn;
    private int pageType=0;//页面类型，0=查看页，1=删除页
    private sqlHelper mDBHelper;
    private List<expandPaper> paperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 设置view
     */
    private void initView(){
        dataCenter.dataList=new ArrayList<>();
        paperList=new ArrayList<>();
        title_t=(TextView) findViewById(R.id.title_t);
        info_t=(TextView) findViewById(R.id.info_t);
        contentList=(LinearLayout) findViewById(R.id.contentList);
        pageTools=(LinearLayout) findViewById(R.id.pageTools);
        searchBtn=(ImageView) findViewById(R.id.searchBtn);
        newBtn=(ImageView) findViewById(R.id.newBtn);
        allBtn=(ImageView) findViewById(R.id.allBtn);
        delBtn=(ImageView) findViewById(R.id.delBtn);
        closeBtn=(ImageView) findViewById(R.id.closeBtn);
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", alertListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", alertListener);

        allBtn.setVisibility(View.GONE);
        delBtn.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);

        searchBtn.setOnClickListener(clickBtn);
        newBtn.setOnClickListener(clickBtn);
        allBtn.setOnClickListener(clickBtn);
        delBtn.setOnClickListener(clickBtn);
        closeBtn.setOnClickListener(clickBtn);

        showPageTitle("查看列表","");

        //获取DB管理
        if(mDBHelper==null){
            //获取数据库对象，如果数据库不存在，则创建数据库，messageCode.APPDBNAME为数据库名
            mDBHelper=new sqlHelper(this);
        }
        sqlCenter.initSqlManager(mDBHelper);
        sqlCenter.getList();
        creatList();
    }

    /**
     * 显示页面抬头信息
     */
    private void showPageTitle(String _title,String _info){
        title_t.setText(_title);
        info_t.setText(_info);
    }

    /**
     * 创建列表
     */
    private void creatList(){
        for (int i=0;i<dataCenter.dataList.size();i++){
            expandPaper paper=new expandPaper(this);
            contentList.addView(paper);
            paper.setData(dataCenter.dataList.get(i));
        }
    }

    /**
     * 新建一条
     */
    private void newOne(){
        accountData acInfo=new accountData();
        acInfo.paperId=sqlCenter.insDataInDB("","","","");
        if(acInfo.paperId==-1){
            Toast.makeText(context, "插入条目失败", Toast.LENGTH_SHORT).show();
            return;
        }else{
            dataCenter.dataList.add(acInfo);
            expandPaper paper=new expandPaper(this);
            contentList.addView(paper);
            paper.expandMe(true);
            paper.editAble(true);
            paper.setData(acInfo);
        }
        return;
    }

    /**
     * 删除条目
     */
    private void deleteOne(int _index){
        long _id=dataCenter.dataList.get(_index).paperId;
        sqlCenter.delDataInDB(String.valueOf(_id));
    }

    /**
     * 打开设置界面
     */
    private void openSetting(){
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
     * 按钮监听
     */
    private View.OnClickListener clickBtn = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchBtn://搜索
                    break;
                case R.id.newBtn://新建
                    newOne();
                    break;
                case R.id.allBtn://全选
                    break;
                case R.id.delBtn://删除
                    break;
                case R.id.closeBtn://退出
                    if(pageType==0){
                        closeBtn.setVisibility(View.GONE);
                    }else{
                        searchBtn.setVisibility(View.VISIBLE);
                        newBtn.setVisibility(View.VISIBLE);
                        allBtn.setVisibility(View.GONE);
                        delBtn.setVisibility(View.GONE);
                        closeBtn.setVisibility(View.GONE);
                    }
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
        sqlCenter.closeDB();
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
     *
     */
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
