package yiqihi.mobile.com.myapplication;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.customview.AutoHideXScrollView;
import yiqihi.mobile.com.commonlib.customview.DisScrollListView;


public class MainActivity extends ActionBarActivity implements View.OnFocusChangeListener, AbsListView.OnScrollListener {
    private Drawable up_unselect, up_select, down_unselect, down_select;
    int num = 300;
    private AutoHideXScrollView auto;
    private RelativeLayout rl_header;
    private View content;
    private DisScrollListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        auto= (AutoHideXScrollView) findViewById(R.id.home_refreshable_view);
        content=View.inflate(this,R.layout.auto,null);
        listView= (DisScrollListView) content.findViewById(android.R.id.list);
        List<String> lists=new ArrayList<String>();
        for(int i=0;i<20;i++){
            lists.add("ddd"+i);
        }
        ArrayAdapter arrayAdapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,lists);
        listView.setAdapter(arrayAdapter);
        listView.setOnScrollListener(this);
        auto.setHeaderAndFooter(rl_header,null);
        auto.addChild(content, 1);
    }


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

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String a = hasFocus ? "true" : "false";
        Toast.makeText(MainActivity.this, "hasfocus" + a, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("ddd","fdsafdsa");
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
