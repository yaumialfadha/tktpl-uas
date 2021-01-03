package id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.HomeActivity;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.R;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.Bill;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.model.BillWithPerson;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.BillDao;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.BillRepository;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.repository.RoomDatabase;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.utils.Constant;
import id.ac.ui.cs.mobileprogramming.yaumialfadha.easysplitbill.viewmodel.BillViewModel;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BackgroundNotificationWorker extends Worker {

    public static final String workTag = "notificationWork";
    public final Context context = this.getApplicationContext();

    public BackgroundNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        boolean[] firstWorker = getInputData().getBooleanArray("firstWorker");
        long diffTomorrowMIllis = getInputData().getLong("diffTomorrowMIllis",
                Calendar.getInstance().getTimeInMillis() + Constant.ONE_DAY_TIME_IN_MILLIS);
        if (firstWorker[0] == true) {
            schedulePeriodicWork(diffTomorrowMIllis);
        }
        if (willGiveNotifToday() == true) {
            triggerNotification();
        }
        return Result.success();
    }

    private void triggerNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setContentTitle(context.getResources().getString(R.string.notification_title))
                        .setContentText(context.getResources().getString(R.string.notification_body))
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(context,
                                0, new Intent(context, HomeActivity.class), 0));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "afk";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "tktpl-channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(0, builder.build());
    }

    public void schedulePeriodicWork(long diffTomorrowMIllis) {
        // When multiple constraints are specified like below,
        // your task will run only when all the constraints are met.
        boolean[] firstWorker = {false};
        Data data = new Data.Builder().putBooleanArray("firstWorker", firstWorker).build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        PeriodicWorkRequest taskWork = new PeriodicWorkRequest
                .Builder(BackgroundNotificationWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(diffTomorrowMIllis, TimeUnit.MICROSECONDS)
                .setConstraints(constraints)
                .setInputData(data)
                .addTag("backgroundNotification")
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(taskWork);
    }

    public boolean willGiveNotifToday() {
        final boolean[] res = {false};
        RoomDatabase db = RoomDatabase.getInstance(getApplicationContext());
        final BillDao billDao = db.getBillDao();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() - Constant.ONE_DAY_TIME_IN_MILLIS);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        List<Bill> list = billDao.getFirstDueBill(calendar.getTimeInMillis());
        if (!list.isEmpty()) {
            Calendar millisTime = Calendar.getInstance();
            millisTime.setTimeInMillis(list.get(0).getDueDate().getTime());
            long diff = millisTime.getTimeInMillis() - calendar.getTimeInMillis();
            int diffhour = (int) diff / Constant.ONE_HOUR_IN_MILLIS;
            Log.d("asda", String.valueOf(diffhour));
            if (diffhour <= 24) res[0] = true;
        }
        return res[0];
    }
}
