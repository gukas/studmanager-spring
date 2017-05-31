package ru.studmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DoCmd {

    @RequestMapping(value = "/test")
    public ResponseEntity<String> index() {
        return new ResponseEntity<String>("lol", HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@ModelAttribute("attrList") ModelMap attrList) {
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", "1");
        row.put("name", "name");
        row.put("surname", "surname");
        row.put("birthday", "1234-45-67");
        row.put("group", "g");
        row.put("mark", "5");
        students.add(row);

        attrList.addAttribute("students", students);

        return "index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@ModelAttribute("attrList") ModelMap attrList) {
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", "1");
        row.put("name", "name");
        row.put("surname", "surname");
        row.put("birthday", "1234-45-67");
        row.put("group", "g");
        row.put("mark", "5");
        students.add(row);

        attrList.addAttribute("students", students);

        return "add";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute("attrList") ModelMap attrList) {
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", "1");
        row.put("name", "name");
        row.put("surname", "surname");
        row.put("birthday", "1234-45-67");
        row.put("group", "g");
        row.put("mark", "5");
        students.add(row);

        attrList.addAttribute("students", students);

        return "edit";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String tasks() {
        return "tasks";
    }
}
