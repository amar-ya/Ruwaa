package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Media;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.MediaRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService
{
    private final MediaRepository mediaRepository;
    private final UsersRepository usersRepository;

    public List<Media> getAll(){
        List<Media> mediaList = mediaRepository.findAll();
        if (mediaList.isEmpty()){
            throw new ApiException("expert not found");
        }
        return mediaList;
    }

    public void addMedia(Integer user_id, Media media){
        Users u = usersRepository.findUserById(user_id);
        if (u == null){
            throw new ApiException("Error couldnt post");
        }
        media.setUsers(u);
        mediaRepository.save(media);
    }

    public void update(Integer id, Media media){
        Media m = mediaRepository.findMediaById(id);
        if (m == null){
            throw new ApiException("media not found");
        }
        m.setTitle(media.getTitle());
        m.setContent(media.getContent());
        mediaRepository.save(m);
    }

    public void deleteMedia(Integer id){
        Media m = mediaRepository.findMediaById(id);
        if (m == null){
            throw new ApiException("media not found");
        }
        mediaRepository.delete(m);
    }
}
