package com.gathering.crawel;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AladinCrawlerTask {
    private final CrawlerService aladinCrawlerService;

    public void crawlAndSave() {
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("0", "종합");
        categoryMap.put("55890", "소설/시/희곡");
        categoryMap.put("170", "경제경영");
        categoryMap.put("2105", "자기계발");
        categoryMap.put("2330", "인문학");
        categoryMap.put("4527", "역사");
        categoryMap.put("2094", "예술/대중문화");
        categoryMap.put("2344", "종교");
        categoryMap.put("2310", "과학");
        categoryMap.put("2095", "여행");
        categoryMap.put("2123", "건강/취미");
        categoryMap.put("2124", "가정/육아");
        categoryMap.put("2125", "요리");
        categoryMap.put("2126", "사전/기타");
        categoryMap.put("2121", "만화");
        categoryMap.put("7459", "청소년");
        categoryMap.put("987", "초등학교");
        categoryMap.put("2551", "중학교");
        categoryMap.put("4395", "고등학교");
        categoryMap.put("798", "외국어");
        categoryMap.put("1", "문학");
        categoryMap.put("1383", "달력/기타");

            // 카테고리별로 책을 30권씩 크롤링
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            String categoryId = entry.getKey();
            String categoryName = entry.getValue();
            String url = "https://www.aladin.co.kr/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=" + categoryId;
            try {
                // Jsoup으로 HTML 문서 불러오기
                Document document = Jsoup.connect(url)
                        .header("Accept-Charset", "UTF-8") // 헤더에 UTF-8 인코딩 설정
                        .get();

                // 책 제목, 가격, 이미지, 저자, 출판사 정보 추출
                Elements bookNameElements = document.select("a.bo3"); // 책 제목
                Elements bookImageElements = document.select("div.cover_area img.front_cover"); // 책 이미지
                Elements bookAuthorElements = document.select("li a[href^=/Search/wSearchResult.aspx?AuthorSearch]"); // 저자, 옮긴이
                Elements bookPublisherElements = document.select("li a[href^=/search/wsearchresult.aspx?PublisherSearch]"); // 출판사
                Elements bookDateElements = document.select("div.ss_book_list ul li"); // 출판일 (2014년 5월)

                // 리스트 저장
                List<String> bookNames = new ArrayList<>();
                List<String> bookImages = new ArrayList<>();
                List<String> bookAuthors = new ArrayList<>();
                List<String> bookPublishers = new ArrayList<>();
                List<String> bookPublishDates = new ArrayList<>();
                List<String> bookRatings = new ArrayList<>();
                // 페이지 수를 저장할 리스트
                List<Integer> bookPages = new ArrayList<>();
                // 책 소개를 저장할 리스트
                List<String> bookDescriptions = new ArrayList<>();

                for (Element nameElement : bookNameElements) {
                    bookNames.add(nameElement.text());

                    // 상세 페이지 접근
                    String detailPageUrl = nameElement.attr("href");
                    try {
                        Document detailPage = Jsoup.connect(detailPageUrl).get();

                        // 페이지 수 추출
                        Element pageElement = detailPage.selectFirst("div.Ere_prod_mconts_R div.conts_info_list1 li");
                        if (pageElement != null) {
                            // "216쪽" 텍스트 추출
                            String pageText = pageElement.text();
                            bookPages.add(Integer.parseInt(pageText));
                        } else {
                            bookPages.add(0);
                        }

                        // 평점 추출
                        Element ratingElement = detailPage.selectFirst("div.info_list a.Ere_str");
                        if (ratingElement != null) {
                            bookRatings.add(ratingElement.text());
                        } else {
                            bookRatings.add("0.0");
                        }

                        bookDescriptions.add("책소개 추후 추가 예정");
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                        bookRatings.add("N/A");
                    }
                }

                // 책 제목, 가격, 이미지, 저자, 출판사 추출
                for (Element nameElement : bookNameElements) {
                    bookNames.add(nameElement.text());
                }

                for (Element imageElement : bookImageElements) {
                    bookImages.add(imageElement.attr("src"));
                }

                for (Element authorElement : bookAuthorElements) {
                    bookAuthors.add(authorElement.text());
                }

                for (Element publisherElement : bookPublisherElements) {
                    bookPublishers.add(publisherElement.text());
                }

                // 출판일 추가: "년"과 "월"을 모두 포함하여 추출
                for (Element listItem : bookDateElements) {
                    String text = listItem.text();
                    System.out.println(text);

                    if (text.contains("년") && text.contains("월")) {
                        // "년"과 "월"의 인덱스 확인
                        int yearIndex = text.indexOf("년");
                        int monthIndex = text.indexOf("월");

                        // 날짜 형식을 추출할 정규 표현식: "YYYY년 M월" 또는 "YYYY년 MM월"
                        Pattern pattern = Pattern.compile("\\d{4}년 \\d{1,2}월");
                        Matcher matcher = pattern.matcher(text);

                        // 날짜 형식에 맞는 부분이 있는 경우 추출
                        if (matcher.find()) {
                            String publishDate = matcher.group();
                            System.out.println("추출된 날짜: " + publishDate);
                            bookPublishDates.add(publishDate);
                        } else {
                            System.out.println("날짜 형식이 없습니다: " + text);
                        }
                    }
                }

                aladinCrawlerService.saveBooksFromCrawler(categoryName, bookNames, bookImages, bookAuthors, bookPublishers, bookPublishDates, bookRatings, bookPages, bookDescriptions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
