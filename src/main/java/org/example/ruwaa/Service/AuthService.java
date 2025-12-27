package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.DTOs.AuthRequest;
import org.example.ruwaa.DTOs.AuthResponse;
import org.example.ruwaa.DTOs.RegisterCustomerRequest;
import org.example.ruwaa.DTOs.RegisterExpertRequest;
import org.example.ruwaa.Config.JWT.JwtUtil;
import org.example.ruwaa.Model.Customer;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.CustomerRepository;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.UsersRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService  {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;

    public AuthResponse login(AuthRequest auth){
        Users u = usersRepository.findUserByUsername(auth.getUsername()).orElseThrow(() -> new ApiException("wrong username"));

        if (!passwordEncoder.matches(auth.getPassword(), u.getPassword())){
            throw new ApiException("wrong password");
        }

        String token = jwtUtil.generateToken(u);

        return new AuthResponse(token, u.getUsername(), u.getRole());
    }

    public AuthResponse expertSignUp(RegisterExpertRequest auth){
        if (!isUnique(auth.getUsername(), auth.getEmail() )){
            throw new ApiException("username or email already exists");
        }

        Users u = new Users();
        u.setUsername(auth.getUsername());
        u.setEmail(auth.getEmail());
        u.setName(auth.getName());
        u.setPassword(passwordEncoder.encode(auth.getPassword()));
        u.setCreatedAt(LocalDateTime.now());
        u.setRole("EXPERT");
        Expert e = new Expert();
        e.setLinkedin_url(auth.getLinkedin_url());
        e.setIsActive(false);
        e.setCategory(auth.getCategory());
        e.setUsers(u);
        expertRepository.save(e);

        return new AuthResponse(jwtUtil.generateToken(u), u.getUsername(), u.getRole());
    }


    public AuthResponse registerCustomer(RegisterCustomerRequest auth){
        System.out.println("service:"+auth);
        if (!isUnique(auth.getUsername(), auth.getEmail() )){
            throw new ApiException("username or email already exists");
        }

        Users u = new Users();
        u.setUsername(auth.getUsername());
        u.setEmail(auth.getEmail());
        u.setName(auth.getName());
        u.setPassword(passwordEncoder.encode(auth.getPassword()));
        u.setCreatedAt(LocalDateTime.now());
        u.setRole("CUSTOMER");
        u.setPhone(auth.getPhone_number());

        Customer c = new Customer();
        c.setUsers(u);
        customerRepository.save(c);
        return new AuthResponse(jwtUtil.generateToken(u), u.getUsername(), u.getRole());
    }



    public boolean isUnique(String username, String email){
        if (usersRepository.findUserByUsername(username).isPresent() || usersRepository.findUserByEmail(email).isPresent()){
            return false;
        }
        return true;
    }
}
