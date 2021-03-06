package ru.studmanager;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import db.generated.enums.StudentSex;
import db.generated.tables.Abroad;
import db.generated.tables.Assignment;
import db.generated.tables.Enterprise;
import db.generated.tables.GroupSt;
import db.generated.tables.Payment;
import db.generated.tables.Profession;
import db.generated.tables.Relative;
import db.generated.tables.Student;
import db.generated.tables.SubFaculty;
import db.generated.tables.VarAssignment;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static ru.studmanager.Validation.validateAddress;
import static ru.studmanager.Validation.validateBirthday;
import static ru.studmanager.Validation.validateGroupId;
import static ru.studmanager.Validation.validateMark;
import static ru.studmanager.Validation.validateName;
import static ru.studmanager.Validation.validateNationality;
import static ru.studmanager.Validation.validateSex;
import static ru.studmanager.Validation.validateSurname;

@Controller
public class DoCmd {

    @Autowired
    private DSLContext dsl;

    @RequestMapping(value = "/test")
    public ResponseEntity<String> index() {
        return new ResponseEntity<String>("lol", HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@ModelAttribute("attrList") ModelMap attrList) {
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();

        Result<Record6<Integer, String, String, Date, String, BigDecimal>> result =
                dsl.select(Student.STUDENT.ID, Student.STUDENT.NAME, Student.STUDENT.SURNAME,
                           Student.STUDENT.BIRTHDAY, GroupSt.GROUP_ST.NOMER, Student.STUDENT.MARK)
                        .from(Student.STUDENT).join(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID)).fetch();
        for (Record rec: result) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("id", rec.getValue(Student.STUDENT.ID));
            row.put("name", rec.getValue(Student.STUDENT.NAME));
            row.put("surname", rec.getValue(Student.STUDENT.SURNAME));
            row.put("birthday", rec.getValue(Student.STUDENT.BIRTHDAY).toString());
            row.put("group", rec.getValue(GroupSt.GROUP_ST.NOMER));
            row.put("mark", rec.getValue(Student.STUDENT.MARK));
            students.add(row);
        }

        attrList.addAttribute("students", students);

