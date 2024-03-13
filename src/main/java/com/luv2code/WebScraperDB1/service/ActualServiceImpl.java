package com.luv2code.WebScraperDB1.service;

import com.luv2code.WebScraperDB1.entity.Actual;
import com.luv2code.WebScraperDB1.entity.History;
import com.luv2code.WebScraperDB1.entity.Logs;
import com.luv2code.WebScraperDB1.repository.ActualRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActualServiceImpl implements ActualService {
    @Autowired
    private ActualRepository actualRepository;

    @Autowired
    private HistoryServiceImpl historyServiceImpl;

    @Autowired
    private LogsServiceImpl logsServiceImpl;

    public ActualServiceImpl(ActualRepository actualRepository, HistoryServiceImpl historyServiceImpl, LogsServiceImpl logsServiceImpl) {
        this.actualRepository = actualRepository;
        this.historyServiceImpl = historyServiceImpl;
        this.logsServiceImpl = logsServiceImpl;
    }

    public ActualServiceImpl(){

    }
    @Override
    public List<Actual> getTableFromWebsite(String website, String classToSearch, LocalDateTime publishedDate, long revisionNumber) {


        List<Actual> storeScraped = new ArrayList<>();

        // char specialCharacterE =203;
//        char specialCharactere=235;
//        char specialCharacterC=231;

        try {

            Document doc = Jsoup.connect(website).get();
            Element table = doc.getElementsByClass(classToSearch).first();

            if (table != null) {
                Element tbody = table.selectFirst("tbody");

                if (tbody != null) {
                    Element thead_class = table.getElementsByClass("thead-light").first();
                    Element title_row = thead_class.select("tr").first();
                    Elements column_titles = title_row.select("th");


                    // Process the rows (tr elements) in the tbody
                    Elements rows = tbody.select("tr");

                    int j = 0;

                    for (Element row : rows) {
                        // Process the cells (td elements) in each row

                        int i = 0;
                        Elements cells = row.select("td");

                        Actual objectRow = new Actual();

                        for (Element cell : cells) {
                            i = i + 1;


                            if (i == 1) {
                                //storeScraped.get(i).setCurrency(cell.text());
                                objectRow.setCurrency(cell.text());
                            } else if (i == 2) {
                                //storeScraped.get(i).setCurrencyAcronym(cell.text());
                                objectRow.setCurrencyAcronym(cell.text());
                            } else if (i == 3) {
                                //storeScraped.get(i).setExchangeRate(Double.parseDouble(cell.text()));
                                objectRow.setExchangeRate(Double.parseDouble(cell.text()));
                            } else if (i == 4) {

                                if (j == 11) {
                                    String str = cell.text();
                                    char charToRemove = ',';
                                    StringBuilder stringBuilder = new StringBuilder(str);
                                    for (int a = 0; a < stringBuilder.length(); a++) {
                                        if (stringBuilder.charAt(a) == charToRemove) {
                                            stringBuilder.deleteCharAt(a);
                                            a--; // Decrement i to account for removed character
                                        }
                                    }
                                    String result = stringBuilder.toString();
                                    System.out.println("deleted sucessfully , from: " + result);
                                    //storeScraped.get(i).setDifference(Double.parseDouble(result));
                                    objectRow.setDifference(Double.parseDouble(result));
                                } else {

                                    //storeScraped.get(i).setDifference(Double.parseDouble(cell.text()));
                                    objectRow.setDifference(Double.parseDouble(cell.text()));
                                }

                            }

                        }
                        objectRow.setPublishedDate(publishedDate);
                        objectRow.setRevisionNo(revisionNumber);
                        storeScraped.add(objectRow);
                        j++;

                    }


                    return storeScraped;

                }
            }


        } catch (IOException ex) {
            System.out.println(ex);
        }

        return null;
    }

    @Override
    public LocalDateTime getWebsiteDateTime() {

        String url = "https://www.bankofalbania.org/Tregjet/Kursi_zyrtar_i_kembimit/";
        String updateDateAndTime = "";
        String spanClass = "text-primary font-size-table";

        try {
            Document doc = Jsoup.connect(url).get();

            Element getSpan = doc.getElementsByClass(spanClass).first();
            Element getDate = getSpan.select("b").first();

            Element getEm = getSpan.select("em").first();
            Element getTime = getEm.select("b").first();
            updateDateAndTime = getDate.text() + getTime.text();
            System.out.println("The website date and time is: " + updateDateAndTime);

            return formatDateTime(updateDateAndTime, "dd.MM.yyyyHH:mm:ss");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LocalDateTime formatDateTime(String timeString, String formatFrom) {
        DateTimeFormatter formatterFrom = DateTimeFormatter.ofPattern(formatFrom);
        LocalDateTime localDateTime = LocalDateTime.parse(timeString, formatterFrom);
        return localDateTime;
    }

    @Override
    public boolean checkTimeBetweenExRates(LocalDateTime existingPublishedDate, LocalDateTime publishedDate) {
        //duhet te mari si parameter vetem daten jo tabelen

        return existingPublishedDate.isBefore(publishedDate);
    }

    @Override
    public void addDataToActual(List<Actual> table) {
        for (Actual actual : table) {
            actualRepository.save(actual);
        }
    }

    @Override
    public void deleteAllFromActual() {
        actualRepository.deleteAll();
    }


    @Override
    public List<Actual> getAllActual() {
        System.out.println("entered getAllActual");
        return actualRepository.findAll();
    }

    @Override
    //@Scheduled(cron = "0 */30 * * * *")
    //@Scheduled(fixedDelay = 6 * 1000)
    @Scheduled(cron = "0 * * * * *")
    public void scheduled() {
        //initialize revisionNo with 0
        long existingRevisionNo = 0;
        //initialize local date with a date 1 year older
        LocalDateTime existingPublishedDate = LocalDateTime.now().minusYears(1);

        boolean flag = false;
        String website = "https://www.bankofalbania.org/Tregjet/Kursi_zyrtar_i_kembimit/";
        String classToSearch = "table table-sm table-responsive w-100 d-block d-md-table table-bordered m-0";
        // GET website date

        LocalDateTime publishedDate = getWebsiteDateTime();

        Logs logCron = new Logs("success","DEBUG","Accessing the time from the website");
        logsServiceImpl.addLogsToDB(logCron);

        // Read actual table
        List<Actual> storeExistingTable = getAllActual();
        logCron = new Logs("success", "DEBUG", "Reading the existing actual table");
        logsServiceImpl.addLogsToDB(logCron);

       // List<History> existingTableToHistory = new ArrayList<>();

        //Read existing revision and date from table.
        if (storeExistingTable != null && !storeExistingTable.isEmpty()) {

            System.out.println("inside if geting values");
            existingRevisionNo = storeExistingTable.get(0).getRevisionNo();
            existingPublishedDate = storeExistingTable.get(0).getPublishedDate();

            logCron = new Logs("success","DEBUG","The existing table is populated");
            logsServiceImpl.addLogsToDB(logCron);

        }else{
            logCron = new Logs("failed","DEBUG","The existing table is empty");
            logsServiceImpl.addLogsToDB(logCron);
        }
        System.out.println("data1: " +existingPublishedDate + "Data new: "+publishedDate);

        if ( checkTimeBetweenExRates(existingPublishedDate, publishedDate) ) {
            System.out.println("Passed the date control");

            logCron = new Logs("success", "DEBUG", "It is time to refresh the actual table");
            logsServiceImpl.addLogsToDB(logCron);

               // First move existing data to history
                  historyServiceImpl.addDataToHistory(storeExistingTable);
                  logCron  = new Logs("success", "DEBUG", "Writing table to history");
                  logsServiceImpl.addLogsToDB(logCron);


               //Delete from actual after moving to history
               if (existingRevisionNo != 0) {
                   logCron = new Logs("success", "DEBUG", "Deleting existing table from actual");
                    logsServiceImpl.addLogsToDB(logCron);
                   deleteAllFromActual();

               }
               //Get fresh data from website
                System.out.println("About to read new data");
                logCron  = new Logs("success", "DEBUG", "Reading new table");
                logsServiceImpl.addLogsToDB(logCron);
                List<Actual> storeNewTable = getTableFromWebsite(website, classToSearch, publishedDate, ++existingRevisionNo);
                // Write new table to DB
                logCron = new Logs("success", "DEBUG", "Writing new table to Actual");
                logsServiceImpl.addLogsToDB(logCron);
                addDataToActual(storeNewTable);

//
            }
//
    }


}

//public void cron
// 1.Lexo tabelen ekzistuese dhe gjej daten e tabeles dhe revision number
//2.1 Nese tabela eshte bosh data merr vleren 1 jan 2023
// 2.2 Therrit metoden checkTimeBetweenExRates dhe jepi si parameter daten e gjetur tek pika 1
// 3. Nese 2 kthen true atehere:
//     3.1 NEse revision number eshte >0 atehere therrit metoden kalo ne historik
//     3.2 Incremento revision number me 1
//     3.3 therrit getTableFromWebsite dhe jepi si parameter revision number (te inkrementuar)
//     3.4 therrit metoden shkruaj ne DB duke i dhene si parameter tabelen qe ktheu pika 3.3
// 4. Therrit metoden shkruaj ne log dhe jepi rezultatin sukses apo failure bashke me mesazh
