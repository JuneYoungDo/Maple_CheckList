package com.maple.checklist.global.utils;

import com.maple.checklist.domain.character.dto.request.CharacterDto;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.CharacterErrorCode;
import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapleService {

    @Value("${nexon.maple.api-key}")
    private String API_KEY;

    public CharacterDto getMapleCharacterInformation(String nickname)
        throws IOException, InterruptedException, ParseException {
        return useGetCharacterInformation(useGetOcidApi(nickname));
    }

    private String useGetOcidApi(String nickname)
        throws IOException, InterruptedException, ParseException {
        String characterName = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
        String url = "https://open.api.nexon.com/maplestory/v1/id?character_name=" + characterName;

        HttpResponse<String> response = useApi(url,"GET");
        if(response.statusCode() == 400){
            throw new BaseException(CharacterErrorCode.INVALID_NICKNAME);
        }
        JSONObject jsonObject = parseBody(response);

        return (String) jsonObject.get("ocid");
    }

    public CharacterDto useGetCharacterInformation(String ocid)
        throws IOException, InterruptedException, ParseException {
        String id = URLEncoder.encode(ocid, StandardCharsets.UTF_8);
        String url = "https://open.api.nexon.com/maplestory/v1/character/basic?ocid=" + id;

        HttpResponse<String> response = useApi(url,"GET");
        if(response.statusCode() == 400){
            throw new BaseException(CharacterErrorCode.INVALID_NICKNAME);
        }
        JSONObject jsonObject = parseBody(response);
        System.out.println((String) jsonObject.get("character_image"));

        return CharacterDto.builder()
            .nickname((String)jsonObject.get("character_name"))
            .level((Long)jsonObject.get("character_level"))
            .world((String)jsonObject.get("world_name"))
            .job((String)jsonObject.get("character_class"))
            .img((String) jsonObject.get("character_image"))
            .ocid(ocid)
            .build();
    }

    private HttpResponse<String> useApi(String url,String method)
        throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(java.net.URI.create(url))
            .header("x-nxopen-api-key", API_KEY)
            .method(method, HttpRequest.BodyPublishers.noBody())
            .build();

        return HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JSONObject parseBody(HttpResponse<String> response) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response.body());
        return (JSONObject) obj;
    }
}
