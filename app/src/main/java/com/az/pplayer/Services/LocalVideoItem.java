package com.az.pplayer.Services;

public class LocalVideoItem {
  public  String VideoPath;
    public String PreviewPath;
    public String ImagePath;
    public String Name;
    public  String[] Tags;
    public LocalVideoItem(DownloadRequest request){
        VideoPath = PathService.GetVideoLocalPath(request.Url);
        PreviewPath = PathService.GetPreviewLocalPath(request.PreviewUrl);
        ImagePath = PathService.GetPreviewLocalPath(request.ImageUrl);
        Name = request.VideoDisplayName;
        Tags = request.Tags;
    }
}
