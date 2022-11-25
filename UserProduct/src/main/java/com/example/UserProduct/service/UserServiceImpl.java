package com.example.UserProduct.service;

import com.example.UserProduct.domain.Product;
import com.example.UserProduct.domain.User;
import com.example.UserProduct.exception.ProductNotFoundException;
import com.example.UserProduct.exception.UserAlreadyFoundException;
import com.example.UserProduct.exception.UserNotFoundException;
import com.example.UserProduct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;



    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public User addUser(User user) throws UserAlreadyFoundException {
        if(userRepository.findById(user.getUserId()).isPresent()) {
            throw new UserAlreadyFoundException();
        }
        return userRepository.insert(user);
    }


    @Override
    public User addProductForUser(String userId, Product product) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userRepository.findByUserId(userId);
        if(user.getProductList()==null){
            user.setProductList(Arrays.asList(product));
        }else {
            List<Product> products=user.getProductList();
            products.add(product);
            user.setProductList(products);
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteProductFromUser(String userId, int productId) throws UserNotFoundException ,ProductNotFoundException{
        boolean result=false;
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(userId).get();
        List<Product> products=user.getProductList();
        result=products.removeIf(x->x.getProductId()==productId);
        if(!result){
            throw new ProductNotFoundException();
        }
        user.setProductList(products);
        return userRepository.save(user);
    }

    @Override
    public List<Product> getProductForUser(String userId) throws UserNotFoundException {
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        return userRepository.findById(userId).get().getProductList();
    }


}
