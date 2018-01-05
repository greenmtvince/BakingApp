package com.quantrian.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Vinnie on 1/4/2018.
 */

public class WidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new WidgetDataProvider(this, intent);
    }
}
