/*
 * This file is generated by jOOQ.
*/
package db.generated.tables;


import db.generated.College;
import db.generated.Keys;
import db.generated.tables.records.AbroadRecord;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Abroad extends TableImpl<AbroadRecord> {

    private static final long serialVersionUID = 155556727;

    /**
     * The reference instance of <code>college.abroad</code>
     */
    public static final Abroad ABROAD = new Abroad();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AbroadRecord> getRecordType() {
        return AbroadRecord.class;
    }

    /**
     * The column <code>college.abroad.id</code>.
     */
    public final TableField<AbroadRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>college.abroad.data</code>.
     */
    public final TableField<AbroadRecord, Date> DATA = createField("data", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "");

    /**
     * The column <code>college.abroad.country</code>.
     */
    public final TableField<AbroadRecord, String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), this, "");

    /**
     * The column <code>college.abroad.rel_id</code>.
     */
    public final TableField<AbroadRecord, Integer> REL_ID = createField("rel_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>college.abroad</code> table reference
     */
    public Abroad() {
        this("abroad", null);
    }

    /**
     * Create an aliased <code>college.abroad</code> table reference
     */
    public Abroad(String alias) {
        this(alias, ABROAD);
    }

    private Abroad(String alias, Table<AbroadRecord> aliased) {
        this(alias, aliased, null);
    }

    private Abroad(String alias, Table<AbroadRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return College.COLLEGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AbroadRecord> getPrimaryKey() {
        return Keys.KEY_ABROAD_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AbroadRecord>> getKeys() {
        return Arrays.<UniqueKey<AbroadRecord>>asList(Keys.KEY_ABROAD_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<AbroadRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AbroadRecord, ?>>asList(Keys.ABROAD_IBFK_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Abroad as(String alias) {
        return new Abroad(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Abroad rename(String name) {
        return new Abroad(name, null);
    }
}
