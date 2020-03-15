package com.az.pplayer.Storage.Db.DbMappers;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbTag;
import com.az.pplayer.Storage.Db.DbModels.dbVideoEntry;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTag;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static List<DownloadRequest> _Map(List<dbDownloadRequest> requests){
        List<DownloadRequest> result = new ArrayList<>();
        if (requests == null)
            return result;
        for (dbDownloadRequest r: requests)
        {

            result.add(new DownloadRequest(r));
        }
        return result;
    }

    public static dbDownloadRequest Map(DownloadRequest request, int itemId) {
        dbDownloadRequest result = new dbDownloadRequest();
        result.Id = request.Id;
        result.ImageUrl = request.ImageUrl;
        result.PercentCompleted = request.PercentCompleted;
        result.PreviewUrl = request.PreviewUrl;
        result.Size = request.Size;
        result.VideoDisplayName = request.VideoDisplayName;
        result.fk_id_item = itemId;
        result.Video = request.Video;
        result.VideoId = request.VideoId;
       // result.Tags = request.
        return result;
    }



    public static dbVideoItem Map(LocalVideoItem item) {
        dbVideoItem result = new dbVideoItem(item);
        return  result;
    }


    public static DownloadRequest Map(dbDownloadRequest request) {
        if (request == null)
            return null;
        return new DownloadRequest(request);
    }

    public static String[] MapTags(List<dbTag> tags) {
        String[] result = new String[tags.size()];
        int j=0;
        for (dbTag i: tags){
            result[j] = i.tag;
        }
        return result;
    }

    public static List<LocalVideoItem> Map(List<dbVideoItemTag> fullVideoItem) {
        List<LocalVideoItem> result = new ArrayList<>();
        if (fullVideoItem == null)
            return result;
        for (dbVideoItemTag r: fullVideoItem)
        {

            result.add(new LocalVideoItem(r));
        }
        return result;
    }
}
