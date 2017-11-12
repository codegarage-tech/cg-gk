package com.customviews.library.application;

import android.support.design.widget.NavigationView;

/**
 * Created by rashed on 3/15/16.
 */
public interface SetUpNavigationDrawerView {
     public void setUpNavigationDrawerHeader(int navigationHeader);
     public void setupNavigationDrawerMenu(int navigationMenu);
     public void setupNavigationDrawerMenuListener(NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener);
}
