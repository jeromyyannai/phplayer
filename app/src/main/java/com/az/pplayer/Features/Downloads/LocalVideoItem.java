package com.az.pplayer.Features.Downloads;

import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.PathService;
import com.az.pplayer.Storage.Db.DbMappers.Mapper;
import com.az.pplayer.Storage.Db.DbModels.dbVideoEntry;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTag;

public class LocalVideoItem {
    public  int Id;
  public  String VideoPath;
    public String PreviewPath;
    public String ImagePath;
    public String Name;
    public  String[] Tags;
    public DownloadRequest Request;
    public boolean IsDownloaded;
    public LocalVideoItem(DownloadRequest request){
        Id=0;
        VideoPath = PathService.GetVideoLocalPath(request.Url);
        PreviewPath = PathService.GetPreviewLocalPath(request.PreviewUrl);
        ImagePath = PathService.GetPreviewLocalPath(request.ImageUrl);
        Name = request.VideoDisplayName;
        Tags = request.Tags;
        Request = request;
    }


    public LocalVideoItem(dbVideoItemTag r) {
        Id = (int)r.VideoItem.id_item;
        VideoPath = r.VideoItem.VideoPath;
        PreviewPath = r.VideoItem.PreviewPath;
        Name = r.VideoItem.Name;
        Tags = Mapper.MapTags( r.Tags);
        Request = Mapper.Map(r.request);
        IsDownloaded = r.VideoItem.IsDownloaded;
    }

    public LocalVideoItem() {
        Id=0;
    }
}
