package com.lin.missyou.service;

import com.lin.missyou.model.Spu;
import com.lin.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private SpuRepository spuRepository;

    public Page<Spu> search(String q,Integer page,Integer count){
        Pageable paging = PageRequest.of(page,count);
        String likeQ = "%" + q + "%";
        return this.spuRepository.findByTitleLikeOrSubtitleLike(likeQ,likeQ,paging);
    }
}
