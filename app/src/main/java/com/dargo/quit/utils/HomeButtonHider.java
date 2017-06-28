package com.dargo.quit.utils;


import android.support.v7.app.ActionBar;

public class HomeButtonHider {

  public static void hide(ActionBar actionBar) {
    if (actionBar == null) { return; }
    actionBar.setHomeButtonEnabled(false);
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled(false);
  }
}
