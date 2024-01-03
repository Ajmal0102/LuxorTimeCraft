package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.AddressService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    UserService userService;

    @GetMapping("/address")
    public String viewAddress(@AuthenticationPrincipal(expression = "email") String email,
                              Model model) {

        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            List<Address> userAddressList = addressService.findAllUserAddresses(user);
            String userName = user.getFirstName();

            model.addAttribute("username", userName);
            model.addAttribute("user",user);
            model.addAttribute("userAddress",userAddressList);

            return "user-address";
        } else {

            return "redirect:/login";
        }
    }


    @GetMapping("/add-address")
    public String getAddAddress(@RequestParam(name = "editSource",required = false,defaultValue = "profile") String editSource,
                                @AuthenticationPrincipal(expression = "id") Long user_id,
                                Model model){

        Optional<User> optionalUser = userService.findById(user_id);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String username = user.getFirstName();
            model.addAttribute("username", username);
        }

        model.addAttribute("address",new Address());
        model.addAttribute("editSource",editSource);

        return "address-form";
    }

    @PostMapping("/save-address")
    public String saveAddress(@ModelAttribute("address")Address address,
                              @RequestParam("editSource") String editSource,
                              @AuthenticationPrincipal(expression = "id") Long user_id,
                              Model model){
        Optional<User> optionalUser = userService.findById(user_id);
        User user = optionalUser.get();
        addressService.addNewAddress(address,user);

        if ("profile".equals(editSource)){
            model.addAttribute("user",user);
            return "redirect:/user/address";
        } else if ("checkout".equals(editSource)) {
            model.addAttribute("user",user);
            return "redirect:/cart/checkout";
        }else {
            return "404";
        }

    }


    @GetMapping("/update/{id}")
    public String updateAddress(@PathVariable("id") Long address_id,
                                Model model) {

        Optional<Address> address = addressService.findAddressById(address_id);
        model.addAttribute("userAddress",address.orElse(null));

        return "edit-user-address";
    }


    @PostMapping("/edit")
    public String postUpdateAddress(@ModelAttribute("address")Address address,
                                    @RequestParam(value = "editSource",required = false,defaultValue = "profile") String editSource,
                                    RedirectAttributes redirectAttributes) {

        addressService.updateAddress(address);
        redirectAttributes.addFlashAttribute("message", "Address is updated");
        if ("profile".equals(editSource)) {
            return "redirect:/user/address";
        } else if ("checkout".equals(editSource)) {
            return "redirect:/cart/checkout";
        } else {
            return "404";
        }
    }

    @GetMapping("/remove/{id}")
    public String deleteAddress(@PathVariable("id") Long address_id, RedirectAttributes redirectAttributes){
        addressService.removeAddress(address_id);
        redirectAttributes.addFlashAttribute("message", "Address is deleted");
        return "redirect:/user/address";
    }

    @GetMapping("/setDefault/{id}")
    public String setDefaultAddress(@PathVariable("id") Long addressId,
                                    @AuthenticationPrincipal(expression = "id") Long user_id,
                                    Model model) {

        Optional<User> optionalUser = userService.findById(user_id);
        User user = optionalUser.get();

        addressService.setDefaultAddress(addressId, user);
        return "redirect:/user/address"; // Redirect to the address management page
    }

}
