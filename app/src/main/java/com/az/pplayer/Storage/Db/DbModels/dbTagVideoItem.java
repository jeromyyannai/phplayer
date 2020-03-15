package com.az.pplayer.Storage.Db.DbModels;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;
public class dbTagVideoItem {

        @Embedded
        public dbTag Tag;
        @Relation(
                parentColumn = "id_tag",
                entityColumn = "id_item",
                associateBy = @Junction(dbVideoItemTagRef.class)
        )
        public List<dbVideoItem> VideoItems;

}
