package com.gathering.crawel;

import com.gathering.common.base.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CrawlerController {

    private final AladinCrawlerTask aladinCrawlerTask;

    @GetMapping("/crawler")
    public BaseResponse<Void> crawler() {
        aladinCrawlerTask.crawlAndSave();
        return new BaseResponse<>();
    }
}
