package com.google.ar.core.examples.java.StarPower;

import android.provider.BaseColumns;

public final class ScoreboardAttr {
    public ScoreboardAttr() {}

    public static abstract class ScoreboardEntry implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_INITIALS = "initials";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TARGETS = "targets";
    }
}
