package com.customviews.library.application;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.customviews.library.R;
import com.customviews.library.customfonttextview.CustomFontTextView;
import com.customviews.library.memoryleakhandler.ParentActivity;

/**
 * Created by rashed on 3/7/16.
 */
public class BaseActivity extends ParentActivity{

    private LayoutInflater inflater;
    public FrameLayout baseLayout;
    private LinearLayout inflateLayout;
    private CustomFontTextView txtActivityTitle;
    private Toolbar toolbar;
    private ImageView burgerMenu,btnHelp;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        inflater = LayoutInflater.from(getParentContext());
        baseLayout = (FrameLayout) findViewById(R.id.root);
        inflateLayout = (LinearLayout) findViewById(R.id.base_layout);
        txtActivityTitle = (CustomFontTextView) findViewById(R.id.txt_title);
        burgerMenu= (ImageView) findViewById(R.id.burger_menu);
        btnHelp= (ImageView) findViewById(R.id.btn_help);

        // set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

    }

     public void setActivityTitle(String title) {
         txtActivityTitle.setText(title);
     }

    public View setActivityLayout(int layout){
        View layoutView = (View) inflater.inflate(layout, inflateLayout, true);
        return layoutView;
    }

    public void setBurgerMenu(boolean isMenu){
        if(isMenu){
            burgerMenu.setVisibility(View.VISIBLE);
        }else{
            burgerMenu.setVisibility(View.GONE);
        }
    }

    public void setHelpButton(){
        btnHelp.setVisibility(View.VISIBLE);
    }

    public void removeHelpButton(){
        btnHelp.setVisibility(View.GONE);
    }

    public View getHelpButton(){
        return btnHelp;
    }

    //remove hamburger menu
    public void removeHamburgerMenu(ActionBarDrawerToggle toggle) {
        toggle.setDrawerIndicatorEnabled(false);
    }

    //lock drawer
    public void lockDrawer(DrawerLayout drawer) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //remove title
    public void removeTitle(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    public void setToolbar(boolean isToolbar){
        if(isToolbar){
            toolbar.setVisibility(View.VISIBLE);
        }else{
            toolbar.setVisibility(View.GONE);
        }
    }

    public ProgressDialog getLoadingDialog(){
        if(loadingDialog==null) {
            loadingDialog = new ProgressDialog(getParentContext());
        }
        return loadingDialog;
    }
}
