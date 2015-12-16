package codepig.passnote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codepig.passnote.Utils.accountData;
import codepig.passnote.Utils.dataCenter;
import codepig.passnote.data.sqlCenter;
import codepig.passnote.data.sqlHelper;
import codepig.passnote.math.filemanager;
import codepig.passnote.view.expandPaper;

/**
 * 主页
 * Created by QZD on 2015/9/15.
 */
public class MainActivity extends ActionBarActivity {
    private Context context;
    private AlertDialog alertDialog;
    private TextView title_t,info_t;
    private EditText searchKey_t;
    private LinearLayout contentList,pageTools;
    private ImageView searchBtn,newBtn,allBtn,delBtn,closeBtn,settingBtn;
    private int pageType=0;//页面类型，0=查看页，1=删除页
    private sqlHelper mDBHelper;
    private List<expandPaper> paperList;
    private ScrollView listSV;
    private Handler mHandler = new Handler();

    private final int ENABLEDEL=0;
    private final int DELPAPER=1;
    private final int CHECKALL=2;
    private final int DISABLEDEL=3;
    private final int SETINDEX=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBackup();
    }

    /**
     * 设置view
     */
    private void initView(){
        dataCenter.dataList=new ArrayList<>();
        paperList=new ArrayList<>();
        title_t=(TextView) findViewById(R.id.title_t);
        info_t=(TextView) findViewById(R.id.info_t);
        searchKey_t=(EditText) findViewById(R.id.searchKey_t);
        contentList=(LinearLayout) findViewById(R.id.contentList);
        pageTools=(LinearLayout) findViewById(R.id.pageTools);
        searchBtn=(ImageView) findViewById(R.id.searchBtn);
        newBtn=(ImageView) findViewById(R.id.newBtn);
        allBtn=(ImageView) findViewById(R.id.allBtn);
        delBtn=(ImageView) findViewById(R.id.delBtn);
        closeBtn=(ImageView) findViewById(R.id.closeBtn);
        settingBtn=(ImageView) findViewById(R.id.settingBtn);
        listSV=(ScrollView) findViewById(R.id.listSV);
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", alertListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", alertListener);

        allBtn.setVisibility(View.GONE);
        delBtn.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);
//        searchBtn.setVisibility(View.GONE);

        searchBtn.setOnClickListener(clickBtn);
        newBtn.setOnClickListener(clickBtn);
        allBtn.setOnClickListener(clickBtn);
        delBtn.setOnClickListener(clickBtn);
        closeBtn.setOnClickListener(clickBtn);
        settingBtn.setOnClickListener(clickBtn);

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
            paperList.add(paper);
            contentList.addView(paper);
            paper.setOnLongClickListener(longClick);
            paper.setData(dataCenter.dataList.get(i));
        }
        action2All(SETINDEX);
    }

    /**
     * 检查是否有新导入的记录
     */
    private void checkBackup(){
        Log.d("LOGCAT","paperList:"+paperList.size());
        if(filemanager.recovered && paperList.size()<dataCenter.dataList.size()){
            for(int i=paperList.size();i<dataCenter.dataList.size();i++){
                expandPaper paper=new expandPaper(this);
                paperList.add(paper);
                contentList.addView(paper);
                paper.setOnLongClickListener(longClick);
                paper.setData(dataCenter.dataList.get(i));
                Log.d("LOGCAT", "dataCenter.dataList.get(i):" + dataCenter.dataList.get(i).paperName);
            }
            filemanager.recovered=false;
            action2All(SETINDEX);
        }
        Log.d("LOGCAT","paperList:"+paperList.size());
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
            paperList.add(paper);
            contentList.addView(paper);
            paper.expandMe(true);
            paper.setOnLongClickListener(longClick);
            paper.setData(acInfo);
            paper.editAble(true);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listSV.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        action2All(SETINDEX);
        return;
    }

    /**
     * 删除条目
     */
    private void deleteOneInData(int _index){
        long _id=dataCenter.dataList.get(_index).paperId;
        sqlCenter.delDataInDB(String.valueOf(_id));
        dataCenter.dataList.remove(_index);
    }

    /**
     * 遍历操作
     */
    private void action2All(int actionType){
        for (int i = 0; i < paperList.size(); i++) {
            switch (actionType) {
                case DELPAPER://删除
                    if(paperList.get(i).isChecked()){
                        deleteOneInData(i);
                        contentList.removeView(paperList.get(i));
                        paperList.remove(i);
                        i--;
                    }
                    break;
                case ENABLEDEL://切换到删除操作
                    paperList.get(i).delAble(true);
                    break;
                case DISABLEDEL://切换到删除操作
                    paperList.get(i).delAble(false);
                    break;
                case CHECKALL://选取全部
                    break;
                case SETINDEX:
                    paperList.get(i).setIndex(i+1);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 打开设置界面
     */
    private void openSetting(){
        Intent intent=new Intent(getApplication(), settingActivity.class);
        startActivity(intent);
    }

    //显示警告消息框
    private void showAlert(String _msg){
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
                    if(pageType==0) {
                        quitApp();
                    }else if(pageType==1){
                        action2All(DELPAPER);
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

    /**w
     * 长按监听
     */
    private View.OnLongClickListener longClick=new View.OnLongClickListener(){
        public boolean onLongClick(View v) {
            //切换到删除界面
            pageType=1;
            //遍历列表，切换到删除操作
            action2All(ENABLEDEL);
            expandPaper _checkedPaper=(expandPaper) v;
            _checkedPaper.delChecked();
            showPageTitle("删除记录", "请勾选您需要删除的记录");
            searchBtn.setVisibility(View.GONE);
            searchKey_t.setVisibility(View.GONE);
            newBtn.setVisibility(View.GONE);
//            allBtn.setVisibility(View.VISIBLE);
            delBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.VISIBLE);
            return true;
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
                    gotoItem(searchItem(searchKey_t.getText().toString()));
                    break;
                case R.id.newBtn://新建
                    newOne();
                    break;
                case R.id.allBtn://全选
                    break;
                case R.id.delBtn://删除
                    showAlert("您确定要删除这些记录吗？");
                    break;
                case R.id.closeBtn://退出删除操作
                    gotoListPage();
                    break;
                case R.id.settingBtn:
                    openSetting();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 按标题查询条目
     * @param _key
     * @return
     */
    private int searchItem(String _key){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchKey_t.getWindowToken(), 0);
        return dataCenter.searchByName(_key);
    }

    /**
     * 跳转到对应条目
     * @param _index
     */
    private void gotoItem(int _index){
        int yPosition=0;
//        Log.d("LOGCAT", "_index:" + _index);
        for (int i=0;i<_index;i++){
            yPosition+=paperList.get(i).getMeasuredHeight();
        }
//        Log.d("LOGCAT", "yPosition:" + yPosition);
        listSV.scrollTo(0, yPosition);
    }

    /**
     * 退出删除操作状态
     */
    private void gotoListPage(){
        pageType=0;
        searchBtn.setVisibility(View.VISIBLE);
        searchKey_t.setVisibility(View.VISIBLE);
        newBtn.setVisibility(View.VISIBLE);
        allBtn.setVisibility(View.GONE);
        delBtn.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);
        action2All(DISABLEDEL);
        showPageTitle("查看列表", "");
    }

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
            if(pageType==0) {
                showAlert("您这就要走了吗？");
            }else if(pageType==1){
                gotoListPage();
            }
            return true;
        }
        return false;
    }

    /**
     * 设置
     * @param menu
     * @return
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            openSetting();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
