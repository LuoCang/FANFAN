package com.fanfan.novel.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fanfan.novel.model.NavigationBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NAVIGATION_BEAN".
*/
public class NavigationBeanDao extends AbstractDao<NavigationBean, Long> {

    public static final String TABLENAME = "NAVIGATION_BEAN";

    /**
     * Properties of entity NavigationBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SaveTime = new Property(1, long.class, "saveTime", false, "saveTime");
        public final static Property Title = new Property(2, String.class, "title", false, "title");
        public final static Property Guide = new Property(3, String.class, "guide", false, "guide");
        public final static Property Datail = new Property(4, String.class, "datail", false, "datail");
        public final static Property PosX = new Property(5, int.class, "posX", false, "posX");
        public final static Property PosY = new Property(6, int.class, "posY", false, "posY");
        public final static Property ImgUrl = new Property(7, String.class, "imgUrl", false, "imgUrl");
        public final static Property Navigation = new Property(8, String.class, "navigation", false, "navigation");
        public final static Property NavigationData = new Property(9, String.class, "navigationData", false, "navigationData");
    }


    public NavigationBeanDao(DaoConfig config) {
        super(config);
    }
    
    public NavigationBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NAVIGATION_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"saveTime\" INTEGER NOT NULL ," + // 1: saveTime
                "\"title\" TEXT," + // 2: title
                "\"guide\" TEXT," + // 3: guide
                "\"datail\" TEXT," + // 4: datail
                "\"posX\" INTEGER NOT NULL ," + // 5: posX
                "\"posY\" INTEGER NOT NULL ," + // 6: posY
                "\"imgUrl\" TEXT," + // 7: imgUrl
                "\"navigation\" TEXT," + // 8: navigation
                "\"navigationData\" TEXT);"); // 9: navigationData
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NAVIGATION_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NavigationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSaveTime());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String guide = entity.getGuide();
        if (guide != null) {
            stmt.bindString(4, guide);
        }
 
        String datail = entity.getDatail();
        if (datail != null) {
            stmt.bindString(5, datail);
        }
        stmt.bindLong(6, entity.getPosX());
        stmt.bindLong(7, entity.getPosY());
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(8, imgUrl);
        }
 
        String navigation = entity.getNavigation();
        if (navigation != null) {
            stmt.bindString(9, navigation);
        }
 
        String navigationData = entity.getNavigationData();
        if (navigationData != null) {
            stmt.bindString(10, navigationData);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NavigationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSaveTime());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String guide = entity.getGuide();
        if (guide != null) {
            stmt.bindString(4, guide);
        }
 
        String datail = entity.getDatail();
        if (datail != null) {
            stmt.bindString(5, datail);
        }
        stmt.bindLong(6, entity.getPosX());
        stmt.bindLong(7, entity.getPosY());
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(8, imgUrl);
        }
 
        String navigation = entity.getNavigation();
        if (navigation != null) {
            stmt.bindString(9, navigation);
        }
 
        String navigationData = entity.getNavigationData();
        if (navigationData != null) {
            stmt.bindString(10, navigationData);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NavigationBean readEntity(Cursor cursor, int offset) {
        NavigationBean entity = new NavigationBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // saveTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // guide
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // datail
            cursor.getInt(offset + 5), // posX
            cursor.getInt(offset + 6), // posY
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // imgUrl
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // navigation
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // navigationData
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NavigationBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSaveTime(cursor.getLong(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGuide(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDatail(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPosX(cursor.getInt(offset + 5));
        entity.setPosY(cursor.getInt(offset + 6));
        entity.setImgUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setNavigation(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setNavigationData(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NavigationBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NavigationBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NavigationBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
