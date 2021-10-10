package com.lin.missyou.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.dto.OrderAddressDTO;
import com.lin.missyou.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericAndJson {

    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper){
        GenericAndJson.mapper = mapper;
    }

    public static <T> String objectToJson(T o){
        try {
            return GenericAndJson.mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(999);
        }
    }

    public static <T> T jsonToObject(String s, TypeReference<OrderAddressDTO> classT){
        if (s == null){
            return null;
        }
        try {
            T o = (T) GenericAndJson.mapper.readValue(s, classT);
            return o;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(999);
        }
    }

    public static <T> T jsonToList(String s, TypeReference<T> tr){
        if (s == null){
            return null;
        }
        try {
            T list = GenericAndJson.mapper.readValue(s, tr);
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(999);
        }
    }

//    public static <T> List<T> jsonToList(String s){
//        if (s == null){
//            return null;
//        }
//        try {
//            List<T> list = GenericAndJson.mapper.readValue(s, new TypeReference<List<T>>() {
//            });
//            return list;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new ServerErrorException(999);
//        }
//    }
}
