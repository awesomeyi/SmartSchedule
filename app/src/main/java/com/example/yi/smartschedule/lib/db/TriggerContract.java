package com.example.yi.smartschedule.lib.db;

import android.provider.BaseColumns;

/**
 * Created by jackphillips on 7/15/16.
 */
public class TriggerContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TriggerContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TriggerEntry implements BaseColumns {
        public static final String TABLE_NAME = "Trigger";
        public static final String COLUMN_NAME_ENTRY_ID = "triggerid";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ADTIONAL_INFO = "adtionalinfo";
        public static final String COLUMN_NAME_ACTIONS = "actions";
        public static final String COLUMN_NAME_VALUE = "actionvalue";
    }
}
