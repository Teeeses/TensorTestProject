package com.tensor.tensortest.data;

import android.provider.BaseColumns;

/**
 * Created by develop on 03.04.2017.
 */

public class NewsContract {

    private NewsContract() {};

    public static final class NewsEntry implements BaseColumns {

        public final static String TABLE_NAME = "news";

        public final static String _ID  = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_PUB_DATE = "pubDate";
        public final static String COLUMN_IMAGE_TITLE = "imageTitle";
    }
}
