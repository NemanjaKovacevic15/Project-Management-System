package com.get.controller;

import com.get.exception.ProjectNotFoundException;
import com.get.model.Project;
import com.get.model.Task;
import com.get.model.UserRoleType;
import com.get.service.ProjectService;
import com.get.service.TaskService;
import com.get.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/project")
@SessionAttributes("redirectUrl")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;
    
    //This method list all projects
    @RequestMapping(value = {"", "/", "/list"})
    public String list(Model model, @RequestParam(value = "q", required = false) String q) {
        List<Project> projects = null;
        q = (q != null) ? q : "";
        model.addAttribute("q", q);
        projects = projectService.findByName(q);
        model.addAttribute("projects", projects);
        return "project/listProject";
    }
    
    //This method is platform for adding projects
    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String addProjectForm(@ModelAttribute("newProject") Project project, Model model) {
        model.addAttribute("projectManagerList", userService.findUsersByRoleName("PROJECT_MANAGER"));
        return "project/addProject";
    }

    //This method is saving projects in DB
    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public String saveProject(@Valid @ModelAttribute("newProject") Project project, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectManagerList", userService.findUsersByRoleName(UserRoleType.PROJECT_MANAGER.getUserRoleType()));
            return "project/addProject";
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("flashMessage", "Project Added Successfully");
        return "redirect:/project/list";
    }
    
    //This method is editing project by his Id
    @RequestMapping(value = {"/edit/{pid}"}, method = RequestMethod.GET)
    public String editProjectForm(@PathVariable("pid") String pid, Model model) {
        Project project = null;
        try {
            project = projectService.findOne(Long.valueOf(pid));
            if (project == null) {
                return "project/notFound";
            }
            model.addAttribute("projectManagerList", userService.findUsersByRoleName(UserRoleType.PROJECT_MANAGER.getUserRoleType()));
            model.addAttribute("action", "edit");
        } catch (Exception e) {
            System.out.println(e);
            return "project/notFound";
        }
        model.addAttribute("newProject", project);
        return "project/addProject";
    }
    
    //This method is editing/saving project by his Id in DB
    @RequestMapping(value = {"/edit/{pid}"}, method = RequestMethod.POST)
    public String editProjectForm(@Valid @ModelAttribute("newProject") Project project, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectManagerList", userService.findUsersByRoleName(UserRoleType.PROJECT_MANAGER.getUserRoleType()));
            model.addAttribute("action", "edit");
            return "project/addProject";
        }

        projectService.updateProject(project);
        redirectAttributes.addFlashAttribute("flashMessage", "Project Updated Successfully");
        return "redirect:/project/list";

    }
    
    //This method is showing project details by his Id
    @RequestMapping(value = "/detail/{pid}", method = RequestMethod.GET)
    public String showProject(@PathVariable String pid, Model model, Pageable pageable) {

        try {
            Project project = projectService.findOne(Long.valueOf(pid));
            List<Task> task = taskService.findAll(pageable);
                        
            model.addAttribute("project", project);
            
            model.addAttribute("Details", task);

            return "project/detail";
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project Not Found for Id " + pid);
        }
    }
    
    //Project Exception Handler
    @ExceptionHandler(ProjectNotFoundException.class)
    public ModelAndView soso(HttpServletRequest req, ProjectNotFoundException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", exception.getMessage());
        mav.setViewName("project/notFound");
        return mav;
    }
}
