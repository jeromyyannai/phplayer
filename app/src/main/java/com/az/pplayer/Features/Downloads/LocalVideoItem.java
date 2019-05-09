package com.az.pplayer.Features.Downloads;

import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.PathService;

public class LocalVideoItem {
  public  String VideoPath;
    public String PreviewPath;
    public String ImagePath;
    public String Name;
    public  String[] Tags;
    public DownloadRequest Request;
    public boolean IsDownloaded;
    public LocalVideoItem(DownloadRequest request){
        VideoPath = PathService.GetVideoLocalPath(request.Url);
        PreviewPath = PathService.GetPreviewLocalPath(request.PreviewUrl);
        ImagePath = PathService.GetPreviewLocalPath(request.ImageUrl);
        Name = request.VideoDisplayName;
        Tags = request.Tags;
        Request = request;
    }
}
