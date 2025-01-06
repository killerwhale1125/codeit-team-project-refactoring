package com.gathering.crawel;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Yes24CrawlerTask {
    private final CrawlerService aladinCrawlerService;
    public void crawlAndSave() {
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("001", "국내도서");
        categoryMap.put("001001001", "가정 살림");
        categoryMap.put("001001011", "건강 취미");
        categoryMap.put("001001025", "경제 경영");
        categoryMap.put("001001004", "국어 외국어 사전");
        categoryMap.put("001001014", "대학교재");
        categoryMap.put("001001008", "만화/라이트노벨");
        categoryMap.put("001001022", "사회 정치");
        categoryMap.put("001001046", "소설/시/희곡");
        categoryMap.put("001001015", "수험서 자격증");
        categoryMap.put("001001016", "어린이");
        categoryMap.put("001001047", "에세이");
        categoryMap.put("001001009", "여행");
        categoryMap.put("001001010", "역사");
        categoryMap.put("001001007", "예술");
        categoryMap.put("001001027", "유아");
        categoryMap.put("001001019", "인문");
        categoryMap.put("001001020", "인물");
        categoryMap.put("001001026", "자기계발");
        categoryMap.put("001001002", "자연과학");
        categoryMap.put("001001024", "잡지");
        categoryMap.put("001001023", "전집");
        categoryMap.put("001001021", "종교");
        categoryMap.put("001001005", "청소년");
        categoryMap.put("001001003", "IT 모바일");

        // 카테고리별로 책을 30권씩 크롤링
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            String categoryId = entry.getKey();
            String categoryName = entry.getValue();
            String url = "https://www.yes24.com/Product/Category/BestSeller?CategoryNumber=" + categoryId;
            try {
                // Jsoup으로 HTML 문서 불러오기
                Document document = Jsoup.connect(url)
                        .header("Accept-Charset", "UTF-8") // 헤더에 UTF-8 인코딩 설정
                        .get();

                // 책 제목, 가격, 이미지, 저자, 출판사 정보 추출
                Elements bookNameElements = document.select("a.gd_name"); // 책 제목
                Elements bookImageElements = document.select("img.lazy"); // 책 이미지
                Elements bookAuthorElements = document.select("span.authPub.info_auth a"); // 저자, 옮긴이
                Elements bookPublisherElements = document.select("span.authPub.info_pub a"); // 출판사
                Elements bookDateElements = document.select("span.authPub.info_date"); // 출판일 (2014년 5월)

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
                        Document detailPage = Jsoup.connect("https://www.yes24.com" + detailPageUrl).get();

                        /**
                         * 페이지 추출
                         */
                        Elements sizeWeightElements = detailPage.select("table.tb_nor tbody.b_size td.lastCol");
                        // 해당 데이터를 처리하여 페이지 수 추출
                        for (Element row : sizeWeightElements) {
                            String text = row.text();

                            // "쪽"이라는 문자를 기준으로 숫자 추출
                            if (text.contains("쪽")) {
                                String pageNumber = text.split("쪽")[0].trim();
                                pageNumber = pageNumber.replaceAll(",", "");
                                if(!StringUtils.hasText(pageNumber)) {
                                    bookPages.add(0);
                                } else {
                                    bookPages.add(Integer.parseInt(pageNumber));
                                }

                            }
                        }


                        /**
                         * 평점 추출
                         */
                        Element ratingElement = detailPage.selectFirst("span.gd_ratingArea em.yes_b");
                        if (ratingElement != null) {
                            bookRatings.add(ratingElement.text());
                        } else {
                            bookRatings.add("0.0");
                        }

                        StringBuilder sb = new StringBuilder();
                        // ChromeDriver 경로 설정 (ChromeDriver가 설치된 경로로 수정)
                        System.setProperty("webdriver.chrome.driver", "D:\\source\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--headless"); // 헤드리스 모드
                        options.addArguments("--disable-gpu"); // GPU 가속 비활성화

                        // WebDriver 객체 생성
                        WebDriver driver = new ChromeDriver(options);

                        try {
                            // 페이지 로드
                            driver.get("https://www.yes24.com" + detailPageUrl);

                            // WebDriverWait 설정 (최대 10초 대기)
                            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                            // <textarea> 내부의 값 추출
                            try {
                                List<WebElement> textareaElements = driver.findElements(By.cssSelector("textarea.txtContentText"));
                                if (!textareaElements.isEmpty()) {
                                    String textareaContent = textareaElements.get(0).getAttribute("value");

                                    // Jsoup을 사용하여 HTML 태그 제거
                                    String cleanedContent = cleanHtml(textareaContent);

                                    sb.append(cleanedContent);
                                } else {
                                    System.out.println("<textarea> 태그를 찾을 수 없습니다.");
                                }
                            } catch (NoSuchElementException e) {
                                System.out.println("<textarea> 태그를 찾을 수 없습니다.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // WebDriver 종료
                            if (driver != null) {
                                driver.quit();
                            }
                        }

                        bookDescriptions.add(sb.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 책 제목, 가격, 이미지, 저자, 출판사 추출
                for (Element nameElement : bookNameElements) {
                    bookNames.add(nameElement.text());
                }

                // data-original 속성에서 이미지 링크 추출
                for (Element imgElement : bookImageElements) {
                    String imageUrl = imgElement.attr("data-original");
                    bookImages.add(imageUrl);
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
                        // 날짜 형식을 추출할 정규 표현식: "YYYY년 M월" 또는 "YYYY년 MM월"
                        Pattern pattern = Pattern.compile("\\d{4}년 \\d{1,2}월");
                        Matcher matcher = pattern.matcher(text);

                        // 날짜 형식에 맞는 부분이 있는 경우 추출
                        if (matcher.find()) {
                            String publishDate = matcher.group();
                            bookPublishDates.add(publishDate);
                        }
                    }
                }

                aladinCrawlerService.saveBooksFromCrawler(categoryName, bookNames, bookImages, bookAuthors, bookPublishers, bookPublishDates, bookRatings, bookPages, bookDescriptions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // HTML 태그 제거 메서드
    public static String removeHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " ").trim();
    }

    // Jsoup을 사용한 HTML 태그 제거 메서드
    public static String cleanHtml(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text(); // HTML 태그를 제거하고 텍스트만 반환
    }
}

