package com.metacoding.integrationtest.user;

import com.metacoding.integrationtest._core.auth.LoginUser;
import com.metacoding.integrationtest._core.error.exception.Exception400;
import com.metacoding.integrationtest._core.error.exception.Exception401;
import com.metacoding.integrationtest._core.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse.DTO 회원가입(UserRequest.JoinDTO reqDTO) {
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) {
            throw new Exception400("유저네임 중복");
        }

        User userPS = userRepository.save(reqDTO.toEntity());
        return new UserResponse.DTO(userPS);
    }

    public LoginUser 로그인(UserRequest.@Valid LoginDTO reqDTO) {
        User user = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception401("유저네임 혹은 패스워드가 틀렸습니다"));

        if (!user.getPassword().equals(reqDTO.getPassword())) {
            throw new Exception401("유저네임 혹은 패스워드가 틀렸습니다");
        }

        String jwt = jwtUtil.create(user);
        return new LoginUser(user.getId(), user.getUsername(), jwt);
    }

}
