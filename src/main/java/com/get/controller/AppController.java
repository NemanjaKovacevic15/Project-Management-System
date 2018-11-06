package com.get.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.get.exception.UserNotFoundException;
import com.get.model.UserRole;
import com.get.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import com.get.model.User;
import com.get.service.UserService;

@Controller
@SessionAttributes(value = {"roles", "pages", "page", "pageSize", "totalRecords"})
public class AppController {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UserService userService;

    @Autowired
    MessageSourceAccessor messageSource;

    /**
     * This method will provide UserProfile list to views
     */
    @ModelAttribute("roles")
    public List<UserRole> initializeProfiles(Model model) {

        return userRoleService.findAll();
    }

    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    public String listUsers(ModelMap model, @PageableDefault(size = 10) Pageable pageable) {

        List<User> users = userService.findAllUsers(pageable);

        int size = userService.totalRecord();
        int pages = (size / 10) + (size % 10 > 0 ? 1 : 0);
        model.addAttribute("totalRecords", size);
        model.addAttribute("pages", pages);

        model.addAttribute("prevPage", pageable.getPageNumber());
        model.addAttribute("nextPage", pageable.getPageNumber() + 1);
        model.addAttribute("pageSize", pageable.getPageSize());

        model.addAttribute("users", users);
        model.addAttribute("loggedinuser", getPrincipal());

        return "user/userslist";
    }

    @GetMapping("/user-search")
    public String searchUser(@RequestParam("q") String query,
            @PageableDefault(size = 10) Pageable pageable, Model model) {

        List<User> matchedUser = userService.filterUserByCriteria(query, pageable);
        model.addAttribute("users", matchedUser);

        int size = 0;
        if (query.isEmpty()) {
            size = userService.totalRecord();
        } else {
            size = matchedUser.size();
        }
        int pages = (size / 10) + (size % 10 > 0 ? 1 : 0);
        model.addAttribute("totalRecords", size);
        model.addAttribute("pages", pages);

        model.addAttribute("prevPage", pageable.getPageNumber());
        model.addAttribute("nextPage", pageable.getPageNumber() + 1);
        model.addAttribute("pageSize", pageable.getPageSize());

        return "user/userslist";

    }

    /**
     * This method will provide the medium to add a new user.
     */
    @RequestMapping(value = {"/newuser"}, method = RequestMethod.GET)
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
        return "user/registration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @PostMapping(value = {"/newuser"})
    public String saveUser(@Valid User user, BindingResult result,
            ModelMap model, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "user/registration";
        }

        userService.saveUser(user);

        model.addAttribute("success", "User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
        model.addAttribute("loggedinuser", getPrincipal());

        return "redirect:/list";
    }

    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = {"/edit-user-{username}"}, method = RequestMethod.GET)
    public String editUser(@PathVariable String username, ModelMap model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Unable to find user with user name: " + username);
        }
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "user/edit";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = {"/edit-user-{username}"}, method = RequestMethod.POST)
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result,
            ModelMap model, @PathVariable String username,
            HttpServletRequest request) {

        if (result.hasErrors()) {
            if (!result.getFieldErrors().get(0).getField().equals("username")) {
                return "user/edit";
            }
        }

        userService.updateUser(user);

        model.addAttribute("success", "User " + user.getFirstName() + " " + user.getLastName() + " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect:/list";
    }

    /**
     * This method will delete an user by it's username value.
     */
    @RequestMapping(value = {"/delete-user-{username}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return "redirect:/list";
    }

    /**
     * This method handles Access-Denied redirect.
     */
    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("loggedinuser", getPrincipal());
        return "accessDenied";
    }

    /**
     * This method handles login GET requests. If users is already logged-in and
     * tries to goto login page again, will be redirected to list page.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        if (userService.isUserAuthenticated()) {
            return "login";
        } else {
            return "redirect:/list";
        }
    }

    /**
     * This method handles logout requests. Toggle the handlers if you are
     * RememberMe functionality is useless in your app.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
