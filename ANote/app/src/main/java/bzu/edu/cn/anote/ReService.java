package bzu.edu.cn.anote;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by hxs on 17-6-18.
 */

public class ReService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ReFactory(this.getApplicationContext(), intent);
    }
}
