package com.get.controller;

import com.get.model.Project;
import com.get.model.Task;
import com.get.model.User;
import com.get.model.UserRoleType;
import com.get.service.ProjectService;
import com.get.service.TaskService;
import com.get.service.UserService;
import com.get.util.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/dev")
public class DeveloperController {

    @Autowired
    private TaskService taskService;

    @Autowired
    HelperUtils helperUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    //Listing all tasks
    @ModelAttribute("tasks")
    public List<Task> collectMyTasks() {
        return taskService.getMyTasks();
    }

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

    /*This method alaow logged in user that is assigned to task to view tasks and make changes.
     */
    @PreAuthorize("#task.id == principal.id")
    @GetMapping(value = {"", "/list/"})
    public String tasks(Task task) {
        return "dev/tasks";
    }

    /*This method provides a platform for editing task by Id*/
    @GetMapping("/edit")
    public String editTask(@RequestParam("taskId") Long id, Model model) {

        Task workOrder = taskService.findOne(id);
        model.addAttribute("statusTypes", helperUtils.getStatusTypes());
        model.addAttribute("task", workOrder);
        model.addAttribute("projectName", workOrder.getProject().getName());

        return "dev/edit";

    }

    /*This method is editing and saving in DB task by Id*/
    @PostMapping("/edit")
    public String saveEditTask(@Valid @ModelAttribute("task") Task workOrder,
            BindingResult bindingResult, @RequestParam("taskId") Long taskId, Model model) {

        if (bindingResult.hasErrors()) {
            return "dev/edit";
        }

        taskService.save(workOrder);

        return "redirect:/dev/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long taskId, Model model) {
        model.addAttribute("task", taskService.findOne(taskId));
        return "dev/detail";
    }

    /*This method is checking task status */
    @GetMapping("/status")
    public String changeStatus(@RequestParam("status") String status,
            @RequestParam("id") Long id, Model model) {

        //check if status is the right type
        Task workOrder = taskService.findOne(id);
        
        workOrder.setStatus(status);

        taskService.save(workOrder);

        return "redirect:/dev/detail?id=" + id;

    }

}
