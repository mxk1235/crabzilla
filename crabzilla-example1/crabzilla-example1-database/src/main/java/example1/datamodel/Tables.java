/*
 * This file is generated by jOOQ.
*/
package example1.datamodel;


import example1.datamodel.tables.CustomerSummary;
import example1.datamodel.tables.EventsChannels;
import example1.datamodel.tables.SchemaVersion;
import example1.datamodel.tables.UnitsOfWork;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in example1db
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>example1db.customer_summary</code>.
     */
    public static final CustomerSummary CUSTOMER_SUMMARY = example1.datamodel.tables.CustomerSummary.CUSTOMER_SUMMARY;

    /**
     * The table <code>example1db.events_channels</code>.
     */
    public static final EventsChannels EVENTS_CHANNELS = example1.datamodel.tables.EventsChannels.EVENTS_CHANNELS;

    /**
     * The table <code>example1db.schema_version</code>.
     */
    public static final SchemaVersion SCHEMA_VERSION = example1.datamodel.tables.SchemaVersion.SCHEMA_VERSION;

    /**
     * The table <code>example1db.units_of_work</code>.
     */
    public static final UnitsOfWork UNITS_OF_WORK = example1.datamodel.tables.UnitsOfWork.UNITS_OF_WORK;
}
