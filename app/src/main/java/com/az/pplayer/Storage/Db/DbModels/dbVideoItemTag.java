package com.az.pplayer.Storage.Db.DbModels;

import com.az.pplayer.Services.DownloadRequest;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;
public class dbVideoItemTag {

        @Embedded
        public dbVideoItem VideoItem;
        @Relation(
                parentColumn = "id_item",
                entityColumn = "id_tag",
                associateBy = @Junction(dbVideoItemTagRef.class)
        )
        public List<dbTag> Tags;

        @Relation( parentColumn = "id_item",
                entityColumn = "fk_id_item")
        public dbDownloadRequest request;
}
