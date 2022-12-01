package com.example.UserProduct.controller;

import com.example.UserProduct.domain.Product;
import com.example.UserProduct.domain.User;
import com.example.UserProduct.exception.ProductNotFoundException;
import com.example.UserProduct.exception.UserAlreadyFoundException;
import com.example.UserProduct.exception.UserNotFoundException;
import com.example.UserProduct.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
@RestController
@RequestMapping("/userProductapp/user")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) throws UserAlreadyFoundException{
        ResponseEntity responseEntity=null;
        try{
            user.setProductList(new ArrayList<>());
            responseEntity=new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
        }catch (UserAlreadyFoundException e){
            throw new UserAlreadyFoundException();
        }catch (Exception e){
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProductFromUser(ServletRequest request, @RequestBody Product product)throws UserNotFoundException{
        ResponseEntity responseEntity=null;
        try{
            Claims claims = (Claims)request.getAttribute("claims");
            String userId = claims.getSubject();
            responseEntity=new ResponseEntity<>(userService.addProductForUser(userId,product),HttpStatus.OK);
        }catch (UserNotFoundException e){
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<?> deleteProductFromUser(@PathVariable int productId,@RequestBody User user)throws ProductNotFoundException,UserNotFoundException{
        ResponseEntity responseEntity=null;
        try{
            responseEntity=new ResponseEntity<>(userService.deleteProductFromUser(user.getUserId(),productId),HttpStatus.OK);
        }catch (ProductNotFoundException e){
            throw new ProductNotFoundException();
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @GetMapping("/product/products")
    public ResponseEntity<?>getProductForUser(@RequestBody User user)throws UserNotFoundException{
        ResponseEntity responseEntity=null;
        try{
            responseEntity=new ResponseEntity<>(userService.getProductForUser(user.getUserId()),HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            System.out.println("e = " + e);
            e.printStackTrace();
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
