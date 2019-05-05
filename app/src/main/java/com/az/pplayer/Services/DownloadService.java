package com.az.pplayer.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationManagerCompat;

import com.az.pplayer.Data.VideoLinkHolder;
import com.az.pplayer.MainActivity;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.VideoPlayerActivity;
import com.tonyodev.fetch2.AbstractFetchListener;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadService {
    private static DownloadService instance;
    public static DownloadService Get() {
        if (instance == null)
            instance = new DownloadService(PhpPlayerApp.getAppContext());
        return instance;
    }

    private Context context;
    Fetch fetch;
    private List<DownloadRequest> uncompletedRequests;

    private DownloadService(Context context) {
        this.context = context;
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(3)
                .build();
        uncompletedRequests = new ArrayList<>();
         fetch = Fetch.Impl.getInstance(fetchConfiguration);

        fetch.addListener(fetchListener);
    }

    public void Download(final DownloadRequest request){



        File file = new File (UserStorage.Get().getDownloadPath(), PathService.GetVideoLocalPath(request.Url));
        if (file.exists ()) file.delete ();
        request.VideoItem = new LocalVideoItem(request);



        final Request downloadRequest = new Request(request.Url, file.getPath());
        downloadRequest.setPriority(Priority.NORMAL);
        downloadRequest.setNetworkType(NetworkType.WIFI_ONLY);

        fetch.enqueue(downloadRequest, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                uncompletedRequests.add(request);
            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {

            }
        });
        querySupportItem(request.ImageUrl);
        querySupportItem(request.PreviewUrl);
    }

    private void querySupportItem(String url){
        File file = new File (PathService.GetPreviewLocalPath(url));
        if (file.exists ()) file.delete ();



        final Request downloadRequest = new Request(url, file.getPath());
        downloadRequest.setPriority(Priority.NORMAL);
        downloadRequest.setNetworkType(NetworkType.WIFI_ONLY);

        fetch.enqueue(downloadRequest, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {

            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {

            }
        });
    }

    private void DownloadCompleted(DownloadRequest request)
    {
        if (request == null)
            return;
        UserStorage.Get().AddDownloadedVideo(request.VideoItem);
     //download all stuff from request
        removeRequest(request);

    }

    private boolean removeRequest(DownloadRequest request){
        for (int i=0;i<uncompletedRequests.size();i++)
        {
            if (uncompletedRequests.get(i).Url.equals(request.Url)) {
                uncompletedRequests.remove(i);
                return true;
            }
        }
        return false;
    }
    private boolean removeRequest(String url){
        for (int i=0;i<uncompletedRequests.size();i++)
        {
            if (uncompletedRequests.get(i).Url.equals(url)) {
                uncompletedRequests.remove(i);
                return true;
            }
        }
        return false;
    }

    private  DownloadRequest getRequest(String url){
        for (int i=0;i<uncompletedRequests.size();i++)
        {
            if (uncompletedRequests.get(i).Url.equals(url)) {
                return uncompletedRequests.get(i);

            }
        }
        return null;
    }


    private FetchListener fetchListener = new AbstractFetchListener() {


        @Override
        public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
            removeRequest(download.getUrl());
            super.onError(download, error, throwable);
        }

        @Override
        public void onCompleted(@NotNull Download download) {
            super.onCompleted(download);
            DownloadCompleted(getRequest(download.getUrl()));
        }

        @Override
        public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
            super.onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond);
            DownloadRequest request = getRequest(download.getUrl());
            if (request == null)
                return;
            int id = uncompletedRequests.indexOf(request);
            int percent = (int)(download.getTotal()/download.getDownloaded());
            Intent notificationIntent = new Intent(context, MainActivity.class); //replace with downloads
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(download.getFile())
                    .setContentText( percent+ "%")
                    .setSmallIcon(R.drawable.ic_file_download)
                    .setContentIntent(pendingIntent)
                    .build();

            /* Startujemy na pierwszym planie, aby nie dało jej się zamknąć */
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(id, notification);
        }
    };
}
