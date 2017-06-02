/*
 * This file is generated by jOOQ.
*/
package db.generated.tables.records;


import db.generated.tables.Abroad;

import java.sql.Date;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


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
public class AbroadRecord extends UpdatableRecordImpl<AbroadRecord> implements Record4<Integer, Date, String, Integer> {

    private static final long serialVersionUID = 1155439379;

    /**
     * Setter for <code>college.abroad.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>college.abroad.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>college.abroad.data</code>.
     */
    public void setData(Date value) {
        set(1, value);
    }

    /**
     * Getter for <code>college.abroad.data</code>.
     */
    public Date getData() {
        return (Date) get(1);
    }

    /**
     * Setter for <code>college.abroad.country</code>.
     */
    public void setCountry(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>college.abroad.country</code>.
     */
    public String getCountry() {
        return (String) get(2);
    }

    /**
     * Setter for <code>college.abroad.rel_id</code>.
     */
    public void setRelId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>college.abroad.rel_id</code>.
     */
    public Integer getRelId() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, Date, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, Date, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Abroad.ABROAD.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field2() {
        return Abroad.ABROAD.DATA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Abroad.ABROAD.COUNTRY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return Abroad.ABROAD.REL_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value2() {
        return getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getCountry();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getRelId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbroadRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbroadRecord value2(Date value) {
        setData(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbroadRecord value3(String value) {
        setCountry(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbroadRecord value4(Integer value) {
        setRelId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbroadRecord values(Integer value1, Date value2, String value3, Integer value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AbroadRecord
     */
    public AbroadRecord() {
        super(Abroad.ABROAD);
    }

    /**
     * Create a detached, initialised AbroadRecord
     */
    public AbroadRecord(Integer id, Date data, String country, Integer relId) {
        super(Abroad.ABROAD);

        set(0, id);
        set(1, data);
        set(2, country);
        set(3, relId);
    }
}
