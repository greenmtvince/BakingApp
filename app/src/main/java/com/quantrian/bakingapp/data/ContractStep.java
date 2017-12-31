package com.quantrian.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vinnie on 12/28/2017.
 */

public class ContractStep {
    public static final String AUTHORITY = "com.quantrian.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_STEP = "step";

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP).build();

        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_FK_RECIPE = "fk_recipe";
    }
}
