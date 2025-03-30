//package com.example.modulus.Class;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.jsoup.*;
//import org.jsoup.nodes.*;
//import org.jsoup.select.*;
//
//public class webScrape {
//
//    public static void main(String[] args) {
//        webScrape scraper = new webScrape();
//        List<String> websites = scraper.scrapeAllWebsites();
//        for (String website : websites) {
//            CourseInfo courseInfo = scraper.courseInfo(website);
//            System.out.println(courseInfo.getName());
//            System.out.println(courseInfo.getDescription());
//            System.out.println(courseInfo.getPrerequisites());
//            System.out.println(courseInfo.getTerms());
//        }
//
//    }
//
//    // Scrapes all websites
//    public List<String> scrapeAllWebsites() {
//        List<String> allWebsites = new ArrayList<>();
//        boolean hasCourses = true;
//        int pageNum = 1;
//
//        while (hasCourses) {
//            String url = String.format("https://www.sutd.edu.sg/epd/education/undergraduate/courses/?paged=%d#general-listing", pageNum); //put website here
//            List<String> courses = scrapeCourseWebsites(url);
//
//            allWebsites.addAll(courses);
//
//            pageNum++;
//            if (courses.isEmpty()) {
//                hasCourses = false;
//            }
//        }
//
//        return allWebsites;
//    }
//
//    // Scrapes the course websites from a given URL
//    public List<String> scrapeCourseWebsites(String url) {
//        List<String> courses = new ArrayList<>();
//        // Fetch the page
//        try {
//            Document doc = Jsoup.connect(url).get();
//
//            // Select course links
//            Elements courseLinks = doc.select("div.block.relative.bg-global-surface-secondary-light > a");
//
//            for (Element link : courseLinks) {
//                String courseUrl = link.attr("href");
//                courses.add(courseUrl);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return courses;
//    }
//
//    // Extract course info from the course page
//    public CourseInfo courseInfo(String url) {
//        CourseInfo courseInfo = new CourseInfo();
//
//        try {
//            // Fetch the page
//            Document doc = Jsoup.connect(url).get();
//
//            // Course Name
//            String name = doc.title().replace(" - Singapore University of Technology and Design (SUTD)", "");
//            courseInfo.setName(name);
//
//            // Course Description
//            String description = getCourseDescription(doc);
//            courseInfo.setDescription(description);
//
//            // Prerequisites
//            List<String> prereqs = getPrerequisites(doc);
//            courseInfo.setPrerequisites(prereqs);
//
//            // Terms Offered
//            List<String> terms = getTermsOffered(doc);
//            courseInfo.setTerms(terms);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return courseInfo;
//    }
//
//    private String getCourseDescription(Document doc) {
//        String description = "";
//        Elements selectors = doc.select("div.xxsmall\\:col-span-4.small\\:col-span-8.xxsmall\\:order-2.medium\\:order-1 span.richText p, div.acf-innerblocks-container");
//
//        for (Element element : selectors) {
//            description += element.text().trim();
//        }
//
//        if (description.isEmpty()){
//            description += "No Descr";
//        }
//
//        return description;
//    }
//
//    private List<String> getPrerequisites(Document doc) {
//        List<String> prereqs = new ArrayList<>();
//        Elements h5Elements = doc.select("h5");
//
//        for (Element h5 : h5Elements) {
//            if (h5.text().contains("equisite")) {
//                Element nextElement = h5.parent().nextElementSibling();
//
//                if (nextElement == null){
//                    nextElement = h5.nextElementSibling();
//                }
//                if (nextElement != null && nextElement.tagName().equals("p") && nextElement.text().contains("N.A.")) { //for that one mofo who put NA
//                    prereqs.add("N.A.");
//                }
//                else if (nextElement != null) {
//                    Elements listItems = nextElement.select("li");
//                    for (Element li : listItems) {
//                        prereqs.add(li.text().trim());
//                    }
//                }
//            }
//
//        }
//
//        if (prereqs.isEmpty() || prereqs.contains("N.A.")) {
//            prereqs.add("No prerequisites");
//        }
//
//        return prereqs;
//    }
//
//    private List<String> getTermsOffered(Document doc) {
//        List<String> terms = new ArrayList<>();
//        Elements tagsSection = doc.select("div.flex.flex-row.flex-wrap a > div");
//
//        for (Element tag : tagsSection) {
//            String txt = tag.text().trim();
//            if (txt.contains("Term")) {
//                terms.add(txt);
//            }
//        }
//
//        if (terms.isEmpty()){
//            terms.add("No term tag");
//        }
//
//        return terms;
//    }
//
//    // Store course info
//    public class CourseInfo {
//        private String name;
//        private String description;
//        private List<String> prerequisites;
//        private List<String> terms;
//
//        public String getName() { return name; }
//        public void setName(String name) { this.name = name; }
//
//        public String getDescription() { return description; }
//        public void setDescription(String description) { this.description = description; }
//
//        public List<String> getPrerequisites() { return prerequisites; }
//        public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
//
//        public List<String> getTerms() { return terms; }
//        public void setTerms(List<String> terms) { this.terms = terms; }
//    }
//}