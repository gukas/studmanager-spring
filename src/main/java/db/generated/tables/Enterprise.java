/*
 * This file is generated by jOOQ.
*/
package db.generated.tables;


import db.generated.College;
import db.generated.Keys;
import db.generated.tables.records.EnterpriseRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
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
public class Enterprise extends TableImpl<EnterpriseRecord> {

    private static final long serialVersionUID = -1028930899;

    /**
     * The reference instance of <code>college.enterprise</code>
     */
    public static final Enterprise ENTERPRISE = new Enterprise();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EnterpriseRecord> getRecordType() {
        return EnterpriseRecord.class;
    }

    /**
     * The column <code>college.enterprise.id</code>.
     */
    public final TableField<EnterpriseRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>college.enterprise.name</code>.
     */
    public final TableField<EnterpriseRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), this, "");

    /**
     * The column <code>college.enterprise.address</code>.
     */
    public final TableField<EnterpriseRecord, String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.VARCHAR.length(30).nullable(false), this, "");

    /**
     * Create a <code>college.enterprise</code> table reference
     */
    public Enterprise() {
        this("enterprise", null);
    }

    /**
     * Create an aliased <code>college.enterprise</code> table reference
     */
    public Enterprise(String alias) {
        this(alias, ENTERPRISE);
    }

    private Enterprise(String alias, Table<EnterpriseRecord> aliased) {
        this(alias, aliased, null);
    }

    private Enterprise(String alias, Table<EnterpriseRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<EnterpriseRecord> getPrimaryKey() {
        return Keys.KEY_ENTERPRISE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EnterpriseRecord>> getKeys() {
        return Arrays.<UniqueKey<EnterpriseRecord>>asList(Keys.KEY_ENTERPRISE_PRIMARY, Keys.KEY_ENTERPRISE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enterprise as(String alias) {
        return new Enterprise(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Enterprise rename(String name) {
        return new Enterprise(name, null);
    }
}
