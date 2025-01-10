package com.metacoding.integrationtest.post;

import com.metacoding.integrationtest._core.auth.LoginUser;
import com.metacoding.integrationtest._core.error.exception.Exception401;
import com.metacoding.integrationtest._core.error.exception.Exception404;
import com.metacoding.integrationtest._core.error.exception.Exception500;
import com.metacoding.integrationtest.user.User;
import com.metacoding.integrationtest.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse.DTO 게시글쓰기(PostRequest.SaveDTO reqDTO, LoginUser loginUser){
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        Post post = postRepository.save(reqDTO.toEntity(user));
        return new PostResponse.DTO(post);
    }

    public List<PostResponse.ListDTO> 게시글목록보기(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Post> posts = postRepository.findAll(sort);

        List<Integer> ids = posts.stream()
                .map(post -> post.getUser().getId())
                .distinct()
                .toList();

        List<User> users = userRepository.findByIds(ids);

        return posts.stream()
                .map(post -> {
                    for(User user : users){
                        if(user.getId() == post.getUser().getId()){
                            return new PostResponse.ListDTO(post, user);
                        }
                    }
                    throw new Exception500("작성자가 존재하지 않는 글이 있어요 : "+post.getId());
                }).toList();
    }

    public PostResponse.DTO 게시글상세보기(int id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 id를 찾을 수 없습니다 : "+id));
        return new PostResponse.DTO(post);
    }
}
