package com.az.pplayer.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.MainActivity;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
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

import androidx.core.app.NotificationManagerCompat;

public class DownloadService {
    private static DownloadService instance;
    public static DownloadService Get() {
        if (instance == null)
            instance = new DownloadService(PhpPlayerApp.getAppContext());
        return instance;
    }

    private Context context;
    Fetch fetch;


    private DownloadService(Context context) {
        this.context = context;
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(3)
                .build();
         fetch = Fetch.Impl.getInstance(fetchConfiguration);

        fetch.addListener(fetchListener);
    }

    public void addFetchListener(FetchListener listener){
        fetch.addListener(listener);
    }

    public void Download(final VideoItem item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadRequest request = ParserService.ParseVideoPage(item);
                if (request != null){
                    Download(request);
                }
            }
            }).start();
    }
    public void Download(final DownloadRequest request){



        File file = new File (PathService.GetVideoLocalPath(request.Url));
        if (file.exists ()) file.delete ();
        final LocalVideoItem videoItem = new LocalVideoItem(request);



        final Request downloadRequest = new Request(request.Url, file.getPath());
        downloadRequest.setPriority(Priority.HIGH);
        downloadRequest.setNetworkType(NetworkType.WIFI_ONLY);

        fetch.enqueue(downloadRequest, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                request.Id = result.getId();
                UserStorage.Get().AddDownloadRequest(request);
                UserStorage.Get().AddDownloadedVideo(videoItem);
            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
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



    private FetchListener fetchListener = new AbstractFetchListener() {


        @Override
        public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
            UserStorage.Get().RemoveDownloadRequest(download.getId());
            super.onError(download, error, throwable);
        }

        @Override
        public void onCompleted(@NotNull Download download) {
            super.onCompleted(download);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            DownloadRequest request = UserStorage.Get().GetRequest(download.getId());
            if (request == null)
                return;
            notificationManager.cancel(request.Id);
            UserStorage.Get().RemoveDownloadRequest(download.getId());

        }

        @Override
        public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
            super.onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond);
            DownloadRequest request = UserStorage.Get().GetRequest(download.getId());
            if (request == null)
                return;
            int id = request.Id;
            int percent =0;
            if (download.getTotal() != 0) {
                 percent = (int) (download.getDownloaded() * 100 / download.getTotal());
            }

        }

        private CharSequence getFileName(String file) {
           String[] parts= file.split("/");
           return parts[parts.length-1];
        }
    };
}
