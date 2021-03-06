package notes.rednitrogen.com.rednotes.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import notes.rednitrogen.com.rednotes.R;
import notes.rednitrogen.com.rednotes.database.TaskDBHelper;
import notes.rednitrogen.com.rednotes.database.model.Task;

public class TaskWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TaskWidgetItemFactory(getApplicationContext(), intent);
    }

    class TaskWidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private ArrayList<String> tasksData;
        private List<Task> tasksList = new ArrayList<>();
        private TaskDBHelper mydb;

        TaskWidgetItemFactory(Context context , Intent intent){
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            this.tasksData = intent.getStringArrayListExtra("tasksData");
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            tasksData.removeAll(tasksData);
            mydb = new TaskDBHelper(context);
            tasksList = mydb.getUncheckedTasks();
            for(int i=0; i<tasksList.size(); i++){
                tasksData.add(tasksList.get(i).getTask());
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return tasksData.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.content_tasks_widget_item);
            views.setTextViewText(R.id.task_widget_dot, Html.fromHtml("&#8226;"));
            views.setTextViewText(R.id.task_widget_name, tasksData.get(position));

            Intent fillIntent = new Intent();
            views.setOnClickFillInIntent(R.id.task_widget_content, fillIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