        return "index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("attrList") ModelMap attrList) {
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveNewStudent(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "surname", defaultValue = "") String surname,
            @RequestParam(value = "birthday", defaultValue = "") String birthday,
            @RequestParam(value = "sex", defaultValue = "") String sex,
            @RequestParam(value = "nationality", defaultValue = "") String nationality,
            @RequestParam(value = "address", defaultValue = "") String address,
            @RequestParam(value = "group_id", defaultValue = "") String group_id,
            @RequestParam(value = "mark", defaultValue = "") String mark,
            @ModelAttribute("error") ModelMap error,
            @ModelAttribute("attrList") ModelMap attrList) {

        // валидация полученных значений
        if (!validateSurname(surname)) {
            error.addAttribute("surname", "Error surname!");
        }
        if (!validateName(name)) {
            error.addAttribute("name", "Error name!");
        }
        if (!validateSex(sex)) {
            error.addAttribute("sex", "Error sex!");
        }
        if (!validateBirthday(birthday)) {
            error.addAttribute("birthday", "Error birthday!");
        }
        if (!validateNationality(nationality)) {
            error.addAttribute("nationality", "Error nationality!");
        }
        if (!validateAddress(address)) {
            error.addAttribute("address", "Error address!");
        }
        if (!validateMark(mark)) {
            error.addAttribute("mark", "Error mark!");

        }
        if (!validateGroupId(group_id)) {
            error.addAttribute("group_id", "Error group!");
        }

        if (error.isEmpty()) {
            dsl.insertInto(Student.STUDENT)
                    .set(Student.STUDENT.NAME, name)
                    .set(Student.STUDENT.SURNAME, surname)
                    .set(Student.STUDENT.BIRTHDAY, Date.valueOf(birthday))
                    .set(Student.STUDENT.SEX, StudentSex.valueOf(sex))
                    .set(Student.STUDENT.NATIONALITY, nationality)
                    .set(Student.STUDENT.ADDRESS, address)
                    .set(Student.STUDENT.GROUP_ID, Integer.valueOf(group_id))
                    .set(Student.STUDENT.MARK, BigDecimal.valueOf(Double.valueOf(mark)))
                    .execute();
            return "redirect:/";
        } else {
            attrList.addAttribute("surname", surname);
            attrList.addAttribute("name", name);
            attrList.addAttribute("sex", sex);
            attrList.addAttribute("birthday", birthday);
            attrList.addAttribute("nationality", nationality);
            attrList.addAttribute("address", address);
            attrList.addAttribute("mark", mark);
            attrList.addAttribute("group_id", group_id);
            return "add";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value="id") Integer id, @ModelAttribute("attrList") ModelMap attrList) {
        Record8<Integer, String, String, Date, StudentSex, String, String, Integer> student =
                dsl.select(Student.STUDENT.ID, Student.STUDENT.NAME, Student.STUDENT.SURNAME,
                           Student.STUDENT.BIRTHDAY, Student.STUDENT.SEX, Student.STUDENT.NATIONALITY,
                           Student.STUDENT.ADDRESS, Student.STUDENT.GROUP_ID)
                .from(Student.STUDENT).where(Student.STUDENT.ID.eq(id)).fetchOne();

        attrList.addAttribute("id", student.getValue(Student.STUDENT.ID));
        attrList.addAttribute("name", student.getValue(Student.STUDENT.NAME));
        attrList.addAttribute("surname", student.getValue(Student.STUDENT.SURNAME));
        attrList.addAttribute("birthday", student.getValue(Student.STUDENT.BIRTHDAY).toString());
        attrList.addAttribute("sex", student.getValue(Student.STUDENT.SEX));
        attrList.addAttribute("nationality", student.getValue(Student.STUDENT.NATIONALITY));
        attrList.addAttribute("address", student.getValue(Student.STUDENT.ADDRESS));
        attrList.addAttribute("group_id", student.getValue(Student.STUDENT.GROUP_ID).toString());

        return "edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveEditedStudent(
            @RequestParam(value="id") Integer id,
            @RequestParam(value="name") String name,
            @RequestParam(value="surname") String surname,
            @RequestParam(value="birthday") Date birthday,
            @RequestParam(value="sex") StudentSex sex,
            @RequestParam(value="nationality") String nationality,
            @RequestParam(value="address") String address,
            @RequestParam(value="group_id") Integer group_id) {

        // добавить валидацию

        dsl.update(Student.STUDENT)
                .set(Student.STUDENT.NAME, name)
                .set(Student.STUDENT.SURNAME, surname)
                .set(Student.STUDENT.BIRTHDAY, birthday)
                .set(Student.STUDENT.SEX, sex)
                .set(Student.STUDENT.NATIONALITY, nationality)
                .set(Student.STUDENT.ADDRESS, address)
                .set(Student.STUDENT.GROUP_ID, group_id)
                .where(Student.STUDENT.ID.eq(id))
                .execute();

        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id") Integer id, @ModelAttribute("attrList") ModelMap attrList) {
        Record4<Integer, String, String, Date> student = dsl.select(Student.STUDENT.ID, Student.STUDENT.NAME, Student.STUDENT.SURNAME, Student.STUDENT.BIRTHDAY)
                .from(Student.STUDENT).where(Student.STUDENT.ID.eq(id)).fetchOne();
        attrList.addAttribute("id", student.getValue(Student.STUDENT.ID));
        attrList.addAttribute("name", student.getValue(Student.STUDENT.NAME));
        attrList.addAttribute("surname", student.getValue(Student.STUDENT.SURNAME));
        attrList.addAttribute("birthday", student.getValue(Student.STUDENT.BIRTHDAY));

        return "delete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(value="id") Integer id, @RequestParam(value="delete") String delete) {
        if (delete.equals("Yes")) {
            dsl.delete(Student.STUDENT).where(Student.STUDENT.ID.eq(id)).execute();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String tasks() {
        return "tasks";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(@RequestParam(value="id") Integer id, @ModelAttribute("attrList") ModelMap attrList) {

        List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
        ResultSet result = null;

        Table<Record3<String, String, String>> t1 =
                dsl.select(Student.STUDENT.SURNAME, Enterprise.ENTERPRISE.NAME, GroupSt.GROUP_ST.NOMER)
                        .from(Student.STUDENT)
                        .leftJoin(VarAssignment.VAR_ASSIGNMENT).on(Student.STUDENT.ID.eq(VarAssignment.VAR_ASSIGNMENT.STUD_ID))
                        .join(Assignment.ASSIGNMENT).on(VarAssignment.VAR_ASSIGNMENT.ID.eq(Assignment.ASSIGNMENT.VAR_ASSIG_ID))
                        .leftJoin(Enterprise.ENTERPRISE).on(Enterprise.ENTERPRISE.ID.eq(VarAssignment.VAR_ASSIGNMENT.ENT_ID))
                        .leftJoin(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID))
                        .asTable();
        Table<Record2<String, BigDecimal>> t2 =
                dsl.select(GroupSt.GROUP_ST.NOMER, DSL.sum(Payment.PAYMENT.SUMMA).as("sum"))
                        .from(Student.STUDENT)
                        .leftJoin(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID))
                        .leftJoin(VarAssignment.VAR_ASSIGNMENT).on(Student.STUDENT.ID.eq(VarAssignment.VAR_ASSIGNMENT.STUD_ID))
                        .join(Assignment.ASSIGNMENT).on(VarAssignment.VAR_ASSIGNMENT.ID.eq(Assignment.ASSIGNMENT.VAR_ASSIG_ID))
                        .leftJoin(Payment.PAYMENT).on(Payment.PAYMENT.ASSIG_ID.eq(Assignment.ASSIGNMENT.ID)).groupBy(GroupSt.GROUP_ST.NOMER)
                        .asTable();

        switch (id) {
            case 1:
                result = dsl.select(Student.STUDENT.NAME, Student.STUDENT.SURNAME, Student.STUDENT.MARK)
                        .from(Student.STUDENT)
                        .fetchResultSet();
                break;
            case 2:
                result = dsl.select(Student.STUDENT.NAME, Student.STUDENT.SURNAME, GroupSt.GROUP_ST.NOMER, Student.STUDENT.MARK)
                        .from(Student.STUDENT).join(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID)).fetchResultSet();
                break;
            case 3:
                result = dsl.select(Student.STUDENT.NAME, Student.STUDENT.SURNAME,
                        Student.STUDENT.BIRTHDAY, GroupSt.GROUP_ST.NOMER, Student.STUDENT.MARK)
                        .from(Student.STUDENT).join(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID)).fetchResultSet();
                break;
            case 4:
                result = dsl.select(Student.STUDENT.NAME, Student.STUDENT.SURNAME, GroupSt.GROUP_ST.NOMER, Student.STUDENT.MARK)
                        .from(Student.STUDENT)
                        .join(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID))
                        .where(Student.STUDENT.NATIONALITY.notIn("Русский", "Русская"))
                        .fetchResultSet();
                break;
            case 5:
                result = dsl.select(DSL.count().as("students count")).from(Student.STUDENT).fetchResultSet();
                break;
            case 6:
                result = dsl.select(DSL.count().as("good students count"))
                        .from(Student.STUDENT)
                        .where(Student.STUDENT.MARK.greaterThan(BigDecimal.valueOf(4L)))
                        .fetchResultSet();
                break;
            case 7:
                result = dsl.select(DSL.count().as("good feamle students count"))
                        .from(Student.STUDENT)
                        .where(Student.STUDENT.MARK.greaterThan(BigDecimal.valueOf(4L))
                                .and(Student.STUDENT.SEX.eq(StudentSex.Female)))
                        .fetchResultSet();
                break;
            case 8:
                result = dsl.select(Student.STUDENT.MARK, DSL.count()).from(Student.STUDENT).groupBy(Student.STUDENT.MARK).fetchResultSet();
                break;
            case 9:
                result = dsl.select(SubFaculty.SUB_FACULTY.NAME, DSL.count().as("prof count"))
                        .from(Profession.PROFESSION)
                        .leftJoin(SubFaculty.SUB_FACULTY).on(Profession.PROFESSION.SUB_FAC_ID.eq(SubFaculty.SUB_FACULTY.ID))
                        .groupBy(SubFaculty.SUB_FACULTY.NAME)
                        .fetchResultSet();
                break;
            case 10:
                result = dsl.select(GroupSt.GROUP_ST.NOMER, DSL.round(DSL.avg(Student.STUDENT.MARK), 2).as("avg mark"))
                        .from(Student.STUDENT)
                        .leftJoin(GroupSt.GROUP_ST).on(Student.STUDENT.GROUP_ID.eq(GroupSt.GROUP_ST.ID))
                        .groupBy(GroupSt.GROUP_ST.NOMER)
                        .fetchResultSet();
                break;
            case 11:
                result = dsl.select(Student.STUDENT.SURNAME, Student.STUDENT.NAME, DSL.count().as("relative count"))
                        .from(Relative.RELATIVE)
                        .leftJoin(Student.STUDENT).on(Relative.RELATIVE.STUD_ID.eq(Student.STUDENT.ID))
                        .groupBy(Student.STUDENT.ID)
                        .fetchResultSet();
                break;
            case 12:
                result = dsl.select(DSL.concat(DSL.concat(Student.STUDENT.SURNAME, " "),
                                               DSL.substring(Student.STUDENT.NAME, 1, 1)).as("student"),
                                               DSL.floor(DSL.abs(DSL.timestampDiff(DSL.timestamp(Student.STUDENT.BIRTHDAY), DSL.currentTimestamp()).div(DSL.val(31536000000L)))).as("age"))
                        .from(Student.STUDENT).fetchResultSet();
                break;
            case 13:
                result = dsl.select(DSL.count())
                        .from(Student.STUDENT)
                        .where(Student.STUDENT.MARK.between(BigDecimal.valueOf(3.00), BigDecimal.valueOf(3.50))
                                .and(DSL.floor(DSL.abs(DSL.timestampDiff(DSL.timestamp(Student.STUDENT.BIRTHDAY), DSL.currentTimestamp()).div(DSL.val(31536000000L)))).greaterThan(
                                        DayToSecond.valueOf(22))))
                        .fetchResultSet();
                break;
            case 14:
                result = dsl.select(Student.STUDENT.SURNAME, Student.STUDENT.NAME)
                        .from(Relative.RELATIVE)
                        .leftJoin(Abroad.ABROAD).on(Abroad.ABROAD.REL_ID.eq(Relative.RELATIVE.ID))
                        .leftJoin(Student.STUDENT).on(Student.STUDENT.ID.eq(Relative.RELATIVE.STUD_ID))
                        .where(Abroad.ABROAD.ID.isNull()).groupBy(Student.STUDENT.ID).fetchResultSet();
                break;
            case 15:
                Table<Record1<String>> nested = dsl.select(GroupSt.GROUP_ST.NOMER)
                        .from(Relative.RELATIVE)
                        .leftJoin(Abroad.ABROAD).on(Abroad.ABROAD.REL_ID.eq(Relative.RELATIVE.ID))
                        .leftJoin(Student.STUDENT).on(Relative.RELATIVE.STUD_ID.eq(Student.STUDENT.ID))
                        .leftJoin(GroupSt.GROUP_ST).on(GroupSt.GROUP_ST.ID.eq(Student.STUDENT.GROUP_ID))
                        .where(Abroad.ABROAD.ID.isNull())
                        .groupBy(Student.STUDENT.ID).asTable();
                result = dsl.select(nested.field(0), DSL.count()).from(nested).groupBy(nested.field(0)).fetchResultSet();
                break;
            case 16:
                result = dsl.select(t1.field(0), t1.field(1), t1.field(2),DSL.ifnull(t2.field(1), 0).as("sum"))
                        .from(t1)
                        .leftJoin(t2).using(GroupSt.GROUP_ST.NOMER)
                        .fetchResultSet();
                break;
            case 17:
                result = dsl.select(GroupSt.GROUP_ST.NOMER).from(GroupSt.GROUP_ST).where(GroupSt.GROUP_ST.NOMER.notIn(dsl.select(t1.field(2))
                        .from(t1)
                        .leftJoin(t2).using(GroupSt.GROUP_ST.NOMER).asField())).fetchResultSet();
                break;
        }

        try {
            records = resultSetToList(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attrList.addAttribute("records", records);

        return "report";
    }

    private List<LinkedHashMap<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
        Map<String, Object> record = new LinkedHashMap<String, Object>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        record.clear();
        for (int i = 1; i <= columnCount; i++ ) {
            record.put(rsmd.getColumnName(i), rsmd.getColumnName(i));
        }
        records.add(new LinkedHashMap<>(record));

        while (rs.next()) {
            record.clear();
            for (int i = 1; i <= columnCount; i++ ) {
                record.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
            }
            records.add(new LinkedHashMap<>(record));
        }
        return records;
    }
}
