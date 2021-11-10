package com.generation.SpringSecurityJWT.controller;

import com.generation.SpringSecurityJWT.model.User;
import com.generation.SpringSecurityJWT.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    @DisplayName("Pruebas UserController save")
     void saveTest(){
         UserService userService = mock(UserService.class);
         BCryptPasswordEncoder bCryptPasswordEncoder =mock(BCryptPasswordEncoder.class);
         UserController userController = new UserController(userService, bCryptPasswordEncoder);
         //verificar que el userservice haya sido llamado el emtodo save
         //prepara objetos  mocks y lo que se vaya a requerir
         User user = new User();
         user.setName("Jake");
         user.setUsername("jake@hda.com");
         user.setPassword("12345");

         when(userService.save(any(User.class))).thenReturn(user);

         User response = userController.saveUser(user);

         verify(userService, times(1)).save(any(User.class));
         verify(bCryptPasswordEncoder, atLeastOnce()).encode("12345");
         assertEquals(user.getName(), response.getName());

     }

    @Test
    @DisplayName("getUser tst")
    void getUserTest(){
        UserService userService = mock(UserService.class);
        BCryptPasswordEncoder bCryptPasswordEncoder =mock(BCryptPasswordEncoder.class);
        UserController userController = new UserController(userService, bCryptPasswordEncoder);
        User user = new User();
        user.setName("Jake");
        user.setUsername("jake@hda.com");
        user.setPassword("12345");
        user.setId(1l);
      //  when(userService.getUser(user.getId()));
        User prueba = userController.getUser(1l);
      // verify(userService, times(1)).getUser(1l);

        //sale error en el assert
      assertEquals(user.getName(), prueba.getName());
        //Verificar llamar servicio
        //Verificar devolución objeto user
    }


}