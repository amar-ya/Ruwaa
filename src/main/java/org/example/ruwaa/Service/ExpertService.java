package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService
{
    private final ExpertRepository expertRepository;

    public List<Expert> getAll(){
        List<Expert> experts = expertRepository.findAll();
        if (experts.isEmpty()){
            throw new ApiException("expert not found");
        }
        return experts;
    }

    public void update(Integer id, Users user){
        Expert e = expertRepository.findExpertById(id).orElseThrow(() -> new ApiException("expert not found"));

        Users u = e.getUsers();
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setEmail(user.getEmail());
        u.setPhone(user.getPhone());
        u.setName(user.getName());
        e.setUsers(u);
        expertRepository.save(e);
    }

    public void delete(Integer id){
        Expert e = expertRepository.findExpertById(id).orElseThrow(() -> new ApiException("expert not found"));

        expertRepository.delete(e);
    }
}
