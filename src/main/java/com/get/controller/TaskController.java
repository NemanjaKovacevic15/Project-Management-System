package com.get.controller;

import com.get.model.Project;
import com.get.model.User;
import com.get.model.UserRoleType;
import com.get.model.Task;
import com.get.service.ProjectService;
import com.get.service.TaskService;
import com.get.service.UserService;
import com.get.util.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    HelperUtils helperUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    //Listing all users by their role
    @ModelAttribute("developers")
    public List<User> collectDevelopers(Model model) {
        return userService.findUsersByRoleName(UserRoleType.DEVELOPER.getUserRoleType());
    }

    //Listing all projects
    @ModelAttribute("projects")
    public List<Project> collectProjects(Model model) {
        return projectService.findAll();
    }

    //Listing all tasks
    @GetMapping(value = {"", "/list"})
    public String tasks(Model model, @PageableDefault(size = 10) Pageable pageable) {

        List<Task> tasks = null;
        int size = 0;
        tasks = taskService.findAll(pageable);
        size = taskService.totalRecord();
        model.addAttribute("tasks", tasks);

        int pages = (size / 10) + (size % 10 > 0 ? 1 : 0);
        model.addAttribute("totalRecords", size);
        model.addAttribute("pages", pages);

        model.addAttribute("prevPage", pageable.getPageNumber());
        model.addAttribute("nextPage", pageable.getPageNumber() + 1);
        model.addAttribute("pageSize", pageable.getPageSize());

        return "task/tasks";
    }

    //This method provide's us platform for filtering and search tasks
    @GetMapping("/list/search")
    public String searchTask(@RequestParam("q") String query,
            @PageableDefault(size = 10) Pageable pageable, Model model) {

        List<Task> matcherTasks = null;

        matcherTasks = taskService.filterTaskByCriteria(query, pageable);

        model.addAttribute("tasks", matcherTasks);

        int size = 0;

        if (query.isEmpty()) {

            size = taskService.totalRecord();

            int pages = (size / 10) + (size % 10 > 0 ? 1 : 0);
            model.addAttribute("totalRecords", size);
            model.addAttribute("pages", pages);

            model.addAttribute("prevPage", pageable.getPageNumber());
            model.addAttribute("nextPage", pageable.getPageNumber() + 1);
            model.addAttribute("pageSize", pageable.getPageSize());

        }
        return "task/tasks";
    }

    //Platform for adding tasks
    @GetMapping("/add")
    public String addTask(Model model, Project project) {

        Task workOrder = new Task();

        model.addAttribute("task", workOrder);
        model.addAttribute("statusTypes", helperUtils.getStatusTypes());
        return "task/add";
    }

    //This method is saving tasks in DB
    @PostMapping("/add")
    public String saveTask(@Valid @ModelAttribute("task") Task workOrder,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "task/add";
        }
        model.addAttribute("statusTypes", helperUtils.getStatusTypes());
        taskService.save(workOrder);
        return "redirect:/task/list";
    }

    //Platform for editing tasks
    @GetMapping("/edit")
    public String editTask(@RequestParam("taskId") Long id, Model model) {

        Task workOrder = taskService.findOne(id);
        model.addAttribute("statusTypes", helperUtils.getStatusTypes());
        model.addAttribute("task", workOrder);
        model.addAttribute("projectName", workOrder.getProject().getName());

        return "task/edit";

    }

    //This method is editing and saving tasks in DB
    @PostMapping("/edit")
    public String saveEditTask(@Valid @ModelAttribute("task") Task workOrder,
            BindingResult bindingResult, @RequestParam("taskId") Long taskId, Model model) {

        if (bindingResult.hasErrors()) {
            return "task/edit";
        }

        taskService.save(workOrder);

        return "redirect:/task/list";
    }

    //This method is deleting tasks
    @GetMapping("/delete")
    public String deleteTask(@RequestParam("taskId") Long id, Model model) {

        taskService.deleteById(id);

        return "redirect:/task";

    }

}
