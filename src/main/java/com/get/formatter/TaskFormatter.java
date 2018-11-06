package com.get.formatter;

import com.get.model.Task;
import com.get.service.TaskService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class TaskFormatter implements Formatter<Task> {

    @Autowired
    private TaskService taskService;

    @Override
    public Task parse(String text, Locale locale) throws ParseException {
        if (text != null && !text.isEmpty()) {
            Task workOrder = taskService.findOne(Long.parseLong(text));
            return workOrder;
        }
        return null;
    }

    @Override
    public String print(Task object, Locale locale) {
        if (object != null && object.getId() != null) {
            return String.valueOf(object.getId());
        }
        return null;
    }
}
