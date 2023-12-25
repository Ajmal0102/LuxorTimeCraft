package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.Role;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.otp.repository.OtpRepository;
import com.ajmal.TimeCraft.Repository.RoleRepository;
import com.ajmal.TimeCraft.Service.UserService;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    OtpRepository otpRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(){

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes) throws ServletException {



        String email = user.getEmail();
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if(optionalUser.isPresent()){
            User user1 = optionalUser.get();
            if(user1.isVerified()){
                redirectAttributes.addFlashAttribute("errMsg","User Already exist !");
                return "redirect:/register";
            }else {
                otpRepository.delete(otpRepository.findByUser(user1));
                userService.delete(user1);

            }

        }
        String number = user.getPhoneNumber();
        user.setPhoneNumber(number);
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());
        user.setRoles(roles);
        user.setActive(true);
        userService.save(user);
        userService.otpManagement(user);
        return "otp-page";
    }

    @PostMapping("/getOtpPage")
    public String otpVerification(@ModelAttribute(name = "otp") String otp,
                                  @ModelAttribute(name = "email") String email,
                                  Model model){

        int flag = userService.verifyAccount(email, otp);
        if(flag == 1){
            return "redirect:/";
        }else if(flag == 2){
            model.addAttribute("message","OTP time exceed!");
            model.addAttribute("user",userService.findUserByEmail(email).get());
            return "otpPage";
        }

        model.addAttribute("message","Please enter a valid OTP");
        model.addAttribute("user",userService.findUserByEmail(email).get());
        return "otpPage";




    }

    @GetMapping("/loginRedirect")
    public String loginRedirect() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }  else {
            return "redirect:/home";
        }
    }
}
