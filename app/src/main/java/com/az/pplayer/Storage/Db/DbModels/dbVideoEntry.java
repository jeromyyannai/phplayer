package com.az.pplayer.Storage.Db.DbModels;



import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class dbVideoEntry {
    @Embedded
    public dbVideoItem videoItem;
    @Relation(parentColumn = "id", entityColumn = "fk_id_item", entity = dbVideoItemTag.class)
    public List<dbVideoItemTag> tags;
}
