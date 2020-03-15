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






    public static dbVideoItem Map(LocalVideoItem item) {
        dbVideoItem result = new dbVideoItem(item);
        return  result;
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

    public static List<LocalVideoItem> MapRequest(List<dbVideoItem> videoItems) {
        List<LocalVideoItem> result = new ArrayList<>();
        if (videoItems == null)
            return result;
        for (dbVideoItem r: videoItems)
        {

            result.add(new LocalVideoItem(r));
        }
        return result;
    }

    public static DownloadRequest _Map(dbVideoItemTag r) {
        if (r == null)
            return null;
        return new DownloadRequest(r.VideoItem);
    }

    public static DownloadRequest _Map(dbVideoItem r) {
        if (r == null)
            return null;
        return new DownloadRequest(r);
    }
}
