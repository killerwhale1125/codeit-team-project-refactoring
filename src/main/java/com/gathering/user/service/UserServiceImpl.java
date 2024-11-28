package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.repository.UserRepository;
import jakarta.annotation.Resource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Value("${keycloak.accessToken}")
    private String tokenEndPoint;
    @Value("${keycloak.client.secret}")
    private String clientSecret;
    @Override
    public String getAccessToken(GetAccessTokenDto getAccessTokenDto){

        try{
            URL url = new URL(tokenEndPoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            String formData = "client_id=codeit4team" +
                    "&client_secret=" + clientSecret +
                    "&grant_type=password" +
                    "&username=" + getAccessTokenDto.getUserId() +
                    "&password=" + getAccessTokenDto.getPassword() +
                    "&scope=" + "openid profile email";


            // 데이터 전송
            OutputStream os = connection.getOutputStream();
            byte[] input = formData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // 응답 읽기
                StringBuilder response = new StringBuilder();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // JSON 파싱하여 access_token 추출
                JSONObject jsonResponse = new JSONObject(response.toString());
                br.close();
                return  jsonResponse.getString("access_token");
            }
            os.close();
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.AUTHORIZATION_FAIL);
        }

        return null;
    }

    @Override
    public UserDto sginIn(SignInRequestDto requestDto) {

        UserDto userDto = userRepository.selectUser(requestDto);

        if (userDto != null ) {
            userRepository.insertAttendance(userDto.getUsersId());
        }
        return userDto;
    }
}
